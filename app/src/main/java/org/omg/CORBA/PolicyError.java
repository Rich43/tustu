package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/PolicyError.class */
public final class PolicyError extends UserException {
    public short reason;

    public PolicyError() {
    }

    public PolicyError(short s2) {
        this.reason = s2;
    }

    public PolicyError(String str, short s2) {
        super(str);
        this.reason = s2;
    }
}
