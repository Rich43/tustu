package bm;

import G.C0088bu;
import G.R;
import G.bC;
import bH.C;
import bp.C1217a;
import bt.InterfaceC1284L;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: bm.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bm/d.class */
public class C1199d implements InterfaceC1284L {
    @Override // bt.InterfaceC1284L
    public boolean a(C0088bu c0088bu) {
        return (c0088bu instanceof bC) && c0088bu.aJ().equals("std_trigwiz");
    }

    @Override // bt.InterfaceC1284L
    public JPanel a(R r2, C0088bu c0088bu) {
        try {
            return new C1217a(r2);
        } catch (Exception e2) {
            C.a("Unable to show TriggerWizard.", e2, this);
            JPanel jPanel = new JPanel();
            jPanel.add(new JLabel("Error"));
            return jPanel;
        }
    }
}
