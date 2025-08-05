package ak;

import java.io.IOException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* renamed from: ak.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/u.class */
public class C0561u extends C0546f {
    public C0561u() {
        super(",", false);
        this.f4837t = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:92:0x03f5, code lost:
    
        a(true);
     */
    @Override // ak.C0546f, W.V
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Iterator b() throws V.a {
        /*
            Method dump skipped, instructions count: 1150
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ak.C0561u.b():java.util.Iterator");
    }

    @Override // ak.C0546f
    protected String l() throws V.f {
        if (p()) {
            a(false);
            return this.f4831o;
        }
        this.f4831o = this.f4830n;
        this.f4830n = this.f4821e.a();
        if (this.f4830n != null && this.f4830n.equals("")) {
            this.f4830n = this.f4821e.a();
        }
        this.f4832p++;
        if (this.f4831o == null) {
            throw new V.f("No records available.");
        }
        if (this.f4823g.size() > 0 && this.f4831o.trim().equals("")) {
            this.f4831o = "MARK Corrupt file blank record";
            System.out.println("Found a bad row");
        }
        if (this.f4831o.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            this.f4831o = bH.W.b(this.f4831o, PdfOps.DOUBLE_QUOTE__TOKEN, "");
        }
        return this.f4831o;
    }

    @Override // ak.C0546f, W.V
    public float[] c() throws V.a {
        String strB;
        try {
            if (this.f4837t && this.f4832p >= 2000 && k()) {
                throw new V.a("This Edition is limited to loading 2000 rows of data. \nPlease Register to load large log files.");
            }
            String strL = l();
            if (strL.startsWith(this.f4822f)) {
                String str = "0";
                if (this.f4825i != null && this.f4825i.length > 0) {
                    str = this.f4825i[0] + "";
                }
                strL = str + strL;
            }
            if (this.f4829m == -1) {
                this.f4829m = this.f4824h.length() / (strL.length() + 3);
            }
            String strD = d(strL);
            if (strD.endsWith(this.f4822f)) {
                strD = strD + " ";
            }
            if (strD.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                strD = strD.substring(1);
            }
            if (strD.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                strD = strD.substring(0, strD.length() - 1);
            }
            aD aDVar = new aD(strD, this.f4822f);
            this.f4825i = new float[aDVar.c()];
            for (int i2 = 0; i2 < this.f4825i.length; i2++) {
                try {
                    strB = aDVar.b().trim();
                } catch (Exception e2) {
                    bH.C.c("Error Parsing record:\n" + strD);
                    strB = "0";
                    e2.printStackTrace();
                }
                if (strB.startsWith("S ")) {
                    strB = bH.W.b(strB, "S ", LanguageTag.SEP);
                } else if (strB.startsWith("E ")) {
                    strB = bH.W.b(strB, "E ", "");
                } else if (strB.startsWith("N ")) {
                    strB = bH.W.b(strB, "N ", "");
                } else if (strB.startsWith("W ")) {
                    strB = bH.W.b(strB, "W ", LanguageTag.SEP);
                }
                if (strB.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) != -1) {
                    this.f4825i[i2] = g(strB);
                } else if (strB.startsWith("0x")) {
                    try {
                        this.f4825i[i2] = Integer.parseInt(strB.substring(2), 16);
                    } catch (NumberFormatException e3) {
                        this.f4825i[i2] = 0.0f;
                    }
                } else if (strB.equals("Off") || strB.equals("Inactive") || strB.equals("Low") || strB.equals("Failure") || strB.equals("No") || strB.equals("Too Low")) {
                    this.f4825i[i2] = 0.0f;
                } else if (strB.equals("On") || strB.equals("Active") || strB.equals("High") || strB.equals("Ok") || strB.equals("Yes") || strB.equals("Too High")) {
                    this.f4825i[i2] = 1.0f;
                } else if (strB.equals("")) {
                    this.f4825i[i2] = Float.NaN;
                } else {
                    try {
                        this.f4825i[i2] = Float.parseFloat(bH.W.b(strB, ",", "."));
                    } catch (NumberFormatException e4) {
                        this.f4825i[i2] = Float.NaN;
                    }
                }
            }
            return this.f4825i;
        } catch (IOException e5) {
            e5.printStackTrace();
            throw new V.a("IO Error reading row from file on row " + this.f4832p + ".");
        }
    }

    @Override // ak.C0546f
    protected boolean c(String str, String str2) {
        try {
            if (str.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                str = str.substring(1);
            }
            Double.parseDouble(str.substring(0, str.indexOf(PdfOps.DOUBLE_QUOTE__TOKEN)));
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f1975q;
    }
}
