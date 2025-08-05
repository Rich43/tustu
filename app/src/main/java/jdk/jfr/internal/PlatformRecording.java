package jdk.jfr.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.function.Supplier;
import jdk.jfr.Configuration;
import jdk.jfr.FlightRecorderListener;
import jdk.jfr.Recording;
import jdk.jfr.RecordingState;
import jdk.jfr.internal.SecuritySupport;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/PlatformRecording.class */
public final class PlatformRecording implements AutoCloseable {
    private final PlatformRecorder recorder;
    private final long id;
    private Duration duration;
    private Duration maxAge;
    private long maxSize;
    private WriteableUserPath destination;
    private String name;
    private boolean dumpOnExit;
    private Instant stopTime;
    private Instant startTime;
    private long size;
    private volatile Recording recording;
    private TimerTask stopTask;
    private TimerTask startTask;
    private Map<String, String> settings = new LinkedHashMap();
    private boolean toDisk = true;
    private SecuritySupport.SafePath dumpOnExitDirectory = new SecuritySupport.SafePath(".");
    private RecordingState state = RecordingState.NEW;
    private final LinkedList<RepositoryChunk> chunks = new LinkedList<>();
    private boolean shuoldWriteActiveRecordingEvent = true;
    private AccessControlContext noDestinationDumpOnExitAccessControlContext = AccessController.getContext();

    PlatformRecording(PlatformRecorder platformRecorder, long j2) {
        this.id = j2;
        this.recorder = platformRecorder;
        this.name = String.valueOf(j2);
    }

    public void start() {
        RecordingState state;
        RecordingState state2;
        synchronized (this.recorder) {
            state = getState();
            if (!Utils.isBefore(this.state, RecordingState.RUNNING)) {
                throw new IllegalStateException("Recording can only be started once.");
            }
            if (this.startTask != null) {
                this.startTask.cancel();
                this.startTask = null;
                this.startTime = null;
            }
            this.recorder.start(this);
            Logger.log(LogTag.JFR, LogLevel.INFO, (Supplier<String>) () -> {
                StringJoiner stringJoiner = new StringJoiner(", ");
                if (!this.toDisk) {
                    stringJoiner.add("disk=false");
                }
                if (this.maxAge != null) {
                    stringJoiner.add("maxage=" + Utils.formatTimespan(this.maxAge, ""));
                }
                if (this.maxSize != 0) {
                    stringJoiner.add("maxsize=" + Utils.formatBytesCompact(this.maxSize));
                }
                if (this.dumpOnExit) {
                    stringJoiner.add("dumponexit=true");
                }
                if (this.duration != null) {
                    stringJoiner.add("duration=" + Utils.formatTimespan(this.duration, ""));
                }
                if (this.destination != null) {
                    stringJoiner.add("filename=" + this.destination.getRealPathText());
                }
                String string = stringJoiner.toString();
                if (string.length() != 0) {
                    string = VectorFormat.DEFAULT_PREFIX + string + "}";
                }
                return "Started recording \"" + getName() + "\" (" + getId() + ") " + string;
            });
            state2 = getState();
        }
        notifyIfStateChanged(state, state2);
    }

    public boolean stop(String str) {
        RecordingState state;
        RecordingState state2;
        synchronized (this.recorder) {
            state = getState();
            if (this.stopTask != null) {
                this.stopTask.cancel();
                this.stopTask = null;
            }
            this.recorder.stop(this);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Stopped recording \"" + getName() + "\" (" + getId() + ")" + (str == null ? "" : ". Reason \"" + str + "\"."));
            this.stopTime = Instant.now();
            state2 = getState();
        }
        WriteableUserPath destination = getDestination();
        if (destination != null) {
            try {
                dumpStopped(destination);
                Logger.log(LogTag.JFR, LogLevel.INFO, "Wrote recording \"" + getName() + "\" (" + getId() + ") to " + destination.getRealPathText());
                notifyIfStateChanged(state2, state);
                close();
                return true;
            } catch (IOException e2) {
                return true;
            }
        }
        notifyIfStateChanged(state2, state);
        return true;
    }

    public void scheduleStart(Duration duration) {
        synchronized (this.recorder) {
            ensureOkForSchedule();
            this.startTime = Instant.now().plus((TemporalAmount) duration);
            LocalDateTime localDateTimePlus = LocalDateTime.now().plus((TemporalAmount) duration);
            setState(RecordingState.DELAYED);
            this.startTask = createStartTask();
            this.recorder.getTimer().schedule(this.startTask, duration.toMillis());
            Logger.log(LogTag.JFR, LogLevel.INFO, "Scheduled recording \"" + getName() + "\" (" + getId() + ") to start at " + ((Object) localDateTimePlus));
        }
    }

    private void ensureOkForSchedule() {
        if (getState() != RecordingState.NEW) {
            throw new IllegalStateException("Only a new recoridng can be scheduled for start");
        }
    }

    private TimerTask createStartTask() {
        return new TimerTask() { // from class: jdk.jfr.internal.PlatformRecording.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                synchronized (PlatformRecording.this.recorder) {
                    if (PlatformRecording.this.getState() != RecordingState.DELAYED) {
                        return;
                    }
                    PlatformRecording.this.start();
                }
            }
        };
    }

    void scheduleStart(Instant instant) {
        synchronized (this.recorder) {
            ensureOkForSchedule();
            this.startTime = instant;
            setState(RecordingState.DELAYED);
            this.startTask = createStartTask();
            this.recorder.getTimer().schedule(this.startTask, instant.toEpochMilli());
        }
    }

    public Map<String, String> getSettings() {
        Map<String, String> map;
        synchronized (this.recorder) {
            map = this.settings;
        }
        return map;
    }

    public long getSize() {
        return this.size;
    }

    public Instant getStopTime() {
        Instant instant;
        synchronized (this.recorder) {
            instant = this.stopTime;
        }
        return instant;
    }

    public Instant getStartTime() {
        Instant instant;
        synchronized (this.recorder) {
            instant = this.startTime;
        }
        return instant;
    }

    public Long getMaxSize() {
        Long lValueOf;
        synchronized (this.recorder) {
            lValueOf = Long.valueOf(this.maxSize);
        }
        return lValueOf;
    }

    public Duration getMaxAge() {
        Duration duration;
        synchronized (this.recorder) {
            duration = this.maxAge;
        }
        return duration;
    }

    public String getName() {
        String str;
        synchronized (this.recorder) {
            str = this.name;
        }
        return str;
    }

    public RecordingState getState() {
        RecordingState recordingState;
        synchronized (this.recorder) {
            recordingState = this.state;
        }
        return recordingState;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        RecordingState state;
        RecordingState state2;
        synchronized (this.recorder) {
            state = getState();
            if (RecordingState.CLOSED != getState()) {
                if (this.startTask != null) {
                    this.startTask.cancel();
                    this.startTask = null;
                }
                this.recorder.finish(this);
                Iterator<RepositoryChunk> it = this.chunks.iterator();
                while (it.hasNext()) {
                    removed(it.next());
                }
                this.chunks.clear();
                setState(RecordingState.CLOSED);
                Logger.log(LogTag.JFR, LogLevel.INFO, "Closed recording \"" + getName() + "\" (" + getId() + ")");
            }
            state2 = getState();
        }
        notifyIfStateChanged(state2, state);
    }

    public PlatformRecording newSnapshotClone(String str, Boolean bool) throws IOException {
        if (!Thread.holdsLock(this.recorder)) {
            throw new InternalError("Caller must have recorder lock");
        }
        RecordingState state = getState();
        if (state == RecordingState.CLOSED) {
            throw new IOException("Recording \"" + this.name + "\" (id=" + this.id + ") has been closed, no contents to write");
        }
        if (state == RecordingState.DELAYED || state == RecordingState.NEW) {
            throw new IOException("Recording \"" + this.name + "\" (id=" + this.id + ") has not started, no contents to write");
        }
        if (state == RecordingState.STOPPED) {
            PlatformRecording platformRecordingNewTemporaryRecording = this.recorder.newTemporaryRecording();
            Iterator<RepositoryChunk> it = this.chunks.iterator();
            while (it.hasNext()) {
                platformRecordingNewTemporaryRecording.add(it.next());
            }
            return platformRecordingNewTemporaryRecording;
        }
        PlatformRecording platformRecordingNewTemporaryRecording2 = this.recorder.newTemporaryRecording();
        platformRecordingNewTemporaryRecording2.setShouldWriteActiveRecordingEvent(false);
        platformRecordingNewTemporaryRecording2.setName(getName());
        platformRecordingNewTemporaryRecording2.setToDisk(true);
        if (!isToDisk()) {
            platformRecordingNewTemporaryRecording2.start();
        } else {
            Iterator<RepositoryChunk> it2 = this.chunks.iterator();
            while (it2.hasNext()) {
                platformRecordingNewTemporaryRecording2.add(it2.next());
            }
            platformRecordingNewTemporaryRecording2.setState(RecordingState.RUNNING);
            platformRecordingNewTemporaryRecording2.setStartTime(getStartTime());
        }
        if (bool == null) {
            platformRecordingNewTemporaryRecording2.setSettings(getSettings());
            platformRecordingNewTemporaryRecording2.stop(str);
        } else {
            synchronized (MetadataRepository.getInstance()) {
                platformRecordingNewTemporaryRecording2.setSettings(OldObjectSample.createSettingsForSnapshot(this, bool));
                platformRecordingNewTemporaryRecording2.stop(str);
            }
        }
        return platformRecordingNewTemporaryRecording2;
    }

    public boolean isToDisk() {
        boolean z2;
        synchronized (this.recorder) {
            z2 = this.toDisk;
        }
        return z2;
    }

    public void setMaxSize(long j2) {
        synchronized (this.recorder) {
            if (getState() == RecordingState.CLOSED) {
                throw new IllegalStateException("Can't set max age when recording is closed");
            }
            this.maxSize = j2;
            trimToSize();
        }
    }

    public void setDestination(WriteableUserPath writeableUserPath) throws IOException {
        synchronized (this.recorder) {
            if (Utils.isState(getState(), RecordingState.STOPPED, RecordingState.CLOSED)) {
                throw new IllegalStateException("Destination can't be set on a recording that has been stopped/closed");
            }
            this.destination = writeableUserPath;
        }
    }

    public WriteableUserPath getDestination() {
        WriteableUserPath writeableUserPath;
        synchronized (this.recorder) {
            writeableUserPath = this.destination;
        }
        return writeableUserPath;
    }

    void setState(RecordingState recordingState) {
        synchronized (this.recorder) {
            this.state = recordingState;
        }
    }

    void setStartTime(Instant instant) {
        synchronized (this.recorder) {
            this.startTime = instant;
        }
    }

    void setStopTime(Instant instant) {
        synchronized (this.recorder) {
            this.stopTime = instant;
        }
    }

    public long getId() {
        long j2;
        synchronized (this.recorder) {
            j2 = this.id;
        }
        return j2;
    }

    public void setName(String str) {
        synchronized (this.recorder) {
            ensureNotClosed();
            this.name = str;
        }
    }

    private void ensureNotClosed() {
        if (getState() == RecordingState.CLOSED) {
            throw new IllegalStateException("Can't change name on a closed recording");
        }
    }

    public void setDumpOnExit(boolean z2) {
        synchronized (this.recorder) {
            this.dumpOnExit = z2;
        }
    }

    public boolean getDumpOnExit() {
        boolean z2;
        synchronized (this.recorder) {
            z2 = this.dumpOnExit;
        }
        return z2;
    }

    public void setToDisk(boolean z2) {
        synchronized (this.recorder) {
            if (Utils.isState(getState(), RecordingState.NEW, RecordingState.DELAYED)) {
                this.toDisk = z2;
            } else {
                throw new IllegalStateException("Recording option disk can't be changed after recording has started");
            }
        }
    }

    public void setSetting(String str, String str2) {
        synchronized (this.recorder) {
            this.settings.put(str, str2);
            if (getState() == RecordingState.RUNNING) {
                this.recorder.updateSettings();
            }
        }
    }

    public void setSettings(Map<String, String> map) {
        setSettings(map, true);
    }

    private void setSettings(Map<String, String> map, boolean z2) {
        if (Logger.shouldLog(LogTag.JFR_SETTING, LogLevel.INFO) && z2) {
            TreeMap treeMap = new TreeMap(map);
            Logger.log(LogTag.JFR_SETTING, LogLevel.INFO, "New settings for recording \"" + getName() + "\" (" + getId() + ")");
            for (Map.Entry entry : treeMap.entrySet()) {
                Logger.log(LogTag.JFR_SETTING, LogLevel.INFO, ((String) entry.getKey()) + "=\"" + ((String) entry.getValue()) + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        }
        synchronized (this.recorder) {
            this.settings = new LinkedHashMap(map);
            if (getState() == RecordingState.RUNNING && z2) {
                this.recorder.updateSettings();
            }
        }
    }

    private void notifyIfStateChanged(RecordingState recordingState, RecordingState recordingState2) {
        if (recordingState2 == recordingState) {
            return;
        }
        Iterator<FlightRecorderListener> it = PlatformRecorder.getListeners().iterator();
        while (it.hasNext()) {
            try {
                it.next().recordingStateChanged(getRecording());
            } catch (RuntimeException e2) {
                Logger.log(LogTag.JFR, LogLevel.WARN, "Error notifying recorder listener:" + e2.getMessage());
            }
        }
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
    }

    public Recording getRecording() {
        return this.recording;
    }

    public String toString() {
        return getName() + " (id=" + getId() + ") " + ((Object) getState());
    }

    public void setConfiguration(Configuration configuration) {
        setSettings(configuration.getSettings());
    }

    public void setMaxAge(Duration duration) {
        synchronized (this.recorder) {
            if (getState() == RecordingState.CLOSED) {
                throw new IllegalStateException("Can't set max age when recording is closed");
            }
            this.maxAge = duration;
            if (duration != null) {
                trimToAge(Instant.now().minus((TemporalAmount) duration));
            }
        }
    }

    void appendChunk(RepositoryChunk repositoryChunk) {
        if (!repositoryChunk.isFinished()) {
            throw new Error("not finished chunk " + ((Object) repositoryChunk.getStartTime()));
        }
        synchronized (this.recorder) {
            if (this.toDisk) {
                if (this.maxAge != null) {
                    trimToAge(repositoryChunk.getEndTime().minus((TemporalAmount) this.maxAge));
                }
                this.chunks.addLast(repositoryChunk);
                added(repositoryChunk);
                trimToSize();
            }
        }
    }

    private void trimToSize() {
        if (this.maxSize == 0) {
            return;
        }
        while (this.size > this.maxSize && this.chunks.size() > 1) {
            removed(this.chunks.removeFirst());
        }
    }

    private void trimToAge(Instant instant) {
        while (!this.chunks.isEmpty()) {
            RepositoryChunk repositoryChunkPeek = this.chunks.peek();
            if (repositoryChunkPeek.getEndTime().isAfter(instant)) {
                return;
            }
            this.chunks.removeFirst();
            removed(repositoryChunkPeek);
        }
    }

    void add(RepositoryChunk repositoryChunk) {
        this.chunks.add(repositoryChunk);
        added(repositoryChunk);
    }

    private void added(RepositoryChunk repositoryChunk) {
        repositoryChunk.use();
        this.size += repositoryChunk.getSize();
        Logger.log(LogTag.JFR, LogLevel.DEBUG, (Supplier<String>) () -> {
            return "Recording \"" + this.name + "\" (" + this.id + ") added chunk " + repositoryChunk.toString() + ", current size=" + this.size;
        });
    }

    private void removed(RepositoryChunk repositoryChunk) {
        this.size -= repositoryChunk.getSize();
        Logger.log(LogTag.JFR, LogLevel.DEBUG, (Supplier<String>) () -> {
            return "Recording \"" + this.name + "\" (" + this.id + ") removed chunk " + repositoryChunk.toString() + ", current size=" + this.size;
        });
        repositoryChunk.release();
    }

    public List<RepositoryChunk> getChunks() {
        return this.chunks;
    }

    public InputStream open(Instant instant, Instant instant2) throws IOException {
        synchronized (this.recorder) {
            if (getState() != RecordingState.STOPPED) {
                throw new IOException("Recording must be stopped before it can be read.");
            }
            ArrayList arrayList = new ArrayList();
            Iterator<RepositoryChunk> it = this.chunks.iterator();
            while (it.hasNext()) {
                RepositoryChunk next = it.next();
                if (next.isFinished()) {
                    Instant startTime = next.getStartTime();
                    Instant endTime = next.getEndTime();
                    if ((instant == null || !endTime.isBefore(instant)) && (instant2 == null || !startTime.isAfter(instant2))) {
                        arrayList.add(next);
                    }
                }
            }
            if (arrayList.isEmpty()) {
                return null;
            }
            return new ChunkInputStream(arrayList);
        }
    }

    public Duration getDuration() {
        Duration duration;
        synchronized (this.recorder) {
            duration = this.duration;
        }
        return duration;
    }

    void setInternalDuration(Duration duration) {
        this.duration = duration;
    }

    public void setDuration(Duration duration) {
        synchronized (this.recorder) {
            if (Utils.isState(getState(), RecordingState.STOPPED, RecordingState.CLOSED)) {
                throw new IllegalStateException("Duration can't be set after a recording has been stopped/closed");
            }
            setInternalDuration(duration);
            if (getState() != RecordingState.NEW) {
                updateTimer();
            }
        }
    }

    void updateTimer() {
        if (this.stopTask != null) {
            this.stopTask.cancel();
            this.stopTask = null;
        }
        if (getState() != RecordingState.CLOSED && this.duration != null) {
            this.stopTask = createStopTask();
            this.recorder.getTimer().schedule(this.stopTask, new Date(this.startTime.plus((TemporalAmount) this.duration).toEpochMilli()));
        }
    }

    TimerTask createStopTask() {
        return new TimerTask() { // from class: jdk.jfr.internal.PlatformRecording.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    PlatformRecording.this.stop("End of duration reached");
                } catch (Throwable th) {
                    Logger.log(LogTag.JFR, LogLevel.ERROR, "Could not stop recording.");
                }
            }
        };
    }

    public Recording newCopy(boolean z2) {
        return this.recorder.newCopy(this, z2);
    }

    void setStopTask(TimerTask timerTask) {
        synchronized (this.recorder) {
            this.stopTask = timerTask;
        }
    }

    void clearDestination() {
        this.destination = null;
    }

    public AccessControlContext getNoDestinationDumpOnExitAccessControlContext() {
        return this.noDestinationDumpOnExitAccessControlContext;
    }

    void setShouldWriteActiveRecordingEvent(boolean z2) {
        this.shuoldWriteActiveRecordingEvent = z2;
    }

    boolean shouldWriteMetadataEvent() {
        return this.shuoldWriteActiveRecordingEvent;
    }

    public void dump(WriteableUserPath writeableUserPath) throws IOException {
        synchronized (this.recorder) {
            PlatformRecording platformRecordingNewSnapshotClone = newSnapshotClone("Dumped by user", null);
            Throwable th = null;
            try {
                try {
                    platformRecordingNewSnapshotClone.dumpStopped(writeableUserPath);
                    if (platformRecordingNewSnapshotClone != null) {
                        if (0 != 0) {
                            try {
                                platformRecordingNewSnapshotClone.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            platformRecordingNewSnapshotClone.close();
                        }
                    }
                } finally {
                }
            } finally {
            }
        }
    }

    public void dumpStopped(WriteableUserPath writeableUserPath) throws IOException {
        synchronized (this.recorder) {
            writeableUserPath.doPriviligedIO(() -> {
                ChunksChannel chunksChannel = new ChunksChannel(this.chunks);
                Throwable th = null;
                try {
                    FileChannel fileChannelOpen = FileChannel.open(writeableUserPath.getReal(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
                    Throwable th2 = null;
                    try {
                        long jTransferTo = chunksChannel.transferTo(fileChannelOpen);
                        Logger.log(LogTag.JFR, LogLevel.INFO, "Transferred " + jTransferTo + " bytes from the disk repository");
                        if (jTransferTo != 0) {
                            fileChannelOpen.force(true);
                        }
                        if (fileChannelOpen != null) {
                            if (0 != 0) {
                                try {
                                    fileChannelOpen.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                fileChannelOpen.close();
                            }
                        }
                        if (chunksChannel == null) {
                            return null;
                        }
                        if (0 != 0) {
                            try {
                                chunksChannel.close();
                                return null;
                            } catch (Throwable th4) {
                                th.addSuppressed(th4);
                                return null;
                            }
                        }
                        chunksChannel.close();
                        return null;
                    } catch (Throwable th5) {
                        if (fileChannelOpen != null) {
                            if (0 != 0) {
                                try {
                                    fileChannelOpen.close();
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                }
                            } else {
                                fileChannelOpen.close();
                            }
                        }
                        throw th5;
                    }
                } catch (Throwable th7) {
                    if (chunksChannel != null) {
                        if (0 != 0) {
                            try {
                                chunksChannel.close();
                            } catch (Throwable th8) {
                                th.addSuppressed(th8);
                            }
                        } else {
                            chunksChannel.close();
                        }
                    }
                    throw th7;
                }
            });
        }
    }

    public void filter(Instant instant, Instant instant2, Long l2) {
        synchronized (this.recorder) {
            List<RepositoryChunk> listRemoveAfter = removeAfter(instant2, removeBefore(instant, new ArrayList(this.chunks)));
            if (l2 != null) {
                if (instant != null && instant2 == null) {
                    listRemoveAfter = reduceFromBeginning(l2, listRemoveAfter);
                } else {
                    listRemoveAfter = reduceFromEnd(l2, listRemoveAfter);
                }
            }
            int size = 0;
            for (RepositoryChunk repositoryChunk : listRemoveAfter) {
                size = (int) (size + repositoryChunk.getSize());
                repositoryChunk.use();
            }
            this.size = size;
            Iterator<RepositoryChunk> it = this.chunks.iterator();
            while (it.hasNext()) {
                it.next().release();
            }
            this.chunks.clear();
            this.chunks.addAll(listRemoveAfter);
        }
    }

    private static List<RepositoryChunk> removeBefore(Instant instant, List<RepositoryChunk> list) {
        if (instant == null) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (RepositoryChunk repositoryChunk : list) {
            if (!repositoryChunk.getEndTime().isBefore(instant)) {
                arrayList.add(repositoryChunk);
            }
        }
        return arrayList;
    }

    private static List<RepositoryChunk> removeAfter(Instant instant, List<RepositoryChunk> list) {
        if (instant == null) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (RepositoryChunk repositoryChunk : list) {
            if (!repositoryChunk.getStartTime().isAfter(instant)) {
                arrayList.add(repositoryChunk);
            }
        }
        return arrayList;
    }

    private static List<RepositoryChunk> reduceFromBeginning(Long l2, List<RepositoryChunk> list) {
        if (l2 == null || list.isEmpty()) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list.size());
        long size = 0;
        for (RepositoryChunk repositoryChunk : list) {
            size += repositoryChunk.getSize();
            if (size > l2.longValue()) {
                break;
            }
            arrayList.add(repositoryChunk);
        }
        if (arrayList.isEmpty()) {
            arrayList.add(list.get(0));
        }
        return arrayList;
    }

    private static List<RepositoryChunk> reduceFromEnd(Long l2, List<RepositoryChunk> list) {
        Collections.reverse(list);
        List<RepositoryChunk> listReduceFromBeginning = reduceFromBeginning(l2, list);
        Collections.reverse(listReduceFromBeginning);
        return listReduceFromBeginning;
    }

    public void setDumpOnExitDirectory(SecuritySupport.SafePath safePath) {
        this.dumpOnExitDirectory = safePath;
    }

    public SecuritySupport.SafePath getDumpOnExitDirectory() {
        return this.dumpOnExitDirectory;
    }
}
