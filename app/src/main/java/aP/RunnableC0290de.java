package aP;

import java.awt.BorderLayout;

/* renamed from: aP.de, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/de.class */
class RunnableC0290de implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0289dd f3220a;

    RunnableC0290de(C0289dd c0289dd) {
        this.f3220a = c0289dd;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.f3220a.f3215b = new bZ();
            this.f3220a.setLayout(new BorderLayout());
            this.f3220a.add(BorderLayout.CENTER, this.f3220a.f3215b);
            if (this.f3220a.f3214a != null) {
                this.f3220a.f3215b.a(this.f3220a.f3216c, this.f3220a.f3214a);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
