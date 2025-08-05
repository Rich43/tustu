package av;

import G.R;
import G.Y;
import G.aM;
import G.aR;
import ao.hH;
import bH.W;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: av.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/i.class */
public class C0870i extends hH {

    /* renamed from: c, reason: collision with root package name */
    private aM f6289c;

    /* renamed from: d, reason: collision with root package name */
    private aM f6290d;

    /* renamed from: e, reason: collision with root package name */
    private aM f6291e;

    /* renamed from: a, reason: collision with root package name */
    Y f6292a;

    /* renamed from: b, reason: collision with root package name */
    C0871j f6293b = new C0871j(this);

    public C0870i(R r2, aM aMVar, aM aMVar2, aM aMVar3) throws V.a {
        this.f6289c = null;
        this.f6290d = null;
        this.f6291e = null;
        this.f6292a = null;
        this.f6292a = r2.h();
        String strC = r2.c();
        this.f6289c = aMVar;
        this.f6290d = aMVar2;
        this.f6291e = aMVar3;
        a(aMVar3.a(), aMVar3.m());
        super.a(aMVar3.u());
        try {
            k();
            this.f6293b.a();
            addTableModelListener(this.f6293b);
            aR.a().a(strC, aMVar.aJ(), this.f6293b);
            aR.a().a(strC, aMVar2.aJ(), this.f6293b);
            aR.a().a(strC, aMVar3.aJ(), this.f6293b);
        } catch (V.g e2) {
            Logger.getLogger(C0870i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.a("Error loading table parameter values.\n" + e2.getMessage());
        }
    }

    protected void k() {
        a(n().i(this.f6292a));
        d(a(m().i(this.f6292a), m().u()));
        c(a(l().i(this.f6292a), l().u()));
        q();
    }

    private String[] a(double[][] dArr, int i2) {
        String[] strArr = new String[dArr.length];
        for (int i3 = 0; i3 < dArr.length; i3++) {
            strArr[i3] = "" + W.b(dArr[i3][0], i2);
        }
        return strArr;
    }

    public aM l() {
        return this.f6289c;
    }

    public aM m() {
        return this.f6290d;
    }

    public aM n() {
        return this.f6291e;
    }
}
