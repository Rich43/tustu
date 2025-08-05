package java.nio.charset.spi;

import java.nio.charset.Charset;
import java.util.Iterator;

/* loaded from: rt.jar:java/nio/charset/spi/CharsetProvider.class */
public abstract class CharsetProvider {
    public abstract Iterator<Charset> charsets();

    public abstract Charset charsetForName(String str);

    protected CharsetProvider() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("charsetProvider"));
        }
    }
}
