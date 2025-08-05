package javax.sound.sampled;

import java.io.IOException;

/* loaded from: rt.jar:javax/sound/sampled/Clip.class */
public interface Clip extends DataLine {
    public static final int LOOP_CONTINUOUSLY = -1;

    void open(AudioFormat audioFormat, byte[] bArr, int i2, int i3) throws LineUnavailableException;

    void open(AudioInputStream audioInputStream) throws LineUnavailableException, IOException;

    int getFrameLength();

    long getMicrosecondLength();

    void setFramePosition(int i2);

    void setMicrosecondPosition(long j2);

    void setLoopPoints(int i2, int i3);

    void loop(int i2);
}
