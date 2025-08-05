package bH;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bH/O.class */
public class O {

    /* renamed from: a, reason: collision with root package name */
    public static String f7012a = "IniUpdate";

    /* renamed from: b, reason: collision with root package name */
    public static String f7013b = "IncUpdate";

    /* renamed from: c, reason: collision with root package name */
    File f7014c;

    /* renamed from: d, reason: collision with root package name */
    String f7015d;

    public O(File file, String str) {
        this.f7014c = file;
        this.f7015d = str;
    }

    public void a() {
        new P(this).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b() {
        bW.a aVar = new bW.a();
        String str = this.f7015d;
        if (!C1005m.a()) {
            return false;
        }
        try {
            bW.e eVar = new bW.e();
            eVar.a(C1798a.a().c(C1798a.f13358aN, ""));
            eVar.b(C1798a.f13268b);
            eVar.c(C1798a.a().c(str, "0"));
            eVar.d(C1798a.a().c(C1798a.f13359aO, ""));
            eVar.e(C1798a.a().c(C1798a.f13364aT, ""));
            eVar.f(C1798a.a().c(C1798a.cF, ""));
            eVar.g(this.f7015d);
            eVar.h(C1798a.a().c(C1798a.cO, ""));
            eVar.i(C1818g.c().getLanguage());
            bW.d dVarA = aVar.a(eVar);
            if (dVarA == null) {
                return false;
            }
            if (dVarA.a() == 0) {
                C.d(this.f7015d + ": No updates available.");
                return true;
            }
            if (dVarA.a() != 2) {
                C.d(this.f7015d + ": No updates accessible.");
                return true;
            }
            C.d("Internet detected, doing update check: " + this.f7015d);
            boolean z2 = false;
            Iterator itC = dVarA.c();
            while (itC.hasNext()) {
                bW.c cVar = (bW.c) itC.next();
                if (cVar.a().equals("fileUpdate")) {
                    try {
                        String[] strArrB = cVar.b();
                        String str2 = null;
                        if (strArrB.length > 2) {
                            str2 = strArrB[2];
                        }
                        String str3 = strArrB[1];
                        aVar.a(this.f7014c, str3.substring(str3.lastIndexOf("/") + 1), str3, str2);
                    } catch (IOException e2) {
                        z2 = true;
                        Logger.getLogger(O.class.getName()).log(Level.SEVERE, "Ini Updater failed to update file: " + ((Object) cVar), (Throwable) e2);
                    }
                }
            }
            if (z2) {
                return true;
            }
            String strB = W.b(W.b(dVarA.b(), C1798a.f13268b + " Update available! Would you like Auto Update to upgrade to version ", ""), " now?", "");
            C1798a.a().b(str, strB.trim());
            C.d(this.f7015d + ": Update to " + strB);
            return true;
        } catch (IOException e3) {
            System.out.println("Unable to read from update server, connection to server unavailable");
            return false;
        }
    }
}
