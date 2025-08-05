package ao;

import java.io.File;
import java.io.FilenameFilter;

/* renamed from: ao.fn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fn.class */
public class C0757fn implements FilenameFilter {
    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.toLowerCase().endsWith(".jpg") || str.toLowerCase().endsWith(".jpeg") || str.toLowerCase().endsWith(".png");
    }
}
