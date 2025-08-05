package javax.xml.transform;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Objects;

/* loaded from: rt.jar:javax/xml/transform/TransformerException.class */
public class TransformerException extends Exception {
    private static final long serialVersionUID = 975798773772956428L;
    SourceLocator locator;
    Throwable containedException;

    public SourceLocator getLocator() {
        return this.locator;
    }

    public void setLocator(SourceLocator location) {
        this.locator = location;
    }

    public Throwable getException() {
        return this.containedException;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        if (this.containedException == this) {
            return null;
        }
        return this.containedException;
    }

    @Override // java.lang.Throwable
    public synchronized Throwable initCause(Throwable cause) {
        if (this.containedException != null) {
            throw new IllegalStateException("Can't overwrite cause");
        }
        if (cause == this) {
            throw new IllegalArgumentException("Self-causation not permitted");
        }
        this.containedException = cause;
        return this;
    }

    public TransformerException(String message) {
        this(message, null, null);
    }

    public TransformerException(Throwable e2) {
        this(null, null, e2);
    }

    public TransformerException(String message, Throwable e2) {
        this(message, null, e2);
    }

    public TransformerException(String message, SourceLocator locator) {
        this(message, locator, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public TransformerException(String message, SourceLocator locator, Throwable e2) {
        String string;
        if (message == null || message.length() == 0) {
            string = e2 == null ? "" : e2.toString();
        } else {
            string = message;
        }
        super(string);
        this.containedException = e2;
        this.locator = locator;
    }

    public String getMessageAndLocation() {
        return Objects.toString(super.getMessage(), "") + Objects.toString(getLocationAsString(), "");
    }

    public String getLocationAsString() {
        if (this.locator == null) {
            return null;
        }
        if (System.getSecurityManager() == null) {
            return getLocationString();
        }
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.xml.transform.TransformerException.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return TransformerException.this.getLocationString();
            }
        }, new AccessControlContext(new ProtectionDomain[]{getNonPrivDomain()}));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLocationString() {
        if (this.locator == null) {
            return null;
        }
        StringBuilder sbuffer = new StringBuilder();
        String systemID = this.locator.getSystemId();
        int line = this.locator.getLineNumber();
        int column = this.locator.getColumnNumber();
        if (null != systemID) {
            sbuffer.append("; SystemID: ");
            sbuffer.append(systemID);
        }
        if (0 != line) {
            sbuffer.append("; Line#: ");
            sbuffer.append(line);
        }
        if (0 != column) {
            sbuffer.append("; Column#: ");
            sbuffer.append(column);
        }
        return sbuffer.toString();
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(new PrintWriter((OutputStream) System.err, true));
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s2) {
        printStackTrace(new PrintWriter(s2));
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter s2) {
        String locInfo;
        if (s2 == null) {
            s2 = new PrintWriter((OutputStream) System.err, true);
        }
        try {
            String locInfo2 = getLocationAsString();
            if (null != locInfo2) {
                s2.println(locInfo2);
            }
            super.printStackTrace(s2);
        } catch (Throwable th) {
        }
        Throwable exception = getException();
        for (int i2 = 0; i2 < 10 && null != exception; i2++) {
            s2.println("---------");
            try {
                if ((exception instanceof TransformerException) && null != (locInfo = ((TransformerException) exception).getLocationAsString())) {
                    s2.println(locInfo);
                }
                exception.printStackTrace(s2);
            } catch (Throwable th2) {
                s2.println("Could not print stack trace...");
            }
            try {
                Method meth = exception.getClass().getMethod("getException", (Class[]) null);
                if (null != meth) {
                    Throwable prev = exception;
                    exception = (Throwable) meth.invoke(exception, (Object[]) null);
                    if (prev == exception) {
                        break;
                    }
                } else {
                    exception = null;
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                exception = null;
            }
        }
        s2.flush();
    }

    private ProtectionDomain getNonPrivDomain() {
        CodeSource nullSource = new CodeSource((URL) null, (CodeSigner[]) null);
        PermissionCollection noPermission = new Permissions();
        return new ProtectionDomain(nullSource, noPermission);
    }
}
