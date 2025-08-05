package javax.sql.rowset;

import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/rowset/RowSetWarning.class */
public class RowSetWarning extends SQLException {
    static final long serialVersionUID = 6678332766434564774L;

    public RowSetWarning(String str) {
        super(str);
    }

    public RowSetWarning() {
    }

    public RowSetWarning(String str, String str2) {
        super(str, str2);
    }

    public RowSetWarning(String str, String str2, int i2) {
        super(str, str2, i2);
    }

    public RowSetWarning getNextWarning() {
        SQLException nextException = getNextException();
        if (nextException == null || (nextException instanceof RowSetWarning)) {
            return (RowSetWarning) nextException;
        }
        throw new Error("RowSetWarning chain holds value that is not a RowSetWarning: ");
    }

    public void setNextWarning(RowSetWarning rowSetWarning) {
        setNextException(rowSetWarning);
    }
}
