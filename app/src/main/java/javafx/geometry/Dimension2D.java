package javafx.geometry;

import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/geometry/Dimension2D.class */
public class Dimension2D {
    private double width;
    private double height;
    private int hash = 0;

    public Dimension2D(@NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height) {
        this.width = width;
        this.height = height;
    }

    public final double getWidth() {
        return this.width;
    }

    public final double getHeight() {
        return this.height;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Dimension2D) {
            Dimension2D other = (Dimension2D) obj;
            return getWidth() == other.getWidth() && getHeight() == other.getHeight();
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (31 * ((31 * 7) + Double.doubleToLongBits(getWidth()))) + Double.doubleToLongBits(getHeight());
            this.hash = (int) (bits ^ (bits >> 32));
        }
        return this.hash;
    }

    public String toString() {
        return "Dimension2D [width = " + getWidth() + ", height = " + getHeight() + "]";
    }
}
