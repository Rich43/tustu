package aP;

import bH.C1005m;
import java.awt.HeadlessException;
import java.awt.Window;
import javax.swing.JOptionPane;
import r.C1798a;
import r.C1806i;

/* renamed from: aP.iu, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/iu.class */
class C0439iu implements G.aG {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0438it f3754a;

    C0439iu(C0438it c0438it) {
        this.f3754a = c0438it;
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) throws HeadlessException {
        int iShowOptionDialog;
        if (bSVar.c().equals(this.f3754a.f3752a)) {
            return true;
        }
        String str2 = "Cannot connect to this ECU using your current Registration!\nYour current Registration is for working with BigStuff3 Serial #" + this.f3754a.f3752a + "\n\nYou can work offline or temporarily disable your registration to connect in Lite mode.\nNote: You can re-enable your registration under the menu:\nOptions --> Enable Registration\n\nIf you disable the Registration, the application must restart.";
        String strB = C1806i.a().b();
        if (!C1005m.b() || strB == null || strB.isEmpty()) {
            String[] strArr = {"Work Offline", "Disable Registration"};
            iShowOptionDialog = JOptionPane.showOptionDialog(cZ.a().c(), str2, "Licensing Restriction", 0, 0, null, strArr, strArr[0]);
        } else {
            String[] strArr2 = {"Work Offline", "Disabled Registration", "Upgrade Registration"};
            iShowOptionDialog = JOptionPane.showOptionDialog(cZ.a().c(), str2, "Licensing Restriction", 1, 0, null, strArr2, strArr2[0]);
        }
        if (iShowOptionDialog == 1) {
            C1798a.a().b(C1798a.f13302J, Boolean.toString(true));
            C0338f.a().d((Window) cZ.a().c());
            return false;
        }
        if (iShowOptionDialog != 2) {
            return false;
        }
        com.efiAnalytics.ui.aN.a(strB);
        return false;
    }

    @Override // G.aG
    public void a(String str) {
    }
}
