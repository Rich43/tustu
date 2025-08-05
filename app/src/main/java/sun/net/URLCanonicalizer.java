package sun.net;

/* loaded from: rt.jar:sun/net/URLCanonicalizer.class */
public class URLCanonicalizer {
    public String canonicalize(String str) {
        String str2 = str;
        if (str.startsWith("ftp.")) {
            str2 = "ftp://" + str;
        } else if (str.startsWith("gopher.")) {
            str2 = "gopher://" + str;
        } else if (str.startsWith("/")) {
            str2 = "file:" + str;
        } else if (!hasProtocolName(str)) {
            if (isSimpleHostName(str)) {
                str = "www." + str + ".com";
            }
            str2 = "http://" + str;
        }
        return str2;
    }

    public boolean hasProtocolName(String str) {
        int iIndexOf = str.indexOf(58);
        if (iIndexOf <= 0) {
            return false;
        }
        for (int i2 = 0; i2 < iIndexOf; i2++) {
            char cCharAt = str.charAt(i2);
            if ((cCharAt < 'A' || cCharAt > 'Z') && ((cCharAt < 'a' || cCharAt > 'z') && cCharAt != '-')) {
                return false;
            }
        }
        return true;
    }

    protected boolean isSimpleHostName(String str) {
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if ((cCharAt < 'A' || cCharAt > 'Z') && ((cCharAt < 'a' || cCharAt > 'z') && ((cCharAt < '0' || cCharAt > '9') && cCharAt != '-'))) {
                return false;
            }
        }
        return true;
    }
}
