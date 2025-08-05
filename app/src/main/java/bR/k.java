package bR;

import G.C0116cv;
import G.F;
import G.R;
import G.T;
import bH.C0995c;
import bN.t;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bR/k.class */
public class k {

    /* renamed from: a, reason: collision with root package name */
    l f7521a = null;

    /* renamed from: b, reason: collision with root package name */
    final List f7522b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    boolean f7523c = true;

    public void a(F f2, bN.k kVar, t tVar) {
        R rC = T.a().c(f2.u());
        if (rC == null) {
            throw new bT.h("Invalid EcuConfiguration: " + f2.u());
        }
        if (tVar.a() != 252) {
            throw new bT.h("Invalid Service Request Packet: " + tVar.toString());
        }
        byte[] bArrC = tVar.c();
        if (C0995c.a(bArrC[0]) != 224) {
            throw new bT.h("Invalid packet for Refresh Command: " + tVar.toString());
        }
        if (bArrC.length < 2) {
            throw new bT.h("Device Required: " + tVar.toString());
        }
        if (bArrC.length < 3) {
            throw new bT.h("Page Required: " + tVar.toString());
        }
        int iA = C0995c.a(bArrC[2]);
        int iA2 = -1;
        int iA3 = -1;
        C0995c.a(bArrC[2]);
        if (bArrC.length >= 5) {
            iA2 = C0995c.a(bArrC, 3, 2, kVar.g(), false);
        }
        if (bArrC.length >= 7) {
            iA3 = C0995c.a(bArrC, 5, 2, kVar.g(), false);
        }
        if (this.f7523c) {
            if (iA2 < 0 || iA3 <= 0) {
                a(rC, iA);
                return;
            } else {
                a(rC, iA, iA2, iA3);
                return;
            }
        }
        if (iA2 < 0 || iA3 <= 0) {
            C0116cv.a(rC, iA);
        } else {
            C0116cv.a(rC, iA, iA2, iA3);
        }
    }

    private void a(R r2, int i2) {
        Iterator it = this.f7522b.iterator();
        while (it.hasNext()) {
            if (((m) it.next()).a(r2, i2, 0, 0)) {
                a().a();
                return;
            }
        }
        synchronized (this.f7522b) {
            int i3 = 0;
            while (i3 < this.f7522b.size()) {
                m mVar = (m) this.f7522b.get(i3);
                if (mVar.a().equals(r2) && i2 == mVar.b()) {
                    this.f7522b.remove(i3);
                    i3--;
                }
                i3++;
            }
        }
        a().a(new m(this, r2, i2));
    }

    private void a(R r2, int i2, int i3, int i4) {
        Iterator it = this.f7522b.iterator();
        while (it.hasNext()) {
            if (((m) it.next()).a(r2, i2, i3, i4)) {
                a().a();
                return;
            }
        }
        m mVar = new m(this, r2, i2, i3, i4);
        synchronized (this.f7522b) {
            int i5 = 0;
            while (i5 < this.f7522b.size()) {
                m mVar2 = (m) this.f7522b.get(i5);
                if (mVar2.a().equals(r2) && mVar.a(r2, mVar2.b(), mVar2.c(), mVar2.d())) {
                    this.f7522b.remove(i5);
                    i5--;
                }
                i5++;
            }
        }
        a().a(mVar);
    }

    private l a() {
        if (this.f7521a == null) {
            this.f7521a = new l(this);
            this.f7521a.start();
        }
        return this.f7521a;
    }
}
