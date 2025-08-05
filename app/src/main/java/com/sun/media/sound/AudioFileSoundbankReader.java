package com.sun.media.sound;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.spi.SoundbankReader;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:com/sun/media/sound/AudioFileSoundbankReader.class */
public final class AudioFileSoundbankReader extends SoundbankReader {
    @Override // javax.sound.midi.spi.SoundbankReader
    public Soundbank getSoundbank(URL url) throws InvalidMidiDataException, IOException {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Soundbank soundbank = getSoundbank(audioInputStream);
            audioInputStream.close();
            return soundbank;
        } catch (IOException e2) {
            return null;
        } catch (UnsupportedAudioFileException e3) {
            return null;
        }
    }

    @Override // javax.sound.midi.spi.SoundbankReader
    public Soundbank getSoundbank(InputStream inputStream) throws InvalidMidiDataException, IOException {
        inputStream.mark(512);
        try {
            Soundbank soundbank = getSoundbank(AudioSystem.getAudioInputStream(inputStream));
            if (soundbank != null) {
                return soundbank;
            }
        } catch (IOException e2) {
        } catch (UnsupportedAudioFileException e3) {
        }
        inputStream.reset();
        return null;
    }

    public Soundbank getSoundbank(AudioInputStream audioInputStream) throws InvalidMidiDataException, IOException {
        byte[] byteArray;
        try {
            int frameSize = audioInputStream.getFormat().getFrameSize();
            if (frameSize <= 0 || frameSize > 1024) {
                throw new InvalidMidiDataException("Formats with frame size " + frameSize + " are not supported");
            }
            long frameLength = audioInputStream.getFrameLength() * frameSize;
            if (frameLength >= 2147483645) {
                throw new InvalidMidiDataException("Can not allocate enough memory to read audio data.");
            }
            if (audioInputStream.getFrameLength() == -1 || frameLength > 1048576) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[65536 - (65536 % frameSize)];
                while (true) {
                    int i2 = audioInputStream.read(bArr);
                    if (i2 == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, i2);
                }
                audioInputStream.close();
                byteArray = byteArrayOutputStream.toByteArray();
            } else {
                byteArray = new byte[(int) frameLength];
                new DataInputStream(audioInputStream).readFully(byteArray);
            }
            ModelByteBufferWavetable modelByteBufferWavetable = new ModelByteBufferWavetable(new ModelByteBuffer(byteArray), audioInputStream.getFormat(), -4800.0f);
            ModelPerformer modelPerformer = new ModelPerformer();
            modelPerformer.getOscillators().add(modelByteBufferWavetable);
            SimpleSoundbank simpleSoundbank = new SimpleSoundbank();
            SimpleInstrument simpleInstrument = new SimpleInstrument();
            simpleInstrument.add(modelPerformer);
            simpleSoundbank.addInstrument(simpleInstrument);
            return simpleSoundbank;
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // javax.sound.midi.spi.SoundbankReader
    public Soundbank getSoundbank(File file) throws InvalidMidiDataException, IOException {
        try {
            AudioSystem.getAudioInputStream(file).close();
            ModelByteBufferWavetable modelByteBufferWavetable = new ModelByteBufferWavetable(new ModelByteBuffer(file, 0L, file.length()), -4800.0f);
            ModelPerformer modelPerformer = new ModelPerformer();
            modelPerformer.getOscillators().add(modelByteBufferWavetable);
            SimpleSoundbank simpleSoundbank = new SimpleSoundbank();
            SimpleInstrument simpleInstrument = new SimpleInstrument();
            simpleInstrument.add(modelPerformer);
            simpleSoundbank.addInstrument(simpleInstrument);
            return simpleSoundbank;
        } catch (IOException e2) {
            return null;
        } catch (UnsupportedAudioFileException e3) {
            return null;
        }
    }
}
