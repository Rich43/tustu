package sun.misc;

import java.util.jar.JarFile;
import java.util.zip.ZipFile;

/* loaded from: rt.jar:sun/misc/JavaUtilZipFileAccess.class */
public interface JavaUtilZipFileAccess {
    boolean startsWithLocHeader(ZipFile zipFile);

    int getManifestNum(JarFile jarFile);
}
