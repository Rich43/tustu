package javax.sound.midi.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.Sequence;

/* loaded from: rt.jar:javax/sound/midi/spi/MidiFileReader.class */
public abstract class MidiFileReader {
    public abstract MidiFileFormat getMidiFileFormat(InputStream inputStream) throws InvalidMidiDataException, IOException;

    public abstract MidiFileFormat getMidiFileFormat(URL url) throws InvalidMidiDataException, IOException;

    public abstract MidiFileFormat getMidiFileFormat(File file) throws InvalidMidiDataException, IOException;

    public abstract Sequence getSequence(InputStream inputStream) throws InvalidMidiDataException, IOException;

    public abstract Sequence getSequence(URL url) throws InvalidMidiDataException, IOException;

    public abstract Sequence getSequence(File file) throws InvalidMidiDataException, IOException;
}
