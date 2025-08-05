package com.sun.security.sasl.gsskerb;

import com.sun.security.sasl.util.AbstractSaslImpl;
import java.util.Map;
import java.util.logging.Level;
import javax.security.sasl.SaslException;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

/* loaded from: rt.jar:com/sun/security/sasl/gsskerb/GssKrb5Base.class */
abstract class GssKrb5Base extends AbstractSaslImpl {
    private static final String KRB5_OID_STR = "1.2.840.113554.1.2.2";
    protected static Oid KRB5_OID;
    protected static final byte[] EMPTY = new byte[0];
    protected GSSContext secCtx;
    protected static final int JGSS_QOP = 0;

    static {
        try {
            KRB5_OID = new Oid(KRB5_OID_STR);
        } catch (GSSException e2) {
        }
    }

    protected GssKrb5Base(Map<String, ?> map, String str) throws SaslException {
        super(map, str);
        this.secCtx = null;
    }

    public String getMechanismName() {
        return "GSSAPI";
    }

    public byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (!this.completed) {
            throw new IllegalStateException("GSSAPI authentication not completed");
        }
        if (!this.integrity) {
            throw new IllegalStateException("No security layer negotiated");
        }
        try {
            MessageProp messageProp = new MessageProp(0, false);
            byte[] bArrUnwrap = this.secCtx.unwrap(bArr, i2, i3, messageProp);
            if (this.privacy && !messageProp.getPrivacy()) {
                throw new SaslException("Privacy not protected");
            }
            checkMessageProp("", messageProp);
            if (logger.isLoggable(Level.FINEST)) {
                traceOutput(this.myClassName, "KRB501:Unwrap", "incoming: ", bArr, i2, i3);
                traceOutput(this.myClassName, "KRB502:Unwrap", "unwrapped: ", bArrUnwrap, 0, bArrUnwrap.length);
            }
            return bArrUnwrap;
        } catch (GSSException e2) {
            throw new SaslException("Problems unwrapping SASL buffer", e2);
        }
    }

    public byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException {
        if (!this.completed) {
            throw new IllegalStateException("GSSAPI authentication not completed");
        }
        if (!this.integrity) {
            throw new IllegalStateException("No security layer negotiated");
        }
        try {
            byte[] bArrWrap = this.secCtx.wrap(bArr, i2, i3, new MessageProp(0, this.privacy));
            if (logger.isLoggable(Level.FINEST)) {
                traceOutput(this.myClassName, "KRB503:Wrap", "outgoing: ", bArr, i2, i3);
                traceOutput(this.myClassName, "KRB504:Wrap", "wrapped: ", bArrWrap, 0, bArrWrap.length);
            }
            return bArrWrap;
        } catch (GSSException e2) {
            throw new SaslException("Problem performing GSS wrap", e2);
        }
    }

    public void dispose() throws SaslException {
        if (this.secCtx != null) {
            try {
                this.secCtx.dispose();
                this.secCtx = null;
            } catch (GSSException e2) {
                throw new SaslException("Problem disposing GSS context", e2);
            }
        }
    }

    protected void finalize() throws Throwable {
        dispose();
    }

    void checkMessageProp(String str, MessageProp messageProp) throws SaslException {
        if (messageProp.isDuplicateToken()) {
            throw new SaslException(str + "Duplicate token");
        }
        if (messageProp.isGapToken()) {
            throw new SaslException(str + "Gap token");
        }
        if (messageProp.isOldToken()) {
            throw new SaslException(str + "Old token");
        }
        if (messageProp.isUnseqToken()) {
            throw new SaslException(str + "Token not in sequence");
        }
    }
}
