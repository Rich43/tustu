package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.logging.POASystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.util.EmptyStackException;
import org.omg.CORBA.Object;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.portable.Delegate;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/DelegateImpl.class */
public class DelegateImpl implements Delegate {
    private ORB orb;
    private POASystemException wrapper;
    private POAFactory factory;

    public DelegateImpl(ORB orb, POAFactory pOAFactory) {
        this.orb = orb;
        this.wrapper = POASystemException.get(orb, CORBALogDomains.OA);
        this.factory = pOAFactory;
    }

    @Override // org.omg.PortableServer.portable.Delegate
    public org.omg.CORBA.ORB orb(Servant servant) {
        return this.orb;
    }

    @Override // org.omg.PortableServer.portable.Delegate
    public Object this_object(Servant servant) {
        try {
            byte[] bArrId = this.orb.peekInvocationInfo().id();
            POA poa = (POA) this.orb.peekInvocationInfo().oa();
            return poa.create_reference_with_id(bArrId, servant._all_interfaces(poa, bArrId)[0]);
        } catch (ClassCastException e2) {
            throw this.wrapper.defaultPoaNotPoaimpl(e2);
        } catch (EmptyStackException e3) {
            try {
                POAImpl pOAImpl = (POAImpl) servant._default_POA();
                try {
                    if (pOAImpl.getPolicies().isImplicitlyActivated() || (pOAImpl.getPolicies().isUniqueIds() && pOAImpl.getPolicies().retainServants())) {
                        return pOAImpl.servant_to_reference(servant);
                    }
                    throw this.wrapper.wrongPoliciesForThisObject();
                } catch (ServantNotActive e4) {
                    throw this.wrapper.thisObjectServantNotActive(e4);
                } catch (WrongPolicy e5) {
                    throw this.wrapper.thisObjectWrongPolicy(e5);
                }
            } catch (ClassCastException e6) {
                throw this.wrapper.defaultPoaNotPoaimpl(e6);
            }
        }
    }

    @Override // org.omg.PortableServer.portable.Delegate
    public POA poa(Servant servant) {
        try {
            return (POA) this.orb.peekInvocationInfo().oa();
        } catch (EmptyStackException e2) {
            POA poaLookupPOA = this.factory.lookupPOA(servant);
            if (poaLookupPOA != null) {
                return poaLookupPOA;
            }
            throw this.wrapper.noContext(e2);
        }
    }

    @Override // org.omg.PortableServer.portable.Delegate
    public byte[] object_id(Servant servant) {
        try {
            return this.orb.peekInvocationInfo().id();
        } catch (EmptyStackException e2) {
            throw this.wrapper.noContext(e2);
        }
    }

    @Override // org.omg.PortableServer.portable.Delegate
    public POA default_POA(Servant servant) {
        return this.factory.getRootPOA();
    }

    @Override // org.omg.PortableServer.portable.Delegate
    public boolean is_a(Servant servant, String str) {
        for (String str2 : servant._all_interfaces(poa(servant), object_id(servant))) {
            if (str.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.omg.PortableServer.portable.Delegate
    public boolean non_existent(Servant servant) {
        try {
            return this.orb.peekInvocationInfo().id() == null;
        } catch (EmptyStackException e2) {
            throw this.wrapper.noContext(e2);
        }
    }

    @Override // org.omg.PortableServer.portable.Delegate
    public Object get_interface_def(Servant servant) {
        throw this.wrapper.methodNotImplemented();
    }
}
