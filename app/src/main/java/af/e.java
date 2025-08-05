package af;

import W.C0200z;
import ad.C0495c;
import ae.C0497a;
import ae.C0500d;
import ae.p;
import ae.s;
import ae.u;
import ae.v;
import bH.C;
import bH.C0995c;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:af/e.class */
public class e implements s, InterfaceC0504a {

    /* renamed from: m, reason: collision with root package name */
    h f4462m;

    /* renamed from: d, reason: collision with root package name */
    List f4453d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    List f4454e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    byte[][] f4455f = (byte[][]) null;

    /* renamed from: g, reason: collision with root package name */
    int f4456g = 31;

    /* renamed from: h, reason: collision with root package name */
    int f4457h = 1024;

    /* renamed from: i, reason: collision with root package name */
    byte[] f4458i = null;

    /* renamed from: j, reason: collision with root package name */
    boolean f4459j = true;

    /* renamed from: k, reason: collision with root package name */
    boolean f4460k = true;

    /* renamed from: l, reason: collision with root package name */
    boolean f4461l = false;

    /* renamed from: p, reason: collision with root package name */
    private String f4463p = null;

    /* renamed from: n, reason: collision with root package name */
    C0497a f4464n = new C0497a();

    /* renamed from: o, reason: collision with root package name */
    C0497a f4465o = new C0497a();

    /* renamed from: q, reason: collision with root package name */
    private boolean f4466q = false;

    public e() {
        this.f4464n.a("Preserve Port States");
        this.f4464n.b(a());
        this.f4464n.a(4);
        this.f4464n.b(Boolean.valueOf(this.f4459j));
        this.f4465o.a("Preserve Sensor Calibrations");
        this.f4465o.b(a());
        this.f4465o.a(4);
        this.f4465o.b(Boolean.valueOf(this.f4460k));
    }

    public String a() {
        return "Will preserve sensor calibration tables during firmware update";
    }

    @Override // ae.s
    public List a(ae.k kVar) {
        this.f4453d.clear();
        if (this.f4463p == null || this.f4463p.length() <= 10) {
            this.f4460k = false;
            this.f4465o.b(Boolean.valueOf(this.f4460k));
            this.f4459j = false;
            this.f4464n.b(Boolean.valueOf(this.f4459j));
        } else {
            this.f4453d.add(this.f4465o);
            List listC = kVar.c();
            String strA = null;
            if (!listC.isEmpty()) {
                strA = C0200z.a((File) listC.get(0));
            }
            if (strA != null && strA.contains(".") && this.f4463p.startsWith(strA.substring(0, strA.indexOf(".")))) {
                this.f4465o.a("Preserve Sensor Calibrations and Tune Data");
                this.f4461l = true;
            } else {
                this.f4465o.a("Preserve Sensor Calibrations");
            }
            this.f4460k = true;
            this.f4465o.b(Boolean.valueOf(this.f4460k));
            this.f4459j = true;
            this.f4464n.b(Boolean.valueOf(this.f4459j));
            this.f4453d.add(this.f4464n);
        }
        return this.f4453d;
    }

    @Override // ae.s
    public List b(ae.k kVar) {
        return this.f4454e;
    }

    @Override // ae.s
    public void a(String str, Object obj) throws ae.n {
        if (str.equals("Preserve Sensor Calibrations") || str.equals("Preserve Sensor Calibrations and Tune Data")) {
            if (!(obj instanceof Boolean)) {
                throw new ae.n("Invalid Data Type for " + str + ", must be Boolean");
            }
            this.f4460k = ((Boolean) obj).booleanValue();
        } else {
            if (!str.equals("Preserve Port States")) {
                throw new ae.n("Unknown Option " + str);
            }
            if (!(obj instanceof Boolean)) {
                throw new ae.n("Invalid Data Type for Preserve Sensor Calibrations, must be Boolean");
            }
            this.f4459j = ((Boolean) obj).booleanValue();
        }
    }

    @Override // ae.s
    public C0500d a(ae.k kVar, p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        if (this.f4459j) {
            C.d("saving Port states");
            c0500d = d(pVar, uVar);
        }
        if (this.f4460k) {
            try {
                File fileG = kVar.g();
                if (fileG == null) {
                    c0500d.a(C0500d.f4347b);
                    c0500d.a("No Firmware File Selected!");
                    return c0500d;
                }
                C.d("Preserving settings based on S19: " + fileG.getAbsolutePath());
                this.f4462m = h.a(kVar.c(fileG).c(), this.f4463p);
                c0500d = c(pVar, uVar);
            } catch (C0495c e2) {
                c0500d.a(C0500d.f4347b);
                c0500d.a("Unable to parse S19 file.");
                C.a(e2.getLocalizedMessage());
            }
        }
        this.f4466q = false;
        return c0500d;
    }

    private C0500d c(p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        byte[] bArr = new byte[5];
        if (this.f4462m.b()) {
            uVar.a("Preserving Sensor Calibration and Tune data");
            C.c("Preserving Sensor Calibration and Tune data");
        } else {
            uVar.a("Preserving Sensor Calibrations");
            C.c("Preserving Sensor Calibrations");
        }
        byte[] bArr2 = new byte[3];
        int iA = this.f4462m.a();
        this.f4455f = new byte[this.f4456g][this.f4457h];
        try {
            for (int i2 = 0; i2 < this.f4456g; i2++) {
                try {
                    if (this.f4462m.a(i2)) {
                        int i3 = 1048576 + (i2 * 1024);
                        for (int i4 = 0; i4 < 64; i4++) {
                            bArr[0] = -64;
                            bArr[1] = (byte) (255 & (i3 >> 16));
                            bArr[2] = (byte) (255 & (i3 >> 8));
                            bArr[3] = (byte) (255 & i3);
                            bArr[4] = 15;
                            byte[] bArrA = pVar.a(bArr, 16 + 3, 300);
                            if (bArrA == null || bArrA.length != 16 + 3) {
                                this.f4455f = (byte[][]) null;
                                C.a("Read Error during read calibrations. block " + i2);
                                c0500d.a(C0500d.f4348c);
                                c0500d.a("Failed to retrieve calibration data.");
                                uVar.a("Failed to retrieve calibration data.");
                                uVar.a(1.0d);
                                return c0500d;
                            }
                            System.arraycopy(bArrA, 16, bArr2, 0, bArr2.length);
                            if (j.a(bArr2) != 0) {
                                c0500d.a(C0500d.f4348c);
                                c0500d.a("Unable to retrieve calibration data. Skipping Preserve Calibration/Sensor");
                                this.f4455f = (byte[][]) null;
                                uVar.a(1.0d);
                                return c0500d;
                            }
                            System.arraycopy(bArrA, 0, this.f4455f[i2], i4 * 16, 16);
                            uVar.a(i2 / iA);
                            i3 += 16;
                        }
                    }
                } catch (v e2) {
                    Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    throw new IOException("Protocol Error while reading calibration data, this should not happen.");
                } catch (IOException e3) {
                    this.f4455f = (byte[][]) null;
                    throw e3;
                }
            }
            c0500d.a(C0500d.f4346a);
            boolean z2 = false;
            for (int i5 = 0; i5 < this.f4455f.length; i5++) {
                int i6 = 0;
                while (true) {
                    if (i6 >= this.f4455f[i5].length) {
                        break;
                    }
                    if (this.f4455f[i5][i6] != -1) {
                        z2 = true;
                        break;
                    }
                    i6++;
                }
            }
            if (!z2) {
                c0500d.a("Calibration not found, data Erased.");
                uVar.a("Calibration data has already been erased, not saving.");
                C.d("Calibration data has already been erased, not saving.");
                this.f4455f = (byte[][]) null;
            }
            return c0500d;
        } finally {
            uVar.a(1.0d);
        }
    }

    private C0500d d(p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        byte[] bArr = new byte[5];
        int i2 = 1080320;
        uVar.a("Saving Port States");
        byte[] bArr2 = new byte[3];
        this.f4458i = new byte[256];
        for (int i3 = 0; i3 < 16; i3++) {
            try {
                try {
                    bArr[0] = -64;
                    bArr[1] = (byte) (255 & (i2 >> 16));
                    bArr[2] = (byte) (255 & (i2 >> 8));
                    bArr[3] = (byte) (255 & i2);
                    bArr[4] = 15;
                    byte[] bArrA = pVar.a(bArr, 16 + 3, 300);
                    if (bArrA == null || bArrA.length != 16 + 3) {
                        this.f4458i = null;
                        C.a("Read Error during read calibrations. block " + i3);
                        c0500d.a(C0500d.f4347b);
                        c0500d.a("Unable to retrieve port states data. Skipping save port states! Disconnect Coils before power cycling.");
                        uVar.a("Failed to retrieve calibration data.");
                        uVar.a(1.0d);
                        return c0500d;
                    }
                    System.arraycopy(bArrA, 16, bArr2, 0, bArr2.length);
                    if (j.a(bArr2) != 0) {
                        c0500d.a(C0500d.f4347b);
                        c0500d.a("Unable to retrieve port states data. Skipping save port states! Disconnect Coils before power cycling.");
                        this.f4458i = null;
                        uVar.a(1.0d);
                        return c0500d;
                    }
                    System.arraycopy(bArrA, 0, this.f4458i, i3 * 16, 16);
                    uVar.a(i3 / 160.0f);
                    uVar.a(1.0d);
                    i2 += 16;
                } catch (v e2) {
                    Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    throw new IOException("Protocol Error while reading calibration data, this should not happen.");
                } catch (IOException e3) {
                    this.f4458i = null;
                    throw e3;
                }
            } catch (Throwable th) {
                uVar.a(1.0d);
                throw th;
            }
        }
        this.f4458i[34] = -1;
        c0500d.a(C0500d.f4346a);
        return c0500d;
    }

    @Override // ae.s
    public C0500d a(p pVar, u uVar) {
        C0500d c0500d;
        if (this.f4458i != null && b(pVar, uVar).a() != C0500d.f4346a) {
            C.b("Problem restoring port states!");
            uVar.a("Unable to restore port states!");
        }
        if (this.f4455f != null) {
            c0500d = e(pVar, uVar);
        } else {
            c0500d = new C0500d();
            c0500d.a("Nothing to do.");
            C.d("Not loading calibration tables.");
            c0500d.a(C0500d.f4346a);
        }
        return c0500d;
    }

    public boolean b() {
        return this.f4458i != null;
    }

    public C0500d b(p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        int i2 = 1080320;
        C.c("Sending port states");
        if (uVar != null) {
            uVar.a("Restoring Port States");
        }
        int[] iArr = new int[3];
        int[] iArr2 = new int[16];
        int[] iArrB = C0995c.b(this.f4458i);
        for (int i3 = 0; i3 < 16; i3++) {
            if (uVar != null) {
                uVar.a(i3 / 16.0f);
            }
            iArr[0] = 255 & (i2 >> 16);
            iArr[1] = 255 & (i2 >> 8);
            iArr[2] = 255 & i2;
            System.arraycopy(iArrB, i3 * 16, iArr2, 0, iArr2.length);
            try {
                j.b(pVar, iArr, iArr2);
            } catch (v e2) {
                Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            i2 += 16;
        }
        if (uVar != null) {
            uVar.a(1.0d);
            uVar.a("Port States Restored");
        }
        c0500d.a(C0500d.f4346a);
        return c0500d;
    }

    private C0500d e(p pVar, u uVar) {
        C0500d c0500d = new C0500d();
        C.c("Sending preserved calibration");
        uVar.a("Restoring Sensor Calibrations");
        int[] iArr = new int[3];
        int[] iArr2 = new int[16];
        int iA = this.f4462m.a();
        for (int i2 = 0; i2 < this.f4456g; i2++) {
            if (this.f4462m.a(i2)) {
                uVar.a(i2 / iA);
                int i3 = 1048576 + (i2 * 1024);
                int[] iArrB = C0995c.b(this.f4455f[i2]);
                for (int i4 = 0; i4 < 64; i4++) {
                    iArr[0] = 255 & (i3 >> 16);
                    iArr[1] = 255 & (i3 >> 8);
                    iArr[2] = 255 & i3;
                    System.arraycopy(iArrB, i4 * 16, iArr2, 0, iArr2.length);
                    try {
                        j.b(pVar, iArr, iArr2);
                    } catch (v e2) {
                        Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                    i3 += 16;
                }
            }
        }
        uVar.a(1.0d);
        uVar.a("");
        c0500d.a(C0500d.f4346a);
        this.f4466q = true;
        return c0500d;
    }

    public boolean c() {
        return this.f4455f != null;
    }

    public void a(String str) {
        this.f4463p = str;
    }

    boolean d() {
        return this.f4461l && this.f4466q;
    }
}
