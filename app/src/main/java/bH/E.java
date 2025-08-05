package bH;

import com.sun.corba.se.impl.util.Version;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:bH/E.class */
public class E implements Serializable {

    /* renamed from: b, reason: collision with root package name */
    static HashMap f7006b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    public static String f7007c = new File(".").getAbsolutePath() + "/inc/";

    /* renamed from: a, reason: collision with root package name */
    ArrayList f7005a = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private File f7008d = null;

    protected E() {
    }

    public void a(String str, String str2) throws IOException {
        File file = new File(new File(str).getAbsolutePath() + "/" + str2);
        if (!file.exists()) {
            file = new File(new File(str).getAbsolutePath() + "/inc/" + str2);
        }
        if (!file.exists()) {
            file = new File(f7007c, str2);
        }
        if (!file.exists()) {
            file = new File(new File(str).getAbsolutePath() + "/projectCfg/" + str2);
        }
        this.f7008d = file;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        int i2 = 0;
        while (true) {
            String line = bufferedReader.readLine();
            String strSubstring = line;
            if (line == null) {
                return;
            }
            if (strSubstring.indexOf(";") != -1) {
                strSubstring = strSubstring.substring(0, strSubstring.indexOf(";"));
            }
            String strTrim = strSubstring.trim();
            if (strTrim.indexOf(FXMLLoader.CONTROLLER_METHOD_PREFIX) != 0 && strTrim.indexOf(PdfOps.SINGLE_QUOTE_TOKEN) != 0 && strTrim.length() > 0 && strTrim.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) == -1) {
                if (strTrim.startsWith(PdfOps.D_TOKEN)) {
                    String strTrim2 = strTrim.substring(strTrim.indexOf(PdfOps.D_TOKEN) + 2).trim();
                    D d2 = new D();
                    int i3 = i2;
                    i2++;
                    d2.a(i3);
                    if (strTrim2.indexOf("T") != -1) {
                        d2.b(Double.parseDouble(W.b(strTrim2, "T", "")));
                    } else {
                        d2.b(Double.parseDouble(strTrim2));
                    }
                    this.f7005a.add(d2);
                } else {
                    int iIndexOf = strTrim.indexOf("\t");
                    String strSubstring2 = strTrim.substring(0, iIndexOf);
                    String strSubstring3 = strTrim.substring(iIndexOf + 1, strTrim.length());
                    D d3 = new D();
                    d3.a(Double.parseDouble(strSubstring2));
                    d3.b(Double.parseDouble(strSubstring3));
                    this.f7005a.add(d3);
                }
            }
        }
    }

    public boolean a() {
        return !this.f7005a.isEmpty();
    }

    public double a(double d2) {
        for (int i2 = 1; i2 < this.f7005a.size(); i2++) {
            D d3 = (D) this.f7005a.get(i2);
            if (d2 < d3.a()) {
                D d4 = (D) this.f7005a.get(i2 - 1);
                return d4.b() + (((d2 - d4.a()) / (d3.a() - d4.a())) * (d3.b() - d4.b()));
            }
        }
        return ((D) this.f7005a.get(this.f7005a.size() - 1)).b();
    }

    public double b(double d2) {
        boolean z2 = ((D) this.f7005a.get(0)).b() > ((D) this.f7005a.get(this.f7005a.size() - 1)).b();
        for (int i2 = 1; i2 < this.f7005a.size(); i2++) {
            D d3 = (D) this.f7005a.get(i2);
            if ((!z2 && d2 < d3.b()) || d2 > d3.b()) {
                D d4 = (D) this.f7005a.get(i2 - 1);
                return d4.a() + (((d2 - d4.b()) / (d3.b() - d4.b())) * (d3.a() - d4.a()));
            }
        }
        return ((D) this.f7005a.get(this.f7005a.size() - 1)).a();
    }

    public int b() {
        return this.f7005a.size();
    }

    public String a(String str) throws NumberFormatException {
        if (str == null || str.length() == 0) {
            str = Version.BUILD;
        }
        String strSubstring = a(Double.parseDouble(str)) + "";
        int iIndexOf = strSubstring.indexOf(46);
        if (iIndexOf != -1 && strSubstring.length() - iIndexOf > 4) {
            strSubstring = strSubstring.substring(0, iIndexOf + 3);
        }
        return strSubstring;
    }

    public static void c() {
        f7006b.clear();
    }

    private static String c(String str, String str2) {
        return str + "_" + str2;
    }

    public static E b(String str, String str2) throws IOException {
        E e2 = (E) f7006b.get(c(str, str2));
        if (e2 != null) {
            return e2;
        }
        E e3 = new E();
        e3.a(str, str2);
        f7006b.put(c(str, str2), e3);
        return e3;
    }

    protected static String b(String str) {
        if (str.indexOf(Marker.ANY_NON_NULL_MARKER) != -1 || str.indexOf(LanguageTag.SEP) != -1) {
            int iIndexOf = str.indexOf(Marker.ANY_NON_NULL_MARKER) != -1 ? str.indexOf(Marker.ANY_NON_NULL_MARKER) : str.indexOf(LanguageTag.SEP);
            str.substring(iIndexOf, str.length());
            str = W.b(str, str.substring(iIndexOf, str.length()), "");
        }
        return str;
    }

    public File d() {
        return this.f7008d;
    }
}
