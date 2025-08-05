package javafx.geometry;

/* loaded from: jfxrt.jar:javafx/geometry/Side.class */
public enum Side {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT;

    public boolean isVertical() {
        return this == LEFT || this == RIGHT;
    }

    public boolean isHorizontal() {
        return this == TOP || this == BOTTOM;
    }
}
