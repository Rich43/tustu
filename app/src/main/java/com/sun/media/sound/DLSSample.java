package com.sun.media.sound;

import java.io.InputStream;
import java.util.Arrays;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/* loaded from: rt.jar:com/sun/media/sound/DLSSample.class */
public final class DLSSample extends SoundbankResource {
    byte[] guid;
    DLSInfo info;
    DLSSampleOptions sampleoptions;
    ModelByteBuffer data;
    AudioFormat format;

    public DLSSample(Soundbank soundbank) {
        super(soundbank, null, AudioInputStream.class);
        this.guid = null;
        this.info = new DLSInfo();
    }

    public DLSSample() {
        super(null, null, AudioInputStream.class);
        this.guid = null;
        this.info = new DLSInfo();
    }

    public DLSInfo getInfo() {
        return this.info;
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

    public AudioFormat getFormat() {
        return this.format;
    }

    public void setFormat(AudioFormat audioFormat) {
        this.format = audioFormat;
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

    @Override // javax.sound.midi.SoundbankResource
    public String getName() {
        return this.info.name;
    }

    public void setName(String str) {
        this.info.name = str;
    }

    public DLSSampleOptions getSampleoptions() {
        return this.sampleoptions;
    }

    public void setSampleoptions(DLSSampleOptions dLSSampleOptions) {
        this.sampleoptions = dLSSampleOptions;
    }

    public String toString() {
        return "Sample: " + this.info.name;
    }

    public byte[] getGuid() {
        if (this.guid == null) {
            return null;
        }
        return Arrays.copyOf(this.guid, this.guid.length);
    }

    public void setGuid(byte[] bArr) {
        this.guid = bArr == null ? null : Arrays.copyOf(bArr, bArr.length);
    }
}
