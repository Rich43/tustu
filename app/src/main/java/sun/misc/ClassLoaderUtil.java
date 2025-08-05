package sun.misc;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.jar.JarFile;
import sun.misc.URLClassPath;

/* loaded from: rt.jar:sun/misc/ClassLoaderUtil.class */
public class ClassLoaderUtil {
    public static void releaseLoader(URLClassLoader uRLClassLoader) {
        releaseLoader(uRLClassLoader, null);
    }

    public static List<IOException> releaseLoader(URLClassLoader uRLClassLoader, List<String> list) {
        LinkedList linkedList = new LinkedList();
        if (list != null) {
            try {
                list.clear();
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        }
        URLClassPath uRLClassPath = SharedSecrets.getJavaNetAccess().getURLClassPath(uRLClassLoader);
        ArrayList<URLClassPath.Loader> arrayList = uRLClassPath.loaders;
        Stack<URL> stack = uRLClassPath.urls;
        HashMap<String, URLClassPath.Loader> map = uRLClassPath.lmap;
        synchronized (stack) {
            stack.clear();
        }
        synchronized (map) {
            map.clear();
        }
        synchronized (uRLClassPath) {
            Iterator<URLClassPath.Loader> it = arrayList.iterator();
            while (it.hasNext()) {
                URLClassPath.Loader next = it.next();
                if (next != null && (next instanceof URLClassPath.JarLoader)) {
                    JarFile jarFile = ((URLClassPath.JarLoader) next).getJarFile();
                    if (jarFile != null) {
                        try {
                            jarFile.close();
                            if (list != null) {
                                list.add(jarFile.getName());
                            }
                        } catch (IOException e2) {
                            IOException iOException = new IOException("Error closing JAR file: " + (jarFile == null ? "filename not available" : jarFile.getName()));
                            iOException.initCause(e2);
                            linkedList.add(iOException);
                        }
                    }
                }
            }
            arrayList.clear();
        }
        return linkedList;
    }
}
