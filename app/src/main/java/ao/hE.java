package ao;

import W.C0184j;
import W.C0188n;
import com.efiAnalytics.ui.C1562b;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;
import com.sun.corba.se.impl.util.Version;
import g.C1726d;
import g.C1727e;
import g.C1733k;
import h.C1737b;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.net.tftp.TFTP;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:ao/hE.class */
public class hE implements Runnable {

    /* renamed from: e, reason: collision with root package name */
    String f6010e;

    /* renamed from: f, reason: collision with root package name */
    String f6011f;

    /* renamed from: g, reason: collision with root package name */
    String f6012g;

    /* renamed from: h, reason: collision with root package name */
    String f6013h;

    /* renamed from: r, reason: collision with root package name */
    C1701s f6023r;

    /* renamed from: a, reason: collision with root package name */
    String f6006a = "O2Corrected";

    /* renamed from: b, reason: collision with root package name */
    String f6007b = h.g.a().a(h.g.f12249h);

    /* renamed from: c, reason: collision with root package name */
    String f6008c = this.f6006a + "|nb.inc";

    /* renamed from: d, reason: collision with root package name */
    String f6009d = null;

    /* renamed from: i, reason: collision with root package name */
    String f6014i = h.g.a().a(h.g.f12246e);

    /* renamed from: j, reason: collision with root package name */
    String f6015j = "MAP";

    /* renamed from: k, reason: collision with root package name */
    String f6016k = "RPM";

    /* renamed from: l, reason: collision with root package name */
    C1562b[][] f6017l = (C1562b[][]) null;

    /* renamed from: m, reason: collision with root package name */
    C1589c f6018m = new C1589c();

    /* renamed from: n, reason: collision with root package name */
    ArrayList f6019n = new ArrayList();

    /* renamed from: o, reason: collision with root package name */
    hH f6020o = null;

    /* renamed from: p, reason: collision with root package name */
    hH f6021p = null;

    /* renamed from: q, reason: collision with root package name */
    C1701s f6022q = null;

    /* renamed from: s, reason: collision with root package name */
    C0188n f6024s = null;

    /* renamed from: B, reason: collision with root package name */
    private C0587E[] f6025B = null;

    /* renamed from: t, reason: collision with root package name */
    int f6026t = 1;

    /* renamed from: u, reason: collision with root package name */
    int f6027u = 2;

    /* renamed from: v, reason: collision with root package name */
    Thread f6028v = null;

    /* renamed from: w, reason: collision with root package name */
    int f6029w = 0;

    /* renamed from: x, reason: collision with root package name */
    boolean f6030x = false;

    /* renamed from: y, reason: collision with root package name */
    int f6031y = TFTP.DEFAULT_TIMEOUT;

    /* renamed from: z, reason: collision with root package name */
    boolean f6032z = h.i.a(h.i.f12340aG, h.i.f12341aH);

    /* renamed from: A, reason: collision with root package name */
    C0184j f6033A = null;

    public hE() {
        this.f6010e = null;
        this.f6011f = null;
        this.f6012g = null;
        this.f6013h = null;
        this.f6010e = h.i.c("DEFINE_CURRENT_CELL_VALUE");
        this.f6012g = h.i.c("DEFINE_CURRENT_AFR_VALUE");
        this.f6011f = h.i.c("DEFINE_INTERPOLATED_CELL_VALUE");
        this.f6013h = h.i.c("DEFINE_AFR_RECORD_OFFSET_VALUE");
    }

    public void a(double d2) {
        this.f6018m.f(d2);
    }

    public void a(hH hHVar, hH hHVar2, C1701s c1701s, C0188n c0188n, int i2, String str, String str2, C0587E[] c0587eArr) {
        this.f6020o = hHVar;
        this.f6021p = a(hHVar);
        this.f6022q = hHVar2;
        this.f6023r = c1701s;
        this.f6024s = c0188n;
        a(c0587eArr);
        this.f6026t = i2;
        this.f6007b = str;
        this.f6033A = null;
        if (this.f6028v != null && this.f6028v.isAlive()) {
            a();
        }
        this.f6029w = 0;
        this.f6009d = "(cellValue*[GEGO_CHANNEL_TAG]/100*[AFR_CHANNEL_TAG+WBafrOffSet]/afrValue)+(cellInterpolated*[GEGO_CHANNEL_TAG]/100*[AFR_CHANNEL_TAG]+WBafrOffSet]/afrValue)/2";
        this.f6009d = h.g.a().c(this.f6009d);
        this.f6015j = hHVar.v();
        this.f6016k = h.g.a().a(h.g.f12245d);
        if (this.f6010e == null) {
            throw new V.h("DEFINE_CURRENT_CELL_VALUE not found in Properties File. Please define in App properties");
        }
        if (this.f6012g == null) {
            throw new V.h("DEFINE_CURRENT_AFR_VALUE not found in Properties File. Please define in App properties");
        }
        if (this.f6013h == null) {
            throw new V.h("DEFINE_AFR_RECORD_OFFSET_VALUE not found in Properties File. Please define in MSGraph.properties");
        }
        if (this.f6009d == null) {
            throw new V.h("formula not found in Properties File. Please define in MSGraph.properties");
        }
        if (c0188n.a(this.f6016k) == null) {
            throw new V.h("X axis field \"" + this.f6016k + "\" not found in Log File.\nThis Field is required for VE Analyze to work.");
        }
        if (c0188n.a(this.f6015j) == null) {
            throw new V.h("Y axis field \"" + this.f6015j + "\" not found in Log File.\nPlease check to make sure it is available on the graph.\nIf this configuration is Alpha-N activate \"TP ADC\" from the Optional Fields menu.\nThen select \"TP ADC\" from the Options Menu");
        }
        if (c1701s == null) {
            this.f6009d = C1733k.a(this.f6009d, this.f6013h, h.i.a("WBafrOffSet", "2"));
            this.f6027u = Integer.parseInt(h.i.a("WBafrOffSet", "2"));
        }
        if (c0188n.a(this.f6014i) == null) {
            System.out.println(this.f6014i + " not found, 100 assumed for VE Analysis.");
            this.f6009d = C1733k.a(this.f6009d, "[GEGO_CHANNEL_TAG]", "100");
        } else if (c0188n.a(this.f6014i).a(0, c0188n.d() - 1, -10.0f, 127.0f) < 50.0d) {
            this.f6009d = C1733k.a(this.f6009d, "[GEGO_CHANNEL_TAG]", "([" + this.f6014i + "]+100)");
        } else {
            this.f6009d = C1733k.a(this.f6009d, "GEGO_CHANNEL_TAG", this.f6014i);
        }
        if (i2 == 1) {
            if (c0188n.a(str) == null) {
                throw new V.h("Please select WideBand O2 sensor from menu, or use Narrowband mode.\nNote: To use Narrowband the data log must be captured with a narrowband O2 sensor.");
            }
            if (c0188n.a(str).f() == 0.0d) {
                throw new V.h("The selected EGO Sensor does not contain values.\nPlease check your log file.");
            }
            if (!str.toLowerCase().contains("lambda") || (hHVar2 != null && hHVar2.e(0, 0).doubleValue() <= 2.5d)) {
                this.f6009d = C1733k.a(this.f6009d, "AFR_CHANNEL_TAG", str);
            } else {
                this.f6009d = C1733k.a(this.f6009d, "AFR_CHANNEL_TAG", str);
                this.f6009d = C1733k.a(this.f6009d, "afrValue", "BlaHbLah");
                this.f6009d = C1733k.a(this.f6009d, "BlaHbLah", "afrValue*14.7");
            }
        } else if (i2 == 2) {
            this.f6009d = C1733k.a(this.f6009d, "AFR_CHANNEL_TAG", this.f6008c);
        }
        if (this.f6022q == null && i2 == 1) {
            throw new V.h("No AFR Table set, only Narrow band is available");
        }
        if (this.f6022q == null || (!this.f6022q.z().toUpperCase().contains("LAMBDA") && this.f6022q.getValueAt(0, 0).doubleValue() < 2.0d)) {
            this.f6022q = hHVar.e();
        }
        this.f6017l = new C1562b[hHVar.getRowCount()][hHVar.getColumnCount()];
        b();
    }

    private int a(double d2, double d3, int i2) {
        C0184j c0184jD = d();
        if (this.f6023r == null || c0184jD == null) {
            return h.i.b("WBafrOffSet", 2);
        }
        double dA = C1677fh.a(this.f6023r, d2, d3) / 1000.0d;
        float fD = c0184jD.d(i2);
        float fD2 = fD;
        int iV = c0184jD.v();
        int i3 = 0;
        while (i3 + i2 < iV) {
            float f2 = fD2;
            fD2 = c0184jD.d(i2 + i3);
            if (fD2 - fD > dA) {
                return (i3 <= 0 || Math.abs(fD - fD2) >= Math.abs(fD - f2)) ? i3 - 1 : i3;
            }
            i3++;
        }
        return h.i.b("WBafrOffSet", 2);
    }

    private C0184j d() {
        if (this.f6033A == null) {
            this.f6033A = this.f6024s.a(h.g.a().a("Time"));
            if (this.f6033A == null) {
                bH.C.b("No time column for using Lambda Delay Table");
            }
        }
        return this.f6033A;
    }

    private double a(C0188n c0188n) throws V.h {
        C0184j c0184jA = c0188n.a(this.f6007b);
        if (c0184jA == null) {
            throw new V.h("O2 Voltage field " + this.f6007b + " not found in current log,\nThis is required for Narrow Band mode.\n");
        }
        float[] fArr = new float[c0188n.d()];
        double d2 = 0.0d;
        double d3 = 0.0d;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < fArr.length; i4++) {
            fArr[i4] = c0184jA.c(i4);
            boolean zA = false;
            try {
                zA = a(c0188n, c(), i4);
                String strA = h.g.a().a(h.g.f12247f);
                if (!zA && c0188n.a(strA) != null) {
                    zA = ((double) c0188n.a(strA).c(i4)) > 80.0d;
                }
            } catch (Exception e2) {
            }
            if (!zA) {
                if (fArr[i4] < 0.451f - 0.1f) {
                    i3++;
                    d3 += fArr[i4];
                } else if (fArr[i4] > 0.451f + 0.1f) {
                    i2++;
                    d2 += fArr[i4];
                }
            }
        }
        double d4 = 0.451f - (((d3 / i3) + (d2 / i2)) / 2.0d);
        if (Double.isNaN(d4)) {
            d4 = 0.0d;
        }
        bH.C.c("low Average=" + (d3 / i3) + ", High Average=" + (d2 / i2) + ", Offset=" + d4);
        if (c0188n.a(this.f6006a) != null) {
            c0188n.e(this.f6006a);
        }
        C0184j c0184j = new C0184j(this.f6006a);
        c0188n.a(c0184j);
        for (float f2 : fArr) {
            c0184j.b("" + (f2 + d4));
        }
        return d4;
    }

    protected hH a(hH hHVar) {
        hH hHVar2 = new hH();
        hHVar2.a(hHVar.getRowCount(), hHVar.getColumnCount());
        hH hHVar3 = (hH) C1677fh.a(hHVar, hHVar2);
        hHVar3.a(hHVar.j());
        hHVar3.d(hHVar.v());
        hHVar3.a(hHVar.g());
        b(hHVar3);
        return hHVar3;
    }

    public void a() {
        if (this.f6028v == null || !this.f6028v.isAlive()) {
            return;
        }
        this.f6030x = true;
        while (this.f6030x) {
            try {
                Thread.currentThread();
                Thread.sleep(20L);
            } catch (Exception e2) {
            }
        }
    }

    private void e() throws V.h {
        long jCurrentTimeMillis = System.currentTimeMillis();
        C1701s c1701s = this.f6022q;
        this.f6017l = a(this.f6017l, this.f6021p, c1701s);
        this.f6021p.c(1);
        int iD = this.f6024s.d();
        if (this.f6026t == 2) {
            a(this.f6024s);
        }
        C0184j c0184jA = this.f6024s.a(this.f6007b);
        String strV = c1701s.v();
        String str = (strV == null || strV.isEmpty() || this.f6024s.a(strV) == null) ? this.f6015j : strV;
        this.f6032z = h.i.a(h.i.f12340aG, h.i.f12341aH);
        int i2 = 0;
        while (true) {
            if (i2 + this.f6027u >= iD) {
                break;
            }
            if (this.f6030x) {
                this.f6030x = false;
                return;
            }
            if (a(this.f6024s, c(), i2) || (this.f6032z && c0184jA != null && c0184jA.e() != c0184jA.f() && c0184jA.c(i2) == c0184jA.e())) {
                this.f6029w++;
            } else {
                if (!C1737b.a().a("advancedVeAnalyze")) {
                    String str2 = "";
                    for (int i3 = 0; i3 < 500; i3++) {
                        str2 = str2 + PdfOps.S_TOKEN + i3;
                    }
                    if (i2 - this.f6029w >= this.f6031y) {
                        f();
                        this.f6029w += iD - i2;
                        break;
                    }
                }
                float fC = this.f6024s.a(this.f6016k).c(i2);
                float fC2 = this.f6024s.a(this.f6015j).c(i2);
                float fC3 = this.f6024s.a(str).c(i2);
                try {
                    double dB = C1677fh.b(this.f6020o.d(), fC);
                    double rowCount = (this.f6020o.getRowCount() - C1677fh.a(this.f6020o.c(), fC2)) - 1.0d;
                    double dB2 = C1677fh.b(c1701s.b(), fC);
                    double rowCount2 = (c1701s.getRowCount() - C1677fh.a(c1701s.a(), fC3)) - 1.0d;
                    for (int i4 = 0; i4 < this.f6020o.getRowCount(); i4++) {
                        a(dB, fC, rowCount, fC2, i4, c1701s, rowCount2, dB2, i2);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    throw new V.h("Error calculating VE values. Debug info written to log file.");
                }
            }
            a(this.f6017l, i2 + 1, iD);
            i2++;
        }
        System.out.println("VE Analyze completed in: " + ((System.currentTimeMillis() - jCurrentTimeMillis) / 1000.0d) + " s.");
        a(this.f6017l, this.f6021p, this.f6029w);
    }

    public void a(double d2, float f2, double d3, float f3, int i2, C1701s c1701s, double d4, double d5, int i3) throws NumberFormatException {
        for (int i4 = 0; i4 < this.f6020o.getColumnCount(); i4++) {
            if (i4 - d2 > -1.0d && i4 - d2 < 1.0d && i2 - d3 > -1.0d && i2 - d3 < 1.0d) {
                a(d2, f2, i4, d3, f3, i2, c1701s, d4, d5, i3);
            }
        }
    }

    public void a(double d2, float f2, int i2, double d3, float f3, int i3, C1701s c1701s, double d4, double d5, int i4) throws NumberFormatException {
        double dAbs = d2 - ((double) i2) >= 0.0d ? Math.abs((d2 - i2) - 1.0d) : Math.abs((1.0d + d2) - i2);
        double dAbs2 = d3 - ((double) i3) >= 0.0d ? Math.abs((d3 - i3) - 1.0d) : Math.abs((1.0d + d3) - i3);
        String strA = C1733k.a(this.f6009d, this.f6010e, (this.f6020o.e(i3, i2).doubleValue() + this.f6020o.j()) + "");
        if (this.f6011f != null && strA.indexOf(this.f6011f) != -1) {
            strA = C1733k.a(strA, this.f6011f, (this.f6020o.a(d3, d2) + this.f6020o.j()) + "");
        }
        String strA2 = C1733k.a(strA, this.f6012g, c1701s.a(d4, d5) + "");
        this.f6027u = a(f2, f3, i4);
        String strA3 = C1733k.a(strA2, this.f6013h, this.f6027u + "");
        double d6 = (dAbs == 0.0d || dAbs2 == 0.0d) ? 0.005d : dAbs * dAbs2;
        String strA4 = C1726d.a(this.f6024s, strA3, i4);
        if (this.f6020o.j() != 0.0d) {
            strA4 = " ( " + strA4 + " ) - " + this.f6020o.j();
        }
        this.f6017l[i3][i2].a(new Double(C1727e.a(this.f6024s, strA4, i4)), d6);
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            e();
        } catch (V.h e2) {
            a(e2);
            e2.printStackTrace();
        }
    }

    public boolean a(C0188n c0188n, C0587E[] c0587eArr, int i2) throws V.h {
        if (c0587eArr == null) {
            return false;
        }
        for (int i3 = 0; i3 < c0587eArr.length; i3++) {
            try {
                if (c0587eArr[i3].a(c0188n, i2)) {
                    return true;
                }
            } catch (Exception e2) {
                throw new V.h("Invalid filter criteria:\n" + c0587eArr[i3].toString());
            }
        }
        return false;
    }

    public C1562b[][] a(C1562b[][] c1562bArr, C1701s c1701s, C1701s c1701s2) {
        double d2;
        double d3;
        double d4 = Double.MIN_VALUE;
        double d5 = Double.NaN;
        try {
            d2 = Double.parseDouble(h.i.a("veAnalysisWeightThreshold", Version.BUILD));
            System.out.println("weightThreshold set to:" + d2);
        } catch (Exception e2) {
            d2 = 0.0d;
            System.out.println("Error retrieving veAnalysisWeightThreshold from properties file. using 0.0");
        }
        try {
            d3 = Double.parseDouble(h.i.a("veAnalysisWindowThreshold", Version.BUILD));
        } catch (Exception e3) {
            d3 = 0.0d;
            System.out.println("Error retrieving veAnalysisWindowThreshold from properties file. using 0.0");
        }
        try {
            d4 = Double.parseDouble(h.i.a("veAnalysisMaxValChange", "50.0"));
        } catch (Exception e4) {
            d3 = 0.0d;
            System.out.println("Error retrieving veAnalysisMaxValChange from properties file. using 50.0");
        }
        try {
            d5 = Double.parseDouble(h.i.a("veAnalysisMaxPercentChange", "50.0")) / 100.0d;
        } catch (Exception e5) {
            d3 = 0.0d;
            System.out.println("Error retrieving veAnalysisMaxPercentChange from properties file. Disabling");
        }
        this.f6018m.c(d3);
        this.f6018m.a(d2);
        this.f6018m.e(d5);
        this.f6018m.d(d4);
        return c1701s.a(c1701s2, this.f6018m);
    }

    public void a(InterfaceC0609a interfaceC0609a) {
        this.f6019n.add(interfaceC0609a);
    }

    private void a(C1562b[][] c1562bArr, int i2, int i3) {
        Iterator it = this.f6019n.iterator();
        while (it.hasNext()) {
            ((InterfaceC0609a) it.next()).a(c1562bArr, i2, i3);
        }
    }

    private void a(V.h hVar) {
        Iterator it = this.f6019n.iterator();
        while (it.hasNext()) {
            ((InterfaceC0609a) it.next()).a(hVar);
        }
    }

    private void a(C1562b[][] c1562bArr, hH hHVar, int i2) {
        Iterator it = this.f6019n.iterator();
        while (it.hasNext()) {
            ((InterfaceC0609a) it.next()).a(c1562bArr, hHVar, i2);
        }
    }

    private void b(hH hHVar) {
        Iterator it = this.f6019n.iterator();
        while (it.hasNext()) {
            ((InterfaceC0609a) it.next()).a(hHVar);
        }
    }

    public void b() {
        if (this.f6028v == null || !this.f6028v.isAlive()) {
            this.f6028v = new Thread(this);
            this.f6028v.start();
        }
    }

    public C0587E[] c() {
        return this.f6025B;
    }

    public void a(C0587E[] c0587eArr) {
        this.f6025B = c0587eArr;
    }

    private void f() {
        com.efiAnalytics.ui.bV.d("<html><body>The " + C1737b.f12222b + " edition of VE Analyze is limited to " + this.f6031y + " records.<br><br>Only the first " + this.f6031y + " records have been used.<br>The remainder will be included int the Filtered Record Count.<br><br><b>Upgrade to:</b><br> - Set AFR Target Table<br> - Adjust Target AFR Values<br> - Set Limits<br> - Adjust Filters<br> - Process larger files<br> - Process 5 times faster<br></body></html>", null);
    }
}
