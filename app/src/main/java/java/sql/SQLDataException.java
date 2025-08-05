package java.sql;

/* loaded from: rt.jar:java/sql/SQLDataException.class */
public class SQLDataException extends SQLNonTransientException {
    private static final long serialVersionUID = -6889123282670549800L;

    public SQLDataException() {
    }

    public SQLDataException(String str) {
        super(str);
    }

    public SQLDataException(String str, String str2) {
        super(str, str2);
    }

    public SQLDataException(String str, String str2, int i2) {
        super(str, str2, i2);
    }

    public SQLDataException(Throwable th) {
        super(th);
    }

    public SQLDataException(String str, Throwable th) {
        super(str, th);
    }

    public SQLDataException(String str, String str2, Throwable th) {
        super(str, str2, th);
    }

    public SQLDataException(String str, String str2, int i2, Throwable th) {
        super(str, str2, i2, th);
    }
}
