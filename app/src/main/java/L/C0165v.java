package L;

import G.aH;
import G.aI;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.Iterator;

/* renamed from: L.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/v.class */
public class C0165v extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    aI f1706a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1707b;

    /* renamed from: c, reason: collision with root package name */
    int f1708c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1709d;

    /* renamed from: e, reason: collision with root package name */
    int f1710e;

    /* renamed from: f, reason: collision with root package name */
    aH f1711f;

    /* renamed from: g, reason: collision with root package name */
    double f1712g;

    public C0165v(ax.ab abVar, ax.ab abVar2) {
        this.f1708c = -1;
        this.f1709d = null;
        this.f1710e = -1;
        this.f1711f = null;
        this.f1712g = Double.NaN;
        this.f1709d = abVar;
        this.f1707b = abVar2;
    }

    public C0165v(aI aIVar, ax.ab abVar) {
        this.f1708c = -1;
        this.f1709d = null;
        this.f1710e = -1;
        this.f1711f = null;
        this.f1712g = Double.NaN;
        this.f1706a = aIVar;
        this.f1707b = abVar;
    }

    public double a(ax.S s2) throws ax.U {
        double dB = this.f1709d != null ? this.f1709d.b(s2) : -1.0d;
        if (this.f1709d != null && this.f1710e != dB) {
            String[] strArrD = G.T.a().d();
            int length = strArrD.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                G.R rC = G.T.a().c(strArrD[i2]);
                if (rC.O().x() == dB) {
                    this.f1706a = rC;
                    this.f1710e = (int) dB;
                    break;
                }
                i2++;
            }
            this.f1708c = -1;
        }
        if (this.f1706a == null) {
            return Double.NaN;
        }
        int iB = (int) this.f1707b.b(s2);
        if (this.f1708c == -1 || iB != this.f1708c || this.f1711f == null) {
            Iterator itQ = this.f1706a.K().q();
            while (true) {
                if (!itQ.hasNext()) {
                    break;
                }
                aH aHVar = (aH) itQ.next();
                if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR) && aHVar.a() == iB) {
                    this.f1711f = aHVar;
                    this.f1708c = iB;
                    break;
                }
            }
            if (this.f1711f == null && iB >= 0) {
                throw new ax.U("No OutputChannel found for offset: " + iB);
            }
        }
        if (this.f1711f != null) {
            return this.f1711f.n();
        }
        return 0.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "getChannelMinByOffset( " + this.f1707b.toString() + " )";
    }
}
