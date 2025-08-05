package r;

import java.io.File;
import java.io.FileFilter;

/* renamed from: r.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/l.class */
final class C1809l implements FileFilter {
    C1809l() {
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return !file.isDirectory() && file.getName().toLowerCase().endsWith(".dash");
    }
}
