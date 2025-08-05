package A;

import G.C0129l;
import G.C0130m;
import G.C0132o;
import G.F;
import G.J;
import bH.C;
import bH.C0995c;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.security.pkcs11.wrapper.Constants;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    */
/* loaded from: TunerStudioMS.jar:A/t.class */
public class t extends J implements g, h {

    /* renamed from: e, reason: collision with root package name */
    u f52e;

    /* renamed from: a, reason: collision with root package name */
    private int f53a;

    /* renamed from: f, reason: collision with root package name */
    boolean f54f;

    /* renamed from: b, reason: collision with root package name */
    private f f55b;

    /* renamed from: g, reason: collision with root package name */
    String f56g;

    /* renamed from: c, reason: collision with root package name */
    private boolean f57c;

    /* renamed from: h, reason: collision with root package name */
    boolean f58h;

    /* renamed from: i, reason: collision with root package name */
    boolean f59i;

    /* renamed from: j, reason: collision with root package name */
    int f60j;

    /* renamed from: k, reason: collision with root package name */
    byte[] f61k;

    /* renamed from: l, reason: collision with root package name */
    HashMap f62l;

    /* renamed from: d, reason: collision with root package name */
    private File f63d;

    /* renamed from: m, reason: collision with root package name */
    BufferedWriter f64m;

    /* renamed from: ak, reason: collision with root package name */
    private String f65ak;

    /* renamed from: n, reason: collision with root package name */
    long f66n;

    /* renamed from: o, reason: collision with root package name */
    static int f67o = 0;

    /* renamed from: al, reason: collision with root package name */
    private static HashMap f68al = new HashMap();

    public t(F f2) {
        super(f2);
        this.f52e = null;
        this.f53a = 600;
        this.f54f = false;
        this.f55b = null;
        this.f56g = null;
        this.f57c = true;
        this.f58h = false;
        this.f59i = false;
        this.f60j = 1024;
        this.f61k = new byte[this.f60j];
        this.f62l = new HashMap();
        this.f63d = null;
        this.f64m = null;
        this.f65ak = "";
        this.f66n = System.currentTimeMillis();
    }

    @Override // G.J
    public String n() {
        return "Multi Interface MegaSquirt Driver";
    }

    @Override // G.J, A.h
    public void c() {
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            C.c("goOffline Starting, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            F fE = e();
            try {
                d(fE);
            } catch (V.b e2) {
                Logger.getLogger(t.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            C.d("Deactivated Turbo Baud, goOffline");
            if (q()) {
                for (int i2 = 0; i2 < this.f416A.size(); i2++) {
                    a((F) this.f416A.get(i2));
                    a(250L);
                }
            }
            C.c("goOffline about to stopProcessing, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            if (this.f52e != null) {
                this.f52e.a(true);
            }
            u uVar = this.f52e;
            fE.i(false);
            this.f52e = null;
            boolean z2 = this.f421F;
            u();
            if (z2) {
                A();
                C.c("goOffline Notified offline, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            }
            C.c("goOffline closed port, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            if (uVar == null || Thread.currentThread().equals(uVar)) {
                a(500L);
            } else {
                uVar.a(true);
                for (int i3 = 0; uVar != null && uVar.isAlive() && i3 < 100; i3++) {
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
            g();
        }
    }

    @Override // G.J
    public boolean a(Thread thread) {
        return (thread == null || this.f52e == null || !thread.equals(this.f52e)) ? false : true;
    }

    @Override // G.J
    public boolean b() {
        return false;
    }

    @Override // G.J, A.h
    public void d() throws C0129l {
        if (a() == null) {
            throw new C0129l("No ControllerInterface! Cannot go online.");
        }
        if (this.f421F && this.f52e != null && this.f52e.isAlive()) {
            C.c("Apparently already online, not goingOnline again.");
        } else {
            f();
        }
    }

    @Override // G.J
    public void a(boolean z2) {
    }

    @Override // G.J
    protected C0132o a(C0130m c0130m) {
        C0132o c0132o = new C0132o();
        try {
            try {
                this.f55b.f();
                if (e().ak() > 0) {
                    a(e().ak());
                }
                byte[] bArrB = b(e().p().d(), this.f53a);
                C.c("Read from " + this.f55b.n() + ", signature:" + C0995c.d(bArrB));
                if (bArrB == null || !b(bArrB)) {
                    c0132o.a(3);
                } else {
                    c0132o.a(1);
                    c0132o.a(new String(bArrB));
                }
                this.f55b.g();
            } catch (Exception e2) {
                Logger.getLogger(t.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                c0132o.a(3);
                this.f55b.g();
            }
            return c0132o;
        } catch (Throwable th) {
            this.f55b.g();
            throw th;
        }
    }

    @Override // G.J, A.h
    public boolean r() {
        return (this.f52e == null || this.f52e.a()) ? false : true;
    }

    @Override // G.J
    public boolean q() {
        return this.f421F;
    }

    @Override // G.J
    protected InputStream i() {
        return s();
    }

    @Override // G.J
    protected void b(String str) {
        C.c("Time:" + ((System.currentTimeMillis() - this.f66n) / 1000.0d) + "s. " + str);
    }

    private void f() {
        if (this.f52e == null || !this.f52e.equals(Thread.currentThread())) {
            if (this.f52e != null) {
                this.f52e.a(false);
            }
            this.f52e = new u(this);
            this.f52e.start();
        }
    }

    @Override // G.J
    protected byte[] a(byte[] bArr, long j2, long j3, int i2, C0130m c0130m, InputStream inputStream) throws IOException, V.b {
        if (inputStream == null) {
            throw new IOException("Null input stream detected");
        }
        int i3 = 0;
        long jO = j3 + this.f55b.o();
        byte[] bArr2 = null;
        OutputStream outputStreamP = P();
        if (this.f56g == null || !this.f56g.equals(Thread.currentThread().getName())) {
            C.d("Comm Read Thread Change! Old Thread:" + this.f56g + ", new Thread:" + Thread.currentThread().getName());
            this.f56g = Thread.currentThread().getName();
        }
        if (bArr != null) {
            try {
                if (bArr.length > 0 && inputStream.available() > 0) {
                    int i4 = 0;
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    while (inputStream.available() > 0) {
                        int i5 = inputStream.read();
                        sb.append("0x").append(Integer.toHexString(C0995c.a((byte) i5)).toUpperCase()).append(" ");
                        if (C0995c.a(i5)) {
                            sb2.append(Constants.INDENT).append((char) i5).append(Constants.INDENT);
                        } else {
                            sb2.append("  .  ");
                        }
                        i4++;
                    }
                    System.out.print("\n");
                    C.c("Purged " + i4 + " orphaned bytes:");
                    b(sb.toString());
                    b("\n");
                    b(sb2.toString());
                }
            } catch (IOException e2) {
                throw e2;
            }
        }
        if (i2 > 0) {
            bArr2 = (byte[]) this.f62l.get(Integer.valueOf(i2));
            if (bArr2 == null) {
                bArr2 = new byte[i2];
                this.f62l.put(Integer.valueOf(i2), bArr2);
            }
        }
        if (bArr != null && bArr.length > 0) {
            if (this.f421F) {
                i(L());
            }
            if (e().j()) {
                try {
                    if (this.f58h) {
                        b("About to write");
                    }
                    if (c0130m == null || bArr.length <= 256) {
                        outputStreamP.write(bArr);
                        outputStreamP.flush();
                    } else {
                        int i6 = 0;
                        while (i6 < bArr.length) {
                            int length = bArr.length - i6 > 256 ? 256 : bArr.length - i6;
                            outputStreamP.write(bArr, i6, length);
                            i6 += length;
                            a(c0130m, i6 / bArr.length);
                            outputStreamP.flush();
                        }
                    }
                    if (this.f58h) {
                        b("Done write, About to flush");
                    }
                    if (f430O) {
                        c("SENT " + bArr.length + " bytes", bArr);
                    }
                } catch (IOException e3) {
                    C.a("Exception during synchronous write");
                    throw e3;
                } catch (Exception e4) {
                    C.a("Exception during synchronous write");
                    e4.printStackTrace();
                }
            } else {
                for (int i7 = 0; i7 < bArr.length; i7++) {
                    try {
                        outputStreamP.write(bArr[i7]);
                        outputStreamP.flush();
                        if (e().k() > 0 && i7 + 1 < bArr.length) {
                            a(e().k());
                        }
                        if (c0130m != null && bArr.length > 0 && i7 % (bArr.length / 99.0f) == 0.0f) {
                            a(c0130m, i7 / bArr.length);
                        }
                    } catch (IOException e5) {
                        C.a("Exception during synchronous write " + e5.getMessage());
                        e5.printStackTrace();
                        throw e5;
                    } catch (Exception e6) {
                        C.a("Exception during synchronous write");
                        c("Failed send:", bArr);
                        e6.printStackTrace();
                    }
                }
                if (f430O) {
                    c("SENT " + bArr.length + " bytes iwd=" + e().k() + ", rd=" + j2 + ", rt=" + j3 + ", ers=" + i2, bArr);
                }
            }
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (j2 == -1) {
            j(L());
            return null;
        }
        if (i2 < 0) {
            a(jO);
            j(L());
            if (this.f421F) {
                k(L());
            }
        } else {
            if (j2 > 0) {
                a(j2);
            }
            j(L());
            int i8 = 0;
            if (this.f421F) {
                k(L());
            }
            if (this.f59i) {
                try {
                    if (this.f58h) {
                        b("start Read");
                    }
                    do {
                        int iAvailable = inputStream.available();
                        if (this.f58h) {
                            b(iAvailable + " bytes Available");
                        }
                        long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
                        if (iAvailable < i2 && jCurrentTimeMillis2 > jO) {
                            c("READ Timout after " + iAvailable + " bytes, Expected:" + i2 + " Raw buffer", bArr2);
                            V.b bVar = new V.b("Timeout after:" + jCurrentTimeMillis2 + "ms. requested timeout:" + jO + ", Expected bytes:" + i2 + ", bytes read:0");
                            bVar.b(i2);
                            bVar.a(0);
                            throw bVar;
                        }
                        if (iAvailable < i2) {
                            int i9 = (i2 - iAvailable) / (i2 > 50 ? 14 : 1);
                            int i10 = i9 < 5 ? 5 : i9;
                            if (this.f58h) {
                                b("Expecting " + i2 + ", need " + (i2 - iAvailable) + " more bytes, going to sleep:" + i10);
                            }
                            a(i10);
                        } else if (i2 > 30 && i2 - inputStream.available() > i2 / 2) {
                            if (this.f58h) {
                                b("about to sleep 25");
                            }
                            a(25L);
                        } else if (i2 - inputStream.available() > 100) {
                            if (this.f58h) {
                                b("about to sleep 15");
                            }
                            a(15L);
                        } else if (iAvailable < i2) {
                            if (this.f58h) {
                                b("about to sleep 5");
                            }
                            a(5L);
                        }
                        i8++;
                    } while (inputStream.available() < i2);
                    if (this.f58h) {
                        b("left 1st block, read " + inputStream.available());
                    }
                } catch (IOException e7) {
                    C.a("Exception during read " + e7.getMessage());
                    throw new V.b("Unable to complete read within timeout period, IOException: " + e7.getLocalizedMessage());
                }
            }
        }
        while (true) {
            if (i2 >= 0 && i3 >= i2) {
                break;
            }
            if (this.f58h) {
                b("In block 2, expectedResponseSize=" + i2 + ", " + i3);
            }
            if (i2 < 0) {
                if (this.f55b.q()) {
                    long jCurrentTimeMillis3 = System.currentTimeMillis();
                    while (inputStream.available() == 0) {
                        if (System.currentTimeMillis() - jCurrentTimeMillis3 > jO) {
                            throw new V.b("No bytes found on unknown read size. timeout x2: " + jO);
                        }
                        a(3L);
                    }
                }
                int i11 = inputStream.read(this.f61k);
                if (i11 <= 0) {
                    throw new V.b("No bytes found on unknown read size. timeout x2: " + jO);
                }
                bArr2 = new byte[i11];
                System.arraycopy(this.f61k, 0, bArr2, 0, i11);
            } else {
                try {
                    if (!l()) {
                        while (i3 < i2) {
                            if (inputStream.available() > 0) {
                                int i12 = i3;
                                i3++;
                                bArr2[i12] = (byte) inputStream.read();
                            } else {
                                a(5L);
                            }
                            long jCurrentTimeMillis4 = System.currentTimeMillis() - jCurrentTimeMillis;
                            if (c0130m != null && i3 % 50 == 0) {
                                a(c0130m, i3 / i2);
                            }
                            if (jCurrentTimeMillis4 > jO && i3 < i2) {
                                if (i3 > 0) {
                                    byte[] bArr3 = new byte[i3];
                                    System.arraycopy(bArr2, 0, bArr3, 0, bArr3.length);
                                    c("READ Timout after " + i3 + " bytes, Expected:" + i2 + ", actual read:" + i3 + " Raw buffer", bArr3);
                                } else {
                                    c("READ Timout after " + i3 + " bytes, Expected:" + i2 + ", actual read:" + i3 + " Raw buffer", (byte[]) null);
                                }
                                V.b bVar2 = new V.b("Timeout after:" + jCurrentTimeMillis4 + "ms. requested timeout:" + jO + ", Expected bytes:" + i2 + ", bytes read:" + i3);
                                bVar2.b(i2);
                                bVar2.a(i3);
                                throw bVar2;
                            }
                        }
                    } else if (inputStream.available() > 0 || !(1 == 0 || this.f55b.q())) {
                        int i13 = inputStream.read(bArr2, i3, i2 - i3 > 4096 ? 4096 : i2 - i3);
                        i3 += i13;
                        if (i13 > 0) {
                            jCurrentTimeMillis = System.currentTimeMillis();
                        } else if (System.currentTimeMillis() - jCurrentTimeMillis > j3) {
                            long jCurrentTimeMillis5 = System.currentTimeMillis() - jCurrentTimeMillis;
                            if (f430O) {
                                if (i3 > 0) {
                                    byte[] bArr4 = new byte[i3];
                                    System.arraycopy(bArr2, 0, bArr4, 0, bArr4.length);
                                    c("READ Timout after " + i3 + " bytes, Expected:" + i2 + ", actual read:" + i3 + " Raw buffer", bArr4);
                                } else {
                                    c("READ Timout after " + i3 + " bytes, Expected:" + i2 + ", actual read:" + i3 + " Raw buffer", (byte[]) null);
                                }
                            }
                            if (c0130m != null && c0130m.aJ() != null) {
                                b("Instruction: " + c0130m.aJ());
                            }
                            V.b bVar3 = new V.b("Timeout after:" + jCurrentTimeMillis5 + "ms. requested timeout:" + jO + ", Expected bytes:" + i2 + ", bytes read:" + i3);
                            bVar3.b(i2);
                            bVar3.a(i3);
                            throw bVar3;
                        }
                        if (c0130m != null && i3 > 20) {
                            a(c0130m, i3 / i2);
                        }
                    } else if (i3 < i2) {
                        long jCurrentTimeMillis6 = System.currentTimeMillis() - jCurrentTimeMillis;
                        if (jCurrentTimeMillis6 > jO) {
                            if (f430O) {
                                c("READ Timout after " + i3 + " bytes, Expected:" + i2 + " Raw buffer", bArr2);
                            }
                            V.b bVar4 = new V.b("Timeout after:" + jCurrentTimeMillis6 + "ms. requested timeout:" + jO + ", Expected bytes:" + i2 + ", bytes read:" + i3);
                            bVar4.b(i2);
                            bVar4.a(i3);
                            throw bVar4;
                        }
                        int i14 = (1000 * (i2 - i3)) / 30;
                        int i15 = i14 / 1000;
                        try {
                            Thread.sleep(i15, i14 - (i15 * 1000));
                        } catch (InterruptedException e8) {
                            Logger.getLogger(t.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
                        }
                    } else {
                        continue;
                    }
                } catch (IOException e9) {
                    C.a("connection lost " + e9.getMessage());
                    Q();
                    if (f430O) {
                        c("READ " + (bArr2 != null ? bArr2.length : 0) + " bytes", bArr2);
                    }
                    l(L());
                    return bArr2;
                }
            }
        }
    }

    @Override // G.J
    protected boolean k() {
        return this.f420E;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
    }

    @Override // A.h
    public synchronized void a(f fVar) {
        this.f55b = fVar;
        if (this.f55b != null) {
            C.d("Setting connection to: " + this.f55b.n());
        }
        notify();
    }

    @Override // A.g, A.h
    public f a() {
        return this.f55b;
    }

    private InputStream s() throws IOException {
        if (this.f55b == null) {
            throw new IOException("No Controller Interface set");
        }
        return this.f55b.i();
    }

    private OutputStream P() throws IOException {
        if (this.f55b == null) {
            throw new IOException("No Controller Interface set");
        }
        return this.f55b.j();
    }

    protected synchronized void h() throws C0129l {
        if (this.f55b == null) {
            throw new C0129l("No Controller Interface.");
        }
        if (j()) {
            return;
        }
        F fE = e();
        this.f55b.f();
        if (fE == null || fE.ak() <= 0) {
            return;
        }
        a(fE.ak());
    }

    protected boolean j() {
        return this.f55b != null && this.f55b.k() == 3;
    }

    private void Q() {
    }

    public boolean l() {
        return this.f57c;
    }

    @Override // G.J
    protected boolean p() {
        return this.f55b != null && this.f55b.m();
    }

    @Override // G.J
    public boolean a(int i2) {
        return this.f55b != null && this.f55b.a(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(f fVar) {
        if (fVar == null) {
            return false;
        }
        Iterator it = f68al.values().iterator();
        while (it.hasNext()) {
            if (fVar.equals((f) it.next())) {
                return true;
            }
        }
        return false;
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
    static /* synthetic */ long a(A.t r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.f432Q = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: A.t.a(A.t, long):long");
    }

    static {
    }
}
