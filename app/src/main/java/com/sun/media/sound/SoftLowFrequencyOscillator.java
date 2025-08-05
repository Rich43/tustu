package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftLowFrequencyOscillator.class */
public final class SoftLowFrequencyOscillator implements SoftProcess {
    private final int max_count = 10;
    private int used_count = 0;
    private final double[][] out = new double[10][1];
    private final double[][] delay = new double[10][1];
    private final double[][] delay2 = new double[10][1];
    private final double[][] freq = new double[10][1];
    private final int[] delay_counter = new int[10];
    private final double[] sin_phase = new double[10];
    private final double[] sin_stepfreq = new double[10];
    private final double[] sin_step = new double[10];
    private double control_time = 0.0d;
    private double sin_factor = 0.0d;
    private static final double PI2 = 6.283185307179586d;

    public SoftLowFrequencyOscillator() {
        for (int i2 = 0; i2 < this.sin_stepfreq.length; i2++) {
            this.sin_stepfreq[i2] = Double.NEGATIVE_INFINITY;
        }
    }

    @Override // com.sun.media.sound.SoftProcess
    public void reset() {
        for (int i2 = 0; i2 < this.used_count; i2++) {
            this.out[i2][0] = 0.0d;
            this.delay[i2][0] = 0.0d;
            this.delay2[i2][0] = 0.0d;
            this.freq[i2][0] = 0.0d;
            this.delay_counter[i2] = 0;
            this.sin_phase[i2] = 0.0d;
            this.sin_stepfreq[i2] = Double.NEGATIVE_INFINITY;
            this.sin_step[i2] = 0.0d;
        }
        this.used_count = 0;
    }

    @Override // com.sun.media.sound.SoftProcess
    public void init(SoftSynthesizer softSynthesizer) {
        this.control_time = 1.0d / softSynthesizer.getControlRate();
        this.sin_factor = this.control_time * 2.0d * 3.141592653589793d;
        for (int i2 = 0; i2 < this.used_count; i2++) {
            this.delay_counter[i2] = (int) (Math.pow(2.0d, this.delay[i2][0] / 1200.0d) / this.control_time);
            int[] iArr = this.delay_counter;
            int i3 = i2;
            iArr[i3] = iArr[i3] + ((int) (this.delay2[i2][0] / (this.control_time * 1000.0d)));
        }
        processControlLogic();
    }

    @Override // com.sun.media.sound.SoftProcess
    public void processControlLogic() {
        double d2;
        for (int i2 = 0; i2 < this.used_count; i2++) {
            if (this.delay_counter[i2] > 0) {
                int[] iArr = this.delay_counter;
                int i3 = i2;
                iArr[i3] = iArr[i3] - 1;
                this.out[i2][0] = 0.5d;
            } else {
                double d3 = this.freq[i2][0];
                if (this.sin_stepfreq[i2] != d3) {
                    this.sin_stepfreq[i2] = d3;
                    this.sin_step[i2] = 440.0d * Math.exp((d3 - 6900.0d) * (Math.log(2.0d) / 1200.0d)) * this.sin_factor;
                }
                double d4 = this.sin_phase[i2] + this.sin_step[i2];
                while (true) {
                    d2 = d4;
                    if (d2 <= 6.283185307179586d) {
                        break;
                    } else {
                        d4 = d2 - 6.283185307179586d;
                    }
                }
                this.out[i2][0] = 0.5d + (Math.sin(d2) * 0.5d);
                this.sin_phase[i2] = d2;
            }
        }
    }

    @Override // com.sun.media.sound.SoftProcess, com.sun.media.sound.SoftControl
    public double[] get(int i2, String str) {
        if (i2 >= this.used_count) {
            this.used_count = i2 + 1;
        }
        if (str == null) {
            return this.out[i2];
        }
        if (str.equals("delay")) {
            return this.delay[i2];
        }
        if (str.equals("delay2")) {
            return this.delay2[i2];
        }
        if (str.equals("freq")) {
            return this.freq[i2];
        }
        return null;
    }
}
