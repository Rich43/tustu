package aP;

import bq.C1219a;
import com.efiAnalytics.ui.InterfaceC1579bq;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/gT.class */
class gT extends C1219a {

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0308dx f3440c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    gT(C0308dx c0308dx, String str) {
        super(C1818g.b(str));
        this.f3440c = c0308dx;
    }

    @Override // x.C1891a, javax.swing.JMenu
    public void setPopupMenuVisible(boolean z2) {
        if (z2) {
            cZ.a().l().a((InterfaceC1579bq) this);
        } else {
            removeAll();
        }
        super.setPopupMenuVisible(z2);
    }
}
