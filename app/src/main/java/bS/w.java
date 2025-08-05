package bs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bs/w.class */
class w extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    boolean f8633a;

    /* renamed from: b, reason: collision with root package name */
    x f8634b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ k f8635c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w(k kVar, String str, int i2) {
        super(str, i2);
        this.f8635c = kVar;
        this.f8633a = false;
        this.f8634b = null;
        setPreferredSize(new Dimension(110, 26));
        setMinimumSize(new Dimension(110, 26));
        setMaximumSize(new Dimension(110, 26));
        setBorder(BorderFactory.createEtchedBorder(0));
    }

    public String a() {
        return !this.f8635c.f8606c.C().q() ? C1818g.b("Offline") : !this.f8635c.f8605b.a() ? C1818g.b("Idle") : C1818g.b("Correcting WUE");
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f8633a) {
            int width = (getWidth() - 14) - 5;
            int height = (getHeight() - 14) / 2;
            graphics.setColor(Color.red);
            graphics.fill3DRect(0, 0, getWidth(), getHeight(), true);
        }
        super.paint(graphics);
    }

    public void a(boolean z2) {
        if (this.f8634b != null) {
            this.f8634b.a();
        }
        if (z2) {
            this.f8634b = new x(this);
            this.f8634b.start();
        }
    }
}
