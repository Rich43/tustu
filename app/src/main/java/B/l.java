package B;

import G.C0129l;
import bH.C;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:B/l.class */
public class l extends A.a {

    /* renamed from: l, reason: collision with root package name */
    private int f169l = 0;

    /* renamed from: m, reason: collision with root package name */
    private Socket f170m = null;

    /* renamed from: c, reason: collision with root package name */
    String f171c = A.l.a();

    /* renamed from: d, reason: collision with root package name */
    protected int f172d = A.l.b();

    /* renamed from: e, reason: collision with root package name */
    int f173e = 2000;

    /* renamed from: f, reason: collision with root package name */
    int f174f = 2000;

    /* renamed from: g, reason: collision with root package name */
    InputStream f175g = null;

    /* renamed from: h, reason: collision with root package name */
    OutputStream f176h = null;

    /* renamed from: i, reason: collision with root package name */
    List f177i = null;

    /* renamed from: b, reason: collision with root package name */
    public static String f168b = "TCP/IP - WiFi driver";

    /* renamed from: j, reason: collision with root package name */
    public static String f178j = "IP Address";

    /* renamed from: k, reason: collision with root package name */
    public static String f179k = RuntimeModeler.PORT;

    @Override // A.f
    public void f() throws C0129l {
        if (k() == 3) {
            throw new C0129l("TCP Device already connected:" + v());
        }
        if (k() == 2) {
            throw new C0129l("TCP Device already connecting:" + v());
        }
        if (this.f171c == null || this.f171c.equals("")) {
            throw new C0129l("IP Address or host name not set! Can not open Connection ");
        }
        if (this.f172d <= 0) {
            throw new C0129l("Invalid Port:" + this.f172d + " Can not open WiFi ");
        }
        b(2);
        c();
        try {
            C.c("Opening Connection to TCP Device: " + v());
            this.f170m = new Socket();
            this.f170m.setTcpNoDelay(true);
            this.f170m.setKeepAlive(true);
            this.f170m.setSoTimeout(this.f173e);
            this.f170m.setTrafficClass(20);
            this.f170m.connect(new InetSocketAddress(this.f171c, this.f172d), this.f174f);
            C.d("Connected to: " + this.f170m.toString());
            this.f175g = this.f170m.getInputStream();
            this.f176h = this.f170m.getOutputStream();
            b(3);
            a();
        } catch (UnknownHostException e2) {
            b(0);
            b();
            throw new C0129l("host not found:" + this.f171c + CallSiteDescriptor.TOKEN_DELIMITER + this.f172d + " Can not connect TCP Device");
        } catch (IOException e3) {
            b(0);
            b();
            throw new C0129l("Unable to connect to: " + this.f171c + CallSiteDescriptor.TOKEN_DELIMITER + this.f172d + ". Time out.");
        } catch (Exception e4) {
            b(0);
            b();
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            throw new C0129l("Unable to open device: " + e4.getLocalizedMessage() + ", " + v());
        }
    }

    @Override // A.f
    public boolean r() {
        C.d("Re-establishing connection to: " + n());
        boolean z2 = false;
        if (k() == 3) {
            try {
                if (this.f170m != null) {
                    try {
                        this.f170m.close();
                    } catch (IOException e2) {
                        C.c("Error closing socket.");
                    }
                }
                C.c("Refresh socket to TCP Device: " + v());
                this.f170m = new Socket();
                this.f170m.connect(new InetSocketAddress(this.f171c, this.f172d), this.f174f);
                this.f170m.setSoTimeout(this.f173e);
                this.f170m.setTcpNoDelay(true);
                this.f175g = this.f170m.getInputStream();
                this.f176h = this.f170m.getOutputStream();
                z2 = true;
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return z2;
    }

    protected void t() {
        if (this.f175g != null) {
            try {
                this.f175g.close();
            } catch (Exception e2) {
            }
        }
        if (this.f176h != null) {
            try {
                this.f176h.close();
            } catch (Exception e3) {
            }
        }
        this.f175g = null;
        this.f176h = null;
        this.f170m = null;
    }

    @Override // A.f
    public void g() {
        try {
            if (this.f170m != null) {
                b(4);
                e();
                try {
                    this.f170m.close();
                    d();
                    this.f170m = null;
                } catch (Exception e2) {
                    C.c("Error closing TCP Connection");
                    Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        } finally {
            b(0);
        }
    }

    @Override // A.f
    public String h() {
        return f168b;
    }

    @Override // A.f
    public InputStream i() {
        return this.f175g;
    }

    protected void a(InputStream inputStream) {
        this.f175g = inputStream;
    }

    @Override // A.f
    public OutputStream j() {
        return this.f176h;
    }

    protected void a(OutputStream outputStream) {
        this.f176h = outputStream;
    }

    protected void a(Socket socket) {
        this.f170m = socket;
        this.f171c = socket.getInetAddress().getHostAddress();
    }

    @Override // A.f
    public int k() {
        return this.f169l;
    }

    @Override // A.f
    public List l() {
        if (this.f177i == null) {
            this.f177i = new ArrayList();
            A.b bVar = new A.b();
            bVar.a(f179k);
            bVar.b("TCP IP Port");
            bVar.a(3);
            bVar.b(0.0d);
            bVar.a(20000.0d);
            this.f177i.add(bVar);
            A.b bVar2 = new A.b();
            bVar2.a(f178j);
            bVar2.b("IP Address or host name of ECU Adapter");
            bVar2.a(1);
            this.f177i.add(bVar2);
        }
        return this.f177i;
    }

    @Override // A.f
    public void a(String str, Object obj) throws A.s {
        try {
            if (str.equals(f179k)) {
                this.f172d = Integer.parseInt(obj.toString());
            } else {
                if (!str.equals(f178j)) {
                    C.c("Unknown Setting Name: " + str);
                    throw new A.s("Unknown Setting Name: " + str);
                }
                this.f171c = obj.toString();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new A.s(obj.toString() + "not a valid value for Setting: " + str);
        }
    }

    @Override // A.f
    public Object a(String str) {
        if (str.equals(f179k)) {
            return Integer.valueOf(this.f172d);
        }
        if (str.equals(f178j)) {
            return this.f171c;
        }
        C.c("Unknown Setting Name: " + str);
        return null;
    }

    public void b(int i2) {
        this.f169l = i2;
        if (i2 == 0) {
            t();
        }
    }

    private String v() {
        return (this.f170m == null || this.f170m.getInetAddress() == null) ? n() : "Connected IP Address:" + this.f170m.getInetAddress().getHostAddress() + CallSiteDescriptor.TOKEN_DELIMITER + this.f170m.getPort();
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
        return this.f171c + CallSiteDescriptor.TOKEN_DELIMITER + this.f172d;
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

    public int u() {
        return this.f172d;
    }

    @Override // A.f
    public int s() {
        return 2;
    }
}
