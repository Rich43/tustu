package bl;

import java.io.File;
import java.io.FileFilter;

/* renamed from: bl.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/o.class */
class C1193o implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1190l f8260a;

    C1193o(C1190l c1190l) {
        this.f8260a = c1190l;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.getName().toLowerCase().endsWith(".jar");
    }
}
