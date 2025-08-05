package javax.print.attribute;

import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/Size2DSyntax.class */
public abstract class Size2DSyntax implements Serializable, Cloneable {
    private static final long serialVersionUID = 5584439964938660530L;

    /* renamed from: x, reason: collision with root package name */
    private int f12781x;

    /* renamed from: y, reason: collision with root package name */
    private int f12782y;
    public static final int INCH = 25400;
    public static final int MM = 1000;

    protected Size2DSyntax(float f2, float f3, int i2) {
        if (f2 < 0.0f) {
            throw new IllegalArgumentException("x < 0");
        }
        if (f3 < 0.0f) {
            throw new IllegalArgumentException("y < 0");
        }
        if (i2 < 1) {
            throw new IllegalArgumentException("units < 1");
        }
        this.f12781x = (int) ((f2 * i2) + 0.5f);
        this.f12782y = (int) ((f3 * i2) + 0.5f);
    }

    protected Size2DSyntax(int i2, int i3, int i4) {
        if (i2 < 0) {
            throw new IllegalArgumentException("x < 0");
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("y < 0");
        }
        if (i4 < 1) {
            throw new IllegalArgumentException("units < 1");
        }
        this.f12781x = i2 * i4;
        this.f12782y = i3 * i4;
    }

    private static float convertFromMicrometers(int i2, int i3) {
        if (i3 < 1) {
            throw new IllegalArgumentException("units is < 1");
        }
        return i2 / i3;
    }

    public float[] getSize(int i2) {
        return new float[]{getX(i2), getY(i2)};
    }

    public float getX(int i2) {
        return convertFromMicrometers(this.f12781x, i2);
    }

    public float getY(int i2) {
        return convertFromMicrometers(this.f12782y, i2);
    }

    public String toString(int i2, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getX(i2));
        stringBuffer.append('x');
        stringBuffer.append(getY(i2));
        if (str != null) {
            stringBuffer.append(' ');
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof Size2DSyntax) && this.f12781x == ((Size2DSyntax) obj).f12781x && this.f12782y == ((Size2DSyntax) obj).f12782y;
    }

    public int hashCode() {
        return (this.f12781x & 65535) | ((this.f12782y & 65535) << 16);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.f12781x);
        stringBuffer.append('x');
        stringBuffer.append(this.f12782y);
        stringBuffer.append(" um");
        return stringBuffer.toString();
    }

    protected int getXMicrometers() {
        return this.f12781x;
    }

    protected int getYMicrometers() {
        return this.f12782y;
    }
}
