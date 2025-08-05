package bb;

import java.awt.Cursor;

/* renamed from: bb.J, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/J.class */
class RunnableC1033J implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1032I f7734a;

    RunnableC1033J(C1032I c1032i) {
        this.f7734a = c1032i;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f7734a.f7732b.getGlassPane().setCursor(Cursor.getDefaultCursor());
        this.f7734a.f7732b.getGlassPane().setVisible(false);
        bH.C.c("Set default Cursor");
    }
}
