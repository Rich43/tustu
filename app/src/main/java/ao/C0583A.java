package ao;

import W.C0184j;
import ar.C0836c;
import ar.C0839f;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: ao.A, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/A.class */
public class C0583A implements InterfaceC0642bf {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f5062a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    int f5063b = h.i.a("numberOfGraphs", h.i.f12273t);

    /* renamed from: c, reason: collision with root package name */
    int f5064c = h.i.a("numberOfOverlays", h.i.f12274u);

    /* renamed from: d, reason: collision with root package name */
    HashMap f5065d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    C0584B f5066e = new C0584B(this);

    public C0583A() {
        C0839f.a().a(new C0585C(this));
    }

    protected void a(String str, String str2, int i2) {
        Iterator it = this.f5062a.iterator();
        while (it.hasNext()) {
            ((InterfaceC0640bd) it.next()).a(str, str2, i2);
        }
    }

    protected void a(String str, C0184j c0184j, int i2) {
        Iterator it = this.f5062a.iterator();
        while (it.hasNext()) {
            ((InterfaceC0640bd) it.next()).a(str, c0184j, i2);
        }
    }

    private List a(int i2) {
        String strC = c(i2);
        List copyOnWriteArrayList = (List) this.f5065d.get(strC);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList();
        }
        if (copyOnWriteArrayList.isEmpty()) {
            for (int i3 = 0; i3 < this.f5064c; i3++) {
                copyOnWriteArrayList.add(this.f5066e);
            }
            this.f5065d.put(strC, copyOnWriteArrayList);
        }
        return copyOnWriteArrayList;
    }

    private int b(int i2) {
        List listA = a(i2);
        for (int i3 = 0; i3 < this.f5064c; i3++) {
            if (i3 >= listA.size() || ((C0584B) listA.get(i3)).a()) {
                return i3;
            }
        }
        return -1;
    }

    private int c() {
        for (int i2 = 0; i2 < this.f5063b; i2++) {
            List listA = a(i2);
            for (int i3 = 0; i3 < this.f5064c; i3++) {
                if (i3 >= listA.size() || ((C0584B) listA.get(i3)).a()) {
                    return i2;
                }
            }
        }
        return -1;
    }

    private String c(int i2) {
        return "graph" + i2;
    }

    private String a(int i2, int i3) {
        return "graph" + i2 + "." + i3;
    }

    public Color a(String str) {
        return a(str, (Color) null);
    }

    public Color a(String str, Color color) {
        int iC = c();
        if (iC >= 0) {
            return a(str, color, iC);
        }
        return null;
    }

    public Color a(String str, Color color, int i2) {
        int i3;
        int iB = b(i2);
        while (true) {
            i3 = iB;
            if (i3 >= 0) {
                break;
            }
            i2++;
            if (i2 >= this.f5063b) {
                break;
            }
            iB = b(i2);
        }
        return a(str, color, i2, i3);
    }

    public Color a(String str, Color color, int i2, int i3) {
        if (i2 == -1) {
            i2 = this.f5063b - 1;
        }
        if (i3 == -1 || i3 >= this.f5064c) {
            i3 = this.f5064c - 1;
        }
        if (color == null) {
            color = aO.a().a(i2, i3);
        } else {
            aO.a().a(color, i2, i3);
        }
        a(c(i2), C0804hg.a().r().a(str), i3);
        a(str, a(i2, i3));
        List listA = a(i2);
        C0584B c0584b = new C0584B(this);
        c0584b.f5069c = str;
        c0584b.f5067a = i2;
        c0584b.f5068b = i3;
        listA.set(i3, c0584b);
        return color;
    }

    public void b(String str) {
        for (int i2 = 0; i2 < this.f5063b; i2++) {
            List listA = a(i2);
            for (int i3 = 0; i3 < this.f5064c; i3++) {
                if (i3 < listA.size() && listA.get(i3) != null && ((C0584B) listA.get(i3)).f5069c.equals(str)) {
                    listA.set(i3, this.f5066e);
                    a(" ", a(i2, i3));
                    a(c(i2), str, i3);
                }
            }
        }
    }

    public void a(InterfaceC0640bd interfaceC0640bd) {
        this.f5062a.add(interfaceC0640bd);
    }

    public void b(InterfaceC0640bd interfaceC0640bd) {
        this.f5062a.remove(interfaceC0640bd);
    }

    @Override // ao.InterfaceC0642bf
    public void a() {
        C0804hg c0804hgA = C0804hg.a();
        for (String str : (String[]) this.f5065d.keySet().toArray(new String[this.f5065d.keySet().size()])) {
            Iterator it = ((List) this.f5065d.get(str)).iterator();
            while (it.hasNext()) {
                b(((C0584B) it.next()).f5069c);
            }
        }
        this.f5065d.clear();
        String strG = C0839f.a().g();
        C0836c c0836cC = strG != null ? C0839f.a().c(strG) : null;
        if (c0836cC == null || c0804hgA.r() == null) {
            return;
        }
        for (int i2 = 0; i2 < this.f5063b; i2++) {
            for (int i3 = 0; i3 < this.f5064c; i3++) {
                String strC = c0836cC.c("graph" + i2 + "." + i3);
                if (!strC.equals(" ")) {
                    if (strC.indexOf("Field.") != -1) {
                        strC = h.g.a().a(strC);
                    }
                    if (c0804hgA.r().a(strC) != null) {
                        a(strC, null, i2, i3);
                    }
                }
            }
        }
    }

    protected void a(String str, String str2) {
        C0184j c0184jA = C0804hg.a().r().a(str);
        String strE = h.g.a().e(str);
        if (strE != null && !strE.equals("")) {
            String str3 = "Field." + strE;
        }
        C0641be.a().a(str2, c0184jA);
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color) {
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color, int i2) {
    }

    @Override // ao.InterfaceC0642bf
    public void b() {
        for (int i2 = 0; i2 < this.f5063b; i2++) {
            List listA = a(i2);
            for (int i3 = 0; i3 < this.f5064c; i3++) {
                if (i3 < listA.size() && listA.get(i3) != null) {
                    String str = ((C0584B) listA.get(i3)).f5069c;
                    listA.set(i3, this.f5066e);
                    a(" ", a(i2, i3));
                    a(c(i2), str, i3);
                }
            }
        }
    }
}
