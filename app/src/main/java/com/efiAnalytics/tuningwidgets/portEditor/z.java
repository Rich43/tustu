package com.efiAnalytics.tuningwidgets.portEditor;

import G.C0083bp;
import G.R;
import G.aH;
import G.aM;
import G.aS;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/z.class */
public class z extends JPanel implements F, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    R f10611a;

    /* renamed from: h, reason: collision with root package name */
    private aM f10612h;

    /* renamed from: i, reason: collision with root package name */
    private aM f10613i;

    /* renamed from: j, reason: collision with root package name */
    private aM f10614j;

    /* renamed from: b, reason: collision with root package name */
    q f10615b;

    /* renamed from: c, reason: collision with root package name */
    C f10616c;

    /* renamed from: d, reason: collision with root package name */
    v f10617d;

    /* renamed from: e, reason: collision with root package name */
    B f10618e;

    /* renamed from: f, reason: collision with root package name */
    B f10619f;

    /* renamed from: g, reason: collision with root package name */
    int f10620g;

    public z(R r2, aS aSVar, int i2) {
        this.f10611a = null;
        this.f10612h = null;
        this.f10613i = null;
        this.f10614j = null;
        this.f10615b = null;
        this.f10616c = null;
        this.f10617d = null;
        this.f10618e = null;
        this.f10619f = null;
        this.f10620g = -1;
        this.f10611a = r2;
        this.f10612h = this.f10611a.c(aSVar.h());
        this.f10613i = this.f10611a.c(aSVar.i());
        this.f10614j = this.f10611a.c(aSVar.j());
        this.f10620g = i2;
        setLayout(new BoxLayout(this, 0));
        this.f10615b = new q(this.f10611a, aSVar.o());
        this.f10615b.b(i2);
        this.f10615b.a(this);
        add(this.f10615b);
        this.f10616c = new C(r2, aSVar.g(), aSVar.f(), aSVar.r(), aSVar.p(), aSVar.q());
        this.f10616c.a(new A(this));
        this.f10616c.a(i2);
        add(this.f10616c);
        a(r2);
        ArrayList arrayList = new ArrayList();
        Iterator itB = aSVar.b();
        while (itB.hasNext()) {
            arrayList.add("" + itB.next());
        }
        this.f10617d = new v(r2, aSVar.h(), (String[]) arrayList.toArray(new String[arrayList.size()]));
        this.f10617d.a(i2);
        this.f10617d.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        add(this.f10617d);
        C0083bp c0083bp = new C0083bp();
        c0083bp.a(aSVar.i());
        this.f10618e = new B(this, r2, c0083bp);
        this.f10618e.a(0, i2);
        add(this.f10618e);
        C0083bp c0083bp2 = new C0083bp();
        c0083bp2.a(aSVar.j());
        this.f10619f = new B(this, r2, c0083bp2);
        this.f10619f.a(0, i2);
        add(this.f10619f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(aH aHVar) {
        a();
    }

    public void a(int i2) {
        this.f10615b.a(i2);
        this.f10616c.b(i2);
        this.f10617d.b(i2);
        this.f10618e.a(this.f10620g, i2);
        this.f10619f.a(this.f10620g, i2);
    }

    public void a() {
        this.f10618e.f();
        this.f10619f.f();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f10616c.close();
        this.f10617d.close();
        this.f10618e.close();
        this.f10619f.close();
        this.f10613i.c(0.0d);
        this.f10613i.b(1.0d);
        this.f10614j.c(0.0d);
        this.f10614j.b(1.0d);
    }

    @Override // com.efiAnalytics.tuningwidgets.portEditor.F
    public void a(R r2) {
        this.f10616c.a(r2);
    }
}
