package J;

import G.C0051ak;
import G.C0094c;
import G.C0128k;
import G.InterfaceC0110cp;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:J/h.class */
public abstract class h implements InterfaceC0110cp, Serializable {

    /* renamed from: a, reason: collision with root package name */
    public static String f1439a = "protocolError";

    public abstract void a();

    public void b() {
        C0051ak c0051ak = new C0051ak();
        c0051ak.v("protocolError");
        c0051ak.a(new C0094c("Protocol Error"));
        c0051ak.b(new C0094c("Protocol Error"));
        c0051ak.a(C0128k.f1266q);
        c0051ak.b(C0128k.f1250a);
        c0051ak.c(C0128k.f1258i);
        c0051ak.d(C0128k.f1252c);
        c0051ak.a(f1439a);
        I.d.a().a(c0051ak);
    }

    public void d() {
        I.d.a().b("protocoloError");
    }
}
