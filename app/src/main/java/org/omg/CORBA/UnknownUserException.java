package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/UnknownUserException.class */
public final class UnknownUserException extends UserException {
    public Any except;

    public UnknownUserException() {
    }

    public UnknownUserException(Any any) {
        this.except = any;
    }
}
