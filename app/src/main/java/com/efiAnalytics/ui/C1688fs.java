package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.math3.geometry.VectorFormat;

/* renamed from: com.efiAnalytics.ui.fs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fs.class */
public class C1688fs extends JDialog implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    public boolean f11699a;

    /* renamed from: b, reason: collision with root package name */
    JButton f11700b;

    /* renamed from: c, reason: collision with root package name */
    JButton f11701c;

    /* renamed from: d, reason: collision with root package name */
    String f11702d;

    /* renamed from: e, reason: collision with root package name */
    ArrayList f11703e;

    /* renamed from: f, reason: collision with root package name */
    ArrayList f11704f;

    /* renamed from: g, reason: collision with root package name */
    Window f11705g;

    /* renamed from: h, reason: collision with root package name */
    boolean f11706h;

    /* renamed from: i, reason: collision with root package name */
    fw f11707i;

    /* renamed from: j, reason: collision with root package name */
    KeyListener f11708j;

    /* renamed from: m, reason: collision with root package name */
    private fx f11709m;

    /* renamed from: n, reason: collision with root package name */
    private bH.aa f11710n;

    /* renamed from: k, reason: collision with root package name */
    JTextField f11711k;

    /* renamed from: l, reason: collision with root package name */
    JPanel f11712l;

    public C1688fs(Window window, String str, boolean z2, String str2, boolean z3) {
        this(window, str, z2, str2, z3, (fw) null);
    }

    public C1688fs(Window window, String str, boolean z2, String str2, boolean z3, String[] strArr) {
        this(window, str, z2, str2, z3, (fw) null, null, null, strArr);
    }

    public C1688fs(Window window, String str, boolean z2, String str2, boolean z3, fw fwVar) {
        this(window, str, z2, str2, z3, fwVar, null, null, null);
    }

    public C1688fs(Window window, String str, boolean z2, String str2, boolean z3, fw fwVar, fx fxVar, bH.aa aaVar) {
        this(window, str, z2, str2, z3, fwVar, fxVar, aaVar, null);
    }

    public C1688fs(Window window, String str, boolean z2, String str2, boolean z3, fw fwVar, fx fxVar, bH.aa aaVar, String[] strArr) {
        super(window, "User Parameter", Dialog.ModalityType.DOCUMENT_MODAL);
        this.f11699a = false;
        this.f11702d = null;
        this.f11703e = new ArrayList();
        this.f11704f = new ArrayList();
        this.f11705g = null;
        this.f11706h = true;
        this.f11707i = null;
        this.f11708j = null;
        this.f11709m = null;
        this.f11710n = null;
        this.f11711k = null;
        this.f11712l = new JPanel();
        this.f11710n = aaVar;
        setTitle(a(""));
        this.f11706h = z2;
        this.f11707i = fwVar;
        this.f11709m = fxVar;
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f11712l);
        this.f11712l.setLayout(new BorderLayout());
        this.f11712l.setBorder(BorderFactory.createEmptyBorder(eJ.a(8), eJ.a(8), eJ.a(8), eJ.a(8)));
        this.f11702d = str;
        this.f11705g = window;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        this.f11712l.add(BorderLayout.CENTER, jPanel);
        StringTokenizer stringTokenizer = new StringTokenizer(str2 == null ? a("User defined values required for this formula.") : str2, "\n");
        while (stringTokenizer.hasMoreElements()) {
            jPanel.add(new JLabel(a(stringTokenizer.nextToken())));
        }
        this.f11708j = new ft(this);
        super.addKeyListener(new fu(this));
        jPanel.add(new JLabel(""));
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int iIndexOf = str.indexOf(VectorFormat.DEFAULT_PREFIX, i2);
            if (iIndexOf == -1) {
                this.f11712l.add(new JLabel(" "), "West");
                this.f11712l.add(new JLabel(" "), "East");
                this.f11712l.add(new JLabel(" "), "North");
                a(z3);
                pack();
                Dimension size = window.getSize();
                Dimension size2 = getSize();
                Point location = window.getLocation();
                setLocation((int) (location.getX() + ((size.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((size.getHeight() - size2.getHeight()) / 2.0d)));
                addKeyListener(this.f11708j);
                new fv(this).start();
                setVisible(true);
                return;
            }
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new GridLayout(1, 2));
            int iIndexOf2 = str.indexOf("}", iIndexOf);
            i2 = iIndexOf2;
            String strSubstring = str.substring(iIndexOf + 1, iIndexOf2);
            if (!strSubstring.equals("") && !this.f11703e.contains(strSubstring)) {
                JTextField cdo = z2 ? new Cdo() : new JTextField();
                if (this.f11711k == null) {
                    this.f11711k = cdo;
                }
                cdo.setBorder(BorderFactory.createLoweredBevelBorder());
                cdo.addKeyListener(this.f11708j);
                if (strSubstring.indexOf("Field Name") != -1) {
                    cdo.setName("Field");
                }
                this.f11703e.add(strSubstring);
                this.f11704f.add(cdo);
                if (fwVar != null) {
                    cdo.setText(fwVar.a("userParameter_" + strSubstring, ""));
                } else if (strArr == null || strArr[i3] == null) {
                    cdo.setText("");
                } else {
                    cdo.setText(strArr[i3]);
                }
                jPanel2.add(new JLabel(strSubstring));
                jPanel2.add(cdo);
                jPanel.add(jPanel2);
                i3++;
            }
        }
    }

    private String a(String str) {
        if (this.f11710n == null) {
            this.f11710n = bV.a();
        }
        return this.f11710n != null ? this.f11710n.a(str) : str;
    }

    public String a() {
        if (!this.f11699a) {
            return null;
        }
        String strB = this.f11702d;
        for (int i2 = 0; i2 < this.f11703e.size(); i2++) {
            String str = (String) this.f11703e.get(i2);
            JTextField jTextField = (JTextField) this.f11704f.get(i2);
            strB = bH.W.b(strB, VectorFormat.DEFAULT_PREFIX + str + "}", jTextField.getText());
            if (this.f11707i != null) {
                fw fwVar = this.f11707i;
                StringBuilder sb = new StringBuilder();
                fw fwVar2 = this.f11707i;
                fwVar.b(sb.append("userParameter_").append(str).toString(), jTextField.getText());
            }
        }
        return strB;
    }

    protected void a(boolean z2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        a(jPanel);
        if (z2) {
            b(jPanel);
        }
        this.f11712l.add("South", jPanel);
    }

    protected void a(JPanel jPanel) {
        JButton jButton = new JButton(a("OK"));
        this.f11700b = jButton;
        jPanel.add(jButton);
        this.f11700b.addActionListener(this);
    }

    protected void b(JPanel jPanel) {
        JButton jButton = new JButton(a("Cancel"));
        this.f11701c = jButton;
        jPanel.add(jButton);
        this.f11701c.addActionListener(this);
    }

    protected void b() {
        dispose();
    }

    protected void c() {
        if (d()) {
            this.f11699a = true;
            setVisible(false);
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.f11700b) {
            c();
        }
        if (actionEvent.getSource() == this.f11701c) {
            this.f11699a = false;
            b();
        }
    }

    private boolean d() {
        Iterator it = this.f11704f.iterator();
        while (it.hasNext()) {
            JTextField jTextField = (JTextField) it.next();
            if (this.f11709m != null) {
                if (!this.f11709m.a(jTextField.getText())) {
                    return false;
                }
            } else if (this.f11706h && (jTextField.getName() == null || !jTextField.getName().equals("Field"))) {
                try {
                    Double.parseDouble(jTextField.getText());
                } catch (Exception e2) {
                    bV.d(a("Values Must Be Numeric"), this.f11705g);
                    return false;
                }
            } else if (jTextField.equals("")) {
                bV.d(a("You must enter a value."), this.f11705g);
            }
        }
        return true;
    }
}
