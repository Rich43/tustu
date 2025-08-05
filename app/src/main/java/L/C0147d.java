package L;

import G.aI;
import G.aM;
import com.efiAnalytics.plugin.ecu.ControllerParameter;

/* renamed from: L.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/d.class */
public class C0147d extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1651a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1652b;

    /* renamed from: c, reason: collision with root package name */
    aM f1653c = null;

    /* renamed from: d, reason: collision with root package name */
    aI f1654d;

    public C0147d(aI aIVar, ax.ab abVar, ax.ab abVar2) {
        this.f1651a = null;
        this.f1651a = abVar2;
        this.f1652b = abVar;
        this.f1654d = aIVar;
    }

    public double a(ax.S s2) throws ax.U {
        c(s2);
        try {
            return a(this.f1653c.i(this.f1654d.h()), this.f1651a.b(s2));
        } catch (V.g e2) {
            throw new ax.U("Unable to evaluate tableLookup: " + e2.getLocalizedMessage());
        }
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    private void c(ax.S s2) throws ax.U {
        if (this.f1653c == null) {
            this.f1653c = C0155l.a().a((int) this.f1652b.b(s2));
            if (this.f1653c == null) {
                throw new ax.U("Array Parameter not found!");
            }
            if (!this.f1653c.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                throw new ax.U("Array Parameter must be of type array!");
            }
            if (this.f1653c.m() != 1) {
                throw new ax.U("Array Parameter must be 1D array!");
            }
        }
    }

    public double a(double[][] dArr, double d2) {
        if (d2 > dArr.length - 1) {
            d2 = dArr.length - 1;
        } else if (d2 < 0.0d) {
            d2 = 0.0d;
        }
        int i2 = (int) d2;
        return dArr[i2][0] + ((d2 - i2) * (dArr[(int) Math.ceil(d2)][0] - dArr[i2][0]));
    }

    public String toString() {
        return "arrayLookup( " + this.f1652b.toString() + " ," + this.f1651a.toString() + " )";
    }
}
