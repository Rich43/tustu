package javax.tools;

import java.util.Locale;

/* loaded from: rt.jar:javax/tools/Diagnostic.class */
public interface Diagnostic<S> {
    public static final long NOPOS = -1;

    /* loaded from: rt.jar:javax/tools/Diagnostic$Kind.class */
    public enum Kind {
        ERROR,
        WARNING,
        MANDATORY_WARNING,
        NOTE,
        OTHER
    }

    Kind getKind();

    S getSource();

    long getPosition();

    long getStartPosition();

    long getEndPosition();

    long getLineNumber();

    long getColumnNumber();

    String getCode();

    String getMessage(Locale locale);
}
