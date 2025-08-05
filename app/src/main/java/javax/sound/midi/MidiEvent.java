package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/MidiEvent.class */
public class MidiEvent {
    private final MidiMessage message;
    private long tick;

    public MidiEvent(MidiMessage midiMessage, long j2) {
        this.message = midiMessage;
        this.tick = j2;
    }

    public MidiMessage getMessage() {
        return this.message;
    }

    public void setTick(long j2) {
        this.tick = j2;
    }

    public long getTick() {
        return this.tick;
    }
}
