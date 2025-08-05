package aC;

import A.f;
import G.C0129l;
import bH.C;
import com.intel.bluetooth.RemoteDeviceHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: TunerStudioMS.jar:aC/a.class */
public class a extends aB.a implements f {

    /* renamed from: j, reason: collision with root package name */
    private String f2285j = "1234";

    /* renamed from: c, reason: collision with root package name */
    RemoteDevice f2286c = null;

    /* renamed from: d, reason: collision with root package name */
    String f2288d = "";

    /* renamed from: f, reason: collision with root package name */
    int f2290f = 0;

    /* renamed from: g, reason: collision with root package name */
    InputStream f2291g = null;

    /* renamed from: h, reason: collision with root package name */
    OutputStream f2292h = null;

    /* renamed from: i, reason: collision with root package name */
    StreamConnection f2293i = null;

    /* renamed from: k, reason: collision with root package name */
    private static List f2287k = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    public static String f2289e = "Bluetooth Direct";

    @Override // A.f
    public void f() throws C0129l {
        if (this.f2288d == null || this.f2288d.isEmpty()) {
            throw new C0129l("No Bluetooth device set!");
        }
        this.f2290f = 2;
        if (this.f2286c == null) {
            b(this.f2288d);
        }
        if (this.f2286c == null) {
            C.b("Unable to find to " + this.f2288d);
            this.f2290f = 0;
            throw new C0129l("Unable to find to " + this.f2288d);
        }
        if (!this.f2286c.isTrustedDevice()) {
            boolean zAuthenticate = false;
            try {
                zAuthenticate = RemoteDeviceHelper.authenticate(this.f2286c, this.f2285j);
                C.c("Pairing results: " + zAuthenticate);
            } catch (IOException e2) {
                C.a("pairing failed: " + a(this.f2286c));
            }
            if (!zAuthenticate) {
                this.f2290f = 0;
                throw new C0129l("Failed to pair with " + this.f2288d);
            }
        }
        try {
            C.d("Opening Bluetooth Connection to: " + this.f2286c.getBluetoothAddress());
            this.f2293i = (StreamConnection) Connector.open("btspp://" + this.f2286c.getBluetoothAddress() + ":1", 3);
            this.f2291g = this.f2293i.openInputStream();
            this.f2292h = this.f2293i.openOutputStream();
            C.d("Bluetooth Connection opened: " + this.f2286c.getBluetoothAddress());
            try {
                Thread.sleep(250L);
            } catch (InterruptedException e3) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
            this.f2290f = 3;
        } catch (IOException e4) {
            g();
            throw new C0129l("Unable to connect to " + this.f2288d);
        }
    }

    @Override // A.f
    public boolean r() {
        return k() == 3;
    }

    @Override // A.f
    public void g() {
        try {
            if (this.f2292h != null) {
                this.f2292h.close();
                C.c("BC output closed");
            }
        } catch (IOException e2) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        try {
            if (this.f2291g != null) {
                this.f2291g.close();
                C.c("BC input closed");
            }
        } catch (IOException e3) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        try {
            if (this.f2293i != null) {
                this.f2293i.close();
                C.c("BC connection closed");
            }
        } catch (IOException e4) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        this.f2291g = null;
        this.f2292h = null;
        this.f2293i = null;
        this.f2290f = 0;
    }

    @Override // A.f
    public String h() {
        return f2289e;
    }

    @Override // A.f
    public InputStream i() {
        return this.f2291g;
    }

    @Override // A.f
    public OutputStream j() {
        return this.f2292h;
    }

    @Override // A.f
    public int k() {
        return this.f2290f;
    }

    @Override // A.f
    public List l() {
        f2287k.clear();
        if (b.a()) {
            f2287k.addAll(b.b());
        }
        ArrayList arrayList = new ArrayList();
        A.b bVar = new A.b();
        bVar.a("Bluetooth Device");
        bVar.b("The Bluetooth Adapter to use for communication.");
        bVar.a(4);
        for (int i2 = 0; i2 < f2287k.size(); i2++) {
            bVar.a((Object) a((RemoteDevice) f2287k.get(i2)));
        }
        arrayList.add(bVar);
        A.b bVar2 = new A.b();
        bVar2.a("Not Listed Bluetooth");
        bVar2.b("Pair New Adapter");
        bVar2.a(5);
        arrayList.add(bVar2);
        return arrayList;
    }

    @Override // A.f
    public void a(String str, Object obj) {
        if (str.equals("Bluetooth Device")) {
            this.f2288d = (String) obj;
            b(this.f2288d);
        } else if (str.equals("Pairing PIN")) {
            this.f2285j = obj.toString();
        }
    }

    private void b(String str) {
        RemoteDevice remoteDevice = null;
        String strC = c(str);
        if (f2287k == null || f2287k.isEmpty()) {
            f2287k.addAll(b.b());
        }
        Iterator it = f2287k.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            RemoteDevice remoteDevice2 = (RemoteDevice) it.next();
            if (remoteDevice2.getBluetoothAddress().equals(strC)) {
                remoteDevice = remoteDevice2;
                C.d("Found Paired BT Device " + str);
                break;
            }
        }
        if (remoteDevice == null) {
            C.d("BT Device " + str + " not found in paired device list, Doing full discovery.");
            Iterator it2 = b.a(false).iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                RemoteDevice remoteDevice3 = (RemoteDevice) it2.next();
                if (remoteDevice3.getBluetoothAddress().equals(strC)) {
                    remoteDevice = remoteDevice3;
                    C.d("Found BT Device " + str + " with full discovery!");
                    break;
                }
            }
        }
        if (remoteDevice == null) {
            Connection connectionOpen = null;
            try {
                try {
                    C.d("Trying direct connect to BT Device " + str);
                    connectionOpen = Connector.open("btspp://" + strC + ":1");
                    remoteDevice = RemoteDevice.getRemoteDevice(connectionOpen);
                    connectionOpen.close();
                    if (connectionOpen != null) {
                        try {
                            connectionOpen.close();
                        } catch (IOException e2) {
                            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                        }
                    }
                } catch (IOException e3) {
                    C.b("Bluetooth Device is not paired and does not appear to be discoverable. Is it on?");
                    if (connectionOpen != null) {
                        try {
                            connectionOpen.close();
                        } catch (IOException e4) {
                            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        }
                    }
                }
            } catch (Throwable th) {
                if (connectionOpen != null) {
                    try {
                        connectionOpen.close();
                    } catch (IOException e5) {
                        Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                    }
                }
                throw th;
            }
        }
        this.f2286c = remoteDevice;
        if (this.f2286c == null || this.f2286c.isTrustedDevice()) {
            return;
        }
        try {
            C.c("Pairing results: " + RemoteDeviceHelper.authenticate(remoteDevice, this.f2285j));
        } catch (IOException e6) {
            C.a("pairing failed: " + strC);
        }
    }

    private String a(RemoteDevice remoteDevice) {
        try {
            return remoteDevice.getFriendlyName(false) + " (" + remoteDevice.getBluetoothAddress() + ")";
        } catch (IOException e2) {
            return "(" + remoteDevice.getBluetoothAddress() + ")";
        }
    }

    private String c(String str) {
        return str.contains("(") ? str.substring(str.indexOf("(") + 1, str.indexOf(")")) : str;
    }

    public static void t() {
        f2287k.clear();
    }

    @Override // A.f
    public Object a(String str) {
        if (str.equals("Bluetooth Device")) {
            return this.f2286c != null ? a(this.f2286c) : this.f2288d;
        }
        if (str.equals("Pairing PIN")) {
            return this.f2285j;
        }
        C.c("Unknown Setting Name: " + str);
        return null;
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
        return this.f2286c == null ? "Bluetooth - Remote Device Not Set" : this.f2290f == 0 ? "Bluetooth - " + a(this.f2286c) : this.f2290f == 2 ? "Bluetooth - " + a(this.f2286c) + " - Connecting" : this.f2290f == 4 ? "Bluetooth - " + a(this.f2286c) + " - Disconnecting" : this.f2290f == 3 ? "Bluetooth - " + a(this.f2286c) + " - Connected" : "Bluetooth - " + a(this.f2286c);
    }

    @Override // A.f
    public int o() {
        return FTPReply.FILE_ACTION_PENDING;
    }

    @Override // A.f
    public int p() {
        return 1000;
    }

    @Override // A.f
    public boolean q() {
        return true;
    }

    @Override // A.f
    public int s() {
        return 1;
    }
}
