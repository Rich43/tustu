package aY;

import java.io.File;
import java.io.FileFilter;

/* loaded from: TunerStudioMS.jar:aY/y.class */
class y implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ s f4086a;

    y(s sVar) {
        this.f4086a = sVar;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.getName().startsWith(this.f4086a.f4080f.a());
    }
}
