package bR;

import G.F;
import G.aI;
import bH.C;
import bH.C0995c;
import bN.t;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bR/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    bQ.e f7503a;

    /* renamed from: b, reason: collision with root package name */
    aI f7504b;

    /* renamed from: c, reason: collision with root package name */
    bQ.l f7505c;

    /* renamed from: d, reason: collision with root package name */
    byte[] f7506d = null;

    /* renamed from: e, reason: collision with root package name */
    long f7507e = 0;

    /* renamed from: f, reason: collision with root package name */
    int f7508f = 0;

    /* renamed from: g, reason: collision with root package name */
    int f7509g = 0;

    public c(aI aIVar, bQ.l lVar, bQ.e eVar) {
        this.f7503a = eVar;
        this.f7504b = aIVar;
        this.f7505c = lVar;
    }

    public void a() {
        this.f7503a.g();
    }

    public boolean a(bN.k kVar, t tVar) {
        int iA = tVar.a();
        if (!bN.h.a(iA)) {
            return false;
        }
        for (bO.c cVar : this.f7503a.e()) {
            if (iA >= cVar.k() && iA <= cVar.k() + cVar.f()) {
                try {
                    bO.k kVarC = this.f7503a.c(iA);
                    if (kVarC.b() != tVar.c().length) {
                        C.c("Unexpected ODT byte count: " + kVarC.b() + ", expected: " + tVar.c().length);
                        int i2 = this.f7509g;
                        this.f7509g = i2 + 1;
                        if (i2 > 3) {
                            C.d(this.f7509g + ", DAQ Errors. Restarting...");
                            this.f7509g = 0;
                            a();
                        }
                    }
                    return kVarC.b() == tVar.c().length;
                } catch (bO.j e2) {
                    return false;
                }
            }
        }
        return false;
    }

    public void a(F f2, bN.k kVar, t tVar) throws bT.h {
        if (this.f7506d == null) {
            this.f7506d = new byte[f2.n()];
        }
        int iA = tVar.a();
        try {
            bO.k kVarC = this.f7503a.c(iA);
            byte[] bArrC = tVar.c();
            if (kVarC.b() != bArrC.length) {
                int i2 = this.f7509g;
                this.f7509g = i2 + 1;
                if (i2 > 3) {
                    if (this.f7505c.a(f2.u()).d()) {
                        this.f7505c.a(f2.u()).c();
                        C.d("ODT, Expected " + kVarC.b() + " received: " + bArrC.length + ", Sending update to Slave");
                        return;
                    } else {
                        String str = "DAQ_HANDLER: ODT Size wrong, Expected " + kVarC.b() + " received: " + bArrC.length;
                        C.c(str);
                        throw new bT.h(str);
                    }
                }
                return;
            }
            byte[] bArrF = ((bN.m) tVar).f();
            if (bArrF.length > 0) {
                int iA2 = C0995c.a(bArrF[0]);
                if (this.f7508f < iA2) {
                    this.f7507e += iA2 - this.f7508f;
                } else if (this.f7508f > iA2) {
                    this.f7507e = this.f7507e + (256 - this.f7508f) + iA2;
                }
                this.f7508f = iA2;
            }
            this.f7509g = 0;
            int iB = 0;
            Iterator it = kVarC.iterator();
            while (it.hasNext()) {
                bO.l lVar = (bO.l) it.next();
                int i3 = 0 + iB;
                for (bO.i iVar : lVar.c()) {
                    System.arraycopy(bArrC, i3 + iVar.c(), this.f7506d, iVar.a(), iVar.b());
                }
                iB += lVar.b();
            }
            kVarC.a(true);
            bO.c cVarA = this.f7503a.a(kVarC);
            if (cVarA == null) {
                C.b("Didn't find a DAQ for PID: " + iA);
            } else {
                boolean z2 = true;
                Iterator it2 = cVarA.e().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    bO.k kVar2 = (bO.k) it2.next();
                    if (!kVar2.isEmpty() && !kVar2.a()) {
                        z2 = false;
                        break;
                    }
                }
                if (z2) {
                    this.f7505c.a(this.f7506d, this.f7507e / 1000.0f);
                    Iterator it3 = cVarA.e().iterator();
                    while (it3.hasNext()) {
                        ((bO.k) it3.next()).a(false);
                    }
                }
            }
        } catch (bO.j e2) {
            Logger.getLogger(c.class.getName()).log(Level.WARNING, "Could not update ODT offsets.", (Throwable) e2);
        }
    }
}
