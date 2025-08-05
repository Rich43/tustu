package bo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: bo.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bo/k.class */
class C1215k extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JLabel f8315a;

    /* renamed from: b, reason: collision with root package name */
    JPanel f8316b = new JPanel();

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1206b f8317c;

    C1215k(C1206b c1206b, String str, JComponent jComponent) {
        this.f8317c = c1206b;
        this.f8315a = null;
        setLayout(new BorderLayout(5, 5));
        this.f8315a = new JLabel(str, 4);
        add(BorderLayout.CENTER, this.f8315a);
        this.f8316b.setLayout(new GridLayout(1, 0, 4, 4));
        this.f8316b.add(jComponent);
        add("East", this.f8316b);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            getComponent(i2).setEnabled(z2);
        }
    }

    public void a(JComponent jComponent) {
        this.f8316b.add(jComponent);
    }
}
