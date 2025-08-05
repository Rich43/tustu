package r;

import bH.W;
import java.util.Iterator;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:r/q.class */
public class q {

    /* renamed from: a, reason: collision with root package name */
    public static String f13505a = "*$email";

    /* renamed from: b, reason: collision with root package name */
    public static String f13506b = "*$appName";

    /* renamed from: c, reason: collision with root package name */
    public static String f13507c = "*$appEdition";

    /* renamed from: d, reason: collision with root package name */
    public static String f13508d = "*$daysToExpire";

    /* renamed from: e, reason: collision with root package name */
    public static String f13509e = "Your Registration information is not valid and will be removed.";

    /* renamed from: f, reason: collision with root package name */
    public static String f13510f = "This version has expired, " + f13506b + " will attempt to update.\nA connection to the internet is required for this to be successful.\nThe latest version can be downloaded from:\nhttp://www.efiAnalytics.com/" + f13506b + "/ ";

    /* renamed from: g, reason: collision with root package name */
    public static String f13511g = "Your system clock appears to be wrong.\nThis version of " + f13506b + " requires your clock to be correct to operate.\nPlease correct your system clock.\nhttp://www.efiAnalytics.com/" + f13506b + "/ \nfor a current version";

    /* renamed from: h, reason: collision with root package name */
    public static String f13512h = "The registration you are using appears to have been used extensively!\nIt will now be disabled!\n\nIf you are the rightful owner of this registration, contact EFI Analytics to get a new registration.\nOtherwise please obtain a valid registration key.";

    /* renamed from: i, reason: collision with root package name */
    public static String f13513i = "Your registration appears to be invalid!\nPlease obtain a valid registration key to run the registered version of " + f13506b;

    /* renamed from: j, reason: collision with root package name */
    public static String f13514j = "This version of " + f13506b + " " + f13507c + " will expire in " + f13508d + " days.\nPlease select \"Check For Updates\" from the help menu or \nvisit www.EFIAnalytics.com/" + f13506b + "/ to upgrade to the latest version\nbefore this copy is disabled.";

    public static String a(Properties properties, String str) {
        Iterator<Object> it = properties.keySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            str = W.b(str, str2, properties.getProperty(str2));
        }
        return str;
    }
}
