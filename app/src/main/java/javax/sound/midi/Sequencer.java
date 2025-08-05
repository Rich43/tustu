package javax.sound.midi;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:javax/sound/midi/Sequencer.class */
public interface Sequencer extends MidiDevice {
    public static final int LOOP_CONTINUOUSLY = -1;

    void setSequence(Sequence sequence) throws InvalidMidiDataException;

    void setSequence(InputStream inputStream) throws InvalidMidiDataException, IOException;

    Sequence getSequence();

    void start();

    void stop();

    boolean isRunning();

    void startRecording();

    void stopRecording();

    boolean isRecording();

    void recordEnable(Track track, int i2);

    void recordDisable(Track track);

    float getTempoInBPM();

    void setTempoInBPM(float f2);

    float getTempoInMPQ();

    void setTempoInMPQ(float f2);

    void setTempoFactor(float f2);

    float getTempoFactor();

    long getTickLength();

    long getTickPosition();

    void setTickPosition(long j2);

    long getMicrosecondLength();

    @Override // javax.sound.midi.MidiDevice
    long getMicrosecondPosition();

    void setMicrosecondPosition(long j2);

    void setMasterSyncMode(SyncMode syncMode);

    SyncMode getMasterSyncMode();

    SyncMode[] getMasterSyncModes();

    void setSlaveSyncMode(SyncMode syncMode);

    SyncMode getSlaveSyncMode();

    SyncMode[] getSlaveSyncModes();

    void setTrackMute(int i2, boolean z2);

    boolean getTrackMute(int i2);

    void setTrackSolo(int i2, boolean z2);

    boolean getTrackSolo(int i2);

    boolean addMetaEventListener(MetaEventListener metaEventListener);

    void removeMetaEventListener(MetaEventListener metaEventListener);

    int[] addControllerEventListener(ControllerEventListener controllerEventListener, int[] iArr);

    int[] removeControllerEventListener(ControllerEventListener controllerEventListener, int[] iArr);

    void setLoopStartPoint(long j2);

    long getLoopStartPoint();

    void setLoopEndPoint(long j2);

    long getLoopEndPoint();

    void setLoopCount(int i2);

    int getLoopCount();

    /* loaded from: rt.jar:javax/sound/midi/Sequencer$SyncMode.class */
    public static class SyncMode {
        private String name;
        public static final SyncMode INTERNAL_CLOCK = new SyncMode("Internal Clock");
        public static final SyncMode MIDI_SYNC = new SyncMode("MIDI Sync");
        public static final SyncMode MIDI_TIME_CODE = new SyncMode("MIDI Time Code");
        public static final SyncMode NO_SYNC = new SyncMode("No Timing");

        protected SyncMode(String str) {
            this.name = str;
        }

        public final boolean equals(Object obj) {
            return super.equals(obj);
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public final String toString() {
            return this.name;
        }
    }
}
