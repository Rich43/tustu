package com.sun.media.sound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.spi.SoundbankReader;

/* loaded from: rt.jar:com/sun/media/sound/SF2SoundbankReader.class */
public final class SF2SoundbankReader extends SoundbankReader {
    @Override // javax.sound.midi.spi.SoundbankReader
    public Soundbank getSoundbank(URL url) throws InvalidMidiDataException, IOException {
        try {
            return new SF2Soundbank(url);
        } catch (RIFFInvalidFormatException e2) {
            return null;
        } catch (IOException e3) {
            return null;
        }
    }

    @Override // javax.sound.midi.spi.SoundbankReader
    public Soundbank getSoundbank(InputStream inputStream) throws InvalidMidiDataException, IOException {
        try {
            inputStream.mark(512);
            return new SF2Soundbank(inputStream);
        } catch (RIFFInvalidFormatException e2) {
            inputStream.reset();
            return null;
        }
    }

    @Override // javax.sound.midi.spi.SoundbankReader
    public Soundbank getSoundbank(File file) throws InvalidMidiDataException, IOException {
        try {
            return new SF2Soundbank(file);
        } catch (RIFFInvalidFormatException e2) {
            return null;
        }
    }
}
