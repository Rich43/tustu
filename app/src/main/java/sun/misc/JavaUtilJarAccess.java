package sun.misc;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/* loaded from: rt.jar:sun/misc/JavaUtilJarAccess.class */
public interface JavaUtilJarAccess {
    boolean jarFileHasClassPathAttribute(JarFile jarFile) throws IOException;

    CodeSource[] getCodeSources(JarFile jarFile, URL url);

    CodeSource getCodeSource(JarFile jarFile, URL url, String str);

    Enumeration<String> entryNames(JarFile jarFile, CodeSource[] codeSourceArr);

    Enumeration<JarEntry> entries2(JarFile jarFile);

    void setEagerValidation(JarFile jarFile, boolean z2);

    List<Object> getManifestDigests(JarFile jarFile);

    Attributes getTrustedAttributes(Manifest manifest, String str);

    void ensureInitialization(JarFile jarFile);
}
