package B;

import G.C0129l;
import bH.C;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

/* loaded from: TunerStudioMS.jar:B/o.class */
public class o extends A.a {

    /* renamed from: k, reason: collision with root package name */
    private int f185k = 0;

    /* renamed from: l, reason: collision with root package name */
    private DatagramSocket f186l = null;

    /* renamed from: c, reason: collision with root package name */
    String f187c = A.l.a();

    /* renamed from: d, reason: collision with root package name */
    protected int f188d = A.l.b();

    /* renamed from: m, reason: collision with root package name */
    private int f189m = 500;

    /* renamed from: e, reason: collision with root package name */
    int f190e = 2000;

    /* renamed from: f, reason: collision with root package name */
    p f191f = null;

    /* renamed from: g, reason: collision with root package name */
    r f192g = null;

    /* renamed from: h, reason: collision with root package name */
    List f193h = null;

    /* renamed from: n, reason: collision with root package name */
    private boolean f196n = false;

    /* renamed from: b, reason: collision with root package name */
    public static String f184b = "UDP - User Datagram Protocol";

    /* renamed from: i, reason: collision with root package name */
    public static String f194i = "IP Address";

    /* renamed from: j, reason: collision with root package name */
    public static String f195j = RuntimeModeler.PORT;

    @Override // A.f
    public void f() throws C0129l {
        if (k() == 3) {
            throw new C0129l("UDP Device already connected:" + y());
        }
        if (k() == 2) {
            throw new C0129l("UDP Device already connecting:" + y());
        }
        if (this.f187c == null || this.f187c.equals("")) {
            throw new C0129l("IP Address or host name not set! Can not open connection ");
        }
        if (this.f188d <= 0) {
            throw new C0129l("Invalid Port:" + this.f188d + " Can not open WiFi ");
        }
        b(2);
        c();
        try {
            C.c("Opening Connection to UDP Device: " + y());
            this.f186l = t();
            this.f186l.setSoTimeout(A());
            this.f186l.setTrafficClass(20);
            this.f192g = new r(this.f186l);
            this.f191f = new p(this.f186l, this.f192g);
            b(3);
            a();
        } catch (UnknownHostException e2) {
            b(0);
            b();
            throw new C0129l("host not found:" + this.f187c + CallSiteDescriptor.TOKEN_DELIMITER + this.f188d + " Can not connect UDP Device");
        } catch (IOException e3) {
            b(0);
            b();
            throw new C0129l("Unable to connect to: " + this.f187c + CallSiteDescriptor.TOKEN_DELIMITER + this.f188d + ". Time out.");
        } catch (Exception e4) {
            b(0);
            b();
            Logger.getLogger(o.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            throw new C0129l("Unable to open device: " + e4.getLocalizedMessage() + ", " + y());
        }
    }

    protected DatagramSocket t() throws SocketException {
        if (this.f186l == null || this.f186l.isClosed()) {
            this.f186l = new DatagramSocket((SocketAddress) null);
            this.f186l.setBroadcast(true);
            if (this.f196n) {
                this.f186l.bind(new InetSocketAddress(InetAddress.getLocalHost(), this.f188d));
            } else {
                this.f186l.connect(new InetSocketAddress(InetAddress.getByName(this.f187c), this.f188d));
            }
            System.out.println("Listen on " + ((Object) this.f186l.getLocalAddress()) + " from " + ((Object) this.f186l.getInetAddress()) + " port " + this.f186l.getPort());
        }
        return this.f186l;
    }

    @Override // A.f
    public boolean r() {
        C.d("Re-establishing connection to: " + n());
        boolean z2 = false;
        if (k() == 3) {
            try {
                if (this.f186l != null) {
                    try {
                        this.f186l.close();
                    } catch (Exception e2) {
                        C.c("Error closing socket.");
                    }
                }
                C.c("Refresh socket to UDP Device: " + y());
                this.f186l = t();
                this.f192g = new r(this.f186l);
                this.f191f = new p(this.f186l, this.f192g);
                z2 = true;
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return z2;
    }

    protected void u() {
        if (this.f191f != null) {
            try {
                this.f191f.close();
            } catch (Exception e2) {
            }
        }
        if (this.f192g != null) {
            try {
                this.f192g.close();
            } catch (Exception e3) {
            }
        }
        this.f191f = null;
        this.f192g = null;
        this.f186l = null;
    }

    @Override // A.f
    public void g() {
        try {
            if (this.f186l != null) {
                b(4);
                u();
                e();
                try {
                    this.f186l.close();
                    this.f186l = null;
                } catch (Exception e2) {
                    C.c("Error closing UDP Connection");
                }
            }
        } finally {
            d();
            b(0);
        }
    }

    @Override // A.f
    public String h() {
        return f184b;
    }

    @Override // A.f
    public InputStream i() {
        return this.f191f;
    }

    @Override // A.f
    public OutputStream j() {
        return this.f192g;
    }

    @Override // A.f
    public int k() {
        return this.f185k;
    }

    @Override // A.f
    public List l() {
        if (this.f193h == null) {
            this.f193h = new ArrayList();
            A.b bVar = new A.b();
            bVar.a(f195j);
            bVar.b("UDP Port");
            bVar.a(3);
            bVar.b(0.0d);
            bVar.a(20000.0d);
            this.f193h.add(bVar);
            A.b bVar2 = new A.b();
            bVar2.a(f194i);
            bVar2.b("IP Address or host name of the target device");
            bVar2.a(1);
            this.f193h.add(bVar2);
        }
        return this.f193h;
    }

    @Override // A.f
    public void a(String str, Object obj) throws A.s {
        try {
            if (str.equals(f195j)) {
                this.f188d = Integer.parseInt(obj.toString());
            } else {
                if (!str.equals(f194i)) {
                    C.c("Unknown Setting Name: " + str);
                    throw new A.s("Unknown Setting Name: " + str);
                }
                this.f187c = obj.toString();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new A.s(obj.toString() + "not a valid value for Setting: " + str);
        }
    }

    @Override // A.f
    public Object a(String str) {
        if (str.equals(f195j)) {
            return Integer.valueOf(this.f188d);
        }
        if (str.equals(f194i)) {
            return this.f187c;
        }
        C.c("Unknown Setting Name: " + str);
        return null;
    }

    public void b(int i2) {
        this.f185k = i2;
        if (i2 == 0) {
            u();
        }
    }

    protected String y() {
        return (this.f186l == null || this.f186l.getInetAddress() == null) ? n() : "Connected IP Address:" + this.f186l.getInetAddress().getHostAddress() + CallSiteDescriptor.TOKEN_DELIMITER + this.f186l.getPort();
    }

    @Override // A.f
    public boolean m() {
        return false;
    }

    @Override // A.f
    public boolean a(int i2) {
        return false;
    }

    @Override // A.f
    public String n() {
        return this.f187c + CallSiteDescriptor.TOKEN_DELIMITER + this.f188d;
    }

    @Override // A.f
    public int o() {
        return 1500;
    }

    @Override // A.f
    public int p() {
        return 1500;
    }

    @Override // A.f
    public boolean q() {
        return true;
    }

    public int z() {
        if (this.f186l != null && !this.f186l.isClosed()) {
            try {
                this.f186l.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return this.f188d;
    }

    @Override // A.f
    public int s() {
        return 2;
    }

    public int A() {
        return this.f189m;
    }

    public void c(int i2) {
        this.f189m = i2;
    }

    public void a(boolean z2) {
        this.f196n = z2;
    }

    protected void a(DatagramSocket datagramSocket) {
        this.f186l = datagramSocket;
        if (this.f192g != null) {
            this.f192g.a(datagramSocket);
            this.f191f.a(datagramSocket);
            this.f191f.a(this.f192g);
        }
        this.f192g = new r(datagramSocket);
        this.f191f = new p(datagramSocket, this.f192g);
        this.f187c = datagramSocket.getInetAddress().getHostAddress();
    }
}
