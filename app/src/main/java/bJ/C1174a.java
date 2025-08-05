package bj;

import G.C0081bn;
import G.R;
import V.g;
import aG.e;
import bD.r;
import bH.C1007o;
import bt.C1345d;
import bt.InterfaceC1349h;
import bt.aT;
import bt.bY;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: bj.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bj/a.class */
public class C1174a extends C1345d implements bY, InterfaceC1349h, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    R f8165a;

    /* renamed from: b, reason: collision with root package name */
    C0081bn f8166b;

    /* renamed from: c, reason: collision with root package name */
    e f8167c;

    /* renamed from: d, reason: collision with root package name */
    String f8168d = null;

    /* renamed from: e, reason: collision with root package name */
    Image f8169e = null;

    /* renamed from: f, reason: collision with root package name */
    JPanel f8170f;

    public C1174a(R r2, C0081bn c0081bn) {
        this.f8167c = null;
        this.f8170f = null;
        this.f8165a = r2;
        this.f8166b = c0081bn;
        setLayout(new BorderLayout());
        this.f8170f = new JPanel();
        this.f8170f.setLayout(new BorderLayout());
        this.f8167c = new e(r2, c0081bn == null ? new C0081bn() : c0081bn, aE.a.A().L());
        r rVar = new r(this.f8167c, C1818g.d());
        rVar.a(new C1175b(this));
        rVar.b().a(450);
        this.f8170f.add(BorderLayout.CENTER, rVar);
        add(BorderLayout.CENTER, this.f8170f);
        rVar.a(true);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f8167c.a();
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (this.f8166b.aH() == null || this.f8166b.aH().isEmpty()) {
            return;
        }
        boolean zA = true;
        try {
            zA = C1007o.a(this.f8166b.aH(), this.f8165a);
        } catch (g e2) {
            Logger.getLogger(aT.class.getName()).log(Level.WARNING, "Failed to evaluate enable condition", (Throwable) e2);
        }
        if (isEnabled() && !zA) {
            setEnabled(false);
            if (getParent() instanceof JPanel) {
                ((JPanel) getParent()).revalidate();
                return;
            }
            return;
        }
        if (isEnabled() || !zA) {
            return;
        }
        setEnabled(true);
        if (getParent() instanceof JPanel) {
            ((JPanel) getParent()).revalidate();
        }
    }

    @Override // bt.bY
    public void b() {
        if (this.f8166b.aH() == null || this.f8166b.aH().isEmpty()) {
            return;
        }
        boolean zA = true;
        try {
            zA = C1007o.a(this.f8166b.V(), this.f8165a);
        } catch (g e2) {
            Logger.getLogger(aT.class.getName()).log(Level.WARNING, "Failed to evaluate visible condition", (Throwable) e2);
        }
        if (isVisible() && !zA) {
            setVisible(false);
            if (getParent() instanceof JPanel) {
                ((JPanel) getParent()).revalidate();
                return;
            }
            return;
        }
        if (isVisible() || !zA) {
            return;
        }
        setVisible(true);
        if (getParent() instanceof JPanel) {
            ((JPanel) getParent()).revalidate();
        }
    }
}
