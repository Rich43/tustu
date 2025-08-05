package sun.nio.fs;

import java.nio.file.InvalidPathException;

/* loaded from: rt.jar:sun/nio/fs/WindowsPathParser.class */
class WindowsPathParser {
    private static final String reservedChars = "<>:\"|?*";

    private WindowsPathParser() {
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsPathParser$Result.class */
    static class Result {
        private final WindowsPathType type;
        private final String root;
        private final String path;

        Result(WindowsPathType windowsPathType, String str, String str2) {
            this.type = windowsPathType;
            this.root = str;
            this.path = str2;
        }

        WindowsPathType type() {
            return this.type;
        }

        String root() {
            return this.root;
        }

        String path() {
            return this.path;
        }
    }

    static Result parse(String str) {
        return parse(str, true);
    }

    static Result parseNormalizedPath(String str) {
        return parse(str, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x011a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static sun.nio.fs.WindowsPathParser.Result parse(java.lang.String r8, boolean r9) {
        /*
            Method dump skipped, instructions count: 384
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.fs.WindowsPathParser.parse(java.lang.String, boolean):sun.nio.fs.WindowsPathParser$Result");
    }

    private static String normalize(StringBuilder sb, String str, int i2) {
        int length = str.length();
        int iNextNonSlash = nextNonSlash(str, i2, length);
        int i3 = iNextNonSlash;
        char c2 = 0;
        while (iNextNonSlash < length) {
            char cCharAt = str.charAt(iNextNonSlash);
            if (isSlash(cCharAt)) {
                if (c2 == ' ') {
                    throw new InvalidPathException(str, "Trailing char <" + c2 + ">", iNextNonSlash - 1);
                }
                sb.append((CharSequence) str, i3, iNextNonSlash);
                iNextNonSlash = nextNonSlash(str, iNextNonSlash, length);
                if (iNextNonSlash != length) {
                    sb.append('\\');
                }
                i3 = iNextNonSlash;
            } else {
                if (isInvalidPathChar(cCharAt)) {
                    throw new InvalidPathException(str, "Illegal char <" + cCharAt + ">", iNextNonSlash);
                }
                c2 = cCharAt;
                iNextNonSlash++;
            }
        }
        if (i3 != iNextNonSlash) {
            if (c2 == ' ') {
                throw new InvalidPathException(str, "Trailing char <" + c2 + ">", iNextNonSlash - 1);
            }
            sb.append((CharSequence) str, i3, iNextNonSlash);
        }
        return sb.toString();
    }

    private static final boolean isSlash(char c2) {
        return c2 == '\\' || c2 == '/';
    }

    private static final int nextNonSlash(String str, int i2, int i3) {
        while (i2 < i3 && isSlash(str.charAt(i2))) {
            i2++;
        }
        return i2;
    }

    private static final int nextSlash(String str, int i2, int i3) {
        while (i2 < i3) {
            char cCharAt = str.charAt(i2);
            if (isSlash(cCharAt)) {
                break;
            }
            if (isInvalidPathChar(cCharAt)) {
                throw new InvalidPathException(str, "Illegal character [" + cCharAt + "] in path", i2);
            }
            i2++;
        }
        return i2;
    }

    private static final boolean isLetter(char c2) {
        return (c2 >= 'a' && c2 <= 'z') || (c2 >= 'A' && c2 <= 'Z');
    }

    private static final boolean isInvalidPathChar(char c2) {
        return c2 < ' ' || reservedChars.indexOf(c2) != -1;
    }
}
