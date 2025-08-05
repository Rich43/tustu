package ao;

import java.util.Iterator;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:ao/hD.class */
public class hD {

    /* renamed from: d, reason: collision with root package name */
    private static String f6002d = "*$";

    /* renamed from: a, reason: collision with root package name */
    public static String f6003a = f6002d + "email";

    /* renamed from: b, reason: collision with root package name */
    public static String f6004b = "<html><body>Free Upgrade.<br>" + h.i.f12255b + " " + h.i.f12256c + " version 3.0 requires a new registration key.<br><br>As an existing registered user, this is a free upgrade. <br>If you are connected to the Internet, <br>Click 'Yes' to have your new Registration Key sent to " + f6003a + ". <br><br>If you are not currently connected to the internet, to get your new registration key go to:<br><br>http://www.efianalytics.com/MegaLogViewer/upgrade/<br><br>You will need your current registered email address:<br>Registered Email: " + f6003a + " <br><br></body></html>";

    /* renamed from: c, reason: collision with root package name */
    static String f6005c = "Your Registration information is not valid and will be removed.";

    public static String a(Properties properties, String str) {
        Iterator<Object> it = properties.keySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            str = bH.W.b(str, str2, properties.getProperty(str2));
        }
        return str;
    }
}
