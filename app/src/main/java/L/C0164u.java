package L;

import G.aH;
import G.aI;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.Iterator;

/* renamed from: L.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/u.class */
public class C0164u extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    aI f1699a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1700b;

    /* renamed from: c, reason: collision with root package name */
    int f1701c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1702d;

    /* renamed from: e, reason: collision with root package name */
    int f1703e;

    /* renamed from: f, reason: collision with root package name */
    aH f1704f;

    /* renamed from: g, reason: collision with root package name */
    double f1705g;

    public C0164u(ax.ab abVar, ax.ab abVar2) {
        this.f1701c = -1;
        this.f1702d = null;
        this.f1703e = -1;
        this.f1704f = null;
        this.f1705g = Double.NaN;
        this.f1702d = abVar;
        this.f1700b = abVar2;
    }

    public C0164u(aI aIVar, ax.ab abVar) {
        this.f1701c = -1;
        this.f1702d = null;
        this.f1703e = -1;
        this.f1704f = null;
        this.f1705g = Double.NaN;
        this.f1699a = aIVar;
        this.f1700b = abVar;
    }

    public double a(ax.S s2) throws ax.U {
        double dB = this.f1702d != null ? this.f1702d.b(s2) : -1.0d;
        if (this.f1702d != null && this.f1703e != dB) {
            String[] strArrD = G.T.a().d();
            int length = strArrD.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                G.R rC = G.T.a().c(strArrD[i2]);
                if (rC.O().x() == dB) {
                    this.f1699a = rC;
                    this.f1703e = (int) dB;
                    break;
                }
                i2++;
            }
            this.f1701c = -1;
        }
        if (this.f1699a == null) {
            return Double.NaN;
        }
        int iB = (int) this.f1700b.b(s2);
        if (this.f1701c == -1 || iB != this.f1701c || this.f1704f == null) {
            Iterator itQ = this.f1699a.K().q();
            while (true) {
                if (!itQ.hasNext()) {
                    break;
                }
                aH aHVar = (aH) itQ.next();
                if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR) && aHVar.a() == iB) {
                    this.f1704f = aHVar;
                    this.f1701c = iB;
                    break;
                }
            }
            if (this.f1704f == null && iB >= 0) {
                throw new ax.U("No OutputChannel found for offset: " + iB);
            }
        }
        if (this.f1704f != null) {
            return this.f1704f.m();
        }
        return 30000.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "getChannelMaxByOffset( " + this.f1700b.toString() + " )";
    }
}
