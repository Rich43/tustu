package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/WrongAdapter.class */
public final class WrongAdapter extends UserException {
    public WrongAdapter() {
        super(WrongAdapterHelper.id());
    }

    public WrongAdapter(String str) {
        super(WrongAdapterHelper.id() + Constants.INDENT + str);
    }
}
