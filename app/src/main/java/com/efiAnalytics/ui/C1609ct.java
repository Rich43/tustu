package com.efiAnalytics.ui;

import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.ui.ct, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ct.class */
public class C1609ct extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f11304a;

    public C1609ct(Window window, JComponent jComponent, String str, InterfaceC1535a interfaceC1535a, int i2) {
        super(window, str);
        this.f11304a = new ArrayList();
        setLayout(new BorderLayout());
        a(interfaceC1535a);
        add(BorderLayout.CENTER, jComponent);
        add("South", a(i2));
        pack();
    }

    private JPanel a(int i2) {
        JButton jButton;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        boolean zD = bV.d();
        if (zD) {
            jButton = new JButton("Ok");
            jButton.setMnemonic(79);
        } else {
            jButton = new JButton(XIncludeHandler.HTTP_ACCEPT);
            jButton.setMnemonic(65);
        }
        jButton.addActionListener(new C1610cu(this));
        JButton jButton2 = new JButton("Cancel");
        jButton2.setMnemonic(27);
        jButton2.addActionListener(new C1611cv(this));
        JButton jButton3 = new JButton("Apply");
        jButton3.setMnemonic(80);
        jButton3.addActionListener(new C1612cw(this));
        if (zD) {
            if ((i2 | 1) == i2) {
                jPanel.add(jButton);
            }
            if ((i2 | 2) == i2) {
                jPanel.add(jButton2);
            }
            if ((i2 | 4) == i2) {
                jPanel.add(jButton3);
            }
        } else {
            if ((i2 | 4) == i2) {
                jPanel.add(jButton3);
            }
            if ((i2 | 2) == i2) {
                jPanel.add(jButton2);
            }
            if ((i2 | 1) == i2) {
                jPanel.add(jButton);
            }
        }
        return jPanel;
    }

    protected void a() {
        Iterator it = this.f11304a.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1535a) it.next()).a()) {
                return;
            }
        }
        dispose();
    }

    protected void b() {
        Iterator it = this.f11304a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1535a) it.next()).b();
        }
        dispose();
    }

    protected void c() {
        Iterator it = this.f11304a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1535a) it.next()).c();
        }
    }

    public void a(InterfaceC1535a interfaceC1535a) {
        if (this.f11304a.contains(interfaceC1535a)) {
            return;
        }
        this.f11304a.add(interfaceC1535a);
    }
}
