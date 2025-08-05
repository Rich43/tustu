package bj;

import W.aA;
import aP.C0338f;
import bD.InterfaceC0961g;
import com.efiAnalytics.ui.bV;
import java.io.File;
import java.util.List;

/* renamed from: bj.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bj/b.class */
class C1175b implements InterfaceC0961g {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1174a f8171a;

    C1175b(C1174a c1174a) {
        this.f8171a = c1174a;
    }

    @Override // bD.InterfaceC0961g
    public aA a(List list) {
        String[] strArr = new String[list.size()];
        for (int i2 = 0; i2 < list.size(); i2++) {
            strArr[i2] = ((File) list.get(i2)).getAbsolutePath();
        }
        return C0338f.a().a(bV.b(this.f8171a.f8170f), strArr);
    }
}
