package bg;

import java.awt.Cursor;

/* renamed from: bg.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/b.class */
class RunnableC1122b implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1121a f8071a;

    RunnableC1122b(C1121a c1121a) {
        this.f8071a = c1121a;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f8071a.d();
        this.f8071a.setCursor(Cursor.getDefaultCursor());
    }
}
