package jdk.jfr;

/* loaded from: jfr.jar:jdk/jfr/FlightRecorderListener.class */
public interface FlightRecorderListener {
    default void recorderInitialized(FlightRecorder flightRecorder) {
    }

    default void recordingStateChanged(Recording recording) {
    }
}
