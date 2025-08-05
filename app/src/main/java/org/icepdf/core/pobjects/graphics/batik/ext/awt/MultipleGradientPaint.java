package org.icepdf.core.pobjects.graphics.batik.ext.awt;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.AffineTransform;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/batik/ext/awt/MultipleGradientPaint.class */
public abstract class MultipleGradientPaint implements Paint {
    protected int transparency;
    protected float[] fractions;
    protected Color[] colors;
    protected AffineTransform gradientTransform;
    protected CycleMethodEnum cycleMethod;
    protected ColorSpaceEnum colorSpace;
    public static final CycleMethodEnum NO_CYCLE = new CycleMethodEnum();
    public static final CycleMethodEnum REFLECT = new CycleMethodEnum();
    public static final CycleMethodEnum REPEAT = new CycleMethodEnum();
    public static final ColorSpaceEnum SRGB = new ColorSpaceEnum();
    public static final ColorSpaceEnum LINEAR_RGB = new ColorSpaceEnum();

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/batik/ext/awt/MultipleGradientPaint$ColorSpaceEnum.class */
    public static class ColorSpaceEnum {
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/batik/ext/awt/MultipleGradientPaint$CycleMethodEnum.class */
    public static class CycleMethodEnum {
    }

    public MultipleGradientPaint(float[] fractions, Color[] colors, CycleMethodEnum cycleMethod, ColorSpaceEnum colorSpace, AffineTransform gradientTransform) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions array cannot be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colors array cannot be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Colors and fractions must have equal size");
        }
        if (colors.length < 2) {
            throw new IllegalArgumentException("User must specify at least 2 colors");
        }
        if (colorSpace != LINEAR_RGB && colorSpace != SRGB) {
            throw new IllegalArgumentException("Invalid colorspace for interpolation.");
        }
        if (cycleMethod != NO_CYCLE && cycleMethod != REFLECT && cycleMethod != REPEAT) {
            throw new IllegalArgumentException("Invalid cycle method.");
        }
        if (gradientTransform == null) {
            throw new IllegalArgumentException("Gradient transform cannot be null.");
        }
        this.fractions = new float[fractions.length];
        System.arraycopy(fractions, 0, this.fractions, 0, fractions.length);
        this.colors = new Color[colors.length];
        System.arraycopy(colors, 0, this.colors, 0, colors.length);
        this.colorSpace = colorSpace;
        this.cycleMethod = cycleMethod;
        this.gradientTransform = (AffineTransform) gradientTransform.clone();
        boolean opaque = true;
        for (Color color : colors) {
            opaque = opaque && color.getAlpha() == 255;
        }
        if (opaque) {
            this.transparency = 1;
        } else {
            this.transparency = 3;
        }
    }

    public Color[] getColors() {
        Color[] colors = new Color[this.colors.length];
        System.arraycopy(this.colors, 0, colors, 0, this.colors.length);
        return colors;
    }

    public float[] getFractions() {
        float[] fractions = new float[this.fractions.length];
        System.arraycopy(this.fractions, 0, fractions, 0, this.fractions.length);
        return fractions;
    }

    @Override // java.awt.Transparency
    public int getTransparency() {
        return this.transparency;
    }

    public CycleMethodEnum getCycleMethod() {
        return this.cycleMethod;
    }

    public ColorSpaceEnum getColorSpace() {
        return this.colorSpace;
    }

    public AffineTransform getTransform() {
        return (AffineTransform) this.gradientTransform.clone();
    }
}
