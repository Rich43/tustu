package com.sun.media.sound;

import java.util.Arrays;

/* loaded from: rt.jar:com/sun/media/sound/SoftChorus.class */
public final class SoftChorus implements SoftAudioProcessor {
    private SoftAudioBuffer inputA;
    private SoftAudioBuffer left;
    private SoftAudioBuffer right;
    private SoftAudioBuffer reverb;
    private LFODelay vdelay1L;
    private LFODelay vdelay1R;
    private double dirty_vdelay1L_rate;
    private double dirty_vdelay1R_rate;
    private double dirty_vdelay1L_depth;
    private double dirty_vdelay1R_depth;
    private float dirty_vdelay1L_feedback;
    private float dirty_vdelay1R_feedback;
    private float dirty_vdelay1L_reverbsendgain;
    private float dirty_vdelay1R_reverbsendgain;
    private float controlrate;
    private boolean mix = true;
    private float rgain = 0.0f;
    private boolean dirty = true;
    double silentcounter = 1000.0d;

    /* loaded from: rt.jar:com/sun/media/sound/SoftChorus$VariableDelay.class */
    private static class VariableDelay {
        private final float[] delaybuffer;
        private int rovepos = 0;
        private float gain = 1.0f;
        private float rgain = 0.0f;
        private float delay = 0.0f;
        private float lastdelay = 0.0f;
        private float feedback = 0.0f;

        VariableDelay(int i2) {
            this.delaybuffer = new float[i2];
        }

        public void setDelay(float f2) {
            this.delay = f2;
        }

        public void setFeedBack(float f2) {
            this.feedback = f2;
        }

        public void setGain(float f2) {
            this.gain = f2;
        }

        public void setReverbSendGain(float f2) {
            this.rgain = f2;
        }

        public void processMix(float[] fArr, float[] fArr2, float[] fArr3) {
            float f2 = this.gain;
            float f3 = this.delay;
            float f4 = this.feedback;
            float[] fArr4 = this.delaybuffer;
            int length = fArr.length;
            float f5 = (f3 - this.lastdelay) / length;
            int length2 = fArr4.length;
            int i2 = this.rovepos;
            if (fArr3 == null) {
                for (int i3 = 0; i3 < length; i3++) {
                    float f6 = (i2 - (this.lastdelay + 2.0f)) + length2;
                    int i4 = (int) f6;
                    float f7 = f6 - i4;
                    float f8 = (fArr4[i4 % length2] * (1.0f - f7)) + (fArr4[(i4 + 1) % length2] * f7);
                    int i5 = i3;
                    fArr2[i5] = fArr2[i5] + (f8 * f2);
                    fArr4[i2] = fArr[i3] + (f8 * f4);
                    i2 = (i2 + 1) % length2;
                    this.lastdelay += f5;
                }
            } else {
                for (int i6 = 0; i6 < length; i6++) {
                    float f9 = (i2 - (this.lastdelay + 2.0f)) + length2;
                    int i7 = (int) f9;
                    float f10 = f9 - i7;
                    float f11 = (fArr4[i7 % length2] * (1.0f - f10)) + (fArr4[(i7 + 1) % length2] * f10);
                    int i8 = i6;
                    fArr2[i8] = fArr2[i8] + (f11 * f2);
                    int i9 = i6;
                    fArr3[i9] = fArr3[i9] + (f11 * this.rgain);
                    fArr4[i2] = fArr[i6] + (f11 * f4);
                    i2 = (i2 + 1) % length2;
                    this.lastdelay += f5;
                }
            }
            this.rovepos = i2;
            this.lastdelay = f3;
        }

        public void processReplace(float[] fArr, float[] fArr2, float[] fArr3) {
            Arrays.fill(fArr2, 0.0f);
            Arrays.fill(fArr3, 0.0f);
            processMix(fArr, fArr2, fArr3);
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftChorus$LFODelay.class */
    private static class LFODelay {
        private double phase = 1.0d;
        private double phase_step = 0.0d;
        private double depth = 0.0d;
        private VariableDelay vdelay = new VariableDelay((int) ((this.depth + 10.0d) * 2.0d));
        private final double samplerate;
        private final double controlrate;

        LFODelay(double d2, double d3) {
            this.samplerate = d2;
            this.controlrate = d3;
        }

        public void setDepth(double d2) {
            this.depth = d2 * this.samplerate;
            this.vdelay = new VariableDelay((int) ((this.depth + 10.0d) * 2.0d));
        }

        public void setRate(double d2) {
            this.phase_step = 6.283185307179586d * (d2 / this.controlrate);
        }

        public void setPhase(double d2) {
            this.phase = d2;
        }

        public void setFeedBack(float f2) {
            this.vdelay.setFeedBack(f2);
        }

        public void setGain(float f2) {
            this.vdelay.setGain(f2);
        }

        public void setReverbSendGain(float f2) {
            this.vdelay.setReverbSendGain(f2);
        }

        public void processMix(float[] fArr, float[] fArr2, float[] fArr3) {
            this.phase += this.phase_step;
            while (this.phase > 6.283185307179586d) {
                this.phase -= 6.283185307179586d;
            }
            this.vdelay.setDelay((float) (this.depth * 0.5d * (Math.cos(this.phase) + 2.0d)));
            this.vdelay.processMix(fArr, fArr2, fArr3);
        }

        public void processReplace(float[] fArr, float[] fArr2, float[] fArr3) {
            this.phase += this.phase_step;
            while (this.phase > 6.283185307179586d) {
                this.phase -= 6.283185307179586d;
            }
            this.vdelay.setDelay((float) (this.depth * 0.5d * (Math.cos(this.phase) + 2.0d)));
            this.vdelay.processReplace(fArr, fArr2, fArr3);
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void init(float f2, float f3) {
        this.controlrate = f3;
        this.vdelay1L = new LFODelay(f2, f3);
        this.vdelay1R = new LFODelay(f2, f3);
        this.vdelay1L.setGain(1.0f);
        this.vdelay1R.setGain(1.0f);
        this.vdelay1L.setPhase(1.5707963267948966d);
        this.vdelay1R.setPhase(0.0d);
        globalParameterControlChange(new int[]{130}, 0L, 2L);
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void globalParameterControlChange(int[] iArr, long j2, long j3) {
        if (iArr.length == 1 && iArr[0] == 130) {
            if (j2 == 0) {
                switch ((int) j3) {
                    case 0:
                        globalParameterControlChange(iArr, 3L, 0L);
                        globalParameterControlChange(iArr, 1L, 3L);
                        globalParameterControlChange(iArr, 2L, 5L);
                        globalParameterControlChange(iArr, 4L, 0L);
                        break;
                    case 1:
                        globalParameterControlChange(iArr, 3L, 5L);
                        globalParameterControlChange(iArr, 1L, 9L);
                        globalParameterControlChange(iArr, 2L, 19L);
                        globalParameterControlChange(iArr, 4L, 0L);
                        break;
                    case 2:
                        globalParameterControlChange(iArr, 3L, 8L);
                        globalParameterControlChange(iArr, 1L, 3L);
                        globalParameterControlChange(iArr, 2L, 19L);
                        globalParameterControlChange(iArr, 4L, 0L);
                        break;
                    case 3:
                        globalParameterControlChange(iArr, 3L, 16L);
                        globalParameterControlChange(iArr, 1L, 9L);
                        globalParameterControlChange(iArr, 2L, 16L);
                        globalParameterControlChange(iArr, 4L, 0L);
                        break;
                    case 4:
                        globalParameterControlChange(iArr, 3L, 64L);
                        globalParameterControlChange(iArr, 1L, 2L);
                        globalParameterControlChange(iArr, 2L, 24L);
                        globalParameterControlChange(iArr, 4L, 0L);
                        break;
                    case 5:
                        globalParameterControlChange(iArr, 3L, 112L);
                        globalParameterControlChange(iArr, 1L, 1L);
                        globalParameterControlChange(iArr, 2L, 5L);
                        globalParameterControlChange(iArr, 4L, 0L);
                        break;
                }
            } else if (j2 == 1) {
                this.dirty_vdelay1L_rate = j3 * 0.122d;
                this.dirty_vdelay1R_rate = j3 * 0.122d;
                this.dirty = true;
            } else if (j2 == 2) {
                this.dirty_vdelay1L_depth = (j3 + 1) / 3200.0d;
                this.dirty_vdelay1R_depth = (j3 + 1) / 3200.0d;
                this.dirty = true;
            } else if (j2 == 3) {
                this.dirty_vdelay1L_feedback = j3 * 0.00763f;
                this.dirty_vdelay1R_feedback = j3 * 0.00763f;
                this.dirty = true;
            }
            if (j2 == 4) {
                this.rgain = j3 * 0.00787f;
                this.dirty_vdelay1L_reverbsendgain = j3 * 0.00787f;
                this.dirty_vdelay1R_reverbsendgain = j3 * 0.00787f;
                this.dirty = true;
            }
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void processControlLogic() {
        if (this.dirty) {
            this.dirty = false;
            this.vdelay1L.setRate(this.dirty_vdelay1L_rate);
            this.vdelay1R.setRate(this.dirty_vdelay1R_rate);
            this.vdelay1L.setDepth(this.dirty_vdelay1L_depth);
            this.vdelay1R.setDepth(this.dirty_vdelay1R_depth);
            this.vdelay1L.setFeedBack(this.dirty_vdelay1L_feedback);
            this.vdelay1R.setFeedBack(this.dirty_vdelay1R_feedback);
            this.vdelay1L.setReverbSendGain(this.dirty_vdelay1L_reverbsendgain);
            this.vdelay1R.setReverbSendGain(this.dirty_vdelay1R_reverbsendgain);
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void processAudio() {
        if (this.inputA.isSilent()) {
            this.silentcounter += 1.0f / this.controlrate;
            if (this.silentcounter > 1.0d) {
                if (!this.mix) {
                    this.left.clear();
                    this.right.clear();
                    return;
                }
                return;
            }
        } else {
            this.silentcounter = 0.0d;
        }
        float[] fArrArray = this.inputA.array();
        float[] fArrArray2 = this.left.array();
        float[] fArrArray3 = this.right == null ? null : this.right.array();
        float[] fArrArray4 = this.rgain != 0.0f ? this.reverb.array() : null;
        if (this.mix) {
            this.vdelay1L.processMix(fArrArray, fArrArray2, fArrArray4);
            if (fArrArray3 != null) {
                this.vdelay1R.processMix(fArrArray, fArrArray3, fArrArray4);
                return;
            }
            return;
        }
        this.vdelay1L.processReplace(fArrArray, fArrArray2, fArrArray4);
        if (fArrArray3 != null) {
            this.vdelay1R.processReplace(fArrArray, fArrArray3, fArrArray4);
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void setInput(int i2, SoftAudioBuffer softAudioBuffer) {
        if (i2 == 0) {
            this.inputA = softAudioBuffer;
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void setMixMode(boolean z2) {
        this.mix = z2;
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void setOutput(int i2, SoftAudioBuffer softAudioBuffer) {
        if (i2 == 0) {
            this.left = softAudioBuffer;
        }
        if (i2 == 1) {
            this.right = softAudioBuffer;
        }
        if (i2 == 2) {
            this.reverb = softAudioBuffer;
        }
    }
}
