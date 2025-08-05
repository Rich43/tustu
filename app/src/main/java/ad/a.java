package aD;

import G.C0129l;
import G.J;
import bH.C;
import bH.I;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;
import sun.java2d.marlin.MarlinConst;
import z.i;
import z.m;

/* loaded from: TunerStudioMS.jar:aD/a.class */
public class a extends aB.a implements A.f {

    /* renamed from: e, reason: collision with root package name */
    f f2299e = new f();

    /* renamed from: f, reason: collision with root package name */
    SerialPort f2300f = null;

    /* renamed from: i, reason: collision with root package name */
    List f2303i = null;

    /* renamed from: j, reason: collision with root package name */
    String f2304j;

    /* renamed from: k, reason: collision with root package name */
    String f2305k;

    /* renamed from: l, reason: collision with root package name */
    String f2306l;

    /* renamed from: m, reason: collision with root package name */
    int f2307m;

    /* renamed from: n, reason: collision with root package name */
    String f2308n;

    /* renamed from: o, reason: collision with root package name */
    int f2309o;

    /* renamed from: p, reason: collision with root package name */
    InputStream f2310p;

    /* renamed from: q, reason: collision with root package name */
    e f2311q;

    /* renamed from: r, reason: collision with root package name */
    int f2312r;

    /* renamed from: s, reason: collision with root package name */
    ArrayList f2313s;

    /* renamed from: t, reason: collision with root package name */
    i f2314t;

    /* renamed from: u, reason: collision with root package name */
    boolean f2315u;

    /* renamed from: w, reason: collision with root package name */
    long f2317w;

    /* renamed from: c, reason: collision with root package name */
    public static String f2297c = "115200";

    /* renamed from: d, reason: collision with root package name */
    public static String f2298d = "RS232 Serial Interface";

    /* renamed from: g, reason: collision with root package name */
    static Map f2301g = new HashMap();

    /* renamed from: h, reason: collision with root package name */
    static Map f2302h = new HashMap();

    /* renamed from: v, reason: collision with root package name */
    public static boolean f2316v = true;

    /* renamed from: x, reason: collision with root package name */
    static c f2318x = null;

    public a() {
        this.f2304j = I.a() ? "COM1" : "/dev/ttyUSB0";
        this.f2305k = "";
        this.f2306l = this.f2304j;
        this.f2307m = 0;
        this.f2308n = f2297c;
        this.f2309o = 0;
        this.f2310p = null;
        this.f2311q = null;
        this.f2312r = 0;
        this.f2313s = new ArrayList();
        this.f2314t = new i();
        this.f2315u = false;
        this.f2317w = 0L;
        if (f2318x == null || !f2318x.isAlive()) {
            f2318x = new c(this);
            f2318x.start();
        }
        f2318x.a(this);
    }

    @Override // A.f
    public void f() throws C0129l {
        if ((this.f2304j == null || this.f2304j.isEmpty()) && (this.f2305k == null || this.f2305k.isEmpty())) {
            throw new C0129l("Com Port Not Set!");
        }
        if (this.f2307m % 2 == 0 || this.f2305k == null || this.f2305k.isEmpty()) {
            this.f2306l = this.f2304j;
        } else {
            this.f2306l = this.f2305k;
        }
        if (this.f2308n == null) {
            throw new C0129l("Baud Rate Not Set!");
        }
        this.f2306l = this.f2306l.trim();
        if (J.I()) {
            C.d("Opening port: " + this.f2306l + " @ " + this.f2308n);
        }
        try {
            this.f2312r = 2;
            b();
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (0 != 0) {
                C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - added Port");
            }
            this.f2300f = new SerialPort(this.f2306l);
            if (0 != 0) {
                C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - 1st got port");
            }
            if (f2302h.get(this.f2306l) != null) {
                SerialPort serialPort = (SerialPort) f2302h.get(this.f2306l);
                try {
                    C.b(this.f2306l + " port instance already found, trying to close.");
                    serialPort.closePort();
                } catch (Exception e2) {
                    C.b("Found port instance, could not close.");
                }
            }
            this.f2300f.openPort();
            if (0 != 0) {
                C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - opened port");
            }
            a(this.f2306l, this.f2300f);
            t();
            if (0 != 0) {
                C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - setParameters");
            }
            this.f2310p = new d(this.f2300f);
            this.f2311q = new e(this.f2300f);
            this.f2312r = 3;
            this.f2317w = System.currentTimeMillis();
            c();
        } catch (Exception e3) {
            if (this.f2300f != null) {
                try {
                    if (J.I()) {
                        C.d("Error creating streams to port, closing port to cleanup.");
                    }
                    this.f2300f.closePort();
                    b(this.f2306l);
                } catch (Exception e4) {
                    if (J.I()) {
                        C.d("Error closing port on cleanup. " + e4.getMessage());
                    }
                }
            }
            this.f2300f = null;
            this.f2310p = null;
            this.f2311q = null;
            this.f2312r = 0;
            d();
            this.f2307m++;
            if (0 != 0) {
                C.c("Unable to open port: " + this.f2306l + "\nPlease check your Communications Settings. ");
            }
            throw new C0129l("Unable to open port: " + this.f2306l + "\nPlease check your Communications Settings. ");
        }
    }

    private void a(String str, SerialPort serialPort) {
        f2301g.put(str, Thread.currentThread());
        f2302h.put(str, serialPort);
    }

    private void b(String str) {
        f2301g.remove(str);
        f2302h.remove(str);
    }

    @Override // A.f
    public boolean r() {
        if (!this.f2315u) {
            C.d("Re-establishing connection to: " + n());
            g();
            try {
                f();
            } catch (C0129l e2) {
                C.b("Failed to re-establish connection to: " + n());
            }
        }
        return k() == 3;
    }

    @Override // A.f
    public void g() {
        if (this.f2300f != null) {
            if (J.I()) {
                C.c("JSSC ControllerInterface Close Connection currentPort == null:" + (this.f2300f == null));
            }
            this.f2312r = 4;
            e();
            if (System.currentTimeMillis() - this.f2317w > MarlinConst.statDump) {
                this.f2307m = 0;
            } else if (this.f2300f != null) {
                this.f2307m++;
            }
            new b(this, this.f2300f, this.f2310p, this.f2311q);
            this.f2300f = null;
            this.f2310p = null;
            this.f2311q = null;
        }
    }

    @Override // A.f
    public String h() {
        return f2298d;
    }

    @Override // A.f
    public InputStream i() {
        if (this.f2310p == null) {
            return null;
        }
        return this.f2310p;
    }

    @Override // A.f
    public OutputStream j() {
        if (this.f2311q == null) {
            return null;
        }
        return this.f2311q;
    }

    @Override // A.f
    public int k() {
        return this.f2312r;
    }

    @Override // A.f
    public List l() {
        boolean zD = I.d();
        this.f2303i = new ArrayList();
        A.b bVar = new A.b();
        bVar.a("Com Port");
        bVar.b("Communication Port using RS232 or Virtual RS232 Serial communications.");
        bVar.a(0);
        String[] strArrB = this.f2314t.b();
        for (String str : strArrB) {
            bVar.a((Object) str);
        }
        this.f2303i.add(bVar);
        if (zD) {
            A.b bVar2 = new A.b();
            bVar2.a("2nd Com Port");
            bVar2.b("An optional 2nd Communication Port. If the main selected port fails, the next connection attempt will be made using this port. This is used in scenarios such as on Linux where sometimes your USB adapter will be reassigned to ttyUSB1 instead of ttyUSB0");
            bVar2.a(0);
            bVar2.a((Object) "");
            for (String str2 : strArrB) {
                bVar2.a((Object) str2);
            }
            this.f2303i.add(bVar2);
        }
        A.b bVar3 = new A.b();
        bVar3.a("Baud Rate");
        bVar3.b("Baud rate to use for RS232 Serial communications.");
        bVar3.a(0);
        for (String str3 : u()) {
            bVar3.a((Object) str3);
        }
        this.f2303i.add(bVar3);
        if (f2316v) {
            A.b bVar4 = new A.b();
            bVar4.a("Bluetooth Port");
            if (I.a()) {
                bVar4.b("On Windows Bluetooth Direct should normally be used for Bluetooth connections instead of virtual comms! \n\nIf active, communication timings will be optimized for Bluetooth Connections, but are less than optimal for USB to RS232 cables and true Serial Ports.");
            } else {
                bVar4.b("If active, communication timings will be optimized for Bluetooth Connections, but are less than optimal for USB to RS232 cables and true Serial Ports.");
            }
            bVar4.a(6);
            this.f2303i.add(bVar4);
        }
        return this.f2303i;
    }

    @Override // A.f
    public void a(String str, Object obj) {
        if (str.equals("Com Port")) {
            this.f2304j = obj.toString();
            this.f2306l = this.f2304j;
            return;
        }
        if (str.equals("2nd Com Port")) {
            this.f2305k = obj.toString();
            return;
        }
        if (str.equals("Baud Rate")) {
            this.f2308n = obj.toString();
            C.c("set baud to " + this.f2308n);
        } else if (str.equals("Bluetooth Port")) {
            this.f2315u = f2316v && Boolean.parseBoolean(obj.toString());
        } else {
            C.c("Unknown Setting Name: " + str);
        }
    }

    protected void t() throws m {
        if (this.f2300f == null) {
            C.b("currentPort is null, can not setConnectionParameters");
            return;
        }
        try {
            this.f2300f.setRTS(false);
            this.f2300f.setDTR(false);
        } catch (SerialPortException e2) {
            C.d("Failed to set RTS or DTR");
        }
        this.f2299e.a(this.f2308n);
        try {
            this.f2300f.setParams(this.f2299e.a(), this.f2299e.d(), this.f2299e.e(), this.f2299e.f());
            try {
                this.f2300f.setFlowControlMode(this.f2299e.b() | this.f2299e.c());
            } catch (SerialPortException e3) {
                throw new m("Unsupported flow control");
            }
        } catch (SerialPortException e4) {
            throw new m("Unsupported parameter");
        }
    }

    @Override // A.f
    public boolean a(int i2) {
        String str = this.f2308n;
        this.f2308n = i2 + "";
        try {
            t();
            return true;
        } catch (m e2) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            this.f2308n = str;
            try {
                t();
                return false;
            } catch (m e3) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                return false;
            }
        }
    }

    protected void a(SerialPort serialPort, InputStream inputStream, e eVar) {
        try {
            if (eVar != null) {
                try {
                    try {
                        eVar.flush();
                        eVar.close();
                    } catch (Throwable th) {
                        if (J.I()) {
                            C.d("Closing Port");
                        }
                        this.f2312r = 0;
                        b(this.f2306l);
                        serialPort.closePort();
                        if (J.I()) {
                            C.d("Successfully Closed Port");
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    if (J.I()) {
                        C.d("Closing Port");
                    }
                    this.f2312r = 0;
                    b(this.f2306l);
                    serialPort.closePort();
                    if (J.I()) {
                        C.d("Successfully Closed Port");
                    }
                }
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (J.I()) {
                C.d("Closing Port");
            }
            this.f2312r = 0;
            b(this.f2306l);
            serialPort.closePort();
            if (J.I()) {
                C.d("Successfully Closed Port");
            }
        } catch (Exception e3) {
            C.c("can not close Port: " + ((Object) serialPort) + ", message: " + e3.getMessage());
        }
    }

    @Override // A.f
    public Object a(String str) {
        if (str.equals("Com Port")) {
            return this.f2304j;
        }
        if (str.equals("Baud Rate")) {
            return this.f2308n;
        }
        if (str.equals("Bluetooth Port")) {
            return Boolean.valueOf(this.f2315u);
        }
        if (str.equals("2nd Com Port")) {
            return this.f2305k;
        }
        C.c("Unknown Setting Name: " + str);
        return null;
    }

    private String[] u() {
        return new String[]{"1200", "2400", "4800", "9600", "14400", "19200", "28800", "38400", "57600", "115200", "150000", "230400", "250000", "460800", "500000"};
    }

    @Override // A.f
    public boolean m() {
        return !this.f2315u;
    }

    @Override // A.f
    public String n() {
        return "RS232: Port:" + this.f2306l + ", Baud:" + this.f2308n;
    }

    @Override // A.f
    public int o() {
        return this.f2315u ? 1000 : 0;
    }

    @Override // A.f
    public int p() {
        if (this.f2315u) {
            return 4000;
        }
        return I.d() ? 400 : 400;
    }

    @Override // A.f
    public boolean q() {
        return false;
    }

    @Override // A.f
    public int s() {
        return 1;
    }
}
