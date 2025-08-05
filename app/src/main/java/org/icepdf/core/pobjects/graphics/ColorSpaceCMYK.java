package org.icepdf.core.pobjects.graphics;

import java.awt.color.ColorSpace;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ColorSpaceCMYK.class */
public class ColorSpaceCMYK extends ColorSpace {
    private static final String[] NAMES = {"Cyan", "Magenta", "Yellow", "Black"};
    private static final ColorSpace COLOR_SPACE_sRGB = ColorSpace.getInstance(1000);
    private float[] _rgbValues;

    public ColorSpaceCMYK() {
        super(9, 4);
        this._rgbValues = new float[4];
    }

    @Override // java.awt.color.ColorSpace
    public int getNumComponents() {
        return 4;
    }

    @Override // java.awt.color.ColorSpace
    public String getName(int index) {
        return NAMES[index];
    }

    @Override // java.awt.color.ColorSpace
    public int getType() {
        return 9;
    }

    @Override // java.awt.color.ColorSpace
    public boolean isCS_sRGB() {
        return false;
    }

    @Override // java.awt.color.ColorSpace
    public float[] fromRGB(float[] rgbValues) {
        float c2 = 1.0f - rgbValues[0];
        float m2 = 1.0f - rgbValues[1];
        float y2 = 1.0f - rgbValues[2];
        float k2 = Math.min(c2, Math.min(m2, y2));
        float km = Math.max(c2, Math.max(m2, y2));
        if (km > k2) {
            k2 = ((k2 * k2) * k2) / (km * km);
        }
        float[] cmykValues = {c2 - k2, m2 - k2, y2 - k2, k2};
        return cmykValues;
    }

    @Override // java.awt.color.ColorSpace
    public float[] toRGB(float[] cmykValues) {
        float c2 = cmykValues[0];
        float m2 = cmykValues[1];
        float y2 = cmykValues[2];
        float k2 = cmykValues[3];
        float c3 = c2 + k2;
        float m3 = m2 + k2;
        float y3 = y2 + k2;
        if (c3 < 0.0f) {
            c3 = 0.0f;
        } else if (c3 > 1.0f) {
            c3 = 1.0f;
        }
        if (m3 < 0.0f) {
            m3 = 0.0f;
        } else if (m3 > 1.0f) {
            m3 = 1.0f;
        }
        if (y3 < 0.0f) {
            y3 = 0.0f;
        } else if (y3 > 1.0f) {
            y3 = 1.0f;
        }
        float[] rgbValues = {1.0f - c3, 1.0f - m3, 1.0f - y3, 0.0f};
        return rgbValues;
    }

    @Override // java.awt.color.ColorSpace
    public float[] fromCIEXYZ(float[] colorvalue) {
        return fromRGB(COLOR_SPACE_sRGB.fromCIEXYZ(colorvalue));
    }

    @Override // java.awt.color.ColorSpace
    public float[] toCIEXYZ(float[] colorvalue) {
        return COLOR_SPACE_sRGB.toCIEXYZ(toRGB(colorvalue));
    }
}
