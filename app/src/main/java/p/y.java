package p;

import java.io.File;
import java.io.FileFilter;

/* loaded from: TunerStudioMS.jar:p/y.class */
class y implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ x f13257a;

    y(x xVar) {
        this.f13257a = xVar;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.getName().toLowerCase().endsWith(x.f13252a);
    }
}
