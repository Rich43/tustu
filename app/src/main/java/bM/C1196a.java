package bm;

import G.C0081bn;
import G.C0088bu;
import G.R;
import bH.C;
import bj.C1174a;
import bt.InterfaceC1284L;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: bm.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bm/a.class */
public class C1196a implements InterfaceC1284L {
    @Override // bt.InterfaceC1284L
    public boolean a(C0088bu c0088bu) {
        return c0088bu instanceof C0081bn;
    }

    @Override // bt.InterfaceC1284L
    public JPanel a(R r2, C0088bu c0088bu) {
        try {
            return new C1174a(r2, (C0081bn) c0088bu);
        } catch (V.a e2) {
            C.a("Unable to show FTP File Browser.", e2, this);
            JPanel jPanel = new JPanel();
            jPanel.add(new JLabel("Error"));
            return jPanel;
        }
    }
}
