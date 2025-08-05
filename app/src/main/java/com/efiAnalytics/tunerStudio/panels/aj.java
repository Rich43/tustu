package com.efiAnalytics.tunerStudio.panels;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/aj.class */
class aj extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f10066a;

    /* renamed from: b, reason: collision with root package name */
    boolean f10067b;

    /* renamed from: c, reason: collision with root package name */
    int f10068c;

    /* renamed from: d, reason: collision with root package name */
    Runnable f10069d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10070e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    aj(TriggerLoggerPanel triggerLoggerPanel) {
        super("HighSpeedLogger Throttle");
        this.f10070e = triggerLoggerPanel;
        this.f10066a = 0L;
        this.f10067b = false;
        this.f10068c = 67;
        this.f10069d = new ak(this);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            if (!this.f10067b || System.currentTimeMillis() - this.f10066a < this.f10068c) {
                try {
                    Thread.sleep(this.f10068c);
                } catch (InterruptedException e2) {
                    Logger.getLogger(TriggerLoggerPanel.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            if (this.f10067b) {
                try {
                    this.f10067b = false;
                    SwingUtilities.invokeAndWait(this.f10069d);
                    this.f10066a = System.currentTimeMillis();
                } catch (InterruptedException e3) {
                    Logger.getLogger(TriggerLoggerPanel.class.getName()).log(Level.SEVERE, "Error setting DataSet 1", (Throwable) e3);
                } catch (InvocationTargetException e4) {
                    Logger.getLogger(TriggerLoggerPanel.class.getName()).log(Level.SEVERE, "Error setting DataSet 2", (Throwable) e4);
                }
            }
        }
    }

    public void a() {
        this.f10067b = true;
    }
}
