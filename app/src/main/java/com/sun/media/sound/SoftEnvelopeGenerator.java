package com.sun.media.sound;

import javafx.fxml.FXMLLoader;
import javax.swing.plaf.basic.BasicRootPaneUI;

/* loaded from: rt.jar:com/sun/media/sound/SoftEnvelopeGenerator.class */
public final class SoftEnvelopeGenerator implements SoftProcess {
    public static final int EG_OFF = 0;
    public static final int EG_DELAY = 1;
    public static final int EG_ATTACK = 2;
    public static final int EG_HOLD = 3;
    public static final int EG_DECAY = 4;
    public static final int EG_SUSTAIN = 5;
    public static final int EG_RELEASE = 6;
    public static final int EG_SHUTDOWN = 7;
    public static final int EG_END = 8;
    int max_count = 10;
    int used_count = 0;
    private final int[] stage = new int[this.max_count];
    private final int[] stage_ix = new int[this.max_count];
    private final double[] stage_v = new double[this.max_count];
    private final int[] stage_count = new int[this.max_count];
    private final double[][] on = new double[this.max_count][1];
    private final double[][] active = new double[this.max_count][1];
    private final double[][] out = new double[this.max_count][1];
    private final double[][] delay = new double[this.max_count][1];
    private final double[][] attack = new double[this.max_count][1];
    private final double[][] hold = new double[this.max_count][1];
    private final double[][] decay = new double[this.max_count][1];
    private final double[][] sustain = new double[this.max_count][1];
    private final double[][] release = new double[this.max_count][1];
    private final double[][] shutdown = new double[this.max_count][1];
    private final double[][] release2 = new double[this.max_count][1];
    private final double[][] attack2 = new double[this.max_count][1];
    private final double[][] decay2 = new double[this.max_count][1];
    private double control_time = 0.0d;

    @Override // com.sun.media.sound.SoftProcess
    public void reset() {
        for (int i2 = 0; i2 < this.used_count; i2++) {
            this.stage[i2] = 0;
            this.on[i2][0] = 0.0d;
            this.out[i2][0] = 0.0d;
            this.delay[i2][0] = 0.0d;
            this.attack[i2][0] = 0.0d;
            this.hold[i2][0] = 0.0d;
            this.decay[i2][0] = 0.0d;
            this.sustain[i2][0] = 0.0d;
            this.release[i2][0] = 0.0d;
            this.shutdown[i2][0] = 0.0d;
            this.attack2[i2][0] = 0.0d;
            this.decay2[i2][0] = 0.0d;
            this.release2[i2][0] = 0.0d;
        }
        this.used_count = 0;
    }

    @Override // com.sun.media.sound.SoftProcess
    public void init(SoftSynthesizer softSynthesizer) {
        this.control_time = 1.0d / softSynthesizer.getControlRate();
        processControlLogic();
    }

    @Override // com.sun.media.sound.SoftProcess, com.sun.media.sound.SoftControl
    public double[] get(int i2, String str) {
        if (i2 >= this.used_count) {
            this.used_count = i2 + 1;
        }
        if (str == null) {
            return this.out[i2];
        }
        if (str.equals(FXMLLoader.EVENT_HANDLER_PREFIX)) {
            return this.on[i2];
        }
        if (str.equals("active")) {
            return this.active[i2];
        }
        if (str.equals("delay")) {
            return this.delay[i2];
        }
        if (str.equals("attack")) {
            return this.attack[i2];
        }
        if (str.equals("hold")) {
            return this.hold[i2];
        }
        if (str.equals("decay")) {
            return this.decay[i2];
        }
        if (str.equals("sustain")) {
            return this.sustain[i2];
        }
        if (str.equals(BasicRootPaneUI.Actions.RELEASE)) {
            return this.release[i2];
        }
        if (str.equals("shutdown")) {
            return this.shutdown[i2];
        }
        if (str.equals("attack2")) {
            return this.attack2[i2];
        }
        if (str.equals("decay2")) {
            return this.decay2[i2];
        }
        if (str.equals("release2")) {
            return this.release2[i2];
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x05a6 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x028f  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x029c  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0309  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x037c  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x03ff  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0549  */
    @Override // com.sun.media.sound.SoftProcess
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void processControlLogic() {
        /*
            Method dump skipped, instructions count: 1453
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.SoftEnvelopeGenerator.processControlLogic():void");
    }
}
