package java.time.format;

import java.time.DateTimeException;

/* loaded from: rt.jar:java/time/format/DateTimeParseException.class */
public class DateTimeParseException extends DateTimeException {
    private static final long serialVersionUID = 4304633501674722597L;
    private final String parsedString;
    private final int errorIndex;

    public DateTimeParseException(String str, CharSequence charSequence, int i2) {
        super(str);
        this.parsedString = charSequence.toString();
        this.errorIndex = i2;
    }

    public DateTimeParseException(String str, CharSequence charSequence, int i2, Throwable th) {
        super(str, th);
        this.parsedString = charSequence.toString();
        this.errorIndex = i2;
    }

    public String getParsedString() {
        return this.parsedString;
    }

    public int getErrorIndex() {
        return this.errorIndex;
    }
}
