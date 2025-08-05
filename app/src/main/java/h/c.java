package H;

import G.C0069bb;
import G.R;
import G.T;
import G.aM;
import G.bT;
import bH.C;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:H/c.class */
public class c implements bT {

    /* renamed from: a, reason: collision with root package name */
    int f1335a = 255;

    @Override // G.bT
    public void a() {
    }

    @Override // G.bT
    public void a(boolean z2) {
        ArrayList arrayListT;
        aM aMVarC;
        if (z2) {
            R rC = T.a().c();
            if (aG.c.a(rC)) {
                arrayListT = rC.u();
            } else if (!aG.c.b(rC)) {
                return;
            } else {
                arrayListT = rC.t();
            }
            try {
                boolean z3 = true;
                aM aMVarC2 = rC.c(rC.O().ag());
                double[][] dArrI = aMVarC2.i(rC.h());
                if (arrayListT != null) {
                    List listB = b(arrayListT);
                    int i2 = 0;
                    while (i2 < dArrI.length) {
                        int iIntValue = i2 < listB.size() ? ((Integer) listB.get(i2)).intValue() : this.f1335a;
                        if (dArrI[i2][0] != iIntValue) {
                            z3 = false;
                            dArrI[i2][0] = iIntValue;
                        }
                        i2++;
                    }
                }
                if (!z3) {
                    C.d("Replay Pad does not match current configuration, Updating Replay Pad.");
                    aMVarC2.b(true);
                    aMVarC2.a(rC.h(), dArrI);
                    aMVarC2.b(false);
                    try {
                        if (aG.c.b(rC) && (aMVarC = rC.c("Log_Time")) != null && aMVarC.j(rC.h()) > aMVarC.r()) {
                            aMVarC.a(rC.h(), aMVarC.r());
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                C.a(e3);
            }
        }
    }

    private List b(List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            C0069bb c0069bb = (C0069bb) it.next();
            if (!arrayList.contains(Integer.valueOf(c0069bb.y()))) {
                arrayList.add(Integer.valueOf(c0069bb.y()));
            }
        }
        return a(arrayList);
    }

    public List a(List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            for (int i3 = i2 + 1; i3 < list.size(); i3++) {
                Integer num = (Integer) list.get(i2);
                Integer num2 = (Integer) list.get(i3);
                if (num.intValue() > num2.intValue()) {
                    list.set(i2, num2);
                    list.set(i3, num);
                }
            }
        }
        return list;
    }
}
