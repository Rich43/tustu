package javafx.scene.layout;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import javafx.beans.NamedArg;
import javafx.scene.image.Image;

/* loaded from: jfxrt.jar:javafx/scene/layout/BackgroundImage.class */
public final class BackgroundImage {
    final Image image;
    final BackgroundRepeat repeatX;
    final BackgroundRepeat repeatY;
    final BackgroundPosition position;
    final BackgroundSize size;
    Boolean opaque = null;
    private final int hash;

    public final Image getImage() {
        return this.image;
    }

    public final BackgroundRepeat getRepeatX() {
        return this.repeatX;
    }

    public final BackgroundRepeat getRepeatY() {
        return this.repeatY;
    }

    public final BackgroundPosition getPosition() {
        return this.position;
    }

    public final BackgroundSize getSize() {
        return this.size;
    }

    public BackgroundImage(@NamedArg(MetadataParser.IMAGE_TAG_NAME) Image image, @NamedArg("repeatX") BackgroundRepeat repeatX, @NamedArg("repeatY") BackgroundRepeat repeatY, @NamedArg(Keywords.FUNC_POSITION_STRING) BackgroundPosition position, @NamedArg("size") BackgroundSize size) {
        if (image == null) {
            throw new NullPointerException("Image cannot be null");
        }
        this.image = image;
        this.repeatX = repeatX == null ? BackgroundRepeat.REPEAT : repeatX;
        this.repeatY = repeatY == null ? BackgroundRepeat.REPEAT : repeatY;
        this.position = position == null ? BackgroundPosition.DEFAULT : position;
        this.size = size == null ? BackgroundSize.DEFAULT : size;
        int result = this.image.hashCode();
        this.hash = (31 * ((31 * ((31 * ((31 * result) + this.repeatX.hashCode())) + this.repeatY.hashCode())) + this.position.hashCode())) + this.size.hashCode();
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        BackgroundImage that = (BackgroundImage) o2;
        return this.hash == that.hash && this.image.equals(that.image) && this.position.equals(that.position) && this.repeatX == that.repeatX && this.repeatY == that.repeatY && this.size.equals(that.size);
    }

    public int hashCode() {
        return this.hash;
    }
}
