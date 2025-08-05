package aP;

import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.ui.C1630dn;
import java.awt.HeadlessException;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/* renamed from: aP.dn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dn.class */
class C0299dn implements aE.e {

    /* renamed from: a, reason: collision with root package name */
    final aE.e f3240a = this;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0293dh f3241b;

    C0299dn(C0293dh c0293dh) {
        this.f3241b = c0293dh;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // aE.e
    public void a(aE.a aVar, G.R r2) throws HeadlessException {
        bT bTVarH = cZ.a().h();
        int iB = C1630dn.b();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; arrayList.size() < iB && i2 < bTVarH.c(); i2++) {
            C1425x c1425xA = bTVarH.a(bTVarH.getTitleAt(i2));
            int iX = c1425xA.x();
            if (!arrayList.contains(Integer.valueOf(iX))) {
                if (iX < 0) {
                    arrayList2.add(c1425xA);
                } else {
                    c1425xA.a(iX);
                    arrayList.add(Integer.valueOf(iX));
                }
            }
        }
        for (int i3 = 0; i3 < iB && i3 < bTVarH.c(); i3++) {
            if (!arrayList.contains(Integer.valueOf(i3)) && !arrayList2.isEmpty()) {
                ((C1425x) arrayList2.remove(0)).a(i3);
            }
        }
        SwingUtilities.invokeLater(new Cdo(this));
    }

    @Override // aE.e
    public void e_() {
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }
}
