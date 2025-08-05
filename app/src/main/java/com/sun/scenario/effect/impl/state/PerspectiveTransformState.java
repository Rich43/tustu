package com.sun.scenario.effect.impl.state;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/PerspectiveTransformState.class */
public class PerspectiveTransformState {
    private float[][] itx = new float[3][3];

    public float[][] getITX() {
        return this.itx;
    }

    public void updateTx(float[][] tx) {
        float det = get3x3Determinant(tx);
        if (Math.abs(det) < 1.0E-10d) {
            float[] fArr = this.itx[0];
            float[] fArr2 = this.itx[1];
            this.itx[2][0] = 0.0f;
            fArr2[0] = 0.0f;
            fArr[0] = 0.0f;
            float[] fArr3 = this.itx[0];
            float[] fArr4 = this.itx[1];
            this.itx[2][1] = 0.0f;
            fArr4[1] = 0.0f;
            fArr3[1] = 0.0f;
            float[] fArr5 = this.itx[0];
            this.itx[1][2] = -1.0f;
            fArr5[2] = -1.0f;
            this.itx[2][2] = 1.0f;
            return;
        }
        float invdet = 1.0f / det;
        this.itx[0][0] = invdet * ((tx[1][1] * tx[2][2]) - (tx[1][2] * tx[2][1]));
        this.itx[1][0] = invdet * ((tx[1][2] * tx[2][0]) - (tx[1][0] * tx[2][2]));
        this.itx[2][0] = invdet * ((tx[1][0] * tx[2][1]) - (tx[1][1] * tx[2][0]));
        this.itx[0][1] = invdet * ((tx[0][2] * tx[2][1]) - (tx[0][1] * tx[2][2]));
        this.itx[1][1] = invdet * ((tx[0][0] * tx[2][2]) - (tx[0][2] * tx[2][0]));
        this.itx[2][1] = invdet * ((tx[0][1] * tx[2][0]) - (tx[0][0] * tx[2][1]));
        this.itx[0][2] = invdet * ((tx[0][1] * tx[1][2]) - (tx[0][2] * tx[1][1]));
        this.itx[1][2] = invdet * ((tx[0][2] * tx[1][0]) - (tx[0][0] * tx[1][2]));
        this.itx[2][2] = invdet * ((tx[0][0] * tx[1][1]) - (tx[0][1] * tx[1][0]));
    }

    private static float get3x3Determinant(float[][] m2) {
        return ((m2[0][0] * ((m2[1][1] * m2[2][2]) - (m2[1][2] * m2[2][1]))) - (m2[0][1] * ((m2[1][0] * m2[2][2]) - (m2[1][2] * m2[2][0])))) + (m2[0][2] * ((m2[1][0] * m2[2][1]) - (m2[1][1] * m2[2][0])));
    }
}
