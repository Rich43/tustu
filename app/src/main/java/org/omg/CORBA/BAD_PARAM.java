package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/BAD_PARAM.class */
public final class BAD_PARAM extends SystemException {
    public BAD_PARAM() {
        this("");
    }

    public BAD_PARAM(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public BAD_PARAM(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public BAD_PARAM(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
