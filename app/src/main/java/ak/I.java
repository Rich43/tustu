package ak;

import W.C0187m;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ak/I.class */
public class I extends C0546f {

    /* renamed from: a, reason: collision with root package name */
    float f4594a;

    public I() {
        super(",", false);
        this.f4594a = -1.0f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:64:0x0271, code lost:
    
        a(true);
     */
    @Override // ak.C0546f, W.V
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Iterator b() throws V.a {
        /*
            Method dump skipped, instructions count: 762
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ak.I.b():java.util.Iterator");
    }

    @Override // ak.C0546f
    protected void e(String str) throws C0187m {
        if (str.indexOf("Device Time") > -1) {
            throw new C0187m("Log Restart");
        }
        super.e(str);
    }

    @Override // ak.C0546f, W.V
    public float[] c() throws V.a {
        float[] fArrC = super.c();
        if (this.f4823g.size() > fArrC.length) {
            float[] fArr = new float[this.f4823g.size()];
            System.arraycopy(fArrC, 0, fArr, 0, fArrC.length);
            fArrC = fArr;
        }
        return fArrC;
    }

    @Override // ak.C0546f
    protected float g(String str) {
        int i2 = 0;
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(str, CallSiteDescriptor.TOKEN_DELIMITER);
            if (stringTokenizer.countTokens() > 2) {
                String strNextToken = stringTokenizer.nextToken();
                if ((strNextToken.contains("/") || strNextToken.contains(LanguageTag.SEP)) && strNextToken.contains(" ")) {
                    strNextToken = strNextToken.substring(strNextToken.indexOf(" ") + 1);
                }
                i2 = Integer.parseInt(strNextToken);
            }
            int i3 = Integer.parseInt(stringTokenizer.nextToken());
            float f2 = (i2 * 3600) + (i3 * 60) + Float.parseFloat(stringTokenizer.nextToken());
            if (this.f4594a < 0.0f) {
                this.f4594a = f2;
            }
            return f2 - this.f4594a;
        } catch (Exception e2) {
            return 0.0f;
        }
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f1978t;
    }
}
