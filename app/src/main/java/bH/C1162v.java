package bh;

import G.C0043ac;
import G.C0113cs;
import G.R;
import G.T;
import G.aH;
import W.C0184j;
import W.C0188n;
import ao.C0804hg;
import bH.C;
import bH.W;
import i.InterfaceC1741a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import r.C1798a;
import r.C1806i;
import sun.util.locale.LanguageTag;

/* renamed from: bh.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/v.class */
public class C1162v implements InterfaceC1741a {

    /* renamed from: a, reason: collision with root package name */
    Map f8134a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    boolean f8135b = C1806i.a().a(" a09kmfds098432lkg89vlk");

    /* renamed from: c, reason: collision with root package name */
    List f8136c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    C1164x f8137d = new C1164x(this);

    /* renamed from: e, reason: collision with root package name */
    Map f8138e;

    public C1162v() {
        C0113cs.a().a(this.f8137d);
        this.f8138e = new HashMap();
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        if (this.f8135b && C1798a.a().a(C1798a.f13308P, C1798a.f13309Q) && !b()) {
            b(i2);
        }
    }

    public void a() {
        this.f8134a.clear();
        this.f8136c.clear();
        this.f8138e.clear();
    }

    private void b(int i2) {
        String strC;
        R rC;
        C0188n c0188nR = C0804hg.a().r();
        ArrayList<C1163w> arrayList = new ArrayList();
        if (c0188nR != null) {
            Iterator it = c0188nR.iterator();
            while (it.hasNext()) {
                String strA = ((C0184j) it.next()).a();
                if (strA.contains(".")) {
                    strC = strA.substring(0, strA.indexOf(46));
                    strA = strA.substring(strA.indexOf(46) + 1);
                    rC = T.a().c(strC);
                    if (rC == null) {
                        strA = strC + "." + strA;
                        strC = T.a().c().c();
                        rC = T.a().c(strC);
                    }
                } else {
                    strC = T.a().c().c();
                    rC = T.a().c(strC);
                }
                if (rC == null) {
                    C.d("EcuConfig '" + strC + "' not found, using working config");
                    rC = T.a().c();
                }
                C0043ac c0043acA = a(rC, strA);
                if (c0043acA != null) {
                    try {
                        aH aHVarG = rC.g(c0043acA.a());
                        byte[] bArrA = a(rC);
                        if (aHVarG != null && !aHVarG.a(bArrA, r0.d(i2))) {
                            arrayList.add(new C1163w(this, strC, aHVarG, r0.d(i2)));
                            if (!this.f8136c.contains(aHVarG)) {
                                this.f8136c.add(aHVarG);
                            }
                        }
                    } catch (Exception e2) {
                    }
                }
            }
            for (String str : this.f8134a.keySet()) {
                try {
                    C0113cs.a().a(str, (byte[]) this.f8134a.get(str));
                } catch (Exception e3) {
                }
            }
            for (C1163w c1163w : arrayList) {
                c1163w.d().c(c1163w.b());
                try {
                    C0113cs.a().a(c1163w.c(), c1163w.a(), c1163w.b());
                } catch (V.a e4) {
                }
            }
        }
    }

    private byte[] a(R r2) {
        byte[] bArr = (byte[]) this.f8134a.get(r2.c());
        if (bArr == null) {
            bArr = new byte[r2.O().n()];
            this.f8134a.put(r2.c(), bArr);
        }
        return bArr;
    }

    private Map a(String str) {
        Map map = (Map) this.f8138e.get(str);
        if (map == null) {
            map = new HashMap();
            this.f8138e.put(str, map);
        }
        return map;
    }

    private C0043ac a(R r2, String str) {
        Map mapA = a(r2.c());
        C0043ac c0043ac = (C0043ac) mapA.get(str);
        if (c0043ac != null) {
            return c0043ac;
        }
        Iterator it = r2.g().iterator();
        while (it.hasNext()) {
            C0043ac c0043ac2 = (C0043ac) it.next();
            if (W.b(c0043ac2.b(), LanguageTag.SEP, " ").equals(str)) {
                mapA.put(str, c0043ac2);
                return c0043ac2;
            }
        }
        return null;
    }

    private boolean b() {
        R rC = T.a().c();
        return rC != null && rC.R();
    }
}
