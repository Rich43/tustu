package aP;

import bH.C1011s;
import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;
import r.C1807j;
import s.C1818g;

/* renamed from: aP.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/q.class */
class C0461q extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0338f f3834a;

    C0461q(C0338f c0338f) {
        this.f3834a = c0338f;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        File file = null;
        try {
            try {
                this.f3834a.e(C1818g.b("Preparing Debug Package"));
                file = new File(C1807j.A(), "DebugPackage");
                C1011s.b(file);
                if (!file.exists()) {
                    File file2 = new File(file, "tmp");
                    file2.mkdirs();
                    file2.delete();
                }
                if (aE.a.A() != null) {
                    C0462r c0462r = new C0462r(this);
                    File file3 = new File(aE.a.A().t());
                    try {
                        C1011s.a(file3, new File(file, file3.getName()), c0462r, true);
                    } catch (V.a e2) {
                        Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                    File fileQ = C1807j.q();
                    if (fileQ.exists()) {
                        try {
                            C1011s.a(fileQ, new File(file, fileQ.getName()), c0462r, true);
                        } catch (V.a e3) {
                            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                        }
                    }
                }
                if (C1798a.a().r() != null) {
                    bH.C.d("Making copy of AppDebug for packaging.");
                    try {
                        C1011s.a(C1798a.a().r(), new File(file, C1798a.a().r().getName()));
                    } catch (V.a e4) {
                        Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    }
                }
                File file4 = new File(System.getProperty("user.home") + File.separator + "Desktop");
                boolean z2 = true;
                if (!file4.exists()) {
                    file4 = new File(System.getProperty("user.home"));
                    z2 = false;
                }
                File file5 = new File(file4, C1798a.f13268b + "DebugPackage.zip");
                C0463s c0463s = new C0463s(this);
                this.f3834a.f(C1818g.b("Building Debug Package"));
                bH.ad.a(file, file5, (FileFilter) null, c0463s);
                String strB = C1818g.b("A Debug Package named " + file5.getName() + " has been created.");
                com.efiAnalytics.ui.bV.d(((z2 ? strB + "\n\n" + C1818g.b("You will find it on your desktop.") : strB + "\n\n" + C1818g.b("You will find it in your home folder.")) + "\n\n" + C1818g.b("Please email this Debug package and any other information about your problem to:")) + "\nsupport@efianalytics.com", cZ.a().c());
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file5.getParentFile());
                }
                if (file != null) {
                    C1011s.b(file);
                }
                this.f3834a.l();
            } catch (IOException e5) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                if (file != null) {
                    C1011s.b(file);
                }
                this.f3834a.l();
            }
        } catch (Throwable th) {
            if (file != null) {
                C1011s.b(file);
            }
            this.f3834a.l();
            throw th;
        }
    }
}
