package aP;

import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:aP/W.class */
class W implements aH.d {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ S f2786a;

    W(S s2) {
        this.f2786a = s2;
    }

    @Override // aH.d
    public void a(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            aH.a aVar = (aH.a) it.next();
            U uB = this.f2786a.b(aVar.b());
            if (uB != null) {
                uB.a(aVar);
            } else {
                bH.C.b("No wrapper found for CAN ID: " + aVar.b());
            }
        }
        this.f2786a.f2780g = false;
        SwingUtilities.invokeLater(new X(this));
    }
}
