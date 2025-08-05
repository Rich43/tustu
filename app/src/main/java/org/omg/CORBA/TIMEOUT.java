package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/TIMEOUT.class */
public final class TIMEOUT extends SystemException {
    public TIMEOUT() {
        this("");
    }

    public TIMEOUT(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public TIMEOUT(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public TIMEOUT(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
