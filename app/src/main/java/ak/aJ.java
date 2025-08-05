package ak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ak/aJ.class */
public class aJ extends C0546f {
    public aJ() {
        super(",", false);
        this.f4837t = true;
    }

    @Override // ak.C0546f
    protected int b(String str) {
        return 0;
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        while (this.f4823g.isEmpty()) {
            try {
                String strL = l();
                int i2 = 0;
                String[] strArrR = r();
                String[] strArrN = n();
                String[] strArrS = s();
                boolean z2 = strArrN == null || strArrN.length == 0;
                int i3 = 0;
                if (!z2) {
                    int length = strArrN.length;
                    int i4 = 0;
                    while (true) {
                        if (i4 >= length) {
                            break;
                        }
                        if (!strArrN[i4].isEmpty()) {
                            i3++;
                            if (i3 > 5) {
                                z2 = false;
                                break;
                            }
                        }
                        i4++;
                    }
                }
                for (String str : strL.split(Pattern.quote(this.f4822f))) {
                    C0543c c0543c = new C0543c();
                    String strTrim = str.trim();
                    if (strTrim.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || strTrim.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                        strTrim = bH.W.b(strTrim, PdfOps.DOUBLE_QUOTE__TOKEN, "").trim();
                    }
                    if (strTrim.isEmpty()) {
                        strTrim = "Col" + i2;
                    }
                    if (strTrim.equalsIgnoreCase("lambda")) {
                        strTrim = "Lambda";
                    }
                    if (strTrim.contains(LanguageTag.SEP)) {
                        strTrim = bH.W.b(strTrim, LanguageTag.SEP, " ");
                    }
                    if (strTrim.contains(Constants.INDENT)) {
                        strTrim = bH.W.b(strTrim, Constants.INDENT, " ");
                    }
                    if (strTrim.equals("Time")) {
                        c0543c.a(3);
                        c0543c.a("Time");
                        c0543c.b(PdfOps.s_TOKEN);
                        strTrim = "Time";
                        c0543c.a(0.001f);
                    }
                    if (strArrR != null) {
                        try {
                            if (strArrR.length > i2) {
                                c0543c.c(strArrR[i2]);
                            }
                        } catch (Exception e2) {
                            bH.C.c("Thought I could parse description row, but it failed on field \"" + strTrim + PdfOps.DOUBLE_QUOTE__TOKEN);
                        }
                    }
                    if (strArrS.length > i2 && (strArrS[i2].equalsIgnoreCase("TRUE") || strArrS[i2].equalsIgnoreCase("FALSE"))) {
                        c0543c.b(8);
                    }
                    if (!z2) {
                        try {
                            if (strArrN.length > i2) {
                                String str2 = strArrN[i2];
                                c0543c.b(str2);
                                if (str2.equals("On/Off")) {
                                    c0543c.b(4);
                                } else if (str2.equals("High/Low")) {
                                    c0543c.b(6);
                                } else if (str2.equals("Active/Inactive") || str2.equals("Act/Inact")) {
                                    c0543c.b(7);
                                } else if (str2.equals("Yes/No")) {
                                    c0543c.b(5);
                                } else if (str2.equals("True/False")) {
                                    c0543c.b(8);
                                }
                            }
                        } catch (Exception e3) {
                            bH.C.c("Thought I could parse units, but it failed on field \"" + strTrim + PdfOps.DOUBLE_QUOTE__TOKEN);
                        }
                    }
                    while (strTrim.endsWith("*")) {
                        strTrim = strTrim.substring(0, strTrim.length() - 1);
                    }
                    String strTrim2 = strTrim.trim();
                    String str3 = strTrim2;
                    for (int i5 = 0; i5 < 100 && i(str3); i5++) {
                        str3 = strTrim2 + i5;
                    }
                    c0543c.a(str3);
                    C0543c c0543cA = a(c0543c);
                    if (c0543cA != null) {
                        this.f4823g.add(c0543cA);
                        i2++;
                    }
                }
            } catch (V.a e4) {
                e4.printStackTrace();
                throw new V.a("No Valid Data found in file");
            } catch (IOException e5) {
                e5.printStackTrace();
                throw new V.a("IO Error reading header rows from file.");
            }
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList.iterator();
    }

    private String[] r() {
        String[] strArrSplit = null;
        String strL = null;
        try {
            strL = l();
            strArrSplit = strL.split(Pattern.quote(this.f4822f));
        } catch (V.f e2) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (IOException e3) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        } catch (Exception e4) {
            bH.C.a("Failed to get units from this row:\n" + strL);
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        return strArrSplit;
    }

    @Override // ak.C0546f
    protected String[] n() {
        String[] strArrSplit = null;
        String strL = null;
        try {
            int i2 = 0;
            strL = l();
            strArrSplit = strL.split(Pattern.quote(this.f4822f));
            for (String str : strArrSplit) {
                String strTrim = bH.W.b(str, PdfOps.DOUBLE_QUOTE__TOKEN, "").trim();
                if (strTrim.isEmpty()) {
                    strArrSplit[i2] = strTrim;
                    i2++;
                } else {
                    if (bH.H.a(strTrim) || !Float.isNaN(g(strTrim))) {
                        a(true);
                        c();
                        a(true);
                        return null;
                    }
                    strArrSplit[i2] = strTrim;
                    i2++;
                }
            }
        } catch (V.f e2) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (IOException e3) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        } catch (Exception e4) {
            bH.C.a("Failed to get units from this row:\n" + strL);
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        return strArrSplit;
    }

    private String[] s() {
        String[] strArrSplit = null;
        String strL = null;
        try {
            strL = l();
            a(true);
            strArrSplit = strL.split(Pattern.quote(this.f4822f));
        } catch (V.f e2) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (IOException e3) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        } catch (Exception e4) {
            bH.C.a("Failed to get val from this row:\n" + strL);
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        return strArrSplit;
    }
}
