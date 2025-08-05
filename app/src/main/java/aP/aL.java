package aP;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:aP/aL.class */
class aL extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextField f2830a = new JTextField("", 6);

    /* renamed from: b, reason: collision with root package name */
    JLabel f2831b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ aJ f2832c;

    aL(aJ aJVar, String str) {
        this.f2832c = aJVar;
        this.f2831b = null;
        setLayout(new BorderLayout(2, 2));
        this.f2831b = new JLabel(str, 4);
        add(BorderLayout.CENTER, this.f2831b);
        add("East", this.f2830a);
        this.f2830a.addFocusListener(new aN(aJVar));
        this.f2830a.addKeyListener(new aM(this, aJVar));
    }

    public String a() {
        return this.f2830a.getText();
    }

    public void a(String str) {
        this.f2830a.setText(str);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        this.f2831b.setEnabled(z2);
        this.f2830a.setEnabled(z2);
        super.setEnabled(z2);
    }
}
