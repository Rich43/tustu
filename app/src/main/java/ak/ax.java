package ak;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ak/ax.class */
public class ax extends R {

    /* renamed from: a, reason: collision with root package name */
    int f4801a;

    public ax() {
        super(",", false);
        this.f4801a = 0;
        this.f4837t = true;
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        while (this.f4823g.isEmpty()) {
            try {
                int i2 = 0;
                String strB = bH.W.b("Time" + l(), this.f4822f + this.f4822f, this.f4822f + " " + this.f4822f);
                String[] strArrSplit = null;
                try {
                    l();
                    l();
                    String strL = l();
                    if (c(strL, ",")) {
                        a(true);
                        strArrSplit = null;
                    } else {
                        strArrSplit = strL.split(",");
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                StringTokenizer stringTokenizer = new StringTokenizer(strB, this.f4822f);
                while (stringTokenizer.hasMoreTokens()) {
                    String strNextToken = stringTokenizer.nextToken();
                    if (strNextToken.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                        strNextToken = bH.W.b(strNextToken, PdfOps.DOUBLE_QUOTE__TOKEN, "").trim();
                    }
                    if (strNextToken.equals(" ")) {
                        strNextToken = "Col" + i2;
                    }
                    if (strNextToken.equalsIgnoreCase("lambda")) {
                        strNextToken = "Lambda";
                    }
                    if (strNextToken.contains(LanguageTag.SEP)) {
                        strNextToken = bH.W.b(strNextToken, LanguageTag.SEP, " ");
                    }
                    if (strNextToken.contains(Constants.INDENT)) {
                        strNextToken = bH.W.b(strNextToken, Constants.INDENT, " ");
                    }
                    C0543c c0543c = new C0543c();
                    if (strNextToken.equals("Timestamp (mS)") || strNextToken.equals("Elapsed Time")) {
                        c0543c.a(3);
                        c0543c.a("Time");
                        c0543c.b(PdfOps.s_TOKEN);
                        strNextToken = "Time";
                        c0543c.a(0.001f);
                    }
                    try {
                        if (strNextToken.indexOf("[") > 1 && strNextToken.contains("]")) {
                            int iIndexOf = strNextToken.indexOf("[");
                            int iIndexOf2 = strNextToken.indexOf("]", iIndexOf);
                            c0543c.b(strNextToken.substring(iIndexOf + 1, iIndexOf2).trim());
                            strNextToken = (strNextToken.substring(0, iIndexOf) + strNextToken.substring(iIndexOf2 + 1, strNextToken.length())).trim();
                            this.f4837t = true;
                        }
                        if (strNextToken.indexOf("(") > 1 && strNextToken.contains(")")) {
                            int iIndexOf3 = strNextToken.indexOf("(");
                            int iIndexOf4 = strNextToken.indexOf(")", iIndexOf3);
                            c0543c.b(strNextToken.substring(iIndexOf3 + 1, iIndexOf4).trim());
                            strNextToken = (strNextToken.substring(0, iIndexOf3) + strNextToken.substring(iIndexOf4 + 1, strNextToken.length())).trim();
                        }
                    } catch (Exception e3) {
                        bH.C.c("Thought I could parse units, but it failed on field \"" + strNextToken + PdfOps.DOUBLE_QUOTE__TOKEN);
                    }
                    if (strNextToken.trim().equals(SchemaSymbols.ATTVAL_TIME)) {
                        strNextToken = "Time";
                    }
                    String strTrim = strNextToken.trim();
                    String str = strTrim;
                    for (int i3 = 0; i3 < 100 && i(str); i3++) {
                        str = strTrim + i3;
                    }
                    c0543c.a(str);
                    if (c0543c.a().contains("Latitude") || c0543c.a().contains("Longitude")) {
                        c0543c.a(7);
                    }
                    if (strArrSplit != null && strArrSplit.length > this.f4823g.size()) {
                        c0543c.b(strArrSplit[this.f4823g.size()]);
                    }
                    this.f4823g.add(c0543c);
                    i2++;
                }
                try {
                    l();
                    l();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            } catch (V.a e5) {
                e5.printStackTrace();
                throw new V.a("No Valid Data found in file");
            } catch (IOException e6) {
                e6.printStackTrace();
                throw new V.a("IO Error reading header rows from file.");
            }
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        this.f4801a = arrayList.size();
        return arrayList.iterator();
    }

    @Override // ak.C0546f, W.V
    public float[] c() {
        float[] fArrC = super.c();
        if (fArrC.length < this.f4801a) {
            float[] fArr = new float[this.f4801a];
            System.arraycopy(fArrC, 0, fArr, 0, fArrC.length);
            fArrC = fArr;
        }
        return fArrC;
    }

    @Override // ak.C0546f
    protected int b(String str) {
        return 1;
    }

    @Override // ak.C0546f
    protected float g(String str) {
        float f2 = 0.0f;
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(str, CallSiteDescriptor.TOKEN_DELIMITER);
            stringTokenizer.nextToken();
            int i2 = Integer.parseInt(stringTokenizer.nextToken());
            int i3 = Integer.parseInt(stringTokenizer.nextToken());
            float f3 = Float.parseFloat(stringTokenizer.nextToken());
            if (stringTokenizer.hasMoreElements()) {
                f2 = Float.parseFloat(stringTokenizer.nextToken()) / 1000.0f;
            }
            this.f4837t = true;
            return (i2 * 3600) + (i3 * 60) + f3 + f2;
        } catch (Exception e2) {
            return 0.0f;
        }
    }

    @Override // ak.R, ak.C0546f, W.V
    public String i() {
        return W.X.f1980v;
    }
}
