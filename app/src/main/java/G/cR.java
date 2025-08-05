package G;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/cR.class */
public class cR implements aG, InterfaceC0107cm {

    /* renamed from: a, reason: collision with root package name */
    R f1060a;

    /* renamed from: b, reason: collision with root package name */
    List f1061b = null;

    cR(R r2) {
        this.f1060a = r2;
        if (r2.C() != null) {
            r2.C().a(this);
        }
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        C0113cs.a().a(this.f1060a);
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        this.f1061b = null;
        this.f1060a.O().b((List) null);
    }

    @Override // G.InterfaceC0107cm
    public void a(List list) {
        double[][] dArrI;
        int i2;
        if (list != null) {
            this.f1061b = new ArrayList();
            this.f1061b.addAll(list);
        } else {
            this.f1061b = null;
        }
        aM aMVarC = this.f1060a.c(this.f1060a.O().aa());
        try {
            dArrI = aMVarC.i(this.f1060a.h());
        } catch (V.g e2) {
            Logger.getLogger(cR.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            dArrI = new double[aMVarC.b()][1];
        }
        int i3 = 0;
        if (list == null || !this.f1060a.O().X()) {
            this.f1060a.O().b((List) null);
            return;
        }
        int i4 = 0;
        while (true) {
            if (i4 < dArrI.length + i3) {
                if (i4 < list.size()) {
                    C0140w c0140w = (C0140w) list.get(i4);
                    int iA = c0140w.a();
                    switch (c0140w.c()) {
                        case 0:
                            i2 = 0;
                            break;
                        case 1:
                            i2 = 8192;
                            break;
                        case 2:
                            i2 = 16384;
                            break;
                        case 4:
                            i2 = 24576;
                            break;
                        case 8:
                            i2 = 32768;
                            break;
                        case 16:
                            i2 = 40960;
                            break;
                        case 32:
                            i2 = 49152;
                            break;
                        case 64:
                            i2 = 57344;
                            break;
                        default:
                            i2 = 0;
                            i3++;
                            bH.C.b("Unsupported data size " + c0140w.c() + " for ScatteredRuntime for offset: " + iA);
                            break;
                    }
                    if (this.f1060a.c(aMVarC.aJ()).e() > 1) {
                        dArrI[i4 - i3][0] = i2 + iA;
                    } else {
                        dArrI[i4 - i3][0] = iA + (this.f1060a.O().af() & 255);
                    }
                } else if (this.f1060a.c(aMVarC.aJ()).e() == 1) {
                    dArrI[i4 - i3][0] = 255.0d;
                } else {
                    dArrI[i4 - i3][0] = 0.0d;
                }
                i4++;
            }
        }
        if (list.size() > dArrI.length) {
            bH.C.b("Too many OutputChannels registered (" + list.size() + ") for size of: " + aMVarC.aJ() + "(" + dArrI.length + ")");
            this.f1060a.O().j(true);
        } else {
            this.f1060a.O().j(false);
        }
        try {
            this.f1060a.a(aMVarC.aJ(), dArrI);
            this.f1060a.H();
            this.f1060a.O().b(this.f1061b);
            aB.a().a(this.f1060a.c(), "Updated Quick Runtime Table");
        } catch (Exception e3) {
            Logger.getLogger(cR.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            bH.C.a("Failed to set Scatter Runtime Read values, disabling.");
            this.f1060a.O().e(new cS(this));
            this.f1060a.O().b((List) null);
        }
    }
}
