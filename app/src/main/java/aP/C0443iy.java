package aP;

import java.awt.Cursor;

/* renamed from: aP.iy, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/iy.class */
class C0443iy extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0442ix f3763a;

    C0443iy(C0442ix c0442ix) {
        this.f3763a = c0442ix;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f3763a.f3762a.c();
        this.f3763a.f3762a.setCursor(Cursor.getDefaultCursor());
    }
}
