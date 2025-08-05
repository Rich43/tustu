package G;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: G.cf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cf.class */
public class C0100cf implements InterfaceC0099ce, Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f1116a = null;

    /* renamed from: b, reason: collision with root package name */
    private R f1117b = null;

    public void a(String str) {
        this.f1116a = str;
    }

    @Override // G.InterfaceC0099ce
    public C0098cd a(C0098cd c0098cd) {
        return b(c0098cd);
    }

    private C0098cd b(C0098cd c0098cd) throws V.g {
        int i2;
        double[][] dArrI = this.f1117b.c(this.f1116a).i(this.f1117b.h());
        c0098cd.k();
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < dArrI.length && (i2 = (int) dArrI[i3][0]) >= 0; i3++) {
            List listA = a(i2);
            if (listA.isEmpty()) {
                throw new V.g("Invalid OutputChannel Offset: " + i2);
            }
            arrayList.addAll(listA);
        }
        int iA = -1;
        int i4 = 0;
        int iL = 0;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            aH aHVar = (aH) arrayList.get(size);
            C0097cc c0097cc = new C0097cc();
            c0097cc.b(aHVar.aJ());
            c0097cc.d(b(aHVar.aJ()));
            if (iA != aHVar.a()) {
                iA = aHVar.a();
                i4 = iL;
                iL = i4 + aHVar.l();
            }
            c0097cc.a(aHVar.h());
            c0097cc.b(aHVar.i());
            c0097cc.c(aHVar.e());
            if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                c0097cc.a((aHVar.r() - aHVar.q()) + 1, (i4 * 8) + aHVar.q());
            } else {
                c0097cc.a(aHVar.l() * 8, i4 * 8);
            }
            c0098cd.a(c0097cc, 0);
        }
        c0098cd.d(iL);
        return c0098cd;
    }

    private String b(String str) {
        Iterator it = this.f1117b.g().iterator();
        while (it.hasNext()) {
            C0043ac c0043ac = (C0043ac) it.next();
            if (c0043ac.a().equals(str)) {
                return c0043ac.b();
            }
        }
        return str;
    }

    private List a(int i2) {
        ArrayList arrayList = new ArrayList();
        Iterator itQ = this.f1117b.q();
        while (itQ.hasNext()) {
            aH aHVar = (aH) itQ.next();
            if (!aHVar.b().equals("formula") && aHVar.a() == i2) {
                arrayList.add(aHVar);
            }
        }
        return arrayList;
    }

    public void a(R r2) {
        this.f1117b = r2;
    }
}
