package org.ietf.jgss;

/* loaded from: rt.jar:org/ietf/jgss/GSSException.class */
public class GSSException extends Exception {
    private static final long serialVersionUID = -2706218945227726672L;
    public static final int BAD_BINDINGS = 1;
    public static final int BAD_MECH = 2;
    public static final int BAD_NAME = 3;
    public static final int BAD_NAMETYPE = 4;
    public static final int BAD_STATUS = 5;
    public static final int BAD_MIC = 6;
    public static final int CONTEXT_EXPIRED = 7;
    public static final int CREDENTIALS_EXPIRED = 8;
    public static final int DEFECTIVE_CREDENTIAL = 9;
    public static final int DEFECTIVE_TOKEN = 10;
    public static final int FAILURE = 11;
    public static final int NO_CONTEXT = 12;
    public static final int NO_CRED = 13;
    public static final int BAD_QOP = 14;
    public static final int UNAUTHORIZED = 15;
    public static final int UNAVAILABLE = 16;
    public static final int DUPLICATE_ELEMENT = 17;
    public static final int NAME_NOT_MN = 18;
    public static final int DUPLICATE_TOKEN = 19;
    public static final int OLD_TOKEN = 20;
    public static final int UNSEQ_TOKEN = 21;
    public static final int GAP_TOKEN = 22;
    private static String[] messages = {"Channel binding mismatch", "Unsupported mechanism requested", "Invalid name provided", "Name of unsupported type provided", "Invalid input status selector", "Token had invalid integrity check", "Specified security context expired", "Expired credentials detected", "Defective credential detected", "Defective token detected", "Failure unspecified at GSS-API level", "Security context init/accept not yet called or context deleted", "No valid credentials provided", "Unsupported QOP value", "Operation unauthorized", "Operation unavailable", "Duplicate credential element requested", "Name contains multi-mechanism elements", "The token was a duplicate of an earlier token", "The token's validity period has expired", "A later token has already been processed", "An expected per-message token was not received"};
    private int major;
    private int minor;
    private String minorMessage;
    private String majorString;

    public GSSException(int i2) {
        this.minor = 0;
        this.minorMessage = null;
        this.majorString = null;
        if (validateMajor(i2)) {
            this.major = i2;
        } else {
            this.major = 11;
        }
    }

    GSSException(int i2, String str) {
        this.minor = 0;
        this.minorMessage = null;
        this.majorString = null;
        if (validateMajor(i2)) {
            this.major = i2;
        } else {
            this.major = 11;
        }
        this.majorString = str;
    }

    public GSSException(int i2, int i3, String str) {
        this.minor = 0;
        this.minorMessage = null;
        this.majorString = null;
        if (validateMajor(i2)) {
            this.major = i2;
        } else {
            this.major = 11;
        }
        this.minor = i3;
        this.minorMessage = str;
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public String getMajorString() {
        if (this.majorString != null) {
            return this.majorString;
        }
        return messages[this.major - 1];
    }

    public String getMinorString() {
        return this.minorMessage;
    }

    public void setMinor(int i2, String str) {
        this.minor = i2;
        this.minorMessage = str;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "GSSException: " + getMessage();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.minor == 0) {
            return getMajorString();
        }
        return getMajorString() + " (Mechanism level: " + getMinorString() + ")";
    }

    private boolean validateMajor(int i2) {
        if (i2 > 0 && i2 <= messages.length) {
            return true;
        }
        return false;
    }
}
