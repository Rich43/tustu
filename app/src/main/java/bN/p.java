package bN;

import G.C0123f;
import G.J;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:bN/p.class */
public class p {

    /* renamed from: a, reason: collision with root package name */
    InputStream f7287a;

    /* renamed from: b, reason: collision with root package name */
    k f7288b;

    /* renamed from: g, reason: collision with root package name */
    public static int f7293g = 0;

    /* renamed from: h, reason: collision with root package name */
    public static int f7294h = 1;

    /* renamed from: i, reason: collision with root package name */
    public static int f7295i = 2;

    /* renamed from: c, reason: collision with root package name */
    C0123f f7289c = new C0123f();

    /* renamed from: d, reason: collision with root package name */
    final List f7290d = new CopyOnWriteArrayList();

    /* renamed from: e, reason: collision with root package name */
    q f7291e = null;

    /* renamed from: f, reason: collision with root package name */
    int f7292f = -1;

    /* renamed from: j, reason: collision with root package name */
    int f7296j = f7293g;

    /* renamed from: k, reason: collision with root package name */
    String f7297k = "RX:";

    /* renamed from: l, reason: collision with root package name */
    public boolean f7298l = false;

    /* renamed from: m, reason: collision with root package name */
    byte[] f7299m = null;

    /* renamed from: n, reason: collision with root package name */
    C0123f f7300n = new C0123f();

    /* renamed from: q, reason: collision with root package name */
    private long f7301q = 0;

    /* renamed from: r, reason: collision with root package name */
    private long f7302r = 0;

    /* renamed from: o, reason: collision with root package name */
    final Object f7303o = new Object();

    /* renamed from: s, reason: collision with root package name */
    private boolean f7304s = false;

    /* renamed from: t, reason: collision with root package name */
    private J.j f7305t = null;

    /* renamed from: p, reason: collision with root package name */
    int f7306p = 0;

    public p(InputStream inputStream, k kVar) {
        this.f7287a = inputStream;
        this.f7288b = kVar;
    }

    public void a(f fVar) {
        this.f7290d.add(fVar);
    }

    public void b(f fVar) {
        this.f7290d.remove(fVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(IOException iOException) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.f7290d) {
            if (!this.f7290d.isEmpty()) {
                arrayList.addAll(this.f7290d);
            }
        }
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            ((f) it.next()).a(iOException);
        }
    }

    private void a(t tVar) {
        if (this.f7302r > 0 && System.currentTimeMillis() - this.f7302r > 1200) {
            C.c("Time since last packet (ms): " + (System.currentTimeMillis() - this.f7302r));
        }
        this.f7302r = System.currentTimeMillis();
        Iterator it = this.f7290d.iterator();
        while (it.hasNext()) {
            ((f) it.next()).a(tVar);
        }
    }

    private void a(int i2, String str) {
        Iterator it = this.f7290d.iterator();
        while (it.hasNext()) {
            ((f) it.next()).a(i2, str);
        }
    }

    private void d() {
        Iterator it = this.f7290d.iterator();
        while (it.hasNext()) {
            ((f) it.next()).a();
        }
    }

    private void e() {
        Iterator it = this.f7290d.iterator();
        while (it.hasNext()) {
            ((f) it.next()).b();
        }
    }

    private void a(byte[] bArr, int i2) {
        if (f()) {
            C.c("Adding back to prebuffer from offset: \n" + C0995c.a(bArr, 16, i2, bArr.length - i2));
        }
        if (this.f7299m == null || this.f7299m.length == 0) {
            this.f7299m = this.f7300n.b(bArr.length - i2);
            System.arraycopy(bArr, i2, this.f7299m, 0, bArr.length - i2);
            return;
        }
        byte[] bArrB = this.f7300n.b((this.f7299m.length + bArr.length) - i2);
        System.arraycopy(bArr, i2, bArrB, 0, bArr.length - i2);
        System.arraycopy(this.f7299m, 0, bArrB, bArr.length - i2, this.f7299m.length);
        this.f7300n.a(this.f7299m);
        this.f7299m = bArrB;
    }

    private boolean f() {
        return J.I() || this.f7304s;
    }

    public long a() {
        return this.f7301q;
    }

    private int a(byte[] bArr) throws IOException {
        long jCurrentTimeMillis = System.currentTimeMillis() + this.f7288b.h();
        int iMin = 0;
        if (this.f7299m != null && this.f7299m.length > 0) {
            iMin = Math.min(this.f7299m.length, bArr.length);
            System.arraycopy(this.f7299m, 0, bArr, 0, iMin);
            if (this.f7299m.length > iMin) {
                byte[] bArrB = this.f7300n.b(this.f7299m.length - iMin);
                System.arraycopy(this.f7299m, iMin, bArrB, 0, bArrB.length);
                this.f7300n.a(this.f7299m);
                this.f7299m = bArrB;
            } else {
                this.f7300n.a(this.f7299m);
                this.f7299m = null;
            }
            if (iMin == bArr.length) {
                return iMin;
            }
        }
        int i2 = iMin;
        long jCurrentTimeMillis2 = System.currentTimeMillis();
        do {
            int i3 = this.f7287a.read(bArr, iMin, bArr.length - iMin);
            if (i3 != -1) {
                if (i3 > 0) {
                    this.f7301q = System.currentTimeMillis();
                }
                iMin += i3;
                if (iMin < bArr.length || System.currentTimeMillis() - jCurrentTimeMillis2 > 2000) {
                    C.c("Looping for reads. Got " + i3 + " in " + (System.currentTimeMillis() - jCurrentTimeMillis2) + "ms, total: " + iMin);
                }
                if (i3 == 0) {
                }
                if (iMin >= bArr.length) {
                    break;
                }
            } else {
                C.c("Connection Closed, ending....");
                throw new IOException("Connection Closed");
            }
        } while (jCurrentTimeMillis > System.currentTimeMillis());
        if (iMin == 0) {
            return 0;
        }
        if (iMin >= 0 && iMin < bArr.length) {
            C.b("Read Timeout after reading " + iMin + " bytes.");
        }
        if (f() && iMin - i2 > 0) {
            byte[] bArr2 = new byte[iMin - i2];
            System.arraycopy(bArr, i2, bArr2, 0, bArr2.length);
            b(bArr2);
        }
        return iMin;
    }

    private void b(byte[] bArr) {
        System.out.println(k.v() + this.f7297k + C0995c.d(bArr));
    }

    public void b() {
        this.f7298l = true;
        synchronized (this.f7303o) {
            try {
                this.f7303o.wait(500L);
            } catch (InterruptedException e2) {
                Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        if (this.f7299m != null) {
            this.f7300n.a(this.f7299m);
            this.f7299m = null;
        }
        byte[] bArrB = this.f7289c.b(14001);
        try {
            try {
                long jCurrentTimeMillis = System.currentTimeMillis() + 200;
                int i2 = 0;
                do {
                    int i3 = this.f7287a.read(bArrB, i2, bArrB.length - i2);
                    if (i3 == -1) {
                        C.c("Connection Closed, ending...");
                        throw new IOException("Connection Closed");
                    }
                    i2 += i3;
                    if (i3 == 0) {
                        a(0, 200);
                    }
                    if (f()) {
                        C.c("Cleared " + i2 + " orphans from buffer.\n" + C0995c.a(bArrB, 16, 0, i2));
                    } else {
                        C.c("Cleared " + i2 + " orphans from buffer.");
                    }
                    if (i3 <= 0 || i2 >= bArrB.length) {
                        break;
                    }
                } while (jCurrentTimeMillis > System.currentTimeMillis());
                if (i2 > 0) {
                    C.d("Cleared Input Buffer of bytes: " + i2);
                }
            } catch (Exception e2) {
                throw new IOException("Failed to read: " + e2.getMessage());
            }
        } finally {
            this.f7289c.a(bArrB);
        }
    }

    private bS.c a(bS.c cVar, boolean z2) {
        byte[] bArrB = this.f7289c.b(cVar.a().a());
        try {
            try {
                if (a(bArrB) < bArrB.length) {
                    throw new c();
                }
                d();
                cVar.a().a(bArrB);
                int iPow = (int) ((this.f7292f + 1) % Math.pow(2.0d, (8 * r0) / 2));
                int iB = cVar.a().b();
                if (this.f7292f <= -1 || iPow == cVar.a().c()) {
                    this.f7306p = 0;
                } else {
                    if (!z2) {
                        C.b("unexpected Counter Missing packets? Expected: x" + Integer.toHexString(this.f7292f + 1) + ", found: x" + Integer.toHexString(cVar.a().c()));
                    }
                    if (!z2 && this.f7305t != null) {
                        int iMax = (this.f7306p != 0 || (cVar.a().c() != 0 && iPow >= 10) || (iB > this.f7288b.i() && iB > this.f7288b.j())) ? Math.max(cVar.a().c(), iPow) - Math.min(cVar.a().c(), iPow) : 0;
                        if (cVar.a().c() < 127 && iPow > Math.pow(2.0d, (8 * r0) / 2) - 127.0d) {
                            iMax = (int) ((Math.pow(2.0d, (8 * r0) / 2) + cVar.a().c()) - iPow);
                        }
                        if (!z2) {
                            C.c("missing: " + iMax + ", message.getHeader().getCounter()=" + cVar.a().c() + ", nextCounter=" + iPow);
                            this.f7305t.a(iMax);
                            this.f7305t.l();
                        }
                    }
                    if (iB > this.f7288b.i() && iB > this.f7288b.j()) {
                        if (!z2) {
                            C.a("Invalid len, larger than MAX_CTO and MAX DTO. LEN=" + iB);
                        }
                        byte[] bArr = new byte[bArrB.length];
                        System.arraycopy(bArrB, 0, bArr, 0, bArrB.length);
                        throw new e(bArr);
                    }
                    cVar.a(false);
                    this.f7306p++;
                }
                this.f7292f = cVar.a().c();
                byte[] bArrB2 = this.f7289c.b(iB + cVar.c().a());
                int iA = a(bArrB2);
                if (iA < bArrB2.length) {
                    C.b("inbound Packet under-run! Read: " + iA + ", expected: " + bArrB2.length);
                    b(50, "Under-run: " + iA + " of " + bArrB2.length);
                    throw new c();
                }
                byte[] bArr2 = new byte[bArrB2.length - cVar.c().a()];
                System.arraycopy(bArrB2, 0, bArr2, 0, bArr2.length);
                cVar.a(bArr2);
                cVar.c().a(bArrB2, cVar.b().b());
                this.f7289c.a(bArrB);
                if (bArrB2 != null) {
                    this.f7289c.a(bArrB2);
                }
                e();
                return cVar;
            } catch (o e2) {
                throw new e(null);
            } catch (IOException e3) {
                throw e3;
            }
        } catch (Throwable th) {
            this.f7289c.a(bArrB);
            if (0 != 0) {
                this.f7289c.a((byte[]) null);
            }
            e();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:102:0x01b8 A[EDGE_INSN: B:102:0x01b8->B:54:0x01b8 BREAK  A[LOOP:0: B:95:0x0080->B:105:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00dc A[Catch: c -> 0x0141, e -> 0x014a, TryCatch #10 {c -> 0x0141, e -> 0x014a, blocks: (B:15:0x0080, B:17:0x0085, B:18:0x0093, B:20:0x0099, B:21:0x009f, B:23:0x00ad, B:24:0x00b2, B:25:0x00bd, B:27:0x00c0, B:28:0x00d2, B:30:0x00dc, B:31:0x00fc, B:33:0x0139), top: B:95:0x0080 }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0263  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x02a7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void h() throws java.io.IOException, bN.e {
        /*
            Method dump skipped, instructions count: 742
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: bN.p.h():void");
    }

    public void a(int i2) {
        if (this.f7291e != null) {
            this.f7291e.f7307a = false;
        }
        this.f7296j = i2;
        if (i2 == f7294h) {
            this.f7297k = ": Slave RX:\n";
        } else if (i2 == f7295i) {
            this.f7297k = ": Master RX:\n";
            try {
                this.f7298l = this.f7287a.available() > 0;
            } catch (IOException e2) {
                Logger.getLogger(p.class.getName()).log(Level.INFO, "input.available() problem", (Throwable) e2);
                this.f7298l = true;
            }
        } else {
            this.f7297k = ": RX:\n";
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.f7291e = new q(this);
        this.f7291e.start();
        synchronized (this.f7303o) {
            while (this.f7298l) {
                try {
                    this.f7303o.wait(100L);
                } catch (InterruptedException e3) {
                    Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        }
        C.c("Cleared Orphans: " + (System.currentTimeMillis() - jCurrentTimeMillis));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2, int i3) {
        try {
            Thread.sleep(i2, i3);
        } catch (InterruptedException e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private void b(int i2, String str) {
        a(i2, str);
    }

    public void c() {
        if (this.f7291e != null) {
            this.f7291e.f7307a = false;
        }
    }

    private void a(List list) {
        if (list.size() > 0) {
            C.c("Unexpected data received, byte count " + list.size() + CallSiteDescriptor.TOKEN_DELIMITER);
            System.out.println(k.v() + CallSiteDescriptor.TOKEN_DELIMITER + C0995c.d(C0995c.a((Byte[]) list.toArray(new Byte[list.size()]))));
        }
    }

    public void a(J.j jVar) {
        this.f7305t = jVar;
    }
}
