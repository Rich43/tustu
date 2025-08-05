package bT;

import bN.t;
import bN.u;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bT/j.class */
public class j implements bN.f, bN.g {

    /* renamed from: a, reason: collision with root package name */
    o f7590a;

    /* renamed from: b, reason: collision with root package name */
    bN.k f7591b;

    j(o oVar) {
        this.f7590a = oVar;
        this.f7591b = oVar.d();
    }

    @Override // bN.f
    public void a(t tVar) {
        a aVarA = this.f7590a.i().a(tVar.a());
        if (aVarA != null) {
            try {
                aVarA.a(this.f7590a, tVar);
            } catch (h e2) {
                Logger.getLogger(j.class.getName()).log(Level.SEVERE, "Unhandled error processing Command", (Throwable) e2);
                b(49, e2.getLocalizedMessage());
            } catch (Exception e3) {
                Logger.getLogger(j.class.getName()).log(Level.SEVERE, "Unhandled error processing Command", (Throwable) e3);
                b(49, e3.getLocalizedMessage());
            }
        } else {
            b(32, "Unsupported Command");
        }
        this.f7590a.e().b();
    }

    private void b(int i2, String str) {
        bN.l lVarB = u.a().b();
        lVarB.a(254);
        if (str == null) {
            str = "";
        }
        if (str.length() > this.f7591b.i() - 2) {
            str = str.substring(0, this.f7591b.i() - 2);
        }
        byte[] bArr = new byte[str.length() + 1];
        bArr[0] = (byte) i2;
        System.arraycopy(str.getBytes(), 0, bArr, 1, bArr.length - 1);
        try {
            this.f7590a.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // bN.f
    public void a(int i2, String str) {
        b(i2, str);
        this.f7590a.e().b();
    }

    @Override // bN.f
    public void a(IOException iOException) {
    }

    @Override // bN.g
    public void b(IOException iOException) {
    }

    @Override // bN.g
    public void a(bN.o oVar, bS.c cVar) {
    }

    @Override // bN.g
    public void a(bS.c cVar) {
    }

    @Override // bN.f
    public void a() {
    }

    @Override // bN.f
    public void b() {
    }
}
