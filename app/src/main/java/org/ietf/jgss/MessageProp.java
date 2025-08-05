package org.ietf.jgss;

/* loaded from: rt.jar:org/ietf/jgss/MessageProp.class */
public class MessageProp {
    private boolean privacyState;
    private int qop;
    private boolean dupToken;
    private boolean oldToken;
    private boolean unseqToken;
    private boolean gapToken;
    private int minorStatus;
    private String minorString;

    public MessageProp(boolean z2) {
        this(0, z2);
    }

    public MessageProp(int i2, boolean z2) {
        this.qop = i2;
        this.privacyState = z2;
        resetStatusValues();
    }

    public int getQOP() {
        return this.qop;
    }

    public boolean getPrivacy() {
        return this.privacyState;
    }

    public void setQOP(int i2) {
        this.qop = i2;
    }

    public void setPrivacy(boolean z2) {
        this.privacyState = z2;
    }

    public boolean isDuplicateToken() {
        return this.dupToken;
    }

    public boolean isOldToken() {
        return this.oldToken;
    }

    public boolean isUnseqToken() {
        return this.unseqToken;
    }

    public boolean isGapToken() {
        return this.gapToken;
    }

    public int getMinorStatus() {
        return this.minorStatus;
    }

    public String getMinorString() {
        return this.minorString;
    }

    public void setSupplementaryStates(boolean z2, boolean z3, boolean z4, boolean z5, int i2, String str) {
        this.dupToken = z2;
        this.oldToken = z3;
        this.unseqToken = z4;
        this.gapToken = z5;
        this.minorStatus = i2;
        this.minorString = str;
    }

    private void resetStatusValues() {
        this.dupToken = false;
        this.oldToken = false;
        this.unseqToken = false;
        this.gapToken = false;
        this.minorStatus = 0;
        this.minorString = null;
    }
}
