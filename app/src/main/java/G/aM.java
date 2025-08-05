package G;

import bH.C0995c;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:G/aM.class */
public class aM extends Q implements Serializable {

    /* renamed from: c, reason: collision with root package name */
    private int f591c = -1;

    /* renamed from: d, reason: collision with root package name */
    private int f592d = 0;

    /* renamed from: e, reason: collision with root package name */
    private bV f593e = new bW(-1);

    /* renamed from: f, reason: collision with root package name */
    private int f594f = -1;

    /* renamed from: b, reason: collision with root package name */
    protected String f595b = null;

    /* renamed from: g, reason: collision with root package name */
    private String f596g = null;

    /* renamed from: h, reason: collision with root package name */
    private int f597h = -1;

    /* renamed from: i, reason: collision with root package name */
    private int f598i = -1;

    /* renamed from: j, reason: collision with root package name */
    private dh f599j = new B(0.0d);

    /* renamed from: k, reason: collision with root package name */
    private A f600k = null;

    /* renamed from: l, reason: collision with root package name */
    private cZ f601l = null;

    /* renamed from: m, reason: collision with root package name */
    private dh f602m = new B(1.0d);

    /* renamed from: n, reason: collision with root package name */
    private dh f603n = new B(0.0d);

    /* renamed from: o, reason: collision with root package name */
    private dh f604o = new B(Double.NEGATIVE_INFINITY);

    /* renamed from: p, reason: collision with root package name */
    private dh f605p = new B(Double.MAX_VALUE);

    /* renamed from: q, reason: collision with root package name */
    private dh f606q = new B(0.0d);

    /* renamed from: r, reason: collision with root package name */
    private dh f607r = new B(0.0d);

    /* renamed from: s, reason: collision with root package name */
    private ArrayList f608s = null;

    /* renamed from: t, reason: collision with root package name */
    private ArrayList f609t = null;

    /* renamed from: u, reason: collision with root package name */
    private boolean f610u = false;

    /* renamed from: v, reason: collision with root package name */
    private boolean f611v = false;

    /* renamed from: w, reason: collision with root package name */
    private boolean f612w = false;

    /* renamed from: x, reason: collision with root package name */
    private double f613x = Double.NaN;

    /* renamed from: y, reason: collision with root package name */
    private String f614y = "";

    /* renamed from: z, reason: collision with root package name */
    private bH.E f615z = null;

    /* renamed from: A, reason: collision with root package name */
    private boolean f616A = false;

    /* renamed from: B, reason: collision with root package name */
    private boolean f617B = false;

    /* renamed from: C, reason: collision with root package name */
    private boolean f618C = false;

    /* renamed from: D, reason: collision with root package name */
    private boolean f619D = false;

    /* renamed from: E, reason: collision with root package name */
    private boolean f620E = true;

    /* renamed from: F, reason: collision with root package name */
    private boolean f621F = true;

    /* renamed from: G, reason: collision with root package name */
    private boolean f622G = false;

    /* renamed from: H, reason: collision with root package name */
    private boolean f623H = false;

    /* renamed from: J, reason: collision with root package name */
    private int f625J = f590a;

    /* renamed from: a, reason: collision with root package name */
    public static int f590a = Integer.MAX_VALUE;

    /* renamed from: I, reason: collision with root package name */
    private static final int[] f624I = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, Integer.MIN_VALUE};

    public int d() {
        return this.f591c;
    }

    public void c(int i2) {
        this.f591c = i2;
    }

    public int b() {
        A aC2 = c();
        return aC2.a() * aC2.b();
    }

    public void d(int i2) {
        this.f625J = i2;
    }

    public int e() {
        return this.f594f;
    }

    public void e(int i2) {
        this.f594f = i2;
    }

    public bV f() {
        return this.f593e;
    }

    public int g() {
        return this.f593e.a();
    }

    public void a(bV bVVar) {
        this.f593e = bVVar;
    }

    public boolean h() {
        return this.f593e instanceof bW;
    }

    public String i() {
        return this.f595b;
    }

    public void a(double d2) {
        if (this.f608s == null) {
            this.f608s = new ArrayList();
        }
        if (this.f608s.contains(Double.valueOf(d2))) {
            return;
        }
        this.f608s.add(Double.valueOf(d2));
    }

    public List j() {
        return this.f608s;
    }

    public void a(String str) throws V.g {
        if (str == null || !(str.equals(ControllerParameter.PARAM_CLASS_BITS) || str.equals(ControllerParameter.PARAM_CLASS_SCALAR) || str.equals("string") || str.equals(ControllerParameter.PARAM_CLASS_ARRAY))) {
            throw new V.g("Invalid Parameter Class for EcuParamter " + aJ() + " attemped parameterClass: " + str + "\nParameter Class must be 1 of: " + ControllerParameter.PARAM_CLASS_BITS + "," + ControllerParameter.PARAM_CLASS_SCALAR + "," + ControllerParameter.PARAM_CLASS_ARRAY + ",string");
        }
        this.f595b = str;
    }

    public void a(boolean z2) {
        this.f610u = z2;
    }

    public void b(String str) throws V.g {
        if (!this.f595b.equals("string")) {
            a(str.startsWith(PdfOps.S_TOKEN) || str.startsWith(PdfOps.F_TOKEN));
            if (!str.equals("U08") && !str.equals("U16") && !str.equals("S08") && !str.equals("M08") && !str.equals("S16") && !str.equals("U32") && !str.equals("S32") && !str.equals("F32")) {
                throw new V.g("Do not know how to handle data type :" + str);
            }
            j(str.startsWith(PdfOps.M_TOKEN));
            e(Integer.parseInt(str.substring(1)) / 8);
            if (str.startsWith(PdfOps.F_TOKEN)) {
                g(true);
            }
        } else {
            if (!str.equals("ASCII")) {
                throw new V.g("Do not know how to handle string data type :" + str);
            }
            e(1);
        }
        this.f596g = str;
    }

    public int k() {
        return this.f597h;
    }

    public boolean a(int i2, int i3, int i4) {
        if (i2 != this.f591c) {
            return false;
        }
        return g() <= (i3 + i4) - 1 && (g() + (b() * this.f594f)) - 1 >= i3;
    }

    public void f(int i2) {
        this.f597h = i2;
    }

    public int l() {
        return this.f598i;
    }

    public void g(int i2) {
        this.f598i = i2;
    }

    public int a() {
        return c().b();
    }

    public int m() {
        return c().a();
    }

    public A c() {
        if (this.f600k == null) {
            this.f600k = new A(1, 1);
        }
        return this.f600k;
    }

    public void a(int i2, int i3) {
        this.f600k = new A(i2, i3);
    }

    public void a(A a2) {
        this.f600k = a2;
    }

    public boolean n() {
        return (c().d() instanceof bQ) || (c().c() instanceof bQ);
    }

    public String o() {
        try {
            return this.f601l != null ? this.f601l.a() : "";
        } catch (V.g e2) {
            Logger.getLogger(aM.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return "INVALID";
        }
    }

    public cZ p() {
        return this.f601l;
    }

    public void c(String str) {
        a(new C0094c(str));
    }

    public void a(cZ cZVar) {
        this.f601l = cZVar;
    }

    public double h(int i2) {
        return this.f602m.a(i2);
    }

    public void b(double d2) {
        this.f602m = new B(d2);
    }

    public void a(dh dhVar) {
        this.f602m = dhVar;
    }

    public void c(double d2) {
        this.f603n = new B(d2);
    }

    public void b(dh dhVar) {
        this.f603n = dhVar;
    }

    public double a(int i2) {
        double dA = this.f604o.a(i2);
        if (H()) {
            return dA;
        }
        if (I()) {
            double dI = dA - (i(i2) / 2.000000001d);
            return this.f602m.a(i2) / (((long) Math.ceil((float) ((this.f602m.a(i2) - (this.f603n.a() * dI)) / dI))) + this.f603n.a());
        }
        if (this.f602m.a(i2) == 0.0d) {
            return Double.MIN_VALUE;
        }
        return (((long) Math.ceil((float) ((dA / this.f602m.a(i2)) - this.f603n.a()))) + this.f603n.a()) * this.f602m.a(i2);
    }

    public double q() {
        return a(0);
    }

    public void c(dh dhVar) {
        this.f604o = dhVar;
    }

    public double r() {
        if (!this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY) || c().a() != 1) {
            return b(0);
        }
        double dA = this.f605p.a(0);
        for (int i2 = 0; i2 < b(); i2++) {
            double dA2 = this.f605p.a(i2);
            if (Double.isNaN(dA) || dA2 > dA) {
                dA = dA2;
            }
        }
        return dA;
    }

    public double b(int i2) {
        double dA = this.f605p.a(i2);
        if (H()) {
            return dA;
        }
        if (I()) {
            double dI = dA + (i(i2) / 2.000000001d);
            return this.f602m.a(i2) / (((long) Math.floor((this.f602m.a(i2) - (this.f603n.a() * dI)) / dI)) + this.f603n.a());
        }
        try {
            return BigDecimal.valueOf(dA).divide(BigDecimal.valueOf(this.f602m.a(i2)), 0, 3).doubleValue() * this.f602m.a(i2);
        } catch (Exception e2) {
            if (this.f602m.a(i2) == 0.0d) {
                return Double.MAX_VALUE;
            }
            bH.C.b(aJ() + ", No exact raw max for Max value: " + dA + ", scale: " + this.f602m.a(i2) + ", using old floor way.");
            return (((long) Math.floor((dA / this.f602m.a(i2)) - this.f603n.a())) + this.f603n.a()) * this.f602m.a(i2);
        }
    }

    public dh s() {
        return this.f605p;
    }

    public dh t() {
        return this.f604o;
    }

    public void d(dh dhVar) {
        this.f605p = dhVar;
    }

    public void d(String str) throws V.g {
        if (this.f609t == null) {
            this.f609t = new ArrayList();
        }
        int iPow = (int) Math.pow(2.0d, w());
        if (this.f609t.size() >= iPow) {
            throw new V.g("More bit options defined than possible, max options:" + iPow + ". Not adding: " + str);
        }
        this.f609t.add(str);
    }

    public int u() {
        int iRound = (int) Math.round(this.f599j.a());
        if (iRound < 0) {
            bH.C.a("Invalid digit expression for " + aJ() + ", should not be negative!");
            iRound = 0;
        }
        return iRound;
    }

    public dh v() {
        return this.f599j;
    }

    public void e(dh dhVar) throws V.g {
        if ((dhVar instanceof B) && dhVar.a() < 0.0d) {
            throw new V.g("Digits cannot be a negative hard number, assuming 0. " + dhVar.a());
        }
        this.f599j = dhVar;
    }

    public String[][] b(Y y2) throws V.g {
        String[][] strArr = new String[a()][m()];
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            strArr[0][0] = f(y2);
        } else if (this.f595b.equals("string")) {
            strArr[0][0] = d(y2);
        } else if (this.f595b.equals(ControllerParameter.PARAM_CLASS_SCALAR) || this.f595b.equals("string")) {
            strArr[0][0] = e(y2);
        } else if (this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
            int[][] iArrA = a(y2);
            for (int i2 = 0; i2 < iArrA.length; i2++) {
                for (int i3 = 0; i3 < iArrA[0].length; i3++) {
                    int i4 = i2;
                    int i5 = i3;
                    double dIntBitsToFloat = !I() ? H() ? (Float.intBitsToFloat(iArrA[i4][i5]) + this.f603n.a()) * this.f602m.a(i4) : (iArrA[i4][i5] + this.f603n.a()) * this.f602m.a(i4) : H() ? this.f602m.a(i4) / (Float.intBitsToFloat(iArrA[i4][i5]) + this.f603n.a()) : this.f602m.a(i4) / (iArrA[i4][i5] + this.f603n.a());
                    if (this.f615z != null) {
                        dIntBitsToFloat = this.f615z.a(dIntBitsToFloat);
                    }
                    strArr[i4][i5] = bH.W.b(dIntBitsToFloat, u());
                }
            }
        }
        return strArr;
    }

    public String[] c(Y y2) throws V.g {
        String[][] strArrB = b(y2);
        String[] strArr = new String[strArrB.length * strArrB[0].length];
        int[][] iArrA = a(y2);
        int i2 = 0;
        for (int i3 = 0; i3 < iArrA.length; i3++) {
            for (int i4 = 0; i4 < iArrA[0].length; i4++) {
                strArr[i2] = strArrB[i3][i4];
                i2++;
            }
        }
        return strArr;
    }

    public int[][] a(Y y2) throws V.g {
        int iB;
        int iA;
        if (g() + (a() * m() * e()) > y2.c(this.f591c)) {
            throw new V.g("Attempt to retrieve data beyond page size!\n\tCheck offset and size for parameter:" + aJ());
        }
        int[][] iArr = new int[a()][m()];
        int iG = g();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            for (int i3 = 0; i3 < iArr[0].length; i3++) {
                if (K()) {
                    iB = (c().b() - i2) - 1;
                    iA = (c().a() - i3) - 1;
                } else {
                    iB = i2;
                    iA = i3;
                }
                iArr[iB][iA] = C0995c.b(y2.b(d()), iG, e(), o(y2), z());
                if (N()) {
                    if (iArr[iB][iA] > ((int) Math.floor((float) ((b(iB) / this.f602m.a(iG)) - this.f603n.a())))) {
                        iArr[iB][iA] = (byte) iArr[iB][iA];
                    }
                }
                iG += e();
            }
        }
        return iArr;
    }

    public String d(Y y2) {
        return this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS) ? f(y2) : e(y2);
    }

    public String e(Y y2) {
        double dJ;
        try {
            if (this.f595b.equals("string")) {
                return n(y2) ? bH.W.a(C0995c.a(a(y2, this.f591c, g(), y()))) : this.f614y;
            }
            try {
                dJ = j(y2);
            } catch (V.g e2) {
                dJ = 0.0d;
                Logger.getLogger(aM.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            return bH.W.b(dJ, u());
        } catch (V.g e3) {
            bH.C.a(e3.getMessage());
            return "Error";
        }
    }

    public int w() {
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            return (l() - k()) + 1;
        }
        return 0;
    }

    public ArrayList x() {
        if (this.f609t != null && this.f609t.size() >= Math.pow(w(), 2.0d)) {
            return this.f609t;
        }
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            int iW = w();
            if (this.f609t == null) {
                this.f609t = new ArrayList();
            }
            for (int size = this.f609t.size(); size < Math.pow(2.0d, iW); size++) {
                this.f609t.add(PdfOps.DOUBLE_QUOTE__TOKEN + (size + ((int) this.f603n.a())) + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        }
        return this.f609t;
    }

    protected int[] a(Y y2, int i2, int i3, int i4) throws V.g {
        try {
            return y2.a(i2, i3, i4);
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new V.g("Invalid Page size on page: " + (i2 + 1) + ", unable to access index: " + e2.getMessage());
        }
    }

    public String f(Y y2) throws V.g {
        try {
            int iB = (C0995c.b(a(y2, this.f591c, g(), e()), 0, e(), o(y2), z()) & R()) >>> k();
            return a(x()) ? (String) x().get(iB) : "" + (iB + ((int) this.f603n.a()));
        } catch (Exception e2) {
            throw new V.g("Getting bit option for " + aJ() + ", optionDescriptions=" + ((Object) this.f609t), e2);
        }
    }

    public int[] g(Y y2) {
        return a(y2, this.f591c, g(), e() * c().b() * c().a());
    }

    public void a(Y y2, double[] dArr) throws V.g {
        if (!this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
            throw new V.g("Method only valid for Array Parameters");
        }
        if (m() != 1) {
            throw new V.g("Method only valid single arrays");
        }
        if (a() != dArr.length) {
            throw new V.g("array size does not match the size defined by " + aJ());
        }
        double[][] dArr2 = new double[dArr.length][1];
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            dArr2[i2][0] = dArr[i2];
        }
        a(y2, dArr2);
    }

    public double[] h(Y y2) throws V.g {
        if (!this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
            throw new V.g("Method only valid for Array Parameters");
        }
        if (m() != 1) {
            throw new V.g("Method only valid single arrays");
        }
        double[][] dArrI = i(y2);
        double[] dArr = new double[dArrI.length];
        for (int i2 = 0; i2 < dArr.length; i2++) {
            dArr[i2] = dArrI[i2][0];
        }
        return dArr;
    }

    public double[][] i(Y y2) throws V.g {
        int iB;
        int iA;
        Y yK = k(y2);
        double[][] dArr = new double[a()][m()];
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS) || this.f595b.equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
            dArr[0][0] = j(yK);
        } else if (this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
            int[][] iArrA = a(yK);
            for (int i2 = 0; i2 < iArrA.length; i2++) {
                for (int i3 = 0; i3 < iArrA[0].length; i3++) {
                    if (K()) {
                        iB = (c().b() - i2) - 1;
                        iA = (c().a() - i3) - 1;
                    } else {
                        iB = i2;
                        iA = i3;
                    }
                    if (this.f602m.a(iB) == 0.0d) {
                        bH.C.b("EcuParameter:: " + aJ() + ", index:" + iB + " scale resolves to 0!");
                    }
                    if (I()) {
                        if (H()) {
                            dArr[iB][iA] = this.f602m.a(iB) / (Float.intBitsToFloat(iArrA[iB][iA]) + this.f603n.a());
                        } else {
                            dArr[iB][iA] = this.f602m.a(iB) / (iArrA[iB][iA] + this.f603n.a());
                        }
                    } else if (H()) {
                        dArr[iB][iA] = (Float.intBitsToFloat(iArrA[iB][iA]) + this.f603n.a()) * this.f602m.a(iB);
                    } else {
                        dArr[iB][iA] = (iArrA[iB][iA] + this.f603n.a()) * this.f602m.a(iB);
                    }
                    if (this.f615z != null) {
                        dArr[iB][iA] = this.f615z.a(dArr[iB][iA]);
                    }
                    if (N()) {
                        if (dArr[iB][iA] > ((int) Math.floor((float) ((b(iB) / this.f602m.a(iB)) - this.f603n.a())))) {
                            dArr[iB][iA] = (byte) dArr[iB][iA];
                        }
                    }
                }
            }
        }
        return dArr;
    }

    public double j(Y y2) throws V.g {
        long jC;
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            return (n(y2) || Double.isNaN(this.f613x)) ? ((C0995c.b(a(y2, this.f591c, g(), e()), 0, e(), o(y2), z()) & R()) >> k()) + this.f603n.a() : this.f613x;
        }
        if (!this.f595b.equals(ControllerParameter.PARAM_CLASS_SCALAR) && !this.f595b.equals(bZ.f883d) && !this.f595b.equals(bZ.f884e) && ((!this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY) && !this.f595b.equals("string")) || this.f600k.a() != 1)) {
            throw new V.g("getDoubleValue only supported for type bits, string, scalars and 1D arrays : " + aJ() + "=" + this.f595b);
        }
        if (!n(y2) && !Double.isNaN(this.f613x)) {
            return this.f613x;
        }
        int[] iArrA = a(y2, this.f591c, g(), e());
        if (H()) {
            float fIntBitsToFloat = Float.intBitsToFloat(C0995c.b(iArrA, 0, e(), o(y2), z()));
            return !I() ? (fIntBitsToFloat + this.f603n.a()) * this.f602m.a() : this.f602m.a() / (fIntBitsToFloat + this.f603n.a());
        }
        if (z()) {
            jC = C0995c.b(iArrA, 0, e(), o(y2), z());
            switch (e() * 8) {
                case 8:
                    jC = (byte) jC;
                    break;
                case 16:
                    jC = (short) jC;
                    break;
            }
        } else {
            jC = C0995c.c(iArrA, 0, e(), o(y2), z());
        }
        double dA = !I() ? (jC + this.f603n.a()) * this.f602m.a() : this.f602m.a() / (jC + this.f603n.a());
        if (this.f615z != null) {
            dA = this.f615z.a(dA);
        }
        return dA;
    }

    public void a(Y y2, String str) {
        if (!this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            if (this.f595b.equals("string")) {
                if (str == null) {
                    str = "";
                }
                try {
                    byte[] bytes = str.getBytes("ISO8859_1");
                    if (bytes.length > y()) {
                        byte[] bArr = new byte[y()];
                        System.arraycopy(bytes, 0, bArr, 0, bArr.length);
                        bytes = bArr;
                    }
                    if (bytes.length < y()) {
                        byte[] bArr2 = new byte[y()];
                        C0995c.a(bArr2, (byte) 0);
                        System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
                        bytes = bArr2;
                    }
                    int[] iArrB = C0995c.b(bytes);
                    if (iArrB.length > y()) {
                        throw new V.g("String length " + iArrB.length + " exceeds defined length " + y());
                    }
                    a(y2, this.f591c, g(), iArrB, Q());
                    return;
                } catch (UnsupportedEncodingException e2) {
                    throw new V.g("Unsupported Character Encoding???");
                }
            }
            return;
        }
        if (str.equals("\"INVALID\"") || str.equals("INVALID")) {
            bH.C.d("Attempt to set Parameter " + aJ() + " to INVALID. This will be ignored.");
        }
        synchronized (y2) {
            if (this.f609t == null || !a(this.f609t)) {
                a(y2, Integer.parseInt(bH.W.b(str, PdfOps.DOUBLE_QUOTE__TOKEN, "")));
                return;
            }
            for (int i2 = 0; i2 < this.f609t.size(); i2++) {
                if (str.equals(this.f609t.get(i2))) {
                    int iB = C0995c.b(b(y2, d()), g(), e(), o(y2), z());
                    if (iB == Integer.MIN_VALUE) {
                        iB = 0;
                    }
                    a(y2, this.f591c, g(), C0995c.a((iB & (R() ^ (-1))) | (i2 << k()), new int[e()], o(y2)), Q());
                    return;
                }
            }
            String strB = bH.W.b(str, PdfOps.DOUBLE_QUOTE__TOKEN, "");
            if (!bH.H.a(strB)) {
                throw new V.g("No options found for Bit EcuParameter:" + aJ() + " equal to the proposed " + strB);
            }
            a(y2, Integer.parseInt(strB));
        }
    }

    protected void a(Y y2, int i2) throws V.g {
        if (i2 < 0 || (this.f609t != null && i2 < this.f609t.size() && ((String) this.f609t.get(i2)).equals("\"INVALID\""))) {
            throw new V.g("No valid options found for Bit EcuParameter:" + aJ() + " equal to the proposed " + i2);
        }
        a(y2, this.f591c, g(), C0995c.a((C0995c.b(b(y2, d()), g(), e(), o(y2), z()) & (R() ^ (-1))) | ((i2 - ((int) this.f603n.a())) << k()), new int[e()], o(y2)), Q());
    }

    protected void a(Y y2, int i2, int i3, int[] iArr, boolean z2) throws V.g {
        if (this.f617B) {
            return;
        }
        try {
            y2.a(i2, i3, iArr, z2, !M() && B());
        } catch (ArrayIndexOutOfBoundsException e2) {
            e2.printStackTrace();
            throw new V.g("Unable to set value for " + aJ() + ", offset:" + i3 + " not valid for the page size defined on page " + (i2 + 1));
        }
    }

    protected int[] b(Y y2, int i2) {
        return y2.b(d());
    }

    protected Y k(Y y2) {
        return y2;
    }

    public void a(Y y2, double d2, int i2, int i3) throws V.j, V.g {
        if (!this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
            throw new V.g("Can not update cell in non array paramClass");
        }
        if (i2 < 0 || i3 < 0 || i2 >= a() || i3 >= m()) {
            throw new V.g("Index out of bounds. row=" + i2 + ", column=" + i3 + ",\n" + aJ() + " is " + a() + LanguageTag.PRIVATEUSE + m());
        }
        if (!Double.isNaN(a(i2)) && d2 < a(i2)) {
            throw new V.j("Invalid Value for " + aJ(), 2, d2, a(i2), i2, i3);
        }
        if (!Double.isNaN(b(i2)) && d2 > b(i2)) {
            throw new V.j("Invalid Value for " + aJ(), 1, d2, b(i2), i2, i3);
        }
        double[][] dArrI = i(y2);
        if (Math.abs(dArrI[i2][i3] - d2) > i(i2) / 2.0d || dArrI[i2][i3] > b(i2) || dArrI[i2][i3] < a(i2)) {
            if (K()) {
                i2 = (c().b() - i2) - 1;
                i3 = (c().a() - i3) - 1;
            }
            a(y2, this.f591c, g() + (i2 * m() * e()) + (i3 * e()), C0995c.a(!I() ? H() ? Float.floatToIntBits((float) ((d2 / this.f602m.a(i2)) - this.f603n.a())) : Math.round((float) ((d2 / this.f602m.a(i2)) - this.f603n.a())) : H() ? Float.floatToIntBits((float) ((this.f602m.a(i2) - (this.f603n.a() * d2)) / d2)) : Math.round((float) ((this.f602m.a(i2) - (this.f603n.a() * d2)) / d2)), new int[e()], o(y2)), Q());
        }
    }

    public void a(Y y2, double d2) throws V.j, V.g {
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            a(y2, (int) d2);
            return;
        }
        if (this.f595b.equals("string")) {
            throw new V.g("set double not supported fot paramClass:" + this.f595b);
        }
        if (d2 >= r() + (A() / 2.0d)) {
            throw new V.j(aJ() + " Value must be between " + q() + " and " + r(), 1, d2, r());
        }
        if (d2 <= q() - (A() / 2.0d)) {
            throw new V.j(aJ() + " Value must be between " + q() + " and " + r(), 2, d2, q());
        }
        double[][] dArr = new double[1][1];
        dArr[0][0] = d2;
        a(y2, dArr);
    }

    public void a(Y y2, double[][] dArr) {
        int i2;
        int i3;
        int iB;
        this.f600k = c();
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            a(y2, "" + ((int) dArr[0][0]));
            return;
        }
        if (this.f595b.equals("string")) {
            throw new V.g("set double[][] not supported fot paramClass:string");
        }
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY) && dArr.length == 0 && (this.f600k.a() == 0 || this.f600k.b() == 0)) {
            return;
        }
        int iA = this.f600k.a();
        int length = dArr[0].length;
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY) && length != iA) {
            bH.C.a(aJ() + " columns " + length + " does not match current configuration " + iA);
        }
        int iB2 = this.f600k.b();
        int length2 = dArr.length;
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY) && length2 != iB2) {
            bH.C.a(aJ() + " rows " + length2 + " does not match current configuration " + iB2);
        }
        for (int i4 = 0; i4 < dArr.length; i4++) {
            for (int i5 = 0; i5 < dArr[0].length; i5++) {
                int i6 = i4;
                int i7 = i5;
                double d2 = dArr[i6][i7];
                if (this.f608s == null || this.f608s.isEmpty()) {
                    if (this.f616A && d2 >= b(i6) + (i(i6) / 2.0d)) {
                        throw new V.j(aJ() + " Value must be between " + a(i6) + " and " + b(i6), 1, dArr[i6][i7], b(i6), i6, i7);
                    }
                    if (this.f616A && d2 <= a(i6) - (A() / 2.0d)) {
                        throw new V.j(aJ() + " Value must be between " + a(i6) + " and " + r(), 2, dArr[i6][i7], a(i6), i6, i7);
                    }
                } else if (!this.f608s.contains(Double.valueOf(d2))) {
                    bH.C.b(aJ() + " row:" + i6 + ", col:" + i7 + ", Invalid value: " + d2 + ", Set to the 1st valid value: " + this.f608s.get(0));
                    dArr[i6][i7] = ((Double) this.f608s.get(0)).doubleValue();
                }
            }
        }
        int[] iArr = new int[dArr.length * dArr[0].length * e()];
        if (iArr.length > b() * e()) {
            throw new V.g(aJ() + " loaded size larger than configured size. Data truncated.");
        }
        for (int i8 = 0; i8 < dArr.length; i8++) {
            for (int i9 = 0; i9 < dArr[0].length; i9++) {
                if (K()) {
                    i2 = i8;
                    i3 = i9;
                    iB = (this.f600k.b() - i8) - 1;
                } else {
                    i2 = i8;
                    i3 = i9;
                    iB = i2;
                }
                double dB = dArr[i2][i3];
                if (this.f615z != null) {
                    dB = this.f615z.b(dB);
                }
                int[] iArrA = C0995c.a(!I() ? H() ? Float.floatToIntBits((float) ((dB / this.f602m.a(iB)) - this.f603n.a())) : Math.round((dB / this.f602m.a(iB)) - this.f603n.a()) : H() ? Math.round(Float.floatToIntBits((float) ((this.f602m.a(iB) - (this.f603n.a() * dB)) / dB))) : Math.round((float) ((this.f602m.a(iB) - (this.f603n.a() * dB)) / dB)), new int[e()], o(y2));
                for (int i10 = 0; i10 < iArrA.length; i10++) {
                    iArr[(i2 * dArr[0].length * e()) + (i3 * e()) + i10] = iArrA[i10];
                }
            }
        }
        a(y2, this.f591c, g(), iArr, Q());
    }

    public void a(Y y2, int[] iArr) throws V.j, V.g {
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS)) {
            throw new V.g("setRawValues int[][] not supported fot paramClass:bit");
        }
        if (this.f595b.equals("string")) {
            throw new V.g("setRawValues int[][] not supported fot paramClass:string");
        }
        if (this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY) && iArr.length != this.f600k.b() * this.f600k.a() * e()) {
            throw new V.g(aJ() + " wrong number of values for setRawValues, expecting raw bytes");
        }
        for (int i2 : iArr) {
            if (i2 < 0) {
                throw new V.j("Value to low for raw.", 2, i2, 0.0d);
            }
            if (i2 > 255.0d) {
                throw new V.j("Value to high for raw.", 1, i2, 255.0d);
            }
        }
        a(y2, this.f591c, g(), iArr, Q());
    }

    private boolean Q() {
        return !this.f621F;
    }

    public int y() {
        if (!this.f595b.equals(ControllerParameter.PARAM_CLASS_BITS) && !this.f595b.equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
            if (this.f595b.equals(ControllerParameter.PARAM_CLASS_ARRAY) || this.f595b.equals("string")) {
                return e() * b();
            }
            bH.C.b("getByteCount shouldn't have gotten here. Param Name:" + aJ());
            return 1;
        }
        return e();
    }

    private boolean a(ArrayList arrayList) {
        if (arrayList == null) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (!((String) it.next()).equals("\"INVALID\"")) {
                return true;
            }
        }
        return false;
    }

    private int R() {
        int i2 = 0;
        for (int iK = k(); iK <= l(); iK++) {
            i2 |= f624I[iK];
        }
        return i2;
    }

    public boolean z() {
        return this.f610u;
    }

    public double A() {
        return i(0);
    }

    public double i(int i2) {
        double dAbs = Math.abs(h(i2));
        double dPow = Math.pow(10.0d, -u());
        return (dAbs < dPow || H()) ? dPow : dAbs;
    }

    public double l(Y y2) throws V.g {
        if (!this.f595b.equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
            throw new V.g("increment not supported for paramClass: " + this.f595b);
        }
        if (I()) {
            a(y2, this.f591c, g(), C0995c.a(C0995c.b(r0, 0, r0.length, o(y2), false) - 1, g(y2), o(y2)), Q());
            return j(y2);
        }
        double dJ = j(y2);
        try {
            if (dJ + A() <= r()) {
                a(y2, dJ + A());
                return dJ + A();
            }
            a(y2, r());
            return r();
        } catch (V.j e2) {
            try {
                a(y2, r());
                return r();
            } catch (V.j e3) {
                Logger.getLogger(aM.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                return dJ;
            }
        }
    }

    public double m(Y y2) throws V.g {
        if (!this.f595b.equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
            throw new V.g("decrement not supported for paramClass: " + this.f595b);
        }
        if (I()) {
            int[] iArrG = g(y2);
            a(y2, this.f591c, g(), C0995c.a(C0995c.b(iArrG, 0, iArrG.length, o(y2), false) + 1, iArrG, o(y2)), Q());
            return j(y2);
        }
        double dJ = j(y2);
        try {
            if (dJ - A() >= q()) {
                a(y2, dJ - A());
                return dJ - A();
            }
            a(y2, q());
            return q();
        } catch (V.j e2) {
            try {
                a(y2, q());
                return q();
            } catch (V.j e3) {
                Logger.getLogger(aM.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                return dJ;
            }
        }
    }

    public boolean n(Y y2) {
        int[] iArrG = g(y2);
        if (i().equals("string")) {
            return iArrG.length > 0 && iArrG[0] >= 0;
        }
        for (int i2 : iArrG) {
            if (i2 < 0) {
                return false;
            }
        }
        if (!i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
            return true;
        }
        try {
            String strF = f(y2);
            if (strF.equals("INVALID")) {
                return false;
            }
            return !strF.equals("\"INVALID\"");
        } catch (V.g e2) {
            bH.C.b(e2.getLocalizedMessage());
            return false;
        }
    }

    public boolean B() {
        return this.f621F;
    }

    public void b(boolean z2) {
        this.f621F = z2;
    }

    public boolean C() {
        return this.f620E;
    }

    public void c(boolean z2) {
        this.f620E = z2;
    }

    public boolean D() {
        return this.f619D;
    }

    public void d(boolean z2) {
        this.f619D = z2;
    }

    public void d(double d2) {
        this.f613x = d2;
    }

    public dh E() {
        return this.f602m;
    }

    public dh F() {
        return this.f603n;
    }

    public void e(String str) {
        if (this.f609t.contains(str)) {
            d(this.f609t.indexOf(str));
        }
    }

    public void e(boolean z2) {
        this.f616A = z2;
    }

    public boolean G() {
        return this.f617B;
    }

    public void f(boolean z2) {
        this.f617B = z2;
    }

    public void f(String str) {
        this.f614y = str;
    }

    public boolean H() {
        return this.f611v;
    }

    public void g(boolean z2) {
        this.f611v = z2;
    }

    public void f(dh dhVar) {
        this.f606q = dhVar;
    }

    public boolean I() {
        return (this.f606q == null || this.f606q.a() == 0.0d) ? false : true;
    }

    public dh J() {
        if (this.f606q != null) {
            return this.f606q;
        }
        return null;
    }

    public void g(dh dhVar) {
        this.f607r = dhVar;
    }

    public boolean K() {
        return (this.f607r == null || this.f607r.a() == 0.0d) ? false : true;
    }

    public dh L() {
        if (this.f607r != null) {
            return this.f607r;
        }
        return null;
    }

    protected boolean o(Y y2) {
        return this.f622G ? !y2.b() : y2.b();
    }

    public void h(boolean z2) {
        this.f622G = z2;
    }

    public boolean M() {
        return this.f618C;
    }

    public void i(boolean z2) {
        this.f618C = z2;
    }

    public void a(bH.E e2) {
        this.f615z = e2;
    }

    public boolean N() {
        return this.f612w;
    }

    public void j(boolean z2) {
        this.f612w = z2;
    }

    public boolean O() {
        return this.f623H;
    }

    public void k(boolean z2) {
        this.f623H = z2;
    }

    public String[] P() {
        String[] strArrB;
        String[] strArrB2;
        ArrayList arrayList = new ArrayList();
        if ((c().d() instanceof bQ) && (strArrB2 = ((bQ) c().d()).b()) != null) {
            arrayList.addAll(Arrays.asList(strArrB2));
        }
        if ((c().c() instanceof bQ) && (strArrB = ((bQ) c().c()).b()) != null) {
            arrayList.addAll(Arrays.asList(strArrB));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }
}
