package com.sun.prism.paint;

import com.sun.prism.paint.Paint;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/paint/Color.class */
public final class Color extends Paint {
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    public static final Color TRANSPARENT = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    private final int argb;

    /* renamed from: r, reason: collision with root package name */
    private final float f12024r;

    /* renamed from: g, reason: collision with root package name */
    private final float f12025g;

    /* renamed from: b, reason: collision with root package name */
    private final float f12026b;

    /* renamed from: a, reason: collision with root package name */
    private final float f12027a;

    public Color(float r2, float g2, float b2, float a2) {
        super(Paint.Type.COLOR, false, false);
        int ia = (int) (255.0d * a2);
        int ir = (int) (255.0d * r2 * a2);
        int ig = (int) (255.0d * g2 * a2);
        int ib = (int) (255.0d * b2 * a2);
        this.argb = (ia << 24) | (ir << 16) | (ig << 8) | (ib << 0);
        this.f12024r = r2;
        this.f12025g = g2;
        this.f12026b = b2;
        this.f12027a = a2;
    }

    public int getIntArgbPre() {
        return this.argb;
    }

    public void putRgbaPreBytes(byte[] arr, int offset) {
        arr[offset + 0] = (byte) ((this.argb >> 16) & 255);
        arr[offset + 1] = (byte) ((this.argb >> 8) & 255);
        arr[offset + 2] = (byte) (this.argb & 255);
        arr[offset + 3] = (byte) ((this.argb >> 24) & 255);
    }

    public void putBgraPreBytes(ByteBuffer buf) {
        buf.put((byte) (this.argb & 255));
        buf.put((byte) ((this.argb >> 8) & 255));
        buf.put((byte) ((this.argb >> 16) & 255));
        buf.put((byte) ((this.argb >> 24) & 255));
    }

    public float getRed() {
        return this.f12024r;
    }

    public float getRedPremult() {
        return this.f12024r * this.f12027a;
    }

    public float getGreen() {
        return this.f12025g;
    }

    public float getGreenPremult() {
        return this.f12025g * this.f12027a;
    }

    public float getBlue() {
        return this.f12026b;
    }

    public float getBluePremult() {
        return this.f12026b * this.f12027a;
    }

    public float getAlpha() {
        return this.f12027a;
    }

    @Override // com.sun.prism.paint.Paint
    public boolean isOpaque() {
        return this.f12027a >= 1.0f;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Color)) {
            return false;
        }
        Color other = (Color) obj;
        return this.f12024r == other.f12024r && this.f12025g == other.f12025g && this.f12026b == other.f12026b && this.f12027a == other.f12027a;
    }

    public int hashCode() {
        int hash = (53 * 3) + Float.floatToIntBits(this.f12024r);
        return (53 * ((53 * ((53 * hash) + Float.floatToIntBits(this.f12025g))) + Float.floatToIntBits(this.f12026b))) + Float.floatToIntBits(this.f12027a);
    }

    public String toString() {
        return "Color[r=" + this.f12024r + ", g=" + this.f12025g + ", b=" + this.f12026b + ", a=" + this.f12027a + "]";
    }
}
