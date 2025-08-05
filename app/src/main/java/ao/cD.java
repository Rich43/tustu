package ao;

import bH.C1011s;
import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ao/cD.class */
class cD extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5458a;

    cD(bP bPVar) {
        this.f5458a = bPVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        String strK = h.i.k();
        String str = h.i.f12262i;
        String str2 = strK + "LogFile.txt";
        File file = null;
        try {
            try {
                file = new File(h.h.a(), strK + "SupportPkg" + System.currentTimeMillis());
                C1011s.b(file);
                file.mkdir();
                File file2 = new File(h.h.a(), str);
                if (file2.exists() && file2.isFile()) {
                    bH.C.d("Making copy of properties file for Support Package.");
                    try {
                        C1011s.a(file2, new File(file, str));
                    } catch (V.a e2) {
                        Logger.getLogger(C0636b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                File file3 = new File(h.h.a(), str2);
                if (file3.exists() && file3.isFile()) {
                    bH.C.d("Making copy of log file for Support Package.");
                    try {
                        C1011s.a(file3, new File(file, file3.getName()));
                    } catch (V.a e3) {
                        Logger.getLogger(C0636b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                }
                if (this.f5458a.f5374t != null && this.f5458a.f5374t.exists() && this.f5458a.f5374t.isFile()) {
                    bH.C.d("Making copy of last opened log file " + this.f5458a.f5374t.getName() + " log file for Support Package.");
                    try {
                        C1011s.a(this.f5458a.f5374t, new File(file, this.f5458a.f5374t.getName()));
                    } catch (V.a e4) {
                        Logger.getLogger(C0636b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    }
                }
                File file4 = new File(System.getProperty("user.home") + File.separator + "Desktop");
                boolean z2 = true;
                if (!file4.exists()) {
                    file4 = new File(System.getProperty("user.home"));
                    z2 = false;
                }
                File file5 = new File(file4, strK + "DebugPackage.zip");
                if (file5.exists()) {
                    file5.delete();
                }
                bH.ad.a(file, file5, (FileFilter) null);
                com.efiAnalytics.ui.bV.d("A Debug Package named " + file5.getName() + " has been created.\n\nYou will find it " + (z2 ? "on your desktop" : "in your home folder") + ".\n\nPlease email this Debug package and any other information about your problem to:\nsupport@efianalytics.com", C0645bi.a().b());
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file5.getParentFile());
                }
                if (file != null) {
                    C1011s.b(file);
                }
            } catch (IOException e5) {
                Logger.getLogger(C0636b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                if (file != null) {
                    C1011s.b(file);
                }
            }
        } catch (Throwable th) {
            if (file != null) {
                C1011s.b(file);
            }
            throw th;
        }
    }
}
