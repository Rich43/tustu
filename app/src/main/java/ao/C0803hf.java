package ao;

import java.awt.MouseInfo;
import java.awt.Point;

/* renamed from: ao.hf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/hf.class */
class C0803hf extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f6051a = false;

    /* renamed from: b, reason: collision with root package name */
    long f6052b = 0;

    /* renamed from: c, reason: collision with root package name */
    long f6053c = 0;

    /* renamed from: d, reason: collision with root package name */
    Object f6054d = new Object();

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ gS f6055e;

    C0803hf(gS gSVar) {
        this.f6055e = gSVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            synchronized (this.f6054d) {
                try {
                    this.f6054d.wait(500L);
                } catch (Exception e2) {
                }
            }
            if ((this.f6051a && System.currentTimeMillis() - this.f6053c > 500) || (System.currentTimeMillis() - this.f6052b > 2000 && !c())) {
                this.f6055e.e();
                this.f6051a = false;
            }
        }
    }

    private boolean c() {
        try {
            Point location = MouseInfo.getPointerInfo().getLocation();
            Point locationOnScreen = this.f6055e.getLocationOnScreen();
            if (location.f12370x > locationOnScreen.f12370x && location.f12371y > locationOnScreen.f12371y && location.f12370x < locationOnScreen.f12370x + this.f6055e.getWidth()) {
                if (location.f12371y < locationOnScreen.f12371y + this.f6055e.getHeight()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    public void a() {
        this.f6051a = false;
        this.f6052b = System.currentTimeMillis();
    }

    public void b() {
        this.f6051a = true;
        this.f6053c = System.currentTimeMillis();
    }
}
