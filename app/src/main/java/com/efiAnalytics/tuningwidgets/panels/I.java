package com.efiAnalytics.tuningwidgets.panels;

import W.C0184j;
import com.efiAnalytics.ui.InterfaceC1579bq;
import com.efiAnalytics.ui.InterfaceC1662et;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/I.class */
public class I {

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1662et f10256b = null;

    /* renamed from: a, reason: collision with root package name */
    List f10257a = new ArrayList();

    public InterfaceC1579bq a(InterfaceC1579bq interfaceC1579bq, C0184j c0184j, Component component) {
        int i2 = Integer.parseInt(this.f10256b.b("fieldSmoothingFactor_" + c0184j.a(), "0"));
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Field Smoothing (" + c0184j.s() + ")", i2 > 0);
        jCheckBoxMenuItem.addActionListener(new J(this, c0184j, component));
        interfaceC1579bq.add(jCheckBoxMenuItem);
        JMenuItem jMenuItem = new JMenuItem("Smoothing Factor");
        jMenuItem.setEnabled(i2 > 0);
        jMenuItem.addActionListener(new K(this, c0184j, component));
        interfaceC1579bq.add(jMenuItem);
        return interfaceC1579bq;
    }

    public void a(C0184j c0184j, int i2) {
        this.f10256b.a("fieldSmoothingFactor_" + c0184j.a(), Integer.toString(i2));
        c0184j.b(true);
        c0184j.g(i2);
        c(c0184j.a());
    }

    public void a(C0184j c0184j) {
        this.f10256b.a("fieldSmoothingFactor_" + c0184j.a(), "0");
        c0184j.b(false);
        c(c0184j.a());
    }

    public boolean a(String str) {
        return Integer.parseInt(this.f10256b.b(new StringBuilder().append("fieldSmoothingFactor_").append(str).toString(), "0")) > 0;
    }

    public int b(String str) {
        return Integer.parseInt(this.f10256b.b("fieldSmoothingFactor_" + str, "0"));
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f10256b = interfaceC1662et;
    }

    public void a(L l2) {
        this.f10257a.add(l2);
    }

    private void c(String str) {
        Iterator it = this.f10257a.iterator();
        while (it.hasNext()) {
            ((L) it.next()).a(str);
        }
    }
}
