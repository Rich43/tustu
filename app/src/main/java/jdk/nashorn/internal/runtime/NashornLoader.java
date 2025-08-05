package jdk.nashorn.internal.runtime;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.SecureClassLoader;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/NashornLoader.class */
abstract class NashornLoader extends SecureClassLoader {
    private static final String OBJECTS_PKG = "jdk.nashorn.internal.objects";
    private static final String RUNTIME_PKG = "jdk.nashorn.internal.runtime";
    private static final String RUNTIME_ARRAYS_PKG = "jdk.nashorn.internal.runtime.arrays";
    private static final String RUNTIME_LINKER_PKG = "jdk.nashorn.internal.runtime.linker";
    private static final String SCRIPTS_PKG = "jdk.nashorn.internal.scripts";
    private static final Permission[] SCRIPT_PERMISSIONS = {new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime.linker"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.objects"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.scripts"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime.arrays")};

    NashornLoader(ClassLoader parent) {
        super(parent);
    }

    protected static void checkPackageAccess(String name) {
        SecurityManager sm;
        String pkgName;
        int i2 = name.lastIndexOf(46);
        if (i2 != -1 && (sm = System.getSecurityManager()) != null) {
            pkgName = name.substring(0, i2);
            switch (pkgName) {
                case "jdk.nashorn.internal.runtime":
                case "jdk.nashorn.internal.runtime.arrays":
                case "jdk.nashorn.internal.runtime.linker":
                case "jdk.nashorn.internal.objects":
                case "jdk.nashorn.internal.scripts":
                    break;
                default:
                    sm.checkPackageAccess(pkgName);
                    break;
            }
        }
    }

    @Override // java.security.SecureClassLoader
    protected PermissionCollection getPermissions(CodeSource codesource) {
        Permissions permCollection = new Permissions();
        for (Permission perm : SCRIPT_PERMISSIONS) {
            permCollection.add(perm);
        }
        return permCollection;
    }

    static ClassLoader createClassLoader(String classPath, ClassLoader parent) {
        URL[] urls = pathToURLs(classPath);
        return URLClassLoader.newInstance(urls, parent);
    }

    private static URL[] pathToURLs(String path) {
        String[] components = path.split(File.pathSeparator);
        URL[] urls = new URL[components.length];
        int count = 0;
        while (count < components.length) {
            URL url = fileToURL(new File(components[count]));
            if (url != null) {
                int i2 = count;
                count++;
                urls[i2] = url;
            }
        }
        if (urls.length != count) {
            URL[] tmp = new URL[count];
            System.arraycopy(urls, 0, tmp, 0, count);
            urls = tmp;
        }
        return urls;
    }

    private static URL fileToURL(File file) {
        String name;
        try {
            name = file.getCanonicalPath();
        } catch (IOException e2) {
            name = file.getAbsolutePath();
        }
        String name2 = name.replace(File.separatorChar, '/');
        if (!name2.startsWith("/")) {
            name2 = "/" + name2;
        }
        if (!file.isFile()) {
            name2 = name2 + "/";
        }
        try {
            return new URL(DeploymentDescriptorParser.ATTR_FILE, "", name2);
        } catch (MalformedURLException e3) {
            throw new IllegalArgumentException(DeploymentDescriptorParser.ATTR_FILE);
        }
    }
}
