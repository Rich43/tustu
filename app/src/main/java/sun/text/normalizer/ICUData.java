package sun.text.normalizer;

import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.MissingResourceException;

/* loaded from: rt.jar:sun/text/normalizer/ICUData.class */
public final class ICUData {
    private static InputStream getStream(final Class<ICUData> cls, final String str, boolean z2) {
        InputStream resourceAsStream;
        if (System.getSecurityManager() != null) {
            resourceAsStream = (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: sun.text.normalizer.ICUData.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public InputStream run() {
                    return cls.getResourceAsStream(str);
                }
            });
        } else {
            resourceAsStream = cls.getResourceAsStream(str);
        }
        if (resourceAsStream == null && z2) {
            throw new MissingResourceException("could not locate data", cls.getPackage().getName(), str);
        }
        return resourceAsStream;
    }

    public static InputStream getStream(String str) {
        return getStream(ICUData.class, str, false);
    }

    public static InputStream getRequiredStream(String str) {
        return getStream(ICUData.class, str, true);
    }
}
