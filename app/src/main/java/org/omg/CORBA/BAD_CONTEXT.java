package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/BAD_CONTEXT.class */
public final class BAD_CONTEXT extends SystemException {
    public BAD_CONTEXT() {
        this("");
    }

    public BAD_CONTEXT(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public BAD_CONTEXT(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public BAD_CONTEXT(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
