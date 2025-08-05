package org.icepdf.core.pobjects.functions;

import java.util.Arrays;
import java.util.List;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/Function_2.class */
public class Function_2 extends Function {
    public static final Name N_KEY = new Name("N");
    public static final Name C0_KEY = new Name("C0");
    public static final Name C1_KEY = new Name("C1");

    /* renamed from: N, reason: collision with root package name */
    private float f13125N;
    private float[] C0;
    private float[] C1;

    Function_2(Dictionary d2) {
        super(d2);
        this.C0 = new float[]{0.0f};
        this.C1 = new float[]{1.0f};
        this.f13125N = d2.getFloat(N_KEY);
        List c0 = (List) d2.getObject(C0_KEY);
        if (c0 != null) {
            this.C0 = new float[c0.size()];
            for (int i2 = 0; i2 < c0.size(); i2++) {
                this.C0[i2] = ((Number) c0.get(i2)).floatValue();
            }
        }
        List c1 = (List) d2.getObject(C1_KEY);
        if (c1 != null) {
            this.C1 = new float[c1.size()];
            for (int i3 = 0; i3 < c1.size(); i3++) {
                this.C1[i3] = ((Number) c1.get(i3)).floatValue();
            }
        }
    }

    @Override // org.icepdf.core.pobjects.functions.Function
    public float[] calculate(float[] x2) {
        float[] y2 = new float[x2.length * this.C0.length];
        for (int i2 = 0; i2 < x2.length; i2++) {
            for (int j2 = 0; j2 < this.C0.length; j2++) {
                float yValue = (float) (this.C0[j2] + (Math.pow(Math.abs(x2[i2]), this.f13125N) * (this.C1[j2] - this.C0[j2])));
                if (this.range != null) {
                    yValue = Math.min(Math.max(yValue, this.range[2 * j2]), this.range[(2 * j2) + 1]);
                }
                y2[(i2 * this.C0.length) + j2] = yValue;
            }
        }
        return y2;
    }

    public String toString() {
        return "FunctionType: " + this.functionType + "\n    domain: " + Arrays.toString(this.domain) + "\n     range: " + Arrays.toString(this.range) + "\n         N: " + this.f13125N + "\n        C0: " + Arrays.toString(this.C0) + "\n        C1: " + Arrays.toString(this.C1);
    }
}
