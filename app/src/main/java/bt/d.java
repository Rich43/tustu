package bT;

import G.F;
import G.R;
import bH.C;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bT/d.class */
public class d {

    /* renamed from: i, reason: collision with root package name */
    private bN.r f7570i;

    /* renamed from: c, reason: collision with root package name */
    F f7572c;

    /* renamed from: d, reason: collision with root package name */
    R f7573d;

    /* renamed from: e, reason: collision with root package name */
    private final bO.a f7565e = new bO.a();

    /* renamed from: f, reason: collision with root package name */
    private int f7566f = -1;

    /* renamed from: g, reason: collision with root package name */
    private int f7567g = -1;

    /* renamed from: h, reason: collision with root package name */
    private int f7568h = -1;

    /* renamed from: a, reason: collision with root package name */
    List f7569a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f7571b = new ArrayList();

    public d(R r2, bN.r rVar) {
        this.f7573d = r2;
        this.f7572c = r2.O();
        this.f7570i = rVar;
    }

    public bO.a a() {
        return this.f7565e;
    }

    public void a(bO.c cVar, int i2) {
        while (this.f7569a.size() <= i2) {
            b bVar = new b(this.f7572c, this.f7570i, this.f7565e);
            bVar.a(new bO.c());
            this.f7573d.C().a(bVar);
            this.f7569a.add(bVar);
        }
        cVar.i(i2);
        ((b) this.f7569a.get(i2)).a(cVar);
    }

    public void a(int i2) {
        bO.c cVarB = b(i2);
        if (this.f7565e.a().c()) {
            cVarB.l();
        } else {
            cVarB.l();
        }
    }

    public int b() {
        return this.f7569a.size();
    }

    public bO.c b(int i2) {
        if (i2 < 0) {
            C.c("Invalid DAQ List number: " + i2);
            return null;
        }
        if (i2 >= this.f7565e.b().b()) {
            return null;
        }
        if (i2 >= this.f7569a.size()) {
            C.a("request for DAQ List: " + i2 + " but that list is no initialized.");
            return null;
        }
        b bVar = (b) this.f7569a.get(i2);
        if (bVar != null) {
            return bVar.a();
        }
        return null;
    }

    public b c(int i2) {
        try {
            return (b) this.f7569a.get(i2);
        } catch (IndexOutOfBoundsException e2) {
            return null;
        }
    }

    public int c() {
        return this.f7566f;
    }

    public void d(int i2) {
        this.f7566f = i2;
    }

    public int d() {
        return this.f7567g;
    }

    public void e(int i2) {
        this.f7567g = i2;
    }

    public int e() {
        return this.f7568h;
    }

    public void f(int i2) {
        this.f7568h = i2;
    }

    public bO.l a(int i2, int i3, int i4) throws bO.j {
        bO.c cVarB = b(i2);
        bO.k kVarC = cVarB.c(i3);
        if (i4 < 0 || i4 >= cVarB.c()) {
            throw new bO.j("Invalid ODT Entry Number: " + i4 + " ,max ODT Entries: " + cVarB.c());
        }
        while (i4 >= kVarC.size()) {
            kVarC.add(new bO.l());
        }
        return (bO.l) kVarC.get(i4);
    }

    public byte g(int i2) {
        int iF = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            bO.c cVarB = b(i3);
            cVarB.h(iF);
            iF += cVarB.f();
        }
        return (byte) iF;
    }

    public void h(int i2) {
        ((b) this.f7569a.get(i2)).c();
    }

    public void i(int i2) {
        ((b) this.f7569a.get(i2)).b();
    }

    public void j(int i2) {
        if (this.f7571b.contains(Integer.valueOf(i2))) {
            return;
        }
        this.f7571b.add(Integer.valueOf(i2));
    }

    public void f() {
        this.f7571b.clear();
    }

    public void g() {
        Iterator it = this.f7571b.iterator();
        while (it.hasNext()) {
            h(((Integer) it.next()).intValue());
        }
    }

    public void h() {
        Iterator it = this.f7571b.iterator();
        while (it.hasNext()) {
            i(((Integer) it.next()).intValue());
        }
    }

    public void i() {
        Iterator it = this.f7569a.iterator();
        while (it.hasNext()) {
            ((b) it.next()).b();
        }
    }
}
