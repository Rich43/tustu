package n;

import G.R;
import G.T;
import W.C0171aa;
import W.C0178d;
import ac.v;
import bH.C;
import bH.W;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:n/m.class */
public class m implements v {
    @Override // ac.v
    public String a() {
        return "LogStart_MAIN_TUNE";
    }

    @Override // ac.v
    public String b() {
        return "msq";
    }

    @Override // ac.v
    public boolean c() {
        return false;
    }

    @Override // ac.v
    public String d() {
        if (!C1806i.a().a(" 09s98r32-po3q9264") || !C1798a.a().c(C1798a.f13323ae, C1798a.f13324af)) {
            C.d("Not saving tune to data log, option disabled.");
            return null;
        }
        aE.a aVarA = aE.a.A();
        String str = null;
        if (aVarA != null) {
            File fileC = aVarA.c(aVarA.u());
            if (fileC.exists() && fileC.lastModified() > System.currentTimeMillis() - 172800000) {
                try {
                    str = new String(C0178d.a(fileC));
                } catch (IOException e2) {
                    Logger.getLogger(m.class.getName()).log(Level.SEVERE, "Error reading " + fileC.getName(), (Throwable) e2);
                }
            }
        }
        if (str != null) {
            return str;
        }
        C.c("No currenttune, will generate new msq for infodata.");
        C0171aa c0171aa = new C0171aa();
        R rC = T.a().c();
        if (rC != null) {
            try {
                File fileCreateTempFile = File.createTempFile(W.a(), "msq");
                c0171aa.a(rC, fileCreateTempFile.getAbsolutePath(), new o());
                str = new String(C0178d.a(fileCreateTempFile));
            } catch (V.g e3) {
                Logger.getLogger(m.class.getName()).log(Level.SEVERE, "Failed to save temp file msq for start tune Info Data", (Throwable) e3);
            } catch (IOException e4) {
                Logger.getLogger(m.class.getName()).log(Level.SEVERE, "Failed to create temp file for start tune Info Data", (Throwable) e4);
            }
        } else {
            C.c("Main Config is null, skipping tune info data.");
        }
        return str;
    }
}
