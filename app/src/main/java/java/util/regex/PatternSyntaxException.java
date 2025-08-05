package java.util.regex;

import java.security.AccessController;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/util/regex/PatternSyntaxException.class */
public class PatternSyntaxException extends IllegalArgumentException {
    private static final long serialVersionUID = -3864639126226059218L;
    private final String desc;
    private final String pattern;
    private final int index;
    private static final String nl = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));

    public PatternSyntaxException(String str, String str2, int i2) {
        this.desc = str;
        this.pattern = str2;
        this.index = i2;
    }

    public int getIndex() {
        return this.index;
    }

    public String getDescription() {
        return this.desc;
    }

    public String getPattern() {
        return this.pattern;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.desc);
        if (this.index >= 0) {
            stringBuffer.append(" near index ");
            stringBuffer.append(this.index);
        }
        stringBuffer.append(nl);
        stringBuffer.append(this.pattern);
        if (this.index >= 0 && this.pattern != null && this.index < this.pattern.length()) {
            stringBuffer.append(nl);
            for (int i2 = 0; i2 < this.index; i2++) {
                stringBuffer.append(' ');
            }
            stringBuffer.append('^');
        }
        return stringBuffer.toString();
    }
}
