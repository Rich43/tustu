package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.ui.InterfaceC1535a;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.Cursor;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import r.C1807j;
import s.C1818g;
import v.C1887g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/C.class */
class C implements InterfaceC1535a {

    /* renamed from: c, reason: collision with root package name */
    private C1429b f9674c;

    /* renamed from: d, reason: collision with root package name */
    private String[] f9675d;

    /* renamed from: a, reason: collision with root package name */
    J f9676a = null;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ z f9677b;

    C(z zVar, C1429b c1429b, String[] strArr) {
        this.f9677b = zVar;
        this.f9674c = null;
        this.f9675d = null;
        this.f9674c = c1429b;
        this.f9675d = strArr;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public boolean a() {
        c();
        return true;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void b() {
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void c() {
        SwingUtilities.invokeLater(new D(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        try {
            try {
                this.f9676a.setCursor(Cursor.getPredefinedCursor(3));
                F fA = new C1887g().a(this.f9674c.a());
                if (this.f9674c.b() && !a(fA.a())) {
                    String str = this.f9675d[0];
                    for (int i2 = 1; i2 < this.f9675d.length; i2++) {
                        str = str + " & " + this.f9675d[i2];
                    }
                    if (!bV.a("Warning: Gauge Cluster firmware signature (" + fA.a() + ")\ndoes not match current firmware (" + str + ").\nYou may need to edit some components of the layout.\n \nContinue Loading?", (Component) this.f9676a.getParent(), true)) {
                        return;
                    }
                }
                fA.a(this.f9675d[0]);
                this.f9676a.a(fA);
                this.f9676a.setCursor(Cursor.getPredefinedCursor(0));
            } catch (V.a e2) {
                bH.C.a("Unable to load dash file:\n" + ((Object) this.f9674c.a()), e2, this.f9676a.getParent());
                this.f9676a.setCursor(Cursor.getPredefinedCursor(0));
            }
        } finally {
            this.f9676a.setCursor(Cursor.getPredefinedCursor(0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public J e() {
        boolean z2;
        String strB = new C1438k(this.f9674c.a()).b();
        do {
            strB = strB.trim().length() == 0 ? JOptionPane.showInputDialog(this.f9677b.f9882a, C1818g.b("New Tuning View Name"), C1818g.b("Add New Tuning View Tab"), 3) : JOptionPane.showInputDialog(this.f9677b.f9882a, C1818g.b("New Tuning View Tab Name"), strB);
            if (strB == null || strB.trim().equals("")) {
                return null;
            }
            if (this.f9677b.f9882a.d(strB)) {
                bV.d(C1818g.b("Name Already In Use."), this.f9677b.f9882a);
                z2 = true;
            } else {
                z2 = false;
            }
        } while (z2);
        J j2 = new J();
        j2.setName(strB);
        this.f9677b.f9882a.a(j2, strB);
        this.f9677b.f9882a.g(strB);
        j2.c(new File(C1807j.a(this.f9677b.f9883b), G.a(this.f9677b.f9882a.f(strB))).getAbsolutePath());
        return j2;
    }

    private boolean a(String str) {
        for (int i2 = 0; i2 < this.f9675d.length; i2++) {
            if (this.f9675d[i2].equals(str)) {
                return true;
            }
        }
        return false;
    }
}
