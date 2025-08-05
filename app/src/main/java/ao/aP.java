package ao;

import W.C0184j;
import java.awt.Rectangle;

/* loaded from: TunerStudioMS.jar:ao/aP.class */
class aP {

    /* renamed from: a, reason: collision with root package name */
    public static String f5138a = "showColumnMenu";

    /* renamed from: b, reason: collision with root package name */
    public static String f5139b = "showXAxisMenu";

    /* renamed from: c, reason: collision with root package name */
    private Rectangle f5136c = null;

    /* renamed from: d, reason: collision with root package name */
    private C0184j f5137d = null;

    /* renamed from: e, reason: collision with root package name */
    private String f5140e = f5138a;

    aP() {
    }

    public boolean a(int i2, int i3) {
        if (this.f5136c == null || this.f5137d == null) {
            return false;
        }
        return this.f5136c.contains(i2, i3);
    }

    Rectangle a() {
        return this.f5136c;
    }

    void a(Rectangle rectangle) {
        this.f5136c = rectangle;
    }

    C0184j b() {
        return this.f5137d;
    }

    void a(C0184j c0184j) {
        this.f5137d = c0184j;
    }

    public String c() {
        return this.f5140e;
    }

    public void a(String str) {
        this.f5140e = str;
    }
}
