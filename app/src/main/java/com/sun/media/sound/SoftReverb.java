package com.sun.media.sound;

import java.util.Arrays;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: rt.jar:com/sun/media/sound/SoftReverb.class */
public final class SoftReverb implements SoftAudioProcessor {
    private float roomsize;
    private float damp;
    private Delay delay;
    private Comb[] combL;
    private Comb[] combR;
    private AllPass[] allpassL;
    private AllPass[] allpassR;
    private float[] input;
    private float[] out;
    private float[] pre1;
    private float[] pre2;
    private float[] pre3;
    private SoftAudioBuffer inputA;
    private SoftAudioBuffer left;
    private SoftAudioBuffer right;
    private float dirty_roomsize;
    private float dirty_damp;
    private float dirty_predelay;
    private float dirty_gain;
    private float samplerate;
    private float gain = 1.0f;
    private boolean denormal_flip = false;
    private boolean mix = true;
    private boolean dirty = true;
    private boolean light = true;
    private boolean silent = true;

    /* loaded from: rt.jar:com/sun/media/sound/SoftReverb$Delay.class */
    private static final class Delay {
        private int rovepos = 0;
        private float[] delaybuffer = null;

        Delay() {
        }

        public void setDelay(int i2) {
            if (i2 == 0) {
                this.delaybuffer = null;
            } else {
                this.delaybuffer = new float[i2];
            }
            this.rovepos = 0;
        }

        public void processReplace(float[] fArr) {
            if (this.delaybuffer == null) {
                return;
            }
            int length = fArr.length;
            int length2 = this.delaybuffer.length;
            int i2 = this.rovepos;
            for (int i3 = 0; i3 < length; i3++) {
                float f2 = fArr[i3];
                fArr[i3] = this.delaybuffer[i2];
                this.delaybuffer[i2] = f2;
                i2++;
                if (i2 == length2) {
                    i2 = 0;
                }
            }
            this.rovepos = i2;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftReverb$AllPass.class */
    private static final class AllPass {
        private final float[] delaybuffer;
        private final int delaybuffersize;
        private int rovepos = 0;
        private float feedback;

        AllPass(int i2) {
            this.delaybuffer = new float[i2];
            this.delaybuffersize = i2;
        }

        public void setFeedBack(float f2) {
            this.feedback = f2;
        }

        public void processReplace(float[] fArr) {
            int length = fArr.length;
            int i2 = this.delaybuffersize;
            int i3 = this.rovepos;
            for (int i4 = 0; i4 < length; i4++) {
                float f2 = this.delaybuffer[i3];
                float f3 = fArr[i4];
                fArr[i4] = f2 - f3;
                this.delaybuffer[i3] = f3 + (f2 * this.feedback);
                i3++;
                if (i3 == i2) {
                    i3 = 0;
                }
            }
            this.rovepos = i3;
        }

        public void processReplace(float[] fArr, float[] fArr2) {
            int length = fArr.length;
            int i2 = this.delaybuffersize;
            int i3 = this.rovepos;
            for (int i4 = 0; i4 < length; i4++) {
                float f2 = this.delaybuffer[i3];
                float f3 = fArr[i4];
                fArr2[i4] = f2 - f3;
                this.delaybuffer[i3] = f3 + (f2 * this.feedback);
                i3++;
                if (i3 == i2) {
                    i3 = 0;
                }
            }
            this.rovepos = i3;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftReverb$Comb.class */
    private static final class Comb {
        private final float[] delaybuffer;
        private final int delaybuffersize;
        private float feedback;
        private int rovepos = 0;
        private float filtertemp = 0.0f;
        private float filtercoeff1 = 0.0f;
        private float filtercoeff2 = 1.0f;

        Comb(int i2) {
            this.delaybuffer = new float[i2];
            this.delaybuffersize = i2;
        }

        public void setFeedBack(float f2) {
            this.feedback = f2;
            this.filtercoeff2 = (1.0f - this.filtercoeff1) * f2;
        }

        public void processMix(float[] fArr, float[] fArr2) {
            int length = fArr.length;
            int i2 = this.delaybuffersize;
            int i3 = this.rovepos;
            float f2 = this.filtertemp;
            float f3 = this.filtercoeff1;
            float f4 = this.filtercoeff2;
            for (int i4 = 0; i4 < length; i4++) {
                float f5 = this.delaybuffer[i3];
                f2 = (f5 * f4) + (f2 * f3);
                int i5 = i4;
                fArr2[i5] = fArr2[i5] + f5;
                this.delaybuffer[i3] = fArr[i4] + f2;
                i3++;
                if (i3 == i2) {
                    i3 = 0;
                }
            }
            this.filtertemp = f2;
            this.rovepos = i3;
        }

        public void processReplace(float[] fArr, float[] fArr2) {
            int length = fArr.length;
            int i2 = this.delaybuffersize;
            int i3 = this.rovepos;
            float f2 = this.filtertemp;
            float f3 = this.filtercoeff1;
            float f4 = this.filtercoeff2;
            for (int i4 = 0; i4 < length; i4++) {
                float f5 = this.delaybuffer[i3];
                f2 = (f5 * f4) + (f2 * f3);
                fArr2[i4] = f5;
                this.delaybuffer[i3] = fArr[i4] + f2;
                i3++;
                if (i3 == i2) {
                    i3 = 0;
                }
            }
            this.filtertemp = f2;
            this.rovepos = i3;
        }

        public void setDamp(float f2) {
            this.filtercoeff1 = f2;
            this.filtercoeff2 = (1.0f - this.filtercoeff1) * this.feedback;
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void init(float f2, float f3) {
        this.samplerate = f2;
        double d2 = f2 / 44100.0d;
        this.delay = new Delay();
        this.combL = new Comb[8];
        this.combR = new Comb[8];
        this.combL[0] = new Comb((int) (d2 * 1116.0d));
        this.combR[0] = new Comb((int) (d2 * (1116 + 23)));
        this.combL[1] = new Comb((int) (d2 * 1188.0d));
        this.combR[1] = new Comb((int) (d2 * (1188 + 23)));
        this.combL[2] = new Comb((int) (d2 * 1277.0d));
        this.combR[2] = new Comb((int) (d2 * (1277 + 23)));
        this.combL[3] = new Comb((int) (d2 * 1356.0d));
        this.combR[3] = new Comb((int) (d2 * (1356 + 23)));
        this.combL[4] = new Comb((int) (d2 * 1422.0d));
        this.combR[4] = new Comb((int) (d2 * (1422 + 23)));
        this.combL[5] = new Comb((int) (d2 * 1491.0d));
        this.combR[5] = new Comb((int) (d2 * (1491 + 23)));
        this.combL[6] = new Comb((int) (d2 * 1557.0d));
        this.combR[6] = new Comb((int) (d2 * (1557 + 23)));
        this.combL[7] = new Comb((int) (d2 * 1617.0d));
        this.combR[7] = new Comb((int) (d2 * (1617 + 23)));
        this.allpassL = new AllPass[4];
        this.allpassR = new AllPass[4];
        this.allpassL[0] = new AllPass((int) (d2 * 556.0d));
        this.allpassR[0] = new AllPass((int) (d2 * (556 + 23)));
        this.allpassL[1] = new AllPass((int) (d2 * 441.0d));
        this.allpassR[1] = new AllPass((int) (d2 * (NNTPReply.POSTING_FAILED + 23)));
        this.allpassL[2] = new AllPass((int) (d2 * 341.0d));
        this.allpassR[2] = new AllPass((int) (d2 * (341 + 23)));
        this.allpassL[3] = new AllPass((int) (d2 * 225.0d));
        this.allpassR[3] = new AllPass((int) (d2 * (225 + 23)));
        for (int i2 = 0; i2 < this.allpassL.length; i2++) {
            this.allpassL[i2].setFeedBack(0.5f);
            this.allpassR[i2].setFeedBack(0.5f);
        }
        globalParameterControlChange(new int[]{129}, 0L, 4L);
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void setInput(int i2, SoftAudioBuffer softAudioBuffer) {
        if (i2 == 0) {
            this.inputA = softAudioBuffer;
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void setOutput(int i2, SoftAudioBuffer softAudioBuffer) {
        if (i2 == 0) {
            this.left = softAudioBuffer;
        }
        if (i2 == 1) {
            this.right = softAudioBuffer;
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void setMixMode(boolean z2) {
        this.mix = z2;
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void processAudio() {
        boolean zIsSilent = this.inputA.isSilent();
        if (!zIsSilent) {
            this.silent = false;
        }
        if (this.silent) {
            if (!this.mix) {
                this.left.clear();
                this.right.clear();
                return;
            }
            return;
        }
        float[] fArrArray = this.inputA.array();
        float[] fArrArray2 = this.left.array();
        float[] fArrArray3 = this.right == null ? null : this.right.array();
        int length = fArrArray.length;
        if (this.input == null || this.input.length < length) {
            this.input = new float[length];
        }
        float f2 = (this.gain * 0.018f) / 2.0f;
        this.denormal_flip = !this.denormal_flip;
        if (this.denormal_flip) {
            for (int i2 = 0; i2 < length; i2++) {
                this.input[i2] = (fArrArray[i2] * f2) + 1.0E-20f;
            }
        } else {
            for (int i3 = 0; i3 < length; i3++) {
                this.input[i3] = (fArrArray[i3] * f2) - 1.0E-20f;
            }
        }
        this.delay.processReplace(this.input);
        if (this.light && fArrArray3 != null) {
            if (this.pre1 == null || this.pre1.length < length) {
                this.pre1 = new float[length];
                this.pre2 = new float[length];
                this.pre3 = new float[length];
            }
            for (int i4 = 0; i4 < this.allpassL.length; i4++) {
                this.allpassL[i4].processReplace(this.input);
            }
            this.combL[0].processReplace(this.input, this.pre3);
            this.combL[1].processReplace(this.input, this.pre3);
            this.combL[2].processReplace(this.input, this.pre1);
            for (int i5 = 4; i5 < this.combL.length - 2; i5 += 2) {
                this.combL[i5].processMix(this.input, this.pre1);
            }
            this.combL[3].processReplace(this.input, this.pre2);
            for (int i6 = 5; i6 < this.combL.length - 2; i6 += 2) {
                this.combL[i6].processMix(this.input, this.pre2);
            }
            if (!this.mix) {
                Arrays.fill(fArrArray3, 0.0f);
                Arrays.fill(fArrArray2, 0.0f);
            }
            for (int length2 = this.combR.length - 2; length2 < this.combR.length; length2++) {
                this.combR[length2].processMix(this.input, fArrArray3);
            }
            for (int length3 = this.combL.length - 2; length3 < this.combL.length; length3++) {
                this.combL[length3].processMix(this.input, fArrArray2);
            }
            for (int i7 = 0; i7 < length; i7++) {
                float f3 = this.pre1[i7] - this.pre2[i7];
                float f4 = this.pre3[i7];
                int i8 = i7;
                fArrArray2[i8] = fArrArray2[i8] + f4 + f3;
                int i9 = i7;
                fArrArray3[i9] = fArrArray3[i9] + (f4 - f3);
            }
        } else {
            if (this.out == null || this.out.length < length) {
                this.out = new float[length];
            }
            if (fArrArray3 != null) {
                if (!this.mix) {
                    Arrays.fill(fArrArray3, 0.0f);
                }
                this.allpassR[0].processReplace(this.input, this.out);
                for (int i10 = 1; i10 < this.allpassR.length; i10++) {
                    this.allpassR[i10].processReplace(this.out);
                }
                for (int i11 = 0; i11 < this.combR.length; i11++) {
                    this.combR[i11].processMix(this.out, fArrArray3);
                }
            }
            if (!this.mix) {
                Arrays.fill(fArrArray2, 0.0f);
            }
            this.allpassL[0].processReplace(this.input, this.out);
            for (int i12 = 1; i12 < this.allpassL.length; i12++) {
                this.allpassL[i12].processReplace(this.out);
            }
            for (int i13 = 0; i13 < this.combL.length; i13++) {
                this.combL[i13].processMix(this.out, fArrArray2);
            }
        }
        if (zIsSilent) {
            this.silent = true;
            for (int i14 = 0; i14 < length; i14++) {
                float f5 = fArrArray2[i14];
                if (f5 > 1.0E-10d || f5 < -1.0E-10d) {
                    this.silent = false;
                    return;
                }
            }
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void globalParameterControlChange(int[] iArr, long j2, long j3) {
        if (iArr.length == 1 && iArr[0] == 129) {
            if (j2 != 0) {
                if (j2 == 1) {
                    this.dirty_roomsize = (float) Math.exp((j3 - 40) * 0.025d);
                    this.dirty = true;
                    return;
                }
                return;
            }
            if (j3 == 0) {
                this.dirty_roomsize = 1.1f;
                this.dirty_damp = 5000.0f;
                this.dirty_predelay = 0.0f;
                this.dirty_gain = 4.0f;
                this.dirty = true;
            }
            if (j3 == 1) {
                this.dirty_roomsize = 1.3f;
                this.dirty_damp = 5000.0f;
                this.dirty_predelay = 0.0f;
                this.dirty_gain = 3.0f;
                this.dirty = true;
            }
            if (j3 == 2) {
                this.dirty_roomsize = 1.5f;
                this.dirty_damp = 5000.0f;
                this.dirty_predelay = 0.0f;
                this.dirty_gain = 2.0f;
                this.dirty = true;
            }
            if (j3 == 3) {
                this.dirty_roomsize = 1.8f;
                this.dirty_damp = 24000.0f;
                this.dirty_predelay = 0.02f;
                this.dirty_gain = 1.5f;
                this.dirty = true;
            }
            if (j3 == 4) {
                this.dirty_roomsize = 1.8f;
                this.dirty_damp = 24000.0f;
                this.dirty_predelay = 0.03f;
                this.dirty_gain = 1.5f;
                this.dirty = true;
            }
            if (j3 == 8) {
                this.dirty_roomsize = 1.3f;
                this.dirty_damp = 2500.0f;
                this.dirty_predelay = 0.0f;
                this.dirty_gain = 6.0f;
                this.dirty = true;
            }
        }
    }

    @Override // com.sun.media.sound.SoftAudioProcessor
    public void processControlLogic() {
        if (this.dirty) {
            this.dirty = false;
            setRoomSize(this.dirty_roomsize);
            setDamp(this.dirty_damp);
            setPreDelay(this.dirty_predelay);
            setGain(this.dirty_gain);
        }
    }

    public void setRoomSize(float f2) {
        this.roomsize = 1.0f - (0.17f / f2);
        for (int i2 = 0; i2 < this.combL.length; i2++) {
            this.combL[i2].feedback = this.roomsize;
            this.combR[i2].feedback = this.roomsize;
        }
    }

    public void setPreDelay(float f2) {
        this.delay.setDelay((int) (f2 * this.samplerate));
    }

    public void setGain(float f2) {
        this.gain = f2;
    }

    public void setDamp(float f2) {
        double dCos = 2.0d - Math.cos((f2 / this.samplerate) * 6.283185307179586d);
        this.damp = (float) (dCos - Math.sqrt((dCos * dCos) - 1.0d));
        if (this.damp > 1.0f) {
            this.damp = 1.0f;
        }
        if (this.damp < 0.0f) {
            this.damp = 0.0f;
        }
        for (int i2 = 0; i2 < this.combL.length; i2++) {
            this.combL[i2].setDamp(this.damp);
            this.combR[i2].setDamp(this.damp);
        }
    }

    public void setLightMode(boolean z2) {
        this.light = z2;
    }
}
