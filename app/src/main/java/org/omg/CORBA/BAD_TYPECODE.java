package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/BAD_TYPECODE.class */
public final class BAD_TYPECODE extends SystemException {
    public BAD_TYPECODE() {
        this("");
    }

    public BAD_TYPECODE(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public BAD_TYPECODE(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public BAD_TYPECODE(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
