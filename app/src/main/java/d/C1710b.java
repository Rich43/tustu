package d;

import bH.C;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: d.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:d/b.class */
public class C1710b extends Properties {

    /* renamed from: a, reason: collision with root package name */
    static String f12094a = "=";

    /* renamed from: b, reason: collision with root package name */
    static String f12095b = "&";

    public String a() {
        StringBuilder sb = new StringBuilder();
        for (String str : stringPropertyNames()) {
            try {
                sb.append(str).append(f12094a);
                sb.append(URLEncoder.encode(getProperty(str), "UTF-8")).append(f12095b);
            } catch (UnsupportedEncodingException e2) {
                Logger.getLogger(C1710b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return sb.toString();
    }

    public static C1710b a(String str) throws e {
        C1710b c1710b = new C1710b();
        if (str == null || str.indexOf(f12094a) == -1) {
            return c1710b;
        }
        if (str.indexOf(f12095b) == -1) {
            str = str + f12095b;
        }
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(str, f12095b);
            while (stringTokenizer.hasMoreElements()) {
                String strNextToken = stringTokenizer.nextToken();
                c1710b.setProperty(strNextToken.substring(0, strNextToken.indexOf(f12094a)), URLDecoder.decode(strNextToken.substring(strNextToken.indexOf(f12094a) + 1), "UTF-8"));
            }
            return c1710b;
        } catch (Exception e2) {
            throw new e("Unable to parse parameter String:\n" + str);
        }
    }

    public static String a(Properties properties) {
        StringBuilder sb = new StringBuilder();
        try {
            Iterator<String> it = properties.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String next = it.next();
                sb.append(next).append(f12094a).append(URLEncoder.encode(properties.getProperty(next), "UTF-8"));
                if (it.hasNext()) {
                    sb.append(f12095b);
                }
            }
        } catch (UnsupportedEncodingException e2) {
            C.c("Failed to create AppAction Parameter String.");
            e2.printStackTrace();
        }
        return sb.toString();
    }
}
