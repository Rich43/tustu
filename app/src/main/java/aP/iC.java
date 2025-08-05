package aP;

import W.C0197w;
import W.C0200z;
import bH.C1005m;
import bH.C1011s;
import c.C1382a;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import r.C1798a;
import r.C1807j;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/iC.class */
public class iC implements G.cU {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f3637a = false;

    @Override // G.cU
    public boolean a(String str, String str2, G.bS bSVar) throws HeadlessException {
        aE.a aVarA = aE.a.A();
        G.R rC = G.T.a().c(str);
        if (rC != null) {
            rC.O().m("");
        }
        if (bSVar.d()) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Successfully connected to your " + C1382a.a(str2, C1798a.f13272f) + ", but no firmware is loaded or the bootload jumper is on.") + "\n\n" + C1818g.b("This must be corrected before connecting."), cZ.a().c());
            return false;
        }
        if (f3637a) {
            a(aVarA, str, str2, bSVar);
            C0404hl.a().d("Updating config file for " + str);
            return false;
        }
        JFrame jFrameC = cZ.a().c();
        C0440iv c0440iv = new C0440iv(jFrameC, aVarA, str, bSVar, str2);
        com.efiAnalytics.ui.bV.a((Window) jFrameC, (Component) c0440iv);
        c0440iv.setVisible(true);
        return c0440iv.b();
    }

    public static File a(G.bS bSVar) throws HeadlessException, FileNotFoundException {
        int iShowOptionDialog;
        if (!C1005m.b()) {
            if (C0404hl.a().a(a(C1798a.f13268b + " does not currently have access to the Internet.") + "\n" + a("To download the ECU Definition file required for this firmware, " + C1798a.f13268b + " requires Internet access.") + "\n\n" + a("Please enable Internet access with " + C1798a.f13268b + " open.") + "\n" + a("You do not need to be connected to your controller, " + C1798a.f13268b + " knows what file it needs.") + "\n" + a("After closing this dialog the file will automatically download it once Internet is detected.") + "\n" + a("You will be notified when download is complete"), new String[]{a("Download when Internet available"), a("Browse for file")}) != 0) {
                throw new FileNotFoundException("The correct ECU Definition not available.");
            }
            n.k.a().a(bSVar);
            return null;
        }
        W.R rA = C0197w.a(bSVar.b());
        if (!rA.a()) {
            throw new FileNotFoundException("The correct ECU Definition not available.");
        }
        if (f3637a || C1798a.a().c(C1798a.f13322ad, Boolean.FALSE.booleanValue())) {
            iShowOptionDialog = 0;
        } else {
            String str = "Your installation of " + C1798a.f13268b + " does not have a Ecu Definition file to support the firmware found.\n\n\nRequired Serial Signature:\n" + ((Object) bSVar) + "\n\nHowever this file is available on EFI Analytics servers.\nWould you like " + C1798a.f13268b + " to download the file for you?\nFile Size: " + bH.W.a(rA.b()) + "\n";
            String[] strArr = {C1818g.b("Yes"), C1818g.b("No"), C1818g.b("Always Yes")};
            iShowOptionDialog = JOptionPane.showOptionDialog(cZ.a().c(), str, "Internet Download", -1, 3, null, strArr, strArr[0]);
            if (iShowOptionDialog == 2) {
                C1798a.a().b(C1798a.f13322ad, Boolean.toString(true));
                iShowOptionDialog = 0;
            }
        }
        if (iShowOptionDialog != 0) {
            return null;
        }
        JDialog jDialogA = com.efiAnalytics.ui.bV.a((Window) cZ.a().c(), "Downloading Ecu Definition file for " + bSVar.c());
        try {
            try {
                W.R rA2 = C0197w.a(bSVar.b(), C1807j.c());
                if (rA2.a()) {
                    File fileC = rA2.c();
                    jDialogA.dispose();
                    return fileC;
                }
                com.efiAnalytics.ui.bV.d(rA2.d(), cZ.a().c());
                jDialogA.dispose();
                return null;
            } catch (V.a e2) {
                com.efiAnalytics.ui.bV.d(e2.getMessage(), cZ.a().c());
                jDialogA.dispose();
                return null;
            }
        } catch (Throwable th) {
            jDialogA.dispose();
            throw th;
        }
    }

    private static String a(String str) {
        return C1818g.b(str);
    }

    public void a(String str, aE.a aVar, File file) {
        if (str.equals(aVar.u())) {
            aVar.l(file.getName().toLowerCase().endsWith(".ecu") ? "mainController.ecu" : "mainController.ini");
            aVar.remove("firmwareDescription");
        }
        File fileF = aVar.f(str);
        if (fileF == null) {
            com.efiAnalytics.ui.bV.d("No target file name for device:" + str, cZ.a().c());
        }
        if (file == null || !file.exists()) {
            com.efiAnalytics.ui.bV.d("The " + C1798a.f13272f + " Configuration file can not be found:\n" + str, cZ.a().c());
            return;
        }
        try {
            bH.C.d("Copying " + file.getName() + " to " + aVar.u() + "'s projectCfg");
            C1011s.a(file, fileF);
        } catch (V.a e2) {
            C0404hl.a().b("Can not copy \n" + file.getAbsolutePath() + "\nTo:\n" + fileF.getAbsolutePath());
        }
        try {
            if (!file.getParentFile().equals(C1807j.c())) {
                bH.C.d("Copying " + file.getName() + " to ecuDef dir for future use.");
                C1011s.a(file, new File(C1807j.c(), C0200z.b(C0200z.a(file))));
            }
        } catch (V.a e3) {
            bH.C.a("Can not copy \n" + file.getAbsolutePath() + "\nTo:\n" + fileF.getAbsolutePath());
        }
        try {
            aVar.b();
        } catch (V.a e4) {
            C0404hl.a().b("Error saving project.");
        }
        String strA = C0200z.a(file);
        if (str.equals(aVar.u())) {
            if (cZ.a().h() != null) {
                cZ.a().h().c(strA);
            } else if (cZ.a().b() != null) {
                cZ.a().b().c(strA);
            }
        }
        C0338f.a().z();
    }

    public boolean a(aE.a aVar, String str, String str2, G.bS bSVar) throws HeadlessException {
        File fileA = null;
        String strA = C1382a.a(str2, C1798a.f13272f);
        try {
            fileA = C1807j.d(bSVar.b());
        } catch (V.a e2) {
            bH.C.a("Signature Mismatch - Error retrieving a file that matches the controller signature:" + ((Object) bSVar));
        }
        if (fileA != null) {
            String str3 = C1818g.b(C1798a.f13268b + " has an ECU Definition (ini) file to support your Controllers firmware.") + "\n\n   " + C1818g.b("Firmware on " + strA) + ": " + bSVar.c() + "\n   " + C1818g.b("Required Serial Signature") + ": " + bSVar.b() + "\n\n" + C1818g.b("Would you like to update your project with " + C1798a.f13268b + "'s correct ECU Definition?") + "\n\n" + C1818g.b("Click Yes to use " + C1798a.f13268b + "'s ECU Definition File.") + "\n" + C1818g.b("Click No to browse to your own ECU Definition File.");
            if (f3637a || C0404hl.a().c(str3)) {
                new iD(this, str, aVar, fileA).start();
                return true;
            }
        } else {
            try {
                fileA = a(bSVar);
                if (fileA != null) {
                    new iD(this, str, aVar, fileA).start();
                    return true;
                }
            } catch (FileNotFoundException e3) {
                bH.C.d("Could not get ECU Definition for signature: " + ((Object) bSVar) + ", Error: " + e3.getLocalizedMessage());
            }
        }
        if (fileA == null) {
            C0404hl.a().b(C1798a.f13268b + " does not have an ECU Definition (ini) file that is\ncompatible with the firmware installed on this Controller.\n\n    Firmware on " + strA + ": " + bSVar.c() + "\n    Required Serial Signature: " + bSVar.b() + "\n\nTo correct this please browse to the ECU Definition (ini) file\nthat was provided with your firmware.\n");
        }
        String strB = com.efiAnalytics.ui.bV.b(cZ.a().c(), "Find ecu definition file", new String[]{"ini", "ecu"}, "", "");
        if (strB == null || strB.equals("")) {
            return false;
        }
        File file = new File(strB);
        if (!file.exists()) {
            return false;
        }
        String strA2 = C0200z.a(strB);
        if ((strA2 == null || !strA2.equals(bSVar.b())) && !C0404hl.a().c("The file:\n" + strB + "\n does not appear correct for the firmware on your " + strA + "!\n\nThe correct ECU Definition (ini) file is required for proper operation\nand to prevent corruption to the settings on your " + strA + "!!!\n\nAre you sure you wish to use this ECU Definition?")) {
            return false;
        }
        try {
            C1011s.a(strB, C1807j.m() + bSVar.b() + ".ini");
        } catch (V.a e4) {
            bH.C.b("Unable to copy ini file to app ecu dir");
        }
        new iD(this, str, aVar, file).start();
        return true;
    }

    public File a() {
        File file;
        String[] strArr = {"ini", "ecu"};
        do {
            String strB = com.efiAnalytics.ui.bV.b(cZ.a().c(), "Find ecu definition file", strArr, "", "");
            file = new File(strB);
            if (strB != null && !strB.equals("")) {
                if (file.exists() && file.length() > 0) {
                    try {
                        C1011s.a(strB, C0200z.b(C0200z.a(strB)));
                    } catch (V.a e2) {
                        bH.C.b("Unable to copy ini file to app ecu dir");
                    }
                    return file;
                }
                com.efiAnalytics.ui.bV.d(((Object) file) + "\nIs not a valid Firmware Definition File.", cZ.a().c());
            }
            if (file == null) {
                return null;
            }
        } while (!file.exists());
        return null;
    }
}
