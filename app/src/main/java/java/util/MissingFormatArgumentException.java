package java.util;

import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:java/util/MissingFormatArgumentException.class */
public class MissingFormatArgumentException extends IllegalFormatException {
    private static final long serialVersionUID = 19190115;

    /* renamed from: s, reason: collision with root package name */
    private String f12561s;

    public MissingFormatArgumentException(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.f12561s = str;
    }

    public String getFormatSpecifier() {
        return this.f12561s;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return "Format specifier '" + this.f12561s + PdfOps.SINGLE_QUOTE_TOKEN;
    }
}
