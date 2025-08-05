package sun.audio;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:sun/audio/AudioStream.class */
public final class AudioStream extends FilterInputStream {
    AudioInputStream ais;
    AudioFormat format;
    MidiFileFormat midiformat;
    InputStream stream;

    public AudioStream(InputStream inputStream) throws IOException {
        super(inputStream);
        this.ais = null;
        this.format = null;
        this.midiformat = null;
        this.stream = null;
        this.stream = inputStream;
        if (!inputStream.markSupported()) {
            this.stream = new BufferedInputStream(inputStream, 1024);
        }
        try {
            this.ais = AudioSystem.getAudioInputStream(this.stream);
            this.format = this.ais.getFormat();
            this.in = this.ais;
        } catch (UnsupportedAudioFileException e2) {
            try {
                this.midiformat = MidiSystem.getMidiFileFormat(this.stream);
            } catch (InvalidMidiDataException e3) {
                throw new IOException("could not create audio stream from input stream");
            }
        }
    }

    public AudioData getData() throws IOException {
        int length = getLength();
        if (length < 1048576) {
            byte[] bArr = new byte[length];
            try {
                this.ais.read(bArr, 0, length);
                return new AudioData(this.format, bArr);
            } catch (IOException e2) {
                throw new IOException("Could not create AudioData Object");
            }
        }
        throw new IOException("could not create AudioData object");
    }

    public int getLength() {
        if (this.ais != null && this.format != null) {
            return (int) (this.ais.getFrameLength() * this.ais.getFormat().getFrameSize());
        }
        if (this.midiformat != null) {
            return this.midiformat.getByteLength();
        }
        return -1;
    }
}
