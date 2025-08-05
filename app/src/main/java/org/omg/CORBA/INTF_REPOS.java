package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/INTF_REPOS.class */
public final class INTF_REPOS extends SystemException {
    public INTF_REPOS() {
        this("");
    }

    public INTF_REPOS(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public INTF_REPOS(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public INTF_REPOS(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
