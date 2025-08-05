package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Enumeration;
import java.util.Vector;
import org.omg.CORBA.BAD_PARAM;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/ServiceContextRegistry.class */
public class ServiceContextRegistry {
    private ORB orb;
    private Vector scCollection = new Vector();

    private void dprint(String str) {
        ORBUtility.dprint(this, str);
    }

    public ServiceContextRegistry(ORB orb) {
        this.orb = orb;
    }

    public void register(Class cls) {
        if (ORB.ORBInitDebug) {
            dprint("Registering service context class " + ((Object) cls));
        }
        ServiceContextData serviceContextData = new ServiceContextData(cls);
        if (findServiceContextData(serviceContextData.getId()) == null) {
            this.scCollection.addElement(serviceContextData);
            return;
        }
        throw new BAD_PARAM("Tried to register duplicate service context");
    }

    public ServiceContextData findServiceContextData(int i2) {
        if (ORB.ORBInitDebug) {
            dprint("Searching registry for service context id " + i2);
        }
        Enumeration enumerationElements = this.scCollection.elements();
        while (enumerationElements.hasMoreElements()) {
            ServiceContextData serviceContextData = (ServiceContextData) enumerationElements.nextElement2();
            if (serviceContextData.getId() == i2) {
                if (ORB.ORBInitDebug) {
                    dprint("Service context data found: " + ((Object) serviceContextData));
                }
                return serviceContextData;
            }
        }
        if (ORB.ORBInitDebug) {
            dprint("Service context data not found");
            return null;
        }
        return null;
    }
}
