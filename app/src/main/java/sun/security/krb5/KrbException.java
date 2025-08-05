package sun.security.krb5;

import sun.security.krb5.internal.KRBError;
import sun.security.krb5.internal.Krb5;

/* loaded from: rt.jar:sun/security/krb5/KrbException.class */
public class KrbException extends Exception {
    private static final long serialVersionUID = -4993302876451928596L;
    private int returnCode;
    private KRBError error;

    public KrbException(String str) {
        super(str);
    }

    public KrbException(Throwable th) {
        super(th);
    }

    public KrbException(int i2) {
        this.returnCode = i2;
    }

    public KrbException(int i2, String str) {
        this(str);
        this.returnCode = i2;
    }

    public KrbException(KRBError kRBError) {
        this.returnCode = kRBError.getErrorCode();
        this.error = kRBError;
    }

    public KrbException(KRBError kRBError, String str) {
        this(str);
        this.returnCode = kRBError.getErrorCode();
        this.error = kRBError;
    }

    public KRBError getError() {
        return this.error;
    }

    public int returnCode() {
        return this.returnCode;
    }

    public String returnCodeSymbol() {
        return returnCodeSymbol(this.returnCode);
    }

    public static String returnCodeSymbol(int i2) {
        return "not yet implemented";
    }

    public String returnCodeMessage() {
        return Krb5.getErrorMessage(this.returnCode);
    }

    public static String errorMessage(int i2) {
        return Krb5.getErrorMessage(i2);
    }

    public String krbErrorMessage() {
        StringBuffer stringBuffer = new StringBuffer("krb_error " + this.returnCode);
        String message = getMessage();
        if (message != null) {
            stringBuffer.append(" ");
            stringBuffer.append(message);
        }
        return stringBuffer.toString();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuffer stringBuffer = new StringBuffer();
        int iReturnCode = returnCode();
        if (iReturnCode != 0) {
            stringBuffer.append(returnCodeMessage());
            stringBuffer.append(" (").append(returnCode()).append(')');
        }
        String message = super.getMessage();
        if (message != null && message.length() != 0) {
            if (iReturnCode != 0) {
                stringBuffer.append(" - ");
            }
            stringBuffer.append(message);
        }
        return stringBuffer.toString();
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "KrbException: " + getMessage();
    }

    public int hashCode() {
        int iHashCode = (37 * 17) + this.returnCode;
        if (this.error != null) {
            iHashCode = (37 * iHashCode) + this.error.hashCode();
        }
        return iHashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KrbException)) {
            return false;
        }
        KrbException krbException = (KrbException) obj;
        if (this.returnCode != krbException.returnCode) {
            return false;
        }
        if (this.error == null) {
            return krbException.error == null;
        }
        return this.error.equals(krbException.error);
    }
}
