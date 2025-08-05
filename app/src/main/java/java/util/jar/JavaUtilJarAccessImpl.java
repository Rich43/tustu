package java.util.jar;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.List;
import sun.misc.JavaUtilJarAccess;

/* loaded from: rt.jar:java/util/jar/JavaUtilJarAccessImpl.class */
class JavaUtilJarAccessImpl implements JavaUtilJarAccess {
    JavaUtilJarAccessImpl() {
    }

    @Override // sun.misc.JavaUtilJarAccess
    public boolean jarFileHasClassPathAttribute(JarFile jarFile) throws IOException {
        return jarFile.hasClassPathAttribute();
    }

    @Override // sun.misc.JavaUtilJarAccess
    public CodeSource[] getCodeSources(JarFile jarFile, URL url) {
        return jarFile.getCodeSources(url);
    }

    @Override // sun.misc.JavaUtilJarAccess
    public CodeSource getCodeSource(JarFile jarFile, URL url, String str) {
        return jarFile.getCodeSource(url, str);
    }

    @Override // sun.misc.JavaUtilJarAccess
    public Enumeration<String> entryNames(JarFile jarFile, CodeSource[] codeSourceArr) {
        return jarFile.entryNames(codeSourceArr);
    }

    @Override // sun.misc.JavaUtilJarAccess
    public Enumeration<JarEntry> entries2(JarFile jarFile) {
        return jarFile.entries2();
    }

    @Override // sun.misc.JavaUtilJarAccess
    public void setEagerValidation(JarFile jarFile, boolean z2) {
        jarFile.setEagerValidation(z2);
    }

    @Override // sun.misc.JavaUtilJarAccess
    public List<Object> getManifestDigests(JarFile jarFile) {
        return jarFile.getManifestDigests();
    }

    @Override // sun.misc.JavaUtilJarAccess
    public Attributes getTrustedAttributes(Manifest manifest, String str) {
        return manifest.getTrustedAttributes(str);
    }

    @Override // sun.misc.JavaUtilJarAccess
    public void ensureInitialization(JarFile jarFile) throws JarException {
        jarFile.ensureInitialization();
    }
}
