package t;

import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/bn.class */
class bn extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: c, reason: collision with root package name */
    JSlider f13849c;

    /* renamed from: d, reason: collision with root package name */
    JPanel f13850d;

    /* renamed from: e, reason: collision with root package name */
    JLabel f13851e;

    bn(Window window, String str) {
        super(window, C1818g.b(str) + " " + C1818g.b("Slider"));
        this.f13849c = new JSlider();
        this.f13850d = new JPanel();
        this.f13851e = new JLabel();
        setLayout(new BorderLayout());
        add(this.f13850d, BorderLayout.CENTER);
        this.f13850d.setLayout(new BorderLayout());
        this.f13849c.setPaintTrack(true);
        this.f13850d.add(BorderLayout.CENTER, this.f13849c);
        this.f13850d.setBorder(BorderFactory.createTitledBorder(str));
        this.f13850d.add("East", this.f13851e);
        this.f13849c.addChangeListener(new bo(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        JButton jButton = new JButton(C1818g.b("Close"));
        jButton.addActionListener(new bp(this));
        jPanel.add(jButton);
        pack();
    }

    public void a(int i2, int i3, int i4, String str) throws IllegalArgumentException {
        this.f13849c.setMajorTickSpacing(10 / (i4 - i3));
        this.f13849c.setMinimum(i3);
        this.f13849c.setMaximum(i4);
        this.f13849c.setValue(i2);
        this.f13851e.setText("" + i2);
        setTitle(str);
        this.f13849c.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), str));
    }

    public void a(ChangeListener changeListener) {
        this.f13849c.addChangeListener(changeListener);
    }

    public void a(ArrayList arrayList) {
    }

    public JPanel a() {
        return this.f13850d;
    }
}
