package bF;

import G.R;
import G.aM;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bF.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bF/a.class */
public class C0970a implements x {

    /* renamed from: a, reason: collision with root package name */
    R f6835a;

    /* renamed from: b, reason: collision with root package name */
    aM f6836b;

    public C0970a(R r2, String str) {
        this.f6835a = r2;
        this.f6836b = r2.c(str);
    }

    @Override // bF.x
    public String a(int i2) {
        try {
            return this.f6836b.c(this.f6835a.h())[i2];
        } catch (V.g e2) {
            Logger.getLogger(C0970a.class.getName()).log(Level.SEVERE, "Invalid parameter", (Throwable) e2);
            return "" + i2;
        } catch (IndexOutOfBoundsException e3) {
            Logger.getLogger(C0970a.class.getName()).log(Level.SEVERE, "Invalid index for parameter " + this.f6836b.aJ(), (Throwable) e3);
            return "" + i2;
        }
    }
}
