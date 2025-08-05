package L;

import G.aH;
import G.aI;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.Iterator;

/* renamed from: L.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/x.class */
public class C0167x extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    aI f1720a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1721b;

    /* renamed from: c, reason: collision with root package name */
    int f1722c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1723d;

    /* renamed from: e, reason: collision with root package name */
    int f1724e;

    /* renamed from: f, reason: collision with root package name */
    aH f1725f;

    /* renamed from: g, reason: collision with root package name */
    double f1726g;

    public C0167x(ax.ab abVar, ax.ab abVar2) {
        this.f1722c = -1;
        this.f1723d = null;
        this.f1724e = -1;
        this.f1725f = null;
        this.f1726g = Double.NaN;
        this.f1723d = abVar;
        this.f1721b = abVar2;
    }

    public C0167x(aI aIVar, ax.ab abVar) {
        this.f1722c = -1;
        this.f1723d = null;
        this.f1724e = -1;
        this.f1725f = null;
        this.f1726g = Double.NaN;
        this.f1720a = aIVar;
        this.f1721b = abVar;
    }

    public double a(ax.S s2) throws ax.U {
        double dB = this.f1723d != null ? this.f1723d.b(s2) : -1.0d;
        if (this.f1723d != null && this.f1724e != dB) {
            String[] strArrD = G.T.a().d();
            int length = strArrD.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                G.R rC = G.T.a().c(strArrD[i2]);
                if (rC.O().x() == dB) {
                    this.f1720a = rC;
                    this.f1724e = (int) dB;
                    break;
                }
                i2++;
            }
            this.f1722c = -1;
        }
        if (this.f1720a == null) {
            return Double.NaN;
        }
        int iB = (int) this.f1721b.b(s2);
        if (this.f1722c == -1 || iB != this.f1722c || this.f1725f == null) {
            Iterator itQ = this.f1720a.K().q();
            while (true) {
                if (!itQ.hasNext()) {
                    break;
                }
                aH aHVar = (aH) itQ.next();
                if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR) && aHVar.a() == iB) {
                    this.f1725f = aHVar;
                    this.f1722c = iB;
                    break;
                }
            }
            if (this.f1725f == null && iB >= 0) {
                throw new ax.U("No OutputChannel found for offset: " + iB);
            }
        }
        if (this.f1725f != null) {
            return this.f1725f.i();
        }
        return 0.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "getChannelTranslateByOffset( " + this.f1721b.toString() + " )";
    }
}
