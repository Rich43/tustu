package org.omg.PortableServer;

import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAManagerPackage.State;

/* loaded from: rt.jar:org/omg/PortableServer/POAManagerOperations.class */
public interface POAManagerOperations {
    void activate() throws AdapterInactive;

    void hold_requests(boolean z2) throws AdapterInactive;

    void discard_requests(boolean z2) throws AdapterInactive;

    void deactivate(boolean z2, boolean z3) throws AdapterInactive;

    State get_state();
}
