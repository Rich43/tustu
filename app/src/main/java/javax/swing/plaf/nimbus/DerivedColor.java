package javax.swing.plaf.nimbus;

import java.awt.Color;
import javax.swing.UIManager;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/DerivedColor.class */
class DerivedColor extends Color {
    private final String uiDefaultParentName;
    private final float hOffset;
    private final float sOffset;
    private final float bOffset;
    private final int aOffset;
    private int argbValue;

    DerivedColor(String str, float f2, float f3, float f4, int i2) {
        super(0);
        this.uiDefaultParentName = str;
        this.hOffset = f2;
        this.sOffset = f3;
        this.bOffset = f4;
        this.aOffset = i2;
    }

    public String getUiDefaultParentName() {
        return this.uiDefaultParentName;
    }

    public float getHueOffset() {
        return this.hOffset;
    }

    public float getSaturationOffset() {
        return this.sOffset;
    }

    public float getBrightnessOffset() {
        return this.bOffset;
    }

    public int getAlphaOffset() {
        return this.aOffset;
    }

    public void rederiveColor() {
        Color color = UIManager.getColor(this.uiDefaultParentName);
        if (color != null) {
            float[] fArrRGBtoHSB = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            fArrRGBtoHSB[0] = clamp(fArrRGBtoHSB[0] + this.hOffset);
            fArrRGBtoHSB[1] = clamp(fArrRGBtoHSB[1] + this.sOffset);
            fArrRGBtoHSB[2] = clamp(fArrRGBtoHSB[2] + this.bOffset);
            this.argbValue = (Color.HSBtoRGB(fArrRGBtoHSB[0], fArrRGBtoHSB[1], fArrRGBtoHSB[2]) & 16777215) | (clamp(color.getAlpha() + this.aOffset) << 24);
            return;
        }
        float[] fArr = {clamp(this.hOffset), clamp(this.sOffset), clamp(this.bOffset)};
        this.argbValue = (Color.HSBtoRGB(fArr[0], fArr[1], fArr[2]) & 16777215) | (clamp(this.aOffset) << 24);
    }

    @Override // java.awt.Color
    public int getRGB() {
        return this.argbValue;
    }

    @Override // java.awt.Color
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DerivedColor)) {
            return false;
        }
        DerivedColor derivedColor = (DerivedColor) obj;
        return this.aOffset == derivedColor.aOffset && Float.compare(derivedColor.bOffset, this.bOffset) == 0 && Float.compare(derivedColor.hOffset, this.hOffset) == 0 && Float.compare(derivedColor.sOffset, this.sOffset) == 0 && this.uiDefaultParentName.equals(derivedColor.uiDefaultParentName);
    }

    @Override // java.awt.Color
    public int hashCode() {
        return (31 * (((float) (31 * (((((float) (31 * (((((float) (31 * this.uiDefaultParentName.hashCode())) + this.hOffset) > 0.0f ? 1 : ((((float) (31 * this.uiDefaultParentName.hashCode())) + this.hOffset) == 0.0f ? 0 : -1)) != 0 ? Float.floatToIntBits(this.hOffset) : 0))) + this.sOffset) > 0.0f ? 1 : ((((float) (31 * (((((float) (31 * this.uiDefaultParentName.hashCode())) + this.hOffset) > 0.0f ? 1 : ((((float) (31 * this.uiDefaultParentName.hashCode())) + this.hOffset) == 0.0f ? 0 : -1)) != 0 ? Float.floatToIntBits(this.hOffset) : 0))) + this.sOffset) == 0.0f ? 0 : -1)) != 0 ? Float.floatToIntBits(this.sOffset) : 0))) + this.bOffset != 0.0f ? Float.floatToIntBits(this.bOffset) : 0)) + this.aOffset;
    }

    private float clamp(float f2) {
        if (f2 < 0.0f) {
            f2 = 0.0f;
        } else if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        return f2;
    }

    private int clamp(int i2) {
        if (i2 < 0) {
            i2 = 0;
        } else if (i2 > 255) {
            i2 = 255;
        }
        return i2;
    }

    @Override // java.awt.Color
    public String toString() {
        Color color = UIManager.getColor(this.uiDefaultParentName);
        String str = "DerivedColor(color=" + getRed() + "," + getGreen() + "," + getBlue() + " parent=" + this.uiDefaultParentName + " offsets=" + getHueOffset() + "," + getSaturationOffset() + "," + getBrightnessOffset() + "," + getAlphaOffset();
        return color == null ? str : str + " pColor=" + color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/DerivedColor$UIResource.class */
    static class UIResource extends DerivedColor implements javax.swing.plaf.UIResource {
        UIResource(String str, float f2, float f3, float f4, int i2) {
            super(str, f2, f3, f4, i2);
        }

        @Override // javax.swing.plaf.nimbus.DerivedColor, java.awt.Color
        public boolean equals(Object obj) {
            return (obj instanceof UIResource) && super.equals(obj);
        }

        @Override // javax.swing.plaf.nimbus.DerivedColor, java.awt.Color
        public int hashCode() {
            return super.hashCode() + 7;
        }
    }
}
