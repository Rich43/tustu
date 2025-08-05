package aP;

import bH.C1011s;
import bH.C1018z;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import r.C1798a;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aP/iN.class */
public class iN {

    /* renamed from: b, reason: collision with root package name */
    static String f3669b = "startScreenVersions.";

    /* renamed from: a, reason: collision with root package name */
    long f3668a = 604800000;

    /* renamed from: c, reason: collision with root package name */
    String f3670c = "1.5";

    /* renamed from: d, reason: collision with root package name */
    iP f3671d = null;

    public void a(File file, String str, iQ iQVar) {
        String str2 = C1798a.f13362aR + "." + str;
        String str3 = bH.W.b(C1798a.f13269c, C1806i.f13441c, "").trim() + str;
        String str4 = f3669b + str;
        String strC = C1798a.a().c(str4, this.f3670c);
        long jCurrentTimeMillis = System.currentTimeMillis() - this.f3668a;
        long jA = C1798a.a().a(str2, -1L);
        boolean z2 = C1798a.a().c(C1798a.f13361aQ, true) || !C1018z.i().a(C1798a.a().c(C1798a.cF, ""));
        if (jA >= jCurrentTimeMillis || !z2) {
            System.out.println(str3 + ": No check, last update check=" + new Date(jA).toString());
            return;
        }
        try {
            bW.a aVar = new bW.a();
            try {
                bW.d dVarA = aVar.a(C1798a.a().c(C1798a.f13358aN, ""), C1798a.f13268b, strC, C1798a.a().c(C1798a.f13359aO, ""), C1798a.a().c(C1798a.f13364aT, ""), C1798a.a().c(C1798a.cF, ""), str3, C1798a.a().c(C1798a.cO, ""));
                if (dVarA == null) {
                    bH.C.d("Start Screen update Failed.");
                    return;
                }
                if (dVarA.a() == 0) {
                    try {
                        C1798a.a().b(str2, "" + new Date().getTime());
                        C1798a.a().e();
                        return;
                    } catch (V.a e2) {
                        Logger.getLogger(iN.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                } else if (dVarA.a() == 2) {
                    try {
                        C1011s.b(file);
                        File file2 = new File(file, "tmp");
                        file2.mkdirs();
                        file2.delete();
                        aVar.a(file, dVarA);
                        String strB = bH.W.b(bH.W.b(dVarA.b(), C1798a.f13268b + " Update available! Would you like Auto Update to upgrade to version ", ""), " now?", "");
                        a(file);
                        C1798a.a().b(str4, strB.trim());
                        bH.C.d("Updated Start Screen: " + str3);
                        if (iQVar != null) {
                            iQVar.a();
                        }
                    } catch (IOException e3) {
                        bH.C.a("Failed to update Start Screen: " + str + ", Error: " + e3.getMessage());
                    }
                } else if (dVarA.a() == 1) {
                    C0404hl.a().a(dVarA.b());
                } else if (dVarA.a() != 8 && dVarA.a() == 4) {
                }
                C1798a.a().b(str2, "" + new Date().getTime());
                C1798a.a().e();
            } catch (IOException e4) {
                System.out.println("Unable to read from start screen update server, connection to server unavailable");
            }
        } catch (V.a e5) {
            Logger.getLogger(iN.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
        }
    }

    private void a(File file) {
        String[] list = file.list();
        for (int i2 = 0; i2 < list.length; i2++) {
            if (list[i2].toLowerCase().endsWith(".zip")) {
                try {
                    C0404hl.a().a("Updating Start Screen.");
                    if (bH.ad.a(new File(file, list[i2]).getAbsolutePath(), file.getAbsolutePath(), (String) null).equals(bH.ad.f7040a)) {
                        new File(file, list[i2]).delete();
                    }
                } catch (ZipException e2) {
                    e2.printStackTrace();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public void b(File file, String str, iQ iQVar) {
        b().a(new iO(this, file, str, iQVar));
    }

    private iP b() {
        if (this.f3671d == null || !this.f3671d.isAlive()) {
            this.f3671d = new iP(this);
        }
        return this.f3671d;
    }

    public void a() {
        C1798a.a().b(f3669b + "ad", this.f3670c);
        C1798a.a().b(f3669b + "main", this.f3670c);
        C1798a.a().b(f3669b + "register", this.f3670c);
    }
}
