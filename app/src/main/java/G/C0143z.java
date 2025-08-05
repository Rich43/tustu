package G;

import bH.C0995c;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: G.z, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/z.class */
public class C0143z {

    /* renamed from: a, reason: collision with root package name */
    bH.aa f1333a;

    public C0143z(bH.aa aaVar) {
        this.f1333a = null;
        this.f1333a = aaVar;
    }

    public ArrayList a(R r2, Y y2, Y y3) {
        ArrayList arrayList = new ArrayList();
        for (String str : r2.k()) {
            aM aMVarC = r2.c(str);
            try {
                double[][] dArrI = aMVarC.i(y2);
                double[][] dArrI2 = aMVarC.i(y3);
                if (aMVarC.n(y2) && aMVarC.B() && aMVarC.aJ().indexOf(Constants.ATTRNAME_TEST) == -1 && !a(dArrI, dArrI2, aMVarC.u(), aMVarC.A()) && !aMVarC.M()) {
                    arrayList.add(aMVarC);
                }
            } catch (V.g e2) {
                bH.C.a("Failed to get data for EcuPrameter: " + aMVarC.aJ());
            }
        }
        return arrayList;
    }

    private List a(R r2) {
        ArrayList arrayList = new ArrayList();
        Iterator itB = r2.e().b();
        while (itB.hasNext()) {
            a(r2, arrayList, (aA) itB.next());
        }
        return arrayList;
    }

    private List a(R r2, List list, aA aAVar) {
        if (aAVar.b()) {
            Iterator itA = aAVar.a();
            while (itA.hasNext()) {
                a(r2, list, (aA) itA.next());
            }
        }
        if (!aAVar.c() && aAVar.d() != null && !aAVar.d().trim().isEmpty()) {
            list.add(aAVar.d());
        }
        return list;
    }

    public ArrayList a(R r2, ArrayList arrayList) {
        bD bDVarE = r2.e();
        ArrayList arrayList2 = new ArrayList();
        Iterator it = a(r2).iterator();
        while (it.hasNext()) {
            C0088bu c0088buC = bDVarE.c((String) it.next());
            if (c0088buC instanceof C0079bl) {
                C0079bl c0079bl = (C0079bl) c0088buC;
                if (a(arrayList, c0079bl)) {
                    C0079bl c0079blClone = c0079bl.clone();
                    c0079blClone.a((String[]) null);
                    c0079blClone.a(true);
                    C0088bu c0088bu = new C0088bu();
                    if (c0079blClone.M() == null || c0079blClone.M().equals("")) {
                        c0088bu.s(a(bL.c(r2, c0079blClone.aJ())));
                    }
                    c0088bu.a(c0079blClone);
                    arrayList2.add(c0088bu);
                }
            } else if (c0088buC instanceof C0072be) {
                C0072be c0072be = (C0072be) c0088buC;
                if (a(arrayList, c0072be.a()) || a(arrayList, c0072be.b()) || a(arrayList, c0072be.c())) {
                    C0072be c0072beClone = c0072be.clone();
                    c0072beClone.a(11);
                    c0072beClone.a(true);
                    C0088bu c0088bu2 = new C0088bu();
                    String strA = a(bL.c(r2, c0072beClone.aJ()));
                    c0088bu2.s(strA);
                    c0088bu2.s(strA);
                    c0088bu2.a(c0072beClone);
                    arrayList2.add(c0088bu2);
                }
            } else if (c0088buC instanceof C0047ag) {
                C0047ag c0047ag = (C0047ag) c0088buC;
                if (a(arrayList, c0047ag.b()) || a(arrayList, c0047ag.a())) {
                    arrayList2.add(c0047ag);
                }
            } else if (c0088buC != null) {
                if (c0088buC.H() > 0) {
                    Iterator itF = c0088buC.F();
                    while (itF.hasNext()) {
                        AbstractC0093bz abstractC0093bz = (AbstractC0093bz) itF.next();
                        if (abstractC0093bz instanceof C0083bp) {
                            C0083bp c0083bp = (C0083bp) abstractC0093bz;
                            if (a(arrayList, c0083bp.b()) && !arrayList2.contains(c0088buC) && !c0083bp.h()) {
                                arrayList2.add(c0088buC);
                            }
                        } else if ((abstractC0093bz instanceof E) && a(arrayList, ((E) abstractC0093bz).a()) && !arrayList2.contains(c0088buC)) {
                            arrayList2.add(c0088buC);
                        }
                    }
                } else {
                    Iterator it2 = c0088buC.e().iterator();
                    while (it2.hasNext()) {
                        if (a(arrayList, (String) it2.next()) && !arrayList2.contains(c0088buC)) {
                            arrayList2.add(c0088buC);
                        }
                    }
                }
            }
        }
        return arrayList2;
    }

    private String a(String str) {
        return this.f1333a != null ? this.f1333a.a(str) : str;
    }

    private boolean a(ArrayList arrayList, String str) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((aM) it.next()).aJ().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean a(ArrayList arrayList, C0079bl c0079bl) {
        if (!c0079bl.z()) {
            for (int i2 = 0; i2 < c0079bl.j(); i2++) {
                if (a(arrayList, c0079bl.d(i2))) {
                    return true;
                }
            }
        }
        for (int i3 = 0; i3 < c0079bl.d(); i3++) {
            if (a(arrayList, c0079bl.b(i3))) {
                return true;
            }
        }
        return false;
    }

    public boolean a(double[][] dArr, double[][] dArr2, int i2, double d2) {
        return C0995c.a(dArr, dArr2, i2, d2);
    }

    public ArrayList b(R r2, ArrayList arrayList) {
        return bL.a(r2, arrayList);
    }
}
