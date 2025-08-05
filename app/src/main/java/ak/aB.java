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

/* loaded from: TunerStudioMS.jar:ak/aB.class */
public class aB extends C0546f {

    /* renamed from: a, reason: collision with root package name */
    float[] f4655a;

    public aB() {
        super(";", false);
        this.f4655a = null;
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        while (this.f4823g.isEmpty()) {
            try {
                String strL = l();
                if (strL.startsWith("\"Elapsed Time\"")) {
                    strL = bH.W.b(strL, PdfOps.DOUBLE_QUOTE__TOKEN, "");
                }
                int i2 = 0;
                for (String str : strL.split(Pattern.quote(this.f4822f))) {
                    C0543c c0543c = new C0543c();
                    String strTrim = str.trim();
                    if (strTrim.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
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
                    this.f4837t = true;
                    try {
                        if (strTrim.lastIndexOf(",") > 1) {
                            int iLastIndexOf = strTrim.lastIndexOf(",");
                            c0543c.b(strTrim.substring(iLastIndexOf + 1, strTrim.length()).trim());
                            strTrim = strTrim.substring(0, iLastIndexOf).trim();
                        }
                    } catch (Exception e2) {
                        bH.C.c("Thought I could parse units, but it failed on field \"" + strTrim + PdfOps.DOUBLE_QUOTE__TOKEN);
                    }
                    if (strTrim.trim().equals(SchemaSymbols.ATTVAL_TIME)) {
                        strTrim = "Time";
                        c0543c.a(3);
                        c0543c.a("Time");
                        c0543c.b(PdfOps.s_TOKEN);
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
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList.iterator();
    }

    @Override // ak.C0546f, W.V
    public float[] c() throws V.a {
        float[] fArrC = super.c();
        if (this.f4655a == null) {
            this.f4655a = new float[fArrC.length];
            for (int i2 = 0; i2 < this.f4655a.length; i2++) {
                this.f4655a[i2] = Float.NaN;
            }
        }
        for (int i3 = 0; i3 < fArrC.length; i3++) {
            if (!Float.isNaN(fArrC[i3])) {
                this.f4655a[i3] = fArrC[i3];
            }
        }
        return this.f4655a;
    }
}
