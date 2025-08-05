package ao;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/* renamed from: ao.y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/y.class */
class C0827y extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    int f6189a = -1;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0823u f6190b;

    C0827y(C0823u c0823u) {
        this.f6190b = c0823u;
    }

    @Override // java.awt.Component
    public void setSize(int i2, int i3) {
        a(i2);
        super.setSize(i2, i3);
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        a(i4);
        super.setBounds(i2, i3, i4, i5);
    }

    public void a(int i2) {
        if (this.f6190b.f6170b != null) {
            int i3 = i2 / this.f6190b.f6180l;
            if (i3 < 2) {
                i3 = 2;
            }
            this.f6190b.f6184o.b(i3);
            this.f6190b.f6184o.a(0);
            if (i3 != this.f6189a && getParent() != null) {
                SwingUtilities.invokeLater(new RunnableC0828z(this));
            }
            this.f6189a = i3;
        }
    }
}
