package O;

import G.T;
import G.bS;
import G.cV;

/* loaded from: TunerStudioMS.jar:O/h.class */
public class h implements cV {
    @Override // G.cV
    public boolean a(bS bSVar) {
        if (bSVar == null) {
            return false;
        }
        String strB = bSVar.b();
        if (strB.equals("\u0014") || strB.equals("\n") || strB.equals("20") || strB.equals("10")) {
            return true;
        }
        if (!strB.startsWith("MSII") && !strB.startsWith("MS2Extra") && !strB.startsWith("MS4PPC Rev") && !strB.startsWith("MSPNP Gen") && !strB.startsWith("P&H Lite V") && !strB.startsWith("MS3") && !strB.startsWith("MS Ext") && !strB.startsWith("MS1/Extra") && !strB.startsWith("Trans") && !strB.startsWith("Picosquirt") && !strB.startsWith("MSnS-extra") && !strB.startsWith("JSMgauge") && !strB.startsWith("Mega") && strB.toLowerCase().indexOf("gpio") == -1 && strB.indexOf("JimStim") == -1 && !strB.startsWith("SEQ Rev") && !strB.startsWith("LCT Rev") && !strB.toLowerCase().startsWith("speeduino") && !strB.startsWith("MShift") && !strB.startsWith("IOExte") && !strB.startsWith("BT Tester") && !strB.startsWith("PWB") && !strB.startsWith("rusEFI ") && !strB.startsWith("DBWX2") && !strB.startsWith("MS/Extra") && !strB.startsWith("DD-EFI Pro") && !strB.startsWith("CoreEFI_V")) {
            return false;
        }
        if (T.a().c() == null) {
            return true;
        }
        return T.a().c().C().H();
    }
}
