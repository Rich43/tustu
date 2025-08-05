package java.awt;

import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.lang.ref.SoftReference;
import java.util.Arrays;

/* loaded from: rt.jar:java/awt/MultipleGradientPaint.class */
public abstract class MultipleGradientPaint implements Paint {
    final int transparency;
    final float[] fractions;
    final Color[] colors;
    final AffineTransform gradientTransform;
    final CycleMethod cycleMethod;
    final ColorSpaceType colorSpace;
    ColorModel model;
    float[] normalizedIntervals;
    boolean isSimpleLookup;
    SoftReference<int[][]> gradients;
    SoftReference<int[]> gradient;
    int fastGradientArraySize;

    /* loaded from: rt.jar:java/awt/MultipleGradientPaint$ColorSpaceType.class */
    public enum ColorSpaceType {
        SRGB,
        LINEAR_RGB
    }

    /* loaded from: rt.jar:java/awt/MultipleGradientPaint$CycleMethod.class */
    public enum CycleMethod {
        NO_CYCLE,
        REFLECT,
        REPEAT
    }

    MultipleGradientPaint(float[] fArr, Color[] colorArr, CycleMethod cycleMethod, ColorSpaceType colorSpaceType, AffineTransform affineTransform) {
        if (fArr == null) {
            throw new NullPointerException("Fractions array cannot be null");
        }
        if (colorArr == null) {
            throw new NullPointerException("Colors array cannot be null");
        }
        if (cycleMethod == null) {
            throw new NullPointerException("Cycle method cannot be null");
        }
        if (colorSpaceType == null) {
            throw new NullPointerException("Color space cannot be null");
        }
        if (affineTransform == null) {
            throw new NullPointerException("Gradient transform cannot be null");
        }
        if (fArr.length != colorArr.length) {
            throw new IllegalArgumentException("Colors and fractions must have equal size");
        }
        if (colorArr.length < 2) {
            throw new IllegalArgumentException("User must specify at least 2 colors");
        }
        float f2 = -1.0f;
        for (float f3 : fArr) {
            if (f3 < 0.0f || f3 > 1.0f) {
                throw new IllegalArgumentException("Fraction values must be in the range 0 to 1: " + f3);
            }
            if (f3 <= f2) {
                throw new IllegalArgumentException("Keyframe fractions must be increasing: " + f3);
            }
            f2 = f3;
        }
        boolean z2 = false;
        boolean z3 = false;
        int length = fArr.length;
        int i2 = 0;
        if (fArr[0] != 0.0f) {
            z2 = true;
            length++;
            i2 = 0 + 1;
        }
        if (fArr[fArr.length - 1] != 1.0f) {
            z3 = true;
            length++;
        }
        this.fractions = new float[length];
        System.arraycopy(fArr, 0, this.fractions, i2, fArr.length);
        this.colors = new Color[length];
        System.arraycopy(colorArr, 0, this.colors, i2, colorArr.length);
        if (z2) {
            this.fractions[0] = 0.0f;
            this.colors[0] = colorArr[0];
        }
        if (z3) {
            this.fractions[length - 1] = 1.0f;
            this.colors[length - 1] = colorArr[colorArr.length - 1];
        }
        this.colorSpace = colorSpaceType;
        this.cycleMethod = cycleMethod;
        this.gradientTransform = new AffineTransform(affineTransform);
        boolean z4 = true;
        for (Color color : colorArr) {
            z4 = z4 && color.getAlpha() == 255;
        }
        this.transparency = z4 ? 1 : 3;
    }

    public final float[] getFractions() {
        return Arrays.copyOf(this.fractions, this.fractions.length);
    }

    public final Color[] getColors() {
        return (Color[]) Arrays.copyOf(this.colors, this.colors.length);
    }

    public final CycleMethod getCycleMethod() {
        return this.cycleMethod;
    }

    public final ColorSpaceType getColorSpace() {
        return this.colorSpace;
    }

    public final AffineTransform getTransform() {
        return new AffineTransform(this.gradientTransform);
    }

    @Override // java.awt.Transparency
    public final int getTransparency() {
        return this.transparency;
    }
}
