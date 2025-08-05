package L;

import G.aH;
import G.aI;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.Iterator;

/* renamed from: L.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/t.class */
public class C0163t extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    aI f1692a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1693b;

    /* renamed from: c, reason: collision with root package name */
    int f1694c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1695d;

    /* renamed from: e, reason: collision with root package name */
    int f1696e;

    /* renamed from: f, reason: collision with root package name */
    aH f1697f;

    /* renamed from: g, reason: collision with root package name */
    double f1698g;

    public C0163t(ax.ab abVar, ax.ab abVar2) {
        this.f1694c = -1;
        this.f1695d = null;
        this.f1696e = -1;
        this.f1697f = null;
        this.f1698g = Double.NaN;
        this.f1695d = abVar;
        this.f1693b = abVar2;
    }

    public C0163t(aI aIVar, ax.ab abVar) {
        this.f1694c = -1;
        this.f1695d = null;
        this.f1696e = -1;
        this.f1697f = null;
        this.f1698g = Double.NaN;
        this.f1692a = aIVar;
        this.f1693b = abVar;
    }

    public double a(ax.S s2) throws ax.U {
        double dB = this.f1695d != null ? this.f1695d.b(s2) : -1.0d;
        if (this.f1695d != null && this.f1696e != dB) {
            String[] strArrD = G.T.a().d();
            int length = strArrD.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                G.R rC = G.T.a().c(strArrD[i2]);
                if (rC.O().x() == dB) {
                    this.f1692a = rC;
                    this.f1696e = (int) dB;
                    break;
                }
                i2++;
            }
            this.f1694c = -1;
        }
        if (this.f1692a == null) {
            return Double.NaN;
        }
        int iB = (int) this.f1693b.b(s2);
        if (this.f1694c == -1 || iB != this.f1694c || this.f1697f == null) {
            Iterator itQ = this.f1692a.K().q();
            while (true) {
                if (!itQ.hasNext()) {
                    break;
                }
                aH aHVar = (aH) itQ.next();
                if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR) && aHVar.a() == iB) {
                    this.f1697f = aHVar;
                    this.f1694c = iB;
                    break;
                }
            }
            if (this.f1697f == null && iB >= 0) {
                throw new ax.U("No OutputChannel found for offset: " + iB);
            }
        }
        if (this.f1697f != null) {
            return this.f1697f.d();
        }
        return 0.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "getChannelDigitsByOffset( " + this.f1693b.toString() + " )";
    }
}
