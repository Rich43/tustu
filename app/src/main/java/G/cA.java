package G;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:G/cA.class */
public class cA implements cZ, Serializable {

    /* renamed from: a, reason: collision with root package name */
    aM f1029a;

    /* renamed from: b, reason: collision with root package name */
    String[] f1030b;

    /* renamed from: e, reason: collision with root package name */
    private String f1031e;

    /* renamed from: f, reason: collision with root package name */
    private String f1032f;

    /* renamed from: g, reason: collision with root package name */
    private int f1033g;

    /* renamed from: c, reason: collision with root package name */
    cX f1034c;

    /* renamed from: d, reason: collision with root package name */
    String f1035d;

    public cA(aI aIVar, aM aMVar) {
        this.f1029a = null;
        this.f1030b = null;
        this.f1031e = "";
        this.f1032f = "";
        this.f1033g = -1;
        this.f1034c = null;
        this.f1035d = null;
        this.f1034c = new cB(this, aIVar.c());
        this.f1029a = aMVar;
        if (aMVar != null) {
            this.f1035d = aMVar.aJ();
        }
    }

    public cA(cX cXVar, String str) {
        this.f1029a = null;
        this.f1030b = null;
        this.f1031e = "";
        this.f1032f = "";
        this.f1033g = -1;
        this.f1034c = null;
        this.f1035d = null;
        this.f1034c = cXVar;
        this.f1035d = str;
    }

    @Override // G.cZ
    public String a() throws V.g {
        R rC = T.a().c(this.f1034c.a());
        if (rC == null) {
            return "";
        }
        if (this.f1029a == null) {
            this.f1029a = rC.c(this.f1035d);
            if (this.f1029a == null) {
                return this.f1035d;
            }
        }
        try {
            if (this.f1033g >= 0) {
                return this.f1031e + this.f1029a.c(rC.h())[this.f1033g] + this.f1032f;
            }
            String strD = this.f1029a.d(rC.h());
            if (this.f1029a.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                strD = bH.W.i(strD);
            }
            return this.f1031e + strD + this.f1032f;
        } catch (Exception e2) {
            throw new V.g("Failed to get String value for: " + (this.f1029a != null ? this.f1029a.aJ() : "NULL") + "\nError: " + e2.getMessage());
        }
    }

    @Override // G.cZ
    public String[] b() {
        return c();
    }

    private String[] c() {
        if (this.f1030b == null) {
            this.f1030b = new String[1];
            this.f1030b[0] = this.f1029a.aJ();
        }
        return this.f1030b;
    }

    public void a(String str) {
        this.f1031e = str;
    }

    public void b(String str) {
        this.f1032f = str;
    }

    public void a(int i2) {
        this.f1033g = i2;
    }

    public String toString() {
        return this.f1033g >= 0 ? this.f1031e + "$stringValue( " + this.f1035d + "[" + this.f1033g + "] )" + this.f1032f : this.f1031e + "$stringValue( " + this.f1035d + " )" + this.f1032f;
    }
}
