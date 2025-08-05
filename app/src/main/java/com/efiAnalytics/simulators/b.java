package com.efiAnalytics.simulators;

import bH.C;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/simulators/b.class */
class b extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f9926a = true;

    /* renamed from: b, reason: collision with root package name */
    int f9927b = 15000;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ UdpMultiCylPressureSimulator f9928c;

    b(UdpMultiCylPressureSimulator udpMultiCylPressureSimulator) {
        this.f9928c = udpMultiCylPressureSimulator;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f9926a) {
            int i2 = 240000 / this.f9927b;
            this.f9928c.c();
            for (int i3 = 0; i3 < this.f9928c.f9920f.length; i3++) {
                try {
                    byte[] bArr = this.f9928c.f9920f[i3];
                    this.f9928c.f9918d = new DatagramPacket(bArr, bArr.length, this.f9928c.f9916c, this.f9928c.f9915b);
                    this.f9928c.f9917i.send(this.f9928c.f9918d);
                    C.c("Send Cyl Pressure Data");
                    Thread.sleep(0L, 1000);
                } catch (IOException e2) {
                    Logger.getLogger(UdpCylPressureSimulator.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                } catch (InterruptedException e3) {
                    Logger.getLogger(UdpMultiCylPressureSimulator.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
            try {
                sleep(i2);
            } catch (InterruptedException e4) {
                Logger.getLogger(UdpCylPressureSimulator.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            }
        }
    }

    public synchronized void a() {
        this.f9926a = false;
        notify();
    }
}
