package javafx.scene.layout;

import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/scene/layout/CornerRadii.class */
public class CornerRadii {
    public static final CornerRadii EMPTY = new CornerRadii(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, false, false, false, false, false, false, false, false);
    private double topLeftHorizontalRadius;
    private double topLeftVerticalRadius;
    private double topRightVerticalRadius;
    private double topRightHorizontalRadius;
    private double bottomRightHorizontalRadius;
    private double bottomRightVerticalRadius;
    private double bottomLeftVerticalRadius;
    private double bottomLeftHorizontalRadius;
    private final boolean topLeftHorizontalRadiusAsPercentage;
    private final boolean topLeftVerticalRadiusAsPercentage;
    private final boolean topRightVerticalRadiusAsPercentage;
    private final boolean topRightHorizontalRadiusAsPercentage;
    private final boolean bottomRightHorizontalRadiusAsPercentage;
    private final boolean bottomRightVerticalRadiusAsPercentage;
    private final boolean bottomLeftVerticalRadiusAsPercentage;
    private final boolean bottomLeftHorizontalRadiusAsPercentage;
    final boolean hasPercentBasedRadii;
    final boolean uniform;
    private final int hash;

    public final double getTopLeftHorizontalRadius() {
        return this.topLeftHorizontalRadius;
    }

    public final double getTopLeftVerticalRadius() {
        return this.topLeftVerticalRadius;
    }

    public final double getTopRightVerticalRadius() {
        return this.topRightVerticalRadius;
    }

    public final double getTopRightHorizontalRadius() {
        return this.topRightHorizontalRadius;
    }

    public final double getBottomRightHorizontalRadius() {
        return this.bottomRightHorizontalRadius;
    }

    public final double getBottomRightVerticalRadius() {
        return this.bottomRightVerticalRadius;
    }

    public final double getBottomLeftVerticalRadius() {
        return this.bottomLeftVerticalRadius;
    }

    public final double getBottomLeftHorizontalRadius() {
        return this.bottomLeftHorizontalRadius;
    }

    public final boolean isTopLeftHorizontalRadiusAsPercentage() {
        return this.topLeftHorizontalRadiusAsPercentage;
    }

    public final boolean isTopLeftVerticalRadiusAsPercentage() {
        return this.topLeftVerticalRadiusAsPercentage;
    }

    public final boolean isTopRightVerticalRadiusAsPercentage() {
        return this.topRightVerticalRadiusAsPercentage;
    }

    public final boolean isTopRightHorizontalRadiusAsPercentage() {
        return this.topRightHorizontalRadiusAsPercentage;
    }

    public final boolean isBottomRightHorizontalRadiusAsPercentage() {
        return this.bottomRightHorizontalRadiusAsPercentage;
    }

    public final boolean isBottomRightVerticalRadiusAsPercentage() {
        return this.bottomRightVerticalRadiusAsPercentage;
    }

    public final boolean isBottomLeftVerticalRadiusAsPercentage() {
        return this.bottomLeftVerticalRadiusAsPercentage;
    }

    public final boolean isBottomLeftHorizontalRadiusAsPercentage() {
        return this.bottomLeftHorizontalRadiusAsPercentage;
    }

    public final boolean isUniform() {
        return this.uniform;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CornerRadii(@NamedArg("radius") double d2) {
        if (d2 < 0.0d) {
            throw new IllegalArgumentException("The radii value may not be < 0");
        }
        this.bottomLeftHorizontalRadius = d2;
        this.bottomLeftVerticalRadius = d2;
        d2.bottomRightVerticalRadius = this;
        this.bottomRightHorizontalRadius = this;
        this.topRightHorizontalRadius = d2;
        d2.topRightVerticalRadius = this;
        this.topLeftVerticalRadius = this;
        this.topLeftHorizontalRadius = d2;
        CornerRadii cornerRadii = null;
        this.bottomLeftHorizontalRadiusAsPercentage = false;
        this.bottomLeftVerticalRadiusAsPercentage = false;
        this.bottomRightVerticalRadiusAsPercentage = false;
        this.bottomRightHorizontalRadiusAsPercentage = false;
        this.topRightHorizontalRadiusAsPercentage = false;
        this.topRightVerticalRadiusAsPercentage = false;
        this.topLeftVerticalRadiusAsPercentage = false;
        this.topLeftHorizontalRadiusAsPercentage = false;
        cornerRadii.hasPercentBasedRadii = false;
        cornerRadii.uniform = true;
        cornerRadii.hash = cornerRadii.preComputeHash();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CornerRadii(@NamedArg("radius") double d2, @NamedArg("asPercent") boolean z2) {
        if (d2 < 0.0d) {
            throw new IllegalArgumentException("The radii value may not be < 0");
        }
        this.bottomLeftHorizontalRadius = d2;
        this.bottomLeftVerticalRadius = d2;
        d2.bottomRightVerticalRadius = this;
        this.bottomRightHorizontalRadius = this;
        this.topRightHorizontalRadius = d2;
        d2.topRightVerticalRadius = this;
        this.topLeftVerticalRadius = this;
        this.topLeftHorizontalRadius = d2;
        this.bottomLeftHorizontalRadiusAsPercentage = z2;
        this.bottomLeftVerticalRadiusAsPercentage = z2;
        this.bottomRightVerticalRadiusAsPercentage = z2;
        this.bottomRightHorizontalRadiusAsPercentage = z2;
        this.topRightHorizontalRadiusAsPercentage = z2;
        this.topRightVerticalRadiusAsPercentage = z2;
        this.topLeftVerticalRadiusAsPercentage = z2;
        this.topLeftHorizontalRadiusAsPercentage = z2;
        z2.uniform = true;
        z2.hasPercentBasedRadii = z2;
        z2.hash = z2.preComputeHash();
    }

    public CornerRadii(@NamedArg("topLeft") double topLeft, @NamedArg("topRight") double topRight, @NamedArg("bottomRight") double bottomRight, @NamedArg("bottomLeft") double bottomLeft, @NamedArg("asPercent") boolean asPercent) {
        if (topLeft < 0.0d || topRight < 0.0d || bottomRight < 0.0d || bottomLeft < 0.0d) {
            throw new IllegalArgumentException("No radii value may be < 0");
        }
        this.topLeftVerticalRadius = topLeft;
        this.topLeftHorizontalRadius = topLeft;
        this.topRightHorizontalRadius = topRight;
        this.topRightVerticalRadius = topRight;
        this.bottomRightVerticalRadius = bottomRight;
        this.bottomRightHorizontalRadius = bottomRight;
        this.bottomLeftHorizontalRadius = bottomLeft;
        this.bottomLeftVerticalRadius = bottomLeft;
        this.bottomLeftHorizontalRadiusAsPercentage = asPercent;
        this.bottomLeftVerticalRadiusAsPercentage = asPercent;
        this.bottomRightVerticalRadiusAsPercentage = asPercent;
        this.bottomRightHorizontalRadiusAsPercentage = asPercent;
        this.topRightHorizontalRadiusAsPercentage = asPercent;
        this.topRightVerticalRadiusAsPercentage = asPercent;
        this.topLeftVerticalRadiusAsPercentage = asPercent;
        this.topLeftHorizontalRadiusAsPercentage = asPercent;
        this.uniform = topLeft == topRight && topLeft == bottomLeft && topLeft == bottomRight;
        this.hasPercentBasedRadii = asPercent;
        this.hash = preComputeHash();
    }

    public CornerRadii(@NamedArg("topLeftHorizontalRadius") double topLeftHorizontalRadius, @NamedArg("topLeftVerticalRadius") double topLeftVerticalRadius, @NamedArg("topRightVerticalRadius") double topRightVerticalRadius, @NamedArg("topRightHorizontalRadius") double topRightHorizontalRadius, @NamedArg("bottomRightHorizontalRadius") double bottomRightHorizontalRadius, @NamedArg("bottomRightVerticalRadius") double bottomRightVerticalRadius, @NamedArg("bottomLeftVerticalRadius") double bottomLeftVerticalRadius, @NamedArg("bottomLeftHorizontalRadius") double bottomLeftHorizontalRadius, @NamedArg("topLeftHorizontalRadiusAsPercent") boolean topLeftHorizontalRadiusAsPercent, @NamedArg("topLeftVerticalRadiusAsPercent") boolean topLeftVerticalRadiusAsPercent, @NamedArg("topRightVerticalRadiusAsPercent") boolean topRightVerticalRadiusAsPercent, @NamedArg("topRightHorizontalRadiusAsPercent") boolean topRightHorizontalRadiusAsPercent, @NamedArg("bottomRightHorizontalRadiusAsPercent") boolean bottomRightHorizontalRadiusAsPercent, @NamedArg("bottomRightVerticalRadiusAsPercent") boolean bottomRightVerticalRadiusAsPercent, @NamedArg("bottomLeftVerticalRadiusAsPercent") boolean bottomLeftVerticalRadiusAsPercent, @NamedArg("bottomLeftHorizontalRadiusAsPercent") boolean bottomLeftHorizontalRadiusAsPercent) {
        if (topLeftHorizontalRadius < 0.0d || topLeftVerticalRadius < 0.0d || topRightVerticalRadius < 0.0d || topRightHorizontalRadius < 0.0d || bottomRightHorizontalRadius < 0.0d || bottomRightVerticalRadius < 0.0d || bottomLeftVerticalRadius < 0.0d || bottomLeftHorizontalRadius < 0.0d) {
            throw new IllegalArgumentException("No radii value may be < 0");
        }
        this.topLeftHorizontalRadius = topLeftHorizontalRadius;
        this.topLeftVerticalRadius = topLeftVerticalRadius;
        this.topRightVerticalRadius = topRightVerticalRadius;
        this.topRightHorizontalRadius = topRightHorizontalRadius;
        this.bottomRightHorizontalRadius = bottomRightHorizontalRadius;
        this.bottomRightVerticalRadius = bottomRightVerticalRadius;
        this.bottomLeftVerticalRadius = bottomLeftVerticalRadius;
        this.bottomLeftHorizontalRadius = bottomLeftHorizontalRadius;
        this.topLeftHorizontalRadiusAsPercentage = topLeftHorizontalRadiusAsPercent;
        this.topLeftVerticalRadiusAsPercentage = topLeftVerticalRadiusAsPercent;
        this.topRightVerticalRadiusAsPercentage = topRightVerticalRadiusAsPercent;
        this.topRightHorizontalRadiusAsPercentage = topRightHorizontalRadiusAsPercent;
        this.bottomRightHorizontalRadiusAsPercentage = bottomRightHorizontalRadiusAsPercent;
        this.bottomRightVerticalRadiusAsPercentage = bottomRightVerticalRadiusAsPercent;
        this.bottomLeftVerticalRadiusAsPercentage = bottomLeftVerticalRadiusAsPercent;
        this.bottomLeftHorizontalRadiusAsPercentage = bottomLeftHorizontalRadiusAsPercent;
        this.hash = preComputeHash();
        this.hasPercentBasedRadii = topLeftHorizontalRadiusAsPercent || topLeftVerticalRadiusAsPercent || topRightVerticalRadiusAsPercent || topRightHorizontalRadiusAsPercent || bottomRightHorizontalRadiusAsPercent || bottomRightVerticalRadiusAsPercent || bottomLeftVerticalRadiusAsPercent || bottomLeftHorizontalRadiusAsPercent;
        this.uniform = topLeftHorizontalRadius == topRightHorizontalRadius && topLeftVerticalRadius == topRightVerticalRadius && topLeftHorizontalRadius == bottomRightHorizontalRadius && topLeftVerticalRadius == bottomRightVerticalRadius && topLeftHorizontalRadius == bottomLeftHorizontalRadius && topLeftVerticalRadius == bottomLeftVerticalRadius && topLeftHorizontalRadiusAsPercent == topRightHorizontalRadiusAsPercent && topLeftVerticalRadiusAsPercent == topRightVerticalRadiusAsPercent && topLeftHorizontalRadiusAsPercent == bottomRightHorizontalRadiusAsPercent && topLeftVerticalRadiusAsPercent == bottomRightVerticalRadiusAsPercent && topLeftHorizontalRadiusAsPercent == bottomLeftHorizontalRadiusAsPercent && topLeftVerticalRadiusAsPercent == bottomLeftVerticalRadiusAsPercent;
    }

    private int preComputeHash() {
        long temp = this.topLeftHorizontalRadius != 0.0d ? Double.doubleToLongBits(this.topLeftHorizontalRadius) : 0L;
        int result = (int) (temp ^ (temp >>> 32));
        long temp2 = this.topLeftVerticalRadius != 0.0d ? Double.doubleToLongBits(this.topLeftVerticalRadius) : 0L;
        int result2 = (31 * result) + ((int) (temp2 ^ (temp2 >>> 32)));
        long temp3 = this.topRightVerticalRadius != 0.0d ? Double.doubleToLongBits(this.topRightVerticalRadius) : 0L;
        int result3 = (31 * result2) + ((int) (temp3 ^ (temp3 >>> 32)));
        long temp4 = this.topRightHorizontalRadius != 0.0d ? Double.doubleToLongBits(this.topRightHorizontalRadius) : 0L;
        int result4 = (31 * result3) + ((int) (temp4 ^ (temp4 >>> 32)));
        long temp5 = this.bottomRightHorizontalRadius != 0.0d ? Double.doubleToLongBits(this.bottomRightHorizontalRadius) : 0L;
        int result5 = (31 * result4) + ((int) (temp5 ^ (temp5 >>> 32)));
        long temp6 = this.bottomRightVerticalRadius != 0.0d ? Double.doubleToLongBits(this.bottomRightVerticalRadius) : 0L;
        int result6 = (31 * result5) + ((int) (temp6 ^ (temp6 >>> 32)));
        long temp7 = this.bottomLeftVerticalRadius != 0.0d ? Double.doubleToLongBits(this.bottomLeftVerticalRadius) : 0L;
        int result7 = (31 * result6) + ((int) (temp7 ^ (temp7 >>> 32)));
        long temp8 = this.bottomLeftHorizontalRadius != 0.0d ? Double.doubleToLongBits(this.bottomLeftHorizontalRadius) : 0L;
        int result8 = (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result7) + ((int) (temp8 ^ (temp8 >>> 32))))) + (this.topLeftHorizontalRadiusAsPercentage ? 1 : 0))) + (this.topLeftVerticalRadiusAsPercentage ? 1 : 0))) + (this.topRightVerticalRadiusAsPercentage ? 1 : 0))) + (this.topRightHorizontalRadiusAsPercentage ? 1 : 0))) + (this.bottomRightHorizontalRadiusAsPercentage ? 1 : 0))) + (this.bottomRightVerticalRadiusAsPercentage ? 1 : 0))) + (this.bottomLeftVerticalRadiusAsPercentage ? 1 : 0))) + (this.bottomLeftHorizontalRadiusAsPercentage ? 1 : 0);
        return (31 * result8) + result8;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        CornerRadii that = (CornerRadii) o2;
        return this.hash == that.hash && Double.compare(that.bottomLeftHorizontalRadius, this.bottomLeftHorizontalRadius) == 0 && this.bottomLeftHorizontalRadiusAsPercentage == that.bottomLeftHorizontalRadiusAsPercentage && Double.compare(that.bottomLeftVerticalRadius, this.bottomLeftVerticalRadius) == 0 && this.bottomLeftVerticalRadiusAsPercentage == that.bottomLeftVerticalRadiusAsPercentage && Double.compare(that.bottomRightVerticalRadius, this.bottomRightVerticalRadius) == 0 && this.bottomRightVerticalRadiusAsPercentage == that.bottomRightVerticalRadiusAsPercentage && Double.compare(that.bottomRightHorizontalRadius, this.bottomRightHorizontalRadius) == 0 && this.bottomRightHorizontalRadiusAsPercentage == that.bottomRightHorizontalRadiusAsPercentage && Double.compare(that.topLeftVerticalRadius, this.topLeftVerticalRadius) == 0 && this.topLeftVerticalRadiusAsPercentage == that.topLeftVerticalRadiusAsPercentage && Double.compare(that.topLeftHorizontalRadius, this.topLeftHorizontalRadius) == 0 && this.topLeftHorizontalRadiusAsPercentage == that.topLeftHorizontalRadiusAsPercentage && Double.compare(that.topRightHorizontalRadius, this.topRightHorizontalRadius) == 0 && this.topRightHorizontalRadiusAsPercentage == that.topRightHorizontalRadiusAsPercentage && Double.compare(that.topRightVerticalRadius, this.topRightVerticalRadius) == 0 && this.topRightVerticalRadiusAsPercentage == that.topRightVerticalRadiusAsPercentage;
    }

    public int hashCode() {
        return this.hash;
    }

    public String toString() {
        if (isUniform()) {
            return "CornerRadii [uniform radius = " + this.topLeftHorizontalRadius + "]";
        }
        return "CornerRadii [" + (this.topLeftHorizontalRadius == this.topLeftVerticalRadius ? "topLeft=" + this.topLeftHorizontalRadius : "topLeftHorizontalRadius=" + this.topLeftHorizontalRadius + ", topLeftVerticalRadius=" + this.topLeftVerticalRadius) + (this.topRightHorizontalRadius == this.topRightVerticalRadius ? ", topRight=" + this.topRightHorizontalRadius : ", topRightVerticalRadius=" + this.topRightVerticalRadius + ", topRightHorizontalRadius=" + this.topRightHorizontalRadius) + (this.bottomRightHorizontalRadius == this.bottomRightVerticalRadius ? ", bottomRight=" + this.bottomRightHorizontalRadius : ", bottomRightHorizontalRadius=" + this.bottomRightHorizontalRadius + ", bottomRightVerticalRadius=" + this.bottomRightVerticalRadius) + (this.bottomLeftHorizontalRadius == this.bottomLeftVerticalRadius ? ", bottomLeft=" + this.bottomLeftHorizontalRadius : ", bottomLeftVerticalRadius=" + this.bottomLeftVerticalRadius + ", bottomLeftHorizontalRadius=" + this.bottomLeftHorizontalRadius) + ']';
    }
}
