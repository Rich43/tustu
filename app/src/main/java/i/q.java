package I;

import G.C0113cs;

/* loaded from: TunerStudioMS.jar:I/q.class */
public class q {

    /* renamed from: a, reason: collision with root package name */
    private r f1411a = null;

    public q() {
        C0113cs.a().d("hourOfDay");
        C0113cs.a().d("minuteOfHour");
        C0113cs.a().d("secondOfMinute");
    }

    public void a() {
        if (this.f1411a == null || !this.f1411a.isAlive()) {
            this.f1411a = new r(this);
            this.f1411a.start();
        }
    }
}
