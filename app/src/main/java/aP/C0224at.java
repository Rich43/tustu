package aP;

import W.C0197w;
import W.C0200z;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1798a;
import r.C1807j;
import s.C1818g;
import z.C1899c;

/* renamed from: aP.at, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/at.class */
public class C0224at extends JPanel implements A.o {

    /* renamed from: g, reason: collision with root package name */
    JCheckBox f2938g;

    /* renamed from: a, reason: collision with root package name */
    JTextField f2932a = new JTextField(C1818g.b("MyCar"), 30);

    /* renamed from: b, reason: collision with root package name */
    JTextField f2933b = new JTextField("", 30);

    /* renamed from: c, reason: collision with root package name */
    JTextField f2934c = new JTextField("", 25);

    /* renamed from: d, reason: collision with root package name */
    JComboBox f2935d = new JComboBox();

    /* renamed from: e, reason: collision with root package name */
    JComboBox f2936e = new JComboBox();

    /* renamed from: f, reason: collision with root package name */
    JCheckBox f2937f = new JCheckBox(C1818g.b("Other / Browse"));

    /* renamed from: h, reason: collision with root package name */
    JButton f2939h = new JButton(C1818g.b("Browse"));

    /* renamed from: i, reason: collision with root package name */
    JButton f2940i = new JButton(C1818g.b("Browse"));

    /* renamed from: j, reason: collision with root package name */
    JButton f2941j = new JButton(C1818g.b("Detect"));

    /* renamed from: k, reason: collision with root package name */
    JButton f2942k = new JButton(C1818g.b("Detect"));

    /* renamed from: l, reason: collision with root package name */
    JLabel f2943l = new JLabel();

    /* renamed from: m, reason: collision with root package name */
    JTextArea f2944m = new JTextArea(8, 0);

    /* renamed from: n, reason: collision with root package name */
    aV f2945n = null;

    /* renamed from: o, reason: collision with root package name */
    ArrayList f2946o = new ArrayList();

    /* renamed from: s, reason: collision with root package name */
    private String f2947s = null;

    /* renamed from: t, reason: collision with root package name */
    private List f2948t = null;

    /* renamed from: u, reason: collision with root package name */
    private G.bS f2949u = null;

    /* renamed from: p, reason: collision with root package name */
    CardLayout f2950p = null;

    /* renamed from: q, reason: collision with root package name */
    JPanel f2951q = new JPanel();

    /* renamed from: r, reason: collision with root package name */
    JPanel f2952r = new JPanel();

    public C0224at() {
        l();
    }

    private void l() {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Project Configuration")));
        setLayout(new BoxLayout(this, 1));
        this.f2932a.addFocusListener(new aC(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Project Name")));
        jPanel.add(BorderLayout.CENTER, this.f2932a);
        this.f2932a.addKeyListener(new aD(this));
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.setBorder(BorderFactory.createTitledBorder(C1818g.b("Project Directory")));
        C1807j.u();
        this.f2933b.setText(C1807j.u() + this.f2932a.getText() + File.separator);
        this.f2933b.setEditable(false);
        jPanel2.add(BorderLayout.CENTER, this.f2933b);
        jPanel2.add("East", this.f2939h);
        this.f2939h.addActionListener(new C0230az(this));
        add(jPanel2);
        this.f2952r.setLayout(new BorderLayout());
        this.f2952r.setBorder(BorderFactory.createTitledBorder(C1818g.b("Firmware")));
        this.f2950p = new CardLayout();
        this.f2951q.setLayout(this.f2950p);
        JPanel jPanel3 = new JPanel();
        long jCurrentTimeMillis = System.currentTimeMillis();
        jPanel3.setLayout(new BorderLayout(5, 5));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout(5, 5));
        this.f2935d.addItem(new aA(this, null));
        try {
            File[] fileArrN = C1807j.n();
            String strP = null;
            String strI = null;
            if (G.T.a().c() != null) {
                strI = G.T.a().c().i();
                strP = G.T.a().c().P();
            }
            for (int i2 = 0; i2 < fileArrN.length; i2++) {
                if (fileArrN[i2].isFile()) {
                    String[] strArrB = C0200z.b(fileArrN[i2]);
                    for (int i3 = 0; i3 < strArrB.length; i3++) {
                        aA aAVar = new aA(this, fileArrN[i2], strArrB[i3]);
                        if (strI == null || !strI.equals(strArrB[i3]) || strP == null || strP.length() > 0) {
                        }
                        this.f2935d.addItem(aAVar);
                    }
                }
            }
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d(e2.getMessage(), this);
        }
        bH.C.c("Time to load ini list: " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
        jPanel4.add(BorderLayout.CENTER, this.f2935d);
        jPanel4.add("East", this.f2941j);
        this.f2941j.addActionListener(new C0225au(this));
        jPanel3.add("North", jPanel4);
        this.f2951q.add("Manual", jPanel3);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BorderLayout());
        this.f2943l.setBorder(BorderFactory.createEtchedBorder());
        jPanel6.add(BorderLayout.CENTER, this.f2943l);
        jPanel6.add("East", this.f2942k);
        this.f2942k.addActionListener(new C0226av(this));
        jPanel5.add("North", jPanel6);
        this.f2951q.add("Basic", jPanel5);
        this.f2952r.add("North", this.f2951q);
        JPanel jPanel7 = new JPanel();
        jPanel7.setLayout(new BorderLayout());
        jPanel7.add("West", this.f2937f);
        this.f2937f.addActionListener(new C0227aw(this));
        jPanel7.add(BorderLayout.CENTER, this.f2934c);
        this.f2934c.setEnabled(this.f2937f.isSelected());
        this.f2934c.setEditable(false);
        this.f2940i.setEnabled(this.f2937f.isSelected());
        this.f2940i.addActionListener(new aB(this));
        this.f2952r.add(BorderLayout.CENTER, jPanel7);
        this.f2938g = new JCheckBox(C1818g.b("Show Advanced / Offline Setup"));
        this.f2938g.addActionListener(new C0228ax(this));
        JPanel jPanel8 = new JPanel();
        jPanel8.setLayout(new BorderLayout());
        jPanel8.add("East", this.f2938g);
        this.f2952r.add("South", jPanel8);
        add(this.f2952r);
        a(false);
        JPanel jPanel9 = new JPanel();
        jPanel9.setLayout(new GridLayout());
        jPanel9.setBorder(BorderFactory.createTitledBorder(C1818g.b("Project Description")));
        JScrollPane jScrollPane = new JScrollPane(this.f2944m);
        this.f2944m.setLineWrap(true);
        jPanel9.add(jScrollPane);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane.setPreferredSize(com.efiAnalytics.ui.eJ.a(300, 160));
        add(jPanel9);
    }

    protected String b() {
        return !this.f2933b.getText().trim().equals("") ? this.f2933b.getText().substring(0, this.f2933b.getText().lastIndexOf(File.separatorChar)) : ".";
    }

    public void a(boolean z2) {
        if (z2) {
            this.f2950p.show(this.f2951q, "Manual");
            this.f2952r.setBorder(BorderFactory.createTitledBorder(C1818g.b("ECU Definition")));
        } else {
            this.f2950p.show(this.f2951q, "Basic");
            this.f2952r.setBorder(BorderFactory.createTitledBorder(C1818g.b("Firmware")));
        }
        this.f2938g.setSelected(z2);
    }

    public void b(String str) throws IllegalArgumentException {
        this.f2943l.setText(str);
    }

    public void b(boolean z2) {
        this.f2932a.setEnabled(!z2);
        this.f2933b.setEnabled(!z2);
        this.f2939h.setEnabled(!z2);
    }

    public void c(String str) {
        if (str != null && !str.endsWith(File.separator)) {
            str = str + File.separator;
        }
        this.f2933b.setText(str);
    }

    public void d(String str) throws IllegalArgumentException {
        a(new File(str));
    }

    public void a(File file) throws IllegalArgumentException {
        aA aAVar = new aA(this, file);
        this.f2935d.addItem(aAVar);
        this.f2935d.setSelectedItem(aAVar);
        this.f2934c.setText(file.getAbsolutePath());
        String strA = C0200z.a(file);
        if (strA != null && G.T.a().c() != null && G.T.a().c().i().equals(strA) && G.T.a().c().P() != null && G.T.a().c().P().length() > 0) {
            this.f2943l.setText(G.T.a().c().P());
        } else if (strA != null) {
            this.f2943l.setText(C1818g.b("Serial Signature") + ": " + strA);
        }
    }

    public String c() {
        return this.f2944m.getText();
    }

    public void e(String str) {
        this.f2944m.setText(str);
    }

    protected void f(String str) {
        this.f2933b.setText(str + File.separator + this.f2932a.getText());
    }

    public void d() {
        if (this.f2945n != null) {
            this.f2945n.e();
            this.f2945n = null;
        }
        this.f2945n = new aV(com.efiAnalytics.ui.bV.b(this));
        this.f2945n.setVisible(true);
        this.f2945n.a(this);
        Iterator it = this.f2946o.iterator();
        while (it.hasNext()) {
            this.f2945n.a((A.o) it.next());
        }
    }

    public boolean e() {
        String str;
        str = "";
        str = h() == null ? str + C1818g.b("ECU Configuration is Required, Please provide a valid ECU configuration File.") + "\n" : "";
        File fileG = g();
        if (fileG == null) {
            str = str + C1818g.b("Project Directory is Required") + ",\n" + C1818g.b("Please select a project directory.") + "\n";
        }
        if (this.f2933b.isEnabled() && fileG.exists()) {
            str = str + C1818g.b("Project Directory already exists") + ",\n" + C1818g.b("Please select a unique project name-directory combination.") + "\n";
        }
        if (str.length() <= 0) {
            return true;
        }
        com.efiAnalytics.ui.bV.d(str, this);
        return false;
    }

    public String f() {
        return this.f2932a.getText();
    }

    public void g(String str) {
        this.f2932a.setText(str);
    }

    public File g() {
        if (this.f2933b.getText().length() == 0) {
            return null;
        }
        return new File(this.f2933b.getText());
    }

    public File h() {
        if (this.f2937f.isSelected()) {
            File file = new File(this.f2934c.getText());
            if (file.exists()) {
                return file;
            }
            return null;
        }
        aA aAVar = (aA) this.f2935d.getSelectedItem();
        if (aAVar == null || aAVar.a() == null) {
            return null;
        }
        return aAVar.a();
    }

    public void b(String str, String str2, List list, G.bS bSVar) {
        bH.C.c("Ok, device accepted:" + ((Object) bSVar));
        h(str2);
        a(list);
        a(bSVar);
        if (bSVar.f()) {
            if (com.efiAnalytics.ui.bV.a(C1818g.b("The selected controller is in Bootload Mode.") + "\n" + C1818g.b("This means there is no firmware loaded or a boot jumper has been installed.") + "\n" + C1818g.b("To use this controller you must have firmware installed.") + "\n\n" + C1818g.b("Would you like to open the Firmware Loader now?"), (Component) this, true)) {
                C0338f.a().f();
                C0338f.a().i((Window) cZ.a().c());
                return;
            }
            return;
        }
        aA aAVarI = i(bSVar.b());
        if (aAVarI == null) {
            W.R rA = C0197w.a(bSVar.b());
            if (rA.a()) {
                if (JOptionPane.showConfirmDialog(this, C1818g.b("Your installation of " + C1798a.f13268b + " does not have a configuration to support the firmware found:") + "\n" + bSVar.c() + "\n\n" + C1818g.b("Required Serial Signature:") + "\n" + bSVar.b() + "\n\n" + C1818g.b("However this file is available on EFI Analytics servers.") + "\n" + C1818g.b("Would you like " + C1798a.f13268b + " to download the file for you?") + "\n" + C1818g.b("File Size") + ": " + bH.W.a(rA.b()) + "\n", C1818g.b("Internet Download"), 0, 3) == 0) {
                    aAVarI = b(bSVar);
                }
                if (aAVarI != null) {
                    this.f2935d.addItem(aAVarI);
                    this.f2935d.setSelectedItem(aAVarI);
                }
            } else {
                com.efiAnalytics.ui.bV.d(C1818g.b(C1798a.f13268b + " does not have a configuration to support the found firmware") + ":\n" + bSVar.c() + "\n\n" + C1818g.b("Required Serial Signature") + ":\n" + bSVar.b() + "\n\n\n" + C1818g.b("Please click the 'Other' checkbox and browse") + ", \n" + C1818g.b("to the Ecu Definition (ini) file provided with your firmware."), this);
            }
        }
        String strB = null;
        if (bSVar.c() != null && bSVar.c().length() > 0 && !bSVar.c().equals(bSVar.b())) {
            strB = bSVar.c();
            this.f2943l.setText(strB);
        } else if (bSVar.b() != null && !bSVar.b().isEmpty()) {
            strB = bSVar.b();
            this.f2943l.setText(strB);
        }
        if (aAVarI != null) {
            this.f2935d.setSelectedItem(aAVarI);
            if (strB != null) {
                aAVarI.b(strB);
            }
            this.f2935d.repaint();
        }
    }

    private aA b(G.bS bSVar) {
        JDialog jDialogA = com.efiAnalytics.ui.bV.a((Window) cZ.a().c(), C1818g.b("Downloading Ecu Definition file for") + " " + bSVar.b());
        try {
            try {
                W.R rA = C0197w.a(bSVar.b(), C1807j.c());
                if (!rA.a()) {
                    com.efiAnalytics.ui.bV.d(rA.d(), this);
                    jDialogA.dispose();
                    return null;
                }
                aA aAVar = new aA(this, rA.c());
                if (bSVar.c() != null && bSVar.c().length() > 5) {
                    aAVar.b(bSVar.c());
                }
                if (aAVar != null) {
                }
                jDialogA.dispose();
                return aAVar;
            } catch (V.a e2) {
                Logger.getLogger(C0224at.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                com.efiAnalytics.ui.bV.d(e2.getMessage(), this);
                jDialogA.dispose();
                return null;
            } catch (Exception e3) {
                Logger.getLogger(C0224at.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                com.efiAnalytics.ui.bV.d("Unexpected Error tring to download Definition File.", this);
                jDialogA.dispose();
                return null;
            }
        } catch (Throwable th) {
            jDialogA.dispose();
            throw th;
        }
    }

    private aA i(String str) {
        for (int i2 = 0; i2 < this.f2935d.getItemCount(); i2++) {
            aA aAVar = (aA) this.f2935d.getItemAt(i2);
            if (aAVar.equals(str)) {
                return aAVar;
            }
        }
        return null;
    }

    @Override // A.o
    public void a() {
        this.f2945n.b(this);
        Iterator it = this.f2946o.iterator();
        while (it.hasNext()) {
            this.f2945n.b((A.o) it.next());
        }
    }

    @Override // A.o
    public void b(double d2) {
    }

    @Override // A.o
    public void a(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() throws IllegalArgumentException {
        String strB = com.efiAnalytics.ui.bV.b(this, C1818g.b("Find ecu definition file"), new String[]{"ini", "ecu"}, "", "");
        if (strB == null || strB.equals("")) {
            this.f2937f.setSelected(false);
            this.f2935d.setEnabled(true);
        } else if (C0200z.a(strB) != null) {
            d(strB);
            this.f2937f.setSelected(true);
        } else {
            com.efiAnalytics.ui.bV.d(C1818g.b("The selected file does not appear to be a valid ECU Definition file.") + "\n" + C1818g.b("No Valid Signature Found in file") + ":\n" + strB, this);
            this.f2937f.setSelected(false);
        }
    }

    @Override // A.o
    public boolean a(String str, String str2, List list, G.bS bSVar) {
        if (str2.contains(CallSiteDescriptor.TOKEN_DELIMITER)) {
            if (!C1899c.a().a(str2.substring(0, str2.indexOf(CallSiteDescriptor.TOKEN_DELIMITER)))) {
                com.efiAnalytics.ui.bV.d(C1818g.b("The Controller found requires a driver that is not available in this edition of the application."), this);
                return false;
            }
        }
        new C0229ay(this, str, str2, list, bSVar).start();
        return true;
    }

    public String i() {
        return this.f2947s;
    }

    public void h(String str) {
        this.f2947s = str;
    }

    public List j() {
        return this.f2948t;
    }

    public void a(List list) {
        this.f2948t = list;
    }

    public G.bS k() {
        return this.f2949u;
    }

    public void a(G.bS bSVar) {
        this.f2949u = bSVar;
    }

    @Override // A.o
    public void a(A.x xVar) {
    }
}
