package aP;

import java.io.File;
import java.io.FileFilter;
import r.C1798a;

/* renamed from: aP.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/r.class */
class C0462r implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0461q f3835a;

    C0462r(C0461q c0461q) {
        this.f3835a = c0461q;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return (file.getName().toLowerCase().endsWith(C1798a.ct) || file.getName().toLowerCase().endsWith(C1798a.cs) || file.getName().toLowerCase().endsWith(C1798a.cr) || file.getName().toLowerCase().endsWith(C1798a.cv) || file.getName().toLowerCase().endsWith(C1798a.f13286t) || file.getParentFile().getName().equals("restorePoints")) ? false : true;
    }
}
