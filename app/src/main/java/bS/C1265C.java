package bs;

import G.R;
import G.dn;
import W.C0172ab;
import java.util.ArrayList;
import java.util.Iterator;
import r.C1798a;
import r.C1807j;

/* renamed from: bs.C, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bs/C.class */
public class C1265C implements aE.e {

    /* renamed from: a, reason: collision with root package name */
    private static C1265C f8539a = null;

    public static C1265C a() {
        if (f8539a == null) {
            f8539a = new C1265C();
        }
        return f8539a;
    }

    public ArrayList a(R r2) {
        Iterator itN = r2.N();
        if (!itN.hasNext()) {
            b(r2);
            itN = r2.N();
        }
        ArrayList arrayList = new ArrayList();
        while (itN.hasNext()) {
            arrayList.add((String) itN.next());
        }
        return arrayList;
    }

    public dn a(R r2, String str) {
        dn dnVarT = r2.t(str);
        if (dnVarT != null) {
            return dnVarT;
        }
        b(r2);
        return r2.t(str);
    }

    private void b(R r2) {
        String strI = r2.i();
        if (strI.startsWith("MS3")) {
            if (a(strI) >= 261.0f) {
                b(r2, C1807j.f13487w);
                return;
            } else {
                b(r2, C1807j.f13488x);
                return;
            }
        }
        if (strI.startsWith("MS2Extra")) {
            b(r2, C1807j.f13486v);
            return;
        }
        if (strI.startsWith("MS/Extra") || strI.startsWith("MS1/Extra") || strI.startsWith("MSnS-extra")) {
            b(r2, C1807j.f13485u);
            return;
        }
        if (strI.startsWith("MSII Rev") || strI.startsWith("LCT Rev")) {
            b(r2, C1807j.f13486v);
        } else if (strI.startsWith("20")) {
            b(r2, C1807j.f13484t);
        }
    }

    private static float a(String str) {
        float f2 = -1.0f;
        try {
            f2 = Float.parseFloat(str.replace("MS3 Format", ""));
        } catch (Exception e2) {
        }
        return f2;
    }

    private void b(R r2, String str) {
        if (C1798a.a().c(C1798a.f13387bq, C1798a.f13388br) && C1798a.a().c(C1798a.f13389bs, C1798a.f13390bt)) {
            return;
        }
        new C0172ab().a(r2, str, false);
    }

    @Override // aE.e
    public void a(aE.a aVar, R r2) {
    }

    @Override // aE.e
    public void e_() {
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }
}
