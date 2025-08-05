package bf;

import G.Q;
import be.InterfaceC1100p;
import javax.swing.tree.DefaultTreeModel;

/* renamed from: bf.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/m.class */
public class C1120m implements InterfaceC1100p {

    /* renamed from: a, reason: collision with root package name */
    C1108a f8058a;

    C1120m(C1108a c1108a) {
        this.f8058a = c1108a;
    }

    @Override // be.InterfaceC1100p
    public void a(Q q2) {
        this.f8058a.a(q2);
    }

    @Override // be.InterfaceC1100p
    public void b(Q q2) {
        for (int i2 = 0; i2 < this.f8058a.f8035d.getChildCount(); i2++) {
            ((DefaultTreeModel) this.f8058a.f8041j.getModel()).reload((C1117j) this.f8058a.f8035d.getChildAt(i2));
        }
    }

    @Override // be.InterfaceC1100p
    public void c(Q q2) {
        for (int i2 = 0; i2 < this.f8058a.f8035d.getChildCount(); i2++) {
            C1117j c1117j = (C1117j) this.f8058a.f8035d.getChildAt(i2);
            for (int i3 = 0; i3 < c1117j.getChildCount(); i3++) {
                C1117j c1117j2 = (C1117j) c1117j.getChildAt(i3);
                if (c1117j2.a() != null && c1117j2.a().equals(q2)) {
                    c1117j.remove(i3);
                    ((DefaultTreeModel) this.f8058a.f8041j.getModel()).reload(c1117j);
                    return;
                }
            }
        }
    }

    @Override // be.InterfaceC1100p
    public void a(boolean z2) {
    }
}
