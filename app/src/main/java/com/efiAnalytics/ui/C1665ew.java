package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.ui.ew, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ew.class */
public class C1665ew extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f11612a = new ArrayList();

    public C1665ew() {
        setLayout(new GridLayout(2, 1));
        C1668ez c1668ez = new C1668ez(this, 1);
        c1668ez.setFocusable(false);
        c1668ez.addActionListener(new C1666ex(this));
        add(c1668ez);
        C1668ez c1668ez2 = new C1668ez(this, 2);
        c1668ez2.setFocusable(false);
        c1668ez2.addActionListener(new C1667ey(this));
        add(c1668ez2);
    }

    protected void a() {
        Iterator it = this.f11612a.iterator();
        while (it.hasNext()) {
            ((cP) it.next()).l();
        }
    }

    protected void b() {
        Iterator it = this.f11612a.iterator();
        while (it.hasNext()) {
            ((cP) it.next()).m();
        }
    }

    public void a(cP cPVar) {
        this.f11612a.add(cPVar);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        for (Component component : getComponents()) {
            component.setEnabled(z2);
        }
    }

    @Override // javax.swing.JComponent
    public void setToolTipText(String str) {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof JComponent) {
                ((JComponent) components[i2]).setToolTipText(str);
            }
        }
        super.setToolTipText(str);
    }
}
