package ao;

import ap.C0829a;
import ap.C0830b;
import ap.C0831c;
import bH.C1018z;
import bv.C1369a;
import com.efiAnalytics.ui.BinTableView;
import h.C1737b;
import i.C1746f;
import javax.swing.SwingUtilities;

/* renamed from: ao.fg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fg.class */
public class C0750fg {
    public void a() {
        int i2 = 1000;
        for (int i3 = 0; i3 < 10000 && i2 < 1800; i3++) {
            i2++;
        }
        C1018z.i().f7069c = i2 + 166;
        C1018z.i().a("TunerStudio", "MS", "http://www.efianalytics.com/TunerStudio/download/");
        C1018z.i().a("TunerStudio", "MS Ultra", "http://www.efianalytics.com/TunerStudio/download/");
        C1018z.i().a("TunerStudio", "MS Dev", "http://www.efianalytics.com/TunerStudio/download/");
        C1018z.i().a("MegaLogViewer", "MS", "https://www.efianalytics.com/MegaLogViewer/download/");
        C1018z.i().a("MegaLogViewer", "HD", "https://www.efianalytics.com/MegaLogViewerHD/download/");
        C1018z.i().a("MegaLogViewer", "BigStuff3", "http://www.bigcommpro.com/downloads");
        C1018z.i().a("Shadow Dash MS", "", "http://www.tunerstudio.com/index.php/downloads");
        C1018z.i().a("Big Dash", "", "http://www.bigcommpro.com/software/bigdash");
        C1018z.i().a("Big Replay Upload", "", "http://www.bigcommpro.com/software/bigreplay");
        C1018z.i().a("BigComm", "Pro", "http://bigcommpro.com/software/bigcomm-pro");
        C1018z.i().a("BigComm", "Pro Single", "http://bigcommpro.com/software/bigcomm-pro");
        W.W.a().a(new C0751fh(this));
        BinTableView.f(C1737b.a().a("tablePremium"));
        C1369a c1369a = new C1369a();
        d.g.a().a(c1369a.a(), c1369a);
        if (C1737b.a().a("fa-9fdspoijoijnfdz09jfdsa098j")) {
            String strD = at.c.a().d();
            if (!strD.equals("")) {
                at.c.a().a(strD, true);
            }
            h.i.a(new C0752fi(this));
        }
        c();
        SwingUtilities.invokeLater(new RunnableC0753fj(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        C1746f.a().a(new C0829a());
        if (C1737b.a().a("okdsas32lkg09832jnegm7")) {
            C1746f.a().a(new C0831c());
        }
        C1746f.a().a(new C0830b());
        C1746f.a().b();
        com.efiAnalytics.ui.bV.g();
    }

    private void c() {
        Z.b.a().a(new dZ());
        Z.b.a().b();
    }
}
