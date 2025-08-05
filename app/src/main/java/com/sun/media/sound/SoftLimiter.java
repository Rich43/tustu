package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftLimiter.class */
public final class SoftLimiter implements SoftAudioProcessor {
    float[] temp_bufferL;
    float[] temp_bufferR;
    SoftAudioBuffer bufferL;
    SoftAudioBuffer bufferR;
    SoftAudioBuffer bufferLout;
    SoftAudioBuffer bufferRout;
    float controlrate;
    float lastmax = 0.0f;
    float gain = 1.0f;
    boolean mix = false;
    double silentcounter = 0.0d;

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void init(float f2, float f3) {
        this.controlrate = f3;
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void setInput(int i2, SoftAudioBuffer softAudioBuffer) {
        if (i2 == 0) {
            this.bufferL = softAudioBuffer;
        }
        if (i2 == 1) {
            this.bufferR = softAudioBuffer;
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void setOutput(int i2, SoftAudioBuffer softAudioBuffer) {
        if (i2 == 0) {
            this.bufferLout = softAudioBuffer;
        }
        if (i2 == 1) {
            this.bufferRout = softAudioBuffer;
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void setMixMode(boolean z2) {
        this.mix = z2;
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void globalParameterControlChange(int[] iArr, long j2, long j3) {
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void processAudio() {
        float f2;
        if (this.bufferL.isSilent() && (this.bufferR == null || this.bufferR.isSilent())) {
            this.silentcounter += 1.0f / this.controlrate;
            if (this.silentcounter > 60.0d) {
                if (!this.mix) {
                    this.bufferLout.clear();
                    if (this.bufferRout != null) {
                        this.bufferRout.clear();
                        return;
                    }
                    return;
                }
                return;
            }
        } else {
            this.silentcounter = 0.0d;
        }
        float[] fArrArray = this.bufferL.array();
        float[] fArrArray2 = this.bufferR == null ? null : this.bufferR.array();
        float[] fArrArray3 = this.bufferLout.array();
        float[] fArrArray4 = this.bufferRout == null ? null : this.bufferRout.array();
        if (this.temp_bufferL == null || this.temp_bufferL.length < fArrArray.length) {
            this.temp_bufferL = new float[fArrArray.length];
        }
        if (fArrArray2 != null && (this.temp_bufferR == null || this.temp_bufferR.length < fArrArray2.length)) {
            this.temp_bufferR = new float[fArrArray2.length];
        }
        float f3 = 0.0f;
        int length = fArrArray.length;
        if (fArrArray2 == null) {
            for (int i2 = 0; i2 < length; i2++) {
                if (fArrArray[i2] > f3) {
                    f3 = fArrArray[i2];
                }
                if ((-fArrArray[i2]) > f3) {
                    f3 = -fArrArray[i2];
                }
            }
        } else {
            for (int i3 = 0; i3 < length; i3++) {
                if (fArrArray[i3] > f3) {
                    f3 = fArrArray[i3];
                }
                if (fArrArray2[i3] > f3) {
                    f3 = fArrArray2[i3];
                }
                if ((-fArrArray[i3]) > f3) {
                    f3 = -fArrArray[i3];
                }
                if ((-fArrArray2[i3]) > f3) {
                    f3 = -fArrArray2[i3];
                }
            }
        }
        float f4 = this.lastmax;
        this.lastmax = f3;
        if (f4 > f3) {
            f3 = f4;
        }
        if (f3 > 0.99f) {
            f2 = 0.99f / f3;
        } else {
            f2 = 1.0f;
        }
        if (f2 > this.gain) {
            f2 = (f2 + (this.gain * 9.0f)) / 10.0f;
        }
        float f5 = (f2 - this.gain) / length;
        if (this.mix) {
            if (fArrArray2 == null) {
                for (int i4 = 0; i4 < length; i4++) {
                    this.gain += f5;
                    float f6 = fArrArray[i4];
                    float f7 = this.temp_bufferL[i4];
                    this.temp_bufferL[i4] = f6;
                    int i5 = i4;
                    fArrArray3[i5] = fArrArray3[i5] + (f7 * this.gain);
                }
            } else {
                for (int i6 = 0; i6 < length; i6++) {
                    this.gain += f5;
                    float f8 = fArrArray[i6];
                    float f9 = fArrArray2[i6];
                    float f10 = this.temp_bufferL[i6];
                    float f11 = this.temp_bufferR[i6];
                    this.temp_bufferL[i6] = f8;
                    this.temp_bufferR[i6] = f9;
                    int i7 = i6;
                    fArrArray3[i7] = fArrArray3[i7] + (f10 * this.gain);
                    int i8 = i6;
                    fArrArray4[i8] = fArrArray4[i8] + (f11 * this.gain);
                }
            }
        } else if (fArrArray2 == null) {
            for (int i9 = 0; i9 < length; i9++) {
                this.gain += f5;
                float f12 = fArrArray[i9];
                float f13 = this.temp_bufferL[i9];
                this.temp_bufferL[i9] = f12;
                fArrArray3[i9] = f13 * this.gain;
            }
        } else {
            for (int i10 = 0; i10 < length; i10++) {
                this.gain += f5;
                float f14 = fArrArray[i10];
                float f15 = fArrArray2[i10];
                float f16 = this.temp_bufferL[i10];
                float f17 = this.temp_bufferR[i10];
                this.temp_bufferL[i10] = f14;
                this.temp_bufferR[i10] = f15;
                fArrArray3[i10] = f16 * this.gain;
                fArrArray4[i10] = f17 * this.gain;
            }
        }
        this.gain = f2;
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void processControlLogic() {
    }
}
