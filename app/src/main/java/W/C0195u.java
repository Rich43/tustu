package W;

import java.io.File;
import java.io.FileFilter;

/* renamed from: W.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/u.class */
final class C0195u implements FileFilter {
    C0195u() {
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file != null && (file.getName().toLowerCase().contains(".ini") || file.getName().toLowerCase().contains(".ecu"));
    }
}
