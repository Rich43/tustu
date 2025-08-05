package com.sun.media.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.spi.FormatConversionProvider;

/* loaded from: rt.jar:com/sun/media/sound/SunCodec.class */
abstract class SunCodec extends FormatConversionProvider {
    private final AudioFormat.Encoding[] inputEncodings;
    private final AudioFormat.Encoding[] outputEncodings;

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public abstract AudioFormat.Encoding[] getTargetEncodings(AudioFormat audioFormat);

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public abstract AudioFormat[] getTargetFormats(AudioFormat.Encoding encoding, AudioFormat audioFormat);

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public abstract AudioInputStream getAudioInputStream(AudioFormat.Encoding encoding, AudioInputStream audioInputStream);

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public abstract AudioInputStream getAudioInputStream(AudioFormat audioFormat, AudioInputStream audioInputStream);

    SunCodec(AudioFormat.Encoding[] encodingArr, AudioFormat.Encoding[] encodingArr2) {
        this.inputEncodings = encodingArr;
        this.outputEncodings = encodingArr2;
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public final AudioFormat.Encoding[] getSourceEncodings() {
        AudioFormat.Encoding[] encodingArr = new AudioFormat.Encoding[this.inputEncodings.length];
        System.arraycopy(this.inputEncodings, 0, encodingArr, 0, this.inputEncodings.length);
        return encodingArr;
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public final AudioFormat.Encoding[] getTargetEncodings() {
        AudioFormat.Encoding[] encodingArr = new AudioFormat.Encoding[this.outputEncodings.length];
        System.arraycopy(this.outputEncodings, 0, encodingArr, 0, this.outputEncodings.length);
        return encodingArr;
    }
}
