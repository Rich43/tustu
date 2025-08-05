package t;

import java.io.File;
import java.io.FileFilter;

/* renamed from: t.ao, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/ao.class */
class C1842ao implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1837aj f13796a;

    C1842ao(C1837aj c1837aj) {
        this.f13796a = c1837aj;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        String lowerCase = file.getName().toLowerCase();
        return lowerCase.endsWith("jpg") || lowerCase.endsWith("png") || lowerCase.endsWith("jpeg") || lowerCase.endsWith("gif");
    }
}
