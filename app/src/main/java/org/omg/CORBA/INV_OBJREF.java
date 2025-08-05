package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/INV_OBJREF.class */
public final class INV_OBJREF extends SystemException {
    public INV_OBJREF() {
        this("");
    }

    public INV_OBJREF(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public INV_OBJREF(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public INV_OBJREF(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
