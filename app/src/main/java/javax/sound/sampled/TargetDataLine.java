package javax.sound.sampled;

/* loaded from: rt.jar:javax/sound/sampled/TargetDataLine.class */
public interface TargetDataLine extends DataLine {
    void open(AudioFormat audioFormat, int i2) throws LineUnavailableException;

    void open(AudioFormat audioFormat) throws LineUnavailableException;

    int read(byte[] bArr, int i2, int i3);
}
