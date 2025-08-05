package javax.sound.midi.spi;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.sound.midi.Sequence;

/* loaded from: rt.jar:javax/sound/midi/spi/MidiFileWriter.class */
public abstract class MidiFileWriter {
    public abstract int[] getMidiFileTypes();

    public abstract int[] getMidiFileTypes(Sequence sequence);

    public abstract int write(Sequence sequence, int i2, OutputStream outputStream) throws IOException;

    public abstract int write(Sequence sequence, int i2, File file) throws IOException;

    public boolean isFileTypeSupported(int i2) {
        for (int i3 : getMidiFileTypes()) {
            if (i2 == i3) {
                return true;
            }
        }
        return false;
    }

    public boolean isFileTypeSupported(int i2, Sequence sequence) {
        for (int i3 : getMidiFileTypes(sequence)) {
            if (i2 == i3) {
                return true;
            }
        }
        return false;
    }
}
