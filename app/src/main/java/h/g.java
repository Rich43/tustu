package h;

import g.C1733k;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:h/g.class */
public class g extends Properties {

    /* renamed from: m, reason: collision with root package name */
    private String f12241m = f12240a;

    /* renamed from: a, reason: collision with root package name */
    public static String f12240a = "FieldMaps/MegaSquirt.properties";

    /* renamed from: n, reason: collision with root package name */
    private static g f12242n = null;

    /* renamed from: b, reason: collision with root package name */
    public static String f12243b = "O2Volts";

    /* renamed from: c, reason: collision with root package name */
    public static String f12244c = "coolantTemp";

    /* renamed from: d, reason: collision with root package name */
    public static String f12245d = "RPM";

    /* renamed from: e, reason: collision with root package name */
    public static String f12246e = "egoCorrection";

    /* renamed from: f, reason: collision with root package name */
    public static String f12247f = "TP";

    /* renamed from: g, reason: collision with root package name */
    public static String f12248g = "pulseWidth";

    /* renamed from: h, reason: collision with root package name */
    public static String f12249h = "AFR";

    /* renamed from: i, reason: collision with root package name */
    public static String f12250i = "Lambda";

    /* renamed from: j, reason: collision with root package name */
    public static String f12251j = "MAP";

    /* renamed from: k, reason: collision with root package name */
    public static String f12252k = "yAxisField";

    /* renamed from: l, reason: collision with root package name */
    public static String f12253l = "warmup";

    private g() {
    }

    public static g a() {
        if (f12242n == null) {
            f12242n = new g();
            f12242n.b();
        }
        return f12242n;
    }

    public String a(String str) {
        if (str == null) {
            return "";
        }
        if (str.indexOf("Field.") != -1) {
            str = C1733k.a(str, "Field.", "");
        }
        String str2 = "";
        if (str.indexOf(Marker.ANY_NON_NULL_MARKER) != -1) {
            str2 = str2 + str.substring(str.indexOf(Marker.ANY_NON_NULL_MARKER));
            str = C1733k.a(str, str2, "");
        }
        if (str.indexOf(LanguageTag.SEP) != -1) {
            str2 = str2 + str.substring(str.indexOf(LanguageTag.SEP));
            str = C1733k.a(str, str2, "");
        }
        if (str.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER) != -1) {
            str2 = str2 + str.substring(str.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER));
            str = C1733k.a(str, str2, "");
        }
        String property = getProperty(str);
        return (property == null || property.equals("")) ? str + str2 : property + str2;
    }

    public void a(String str, String str2) {
        super.setProperty(str, str2);
    }

    public void b() {
        clear();
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = new FileInputStream(this.f12241m);
                super.load(fileInputStream);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (FileNotFoundException e3) {
                new RuntimeException(new File(this.f12241m).getAbsolutePath() + " does not exist", e3).printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e4) {
                    }
                }
            } catch (IOException e5) {
                e5.printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e6) {
                    }
                }
            }
            File file = new File(".", "FieldMaps/Normalized.properties");
            if (C1737b.a().a("fieldNameNormalizationEditable")) {
                try {
                    if (i.a("fieldNameNormaization", true)) {
                        try {
                            try {
                                fileInputStream = new FileInputStream(file);
                                super.load(fileInputStream);
                                if (fileInputStream != null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (Exception e7) {
                                    }
                                }
                            } catch (FileNotFoundException e8) {
                                new RuntimeException(file.getAbsolutePath() + " does not exist", e8).printStackTrace();
                                if (fileInputStream != null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (Exception e9) {
                                    }
                                }
                            }
                        } catch (IOException e10) {
                            e10.printStackTrace();
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (Exception e11) {
                                }
                            }
                        }
                    }
                } catch (Throwable th) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e12) {
                        }
                    }
                    throw th;
                }
            }
        } catch (Throwable th2) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e13) {
                }
            }
            throw th2;
        }
    }

    public static void b(String str) {
        a().f12241m = str;
        a().b();
    }

    public String c(String str) {
        int iIndexOf = -1;
        while (str.indexOf("[", iIndexOf + 1) != -1) {
            iIndexOf = str.indexOf("[", iIndexOf + 1);
            int iIndexOf2 = str.indexOf("]", iIndexOf + 1);
            String strSubstring = str.substring(iIndexOf + 1, iIndexOf2);
            String strSubstring2 = str.substring(0, iIndexOf);
            String strSubstring3 = str.substring(iIndexOf2 + 1);
            if (strSubstring.contains("Field.")) {
                strSubstring = a(strSubstring);
            }
            str = strSubstring2 + "[" + strSubstring + "]" + strSubstring3;
        }
        return str;
    }

    public String d(String str) {
        String strE = e(str);
        return strE == null ? str : "Field." + strE;
    }

    public String e(String str) {
        if (!super.containsValue(str)) {
            return null;
        }
        for (String str2 : super.keySet()) {
            String property = super.getProperty(str2);
            if (property != null && property.equals(str)) {
                return str2;
            }
        }
        return null;
    }

    public String f(String str) {
        int iIndexOf = -1;
        while (str.indexOf("[", iIndexOf + 1) != -1) {
            iIndexOf = str.indexOf("[", iIndexOf + 1);
            int iIndexOf2 = str.indexOf("]", iIndexOf + 1);
            String strSubstring = str.substring(iIndexOf + 1, iIndexOf2);
            String strSubstring2 = str.substring(0, iIndexOf);
            String strSubstring3 = str.substring(iIndexOf2 + 1);
            String strA = a(strSubstring);
            String strE = e(strA);
            if (strE != null && strE.length() > 0) {
                strA = "Field." + strE;
            }
            str = strSubstring2 + "[" + strA + "]" + strSubstring3;
        }
        return str;
    }
}
