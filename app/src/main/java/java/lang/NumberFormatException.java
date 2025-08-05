package java.lang;

import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:java/lang/NumberFormatException.class */
public class NumberFormatException extends IllegalArgumentException {
    static final long serialVersionUID = -2848938806368998894L;

    public NumberFormatException() {
    }

    public NumberFormatException(String str) {
        super(str);
    }

    static NumberFormatException forInputString(String str) {
        return new NumberFormatException("For input string: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
    }
}
