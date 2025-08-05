package org.omg.CORBA.portable;

import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:org/omg/CORBA/portable/UnknownException.class */
public class UnknownException extends SystemException {
    public Throwable originalEx;

    public UnknownException(Throwable th) {
        super("", 0, CompletionStatus.COMPLETED_MAYBE);
        this.originalEx = th;
    }
}
