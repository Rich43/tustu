package bQ;

import G.J;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bQ/g.class */
class g implements bN.f {

    /* renamed from: a, reason: collision with root package name */
    long f7422a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ f f7423b;

    g(f fVar) {
        this.f7423b = fVar;
    }

    @Override // bN.f
    public void a(bN.t tVar) {
        if (tVar instanceof bN.l) {
            i iVar = (i) this.f7423b.f7417j.get(Integer.valueOf(tVar.a()));
            if (iVar != null) {
                try {
                    iVar.a(this.f7423b.f7409k, this.f7423b.f7408c, tVar);
                } catch (bT.h e2) {
                    Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            } else {
                synchronized (this.f7423b.f7414l) {
                    this.f7423b.f7414l.add(tVar);
                }
                synchronized (this.f7423b.f7416i) {
                    this.f7423b.f7416i.notifyAll();
                }
            }
            if (this.f7423b.f7421p != null) {
                this.f7423b.f7421p.f();
            }
        } else {
            if (this.f7423b.f7419n == null || !this.f7423b.f7419n.a(this.f7423b.f7408c, tVar)) {
                if (J.I()) {
                    C.b("Received DTO Type Packet that is unknown? DAQ Running before started?\n" + C0995c.d(tVar.d()));
                }
                if (System.currentTimeMillis() - this.f7422a > 3000) {
                    this.f7422a = System.currentTimeMillis();
                }
            } else {
                try {
                    this.f7423b.f7419n.a(this.f7423b.f7409k, this.f7423b.f7408c, tVar);
                } catch (bT.h e3) {
                    Logger.getLogger(f.class.getName()).log(Level.WARNING, "Unable to process DAQ Message", (Throwable) e3);
                }
            }
            if (this.f7423b.f7421p != null) {
                this.f7423b.f7421p.k();
            }
        }
        if (this.f7423b.f7406a != null) {
            this.f7423b.f7406a.b();
        }
    }

    @Override // bN.f
    public void a(int i2, String str) {
        C.a("Message validation error: 0x" + Integer.toHexString(i2).toUpperCase() + ", message:" + str);
        bN.l lVarB = bN.u.a().b();
        this.f7423b.f7407b.b();
        lVarB.a(254);
        byte[] bArr = new byte[str.getBytes().length + 1];
        bArr[0] = (byte) i2;
        System.arraycopy(str.getBytes(), 0, bArr, 1, bArr.length - 1);
        synchronized (this.f7423b.f7414l) {
            this.f7423b.f7414l.add(lVarB);
        }
        synchronized (this.f7423b.f7416i) {
            this.f7423b.f7416i.notifyAll();
        }
    }

    @Override // bN.f
    public void a(IOException iOException) {
    }

    @Override // bN.f
    public void a() {
        this.f7423b.c(this.f7423b.f7409k != null ? this.f7423b.f7409k.u() : null);
    }

    @Override // bN.f
    public void b() {
        this.f7423b.d(this.f7423b.f7409k != null ? this.f7423b.f7409k.u() : null);
    }
}
