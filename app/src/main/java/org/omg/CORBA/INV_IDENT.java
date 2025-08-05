package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/INV_IDENT.class */
public final class INV_IDENT extends SystemException {
    public INV_IDENT() {
        this("");
    }

    public INV_IDENT(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public INV_IDENT(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public INV_IDENT(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
