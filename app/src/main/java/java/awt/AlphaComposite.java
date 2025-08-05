package java.awt;

import java.awt.image.ColorModel;
import sun.java2d.SunCompositeContext;

/* loaded from: rt.jar:java/awt/AlphaComposite.class */
public final class AlphaComposite implements Composite {
    public static final int CLEAR = 1;
    public static final int SRC = 2;
    public static final int DST = 9;
    public static final int SRC_OVER = 3;
    public static final int DST_OVER = 4;
    public static final int SRC_IN = 5;
    public static final int DST_IN = 6;
    public static final int SRC_OUT = 7;
    public static final int DST_OUT = 8;
    public static final int SRC_ATOP = 10;
    public static final int DST_ATOP = 11;
    public static final int XOR = 12;
    public static final AlphaComposite Clear = new AlphaComposite(1);
    public static final AlphaComposite Src = new AlphaComposite(2);
    public static final AlphaComposite Dst = new AlphaComposite(9);
    public static final AlphaComposite SrcOver = new AlphaComposite(3);
    public static final AlphaComposite DstOver = new AlphaComposite(4);
    public static final AlphaComposite SrcIn = new AlphaComposite(5);
    public static final AlphaComposite DstIn = new AlphaComposite(6);
    public static final AlphaComposite SrcOut = new AlphaComposite(7);
    public static final AlphaComposite DstOut = new AlphaComposite(8);
    public static final AlphaComposite SrcAtop = new AlphaComposite(10);
    public static final AlphaComposite DstAtop = new AlphaComposite(11);
    public static final AlphaComposite Xor = new AlphaComposite(12);
    private static final int MIN_RULE = 1;
    private static final int MAX_RULE = 12;
    float extraAlpha;
    int rule;

    private AlphaComposite(int i2) {
        this(i2, 1.0f);
    }

    private AlphaComposite(int i2, float f2) {
        if (i2 < 1 || i2 > 12) {
            throw new IllegalArgumentException("unknown composite rule");
        }
        if (f2 >= 0.0f && f2 <= 1.0f) {
            this.rule = i2;
            this.extraAlpha = f2;
            return;
        }
        throw new IllegalArgumentException("alpha value out of range");
    }

    public static AlphaComposite getInstance(int i2) {
        switch (i2) {
            case 1:
                return Clear;
            case 2:
                return Src;
            case 3:
                return SrcOver;
            case 4:
                return DstOver;
            case 5:
                return SrcIn;
            case 6:
                return DstIn;
            case 7:
                return SrcOut;
            case 8:
                return DstOut;
            case 9:
                return Dst;
            case 10:
                return SrcAtop;
            case 11:
                return DstAtop;
            case 12:
                return Xor;
            default:
                throw new IllegalArgumentException("unknown composite rule");
        }
    }

    public static AlphaComposite getInstance(int i2, float f2) {
        if (f2 == 1.0f) {
            return getInstance(i2);
        }
        return new AlphaComposite(i2, f2);
    }

    @Override // java.awt.Composite
    public CompositeContext createContext(ColorModel colorModel, ColorModel colorModel2, RenderingHints renderingHints) {
        return new SunCompositeContext(this, colorModel, colorModel2);
    }

    public float getAlpha() {
        return this.extraAlpha;
    }

    public int getRule() {
        return this.rule;
    }

    public AlphaComposite derive(int i2) {
        return this.rule == i2 ? this : getInstance(i2, this.extraAlpha);
    }

    public AlphaComposite derive(float f2) {
        return this.extraAlpha == f2 ? this : getInstance(this.rule, f2);
    }

    public int hashCode() {
        return (Float.floatToIntBits(this.extraAlpha) * 31) + this.rule;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AlphaComposite)) {
            return false;
        }
        AlphaComposite alphaComposite = (AlphaComposite) obj;
        if (this.rule != alphaComposite.rule || this.extraAlpha != alphaComposite.extraAlpha) {
            return false;
        }
        return true;
    }
}
