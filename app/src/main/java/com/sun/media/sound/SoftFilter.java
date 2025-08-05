package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftFilter.class */
public final class SoftFilter {
    public static final int FILTERTYPE_LP6 = 0;
    public static final int FILTERTYPE_LP12 = 1;
    public static final int FILTERTYPE_HP12 = 17;
    public static final int FILTERTYPE_BP12 = 33;
    public static final int FILTERTYPE_NP12 = 49;
    public static final int FILTERTYPE_LP24 = 3;
    public static final int FILTERTYPE_HP24 = 19;
    private final float samplerate;
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    private float xx1;
    private float xx2;
    private float yy1;
    private float yy2;
    private float a0;
    private float a1;
    private float a2;
    private float b1;
    private float b2;

    /* renamed from: q, reason: collision with root package name */
    private float f11982q;
    private float last_a0;
    private float last_a1;
    private float last_a2;
    private float last_b1;
    private float last_b2;
    private float last_q;
    private float last_gain;
    private boolean dirty;
    private int filtertype = 0;
    private float gain = 1.0f;
    private float wet = 0.0f;
    private float last_wet = 0.0f;
    private boolean last_set = false;
    private double cutoff = 44100.0d;
    private double resonancedB = 0.0d;

    public SoftFilter(float f2) {
        this.dirty = true;
        this.samplerate = f2;
        this.dirty = true;
    }

    public void setFrequency(double d2) {
        if (this.cutoff == d2) {
            return;
        }
        this.cutoff = d2;
        this.dirty = true;
    }

    public void setResonance(double d2) {
        if (this.resonancedB == d2) {
            return;
        }
        this.resonancedB = d2;
        this.dirty = true;
    }

    public void reset() {
        this.dirty = true;
        this.last_set = false;
        this.x1 = 0.0f;
        this.x2 = 0.0f;
        this.y1 = 0.0f;
        this.y2 = 0.0f;
        this.xx1 = 0.0f;
        this.xx2 = 0.0f;
        this.yy1 = 0.0f;
        this.yy2 = 0.0f;
        this.wet = 0.0f;
        this.gain = 1.0f;
        this.a0 = 0.0f;
        this.a1 = 0.0f;
        this.a2 = 0.0f;
        this.b1 = 0.0f;
        this.b2 = 0.0f;
    }

    public void setFilterType(int i2) {
        this.filtertype = i2;
    }

    public void processAudio(SoftAudioBuffer softAudioBuffer) {
        if (this.filtertype == 0) {
            filter1(softAudioBuffer);
        }
        if (this.filtertype == 1) {
            filter2(softAudioBuffer);
        }
        if (this.filtertype == 17) {
            filter2(softAudioBuffer);
        }
        if (this.filtertype == 33) {
            filter2(softAudioBuffer);
        }
        if (this.filtertype == 49) {
            filter2(softAudioBuffer);
        }
        if (this.filtertype == 3) {
            filter4(softAudioBuffer);
        }
        if (this.filtertype == 19) {
            filter4(softAudioBuffer);
        }
    }

    public void filter4(SoftAudioBuffer softAudioBuffer) {
        float[] fArrArray = softAudioBuffer.array();
        if (this.dirty) {
            filter2calc();
            this.dirty = false;
        }
        if (!this.last_set) {
            this.last_a0 = this.a0;
            this.last_a1 = this.a1;
            this.last_a2 = this.a2;
            this.last_b1 = this.b1;
            this.last_b2 = this.b2;
            this.last_gain = this.gain;
            this.last_wet = this.wet;
            this.last_set = true;
        }
        if (this.wet > 0.0f || this.last_wet > 0.0f) {
            int length = fArrArray.length;
            float f2 = this.last_a0;
            float f3 = this.last_a1;
            float f4 = this.last_a2;
            float f5 = this.last_b1;
            float f6 = this.last_b2;
            float f7 = this.last_gain;
            float f8 = this.last_wet;
            float f9 = (this.a0 - this.last_a0) / length;
            float f10 = (this.a1 - this.last_a1) / length;
            float f11 = (this.a2 - this.last_a2) / length;
            float f12 = (this.b1 - this.last_b1) / length;
            float f13 = (this.b2 - this.last_b2) / length;
            float f14 = (this.gain - this.last_gain) / length;
            float f15 = (this.wet - this.last_wet) / length;
            float f16 = this.x1;
            float f17 = this.x2;
            float f18 = this.y1;
            float f19 = this.y2;
            float f20 = this.xx1;
            float f21 = this.xx2;
            float f22 = this.yy1;
            float f23 = this.yy2;
            if (f15 != 0.0f) {
                for (int i2 = 0; i2 < length; i2++) {
                    f2 += f9;
                    f3 += f10;
                    f4 += f11;
                    f5 += f12;
                    f6 += f13;
                    f7 += f14;
                    f8 += f15;
                    float f24 = fArrArray[i2];
                    float f25 = ((((f2 * f24) + (f3 * f16)) + (f4 * f17)) - (f5 * f18)) - (f6 * f19);
                    float f26 = (f25 * f7 * f8) + (f24 * (1.0f - f8));
                    f17 = f16;
                    f16 = f24;
                    f19 = f18;
                    f18 = f25;
                    float f27 = ((((f2 * f26) + (f3 * f20)) + (f4 * f21)) - (f5 * f22)) - (f6 * f23);
                    fArrArray[i2] = (f27 * f7 * f8) + (f26 * (1.0f - f8));
                    f21 = f20;
                    f20 = f26;
                    f23 = f22;
                    f22 = f27;
                }
            } else if (f9 == 0.0f && f10 == 0.0f && f11 == 0.0f && f12 == 0.0f && f13 == 0.0f) {
                for (int i3 = 0; i3 < length; i3++) {
                    float f28 = fArrArray[i3];
                    float f29 = ((((f2 * f28) + (f3 * f16)) + (f4 * f17)) - (f5 * f18)) - (f6 * f19);
                    float f30 = (f29 * f7 * f8) + (f28 * (1.0f - f8));
                    f17 = f16;
                    f16 = f28;
                    f19 = f18;
                    f18 = f29;
                    float f31 = ((((f2 * f30) + (f3 * f20)) + (f4 * f21)) - (f5 * f22)) - (f6 * f23);
                    fArrArray[i3] = (f31 * f7 * f8) + (f30 * (1.0f - f8));
                    f21 = f20;
                    f20 = f30;
                    f23 = f22;
                    f22 = f31;
                }
            } else {
                for (int i4 = 0; i4 < length; i4++) {
                    f2 += f9;
                    f3 += f10;
                    f4 += f11;
                    f5 += f12;
                    f6 += f13;
                    f7 += f14;
                    float f32 = fArrArray[i4];
                    float f33 = ((((f2 * f32) + (f3 * f16)) + (f4 * f17)) - (f5 * f18)) - (f6 * f19);
                    float f34 = (f33 * f7 * f8) + (f32 * (1.0f - f8));
                    f17 = f16;
                    f16 = f32;
                    f19 = f18;
                    f18 = f33;
                    float f35 = ((((f2 * f34) + (f3 * f20)) + (f4 * f21)) - (f5 * f22)) - (f6 * f23);
                    fArrArray[i4] = (f35 * f7 * f8) + (f34 * (1.0f - f8));
                    f21 = f20;
                    f20 = f34;
                    f23 = f22;
                    f22 = f35;
                }
            }
            if (Math.abs(f16) < 1.0E-8d) {
                f16 = 0.0f;
            }
            if (Math.abs(f17) < 1.0E-8d) {
                f17 = 0.0f;
            }
            if (Math.abs(f18) < 1.0E-8d) {
                f18 = 0.0f;
            }
            if (Math.abs(f19) < 1.0E-8d) {
                f19 = 0.0f;
            }
            this.x1 = f16;
            this.x2 = f17;
            this.y1 = f18;
            this.y2 = f19;
            this.xx1 = f20;
            this.xx2 = f21;
            this.yy1 = f22;
            this.yy2 = f23;
        }
        this.last_a0 = this.a0;
        this.last_a1 = this.a1;
        this.last_a2 = this.a2;
        this.last_b1 = this.b1;
        this.last_b2 = this.b2;
        this.last_gain = this.gain;
        this.last_wet = this.wet;
    }

    private double sinh(double d2) {
        return (Math.exp(d2) - Math.exp(-d2)) * 0.5d;
    }

    public void filter2calc() {
        double d2 = this.resonancedB;
        if (d2 < 0.0d) {
            d2 = 0.0d;
        }
        if (d2 > 30.0d) {
            d2 = 30.0d;
        }
        if (this.filtertype == 3 || this.filtertype == 19) {
            d2 *= 0.6d;
        }
        if (this.filtertype == 33) {
            this.wet = 1.0f;
            double d3 = this.cutoff / this.samplerate;
            if (d3 > 0.45d) {
                d3 = 0.45d;
            }
            double dPow = 3.141592653589793d * Math.pow(10.0d, -(d2 / 20.0d));
            double d4 = 6.283185307179586d * d3;
            double dCos = Math.cos(d4);
            double dSin = Math.sin(d4);
            double dSinh = dSin * sinh(((Math.log(2.0d) * dPow) * d4) / (dSin * 2.0d));
            double d5 = -dSinh;
            double d6 = 1.0d + dSinh;
            double d7 = (-2.0d) * dCos;
            double d8 = 1.0d - dSinh;
            double d9 = 1.0d / d6;
            this.b1 = (float) (d7 * d9);
            this.b2 = (float) (d8 * d9);
            this.a0 = (float) (dSinh * d9);
            this.a1 = (float) (0.0d * d9);
            this.a2 = (float) (d5 * d9);
        }
        if (this.filtertype == 49) {
            this.wet = 1.0f;
            double d10 = this.cutoff / this.samplerate;
            if (d10 > 0.45d) {
                d10 = 0.45d;
            }
            double dPow2 = 3.141592653589793d * Math.pow(10.0d, -(d2 / 20.0d));
            double d11 = 6.283185307179586d * d10;
            double dCos2 = Math.cos(d11);
            double dSin2 = Math.sin(d11);
            double dSinh2 = dSin2 * sinh(((Math.log(2.0d) * dPow2) * d11) / (dSin2 * 2.0d));
            double d12 = (-2.0d) * dCos2;
            double d13 = 1.0d + dSinh2;
            double d14 = (-2.0d) * dCos2;
            double d15 = 1.0d - dSinh2;
            double d16 = 1.0d / d13;
            this.b1 = (float) (d14 * d16);
            this.b2 = (float) (d15 * d16);
            this.a0 = (float) (1.0d * d16);
            this.a1 = (float) (d12 * d16);
            this.a2 = (float) (1.0d * d16);
        }
        if (this.filtertype == 1 || this.filtertype == 3) {
            double d17 = this.cutoff / this.samplerate;
            if (d17 > 0.45d) {
                if (this.wet == 0.0f) {
                    if (d2 < 1.0E-5d) {
                        this.wet = 0.0f;
                    } else {
                        this.wet = 1.0f;
                    }
                }
                d17 = 0.45d;
            } else {
                this.wet = 1.0f;
            }
            double dTan = 1.0d / Math.tan(3.141592653589793d * d17);
            double d18 = dTan * dTan;
            double dSqrt = Math.sqrt(2.0d) * Math.pow(10.0d, -(d2 / 20.0d));
            double d19 = 1.0d / ((1.0d + (dSqrt * dTan)) + d18);
            double d20 = 2.0d * d19;
            this.a0 = (float) d19;
            this.a1 = (float) d20;
            this.a2 = (float) d19;
            this.b1 = (float) (2.0d * d19 * (1.0d - d18));
            this.b2 = (float) (d19 * ((1.0d - (dSqrt * dTan)) + d18));
        }
        if (this.filtertype == 17 || this.filtertype == 19) {
            double d21 = this.cutoff / this.samplerate;
            if (d21 > 0.45d) {
                d21 = 0.45d;
            }
            if (d21 < 1.0E-4d) {
                d21 = 1.0E-4d;
            }
            this.wet = 1.0f;
            double dTan2 = Math.tan(3.141592653589793d * d21);
            double d22 = dTan2 * dTan2;
            double dSqrt2 = Math.sqrt(2.0d) * Math.pow(10.0d, -(d2 / 20.0d));
            double d23 = 1.0d / ((1.0d + (dSqrt2 * dTan2)) + d22);
            double d24 = (-2.0d) * d23;
            this.a0 = (float) d23;
            this.a1 = (float) d24;
            this.a2 = (float) d23;
            this.b1 = (float) (2.0d * d23 * (d22 - 1.0d));
            this.b2 = (float) (d23 * ((1.0d - (dSqrt2 * dTan2)) + d22));
        }
    }

    public void filter2(SoftAudioBuffer softAudioBuffer) {
        float[] fArrArray = softAudioBuffer.array();
        if (this.dirty) {
            filter2calc();
            this.dirty = false;
        }
        if (!this.last_set) {
            this.last_a0 = this.a0;
            this.last_a1 = this.a1;
            this.last_a2 = this.a2;
            this.last_b1 = this.b1;
            this.last_b2 = this.b2;
            this.last_q = this.f11982q;
            this.last_gain = this.gain;
            this.last_wet = this.wet;
            this.last_set = true;
        }
        if (this.wet > 0.0f || this.last_wet > 0.0f) {
            int length = fArrArray.length;
            float f2 = this.last_a0;
            float f3 = this.last_a1;
            float f4 = this.last_a2;
            float f5 = this.last_b1;
            float f6 = this.last_b2;
            float f7 = this.last_gain;
            float f8 = this.last_wet;
            float f9 = (this.a0 - this.last_a0) / length;
            float f10 = (this.a1 - this.last_a1) / length;
            float f11 = (this.a2 - this.last_a2) / length;
            float f12 = (this.b1 - this.last_b1) / length;
            float f13 = (this.b2 - this.last_b2) / length;
            float f14 = (this.gain - this.last_gain) / length;
            float f15 = (this.wet - this.last_wet) / length;
            float f16 = this.x1;
            float f17 = this.x2;
            float f18 = this.y1;
            float f19 = this.y2;
            if (f15 != 0.0f) {
                for (int i2 = 0; i2 < length; i2++) {
                    f2 += f9;
                    f3 += f10;
                    f4 += f11;
                    f5 += f12;
                    f6 += f13;
                    f7 += f14;
                    f8 += f15;
                    float f20 = fArrArray[i2];
                    float f21 = ((((f2 * f20) + (f3 * f16)) + (f4 * f17)) - (f5 * f18)) - (f6 * f19);
                    fArrArray[i2] = (f21 * f7 * f8) + (f20 * (1.0f - f8));
                    f17 = f16;
                    f16 = f20;
                    f19 = f18;
                    f18 = f21;
                }
            } else if (f9 == 0.0f && f10 == 0.0f && f11 == 0.0f && f12 == 0.0f && f13 == 0.0f) {
                for (int i3 = 0; i3 < length; i3++) {
                    float f22 = fArrArray[i3];
                    float f23 = ((((f2 * f22) + (f3 * f16)) + (f4 * f17)) - (f5 * f18)) - (f6 * f19);
                    fArrArray[i3] = f23 * f7;
                    f17 = f16;
                    f16 = f22;
                    f19 = f18;
                    f18 = f23;
                }
            } else {
                for (int i4 = 0; i4 < length; i4++) {
                    f2 += f9;
                    f3 += f10;
                    f4 += f11;
                    f5 += f12;
                    f6 += f13;
                    f7 += f14;
                    float f24 = fArrArray[i4];
                    float f25 = ((((f2 * f24) + (f3 * f16)) + (f4 * f17)) - (f5 * f18)) - (f6 * f19);
                    fArrArray[i4] = f25 * f7;
                    f17 = f16;
                    f16 = f24;
                    f19 = f18;
                    f18 = f25;
                }
            }
            if (Math.abs(f16) < 1.0E-8d) {
                f16 = 0.0f;
            }
            if (Math.abs(f17) < 1.0E-8d) {
                f17 = 0.0f;
            }
            if (Math.abs(f18) < 1.0E-8d) {
                f18 = 0.0f;
            }
            if (Math.abs(f19) < 1.0E-8d) {
                f19 = 0.0f;
            }
            this.x1 = f16;
            this.x2 = f17;
            this.y1 = f18;
            this.y2 = f19;
        }
        this.last_a0 = this.a0;
        this.last_a1 = this.a1;
        this.last_a2 = this.a2;
        this.last_b1 = this.b1;
        this.last_b2 = this.b2;
        this.last_q = this.f11982q;
        this.last_gain = this.gain;
        this.last_wet = this.wet;
    }

    public void filter1calc() {
        if (this.cutoff < 120.0d) {
            this.cutoff = 120.0d;
        }
        double d2 = (7.3303828583761845d * this.cutoff) / this.samplerate;
        if (d2 > 1.0d) {
            d2 = 1.0d;
        }
        this.a0 = (float) (Math.sqrt(1.0d - Math.cos(d2)) * Math.sqrt(1.5707963267948966d));
        if (this.resonancedB < 0.0d) {
            this.resonancedB = 0.0d;
        }
        if (this.resonancedB > 20.0d) {
            this.resonancedB = 20.0d;
        }
        this.f11982q = (float) (Math.sqrt(0.5d) * Math.pow(10.0d, -(this.resonancedB / 20.0d)));
        this.gain = (float) Math.pow(10.0d, (-this.resonancedB) / 40.0d);
        if (this.wet == 0.0f) {
            if (this.resonancedB > 1.0E-5d || d2 < 0.9999999d) {
                this.wet = 1.0f;
            }
        }
    }

    public void filter1(SoftAudioBuffer softAudioBuffer) {
        if (this.dirty) {
            filter1calc();
            this.dirty = false;
        }
        if (!this.last_set) {
            this.last_a0 = this.a0;
            this.last_q = this.f11982q;
            this.last_gain = this.gain;
            this.last_wet = this.wet;
            this.last_set = true;
        }
        if (this.wet > 0.0f || this.last_wet > 0.0f) {
            float[] fArrArray = softAudioBuffer.array();
            int length = fArrArray.length;
            float f2 = this.last_a0;
            float f3 = this.last_q;
            float f4 = this.last_gain;
            float f5 = this.last_wet;
            float f6 = (this.a0 - this.last_a0) / length;
            float f7 = (this.f11982q - this.last_q) / length;
            float f8 = (this.gain - this.last_gain) / length;
            float f9 = (this.wet - this.last_wet) / length;
            float f10 = this.y2;
            float f11 = this.y1;
            if (f9 != 0.0f) {
                for (int i2 = 0; i2 < length; i2++) {
                    f2 += f6;
                    f3 += f7;
                    f4 += f8;
                    f5 += f9;
                    float f12 = 1.0f - (f3 * f2);
                    f11 = (f12 * f11) + (f2 * (fArrArray[i2] - f10));
                    f10 = (f12 * f10) + (f2 * f11);
                    fArrArray[i2] = (f10 * f4 * f5) + (fArrArray[i2] * (1.0f - f5));
                }
            } else if (f6 == 0.0f && f7 == 0.0f) {
                float f13 = 1.0f - (f3 * f2);
                for (int i3 = 0; i3 < length; i3++) {
                    f11 = (f13 * f11) + (f2 * (fArrArray[i3] - f10));
                    f10 = (f13 * f10) + (f2 * f11);
                    fArrArray[i3] = f10 * f4;
                }
            } else {
                for (int i4 = 0; i4 < length; i4++) {
                    f2 += f6;
                    f3 += f7;
                    f4 += f8;
                    float f14 = 1.0f - (f3 * f2);
                    f11 = (f14 * f11) + (f2 * (fArrArray[i4] - f10));
                    f10 = (f14 * f10) + (f2 * f11);
                    fArrArray[i4] = f10 * f4;
                }
            }
            if (Math.abs(f10) < 1.0E-8d) {
                f10 = 0.0f;
            }
            if (Math.abs(f11) < 1.0E-8d) {
                f11 = 0.0f;
            }
            this.y2 = f10;
            this.y1 = f11;
        }
        this.last_a0 = this.a0;
        this.last_q = this.f11982q;
        this.last_gain = this.gain;
        this.last_wet = this.wet;
    }
}
