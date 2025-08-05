package aP;

import bH.C1005m;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;

/* renamed from: aP.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/m.class */
class C0457m extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ File f3823a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ String f3824b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ InterfaceC0253bv f3825c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ JDialog f3826d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C0338f f3827e;

    C0457m(C0338f c0338f, File file, String str, InterfaceC0253bv interfaceC0253bv, JDialog jDialog) {
        this.f3827e = c0338f;
        this.f3823a = file;
        this.f3824b = str;
        this.f3825c = interfaceC0253bv;
        this.f3826d = jDialog;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f3823a.mkdirs();
            this.f3823a.delete();
            C1005m.a(this.f3824b, this.f3823a.getAbsolutePath());
            if (this.f3825c != null) {
                try {
                    this.f3825c.a();
                } catch (Exception e2) {
                }
            }
        } catch (Exception e3) {
            Logger.getLogger(C0224at.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            com.efiAnalytics.ui.bV.d("Unexpected Error tring to download File.", cZ.a().c());
        } finally {
            this.f3826d.dispose();
        }
    }
}
