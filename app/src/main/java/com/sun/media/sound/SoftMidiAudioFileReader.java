package com.sun.media.sound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

/* loaded from: rt.jar:com/sun/media/sound/SoftMidiAudioFileReader.class */
public final class SoftMidiAudioFileReader extends AudioFileReader {
    public static final AudioFileFormat.Type MIDI = new AudioFileFormat.Type("MIDI", "mid");
    private static AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);

    public AudioFileFormat getAudioFileFormat(Sequence sequence) throws UnsupportedAudioFileException, IOException {
        return new AudioFileFormat(MIDI, format, (int) (format.getFrameRate() * ((sequence.getMicrosecondLength() / 1000000) + 4)));
    }

    public AudioInputStream getAudioInputStream(Sequence sequence) throws UnsupportedAudioFileException, IOException, ArrayIndexOutOfBoundsException {
        SoftSynthesizer softSynthesizer = new SoftSynthesizer();
        try {
            AudioInputStream audioInputStreamOpenStream = softSynthesizer.openStream(format, null);
            Receiver receiver = softSynthesizer.getReceiver();
            float divisionType = sequence.getDivisionType();
            Track[] tracks = sequence.getTracks();
            int[] iArr = new int[tracks.length];
            int i2 = 500000;
            int resolution = sequence.getResolution();
            long j2 = 0;
            long j3 = 0;
            while (true) {
                MidiEvent midiEvent = null;
                int i3 = -1;
                for (int i4 = 0; i4 < tracks.length; i4++) {
                    int i5 = iArr[i4];
                    Track track = tracks[i4];
                    if (i5 < track.size()) {
                        MidiEvent midiEvent2 = track.get(i5);
                        if (midiEvent == null || midiEvent2.getTick() < midiEvent.getTick()) {
                            midiEvent = midiEvent2;
                            i3 = i4;
                        }
                    }
                }
                if (i3 != -1) {
                    int i6 = i3;
                    iArr[i6] = iArr[i6] + 1;
                    long tick = midiEvent.getTick();
                    if (divisionType == 0.0f) {
                        j3 += ((tick - j2) * i2) / resolution;
                    } else {
                        j3 = (long) (((tick * 1000000.0d) * divisionType) / resolution);
                    }
                    j2 = tick;
                    MidiMessage message = midiEvent.getMessage();
                    if (message instanceof MetaMessage) {
                        if (divisionType == 0.0f && ((MetaMessage) message).getType() == 81) {
                            byte[] data = ((MetaMessage) message).getData();
                            if (data.length < 3) {
                                throw new UnsupportedAudioFileException();
                            }
                            i2 = ((data[0] & 255) << 16) | ((data[1] & 255) << 8) | (data[2] & 255);
                        }
                    } else {
                        receiver.send(message, j3);
                    }
                } else {
                    return new AudioInputStream(audioInputStreamOpenStream, audioInputStreamOpenStream.getFormat(), (long) (audioInputStreamOpenStream.getFormat().getFrameRate() * ((j3 / 1000000) + 4)));
                }
            }
        } catch (MidiUnavailableException e2) {
            throw new IOException(e2.toString());
        }
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        inputStream.mark(200);
        try {
            return getAudioInputStream(MidiSystem.getSequence(inputStream));
        } catch (IOException e2) {
            inputStream.reset();
            throw new UnsupportedAudioFileException();
        } catch (InvalidMidiDataException e3) {
            inputStream.reset();
            throw new UnsupportedAudioFileException();
        }
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        try {
            return getAudioFileFormat(MidiSystem.getSequence(url));
        } catch (IOException e2) {
            throw new UnsupportedAudioFileException();
        } catch (InvalidMidiDataException e3) {
            throw new UnsupportedAudioFileException();
        }
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        try {
            return getAudioFileFormat(MidiSystem.getSequence(file));
        } catch (IOException e2) {
            throw new UnsupportedAudioFileException();
        } catch (InvalidMidiDataException e3) {
            throw new UnsupportedAudioFileException();
        }
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        try {
            return getAudioInputStream(MidiSystem.getSequence(url));
        } catch (IOException e2) {
            throw new UnsupportedAudioFileException();
        } catch (InvalidMidiDataException e3) {
            throw new UnsupportedAudioFileException();
        }
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        if (!file.getName().toLowerCase().endsWith(".mid")) {
            throw new UnsupportedAudioFileException();
        }
        try {
            return getAudioInputStream(MidiSystem.getSequence(file));
        } catch (IOException e2) {
            throw new UnsupportedAudioFileException();
        } catch (InvalidMidiDataException e3) {
            throw new UnsupportedAudioFileException();
        }
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        inputStream.mark(200);
        try {
            return getAudioFileFormat(MidiSystem.getSequence(inputStream));
        } catch (IOException e2) {
            inputStream.reset();
            throw new UnsupportedAudioFileException();
        } catch (InvalidMidiDataException e3) {
            inputStream.reset();
            throw new UnsupportedAudioFileException();
        }
    }
}
