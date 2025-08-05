package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/Transmitter.class */
public interface Transmitter extends AutoCloseable {
    void setReceiver(Receiver receiver);

    Receiver getReceiver();

    @Override // java.lang.AutoCloseable
    void close();
}
