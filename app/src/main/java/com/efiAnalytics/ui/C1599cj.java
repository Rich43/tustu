package com.efiAnalytics.ui;

import W.C0184j;
import W.C0188n;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sun.security.pkcs11.wrapper.Constants;

/* renamed from: com.efiAnalytics.ui.cj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cj.class */
public class C1599cj extends JDialog implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    public boolean f11259a;

    /* renamed from: b, reason: collision with root package name */
    JButton f11260b;

    /* renamed from: c, reason: collision with root package name */
    JButton f11261c;

    /* renamed from: d, reason: collision with root package name */
    JTextField f11262d;

    /* renamed from: e, reason: collision with root package name */
    JTextField f11263e;

    /* renamed from: f, reason: collision with root package name */
    JTextField f11264f;

    /* renamed from: g, reason: collision with root package name */
    Frame f11265g;

    /* renamed from: h, reason: collision with root package name */
    JComboBox f11266h;

    public C1599cj(Frame frame, C0188n c0188n, String str, String str2, String str3) {
        super(frame, "Field Properties", true);
        this.f11259a = false;
        this.f11266h = null;
        this.f11265g = frame;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        jPanel.add(new JLabel("    This sets a fixed Min and Max value for the defined field. "));
        jPanel.add(new JLabel("  The graph for this field will be scaled based on this min max,"));
        jPanel.add(new JLabel("  instead of auto-scaling based on actual min and max in log file."));
        jPanel.add(new JLabel(" "));
        jPanel.add(new JLabel("         Enter Field Name (case sensitive), Min and Max"));
        add(jPanel, "North");
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 2));
        add(BorderLayout.CENTER, jPanel2);
        jPanel2.add(new JLabel("Field Name"));
        this.f11262d = new JTextField(str);
        this.f11262d.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f11262d.setEnabled(str.equals(""));
        if (str == null || !str.isEmpty() || c0188n == null) {
            jPanel2.add(this.f11262d);
        } else {
            this.f11266h = new JComboBox();
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < c0188n.size(); i2++) {
                arrayList.add(((C0184j) c0188n.get(i2)).a());
            }
            Iterator it = bH.R.b(arrayList).iterator();
            while (it.hasNext()) {
                this.f11266h.addItem((String) it.next());
            }
            jPanel2.add(this.f11266h);
        }
        jPanel2.add(new JLabel("Scale Minimum"));
        this.f11263e = new JTextField(str2);
        this.f11263e.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel2.add(this.f11263e);
        jPanel2.add(new JLabel("Scale Maximum"));
        this.f11264f = new JTextField(str3);
        this.f11264f.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel2.add(this.f11264f);
        add(new JLabel(Constants.INDENT), "West");
        add(new JLabel(Constants.INDENT), "East");
        a(true);
        pack();
        Dimension size = frame.getSize();
        Dimension size2 = getSize();
        Point location = frame.getLocation();
        setLocation((int) (location.getX() + ((size.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((size.getHeight() - size2.getHeight()) / 2.0d)));
        setVisible(true);
    }

    protected void a(boolean z2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        a(jPanel);
        if (z2) {
            b(jPanel);
        }
        add("South", jPanel);
    }

    protected void a(JPanel jPanel) {
        JButton jButton = new JButton("OK");
        this.f11260b = jButton;
        jPanel.add(jButton);
        this.f11260b.addActionListener(this);
    }

    protected void b(JPanel jPanel) {
        JButton jButton = new JButton("Cancel");
        this.f11261c = jButton;
        jPanel.add(jButton);
        this.f11261c.addActionListener(this);
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.f11260b && d()) {
            this.f11259a = true;
            setVisible(false);
        } else if (actionEvent.getSource() == this.f11261c) {
            setVisible(false);
        }
    }

    private boolean d() {
        if (!bH.H.a(this.f11263e.getText()) || !bH.H.a(this.f11264f.getText())) {
            bV.d("Minimum and Maximum Must Be Numeric", this.f11265g);
            return false;
        }
        if (this.f11266h != null || !this.f11262d.getText().equals("")) {
            return true;
        }
        bV.d("Invalid Field Name", this.f11265g);
        return false;
    }

    public String a() {
        return this.f11266h != null ? (String) this.f11266h.getSelectedItem() : this.f11262d.getText();
    }

    public String b() {
        return this.f11263e.getText();
    }

    public String c() {
        return this.f11264f.getText();
    }
}
