package aP;

import G.C0043ac;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:aP/hX.class */
class hX implements ak.K {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hJ f3550a;

    hX(hJ hJVar) {
        this.f3550a = hJVar;
    }

    @Override // ak.K
    public int a(String str) {
        for (String str2 : G.T.a().d()) {
            Iterator it = G.T.a().c(str2).g().iterator();
            while (it.hasNext()) {
                C0043ac c0043ac = (C0043ac) it.next();
                if (c0043ac.b().equals(str)) {
                    return c0043ac.e();
                }
            }
        }
        return -1;
    }
}
