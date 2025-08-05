package javax.sound.sampled.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:javax/sound/sampled/spi/AudioFileReader.class */
public abstract class AudioFileReader {
    public abstract AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException;

    public abstract AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException;

    public abstract AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException;

    public abstract AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException;

    public abstract AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException;

    public abstract AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException;
}
