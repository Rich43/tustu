package sun.misc;

/* loaded from: rt.jar:sun/misc/MessageUtils.class */
public class MessageUtils {
    public static native void toStderr(String str);

    public static native void toStdout(String str);

    public static String subst(String str, String str2) {
        return subst(str, new String[]{str2});
    }

    public static String subst(String str, String str2, String str3) {
        return subst(str, new String[]{str2, str3});
    }

    public static String subst(String str, String str2, String str3, String str4) {
        return subst(str, new String[]{str2, str3, str4});
    }

    public static String subst(String str, String[] strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int length = str.length();
        int i2 = 0;
        while (i2 >= 0 && i2 < length) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '%') {
                if (i2 != length) {
                    int iDigit = Character.digit(str.charAt(i2 + 1), 10);
                    if (iDigit == -1) {
                        stringBuffer.append(str.charAt(i2 + 1));
                        i2++;
                    } else if (iDigit < strArr.length) {
                        stringBuffer.append(strArr[iDigit]);
                        i2++;
                    }
                }
            } else {
                stringBuffer.append(cCharAt);
            }
            i2++;
        }
        return stringBuffer.toString();
    }

    public static String substProp(String str, String str2) {
        return subst(System.getProperty(str), str2);
    }

    public static String substProp(String str, String str2, String str3) {
        return subst(System.getProperty(str), str2, str3);
    }

    public static String substProp(String str, String str2, String str3, String str4) {
        return subst(System.getProperty(str), str2, str3, str4);
    }

    public static void err(String str) {
        toStderr(str + "\n");
    }

    public static void out(String str) {
        toStdout(str + "\n");
    }

    public static void where() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (int i2 = 1; i2 < stackTrace.length; i2++) {
            toStderr("\t" + stackTrace[i2].toString() + "\n");
        }
    }
}
