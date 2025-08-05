package sun.security.jgss;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

/* loaded from: rt.jar:sun/security/jgss/GSSExceptionImpl.class */
public class GSSExceptionImpl extends GSSException {
    private static final long serialVersionUID = 4251197939069005575L;
    private String majorMessage;

    GSSExceptionImpl(int i2, Oid oid) {
        super(i2);
        this.majorMessage = super.getMajorString() + ": " + ((Object) oid);
    }

    public GSSExceptionImpl(int i2, String str) {
        super(i2);
        this.majorMessage = str;
    }

    public GSSExceptionImpl(int i2, Exception exc) {
        super(i2);
        initCause(exc);
    }

    public GSSExceptionImpl(int i2, String str, Exception exc) {
        this(i2, str);
        initCause(exc);
    }

    @Override // org.ietf.jgss.GSSException, java.lang.Throwable
    public String getMessage() {
        if (this.majorMessage != null) {
            return this.majorMessage;
        }
        return super.getMessage();
    }
}
