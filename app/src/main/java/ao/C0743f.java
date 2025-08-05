package ao;

import W.C0188n;
import W.C0189o;
import bu.C1368a;
import com.efiAnalytics.ui.InterfaceC1535a;
import i.C1743c;
import i.InterfaceC1742b;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ao.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/f.class */
class C0743f implements InterfaceC1535a {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ com.efiAnalytics.dialogs.b f5696a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0188n f5697b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0636b f5698c;

    C0743f(C0636b c0636b, com.efiAnalytics.dialogs.b bVar, C0188n c0188n) {
        this.f5698c = c0636b;
        this.f5696a = bVar;
        this.f5697b = c0188n;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public boolean a() {
        try {
            C1368a c1368aA = this.f5696a.a();
            C0188n c0188nR = C0804hg.a().r();
            if (c0188nR != null) {
                c1368aA.b(c0188nR);
                h.i.c(h.i.f12330aw, this.f5696a.b());
                if (!ac.r.a() && c0188nR.k() != null && c0188nR.k().exists()) {
                    if (this.f5697b.k().getAbsolutePath().toLowerCase().endsWith(".mlg")) {
                        ac.y.a(c0188nR, c0188nR.k().getAbsolutePath());
                    } else {
                        C0189o.b(c0188nR, c0188nR.k().getAbsolutePath(), h.i.f12275v);
                    }
                }
                Iterator itG = C1743c.a().g();
                while (itG.hasNext()) {
                    ((InterfaceC1742b) itG.next()).a(1.0d);
                }
            }
            return true;
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d(e2.getLocalizedMessage(), C0645bi.a().b());
            Logger.getLogger(C0636b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return true;
        } catch (IOException e3) {
            com.efiAnalytics.ui.bV.d(e3.getLocalizedMessage(), C0645bi.a().b());
            e3.printStackTrace();
            return true;
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void b() {
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void c() {
    }
}
