package bh;

import G.C0141x;
import G.C0142y;
import G.R;
import ar.C0836c;
import ar.C0837d;
import ar.InterfaceC0845l;
import bH.W;
import g.C1733k;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;

/* renamed from: bh.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/l.class */
public class C1152l implements InterfaceC0845l {

    /* renamed from: a, reason: collision with root package name */
    private R f8115a = null;

    /* renamed from: b, reason: collision with root package name */
    private static C1152l f8116b = null;

    private C1152l() {
    }

    public static C1152l b() {
        if (f8116b == null) {
            f8116b = new C1152l();
        }
        return f8116b;
    }

    @Override // ar.InterfaceC0845l
    public void a(C0836c c0836c, int i2) {
        if (c0836c.b().equals(Action.DEFAULT)) {
            a(c0836c);
            return;
        }
        b(c0836c.b());
        h.i.c("FIELD_GROUP_NAME_" + c0836c.b(), Integer.toString(i2));
        for (int i3 = 0; i3 < c0836c.c().size(); i3++) {
            C0837d c0837d = (C0837d) c0836c.c().get(i3);
            if (c0837d.a().indexOf(".") > 0) {
                h.i.c("FIELD_SELECTED_GROUP_" + c0836c.b() + "_" + c0837d.a(), c0837d.b());
            }
        }
    }

    @Override // ar.InterfaceC0845l
    public List a() {
        String[] strArrE = h.i.e("FIELD_GROUP_NAME_");
        if (this.f8115a != null && !this.f8115a.ad().isEmpty()) {
            ArrayList arrayList = new ArrayList();
            Iterator it = this.f8115a.ad().iterator();
            while (it.hasNext()) {
                arrayList.add(((C0141x) it.next()).a());
            }
            for (String str : strArrE) {
                String strB = W.b(str, "FIELD_GROUP_NAME_", "");
                if (!arrayList.contains(strB)) {
                    arrayList.add(strB);
                }
            }
            strArrE = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        ArrayList arrayList2 = new ArrayList();
        C0836c c0836cC = c();
        c0836cC.a(-1);
        arrayList2.add(c0836cC);
        int i2 = 0;
        for (String str2 : strArrE) {
            if (!str2.equals(Action.DEFAULT)) {
                String strA = C1733k.a(str2, "FIELD_GROUP_NAME_", "");
                C0836c c0836cA = a(strA);
                C0141x c0141xB = this.f8115a.B(strA);
                try {
                    c0836cA.a(Integer.parseInt(h.i.e("FIELD_GROUP_NAME_" + c0836cA.b(), "" + (c0141xB != null ? c0141xB.c() : i2))));
                    i2++;
                } catch (Exception e2) {
                    int i3 = i2;
                    i2++;
                    c0836cA.a(i3);
                }
                arrayList2.add(c0836cA);
            }
        }
        Collections.sort(arrayList2, new C1153m(this));
        return arrayList2;
    }

    @Override // ar.InterfaceC0845l
    public C0836c a(String str) {
        C0836c c0836c = new C0836c(str);
        C0141x c0141xB = this.f8115a.B(str);
        if (c0141xB != null) {
            c0836c.a(true);
            c0836c.a(c0141xB.c());
            for (C0142y c0142y : c0141xB.b()) {
                c0836c.a(new C0837d("graph" + c0142y.a() + "." + c0142y.b(), c0142y.c()));
            }
            c0836c = b(c0836c);
        } else {
            c0836c.a(!h.i.b(new StringBuilder().append("FIELD_GROUP_NAME_").append(c0836c.b()).toString(), "Yes").equals("Yes"));
        }
        for (String strSubstring : bH.R.a(h.i.e("FIELD_SELECTED_GROUP_" + str + "_"))) {
            String strF = h.i.f(strSubstring, c0836c.c(strSubstring));
            if (strSubstring.contains("_")) {
                strSubstring = strSubstring.substring(strSubstring.lastIndexOf("_") + 1);
            }
            c0836c.a(new C0837d(strSubstring, strF));
        }
        return b(c0836c);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private C0836c b(C0836c c0836c) {
        int iA = h.i.a("numberOfGraphs", h.i.f12273t);
        int iA2 = h.i.a("numberOfOverlays", h.i.f12274u);
        ArrayList<C0837d> arrayList = new ArrayList();
        arrayList.addAll(c0836c.c());
        ArrayList arrayList2 = new ArrayList();
        for (C0837d c0837d : arrayList) {
            String strB = W.b(c0837d.a(), "graph", "");
            try {
                int i2 = Integer.parseInt(strB.substring(0, strB.indexOf(".")));
                int i3 = Integer.parseInt(strB.substring(strB.indexOf(".") + 1, strB.length()));
                if (i2 >= iA || i3 >= iA2) {
                    arrayList2.add(c0837d);
                    c0836c.b(c0837d.a());
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        for (int i4 = 0; i4 < iA && !arrayList2.isEmpty(); i4++) {
            for (int i5 = 0; i5 < iA2 && !arrayList2.isEmpty(); i5++) {
                String str = "graph" + i4 + "." + i5;
                if (c0836c.c(str).equals(C0836c.f6222a)) {
                    C0837d c0837d2 = (C0837d) arrayList2.remove(0);
                    c0837d2.a(str);
                    c0836c.a(c0837d2);
                }
            }
        }
        return c0836c;
    }

    @Override // ar.InterfaceC0845l
    public void b(String str) {
        String[] strArrE;
        if (!str.equals(Action.DEFAULT)) {
            String[] strArrE2 = h.i.e("FIELD_SELECTED_GROUP_" + str + "_");
            if (strArrE2 == null) {
                return;
            }
            h.i.d("FIELD_GROUP_NAME_" + str);
            for (String str2 : strArrE2) {
                h.i.d(str2);
            }
            return;
        }
        int iA = h.i.a("numberOfGraphs", h.i.f12273t);
        for (int i2 = 0; i2 < iA && (strArrE = h.i.e("graph" + i2 + ".")) != null; i2++) {
            for (String str3 : strArrE) {
                h.i.d(str3);
            }
        }
    }

    private C0836c c() {
        int iA = h.i.a("numberOfGraphs", h.i.f12273t);
        int iA2 = h.i.a("numberOfOverlays", h.i.f12274u);
        C0836c c0836c = new C0836c(Action.DEFAULT);
        c0836c.a(0);
        C0141x c0141xB = this.f8115a.B(Action.DEFAULT);
        if (c0141xB != null) {
            for (C0142y c0142y : c0141xB.b()) {
                c0836c.a(new C0837d("graph" + c0142y.a() + "." + c0142y.b(), c0142y.c()));
            }
        }
        for (int i2 = 0; i2 < iA; i2++) {
            for (int i3 = 0; i3 < iA2; i3++) {
                String str = "graph" + i2 + "." + i3;
                c0836c.a(new C0837d(str, h.i.a(str, c0836c.c(str))));
            }
        }
        c0836c.a(true);
        return c0836c;
    }

    public void a(C0836c c0836c) {
        int iA = h.i.a("numberOfGraphs", h.i.f12273t);
        int iA2 = h.i.a("numberOfOverlays", h.i.f12274u);
        for (int i2 = 0; i2 < iA; i2++) {
            for (int i3 = 0; i3 < iA2; i3++) {
                h.i.d("graph" + i2 + "." + i3);
            }
        }
        for (C0837d c0837d : c0836c.c()) {
            String strA = c0837d.a();
            if (strA.contains("_")) {
                strA = strA.substring(strA.lastIndexOf("_") + 1);
            }
            h.i.c(strA, c0837d.b());
        }
    }

    public void a(R r2) {
        this.f8115a = r2;
    }
}
