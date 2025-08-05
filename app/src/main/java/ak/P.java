package ak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ak/P.class */
public class P extends R {
    public P() {
        super(",", false);
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        while (this.f4823g.isEmpty()) {
            try {
                String strL = l();
                int i2 = 0;
                String[] strArrN = n();
                int i3 = 0;
                if (!(strArrN == null || strArrN.length == 0)) {
                    for (String str : strArrN) {
                        if (!str.isEmpty()) {
                            i3++;
                            if (i3 > 5) {
                                break;
                            }
                        }
                    }
                }
                ArrayList arrayList = new ArrayList();
                if (strL.startsWith("Date,Time,")) {
                    String strSubstring = strL.substring(10);
                    arrayList.add("Date");
                    arrayList.add("Time");
                    for (String str2 : strSubstring.split(Pattern.quote("\",\""))) {
                        arrayList.add(str2);
                    }
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    String str3 = (String) it.next();
                    C0543c c0543c = new C0543c();
                    String strTrim = str3.trim();
                    if (strTrim.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || strTrim.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                        strTrim = bH.W.b(strTrim, PdfOps.DOUBLE_QUOTE__TOKEN, "").trim();
                    }
                    if (strTrim.isEmpty()) {
                        strTrim = "Col" + i2;
                    }
                    if (strTrim.contains(LanguageTag.SEP)) {
                        strTrim = bH.W.b(strTrim, LanguageTag.SEP, " ");
                    }
                    while (strTrim.contains(Constants.INDENT)) {
                        strTrim = bH.W.b(strTrim, Constants.INDENT, " ");
                    }
                    if (strTrim.equals("Time")) {
                        c0543c.a(3);
                        c0543c.a("Time");
                        c0543c.b(PdfOps.s_TOKEN);
                        strTrim = "Time";
                    }
                    try {
                        if (strTrim.lastIndexOf("[") > 1 && strTrim.lastIndexOf("]") > strTrim.lastIndexOf("[")) {
                            int iLastIndexOf = strTrim.lastIndexOf("[");
                            int iIndexOf = strTrim.indexOf("]", iLastIndexOf);
                            c0543c.b(strTrim.substring(iLastIndexOf + 1, iIndexOf).trim());
                            strTrim = (strTrim.substring(0, iLastIndexOf) + strTrim.substring(iIndexOf + 1, strTrim.length())).trim();
                            this.f4837t = true;
                        } else if (strTrim.lastIndexOf("(") > 1 && strTrim.lastIndexOf(")") > strTrim.lastIndexOf("(")) {
                            int iLastIndexOf2 = strTrim.lastIndexOf("(");
                            int iIndexOf2 = strTrim.indexOf(")", iLastIndexOf2);
                            c0543c.b(strTrim.substring(iLastIndexOf2 + 1, iIndexOf2).trim());
                            strTrim = (strTrim.substring(0, iLastIndexOf2) + strTrim.substring(iIndexOf2 + 1, strTrim.length())).trim();
                        }
                    } catch (Exception e2) {
                        bH.C.c("Thought I could parse units, but it failed on field \"" + strTrim + PdfOps.DOUBLE_QUOTE__TOKEN);
                    }
                    String strB = c0543c.b();
                    if (strB != null) {
                        if (strB.equals("On/Off")) {
                            c0543c.b(4);
                        } else if (strB.equals("High/Low")) {
                            c0543c.b(6);
                        } else if (strB.equals("Active/Inactive") || strB.equals("Act/Inact")) {
                            c0543c.b(7);
                        } else if (strB.equals("Yes/No")) {
                            c0543c.b(5);
                        } else if (strB.equals("True/False")) {
                            c0543c.b(8);
                        }
                    }
                    while (strTrim.endsWith("*")) {
                        strTrim = strTrim.substring(0, strTrim.length() - 1);
                    }
                    if (strTrim.contains(CallSiteDescriptor.OPERATOR_DELIMITER)) {
                        strTrim = strTrim.substring(0, strTrim.indexOf(124));
                        this.f4837t = true;
                    }
                    String strTrim2 = strTrim.trim();
                    String str4 = strTrim2;
                    for (int i4 = 0; i4 < 100 && i(str4); i4++) {
                        str4 = strTrim2 + i4;
                    }
                    c0543c.a(str4);
                    C0543c c0543cA = a(c0543c);
                    if (c0543cA != null) {
                        this.f4823g.add(c0543cA);
                        i2++;
                    }
                }
            } catch (V.a e3) {
                e3.printStackTrace();
                throw new V.a("No Valid Data found in file");
            } catch (IOException e4) {
                e4.printStackTrace();
                throw new V.a("IO Error reading header rows from file.");
            }
        }
        this.f4835r = this.f4822f + this.f4822f;
        this.f4836s = this.f4822f + " " + this.f4822f;
        ArrayList arrayList2 = new ArrayList();
        Iterator it2 = this.f4823g.iterator();
        while (it2.hasNext()) {
            arrayList2.add(it2.next());
        }
        return arrayList2.iterator();
    }

    @Override // ak.C0546f
    protected String l() throws V.f {
        if (p()) {
            a(false);
            return this.f4831o;
        }
        this.f4831o = this.f4830n;
        do {
            this.f4830n = this.f4821e.a();
            if (this.f4830n == null || !this.f4830n.isEmpty()) {
                break;
            }
        } while (this.f4830n.indexOf(this.f4822f) == -1);
        this.f4832p++;
        if (this.f4831o == null) {
            throw new V.f("No records available.");
        }
        if (this.f4830n != null && this.f4830n.startsWith("Date,Time")) {
            String str = this.f4831o;
            q();
            this.f4830n = null;
            this.f4831o = str;
        }
        if (this.f4823g.size() > 0 && this.f4831o.trim().equals("")) {
            this.f4831o = "MARK Corrupt file blank record";
            System.out.println("Found a bad row");
        }
        return this.f4831o;
    }

    @Override // ak.C0546f
    protected int b(String str) {
        return 0;
    }
}
