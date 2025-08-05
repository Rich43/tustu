package jdk.jfr.internal;

import java.security.AccessController;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.jfr.EventType;
import jdk.jfr.FlightRecorder;
import jdk.jfr.FlightRecorderListener;
import jdk.jfr.Recording;
import jdk.jfr.RecordingState;
import jdk.jfr.events.ActiveRecordingEvent;
import jdk.jfr.events.ActiveSettingEvent;
import jdk.jfr.internal.SecuritySupport;
import jdk.jfr.internal.ShutdownHook;
import jdk.jfr.internal.instrument.JDKEvents;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/PlatformRecorder.class */
public final class PlatformRecorder {
    private final Timer timer;
    private final EventType activeRecordingEvent;
    private final EventType activeSettingEvent;
    private final Thread shutdownHook;
    private RepositoryChunk currentChunk;
    private static final List<SecuritySupport.SecureRecorderListener> changeListeners = new ArrayList();
    private static final JVM jvm = JVM.getJVM();
    private final List<PlatformRecording> recordings = new ArrayList();
    private long recordingCounter = 0;
    private final Repository repository = Repository.getRepository();

    public PlatformRecorder() throws Exception {
        Logger.log(LogTag.JFR_SYSTEM, LogLevel.INFO, "Initialized disk repository");
        this.repository.ensureRepository();
        jvm.createNativeJFR();
        Logger.log(LogTag.JFR_SYSTEM, LogLevel.INFO, "Created native");
        JDKEvents.initialize();
        Logger.log(LogTag.JFR_SYSTEM, LogLevel.INFO, "Registered JDK events");
        JDKEvents.addInstrumentation();
        startDiskMonitor();
        SecuritySupport.registerEvent(ActiveRecordingEvent.class);
        this.activeRecordingEvent = EventType.getEventType(ActiveRecordingEvent.class);
        SecuritySupport.registerEvent(ActiveSettingEvent.class);
        this.activeSettingEvent = EventType.getEventType(ActiveSettingEvent.class);
        this.shutdownHook = SecuritySupport.createThreadWitNoPermissions("JFR: Shutdown Hook", new ShutdownHook(this));
        SecuritySupport.setUncaughtExceptionHandler(this.shutdownHook, new ShutdownHook.ExceptionHandler());
        SecuritySupport.registerShutdownHook(this.shutdownHook);
        this.timer = createTimer();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Timer createTimer() {
        try {
            CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
            Thread threadCreateThreadWitNoPermissions = SecuritySupport.createThreadWitNoPermissions("Permissionless thread", () -> {
                copyOnWriteArrayList.add(new Timer("JFR Recording Scheduler", true));
            });
            threadCreateThreadWitNoPermissions.start();
            threadCreateThreadWitNoPermissions.join();
            return (Timer) copyOnWriteArrayList.get(0);
        } catch (InterruptedException e2) {
            throw new IllegalStateException("Not able to create timer task. " + e2.getMessage(), e2);
        }
    }

    public synchronized PlatformRecording newRecording(Map<String, String> map) {
        long j2 = this.recordingCounter + 1;
        this.recordingCounter = j2;
        return newRecording(map, j2);
    }

    public PlatformRecording newTemporaryRecording() {
        if (!Thread.holdsLock(this)) {
            throw new InternalError("Caller must have recorder lock");
        }
        return newRecording(new HashMap(), 0L);
    }

    private synchronized PlatformRecording newRecording(Map<String, String> map, long j2) {
        PlatformRecording platformRecording = new PlatformRecording(this, j2);
        if (!map.isEmpty()) {
            platformRecording.setSettings(map);
        }
        this.recordings.add(platformRecording);
        return platformRecording;
    }

    synchronized void finish(PlatformRecording platformRecording) {
        if (platformRecording.getState() == RecordingState.RUNNING) {
            platformRecording.stop("Recording closed");
        }
        this.recordings.remove(platformRecording);
    }

    public synchronized List<PlatformRecording> getRecordings() {
        return Collections.unmodifiableList(new ArrayList(this.recordings));
    }

    public static synchronized void addListener(FlightRecorderListener flightRecorderListener) {
        boolean zIsInitialized;
        SecuritySupport.SecureRecorderListener secureRecorderListener = new SecuritySupport.SecureRecorderListener(AccessController.getContext(), flightRecorderListener);
        synchronized (PlatformRecorder.class) {
            zIsInitialized = FlightRecorder.isInitialized();
            changeListeners.add(secureRecorderListener);
        }
        if (zIsInitialized) {
            secureRecorderListener.recorderInitialized(FlightRecorder.getFlightRecorder());
        }
    }

    public static synchronized boolean removeListener(FlightRecorderListener flightRecorderListener) {
        Iterator it = new ArrayList(changeListeners).iterator();
        while (it.hasNext()) {
            SecuritySupport.SecureRecorderListener secureRecorderListener = (SecuritySupport.SecureRecorderListener) it.next();
            if (secureRecorderListener.getChangeListener() == flightRecorderListener) {
                changeListeners.remove(secureRecorderListener);
                return true;
            }
        }
        return false;
    }

    static synchronized List<FlightRecorderListener> getListeners() {
        return new ArrayList(changeListeners);
    }

    Timer getTimer() {
        return this.timer;
    }

    public static void notifyRecorderInitialized(FlightRecorder flightRecorder) {
        Logger.log(LogTag.JFR_SYSTEM, LogLevel.TRACE, "Notifying listeners that Flight Recorder is initialized");
        Iterator<FlightRecorderListener> it = getListeners().iterator();
        while (it.hasNext()) {
            it.next().recorderInitialized(flightRecorder);
        }
    }

    synchronized void destroy() {
        try {
            this.timer.cancel();
        } catch (Exception e2) {
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.WARN, "Shutdown hook could not cancel timer");
        }
        for (PlatformRecording platformRecording : getRecordings()) {
            if (platformRecording.getState() == RecordingState.RUNNING) {
                try {
                    platformRecording.stop("Shutdown");
                } catch (Exception e3) {
                    Logger.log(LogTag.JFR, LogLevel.WARN, "Recording " + platformRecording.getName() + CallSiteDescriptor.TOKEN_DELIMITER + platformRecording.getId() + " could not be stopped");
                }
            }
        }
        JDKEvents.remove();
        if (jvm.hasNativeJFR()) {
            if (jvm.isRecording()) {
                jvm.endRecording_();
            }
            jvm.destroyNativeJFR();
        }
        this.repository.clear();
    }

    synchronized void start(PlatformRecording platformRecording) {
        Instant instantNow = Instant.now();
        platformRecording.setStartTime(instantNow);
        platformRecording.updateTimer();
        Duration duration = platformRecording.getDuration();
        if (duration != null) {
            platformRecording.setStopTime(instantNow.plus((TemporalAmount) duration));
        }
        boolean zIsToDisk = platformRecording.isToDisk();
        boolean z2 = true;
        for (PlatformRecording platformRecording2 : getRecordings()) {
            if (platformRecording2.getState() == RecordingState.RUNNING) {
                z2 = false;
                if (platformRecording2.isToDisk()) {
                    zIsToDisk = true;
                }
            }
        }
        if (z2) {
            RepositoryChunk repositoryChunkNewChunk = null;
            if (zIsToDisk) {
                repositoryChunkNewChunk = this.repository.newChunk(instantNow);
                MetadataRepository.getInstance().setOutput(repositoryChunkNewChunk.getUnfishedFile().toString());
            } else {
                MetadataRepository.getInstance().setOutput(null);
            }
            this.currentChunk = repositoryChunkNewChunk;
            jvm.beginRecording_();
            platformRecording.setState(RecordingState.RUNNING);
            updateSettings();
            writeMetaEvents();
        } else {
            RepositoryChunk repositoryChunkNewChunk2 = null;
            if (zIsToDisk) {
                repositoryChunkNewChunk2 = this.repository.newChunk(instantNow);
                RequestEngine.doChunkEnd();
                MetadataRepository.getInstance().setOutput(repositoryChunkNewChunk2.getUnfishedFile().toString());
            }
            platformRecording.setState(RecordingState.RUNNING);
            updateSettings();
            writeMetaEvents();
            if (this.currentChunk != null) {
                finishChunk(this.currentChunk, instantNow, platformRecording);
            }
            this.currentChunk = repositoryChunkNewChunk2;
        }
        RequestEngine.doChunkBegin();
    }

    synchronized void stop(PlatformRecording platformRecording) {
        RecordingState state = platformRecording.getState();
        if (Utils.isAfter(state, RecordingState.RUNNING)) {
            throw new IllegalStateException("Can't stop an already stopped recording.");
        }
        if (Utils.isBefore(state, RecordingState.RUNNING)) {
            throw new IllegalStateException("Recording must be started before it can be stopped.");
        }
        Instant instantNow = Instant.now();
        boolean z2 = false;
        boolean z3 = true;
        for (PlatformRecording platformRecording2 : getRecordings()) {
            RecordingState state2 = platformRecording2.getState();
            if (platformRecording2 != platformRecording && RecordingState.RUNNING == state2) {
                z3 = false;
                if (platformRecording2.isToDisk()) {
                    z2 = true;
                }
            }
        }
        OldObjectSample.emit(platformRecording);
        if (z3) {
            RequestEngine.doChunkEnd();
            if (platformRecording.isToDisk()) {
                if (this.currentChunk != null) {
                    MetadataRepository.getInstance().setOutput(null);
                    finishChunk(this.currentChunk, instantNow, null);
                    this.currentChunk = null;
                }
            } else {
                dumpMemoryToDestination(platformRecording);
            }
            jvm.endRecording_();
            disableEvents();
        } else {
            RepositoryChunk repositoryChunkNewChunk = null;
            RequestEngine.doChunkEnd();
            updateSettingsButIgnoreRecording(platformRecording);
            if (z2) {
                repositoryChunkNewChunk = this.repository.newChunk(instantNow);
                MetadataRepository.getInstance().setOutput(repositoryChunkNewChunk.getUnfishedFile().toString());
            } else {
                MetadataRepository.getInstance().setOutput(null);
            }
            writeMetaEvents();
            if (this.currentChunk != null) {
                finishChunk(this.currentChunk, instantNow, null);
            }
            this.currentChunk = repositoryChunkNewChunk;
            RequestEngine.doChunkBegin();
        }
        platformRecording.setState(RecordingState.STOPPED);
    }

    private void dumpMemoryToDestination(PlatformRecording platformRecording) {
        WriteableUserPath destination = platformRecording.getDestination();
        if (destination != null) {
            MetadataRepository.getInstance().setOutput(destination.getRealPathText());
            platformRecording.clearDestination();
        }
    }

    private void disableEvents() {
        MetadataRepository.getInstance().disableEvents();
    }

    void updateSettings() {
        updateSettingsButIgnoreRecording(null);
    }

    void updateSettingsButIgnoreRecording(PlatformRecording platformRecording) {
        List<PlatformRecording> runningRecordings = getRunningRecordings();
        ArrayList arrayList = new ArrayList(runningRecordings.size());
        for (PlatformRecording platformRecording2 : runningRecordings) {
            if (platformRecording2 != platformRecording) {
                arrayList.add(platformRecording2.getSettings());
            }
        }
        MetadataRepository.getInstance().setSettings(arrayList);
    }

    synchronized void rotateDisk() {
        Instant instantNow = Instant.now();
        RepositoryChunk repositoryChunkNewChunk = this.repository.newChunk(instantNow);
        RequestEngine.doChunkEnd();
        MetadataRepository.getInstance().setOutput(repositoryChunkNewChunk.getUnfishedFile().toString());
        writeMetaEvents();
        if (this.currentChunk != null) {
            finishChunk(this.currentChunk, instantNow, null);
        }
        this.currentChunk = repositoryChunkNewChunk;
        RequestEngine.doChunkBegin();
    }

    private List<PlatformRecording> getRunningRecordings() {
        ArrayList arrayList = new ArrayList();
        for (PlatformRecording platformRecording : getRecordings()) {
            if (platformRecording.getState() == RecordingState.RUNNING) {
                arrayList.add(platformRecording);
            }
        }
        return arrayList;
    }

    private List<RepositoryChunk> makeChunkList(Instant instant, Instant instant2) {
        HashSet<RepositoryChunk> hashSet = new HashSet();
        Iterator<PlatformRecording> it = getRecordings().iterator();
        while (it.hasNext()) {
            hashSet.addAll(it.next().getChunks());
        }
        if (hashSet.size() > 0) {
            ArrayList arrayList = new ArrayList(hashSet.size());
            for (RepositoryChunk repositoryChunk : hashSet) {
                if (repositoryChunk.inInterval(instant, instant2)) {
                    arrayList.add(repositoryChunk);
                }
            }
            Collections.sort(arrayList, RepositoryChunk.END_TIME_COMPARATOR);
            return arrayList;
        }
        return Collections.emptyList();
    }

    private void startDiskMonitor() {
        Thread threadCreateThreadWitNoPermissions = SecuritySupport.createThreadWitNoPermissions("JFR Periodic Tasks", () -> {
            periodicTask();
        });
        SecuritySupport.setDaemonThread(threadCreateThreadWitNoPermissions, true);
        threadCreateThreadWitNoPermissions.start();
    }

    private void finishChunk(RepositoryChunk repositoryChunk, Instant instant, PlatformRecording platformRecording) {
        repositoryChunk.finish(instant);
        for (PlatformRecording platformRecording2 : getRecordings()) {
            if (platformRecording2 != platformRecording && platformRecording2.getState() == RecordingState.RUNNING) {
                platformRecording2.appendChunk(repositoryChunk);
            }
        }
    }

    private void writeMetaEvents() {
        if (this.activeRecordingEvent.isEnabled()) {
            for (PlatformRecording platformRecording : getRecordings()) {
                if (platformRecording.getState() == RecordingState.RUNNING && platformRecording.shouldWriteMetadataEvent()) {
                    ActiveRecordingEvent activeRecordingEvent = new ActiveRecordingEvent();
                    activeRecordingEvent.id = platformRecording.getId();
                    activeRecordingEvent.name = platformRecording.getName();
                    WriteableUserPath destination = platformRecording.getDestination();
                    activeRecordingEvent.destination = destination == null ? null : destination.getRealPathText();
                    Duration duration = platformRecording.getDuration();
                    activeRecordingEvent.recordingDuration = duration == null ? Long.MAX_VALUE : duration.toMillis();
                    Duration maxAge = platformRecording.getMaxAge();
                    activeRecordingEvent.maxAge = maxAge == null ? Long.MAX_VALUE : maxAge.toMillis();
                    Long maxSize = platformRecording.getMaxSize();
                    activeRecordingEvent.maxSize = maxSize == null ? Long.MAX_VALUE : maxSize.longValue();
                    Instant startTime = platformRecording.getStartTime();
                    activeRecordingEvent.recordingStart = startTime == null ? Long.MAX_VALUE : startTime.toEpochMilli();
                    activeRecordingEvent.commit();
                }
            }
        }
        if (this.activeSettingEvent.isEnabled()) {
            Iterator<EventControl> it = MetadataRepository.getInstance().getEventControls().iterator();
            while (it.hasNext()) {
                it.next().writeActiveSettingEvent();
            }
        }
    }

    private void periodicTask() {
        if (!jvm.hasNativeJFR()) {
            return;
        }
        while (true) {
            synchronized (this) {
                if (jvm.shouldRotateDisk()) {
                    rotateDisk();
                }
            }
            takeNap(Math.min(RequestEngine.doPeriodic(), Options.getWaitInterval()));
        }
    }

    private void takeNap(long j2) {
        try {
            synchronized (JVM.FILE_DELTA_CHANGE) {
                JVM.FILE_DELTA_CHANGE.wait(j2 < 10 ? 10L : j2);
            }
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    synchronized Recording newCopy(PlatformRecording platformRecording, boolean z2) {
        Recording recording = new Recording();
        PlatformRecording platformRecording2 = PrivateAccess.getInstance().getPlatformRecording(recording);
        platformRecording2.setSettings(platformRecording.getSettings());
        platformRecording2.setMaxAge(platformRecording.getMaxAge());
        platformRecording2.setMaxSize(platformRecording.getMaxSize().longValue());
        platformRecording2.setDumpOnExit(platformRecording.getDumpOnExit());
        platformRecording2.setName("Clone of " + platformRecording.getName());
        platformRecording2.setToDisk(platformRecording.isToDisk());
        platformRecording2.setInternalDuration(platformRecording.getDuration());
        platformRecording2.setStartTime(platformRecording.getStartTime());
        platformRecording2.setStopTime(platformRecording.getStopTime());
        if (platformRecording.getState() == RecordingState.NEW) {
            return recording;
        }
        if (platformRecording.getState() == RecordingState.DELAYED) {
            platformRecording2.scheduleStart(platformRecording.getStartTime());
            return recording;
        }
        platformRecording2.setState(platformRecording.getState());
        Iterator<RepositoryChunk> it = platformRecording.getChunks().iterator();
        while (it.hasNext()) {
            platformRecording2.add(it.next());
        }
        if (platformRecording.getState() == RecordingState.RUNNING) {
            if (z2) {
                platformRecording2.stop("Stopped when cloning recording '" + platformRecording.getName() + PdfOps.SINGLE_QUOTE_TOKEN);
            } else if (platformRecording.getStopTime() != null) {
                TimerTask timerTaskCreateStopTask = platformRecording2.createStopTask();
                platformRecording2.setStopTask(platformRecording2.createStopTask());
                getTimer().schedule(timerTaskCreateStopTask, platformRecording.getStopTime().toEpochMilli());
            }
        }
        return recording;
    }

    public synchronized void fillWithRecordedData(PlatformRecording platformRecording, Boolean bool) {
        boolean z2 = false;
        boolean z3 = false;
        for (PlatformRecording platformRecording2 : this.recordings) {
            if (platformRecording2.getState() == RecordingState.RUNNING) {
                z2 = true;
                if (platformRecording2.isToDisk()) {
                    z3 = true;
                }
            }
        }
        if (z2) {
            if (z3) {
                OldObjectSample.emit(this.recordings, bool);
                rotateDisk();
            } else {
                PlatformRecording platformRecordingNewTemporaryRecording = newTemporaryRecording();
                Throwable th = null;
                try {
                    try {
                        platformRecordingNewTemporaryRecording.setToDisk(true);
                        platformRecordingNewTemporaryRecording.setShouldWriteActiveRecordingEvent(false);
                        platformRecordingNewTemporaryRecording.start();
                        OldObjectSample.emit(this.recordings, bool);
                        platformRecordingNewTemporaryRecording.stop("Snapshot dump");
                        fillWithDiskChunks(platformRecording);
                        if (platformRecordingNewTemporaryRecording != null) {
                            if (0 != 0) {
                                try {
                                    platformRecordingNewTemporaryRecording.close();
                                    return;
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                    return;
                                }
                            }
                            platformRecordingNewTemporaryRecording.close();
                            return;
                        }
                        return;
                    } catch (Throwable th3) {
                        th = th3;
                        throw th3;
                    }
                } catch (Throwable th4) {
                    if (platformRecordingNewTemporaryRecording != null) {
                        if (th != null) {
                            try {
                                platformRecordingNewTemporaryRecording.close();
                            } catch (Throwable th5) {
                                th.addSuppressed(th5);
                            }
                        } else {
                            platformRecordingNewTemporaryRecording.close();
                        }
                    }
                    throw th4;
                }
            }
        }
        fillWithDiskChunks(platformRecording);
    }

    private void fillWithDiskChunks(PlatformRecording platformRecording) {
        Iterator<RepositoryChunk> it = makeChunkList(null, null).iterator();
        while (it.hasNext()) {
            platformRecording.add(it.next());
        }
        platformRecording.setState(RecordingState.STOPPED);
        Instant startTime = null;
        Instant endTime = null;
        for (RepositoryChunk repositoryChunk : platformRecording.getChunks()) {
            if (startTime == null || repositoryChunk.getStartTime().isBefore(startTime)) {
                startTime = repositoryChunk.getStartTime();
            }
            if (endTime == null || repositoryChunk.getEndTime().isAfter(endTime)) {
                endTime = repositoryChunk.getEndTime();
            }
        }
        Instant instantNow = Instant.now();
        if (startTime == null) {
            startTime = instantNow;
        }
        if (endTime == null) {
            endTime = instantNow;
        }
        platformRecording.setStartTime(startTime);
        platformRecording.setStopTime(endTime);
        platformRecording.setInternalDuration(Duration.between(startTime, endTime));
    }
}
