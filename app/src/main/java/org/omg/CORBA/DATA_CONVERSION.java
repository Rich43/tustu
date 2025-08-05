package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/DATA_CONVERSION.class */
public final class DATA_CONVERSION extends SystemException {
    public DATA_CONVERSION() {
        this("");
    }

    public DATA_CONVERSION(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public DATA_CONVERSION(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public DATA_CONVERSION(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
