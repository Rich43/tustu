package javax.sound.sampled.spi;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;

/* loaded from: rt.jar:javax/sound/sampled/spi/AudioFileWriter.class */
public abstract class AudioFileWriter {
    public abstract AudioFileFormat.Type[] getAudioFileTypes();

    public abstract AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream audioInputStream);

    public abstract int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, OutputStream outputStream) throws IOException;

    public abstract int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, File file) throws IOException;

    public boolean isFileTypeSupported(AudioFileFormat.Type type) {
        for (AudioFileFormat.Type type2 : getAudioFileTypes()) {
            if (type.equals(type2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFileTypeSupported(AudioFileFormat.Type type, AudioInputStream audioInputStream) {
        for (AudioFileFormat.Type type2 : getAudioFileTypes(audioInputStream)) {
            if (type.equals(type2)) {
                return true;
            }
        }
        return false;
    }
}
