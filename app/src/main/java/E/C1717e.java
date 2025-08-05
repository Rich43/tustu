package e;

import G.C0126i;
import G.R;
import G.T;
import G.aH;
import V.g;
import d.InterfaceC1711c;
import d.i;
import d.k;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

/* renamed from: e.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:e/e.class */
public class C1717e implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f12139a = "outputChannelName";

    /* renamed from: b, reason: collision with root package name */
    public static String f12140b = "ecuConfigName";

    /* renamed from: c, reason: collision with root package name */
    String f12141c = null;

    @Override // d.InterfaceC1711c
    public String b() {
        return "Reset Runtime Value";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Runtime Values";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws d.e {
        String property = properties.getProperty(f12140b);
        String property2 = properties.getProperty(f12139a, null);
        if (property2 == null) {
            throw new d.e(f12139a + " is required");
        }
        try {
            C0126i.a(property, property2);
        } catch (g e2) {
            throw new d.e(e2.getMessage());
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "resetRuntimeValues";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty(f12140b);
        String property2 = properties.getProperty(f12139a, null);
        if (property2 == null) {
            throw new d.e(f12139a + " is required");
        }
        R rC = (property == null || property.isEmpty()) ? T.a().c() : T.a().c(property);
        if (rC == null) {
            if (property != null) {
                throw new d.e("No working configuration and no config name requested");
            }
            throw new d.e("Configuration Name not found: " + property);
        }
        if (rC.g(property2) == null) {
            throw new d.e("OutputChannel not found: " + property2);
        }
    }

    @Override // d.InterfaceC1711c
    public k e() {
        k kVar = new k();
        i iVar = new i(f12139a, null);
        R rC = T.a().c();
        if (rC != null && (this.f12141c == null || !this.f12141c.equals(rC.c()))) {
            this.f12141c = rC.c();
            iVar.a(0);
            ArrayList arrayList = new ArrayList();
            Iterator itQ = rC.q();
            while (itQ.hasNext()) {
                aH aHVar = (aH) itQ.next();
                if (aHVar.s() && aHVar.w()) {
                    arrayList.add(aHVar.aJ());
                }
            }
            Collections.sort(arrayList);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.addAll(arrayList);
            iVar.a(arrayList2);
            iVar.c("Select the Accumulated OutputChannel to Rest the accumulated value.");
        }
        kVar.add(iVar);
        return kVar;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return true;
    }
}
