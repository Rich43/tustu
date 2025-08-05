package netscape.javascript;

/* loaded from: jfxrt.jar:netscape/javascript/JSException.class */
public class JSException extends RuntimeException {
    public static final int EXCEPTION_TYPE_EMPTY = -1;
    public static final int EXCEPTION_TYPE_VOID = 0;
    public static final int EXCEPTION_TYPE_OBJECT = 1;
    public static final int EXCEPTION_TYPE_FUNCTION = 2;
    public static final int EXCEPTION_TYPE_STRING = 3;
    public static final int EXCEPTION_TYPE_NUMBER = 4;
    public static final int EXCEPTION_TYPE_BOOLEAN = 5;
    public static final int EXCEPTION_TYPE_ERROR = 6;
    protected String message;
    protected String filename;
    protected int lineno;
    protected String source;
    protected int tokenIndex;
    private int wrappedExceptionType;
    private Object wrappedException;

    public JSException() {
        this(null);
    }

    public JSException(String s2) {
        this(s2, null, -1, null, -1);
    }

    public JSException(String s2, String filename, int lineno, String source, int tokenIndex) {
        super(s2);
        this.message = null;
        this.filename = null;
        this.lineno = -1;
        this.source = null;
        this.tokenIndex = -1;
        this.wrappedExceptionType = -1;
        this.wrappedException = null;
        this.message = s2;
        this.filename = filename;
        this.lineno = lineno;
        this.source = source;
        this.tokenIndex = tokenIndex;
        this.wrappedExceptionType = -1;
    }

    public JSException(int wrappedExceptionType, Object wrappedException) {
        this();
        this.wrappedExceptionType = wrappedExceptionType;
        this.wrappedException = wrappedException;
    }

    public int getWrappedExceptionType() {
        return this.wrappedExceptionType;
    }

    public Object getWrappedException() {
        return this.wrappedException;
    }
}
