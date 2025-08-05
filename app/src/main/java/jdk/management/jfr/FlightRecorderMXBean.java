package jdk.management.jfr;

import java.io.IOException;
import java.lang.management.PlatformManagedObject;
import java.util.List;
import java.util.Map;

/* loaded from: jfr.jar:jdk/management/jfr/FlightRecorderMXBean.class */
public interface FlightRecorderMXBean extends PlatformManagedObject {
    public static final String MXBEAN_NAME = "jdk.management.jfr:type=FlightRecorder";

    long newRecording() throws IllegalStateException, SecurityException;

    long takeSnapshot();

    long cloneRecording(long j2, boolean z2) throws SecurityException, IllegalArgumentException;

    void startRecording(long j2) throws IllegalStateException, SecurityException;

    boolean stopRecording(long j2) throws IllegalStateException, SecurityException, IllegalArgumentException;

    void closeRecording(long j2) throws IOException;

    long openStream(long j2, Map<String, String> map) throws IOException;

    void closeStream(long j2) throws IOException;

    byte[] readStream(long j2) throws IOException;

    Map<String, String> getRecordingOptions(long j2) throws IllegalArgumentException;

    Map<String, String> getRecordingSettings(long j2) throws IllegalArgumentException;

    void setConfiguration(long j2, String str) throws IllegalArgumentException;

    void setPredefinedConfiguration(long j2, String str) throws IllegalArgumentException;

    void setRecordingSettings(long j2, Map<String, String> map) throws IllegalArgumentException;

    void setRecordingOptions(long j2, Map<String, String> map) throws IllegalArgumentException;

    List<RecordingInfo> getRecordings();

    List<ConfigurationInfo> getConfigurations();

    List<EventTypeInfo> getEventTypes();

    void copyTo(long j2, String str) throws IOException, SecurityException;
}
