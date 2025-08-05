package aP;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: TunerStudioMS.jar:aP/A.class */
class A implements FilenameFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0338f f2732a;

    A(C0338f c0338f) {
        this.f2732a = c0338f;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.toLowerCase().endsWith(".sh");
    }
}
