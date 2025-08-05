package ac;

import G.C0123f;
import G.R;
import bH.C0995c;
import bH.W;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ac/u.class */
public class u extends h {

    /* renamed from: a, reason: collision with root package name */
    static final byte[] f4273a = {77, 76, 86, 76, 71, 0};

    /* renamed from: z, reason: collision with root package name */
    private static u f4274z = null;

    /* renamed from: A, reason: collision with root package name */
    private List f4275A = Collections.synchronizedList(new ArrayList());

    /* renamed from: B, reason: collision with root package name */
    private List f4276B = Collections.synchronizedList(new ArrayList());

    /* renamed from: b, reason: collision with root package name */
    List f4277b = new ArrayList();

    /* renamed from: C, reason: collision with root package name */
    private int f4278C = 0;

    /* renamed from: D, reason: collision with root package name */
    private int f4279D = 0;

    /* renamed from: E, reason: collision with root package name */
    private double f4280E = -1.0d;

    /* renamed from: F, reason: collision with root package name */
    private boolean f4281F = false;

    /* renamed from: G, reason: collision with root package name */
    private int f4282G = 0;

    /* renamed from: H, reason: collision with root package name */
    private int f4283H = 21;

    /* renamed from: I, reason: collision with root package name */
    private double f4284I = this.f4283H;

    /* renamed from: c, reason: collision with root package name */
    int f4285c = -1;

    /* renamed from: p, reason: collision with root package name */
    int f4286p = 0;

    /* renamed from: q, reason: collision with root package name */
    int f4287q = 0;

    /* renamed from: r, reason: collision with root package name */
    int f4288r = 4;

    /* renamed from: s, reason: collision with root package name */
    byte f4289s = 0;

    /* renamed from: t, reason: collision with root package name */
    long f4290t = 0;

    /* renamed from: u, reason: collision with root package name */
    int f4291u = 0;

    /* renamed from: J, reason: collision with root package name */
    private double f4292J = 0.0d;

    /* renamed from: K, reason: collision with root package name */
    private int f4293K;

    /* renamed from: v, reason: collision with root package name */
    byte[] f4294v;

    /* renamed from: w, reason: collision with root package name */
    List f4295w;

    /* renamed from: x, reason: collision with root package name */
    C0123f f4296x;

    /* renamed from: L, reason: collision with root package name */
    private InterfaceC0492d f4297L;

    /* renamed from: y, reason: collision with root package name */
    byte[] f4298y;

    /* renamed from: M, reason: collision with root package name */
    private int f4299M;

    protected u() {
        this.f4293K = this.f4278C == 1 ? 22 : 24;
        this.f4294v = new byte[]{0};
        this.f4295w = new ArrayList();
        this.f4296x = new C0123f();
        this.f4297L = new n();
        this.f4298y = new byte[2];
        this.f4299M = 0;
    }

    public static u a() {
        if (f4274z == null) {
            f4274z = new u();
        }
        return f4274z;
    }

    public void a(v vVar) {
        this.f4295w.add(vVar);
    }

    @Override // ac.h
    protected void a(R[] rArr, OutputStream outputStream) {
        byte[] bArr;
        try {
            this.f4275A = a(rArr);
            this.f4276B = a(this.f4275A);
            StringBuilder sb = new StringBuilder();
            String strA = this.f4297L.a(rArr);
            if (strA != null) {
                sb.append(strA).append("\n");
            }
            StringBuilder sb2 = new StringBuilder();
            for (q qVar : this.f4275A) {
                if (qVar.j() != null && !qVar.j().isEmpty()) {
                    sb2.append(qVar.a()).append(" = ").append(qVar.j()).append("\n");
                }
            }
            if (sb2.length() > 0) {
                sb.append("NEW_INFO_PROVIDER").append(",").append("Field Descriptions").append(",").append("Type:Properties").append("Visible:true").append("Length:").append(sb2.length()).append("\n");
                sb.append((CharSequence) sb2);
            }
            for (v vVar : this.f4295w) {
                String strD = vVar.d();
                if (strD != null && !strD.isEmpty()) {
                    sb.append("NEW_INFO_PROVIDER").append(",").append(vVar.a()).append(",").append("Type:").append(vVar.b()).append("Visible:").append(vVar.c()).append("Length:").append(strD.getBytes().length).append("\n");
                    sb.append(strD);
                }
            }
            if (sb.length() > 0) {
                byte[] bytes = sb.toString().getBytes();
                bArr = new byte[bytes.length + 1];
                System.arraycopy(bytes, 0, bArr, 0, bytes.length);
                bArr[bytes.length] = 0;
            } else {
                bArr = new byte[]{0};
            }
            outputStream.write(f4273a);
            outputStream.write(C0995c.a(2, new byte[2], true));
            outputStream.write(C0995c.a((int) (System.currentTimeMillis() / 1000), new byte[4], true));
            outputStream.write(C0995c.a(this.f4278C, new byte[4], true));
            outputStream.write(C0995c.a(this.f4278C + bArr.length, new byte[4], true));
            this.f4287q = 0;
            Iterator it = this.f4276B.iterator();
            while (it.hasNext()) {
                this.f4287q += ((w) it.next()).k();
            }
            outputStream.write(C0995c.a(this.f4287q, new byte[2], true));
            outputStream.write(C0995c.a(this.f4276B.size(), new byte[2], true));
            Iterator it2 = this.f4276B.iterator();
            while (it2.hasNext()) {
                outputStream.write(((w) it2.next()).a(2));
            }
            for (w wVar : this.f4276B) {
                if (wVar instanceof t) {
                    outputStream.write(((t) wVar).f());
                }
            }
            outputStream.write(bArr);
            outputStream.flush();
            this.f4286p = 0;
            this.f4282G = 0;
            this.f4279D = 0;
            this.f4289s = (byte) 0;
            this.f4290t = System.nanoTime();
            bH.C.c("MLG header created");
            a(this);
        } catch (IOException e2) {
            bH.C.a("Failed to write Log Header.", e2, null);
        }
    }

    @Override // ac.h
    protected void a(OutputStream outputStream, String str) {
        this.f4219o.add(new l(this, str, r()));
    }

    private void a(OutputStream outputStream, l lVar) {
        if (this.f4282G > 0) {
            try {
                String str = "MARK " + W.a("" + this.f4279D, '0', 3) + " - " + lVar.a() + " - " + new Date().toString();
                outputStream.write(new byte[]{1});
                this.f4279D++;
                byte b2 = this.f4289s;
                this.f4289s = (byte) (b2 + 1);
                outputStream.write(new byte[]{b2});
                outputStream.write(e());
                byte[] bArr = new byte[50];
                w.a(bArr, str);
                outputStream.write(bArr);
            } catch (IOException e2) {
                Logger.getLogger(u.class.getName()).log(Level.WARNING, "Unable to write Mark to data log", (Throwable) e2);
            }
        }
    }

    private byte[] e() {
        InterfaceC0486B interfaceC0486BS = s();
        if (interfaceC0486BS != null) {
            int iA = ((int) (interfaceC0486BS.a() * 100000.0d)) % 65535;
            this.f4298y[0] = (byte) (iA >>> 8);
            this.f4298y[1] = (byte) (iA & 255);
            return this.f4298y;
        }
        long jNanoTime = ((System.nanoTime() - this.f4290t) / 10000) % 65535;
        this.f4298y[0] = (byte) (jNanoTime >>> 8);
        this.f4298y[1] = (byte) (jNanoTime & 255);
        return this.f4298y;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:74:0x02b6 A[Catch: IOException -> 0x035b, all -> 0x037a, TryCatch #3 {IOException -> 0x035b, blocks: (B:72:0x02ad, B:85:0x0321, B:86:0x0342, B:88:0x034b, B:74:0x02b6, B:76:0x02d5, B:77:0x02e0, B:79:0x02ea, B:81:0x02fe, B:82:0x030f), top: B:101:0x02ad, outer: #1 }] */
    /* JADX WARN: Type inference failed for: r0v72, types: [int] */
    @Override // ac.h
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected synchronized void a(java.io.OutputStream r9, byte[][] r10) {
        /*
            Method dump skipped, instructions count: 914
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ac.u.a(java.io.OutputStream, byte[][]):void");
    }

    private byte[] a(byte[] bArr, byte[] bArr2, int i2) {
        System.arraycopy(bArr2, 0, bArr, i2, bArr2.length);
        return bArr;
    }

    @Override // ac.h
    protected void a(OutputStream outputStream) {
        this.f4275A.clear();
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e2) {
            Logger.getLogger(C0491c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public double b() {
        return this.f4284I;
    }

    public double c() {
        return this.f4292J;
    }

    public boolean d() {
        return this.f4281F;
    }

    private List a(List list) {
        this.f4276B.clear();
        List<w> list2 = this.f4276B;
        t tVar = null;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            q qVar = (q) it.next();
            if (!qVar.b().l()) {
                try {
                    list2.add(new x(qVar));
                } catch (V.g e2) {
                    Logger.getLogger(u.class.getName()).log(Level.WARNING, "Unable to create Logger Field", (Throwable) e2);
                }
            } else if (tVar == null || tVar.c().size() >= tVar.k() * 8 || tVar.b() != qVar.b().e()) {
                try {
                    tVar = new t("", "U08");
                    list2.add(tVar);
                    tVar.a(qVar);
                    tVar.e(2);
                    tVar.c(qVar.b().e());
                    tVar.a((byte) 1);
                } catch (V.g e3) {
                    Logger.getLogger(u.class.getName()).log(Level.WARNING, "Unable to create MlvLgBitField", (Throwable) e3);
                }
            } else {
                tVar.a(qVar);
                tVar.a((byte) (tVar.e() + 1));
            }
        }
        for (InterfaceC0489a interfaceC0489a : j()) {
            if (interfaceC0489a instanceof C0488D) {
                try {
                    list2.add(new z((C0488D) interfaceC0489a));
                } catch (V.g e4) {
                    Logger.getLogger(u.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                }
            }
        }
        int length = this.f4293K;
        Iterator it2 = list2.iterator();
        while (it2.hasNext()) {
            length += ((w) it2.next()).d(2);
        }
        this.f4277b.clear();
        for (w wVar : list2) {
            if (wVar instanceof t) {
                t tVar2 = (t) wVar;
                tVar2.b(length);
                byte[] bArrF = tVar2.f();
                this.f4277b.add(bArrF);
                length += bArrF.length;
            }
        }
        this.f4278C = length;
        return list2;
    }
}
