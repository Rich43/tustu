package sun.nio.fs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.Locale;

/* loaded from: rt.jar:sun/nio/fs/AbstractFileTypeDetector.class */
public abstract class AbstractFileTypeDetector extends FileTypeDetector {
    private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";

    protected abstract String implProbeContentType(Path path) throws IOException;

    protected AbstractFileTypeDetector() {
    }

    @Override // java.nio.file.spi.FileTypeDetector
    public final String probeContentType(Path path) throws IOException {
        if (path == null) {
            throw new NullPointerException("'file' is null");
        }
        String strImplProbeContentType = implProbeContentType(path);
        if (strImplProbeContentType == null) {
            return null;
        }
        return parse(strImplProbeContentType);
    }

    private static String parse(String str) {
        int iIndexOf = str.indexOf(47);
        int iIndexOf2 = str.indexOf(59);
        if (iIndexOf < 0) {
            return null;
        }
        String lowerCase = str.substring(0, iIndexOf).trim().toLowerCase(Locale.ENGLISH);
        if (!isValidToken(lowerCase)) {
            return null;
        }
        String lowerCase2 = (iIndexOf2 < 0 ? str.substring(iIndexOf + 1) : str.substring(iIndexOf + 1, iIndexOf2)).trim().toLowerCase(Locale.ENGLISH);
        if (!isValidToken(lowerCase2)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(lowerCase.length() + lowerCase2.length() + 1);
        sb.append(lowerCase);
        sb.append('/');
        sb.append(lowerCase2);
        return sb.toString();
    }

    private static boolean isTokenChar(char c2) {
        return c2 > ' ' && c2 < 127 && TSPECIALS.indexOf(c2) < 0;
    }

    private static boolean isValidToken(String str) {
        int length = str.length();
        if (length == 0) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (!isTokenChar(str.charAt(i2))) {
                return false;
            }
        }
        return true;
    }
}
