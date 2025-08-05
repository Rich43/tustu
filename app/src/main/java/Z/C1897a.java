package z;

import G.R;
import G.aF;
import bH.C;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: z.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:z/a.class */
public class C1897a {

    /* renamed from: a, reason: collision with root package name */
    R f14069a;

    /* renamed from: b, reason: collision with root package name */
    C1901e f14070b;

    /* renamed from: c, reason: collision with root package name */
    String f14071c = "9600";

    /* renamed from: d, reason: collision with root package name */
    int f14072d = 15;

    /* renamed from: e, reason: collision with root package name */
    ArrayList f14073e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    C1898b f14074f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f14075g = "115200";

    /* renamed from: h, reason: collision with root package name */
    private String f14076h = "COM1";

    /* renamed from: i, reason: collision with root package name */
    private boolean f14077i = false;

    public C1897a(R r2) {
        this.f14069a = null;
        this.f14070b = null;
        this.f14069a = r2;
        this.f14070b = (C1901e) this.f14069a.C();
    }

    public void a() {
        if (this.f14074f == null || !this.f14074f.a()) {
            this.f14074f = new C1898b(this);
            this.f14074f.start();
        }
    }

    public void b() {
        if (this.f14074f != null) {
            this.f14074f.a(false);
        }
    }

    public boolean c() {
        return this.f14074f != null && this.f14074f.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(byte[] bArr) {
        Iterator it = this.f14073e.iterator();
        while (it.hasNext()) {
            try {
                ((aF) it.next()).a(this.f14069a.c(), bArr);
            } catch (Exception e2) {
                C.a("Exception while notifiying BurstMode OchListener");
                e2.printStackTrace();
            }
        }
    }

    public void a(aF aFVar) {
        this.f14073e.add(aFVar);
    }

    public void b(aF aFVar) {
        this.f14073e.remove(aFVar);
    }

    public String d() {
        return this.f14075g;
    }

    public void a(String str) {
        this.f14075g = str;
    }

    public String e() {
        return this.f14076h;
    }

    public void b(String str) {
        this.f14076h = str;
    }

    public boolean f() {
        return this.f14077i;
    }

    public void a(boolean z2) {
        this.f14077i = z2;
    }
}
