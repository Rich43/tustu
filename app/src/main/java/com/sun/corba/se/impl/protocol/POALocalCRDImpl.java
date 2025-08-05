package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.logging.POASystemException;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.OADestroyed;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.ForwardException;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/POALocalCRDImpl.class */
public class POALocalCRDImpl extends LocalClientRequestDispatcherBase {
    private ORBUtilSystemException wrapper;
    private POASystemException poaWrapper;

    public POALocalCRDImpl(ORB orb, int i2, IOR ior) {
        super(orb, i2, ior);
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.poaWrapper = POASystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    private OAInvocationInfo servantEnter(ObjectAdapter objectAdapter) throws OADestroyed {
        objectAdapter.enter();
        OAInvocationInfo oAInvocationInfoMakeInvocationInfo = objectAdapter.makeInvocationInfo(this.objectId);
        this.orb.pushInvocationInfo(oAInvocationInfoMakeInvocationInfo);
        return oAInvocationInfoMakeInvocationInfo;
    }

    private void servantExit(ObjectAdapter objectAdapter) {
        try {
            objectAdapter.returnServant();
        } finally {
            objectAdapter.exit();
            this.orb.popInvocationInfo();
        }
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public ServantObject servant_preinvoke(Object object, String str, Class cls) {
        ObjectAdapter objectAdapterFind = this.oaf.find(this.oaid);
        try {
            OAInvocationInfo oAInvocationInfoServantEnter = servantEnter(objectAdapterFind);
            oAInvocationInfoServantEnter.setOperation(str);
            try {
                try {
                    objectAdapterFind.getInvocationServant(oAInvocationInfoServantEnter);
                    if (!checkForCompatibleServant(oAInvocationInfoServantEnter, cls)) {
                        return null;
                    }
                    if (!checkForCompatibleServant(oAInvocationInfoServantEnter, cls)) {
                        servantExit(objectAdapterFind);
                        return null;
                    }
                    return oAInvocationInfoServantEnter;
                } catch (ForwardException e2) {
                    RuntimeException runtimeException = new RuntimeException("deal with this.");
                    runtimeException.initCause(e2);
                    throw runtimeException;
                } catch (ThreadDeath e3) {
                    throw this.wrapper.runtimeexception(e3);
                } catch (Throwable th) {
                    if (th instanceof SystemException) {
                        throw ((SystemException) th);
                    }
                    throw this.poaWrapper.localServantLookup(th);
                }
            } finally {
            }
        } catch (OADestroyed e4) {
            return servant_preinvoke(object, str, cls);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher
    public void servant_postinvoke(Object object, ServantObject servantObject) {
        servantExit(this.orb.peekInvocationInfo().oa());
    }
}
