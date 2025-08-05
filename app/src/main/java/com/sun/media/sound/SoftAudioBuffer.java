package com.sun.media.sound;

import java.util.Arrays;
import javax.sound.sampled.AudioFormat;

/* loaded from: rt.jar:com/sun/media/sound/SoftAudioBuffer.class */
public final class SoftAudioBuffer {
    private int size;
    private float[] buffer;
    private boolean empty = true;
    private AudioFormat format;
    private AudioFloatConverter converter;
    private byte[] converter_buffer;

    public SoftAudioBuffer(int i2, AudioFormat audioFormat) {
        this.size = i2;
        this.format = audioFormat;
        this.converter = AudioFloatConverter.getConverter(audioFormat);
    }

    public void swap(SoftAudioBuffer softAudioBuffer) {
        int i2 = this.size;
        float[] fArr = this.buffer;
        boolean z2 = this.empty;
        AudioFormat audioFormat = this.format;
        AudioFloatConverter audioFloatConverter = this.converter;
        byte[] bArr = this.converter_buffer;
        this.size = softAudioBuffer.size;
        this.buffer = softAudioBuffer.buffer;
        this.empty = softAudioBuffer.empty;
        this.format = softAudioBuffer.format;
        this.converter = softAudioBuffer.converter;
        this.converter_buffer = softAudioBuffer.converter_buffer;
        softAudioBuffer.size = i2;
        softAudioBuffer.buffer = fArr;
        softAudioBuffer.empty = z2;
        softAudioBuffer.format = audioFormat;
        softAudioBuffer.converter = audioFloatConverter;
        softAudioBuffer.converter_buffer = bArr;
    }

    public AudioFormat getFormat() {
        return this.format;
    }

    public int getSize() {
        return this.size;
    }

    public void clear() {
        if (!this.empty) {
            Arrays.fill(this.buffer, 0.0f);
            this.empty = true;
        }
    }

    public boolean isSilent() {
        return this.empty;
    }

    public float[] array() {
        this.empty = false;
        if (this.buffer == null) {
            this.buffer = new float[this.size];
        }
        return this.buffer;
    }

    public void get(byte[] bArr, int i2) {
        int frameSize = this.format.getFrameSize() / this.format.getChannels();
        int i3 = this.size * frameSize;
        if (this.converter_buffer == null || this.converter_buffer.length < i3) {
            this.converter_buffer = new byte[i3];
        }
        if (this.format.getChannels() == 1) {
            this.converter.toByteArray(array(), this.size, bArr);
            return;
        }
        this.converter.toByteArray(array(), this.size, this.converter_buffer);
        if (i2 >= this.format.getChannels()) {
            return;
        }
        int channels = this.format.getChannels() * frameSize;
        for (int i4 = 0; i4 < frameSize; i4++) {
            int i5 = i4;
            int i6 = (i2 * frameSize) + i4;
            for (int i7 = 0; i7 < this.size; i7++) {
                bArr[i6] = this.converter_buffer[i5];
                i6 += channels;
                i5 += frameSize;
            }
        }
    }
}
