package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/AlreadyBound.class */
public final class AlreadyBound extends UserException {
    public AlreadyBound() {
        super(AlreadyBoundHelper.id());
    }

    public AlreadyBound(String str) {
        super(AlreadyBoundHelper.id() + Constants.INDENT + str);
    }
}
