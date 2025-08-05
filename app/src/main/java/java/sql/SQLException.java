package java.sql;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: rt.jar:java/sql/SQLException.class */
public class SQLException extends Exception implements Iterable<Throwable> {
    private String SQLState;
    private int vendorCode;
    private volatile SQLException next;
    private static final AtomicReferenceFieldUpdater<SQLException, SQLException> nextUpdater = AtomicReferenceFieldUpdater.newUpdater(SQLException.class, SQLException.class, Constants.NEXT);
    private static final long serialVersionUID = 2135244094396331484L;

    public SQLException(String str, String str2, int i2) {
        super(str);
        this.SQLState = str2;
        this.vendorCode = i2;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            DriverManager.println("SQLState(" + str2 + ") vendor code(" + i2 + ")");
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException(String str, String str2) {
        super(str);
        this.SQLState = str2;
        this.vendorCode = 0;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
            DriverManager.println("SQLException: SQLState(" + str2 + ")");
        }
    }

    public SQLException(String str) {
        super(str);
        this.SQLState = null;
        this.vendorCode = 0;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException() {
        this.SQLState = null;
        this.vendorCode = 0;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException(Throwable th) {
        super(th);
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException(String str, Throwable th) {
        super(str, th);
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException(String str, String str2, Throwable th) {
        super(str, th);
        this.SQLState = str2;
        this.vendorCode = 0;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
            DriverManager.println("SQLState(" + this.SQLState + ")");
        }
    }

    public SQLException(String str, String str2, int i2, Throwable th) {
        super(str, th);
        this.SQLState = str2;
        this.vendorCode = i2;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            DriverManager.println("SQLState(" + this.SQLState + ") vendor code(" + i2 + ")");
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public String getSQLState() {
        return this.SQLState;
    }

    public int getErrorCode() {
        return this.vendorCode;
    }

    public SQLException getNextException() {
        return this.next;
    }

    public void setNextException(SQLException sQLException) {
        SQLException sQLException2 = this;
        while (true) {
            SQLException sQLException3 = sQLException2;
            SQLException sQLException4 = sQLException3.next;
            if (sQLException4 != null) {
                sQLException2 = sQLException4;
            } else if (nextUpdater.compareAndSet(sQLException3, null, sQLException)) {
                return;
            } else {
                sQLException2 = sQLException3.next;
            }
        }
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<Throwable> iterator() {
        return new Iterator<Throwable>() { // from class: java.sql.SQLException.1
            SQLException firstException;
            SQLException nextException;
            Throwable cause;

            {
                this.firstException = SQLException.this;
                this.nextException = this.firstException.getNextException();
                this.cause = this.firstException.getCause();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.firstException != null || this.nextException != null || this.cause != null) {
                    return true;
                }
                return false;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Throwable] */
            @Override // java.util.Iterator
            public Throwable next() {
                SQLException sQLException;
                if (this.firstException != null) {
                    sQLException = this.firstException;
                    this.firstException = null;
                } else if (this.cause != null) {
                    sQLException = this.cause;
                    this.cause = this.cause.getCause();
                } else if (this.nextException != null) {
                    sQLException = this.nextException;
                    this.cause = this.nextException.getCause();
                    this.nextException = this.nextException.getNextException();
                } else {
                    throw new NoSuchElementException();
                }
                return sQLException;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
