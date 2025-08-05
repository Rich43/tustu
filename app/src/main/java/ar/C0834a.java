package ar;

import bH.R;
import g.C1733k;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;

/* renamed from: ar.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ar/a.class */
public class C0834a implements InterfaceC0845l {
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
            if (c0837d.a().indexOf(".") > 0 && c0837d.a().length() < h.i.f12271r) {
                h.i.c("FIELD_SELECTED_GROUP_" + c0836c.b() + "_" + c0837d.a(), c0837d.b());
            }
        }
    }

    @Override // ar.InterfaceC0845l
    public List a() {
        String[] strArrE = h.i.e("FIELD_GROUP_NAME_");
        ArrayList arrayList = new ArrayList();
        C0836c c0836cB = b();
        c0836cB.a(-1);
        arrayList.add(c0836cB);
        int i2 = 0;
        for (String str : strArrE) {
            C0836c c0836cA = a(C1733k.a(str, "FIELD_GROUP_NAME_", ""));
            try {
                c0836cA.a(Integer.parseInt(h.i.e("FIELD_GROUP_NAME_" + c0836cA.b(), "0")));
                i2++;
            } catch (Exception e2) {
                int i3 = i2;
                i2++;
                c0836cA.a(i3);
            }
            arrayList.add(c0836cA);
        }
        Collections.sort(arrayList, new C0835b(this));
        return arrayList;
    }

    @Override // ar.InterfaceC0845l
    public C0836c a(String str) {
        C0836c c0836c = new C0836c(str);
        for (String strSubstring : R.a(h.i.e("FIELD_SELECTED_GROUP_" + str + "_"))) {
            String strF = h.i.f(strSubstring, " ");
            if (strSubstring.contains("_")) {
                strSubstring = strSubstring.substring(strSubstring.lastIndexOf("_") + 1);
            }
            c0836c.a(new C0837d(strSubstring, strF));
        }
        c0836c.a(!h.i.b(new StringBuilder().append("FIELD_GROUP_NAME_").append(c0836c.b()).toString(), "Yes").equals("Yes"));
        return c0836c;
    }

    @Override // ar.InterfaceC0845l
    public void b(String str) {
        String[] strArrE = h.i.e("FIELD_SELECTED_GROUP_" + str + "_");
        if (strArrE == null) {
            return;
        }
        h.i.d("FIELD_GROUP_NAME_" + str);
        for (String str2 : strArrE) {
            h.i.d(str2);
        }
    }

    private C0836c b() {
        int iA = h.i.a("numberOfGraphs", h.i.f12273t);
        int iA2 = h.i.a("numberOfOverlays", h.i.f12274u);
        C0836c c0836c = new C0836c(Action.DEFAULT);
        for (int i2 = 0; i2 < iA; i2++) {
            for (int i3 = 0; i3 < iA2; i3++) {
                String str = "graph" + i2 + "." + i3;
                c0836c.a(new C0837d(str, h.i.a(str, " ")));
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
                h.i.c("graph" + i2 + "." + i3, " ");
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
}
