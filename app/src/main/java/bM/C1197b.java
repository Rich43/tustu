package bm;

import G.C0088bu;
import G.R;
import G.bC;
import bH.C;
import bk.C1176a;
import bt.InterfaceC1284L;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: bm.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bm/b.class */
public class C1197b implements InterfaceC1284L {
    @Override // bt.InterfaceC1284L
    public boolean a(C0088bu c0088bu) {
        return (c0088bu instanceof bC) && c0088bu.aJ().equals("std_ms3Rtc");
    }

    @Override // bt.InterfaceC1284L
    public JPanel a(R r2, C0088bu c0088bu) {
        try {
            return new C1176a(r2);
        } catch (V.a e2) {
            C.a("Unable to show MS3 Real-Time Clock Interface.", e2, this);
            JPanel jPanel = new JPanel();
            jPanel.add(new JLabel("Error"));
            return jPanel;
        }
    }
}
