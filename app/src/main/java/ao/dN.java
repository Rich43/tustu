package ao;

import as.InterfaceC0846a;
import java.io.File;

/* loaded from: TunerStudioMS.jar:ao/dN.class */
class dN implements InterfaceC0846a {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5528a;

    dN(bP bPVar) {
        this.f5528a = bPVar;
    }

    @Override // as.InterfaceC0846a
    public void a(File file) {
        this.f5528a.a(new String[]{file.getAbsolutePath()});
        this.f5528a.q();
    }
}
