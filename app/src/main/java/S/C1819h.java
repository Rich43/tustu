package s;

import java.io.File;
import java.io.FilenameFilter;

/* renamed from: s.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:s/h.class */
final class C1819h implements FilenameFilter {
    C1819h() {
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.length() == 2 && str.indexOf(".") == -1;
    }
}
