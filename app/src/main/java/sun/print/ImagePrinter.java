package sun.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

/* loaded from: rt.jar:sun/print/ImagePrinter.class */
class ImagePrinter implements Printable {
    BufferedImage image;

    ImagePrinter(InputStream inputStream) {
        try {
            this.image = ImageIO.read(inputStream);
        } catch (Exception e2) {
        }
    }

    ImagePrinter(URL url) {
        try {
            this.image = ImageIO.read(url);
        } catch (Exception e2) {
        }
    }

    @Override // java.awt.print.Printable
    public int print(Graphics graphics, PageFormat pageFormat, int i2) {
        if (i2 > 0 || this.image == null) {
            return 1;
        }
        ((Graphics2D) graphics).translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        int width = this.image.getWidth(null);
        int height = this.image.getHeight(null);
        int imageableWidth = (int) pageFormat.getImageableWidth();
        int imageableHeight = (int) pageFormat.getImageableHeight();
        int i3 = width;
        int i4 = height;
        if (i3 > imageableWidth) {
            i4 = (int) (i4 * (imageableWidth / i3));
            i3 = imageableWidth;
        }
        if (i4 > imageableHeight) {
            i3 = (int) (i3 * (imageableHeight / i4));
            i4 = imageableHeight;
        }
        int i5 = (imageableWidth - i3) / 2;
        int i6 = (imageableHeight - i4) / 2;
        graphics.drawImage(this.image, i5, i6, i5 + i3, i6 + i4, 0, 0, width, height, null);
        return 0;
    }
}
