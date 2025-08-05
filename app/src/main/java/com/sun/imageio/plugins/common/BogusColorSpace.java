package com.sun.imageio.plugins.common;

import java.awt.color.ColorSpace;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/BogusColorSpace.class */
public class BogusColorSpace extends ColorSpace {
    private static int getType(int i2) {
        int i3;
        if (i2 < 1) {
            throw new IllegalArgumentException("numComponents < 1!");
        }
        switch (i2) {
            case 1:
                i3 = 6;
                break;
            default:
                i3 = i2 + 10;
                break;
        }
        return i3;
    }

    public BogusColorSpace(int i2) {
        super(getType(i2), i2);
    }

    @Override // java.awt.color.ColorSpace
    public float[] toRGB(float[] fArr) {
        if (fArr.length < getNumComponents()) {
            throw new ArrayIndexOutOfBoundsException("colorvalue.length < getNumComponents()");
        }
        System.arraycopy(fArr, 0, new float[3], 0, Math.min(3, getNumComponents()));
        return fArr;
    }

    @Override // java.awt.color.ColorSpace
    public float[] fromRGB(float[] fArr) {
        if (fArr.length < 3) {
            throw new ArrayIndexOutOfBoundsException("rgbvalue.length < 3");
        }
        float[] fArr2 = new float[getNumComponents()];
        System.arraycopy(fArr, 0, fArr2, 0, Math.min(3, fArr2.length));
        return fArr;
    }

    @Override // java.awt.color.ColorSpace
    public float[] toCIEXYZ(float[] fArr) {
        if (fArr.length < getNumComponents()) {
            throw new ArrayIndexOutOfBoundsException("colorvalue.length < getNumComponents()");
        }
        System.arraycopy(fArr, 0, new float[3], 0, Math.min(3, getNumComponents()));
        return fArr;
    }

    @Override // java.awt.color.ColorSpace
    public float[] fromCIEXYZ(float[] fArr) {
        if (fArr.length < 3) {
            throw new ArrayIndexOutOfBoundsException("xyzvalue.length < 3");
        }
        float[] fArr2 = new float[getNumComponents()];
        System.arraycopy(fArr, 0, fArr2, 0, Math.min(3, fArr2.length));
        return fArr;
    }
}
