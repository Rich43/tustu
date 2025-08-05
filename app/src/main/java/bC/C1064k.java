package bc;

import ae.C0499c;
import ae.q;
import ae.s;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: bc.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/k.class */
public class C1064k extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    q f7877a = null;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f7878b = new ArrayList();

    public C1064k() {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Firmware Loader Options")));
        setLayout(new BoxLayout(this, 1));
    }

    public void a(q qVar, ae.k kVar, boolean z2) {
        this.f7877a = qVar;
        b();
        for (s sVar : z2 ? C0499c.a(qVar.c(), kVar) : C0499c.b(qVar.d(), kVar)) {
            C1063j c1063j = new C1063j();
            if (z2) {
                c1063j.a(sVar.a(kVar));
            } else {
                c1063j.a(sVar.b(kVar));
            }
            c1063j.a(new C1065l(this, sVar));
            this.f7878b.add(c1063j);
            add(c1063j);
        }
    }

    private void b() {
        removeAll();
        this.f7878b.clear();
    }

    public boolean a() {
        Iterator it = this.f7878b.iterator();
        while (it.hasNext()) {
            if (!((C1063j) it.next()).b()) {
                return false;
            }
        }
        Iterator it2 = this.f7878b.iterator();
        while (it2.hasNext()) {
            ((C1063j) it2.next()).c();
        }
        return true;
    }
}
