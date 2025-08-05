package G;

import java.io.Serializable;
import java.util.HashMap;

/* loaded from: TunerStudioMS.jar:G/bQ.class */
public class bQ implements dh, Serializable {

    /* renamed from: b, reason: collision with root package name */
    private String f862b;

    /* renamed from: a, reason: collision with root package name */
    cX f863a;

    /* renamed from: c, reason: collision with root package name */
    private HashMap f864c = null;

    public bQ(aI aIVar, String str) {
        this.f862b = null;
        this.f863a = null;
        this.f863a = new bR(this, aIVar.c());
        this.f862b = str;
    }

    public bQ(cX cXVar, String str) {
        this.f862b = null;
        this.f863a = null;
        this.f863a = cXVar;
        this.f862b = str;
        if (str.equals("fuelLoadRes")) {
            bH.C.c("break 1209873");
        }
    }

    @Override // G.dh
    public double a() {
        R rC = T.a().c(this.f863a.a());
        if (rC == null || this.f862b == null) {
            return Double.NaN;
        }
        try {
            return C0126i.a((aI) rC, this.f862b);
        } catch (V.g e2) {
            e();
            return Double.NaN;
        }
    }

    private void e() {
        R rC = T.a().c(this.f863a.a());
        if (rC != null) {
            try {
                for (String str : C0126i.f(this.f862b, rC)) {
                    try {
                        C0126i.a((aI) rC, str);
                    } catch (V.g e2) {
                        bH.C.b("Disabling expression evaluation, Variable not found '" + str + "' in expression: " + this.f862b);
                        this.f862b = null;
                    }
                }
            } catch (ax.U e3) {
                bH.C.b("Invalid expression: " + this.f862b);
                this.f862b = null;
            }
        }
    }

    public String[] b() {
        R rC = T.a().c(this.f863a.a());
        return rC == null ? new String[0] : C0126i.h(this.f862b, rC);
    }

    public String c() {
        return this.f862b;
    }

    public String toString() {
        return this.f862b;
    }

    @Override // G.dh
    public double a(int i2) {
        R rC = T.a().c(this.f863a.a());
        if (rC == null) {
            return Double.NaN;
        }
        String strB = this.f862b;
        if (this.f864c != null || strB.contains("%INDEX%")) {
            strB = (String) d().get(Integer.valueOf(i2));
            if (strB == null) {
                strB = bH.W.b(this.f862b, "%INDEX%", Integer.toString(i2));
                d().put(Integer.valueOf(i2), strB);
            }
        }
        try {
            return C0126i.a((aI) rC, strB);
        } catch (V.g e2) {
            bH.C.a("Unable to resolve Expression:" + strB + "\n" + e2.getMessage());
            return Double.NaN;
        }
    }

    public HashMap d() {
        if (this.f864c == null) {
            this.f864c = new HashMap();
        }
        return this.f864c;
    }
}
