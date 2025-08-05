package bO;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:bO/k.class */
public class k extends ArrayList {

    /* renamed from: b, reason: collision with root package name */
    private boolean f7376b = false;

    /* renamed from: a, reason: collision with root package name */
    int f7377a = -1;

    public boolean a() {
        return this.f7376b;
    }

    public void a(boolean z2) {
        this.f7376b = z2;
    }

    public int b() {
        if (this.f7377a < 0) {
            this.f7377a = 0;
            Iterator it = iterator();
            while (it.hasNext()) {
                this.f7377a += ((l) it.next()).b();
            }
        }
        return this.f7377a;
    }
}
