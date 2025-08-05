package org.omg.CORBA;

import org.omg.CORBA.portable.ObjectImpl;

@Deprecated
/* loaded from: rt.jar:org/omg/CORBA/DynamicImplementation.class */
public class DynamicImplementation extends ObjectImpl {
    @Deprecated
    public void invoke(ServerRequest serverRequest) {
        throw new NO_IMPLEMENT();
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        throw new NO_IMPLEMENT();
    }
}
