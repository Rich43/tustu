package G;

import bH.C0995c;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:G/bX.class */
public class bX extends Q {

    /* renamed from: a, reason: collision with root package name */
    private String f872a = "big";

    /* renamed from: b, reason: collision with root package name */
    private bY f873b = null;

    /* renamed from: c, reason: collision with root package name */
    private bY f874c = null;

    /* renamed from: d, reason: collision with root package name */
    private bY f875d = null;

    public bY a() {
        return this.f873b;
    }

    public void a(String str) {
        this.f873b = b(str);
    }

    public bY b(String str) {
        bY bYVar = new bY(this);
        bYVar.a(str);
        return a(bYVar);
    }

    public boolean b() {
        return c().equals("big");
    }

    public String c() {
        return this.f872a;
    }

    public bY a(bY bYVar) throws V.g {
        String strA = bYVar.a();
        try {
            if (bYVar.f()) {
                bH.C.b("Variables not currently supported in Interrogation command strings.");
            }
            int iIndexOf = strA.indexOf(FXMLLoader.RESOURCE_KEY_PREFIX) != -1 ? strA.indexOf(FXMLLoader.RESOURCE_KEY_PREFIX) : strA.length();
            if (iIndexOf == -1 && strA.indexOf(FXMLLoader.ESCAPE_PREFIX) == -1 && strA.indexOf(FXMLLoader.RESOURCE_KEY_PREFIX) == -1) {
                bYVar.b("");
                bYVar.a(strA.getBytes());
                return bYVar;
            }
            bYVar.b(strA.substring(iIndexOf));
            if (bYVar.c().indexOf("%2cId") != -1) {
                bYVar.b(2);
            } else if (bYVar.c().indexOf("%cId") != -1) {
                bYVar.b(1);
            } else {
                bYVar.b(0);
            }
            String strSubstring = strA.substring(0, iIndexOf);
            byte[] bArrC = c(strSubstring);
            bH.C.c("Command Non-VolatileBytes for " + strSubstring + " resolved to: " + C0995c.d(bArrC));
            bYVar.a(bArrC);
            return bYVar;
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
            throw new V.g("Unable to parse SendCommand:" + strA + "\nCould not convert to number " + e2.getMessage(), e2);
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.g("Unable to parse SendCommand:" + strA, e3);
        }
    }

    public static byte[] c(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, FXMLLoader.ESCAPE_PREFIX);
        byte[] bArr = new byte[stringTokenizer.countTokens()];
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (i2 == 0) {
                bArr[i2] = strNextToken.getBytes()[0];
            } else {
                bArr[i2] = f(strNextToken);
            }
            i2++;
        }
        return bArr;
    }

    private static byte f(String str) {
        int i2;
        if (str.startsWith(LanguageTag.PRIVATEUSE)) {
            str = str.substring(1);
            i2 = 16;
        } else {
            i2 = 8;
        }
        return (byte) Integer.parseInt(str, i2);
    }

    public bY d() {
        return this.f874c;
    }

    public void d(String str) {
        this.f874c = b(str);
    }

    public bY e() {
        return this.f875d;
    }

    public void e(String str) {
        this.f875d = b(str);
    }
}
