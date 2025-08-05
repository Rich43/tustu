package org.icepdf.core.pobjects.functions;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/Function_0.class */
public class Function_0 extends Function {
    private static final Logger logger = Logger.getLogger(Function_0.class.toString());
    public static final Name SIZE_KEY = new Name("Size");
    public static final Name BITSPERSAMPLE_KEY = new Name("BitsPerSample");
    public static final Name ENCODE_KEY = new Name("Encode");
    public static final Name DECODE_KEY = new Name(PdfOps.D_NAME);
    private int[] size;
    private int bitspersample;
    private int order;
    private float[] encode;
    private float[] decode;
    private byte[] bytes;

    Function_0(Dictionary d2) {
        super(d2);
        List s2 = (List) d2.getObject(SIZE_KEY);
        this.size = new int[s2.size()];
        for (int i2 = 0; i2 < s2.size(); i2++) {
            this.size[i2] = (int) ((Number) s2.get(i2)).floatValue();
        }
        this.bitspersample = d2.getInt(BITSPERSAMPLE_KEY);
        List enc = (List) d2.getObject(ENCODE_KEY);
        this.encode = new float[this.size.length * 2];
        if (enc != null) {
            for (int i3 = 0; i3 < this.size.length * 2; i3++) {
                this.encode[i3] = ((Number) enc.get(i3)).floatValue();
            }
        } else {
            for (int i4 = 0; i4 < this.size.length; i4++) {
                this.encode[2 * i4] = 0.0f;
                this.encode[(2 * i4) + 1] = this.size[i4] - 1;
            }
        }
        List dec = (List) d2.getObject(DECODE_KEY);
        this.decode = new float[this.range.length];
        if (dec != null) {
            for (int i5 = 0; i5 < this.range.length; i5++) {
                this.decode[i5] = ((Number) dec.get(i5)).floatValue();
            }
        } else {
            System.arraycopy(this.range, 0, this.decode, 0, this.range.length);
        }
        Stream stream = (Stream) d2;
        this.bytes = stream.getDecodedStreamBytes(0);
    }

    @Override // org.icepdf.core.pobjects.functions.Function
    public float[] calculate(float[] x2) {
        int n2 = this.range.length / 2;
        float[] y2 = new float[n2];
        for (int i2 = 0; i2 < this.size.length; i2++) {
            try {
                x2[i2] = Math.min(Math.max(x2[i2], this.domain[2 * i2]), this.domain[(2 * i2) + 1]);
                float e2 = interpolate(x2[i2], this.domain[2 * i2], this.domain[(2 * i2) + 1], this.encode[2 * i2], this.encode[(2 * i2) + 1]);
                float e3 = Math.min(Math.max(e2, 0.0f), this.size[i2] - 1);
                int e1 = (int) Math.floor(e3);
                int e22 = (int) Math.ceil(e3);
                for (int j2 = 0; j2 < n2; j2++) {
                    int b1 = this.bytes[(e1 * n2) + j2] & 255;
                    int b2 = this.bytes[(e22 * n2) + j2] & 255;
                    float r2 = (b1 + b2) / 2.0f;
                    float r3 = Math.min(Math.max(interpolate(r2, 0.0f, ((float) Math.pow(2.0d, this.bitspersample)) - 1.0f, this.decode[2 * j2], this.decode[(2 * j2) + 1]), this.range[2 * j2]), this.range[(2 * j2) + 1]);
                    int index = (i2 * n2) + j2;
                    if (index < y2.length) {
                        y2[index] = r3;
                    }
                }
            } catch (Exception e4) {
                logger.log(Level.FINER, "Error calculating function 0 values", (Throwable) e4);
            }
        }
        return y2;
    }
}
