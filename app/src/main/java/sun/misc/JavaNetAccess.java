package sun.misc;

import java.net.InetAddress;
import java.net.URLClassLoader;

/* loaded from: rt.jar:sun/misc/JavaNetAccess.class */
public interface JavaNetAccess {
    URLClassPath getURLClassPath(URLClassLoader uRLClassLoader);

    String getOriginalHostName(InetAddress inetAddress);
}
