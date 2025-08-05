package org.omg.DynamicAny.DynAnyFactoryPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/DynamicAny/DynAnyFactoryPackage/InconsistentTypeCode.class */
public final class InconsistentTypeCode extends UserException {
    public InconsistentTypeCode() {
        super(InconsistentTypeCodeHelper.id());
    }

    public InconsistentTypeCode(String str) {
        super(InconsistentTypeCodeHelper.id() + Constants.INDENT + str);
    }
}
