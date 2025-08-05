package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/NotEmpty.class */
public final class NotEmpty extends UserException {
    public NotEmpty() {
        super(NotEmptyHelper.id());
    }

    public NotEmpty(String str) {
        super(NotEmptyHelper.id() + Constants.INDENT + str);
    }
}
