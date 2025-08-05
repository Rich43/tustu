package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/BAD_OPERATION.class */
public final class BAD_OPERATION extends SystemException {
    public BAD_OPERATION() {
        this("");
    }

    public BAD_OPERATION(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public BAD_OPERATION(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public BAD_OPERATION(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
