package z;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: TunerStudioMS.jar:z/j.class */
class j implements FilenameFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ i f14120a;

    j(i iVar) {
        this.f14120a = iVar;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return (str.startsWith("tty.") || str.startsWith("cu.")) && !str.contains("Incoming");
    }
}
