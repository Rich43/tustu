package java.util;

import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:java/util/IllegalFormatFlagsException.class */
public class IllegalFormatFlagsException extends IllegalFormatException {
    private static final long serialVersionUID = 790824;
    private String flags;

    public IllegalFormatFlagsException(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.flags = str;
    }

    public String getFlags() {
        return this.flags;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return "Flags = '" + this.flags + PdfOps.SINGLE_QUOTE_TOKEN;
    }
}
