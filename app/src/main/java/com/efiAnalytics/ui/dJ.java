package com.efiAnalytics.ui;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dJ.class */
class dJ extends Thread {

    /* renamed from: b, reason: collision with root package name */
    private boolean f11333b = true;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ dI f11334a;

    dJ(dI dIVar) {
        this.f11334a = dIVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Robot robot = new Robot();
            while (this.f11333b) {
                robot.delay(15000);
                if (!this.f11334a.f()) {
                    Point location = MouseInfo.getPointerInfo().getLocation();
                    robot.mouseMove(location.f12370x + 1, location.f12371y + 1);
                    robot.mouseMove(location.f12370x, location.f12371y);
                }
            }
        } catch (AWTException e2) {
            Logger.getLogger(dI.class.getName()).log(Level.WARNING, "Unable to start Sleep Prevent", (Throwable) e2);
        }
    }

    public boolean a() {
        return this.f11333b;
    }

    public void a(boolean z2) {
        this.f11333b = z2;
    }
}
