package com.efiAnalytics.ui;

import bH.C0997e;
import bH.C1018z;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import jdk.internal.dynalink.CallSiteDescriptor;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dS.class */
public class dS extends JDialog implements ClipboardOwner {

    /* renamed from: o, reason: collision with root package name */
    private C1643ea f11369o;

    /* renamed from: p, reason: collision with root package name */
    private C1643ea f11370p;

    /* renamed from: q, reason: collision with root package name */
    private C1643ea f11371q;

    /* renamed from: r, reason: collision with root package name */
    private C1643ea f11372r;

    /* renamed from: s, reason: collision with root package name */
    private C1643ea f11373s;

    /* renamed from: h, reason: collision with root package name */
    dZ f11374h;

    /* renamed from: i, reason: collision with root package name */
    JPanel f11375i;

    /* renamed from: j, reason: collision with root package name */
    JTextPane f11376j;

    /* renamed from: t, reason: collision with root package name */
    private int f11377t;

    /* renamed from: k, reason: collision with root package name */
    bH.N f11378k;

    /* renamed from: l, reason: collision with root package name */
    CardLayout f11379l;

    /* renamed from: m, reason: collision with root package name */
    JPanel f11380m;

    /* renamed from: n, reason: collision with root package name */
    Clipboard f11383n;

    /* renamed from: a, reason: collision with root package name */
    public static int f11362a = 1;

    /* renamed from: b, reason: collision with root package name */
    public static int f11363b = 2;

    /* renamed from: c, reason: collision with root package name */
    public static int f11364c = 4;

    /* renamed from: d, reason: collision with root package name */
    public static int f11365d = 8;

    /* renamed from: e, reason: collision with root package name */
    public static int f11366e = 16;

    /* renamed from: f, reason: collision with root package name */
    public static int f11367f = 32;

    /* renamed from: g, reason: collision with root package name */
    public static int f11368g = 64;

    /* renamed from: u, reason: collision with root package name */
    private static String f11381u = "inputs";

    /* renamed from: v, reason: collision with root package name */
    private static String f11382v = "paste";

    public dS(Window window, bH.N n2) {
        super(window);
        this.f11369o = null;
        this.f11370p = null;
        this.f11371q = null;
        this.f11372r = null;
        this.f11373s = null;
        this.f11376j = new JTextPane();
        this.f11377t = f11362a + f11363b + f11364c + f11365d + f11366e;
        this.f11378k = null;
        this.f11380m = new JPanel();
        this.f11383n = Toolkit.getDefaultToolkit().getSystemClipboard();
        super.setTitle(a("Enter Registration Information"));
        this.f11378k = n2;
        c();
        pack();
        this.f11369o.requestFocus();
    }

    private String a(String str) {
        return C1818g.b(str);
    }

    private void c() {
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(1));
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton jRadioButton = new JRadioButton(a("Inputs"));
        buttonGroup.add(jRadioButton);
        jRadioButton.addActionListener(new dT(this));
        jPanel.add(jRadioButton);
        JRadioButton jRadioButton2 = new JRadioButton(a("Paste Email"));
        buttonGroup.add(jRadioButton2);
        jRadioButton2.addActionListener(new dU(this));
        jPanel.add(jRadioButton2);
        add(jPanel, "North");
        this.f11379l = new CardLayout();
        this.f11380m.setLayout(this.f11379l);
        add(this.f11380m, BorderLayout.CENTER);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(10, 10));
        this.f11374h = new dZ(this, "Register " + this.f11378k.a() + " (Select Edition)", this.f11378k.k());
        boolean z2 = false;
        String[] strArrK = this.f11378k.k();
        int length = strArrK.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            if (this.f11378k.a(strArrK[i2])) {
                z2 = true;
                break;
            }
            i2++;
        }
        this.f11375i = new JPanel();
        this.f11375i.setBorder(BorderFactory.createTitledBorder("Registration Information"));
        this.f11375i.setLayout(new GridLayout(0, 1, 3, 3));
        this.f11369o = new C1643ea(this, a("Registered First Name") + CallSiteDescriptor.TOKEN_DELIMITER, this.f11378k.b());
        this.f11375i.add(this.f11369o);
        this.f11370p = new C1643ea(this, a("Registered Last Name") + CallSiteDescriptor.TOKEN_DELIMITER, this.f11378k.c());
        this.f11375i.add(this.f11370p);
        this.f11371q = new C1643ea(this, a("Registered eMail Address") + CallSiteDescriptor.TOKEN_DELIMITER, this.f11378k.d());
        this.f11375i.add(this.f11371q);
        this.f11372r = new C1643ea(this, a("Registration Key") + CallSiteDescriptor.TOKEN_DELIMITER, this.f11378k.f());
        this.f11375i.add(this.f11372r);
        this.f11373s = new C1643ea(this, a(this.f11378k.m()), this.f11378k.h());
        if (z2) {
            this.f11375i.add(this.f11373s);
        }
        jPanel2.add(BorderLayout.CENTER, this.f11375i);
        this.f11380m.add(jPanel2, f11381u);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(1));
        JButton jButton = new JButton(a("Paste"));
        jButton.addActionListener(new dV(this));
        jPanel4.add(jButton);
        jPanel3.add(jPanel4, "North");
        JScrollPane jScrollPane = new JScrollPane(this.f11376j);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel3.add(jScrollPane, BorderLayout.CENTER);
        this.f11376j.addKeyListener(new dW(this));
        this.f11380m.add(jPanel3, f11382v);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new FlowLayout(2));
        JButton jButton2 = new JButton(a("Ok"));
        jButton2.addActionListener(new dX(this));
        jPanel5.add(jButton2);
        JButton jButton3 = new JButton(a("Cancel"));
        jButton3.addActionListener(new dY(this));
        jPanel5.add(jButton3);
        add("South", jPanel5);
        jRadioButton.setSelected(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        try {
            this.f11376j.setText(this.f11383n.getData(DataFlavor.stringFlavor).toString());
            e();
        } catch (Exception e2) {
            System.out.println("Clipboard data not valid");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        boolean z2 = false;
        StringTokenizer stringTokenizer = new StringTokenizer(this.f11376j.getText(), "\n");
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (z2) {
                String strTrim = strNextToken.trim();
                if (strTrim.startsWith("[End Registration]")) {
                    z2 = false;
                } else if (strTrim.startsWith("First Name")) {
                    this.f11369o.f11569a.setText(strTrim.substring(strTrim.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1).trim());
                } else if (strTrim.startsWith("Last Name")) {
                    this.f11370p.f11569a.setText(strTrim.substring(strTrim.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1).trim());
                } else if (strTrim.startsWith("Registered email")) {
                    this.f11371q.f11569a.setText(strTrim.substring(strTrim.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1).trim());
                } else if (strTrim.contains("Serial Number:")) {
                    this.f11373s.f11569a.setText(strTrim.substring(strTrim.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1).trim());
                } else if (strTrim.startsWith("Registration Key")) {
                    this.f11372r.f11569a.setText(strTrim.substring(strTrim.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1).trim());
                }
            } else if (strNextToken.startsWith("[Registration]")) {
                z2 = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() throws HeadlessException {
        String str;
        str = "";
        String strTrim = this.f11369o.a().trim();
        str = strTrim.isEmpty() ? str + a("First Name") + "\n" : "";
        String strTrim2 = this.f11370p.a().trim();
        if (strTrim2.isEmpty()) {
            str = str + a("Last Name") + "\n";
        }
        String strTrim3 = this.f11371q.a().trim();
        if (strTrim3.isEmpty()) {
            str = str + a("eMail Address") + "\n";
        }
        String strTrim4 = this.f11372r.a().trim();
        if (strTrim4.equals("")) {
            str = str + a("Registration Key") + "\n";
        }
        if (!str.equals("")) {
            JOptionPane.showMessageDialog(this, a("You must provide the information used during registration for") + ":\n" + str);
            return;
        }
        String strA = this.f11378k.a();
        String str2 = "";
        String[] strArrK = this.f11378k.k();
        String str3 = strArrK[0];
        for (int i2 = 0; i2 < strArrK.length; i2++) {
            if (str3.length() > strArrK[i2].length() && !strArrK[i2].isEmpty()) {
                str3 = strArrK[i2];
            }
        }
        String strA2 = C0997e.a(strTrim, strTrim2, strA, str3, strTrim3);
        if (a(this.f11378k.l(), 3) || a(this.f11378k.l(), 4)) {
            String strA3 = "";
            int i3 = 0;
            while (true) {
                if (strTrim4.equals(strA3) || i3 >= strArrK.length) {
                    break;
                }
                if (this.f11378k.a(strArrK[i3])) {
                    String strA4 = C0997e.a(strTrim, strTrim2, strA, strArrK[i3], strTrim3, "01", "2015", this.f11373s.a());
                    if (strTrim4.equals(strA4)) {
                        this.f11374h.f11390a.setSelectedIndex(i3);
                        str2 = strA4;
                        break;
                    }
                    i3++;
                } else {
                    strA3 = C0997e.a(strTrim, strTrim2, strA, strArrK[i3], strTrim3, "01", "2015");
                    if (strTrim4.equals(strA3)) {
                        this.f11374h.f11390a.setSelectedIndex(i3);
                        str2 = strA3;
                        break;
                    }
                    i3++;
                }
            }
        } else if (strA.equals("MegaLogViewer")) {
            strA2 = C0997e.a(strTrim, strTrim2, strA, strTrim3);
        }
        if (str2 == null || str2.isEmpty()) {
            str2 = strA2;
        }
        if (this.f11378k.b(strTrim, strTrim2, str2, strTrim3, "", this.f11373s.a())) {
            return;
        }
        if (str2 != null && !str2.isEmpty() && str2.equals(strTrim4)) {
            this.f11378k.a(strTrim, strTrim2, strTrim4, strTrim3, this.f11374h.a(), this.f11378k.a(this.f11374h.a()) ? this.f11373s.a() : "");
            dispose();
            return;
        }
        boolean z2 = false;
        if (str2 != null && !str2.isEmpty()) {
            Iterator it = C1018z.i().j().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                bH.A a2 = (bH.A) it.next();
                if (a(a2.a(), a2.b())) {
                    String str4 = strA;
                    if (strArrK.length == 1) {
                        str4 = str4 + " " + strArrK[0];
                    }
                    if (bV.a("The Registration information you entered is for\n" + a2.a() + " " + a2.b() + "\nYou are currently running " + str4 + "\n\nPlease check that you have the right registration information\nor downloaded the intended application edition.", "Registration Error", this, new String[]{"Download " + (a2.a() + " " + a2.b()), "Cancel"})) {
                        aN.a(a2.c());
                    }
                    z2 = true;
                }
            }
        }
        if (z2) {
            return;
        }
        JOptionPane.showMessageDialog(this, "Invalid Registration Information!\nPlease be sure to select the correct Edition\nand use the name and email address\nExactly as presented in the registration.\nIt is case sensitive.");
    }

    private boolean a(int[] iArr, int i2) {
        for (int i3 : iArr) {
            if (i3 == i2) {
                return true;
            }
        }
        return false;
    }

    private boolean a(String str, String str2) {
        String strA = C0997e.a(this.f11369o.a().trim(), this.f11370p.a().trim(), str, str2, this.f11371q.a().trim(), "01", "2015");
        String strA2 = C0997e.a(this.f11369o.a().trim(), this.f11370p.a().trim(), str, str2, this.f11371q.a().trim(), "01", "2015", this.f11373s.a());
        String strTrim = this.f11372r.a().trim();
        return (strA != null && strA.equals(strTrim)) || (strA2 != null && strA2.equals(strTrim));
    }

    @Override // java.awt.datatransfer.ClipboardOwner
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }
}
