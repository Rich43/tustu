package jdk.jfr;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import jdk.jfr.internal.PlatformRecorder;
import jdk.jfr.internal.PlatformRecording;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;
import jdk.jfr.internal.WriteableUserPath;

/* loaded from: jfr.jar:jdk/jfr/Recording.class */
public final class Recording implements Closeable {
    private final PlatformRecording internal;

    /* loaded from: jfr.jar:jdk/jfr/Recording$RecordingSettings.class */
    private static class RecordingSettings extends EventSettings {
        private final Recording recording;
        private final String identifier;

        RecordingSettings(Recording recording, String str) {
            this.recording = recording;
            this.identifier = str;
        }

        RecordingSettings(Recording recording, Class<? extends Event> cls) {
            Utils.ensureValidEventSubclass(cls);
            this.recording = recording;
            this.identifier = String.valueOf(Type.getTypeId(cls));
        }

        @Override // jdk.jfr.EventSettings
        public EventSettings with(String str, String str2) {
            Objects.requireNonNull(str2);
            this.recording.setSetting(this.identifier + FXMLLoader.CONTROLLER_METHOD_PREFIX + str, str2);
            return this;
        }

        @Override // jdk.jfr.EventSettings
        public Map<String, String> toMap() {
            return this.recording.getSettings();
        }
    }

    public Recording(Map<String, String> map) {
        PlatformRecorder internal = FlightRecorder.getFlightRecorder().getInternal();
        synchronized (internal) {
            this.internal = internal.newRecording(map);
            this.internal.setRecording(this);
            if (this.internal.getRecording() != this) {
                throw new InternalError("Internal recording not properly setup");
            }
        }
    }

    public Recording() {
        this(new HashMap());
    }

    public Recording(Configuration configuration) {
        this(configuration.getSettings());
    }

    public void start() {
        this.internal.start();
    }

    public void scheduleStart(Duration duration) {
        Objects.requireNonNull(duration);
        this.internal.scheduleStart(duration);
    }

    public boolean stop() {
        return this.internal.stop("Stopped by user");
    }

    public Map<String, String> getSettings() {
        return new HashMap(this.internal.getSettings());
    }

    public long getSize() {
        return this.internal.getSize();
    }

    public Instant getStopTime() {
        return this.internal.getStopTime();
    }

    public Instant getStartTime() {
        return this.internal.getStartTime();
    }

    public long getMaxSize() {
        return this.internal.getMaxSize().longValue();
    }

    public Duration getMaxAge() {
        return this.internal.getMaxAge();
    }

    public String getName() {
        return this.internal.getName();
    }

    public void setSettings(Map<String, String> map) {
        Objects.requireNonNull(map);
        this.internal.setSettings(Utils.sanitizeNullFreeStringMap(map));
    }

    public RecordingState getState() {
        return this.internal.getState();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.internal.close();
    }

    public Recording copy(boolean z2) {
        return this.internal.newCopy(z2);
    }

    public void dump(Path path) throws IOException {
        Objects.requireNonNull(path);
        this.internal.dump(new WriteableUserPath(path));
    }

    public boolean isToDisk() {
        return this.internal.isToDisk();
    }

    public void setMaxSize(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Max size of recording can't be negative");
        }
        this.internal.setMaxSize(j2);
    }

    public void setMaxAge(Duration duration) {
        if (duration != null && duration.isNegative()) {
            throw new IllegalArgumentException("Max age of recording can't be negative");
        }
        this.internal.setMaxAge(duration);
    }

    public void setDestination(Path path) throws IOException {
        this.internal.setDestination(path != null ? new WriteableUserPath(path) : null);
    }

    public Path getDestination() {
        WriteableUserPath destination = this.internal.getDestination();
        if (destination == null) {
            return null;
        }
        return destination.getPotentiallyMaliciousOriginal();
    }

    public long getId() {
        return this.internal.getId();
    }

    public void setName(String str) {
        Objects.requireNonNull(str);
        this.internal.setName(str);
    }

    public void setDumpOnExit(boolean z2) {
        this.internal.setDumpOnExit(z2);
    }

    public boolean getDumpOnExit() {
        return this.internal.getDumpOnExit();
    }

    public void setToDisk(boolean z2) {
        this.internal.setToDisk(z2);
    }

    public InputStream getStream(Instant instant, Instant instant2) throws IOException {
        if (instant != null && instant2 != null && instant2.isBefore(instant)) {
            throw new IllegalArgumentException("End time of requested stream must not be before start time");
        }
        return this.internal.open(instant, instant2);
    }

    public Duration getDuration() {
        return this.internal.getDuration();
    }

    public void setDuration(Duration duration) {
        this.internal.setDuration(duration);
    }

    public EventSettings enable(String str) {
        Objects.requireNonNull(str);
        RecordingSettings recordingSettings = new RecordingSettings(this, str);
        recordingSettings.with(Enabled.NAME, "true");
        return recordingSettings;
    }

    public EventSettings disable(String str) {
        Objects.requireNonNull(str);
        RecordingSettings recordingSettings = new RecordingSettings(this, str);
        recordingSettings.with(Enabled.NAME, "false");
        return recordingSettings;
    }

    public EventSettings enable(Class<? extends Event> cls) {
        Objects.requireNonNull(cls);
        RecordingSettings recordingSettings = new RecordingSettings(this, cls);
        recordingSettings.with(Enabled.NAME, "true");
        return recordingSettings;
    }

    public EventSettings disable(Class<? extends Event> cls) {
        Objects.requireNonNull(cls);
        RecordingSettings recordingSettings = new RecordingSettings(this, cls);
        recordingSettings.with(Enabled.NAME, "false");
        return recordingSettings;
    }

    PlatformRecording getInternal() {
        return this.internal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSetting(String str, String str2) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(str2);
        this.internal.setSetting(str, str2);
    }
}
