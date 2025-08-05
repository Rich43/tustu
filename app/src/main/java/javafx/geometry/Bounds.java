package javafx.geometry;

/* loaded from: jfxrt.jar:javafx/geometry/Bounds.class */
public abstract class Bounds {
    private double minX;
    private double minY;
    private double minZ;
    private double width;
    private double height;
    private double depth;
    private double maxX;
    private double maxY;
    private double maxZ;

    public abstract boolean isEmpty();

    public abstract boolean contains(Point2D point2D);

    public abstract boolean contains(Point3D point3D);

    public abstract boolean contains(double d2, double d3);

    public abstract boolean contains(double d2, double d3, double d4);

    public abstract boolean contains(Bounds bounds);

    public abstract boolean contains(double d2, double d3, double d4, double d5);

    public abstract boolean contains(double d2, double d3, double d4, double d5, double d6, double d7);

    public abstract boolean intersects(Bounds bounds);

    public abstract boolean intersects(double d2, double d3, double d4, double d5);

    public abstract boolean intersects(double d2, double d3, double d4, double d5, double d6, double d7);

    public final double getMinX() {
        return this.minX;
    }

    public final double getMinY() {
        return this.minY;
    }

    public final double getMinZ() {
        return this.minZ;
    }

    public final double getWidth() {
        return this.width;
    }

    public final double getHeight() {
        return this.height;
    }

    public final double getDepth() {
        return this.depth;
    }

    public final double getMaxX() {
        return this.maxX;
    }

    public final double getMaxY() {
        return this.maxY;
    }

    public final double getMaxZ() {
        return this.maxZ;
    }

    protected Bounds(double minX, double minY, double minZ, double width, double height, double depth) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.maxX = minX + width;
        this.maxY = minY + height;
        this.maxZ = minZ + depth;
    }
}
