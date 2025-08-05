package com.sun.media.sound;

import java.io.IOException;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/* loaded from: rt.jar:com/sun/media/sound/PCMtoPCMCodec.class */
public final class PCMtoPCMCodec extends SunCodec {
    private static final AudioFormat.Encoding[] inputEncodings = {AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED};
    private static final AudioFormat.Encoding[] outputEncodings = {AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED};
    private static final int tempBufferSize = 64;
    private byte[] tempBuffer;

    public PCMtoPCMCodec() {
        super(inputEncodings, outputEncodings);
        this.tempBuffer = null;
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat.Encoding[] getTargetEncodings(AudioFormat audioFormat) {
        if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) || audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            return new AudioFormat.Encoding[]{AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED};
        }
        return new AudioFormat.Encoding[0];
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat[] getTargetFormats(AudioFormat.Encoding encoding, AudioFormat audioFormat) {
        AudioFormat[] outputFormats = getOutputFormats(audioFormat);
        Vector vector = new Vector();
        for (int i2 = 0; i2 < outputFormats.length; i2++) {
            if (outputFormats[i2].getEncoding().equals(encoding)) {
                vector.addElement(outputFormats[i2]);
            }
        }
        AudioFormat[] audioFormatArr = new AudioFormat[vector.size()];
        for (int i3 = 0; i3 < audioFormatArr.length; i3++) {
            audioFormatArr[i3] = (AudioFormat) vector.elementAt(i3);
        }
        return audioFormatArr;
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioInputStream getAudioInputStream(AudioFormat.Encoding encoding, AudioInputStream audioInputStream) {
        if (isConversionSupported(encoding, audioInputStream.getFormat())) {
            AudioFormat format = audioInputStream.getFormat();
            return getAudioInputStream(new AudioFormat(encoding, format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), format.isBigEndian()), audioInputStream);
        }
        throw new IllegalArgumentException("Unsupported conversion: " + audioInputStream.getFormat().toString() + " to " + encoding.toString());
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioInputStream getAudioInputStream(AudioFormat audioFormat, AudioInputStream audioInputStream) {
        return getConvertedStream(audioFormat, audioInputStream);
    }

    private AudioInputStream getConvertedStream(AudioFormat audioFormat, AudioInputStream audioInputStream) {
        AudioInputStream pCMtoPCMCodecStream;
        if (audioInputStream.getFormat().matches(audioFormat)) {
            pCMtoPCMCodecStream = audioInputStream;
        } else {
            pCMtoPCMCodecStream = new PCMtoPCMCodecStream(audioInputStream, audioFormat);
            this.tempBuffer = new byte[64];
        }
        return pCMtoPCMCodecStream;
    }

    private AudioFormat[] getOutputFormats(AudioFormat audioFormat) {
        AudioFormat[] audioFormatArr;
        Vector vector = new Vector();
        int sampleSizeInBits = audioFormat.getSampleSizeInBits();
        boolean zIsBigEndian = audioFormat.isBigEndian();
        if (sampleSizeInBits == 8) {
            if (AudioFormat.Encoding.PCM_SIGNED.equals(audioFormat.getEncoding())) {
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), false));
            }
            if (AudioFormat.Encoding.PCM_UNSIGNED.equals(audioFormat.getEncoding())) {
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), false));
            }
        } else if (sampleSizeInBits == 16) {
            if (AudioFormat.Encoding.PCM_SIGNED.equals(audioFormat.getEncoding()) && zIsBigEndian) {
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), true));
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), false));
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), false));
            }
            if (AudioFormat.Encoding.PCM_UNSIGNED.equals(audioFormat.getEncoding()) && zIsBigEndian) {
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), true));
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), false));
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), false));
            }
            if (AudioFormat.Encoding.PCM_SIGNED.equals(audioFormat.getEncoding()) && !zIsBigEndian) {
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), false));
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), true));
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), true));
            }
            if (AudioFormat.Encoding.PCM_UNSIGNED.equals(audioFormat.getEncoding()) && !zIsBigEndian) {
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), false));
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), true));
                vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), true));
            }
        }
        synchronized (vector) {
            audioFormatArr = new AudioFormat[vector.size()];
            for (int i2 = 0; i2 < audioFormatArr.length; i2++) {
                audioFormatArr[i2] = (AudioFormat) vector.elementAt(i2);
            }
        }
        return audioFormatArr;
    }

    /* loaded from: rt.jar:com/sun/media/sound/PCMtoPCMCodec$PCMtoPCMCodecStream.class */
    class PCMtoPCMCodecStream extends AudioInputStream {
        private final int PCM_SWITCH_SIGNED_8BIT = 1;
        private final int PCM_SWITCH_ENDIAN = 2;
        private final int PCM_SWITCH_SIGNED_LE = 3;
        private final int PCM_SWITCH_SIGNED_BE = 4;
        private final int PCM_UNSIGNED_LE2SIGNED_BE = 5;
        private final int PCM_SIGNED_LE2UNSIGNED_BE = 6;
        private final int PCM_UNSIGNED_BE2SIGNED_LE = 7;
        private final int PCM_SIGNED_BE2UNSIGNED_LE = 8;
        private final int sampleSizeInBytes;
        private int conversionType;

        PCMtoPCMCodecStream(AudioInputStream audioInputStream, AudioFormat audioFormat) {
            super(audioInputStream, audioFormat, -1L);
            this.PCM_SWITCH_SIGNED_8BIT = 1;
            this.PCM_SWITCH_ENDIAN = 2;
            this.PCM_SWITCH_SIGNED_LE = 3;
            this.PCM_SWITCH_SIGNED_BE = 4;
            this.PCM_UNSIGNED_LE2SIGNED_BE = 5;
            this.PCM_SIGNED_LE2UNSIGNED_BE = 6;
            this.PCM_UNSIGNED_BE2SIGNED_LE = 7;
            this.PCM_SIGNED_BE2UNSIGNED_LE = 8;
            this.conversionType = 0;
            AudioFormat format = audioInputStream.getFormat();
            if (!PCMtoPCMCodec.this.isConversionSupported(format, audioFormat)) {
                throw new IllegalArgumentException("Unsupported conversion: " + format.toString() + " to " + audioFormat.toString());
            }
            AudioFormat.Encoding encoding = format.getEncoding();
            AudioFormat.Encoding encoding2 = audioFormat.getEncoding();
            boolean zIsBigEndian = format.isBigEndian();
            boolean zIsBigEndian2 = audioFormat.isBigEndian();
            int sampleSizeInBits = format.getSampleSizeInBits();
            this.sampleSizeInBytes = sampleSizeInBits / 8;
            if (sampleSizeInBits == 8) {
                if (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding) && AudioFormat.Encoding.PCM_SIGNED.equals(encoding2)) {
                    this.conversionType = 1;
                } else if (AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding2)) {
                    this.conversionType = 1;
                }
            } else if (encoding.equals(encoding2) && zIsBigEndian != zIsBigEndian2) {
                this.conversionType = 2;
            } else if (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding) && !zIsBigEndian && AudioFormat.Encoding.PCM_SIGNED.equals(encoding2) && zIsBigEndian2) {
                this.conversionType = 5;
            } else if (AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && !zIsBigEndian && AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding2) && zIsBigEndian2) {
                this.conversionType = 6;
            } else if (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding) && zIsBigEndian && AudioFormat.Encoding.PCM_SIGNED.equals(encoding2) && !zIsBigEndian2) {
                this.conversionType = 7;
            } else if (AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && zIsBigEndian && AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding2) && !zIsBigEndian2) {
                this.conversionType = 8;
            }
            this.frameSize = format.getFrameSize();
            if (this.frameSize == -1) {
                this.frameSize = 1;
            }
            if (audioInputStream instanceof AudioInputStream) {
                this.frameLength = audioInputStream.getFrameLength();
            } else {
                this.frameLength = -1L;
            }
            this.framePos = 0L;
        }

        @Override // javax.sound.sampled.AudioInputStream, java.io.InputStream
        public int read() throws IOException {
            if (this.frameSize == 1) {
                if (this.conversionType == 1) {
                    int i2 = super.read();
                    if (i2 < 0) {
                        return i2;
                    }
                    byte b2 = (byte) (i2 & 15);
                    return (b2 >= 0 ? (byte) (128 | b2) : (byte) (Byte.MAX_VALUE & b2)) & 15;
                }
                throw new IOException("cannot read a single byte if frame size > 1");
            }
            throw new IOException("cannot read a single byte if frame size > 1");
        }

        @Override // javax.sound.sampled.AudioInputStream, java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // javax.sound.sampled.AudioInputStream, java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            if (i3 % this.frameSize != 0) {
                i3 -= i3 % this.frameSize;
            }
            if (this.frameLength != -1 && i3 / this.frameSize > this.frameLength - this.framePos) {
                i3 = ((int) (this.frameLength - this.framePos)) * this.frameSize;
            }
            int i4 = super.read(bArr, i2, i3);
            if (i4 < 0) {
                return i4;
            }
            switch (this.conversionType) {
                case 1:
                    switchSigned8bit(bArr, i2, i3, i4);
                    break;
                case 2:
                    switchEndian(bArr, i2, i3, i4);
                    break;
                case 3:
                    switchSignedLE(bArr, i2, i3, i4);
                    break;
                case 4:
                    switchSignedBE(bArr, i2, i3, i4);
                    break;
                case 5:
                case 6:
                    switchSignedLE(bArr, i2, i3, i4);
                    switchEndian(bArr, i2, i3, i4);
                    break;
                case 7:
                case 8:
                    switchSignedBE(bArr, i2, i3, i4);
                    switchEndian(bArr, i2, i3, i4);
                    break;
            }
            return i4;
        }

        private void switchSigned8bit(byte[] bArr, int i2, int i3, int i4) {
            for (int i5 = i2; i5 < i2 + i4; i5++) {
                bArr[i5] = (byte) (bArr[i5] >= 0 ? 128 | bArr[i5] : Byte.MAX_VALUE & bArr[i5]);
            }
        }

        private void switchSignedBE(byte[] bArr, int i2, int i3, int i4) {
            int i5 = i2;
            while (true) {
                int i6 = i5;
                if (i6 < i2 + i4) {
                    bArr[i6] = (byte) (bArr[i6] >= 0 ? 128 | bArr[i6] : Byte.MAX_VALUE & bArr[i6]);
                    i5 = i6 + this.sampleSizeInBytes;
                } else {
                    return;
                }
            }
        }

        private void switchSignedLE(byte[] bArr, int i2, int i3, int i4) {
            int i5 = (i2 + this.sampleSizeInBytes) - 1;
            while (true) {
                int i6 = i5;
                if (i6 < i2 + i4) {
                    bArr[i6] = (byte) (bArr[i6] >= 0 ? 128 | bArr[i6] : Byte.MAX_VALUE & bArr[i6]);
                    i5 = i6 + this.sampleSizeInBytes;
                } else {
                    return;
                }
            }
        }

        private void switchEndian(byte[] bArr, int i2, int i3, int i4) {
            if (this.sampleSizeInBytes == 2) {
                int i5 = i2;
                while (true) {
                    int i6 = i5;
                    if (i6 < i2 + i4) {
                        byte b2 = bArr[i6];
                        bArr[i6] = bArr[i6 + 1];
                        bArr[i6 + 1] = b2;
                        i5 = i6 + this.sampleSizeInBytes;
                    } else {
                        return;
                    }
                }
            }
        }
    }
}
