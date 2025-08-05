package sun.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:sun/audio/AudioData.class */
public final class AudioData {
    private static final AudioFormat DEFAULT_FORMAT = new AudioFormat(AudioFormat.Encoding.ULAW, 8000.0f, 8, 1, 1, 8000.0f, true);
    AudioFormat format;
    byte[] buffer;

    public AudioData(byte[] bArr) {
        this(DEFAULT_FORMAT, bArr);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(bArr));
            this.format = audioInputStream.getFormat();
            audioInputStream.close();
        } catch (IOException e2) {
        } catch (UnsupportedAudioFileException e3) {
        }
    }

    AudioData(AudioFormat audioFormat, byte[] bArr) {
        this.format = audioFormat;
        if (bArr != null) {
            this.buffer = Arrays.copyOf(bArr, bArr.length);
        }
    }
}
