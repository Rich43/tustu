package G;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: G.an, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/an.class */
public class C0054an implements aG, InterfaceC0042ab, InterfaceC0044ad, InterfaceC0071bd, InterfaceC0124g, Serializable {

    /* renamed from: a, reason: collision with root package name */
    R f782a;

    /* renamed from: q, reason: collision with root package name */
    private C0062av f797q;

    /* renamed from: b, reason: collision with root package name */
    boolean f783b = false;

    /* renamed from: c, reason: collision with root package name */
    List f784c = Collections.synchronizedList(new ArrayList());

    /* renamed from: d, reason: collision with root package name */
    List f785d = Collections.synchronizedList(new ArrayList());

    /* renamed from: e, reason: collision with root package name */
    List f786e = Collections.synchronizedList(new ArrayList());

    /* renamed from: f, reason: collision with root package name */
    List f787f = Collections.synchronizedList(new ArrayList());

    /* renamed from: g, reason: collision with root package name */
    final List f788g = Collections.synchronizedList(new ArrayList());

    /* renamed from: m, reason: collision with root package name */
    private long f789m = -1;

    /* renamed from: n, reason: collision with root package name */
    private final long f790n = 20000;

    /* renamed from: h, reason: collision with root package name */
    boolean f791h = false;

    /* renamed from: i, reason: collision with root package name */
    boolean f792i = false;

    /* renamed from: j, reason: collision with root package name */
    int f793j = 0;

    /* renamed from: k, reason: collision with root package name */
    int f794k = 15;

    /* renamed from: o, reason: collision with root package name */
    private boolean f795o = true;

    /* renamed from: p, reason: collision with root package name */
    private final ArrayList f796p = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    boolean f798l = false;

    public C0054an(R r2) {
        this.f782a = null;
        this.f797q = null;
        this.f782a = r2;
        this.f797q = new C0062av(this);
        this.f797q.start();
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) throws IOException {
        if (this.f782a != null && this.f782a.c().equals(str) && !this.f782a.i().equals(bSVar.b()) && !a(str, this.f782a.i(), bSVar)) {
            return false;
        }
        if (!this.f782a.c().equals(str)) {
            return true;
        }
        if (System.currentTimeMillis() - this.f789m <= 20000 && !this.f795o) {
            return true;
        }
        h();
        return true;
    }

    private void g() {
        new C0055ao(this, "delayed full read").start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() throws IOException {
        i();
        C0130m c0130mG = C0130m.g(this.f782a.O());
        c0130mG.b(new C0056ap(this));
        Iterator it = this.f784c.iterator();
        while (it.hasNext()) {
            c0130mG.b((InterfaceC0131n) it.next());
        }
        a(c0130mG);
        k();
        this.f793j++;
    }

    private void i() {
        J jC = this.f782a.C();
        Y yH = this.f782a.h();
        for (int i2 = 0; i2 < yH.e(); i2++) {
            int[] iArrB = yH.b(i2);
            int[] iArrA = jC.a(this.f782a.c(), i2);
            for (int i3 = 0; i3 < iArrB.length; i3++) {
                if (iArrB[i3] <= 255 && iArrB[i3] >= -128) {
                    iArrA[i3] = iArrB[i3];
                }
            }
        }
    }

    @Override // G.aG
    public void a(String str) {
        this.f788g.clear();
        this.f789m = System.currentTimeMillis();
    }

    public void a(C0132o c0132o) throws IOException {
        if (c0132o.a() != 1) {
            if (c0132o.a() == 3) {
                a(false);
                aB.a().c();
                if (this.f793j < 3) {
                    bH.C.c("result.FAILED: " + this.f793j + " tryCount");
                    g();
                    return;
                } else {
                    aB.a().b(this.f782a.c(), "Error reading data from " + c0132o.f() + " after " + this.f793j + " attempts\nError message returned:\n" + c0132o.c() + "\n\nApplication will now go offline. To attempt going back online\ncheck the Project menu when connection problems are resolved.");
                    this.f782a.C().c();
                    this.f793j = 0;
                    return;
                }
            }
            return;
        }
        int[][] iArrD = c0132o.d();
        aB.a().a("Validating Data", 0.92d);
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.f782a.h().h()) {
            aB.a().c();
            this.f782a.h().b(this);
            for (int i2 = 0; i2 < iArrD.length; i2++) {
                try {
                    this.f782a.h().a(i2, 0, iArrD[i2], false);
                    this.f782a.h().g();
                } catch (V.g e2) {
                    e2.printStackTrace();
                    aB.a().b(this.f782a.c(), "Data size from Controller does not match configuration!\nCan not continue in this condition.\nGoing offline.");
                    this.f782a.C().c();
                    return;
                }
            }
            this.f782a.h().g();
            this.f795o = false;
        } else {
            bH.C.c("isBlank Time: " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms.");
            if (iArrD.length == 0) {
                aB.a().b(this.f782a.c(), "Error trying to read data from Controller.");
                this.f782a.C().c();
            }
            aB.a().a("Performing Difference Report", 0.94d);
            Y y2 = new Y(this.f782a, iArrD);
            y2.a(this.f782a.p().b());
            y2.b(true);
            ArrayList arrayListA = null;
            try {
                arrayListA = new C0143z(bH.ab.a().b()).a(this.f782a, this.f782a.h(), y2);
            } catch (Exception e3) {
                aB.a().b(this.f782a.c(), "Error trying to compare data from Controller:\n" + e3.getMessage());
                e3.printStackTrace();
                this.f782a.C().c();
            }
            bH.C.c("DiffTime: " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms.");
            if (arrayListA.size() > 0) {
                aB.a().a("Generating Difference Report, Please Wait.", 95.0d);
                boolean zA = a(arrayListA, this.f782a, y2, "The Controller");
                aB.a().c();
                if (zA) {
                    h();
                }
            } else {
                aB.a().c();
                for (int i3 = 0; i3 < iArrD.length; i3++) {
                    try {
                        this.f782a.h().b(i3, 0, iArrD[i3]);
                    } catch (V.g e4) {
                        e4.printStackTrace();
                    }
                }
                this.f795o = false;
            }
        }
        this.f793j = 0;
        this.f782a.h().a((InterfaceC0042ab) this);
        a(true);
        aL.a(this.f782a);
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        if (i2 < 0) {
            return;
        }
        this.f795o = true;
        if (c(Thread.currentThread()) || this.f791h || this.f782a.C() == null || !this.f782a.C().q()) {
            return;
        }
        a(i2, i3, iArr.length);
    }

    private boolean c(Thread thread) {
        return this.f796p.contains(thread);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(int i2) {
        Iterator it = this.f788g.iterator();
        while (it.hasNext()) {
            if (((C0063aw) it.next()).a() == i2) {
                return true;
            }
        }
        return false;
    }

    private void a(int i2, int i3, int i4) {
        if (this.f797q != null) {
            this.f797q.b();
        }
        C0103ci c0103ciA = this.f782a.a(i2, i3);
        C0103ci c0103ciB = this.f782a.b(i2, i3);
        if (this.f794k > 1 && this.f782a.O().aC() == null) {
            this.f794k = 1;
        }
        int i5 = i3 + i4;
        if (c0103ciA != null && i3 + i4 > c0103ciA.a()) {
            int iA = c0103ciA.a() - i3;
            if (iA > 0) {
                a(i2, i3, iA);
            }
            a(i2, c0103ciA.a(), i4 - iA);
            return;
        }
        if (c0103ciB != null && (c0103ciB.a() + c0103ciB.b()) - 1 > i3) {
            int iMin = Math.min((c0103ciB.a() + c0103ciB.b()) - i3, i4);
            C0063aw c0063awB = b(i2, c0103ciB.a(), c0103ciB.b());
            bH.C.c("In Protected DirtyData. page: " + i2 + ", start: " + i3 + ", end: " + i4);
            if (c0063awB != null) {
                c0063awB.b(Math.min(i3, c0063awB.b()));
                c0063awB.c(Math.max((i3 + iMin) - 1, c0063awB.c()));
                bH.C.c("updating a protected region range");
            } else {
                c(i2, i3, iMin);
                bH.C.c("Writing to protected region: " + i3 + CallSiteDescriptor.TOKEN_DELIMITER + i4);
            }
            if (i4 > iMin) {
                a(i2, c0103ciB.a() + c0103ciB.b(), i4 - iMin);
                bH.C.c("Writing to unprotected region above protected");
                return;
            }
            return;
        }
        int iA2 = c0103ciA == null ? Integer.MAX_VALUE : c0103ciA.a();
        int iA3 = c0103ciB == null ? -1 : (c0103ciB.a() + c0103ciB.b()) - 1;
        synchronized (this.f788g) {
            for (C0063aw c0063aw : this.f788g) {
                if (c0063aw.a() == i2 && i3 > iA3 && (i3 + i4) - 1 < iA2 && a(c0063aw, i3, i4) < this.f794k) {
                    c0063aw.b(Math.min(c0063aw.b(), i3));
                    c0063aw.c(Math.max(c0063aw.c(), i5));
                    for (C0063aw c0063aw2 : this.f788g) {
                        if (!c0063aw2.equals(c0063aw) && c0063aw2.a() == c0063aw.a() && iA2 > c0063aw.c() && iA2 > c0063aw2.c() && iA3 < c0063aw.b() && iA3 < c0063aw2.b() && (c0063aw2.c() - c0063aw.b() < this.f794k || c0063aw.c() - c0063aw2.b() < this.f794k)) {
                            c0063aw.b(Math.min(c0063aw.b(), c0063aw2.b()));
                            c0063aw.c(Math.max(c0063aw.c(), c0063aw2.c()));
                            this.f788g.remove(c0063aw2);
                            break;
                        }
                    }
                    j();
                    return;
                }
            }
            c(i2, i3, i4);
        }
    }

    private C0063aw b(int i2, int i3, int i4) {
        for (C0063aw c0063aw : this.f788g) {
            if (c0063aw.a() == i2 && c0063aw.b() <= i3 && c0063aw.c() >= (i3 + i4) - 1) {
                return c0063aw;
            }
        }
        return null;
    }

    private void j() {
        for (int i2 = 0; i2 < this.f788g.size(); i2++) {
            C0063aw c0063aw = (C0063aw) this.f788g.get(i2);
            int i3 = 0;
            while (i3 < this.f788g.size()) {
                C0063aw c0063aw2 = (C0063aw) this.f788g.get(i3);
                if (c0063aw.a() == c0063aw2.a() && !c0063aw.equals(c0063aw2) && a(c0063aw, c0063aw2)) {
                    c0063aw.b(Math.min(c0063aw.b(), c0063aw2.b()));
                    c0063aw.c(Math.max(c0063aw.c(), c0063aw2.c()));
                    this.f788g.remove(i3);
                    i3--;
                }
                i3++;
            }
        }
    }

    private int a(C0063aw c0063aw, int i2, int i3) {
        int iB = (c0063aw.b() - ((i2 + i3) - 1)) - 1;
        int iC = (i2 - c0063aw.c()) - 1;
        if (iC >= 0 || iB >= 0) {
            return Math.max(iB, iC);
        }
        return 0;
    }

    private boolean a(C0063aw c0063aw, C0063aw c0063aw2) {
        return (c0063aw2.b() - c0063aw.c()) - 1 < 0 && (c0063aw.b() - c0063aw2.c()) - 1 < 0;
    }

    private void c(int i2, int i3, int i4) {
        C0063aw c0063aw = new C0063aw(this);
        c0063aw.a(i2);
        this.f788g.add(c0063aw);
        c0063aw.b(i3);
        c0063aw.c(i3 + i4);
    }

    public void a(C0130m c0130m) throws IOException {
        this.f782a.C().b(c0130m);
    }

    public void a(InterfaceC0131n interfaceC0131n) {
        this.f784c.add(interfaceC0131n);
    }

    public void b(InterfaceC0131n interfaceC0131n) {
        this.f784c.remove(interfaceC0131n);
    }

    public void a(cU cUVar) {
        this.f785d.add(cUVar);
    }

    public void b(cU cUVar) {
        this.f785d.remove(cUVar);
    }

    private boolean a(String str, String str2, bS bSVar) {
        Iterator it = this.f785d.iterator();
        while (it.hasNext()) {
            if (!((cU) it.next()).a(str, str2, bSVar)) {
                return false;
            }
        }
        return true;
    }

    private C0063aw[] a(C0063aw[] c0063awArr) {
        for (int i2 = 0; i2 < c0063awArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < c0063awArr.length; i3++) {
                C0063aw c0063aw = c0063awArr[i2];
                C0063aw c0063aw2 = c0063awArr[i3];
                if (a(c0063aw) > a(c0063aw2)) {
                    c0063awArr[i2] = c0063aw2;
                    c0063awArr[i3] = c0063aw;
                }
            }
        }
        return c0063awArr;
    }

    private int a(C0063aw c0063aw) {
        return (c0063aw.a() * 16777216) + c0063aw.b();
    }

    protected synchronized void c() throws IOException {
        C0063aw[] c0063awArr;
        if (this.f788g.isEmpty() || this.f798l) {
            return;
        }
        synchronized (this.f788g) {
            c0063awArr = (C0063aw[]) this.f788g.toArray(new C0063aw[this.f788g.size()]);
            this.f788g.clear();
        }
        a(c0063awArr);
        for (int i2 = 0; i2 < c0063awArr.length; i2++) {
            int iG = this.f782a.O().G(c0063awArr[i2].a()) - this.f782a.O().am();
            if (iG <= 0 || c0063awArr[i2].c() - c0063awArr[i2].b() <= iG) {
                b(c0063awArr[i2]);
            } else {
                while (c0063awArr[i2].c() - c0063awArr[i2].b() > 0) {
                    int iB = c0063awArr[i2].b();
                    int iC = c0063awArr[i2].c() - iB > iG ? iB + iG : c0063awArr[i2].c();
                    C0063aw c0063aw = new C0063aw(this);
                    c0063aw.a(c0063awArr[i2].a());
                    c0063aw.b(iB);
                    c0063aw.c(iC);
                    b(c0063aw);
                    c0063awArr[i2].b(iC);
                }
            }
        }
        if (this.f792i) {
            this.f782a.I();
            this.f792i = false;
        }
    }

    private void b(C0063aw c0063aw) throws IOException {
        int[] iArrA = this.f782a.p().a(c0063aw.a(), c0063aw.b(), c0063aw.c() - c0063aw.b());
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= iArrA.length) {
                break;
            }
            while (i3 < iArrA.length && iArrA[i3] == Integer.MIN_VALUE) {
                i3++;
            }
            int i4 = i3;
            while (i4 < iArrA.length && iArrA[i4] != Integer.MIN_VALUE) {
                i4++;
            }
            if (i4 > i3) {
                C0063aw c0063aw2 = new C0063aw(this);
                c0063aw2.a(c0063aw.a());
                c0063aw2.b(i3 + c0063aw.b());
                c0063aw2.c(i4 + c0063aw.b());
                arrayList.add(c0063aw2);
            }
            i2 = i4;
        }
        if (arrayList.isEmpty()) {
            return;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C0063aw c0063aw3 = (C0063aw) it.next();
            int[] iArr = new int[c0063aw3.c() - c0063aw3.b()];
            System.arraycopy(iArrA, c0063aw3.b() - c0063aw.b(), iArr, 0, c0063aw3.c() - c0063aw3.b());
            if (this.f782a.O().j(c0063aw3.a()) != null) {
                C0130m c0130mA = C0130m.a(this.f782a.O(), c0063aw3.a(), c0063aw3.b(), iArr);
                if (iArr.length > 25) {
                    if (!b(c0130mA)) {
                        aB.a().a(this.f782a.c(), "Large Write");
                        bH.C.c("page: " + (c0130mA.o() + 1) + " offset: " + c0130mA.q() + ", len: " + c0130mA.r());
                    }
                    c0130mA.b(new C0057aq(this));
                } else if (iArr.length > 0) {
                    if (!b(c0130mA)) {
                        aB.a().a(this.f782a.c(), "Writing to Controller");
                    }
                    c0130mA.b(new C0058ar(this));
                }
                a(c0130mA);
            } else if (this.f782a.O().i(c0063aw3.a()) != null) {
                for (int i5 = 0; i5 < iArr.length; i5++) {
                    C0130m c0130mA2 = C0130m.a(this.f782a.O(), c0063aw3.a(), c0063aw3.b() + i5, iArr[i5]);
                    if (i5 == 0) {
                        if (!b(c0130mA2)) {
                            aB.a().a(this.f782a.c(), "Writing to Controller");
                        }
                        if (iArr.length > 20) {
                            c0130mA2.b(new C0059as(this));
                        }
                    }
                    if (i5 == iArr.length - 1) {
                        c0130mA2.b(new C0060at(this));
                    }
                    a(c0130mA2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(C0132o c0132o) {
        if (c0132o.b() == null || c0132o.b().v() == null) {
            return false;
        }
        return b(c0132o.b());
    }

    private boolean b(C0130m c0130m) {
        try {
            F fV = c0130m.v();
            if (fV.aa() == null) {
                return false;
            }
            aM aMVarC = T.a().c(fV.u()).c(fV.aa());
            if ((c0130m.n() == 5 || c0130m.n() == 4) && c0130m.o() == aMVarC.d() && c0130m.q() >= aMVarC.g()) {
                return c0130m.r() <= aMVarC.y();
            }
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public void d() {
        c();
    }

    public void e() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.f797q != null && this.f797q.isAlive()) {
            this.f797q.a();
        }
        bH.C.c("Flush lasted: " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
    }

    @Override // G.InterfaceC0124g
    public void b(String str, int i2) throws IOException {
        if (!this.f782a.O().v() || this.f782a.C().c(this.f782a.O(), i2) || !str.equals(this.f782a.c()) || this.f782a.C().c(i2)) {
            return;
        }
        C0130m c0130mD = C0130m.d(this.f782a.O(), i2);
        c0130mD.b(new C0061au(this));
        a(c0130mD);
    }

    private boolean a(ArrayList arrayList, R r2, Y y2, String str) {
        Iterator it = this.f786e.iterator();
        while (it.hasNext()) {
            if (((InterfaceC0120cz) it.next()).a(arrayList, r2, y2, str)) {
                return true;
            }
        }
        return false;
    }

    private void k() {
        Iterator it = this.f787f.iterator();
        while (it.hasNext()) {
            try {
                ((bT) it.next()).a();
            } catch (Exception e2) {
                bH.C.d("Caught Exception notifying of fullread, continued.");
                e2.printStackTrace();
            }
        }
    }

    private void a(boolean z2) {
        Iterator it = this.f787f.iterator();
        while (it.hasNext()) {
            try {
                ((bT) it.next()).a(z2);
            } catch (Exception e2) {
                bH.C.d("Caught Exception notifying of fullread, continued.");
                e2.printStackTrace();
            }
        }
    }

    public void a(InterfaceC0120cz interfaceC0120cz) {
        this.f786e.add(interfaceC0120cz);
    }

    public void b(InterfaceC0120cz interfaceC0120cz) {
        this.f786e.remove(interfaceC0120cz);
    }

    public void a(bT bTVar) {
        this.f787f.add(bTVar);
    }

    public void b(bT bTVar) {
        this.f787f.remove(bTVar);
    }

    @Override // G.InterfaceC0044ad
    public void a() {
        this.f795o = true;
    }

    @Override // G.InterfaceC0044ad
    public void b() {
        this.f795o = true;
    }

    void a(Thread thread) {
        this.f796p.add(thread);
    }

    void b(Thread thread) {
        this.f796p.remove(thread);
    }

    @Override // G.InterfaceC0124g
    public void a(String str, boolean z2) {
    }

    void f() {
        this.f797q.c();
    }

    @Override // G.InterfaceC0124g
    public void a(String str, int i2) {
    }
}
