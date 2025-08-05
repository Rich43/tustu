package aj;

import G.C0123f;
import G.J;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.net.tftp.TFTP;

/* loaded from: TunerStudioMS.jar:aj/k.class */
public class k extends d {

    /* renamed from: u, reason: collision with root package name */
    private String f4567u;

    /* renamed from: v, reason: collision with root package name */
    private DatagramSocket f4568v;

    /* renamed from: t, reason: collision with root package name */
    private int f4566t = 28555;

    /* renamed from: p, reason: collision with root package name */
    byte[] f4569p = new byte[TFTP.DEFAULT_TIMEOUT];

    /* renamed from: w, reason: collision with root package name */
    private byte[] f4570w = null;

    /* renamed from: x, reason: collision with root package name */
    private byte[] f4571x = null;

    /* renamed from: y, reason: collision with root package name */
    private final List f4572y = new ArrayList();

    /* renamed from: q, reason: collision with root package name */
    l f4573q = null;

    /* renamed from: r, reason: collision with root package name */
    m f4574r = null;

    /* renamed from: s, reason: collision with root package name */
    C0123f f4575s = new C0123f();

    public k(String str) {
        this.f4567u = str;
    }

    @Override // aj.d
    public void b() {
        if (f4531d) {
            return;
        }
        f4531d = true;
        if (this.f4573q != null) {
            this.f4573q.a();
        }
        e();
        this.f4573q = new l(this);
        this.f4573q.start();
    }

    @Override // aj.d
    public void b(String str) {
        if (str != null) {
            this.f4570w = d(str)[0];
        } else {
            this.f4570w = null;
        }
    }

    @Override // aj.d
    public void c(String str) {
        if (str != null) {
            this.f4571x = d(str)[0];
        } else {
            this.f4571x = null;
        }
    }

    @Override // aj.d
    protected void e() {
        if (this.f4570w == null) {
            C.b("No Start Commad Set!");
            return;
        }
        try {
            DatagramPacket datagramPacket = new DatagramPacket(this.f4570w, this.f4570w.length, n(), l());
            if (J.I()) {
                C.c("Sending UDP Logger start command to " + ((Object) n()) + CallSiteDescriptor.TOKEN_DELIMITER + l() + ":\n" + C0995c.d(this.f4570w));
            }
            k().send(datagramPacket);
            if (J.I()) {
                C.c("UDP Logger start command successfully sent.");
            }
        } catch (SocketException e2) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, "Unable to connect to UDP server", (Throwable) e2);
        } catch (UnknownHostException e3) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, "Unable to connect to UDP server", (Throwable) e3);
        } catch (IOException e4) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, "Unable to connect to UDP server", (Throwable) e4);
        }
    }

    private InetAddress n() {
        return InetAddress.getByName(m());
    }

    @Override // aj.d
    public void c() {
        if (this.f4573q != null) {
            this.f4573q.a();
        }
        if (this.f4571x != null) {
            try {
                k().send(new DatagramPacket(this.f4571x, this.f4571x.length, n(), l()));
                Thread.sleep(40L);
            } catch (UnknownHostException e2) {
                Logger.getLogger(k.class.getName()).log(Level.WARNING, "Unable to connect to UDP server", (Throwable) e2);
            } catch (IOException e3) {
                Logger.getLogger(k.class.getName()).log(Level.WARNING, "Unable to connect to UDP server", (Throwable) e3);
            } catch (InterruptedException e4) {
                Logger.getLogger(k.class.getName()).log(Level.INFO, "Sleep failed.", (Throwable) e4);
            } catch (SocketException e5) {
                Logger.getLogger(k.class.getName()).log(Level.WARNING, "Unable to connect to UDP server", (Throwable) e5);
            }
        } else {
            C.b("No Stop Command Set!");
        }
        try {
            k().close();
        } catch (SocketException e6) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
        } catch (UnknownHostException e7) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
        }
        this.f4568v = null;
        f4531d = false;
        if (this.f4574r != null) {
            this.f4574r.a();
        }
    }

    @Override // aj.d
    public void a() {
        if (this.f4573q != null) {
            this.f4573q.a();
            this.f4573q = null;
        }
        this.f4570w = null;
        this.f4571x = null;
        this.f4568v = null;
    }

    protected DatagramSocket k() throws SocketException {
        if (this.f4568v == null || this.f4568v.isClosed()) {
            this.f4568v = new DatagramSocket((SocketAddress) null);
            this.f4568v.setBroadcast(true);
            this.f4568v.connect(new InetSocketAddress(n(), this.f4566t));
            System.out.println("Listen on " + ((Object) this.f4568v.getLocalAddress()) + " from " + ((Object) this.f4568v.getInetAddress()) + " port " + this.f4568v.getPort());
        }
        return this.f4568v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() throws IOException {
        if (k() == null) {
            throw new IOException("UDP Logger Socket closed");
        }
        DatagramPacket datagramPacket = new DatagramPacket(this.f4575s.b(TFTP.DEFAULT_TIMEOUT), TFTP.DEFAULT_TIMEOUT);
        k().receive(datagramPacket);
        this.f4572y.add(datagramPacket);
        synchronized (this.f4572y) {
            this.f4572y.notify();
        }
    }

    private byte[] p() throws IOException {
        if (this.f4574r == null || !this.f4574r.isAlive()) {
            this.f4574r = new m(this);
            this.f4574r.start();
        }
        if (this.f4572y.isEmpty()) {
            synchronized (this.f4572y) {
                try {
                    this.f4572y.wait();
                } catch (InterruptedException e2) {
                    Logger.getLogger(k.class.getName()).log(Level.INFO, "Hmmm", (Throwable) e2);
                }
            }
        }
        if (this.f4572y.isEmpty()) {
            C.b("Waited, but packetStack is still empty.");
            throw new IOException("Waited, but packetStack is still empty.");
        }
        DatagramPacket datagramPacket = (DatagramPacket) this.f4572y.remove(0);
        byte[] bArrB = this.f4575s.b(datagramPacket.getLength());
        System.arraycopy(datagramPacket.getData(), 0, bArrB, 0, bArrB.length);
        return bArrB;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() throws IOException {
        byte[] bArrP = p();
        int[] iArrB = C0995c.b(bArrP);
        this.f4575s.a(bArrP);
        if (J.I()) {
            C.c("Received UDP Logger Packet:\n" + C0995c.b(iArrB));
        }
        a(iArrB);
    }

    public int l() {
        return this.f4566t;
    }

    public void d(int i2) {
        this.f4566t = i2;
    }

    public String m() {
        return this.f4567u;
    }

    public void h(String str) {
        this.f4567u = str;
    }
}
