package R;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: TunerStudioMS.jar:R/h.class */
class h implements FilenameFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ g f1801a;

    h(g gVar) {
        this.f1801a = gVar;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.toLowerCase().endsWith(".svcmsg");
    }
}
