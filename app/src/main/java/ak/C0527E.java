package ak;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* renamed from: ak.E, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/E.class */
public class C0527E extends R {

    /* renamed from: a, reason: collision with root package name */
    boolean f4592a;

    public C0527E() {
        super(" ", true);
        this.f4592a = false;
        this.f4847C = true;
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        while (this.f4823g.isEmpty()) {
            try {
                int i2 = 0;
                for (String str : l().split(Pattern.quote(this.f4822f))) {
                    C0543c c0543c = new C0543c();
                    String strTrim = str.trim();
                    if (strTrim.isEmpty()) {
                        strTrim = "Col" + i2;
                    }
                    if (strTrim.contains(LanguageTag.SEP)) {
                        strTrim = bH.W.b(strTrim, LanguageTag.SEP, " ");
                    }
                    if (strTrim.contains(Constants.INDENT)) {
                        strTrim = bH.W.b(strTrim, Constants.INDENT, " ");
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
                    if (strTrim.startsWith("<")) {
                        strTrim = strTrim.substring(1);
                    }
                    if (strTrim.endsWith(">")) {
                        strTrim = strTrim.substring(0, strTrim.length() - 1);
                    }
                    if (strTrim.equalsIgnoreCase("Clock")) {
                        new C0543c();
                        C0543c c0543c2 = new C0543c();
                        c0543c2.a(3);
                        c0543c2.a("Time");
                        c0543c2.b(PdfOps.s_TOKEN);
                        c0543c2.a(0.001f);
                        this.f4823g.add(c0543c2);
                        this.f4592a = true;
                    }
                    while (strTrim.endsWith("*")) {
                        strTrim = strTrim.substring(0, strTrim.length() - 1);
                    }
                    if (strTrim.trim().equals(SchemaSymbols.ATTVAL_TIME)) {
                        strTrim = "Time";
                    }
                    if (strTrim.contains(CallSiteDescriptor.OPERATOR_DELIMITER)) {
                        strTrim = strTrim.substring(0, strTrim.indexOf(124));
                        this.f4837t = true;
                    }
                    String strTrim2 = strTrim.trim();
                    String str2 = strTrim2;
                    for (int i3 = 0; i3 < 100 && i(str2); i3++) {
                        str2 = strTrim2 + i3;
                    }
                    c0543c.a(str2);
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
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList.iterator();
    }

    @Override // ak.C0546f, W.V
    public float[] c() {
        float[] fArrC = super.c();
        if (this.f4592a) {
            float[] fArr = new float[fArrC.length + 1];
            fArr[0] = fArrC[0];
            System.arraycopy(fArrC, 0, fArr, 1, fArrC.length);
            fArrC = fArr;
        }
        return fArrC;
    }
}
