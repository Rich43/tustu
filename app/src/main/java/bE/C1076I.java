package be;

import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import s.C1818g;

/* renamed from: be.I, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/I.class */
class C1076I extends WindowAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JDialog f7902a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1070C f7903b;

    C1076I(C1070C c1070c, JDialog jDialog) {
        this.f7903b = c1070c;
        this.f7902a = jDialog;
    }

    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        if (this.f7903b.f7890c != null && this.f7903b.f7890c.c() && bV.a(C1818g.b("There are unsaved changes, Save them Now?"), (Component) this.f7902a, true)) {
            this.f7903b.c();
        }
    }
}
