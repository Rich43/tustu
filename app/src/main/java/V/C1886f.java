package v;

import W.C0197w;
import W.R;
import bH.W;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import r.C1798a;
import r.C1807j;
import s.C1818g;

/* renamed from: v.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:v/f.class */
public class C1886f {
    public static R a(Component component, String str) throws HeadlessException {
        int iShowOptionDialog;
        R rA = C0197w.a(str);
        if (!rA.a()) {
            return null;
        }
        if (C1798a.a().c(C1798a.f13322ad, Boolean.FALSE.booleanValue())) {
            String str2 = "Your installation of " + C1798a.f13268b + " does not have a Ecu Definition file to support the firmware found.\n\n\nRequired Serial Signature:\n" + str + "\n\nHowever this file is available on EFI Analytics servers.\nWould you like " + C1798a.f13268b + " to download the file for you?\nFile Size: " + W.a(rA.b()) + "\n";
            String[] strArr = {C1818g.b("Yes"), C1818g.b("No"), C1818g.b("Always Yes")};
            iShowOptionDialog = JOptionPane.showOptionDialog(component, str2, "Internet Download", -1, 3, null, strArr, strArr[0]);
            if (iShowOptionDialog == 2) {
                C1798a.a().b(C1798a.f13322ad, Boolean.toString(true));
                iShowOptionDialog = 0;
            }
        } else {
            iShowOptionDialog = 0;
        }
        if (iShowOptionDialog != 0) {
            return null;
        }
        JDialog jDialogA = bV.a(a(), "Downloading Ecu Definition file for " + str);
        try {
            try {
                R rA2 = C0197w.a(str, C1807j.c());
                if (rA2 != null) {
                    return rA2;
                }
                throw new V.a("Error downloading ECU Definition");
            } catch (V.a e2) {
                Logger.getLogger(C1886f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                throw new V.a(e2.getMessage());
            } catch (Exception e3) {
                Logger.getLogger(C1886f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                throw new V.a("Unexpected Error tring to download Definition File.");
            }
        } finally {
            jDialogA.dispose();
        }
    }

    private static Window a() {
        Window[] windows = JWindow.getWindows();
        return windows.length > 0 ? windows[0] : null;
    }
}
