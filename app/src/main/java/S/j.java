package S;

import G.T;
import W.ap;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:S/j.class */
public class j extends h {

    /* renamed from: c, reason: collision with root package name */
    private ap f1840c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f1841d = true;

    @Override // S.h, S.a
    public void d(String str) {
        super.d(str);
        i(str);
    }

    public ap h() {
        return b.a().b();
    }

    public void h(String str) {
        if (h() != null) {
            e(a(str, "triggerExpression", ""));
            f(a(str, "resetExpression", ""));
            b(false);
            a(Boolean.parseBoolean(a(str, "active", "false")));
        }
    }

    public void i(String str) {
        b(str, "triggerExpression", d());
        b(str, "resetExpression", e());
        b(str, "triggered", Boolean.toString(b()));
        b(str, "active", Boolean.toString(c()));
    }

    private String a(String str, String str2) {
        return "EventTrigger:" + a() + CallSiteDescriptor.TOKEN_DELIMITER + ((T.a().c() == null || (T.a().c() != null && T.a().c().c().equals(str))) ? "" : str + ".") + str2;
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
}
