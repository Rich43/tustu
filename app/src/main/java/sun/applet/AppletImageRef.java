package sun.applet;

import java.awt.Toolkit;
import java.net.URL;
import sun.awt.image.URLImageSource;
import sun.misc.Ref;

/* loaded from: rt.jar:sun/applet/AppletImageRef.class */
class AppletImageRef extends Ref {
    URL url;

    AppletImageRef(URL url) {
        this.url = url;
    }

    @Override // sun.misc.Ref
    public void flush() {
        super.flush();
    }

    @Override // sun.misc.Ref
    public Object reconstitute() {
        return Toolkit.getDefaultToolkit().createImage(new URLImageSource(this.url));
    }
}
