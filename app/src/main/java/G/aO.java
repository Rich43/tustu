package G;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/aO.class */
public class aO implements aN, InterfaceC0109co {

    /* renamed from: b, reason: collision with root package name */
    private aM f626b;

    /* renamed from: c, reason: collision with root package name */
    private R f627c;

    /* renamed from: d, reason: collision with root package name */
    private String f628d;

    /* renamed from: e, reason: collision with root package name */
    private aP f629e = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f630a = new HashMap();

    public aO(aM aMVar, R r2, String str) {
        this.f627c = r2;
        this.f626b = aMVar;
        this.f628d = str;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (this.f629e == null) {
            this.f629e = new aP(this);
            this.f629e.start();
        }
    }

    public String a() {
        return this.f628d;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) throws NumberFormatException {
        Double d3 = (Double) this.f630a.get(str);
        if (d3 == null || d3.doubleValue() != d2) {
            try {
                this.f626b.a(this.f627c.h(), C0126i.a(this.f628d, (aI) this.f627c));
                this.f630a.put(str, Double.valueOf(d2));
            } catch (V.g e2) {
                Logger.getLogger(aO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (V.j e3) {
                bH.C.c("unable to evaluate expression: " + this.f628d + ", Error: " + e3.getLocalizedMessage());
            } catch (ax.U e4) {
                bH.C.c("unable to evaluate expression: " + this.f628d + ", Error: " + e4.getLocalizedMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() throws NumberFormatException {
        try {
            this.f626b.a(this.f627c.h(), C0126i.a(this.f628d, (aI) this.f627c));
        } catch (V.g e2) {
            Logger.getLogger(aO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (V.j e3) {
            bH.C.c("unable to evaluate expression: " + this.f628d + ", Error: " + e3.getLocalizedMessage());
        } catch (ax.U e4) {
            bH.C.c("unable to evaluate expression: " + this.f628d + ", Error: " + e4.getLocalizedMessage());
        }
    }
}
