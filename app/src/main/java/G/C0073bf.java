package G;

import bH.C1011s;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: G.bf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bf.class */
public class C0073bf extends Q implements Serializable {

    /* renamed from: d, reason: collision with root package name */
    private bP f912d;

    /* renamed from: b, reason: collision with root package name */
    private String f909b = null;

    /* renamed from: c, reason: collision with root package name */
    private int f910c = -1;

    /* renamed from: a, reason: collision with root package name */
    String f911a = null;

    /* renamed from: e, reason: collision with root package name */
    private String f913e = null;

    public C0073bf() {
        this.f912d = null;
        this.f912d = new C0074bg(this);
    }

    public C0073bf(bP bPVar) {
        this.f912d = null;
        this.f912d = bPVar;
    }

    public void a(String str) {
        try {
            d().a(str.getBytes("UTF-8"));
            this.f911a = str;
            b();
        } catch (IOException e2) {
            Logger.getLogger(C0073bf.class.getName()).log(Level.SEVERE, "Unable to set TuningView", (Throwable) e2);
        }
    }

    public String a() {
        return this.f913e;
    }

    public String b() {
        String strA = C1011s.a(d().a());
        if (this.f913e == null) {
            bH.C.d("Setting checksum for TuningView: " + aJ());
        } else if (!this.f913e.equals(strA)) {
            bH.C.d("updating checksum for TuningView: " + aJ());
        }
        this.f913e = strA;
        return this.f913e;
    }

    public void b(String str) {
        this.f913e = str;
    }

    public String c() {
        return this.f909b;
    }

    public void c(String str) {
        this.f909b = str;
    }

    public bO d() {
        return e().a(aJ(), this.f913e);
    }

    public bP e() {
        if (this.f912d == null) {
            this.f912d = new C0074bg(this);
        }
        return this.f912d;
    }

    public void a(int i2) {
        this.f910c = i2;
    }

    public int f() {
        return this.f910c;
    }
}
