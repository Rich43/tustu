package javax.naming;

import java.util.Hashtable;

/* loaded from: rt.jar:javax/naming/CannotProceedException.class */
public class CannotProceedException extends NamingException {
    protected Name remainingNewName;
    protected Hashtable<?, ?> environment;
    protected Name altName;
    protected Context altNameCtx;
    private static final long serialVersionUID = 1219724816191576813L;

    public CannotProceedException(String str) {
        super(str);
        this.remainingNewName = null;
        this.environment = null;
        this.altName = null;
        this.altNameCtx = null;
    }

    public CannotProceedException() {
        this.remainingNewName = null;
        this.environment = null;
        this.altName = null;
        this.altNameCtx = null;
    }

    public Hashtable<?, ?> getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(Hashtable<?, ?> hashtable) {
        this.environment = hashtable;
    }

    public Name getRemainingNewName() {
        return this.remainingNewName;
    }

    public void setRemainingNewName(Name name) {
        if (name != null) {
            this.remainingNewName = (Name) name.clone();
        } else {
            this.remainingNewName = null;
        }
    }

    public Name getAltName() {
        return this.altName;
    }

    public void setAltName(Name name) {
        this.altName = name;
    }

    public Context getAltNameCtx() {
        return this.altNameCtx;
    }

    public void setAltNameCtx(Context context) {
        this.altNameCtx = context;
    }
}
