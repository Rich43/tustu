package bt;

import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;

/* renamed from: bt.ad, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ad.class */
class C1295ad implements aH.d {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1292aa f8818a;

    C1295ad(C1292aa c1292aa) {
        this.f8818a = c1292aa;
    }

    @Override // aH.d
    public void a(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            aH.a aVar = (aH.a) it.next();
            C1294ac c1294acA = this.f8818a.a(aVar.b());
            if (c1294acA != null) {
                c1294acA.a(aVar);
            } else {
                bH.C.b("No wrapper found for CAN ID: " + aVar.b());
            }
        }
        SwingUtilities.invokeLater(new RunnableC1296ae(this));
    }
}
