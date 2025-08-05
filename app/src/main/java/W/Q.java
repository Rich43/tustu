package W;

import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:W/Q.class */
public class Q {
    public static String a(String str) {
        return str.substring(str.indexOf("=") + 1, str.length()).trim();
    }

    public static String b(String str) {
        return str.substring(0, str.indexOf("=")).trim();
    }

    public static String[] c(String str) {
        boolean z2 = false;
        boolean z3 = false;
        int i2 = 0;
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        while (i3 < str.length()) {
            if (str.charAt(i3) == '\"' || str.charAt(i3) == '[' || str.charAt(i3) == ']') {
                z2 = !z2;
            } else if (str.charAt(i3) == '{' || str.charAt(i3) == '}') {
                z3 = !z3;
            } else if (!z3 && !z2 && (str.charAt(i3) == ',' || str.charAt(i3) == ' ' || str.charAt(i3) == '\t' || i3 == str.length())) {
                arrayList.add(str.substring(i2, i3));
                while (true) {
                    if (i3 < str.length()) {
                        if (str.charAt(i3) != ',' && str.charAt(i3) != ' ' && str.charAt(i3) != '\t') {
                            i3--;
                            break;
                        }
                        i3++;
                    } else {
                        break;
                    }
                }
                i2 = i3 + 1;
            }
            i3++;
        }
        if (i2 < str.length()) {
            arrayList.add(str.substring(i2, str.length()));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }
}
