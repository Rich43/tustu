package af;

import ae.C0497a;
import ae.C0500d;
import ae.p;
import ae.s;
import ae.u;
import ae.v;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:af/b.class */
public class b implements s, InterfaceC0504a {

    /* renamed from: d, reason: collision with root package name */
    List f4437d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    List f4438e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    byte[] f4439f = null;

    /* renamed from: g, reason: collision with root package name */
    boolean f4440g = true;

    public b() {
        C0497a c0497a = new C0497a();
        c0497a.a("Preserve Sensor Calibrations");
        c0497a.b(a());
        c0497a.a(4);
        c0497a.b(Boolean.valueOf(this.f4440g));
        this.f4437d.add(c0497a);
    }

    public String a() {
        return "Will preserve sensor calibration tables during firmware update";
    }

    @Override // ae.s
    public List a(ae.k kVar) {
        return this.f4437d;
    }

    @Override // ae.s
    public List b(ae.k kVar) {
        return this.f4438e;
    }

    @Override // ae.s
    public void a(String str, Object obj) throws ae.n {
        if (!str.equals("Preserve Sensor Calibrations")) {
            throw new ae.n("Unknown Option " + str);
        }
        if (!(obj instanceof Boolean)) {
            throw new ae.n("Invalid Data Type for Preserve Sensor Calibrations, must be Boolean");
        }
        this.f4440g = ((Boolean) obj).booleanValue();
    }

    @Override // ae.s
    public C0500d a(ae.k kVar, p pVar, u uVar) {
        if (this.f4440g) {
            return b(pVar, uVar);
        }
        C0500d c0500d = new C0500d();
        c0500d.a(C0500d.f4346a);
        c0500d.a("Not saving Sensor Calibrations");
        uVar.a("Not saving Sensor Calibrations");
        C.d("Not saving Sensor Calibrations");
        return c0500d;
    }

    private C0500d b(p pVar, u uVar) throws IOException {
        C0500d c0500dA = j.a(pVar, 3974144, false);
        if (c0500dA.a() != C0500d.f4346a) {
            C.a("Error changing page to preserve MS2 Calibration Data.");
            return c0500dA;
        }
        byte[] bArr = new byte[4];
        byte[] bArr2 = new byte[3];
        this.f4439f = new byte[28 * 256];
        for (int i2 = 0; i2 < 28; i2++) {
            try {
                try {
                    bArr[0] = -89;
                    bArr[1] = (byte) (i2 + 164);
                    bArr[2] = 0;
                    bArr[3] = -1;
                    byte[] bArrA = pVar.a(bArr, 256 + 3, 300);
                    if (bArrA == null || bArrA.length != 256 + 3) {
                        this.f4439f = null;
                        C.a("Read Error during read calibrations. block " + i2);
                        c0500dA.a(C0500d.f4348c);
                        c0500dA.a("Failed to retrieve calibration data.");
                        uVar.a("Failed to retrieve calibration data.");
                        uVar.a(1.0d);
                        return c0500dA;
                    }
                    System.arraycopy(bArrA, 256, bArr2, 0, bArr2.length);
                    if (j.a(bArr2) != 0) {
                        c0500dA.a(C0500d.f4348c);
                        c0500dA.a("Unable to retrieve sensor calibration data. Skipping Preserve Sensor Calibration");
                        this.f4439f = null;
                        uVar.a(1.0d);
                        return c0500dA;
                    }
                    System.arraycopy(bArrA, 0, this.f4439f, i2 * 256, 256);
                    uVar.a(i2 / 27.0f);
                    uVar.a(1.0d);
                } catch (v e2) {
                    Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    throw new IOException("Protocol Error while reading calibration data, this should not happen.");
                } catch (IOException e3) {
                    this.f4439f = null;
                    throw e3;
                }
            } catch (Throwable th) {
                uVar.a(1.0d);
                throw th;
            }
        }
        boolean z2 = false;
        int i3 = 0;
        while (true) {
            if (i3 >= this.f4439f.length) {
                break;
            }
            if (this.f4439f[i3] != -1) {
                z2 = true;
                break;
            }
            i3++;
        }
        if (!z2) {
            c0500dA.a("Calibration not found, data Erased.");
            uVar.a("Calibration data has already been erased, not saving.");
            C.d("Calibration data has already been erased, not saving.");
            this.f4439f = null;
        }
        return c0500dA;
    }

    @Override // ae.s
    public C0500d a(p pVar, u uVar) {
        C0500d c0500dA;
        if (this.f4439f != null) {
            try {
                c0500dA = j.a(pVar, 3974144, false);
                if (c0500dA.a() != C0500d.f4346a) {
                    C.a("Error changing page to preserve MS2 Calibration Data.");
                    return c0500dA;
                }
                byte[] bArr = new byte[256];
                for (int i2 = 0; i2 < 28; i2++) {
                    System.arraycopy(this.f4439f, 256 * i2, bArr, 0, bArr.length);
                    int i3 = 3974144 + (i2 * 256);
                    int iA = -1;
                    for (int i4 = 0; i4 < 3 && iA != 0; i4++) {
                        if (i4 > 0 && i4 < 2) {
                            try {
                                C.b("Send Calibration Record Failed, retrying.");
                                j.a(250);
                            } catch (v e2) {
                                Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                            }
                        } else if (i4 > 0) {
                            C.b("Send Calibration Record Failed, 3 tries, giving up.");
                            c0500dA.a(C0500d.f4347b);
                            c0500dA.a("Failed to send sensor calibration data after multiple retries.");
                            return c0500dA;
                        }
                        iA = j.a(pVar, C0995c.b(i3), bArr);
                    }
                }
            } catch (IOException e3) {
                C0500d c0500d = new C0500d();
                c0500d.a("Unable to restore Sensor Calibrations");
                c0500d.a(C0500d.f4348c);
                C.a("IOException while restoring sensor calibrations: " + e3.getLocalizedMessage());
                return c0500d;
            }
        } else {
            c0500dA = new C0500d();
            c0500dA.a("Nothing to do.");
            C.d("Not loading calibration tables.");
            c0500dA.a(C0500d.f4346a);
        }
        return c0500dA;
    }

    public boolean b() {
        return this.f4439f != null;
    }
}
