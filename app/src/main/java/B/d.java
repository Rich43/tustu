package B;

import G.R;
import G.T;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.java2d.marlin.MarlinConst;

/* loaded from: TunerStudioMS.jar:B/d.class */
class d extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f119a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ c f120b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    d(c cVar) {
        super("BroadcastListener");
        this.f120b = cVar;
        this.f119a = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        DatagramSocket datagramSocketD;
        DatagramPacket datagramPacket;
        String strTrim;
        boolean z2;
        while (!this.f119a) {
            try {
                datagramSocketD = this.f120b.d();
                datagramSocketD.setBroadcast(true);
                byte[] bArr = new byte[512];
                try {
                    datagramPacket = new DatagramPacket(bArr, bArr.length);
                    datagramSocketD.receive(datagramPacket);
                    strTrim = new String(datagramPacket.getData()).trim();
                    C.d("BroadcastListener: Packet received from: " + datagramPacket.getAddress().getHostAddress() + ": " + strTrim);
                    z2 = e.a(this.f120b.f100n).b() && C0995c.b(datagramPacket.getAddress().getAddress(), InetAddress.getLocalHost().getAddress());
                } catch (IOException e2) {
                    C.d("Slave Server Broadcast exception: " + e2.getMessage());
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e3) {
                        Logger.getLogger(c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                }
            } catch (SocketException e4) {
                C.a("Socket Exception! Can not listen for broadcast. \n" + e4.getMessage());
                try {
                    Thread.sleep(MarlinConst.statDump);
                } catch (InterruptedException e5) {
                    Logger.getLogger(c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
            } catch (UnknownHostException e6) {
                C.a("UnknownHostException! Can not listen for broadcast. \n" + e6.getMessage());
                try {
                    Thread.sleep(MarlinConst.statDump);
                } catch (InterruptedException e7) {
                    Logger.getLogger(c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                }
            }
            if (strTrim.equals(c.f112l) && !z2) {
                String strE = this.f120b.e();
                String str = c.f101a + CallSiteDescriptor.TOKEN_DELIMITER + this.f120b.f114p + "\n" + c.f104d + CallSiteDescriptor.TOKEN_DELIMITER;
                if (this.f120b.f115m != null) {
                    String str2 = (((((this.f120b.f115m.P() != null ? str + this.f120b.f115m.P() + "\n" : str + this.f120b.f115m.i() + "\n") + c.f102b + CallSiteDescriptor.TOKEN_DELIMITER + this.f120b.f115m.i() + "\n") + c.f105e + CallSiteDescriptor.TOKEN_DELIMITER + this.f120b.f113o + "\n") + c.f107g + CallSiteDescriptor.TOKEN_DELIMITER + this.f120b.f116q + "\n") + c.f106f + CallSiteDescriptor.TOKEN_DELIMITER + this.f120b.f115m.c() + "\n") + c.f109i + CallSiteDescriptor.TOKEN_DELIMITER + this.f120b.f115m.O().x() + "\n";
                    if (strE != null && !strE.isEmpty()) {
                        str2 = str2 + c.f103c + CallSiteDescriptor.TOKEN_DELIMITER + strE + "\n";
                    }
                    if (aE.a.A() != null) {
                        str2 = str2 + c.f110j + CallSiteDescriptor.TOKEN_DELIMITER + aE.a.A().V() + "\n";
                    }
                    String[] strArrD = T.a().d();
                    if (strArrD.length > 1) {
                        int i2 = 1;
                        for (String str3 : strArrD) {
                            if (!str3.equals(this.f120b.f115m.c())) {
                                R rC = T.a().c(str3);
                                String str4 = c.f111k + i2 + "_";
                                int i3 = i2;
                                i2++;
                                str2 = (((((rC.P() != null ? str2 + str4 + c.f104d + CallSiteDescriptor.TOKEN_DELIMITER + rC.P() + "\n" : str2 + str4 + c.f104d + CallSiteDescriptor.TOKEN_DELIMITER + rC.i() + "\n") + str4 + c.f102b + CallSiteDescriptor.TOKEN_DELIMITER + rC.i() + "\n") + str4 + c.f105e + CallSiteDescriptor.TOKEN_DELIMITER + (this.f120b.f113o + i3) + "\n") + str4 + c.f107g + CallSiteDescriptor.TOKEN_DELIMITER + this.f120b.f116q + "\n") + str4 + c.f106f + CallSiteDescriptor.TOKEN_DELIMITER + rC.c() + "\n") + str4 + c.f109i + CallSiteDescriptor.TOKEN_DELIMITER + rC.O().x() + "\n";
                            }
                        }
                    }
                    byte[] bytes = str2.getBytes();
                    datagramSocketD.send(new DatagramPacket(bytes, bytes.length, datagramPacket.getAddress(), datagramPacket.getPort()));
                }
            }
        }
    }

    public synchronized void a() {
        this.f119a = true;
        notify();
        if (this.f120b.f118s != null) {
            try {
                this.f120b.f118s.close();
                this.f120b.f118s = null;
            } catch (Exception e2) {
            }
        }
    }
}
