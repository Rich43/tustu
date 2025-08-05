package aM;

import bH.C;
import bH.C1005m;
import bH.C1011s;
import bH.I;
import bH.aa;
import com.efiAnalytics.ui.C1616d;
import com.efiAnalytics.ui.aN;
import com.efiAnalytics.ui.bV;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:aM/a.class */
public class a {

    /* renamed from: d, reason: collision with root package name */
    aa f2618d;

    /* renamed from: f, reason: collision with root package name */
    Window f2620f;

    /* renamed from: a, reason: collision with root package name */
    File f2614a = null;

    /* renamed from: b, reason: collision with root package name */
    d f2615b = null;

    /* renamed from: c, reason: collision with root package name */
    C1616d f2616c = null;

    /* renamed from: e, reason: collision with root package name */
    e f2619e = new e(this);

    /* renamed from: g, reason: collision with root package name */
    private String f2617g = a("Downloading TeamViewer for Desktop sharing") + "\n" + a("Please Wait");

    public a(Window window, aa aaVar) {
        this.f2618d = aaVar;
        this.f2620f = window;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) {
        if (this.f2618d != null) {
            str = this.f2618d.a(str);
        }
        return str;
    }

    public void a(File file) {
        if (!I.a() && !I.b()) {
            aN.a("http://connect.teamviewer.com/v12");
            return;
        }
        this.f2614a = file;
        this.f2615b = new d(this);
        this.f2615b.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        File file;
        Object obj;
        String str;
        String strC;
        if (I.a()) {
            file = new File(this.f2614a, "TeamViewerQS.exe");
            obj = "692dff991182dcd4a29f47f987ae1c7e";
            str = "http://www.tunerstudio.com/teamviewer/TeamViewerQS.exe";
        } else if (!I.b()) {
            bV.d("TeamViewer download not supported on this OS. Install version 12 manually.", this.f2620f);
            return;
        } else {
            file = new File(this.f2614a, "TeamViewerQS.dmg");
            obj = "4dacc7e0ed5ea9069d22e399c6379b69";
            str = "http://www.tunerstudio.com/teamviewer/TeamViewerQS.dmg";
        }
        try {
            if (file.exists()) {
                strC = C1011s.c(file);
                C.c("checksum: " + strC);
            } else {
                strC = "";
            }
        } catch (IOException e2) {
            strC = "";
            file.delete();
        }
        if (!strC.equals(obj)) {
            b();
            try {
                try {
                    C1005m.a(str, file.getAbsolutePath(), this.f2619e);
                    c();
                } catch (IOException e3) {
                    Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    bV.d("Failed to download TeamViewer.\n" + e3.getLocalizedMessage(), this.f2620f);
                    c();
                }
            } catch (Throwable th) {
                c();
                throw th;
            }
        }
        if (!file.exists()) {
            C.a("Downloaded teamViewer File does not exist.");
            return;
        }
        this.f2617g = a("Starting TeamViewer, please wait....");
        b();
        try {
            try {
                if (I.b()) {
                    Runtime.getRuntime().exec(new String[]{"/usr/bin/open", file.getAbsolutePath()}).waitFor();
                } else {
                    Runtime.getRuntime().exec(file.getAbsolutePath()).waitFor();
                }
                c();
            } catch (IOException e4) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                bV.d("Failed to launch TeamViewer.\n" + e4.getLocalizedMessage(), this.f2620f);
                c();
            } catch (InterruptedException e5) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                c();
            }
        } catch (Throwable th2) {
            c();
            throw th2;
        }
    }

    private void b() {
        SwingUtilities.invokeLater(new b(this));
    }

    private void c() {
        try {
            SwingUtilities.invokeAndWait(new c(this));
        } catch (Exception e2) {
            Logger.getLogger(a.class.getName()).log(Level.WARNING, "Error closing wait dialog.", (Throwable) e2);
        }
    }
}
