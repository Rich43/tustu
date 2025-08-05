package L;

import G.aH;
import G.aI;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.Iterator;

/* renamed from: L.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/w.class */
public class C0166w extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    aI f1713a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1714b;

    /* renamed from: c, reason: collision with root package name */
    int f1715c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1716d;

    /* renamed from: e, reason: collision with root package name */
    int f1717e;

    /* renamed from: f, reason: collision with root package name */
    aH f1718f;

    /* renamed from: g, reason: collision with root package name */
    double f1719g;

    public C0166w(ax.ab abVar, ax.ab abVar2) {
        this.f1715c = -1;
        this.f1716d = null;
        this.f1717e = -1;
        this.f1718f = null;
        this.f1719g = Double.NaN;
        this.f1716d = abVar;
        this.f1714b = abVar2;
    }

    public C0166w(aI aIVar, ax.ab abVar) {
        this.f1715c = -1;
        this.f1716d = null;
        this.f1717e = -1;
        this.f1718f = null;
        this.f1719g = Double.NaN;
        this.f1713a = aIVar;
        this.f1714b = abVar;
    }

    public double a(ax.S s2) throws ax.U {
        double dB = this.f1716d != null ? this.f1716d.b(s2) : -1.0d;
        if (this.f1716d != null && this.f1717e != dB) {
            String[] strArrD = G.T.a().d();
            int length = strArrD.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                G.R rC = G.T.a().c(strArrD[i2]);
                if (rC.O().x() == dB) {
                    this.f1713a = rC;
                    this.f1717e = (int) dB;
                    break;
                }
                i2++;
            }
            this.f1715c = -1;
        }
        if (this.f1713a == null) {
            return Double.NaN;
        }
        int iB = (int) this.f1714b.b(s2);
        if (this.f1715c == -1 || iB != this.f1715c || this.f1718f == null) {
            Iterator itQ = this.f1713a.K().q();
            while (true) {
                if (!itQ.hasNext()) {
                    break;
                }
                aH aHVar = (aH) itQ.next();
                if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR) && aHVar.a() == iB) {
                    this.f1718f = aHVar;
                    this.f1715c = iB;
                    break;
                }
            }
            if (this.f1718f == null && iB >= 0) {
                throw new ax.U("No OutputChannel found for offset: " + iB);
            }
        }
        if (this.f1718f != null) {
            return this.f1718f.h();
        }
        return 1.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "getChannelScaleByOffset( " + this.f1714b.toString() + " )";
    }
}
