package javafx.geometry;

import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/geometry/BoundingBox.class */
public class BoundingBox extends Bounds {
    private int hash;

    public BoundingBox(@NamedArg("minX") double minX, @NamedArg("minY") double minY, @NamedArg("minZ") double minZ, @NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height, @NamedArg("depth") double depth) {
        super(minX, minY, minZ, width, height, depth);
        this.hash = 0;
    }

    public BoundingBox(@NamedArg("minX") double minX, @NamedArg("minY") double minY, @NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height) {
        super(minX, minY, 0.0d, width, height, 0.0d);
        this.hash = 0;
    }

    @Override // javafx.geometry.Bounds
    public boolean isEmpty() {
        return getMaxX() < getMinX() || getMaxY() < getMinY() || getMaxZ() < getMinZ();
    }

    @Override // javafx.geometry.Bounds
    public boolean contains(Point2D p2) {
        if (p2 == null) {
            return false;
        }
        return contains(p2.getX(), p2.getY(), 0.0d);
    }

    @Override // javafx.geometry.Bounds
    public boolean contains(Point3D p2) {
        if (p2 == null) {
            return false;
        }
        return contains(p2.getX(), p2.getY(), p2.getZ());
    }

    @Override // javafx.geometry.Bounds
    public boolean contains(double x2, double y2) {
        return contains(x2, y2, 0.0d);
    }

    @Override // javafx.geometry.Bounds
    public boolean contains(double x2, double y2, double z2) {
        return !isEmpty() && x2 >= getMinX() && x2 <= getMaxX() && y2 >= getMinY() && y2 <= getMaxY() && z2 >= getMinZ() && z2 <= getMaxZ();
    }

    @Override // javafx.geometry.Bounds
    public boolean contains(Bounds b2) {
        if (b2 == null || b2.isEmpty()) {
            return false;
        }
        return contains(b2.getMinX(), b2.getMinY(), b2.getMinZ(), b2.getWidth(), b2.getHeight(), b2.getDepth());
    }

    @Override // javafx.geometry.Bounds
    public boolean contains(double x2, double y2, double w2, double h2) {
        return contains(x2, y2) && contains(x2 + w2, y2 + h2);
    }

    @Override // javafx.geometry.Bounds
    public boolean contains(double x2, double y2, double z2, double w2, double h2, double d2) {
        return contains(x2, y2, z2) && contains(x2 + w2, y2 + h2, z2 + d2);
    }

    @Override // javafx.geometry.Bounds
    public boolean intersects(Bounds b2) {
        if (b2 == null || b2.isEmpty()) {
            return false;
        }
        return intersects(b2.getMinX(), b2.getMinY(), b2.getMinZ(), b2.getWidth(), b2.getHeight(), b2.getDepth());
    }

    @Override // javafx.geometry.Bounds
    public boolean intersects(double x2, double y2, double w2, double h2) {
        return intersects(x2, y2, 0.0d, w2, h2, 0.0d);
    }

    @Override // javafx.geometry.Bounds
    public boolean intersects(double x2, double y2, double z2, double w2, double h2, double d2) {
        return !isEmpty() && w2 >= 0.0d && h2 >= 0.0d && d2 >= 0.0d && x2 + w2 >= getMinX() && y2 + h2 >= getMinY() && z2 + d2 >= getMinZ() && x2 <= getMaxX() && y2 <= getMaxY() && z2 <= getMaxZ();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BoundingBox) {
            BoundingBox other = (BoundingBox) obj;
            return getMinX() == other.getMinX() && getMinY() == other.getMinY() && getMinZ() == other.getMinZ() && getWidth() == other.getWidth() && getHeight() == other.getHeight() && getDepth() == other.getDepth();
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (31 * 7) + Double.doubleToLongBits(getMinX());
            long bits2 = (31 * ((31 * ((31 * ((31 * ((31 * bits) + Double.doubleToLongBits(getMinY()))) + Double.doubleToLongBits(getMinZ()))) + Double.doubleToLongBits(getWidth()))) + Double.doubleToLongBits(getHeight()))) + Double.doubleToLongBits(getDepth());
            this.hash = (int) (bits2 ^ (bits2 >> 32));
        }
        return this.hash;
    }

    public String toString() {
        return "BoundingBox [minX:" + getMinX() + ", minY:" + getMinY() + ", minZ:" + getMinZ() + ", width:" + getWidth() + ", height:" + getHeight() + ", depth:" + getDepth() + ", maxX:" + getMaxX() + ", maxY:" + getMaxY() + ", maxZ:" + getMaxZ() + "]";
    }
}
