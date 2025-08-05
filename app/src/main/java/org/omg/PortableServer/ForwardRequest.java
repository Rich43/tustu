package org.omg.PortableServer;

import org.omg.CORBA.Object;
import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/ForwardRequest.class */
public final class ForwardRequest extends UserException {
    public Object forward_reference;

    public ForwardRequest() {
        super(ForwardRequestHelper.id());
        this.forward_reference = null;
    }

    public ForwardRequest(Object object) {
        super(ForwardRequestHelper.id());
        this.forward_reference = null;
        this.forward_reference = object;
    }

    public ForwardRequest(String str, Object object) {
        super(ForwardRequestHelper.id() + Constants.INDENT + str);
        this.forward_reference = null;
        this.forward_reference = object;
    }
}
