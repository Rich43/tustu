package sun.awt.image;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/* loaded from: rt.jar:sun/awt/image/FileImageSource.class */
public class FileImageSource extends InputStreamImageSource {
    String imagefile;

    public FileImageSource(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(str);
        }
        this.imagefile = str;
    }

    @Override // sun.awt.image.InputStreamImageSource
    final boolean checkSecurity(Object obj, boolean z2) {
        return true;
    }

    @Override // sun.awt.image.InputStreamImageSource
    protected ImageDecoder getDecoder() {
        if (this.imagefile == null) {
            return null;
        }
        try {
            return getDecoder(new BufferedInputStream(new FileInputStream(this.imagefile)));
        } catch (FileNotFoundException e2) {
            return null;
        }
    }
}
