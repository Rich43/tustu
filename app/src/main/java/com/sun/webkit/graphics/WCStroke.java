package com.sun.webkit.graphics;

import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCStroke.class */
public abstract class WCStroke<P, S> {
    public static final int NO_STROKE = 0;
    public static final int SOLID_STROKE = 1;
    public static final int DOTTED_STROKE = 2;
    public static final int DASHED_STROKE = 3;
    public static final int BUTT_CAP = 0;
    public static final int ROUND_CAP = 1;
    public static final int SQUARE_CAP = 2;
    public static final int MITER_JOIN = 0;
    public static final int ROUND_JOIN = 1;
    public static final int BEVEL_JOIN = 2;
    private int style = 1;
    private int lineCap = 0;
    private int lineJoin = 0;
    private float miterLimit = 10.0f;
    private float thickness = 1.0f;
    private float offset;
    private float[] sizes;
    private P paint;

    protected abstract void invalidate();

    public abstract S getPlatformStroke();

    public void copyFrom(WCStroke<P, S> stroke) {
        this.style = stroke.style;
        this.lineCap = stroke.lineCap;
        this.lineJoin = stroke.lineJoin;
        this.miterLimit = stroke.miterLimit;
        this.thickness = stroke.thickness;
        this.offset = stroke.offset;
        this.sizes = stroke.sizes;
        this.paint = stroke.paint;
    }

    public void setStyle(int style) {
        if (style != 1 && style != 2 && style != 3) {
            style = 0;
        }
        if (this.style != style) {
            this.style = style;
            invalidate();
        }
    }

    public void setLineCap(int lineCap) {
        if (lineCap != 1 && lineCap != 2) {
            lineCap = 0;
        }
        if (this.lineCap != lineCap) {
            this.lineCap = lineCap;
            invalidate();
        }
    }

    public void setLineJoin(int lineJoin) {
        if (lineJoin != 1 && lineJoin != 2) {
            lineJoin = 0;
        }
        if (this.lineJoin != lineJoin) {
            this.lineJoin = lineJoin;
            invalidate();
        }
    }

    public void setMiterLimit(float miterLimit) {
        if (miterLimit < 1.0f) {
            miterLimit = 1.0f;
        }
        if (this.miterLimit != miterLimit) {
            this.miterLimit = miterLimit;
            invalidate();
        }
    }

    public void setThickness(float thickness) {
        if (thickness < 0.0f) {
            thickness = 1.0f;
        }
        if (this.thickness != thickness) {
            this.thickness = thickness;
            invalidate();
        }
    }

    public void setDashOffset(float offset) {
        if (this.offset != offset) {
            this.offset = offset;
            invalidate();
        }
    }

    public void setDashSizes(float... sizes) {
        if (sizes == null || sizes.length == 0) {
            if (this.sizes != null) {
                this.sizes = null;
                invalidate();
                return;
            }
            return;
        }
        if (!Arrays.equals(this.sizes, sizes)) {
            this.sizes = (float[]) sizes.clone();
            invalidate();
        }
    }

    public void setPaint(P paint) {
        this.paint = paint;
    }

    public int getStyle() {
        return this.style;
    }

    public int getLineCap() {
        return this.lineCap;
    }

    public int getLineJoin() {
        return this.lineJoin;
    }

    public float getMiterLimit() {
        return this.miterLimit;
    }

    public float getThickness() {
        return this.thickness;
    }

    public float getDashOffset() {
        return this.offset;
    }

    public float[] getDashSizes() {
        if (this.sizes != null) {
            return (float[]) this.sizes.clone();
        }
        return null;
    }

    public P getPaint() {
        return this.paint;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("[style=").append(this.style);
        sb.append(", lineCap=").append(this.lineCap);
        sb.append(", lineJoin=").append(this.lineJoin);
        sb.append(", miterLimit=").append(this.miterLimit);
        sb.append(", thickness=").append(this.thickness);
        sb.append(", offset=").append(this.offset);
        sb.append(", sizes=").append(Arrays.toString(this.sizes));
        sb.append(", paint=").append((Object) this.paint);
        return sb.append("]").toString();
    }
}
