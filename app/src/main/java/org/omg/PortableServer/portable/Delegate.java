package org.omg.PortableServer.portable;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

/* loaded from: rt.jar:org/omg/PortableServer/portable/Delegate.class */
public interface Delegate {
    ORB orb(Servant servant);

    Object this_object(Servant servant);

    POA poa(Servant servant);

    byte[] object_id(Servant servant);

    POA default_POA(Servant servant);

    boolean is_a(Servant servant, String str);

    boolean non_existent(Servant servant);

    Object get_interface_def(Servant servant);
}
