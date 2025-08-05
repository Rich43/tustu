package J;

import G.C0113cs;
import G.C0123f;
import G.cP;
import bH.C;
import bH.C0995c;
import bH.C0996d;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:J/f.class */
public class f implements cP, Serializable {

    /* renamed from: a, reason: collision with root package name */
    C0996d f1427a = new C0996d();

    /* renamed from: h, reason: collision with root package name */
    private boolean f1428h = true;

    /* renamed from: b, reason: collision with root package name */
    int f1429b = 0;

    /* renamed from: c, reason: collision with root package name */
    int f1430c = 0;

    /* renamed from: d, reason: collision with root package name */
    int f1431d = 7;

    /* renamed from: i, reason: collision with root package name */
    private int f1432i = -1;

    /* renamed from: e, reason: collision with root package name */
    C0123f f1433e = new C0123f();

    /* renamed from: f, reason: collision with root package name */
    i f1434f = new i();

    /* renamed from: j, reason: collision with root package name */
    private String f1435j = null;

    /* renamed from: g, reason: collision with root package name */
    boolean f1436g = true;

    public f() {
        for (String str : this.f1434f.s()) {
            C0113cs.a().d(str);
        }
        this.f1434f.b();
    }

    public boolean b(byte[] bArr, byte[] bArr2) {
        int iD = d(bArr2);
        if ((iD & 128) != 128) {
            return true;
        }
        if (iD == 128) {
            this.f1435j = "Controller Reported an Under-run";
            this.f1434f.w();
            C.b("Controller Reported an Under-run");
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 129) {
            this.f1435j = "Controller Reported an Over-run";
            this.f1434f.t();
            C.b("Controller Reported an Over-run");
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 130) {
            this.f1435j = "Controller Reported a CRC Mismatch";
            this.f1434f.x();
            C.b("Controller Reported a CRC Mismatch");
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 131) {
            this.f1435j = "Controller Reported a Unrecognized Command";
            this.f1434f.y();
            C.b("Controller Reported a Unrecognized Command!");
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 132) {
            this.f1435j = "Controller Reported a Out of Range";
            this.f1434f.z();
            C.b("Controller Reported a Out of Range");
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 133) {
            this.f1435j = "Controller reporting BUSY";
            this.f1434f.A();
            C.b("Controller reporting BUSY");
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 134) {
            this.f1435j = "Controller Reported Flash Locked";
            this.f1434f.B();
            C.b("Controller Reported Flash Locked");
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 140) {
            this.f1435j = "Controller Reported Parity Error";
            this.f1434f.K();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 141) {
            this.f1435j = "Controller Reported Framing Error";
            this.f1434f.L();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 142) {
            this.f1435j = "Controller Reported Serial Noise";
            this.f1434f.M();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 143) {
            this.f1435j = "Controller Reported txmode range error";
            this.f1434f.N();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 144) {
            this.f1435j = "Controller Reported Unknown Serial Error";
            this.f1434f.O();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 135) {
            this.f1435j = "Controller Reported Sequence Failure 1";
            this.f1434f.P();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 136) {
            this.f1435j = "Controller Reported Sequence Failure 2";
            this.f1434f.Q();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 139) {
            this.f1435j = "Controller Reported CAN Failure";
            this.f1434f.T();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 137) {
            this.f1435j = "Controller Reported CAN Queue full";
            this.f1434f.R();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 138) {
            this.f1435j = "Controller Reported CAN Timeout";
            this.f1434f.S();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 146) {
            this.f1435j = "Controller Reported CAN Device Unavailable";
            this.f1434f.U();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 147) {
            this.f1435j = "High speed runtime table not set.";
            this.f1434f.U();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 145) {
            this.f1435j = "Too Many Bad Requests for unavailble CAN ID.";
            this.f1434f.U();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 148) {
            this.f1435j = "Generic Error";
            this.f1434f.al();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if (iD == 149) {
            this.f1435j = "Critical Error";
            this.f1434f.am();
            C.b(this.f1435j);
            this.f1429b++;
            g();
            return false;
        }
        if ((iD & 128) != 128) {
            return true;
        }
        this.f1435j = "Controller Reported Undefined Error: 0x" + Integer.toHexString(iD);
        this.f1434f.J();
        C.b(this.f1435j);
        this.f1429b++;
        g();
        return false;
    }

    @Override // G.cP
    public boolean a(byte[] bArr, byte[] bArr2) {
        this.f1435j = null;
        if (!b(bArr, bArr2)) {
            return false;
        }
        int iD = d(bArr2);
        byte[] bArrB = this.f1433e.b(4);
        byte[] bArrB2 = this.f1433e.b(4);
        try {
            byte[] bArrC = c(bArr2, bArrB);
            int iF = f(bArr2);
            if (iF != bArr2.length) {
                this.f1435j = "Unexpeced array length from controller";
                C.b("Unexpeced array length from controller! Envelope declared size:" + (1 + iF) + " actual measured size:" + (bArr2.length - 6) + ", envelope response data:\n" + C0995c.d(bArr2));
                this.f1429b++;
            }
            byte[] bArrD = d(bArr2, bArrB2);
            if (!C0995c.b(bArrC, bArrD)) {
                this.f1435j = "CRC failure on received payload.";
                this.f1434f.C();
                C.b("CRC failure on received payload.\n\tReported CRC: " + C0995c.d(bArrC) + "\n\tCalculated CRC:" + C0995c.d(bArrD) + "\n\tResponse data:\n" + C0995c.d(bArr2));
                this.f1429b++;
                g();
                if (this.f1436g) {
                    return false;
                }
            }
            this.f1429b = 0;
            if (iD == 0) {
                this.f1434f.D();
            }
            if (iD == 4) {
                this.f1434f.E();
            }
            if (iD == 1) {
                this.f1434f.F();
            }
            if (iD == 2) {
                this.f1434f.G();
            }
            if (iD == 3) {
                this.f1434f.H();
            }
            if (iD == 5) {
                this.f1434f.I();
            }
            if (iD == 6) {
                this.f1434f.ag();
            }
            g();
            this.f1433e.a(bArrB);
            this.f1433e.a(bArrB2);
            return true;
        } finally {
            this.f1433e.a(bArrB);
            this.f1433e.a(bArrB2);
        }
    }

    private void g() {
        if (this.f1430c < this.f1434f.f()) {
            C0113cs.a().a("protocolError", 1.0d);
            this.f1430c = this.f1434f.f();
        } else {
            C0113cs.a().a("protocolError", 0.0d);
        }
        this.f1434f.an();
    }

    @Override // G.cP
    public byte[] a(byte[] bArr) {
        return e(bArr);
    }

    @Override // G.cP
    public boolean b(byte[] bArr) {
        String strC = c(bArr);
        return strC != null && strC.length() > 0;
    }

    @Override // G.cP
    public String c(byte[] bArr) {
        int iD = d(bArr);
        if (iD == 3) {
            byte[] bArrE = e(bArr);
            if (bArrE.length > 0) {
                return new String(bArrE);
            }
            C.b("Received Settings Error indicator, but no message.");
            return "The Controller has not provided a reason for the Settings Error.\n\nYou must correct this error, then power cycle your controller.";
        }
        if (iD == 146) {
            byte[] bArrE2 = e(bArr);
            if (bArrE2.length > 0) {
                C.b("CAN Device Unavailable, Controller reported: " + new String(bArrE2));
                return "A Controller is responding, but not at the project assigned CAN ID.\n\nCheck:\n- The target device is powered on.\n- The CAN ID assigned in Project Settings matches that of your target controller.\n\nPlease correct the problem and power cycle the device you are connected to.Then uncheck Work Offline";
            }
            C.b("Received Settings Error indicator, but no message.");
            return "A Controller is responding, but not at the project assigned CAN ID.\n\nCheck:\n- The target device is powered on.\n- The CAN ID assigned in Project Settings matches that of your target controller.\n\nPlease correct the problem and power cycle the device you are connected to.Then uncheck Work Offline";
        }
        if (iD == 148) {
            byte[] bArrE3 = e(bArr);
            String str = bArrE3.length > 0 ? new String(bArrE3) : "No controller Message recieved";
            if (bArrE3.length > 0) {
                C.b("GENERIC_ERROR, Controller reported: " + str);
            } else {
                C.b("Received GENERIC_ERROR indicator, but no message.");
            }
            return "The Controller Reported an error:\n" + str;
        }
        if (iD != 149) {
            if (this.f1429b == 5) {
                return "Failed to get a valid read of OutputChannel data in 5 consecutive attempts.\nPlease check your project settings and ensure you have the correct ini for your project.\n\nAlso check your cabling for resistence to electromagnetic noise.";
            }
            return null;
        }
        byte[] bArrE4 = e(bArr);
        String str2 = bArrE4.length > 0 ? new String(bArrE4) : "No controller Message recieved";
        if (bArrE4.length > 0) {
            C.b("CRITICAL_ERROR, Controller reported: " + str2);
        } else {
            C.b("Received CRITICAL_ERROR indicator, but no message.");
        }
        return "The Controller Reported a Critical Error:\n" + str2;
    }

    @Override // G.cP
    public int d(byte[] bArr) {
        return C0995c.a(bArr, 2, 1, true, false);
    }

    private byte[] c(byte[] bArr, byte[] bArr2) {
        System.arraycopy(bArr, bArr.length - 4, bArr2, 0, 4);
        return bArr2;
    }

    private byte[] e(byte[] bArr) {
        byte[] bArrA = this.f1433e.a(bArr.length - this.f1431d);
        System.arraycopy(bArr, 3, bArrA, 0, bArrA.length);
        return bArrA;
    }

    private int f(byte[] bArr) {
        return (C0995c.a(bArr, 0, 2, true, false) + a()) - 1;
    }

    @Override // G.cP
    public int a(byte[] bArr, int i2) {
        return (C0995c.a(bArr, 0, 2, true, false) + f()) - 1;
    }

    private byte[] d(byte[] bArr, byte[] bArr2) {
        this.f1427a.reset();
        this.f1427a.update(bArr, 2, (bArr.length - this.f1431d) + 1);
        return C0995c.a((int) this.f1427a.getValue(), bArr2, true);
    }

    @Override // G.cP
    public int a() {
        return this.f1431d;
    }

    @Override // G.cP
    public String c() {
        return this.f1435j;
    }

    @Override // G.cP
    public int b() {
        return 3;
    }

    public int f() {
        return 4;
    }

    @Override // G.cP
    public h d() {
        return this.f1434f;
    }

    @Override // G.cP
    public void e() {
        this.f1434f.d();
    }

    @Override // G.cP
    public boolean a(int i2) {
        return (i2 == 131 || i2 == 146 || i2 == 147 || i2 == 3 || i2 == 134 || i2 == 149 || i2 == 132) ? false : true;
    }

    public void d(int i2) {
        this.f1432i = i2;
    }

    @Override // G.cP
    public boolean b(int i2) {
        return 146 == i2 || 149 == i2;
    }

    public void b(boolean z2) {
        this.f1428h = z2;
    }

    @Override // G.cP
    public boolean c(int i2) {
        return i2 == 3;
    }

    @Override // G.cP
    public void a(boolean z2) {
        this.f1436g = z2;
    }
}
