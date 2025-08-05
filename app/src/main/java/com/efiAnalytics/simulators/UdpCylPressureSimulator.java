package com.efiAnalytics.simulators;

import bH.C;
import bH.C0995c;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocksConsts;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/simulators/UdpCylPressureSimulator.class */
public class UdpCylPressureSimulator {

    /* renamed from: h, reason: collision with root package name */
    private DatagramSocket f9909h;

    /* renamed from: d, reason: collision with root package name */
    DatagramPacket f9910d;

    /* renamed from: a, reason: collision with root package name */
    int f9906a = 28555;

    /* renamed from: b, reason: collision with root package name */
    int f9907b = 0;

    /* renamed from: c, reason: collision with root package name */
    InetAddress f9908c = null;

    /* renamed from: e, reason: collision with root package name */
    boolean f9911e = true;

    /* renamed from: f, reason: collision with root package name */
    byte[] f9912f = new byte[SocksConsts.DEFAULT_PORT];

    /* renamed from: g, reason: collision with root package name */
    a f9913g = null;

    public void a() {
        int length;
        try {
            this.f9909h = new DatagramSocket((SocketAddress) null);
            this.f9909h.setBroadcast(true);
            this.f9909h.bind(new InetSocketAddress(InetAddress.getLocalHost(), this.f9906a));
            byte[] bArr = new byte[100];
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
            this.f9909h.setSoTimeout(15000);
            this.f9909h.setTrafficClass(20);
            do {
                try {
                    this.f9909h.receive(datagramPacket);
                    length = datagramPacket.getLength();
                    C.c("bytesRecieved = " + length);
                } catch (Exception e2) {
                    if (this.f9911e) {
                        C.d("No Connection on UDP, try again.");
                    }
                    length = 0;
                }
                if (this.f9911e && length > 0) {
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, 0, bArr2, 0, length);
                    C.d("Received Message: " + C0995c.d(bArr2));
                    if (C0995c.a(bArr2[0]) == 255) {
                        C.d("ConnectCommand Received, starting broadcast...");
                        this.f9907b = datagramPacket.getPort();
                        this.f9908c = datagramPacket.getAddress();
                        d();
                    } else if (C0995c.a(bArr2[0]) == 254) {
                        C.d("Disconnect received, stopping broadcast");
                        b();
                    } else if (C0995c.a(bArr2[0]) == 253) {
                        C.d("Stop server command received, stopping broadcast, ending server");
                        b();
                        this.f9911e = false;
                    } else {
                        C.b("Unknown Command Received: " + C0995c.d(bArr2));
                    }
                }
            } while (this.f9911e);
        } catch (SocketException e3) {
            Logger.getLogger(UdpCylPressureSimulator.class.getName()).log(Level.SEVERE, "Failed to start Server", (Throwable) e3);
        } catch (UnknownHostException e4) {
            Logger.getLogger(UdpCylPressureSimulator.class.getName()).log(Level.SEVERE, "Failed to start server", (Throwable) e4);
        }
    }

    public static void main(String[] strArr) {
        new UdpCylPressureSimulator().a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        byte[] bArr = new byte[2];
        double dRandom = Math.random() / 10.0d;
        for (int i2 = 0; i2 < this.f9912f.length; i2 = i2 + 1 + 1) {
            C0995c.a((int) ((200.0d * dRandom) + Math.pow((68.0f - Math.abs((((float) (i2 + (dRandom * 150.0d))) / 8.0f) - 65.0f)) / 50.0f, 32.0d)), bArr, true);
            System.arraycopy(bArr, 0, this.f9912f, i2, bArr.length);
        }
    }

    private void d() {
        if (this.f9913g != null) {
            this.f9913g.a();
        }
        this.f9913g = new a(this);
        this.f9913g.start();
    }

    public void b() {
        if (this.f9913g != null) {
            this.f9913g.a();
        }
    }
}
