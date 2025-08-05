package bm;

import G.C0088bu;
import G.R;
import G.bC;
import bH.C;
import bt.InterfaceC1284L;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: bm.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bm/c.class */
public class C1198c implements InterfaceC1284L {
    @Override // bt.InterfaceC1284L
    public boolean a(C0088bu c0088bu) {
        return (c0088bu instanceof bC) && c0088bu.aJ().equals("std_ms3SdConsole");
    }

    @Override // bt.InterfaceC1284L
    public JPanel a(R r2, C0088bu c0088bu) {
        try {
            return new bk.d(r2);
        } catch (V.a e2) {
            C.a("Unable to show MS3 SD File Browser.", e2, this);
            JPanel jPanel = new JPanel();
            jPanel.add(new JLabel("Error"));
            return jPanel;
        }
    }
}
