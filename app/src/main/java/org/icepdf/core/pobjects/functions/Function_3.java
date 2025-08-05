package org.icepdf.core.pobjects.functions;

import java.util.List;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/Function_3.class */
public class Function_3 extends Function {
    public static final Name BOUNDS_KEY = new Name("Bounds");
    public static final Name ENCODE_KEY = new Name("Encode");
    public static final Name FUNCTIONS_KEY = new Name("Functions");
    private float[] bounds;
    private float[] encode;
    private Function[] functions;

    Function_3(Dictionary d2) {
        super(d2);
        List boundTemp = (List) d2.getObject(BOUNDS_KEY);
        if (boundTemp != null) {
            this.bounds = new float[boundTemp.size()];
            for (int i2 = 0; i2 < boundTemp.size(); i2++) {
                this.bounds[i2] = ((Number) boundTemp.get(i2)).floatValue();
            }
        }
        List encodeTemp = (List) d2.getObject(ENCODE_KEY);
        if (encodeTemp != null) {
            this.encode = new float[encodeTemp.size()];
            for (int i3 = 0; i3 < encodeTemp.size(); i3++) {
                this.encode[i3] = ((Number) encodeTemp.get(i3)).floatValue();
            }
        }
        List functionTemp = (List) d2.getObject(FUNCTIONS_KEY);
        if (encodeTemp != null) {
            this.functions = new Function[functionTemp.size()];
            for (int i4 = 0; i4 < functionTemp.size(); i4++) {
                this.functions[i4] = Function.getFunction(d2.getLibrary(), functionTemp.get(i4));
            }
        }
    }

    @Override // org.icepdf.core.pobjects.functions.Function
    public float[] calculate(float[] x2) {
        int k2 = this.functions.length;
        if (k2 == 1 && this.bounds.length == 0 && this.domain[0] <= x2[0] && x2[0] <= this.domain[1]) {
            return encode(x2, this.functions[0], 0);
        }
        for (int b2 = 0; b2 < this.bounds.length; b2++) {
            if (b2 == 0 && this.domain[0] <= x2[0] && x2[0] < this.bounds[b2]) {
                return encode(x2, this.functions[b2], b2);
            }
            if (b2 == k2 - 2 && this.bounds[b2] <= x2[0] && x2[0] <= this.domain[1]) {
                return encode(x2, this.functions[k2 - 1], k2 - 1);
            }
            if (this.bounds[b2] <= x2[0] && x2[0] < this.bounds[b2 + 1]) {
                return encode(x2, this.functions[b2], b2);
            }
        }
        return null;
    }

    private float[] encode(float[] x2, Function function, int i2) {
        float[] x3;
        float b1;
        float b2;
        int k2 = this.functions.length;
        if (i2 <= 0 && i2 < k2 && this.bounds.length > 0) {
            if (i2 - 1 == -1) {
                b1 = this.domain[0];
            } else {
                b1 = this.bounds[i2 - 1];
            }
            if (i2 == k2 - 1) {
                b2 = this.domain[1];
            } else {
                b2 = this.bounds[i2];
            }
            if (k2 - 2 < this.bounds.length && this.bounds[k2 - 2] == this.domain[1]) {
                x2[0] = this.encode[2 * i2];
            }
            x2[0] = interpolate(x2[0], b1, b2, this.encode[2 * i2], this.encode[(2 * i2) + 1]);
            x3 = function.calculate(x2);
        } else {
            x2[0] = interpolate(x2[0], this.domain[0], this.domain[1], this.encode[2 * i2], this.encode[(2 * i2) + 1]);
            x3 = function.calculate(x2);
        }
        if (x3 != null) {
            return validateAgainstRange(x3);
        }
        return new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    }

    private float[] validateAgainstRange(float[] values) {
        int max = values.length;
        for (int j2 = 0; j2 < max; j2++) {
            if (this.range != null && values[j2] < this.range[2 * j2]) {
                values[j2] = this.range[2 * j2];
            } else if (this.range != null && values[j2] > this.range[(2 * j2) + 1]) {
                values[j2] = this.range[(2 * j2) + 1];
            } else if (values[j2] < 0.0f) {
                values[j2] = 0.0f;
            }
        }
        return values;
    }
}
