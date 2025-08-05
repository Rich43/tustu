package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: G.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/d.class */
public class C0121d implements cZ, Serializable {

    /* renamed from: a, reason: collision with root package name */
    aM f1174a;

    /* renamed from: b, reason: collision with root package name */
    String f1175b;

    /* renamed from: c, reason: collision with root package name */
    cX f1176c;

    /* renamed from: d, reason: collision with root package name */
    String f1177d;

    /* renamed from: f, reason: collision with root package name */
    private String f1178f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f1179g = null;

    /* renamed from: e, reason: collision with root package name */
    StringBuffer f1180e = new StringBuffer();

    public C0121d(cX cXVar, String str, String str2) {
        this.f1176c = null;
        this.f1177d = null;
        this.f1176c = cXVar;
        this.f1177d = str;
        this.f1175b = str2;
    }

    @Override // G.cZ
    public String a() {
        aI aIVarA = C0125h.a().a(this.f1176c.a());
        if (aIVarA == null) {
            return "";
        }
        this.f1174a = aIVarA.c(this.f1177d);
        if (this.f1174a == null) {
            return "Invalid bitParameter";
        }
        try {
            if (this.f1174a == null) {
                return "Invalid bitParameter";
            }
            int iRound = (int) Math.round(C0126i.a(this.f1175b, aIVarA));
            ArrayList arrayListX = this.f1174a.x();
            if (iRound < 0 || iRound >= arrayListX.size()) {
                return "";
            }
            this.f1180e.setLength(0);
            if (this.f1178f != null && !this.f1178f.isEmpty()) {
                this.f1180e.append(this.f1178f);
            }
            this.f1180e.append(bH.W.i(((String) this.f1174a.x().get(iRound)).toString()));
            if (this.f1179g != null && !this.f1179g.isEmpty()) {
                this.f1180e.append(this.f1179g);
            }
            return this.f1180e.toString();
        } catch (ax.U e2) {
            throw new V.g("bit class value provider error! Failed to evaluate:\n" + this.f1175b + "Error: " + e2.getMessage());
        }
    }

    @Override // G.cZ
    public String[] b() {
        aI aIVarA = C0125h.a().a(this.f1176c.a());
        if (aIVarA == null) {
            return null;
        }
        try {
            return C0126i.f(this.f1175b, aIVarA);
        } catch (ax.U e2) {
            Logger.getLogger(C0121d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    public String toString() {
        this.f1180e.setLength(0);
        if (this.f1178f != null && !this.f1178f.isEmpty()) {
            this.f1180e.append(this.f1178f);
        }
        this.f1180e.append("bitStringValue( " + (this.f1174a != null ? this.f1174a.aJ() : this.f1177d) + " ,  " + this.f1175b + " )");
        if (this.f1179g != null && !this.f1179g.isEmpty()) {
            this.f1180e.append(this.f1179g);
        }
        return this.f1180e.toString();
    }

    public void a(String str) {
        this.f1178f = str;
    }

    public void b(String str) {
        this.f1179g = str;
    }
}
