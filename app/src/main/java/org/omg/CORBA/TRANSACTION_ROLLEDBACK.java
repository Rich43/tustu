package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/TRANSACTION_ROLLEDBACK.class */
public final class TRANSACTION_ROLLEDBACK extends SystemException {
    public TRANSACTION_ROLLEDBACK() {
        this("");
    }

    public TRANSACTION_ROLLEDBACK(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public TRANSACTION_ROLLEDBACK(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public TRANSACTION_ROLLEDBACK(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
