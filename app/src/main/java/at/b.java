package at;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: TunerStudioMS.jar:at/b.class */
final class b implements FilenameFilter {
    b() {
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.endsWith(C0858a.f6266a);
    }
}
