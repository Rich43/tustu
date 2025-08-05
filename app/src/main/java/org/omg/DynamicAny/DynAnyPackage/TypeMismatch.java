package org.omg.DynamicAny.DynAnyPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/DynamicAny/DynAnyPackage/TypeMismatch.class */
public final class TypeMismatch extends UserException {
    public TypeMismatch() {
        super(TypeMismatchHelper.id());
    }

    public TypeMismatch(String str) {
        super(TypeMismatchHelper.id() + Constants.INDENT + str);
    }
}
