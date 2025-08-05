package L;

import G.C0113cs;
import G.InterfaceC0109co;
import G.aH;
import G.aI;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: L.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/s.class */
public class C0162s extends ax.ac implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    aI f1685a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1686b;

    /* renamed from: c, reason: collision with root package name */
    int f1687c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1688d;

    /* renamed from: e, reason: collision with root package name */
    int f1689e;

    /* renamed from: f, reason: collision with root package name */
    aH f1690f;

    /* renamed from: g, reason: collision with root package name */
    double f1691g;

    public C0162s(ax.ab abVar, ax.ab abVar2) {
        this.f1687c = -1;
        this.f1688d = null;
        this.f1689e = -1;
        this.f1690f = null;
        this.f1691g = Double.NaN;
        this.f1688d = abVar;
        this.f1686b = abVar2;
    }

    public C0162s(aI aIVar, ax.ab abVar) {
        this.f1687c = -1;
        this.f1688d = null;
        this.f1689e = -1;
        this.f1690f = null;
        this.f1691g = Double.NaN;
        this.f1685a = aIVar;
        this.f1686b = abVar;
    }

    public double a(ax.S s2) {
        double dB = this.f1688d != null ? this.f1688d.b(s2) : -1.0d;
        if (this.f1688d != null && this.f1689e != dB) {
            String[] strArrD = G.T.a().d();
            int length = strArrD.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                G.R rC = G.T.a().c(strArrD[i2]);
                if (rC.O().x() == dB) {
                    this.f1685a = rC;
                    this.f1689e = (int) dB;
                    break;
                }
                i2++;
            }
            this.f1687c = -1;
        }
        if (this.f1685a == null) {
            return Double.NaN;
        }
        int iB = (int) this.f1686b.b(s2);
        if (this.f1687c == -1 || iB != this.f1687c || this.f1690f == null) {
            C0113cs.a().a(this);
            Iterator itQ = this.f1685a.K().q();
            while (true) {
                if (!itQ.hasNext()) {
                    break;
                }
                aH aHVar = (aH) itQ.next();
                if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR) && aHVar.a() == iB) {
                    this.f1690f = aHVar;
                    this.f1687c = iB;
                    this.f1691g = this.f1690f.o();
                    try {
                        C0113cs.a().a(this.f1685a.c(), this.f1690f.aJ(), this);
                        return aHVar.o();
                    } catch (V.a e2) {
                        Logger.getLogger(C0162s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
            }
        }
        return this.f1691g;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        this.f1691g = d2;
    }

    public String toString() {
        return "getChannelByOffset( " + this.f1686b.toString() + " )";
    }
}
