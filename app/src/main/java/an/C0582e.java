package an;

import am.C0574b;
import am.C0576d;
import am.k;
import bH.C;
import bH.C0995c;
import java.util.ArrayList;

/* renamed from: an.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:an/e.class */
public class C0582e extends AbstractC0578a {
    C0582e(C0576d c0576d, C0574b c0574b) {
        super(c0576d, c0574b);
        k[] kVarArrI = c0574b.i();
        if (kVarArrI != null) {
            ArrayList arrayList = new ArrayList();
            for (k kVar : kVarArrI) {
                if (kVar != null) {
                    arrayList.add(kVar.e());
                }
            }
            if (arrayList.isEmpty()) {
                return;
            }
            a(arrayList);
        }
    }

    @Override // an.AbstractC0578a
    public double a(byte[] bArr) {
        if (this.f5048a.h() > 32) {
            return C0995c.a(C0995c.b(bArr, (int) this.f5048a.g(), 2, this.f5050c, true), this.f5048a.f(), (int) ((this.f5048a.f() + this.f5048a.h()) - 1));
        }
        if (this.f5048a.h() <= 8) {
            return C0995c.a((int) bArr[(int) this.f5048a.g()], (int) this.f5048a.f(), (int) (this.f5048a.f() + this.f5048a.h()));
        }
        try {
            return C0995c.a(C0995c.a(bArr, (int) this.f5048a.g(), 2, this.f5050c, true), (int) this.f5048a.f(), (int) ((this.f5048a.f() + this.f5048a.h()) - 1));
        } catch (ArrayIndexOutOfBoundsException e2) {
            C.c("Boom 2579698243");
            return Double.NaN;
        }
    }
}
