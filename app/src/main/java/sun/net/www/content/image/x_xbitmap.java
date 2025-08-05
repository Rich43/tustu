package sun.net.www.content.image;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.ContentHandler;
import java.net.URLConnection;
import sun.awt.image.URLImageSource;

/* loaded from: rt.jar:sun/net/www/content/image/x_xbitmap.class */
public class x_xbitmap extends ContentHandler {
    @Override // java.net.ContentHandler
    public Object getContent(URLConnection uRLConnection) throws IOException {
        return new URLImageSource(uRLConnection);
    }

    @Override // java.net.ContentHandler
    public Object getContent(URLConnection uRLConnection, Class[] clsArr) throws IOException {
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            if (clsArr[i2].isAssignableFrom(URLImageSource.class)) {
                return new URLImageSource(uRLConnection);
            }
            if (clsArr[i2].isAssignableFrom(Image.class)) {
                return Toolkit.getDefaultToolkit().createImage(new URLImageSource(uRLConnection));
            }
        }
        return null;
    }
}
