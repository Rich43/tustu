package br;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:br/aa.class */
class aa extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    boolean f8404a;

    /* renamed from: b, reason: collision with root package name */
    ab f8405b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ P f8406c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aa(P p2, String str, int i2) {
        super(str, i2);
        this.f8406c = p2;
        this.f8404a = false;
        this.f8405b = null;
        setPreferredSize(new Dimension(110, 26));
        setMinimumSize(new Dimension(110, 26));
        setMaximumSize(new Dimension(110, 26));
        setBorder(BorderFactory.createEtchedBorder(0));
    }

    public String a() {
        return !this.f8406c.f8374a.C().q() ? C1818g.b("Offline") : !((ag) this.f8406c.f8389p.get(0)).f8425a.b() ? C1818g.b("Idle") : !((ag) this.f8406c.f8389p.get(0)).f8425a.f() ? C1818g.b("Tables Inactive") : C1818g.b("Correcting Tables");
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f8404a) {
            int width = (getWidth() - 14) - 5;
            int height = (getHeight() - 14) / 2;
            graphics.setColor(Color.red);
            graphics.fill3DRect(0, 0, getWidth(), getHeight(), true);
        }
        super.paint(graphics);
    }

    public void a(boolean z2) {
        if (this.f8405b != null) {
            this.f8405b.a();
        }
        if (z2) {
            this.f8405b = new ab(this);
            this.f8405b.start();
        }
    }
}
