package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/IMP_LIMIT.class */
public final class IMP_LIMIT extends SystemException {
    public IMP_LIMIT() {
        this("");
    }

    public IMP_LIMIT(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public IMP_LIMIT(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public IMP_LIMIT(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
