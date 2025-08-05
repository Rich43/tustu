package com.sun.corba.se.impl.naming.cosnaming;

import com.sun.corba.se.impl.logging.NamingSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.SystemException;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.ServantRetentionPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/TransientNameService.class */
public class TransientNameService {
    private Object theInitialNamingContext;

    public TransientNameService(ORB orb) throws INITIALIZE {
        initialize(orb, ORBConstants.PERSISTENT_NAME_SERVICE_NAME);
    }

    public TransientNameService(ORB orb, String str) throws INITIALIZE {
        initialize(orb, str);
    }

    private void initialize(ORB orb, String str) throws INITIALIZE {
        NamingSystemException namingSystemException = NamingSystemException.get(orb, CORBALogDomains.NAMING);
        try {
            POA poa = (POA) orb.resolve_initial_references(ORBConstants.ROOT_POA_NAME);
            poa.the_POAManager().activate();
            Policy[] policyArr = new Policy[3];
            int i2 = 0 + 1;
            policyArr[0] = poa.create_lifespan_policy(LifespanPolicyValue.TRANSIENT);
            int i3 = i2 + 1;
            policyArr[i2] = poa.create_id_assignment_policy(IdAssignmentPolicyValue.SYSTEM_ID);
            int i4 = i3 + 1;
            policyArr[i3] = poa.create_servant_retention_policy(ServantRetentionPolicyValue.RETAIN);
            POA poaCreate_POA = poa.create_POA(ORBConstants.TRANSIENT_NAME_SERVICE_NAME, null, policyArr);
            poaCreate_POA.the_POAManager().activate();
            TransientNamingContext transientNamingContext = new TransientNamingContext(orb, null, poaCreate_POA);
            transientNamingContext.localRoot = poaCreate_POA.id_to_reference(poaCreate_POA.activate_object(transientNamingContext));
            this.theInitialNamingContext = transientNamingContext.localRoot;
            orb.register_initial_reference(str, this.theInitialNamingContext);
        } catch (SystemException e2) {
            throw namingSystemException.transNsCannotCreateInitialNcSys(e2);
        } catch (Exception e3) {
            throw namingSystemException.transNsCannotCreateInitialNc(e3);
        }
    }

    public Object initialNamingContext() {
        return this.theInitialNamingContext;
    }
}
