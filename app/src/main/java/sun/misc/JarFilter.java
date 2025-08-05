package sun.misc;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: rt.jar:sun/misc/JarFilter.class */
public class JarFilter implements FilenameFilter {
    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        String lowerCase = str.toLowerCase();
        return lowerCase.endsWith(".jar") || lowerCase.endsWith(".zip");
    }
}
