package bk;

import G.C0113cs;
import G.InterfaceC0109co;
import G.R;
import aI.w;
import com.efiAnalytics.ui.InterfaceC1565bc;
import javax.swing.JToggleButton;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bk/o.class */
public class o extends JToggleButton implements InterfaceC0109co, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    R f8224a;

    /* renamed from: b, reason: collision with root package name */
    aI.p f8225b;

    /* renamed from: c, reason: collision with root package name */
    long f8226c = 0;

    /* renamed from: d, reason: collision with root package name */
    int f8227d = 150;

    public o(R r2) throws V.a {
        this.f8224a = null;
        this.f8225b = null;
        this.f8224a = r2;
        this.f8225b = new aI.p(r2);
        setText(C1818g.b("Start SD Log"));
        C0113cs.a().a(r2.c(), aI.d.f2436a, this);
        super.addActionListener(new p(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() throws w {
        this.f8225b.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() throws w {
        this.f8225b.c();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        C0113cs.a().a(this);
        this.f8225b.a();
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (System.currentTimeMillis() - this.f8226c > this.f8227d) {
            boolean z2 = (((int) d2) & aI.d.f2439d) == aI.d.f2439d;
            if (z2 && !a()) {
                a(true);
            } else if (!z2 && a()) {
                a(false);
            }
        }
        boolean z3 = (((int) d2) & aI.d.f2437b) == aI.d.f2437b;
        if (z3 != isEnabled()) {
            setEnabled(z3);
        }
    }

    public void a(boolean z2) {
        this.f8226c = System.currentTimeMillis();
        if (z2) {
            setText(C1818g.b("Stop SD Log"));
        } else {
            setText(C1818g.b("Start SD Log"));
        }
    }

    public boolean a() {
        return super.getText().equals("Stop SD Log");
    }
}
