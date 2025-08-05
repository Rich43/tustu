package sun.nio.fs;

/* loaded from: rt.jar:sun/nio/fs/Globs.class */
public class Globs {
    private static final String regexMetaChars = ".^$+{[]|()";
    private static final String globMetaChars = "\\*?[{";
    private static char EOL = 0;

    private Globs() {
    }

    private static boolean isRegexMeta(char c2) {
        return regexMetaChars.indexOf(c2) != -1;
    }

    private static boolean isGlobMeta(char c2) {
        return globMetaChars.indexOf(c2) != -1;
    }

    private static char next(String str, int i2) {
        if (i2 < str.length()) {
            return str.charAt(i2);
        }
        return EOL;
    }

    /* JADX WARN: Removed duplicated region for block: B:131:0x0201 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0210  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String toRegexPattern(java.lang.String r7, boolean r8) {
        /*
            Method dump skipped, instructions count: 745
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.fs.Globs.toRegexPattern(java.lang.String, boolean):java.lang.String");
    }

    static String toUnixRegexPattern(String str) {
        return toRegexPattern(str, false);
    }

    static String toWindowsRegexPattern(String str) {
        return toRegexPattern(str, true);
    }
}
