package sun.swing;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:sun/swing/ImageIconUIResource.class */
public class ImageIconUIResource extends ImageIcon implements UIResource {
    public ImageIconUIResource(byte[] bArr) {
        super(bArr);
    }

    public ImageIconUIResource(Image image) {
        super(image);
    }
}
