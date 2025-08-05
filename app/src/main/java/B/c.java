package B;

import G.R;
import bH.C;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Types;

/* loaded from: TunerStudioMS.jar:B/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    public static String f101a = "slave";

    /* renamed from: b, reason: collision with root package name */
    public static String f102b = "id";

    /* renamed from: c, reason: collision with root package name */
    public static String f103c = "serial";

    /* renamed from: d, reason: collision with root package name */
    public static String f104d = "info";

    /* renamed from: e, reason: collision with root package name */
    public static String f105e = DeploymentDescriptorParser.ATTR_PORT;

    /* renamed from: f, reason: collision with root package name */
    public static String f106f = "name";

    /* renamed from: g, reason: collision with root package name */
    public static String f107g = "protocol";

    /* renamed from: h, reason: collision with root package name */
    public static String f108h = "connectionState";

    /* renamed from: i, reason: collision with root package name */
    public static String f109i = "canId";

    /* renamed from: j, reason: collision with root package name */
    public static String f110j = "projectUUID";

    /* renamed from: k, reason: collision with root package name */
    public static String f111k = "CAN_DEVICE_";

    /* renamed from: l, reason: collision with root package name */
    public static String f112l = "DISCOVER_SLAVE_SERVER";

    /* renamed from: m, reason: collision with root package name */
    R f115m;

    /* renamed from: n, reason: collision with root package name */
    private int f100n = 21846;

    /* renamed from: o, reason: collision with root package name */
    private int f113o = Types.DISTINCT;

    /* renamed from: p, reason: collision with root package name */
    private String f114p = bT.o.f7604a;

    /* renamed from: q, reason: collision with root package name */
    private String f116q = "TCP";

    /* renamed from: r, reason: collision with root package name */
    private d f117r = null;

    /* renamed from: s, reason: collision with root package name */
    private DatagramSocket f118s = null;

    public c(R r2) {
        this.f115m = r2;
    }

    public void a() {
        if (this.f117r != null) {
            this.f117r.a();
        }
    }

    public void b() {
        a();
        this.f117r = new d(this);
        this.f117r.start();
    }

    public int c() {
        return this.f100n;
    }

    public void a(int i2) {
        this.f100n = i2;
    }

    public void b(int i2) {
        this.f113o = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DatagramSocket d() throws SocketException {
        if (this.f118s == null || this.f118s.isClosed()) {
            this.f118s = new DatagramSocket(c(), InetAddress.getByName("0.0.0.0"));
            this.f118s.setBroadcast(true);
            System.out.println("Listen on " + ((Object) this.f118s.getLocalAddress()) + " from " + ((Object) this.f118s.getInetAddress()) + " port " + this.f118s.getPort());
        }
        return this.f118s;
    }

    public void a(String str) {
        this.f116q = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String e() {
        try {
            return f.f.k();
        } catch (Exception e2) {
            C.c("Failed to get MAC for serial#, msg: " + e2.getMessage());
            return null;
        }
    }
}
