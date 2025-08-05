package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/MediaPrintableArea.class */
public final class MediaPrintableArea implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {

    /* renamed from: x, reason: collision with root package name */
    private int f12783x;

    /* renamed from: y, reason: collision with root package name */
    private int f12784y;

    /* renamed from: w, reason: collision with root package name */
    private int f12785w;

    /* renamed from: h, reason: collision with root package name */
    private int f12786h;
    private int units;
    private static final long serialVersionUID = -1597171464050795793L;
    public static final int INCH = 25400;
    public static final int MM = 1000;

    public MediaPrintableArea(float f2, float f3, float f4, float f5, int i2) {
        if (f2 < 0.0d || f3 < 0.0d || f4 <= 0.0d || f5 <= 0.0d || i2 < 1) {
            throw new IllegalArgumentException("0 or negative value argument");
        }
        this.f12783x = (int) ((f2 * i2) + 0.5f);
        this.f12784y = (int) ((f3 * i2) + 0.5f);
        this.f12785w = (int) ((f4 * i2) + 0.5f);
        this.f12786h = (int) ((f5 * i2) + 0.5f);
    }

    public MediaPrintableArea(int i2, int i3, int i4, int i5, int i6) {
        if (i2 < 0 || i3 < 0 || i4 <= 0 || i5 <= 0 || i6 < 1) {
            throw new IllegalArgumentException("0 or negative value argument");
        }
        this.f12783x = i2 * i6;
        this.f12784y = i3 * i6;
        this.f12785w = i4 * i6;
        this.f12786h = i5 * i6;
    }

    public float[] getPrintableArea(int i2) {
        return new float[]{getX(i2), getY(i2), getWidth(i2), getHeight(i2)};
    }

    public float getX(int i2) {
        return convertFromMicrometers(this.f12783x, i2);
    }

    public float getY(int i2) {
        return convertFromMicrometers(this.f12784y, i2);
    }

    public float getWidth(int i2) {
        return convertFromMicrometers(this.f12785w, i2);
    }

    public float getHeight(int i2) {
        return convertFromMicrometers(this.f12786h, i2);
    }

    public boolean equals(Object obj) {
        boolean z2 = false;
        if (obj instanceof MediaPrintableArea) {
            MediaPrintableArea mediaPrintableArea = (MediaPrintableArea) obj;
            if (this.f12783x == mediaPrintableArea.f12783x && this.f12784y == mediaPrintableArea.f12784y && this.f12785w == mediaPrintableArea.f12785w && this.f12786h == mediaPrintableArea.f12786h) {
                z2 = true;
            }
        }
        return z2;
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return MediaPrintableArea.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "media-printable-area";
    }

    public String toString(int i2, String str) {
        if (str == null) {
            str = "";
        }
        float[] printableArea = getPrintableArea(i2);
        return ("(" + printableArea[0] + "," + printableArea[1] + ")->(" + printableArea[2] + "," + printableArea[3] + ")") + str;
    }

    public String toString() {
        return toString(1000, "mm");
    }

    public int hashCode() {
        return this.f12783x + (37 * this.f12784y) + (43 * this.f12785w) + (47 * this.f12786h);
    }

    private static float convertFromMicrometers(int i2, int i3) {
        if (i3 < 1) {
            throw new IllegalArgumentException("units is < 1");
        }
        return i2 / i3;
    }
}
