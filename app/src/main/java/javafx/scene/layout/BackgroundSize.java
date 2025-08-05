package javafx.scene.layout;

import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/scene/layout/BackgroundSize.class */
public final class BackgroundSize {
    public static final double AUTO = -1.0d;
    public static final BackgroundSize DEFAULT = new BackgroundSize(-1.0d, -1.0d, true, true, false, false);
    final double width;
    final double height;
    final boolean widthAsPercentage;
    final boolean heightAsPercentage;
    final boolean contain;
    final boolean cover;
    private final int hash;

    public final double getWidth() {
        return this.width;
    }

    public final double getHeight() {
        return this.height;
    }

    public final boolean isWidthAsPercentage() {
        return this.widthAsPercentage;
    }

    public final boolean isHeightAsPercentage() {
        return this.heightAsPercentage;
    }

    public final boolean isContain() {
        return this.contain;
    }

    public final boolean isCover() {
        return this.cover;
    }

    public BackgroundSize(@NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height, @NamedArg("widthAsPercentage") boolean widthAsPercentage, @NamedArg("heightAsPercentage") boolean heightAsPercentage, @NamedArg("contain") boolean contain, @NamedArg("cover") boolean cover) {
        if (width < 0.0d && width != -1.0d) {
            throw new IllegalArgumentException("Width cannot be < 0, except when AUTO");
        }
        if (height < 0.0d && height != -1.0d) {
            throw new IllegalArgumentException("Height cannot be < 0, except when AUTO");
        }
        this.width = width;
        this.height = height;
        this.widthAsPercentage = widthAsPercentage;
        this.heightAsPercentage = heightAsPercentage;
        this.contain = contain;
        this.cover = cover;
        int result = this.widthAsPercentage ? 1 : 0;
        int result2 = (31 * result) + (this.heightAsPercentage ? 1 : 0);
        long temp = this.width != 0.0d ? Double.doubleToLongBits(this.width) : 0L;
        int result3 = (31 * result2) + ((int) (temp ^ (temp >>> 32)));
        long temp2 = this.height != 0.0d ? Double.doubleToLongBits(this.height) : 0L;
        this.hash = (31 * ((31 * ((31 * result3) + ((int) (temp2 ^ (temp2 >>> 32))))) + (this.cover ? 1 : 0))) + (this.contain ? 1 : 0);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        BackgroundSize that = (BackgroundSize) o2;
        return this.hash == that.hash && this.contain == that.contain && this.cover == that.cover && Double.compare(that.height, this.height) == 0 && this.heightAsPercentage == that.heightAsPercentage && this.widthAsPercentage == that.widthAsPercentage && Double.compare(that.width, this.width) == 0;
    }

    public int hashCode() {
        return this.hash;
    }
}
