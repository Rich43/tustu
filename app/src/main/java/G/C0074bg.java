package G;

import W.C0172ab;
import W.C0178d;
import java.io.File;

/* renamed from: G.bg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bg.class */
class C0074bg implements bP {

    /* renamed from: a, reason: collision with root package name */
    bO f914a = null;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0073bf f915b;

    C0074bg(C0073bf c0073bf) {
        this.f915b = c0073bf;
    }

    @Override // G.bP
    public bO a(String str, String str2) {
        if (this.f914a == null) {
            this.f914a = new bO(str);
        }
        return this.f914a;
    }

    @Override // G.bP
    public File b(String str, String str2) {
        File file = new File(C0172ab.f2049c, str2);
        if (!file.exists()) {
            C0178d.a(file, a(str, str2).a());
        }
        return file;
    }
}
