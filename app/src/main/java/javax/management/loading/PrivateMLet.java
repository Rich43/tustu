package javax.management.loading;

import java.net.URL;
import java.net.URLStreamHandlerFactory;

/* loaded from: rt.jar:javax/management/loading/PrivateMLet.class */
public class PrivateMLet extends MLet implements PrivateClassLoader {
    private static final long serialVersionUID = 2503458973393711979L;

    public PrivateMLet(URL[] urlArr, boolean z2) {
        super(urlArr, z2);
    }

    public PrivateMLet(URL[] urlArr, ClassLoader classLoader, boolean z2) {
        super(urlArr, classLoader, z2);
    }

    public PrivateMLet(URL[] urlArr, ClassLoader classLoader, URLStreamHandlerFactory uRLStreamHandlerFactory, boolean z2) {
        super(urlArr, classLoader, uRLStreamHandlerFactory, z2);
    }
}
