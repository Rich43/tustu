package r;

import java.io.File;
import java.io.FileFilter;

/* renamed from: r.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/k.class */
final class C1808k implements FileFilter {
    C1808k() {
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
