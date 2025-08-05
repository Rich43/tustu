package aj;

import G.C0095ca;
import G.C0096cb;
import G.C0097cc;
import G.C0098cd;
import G.C0113cs;
import G.C0126i;
import G.C0130m;
import G.C0132o;
import G.InterfaceC0109co;
import G.R;
import G.T;
import G.aB;
import G.aI;
import G.da;
import L.C0154k;
import L.C0157n;
import W.C0184j;
import W.C0188n;
import ax.U;
import bH.C;
import bH.C0995c;
import bH.F;
import bH.W;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:aj/d.class */
public class d implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    byte[][] f4527a = (byte[][]) null;

    /* renamed from: b, reason: collision with root package name */
    byte[][] f4528b = (byte[][]) null;

    /* renamed from: c, reason: collision with root package name */
    byte[][] f4529c = (byte[][]) null;

    /* renamed from: p, reason: collision with root package name */
    private R f4530p = null;

    /* renamed from: q, reason: collision with root package name */
    private boolean f4532q = true;

    /* renamed from: r, reason: collision with root package name */
    private String f4533r = null;

    /* renamed from: s, reason: collision with root package name */
    private C0096cb f4534s = null;

    /* renamed from: e, reason: collision with root package name */
    h f4535e = new h(this);

    /* renamed from: f, reason: collision with root package name */
    ArrayList f4536f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    String f4537g = "";

    /* renamed from: h, reason: collision with root package name */
    List f4538h = new ArrayList();

    /* renamed from: t, reason: collision with root package name */
    private HashMap f4539t = new HashMap();

    /* renamed from: u, reason: collision with root package name */
    private da f4544u = new da();

    /* renamed from: w, reason: collision with root package name */
    private String f4546w = f4540i;

    /* renamed from: x, reason: collision with root package name */
    private int f4547x = 500;

    /* renamed from: y, reason: collision with root package name */
    private int f4548y = 10000;

    /* renamed from: z, reason: collision with root package name */
    private String f4549z = null;

    /* renamed from: A, reason: collision with root package name */
    private long f4550A = 0;

    /* renamed from: B, reason: collision with root package name */
    private double f4551B = 0.0d;

    /* renamed from: C, reason: collision with root package name */
    private i f4552C = null;

    /* renamed from: D, reason: collision with root package name */
    private j f4553D = null;

    /* renamed from: E, reason: collision with root package name */
    private int f4554E = -1;

    /* renamed from: m, reason: collision with root package name */
    int f4555m = 0;

    /* renamed from: n, reason: collision with root package name */
    double f4556n = 1.0d;

    /* renamed from: o, reason: collision with root package name */
    boolean f4557o = false;

    /* renamed from: d, reason: collision with root package name */
    protected static boolean f4531d = false;

    /* renamed from: i, reason: collision with root package name */
    public static String f4540i = C0096cb.f1066b;

    /* renamed from: j, reason: collision with root package name */
    public static String f4541j = C0096cb.f1067c;

    /* renamed from: k, reason: collision with root package name */
    public static String f4542k = C0096cb.f1065a;

    /* renamed from: l, reason: collision with root package name */
    public static String f4543l = C0096cb.f1068d;

    /* renamed from: v, reason: collision with root package name */
    private static String f4545v = "tsInternalLoggerCommand";

    public void a() {
        c();
        f(f4540i);
        this.f4527a = (byte[][]) null;
        this.f4528b = (byte[][]) null;
        a((C0096cb) null);
        e("");
        this.f4555m = 0;
        this.f4538h.clear();
    }

    public void b() {
        if (f4531d) {
            return;
        }
        f4531d = true;
        this.f4551B = 0.0d;
        this.f4532q = true;
        if (this.f4533r != null) {
            C0113cs.a().a(i().c(), this.f4533r, this);
            e();
            return;
        }
        if (this.f4552C != null) {
            this.f4552C.a();
        }
        if (this.f4554E != -1 && this.f4554E >= i().O().g()) {
            throw new V.a("Please enable the LOGPAGES setting in Project Properties");
        }
        this.f4552C = new i(this);
        this.f4552C.start();
    }

    public void c() {
        C0113cs.a().a(this);
        f4531d = false;
        if (this.f4552C != null) {
            this.f4552C.a();
            this.f4552C = null;
        }
        if (this.f4553D != null) {
            this.f4553D.b();
            this.f4553D = null;
        }
        C.d("Logger Processor stopped.");
    }

    protected void d() throws IOException {
        C0130m c0130mD = C0130m.d(i().O(), this.f4554E);
        c0130mD.b(this.f4535e);
        c0130mD.b(new e(this));
        i().C().b(c0130mD);
    }

    protected void e() throws NumberFormatException, IOException {
        if (this.f4554E == -1) {
            int[] iArrE = null;
            this.f4557o = true;
            boolean z2 = this.f4532q && this.f4528b != null;
            byte[][] bArr = z2 ? this.f4528b : this.f4527a;
            this.f4532q = false;
            for (byte[] bArr2 : bArr) {
                int[] iArr = new int[bArr2.length];
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    iArr[i2] = C0995c.a(bArr2[i2]);
                }
                C0130m c0130mA = C0130m.a(iArr);
                c0130mA.i(150);
                c0130mA.e(true);
                c0130mA.h(i().O());
                c0130mA.b(new f(this));
                C0132o c0132oA = this.f4544u.a(i(), c0130mA, 2000);
                if (c0132oA.a() != 1) {
                    C.a("Unable to read Logger data. " + c0132oA.c());
                    b(new C0188n());
                } else if (iArrE == null) {
                    iArrE = c0132oA.e();
                } else {
                    int[] iArr2 = new int[iArrE.length + c0132oA.e().length];
                    int[] iArrE2 = c0132oA.e();
                    System.arraycopy(iArrE, 0, iArr2, 0, iArrE.length);
                    System.arraycopy(iArrE2, 0, iArr2, iArrE.length, iArrE2.length);
                    iArrE = iArr2;
                }
            }
            this.f4557o = false;
            if (!z2) {
                if (this.f4534s != null && !this.f4534s.l()) {
                    c();
                }
                a(iArrE);
            }
        } else {
            d();
        }
        this.f4550A = System.currentTimeMillis();
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (f4531d && this.f4533r != null && str.equals(this.f4533r)) {
            String strB = W.b(this.f4549z, this.f4533r, d2 + "");
            try {
                if (!this.f4557o && (System.currentTimeMillis() - this.f4550A > this.f4548y || F.g(strB) != 0.0d)) {
                    j();
                }
            } catch (Exception e2) {
                C.a("Unable to evaluate Logger condition:" + this.f4549z);
                e2.printStackTrace();
            }
        }
    }

    public boolean f() {
        return f4531d;
    }

    public void a(String str) {
        if (str != null) {
            this.f4527a = d(str);
        } else {
            this.f4527a = (byte[][]) null;
        }
    }

    public void b(String str) {
        if (str != null) {
            this.f4528b = d(str);
        } else {
            this.f4528b = (byte[][]) null;
        }
    }

    public void c(String str) {
        if (str != null) {
            this.f4529c = d(str);
        } else {
            this.f4529c = (byte[][]) null;
        }
    }

    public byte[][] d(String str) {
        int iG = i().O().G(0);
        if (!i().equals(T.a().c())) {
            int iG2 = T.a().c().O().G(0);
            iG = iG2 < iG ? iG2 : iG;
        }
        byte[] bArrD = i().O().e(str).d();
        int iA = bArrD.length == 7 ? C0995c.a(bArrD, 3, 2, true, false) : -1;
        int iM = (bArrD.length != 7 || this.f4534s.m() >= 0) ? this.f4534s.m() : C0995c.a(bArrD, 5, 2, true, false);
        if (iA < 0 || iM < 0 || iG <= 0 || iG >= iM - iA) {
            return new byte[][]{bArrD};
        }
        byte[][] bArr = new byte[(int) Math.ceil(iM / iG)][7];
        byte[] bArrA = new byte[2];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            int i3 = iG < iM - iA ? iG : iM - iA;
            bArr[i2][0] = bArrD[0];
            bArr[i2][1] = bArrD[1];
            bArr[i2][2] = bArrD[2];
            byte[] bArrA2 = C0995c.a(iA, bArrA, true);
            bArr[i2][3] = bArrA2[0];
            bArr[i2][4] = bArrA2[1];
            bArrA = C0995c.a(i3, bArrA2, true);
            bArr[i2][5] = bArrA[0];
            bArr[i2][6] = bArrA[1];
            iA += i3;
        }
        return bArr;
    }

    public void e(String str) {
        this.f4533r = str;
    }

    protected void a(int[] iArr) throws NumberFormatException {
        if (this.f4534s == null || !this.f4534s.d().equals(f4543l)) {
            d(iArr);
        } else {
            b(iArr);
        }
    }

    private void b(int[] iArr) throws NumberFormatException {
        try {
            C0098cd c0098cdG = g();
            C0188n c0188n = new C0188n();
            for (int i2 = 0; i2 < c0098cdG.f(); i2++) {
                C0097cc c0097ccB = c0098cdG.b(i2);
                if (c0097ccB.b().equals(C0522c.f4524a)) {
                    this.f4555m = (int) c0098cdG.a(i2, iArr);
                } else if (c0097ccB.b().equals(C0522c.f4525b)) {
                    this.f4555m = ((int) c0098cdG.a(i2, iArr)) + c0098cdG.c();
                } else if (c0097ccB.b().equals(C0522c.f4526c)) {
                    if (((int) c0098cdG.a(i2, iArr)) == 1) {
                        this.f4556n = 100.0d;
                    } else {
                        this.f4556n = 1.0d;
                    }
                }
            }
            HashMap map = new HashMap();
            if (c0098cdG.d() > 0) {
                String str = "";
                for (int i3 = 0; i3 < c0098cdG.d(); i3++) {
                    C0097cc c0097ccC = c0098cdG.c(i3);
                    str = c0097ccC.d().length() > 0 ? str + c0097ccC.g() + "(" + c0097ccC.d() + ") = " + c0098cdG.b(i3, iArr) : str + c0097ccC.g() + " = " + c0098cdG.b(i3, iArr);
                    if (i3 + 1 < c0098cdG.d()) {
                        str = str + VectorFormat.DEFAULT_SEPARATOR;
                    }
                    map.put(c0097ccC.b(), Double.valueOf(c0098cdG.b(i3, iArr)));
                }
                if (str.length() > 0) {
                    str = PdfOps.DOUBLE_QUOTE__TOKEN + str;
                }
                c0188n.d(str);
            }
            String[] strArrA = c0098cdG.a();
            for (String str2 : strArrA) {
                map.put(str2, Double.valueOf(Double.NaN));
            }
            String[] strArrB = c0098cdG.b();
            for (int i4 = 0; i4 < strArrB.length; i4++) {
                C0184j c0184j = new C0184j(strArrB[i4]);
                c0184j.e(c0098cdG.a(i4).d());
                c0188n.a(c0184j);
            }
            if (c(iArr)) {
                b(c0188n);
                return;
            }
            int length = ((iArr.length - c0098cdG.e()) - c0098cdG.g()) / c0098cdG.c();
            c0098cdG.g(this.f4555m);
            for (int i5 = 0; i5 < length; i5++) {
                C0157n.a().b(i5);
                for (int i6 = 0; i6 < c0188n.size(); i6++) {
                    C0097cc c0097ccA = c0098cdG.a(strArrA[i6]);
                    C0184j c0184j2 = (C0184j) c0188n.get(i6);
                    if (c0097ccA instanceof C0095ca) {
                        C0095ca c0095ca = (C0095ca) c0097ccA;
                        double dA = Double.NaN;
                        try {
                            dA = a(c0095ca.a(), map, this.f4530p);
                        } catch (U e2) {
                            Logger.getLogger(d.class.getName()).log(Level.SEVERE, "Failed to evaluate Expression: '" + c0095ca.a() + PdfOps.SINGLE_QUOTE_TOKEN, (Throwable) e2);
                        }
                        map.put(strArrA[i6], Double.valueOf(dA));
                        c0184j2.b("" + dA);
                    } else if (a(map, c0097ccA)) {
                        double dA2 = c0098cdG.a(i6, iArr, i5);
                        map.put(strArrA[i6], Double.valueOf(dA2));
                        c0184j2.b("" + dA2);
                    } else {
                        Double d2 = (Double) map.get(strArrA[i6]);
                        if (d2 == null) {
                            c0184j2.b("NaN");
                        } else {
                            c0184j2.b(d2.toString());
                        }
                    }
                }
            }
            for (int i7 = 0; i7 < c0098cdG.i(); i7++) {
                if (c0188n.a(c0098cdG.a(i7).g()) != null && c0098cdG.a(i7).i()) {
                    c0188n.e(c0098cdG.a(i7).g());
                }
            }
            if (this.f4534s == null || this.f4538h.size() < this.f4534s.p()) {
                this.f4538h.add(c0188n);
            } else {
                C.b("Logger overflow, skipping data set.");
            }
            if (this.f4534s == null || this.f4534s.p() <= 1 || this.f4534s.p() != this.f4538h.size()) {
                if (this.f4534s == null || this.f4534s.p() <= 1) {
                    a(c0188n);
                    this.f4538h.clear();
                    return;
                }
                return;
            }
            C0188n c0188n2 = (C0188n) this.f4538h.get(0);
            for (int i8 = 1; i8 < this.f4538h.size(); i8++) {
                C0188n c0188n3 = (C0188n) this.f4538h.get(i8);
                String strG = c0188n2.g();
                if (c0188n3.g() != null && !c0188n3.g().isEmpty()) {
                    c0188n2.d(strG + "\n" + c0188n3.g());
                }
                for (int i9 = 0; i9 < c0188n2.size(); i9++) {
                    C0184j c0184j3 = (C0184j) c0188n2.get(i9);
                    C0184j c0184j4 = (C0184j) c0188n3.get(i9);
                    for (int i10 = 0; i10 < c0184j3.i() && i10 < c0184j4.i(); i10++) {
                        if (Float.isNaN(c0184j3.d(i10)) && !Float.isNaN(c0184j4.d(i10))) {
                            c0184j3.b(i10, c0184j4.d(i10));
                        }
                    }
                }
            }
            a(c0188n2);
            this.f4538h.clear();
        } catch (V.g e3) {
            Logger.getLogger(d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            C.a("Failed to generate Logger Record Definitions: \n" + e3.getMessage(), e3, null);
        }
    }

    private boolean a(HashMap map, C0097cc c0097cc) throws NumberFormatException {
        if (c0097cc.h() == null || c0097cc.h().length() == 0) {
            return true;
        }
        try {
            return a(c0097cc.h(), map, this.f4530p) != 0.0d;
        } catch (U e2) {
            Logger.getLogger(d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return true;
        }
    }

    private boolean c(int[] iArr) {
        if (iArr == null) {
            return true;
        }
        for (int i2 : iArr) {
            if (i2 != 0) {
                return false;
            }
        }
        return true;
    }

    private void d(int[] iArr) throws NumberFormatException {
        C0188n c0188n = new C0188n();
        try {
            C0098cd c0098cdG = g();
            for (int i2 = 0; i2 < c0098cdG.f(); i2++) {
                C0097cc c0097ccB = c0098cdG.b(i2);
                if (c0097ccB.b().equals(C0522c.f4524a)) {
                    this.f4555m = (int) c0098cdG.a(i2, iArr);
                } else if (c0097ccB.b().equals(C0522c.f4525b)) {
                    this.f4555m = ((int) c0098cdG.a(i2, iArr)) + c0098cdG.c();
                } else if (c0097ccB.b().equals(C0522c.f4526c)) {
                    if (((int) c0098cdG.a(i2, iArr)) == 1) {
                        this.f4556n = 100.0d;
                    } else {
                        this.f4556n = 1.0d;
                    }
                }
            }
            if (c0098cdG.d() > 0) {
                String str = "";
                for (int i3 = 0; i3 < c0098cdG.d(); i3++) {
                    C0097cc c0097ccC = c0098cdG.c(i3);
                    str = str + c0097ccC.g() + "(" + c0097ccC.d() + ") = " + c0098cdG.b(i3, iArr);
                    if (i3 + 1 < c0098cdG.d()) {
                        str = str + VectorFormat.DEFAULT_SEPARATOR;
                    }
                }
                c0188n.d(str);
            }
            String[] strArrB = c0098cdG.b();
            String[] strArrA = c0098cdG.a();
            HashMap map = new HashMap();
            for (int i4 = 0; i4 < strArrB.length; i4++) {
                map.put(strArrA[i4], Double.valueOf(Double.NaN));
            }
            String str2 = null;
            C0184j c0184j = null;
            for (int i5 = 0; i5 < strArrB.length; i5++) {
                C0184j c0184j2 = new C0184j(strArrB[i5]);
                c0184j2.e(c0098cdG.a(i5).d());
                c0188n.a(c0184j2);
                if (strArrB[i5].contains("ToothTime") || strArrB[i5].contains("TriggerTime")) {
                    str2 = strArrB[i5];
                    c0184j = c0184j2;
                }
            }
            C0184j c0184jA = c0188n.a("Time");
            boolean z2 = false;
            if (str2 == null || c0184jA != null) {
                z2 = true;
            } else {
                c0184jA = new C0184j("Time");
                c0184jA.e(c0184j.n());
                c0188n.a(c0184jA);
            }
            if (c(iArr)) {
                b(c0188n);
                return;
            }
            int length = ((iArr.length - c0098cdG.e()) - c0098cdG.g()) / c0098cdG.c();
            c0098cdG.g(this.f4555m);
            for (int i6 = 0; i6 < length; i6++) {
                for (int i7 = 0; i7 < c0188n.size(); i7++) {
                    C0184j c0184j3 = (C0184j) c0188n.get(i7);
                    if (z2 || !c0184j3.a().equals(c0184jA.a())) {
                        if (i6 == 1 && c0184j3.a().equals("PriLevel")) {
                            c0184j3.e(Float.MIN_VALUE);
                            c0184j3.d(Float.MAX_VALUE);
                        }
                        C0097cc c0097ccA = c0098cdG.a(strArrA[i7]);
                        if (c0097ccA instanceof C0095ca) {
                            C0095ca c0095ca = (C0095ca) c0097ccA;
                            try {
                                double dA = a(c0095ca.a(), map, this.f4530p);
                                map.put(strArrA[i7], Double.valueOf(dA));
                                c0184j3.a((float) dA);
                            } catch (U e2) {
                                String str3 = "Failed to evaluate Expression: '" + c0095ca.a() + PdfOps.SINGLE_QUOTE_TOKEN;
                                Logger.getLogger(d.class.getName()).log(Level.SEVERE, str3, (Throwable) e2);
                                c0188n.d(str3);
                                a(c0188n);
                                return;
                            }
                        } else if (c0184j3.a().equals(str2)) {
                            double dA2 = c0098cdG.a(i7, iArr, i6) * this.f4556n;
                            map.put(strArrA[i7], Double.valueOf(dA2));
                            c0184j3.a((float) dA2);
                        } else if (a(map, c0097ccA)) {
                            double dA3 = c0098cdG.a(i7, iArr, i6);
                            map.put(strArrA[i7], Double.valueOf(dA3));
                            c0184j3.b("" + dA3);
                        } else {
                            Double d2 = (Double) map.get(strArrA[i7]);
                            if (d2 == null) {
                                c0184j3.a(Float.NaN);
                            } else {
                                c0184j3.a(d2.floatValue());
                            }
                        }
                    } else if (i6 > 0) {
                        this.f4551B = c0184j.c(i6) + c0184jA.c(i6 - 1);
                        map.put(c0184j3.a(), Double.valueOf(this.f4551B));
                        c0184j3.a((float) this.f4551B);
                    } else {
                        double dC = (c0184j.c(i6) * this.f4556n) + this.f4551B;
                        map.put(c0184j3.a(), Double.valueOf(dC));
                        c0184j3.a((float) dC);
                    }
                }
            }
            int i8 = 0;
            while (i8 < c0098cdG.i()) {
                if (c0188n.a(c0098cdG.a(i8).g()) != null && c0098cdG.a(i8).i() && c0188n.e(c0098cdG.a(i8).g())) {
                    i8--;
                }
                i8++;
            }
            a(c0188n);
        } catch (V.g e3) {
            Logger.getLogger(d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            C.a("Failed to generate Logger Record Definitions: \n" + e3.getMessage(), e3, null);
        }
    }

    private void a(C0188n c0188n) {
        Iterator it = this.f4536f.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC0521b) it.next()).a(c0188n);
            } catch (Exception e2) {
                C.b("Caught Exception Notifying LoggerDataListener, continuing");
                e2.printStackTrace();
            }
        }
    }

    private void b(C0188n c0188n) {
        Iterator it = this.f4536f.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC0521b) it.next()).b(c0188n);
            } catch (Exception e2) {
                C.b("Caught Exception Notifying LoggerDataListener, continuing");
                e2.printStackTrace();
            }
        }
    }

    public void a(InterfaceC0521b interfaceC0521b) {
        this.f4536f.add(interfaceC0521b);
    }

    public void f(String str) {
        this.f4546w = str;
    }

    public void a(int i2) {
        this.f4547x = i2;
    }

    public void b(int i2) {
        this.f4548y = i2;
    }

    public void g(String str) {
        if (str == null) {
            this.f4549z = str;
            e(null);
            return;
        }
        String[] strArrE = C0126i.e(str, i());
        if (strArrE == null || strArrE.length == 0) {
            throw new V.a("Invalid Logger Read Condition:\nThe expression must contain at least 1 valid OutputChannel:\n" + str);
        }
        e(strArrE[0]);
        this.f4549z = str;
    }

    public C0098cd g() {
        if (this.f4534s == null) {
            return null;
        }
        return this.f4534s.b();
    }

    public void a(C0096cb c0096cb) {
        this.f4534s = c0096cb;
        this.f4539t.clear();
    }

    public C0096cb h() {
        return this.f4534s;
    }

    public R i() {
        return this.f4530p;
    }

    public void a(R r2) {
        this.f4530p = r2;
        this.f4539t.clear();
    }

    public void c(int i2) {
        this.f4554E = i2;
    }

    public double a(String str, HashMap map, aI aIVar) throws NumberFormatException {
        C0154k c0154k = (C0154k) this.f4539t.get(str);
        if (c0154k == null) {
            c0154k = new C0154k(aIVar);
            c0154k.a(str);
            this.f4539t.put(str, c0154k);
        }
        String[] strArrA = c0154k.a();
        for (int i2 = 0; i2 < strArrA.length; i2++) {
            if (map.get(strArrA[i2]) != null) {
                c0154k.a(strArrA[i2], ((Double) map.get(strArrA[i2])).doubleValue());
            } else {
                try {
                    c0154k.a(strArrA[i2], C0126i.b(strArrA[i2], aIVar));
                } catch (V.g e2) {
                    throw new U(e2.getMessage());
                }
            }
        }
        return c0154k.d();
    }

    public void j() {
        if (this.f4553D == null || !this.f4553D.isAlive()) {
            this.f4553D = new j(this);
            this.f4553D.start();
        }
        this.f4553D.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        if (this.f4529c != null) {
            for (byte[] bArr : this.f4529c) {
                int[] iArr = new int[bArr.length];
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    iArr[i2] = C0995c.a(bArr[i2]);
                }
                C0130m c0130mA = C0130m.a(iArr);
                c0130mA.i(150);
                c0130mA.e(true);
                c0130mA.h(i().O());
                c0130mA.b(new g(this));
                if (this.f4544u.a(i(), c0130mA, 2000).a() != 1) {
                    aB.a().a("Logger Stop Command failed.");
                }
            }
        }
    }
}
