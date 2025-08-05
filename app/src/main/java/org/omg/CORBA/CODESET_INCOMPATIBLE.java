package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/CODESET_INCOMPATIBLE.class */
public final class CODESET_INCOMPATIBLE extends SystemException {
    public CODESET_INCOMPATIBLE() {
        this("");
    }

    public CODESET_INCOMPATIBLE(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public CODESET_INCOMPATIBLE(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public CODESET_INCOMPATIBLE(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
