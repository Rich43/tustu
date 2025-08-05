package com.efiAnalytics.tuningwidgets.panels;

import G.C0113cs;
import com.efiAnalytics.ui.C1642e;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/V.class */
public class V extends C1642e {
    public V() {
    }

    public V(G.R r2) {
        setEditable(false);
        a(r2);
    }

    public void a(G.R r2) {
        if (r2.c().equals(C0113cs.f1154a)) {
            a();
        } else {
            b(r2);
        }
    }

    private void a() {
        a(bH.W.a(C0113cs.a().b()));
    }

    private void b(G.R r2) {
        a(bH.W.a(r2.s()));
    }

    private void a(String[] strArr) {
        ActionListener[] actionListeners = getActionListeners();
        for (ActionListener actionListener : actionListeners) {
            removeActionListener(actionListener);
        }
        removeAllItems();
        a("");
        for (String str : strArr) {
            a(str);
        }
        setSelectedItem("");
        for (ActionListener actionListener2 : actionListeners) {
            addActionListener(actionListener2);
        }
    }
}
