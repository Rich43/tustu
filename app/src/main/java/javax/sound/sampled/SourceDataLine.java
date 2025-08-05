package javax.sound.sampled;

/* loaded from: rt.jar:javax/sound/sampled/SourceDataLine.class */
public interface SourceDataLine extends DataLine {
    void open(AudioFormat audioFormat, int i2) throws LineUnavailableException;

    void open(AudioFormat audioFormat) throws LineUnavailableException;

    int write(byte[] bArr, int i2, int i3);
}
