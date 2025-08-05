package org.omg.PortableServer;

import org.omg.CORBA.ServerRequest;

/* loaded from: rt.jar:org/omg/PortableServer/DynamicImplementation.class */
public abstract class DynamicImplementation extends Servant {
    public abstract void invoke(ServerRequest serverRequest);
}
