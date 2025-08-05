package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/BaseBounds.class */
public abstract class BaseBounds {

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/BaseBounds$BoundsType.class */
    public enum BoundsType {
        RECTANGLE,
        BOX
    }

    public abstract BaseBounds copy();

    public abstract boolean is2D();

    public abstract BoundsType getBoundsType();

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract float getDepth();

    public abstract float getMinX();

    public abstract float getMinY();

    public abstract float getMinZ();

    public abstract float getMaxX();

    public abstract float getMaxY();

    public abstract float getMaxZ();

    public abstract void translate(float f2, float f3, float f4);

    public abstract Vec2f getMin(Vec2f vec2f);

    public abstract Vec2f getMax(Vec2f vec2f);

    public abstract Vec3f getMin(Vec3f vec3f);

    public abstract Vec3f getMax(Vec3f vec3f);

    public abstract BaseBounds deriveWithUnion(BaseBounds baseBounds);

    public abstract BaseBounds deriveWithNewBounds(Rectangle rectangle);

    public abstract BaseBounds deriveWithNewBounds(BaseBounds baseBounds);

    public abstract BaseBounds deriveWithNewBounds(float f2, float f3, float f4, float f5, float f6, float f7);

    public abstract BaseBounds deriveWithNewBoundsAndSort(float f2, float f3, float f4, float f5, float f6, float f7);

    public abstract BaseBounds deriveWithPadding(float f2, float f3, float f4);

    public abstract void intersectWith(Rectangle rectangle);

    public abstract void intersectWith(BaseBounds baseBounds);

    public abstract void intersectWith(float f2, float f3, float f4, float f5, float f6, float f7);

    public abstract void setBoundsAndSort(Point2D point2D, Point2D point2D2);

    public abstract void setBoundsAndSort(float f2, float f3, float f4, float f5, float f6, float f7);

    public abstract void add(Point2D point2D);

    public abstract void add(float f2, float f3, float f4);

    public abstract boolean contains(Point2D point2D);

    public abstract boolean contains(float f2, float f3);

    public abstract boolean intersects(float f2, float f3, float f4, float f5);

    public abstract boolean isEmpty();

    public abstract void roundOut();

    public abstract RectBounds flattenInto(RectBounds rectBounds);

    public abstract BaseBounds makeEmpty();

    public abstract boolean disjoint(float f2, float f3, float f4, float f5);

    protected abstract void sortMinMax();

    BaseBounds() {
    }

    public static BaseBounds getInstance(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        if (minZ == 0.0f && maxZ == 0.0f) {
            return getInstance(minX, minY, maxX, maxY);
        }
        return new BoxBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static BaseBounds getInstance(float minX, float minY, float maxX, float maxY) {
        return new RectBounds(minX, minY, maxX, maxY);
    }
}
