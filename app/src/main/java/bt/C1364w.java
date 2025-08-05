package bt;

import G.C0050aj;
import G.C0072be;
import G.C0076bi;
import G.C0079bl;
import G.C0088bu;
import java.awt.Toolkit;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/* renamed from: bt.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/w.class */
public class C1364w {

    /* renamed from: a, reason: collision with root package name */
    String[] f9135a = {"resources/wrench.png", "resources/tools.gif", "resources/hammer.gif"};

    /* renamed from: b, reason: collision with root package name */
    int f9136b = 0;

    public Icon a(G.aA aAVar) {
        G.R rC = G.T.a().c();
        return a(rC.e().c(aAVar.d()), rC.e().b(aAVar.d()), aAVar);
    }

    public Icon a(C0088bu c0088bu, C0050aj c0050aj, G.aA aAVar) {
        return (aAVar == null || !aAVar.b()) ? ((c0088bu instanceof C0072be) || (c0088bu != null && c0088bu.S())) ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/table.png"))) : ((c0088bu instanceof C0079bl) || (c0088bu != null && c0088bu.T())) ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/curve.png"))) : c0050aj instanceof C0050aj ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/help16.gif"))) : c0088bu instanceof C0076bi ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/table3d.png"))) : new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/settings.gif"))) : new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/folder16.png")));
    }
}
