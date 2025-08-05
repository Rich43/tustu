package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/Receiver.class */
public interface Receiver extends AutoCloseable {
    void send(MidiMessage midiMessage, long j2);

    @Override // java.lang.AutoCloseable
    void close();
}
