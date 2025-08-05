package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.POASystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orbutil.closure.Closure;
import com.sun.corba.se.spi.orbutil.closure.ClosureFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.TRANSIENT;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManager;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.AdapterNonExistent;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.portable.Delegate;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAFactory.class */
public class POAFactory implements ObjectAdapterFactory {
    private POASystemException wrapper;
    private OMGSystemException omgWrapper;
    private Map exportedServantsToPOA = new WeakHashMap();
    private boolean isShuttingDown = false;
    private Set poaManagers = Collections.synchronizedSet(new HashSet(4));
    private int poaManagerId = 0;
    private int poaId = 0;
    private POAImpl rootPOA = null;
    private DelegateImpl delegateImpl = null;
    private ORB orb = null;

    public POASystemException getWrapper() {
        return this.wrapper;
    }

    public synchronized POA lookupPOA(Servant servant) {
        return (POA) this.exportedServantsToPOA.get(servant);
    }

    public synchronized void registerPOAForServant(POA poa, Servant servant) {
        this.exportedServantsToPOA.put(servant, poa);
    }

    public synchronized void unregisterPOAForServant(POA poa, Servant servant) {
        this.exportedServantsToPOA.remove(servant);
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterFactory
    public void init(ORB orb) {
        this.orb = orb;
        this.wrapper = POASystemException.get(orb, CORBALogDomains.OA_LIFECYCLE);
        this.omgWrapper = OMGSystemException.get(orb, CORBALogDomains.OA_LIFECYCLE);
        this.delegateImpl = new DelegateImpl(orb, this);
        registerRootPOA();
        orb.getLocalResolver().register(ORBConstants.POA_CURRENT_NAME, ClosureFactory.makeConstant(new POACurrent(orb)));
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterFactory
    public ObjectAdapter find(ObjectAdapterId objectAdapterId) {
        try {
            boolean z2 = true;
            Iterator it = objectAdapterId.iterator();
            POA rootPOA = getRootPOA();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (z2) {
                    if (!str.equals(ORBConstants.ROOT_POA_NAME)) {
                        throw this.wrapper.makeFactoryNotPoa(str);
                    }
                    z2 = false;
                } else {
                    rootPOA = rootPOA.find_POA(str, true);
                }
            }
            if (rootPOA == null) {
                throw this.wrapper.poaLookupError();
            }
            return (ObjectAdapter) rootPOA;
        } catch (OBJECT_NOT_EXIST e2) {
            throw e2;
        } catch (TRANSIENT e3) {
            throw e3;
        } catch (AdapterNonExistent e4) {
            throw this.omgWrapper.noObjectAdaptor(e4);
        } catch (Exception e5) {
            throw this.wrapper.poaLookupError(e5);
        }
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterFactory
    public void shutdown(boolean z2) {
        Iterator it;
        synchronized (this) {
            this.isShuttingDown = true;
            it = new HashSet(this.poaManagers).iterator();
        }
        while (it.hasNext()) {
            try {
                ((POAManager) it.next()).deactivate(true, z2);
            } catch (AdapterInactive e2) {
            }
        }
    }

    public synchronized void removePoaManager(POAManager pOAManager) {
        this.poaManagers.remove(pOAManager);
    }

    public synchronized void addPoaManager(POAManager pOAManager) {
        this.poaManagers.add(pOAManager);
    }

    public synchronized int newPOAManagerId() {
        int i2 = this.poaManagerId;
        this.poaManagerId = i2 + 1;
        return i2;
    }

    public void registerRootPOA() {
        this.orb.getLocalResolver().register(ORBConstants.ROOT_POA_NAME, ClosureFactory.makeFuture(new Closure() { // from class: com.sun.corba.se.impl.oa.poa.POAFactory.1
            @Override // com.sun.corba.se.spi.orbutil.closure.Closure
            public Object evaluate() {
                return POAImpl.makeRootPOA(POAFactory.this.orb);
            }
        }));
    }

    public synchronized POA getRootPOA() {
        if (this.rootPOA == null) {
            if (this.isShuttingDown) {
                throw this.omgWrapper.noObjectAdaptor();
            }
            try {
                this.rootPOA = (POAImpl) this.orb.resolve_initial_references(ORBConstants.ROOT_POA_NAME);
            } catch (InvalidName e2) {
                throw this.wrapper.cantResolveRootPoa(e2);
            }
        }
        return this.rootPOA;
    }

    public Delegate getDelegateImpl() {
        return this.delegateImpl;
    }

    public synchronized int newPOAId() {
        int i2 = this.poaId;
        this.poaId = i2 + 1;
        return i2;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterFactory
    public ORB getORB() {
        return this.orb;
    }
}
