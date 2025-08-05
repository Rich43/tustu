package aB;

import B.l;
import G.C0129l;
import aP.C0404hl;
import bH.C;
import com.ftdi.FTD2XXException;
import com.ftdi.FTDevice;
import com.ftdi.Parity;
import com.ftdi.StopBits;
import com.ftdi.WordLength;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import z.i;

/* loaded from: TunerStudioMS.jar:aB/b.class */
public class b extends A.a {

    /* renamed from: b, reason: collision with root package name */
    FTDevice f2273b = null;

    /* renamed from: i, reason: collision with root package name */
    private int f2275i = 0;

    /* renamed from: d, reason: collision with root package name */
    List f2276d = null;

    /* renamed from: e, reason: collision with root package name */
    c f2277e = null;

    /* renamed from: f, reason: collision with root package name */
    int f2278f = SerialPort.BAUDRATE_115200;

    /* renamed from: h, reason: collision with root package name */
    String f2280h = f2279g;

    /* renamed from: j, reason: collision with root package name */
    private int f2281j = 400;

    /* renamed from: c, reason: collision with root package name */
    public static String f2274c = "FTDI - D2XX driver";

    /* renamed from: g, reason: collision with root package name */
    static String f2279g = "Auto";

    @Override // A.f
    public synchronized void f() throws C0129l {
        if (k() == 3) {
            throw new C0129l("FTDI Device already connected:" + n());
        }
        if (k() == 2) {
            throw new C0129l("FTDI Device already connecting:" + n());
        }
        b(2);
        this.f2273b = null;
        c();
        try {
            for (FTDevice fTDevice : FTDevice.getDevices(true)) {
                if (this.f2280h.equals(f2279g) || (fTDevice != null && this.f2280h.equals(fTDevice.getDevSerialNumber()))) {
                    this.f2273b = fTDevice;
                }
            }
            if (this.f2273b == null) {
                b(0);
                b();
                this.f2281j = 2000;
                if (!this.f2280h.equals(f2279g)) {
                    throw new C0129l("Did not find D2XX Device or it is already in use. Device ID: " + this.f2280h);
                }
                throw new C0129l("Did not find any supported FTDI Device or it is already in use.");
            }
            this.f2281j = 250;
            try {
                C.c("Opening Connection to FTDI Device: " + n());
                System.out.println("Device: " + ((Object) this.f2273b));
                System.out.println("Device Type: " + ((Object) this.f2273b.getDevType()));
                System.out.println("Device ID: " + this.f2273b.getDevID());
                System.out.println("Device Location ID: " + this.f2273b.getDevLocationID());
                this.f2273b.open();
                this.f2273b.setBaudRate(this.f2278f);
                this.f2273b.setLatencyTimer((short) 1);
                this.f2273b.setDataCharacteristics(WordLength.BITS_8, StopBits.STOP_BITS_1, Parity.PARITY_NONE);
                this.f2273b.setTimeouts(250L, 500L);
                b(3);
                this.f2277e = new c(this.f2273b);
                a();
                C.d("D2XX Opened: " + this.f2273b.getDevDescription());
            } catch (Error e2) {
                b(0);
                b();
                if (this.f2273b != null) {
                    try {
                        this.f2273b.close();
                        this.f2273b = null;
                        this.f2277e = null;
                    } catch (Exception e3) {
                        this.f2273b = null;
                        this.f2277e = null;
                    } catch (Throwable th) {
                        this.f2273b = null;
                        this.f2277e = null;
                        throw th;
                    }
                }
                Logger.getLogger(b.class.getName()).log(Level.SEVERE, "Error loafing FTDI Library", (Throwable) e2);
                throw new C0129l("Unable to open device: " + e2.getLocalizedMessage() + ", " + n());
            } catch (Exception e4) {
                b(0);
                b();
                if (this.f2273b != null) {
                    try {
                        this.f2273b.close();
                        this.f2273b = null;
                        this.f2277e = null;
                    } catch (Exception e5) {
                        this.f2273b = null;
                        this.f2277e = null;
                    } catch (Throwable th2) {
                        this.f2273b = null;
                        this.f2277e = null;
                        throw th2;
                    }
                }
                Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                throw new C0129l("Unable to open device: " + e4.getLocalizedMessage() + ", " + n());
            }
        } catch (FTD2XXException e6) {
            b(0);
            b();
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, "Exception getting devices.", (Throwable) e6);
            throw new C0129l("Unable to get list of FTDI Devices.");
        } catch (Error e7) {
            b(0);
            b();
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, "System Error atteempting to get FTDI Devices.", (Throwable) e7);
            throw new C0129l("Unable to get list of FTDI Devices.");
        }
    }

    @Override // A.f
    public boolean r() {
        g();
        try {
            f();
        } catch (C0129l e2) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        return k() == 3;
    }

    @Override // A.f
    public void g() {
        if (this.f2273b != null) {
            b(4);
            e();
            try {
                this.f2273b.close();
                this.f2277e.close();
                d();
            } catch (Exception e2) {
                C.c("Error closing FTDI Connection");
            }
            C.d("D2XX Closed: " + this.f2273b.getDevDescription());
            this.f2277e = null;
        }
        b(0);
    }

    @Override // A.f
    public String h() {
        return f2274c;
    }

    @Override // A.f
    public InputStream i() {
        return this.f2277e;
    }

    @Override // A.f
    public OutputStream j() {
        return this.f2273b.getOutputStream();
    }

    @Override // A.f
    public int k() {
        return this.f2275i;
    }

    public List t() {
        ArrayList arrayList = new ArrayList();
        try {
            Iterator<FTDevice> it = FTDevice.getDevices().iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().getDevSerialNumber());
            }
        } catch (FTD2XXException e2) {
        }
        return arrayList;
    }

    @Override // A.f
    public List l() {
        this.f2276d = new ArrayList();
        A.b bVar = new A.b();
        bVar.a("Device Serial #");
        bVar.b("FTDI USB Device ID. This can be used to distinctly identify a specific USB device or left on Auto to connect to the 1st found.");
        bVar.a(4);
        bVar.a((Object) f2279g);
        try {
            Iterator<FTDevice> it = FTDevice.getDevices(true).iterator();
            while (it.hasNext()) {
                bVar.a((Object) it.next().getDevSerialNumber());
            }
        } catch (Error e2) {
            C.a("Handled Error: " + e2.getLocalizedMessage());
        } catch (Exception e3) {
            C.a("Handled Exception: " + e3.getLocalizedMessage());
        }
        this.f2276d.add(bVar);
        A.b bVar2 = new A.b();
        bVar2.a("Baud Rate");
        bVar2.b("Baud rate to use for D2XX Serial communications.");
        bVar2.a(4);
        for (String str : new i().c()) {
            bVar2.a((Object) str);
        }
        this.f2276d.add(bVar2);
        return this.f2276d;
    }

    @Override // A.f
    public void a(String str, Object obj) {
        if (!str.equals("Device Serial #")) {
            if (str.equals("Baud Rate")) {
                this.f2278f = Integer.parseInt(obj.toString());
                return;
            } else {
                C.c("Unknown Setting Name: " + str);
                return;
            }
        }
        if (obj.toString().equals(f2279g)) {
            this.f2280h = f2279g;
            return;
        }
        this.f2280h = obj.toString();
        if (b(this.f2280h)) {
            C0404hl.a().a("Counterfeit FTDI Chip!! " + this.f2280h);
            C.b("The serial cable being used appears to be using a Counterfeit FTDI chipset!!!");
        }
    }

    @Override // A.f
    public Object a(String str) {
        if (str.equals("Device Serial #")) {
            return this.f2280h;
        }
        if (str.equals("Baud Rate")) {
            return this.f2278f + "";
        }
        C.c("Unknown Setting Name: " + str);
        return null;
    }

    public void b(int i2) {
        this.f2275i = i2;
    }

    @Override // A.f
    public String n() {
        if (this.f2273b == null) {
            return "USB D2XX, Serial #" + f2279g + " @ " + this.f2278f + " Baud";
        }
        if (!b(this.f2280h)) {
            return "USB D2XX, Serial #" + this.f2273b.getDevSerialNumber() + " @ " + this.f2278f + " Baud";
        }
        C0404hl.a().a("Counterfeit FTDI Chip!! " + this.f2280h);
        C.b("The serial cable being used appears to be using a Counterfeit FTDI chipset!!!");
        return "Counterfeit FTDI Serial #" + this.f2273b.getDevSerialNumber() + " @ " + this.f2278f + " Baud";
    }

    @Override // A.f
    public boolean m() {
        return true;
    }

    @Override // A.f
    public boolean a(int i2) {
        try {
            this.f2273b.setBaudRate(i2);
            return true;
        } catch (Exception e2) {
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return false;
        }
    }

    @Override // A.f
    public int o() {
        return 0;
    }

    @Override // A.f
    public int p() {
        return this.f2281j;
    }

    @Override // A.f
    public boolean q() {
        return false;
    }

    @Override // A.f
    public int s() {
        return 1;
    }

    private boolean b(String str) {
        return str.equals("A50285BI") || str.equals("FTB6SPL3") || str.equals("A601CKQK") || str.contains("0000000") || str.equals("A5XK3RJJ") || str.equals("18F54CB7") || str.equals("A5XK3RJT") || str.equals("");
    }
}
