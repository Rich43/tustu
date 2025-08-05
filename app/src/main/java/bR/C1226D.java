package br;

import com.efiAnalytics.ui.eJ;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import s.C1818g;

/* renamed from: br.D, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/D.class */
class C1226D extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    boolean f8338a;

    /* renamed from: b, reason: collision with root package name */
    C1227E f8339b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1255s f8340c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1226D(C1255s c1255s, String str, int i2) {
        super(str, i2);
        this.f8340c = c1255s;
        this.f8338a = false;
        this.f8339b = null;
        setPreferredSize(new Dimension(eJ.a(110), eJ.a(26)));
        setMinimumSize(new Dimension(eJ.a(110), eJ.a(26)));
        setMaximumSize(new Dimension(eJ.a(110), eJ.a(26)));
        setBorder(BorderFactory.createEtchedBorder(0));
    }

    public String a() {
        return !this.f8340c.f8512a.C().q() ? C1818g.b("Offline") : !this.f8340c.f8519h.b() ? C1818g.b("Idle") : !this.f8340c.f8519h.f() ? C1818g.b("Table Inactive") : C1818g.b("Correcting Table");
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f8338a) {
            int width = (getWidth() - 14) - 5;
            int height = (getHeight() - 14) / 2;
            graphics.setColor(Color.red);
            graphics.fill3DRect(0, 0, getWidth(), getHeight(), true);
        }
        super.paint(graphics);
    }

    public void a(boolean z2) {
        if (this.f8339b != null) {
            this.f8339b.a();
        }
        if (z2) {
            this.f8339b = new C1227E(this);
            this.f8339b.start();
        }
    }
}
