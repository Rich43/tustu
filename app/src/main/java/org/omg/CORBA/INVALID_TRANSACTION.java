package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/INVALID_TRANSACTION.class */
public final class INVALID_TRANSACTION extends SystemException {
    public INVALID_TRANSACTION() {
        this("");
    }

    public INVALID_TRANSACTION(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public INVALID_TRANSACTION(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public INVALID_TRANSACTION(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
