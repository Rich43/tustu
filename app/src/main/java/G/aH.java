package G;

import bH.C0995c;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:G/aH.class */
public class aH extends Q implements Serializable {

    /* renamed from: q, reason: collision with root package name */
    private String f570q;

    /* renamed from: y, reason: collision with root package name */
    private static final int[] f578y = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, Integer.MIN_VALUE};

    /* renamed from: f, reason: collision with root package name */
    private String f558f = null;

    /* renamed from: g, reason: collision with root package name */
    private int f559g = 0;

    /* renamed from: h, reason: collision with root package name */
    private String f560h = null;

    /* renamed from: i, reason: collision with root package name */
    private String f561i = null;

    /* renamed from: j, reason: collision with root package name */
    private int f562j = -1;

    /* renamed from: k, reason: collision with root package name */
    private cZ f563k = null;

    /* renamed from: l, reason: collision with root package name */
    private dh f564l = new B(1.0d);

    /* renamed from: m, reason: collision with root package name */
    private dh f565m = new B(0.0d);

    /* renamed from: n, reason: collision with root package name */
    private String f566n = null;

    /* renamed from: a, reason: collision with root package name */
    boolean f567a = true;

    /* renamed from: o, reason: collision with root package name */
    private int f568o = 0;

    /* renamed from: p, reason: collision with root package name */
    private double f569p = Double.NaN;

    /* renamed from: r, reason: collision with root package name */
    private boolean f571r = false;

    /* renamed from: s, reason: collision with root package name */
    private int f572s = 0;

    /* renamed from: t, reason: collision with root package name */
    private int f573t = 0;

    /* renamed from: u, reason: collision with root package name */
    private ArrayList f574u = null;

    /* renamed from: v, reason: collision with root package name */
    private boolean f575v = false;

    /* renamed from: w, reason: collision with root package name */
    private Integer f576w = null;

    /* renamed from: x, reason: collision with root package name */
    private dh f577x = null;

    /* renamed from: z, reason: collision with root package name */
    private int f579z = -1;

    /* renamed from: A, reason: collision with root package name */
    private int f580A = 0;

    /* renamed from: B, reason: collision with root package name */
    private boolean f581B = true;

    /* renamed from: C, reason: collision with root package name */
    private boolean f582C = false;

    /* renamed from: D, reason: collision with root package name */
    private long f583D = -1;

    /* renamed from: b, reason: collision with root package name */
    int f584b = -1;

    /* renamed from: c, reason: collision with root package name */
    boolean f585c = false;

    /* renamed from: d, reason: collision with root package name */
    byte[] f586d = null;

    /* renamed from: e, reason: collision with root package name */
    String[] f587e = null;

    public aH(String str) {
        this.f570q = str;
    }

    @Override // G.Q
    public String aJ() {
        return this.f558f;
    }

    public void a(boolean z2) {
        this.f567a = z2;
    }

    @Override // G.Q
    public void v(String str) {
        this.f558f = str;
    }

    public int a() {
        return this.f559g;
    }

    public void a(int i2) {
        this.f559g = i2;
    }

    public String b() {
        return this.f560h;
    }

    public void a(String str) throws V.g {
        if (!str.equals(ControllerParameter.PARAM_CLASS_SCALAR) && !str.equals(ControllerParameter.PARAM_CLASS_BITS) && !str.equals("dotScalar") && !str.equals("formula")) {
            throw new V.g("Unknown paramClass: " + str);
        }
        this.f560h = str;
    }

    public String c() {
        return this.f561i;
    }

    public void b(String str) throws V.g {
        if (str == null || !(str.equals("U08") || str.equals("U16") || str.equals("U32") || str.equals("S08") || str.equals("S16") || str.equals("S32") || str.equals("F16") || str.equals("F32"))) {
            throw new V.g("Unsupported data type: " + str);
        }
        b(str.startsWith(PdfOps.S_TOKEN) || str.startsWith(PdfOps.F_TOKEN));
        try {
            this.f568o = Integer.parseInt(bH.W.b(bH.W.b(bH.W.b(str, "U", ""), PdfOps.S_TOKEN, ""), PdfOps.F_TOKEN, "")) / 8;
        } catch (Exception e2) {
            bH.C.a("type:" + str + ", not valid for OutputChannel " + this.f558f);
        }
        this.f575v = str.startsWith(PdfOps.F_TOKEN);
        this.f561i = str;
    }

    public int d() {
        if (this.f562j != -1) {
            return this.f562j;
        }
        int iCeil = (int) Math.ceil(Math.log10(1.0d / h()));
        return iCeil < 0 ? 0 : iCeil;
    }

    public void b(int i2) {
        this.f562j = i2;
    }

    public String e() {
        try {
            return this.f563k != null ? this.f563k.a() : "";
        } catch (V.g e2) {
            Logger.getLogger(aM.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return "INVALID";
        }
    }

    public void c(String str) {
        a(new C0094c(str));
    }

    public void c(int i2) {
        this.f580A = i2;
        this.f579z = (int) (Math.pow(2.0d, i2) - 1.0d);
    }

    public void d(String str) throws V.g {
        if (this.f574u == null) {
            this.f574u = new ArrayList();
        }
        int iPow = (int) Math.pow(2.0d, f());
        if (this.f574u.size() >= iPow) {
            throw new V.g("More bit options defined than possible, max options:" + iPow + ". Not adding: " + str);
        }
        this.f574u.add(str);
    }

    public int f() {
        if (this.f560h.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            return (r() - q()) + 1;
        }
        return 0;
    }

    public void a(cZ cZVar) {
        this.f563k = cZVar;
    }

    public dh g() {
        return this.f564l;
    }

    public double h() {
        return this.f564l.a();
    }

    public void a(dh dhVar) {
        this.f564l = dhVar;
    }

    public void a(double d2) {
        a(new B(d2));
    }

    public double i() {
        return this.f565m.a();
    }

    public dh j() {
        return this.f565m;
    }

    public void b(dh dhVar) {
        this.f565m = dhVar;
    }

    public void b(double d2) {
        b(new B(d2));
    }

    public String k() {
        return this.f566n;
    }

    public void e(String str) {
        this.f566n = str;
    }

    public int l() {
        return this.f568o;
    }

    public double m() {
        int iPow = ((int) Math.pow(2.0d, l() * 8)) - 1;
        if (p()) {
            iPow /= 2;
        }
        return (iPow + this.f565m.a()) * this.f564l.a();
    }

    public double n() {
        int iPow = 0;
        if (p()) {
            iPow = 0 - ((((int) Math.pow(2.0d, l() * 8)) - 1) / 2);
        }
        return (iPow + this.f565m.a()) * this.f564l.a();
    }

    public void c(double d2) {
        this.f569p = d2;
    }

    public double o() {
        if (this.f560h.equals("formula")) {
            String strD = this.f566n;
            if (this.f581B) {
                boolean zIsNaN = Double.isNaN(this.f569p);
                if (this.f584b == -1) {
                    if (strD.contains(this.f558f)) {
                        this.f584b = 1;
                    } else {
                        this.f584b = 0;
                    }
                }
                try {
                    if (this.f584b != 1) {
                        this.f569p = C0126i.a(strD, v());
                    }
                } catch (ax.U e2) {
                    if (zIsNaN) {
                        bH.C.d("EcuOutputChannel::Error executing formula (fastMath):" + strD + "\nWas:" + this.f566n + "\nReported Error:\n" + e2.getMessage() + "\n\tUsing last set value for " + aJ());
                    } else {
                        bH.C.b("EcuOutputChannel::Error executing formula (fastMath):" + strD + "\nWas:" + this.f566n + "\nReported Error:\n" + e2.getMessage() + "\n\tUsing last set value for " + aJ());
                    }
                }
            } else {
                try {
                    strD = C0126i.d(strD, v());
                    this.f569p = bH.F.g(strD);
                } catch (Exception e3) {
                    bH.C.b("EcuOutputChannel::Error executing formula(legacy) for last value:" + strD + "\nWas:" + this.f566n + "\n\tUsing last set value for " + aJ());
                }
            }
        }
        return this.f569p;
    }

    public String a(byte[] bArr) throws V.g {
        double dB = b(bArr);
        return this.f560h.equals(ControllerParameter.PARAM_CLASS_BITS) ? (this.f574u == null || dB >= ((double) this.f574u.size())) ? Integer.toString((int) dB) : (String) this.f574u.get((int) dB) : this.f562j >= 0 ? bH.W.b(dB, this.f562j) : bH.W.a(dB);
    }

    /* JADX WARN: Finally extract failed */
    public synchronized double b(byte[] bArr) throws V.g {
        if (this.f582C) {
            return this.f569p;
        }
        if (this.f560h.equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
            if (this.f561i.equals("U32")) {
                this.f569p = (C0995c.b(bArr, this.f559g, l(), this.f567a, p()) + this.f565m.a()) * this.f564l.a();
            } else {
                int iA = C0995c.a(bArr, this.f559g, l(), this.f567a, p());
                if (t()) {
                    this.f569p = (Float.intBitsToFloat(iA) + this.f565m.a()) * this.f564l.a();
                } else {
                    this.f569p = (iA + this.f565m.a()) * this.f564l.a();
                }
            }
        } else if (this.f560h.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            this.f569p = (C0995c.a(bArr, this.f559g, l(), this.f567a, p()) & y()) >> q();
        } else if (this.f560h.equals("dotScalar")) {
            int iA2 = C0995c.a(bArr, this.f559g, l(), this.f567a, p());
            this.f569p = ((iA2 & (this.f579z ^ (-1))) >> this.f580A) + ((iA2 & this.f579z) / Math.pow(2.0d, this.f580A));
            this.f569p = (this.f569p + this.f565m.a()) * this.f564l.a();
        } else {
            if (!this.f560h.equals("formula")) {
                throw new V.g("EcuOutputChannel::getValueFromResponse not intialized, paramClass=" + this.f560h);
            }
            String str = this.f566n;
            if (this.f581B) {
                try {
                    try {
                        if (!this.f585c) {
                            this.f585c = true;
                            this.f569p = C0126i.a(str, v(), bArr);
                        }
                        this.f585c = false;
                    } catch (ax.U e2) {
                        StringBuilder sb = new StringBuilder("EcuOutputChannel::Error executing formula for ");
                        sb.append(aJ()).append(CallSiteDescriptor.TOKEN_DELIMITER);
                        sb.append(str).append("\nWas:").append(this.f566n).append("\nReported Error:\n").append(e2.getMessage());
                        try {
                            String[] strArrG = C0126i.g(this.f566n, v());
                            double[] dArr = new double[strArrG.length];
                            for (int i2 = 0; strArrG != null && i2 < strArrG.length; i2++) {
                                try {
                                    dArr[i2] = C0126i.a(strArrG[i2], v(), bArr);
                                } catch (ax.U e3) {
                                    bH.C.b("Could not get value for: " + strArrG[i2]);
                                    dArr[i2] = Double.NaN;
                                }
                            }
                            if (strArrG != null) {
                                sb.append("\nComponent Values:\n");
                                for (int i3 = 0; i3 < strArrG.length; i3++) {
                                    sb.append(strArrG[i3]).append(" = ").append(dArr[i3]).append("\n");
                                }
                            }
                        } catch (ax.U e4) {
                            sb.append("Failed to get Component Values");
                            throw new V.g(sb.toString());
                        }
                        throw new V.g(sb.toString());
                    }
                } catch (Throwable th) {
                    this.f585c = false;
                    throw th;
                }
            } else {
                String strD = C0126i.d(str, v(), bArr);
                try {
                    this.f569p = bH.F.g(strD);
                } catch (Exception e5) {
                    throw new V.g("EcuOutputChannel::Error executing formula(legacy):" + strD + "\nWas:" + this.f566n);
                }
            }
        }
        return this.f569p;
    }

    public int c(byte[] bArr) throws V.g {
        int iA;
        if (this.f560h.equals(ControllerParameter.PARAM_CLASS_SCALAR) || this.f560h.equals("dotScalar")) {
            int iA2 = C0995c.a(bArr, this.f559g, l(), this.f567a, p());
            iA = t() ? iA2 : iA2;
        } else {
            if (!this.f560h.equals(ControllerParameter.PARAM_CLASS_BITS)) {
                if (this.f560h.equals("formula")) {
                    throw new V.g("Can not get the raw value for a formula based channel");
                }
                throw new V.g("EcuOutputChannel::getValueFromResponse not intialized, paramClass=" + this.f560h);
            }
            iA = (C0995c.a(bArr, this.f559g, l(), this.f567a, p()) & y()) >> q();
        }
        return Math.round(iA);
    }

    public boolean p() {
        return this.f571r;
    }

    public void b(boolean z2) {
        this.f571r = z2;
    }

    public int q() {
        return this.f572s;
    }

    public void d(int i2) {
        this.f572s = i2;
    }

    public int r() {
        return this.f573t;
    }

    public void e(int i2) {
        this.f573t = i2;
    }

    private int y() {
        if (this.f576w == null) {
            int i2 = 0;
            for (int iQ = q(); iQ <= r(); iQ++) {
                i2 |= f578y[iQ];
            }
            this.f576w = new Integer(i2);
        }
        return this.f576w.intValue();
    }

    public boolean s() {
        return this.f577x == null || this.f577x.a() != 0.0d;
    }

    public void c(dh dhVar) {
        this.f577x = dhVar;
    }

    public boolean t() {
        return this.f575v;
    }

    public boolean u() {
        return this.f560h.equals("formula") && this.f566n.contains("persistentAccumulate(");
    }

    public aI v() {
        return C0125h.a().a(this.f570q);
    }

    public boolean w() {
        return this.f560h.equals("formula") && (this.f566n.toLowerCase().contains("accumulate(") || this.f566n.toLowerCase().contains("persistentaccumulate("));
    }

    public long x() {
        return this.f583D < 0 ? a() : this.f583D;
    }

    public void a(long j2) {
        this.f583D = j2;
    }

    public boolean a(byte[] bArr, double d2) {
        if (this.f586d == null || this.f586d.length != this.f568o) {
            this.f586d = new byte[this.f568o];
        }
        if (this.f560h.equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
            long jRound = Math.round((d2 / this.f564l.a()) - this.f565m.a());
            if (t()) {
                jRound = Float.floatToIntBits((float) d2);
            }
            this.f586d = C0995c.a(jRound, this.f586d, this.f567a);
            System.arraycopy(this.f586d, 0, bArr, a(), this.f568o);
            return true;
        }
        if (this.f560h.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            this.f586d = C0995c.a((C0995c.a(bArr, this.f559g, l(), this.f567a, p()) & (y() ^ (-1))) | ((((int) d2) << q()) & y()), this.f586d, this.f567a);
            System.arraycopy(this.f586d, 0, bArr, a(), this.f568o);
            return true;
        }
        if (!this.f560h.equals("formula")) {
            return false;
        }
        if (this.f587e != null && this.f587e.length != 1) {
            return false;
        }
        if (C0126i.c(this.f566n)) {
            this.f587e = new String[0];
            return false;
        }
        if (this.f587e == null) {
            try {
                this.f587e = C0126i.f(this.f566n, C0125h.a().a(this.f570q));
                if (this.f587e == null) {
                    return false;
                }
                if (this.f587e.length != 1) {
                    return false;
                }
            } catch (ax.U e2) {
                Logger.getLogger(aH.class.getName()).log(Level.SEVERE, "Failed to get Channel Terms", (Throwable) e2);
                this.f587e = new String[0];
                return false;
            }
        }
        aH aHVarG = C0125h.a().a(this.f570q).K().g(this.f587e[0]);
        if (aHVarG != null) {
            return aHVarG.a(bArr, d2);
        }
        return false;
    }

    public void c(boolean z2) {
        this.f582C = z2;
    }
}
