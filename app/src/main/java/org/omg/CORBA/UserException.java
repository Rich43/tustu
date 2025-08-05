package org.omg.CORBA;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CORBA/UserException.class */
public abstract class UserException extends Exception implements IDLEntity {
    protected UserException() {
    }

    protected UserException(String str) {
        super(str);
    }
}
