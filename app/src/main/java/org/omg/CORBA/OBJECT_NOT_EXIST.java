package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/OBJECT_NOT_EXIST.class */
public final class OBJECT_NOT_EXIST extends SystemException {
    public OBJECT_NOT_EXIST() {
        this("");
    }

    public OBJECT_NOT_EXIST(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public OBJECT_NOT_EXIST(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public OBJECT_NOT_EXIST(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
