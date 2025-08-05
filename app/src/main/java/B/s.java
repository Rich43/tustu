package B;

import G.C0129l;
import G.R;
import bH.C;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.optimization.direct.CMAESOptimizer;
import org.apache.commons.net.tftp.TFTP;

/* loaded from: TunerStudioMS.jar:B/s.class */
public class s extends o implements bT.r {

    /* renamed from: k, reason: collision with root package name */
    c f217k;

    /* renamed from: l, reason: collision with root package name */
    boolean f218l = true;

    /* renamed from: m, reason: collision with root package name */
    R f219m;

    public s(R r2) {
        this.f188d = 21845;
        this.f219m = r2;
        this.f217k = new c(r2);
        a(true);
        c(CMAESOptimizer.DEFAULT_MAXITERATIONS);
    }

    @Override // bT.r
    public bT.r v() throws C0129l {
        int length;
        this.f218l = true;
        this.f217k.b(z());
        this.f217k.a("UDP");
        this.f217k.b();
        try {
            this.f187c = InetAddress.getLocalHost().getHostAddress();
            if (this.f187c == null || this.f187c.equals("")) {
                throw new C0129l("IP Address or host name not set! Can not open UDP ");
            }
            if (this.f188d <= 0) {
                throw new C0129l("Invalid Port:" + this.f188d + " Can not open UDP ");
            }
            c();
            try {
                try {
                    C.c("Opening UDP Server listener: " + y());
                    s sVar = new s(this.f219m);
                    for (A.r rVar : l()) {
                        try {
                            sVar.a(rVar.c(), a(rVar.c()));
                        } catch (A.s e2) {
                            Logger.getLogger(m.class.getName()).log(Level.WARNING, "Failed to set Setting", (Throwable) e2);
                        }
                    }
                    sVar.a(true);
                    DatagramSocket datagramSocketT = sVar.t();
                    byte[] bArr = new byte[50];
                    DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
                    datagramSocketT.setSoTimeout(TFTP.DEFAULT_TIMEOUT);
                    datagramSocketT.setTrafficClass(20);
                    do {
                        try {
                            datagramSocketT.receive(datagramPacket);
                            length = datagramPacket.getLength();
                            C.c("bytesRecieved = " + length);
                        } catch (Exception e3) {
                            if (this.f218l) {
                                C.d("No Connection on UDP, try again.");
                            }
                            length = 0;
                        }
                        if (!this.f218l) {
                            break;
                        }
                    } while (length <= 0);
                    if (datagramPacket.getLength() <= 0) {
                        this.f217k.a();
                        return null;
                    }
                    sVar.a(datagramSocketT);
                    byte[] bArr2 = new byte[datagramPacket.getLength()];
                    System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
                    sVar.f191f.a(bArr2);
                    sVar.f192g.a(datagramPacket.getAddress());
                    sVar.f192g.a(datagramPacket.getPort());
                    sVar.b(3);
                    sVar.a();
                    this.f217k.a();
                    return sVar;
                } catch (IOException e4) {
                    g();
                    C.a(e4);
                    this.f217k.a();
                    return null;
                }
            } catch (Throwable th) {
                this.f217k.a();
                throw th;
            }
        } catch (UnknownHostException e5) {
            Logger.getLogger(s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
            throw new C0129l("failed to get localhost ip address");
        }
    }

    @Override // bT.r
    public void w() {
        this.f218l = false;
    }

    @Override // B.o, A.f
    public int s() {
        return 2;
    }

    @Override // bT.r
    public int x() {
        return 1;
    }
}
