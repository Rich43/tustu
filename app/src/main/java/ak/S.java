package ak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:ak/S.class */
public class S extends R {

    /* renamed from: a, reason: collision with root package name */
    private boolean f4598a;

    /* renamed from: b, reason: collision with root package name */
    private int f4599b;

    public S() {
        super(",", false);
        this.f4598a = false;
        this.f4599b = 100;
    }

    @Override // ak.C0546f, W.V
    public Iterator b() {
        Iterator itB = super.b();
        ArrayList arrayList = new ArrayList();
        while (itB.hasNext()) {
            W.T t2 = (W.T) itB.next();
            C0543c c0543c = (C0543c) t2;
            if (t2.a().equals("RTC")) {
                c0543c.a("Time");
            }
            c0543c.b(bH.W.b(bH.W.b(c0543c.b(), "[", ""), "]", ""));
            arrayList.add(t2);
        }
        return arrayList.iterator();
    }

    @Override // ak.C0546f
    protected int b(String str) {
        return 0;
    }

    @Override // ak.C0546f, W.V
    public float[] c() throws V.a {
        String strSubstring;
        boolean zP = p();
        try {
            if (this.f4837t && this.f4832p >= 500 && k()) {
                throw new V.a("This Edition is limited to loading 500 rows of data. \nPlease Register to load large log files.");
            }
            String strA = a(l(), zP);
            String str = this.f4822f;
            if (this.f4829m == -1) {
                this.f4829m = this.f4824h.length() / (strA.length() + 3);
            }
            aD aDVar = new aD(strA, str);
            this.f4825i = new float[aDVar.c()];
            for (int i2 = 0; i2 < this.f4825i.length && i2 < this.f4823g.size(); i2++) {
                try {
                    strSubstring = aDVar.b().trim();
                } catch (Exception e2) {
                    bH.C.c("Error Parsing record:\n" + strA);
                    strSubstring = "0";
                    e2.printStackTrace();
                }
                if (((C0543c) this.f4823g.get(i2)).f() == 0) {
                    try {
                        if (this.f4598a || this.f4832p < this.f4599b) {
                            int iIndexOf = strSubstring.indexOf("/");
                            if (!this.f4598a && iIndexOf >= 0) {
                                this.f4598a = true;
                            }
                            if (iIndexOf >= 0) {
                                strSubstring = strSubstring.substring(0, iIndexOf);
                            }
                        }
                        this.f4825i[i2] = Float.parseFloat(strSubstring);
                    } catch (NumberFormatException e3) {
                        this.f4825i[i2] = Float.NaN;
                    }
                } else {
                    this.f4825i[i2] = Float.NaN;
                }
                if (Float.isNaN(this.f4825i[i2])) {
                    if (strSubstring.isEmpty()) {
                        this.f4825i[i2] = Float.NaN;
                    } else if (strSubstring.indexOf(58) != -1) {
                        this.f4825i[i2] = g(strSubstring);
                    } else if (strSubstring.startsWith("0x") || strSubstring.startsWith("0X")) {
                        try {
                            this.f4825i[i2] = Integer.parseInt(strSubstring.substring(2), 16);
                            this.f4837t = true;
                        } catch (Exception e4) {
                            this.f4825i[i2] = Float.NaN;
                        }
                    } else if (bH.W.a(f4844z, strSubstring)) {
                        this.f4825i[i2] = 0.0f;
                        this.f4837t = true;
                        if (this.f4823g.size() > i2 && ((C0543c) this.f4823g.get(i2)).f() == 0) {
                            a((C0543c) this.f4823g.get(i2), strSubstring);
                        }
                    } else if (bH.W.a(f4843y, strSubstring)) {
                        this.f4825i[i2] = 1.0f;
                        this.f4837t = true;
                        if (this.f4823g.size() > i2 && ((C0543c) this.f4823g.get(i2)).f() == 0) {
                            a((C0543c) this.f4823g.get(i2), strSubstring);
                        }
                    } else if (f(strSubstring)) {
                        this.f4825i[i2] = Float.parseFloat(strSubstring.substring(0, 7) + strSubstring.substring(8));
                    }
                }
            }
            return this.f4825i;
        } catch (IOException e5) {
            e5.printStackTrace();
            throw new V.a("IO Error reading row from file on row " + this.f4832p + ".");
        }
    }
}
