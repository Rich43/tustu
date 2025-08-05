package java.awt;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import sun.java2d.pipe.RenderingEngine;

/* loaded from: rt.jar:java/awt/BasicStroke.class */
public class BasicStroke implements Stroke {
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final int JOIN_BEVEL = 2;
    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    float width;
    int join;
    int cap;
    float miterlimit;
    float[] dash;
    float dash_phase;

    @ConstructorProperties({"lineWidth", "endCap", "lineJoin", "miterLimit", "dashArray", "dashPhase"})
    public BasicStroke(float f2, int i2, int i3, float f3, float[] fArr, float f4) {
        if (f2 < 0.0f) {
            throw new IllegalArgumentException("negative width");
        }
        if (i2 != 0 && i2 != 1 && i2 != 2) {
            throw new IllegalArgumentException("illegal end cap value");
        }
        if (i3 == 0) {
            if (f3 < 1.0f) {
                throw new IllegalArgumentException("miter limit < 1");
            }
        } else if (i3 != 1 && i3 != 2) {
            throw new IllegalArgumentException("illegal line join value");
        }
        if (fArr != null) {
            if (f4 < 0.0f) {
                throw new IllegalArgumentException("negative dash phase");
            }
            boolean z2 = true;
            for (float f5 : fArr) {
                if (f5 > 0.0d) {
                    z2 = false;
                } else if (f5 < 0.0d) {
                    throw new IllegalArgumentException("negative dash length");
                }
            }
            if (z2) {
                throw new IllegalArgumentException("dash lengths all zero");
            }
        }
        this.width = f2;
        this.cap = i2;
        this.join = i3;
        this.miterlimit = f3;
        if (fArr != null) {
            this.dash = (float[]) fArr.clone();
        }
        this.dash_phase = f4;
    }

    public BasicStroke(float f2, int i2, int i3, float f3) {
        this(f2, i2, i3, f3, null, 0.0f);
    }

    public BasicStroke(float f2, int i2, int i3) {
        this(f2, i2, i3, 10.0f, null, 0.0f);
    }

    public BasicStroke(float f2) {
        this(f2, 2, 0, 10.0f, null, 0.0f);
    }

    public BasicStroke() {
        this(1.0f, 2, 0, 10.0f, null, 0.0f);
    }

    @Override // java.awt.Stroke
    public Shape createStrokedShape(Shape shape) {
        return RenderingEngine.getInstance().createStrokedShape(shape, this.width, this.cap, this.join, this.miterlimit, this.dash, this.dash_phase);
    }

    public float getLineWidth() {
        return this.width;
    }

    public int getEndCap() {
        return this.cap;
    }

    public int getLineJoin() {
        return this.join;
    }

    public float getMiterLimit() {
        return this.miterlimit;
    }

    public float[] getDashArray() {
        if (this.dash == null) {
            return null;
        }
        return (float[]) this.dash.clone();
    }

    public float getDashPhase() {
        return this.dash_phase;
    }

    public int hashCode() {
        int iFloatToIntBits = (((((Float.floatToIntBits(this.width) * 31) + this.join) * 31) + this.cap) * 31) + Float.floatToIntBits(this.miterlimit);
        if (this.dash != null) {
            iFloatToIntBits = (iFloatToIntBits * 31) + Float.floatToIntBits(this.dash_phase);
            for (int i2 = 0; i2 < this.dash.length; i2++) {
                iFloatToIntBits = (iFloatToIntBits * 31) + Float.floatToIntBits(this.dash[i2]);
            }
        }
        return iFloatToIntBits;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BasicStroke)) {
            return false;
        }
        BasicStroke basicStroke = (BasicStroke) obj;
        if (this.width != basicStroke.width || this.join != basicStroke.join || this.cap != basicStroke.cap || this.miterlimit != basicStroke.miterlimit) {
            return false;
        }
        if (this.dash != null) {
            if (this.dash_phase != basicStroke.dash_phase || !Arrays.equals(this.dash, basicStroke.dash)) {
                return false;
            }
            return true;
        }
        if (basicStroke.dash != null) {
            return false;
        }
        return true;
    }
}
