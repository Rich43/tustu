package java.sql;

/* loaded from: rt.jar:java/sql/SQLTransientException.class */
public class SQLTransientException extends SQLException {
    private static final long serialVersionUID = -9042733978262274539L;

    public SQLTransientException() {
    }

    public SQLTransientException(String str) {
        super(str);
    }

    public SQLTransientException(String str, String str2) {
        super(str, str2);
    }

    public SQLTransientException(String str, String str2, int i2) {
        super(str, str2, i2);
    }

    public SQLTransientException(Throwable th) {
        super(th);
    }

    public SQLTransientException(String str, Throwable th) {
        super(str, th);
    }

    public SQLTransientException(String str, String str2, Throwable th) {
        super(str, str2, th);
    }

    public SQLTransientException(String str, String str2, int i2, Throwable th) {
        super(str, str2, i2, th);
    }
}
