package p;

import java.io.File;
import java.io.FileFilter;

/* renamed from: p.A, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/A.class */
class C1772A implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ z f13142a;

    C1772A(z zVar) {
        this.f13142a = zVar;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.getName().toLowerCase().endsWith(z.f13258a);
    }
}
