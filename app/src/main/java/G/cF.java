package G;

/* loaded from: TunerStudioMS.jar:G/cF.class */
public class cF implements InterfaceC0131n {
    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        R r2 = (R) c0132o.b().v().E();
        if (c0132o.a() == 1) {
            for (String str : c0132o.c().split("~")) {
                int iIndexOf = str.indexOf("=");
                if (iIndexOf >= 0) {
                    String strSubstring = str.substring(0, iIndexOf);
                    String strSubstring2 = str.substring(iIndexOf + 1);
                    aM aMVarC = r2.c(strSubstring);
                    if (aMVarC != null) {
                        try {
                            cC.a(r2, aMVarC, strSubstring2);
                        } catch (V.i e2) {
                            bH.C.a("Failed to set PcVariiable Value: " + str + ", Err:" + e2.getLocalizedMessage());
                        }
                    }
                } else {
                    bH.C.b("Invalid PcVariable line: " + str);
                }
            }
        }
    }
}
