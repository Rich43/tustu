package S;

import G.T;
import W.ap;
import d.InterfaceC1711c;

/* loaded from: TunerStudioMS.jar:S/n.class */
public class n extends h implements k {

    /* renamed from: c, reason: collision with root package name */
    private ap f1842c = null;

    /* renamed from: d, reason: collision with root package name */
    private String f1843d = null;

    public n() {
        a(new o(this));
    }

    @Override // S.h, S.a
    public void d(String str) {
        super.d(str);
        i(str);
    }

    public ap h() {
        return this.f1842c;
    }

    public void a(ap apVar) {
        this.f1842c = apVar;
    }

    public void h(String str) {
        if (h() != null) {
            e(a(str, "triggerExpression", ""));
            f(a(str, "resetExpression", ""));
            j(a(str, "targetAction", ""));
            try {
                a(Integer.parseInt(a(str, "resetDelay", "")));
            } catch (Exception e2) {
                a(-1);
            }
            b(false);
            a(Boolean.parseBoolean(a(str, "active", "false")));
        }
    }

    public void i(String str) {
        b(str, "triggerExpression", d());
        b(str, "resetExpression", e());
        b(str, "targetAction", this.f1843d);
        b(str, "resetDelay", Integer.toString(g()));
        b(str, "triggered", Boolean.toString(b()));
        b(str, "active", Boolean.toString(c()));
    }

    private String a(String str, String str2) {
        return ((str.isEmpty() || T.a().c() == null || (T.a().c() != null && T.a().c().c().equals(str))) ? "" : str + ".") + str2;
    }

    private String a(String str, String str2, String str3) {
        if (h() == null) {
            return str3;
        }
        return h().b(a(str, str2), str3);
    }

    private void b(String str, String str2, String str3) {
        if (h() == null || str3 == null) {
            return;
        }
        h().a(a(str, str2), str3);
    }

    public String i() {
        return this.f1843d;
    }

    public void j(String str) {
        this.f1843d = str;
    }

    @Override // S.k
    public void d_() {
        if (this.f1843d != null) {
            InterfaceC1711c interfaceC1711cB = d.g.a().b(this.f1843d);
            if (interfaceC1711cB instanceof d.m) {
                ((d.m) interfaceC1711cB).n();
            }
        }
    }
}
