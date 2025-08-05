package bc;

import ae.n;
import ae.s;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bc.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/l.class */
class C1065l implements InterfaceC1066m {

    /* renamed from: a, reason: collision with root package name */
    s f7879a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1064k f7880b;

    C1065l(C1064k c1064k, s sVar) {
        this.f7880b = c1064k;
        this.f7879a = sVar;
    }

    @Override // bc.InterfaceC1066m
    public void a(String str, Object obj) {
        try {
            this.f7879a.a(str, obj);
        } catch (n e2) {
            Logger.getLogger(C1064k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
