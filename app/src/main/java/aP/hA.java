package aP;

import c.C1382a;
import java.awt.Frame;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/hA.class */
public class hA implements G.S {

    /* renamed from: a, reason: collision with root package name */
    hB f3506a = new hB(this);

    /* renamed from: b, reason: collision with root package name */
    int f3507b = 0;

    /* renamed from: c, reason: collision with root package name */
    final int f3508c = 2;

    /* renamed from: d, reason: collision with root package name */
    String f3509d = "";

    @Override // G.S
    public void a(G.R r2) {
        r2.C().a(this.f3506a);
        if (r2 != null) {
            this.f3509d = r2.i();
        }
        this.f3507b = 0;
    }

    @Override // G.S
    public void b(G.R r2) {
        if (r2 == null || r2.C() == null) {
            return;
        }
        r2.C().a(this.f3506a);
    }

    @Override // G.S
    public void c(G.R r2) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) throws HeadlessException {
        int i2 = this.f3507b;
        this.f3507b = i2 + 1;
        if (i2 == 2 && C1798a.a().c(C1798a.f13303K, C1798a.f13304L)) {
            String strA = C1382a.a(this.f3509d, C1798a.f13272f);
            String str2 = strA + " " + C1818g.b("not found on") + " " + str + "\n\n- If your " + strA + " is not connected and the above settings are correct,\n select Continue Scanning. Your " + strA + " will come online when powered up.\n\n- If your " + strA + " is Connected, Powered and Key On Click \n   \"Search for " + strA + "\" to detect the correct Communication Settings.\n\n- If you do not want to see this message and always scan until the " + strA + "\n   is available, click \"Continue Scanning Don't Ask Again\"\n\n" + C1818g.b("What would you like to do?") + "\n ";
            String[] strArr = {"<html><center>Continue Scanning<br>Current Settings</center>", "<html><center>Search for<br>" + strA + "</center>", "<html><center>Continue Scanning<br>Don't Ask Again</center>"};
            JFrame jFrameC = cZ.a().c();
            int iShowOptionDialog = JOptionPane.showOptionDialog(jFrameC, str2, "Controller not Found", 1, 1, null, strArr, strArr[0]);
            if (iShowOptionDialog == 1) {
                C0338f.a().e((Frame) jFrameC).l();
            } else if (iShowOptionDialog == 2) {
                C1798a.a().b(C1798a.f13303K, Boolean.toString(false));
            }
        }
    }
}
