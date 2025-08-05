package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/TRANSACTION_MODE.class */
public final class TRANSACTION_MODE extends SystemException {
    public TRANSACTION_MODE() {
        this("");
    }

    public TRANSACTION_MODE(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public TRANSACTION_MODE(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public TRANSACTION_MODE(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
