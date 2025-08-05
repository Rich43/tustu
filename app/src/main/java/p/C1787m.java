package p;

import com.efiAnalytics.tuningwidgets.panels.V;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import s.C1818g;
import sun.security.validator.Validator;

/* renamed from: p.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/m.class */
class C1787m extends JPanel {

    /* renamed from: d, reason: collision with root package name */
    JSlider f13220d;

    /* renamed from: f, reason: collision with root package name */
    JTextField f13222f;

    /* renamed from: g, reason: collision with root package name */
    Cdo f13223g;

    /* renamed from: h, reason: collision with root package name */
    V f13224h;

    /* renamed from: i, reason: collision with root package name */
    JPanel f13225i;

    /* renamed from: j, reason: collision with root package name */
    JPanel f13226j;

    /* renamed from: k, reason: collision with root package name */
    JPanel f13227k;

    /* renamed from: l, reason: collision with root package name */
    boolean f13228l;

    /* renamed from: m, reason: collision with root package name */
    final /* synthetic */ C1781g f13229m;

    /* renamed from: a, reason: collision with root package name */
    JRadioButton f13217a = new JRadioButton(C1818g.b(Validator.TYPE_SIMPLE));

    /* renamed from: b, reason: collision with root package name */
    JRadioButton f13218b = new JRadioButton(C1818g.b("Expression"));

    /* renamed from: c, reason: collision with root package name */
    JRadioButton f13219c = new JRadioButton(C1818g.b("Reset after:"));

    /* renamed from: e, reason: collision with root package name */
    JComboBox f13221e = new JComboBox();

    public C1787m(C1781g c1781g, G.R r2, boolean z2, String str) {
        this.f13229m = c1781g;
        this.f13220d = null;
        this.f13227k = null;
        ButtonGroup buttonGroup = new ButtonGroup();
        this.f13228l = false;
        C1788n c1788n = new C1788n(this, c1781g);
        buttonGroup.add(this.f13217a);
        this.f13217a.addActionListener(c1788n);
        buttonGroup.add(this.f13218b);
        this.f13218b.addActionListener(c1788n);
        setLayout(new BoxLayout(this, 1));
        this.f13225i = a(this.f13217a);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        this.f13224h = new V(r2);
        if (this.f13228l) {
            this.f13224h.removeAllItems();
            this.f13224h.a("TPS_Pct");
        }
        jPanel.add("West", this.f13224h);
        this.f13221e.setEditable(false);
        this.f13221e.addItem(">");
        this.f13221e.addItem("=");
        this.f13221e.addItem("<");
        jPanel.add(BorderLayout.CENTER, this.f13221e);
        this.f13223g = new Cdo("", 4);
        jPanel.add("East", this.f13223g);
        this.f13225i.add(BorderLayout.CENTER, jPanel);
        add(this.f13225i);
        this.f13226j = a(this.f13218b);
        this.f13222f = new JTextField("", 25);
        this.f13222f.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f13226j.add(BorderLayout.CENTER, this.f13222f);
        if (!this.f13228l) {
            add(this.f13226j);
        }
        if (z2) {
            this.f13227k = a(this.f13219c);
            this.f13220d = new JSlider(0, 0, 600, 30);
            this.f13220d.setMajorTickSpacing(100);
            this.f13220d.setMinorTickSpacing(10);
            this.f13220d.setPaintLabels(true);
            this.f13220d.setPaintTicks(true);
            buttonGroup.add(this.f13219c);
            this.f13219c.addActionListener(c1788n);
            this.f13227k.add(BorderLayout.CENTER, this.f13220d);
            JLabel jLabel = new JLabel("   " + this.f13220d.getValue() + " s.");
            this.f13220d.addChangeListener(new C1789o(this, c1781g, jLabel));
            this.f13227k.add("East", jLabel);
            add(this.f13227k);
        }
        this.f13217a.setSelected(true);
    }

    public void a(String str) {
        if (str == null) {
            this.f13222f.setText(str);
            return;
        }
        this.f13222f.setText(str);
        String[] strArrSplit = str.split(" ");
        if (strArrSplit.length == 1 && strArrSplit[0].trim().isEmpty()) {
            this.f13217a.setSelected(true);
        } else if (strArrSplit.length == 3 && ((strArrSplit[1].equals("<") || strArrSplit[1].equals("=") || strArrSplit[1].equals(">")) && bH.H.a(strArrSplit[2]))) {
            String strTrim = strArrSplit[0].trim();
            if (this.f13220d == null || !strTrim.equals("AppEvent.")) {
                this.f13224h.setSelectedItem(strTrim);
                this.f13221e.setSelectedItem(strArrSplit[1].trim());
                this.f13223g.setText(strArrSplit[2].trim());
                this.f13217a.setSelected(true);
            } else {
                this.f13220d.setValue((int) Double.parseDouble(strArrSplit[2]));
                this.f13219c.setSelected(true);
            }
        } else {
            this.f13218b.setSelected(true);
        }
        b();
    }

    public String a() {
        if (this.f13219c != null && this.f13219c.isSelected()) {
            StringBuilder sb = new StringBuilder();
            sb.append(C1781g.f13198a).append(this.f13220d.getValue());
            return sb.toString();
        }
        if (!this.f13217a.isSelected()) {
            return this.f13222f.getText();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.f13224h.getSelectedItem().toString()).append(" ").append(this.f13221e.getSelectedItem().toString()).append(" ").append(this.f13223g.getText());
        return sb2.toString();
    }

    public void b() {
        if (this.f13229m.f13200c == null || !this.f13229m.f13202e.isSelected()) {
            C1685fp.a((Component) this, false);
            return;
        }
        C1685fp.a((Component) this, true);
        if (this.f13217a.isSelected()) {
            C1685fp.a(this.f13225i.getComponent(1), true);
            C1685fp.a(this.f13226j.getComponent(1), false);
            if (this.f13227k != null) {
                C1685fp.a(this.f13227k.getComponent(1), false);
                C1685fp.a(this.f13227k.getComponent(2), false);
                return;
            }
            return;
        }
        if (this.f13219c.isSelected()) {
            C1685fp.a(this.f13225i.getComponent(1), false);
            C1685fp.a(this.f13226j.getComponent(1), false);
            C1685fp.a(this.f13227k.getComponent(1), true);
            C1685fp.a(this.f13227k.getComponent(2), true);
            return;
        }
        C1685fp.a(this.f13225i.getComponent(1), false);
        C1685fp.a(this.f13226j.getComponent(1), true);
        if (this.f13227k != null) {
            C1685fp.a(this.f13227k.getComponent(1), false);
            C1685fp.a(this.f13227k.getComponent(2), false);
        }
    }

    private JPanel a(JRadioButton jRadioButton) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jRadioButton.setPreferredSize(eJ.a(120, 25));
        jPanel.add("West", jRadioButton);
        return jPanel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        this.f13220d.setValue(i2);
        this.f13219c.setSelected(true);
    }
}
