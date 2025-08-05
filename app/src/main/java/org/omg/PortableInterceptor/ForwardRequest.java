package org.omg.PortableInterceptor;

import org.omg.CORBA.Object;
import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ForwardRequest.class */
public final class ForwardRequest extends UserException {
    public Object forward;

    public ForwardRequest() {
        super(ForwardRequestHelper.id());
        this.forward = null;
    }

    public ForwardRequest(Object object) {
        super(ForwardRequestHelper.id());
        this.forward = null;
        this.forward = object;
    }

    public ForwardRequest(String str, Object object) {
        super(ForwardRequestHelper.id() + Constants.INDENT + str);
        this.forward = null;
        this.forward = object;
    }
}
