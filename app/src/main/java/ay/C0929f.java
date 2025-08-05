package ay;

import bH.C;
import bH.W;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/* renamed from: ay.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/f.class */
public class C0929f {

    /* renamed from: e, reason: collision with root package name */
    private int f6440e;

    /* renamed from: f, reason: collision with root package name */
    private C0930g f6443f;

    /* renamed from: c, reason: collision with root package name */
    List f6444c;

    /* renamed from: g, reason: collision with root package name */
    private boolean f6445g;

    /* renamed from: a, reason: collision with root package name */
    public static String f6441a = "Service:";

    /* renamed from: b, reason: collision with root package name */
    public static String f6442b = "DISCOVER_HTTP_SERVICES";

    /* renamed from: h, reason: collision with root package name */
    private static final Map f6446h = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    static boolean f6447d = true;

    private C0929f() {
        this.f6440e = 21852;
        this.f6443f = null;
        this.f6444c = new ArrayList();
        this.f6445g = false;
    }

    private C0929f(int i2) {
        this.f6440e = 21852;
        this.f6443f = null;
        this.f6444c = new ArrayList();
        this.f6445g = false;
        this.f6440e = i2;
    }

    public static C0929f a(int i2) {
        C0929f c0929f = (C0929f) f6446h.get(Integer.valueOf(i2));
        if (c0929f == null) {
            c0929f = new C0929f(i2);
            f6446h.put(Integer.valueOf(i2), c0929f);
        }
        return c0929f;
    }

    public static C0929f a() {
        return a(21852);
    }

    public void a(InterfaceC0928e interfaceC0928e) {
        this.f6444c.add(interfaceC0928e);
    }

    public void b() {
        if (this.f6443f != null && this.f6443f.isAlive()) {
            this.f6443f.a();
        } else {
            this.f6443f = new C0930g(this);
            this.f6443f.start();
        }
    }

    private void a(C0926c c0926c) {
        Iterator it = this.f6444c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0928e) it.next()).a(c0926c);
        }
    }

    private void c() {
        Iterator it = this.f6444c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0928e) it.next()).a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        Iterator it = this.f6444c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0928e) it.next()).b();
        }
    }

    private void a(String str, String str2) {
        ArrayList arrayList = new ArrayList();
        C0926c c0926c = null;
        StringTokenizer stringTokenizer = new StringTokenizer(str2, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            try {
                String strNextToken = stringTokenizer.nextToken();
                if (strNextToken.startsWith(f6441a)) {
                    c0926c = new C0926c(strNextToken.substring(f6441a.length()), str);
                    arrayList.add(c0926c);
                } else if (c0926c != null) {
                    c0926c.a(strNextToken.substring(0, strNextToken.indexOf("=")), a(c0926c, strNextToken.substring(strNextToken.indexOf("=") + 1, strNextToken.length())));
                } else {
                    C.c("Service Attribute given, but no service defined? " + strNextToken);
                }
            } catch (Exception e2) {
            }
        }
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            a((C0926c) it.next());
        }
    }

    private String a(C0926c c0926c, String str) {
        if (str.contains("$ipAddress")) {
            str = W.b(str, "$ipAddress", c0926c.c());
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() throws IOException {
        c();
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setBroadcast(true);
        this.f6445g = true;
        byte[] bytes = f6442b.getBytes();
        datagramSocket.send(new DatagramPacket(bytes, bytes.length, InetAddress.getByName("255.255.255.255"), this.f6440e));
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterfaceNextElement2 = networkInterfaces.nextElement2();
            if (!networkInterfaceNextElement2.isLoopback() && networkInterfaceNextElement2.isUp()) {
                Iterator<InterfaceAddress> it = networkInterfaceNextElement2.getInterfaceAddresses().iterator();
                while (it.hasNext()) {
                    InetAddress broadcast = it.next().getBroadcast();
                    if (broadcast != null) {
                        try {
                            datagramSocket.send(new DatagramPacket(bytes, bytes.length, broadcast, this.f6440e));
                        } catch (Exception e2) {
                        }
                    }
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        long jCurrentTimeMillis = System.currentTimeMillis() + 1500;
        datagramSocket.setSoTimeout(2500);
        do {
            byte[] bArr = new byte[15000];
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
            try {
                datagramSocket.receive(datagramPacket);
                String strTrim = new String(datagramPacket.getData()).trim();
                String str = "" + datagramPacket.getAddress().getHostAddress();
                String str2 = str + strTrim;
                if (!arrayList.contains(str2) && strTrim.startsWith(f6441a)) {
                    a(str, strTrim);
                    arrayList.add(str2);
                    if (0 != 0) {
                        a("192.168.0.122", f6441a + "LogFileServer\nLink=http://192.168.4.77:16680/LogFileActions\nsupportsList=true\nsupportsDownload=true\nsupportsDelete=true\n");
                    }
                }
            } catch (SocketTimeoutException e3) {
            }
        } while (jCurrentTimeMillis - System.currentTimeMillis() > 0);
        datagramSocket.close();
        this.f6445g = false;
    }
}
