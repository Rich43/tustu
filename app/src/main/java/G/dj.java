package G;

import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:G/dj.class */
public class dj {

    /* renamed from: a, reason: collision with root package name */
    public static String f1196a = "+-/=&<>*^!,% [{()}]|\n\t?:\\";

    public static ArrayList a(String str) {
        ArrayList arrayList = new ArrayList();
        String str2 = "";
        boolean z2 = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == '$') {
                z2 = true;
            } else if (f1196a.indexOf(str.charAt(i2)) != -1) {
                z2 = false;
                if (str2.trim().length() > 0) {
                    arrayList.add(str2);
                }
                str2 = "";
            }
            if (z2) {
                str2 = str2 + str.charAt(i2);
            }
        }
        if (str2.trim().length() > 0) {
            arrayList.add(str2.trim());
        }
        return arrayList;
    }
}
