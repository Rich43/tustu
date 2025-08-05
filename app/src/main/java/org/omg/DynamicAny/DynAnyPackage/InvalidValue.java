package org.omg.DynamicAny.DynAnyPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/DynamicAny/DynAnyPackage/InvalidValue.class */
public final class InvalidValue extends UserException {
    public InvalidValue() {
        super(InvalidValueHelper.id());
    }

    public InvalidValue(String str) {
        super(InvalidValueHelper.id() + Constants.INDENT + str);
    }
}
