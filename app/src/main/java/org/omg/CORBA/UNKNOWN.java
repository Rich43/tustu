package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/UNKNOWN.class */
public final class UNKNOWN extends SystemException {
    public UNKNOWN() {
        this("");
    }

    public UNKNOWN(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public UNKNOWN(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public UNKNOWN(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
