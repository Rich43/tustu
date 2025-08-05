package javafx.scene.layout;

import javafx.beans.NamedArg;
import javafx.geometry.Side;

/* loaded from: jfxrt.jar:javafx/scene/layout/BackgroundPosition.class */
public class BackgroundPosition {
    public static final BackgroundPosition DEFAULT = new BackgroundPosition(Side.LEFT, 0.0d, true, Side.TOP, 0.0d, true);
    public static final BackgroundPosition CENTER = new BackgroundPosition(Side.LEFT, 0.5d, true, Side.TOP, 0.5d, true);
    final Side horizontalSide;
    final Side verticalSide;
    final double horizontalPosition;
    final double verticalPosition;
    final boolean horizontalAsPercentage;
    final boolean verticalAsPercentage;
    private final int hash;

    public final Side getHorizontalSide() {
        return this.horizontalSide;
    }

    public final Side getVerticalSide() {
        return this.verticalSide;
    }

    public final double getHorizontalPosition() {
        return this.horizontalPosition;
    }

    public final double getVerticalPosition() {
        return this.verticalPosition;
    }

    public final boolean isHorizontalAsPercentage() {
        return this.horizontalAsPercentage;
    }

    public final boolean isVerticalAsPercentage() {
        return this.verticalAsPercentage;
    }

    public BackgroundPosition(@NamedArg("horizontalSide") Side horizontalSide, @NamedArg("horizontalPosition") double horizontalPosition, @NamedArg("horizontalAsPercentage") boolean horizontalAsPercentage, @NamedArg("verticalSide") Side verticalSide, @NamedArg("verticalPosition") double verticalPosition, @NamedArg("verticalAsPercentage") boolean verticalAsPercentage) {
        if (horizontalSide == Side.TOP || horizontalSide == Side.BOTTOM) {
            throw new IllegalArgumentException("The horizontalSide must be LEFT or RIGHT");
        }
        if (verticalSide == Side.LEFT || verticalSide == Side.RIGHT) {
            throw new IllegalArgumentException("The verticalSide must be TOP or BOTTOM");
        }
        this.horizontalSide = horizontalSide == null ? Side.LEFT : horizontalSide;
        this.verticalSide = verticalSide == null ? Side.TOP : verticalSide;
        this.horizontalPosition = horizontalPosition;
        this.verticalPosition = verticalPosition;
        this.horizontalAsPercentage = horizontalAsPercentage;
        this.verticalAsPercentage = verticalAsPercentage;
        int result = this.horizontalSide.hashCode();
        int result2 = (31 * result) + this.verticalSide.hashCode();
        long temp = this.horizontalPosition != 0.0d ? Double.doubleToLongBits(this.horizontalPosition) : 0L;
        int result3 = (31 * result2) + ((int) (temp ^ (temp >>> 32)));
        long temp2 = this.verticalPosition != 0.0d ? Double.doubleToLongBits(this.verticalPosition) : 0L;
        this.hash = (31 * ((31 * ((31 * result3) + ((int) (temp2 ^ (temp2 >>> 32))))) + (this.horizontalAsPercentage ? 1 : 0))) + (this.verticalAsPercentage ? 1 : 0);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        BackgroundPosition that = (BackgroundPosition) o2;
        return this.hash == that.hash && this.horizontalAsPercentage == that.horizontalAsPercentage && Double.compare(that.horizontalPosition, this.horizontalPosition) == 0 && this.verticalAsPercentage == that.verticalAsPercentage && Double.compare(that.verticalPosition, this.verticalPosition) == 0 && this.horizontalSide == that.horizontalSide && this.verticalSide == that.verticalSide;
    }

    public int hashCode() {
        return this.hash;
    }
}
