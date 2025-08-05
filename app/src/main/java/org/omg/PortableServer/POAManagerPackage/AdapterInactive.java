package org.omg.PortableServer.POAManagerPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAManagerPackage/AdapterInactive.class */
public final class AdapterInactive extends UserException {
    public AdapterInactive() {
        super(AdapterInactiveHelper.id());
    }

    public AdapterInactive(String str) {
        super(AdapterInactiveHelper.id() + Constants.INDENT + str);
    }
}
