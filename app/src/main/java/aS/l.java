package aS;

import G.C0051ak;
import G.C0094c;
import G.C0113cs;
import G.C0128k;
import G.R;
import aR.A;
import bH.C;
import br.C1232J;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aS/l.class */
public class l {

    /* renamed from: b, reason: collision with root package name */
    private Map f3931b = new HashMap();

    /* renamed from: a, reason: collision with root package name */
    public static String f3930a = "veAnalyzeActive_";

    /* renamed from: c, reason: collision with root package name */
    private static l f3932c = null;

    private l() {
    }

    public static l a() {
        if (f3932c == null) {
            f3932c = new l();
        }
        return f3932c;
    }

    public String a(R r2, String str) {
        String str2 = f3930a + str;
        C0113cs.a().d(str2);
        C0113cs.a().a(str2, 0.0d);
        this.f3931b.put(str, str2);
        try {
            if (C1232J.a().a(r2, str) != null) {
                C0051ak c0051ak = new C0051ak();
                c0051ak.v(str2 + "Active");
                c0051ak.a(new C0094c("VEAL " + str));
                c0051ak.b(new C0094c("VEAL " + str));
                c0051ak.a(C0128k.f1268s);
                c0051ak.b(C0128k.f1250a);
                c0051ak.c(C0128k.f1258i);
                c0051ak.d(C0128k.f1252c);
                if (C1806i.a().a("poij  fdsz poi9ure895 ms7(")) {
                    c0051ak.b(A.f3846a + "?" + A.f3847b + "=" + str);
                }
                c0051ak.a(str2);
                I.d.a().a(c0051ak);
            }
        } catch (V.g e2) {
        }
        return str2;
    }

    public List a(R r2, List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(a(r2, (String) it.next()));
        }
        return arrayList;
    }

    public void b() {
        this.f3931b.clear();
    }

    public void a(String str) {
        String str2 = (String) this.f3931b.get(str);
        if (str2 != null) {
            C0113cs.a().a(str2, 1.0d);
        } else {
            C.b("Attempt to start VE Analyze on unregistered table name: " + str);
        }
    }

    public void b(String str) {
        String str2 = (String) this.f3931b.get(str);
        if (str2 != null) {
            C0113cs.a().a(str2, 0.0d);
        } else {
            C.b("Attempt to stop VE Analyze on unregistered table name: " + str);
        }
    }
}
