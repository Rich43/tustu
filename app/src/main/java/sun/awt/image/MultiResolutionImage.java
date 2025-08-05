package sun.awt.image;

import java.awt.Image;
import java.util.List;

/* loaded from: rt.jar:sun/awt/image/MultiResolutionImage.class */
public interface MultiResolutionImage {
    Image getResolutionVariant(int i2, int i3);

    List<Image> getResolutionVariants();
}
