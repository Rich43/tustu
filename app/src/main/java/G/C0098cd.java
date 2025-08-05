package G;

import bH.C0995c;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: G.cd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cd.class */
public class C0098cd implements Serializable {

    /* renamed from: d, reason: collision with root package name */
    private int f1109d = 2;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f1110a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    ArrayList f1111b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f1112c = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    private int f1113e = 0;

    /* renamed from: f, reason: collision with root package name */
    private int f1114f = 0;

    /* renamed from: g, reason: collision with root package name */
    private int f1115g = 0;

    public void a(C0097cc c0097cc) {
        this.f1110a.add(c0097cc);
    }

    public void a(C0097cc c0097cc, int i2) {
        this.f1110a.add(i2, c0097cc);
    }

    public void a(C0095ca c0095ca) {
        this.f1110a.add(c0095ca);
    }

    public void b(C0097cc c0097cc) {
        this.f1111b.add(c0097cc);
    }

    public void c(C0097cc c0097cc) {
        this.f1112c.add(c0097cc);
    }

    public C0097cc a(int i2) {
        return (C0097cc) this.f1110a.get(i2);
    }

    public String[] a() {
        String[] strArr = new String[this.f1110a.size()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = ((C0097cc) this.f1110a.get(i2)).b();
        }
        return strArr;
    }

    public String[] b() {
        String[] strArr = new String[this.f1110a.size()];
        for (int i2 = 0; i2 < this.f1110a.size(); i2++) {
            strArr[i2] = ((C0097cc) this.f1110a.get(i2)).g();
        }
        return strArr;
    }

    public double a(int i2, int[] iArr, int i3) {
        return ((C0097cc) this.f1110a.get(i2)).a(a(iArr, i3));
    }

    public double a(int i2, int[] iArr) {
        C0097cc c0097cc = (C0097cc) this.f1111b.get(i2);
        return c0097cc.a(C0995c.a(iArr, c0097cc.e(), c0097cc.f(), true, false));
    }

    public C0097cc b(int i2) {
        return (C0097cc) this.f1111b.get(i2);
    }

    public double b(int i2, int[] iArr) {
        C0097cc c0097cc = (C0097cc) this.f1112c.get(i2);
        return c0097cc.a(C0995c.a(iArr, c0097cc.e(), e(), true, false));
    }

    public C0097cc c(int i2) {
        return (C0097cc) this.f1112c.get(i2);
    }

    public C0097cc a(String str) {
        int iB = b(str);
        if (iB == -1) {
            return null;
        }
        return a(iB);
    }

    public int b(String str) {
        for (int i2 = 0; i2 < this.f1110a.size(); i2++) {
            if (((C0097cc) this.f1110a.get(i2)).b().equals(str)) {
                return i2;
            }
        }
        return -1;
    }

    public BigInteger a(int[] iArr, int i2) {
        return C0995c.a(iArr, (((i2 + (h() / c())) % (((iArr.length - e()) - g()) / c())) * c()) + e(), c(), true, false);
    }

    public int c() {
        return this.f1109d;
    }

    public void d(int i2) {
        this.f1109d = i2;
    }

    public int d() {
        return this.f1112c.size();
    }

    public int e() {
        return this.f1113e;
    }

    public void e(int i2) {
        this.f1113e = i2;
    }

    public int f() {
        return this.f1111b.size();
    }

    public int g() {
        return this.f1114f;
    }

    public void f(int i2) {
        this.f1114f = i2;
    }

    public int h() {
        return this.f1115g;
    }

    public void g(int i2) {
        this.f1115g = i2;
    }

    public int i() {
        return this.f1110a.size();
    }

    public List j() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f1110a.iterator();
        while (it.hasNext()) {
            C0097cc c0097cc = (C0097cc) it.next();
            if (!c0097cc.i()) {
                arrayList.add(c0097cc.g());
            }
        }
        return arrayList;
    }

    public void k() {
        this.f1110a.clear();
    }
}
