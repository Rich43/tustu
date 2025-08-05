package sun.net.www.protocol.jar;

import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

/* loaded from: rt.jar:sun/net/www/protocol/jar/URLJarFileCallBack.class */
public interface URLJarFileCallBack {
    JarFile retrieve(URL url) throws IOException;
}
