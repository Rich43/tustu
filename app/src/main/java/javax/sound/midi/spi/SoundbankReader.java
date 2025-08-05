package javax.sound.midi.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Soundbank;

/* loaded from: rt.jar:javax/sound/midi/spi/SoundbankReader.class */
public abstract class SoundbankReader {
    public abstract Soundbank getSoundbank(URL url) throws InvalidMidiDataException, IOException;

    public abstract Soundbank getSoundbank(InputStream inputStream) throws InvalidMidiDataException, IOException;

    public abstract Soundbank getSoundbank(File file) throws InvalidMidiDataException, IOException;
}
