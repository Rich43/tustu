package ap;

import ao.C0645bi;
import bH.C;
import i.InterfaceC1745e;
import java.io.File;

/* renamed from: ap.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ap/a.class */
public class C0829a implements InterfaceC1745e {
    @Override // i.InterfaceC1745e
    public boolean a(String str, String str2) {
        if (str == null || !str.equals("openLog")) {
            return false;
        }
        File file = new File(str2);
        if (!file.exists()) {
            C.a("Log File not found: " + file.getAbsolutePath());
            return true;
        }
        C0645bi.a().b().b(file.getAbsolutePath(), false);
        C0645bi.a().b().c();
        return true;
    }
}
