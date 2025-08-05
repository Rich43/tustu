package com.efiAnalytics.ui;

import c.InterfaceC1386e;
import java.util.HashMap;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eK.class */
public class eK extends JTabbedPane {

    /* renamed from: a, reason: collision with root package name */
    private HashMap f11487a = new HashMap();

    public boolean d(String str) {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (getTitleAt(i2).equals(str)) {
                return true;
            }
        }
        return false;
    }

    public boolean e(String str) {
        return f(str) == getSelectedIndex();
    }

    public int f(String str) {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (getTitleAt(i2).equals(str)) {
                return i2;
            }
        }
        return -1;
    }

    public boolean g(String str) {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (getTitleAt(i2).equals(str)) {
                setSelectedIndex(i2);
                return true;
            }
        }
        return false;
    }

    public void a(String str, boolean z2) {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (getTitleAt(i2).equals(str)) {
                setEnabledAt(i2, z2);
            }
        }
    }

    @Override // javax.swing.JTabbedPane
    public void setEnabledAt(int i2, boolean z2) {
        if (SwingUtilities.isEventDispatchThread()) {
            super.setEnabledAt(i2, z2);
        } else {
            SwingUtilities.invokeLater(new eL(this, i2, z2));
        }
    }

    public void a(String str, InterfaceC1386e interfaceC1386e) {
        i().put(str, interfaceC1386e);
    }

    public void d() {
        boolean zA;
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            InterfaceC1386e interfaceC1386e = (InterfaceC1386e) i().get(getTitleAt(i2));
            if (interfaceC1386e != null && isEnabledAt(i2) != (zA = interfaceC1386e.a())) {
                setEnabledAt(i2, zA);
            }
        }
    }

    public HashMap i() {
        return this.f11487a;
    }

    public void j() {
        this.f11487a.clear();
    }

    @Override // javax.swing.JTabbedPane, java.awt.Container
    public void removeAll() {
        super.removeAll();
        j();
    }
}
