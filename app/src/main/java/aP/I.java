package aP;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import r.C1807j;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/I.class */
class I extends Thread {

    /* renamed from: a, reason: collision with root package name */
    File f2753a;

    /* renamed from: b, reason: collision with root package name */
    String f2754b;

    /* renamed from: c, reason: collision with root package name */
    bH.L f2755c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C0338f f2756d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    I(C0338f c0338f, File file, String str, bH.L l2) {
        super("UnarchiveProcessor");
        this.f2756d = c0338f;
        setDaemon(true);
        this.f2753a = file;
        this.f2754b = str;
        this.f2755c = l2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        File file = new File(C1807j.u(), this.f2754b);
        File file2 = new File(((Object) this.f2753a) + File.separator + aE.a.f2350j, "dummy");
        file2.mkdirs();
        file2.delete();
        File file3 = new File(((Object) this.f2753a) + File.separator + aE.a.f2351k, "dummy");
        file3.mkdirs();
        file3.delete();
        File file4 = new File(((Object) this.f2753a) + File.separator + aE.a.f2352l, "dummy");
        file4.mkdirs();
        file4.delete();
        try {
            bH.ad.a(this.f2753a, file, (String) null, this.f2755c);
            if (com.efiAnalytics.ui.bV.a(C1818g.b("Project has been imported.\nWould you like to open it now?"), (Component) cZ.a().c(), true)) {
                this.f2756d.a((Window) cZ.a().c(), file.getAbsolutePath());
            }
        } catch (ZipException e2) {
            C0404hl.a().b("Error! Not a valid Project file.");
        } catch (Exception e3) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            C0404hl.a().b("Archive import Failed! " + this.f2753a.getName() + "\nMessage: " + e3.getMessage());
        }
    }
}
