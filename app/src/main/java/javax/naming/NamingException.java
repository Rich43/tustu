package javax.naming;

import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/naming/NamingException.class */
public class NamingException extends Exception {
    protected Name resolvedName;
    protected Object resolvedObj;
    protected Name remainingName;
    protected Throwable rootException;
    private static final long serialVersionUID = -1299181962103167177L;

    public NamingException(String str) {
        super(str);
        this.rootException = null;
        this.remainingName = null;
        this.resolvedName = null;
        this.resolvedObj = null;
    }

    public NamingException() {
        this.rootException = null;
        this.remainingName = null;
        this.resolvedName = null;
        this.resolvedObj = null;
    }

    public Name getResolvedName() {
        return this.resolvedName;
    }

    public Name getRemainingName() {
        return this.remainingName;
    }

    public Object getResolvedObj() {
        return this.resolvedObj;
    }

    public String getExplanation() {
        return getMessage();
    }

    public void setResolvedName(Name name) {
        if (name != null) {
            this.resolvedName = (Name) name.clone();
        } else {
            this.resolvedName = null;
        }
    }

    public void setRemainingName(Name name) {
        if (name != null) {
            this.remainingName = (Name) name.clone();
        } else {
            this.remainingName = null;
        }
    }

    public void setResolvedObj(Object obj) {
        this.resolvedObj = obj;
    }

    public void appendRemainingComponent(String str) {
        if (str != null) {
            try {
                if (this.remainingName == null) {
                    this.remainingName = new CompositeName();
                }
                this.remainingName.add(str);
            } catch (NamingException e2) {
                throw new IllegalArgumentException(e2.toString());
            }
        }
    }

    public void appendRemainingName(Name name) {
        if (name == null) {
            return;
        }
        if (this.remainingName != null) {
            try {
                this.remainingName.addAll(name);
                return;
            } catch (NamingException e2) {
                throw new IllegalArgumentException(e2.toString());
            }
        }
        this.remainingName = (Name) name.clone();
    }

    public Throwable getRootCause() {
        return this.rootException;
    }

    public void setRootCause(Throwable th) {
        if (th != this) {
            this.rootException = th;
        }
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return getRootCause();
    }

    @Override // java.lang.Throwable
    public Throwable initCause(Throwable th) {
        super.initCause(th);
        setRootCause(th);
        return this;
    }

    @Override // java.lang.Throwable
    public String toString() {
        String string = super.toString();
        if (this.rootException != null) {
            string = string + " [Root exception is " + ((Object) this.rootException) + "]";
        }
        if (this.remainingName != null) {
            string = string + "; remaining name '" + ((Object) this.remainingName) + PdfOps.SINGLE_QUOTE_TOKEN;
        }
        return string;
    }

    public String toString(boolean z2) {
        if (!z2 || this.resolvedObj == null) {
            return toString();
        }
        return toString() + "; resolved object " + this.resolvedObj;
    }
}
