package K;

import G.C0088bu;
import G.R;
import G.aH;
import G.aM;
import G.aS;
import G.bT;
import V.j;
import bH.C;
import bH.C0998f;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:K/e.class */
public class e implements bT {

    /* renamed from: a, reason: collision with root package name */
    C0998f f1501a = new C0998f();

    /* renamed from: b, reason: collision with root package name */
    R f1502b;

    public e(R r2) {
        this.f1502b = r2;
    }

    @Override // G.bT
    public void a() {
    }

    @Override // G.bT
    public void a(boolean z2) {
        if (z2) {
            a(this.f1502b);
        }
    }

    public void a(R r2) {
        Iterator itC = r2.e().c();
        while (itC.hasNext()) {
            C0088bu c0088bu = (C0088bu) itC.next();
            if (c0088bu instanceof aS) {
                try {
                    a(r2, (aS) c0088bu);
                } catch (V.g e2) {
                    Logger.getLogger(e.class.getName()).log(Level.WARNING, "Error trying to validate PortEditor addressing.", (Throwable) e2);
                }
            }
        }
    }

    private void a(R r2, aS aSVar) throws V.g {
        String strR = aSVar.r();
        if (strR == null || strR.isEmpty()) {
            return;
        }
        boolean z2 = false;
        boolean z3 = a(r2, aSVar.m());
        if (a(r2, aSVar.h())) {
            z3 = true;
        }
        aM aMVarC = r2.c(strR);
        if (aMVarC == null) {
            C.b("PortEditor: outputName parameter not found in configuration. " + strR);
            return;
        }
        aM aMVarC2 = r2.c(aSVar.g());
        if (!aMVarC2.c().equals(aMVarC.c())) {
            C.b("PortEditor outputOffset and outputName should be the same size! Cannot validate offsets.");
            return;
        }
        aM aMVarC3 = r2.c(aSVar.t());
        aM aMVarC4 = r2.c(aSVar.u());
        double[][] dArrI = r2.c(aSVar.d()).i(r2.h());
        boolean zEquals = r2.O().al().equals("XCP");
        double[][] dArrI2 = aMVarC2.i(r2.h());
        double[][] dArrI3 = aMVarC.i(r2.h());
        double[][] dArrI4 = aMVarC3 != null ? aMVarC3.i(r2.h()) : (double[][]) null;
        double[][] dArrI5 = aMVarC3 != null ? aMVarC4.i(r2.h()) : (double[][]) null;
        for (int i2 = 0; i2 < dArrI2[0].length; i2++) {
            if (dArrI[i2][0] != 0.0d) {
                for (int i3 = 0; i3 < dArrI2.length; i3++) {
                    double d2 = dArrI2[i3][i2];
                    double d3 = dArrI3[i3][i2];
                    aH aHVarA = a(r2, (int) d2, aMVarC2, zEquals);
                    if (d3 > 0.0d) {
                        boolean z4 = false;
                        if (aHVarA == null) {
                            C.d("PortEditor: No channel at offset/address, updating.");
                            z4 = true;
                        } else {
                            this.f1501a.a();
                            this.f1501a.a(aHVarA.aJ().getBytes());
                            if (this.f1501a.b() != d3) {
                                C.d("PortEditor offset/address does not match name crc, updating.");
                                z4 = true;
                            }
                        }
                        if (z4) {
                            aH aHVarA2 = a(r2, d3);
                            if (aHVarA2 == null) {
                                C.b("No Channel found in configuration for crc: " + d3 + ", no way to know this port is using valid conditions!");
                                dArrI3[i3][i2] = 0.0d;
                                try {
                                    aMVarC.a(r2.h(), dArrI3);
                                    r2.I();
                                } catch (j e2) {
                                    C.b("Attempted to set invalid offset values in PortEditor! Message: " + e2.getLocalizedMessage());
                                }
                            } else {
                                if (!zEquals) {
                                    dArrI2[i3][i2] = aHVarA2.a();
                                } else if (aMVarC2.e() >= 4) {
                                    dArrI2[i3][i2] = aHVarA2.x();
                                } else {
                                    dArrI2[i3][i2] = aHVarA2.x() - r2.O().af();
                                }
                                C.d("Updated PortEditor channel based on crc. condition:" + i3 + ", offsetIndex:" + i2 + " Channel assigned: " + aHVarA2.aJ());
                                z3 = true;
                            }
                        }
                    }
                }
            } else {
                if (dArrI4 != null && dArrI4[i2][0] != 0.0d) {
                    dArrI4[i2][0] = 0.0d;
                    z2 = true;
                }
                if (dArrI5 != null && dArrI5[i2][0] != 0.0d) {
                    dArrI5[i2][0] = 0.0d;
                    z2 = true;
                }
            }
        }
        if (z2) {
            if (aMVarC3 != null) {
                try {
                    aMVarC3.a(r2.h(), dArrI4);
                } catch (j e3) {
                    C.b("Attempted to set 0.0 on delay, this should be allowed. Message: " + e3.getLocalizedMessage());
                }
            }
            if (aMVarC4 != null) {
                try {
                    aMVarC4.a(r2.h(), dArrI5);
                } catch (j e4) {
                    C.b("Attempted to set 0.0 on delay, this should be allowed. Message: " + e4.getLocalizedMessage());
                }
            }
        }
        if (z3) {
            try {
                aMVarC2.a(r2.h(), dArrI2);
                r2.I();
                C.d("Updated PortEditor");
            } catch (j e5) {
                C.b("Attempted to set invalid offset values in PortEditor! Message: " + e5.getLocalizedMessage());
            }
        }
    }

    private boolean a(R r2, String str) throws V.g {
        aM aMVarC = r2.c(str);
        if (aMVarC == null) {
            C.b("Parameter not found, cannot perform specific check: " + str);
            return false;
        }
        boolean z2 = false;
        List listJ = aMVarC.j();
        if (listJ != null && !listJ.isEmpty()) {
            double[][] dArrI = aMVarC.i(r2.p());
            for (int i2 = 0; i2 < dArrI.length; i2++) {
                for (int i3 = 0; i3 < dArrI[0].length; i3++) {
                    if (!listJ.contains(Double.valueOf(dArrI[i2][i3]))) {
                        dArrI[i2][i3] = ((Double) listJ.get(0)).doubleValue();
                        z2 = true;
                    }
                }
            }
            if (z2) {
                try {
                    aMVarC.a(r2.h(), dArrI);
                } catch (j e2) {
                    C.b(e2.getMessage());
                }
            }
        }
        return z2;
    }

    private aH a(R r2, int i2, aM aMVar, boolean z2) {
        Iterator itQ = r2.K().q();
        while (itQ.hasNext()) {
            aH aHVar = (aH) itQ.next();
            if (z2) {
                long jX = (aMVar == null || aMVar.e() < 4) ? aHVar.x() - r2.O().af() : aHVar.x();
                if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR) && jX == i2) {
                    return aHVar;
                }
            } else if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR) && aHVar.a() == i2) {
                return aHVar;
            }
        }
        return null;
    }

    private aH a(R r2, double d2) {
        Iterator itQ = r2.K().q();
        while (itQ.hasNext()) {
            aH aHVar = (aH) itQ.next();
            if (aHVar.b().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                this.f1501a.a();
                this.f1501a.a(aHVar.aJ().getBytes());
                if (this.f1501a.b() == d2) {
                    return aHVar;
                }
            }
        }
        return null;
    }
}
