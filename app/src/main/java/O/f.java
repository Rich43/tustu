package O;

import G.bS;
import G.cV;
import bH.C;

/* loaded from: TunerStudioMS.jar:O/f.class */
public class f implements cV {

    /* renamed from: a, reason: collision with root package name */
    boolean f1751a = false;

    @Override // G.cV
    public boolean a(bS bSVar) {
        if (bSVar != null && bSVar.b() != null && bSVar.b().length() < 8) {
            if (this.f1751a) {
                return false;
            }
            C.c("Other than specific legacy devices, signature len should be at least 8 characters, rejecting: " + bSVar.b());
            this.f1751a = true;
            return false;
        }
        if (bSVar != null && bSVar.b() != null && bSVar.b().length() > 40) {
            C.c("Signature too long, rejecting: " + bSVar.b());
            return false;
        }
        if (bSVar == null || bSVar.a() == null || bSVar.a().length <= 0 || bSVar.a()[0] != 0) {
            return true;
        }
        C.c("Signature starts with a null call 0, rejecting.");
        return false;
    }
}
