package z;

import G.C0129l;
import G.C0130m;
import G.F;
import G.J;
import bH.C;
import bH.C0995c;
import bH.W;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.RXTXCommDriver;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.pkcs11.wrapper.Constants;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* renamed from: z.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:z/e.class */
public class C1901e extends J implements SerialPortEventListener {

    /* renamed from: a, reason: collision with root package name */
    n f14091a;

    /* renamed from: b, reason: collision with root package name */
    SerialPort f14092b;

    /* renamed from: c, reason: collision with root package name */
    public static RXTXCommDriver f14093c = new RXTXCommDriver();

    /* renamed from: d, reason: collision with root package name */
    static HashMap f14094d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    C1902f f14095e;

    /* renamed from: f, reason: collision with root package name */
    g f14096f;

    /* renamed from: o, reason: collision with root package name */
    private boolean f14097o;

    /* renamed from: g, reason: collision with root package name */
    boolean f14098g;

    /* renamed from: h, reason: collision with root package name */
    boolean f14099h;

    /* renamed from: i, reason: collision with root package name */
    int f14100i;

    /* renamed from: j, reason: collision with root package name */
    String f14101j;

    /* renamed from: k, reason: collision with root package name */
    long f14102k;

    /* renamed from: l, reason: collision with root package name */
    int f14103l;

    /* renamed from: m, reason: collision with root package name */
    int f14104m;

    /* renamed from: n, reason: collision with root package name */
    boolean f14105n;

    public static void a() {
        f14093c.initialize();
    }

    @Override // G.J
    public boolean b() {
        return this.f14095e != null && this.f14095e.a();
    }

    @Override // G.J, A.h
    public void c() {
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            C.c("goOffline Starting, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            try {
                d(e());
            } catch (V.b e2) {
                Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            C.d("Deactivated Turbo Baud, goOffline");
            if (q()) {
                a(e());
                a(250L);
            }
            C.c("goOffline about to stopProcessing, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            if (this.f14095e != null) {
                this.f14095e.b();
            }
            C1902f c1902f = this.f14095e;
            this.f14095e = null;
            boolean z2 = this.f421F;
            u();
            if (z2) {
                A();
                C.c("goOffline Notified offline, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            }
            h();
            C.c("goOffline closed port, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            if (!Thread.currentThread().equals(c1902f) && (this.f14096f == null || !Thread.currentThread().equals(this.f14096f))) {
                for (int i2 = 0; c1902f != null && c1902f.isAlive() && i2 < 100; i2++) {
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e3) {
                        e3.printStackTrace();
                    }
                }
            }
            C.c("goOffline comm thread stopped, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            v();
            try {
                j(e().u());
            } catch (Exception e4) {
            }
        } finally {
            this.f421F = false;
            Q();
        }
    }

    @Override // G.J, A.h
    public void d() throws C0129l {
        if (g() == null || g().length() < 1) {
            C.d("no Comm port set, so not going online.");
            return;
        }
        if (q()) {
            return;
        }
        boolean z2 = false;
        if (this.f14095e != null) {
            this.f14095e.b();
        }
        try {
            a(e().r() + "");
            a(e().u(), e().s());
            C.c("Monitoring " + e().s() + " @ " + e().r() + " baud for controllers.");
        } catch (PortInUseException e2) {
            throw new C0129l("Serial Port " + e().s() + " is in use by " + e2.currentOwner + "\n\nWould you like to go offline?");
        } catch (NullPointerException e3) {
            throw new C0129l("Failed to open current port.");
        } catch (Exception e4) {
            C.c("Error opeing port, ignoring and monitoring anyway.");
            z2 = true;
        }
        this.f14095e = new C1902f(this);
        this.f14095e.start();
        C.c("Started new CommThread for " + e().u());
        if (z2) {
            throw new C0129l(e().s() + " is not currently a valid port.");
        }
        P();
    }

    private void P() {
        if (this.f439W) {
            return;
        }
        if (this.f14096f != null) {
            this.f14096f.a();
        }
        this.f14096f = new g(this, this);
        this.f14096f.start();
        C.d("Started Comms Watchdog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Q() {
        if (this.f14096f != null) {
            this.f14096f.a();
            this.f14096f = null;
            C.d("Stopped Comms Watchdog");
        }
    }

    @Override // G.J
    public F e() {
        return (F) this.f416A.get(0);
    }

    public void a(String str) {
        this.f14091a.b(str);
    }

    public String f() {
        return this.f14092b != null ? this.f14092b.getBaudRate() + "" : e().r() + "";
    }

    public String g() {
        return this.f14092b != null ? this.f14092b.getName() : e().s();
    }

    protected void h() {
        C.c("currentPort == null:" + (this.f14092b == null));
        if (this.f14092b != null) {
            f14094d.remove(W.b(this.f14092b.getName(), "//./", ""));
            new h(this, this.f14092b);
            this.f14092b = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0089 A[Catch: Exception -> 0x009c, all -> 0x00a6, TryCatch #3 {Exception -> 0x009c, all -> 0x00a6, blocks: (B:13:0x0071, B:15:0x007c, B:16:0x0089), top: B:30:0x0071 }] */
    @Override // G.J
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected G.C0132o a(G.C0130m r6) {
        /*
            r5 = this;
            G.o r0 = new G.o
            r1 = r0
            r1.<init>()
            r7 = r0
            r0 = r6
            java.lang.String r0 = r0.s()
            if (r0 == 0) goto L16
            r0 = r6
            java.lang.String r0 = r0.t()
            if (r0 != 0) goto L4a
        L16:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "testPort: uninitialized parameters, port ="
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r6
            java.lang.String r1 = r1.s()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = ", baud="
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r6
            java.lang.String r1 = r1.t()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r8 = r0
            r0 = r8
            bH.C.a(r0)
            r0 = r7
            r1 = 3
            r0.a(r1)
            r0 = r7
            r1 = r8
            r0.a(r1)
            goto Lb0
        L4a:
            r0 = 0
            r8 = r0
            r0 = r5
            r1 = r6
            java.lang.String r1 = r1.s()     // Catch: G.C0129l -> L66
            r2 = r6
            java.lang.String r2 = r2.t()     // Catch: G.C0129l -> L66
            r3 = r5
            G.F r3 = r3.e()     // Catch: G.C0129l -> L66
            G.H r3 = r3.p()     // Catch: G.C0129l -> L66
            byte[] r3 = r3.d()     // Catch: G.C0129l -> L66
            java.lang.String r0 = r0.a(r1, r2, r3)     // Catch: G.C0129l -> L66
            r8 = r0
            goto L68
        L66:
            r9 = move-exception
        L68:
            r0 = r5
            r1 = 1
            r0.f14098g = r1
            r0 = r8
            if (r0 == 0) goto L89
            r0 = r5
            r1 = r8
            byte[] r1 = r1.getBytes()     // Catch: java.lang.Exception -> L9c java.lang.Throwable -> La6
            boolean r0 = r0.b(r1)     // Catch: java.lang.Exception -> L9c java.lang.Throwable -> La6
            if (r0 == 0) goto L89
            r0 = r7
            r1 = 1
            r0.a(r1)     // Catch: java.lang.Exception -> L9c java.lang.Throwable -> La6
            r0 = r7
            r1 = r8
            r0.a(r1)     // Catch: java.lang.Exception -> L9c java.lang.Throwable -> La6
            goto L94
        L89:
            r0 = r7
            r1 = 3
            r0.a(r1)     // Catch: java.lang.Exception -> L9c java.lang.Throwable -> La6
            r0 = r7
            java.lang.String r1 = "Device Unavailable"
            r0.a(r1)     // Catch: java.lang.Exception -> L9c java.lang.Throwable -> La6
        L94:
            r0 = r5
            r1 = 0
            r0.f14098g = r1
            goto Lb0
        L9c:
            r9 = move-exception
            r0 = r5
            r1 = 0
            r0.f14098g = r1
            goto Lb0
        La6:
            r10 = move-exception
            r0 = r5
            r1 = 0
            r0.f14098g = r1
            r0 = r10
            throw r0
        Lb0:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: z.C1901e.a(G.m):G.o");
    }

    protected String a(String str, String str2, byte[] bArr) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            try {
                try {
                    this.f14098g = true;
                    a(200L);
                    a(str2);
                    a("TunerStudiotest", str);
                    String strA = a(bArr, 400L);
                    if (strA.length() == 1) {
                        strA = ((int) strA.getBytes()[0]) + "";
                    }
                    h();
                    a(e().r() + "");
                    C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - Reset Port");
                    try {
                        if (e().u() != null && !e().u().equals("") && e().s() != null && !e().s().equals("")) {
                            a(e().u(), e().s());
                            C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - ReOpened Port");
                        }
                    } catch (C0129l e2) {
                        C.c("Could not open port");
                    } catch (PortInUseException e3) {
                        C.c("Port In Use By:" + e3.currentOwner);
                    }
                    this.f14098g = false;
                    return strA;
                } catch (C0129l e4) {
                    throw e4;
                } catch (Exception e5) {
                    if (this.f14092b != null) {
                        C.a("Exception reading port:" + this.f14092b.getName() + ", @" + this.f14092b.getBaudRate() + " Baud. Error Message:\n" + e5.getMessage());
                    } else {
                        C.d("Port " + str + " Not found.");
                    }
                    h();
                    a(e().r() + "");
                    C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - Reset Port");
                    try {
                        if (e().u() != null && !e().u().equals("") && e().s() != null && !e().s().equals("")) {
                            a(e().u(), e().s());
                            C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - ReOpened Port");
                        }
                    } catch (C0129l e6) {
                        C.c("Could not open port");
                    } catch (PortInUseException e7) {
                        C.c("Port In Use By:" + e7.currentOwner);
                    }
                    this.f14098g = false;
                    return null;
                }
            } catch (PortInUseException e8) {
                throw new C0129l("Com Port " + str + ", in use by " + e8.currentOwner);
            }
        } catch (Throwable th) {
            h();
            a(e().r() + "");
            C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - Reset Port");
            try {
                if (e().u() != null && !e().u().equals("") && e().s() != null && !e().s().equals("")) {
                    a(e().u(), e().s());
                    C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - ReOpened Port");
                }
            } catch (C0129l e9) {
                C.c("Could not open port");
            } catch (PortInUseException e10) {
                C.c("Port In Use By:" + e10.currentOwner);
            }
            this.f14098g = false;
            throw th;
        }
    }

    protected String a(byte[] bArr) {
        return C0995c.d(bArr);
    }

    @Override // G.J
    public void a(boolean z2) {
        if (this.f14096f == null) {
            if (I() || z2) {
                return;
            }
            C.d("Comms Watchdog is inactive, staring.");
            P();
            return;
        }
        if (!this.f14096f.b()) {
            this.f432Q = System.currentTimeMillis();
        }
        if (this.f14096f.b() && z2) {
            C.d("Comms Watchdog is active");
        }
        this.f14096f.a(z2);
    }

    @Override // G.J
    protected void a(long j2) {
        if (j2 <= 0) {
            return;
        }
        try {
            if (this.f439W) {
                C.c("sleep:" + j2);
            }
            Thread.sleep(j2);
        } catch (Exception e2) {
        }
    }

    @Override // G.J
    protected synchronized byte[] a(byte[] bArr, long j2, long j3, int i2, C0130m c0130m) {
        return super.a(bArr, j2, j3, i2, c0130m);
    }

    @Override // G.J
    protected byte[] a(byte[] bArr, long j2, long j3, int i2, C0130m c0130m, InputStream inputStream) throws IOException, V.b {
        int i3;
        byte[] bArr2;
        String str;
        long jNanoTime = System.nanoTime();
        if (this.f439W) {
            C.c("read Called: readDelay=" + j2 + ", readTimeout=" + j3 + ", expectedBytes=" + i2 + ", time since last read complete=" + (System.currentTimeMillis() - this.f14102k));
        }
        if (bArr != null) {
            int iK = e().k();
            if (iK < 6) {
                iK = 6;
            }
            long length = (2 * bArr.length * iK) + (2 * (j3 >= 0 ? j3 : 400L));
            if (length < 400) {
                length = 400;
            }
            this.f433R = System.currentTimeMillis() + length;
            if (I()) {
                C.c("commThreadExpectedReturnTime set to ms from now: " + length);
            }
        }
        if (this.f14101j == null || !this.f14101j.equals(Thread.currentThread().getName())) {
            C.c("Comm Read Thread Change! Old Thread:" + this.f14101j + ", new Thread:" + Thread.currentThread().getName());
            this.f14101j = Thread.currentThread().getName();
        }
        int i4 = i2 > 0 ? i2 > 2048 ? 2048 : i2 : 512;
        OutputStream outputStreamJ = j();
        if (inputStream == null) {
            inputStream = i();
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (bArr != null) {
            if (inputStream.available() > 0) {
                System.out.print("Purging orphaned bytes: \n");
                a(40L);
                ArrayList arrayList = new ArrayList();
                String str2 = "";
                while (inputStream.available() > 0) {
                    int i5 = inputStream.read();
                    System.out.print("0x" + Integer.toHexString(i5) + " ");
                    str2 = C0995c.a(i5) ? str2 + Constants.INDENT + ((char) i5) + Constants.INDENT : str2 + "  .  ";
                    arrayList.add(new Byte((byte) i5));
                }
                System.out.println(str2);
                System.out.print("\n");
                System.out.println(str2);
                if (arrayList.size() > 0) {
                    C.c("Cleared " + arrayList.size() + " orphans");
                }
                if (!a(arrayList) && i2 == ((F) this.f416A.get(this.f416A.size() - 1)).n()) {
                    e().q(e().t() + 5);
                    l();
                    if (this.f14103l > e().t() && e().t() < 70) {
                        C.c("Increasing ochDelay because of orphans to:" + (this.f14103l - 5));
                        e().q(this.f14103l - 5);
                    }
                }
            }
            if (this.f439W) {
                C.c("Check 1:" + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
            }
            if (this.f421F) {
                i(L());
            }
            if (this.f439W) {
                C.c("Check 2  notify write:" + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
            }
            boolean zJ = e().j();
            if (zJ) {
                outputStreamJ.write(bArr);
                this.f432Q = System.currentTimeMillis();
            } else {
                for (int i6 = 0; i6 < bArr.length; i6++) {
                    outputStreamJ.write(bArr[i6]);
                    this.f432Q = System.currentTimeMillis();
                    if (c0130m != null && i6 % (bArr.length / 99) == 0) {
                        a(c0130m, i6 / bArr.length);
                    }
                    if (i6 < bArr.length - 1) {
                        a(e().k());
                    }
                }
            }
            c("SENT", bArr);
            jCurrentTimeMillis = System.currentTimeMillis();
            if (this.f439W) {
                C.c("Check 3 wrote bytes, writeBlock=" + zJ + " about to flush out buffer:" + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
            }
            outputStreamJ.flush();
            this.f433R = Long.MAX_VALUE;
            if (j2 == -1) {
                j(L());
                return null;
            }
            if (this.f439W) {
                C.c("Check 4 out buffer flushed: " + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
            }
            if (this.f421F) {
                k(L());
            }
            a(j2);
            if (this.f439W) {
                C.c("Check 4.1 past sleep readDelay: " + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
            }
            j(L());
            if (this.f439W) {
                C.c("Check 4.2 Notified Write end.: " + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
            }
        }
        if (this.f439W) {
            C.c("Check 5:" + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
        }
        byte[] bArr3 = new byte[i4];
        System.currentTimeMillis();
        long jCurrentTimeMillis2 = System.currentTimeMillis() + j3;
        while (inputStream.available() == 0 && System.currentTimeMillis() < jCurrentTimeMillis2 - 1) {
            b(jCurrentTimeMillis2 - System.currentTimeMillis());
        }
        if (inputStream.available() == 0) {
            if (bArr != null) {
                str = "Read timeout for send command: " + a(bArr) + "\nExpected " + (i2 > 0 ? Integer.valueOf(i2) : "Unspecified") + " bytes, still no response after " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.";
                c("Timed out on read", bArr);
            } else {
                str = "Underrun\nExpected " + (i2 > 0 ? Integer.valueOf(i2) : "Unspecified") + " bytes, still no response after " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.";
            }
            throw new V.b(str);
        }
        if (this.f439W) {
            C.c("Check 6, start read of " + (i2 > 0 ? Integer.valueOf(i2) : "Unspecified") + " bytes: " + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
        }
        int i7 = 0;
        if (i2 < 0) {
            int i8 = inputStream.read();
            while (true) {
                int i9 = i8;
                if (i9 == -1 || (i2 >= 0 && i7 >= i2)) {
                    break;
                }
                if (i7 == bArr3.length) {
                    byte[] bArr4 = bArr3;
                    bArr3 = new byte[bArr4.length + i4];
                    System.arraycopy(bArr4, 0, bArr3, 0, bArr4.length);
                }
                bArr3[i7] = (byte) i9;
                i7++;
                this.f432Q = System.currentTimeMillis();
                while (inputStream.available() == 0 && i7 < i2 - 1) {
                    b(j3);
                    if (inputStream.available() == 0) {
                        String str3 = "break wait after " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms, " + i7 + " bytes read, expected:" + i2;
                        ArrayList arrayList2 = new ArrayList();
                        for (int i10 = 0; i10 < i7; i10++) {
                            arrayList2.add(Byte.valueOf(bArr3[i10]));
                        }
                        a(arrayList2);
                        C.c(str3);
                        int[] iArr = new int[i7];
                        System.arraycopy(bArr3, 0, iArr, 0, i7);
                        a("Expected " + i2 + " bytes, Received", iArr);
                        throw new V.b(str3);
                    }
                }
                i8 = inputStream.available() > 0 ? inputStream.read() : -1;
            }
        } else {
            byte[] bArr5 = new byte[i2];
            int i11 = 0;
            long jCurrentTimeMillis3 = System.currentTimeMillis();
            do {
                if (i2 - i11 > bArr3.length) {
                    i3 = inputStream.read(bArr3, 0, bArr3.length);
                    if (i3 > 0) {
                        System.arraycopy(bArr3, 0, bArr5, i11, i3);
                    }
                } else {
                    i3 = inputStream.read(bArr5, i11, i2 - i11);
                }
                if (this.f439W) {
                    C.c("read:" + i3 + " bytes  " + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
                }
                if (i3 > 0) {
                    i11 += i3;
                    this.f432Q = System.currentTimeMillis();
                    jCurrentTimeMillis3 = System.currentTimeMillis();
                }
                if ((i3 <= 0 && System.currentTimeMillis() - jCurrentTimeMillis3 > j3) || System.currentTimeMillis() - jCurrentTimeMillis3 > j3 * 2) {
                    String str4 = "break wait after " + (System.currentTimeMillis() - jCurrentTimeMillis3) + " ms, " + i11 + " bytes read, expected:" + i2;
                    C.c(str4);
                    int[] iArr2 = new int[i11];
                    System.arraycopy(C0995c.b(bArr5), 0, iArr2, 0, iArr2.length);
                    a("Expected " + i2 + " bytes, Received", iArr2);
                    ArrayList arrayList3 = new ArrayList();
                    for (int i12 = 0; i12 < iArr2.length; i12++) {
                        arrayList3.add(Byte.valueOf(bArr5[i12]));
                    }
                    if (bArr != null) {
                        a(arrayList3);
                    }
                    throw new V.b(str4);
                }
                if (i11 < i2) {
                    if (this.f439W) {
                        C.c("Check 6.2: need " + (i2 - i11) + " sleeping " + (1 + ((i2 - i11) / this.f14104m)) + "ms to fill buffer: " + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
                    }
                    a(1 + ((i2 - i11) / this.f14104m));
                }
            } while (i11 < i2);
            if (this.f439W) {
                C.c("Check 6.3:read all " + i2 + "bytes " + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
            }
            bArr3 = bArr5;
        }
        this.f14099h = false;
        this.f432Q = System.currentTimeMillis();
        if (i2 <= 0) {
            bArr2 = new byte[i7];
            System.arraycopy(bArr3, 0, bArr2, 0, bArr2.length);
        } else {
            bArr2 = bArr3;
        }
        if (this.f439W) {
            C.c("Check 7, Done reading:" + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
        }
        c("Received", bArr2);
        l(L());
        if (this.f439W) {
            C.c("Check 8, Notify app components:" + ((System.nanoTime() - jNanoTime) / 1000000.0d) + "ms.");
        }
        this.f14102k = System.currentTimeMillis();
        return bArr2;
    }

    @Override // G.J
    protected InputStream i() {
        return this.f14092b.getInputStream();
    }

    protected OutputStream j() {
        return this.f14092b.getOutputStream();
    }

    @Override // G.J
    protected boolean k() {
        return this.f420E;
    }

    public void l() {
        try {
            m();
        } catch (Exception e2) {
            Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    protected void a(String str, String str2) throws C0129l {
        boolean zI = I();
        String strTrim = str2.trim();
        C.d("Opening port: " + strTrim);
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.f14092b = (SerialPort) f14094d.get(strTrim);
            if (this.f14092b == null) {
                CommPortIdentifier.addPortName(strTrim, 1, f14093c);
                if (zI) {
                    C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - added Port");
                }
                this.f14092b = (SerialPort) f14093c.getCommPort(strTrim, 1);
                f14094d.put(strTrim, this.f14092b);
                if (zI) {
                    C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - 1st got port");
                }
                this.f14092b.addEventListener(this);
                this.f14092b.notifyOnDataAvailable(true);
            }
            if (zI) {
                C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - got port");
            }
            m();
            if (zI) {
                C.c((System.currentTimeMillis() - jCurrentTimeMillis) + "ms. - setParameters");
            }
        } catch (TooManyListenersException e2) {
            C.c("Failed to add as serial listener");
        } catch (Exception e3) {
            f14094d.remove(strTrim);
            if (I()) {
                C.c("Unable to open port: " + strTrim + "\nPlease check your Communications Settings. ");
            }
            throw new C0129l("Unable to open port: " + strTrim + "\nPlease check your Communications Settings. ");
        }
    }

    protected void m() throws m {
        if (this.f14092b == null) {
            C.b("currentPort is null, can not setConnectionParameters");
            return;
        }
        int baudRate = this.f14092b.getBaudRate();
        int dataBits = this.f14092b.getDataBits();
        int stopBits = this.f14092b.getStopBits();
        int parity = this.f14092b.getParity();
        this.f14092b.getFlowControlMode();
        this.f14092b.setRTS(false);
        this.f14092b.setDTR(false);
        this.f14092b.setInputBufferSize(this.f14100i);
        this.f14092b.setOutputBufferSize(this.f14100i);
        try {
            this.f14092b.setSerialPortParams(this.f14091a.b(), this.f14091a.f(), this.f14091a.g(), this.f14091a.h());
            this.f14092b.enableReceiveTimeout(5);
            this.f14104m = (12 * this.f14091a.b()) / jssc.SerialPort.BAUDRATE_115200;
            try {
                this.f14092b.setFlowControlMode(this.f14091a.d() | this.f14091a.e());
            } catch (UnsupportedCommOperationException e2) {
                throw new m("Unsupported flow control");
            }
        } catch (UnsupportedCommOperationException e3) {
            this.f14091a.a(baudRate);
            this.f14091a.b(dataBits);
            this.f14091a.c(stopBits);
            this.f14091a.d(parity);
            throw new m("Unsupported parameter");
        }
    }

    @Override // gnu.io.SerialPortEventListener
    public synchronized void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == 1) {
            this.f14099h = true;
            notify();
        }
    }

    protected synchronized void b(long j2) {
        this.f14099h = false;
        long jCurrentTimeMillis = System.currentTimeMillis() + j2;
        int i2 = 0;
        while (!this.f14099h && System.currentTimeMillis() < jCurrentTimeMillis) {
            try {
                wait(j2);
            } catch (InterruptedException e2) {
                Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            i2++;
        }
    }

    @Override // G.J
    public void b(boolean z2) {
        this.f14097o = z2;
    }

    @Override // G.J
    public String n() {
        return "MegaSquirtRs232CommManager";
    }

    protected void o() throws C0129l {
        if (this.f14092b == null) {
            if (I()) {
                C.d("Had to open port " + e().s() + " from the comm thread");
            }
            f14093c = new RXTXCommDriver();
            C.c("re-initializing driver in loop");
            f14093c.initialize();
            a(1000L);
            a(e().u(), e().s());
            a(500L);
        }
    }

    @Override // G.J
    protected boolean p() {
        return true;
    }

    @Override // G.J
    protected synchronized boolean a(int i2) {
        String strF = f();
        a(i2 + "");
        try {
            m();
            return true;
        } catch (m e2) {
            Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            a(strF);
            try {
                m();
                return false;
            } catch (m e3) {
                Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                return false;
            }
        }
    }

    @Override // G.J
    public boolean a(Thread thread) {
        return (thread == null || this.f14095e == null || !thread.equals(this.f14095e)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void R() {
        String property = System.getProperty("os.name");
        if (property != null && property.startsWith("Linux")) {
            try {
                String str = "/var/lock/LCK.." + e().s().replace("/dev/", "");
                a(500L);
                Process processExec = Runtime.getRuntime().exec("/bin/chmod 777 " + str);
                processExec.waitFor();
                C.c("chmod lock file exit code:" + processExec.exitValue());
                a(250L);
                Process processExec2 = Runtime.getRuntime().exec("/bin/rm " + str);
                processExec2.waitFor();
                C.d("Failed to open port, deleted Lock file, exit code: " + processExec2.exitValue() + CallSiteDescriptor.TOKEN_DELIMITER + str);
            } catch (IOException e2) {
                Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (InterruptedException e3) {
                Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }

    @Override // G.J
    public boolean q() {
        return (!this.f421F || this.f14095e == null || this.f14095e.a()) ? false : true;
    }

    @Override // G.J, A.h
    public boolean r() {
        return (this.f14095e == null || this.f14095e.a()) ? false : true;
    }

    /* JADX WARN: Finally extract failed */
    protected void a(SerialPort serialPort) {
        try {
            f14094d.remove(W.b(this.f14092b.getName(), "//./", ""));
            C.d("Removed Port from cache");
            try {
                try {
                    serialPort.getOutputStream().flush();
                    serialPort.close();
                    C.d("Successfully Closed Port");
                } catch (Throwable th) {
                    serialPort.close();
                    C.d("Successfully Closed Port");
                    throw th;
                }
            } catch (IOException e2) {
                serialPort.close();
                C.d("Successfully Closed Port");
            }
        } catch (Exception e3) {
            C.c("can not close Port: " + ((Object) serialPort) + ", message: " + e3.getMessage());
        }
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long a(z.C1901e r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.f432Q = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: z.C1901e.a(z.e, long):long");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long b(z.C1901e r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.f432Q = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: z.C1901e.b(z.e, long):long");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long c(z.C1901e r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.f432Q = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: z.C1901e.c(z.e, long):long");
    }

    static {
    }
}
