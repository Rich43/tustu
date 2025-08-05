package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/TRANSACTION_REQUIRED.class */
public final class TRANSACTION_REQUIRED extends SystemException {
    public TRANSACTION_REQUIRED() {
        this("");
    }

    public TRANSACTION_REQUIRED(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public TRANSACTION_REQUIRED(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public TRANSACTION_REQUIRED(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
