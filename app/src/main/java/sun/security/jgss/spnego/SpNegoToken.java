package sun.security.jgss.spnego;

import java.io.IOException;
import org.ietf.jgss.GSSException;
import sun.security.jgss.GSSToken;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/jgss/spnego/SpNegoToken.class */
abstract class SpNegoToken extends GSSToken {
    static final int NEG_TOKEN_INIT_ID = 0;
    static final int NEG_TOKEN_TARG_ID = 1;
    private int tokenType;
    static final boolean DEBUG = SpNegoContext.DEBUG;
    public static ObjectIdentifier OID;

    /* loaded from: rt.jar:sun/security/jgss/spnego/SpNegoToken$NegoResult.class */
    enum NegoResult {
        ACCEPT_COMPLETE,
        ACCEPT_INCOMPLETE,
        REJECT
    }

    abstract byte[] encode() throws GSSException;

    static {
        try {
            OID = new ObjectIdentifier(SpNegoMechFactory.GSS_SPNEGO_MECH_OID.toString());
        } catch (IOException e2) {
        }
    }

    protected SpNegoToken(int i2) {
        this.tokenType = i2;
    }

    byte[] getEncoded() throws IOException, GSSException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.write(encode());
        switch (this.tokenType) {
            case 0:
                DerOutputStream derOutputStream2 = new DerOutputStream();
                derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream);
                return derOutputStream2.toByteArray();
            case 1:
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream);
                return derOutputStream3.toByteArray();
            default:
                return derOutputStream.toByteArray();
        }
    }

    final int getType() {
        return this.tokenType;
    }

    static String getTokenName(int i2) {
        switch (i2) {
            case 0:
                return "SPNEGO NegTokenInit";
            case 1:
                return "SPNEGO NegTokenTarg";
            default:
                return "SPNEGO Mechanism Token";
        }
    }

    static NegoResult getNegoResultType(int i2) {
        switch (i2) {
            case 0:
                return NegoResult.ACCEPT_COMPLETE;
            case 1:
                return NegoResult.ACCEPT_INCOMPLETE;
            case 2:
                return NegoResult.REJECT;
            default:
                return NegoResult.ACCEPT_COMPLETE;
        }
    }

    static String getNegoResultString(int i2) {
        switch (i2) {
            case 0:
                return "Accept Complete";
            case 1:
                return "Accept InComplete";
            case 2:
                return "Reject";
            default:
                return "Unknown Negotiated Result: " + i2;
        }
    }

    static int checkNextField(int i2, int i3) throws GSSException {
        if (i2 < i3) {
            return i3;
        }
        throw new GSSException(10, -1, "Invalid SpNegoToken token : wrong order");
    }
}
