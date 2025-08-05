package G;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: G.cy, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cy.class */
class C0119cy implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    bZ f1172a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0117cw f1173b;

    C0119cy(C0117cw c0117cw, bZ bZVar) {
        this.f1173b = c0117cw;
        this.f1172a = bZVar;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        try {
            if (this.f1173b.f1166b.R()) {
                this.f1172a.a(this.f1173b.f1166b.h(), d2);
                if (this.f1172a.i().equals(bZ.f883d)) {
                    this.f1173b.a(this);
                }
            }
        } catch (V.g e2) {
            Logger.getLogger(C0117cw.class.getName()).log(Level.SEVERE, "Invalid PcVariable Use for " + this.f1172a.aJ(), (Throwable) e2);
        } catch (V.j e3) {
            Logger.getLogger(C0117cw.class.getName()).log(Level.SEVERE, "Received Invalid OutputChannel value for " + this.f1172a.aJ(), (Throwable) e3);
        }
    }
}
