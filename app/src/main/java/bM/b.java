package bM;

import G.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bM/b.class */
public class b {

    /* renamed from: a, reason: collision with root package name */
    R f7205a;

    /* renamed from: b, reason: collision with root package name */
    List f7206b = new ArrayList();

    public b(R r2) {
        this.f7205a = r2;
    }

    public void a(a aVar) {
        if (aVar.f()) {
            return;
        }
        c cVarD = d((int) Math.round(aVar.e()));
        cVarD.a(aVar.d());
        cVarD.c(aVar.c());
        cVarD.b(aVar.a() * (aVar.c() / aVar.d()) * (aVar.b() / 100.0d));
    }

    public double a(int i2) {
        if (i2 >= b() && i2 <= a()) {
            return c(i2).c();
        }
        if (i2 > a()) {
            return c(a()).c() + ((i2 - a()) * b(i2));
        }
        double dB = b(i2);
        return c(b()).c() + ((i2 - b()) * (dB > 0.0d ? 0.0d : dB));
    }

    public double b(int i2) {
        if (this.f7206b.size() < 2) {
            return 0.0d;
        }
        int iF = f(i2);
        int iF2 = iF;
        if (iF != -1) {
            while (iF > 0 && ((c) this.f7206b.get(iF - 1)).d() > i2 - 5) {
                iF--;
            }
            while (iF2 < this.f7206b.size() - 1 && ((c) this.f7206b.get(iF2 + 1)).d() < i2 + 5) {
                iF2++;
            }
        } else if (i2 < b()) {
            iF = f(b());
            iF2 = iF;
            while (iF2 < this.f7206b.size() - 1 && ((c) this.f7206b.get(iF2 + 1)).d() < b() + 15) {
                iF2++;
            }
        } else if (i2 > a()) {
            iF2 = f(a());
            iF = iF2;
            while (iF > 0 && ((c) this.f7206b.get(iF - 1)).d() > a() - 15) {
                iF--;
            }
        }
        int iD = ((c) this.f7206b.get(iF)).d();
        return (c(((c) this.f7206b.get(iF2)).d()).c() - c(iD).c()) / (r0 - iD);
    }

    public int a() {
        if (this.f7206b.size() > 0) {
            return ((c) this.f7206b.get(this.f7206b.size() - 1)).d();
        }
        return Integer.MIN_VALUE;
    }

    public int b() {
        if (this.f7206b.size() > 0) {
            return ((c) this.f7206b.get(0)).d();
        }
        return Integer.MAX_VALUE;
    }

    public c c(int i2) {
        int iF = f(i2);
        if (iF < 0) {
            return null;
        }
        int i3 = 7;
        while (true) {
            if (iF - i3 >= 0 && iF + i3 <= this.f7206b.size() - 1) {
                break;
            }
            i3--;
        }
        c cVar = (c) this.f7206b.get(iF);
        c cVar2 = new c(i2);
        cVar2.a(cVar.b());
        double dC = 0.0d;
        int i4 = 0;
        double dE = 0.0d;
        for (int i5 = iF - i3; i5 <= iF + i3; i5++) {
            int iA = ((c) this.f7206b.get(i5)).a();
            i4 += iA;
            dC += ((c) this.f7206b.get(i5)).c() * iA;
            dE += ((c) this.f7206b.get(i5)).e() * iA;
        }
        cVar2.f7210b = dC / i4;
        cVar2.f7207a = i4;
        cVar2.d(dE / i4);
        return cVar2;
    }

    public c d(int i2) {
        for (int i3 = 0; i3 < this.f7206b.size(); i3++) {
            c cVar = (c) this.f7206b.get(i3);
            if (cVar.d() == i2) {
                return cVar;
            }
            if (cVar.d() > i2) {
                c cVar2 = new c(i2);
                cVar2.a(i2);
                this.f7206b.add(i3, cVar2);
                return cVar2;
            }
        }
        c cVar3 = new c(i2);
        this.f7206b.add(cVar3);
        return cVar3;
    }

    public int e(int i2) {
        return d(i2).a();
    }

    private int f(int i2) {
        if (this.f7206b.size() <= 0 || ((c) this.f7206b.get(0)).d() > i2) {
            return -1;
        }
        for (int i3 = 0; i3 < this.f7206b.size(); i3++) {
            if (((c) this.f7206b.get(i3)).d() >= i2) {
                return i3;
            }
        }
        return -1;
    }

    public void c() {
        this.f7206b.clear();
    }
}
