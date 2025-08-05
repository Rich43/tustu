package L;

import G.aI;
import G.aM;
import com.efiAnalytics.plugin.ecu.ControllerParameter;

/* loaded from: TunerStudioMS.jar:L/U.class */
public class U extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    aI f1591a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1592b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1593c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1594d;

    /* renamed from: e, reason: collision with root package name */
    aM f1595e = null;

    /* renamed from: f, reason: collision with root package name */
    aM f1596f = null;

    public U(aI aIVar, ax.ab abVar, ax.ab abVar2, ax.ab abVar3) {
        this.f1591a = aIVar;
        this.f1592b = abVar;
        this.f1593c = abVar2;
        this.f1594d = abVar3;
    }

    public double a(ax.S s2) throws ax.U {
        c(s2);
        try {
            return a(this.f1595e.i(this.f1591a.h()), b(this.f1596f.i(this.f1591a.h()), this.f1594d.b(s2)));
        } catch (V.g e2) {
            throw new ax.U("Unable to evaluate tableLookup: " + e2.getLocalizedMessage());
        }
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    private void c(ax.S s2) throws ax.U {
        if (this.f1595e == null) {
            this.f1595e = C0155l.a().a((int) this.f1592b.b(s2));
            if (this.f1595e == null) {
                throw new ax.U("Array Parameter not found!");
            }
            if (!this.f1595e.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                throw new ax.U("Array Parameter must be of type array!");
            }
            if (this.f1595e.m() != 1) {
                throw new ax.U("Array Parameter must be 1D array!");
            }
        }
        if (this.f1596f == null) {
            this.f1596f = C0155l.a().a((int) this.f1593c.b(s2));
            if (this.f1596f == null) {
                throw new ax.U("Array Parameter not found!");
            }
            if (!this.f1596f.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                throw new ax.U("Array Parameter must be of type array!");
            }
            if (this.f1596f.m() != 1) {
                throw new ax.U("Array Parameter must be 1D array!");
            }
        }
    }

    public double a(double[][] dArr, double d2) {
        int i2 = (int) d2;
        return dArr[i2][0] + ((d2 - i2) * (dArr[(int) Math.ceil(d2)][0] - dArr[i2][0]));
    }

    public static double b(double[][] dArr, double d2) {
        double d3 = 0.0d;
        double length = dArr.length - 1;
        if (!(dArr[0][0] <= dArr[dArr.length - 1][0])) {
            length = 0.0d;
            double d4 = dArr[dArr.length - 1][0];
            int length2 = dArr.length - 1;
            while (true) {
                if (length2 < 0) {
                    break;
                }
                double d5 = dArr[length2][0];
                if (d5 == d2) {
                    length = length2;
                    break;
                }
                if (d5 > d2) {
                    length = (length2 != dArr.length || d2 > d5) ? length2 + ((d4 - d2) / (d5 - d4)) : dArr.length;
                } else {
                    d4 = d5;
                    length2--;
                }
            }
        } else {
            int i2 = 0;
            while (true) {
                if (i2 >= dArr.length) {
                    break;
                }
                double d6 = dArr[i2][0];
                if (d6 == d2) {
                    length = i2;
                    break;
                }
                if (d6 > d2) {
                    length = (i2 != 0 || d2 > d6) ? (i2 - 1.0d) + ((d2 - d3) / (d6 - d3)) : 0.0d;
                } else {
                    d3 = d6;
                    i2++;
                }
            }
        }
        return length;
    }

    public String toString() {
        return "tableLookup(" + this.f1592b.toString() + ", " + this.f1593c.toString() + ", " + this.f1594d.toString() + " )";
    }
}
