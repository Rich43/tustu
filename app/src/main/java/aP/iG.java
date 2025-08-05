package aP;

import G.C0088bu;
import W.C0172ab;
import bi.C1166a;
import bi.C1172g;
import bj.C1174a;
import bk.C1176a;
import bp.C1217a;
import bt.C1345d;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JPanel;
import n.C1762b;
import r.C1798a;
import r.C1806i;
import r.C1807j;
import s.C1818g;
import sun.java2d.marlin.MarlinConst;

/* loaded from: TunerStudioMS.jar:aP/iG.class */
public class iG {

    /* renamed from: a, reason: collision with root package name */
    private static iG f3645a = null;

    private iG() {
    }

    public static iG a() {
        if (f3645a == null) {
            f3645a = new iG();
        }
        return f3645a;
    }

    public boolean a(Frame frame, G.R r2, String str, String str2) throws NumberFormatException {
        if (str.equals("std_constants")) {
            com.efiAnalytics.tuningwidgets.panels.az azVar = new com.efiAnalytics.tuningwidgets.panels.az(r2, Integer.parseInt(str2));
            a(azVar, r2, frame, "Standard Constants - Page " + str2, azVar);
            return true;
        }
        if (str.equals("std_injection")) {
            Integer.parseInt(str2);
            com.efiAnalytics.tuningwidgets.panels.aF aFVar = new com.efiAnalytics.tuningwidgets.panels.aF(r2);
            a(aFVar, r2, frame, "Standard Injection", aFVar);
            return true;
        }
        if (str.equals("std_realtime")) {
            com.efiAnalytics.tunerStudio.panels.H h2 = new com.efiAnalytics.tunerStudio.panels.H(r2);
            h2.a(frame, "Real-Time Display", h2);
            return true;
        }
        if (str.equals("std_warmup")) {
            C0338f.a().a(r2, new K.i(r2, Integer.parseInt(str2)), frame);
            return true;
        }
        if (str.equals("std_accel")) {
            C0338f.a().a(r2, new K.a(r2, Integer.parseInt(str2), C1806i.a().a("0532fewkjfewpoijrew98")), frame);
            return true;
        }
        if (str.equals("std_ms2gentherm")) {
            a(r2, frame);
            C0338f.a().a(r2, (G.aX) r2.e().c("std_ms2gentherm"), frame);
            return true;
        }
        if (str.equals("std_ms2geno2")) {
            a(r2, frame);
            C0338f.a().a(r2, (G.aX) r2.e().c("std_ms2geno2"), frame);
            return true;
        }
        if (str.equals("std_port_edit")) {
            b(r2, frame);
            C0088bu c0088buC = r2.e().c("std_port_edit");
            c0088buC.g(true);
            C0338f.a().a(r2, (G.aS) c0088buC, frame);
            return true;
        }
        if (str.equals("std_trigwiz")) {
            C1217a c1217a = new C1217a(r2);
            a(c1217a, r2, frame, "Trigger Wizard", c1217a);
            return true;
        }
        if (str.equals("std_ms3Rtc")) {
            C1176a c1176a = new C1176a(r2);
            com.efiAnalytics.ui.bV.a(c1176a, frame, "MS3 Real-Time Clock", c1176a);
            return true;
        }
        if (str.equals("std_ms3SdConsole")) {
            bk.d dVar = new bk.d(r2);
            JDialog jDialogB = com.efiAnalytics.ui.bV.b(dVar, frame, "MS3 SD Console", dVar);
            jDialogB.pack();
            com.efiAnalytics.ui.bV.a((Window) com.efiAnalytics.ui.bV.a((Component) frame), (Component) jDialogB);
            jDialogB.addWindowListener(new iH(this, dVar));
            jDialogB.setModal(true);
            jDialogB.setVisible(true);
            return true;
        }
        if (str.equals("std_ftpSdBrowser")) {
            C1174a c1174a = new C1174a(r2, null);
            JDialog jDialogB2 = com.efiAnalytics.ui.bV.b(c1174a, frame, "SD Log Browser", c1174a);
            jDialogB2.pack();
            com.efiAnalytics.ui.bV.a((Window) com.efiAnalytics.ui.bV.a((Component) frame), (Component) jDialogB2);
            jDialogB2.addWindowListener(new iI(this, c1174a));
            jDialogB2.setVisible(true);
            return true;
        }
        if (!str.equals("std_replay_upload")) {
            if (!str.equals("std_bootstrap")) {
                return false;
            }
            com.efiAnalytics.ui.bV.a(new C1172g(r2), frame, "Bootstrap", (InterfaceC1565bc) null);
            return true;
        }
        if (r2.R()) {
            new C1166a(r2).a(frame, "Replay Upload");
            return true;
        }
        if (System.currentTimeMillis() - H.a.a() < MarlinConst.statDump) {
            com.efiAnalytics.ui.bV.d((C1818g.b("BigComm Pro must be online!") + "\n") + C1818g.b("Please turn the Key to the on position and make sure BigComm Pro is online."), frame);
            return true;
        }
        com.efiAnalytics.ui.bV.d((C1818g.b("BigComm Pro must be online!") + "\n") + C1818g.b("Please connect to your BigStuff 3 with the Key On."), frame);
        return true;
    }

    public void a(C1345d c1345d, G.R r2, Window window, String str, com.efiAnalytics.ui.aO aOVar) {
        com.efiAnalytics.ui.dF dFVar = new com.efiAnalytics.ui.dF(window, C1818g.b(str), aOVar);
        dFVar.add(BorderLayout.CENTER, c1345d);
        C1762b c1762b = new C1762b();
        c1762b.a(aOVar);
        JPanel jPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(2);
        jPanel.setLayout(flowLayout);
        jPanel.add(c1762b);
        dFVar.add("South", jPanel);
        r2.p().a(new iJ(this, c1762b));
        c1345d.a(dFVar);
        dFVar.pack();
        dFVar.setResizable(false);
        com.efiAnalytics.ui.bV.a(window, (Component) dFVar);
        dFVar.setVisible(true);
    }

    public void a(G.R r2, Frame frame) {
        String str = C1807j.f13475k;
        W.J j2 = new W.J();
        try {
            j2.a(new File(str));
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Error reading  " + str + "\n" + e2.getMessage(), frame);
        }
        try {
            if (!(C1798a.a().c(C1798a.f13387bq, C1798a.f13388br) && C1798a.a().c(C1798a.f13389bs, C1798a.f13390bt))) {
                new C0172ab().a(r2, r2.e(), j2);
            }
        } catch (V.g e3) {
            com.efiAnalytics.ui.bV.d(e3.getMessage(), frame);
        }
    }

    public void b(G.R r2, Frame frame) {
        String str = C1807j.f13476l;
        W.J j2 = new W.J();
        try {
            j2.a(new File(str));
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Error reading  " + str + "\n" + e2.getMessage(), frame);
        }
        try {
            if (!(C1798a.a().c(C1798a.f13387bq, C1798a.f13388br) && C1798a.a().c(C1798a.f13389bs, C1798a.f13390bt))) {
                new C0172ab().b(r2, r2.e(), j2);
            }
        } catch (V.g e3) {
            com.efiAnalytics.ui.bV.d(e3.getMessage(), frame);
        }
    }
}
