package java.awt;

import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import org.icepdf.core.pobjects.graphics.SoftMask;

/* loaded from: rt.jar:java/awt/Color.class */
public class Color implements Paint, Serializable {
    public static final Color white = new Color(255, 255, 255);
    public static final Color WHITE = white;
    public static final Color lightGray = new Color(192, 192, 192);
    public static final Color LIGHT_GRAY = lightGray;
    public static final Color gray = new Color(128, 128, 128);
    public static final Color GRAY = gray;
    public static final Color darkGray = new Color(64, 64, 64);
    public static final Color DARK_GRAY = darkGray;
    public static final Color black = new Color(0, 0, 0);
    public static final Color BLACK = black;
    public static final Color red = new Color(255, 0, 0);
    public static final Color RED = red;
    public static final Color pink = new Color(255, 175, 175);
    public static final Color PINK = pink;
    public static final Color orange = new Color(255, 200, 0);
    public static final Color ORANGE = orange;
    public static final Color yellow = new Color(255, 255, 0);
    public static final Color YELLOW = yellow;
    public static final Color green = new Color(0, 255, 0);
    public static final Color GREEN = green;
    public static final Color magenta = new Color(255, 0, 255);
    public static final Color MAGENTA = magenta;
    public static final Color cyan = new Color(0, 255, 255);
    public static final Color CYAN = cyan;
    public static final Color blue = new Color(0, 0, 255);
    public static final Color BLUE = blue;
    int value;
    private float[] frgbvalue;
    private float[] fvalue;
    private float falpha;
    private ColorSpace cs;
    private static final long serialVersionUID = 118526816881161077L;
    private static final double FACTOR = 0.7d;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    private static void testColorValueRange(int i2, int i3, int i4, int i5) {
        boolean z2 = false;
        String str = "";
        if (i5 < 0 || i5 > 255) {
            z2 = true;
            str = str + " Alpha";
        }
        if (i2 < 0 || i2 > 255) {
            z2 = true;
            str = str + " Red";
        }
        if (i3 < 0 || i3 > 255) {
            z2 = true;
            str = str + " Green";
        }
        if (i4 < 0 || i4 > 255) {
            z2 = true;
            str = str + " Blue";
        }
        if (z2) {
            throw new IllegalArgumentException("Color parameter outside of expected range:" + str);
        }
    }

    private static void testColorValueRange(float f2, float f3, float f4, float f5) {
        boolean z2 = false;
        String str = "";
        if (f5 < 0.0d || f5 > 1.0d) {
            z2 = true;
            str = str + " Alpha";
        }
        if (f2 < 0.0d || f2 > 1.0d) {
            z2 = true;
            str = str + " Red";
        }
        if (f3 < 0.0d || f3 > 1.0d) {
            z2 = true;
            str = str + " Green";
        }
        if (f4 < 0.0d || f4 > 1.0d) {
            z2 = true;
            str = str + " Blue";
        }
        if (z2) {
            throw new IllegalArgumentException("Color parameter outside of expected range:" + str);
        }
    }

    public Color(int i2, int i3, int i4) {
        this(i2, i3, i4, 255);
    }

    @ConstructorProperties({"red", "green", "blue", "alpha"})
    public Color(int i2, int i3, int i4, int i5) {
        this.frgbvalue = null;
        this.fvalue = null;
        this.falpha = 0.0f;
        this.cs = null;
        this.value = ((i5 & 255) << 24) | ((i2 & 255) << 16) | ((i3 & 255) << 8) | ((i4 & 255) << 0);
        testColorValueRange(i2, i3, i4, i5);
    }

    public Color(int i2) {
        this.frgbvalue = null;
        this.fvalue = null;
        this.falpha = 0.0f;
        this.cs = null;
        this.value = (-16777216) | i2;
    }

    public Color(int i2, boolean z2) {
        this.frgbvalue = null;
        this.fvalue = null;
        this.falpha = 0.0f;
        this.cs = null;
        if (z2) {
            this.value = i2;
        } else {
            this.value = (-16777216) | i2;
        }
    }

    public Color(float f2, float f3, float f4) {
        this((int) ((f2 * 255.0f) + 0.5d), (int) ((f3 * 255.0f) + 0.5d), (int) ((f4 * 255.0f) + 0.5d));
        testColorValueRange(f2, f3, f4, 1.0f);
        this.frgbvalue = new float[3];
        this.frgbvalue[0] = f2;
        this.frgbvalue[1] = f3;
        this.frgbvalue[2] = f4;
        this.falpha = 1.0f;
        this.fvalue = this.frgbvalue;
    }

    public Color(float f2, float f3, float f4, float f5) {
        this((int) ((f2 * 255.0f) + 0.5d), (int) ((f3 * 255.0f) + 0.5d), (int) ((f4 * 255.0f) + 0.5d), (int) ((f5 * 255.0f) + 0.5d));
        this.frgbvalue = new float[3];
        this.frgbvalue[0] = f2;
        this.frgbvalue[1] = f3;
        this.frgbvalue[2] = f4;
        this.falpha = f5;
        this.fvalue = this.frgbvalue;
    }

    public Color(ColorSpace colorSpace, float[] fArr, float f2) {
        this.frgbvalue = null;
        this.fvalue = null;
        this.falpha = 0.0f;
        this.cs = null;
        boolean z2 = false;
        String str = "";
        int numComponents = colorSpace.getNumComponents();
        this.fvalue = new float[numComponents];
        for (int i2 = 0; i2 < numComponents; i2++) {
            if (fArr[i2] < 0.0d || fArr[i2] > 1.0d) {
                z2 = true;
                str = str + "Component " + i2 + " ";
            } else {
                this.fvalue[i2] = fArr[i2];
            }
        }
        if (f2 < 0.0d || f2 > 1.0d) {
            z2 = true;
            str = str + SoftMask.SOFT_MASK_TYPE_ALPHA;
        } else {
            this.falpha = f2;
        }
        if (z2) {
            throw new IllegalArgumentException("Color parameter outside of expected range: " + str);
        }
        this.frgbvalue = colorSpace.toRGB(this.fvalue);
        this.cs = colorSpace;
        this.value = ((((int) (this.falpha * 255.0f)) & 255) << 24) | ((((int) (this.frgbvalue[0] * 255.0f)) & 255) << 16) | ((((int) (this.frgbvalue[1] * 255.0f)) & 255) << 8) | ((((int) (this.frgbvalue[2] * 255.0f)) & 255) << 0);
    }

    public int getRed() {
        return (getRGB() >> 16) & 255;
    }

    public int getGreen() {
        return (getRGB() >> 8) & 255;
    }

    public int getBlue() {
        return (getRGB() >> 0) & 255;
    }

    public int getAlpha() {
        return (getRGB() >> 24) & 255;
    }

    public int getRGB() {
        return this.value;
    }

    public Color brighter() {
        int red2 = getRed();
        int green2 = getGreen();
        int blue2 = getBlue();
        int alpha = getAlpha();
        if (red2 == 0 && green2 == 0 && blue2 == 0) {
            return new Color(3, 3, 3, alpha);
        }
        if (red2 > 0 && red2 < 3) {
            red2 = 3;
        }
        if (green2 > 0 && green2 < 3) {
            green2 = 3;
        }
        if (blue2 > 0 && blue2 < 3) {
            blue2 = 3;
        }
        return new Color(Math.min((int) (red2 / FACTOR), 255), Math.min((int) (green2 / FACTOR), 255), Math.min((int) (blue2 / FACTOR), 255), alpha);
    }

    public Color darker() {
        return new Color(Math.max((int) (getRed() * FACTOR), 0), Math.max((int) (getGreen() * FACTOR), 0), Math.max((int) (getBlue() * FACTOR), 0), getAlpha());
    }

    public int hashCode() {
        return this.value;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Color) && ((Color) obj).getRGB() == getRGB();
    }

    public String toString() {
        return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
    }

    public static Color decode(String str) throws NumberFormatException {
        int iIntValue = Integer.decode(str).intValue();
        return new Color((iIntValue >> 16) & 255, (iIntValue >> 8) & 255, iIntValue & 255);
    }

    public static Color getColor(String str) {
        return getColor(str, (Color) null);
    }

    public static Color getColor(String str, Color color) {
        Integer integer = Integer.getInteger(str);
        if (integer == null) {
            return color;
        }
        int iIntValue = integer.intValue();
        return new Color((iIntValue >> 16) & 255, (iIntValue >> 8) & 255, iIntValue & 255);
    }

    public static Color getColor(String str, int i2) {
        Integer integer = Integer.getInteger(str);
        int iIntValue = integer != null ? integer.intValue() : i2;
        return new Color((iIntValue >> 16) & 255, (iIntValue >> 8) & 255, (iIntValue >> 0) & 255);
    }

    public static int HSBtoRGB(float f2, float f3, float f4) {
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        if (f3 == 0.0f) {
            int i5 = (int) ((f4 * 255.0f) + 0.5f);
            i4 = i5;
            i3 = i5;
            i2 = i5;
        } else {
            float fFloor = (f2 - ((float) Math.floor(f2))) * 6.0f;
            float fFloor2 = fFloor - ((float) Math.floor(fFloor));
            float f5 = f4 * (1.0f - f3);
            float f6 = f4 * (1.0f - (f3 * fFloor2));
            float f7 = f4 * (1.0f - (f3 * (1.0f - fFloor2)));
            switch ((int) fFloor) {
                case 0:
                    i2 = (int) ((f4 * 255.0f) + 0.5f);
                    i3 = (int) ((f7 * 255.0f) + 0.5f);
                    i4 = (int) ((f5 * 255.0f) + 0.5f);
                    break;
                case 1:
                    i2 = (int) ((f6 * 255.0f) + 0.5f);
                    i3 = (int) ((f4 * 255.0f) + 0.5f);
                    i4 = (int) ((f5 * 255.0f) + 0.5f);
                    break;
                case 2:
                    i2 = (int) ((f5 * 255.0f) + 0.5f);
                    i3 = (int) ((f4 * 255.0f) + 0.5f);
                    i4 = (int) ((f7 * 255.0f) + 0.5f);
                    break;
                case 3:
                    i2 = (int) ((f5 * 255.0f) + 0.5f);
                    i3 = (int) ((f6 * 255.0f) + 0.5f);
                    i4 = (int) ((f4 * 255.0f) + 0.5f);
                    break;
                case 4:
                    i2 = (int) ((f7 * 255.0f) + 0.5f);
                    i3 = (int) ((f5 * 255.0f) + 0.5f);
                    i4 = (int) ((f4 * 255.0f) + 0.5f);
                    break;
                case 5:
                    i2 = (int) ((f4 * 255.0f) + 0.5f);
                    i3 = (int) ((f5 * 255.0f) + 0.5f);
                    i4 = (int) ((f6 * 255.0f) + 0.5f);
                    break;
            }
        }
        return (-16777216) | (i2 << 16) | (i3 << 8) | (i4 << 0);
    }

    public static float[] RGBtoHSB(int i2, int i3, int i4, float[] fArr) {
        float f2;
        float f3;
        float f4;
        if (fArr == null) {
            fArr = new float[3];
        }
        int i5 = i2 > i3 ? i2 : i3;
        if (i4 > i5) {
            i5 = i4;
        }
        int i6 = i2 < i3 ? i2 : i3;
        if (i4 < i6) {
            i6 = i4;
        }
        float f5 = i5 / 255.0f;
        if (i5 != 0) {
            f2 = (i5 - i6) / i5;
        } else {
            f2 = 0.0f;
        }
        if (f2 == 0.0f) {
            f4 = 0.0f;
        } else {
            float f6 = (i5 - i2) / (i5 - i6);
            float f7 = (i5 - i3) / (i5 - i6);
            float f8 = (i5 - i4) / (i5 - i6);
            if (i2 == i5) {
                f3 = f8 - f7;
            } else if (i3 == i5) {
                f3 = (2.0f + f6) - f8;
            } else {
                f3 = (4.0f + f7) - f6;
            }
            f4 = f3 / 6.0f;
            if (f4 < 0.0f) {
                f4 += 1.0f;
            }
        }
        fArr[0] = f4;
        fArr[1] = f2;
        fArr[2] = f5;
        return fArr;
    }

    public static Color getHSBColor(float f2, float f3, float f4) {
        return new Color(HSBtoRGB(f2, f3, f4));
    }

    public float[] getRGBComponents(float[] fArr) {
        float[] fArr2;
        if (fArr == null) {
            fArr2 = new float[4];
        } else {
            fArr2 = fArr;
        }
        if (this.frgbvalue == null) {
            fArr2[0] = getRed() / 255.0f;
            fArr2[1] = getGreen() / 255.0f;
            fArr2[2] = getBlue() / 255.0f;
            fArr2[3] = getAlpha() / 255.0f;
        } else {
            fArr2[0] = this.frgbvalue[0];
            fArr2[1] = this.frgbvalue[1];
            fArr2[2] = this.frgbvalue[2];
            fArr2[3] = this.falpha;
        }
        return fArr2;
    }

    public float[] getRGBColorComponents(float[] fArr) {
        float[] fArr2;
        if (fArr == null) {
            fArr2 = new float[3];
        } else {
            fArr2 = fArr;
        }
        if (this.frgbvalue == null) {
            fArr2[0] = getRed() / 255.0f;
            fArr2[1] = getGreen() / 255.0f;
            fArr2[2] = getBlue() / 255.0f;
        } else {
            fArr2[0] = this.frgbvalue[0];
            fArr2[1] = this.frgbvalue[1];
            fArr2[2] = this.frgbvalue[2];
        }
        return fArr2;
    }

    public float[] getComponents(float[] fArr) {
        float[] fArr2;
        if (this.fvalue == null) {
            return getRGBComponents(fArr);
        }
        int length = this.fvalue.length;
        if (fArr == null) {
            fArr2 = new float[length + 1];
        } else {
            fArr2 = fArr;
        }
        for (int i2 = 0; i2 < length; i2++) {
            fArr2[i2] = this.fvalue[i2];
        }
        fArr2[length] = this.falpha;
        return fArr2;
    }

    public float[] getColorComponents(float[] fArr) {
        float[] fArr2;
        if (this.fvalue == null) {
            return getRGBColorComponents(fArr);
        }
        int length = this.fvalue.length;
        if (fArr == null) {
            fArr2 = new float[length];
        } else {
            fArr2 = fArr;
        }
        for (int i2 = 0; i2 < length; i2++) {
            fArr2[i2] = this.fvalue[i2];
        }
        return fArr2;
    }

    public float[] getComponents(ColorSpace colorSpace, float[] fArr) {
        float[] fArr2;
        if (this.cs == null) {
            this.cs = ColorSpace.getInstance(1000);
        }
        if (this.fvalue == null) {
            fArr2 = new float[]{getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f};
        } else {
            fArr2 = this.fvalue;
        }
        float[] fArrFromCIEXYZ = colorSpace.fromCIEXYZ(this.cs.toCIEXYZ(fArr2));
        if (fArr == null) {
            fArr = new float[fArrFromCIEXYZ.length + 1];
        }
        for (int i2 = 0; i2 < fArrFromCIEXYZ.length; i2++) {
            fArr[i2] = fArrFromCIEXYZ[i2];
        }
        if (this.fvalue == null) {
            fArr[fArrFromCIEXYZ.length] = getAlpha() / 255.0f;
        } else {
            fArr[fArrFromCIEXYZ.length] = this.falpha;
        }
        return fArr;
    }

    public float[] getColorComponents(ColorSpace colorSpace, float[] fArr) {
        float[] fArr2;
        if (this.cs == null) {
            this.cs = ColorSpace.getInstance(1000);
        }
        if (this.fvalue == null) {
            fArr2 = new float[]{getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f};
        } else {
            fArr2 = this.fvalue;
        }
        float[] fArrFromCIEXYZ = colorSpace.fromCIEXYZ(this.cs.toCIEXYZ(fArr2));
        if (fArr == null) {
            return fArrFromCIEXYZ;
        }
        for (int i2 = 0; i2 < fArrFromCIEXYZ.length; i2++) {
            fArr[i2] = fArrFromCIEXYZ[i2];
        }
        return fArr;
    }

    public ColorSpace getColorSpace() {
        if (this.cs == null) {
            this.cs = ColorSpace.getInstance(1000);
        }
        return this.cs;
    }

    @Override // java.awt.Paint
    public synchronized PaintContext createContext(ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints) {
        return new ColorPaintContext(getRGB(), colorModel);
    }

    @Override // java.awt.Transparency
    public int getTransparency() {
        int alpha = getAlpha();
        if (alpha == 255) {
            return 1;
        }
        if (alpha == 0) {
            return 2;
        }
        return 3;
    }
}
