package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/COMM_FAILURE.class */
public final class COMM_FAILURE extends SystemException {
    public COMM_FAILURE() {
        this("");
    }

    public COMM_FAILURE(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public COMM_FAILURE(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public COMM_FAILURE(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
