package ao;

import W.C0184j;
import W.C0188n;
import g.C1724b;
import g.C1733k;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:ao/dY.class */
public class dY implements bB.q {
    private void c(String str) {
        String str2 = "FIELD_MIN_MAX_" + str;
        if (h.i.c(str2) == null) {
            h.i.d(str2);
        } else {
            h.i.c(str2, ";");
        }
    }

    @Override // bB.q
    public void a(bB.r rVar) {
        C0188n c0188nR = C0804hg.a().r();
        C0184j c0184jA = c0188nR == null ? null : c0188nR.a(rVar.e());
        StringBuilder sb = new StringBuilder();
        if (rVar.c()) {
            sb.append("Auto");
        } else {
            sb.append(rVar.a());
        }
        sb.append(";");
        if (rVar.d()) {
            sb.append("Auto");
        } else {
            sb.append(rVar.b());
        }
        sb.append(";");
        sb.append(rVar.f());
        h.i.c("FIELD_MIN_MAX_" + rVar.e(), sb.toString());
        if (c0184jA != null) {
            c0184jA.g((float) rVar.a());
            c0184jA.f((float) rVar.b());
            if (rVar.f() != -1) {
                c0184jA.e(rVar.f());
            } else {
                c0184jA.e(c0184jA.B());
            }
        }
        C0645bi.a().e().o();
    }

    @Override // bB.q
    public void a(String str) {
        c(str);
        C0645bi.a().e().o();
    }

    @Override // bB.q
    public List a() {
        ArrayList arrayList = new ArrayList();
        String[] strArrE = h.i.e("FIELD_MIN_MAX_");
        if (h.i.a(h.i.f12284E, h.i.f12285F)) {
            strArrE = bH.R.a(strArrE);
        }
        for (int i2 = 0; i2 < strArrE.length; i2++) {
            h.i.a(strArrE[i2], "");
            String strA = C1733k.a(strArrE[i2], "FIELD_MIN_MAX_", "");
            if (C0804hg.a().r() == null || C0804hg.a().r().a(strA) != null) {
                bB.r aVar = null;
                try {
                    aVar = C1724b.a().a(strA);
                } catch (Exception e2) {
                }
                if (aVar == null) {
                    aVar = new bB.a(strA);
                    aVar.a(Double.NaN);
                    aVar.b(Double.NaN);
                    aVar.a(-1);
                }
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    @Override // bB.q
    public bB.r b(String str) {
        return C1724b.a().a(str);
    }

    @Override // bB.q
    public List b() {
        ArrayList arrayList = new ArrayList();
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR != null) {
            Iterator it = c0188nR.iterator();
            while (it.hasNext()) {
                arrayList.add(((C0184j) it.next()).a());
            }
        }
        return arrayList;
    }

    @Override // bB.q
    public bB.r b(bB.r rVar) {
        return C1724b.a().a(rVar);
    }
}
