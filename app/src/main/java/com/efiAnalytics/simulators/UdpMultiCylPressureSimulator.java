package com.efiAnalytics.simulators;

import bH.C;
import bH.C0995c;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/simulators/UdpMultiCylPressureSimulator.class */
public class UdpMultiCylPressureSimulator {

    /* renamed from: i, reason: collision with root package name */
    private DatagramSocket f9917i;

    /* renamed from: d, reason: collision with root package name */
    DatagramPacket f9918d;

    /* renamed from: a, reason: collision with root package name */
    int f9914a = 28555;

    /* renamed from: b, reason: collision with root package name */
    int f9915b = 0;

    /* renamed from: c, reason: collision with root package name */
    InetAddress f9916c = null;

    /* renamed from: e, reason: collision with root package name */
    boolean f9919e = true;

    /* renamed from: f, reason: collision with root package name */
    byte[][] f9920f = new byte[4][1800];

    /* renamed from: g, reason: collision with root package name */
    b f9921g = null;

    /* renamed from: h, reason: collision with root package name */
    int f9922h = 0;

    public void a() {
        int length;
        try {
            this.f9917i = new DatagramSocket((SocketAddress) null);
            this.f9917i.setBroadcast(true);
            this.f9917i.bind(new InetSocketAddress(InetAddress.getLocalHost(), this.f9914a));
            byte[] bArr = new byte[100];
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
            this.f9917i.setSoTimeout(15000);
            this.f9917i.setTrafficClass(20);
            do {
                try {
                    this.f9917i.receive(datagramPacket);
                    length = datagramPacket.getLength();
                    C.c("bytesRecieved = " + length);
                } catch (Exception e2) {
                    if (this.f9919e) {
                        C.d("No Connection on UDP, try again.");
                    }
                    length = 0;
                }
                if (this.f9919e && length > 0) {
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, 0, bArr2, 0, length);
                    C.d("Received Message: " + C0995c.d(bArr2));
                    if (C0995c.a(bArr2[0]) == 255) {
                        C.d("ConnectCommand Received, starting broadcast...");
                        this.f9915b = datagramPacket.getPort();
                        this.f9916c = datagramPacket.getAddress();
                        d();
                    } else if (C0995c.a(bArr2[0]) == 254) {
                        C.d("Disconnect received, stopping broadcast");
                        b();
                    } else if (C0995c.a(bArr2[0]) == 253) {
                        C.d("Stop server command received, stopping broadcast, ending server");
                        b();
                        this.f9919e = false;
                    } else {
                        C.b("Unknown Command Received: " + C0995c.d(bArr2));
                    }
                }
            } while (this.f9919e);
        } catch (SocketException e3) {
            Logger.getLogger(UdpCylPressureSimulator.class.getName()).log(Level.SEVERE, "Failed to start Server", (Throwable) e3);
        } catch (UnknownHostException e4) {
            Logger.getLogger(UdpCylPressureSimulator.class.getName()).log(Level.SEVERE, "Failed to start server", (Throwable) e4);
        }
    }

    public static void main(String[] strArr) {
        new UdpMultiCylPressureSimulator().a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        byte b2 = 0;
        while (true) {
            byte b3 = b2;
            if (b3 >= this.f9920f.length) {
                return;
            }
            a(this.f9920f[b3]);
            if (b3 == 1) {
                this.f9920f[b3][0] = 64;
            } else if (b3 == 2) {
                this.f9920f[b3][0] = Byte.MIN_VALUE;
            } else if (b3 == 3) {
                this.f9920f[b3][0] = -64;
            } else {
                this.f9920f[b3][0] = 0;
            }
            this.f9920f[b3][1] = 0;
            this.f9920f[b3][2] = (byte) (this.f9922h >> 8);
            this.f9920f[b3][3] = (byte) (this.f9922h & 255);
            this.f9922h++;
            b2 = (byte) (b3 + 1);
        }
    }

    private void a(byte[] bArr) {
        byte[] bArr2 = new byte[2];
        double dRandom = Math.random() / 10.0d;
        for (int i2 = 0; i2 < bArr.length; i2 = i2 + 1 + 1) {
            C0995c.a((int) (250.0d * (dRandom + Math.pow((62.0f - Math.abs((((float) (i2 + (dRandom * 150.0d))) / 15.0f) - 65.0f)) / 50.0f, 16.0d))), bArr2, true);
            System.arraycopy(bArr2, 0, bArr, i2, bArr2.length);
        }
    }

    private void d() {
        if (this.f9921g != null) {
            this.f9921g.a();
        }
        this.f9921g = new b(this);
        this.f9921g.start();
    }

    public void b() {
        if (this.f9921g != null) {
            this.f9921g.a();
        }
    }
}
