package javafx.geometry;

import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/geometry/Rectangle2D.class */
public class Rectangle2D {
    public static final Rectangle2D EMPTY = new Rectangle2D(0.0d, 0.0d, 0.0d, 0.0d);
    private double minX;
    private double minY;
    private double width;
    private double height;
    private double maxX;
    private double maxY;
    private int hash = 0;

    public double getMinX() {
        return this.minX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public Rectangle2D(@NamedArg("minX") double minX, @NamedArg("minY") double minY, @NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height) {
        if (width < 0.0d || height < 0.0d) {
            throw new IllegalArgumentException("Both width and height must be >= 0");
        }
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        this.maxX = minX + width;
        this.maxY = minY + height;
    }

    public boolean contains(Point2D p2) {
        if (p2 == null) {
            return false;
        }
        return contains(p2.getX(), p2.getY());
    }

    public boolean contains(double x2, double y2) {
        return x2 >= this.minX && x2 <= this.maxX && y2 >= this.minY && y2 <= this.maxY;
    }

    public boolean contains(Rectangle2D r2) {
        return r2 != null && r2.minX >= this.minX && r2.minY >= this.minY && r2.maxX <= this.maxX && r2.maxY <= this.maxY;
    }

    public boolean contains(double x2, double y2, double w2, double h2) {
        return x2 >= this.minX && y2 >= this.minY && w2 <= this.maxX - x2 && h2 <= this.maxY - y2;
    }

    public boolean intersects(Rectangle2D r2) {
        return r2 != null && r2.maxX > this.minX && r2.maxY > this.minY && r2.minX < this.maxX && r2.minY < this.maxY;
    }

    public boolean intersects(double x2, double y2, double w2, double h2) {
        return x2 < this.maxX && y2 < this.maxY && x2 + w2 > this.minX && y2 + h2 > this.minY;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Rectangle2D) {
            Rectangle2D other = (Rectangle2D) obj;
            return this.minX == other.minX && this.minY == other.minY && this.width == other.width && this.height == other.height;
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (31 * 7) + Double.doubleToLongBits(this.minX);
            long bits2 = (31 * ((31 * ((31 * bits) + Double.doubleToLongBits(this.minY))) + Double.doubleToLongBits(this.width))) + Double.doubleToLongBits(this.height);
            this.hash = (int) (bits2 ^ (bits2 >> 32));
        }
        return this.hash;
    }

    public String toString() {
        return "Rectangle2D [minX = " + this.minX + ", minY=" + this.minY + ", maxX=" + this.maxX + ", maxY=" + this.maxY + ", width=" + this.width + ", height=" + this.height + "]";
    }
}
