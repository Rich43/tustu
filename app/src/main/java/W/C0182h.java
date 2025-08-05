package W;

import org.icepdf.core.util.PdfOps;

/* renamed from: W.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/h.class */
public class C0182h {

    /* renamed from: a, reason: collision with root package name */
    private String f2131a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f2132b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f2133c = null;

    public void a(String str) throws S {
        this.f2131a = str;
        try {
            b(str);
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new S("Unable to parse row. \n" + str);
        }
    }

    public String a() {
        return this.f2132b;
    }

    public String b() {
        return this.f2133c;
    }

    private void b(String str) {
        String[] strArrJ = bH.W.j(str);
        if (strArrJ.length > 0) {
            this.f2132b = c(strArrJ[0].trim());
        }
        if (strArrJ.length > 1) {
            String strTrim = strArrJ[1].trim();
            if (strTrim.length() > 1) {
                this.f2133c = c(strTrim);
            }
        }
    }

    private String c(String str) {
        if (str.charAt(0) == '\"') {
            str = str.substring(1);
        }
        if (str.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
