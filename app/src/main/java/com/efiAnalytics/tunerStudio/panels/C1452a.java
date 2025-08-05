package com.efiAnalytics.tunerStudio.panels;

import G.bS;
import W.C0196v;
import W.C0200z;
import aP.aE;
import aP.iC;
import bH.C1011s;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import r.C1798a;
import r.C1807j;
import s.C1818g;

/* renamed from: com.efiAnalytics.tunerStudio.panels.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/a.class */
public class C1452a extends JPanel {

    /* renamed from: d, reason: collision with root package name */
    aP.S f10049d;

    /* renamed from: i, reason: collision with root package name */
    G.R f10055i;

    /* renamed from: a, reason: collision with root package name */
    JTextField f10046a = new JTextField("", 16);

    /* renamed from: b, reason: collision with root package name */
    JTextField f10047b = new JTextField("", 16);

    /* renamed from: c, reason: collision with root package name */
    JTextField f10048c = new JTextField("", 20);

    /* renamed from: e, reason: collision with root package name */
    JButton f10050e = new JButton("...");

    /* renamed from: f, reason: collision with root package name */
    JCheckBox f10051f = new JCheckBox(C1818g.b("Disable Runtime Data"));

    /* renamed from: g, reason: collision with root package name */
    aE f10052g = new aE();

    /* renamed from: j, reason: collision with root package name */
    private aE.d f10053j = null;

    /* renamed from: h, reason: collision with root package name */
    aE.a f10054h = null;

    public C1452a(G.R r2) {
        this.f10055i = r2;
        this.f10049d = new aP.S(r2);
        this.f10049d.a(new C1455d(this));
        setLayout(new BorderLayout(4, 4));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        jPanel.add(a(this.f10046a, "<html>Device Identifier (short)<br>", "The short name, 6 characters or less for this CAN device.<br><b>Must be unique</b><br>This will be used to identify components of this device<br>such as OutputChannels and Data Log field names.</html>"));
        this.f10046a.addKeyListener(new C1453b(this));
        jPanel.add(a(this.f10047b, "Device description", "The long name or description used for this CAN device"));
        this.f10047b.addKeyListener(new C1454c(this));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add("West", this.f10051f);
        this.f10051f.setToolTipText(C1818g.b("When checked, runtime data for gauges and data logs will not be read directly from this controller."));
        jPanel2.add(jPanel3);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout(8, 8));
        jPanel4.add("East", this.f10049d);
        jPanel2.add(jPanel4);
        jPanel.add(jPanel2);
        add("North", jPanel);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout(4, 4));
        jPanel5.setBorder(BorderFactory.createTitledBorder(C1818g.b("Device Configuration File:")));
        this.f10048c.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f10048c.setEditable(false);
        jPanel5.add(BorderLayout.CENTER, this.f10048c);
        this.f10050e.addActionListener(new C1457f(this));
        jPanel5.add("East", this.f10050e);
        add(BorderLayout.CENTER, jPanel5);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().setView(this.f10052g);
        jScrollPane.setAutoscrolls(true);
        jScrollPane.setMinimumSize(new Dimension(250, 60));
        jScrollPane.setPreferredSize(new Dimension(300, 100));
        add("South", jScrollPane);
        b(false);
    }

    public void a(aE.a aVar) {
        this.f10054h = aVar;
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.f10046a.setEnabled(z2);
        this.f10047b.setEnabled(z2);
        this.f10048c.setEnabled(z2);
        this.f10049d.setEnabled(z2);
        this.f10050e.setEnabled(z2);
        this.f10051f.setEnabled(z2);
    }

    private JPanel a(JTextField jTextField, String str, String str2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(8, 8));
        JLabel jLabel = new JLabel(C1818g.b(str), 4);
        jPanel.add(BorderLayout.CENTER, jLabel);
        jLabel.setToolTipText(str2);
        jPanel.add("East", jTextField);
        jTextField.setToolTipText(C1818g.b(str2));
        jTextField.setBorder(BorderFactory.createLoweredBevelBorder());
        return jPanel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) {
        String strA = this.f10053j.a(this.f10054h);
        try {
            String str2 = this.f10054h.t() + aE.a.f2348h;
            if (!str.startsWith(str2)) {
                String str3 = str2 + (this.f10046a.getText() + ".ini");
                C1011s.a(str, str3);
                str = str3;
            }
            this.f10053j.c(str);
            this.f10048c.setText(str);
            a(str);
        } catch (V.a e2) {
            bV.d(C1818g.b("Unable to load configuration settings.") + "\n" + e2.getLocalizedMessage(), this);
            e2.printStackTrace();
            this.f10053j.c(strA);
        }
    }

    protected void a(String str) throws V.a {
        if (this.f10053j == null) {
            throw new V.a("CAN Device not initialized.");
        }
        this.f10052g.a(new W.I().a(C0196v.a().b(str), str));
        if (this.f10053j != null && this.f10053j.d() != null) {
            for (String str2 : this.f10053j.d()) {
                this.f10052g.a(str2);
            }
        }
        this.f10052g.validate();
    }

    public aE.d a() {
        if (this.f10053j == null) {
            return null;
        }
        this.f10053j.a(this.f10046a.getText());
        this.f10053j.b(this.f10047b.getText());
        this.f10053j.c(this.f10048c.getText());
        this.f10053j.a(this.f10052g.d());
        this.f10053j.a(b());
        this.f10053j.a(!this.f10051f.isSelected());
        return this.f10053j;
    }

    public void a(aE.d dVar) {
        this.f10053j = dVar;
        try {
            if (dVar.c() == null || dVar.c().equals("")) {
                this.f10048c.setText("");
                this.f10052g.a();
            } else {
                this.f10048c.setText(dVar.a(this.f10054h));
                a(this.f10048c.getText());
            }
            this.f10046a.setText(dVar.a());
            this.f10047b.setText(dVar.b());
            a(dVar.e());
            b(true);
            this.f10051f.setSelected(!dVar.f());
        } catch (V.a e2) {
            bV.d(C1818g.b("Unable to load configuration settings.") + "\n" + e2.getMessage(), this);
            e2.printStackTrace();
        }
        if (dVar.e() < 0 || this.f10049d.d() < 0) {
            this.f10049d.e();
        }
    }

    public int b() {
        return this.f10049d.d();
    }

    public void a(int i2) {
        this.f10049d.a(i2);
    }

    public void c() {
        this.f10053j = null;
        this.f10046a.setText("");
        this.f10047b.setText("");
        this.f10048c.setText("");
        b(false);
        this.f10052g.a();
        this.f10052g.validate();
    }

    private void b(boolean z2) {
        this.f10046a.setEditable(z2);
        this.f10047b.setEditable(z2);
        this.f10050e.setEnabled(z2);
    }

    public void a(boolean z2) {
        this.f10046a.setEnabled(z2);
    }

    public void d() {
        this.f10049d.close();
    }

    public void a(aH.a aVar) throws HeadlessException {
        if (aVar.d() == null || aVar.d().isEmpty()) {
            return;
        }
        bS bSVar = new bS();
        bSVar.a(aVar.d());
        bSVar.b(aVar.a());
        File file = new File(this.f10048c.getText());
        String strA = C0200z.a(file);
        if (strA == null || strA.equals(bSVar.b())) {
            return;
        }
        try {
            file = C1807j.d(bSVar.b());
        } catch (V.a e2) {
            e2.printStackTrace();
        }
        if (file != null && file.exists()) {
            b(file.getAbsolutePath());
            this.f10052g.validate();
            return;
        }
        try {
            file = iC.a(bSVar);
        } catch (FileNotFoundException e3) {
        }
        if (file == null || !file.exists()) {
            bV.d(C1818g.b(C1798a.f13268b + " does not have a configuration to support the found firmware") + ":\n" + aVar.a() + "\n\n" + C1818g.b("Required Serial Signature") + ":\n" + aVar.d() + "\n\n\n" + C1818g.b("Please click the 'Other' checkbox and browse") + ", \n" + C1818g.b("to the Ecu Definition (ini) file provided with your firmware."), this);
        } else {
            b(file.getAbsolutePath());
            this.f10052g.validate();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        return new Dimension(preferredSize.width, preferredSize.height + eJ.a(12));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        return new Dimension(minimumSize.width, minimumSize.height + eJ.a(12));
    }
}
