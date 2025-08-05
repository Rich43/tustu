package com.efiAnalytics.simulators;

import bH.C;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/simulators/a.class */
class a extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f9923a = true;

    /* renamed from: b, reason: collision with root package name */
    int f9924b = 2000;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ UdpCylPressureSimulator f9925c;

    a(UdpCylPressureSimulator udpCylPressureSimulator) {
        this.f9925c = udpCylPressureSimulator;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f9923a) {
            int i2 = 120000 / this.f9924b;
            try {
                this.f9925c.c();
                this.f9925c.f9910d = new DatagramPacket(this.f9925c.f9912f, this.f9925c.f9912f.length, this.f9925c.f9908c, this.f9925c.f9907b);
                this.f9925c.f9909h.send(this.f9925c.f9910d);
                C.c("Send Cyl Pressure Data");
            } catch (IOException e2) {
                Logger.getLogger(UdpCylPressureSimulator.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            try {
                sleep(i2);
            } catch (InterruptedException e3) {
                Logger.getLogger(UdpCylPressureSimulator.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }

    public synchronized void a() {
        this.f9923a = false;
        notify();
    }
}
