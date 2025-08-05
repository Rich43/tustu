package G;

import java.io.IOException;

/* renamed from: G.cv, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cv.class */
public class C0116cv implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    R f1163a;

    /* renamed from: b, reason: collision with root package name */
    private int f1164b = 0;

    public C0116cv(R r2) {
        this.f1163a = null;
        this.f1163a = r2;
    }

    public static void a(R r2, int i2) throws IOException {
        C0130m c0130mD = C0130m.d(r2.O(), i2);
        c0130mD.b(new C0116cv(r2));
        r2.C().b(c0130mD);
    }

    public static void a(R r2, int i2, int i3, int i4) throws IOException {
        if (i2 < 255) {
            C0130m c0130mB = C0130m.b(r2.O(), i2, i3, i4);
            C0116cv c0116cv = new C0116cv(r2);
            c0116cv.b(i3);
            c0130mB.b(c0116cv);
            r2.C().b(c0130mB);
        }
    }

    public void a(int i2) throws IOException {
        C0130m c0130mD = C0130m.d(this.f1163a.O(), i2);
        c0130mD.b(this);
        this.f1163a.C().b(c0130mD);
    }

    public static void b(R r2, int i2) {
        C0130m c0130mD = C0130m.d(r2.O(), i2);
        c0130mD.b(new C0116cv(r2));
        C0132o c0132oA = new da().a(r2, c0130mD, 500);
        if (c0132oA.a() == 3) {
            bH.C.a("Page Refresh Failed: " + c0132oA.c());
        }
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        if (T.a().c(c0132o.f()) == null) {
            R r2 = this.f1163a;
        }
        if (c0132o.a() == 1) {
            try {
                if (0 != 0) {
                    for (int i2 = 0; i2 < c0132o.e().length; i2++) {
                        this.f1163a.h().a(c0132o.b().o(), this.f1164b + i2, new int[]{c0132o.e()[i2]});
                    }
                } else {
                    this.f1163a.h().a(c0132o.b().o(), this.f1164b, c0132o.e());
                }
            } catch (V.g e2) {
                bH.C.b("failed to update local data store.");
            }
        }
    }

    public void b(int i2) {
        this.f1164b = i2;
    }
}
