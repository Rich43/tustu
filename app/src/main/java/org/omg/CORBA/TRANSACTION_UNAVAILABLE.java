package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/TRANSACTION_UNAVAILABLE.class */
public final class TRANSACTION_UNAVAILABLE extends SystemException {
    public TRANSACTION_UNAVAILABLE() {
        this("");
    }

    public TRANSACTION_UNAVAILABLE(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public TRANSACTION_UNAVAILABLE(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public TRANSACTION_UNAVAILABLE(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
