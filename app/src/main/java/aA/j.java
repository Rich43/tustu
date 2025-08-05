package aA;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: TunerStudioMS.jar:aA/j.class */
class j implements FilenameFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f2267a;

    j(h hVar) {
        this.f2267a = hVar;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.endsWith(".pend");
    }
}
