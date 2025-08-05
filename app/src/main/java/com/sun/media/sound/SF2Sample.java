package com.sun.media.sound;

import java.io.InputStream;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/* loaded from: rt.jar:com/sun/media/sound/SF2Sample.class */
public final class SF2Sample extends SoundbankResource {
    String name;
    long startLoop;
    long endLoop;
    long sampleRate;
    int originalPitch;
    byte pitchCorrection;
    int sampleLink;
    int sampleType;
    ModelByteBuffer data;
    ModelByteBuffer data24;

    public SF2Sample(Soundbank soundbank) {
        super(soundbank, null, AudioInputStream.class);
        this.name = "";
        this.startLoop = 0L;
        this.endLoop = 0L;
        this.sampleRate = 44100L;
        this.originalPitch = 60;
        this.pitchCorrection = (byte) 0;
        this.sampleLink = 0;
        this.sampleType = 0;
    }

    public SF2Sample() {
        super(null, null, AudioInputStream.class);
        this.name = "";
        this.startLoop = 0L;
        this.endLoop = 0L;
        this.sampleRate = 44100L;
        this.originalPitch = 60;
        this.pitchCorrection = (byte) 0;
        this.sampleLink = 0;
        this.sampleType = 0;
    }

    @Override // javax.sound.midi.SoundbankResource
    public Object getData() {
        AudioFormat format = getFormat();
        InputStream inputStream = this.data.getInputStream();
        if (inputStream == null) {
            return null;
        }
        return new AudioInputStream(inputStream, format, this.data.capacity());
    }

    public ModelByteBuffer getDataBuffer() {
        return this.data;
    }

    public ModelByteBuffer getData24Buffer() {
        return this.data24;
    }

    public AudioFormat getFormat() {
        return new AudioFormat(this.sampleRate, 16, 1, true, false);
    }

    public void setData(ModelByteBuffer modelByteBuffer) {
        this.data = modelByteBuffer;
    }

    public void setData(byte[] bArr) {
        this.data = new ModelByteBuffer(bArr);
    }

    public void setData(byte[] bArr, int i2, int i3) {
        this.data = new ModelByteBuffer(bArr, i2, i3);
    }

    public void setData24(ModelByteBuffer modelByteBuffer) {
        this.data24 = modelByteBuffer;
    }

    public void setData24(byte[] bArr) {
        this.data24 = new ModelByteBuffer(bArr);
    }

    public void setData24(byte[] bArr, int i2, int i3) {
        this.data24 = new ModelByteBuffer(bArr, i2, i3);
    }

    @Override // javax.sound.midi.SoundbankResource
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public long getEndLoop() {
        return this.endLoop;
    }

    public void setEndLoop(long j2) {
        this.endLoop = j2;
    }

    public int getOriginalPitch() {
        return this.originalPitch;
    }

    public void setOriginalPitch(int i2) {
        this.originalPitch = i2;
    }

    public byte getPitchCorrection() {
        return this.pitchCorrection;
    }

    public void setPitchCorrection(byte b2) {
        this.pitchCorrection = b2;
    }

    public int getSampleLink() {
        return this.sampleLink;
    }

    public void setSampleLink(int i2) {
        this.sampleLink = i2;
    }

    public long getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(long j2) {
        this.sampleRate = j2;
    }

    public int getSampleType() {
        return this.sampleType;
    }

    public void setSampleType(int i2) {
        this.sampleType = i2;
    }

    public long getStartLoop() {
        return this.startLoop;
    }

    public void setStartLoop(long j2) {
        this.startLoop = j2;
    }

    public String toString() {
        return "Sample: " + this.name;
    }
}
