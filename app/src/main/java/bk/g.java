package bk;

import W.aA;
import aP.C0338f;
import bD.InterfaceC0961g;
import com.efiAnalytics.ui.bV;
import java.io.File;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bk/g.class */
class g implements InterfaceC0961g {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f8191a;

    g(d dVar) {
        this.f8191a = dVar;
    }

    @Override // bD.InterfaceC0961g
    public aA a(List list) {
        String[] strArr = new String[list.size()];
        for (int i2 = 0; i2 < list.size(); i2++) {
            strArr[i2] = ((File) list.get(i2)).getAbsolutePath();
        }
        return C0338f.a().a(bV.b(this.f8191a.f8188f), strArr);
    }
}
