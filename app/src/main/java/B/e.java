package B;

import A.x;
import G.bS;
import bH.C;
import bH.H;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
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
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:B/e.class */
public class e {

    /* renamed from: p, reason: collision with root package name */
    private int f121p;

    /* renamed from: q, reason: collision with root package name */
    private f f135q;

    /* renamed from: r, reason: collision with root package name */
    private String f136r;

    /* renamed from: n, reason: collision with root package name */
    List f137n;

    /* renamed from: o, reason: collision with root package name */
    List f138o;

    /* renamed from: s, reason: collision with root package name */
    private boolean f139s;

    /* renamed from: a, reason: collision with root package name */
    public static String f122a = "slave";

    /* renamed from: b, reason: collision with root package name */
    public static String f123b = "id";

    /* renamed from: c, reason: collision with root package name */
    public static String f124c = "name";

    /* renamed from: d, reason: collision with root package name */
    public static String f125d = "serial";

    /* renamed from: e, reason: collision with root package name */
    public static String f126e = "info";

    /* renamed from: f, reason: collision with root package name */
    public static String f127f = DeploymentDescriptorParser.ATTR_PORT;

    /* renamed from: g, reason: collision with root package name */
    public static String f128g = "protocol";

    /* renamed from: h, reason: collision with root package name */
    public static String f129h = "projectName";

    /* renamed from: i, reason: collision with root package name */
    public static String f130i = "connectionState";

    /* renamed from: j, reason: collision with root package name */
    public static String f131j = "canId";

    /* renamed from: k, reason: collision with root package name */
    public static String f132k = "projectUUID";

    /* renamed from: l, reason: collision with root package name */
    public static String f133l = "CAN_DEVICE_";

    /* renamed from: m, reason: collision with root package name */
    public static String f134m = "DISCOVER_SLAVE_SERVER";

    /* renamed from: t, reason: collision with root package name */
    private static boolean f140t = false;

    /* renamed from: u, reason: collision with root package name */
    private static final Map f141u = new HashMap();

    private e() {
        this.f121p = 21846;
        this.f135q = null;
        this.f136r = l.f168b;
        this.f137n = new ArrayList();
        this.f138o = new ArrayList();
        this.f139s = false;
    }

    private e(int i2) {
        this.f121p = 21846;
        this.f135q = null;
        this.f136r = l.f168b;
        this.f137n = new ArrayList();
        this.f138o = new ArrayList();
        this.f139s = false;
        this.f121p = i2;
    }

    public static e a(int i2) {
        e eVar = (e) f141u.get(Integer.valueOf(i2));
        if (eVar == null) {
            eVar = new e(i2);
            f141u.put(Integer.valueOf(i2), eVar);
        }
        return eVar;
    }

    public void a(A.o oVar) {
        this.f137n.add(oVar);
    }

    public void b(A.o oVar) {
        this.f137n.remove(oVar);
    }

    public void a(k kVar) {
        this.f138o.add(kVar);
    }

    public void b(k kVar) {
        this.f138o.remove(kVar);
    }

    public void a() {
        if (this.f135q != null && this.f135q.isAlive()) {
            this.f135q.a();
        } else {
            this.f135q = new f(this);
            this.f135q.start();
        }
    }

    private void a(i iVar) {
        Iterator it = this.f138o.iterator();
        while (it.hasNext()) {
            ((k) it.next()).a(iVar);
        }
    }

    private void a(String str, String str2, List list, bS bSVar) {
        Iterator it = this.f137n.iterator();
        while (it.hasNext()) {
            ((A.o) it.next()).a(str, str2, list, bSVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        Iterator<E> it = new ArrayList(this.f137n).iterator();
        while (it.hasNext()) {
            ((A.o) it.next()).a((x) null);
        }
    }

    private void a(String str, String str2) {
        String str3;
        HashMap map = new HashMap();
        StringTokenizer stringTokenizer = new StringTokenizer(str2, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            try {
                String strNextToken = stringTokenizer.nextToken();
                map.put(strNextToken.substring(0, strNextToken.indexOf(CallSiteDescriptor.TOKEN_DELIMITER)), strNextToken.substring(strNextToken.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1, strNextToken.length()));
            } catch (Exception e2) {
            }
        }
        String str4 = (String) map.get(f122a);
        if (map.get(f124c) != null && !((String) map.get(f124c)).isEmpty()) {
            str4 = str4 + " - " + ((String) map.get(f124c));
        }
        String str5 = str4 + " IP:" + str;
        if (map.get(f130i) != null && !((String) map.get(f130i)).equals("0")) {
            str5 = str5 + " In Use";
        }
        String str6 = (String) map.get(f125d);
        str3 = "";
        if (map.get(f126e) != null) {
            str3 = str3 + ((String) map.get(f126e));
        } else {
            str3 = str6 != null ? str3 + "Serial# " + str6 + " " : "";
            if (map.get(f123b) != null) {
                str3 = str3 + ((String) map.get(f123b)) + " ";
            }
        }
        String str7 = map.get(f127f) != null ? (String) map.get(f127f) : "21845";
        bS bSVar = new bS();
        String str8 = (map.get(f122a) == null || !((String) map.get(f122a)).startsWith("BigStuff")) ? (String) map.get(f123b) : ((String) map.get(f122a)) + " " + ((String) map.get(f123b));
        bSVar.a(str8);
        bSVar.b(str3);
        bSVar.a(ae.o.b(bSVar));
        if (map.get(f128g) != null && ((String) map.get(f128g)).equalsIgnoreCase("UDP")) {
            this.f136r = o.f184b;
        } else if (map.get(f128g) != null && ((String) map.get(f128g)).equalsIgnoreCase("TCP")) {
            this.f136r = l.f168b;
        }
        String str9 = bQ.l.f7436a + CallSiteDescriptor.TOKEN_DELIMITER + this.f136r;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new A.c(l.f178j, str));
        arrayList.add(new A.c(l.f179k, str7));
        a(str5, str9, arrayList, bSVar);
        if (!this.f138o.isEmpty()) {
            i iVar = new i();
            iVar.f((String) map.get(f122a));
            iVar.a(str8);
            iVar.d(str6);
            iVar.i((String) map.get(f132k));
            iVar.g((String) map.get(f129h));
            if (iVar.j() == null) {
                iVar.g((String) map.get(f124c));
            }
            iVar.b(str);
            iVar.h(str);
            if (map.get(f128g) == null) {
                iVar.c("TCP");
            } else {
                iVar.c((String) map.get(f128g));
            }
            String str10 = (String) map.get(f127f);
            if (str10 != null && H.a(str10)) {
                iVar.a((int) H.a((Object) str10).doubleValue());
            }
            String str11 = (String) map.get(f130i);
            if (str11 != null && str11.equals("1")) {
                iVar.a(true);
            }
            iVar.e(str3);
            for (int i2 = 1; map.containsKey(f133l + i2 + "_" + f123b); i2++) {
                i iVar2 = new i();
                iVar2.f((String) map.get(f122a));
                iVar2.d(str6);
                iVar2.b(str);
                String str12 = f133l + i2 + "_";
                iVar2.g((String) map.get(str12 + f124c));
                iVar2.a((String) map.get(str12 + f123b));
                iVar2.e((String) map.get(str12 + f126e));
                if (map.get(str12 + f128g) != null) {
                    iVar2.c((String) map.get(str12 + f128g));
                } else {
                    iVar2.c("TCP");
                }
                iVar2.i((String) map.get(f132k));
                try {
                    iVar2.a(Integer.parseInt((String) map.get(str12 + f127f)));
                } catch (Exception e3) {
                    C.c("Unable to get port from: " + ((String) map.get(str12 + f127f)));
                }
                try {
                    iVar2.b(Integer.parseInt((String) map.getOrDefault(str12 + f131j, "-1")));
                } catch (Exception e4) {
                    C.b("Unable to get CAN ID from broadcast");
                }
                iVar.a(iVar2);
            }
            a(iVar);
        }
        for (int i3 = 1; map.containsKey(f133l + i3 + "_" + f123b); i3++) {
            String str13 = f133l + i3 + "_";
            String str14 = (String) map.get(f122a);
            if (map.get(str13 + f124c) != null && !((String) map.get(str13 + f124c)).isEmpty()) {
                str14 = str14 + " - " + ((String) map.get(str13 + f124c));
            }
            String str15 = str14 + " IP:" + str;
            if (map.get(str13 + f130i) != null && !((String) map.get(str13 + f130i)).equals("0")) {
                str15 = str15 + " In Use";
            }
            bS bSVar2 = new bS();
            bSVar2.a((String) map.get(str13 + f123b));
            bSVar2.b(str3);
            bSVar2.a(ae.o.b(bSVar2));
            if (map.get(str13 + f128g) != null && ((String) map.get(str13 + f128g)).equalsIgnoreCase("UDP")) {
                this.f136r = o.f184b;
            } else if (map.get(str13 + f128g) != null && ((String) map.get(str13 + f128g)).equalsIgnoreCase("TCP")) {
                this.f136r = l.f168b;
            }
            String str16 = bQ.l.f7436a + CallSiteDescriptor.TOKEN_DELIMITER + this.f136r;
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new A.c(l.f178j, str));
            arrayList2.add(new A.c(l.f179k, map.getOrDefault(str13 + f127f, "-1")));
            try {
                bSVar2.a(Integer.parseInt((String) map.getOrDefault(str13 + f131j, "-1")));
            } catch (Exception e5) {
                C.b("Unable to get CAN ID from broadcast");
            }
            a(str15, str16, arrayList2, bSVar2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setBroadcast(true);
        this.f139s = true;
        byte[] bytes = f134m.getBytes();
        datagramSocket.send(new DatagramPacket(bytes, bytes.length, InetAddress.getByName("255.255.255.255"), this.f121p));
        if (f140t) {
            System.out.println(">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
        }
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterfaceNextElement = networkInterfaces.nextElement2();
            if (!networkInterfaceNextElement.isLoopback() && networkInterfaceNextElement.isUp()) {
                Iterator<InterfaceAddress> it = networkInterfaceNextElement.getInterfaceAddresses().iterator();
                while (it.hasNext()) {
                    InetAddress broadcast = it.next().getBroadcast();
                    if (broadcast != null) {
                        try {
                            datagramSocket.send(new DatagramPacket(bytes, bytes.length, broadcast, this.f121p));
                        } catch (Exception e2) {
                        }
                        if (f140t) {
                            System.out.println("Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterfaceNextElement.getDisplayName());
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
                if (!arrayList.contains(str2) && strTrim.startsWith("slave:")) {
                    a(str, strTrim);
                    arrayList.add(str2);
                    if (0 != 0) {
                        a("192.168.0.122", "slave:BigStuff Gen4\nid:00.0.2\nserial:70-B3-D5-64-E0-0A\nport:21845\nprotocol:UDP\nconnectionState:0");
                        a("192.168.0.123", "slave:RIM\nid:00.0.5\nserial:70-B3-D5-64-E0-B1\nport:21845\nprotocol:UDP\nconnectionState:0");
                        a("192.168.0.124", "slave:RIM\nid:00.0.6\nserial:E0-B3-D5-64-E0-B2\nport:21845\nprotocol:UDP\nconnectionState:0");
                    }
                }
            } catch (SocketTimeoutException e3) {
            }
        } while (jCurrentTimeMillis - System.currentTimeMillis() > 0);
        datagramSocket.close();
        this.f139s = false;
    }

    public boolean b() {
        return this.f139s;
    }
}
