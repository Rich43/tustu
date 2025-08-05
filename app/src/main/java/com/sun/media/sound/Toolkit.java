package com.sun.media.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/* loaded from: rt.jar:com/sun/media/sound/Toolkit.class */
public final class Toolkit {
    private Toolkit() {
    }

    static void getUnsigned8(byte[] bArr, int i2, int i3) {
        for (int i4 = i2; i4 < i2 + i3; i4++) {
            int i5 = i4;
            bArr[i5] = (byte) (bArr[i5] + 128);
        }
    }

    static void getByteSwapped(byte[] bArr, int i2, int i3) {
        for (int i4 = i2; i4 < i2 + i3; i4 += 2) {
            byte b2 = bArr[i4];
            bArr[i4] = bArr[i4 + 1];
            bArr[i4 + 1] = b2;
        }
    }

    static float linearToDB(float f2) {
        return (float) ((Math.log(((double) f2) == 0.0d ? 1.0E-4d : f2) / Math.log(10.0d)) * 20.0d);
    }

    static float dBToLinear(float f2) {
        return (float) Math.pow(10.0d, f2 / 20.0d);
    }

    static long align(long j2, int i2) {
        if (i2 <= 1) {
            return j2;
        }
        return j2 - (j2 % i2);
    }

    static int align(int i2, int i3) {
        if (i3 <= 1) {
            return i2;
        }
        return i2 - (i2 % i3);
    }

    static long millis2bytes(AudioFormat audioFormat, long j2) {
        return align((long) (((j2 * audioFormat.getFrameRate()) / 1000.0f) * audioFormat.getFrameSize()), audioFormat.getFrameSize());
    }

    static long bytes2millis(AudioFormat audioFormat, long j2) {
        return (long) (((j2 / audioFormat.getFrameRate()) * 1000.0f) / audioFormat.getFrameSize());
    }

    static long micros2bytes(AudioFormat audioFormat, long j2) {
        return align((long) (((j2 * audioFormat.getFrameRate()) / 1000000.0f) * audioFormat.getFrameSize()), audioFormat.getFrameSize());
    }

    static long bytes2micros(AudioFormat audioFormat, long j2) {
        return (long) (((j2 / audioFormat.getFrameRate()) * 1000000.0f) / audioFormat.getFrameSize());
    }

    static long micros2frames(AudioFormat audioFormat, long j2) {
        return (long) ((j2 * audioFormat.getFrameRate()) / 1000000.0f);
    }

    static long frames2micros(AudioFormat audioFormat, long j2) {
        return (long) ((j2 / audioFormat.getFrameRate()) * 1000000.0d);
    }

    static void isFullySpecifiedAudioFormat(AudioFormat audioFormat) {
        if (audioFormat.getFrameSize() <= 0) {
            throw new IllegalArgumentException("invalid frame size: " + (audioFormat.getFrameSize() == -1 ? "NOT_SPECIFIED" : String.valueOf(audioFormat.getFrameSize())));
        }
        if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) || audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED) || audioFormat.getEncoding().equals(AudioFormat.Encoding.ULAW) || audioFormat.getEncoding().equals(AudioFormat.Encoding.ALAW)) {
            if (audioFormat.getFrameRate() <= 0.0f) {
                throw new IllegalArgumentException("invalid frame rate: " + (audioFormat.getFrameRate() == -1.0f ? "NOT_SPECIFIED" : String.valueOf(audioFormat.getFrameRate())));
            }
            if (audioFormat.getSampleRate() <= 0.0f) {
                throw new IllegalArgumentException("invalid sample rate: " + (audioFormat.getSampleRate() == -1.0f ? "NOT_SPECIFIED" : String.valueOf(audioFormat.getSampleRate())));
            }
            if (audioFormat.getSampleSizeInBits() <= 0) {
                throw new IllegalArgumentException("invalid sample size in bits: " + (audioFormat.getSampleSizeInBits() == -1 ? "NOT_SPECIFIED" : String.valueOf(audioFormat.getSampleSizeInBits())));
            }
            if (audioFormat.getChannels() <= 0) {
                throw new IllegalArgumentException("invalid number of channels: " + (audioFormat.getChannels() == -1 ? "NOT_SPECIFIED" : String.valueOf(audioFormat.getChannels())));
            }
        }
    }

    static boolean isFullySpecifiedPCMFormat(AudioFormat audioFormat) {
        if ((!audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && !audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) || audioFormat.getFrameRate() <= 0.0f || audioFormat.getSampleRate() <= 0.0f || audioFormat.getSampleSizeInBits() <= 0 || audioFormat.getFrameSize() <= 0 || audioFormat.getChannels() <= 0) {
            return false;
        }
        return true;
    }

    public static AudioInputStream getPCMConvertedAudioInputStream(AudioInputStream audioInputStream) {
        AudioFormat format = audioInputStream.getFormat();
        if (!format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            try {
                audioInputStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), Platform.isBigEndian()), audioInputStream);
            } catch (Exception e2) {
                audioInputStream = null;
            }
        }
        return audioInputStream;
    }
}
