package aP;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/C.class */
class C extends Thread {

    /* renamed from: a, reason: collision with root package name */
    File f2734a;

    /* renamed from: b, reason: collision with root package name */
    File f2735b;

    /* renamed from: c, reason: collision with root package name */
    FileFilter f2736c;

    /* renamed from: d, reason: collision with root package name */
    bH.L f2737d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C0338f f2738e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C(C0338f c0338f, File file, File file2, FileFilter fileFilter, bH.L l2) {
        super("ArchiveProcessor");
        this.f2738e = c0338f;
        setDaemon(true);
        this.f2734a = file;
        this.f2735b = file2;
        this.f2736c = fileFilter;
        this.f2737d = l2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            bH.ad.a(this.f2734a, this.f2735b, this.f2736c, this.f2737d);
            C1798a.a().b(C1798a.f13287u, this.f2735b.getParentFile().getAbsolutePath());
            C0404hl.a().d("Archive Complete: " + this.f2735b.getName());
        } catch (IOException e2) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            String message = e2.getMessage();
            if (message != null) {
                message = bH.W.b(bH.W.b(message, "ZIP", "Project"), "zip", "Project");
            }
            C0404hl.a().b("Archive Failed! " + this.f2735b.getName() + "\nMessage: " + message);
        }
    }
}
