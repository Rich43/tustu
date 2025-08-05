package com.sun.corba.se.impl.naming.pcosnaming;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.orb.ORB;
import java.io.File;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAPackage.WrongAdapter;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.RequestProcessingPolicyValue;
import org.omg.PortableServer.ServantRetentionPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/pcosnaming/NameService.class */
public class NameService {
    private NamingContext rootContext;
    private POA nsPOA;
    private ServantManagerImpl contextMgr;
    private ORB theorb;

    public NameService(ORB orb, File file) throws Exception {
        this.rootContext = null;
        this.nsPOA = null;
        this.theorb = orb;
        POA poa = (POA) orb.resolve_initial_references(ORBConstants.ROOT_POA_NAME);
        poa.the_POAManager().activate();
        Policy[] policyArr = new Policy[4];
        int i2 = 0 + 1;
        policyArr[0] = poa.create_lifespan_policy(LifespanPolicyValue.PERSISTENT);
        int i3 = i2 + 1;
        policyArr[i2] = poa.create_request_processing_policy(RequestProcessingPolicyValue.USE_SERVANT_MANAGER);
        int i4 = i3 + 1;
        policyArr[i3] = poa.create_id_assignment_policy(IdAssignmentPolicyValue.USER_ID);
        int i5 = i4 + 1;
        policyArr[i4] = poa.create_servant_retention_policy(ServantRetentionPolicyValue.NON_RETAIN);
        this.nsPOA = poa.create_POA(ORBConstants.PERSISTENT_NAME_SERVICE_NAME, null, policyArr);
        this.nsPOA.the_POAManager().activate();
        this.contextMgr = new ServantManagerImpl(orb, file, this);
        ServantManagerImpl servantManagerImpl = this.contextMgr;
        String rootObjectKey = ServantManagerImpl.getRootObjectKey();
        NamingContextImpl namingContextImplAddContext = this.contextMgr.addContext(rootObjectKey, new NamingContextImpl(orb, rootObjectKey, this, this.contextMgr));
        namingContextImplAddContext.setServantManagerImpl(this.contextMgr);
        namingContextImplAddContext.setORB(orb);
        namingContextImplAddContext.setRootNameService(this);
        this.nsPOA.set_servant_manager(this.contextMgr);
        this.rootContext = NamingContextHelper.narrow(this.nsPOA.create_reference_with_id(rootObjectKey.getBytes(), NamingContextHelper.id()));
    }

    public NamingContext initialNamingContext() {
        return this.rootContext;
    }

    POA getNSPOA() {
        return this.nsPOA;
    }

    public NamingContext NewContext() throws SystemException {
        try {
            String newObjectKey = this.contextMgr.getNewObjectKey();
            NamingContextImpl namingContextImpl = new NamingContextImpl(this.theorb, newObjectKey, this, this.contextMgr);
            NamingContextImpl namingContextImplAddContext = this.contextMgr.addContext(newObjectKey, namingContextImpl);
            if (namingContextImplAddContext != null) {
                namingContextImpl = namingContextImplAddContext;
            }
            namingContextImpl.setServantManagerImpl(this.contextMgr);
            namingContextImpl.setORB(this.theorb);
            namingContextImpl.setRootNameService(this);
            return NamingContextHelper.narrow(this.nsPOA.create_reference_with_id(newObjectKey.getBytes(), NamingContextHelper.id()));
        } catch (SystemException e2) {
            throw e2;
        } catch (Exception e3) {
            return null;
        }
    }

    Object getObjectReferenceFromKey(String str) {
        Object objectCreate_reference_with_id;
        try {
            objectCreate_reference_with_id = this.nsPOA.create_reference_with_id(str.getBytes(), NamingContextHelper.id());
        } catch (Exception e2) {
            objectCreate_reference_with_id = null;
        }
        return objectCreate_reference_with_id;
    }

    String getObjectKey(Object object) {
        try {
            return new String(this.nsPOA.reference_to_id(object));
        } catch (WrongAdapter e2) {
            return null;
        } catch (WrongPolicy e3) {
            return null;
        } catch (Exception e4) {
            return null;
        }
    }
}
