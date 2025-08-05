package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/BAD_INV_ORDER.class */
public final class BAD_INV_ORDER extends SystemException {
    public BAD_INV_ORDER() {
        this("");
    }

    public BAD_INV_ORDER(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public BAD_INV_ORDER(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public BAD_INV_ORDER(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
