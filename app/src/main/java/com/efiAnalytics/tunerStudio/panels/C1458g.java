package com.efiAnalytics.tunerStudio.panels;

import aP.C0338f;
import aP.cZ;
import ai.C0512b;
import bt.C1324bf;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.HashMap;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import jdk.internal.dynalink.CallSiteDescriptor;
import s.C1818g;
import sun.security.pkcs11.wrapper.Constants;

/* renamed from: com.efiAnalytics.tunerStudio.panels.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/g.class */
public class C1458g extends C1324bf {

    /* renamed from: c, reason: collision with root package name */
    JButton f10112c;

    /* renamed from: d, reason: collision with root package name */
    JButton f10113d;

    /* renamed from: e, reason: collision with root package name */
    aE.a f10114e;

    /* renamed from: i, reason: collision with root package name */
    static String f10118i = C1818g.b(Action.DEFAULT);

    /* renamed from: a, reason: collision with root package name */
    JComboBox f10110a = new JComboBox();

    /* renamed from: f, reason: collision with root package name */
    JTextPane f10115f = new JTextPane();

    /* renamed from: g, reason: collision with root package name */
    JDialog f10116g = null;

    /* renamed from: h, reason: collision with root package name */
    HashMap f10117h = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    com.efiAnalytics.tuningwidgets.panels.A f10111b = new com.efiAnalytics.tuningwidgets.panels.A();

    public C1458g(aE.a aVar) {
        this.f10114e = aVar;
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Data Log Profile Editor")));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("West", new JLabel(Constants.INDENT + C1818g.b("Current Data Log Profile") + ": "));
        jPanel2.add(BorderLayout.CENTER, this.f10110a);
        jPanel.add("West", jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(1, 0, eJ.a(3), eJ.a(3)));
        Dimension dimension = new Dimension(eJ.a(24), eJ.a(24));
        try {
            this.f10112c = new JButton(null, new ImageIcon(cO.a().a(cO.f11085a, jPanel3, 20)));
            this.f10112c.setPreferredSize(dimension);
            this.f10112c.setToolTipText(C1818g.b("Add Logging Profile"));
            this.f10112c.addActionListener(new C1459h(this));
            jPanel3.add(this.f10112c);
        } catch (V.a e2) {
            bH.C.a(e2.getLocalizedMessage());
        }
        try {
            this.f10113d = new JButton(null, new ImageIcon(cO.a().a(cO.f11086b, jPanel3, 20)));
            this.f10113d.setPreferredSize(dimension);
            this.f10113d.setToolTipText(C1818g.b("Delete Current Logging Profile"));
            this.f10113d.addActionListener(new C1460i(this));
            jPanel3.add(this.f10113d);
        } catch (V.a e3) {
            bH.C.a(e3.getLocalizedMessage());
        }
        try {
            JButton jButton = new JButton(null, new ImageIcon(cO.a().a(cO.f11087c, jPanel3, 20)));
            jButton.setPreferredSize(dimension);
            jButton.setToolTipText(C1818g.b("Open Data Log Profiles Help"));
            jButton.addActionListener(new C1461j(this));
            jPanel3.add(jButton);
        } catch (V.a e4) {
            bH.C.a(e4.getLocalizedMessage());
        }
        jPanel3.add(new JLabel(" "));
        jPanel.add("East", jPanel3);
        jPanel.add("South", new JLabel(" "));
        add("North", jPanel);
        String[] strArrA = bH.R.a(aVar.P());
        boolean z2 = false;
        for (int i2 = 0; i2 < strArrA.length; i2++) {
            this.f10110a.addItem(new C1465n(this, strArrA[i2]));
            if (!z2 && strArrA[i2].equals("")) {
                z2 = true;
            }
            this.f10117h.put(strArrA[i2], aVar.w(strArrA[i2]));
        }
        if (!z2) {
            this.f10117h.put("", new String[0]);
            this.f10110a.insertItemAt(new C1465n(this, ""), 0);
        }
        this.f10110a.addItemListener(new C1462k(this));
        add(BorderLayout.CENTER, this.f10111b);
        String strO = aVar.O();
        this.f10110a.setSelectedItem(new C1465n(this, strO));
        b(strO);
        this.f10111b.h();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) {
        if (!this.f10111b.f().equals("")) {
            this.f10117h.put(this.f10111b.f(), this.f10111b.i());
        }
        if (str.equals(f10118i) || str.equals("")) {
            str = "";
            this.f10111b.a(false);
            this.f10113d.setEnabled(false);
        } else {
            this.f10111b.a(true);
            this.f10113d.setEnabled(true);
        }
        this.f10111b.a(str, bH.R.a((String[]) this.f10117h.get(str)));
    }

    public void c() {
        if (!this.f10111b.f().equals("")) {
            this.f10117h.put(this.f10111b.f(), this.f10111b.i());
        }
        this.f10114e.a(this.f10111b.f(), this.f10111b.i());
    }

    public void d() {
        String strA;
        do {
            strA = bV.a((Component) this, false, C1818g.b("Name for your new Logging Profile"), "");
            if (strA == null || strA.equals("")) {
                break;
            }
        } while (!c(strA));
        if (strA == null || strA.equals("")) {
            return;
        }
        this.f10117h.put(this.f10111b.f(), this.f10111b.i());
        this.f10117h.put(strA, this.f10111b.i());
        this.f10111b.a(true);
        this.f10113d.setEnabled(true);
        C1465n c1465n = new C1465n(this, strA);
        this.f10110a.addItem(c1465n);
        this.f10110a.setSelectedItem(c1465n);
        this.f10111b.g();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        C0512b c0512b = new C0512b();
        c0512b.a(C1818g.b("Data Log Profiles"));
        c0512b.b("/help/datalogProfiles.html");
        C0338f.a().a(c0512b, cZ.a().c());
    }

    private boolean c(String str) {
        if (!str.contains(CallSiteDescriptor.OPERATOR_DELIMITER) && !str.contains(";") && !str.contains(",")) {
            return true;
        }
        bV.d(C1818g.b("Illegal Characters.") + " | ; ,", this);
        return false;
    }

    public void e() {
        if (bV.a(C1818g.b("Are you sure you want to delete logging Profile?") + "\n" + C1818g.b("Profile") + ": " + this.f10111b.f(), (Component) this, true)) {
            this.f10117h.remove(this.f10111b.f());
            this.f10110a.removeItem(new C1465n(this, this.f10111b.f()));
        }
    }

    public boolean f() {
        return true;
    }

    public void g() {
        c();
        String[] strArr = new String[this.f10110a.getItemCount()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = ((C1465n) this.f10110a.getItemAt(i2)).a();
            if (!strArr[i2].equals("")) {
                String[] strArr2 = (String[]) this.f10117h.get(strArr[i2]);
                if (strArr2 != null) {
                    this.f10114e.a(strArr[i2], strArr2);
                } else {
                    bH.C.b("disabledFields null for log profile: " + strArr[i2] + ", not saving.");
                }
            }
        }
        this.f10114e.a(strArr);
        this.f10114e.v(i());
    }

    private String i() {
        return ((C1465n) this.f10110a.getSelectedItem()).a();
    }

    public void a(Component component) {
        this.f10116g = new JDialog(bV.a(component), C1818g.b("Data Log Profile Editor"));
        this.f10116g.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton(C1818g.b("Cancel"));
        jButton.addActionListener(new C1463l(this));
        JButton jButton2 = new JButton(C1818g.b(XIncludeHandler.HTTP_ACCEPT));
        jButton2.addActionListener(new C1464m(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (bV.d()) {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        } else {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        }
        this.f10116g.add("South", jPanel);
        this.f10116g.pack();
        bV.a((Window) bV.a(component), (Component) this.f10116g);
        this.f10116g.setVisible(true);
        validate();
        this.f10116g.pack();
        this.f10116g.setResizable(false);
    }
}
