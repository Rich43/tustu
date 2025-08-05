package af;

import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:af/i.class */
class i {

    /* renamed from: a, reason: collision with root package name */
    int f4481a;

    /* renamed from: b, reason: collision with root package name */
    boolean f4482b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ h f4483c;

    i(h hVar, int i2, boolean z2) {
        this.f4483c = hVar;
        this.f4481a = -1;
        this.f4482b = false;
        this.f4481a = i2;
        this.f4482b = z2;
    }

    boolean a() {
        if (this.f4482b && this.f4483c.f4480c == null) {
            return false;
        }
        if (!this.f4482b) {
            return true;
        }
        Iterator it = this.f4483c.f4479b.iterator();
        while (it.hasNext()) {
            if (this.f4483c.f4480c.startsWith((String) it.next())) {
                return true;
            }
        }
        return false;
    }
}
