package av;

import W.C0171aa;
import W.C0200z;
import W.aj;
import W.av;
import ao.C0645bi;
import bH.C;
import com.efiAnalytics.ui.bV;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: av.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/a.class */
public class C0862a {
    public AbstractC0868g a(String str, AbstractC0868g abstractC0868g) throws V.a {
        try {
            if (C0171aa.a(str)) {
                new C0171aa().a(abstractC0868g.h(), str, (List) null);
                try {
                    abstractC0868g.h().e(C0200z.a(new File(str)));
                } catch (Exception e2) {
                    C.c("Failed to get signature from bigTune file: " + str);
                }
            } else {
                new av().a(abstractC0868g.h(), str, null);
            }
            return abstractC0868g;
        } catch (V.g e3) {
            Logger.getLogger(C0862a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new V.a("Failed to set the data from:\n" + str);
        } catch (aj e4) {
            e4.printStackTrace();
            throw new V.a("Password Protected Tune File:\n" + str);
        }
    }

    protected void b(String str, AbstractC0868g abstractC0868g) {
        if (str.endsWith(h.i.f12276w)) {
            try {
                new av().a(str, abstractC0868g.h());
            } catch (IOException e2) {
                Logger.getLogger(C0862a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                bV.d("Error Saving Tune.\n" + e2.getMessage(), C0645bi.a().b());
                return;
            }
        } else {
            try {
                new C0171aa().a(abstractC0868g.h(), str, new C0863b(this), null);
            } catch (V.g e3) {
                bV.d(e3.getMessage(), C0645bi.a().b());
            } catch (Exception e4) {
                C.a("Unhandled error occured saving tune.\nSee log for more detail", e4, C0645bi.a().b());
            }
        }
        abstractC0868g.h().p().g();
    }
}
