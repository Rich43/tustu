package E;

import bH.C;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:E/c.class */
public class c extends Thread {

    /* renamed from: i, reason: collision with root package name */
    private boolean f250i;

    /* renamed from: j, reason: collision with root package name */
    private int f251j;

    /* renamed from: a, reason: collision with root package name */
    DatagramSocket f252a;

    /* renamed from: k, reason: collision with root package name */
    private int f253k;

    /* renamed from: b, reason: collision with root package name */
    boolean f254b;

    /* renamed from: c, reason: collision with root package name */
    Object f255c;

    /* renamed from: l, reason: collision with root package name */
    private b f256l;

    /* renamed from: d, reason: collision with root package name */
    InetAddress f257d;

    /* renamed from: m, reason: collision with root package name */
    private NetworkInterface f258m;

    /* renamed from: e, reason: collision with root package name */
    static int f259e = 0;

    /* renamed from: f, reason: collision with root package name */
    static Map f260f = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    static List f261g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    static Map f262h = new HashMap();

    public c(NetworkInterface networkInterface, boolean z2) {
        super("DHCP: " + networkInterface.getName());
        this.f250i = true;
        this.f251j = 67;
        this.f252a = null;
        this.f253k = 1500;
        this.f255c = new Object();
        this.f258m = null;
        this.f254b = z2;
        this.f258m = networkInterface;
        if (f262h.get(networkInterface.getName()) == null) {
            f262h.put(networkInterface.getName(), new e());
        }
    }

    public static void a() {
        new d("DHCP Monitor").start();
    }

    public void b() {
        this.f250i = false;
        if (this.f252a != null && !this.f252a.isClosed()) {
            this.f252a.close();
        }
        synchronized (this.f255c) {
            this.f255c.notify();
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            c();
        } catch (Exception e2) {
        } finally {
            C.c("DatagramSocket server shutdown: " + this.f258m.getName());
            f260f.remove(this.f258m.getName());
        }
    }

    private void c() {
        while (this.f257d == null) {
            try {
                this.f258m = NetworkInterface.getByName(this.f258m.getName());
                if (this.f258m != null) {
                    this.f257d = j.a(this.f258m);
                } else {
                    this.f257d = null;
                }
                if (!this.f250i) {
                    return;
                } else {
                    Thread.sleep(500L);
                }
            } catch (Exception e2) {
                int i2 = f259e;
                f259e = i2 + 1;
                if (i2 <= 3) {
                    C.a("DHCP Server failed to start after Retrying. Error: " + e2.getLocalizedMessage());
                    return;
                } else {
                    C.a("DHCP Server failed to start after 3 attempts, giving up.");
                    f261g.add(this.f258m.getName());
                    return;
                }
            }
        }
        C.d("Got address for " + this.f258m.getDisplayName() + ": " + ((Object) this.f257d));
        if (this.f257d.getAddress() != null && this.f257d.getAddress().length == 4 && this.f257d.getAddress()[0] == 172) {
            C.c("Private Network NIC, ending.");
            f261g.add(this.f258m.getName());
            return;
        }
        this.f256l = new b(new F.a(j.a(this.f257d)), this.f257d);
        ((e) f262h.get(this.f258m.getName())).a(this.f256l);
        if (this.f250i) {
            if (this.f254b) {
                this.f252a = d();
            }
            if (this.f250i) {
                try {
                    DatagramSocket datagramSocket = new DatagramSocket(0, this.f257d);
                    ((e) f262h.get(this.f258m.getName())).a(datagramSocket);
                    ((e) f262h.get(this.f258m.getName())).a(this.f257d, this.f258m);
                    datagramSocket.setBroadcast(true);
                } catch (SocketException e3) {
                    e3.printStackTrace();
                }
            }
            f259e = 0;
            while (this.f250i && this.f254b) {
                DatagramPacket datagramPacket = null;
                try {
                    datagramPacket = new DatagramPacket(new byte[this.f253k], this.f253k);
                    this.f252a.receive(datagramPacket);
                    for (e eVar : f262h.values()) {
                        eVar.a(datagramPacket);
                        eVar.run();
                    }
                } catch (Exception e4) {
                    if (this.f250i && (e4.getCause() instanceof BindException)) {
                        while (true) {
                            try {
                                this.f257d = null;
                                this.f258m = NetworkInterface.getByName(this.f258m.getName());
                                this.f257d = j.a(this.f258m);
                            } catch (Exception e5) {
                                this.f257d = null;
                            }
                            if (this.f257d == null) {
                                try {
                                    Thread.sleep(500L);
                                } catch (InterruptedException e6) {
                                    Logger.getLogger(c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                                }
                            } else {
                                try {
                                    C.c("DHCP: Rebinding sockets to: " + this.f257d.toString());
                                    this.f256l.a(this.f257d);
                                    ((e) f262h.get(this.f258m.getName())).a(this.f257d, this.f258m);
                                    this.f252a = d();
                                } catch (IOException e7) {
                                    Logger.getLogger(c.class.getName()).log(Level.SEVERE, "Failed to create server socket", (Throwable) e7);
                                }
                                for (e eVar2 : f262h.values()) {
                                    eVar2.a(eVar2.a());
                                    if (datagramPacket != null && datagramPacket.getLength() > 0) {
                                        eVar2.a(datagramPacket);
                                        eVar2.run();
                                    }
                                }
                            }
                            if (!this.f250i || (this.f257d != null && this.f257d.getAddress()[0] != Byte.MAX_VALUE)) {
                                break;
                            }
                        }
                    } else {
                        C.d("DHCP: Exception caught on read: " + e4.getLocalizedMessage());
                    }
                }
            }
            if (!this.f254b && this.f250i) {
                synchronized (this.f255c) {
                    try {
                        this.f255c.wait();
                    } catch (InterruptedException e8) {
                        Logger.getLogger(c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
                    }
                }
            }
            C.b("DHCP ending for " + this.f258m.getDisplayName());
            try {
                if (!this.f252a.isClosed()) {
                    this.f252a.close();
                }
            } catch (Exception e9) {
                e9.printStackTrace();
            }
        }
    }

    private DatagramSocket d() throws SocketException {
        if (this.f252a != null) {
            this.f252a.close();
        }
        C.c(this.f258m.toString());
        this.f252a = new DatagramSocket((SocketAddress) null);
        this.f252a.bind(new InetSocketAddress(this.f256l.a(), this.f251j));
        this.f252a.setBroadcast(true);
        C.c("DatagramSocket Server Listening: " + this.f251j);
        return this.f252a;
    }
}
