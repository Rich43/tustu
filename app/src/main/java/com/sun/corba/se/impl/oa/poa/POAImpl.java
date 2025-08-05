package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.ior.ObjectAdapterIdArray;
import com.sun.corba.se.impl.ior.POAObjectKeyTemplate;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.orbutil.concurrent.CondVar;
import com.sun.corba.se.impl.orbutil.concurrent.ReentrantMutex;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import com.sun.corba.se.impl.orbutil.concurrent.SyncUtil;
import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.ior.TaggedProfile;
import com.sun.corba.se.spi.oa.OADestroyed;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import com.sun.corba.se.spi.oa.ObjectAdapterBase;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.ForwardException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.SystemException;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;
import org.omg.PortableServer.AdapterActivator;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.IdAssignmentPolicy;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.IdUniquenessPolicy;
import org.omg.PortableServer.IdUniquenessPolicyValue;
import org.omg.PortableServer.ImplicitActivationPolicy;
import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.omg.PortableServer.LifespanPolicy;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManager;
import org.omg.PortableServer.POAPackage.AdapterAlreadyExists;
import org.omg.PortableServer.POAPackage.AdapterNonExistent;
import org.omg.PortableServer.POAPackage.InvalidPolicy;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongAdapter;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.RequestProcessingPolicy;
import org.omg.PortableServer.RequestProcessingPolicyValue;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantManager;
import org.omg.PortableServer.ServantRetentionPolicy;
import org.omg.PortableServer.ServantRetentionPolicyValue;
import org.omg.PortableServer.ThreadPolicy;
import org.omg.PortableServer.ThreadPolicyValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAImpl.class */
public class POAImpl extends ObjectAdapterBase implements POA {
    private boolean debug;
    private static final int STATE_START = 0;
    private static final int STATE_INIT = 1;
    private static final int STATE_INIT_DONE = 2;
    private static final int STATE_RUN = 3;
    private static final int STATE_DESTROYING = 4;
    private static final int STATE_DESTROYED = 5;
    private int state;
    private POAPolicyMediator mediator;
    private int numLevels;
    private ObjectAdapterId poaId;
    private String name;
    private POAManagerImpl manager;
    private int uniquePOAId;
    private POAImpl parent;
    private Map children;
    private AdapterActivator activator;
    private int invocationCount;
    Sync poaMutex;
    private CondVar adapterActivatorCV;
    private CondVar invokeCV;
    private CondVar beingDestroyedCV;
    protected ThreadLocal isDestroying;

    private String stateToString() {
        switch (this.state) {
            case 0:
                return "START";
            case 1:
                return "INIT";
            case 2:
                return "INIT_DONE";
            case 3:
                return "RUN";
            case 4:
                return "DESTROYING";
            case 5:
                return "DESTROYED";
            default:
                return "UNKNOWN(" + this.state + ")";
        }
    }

    public String toString() {
        return "POA[" + this.poaId.toString() + ", uniquePOAId=" + this.uniquePOAId + ", state=" + stateToString() + ", invocationCount=" + this.invocationCount + "]";
    }

    boolean getDebug() {
        return this.debug;
    }

    static POAFactory getPOAFactory(ORB orb) {
        return (POAFactory) orb.getRequestDispatcherRegistry().getObjectAdapterFactory(32);
    }

    static POAImpl makeRootPOA(ORB orb) {
        POAManagerImpl pOAManagerImpl = new POAManagerImpl(getPOAFactory(orb), orb.getPIHandler());
        POAImpl pOAImpl = new POAImpl(ORBConstants.ROOT_POA_NAME, null, orb, 0);
        pOAImpl.initialize(pOAManagerImpl, Policies.rootPOAPolicies);
        return pOAImpl;
    }

    int getPOAId() {
        return this.uniquePOAId;
    }

    void lock() {
        SyncUtil.acquire(this.poaMutex);
        if (this.debug) {
            ORBUtility.dprint(this, "LOCKED poa " + ((Object) this));
        }
    }

    void unlock() {
        if (this.debug) {
            ORBUtility.dprint(this, "UNLOCKED poa " + ((Object) this));
        }
        this.poaMutex.release();
    }

    Policies getPolicies() {
        return this.mediator.getPolicies();
    }

    private POAImpl(String str, POAImpl pOAImpl, ORB orb, int i2) {
        super(orb);
        this.debug = orb.poaDebugFlag;
        if (this.debug) {
            ORBUtility.dprint(this, "Creating POA with name=" + str + " parent=" + ((Object) pOAImpl));
        }
        this.state = i2;
        this.name = str;
        this.parent = pOAImpl;
        this.children = new HashMap();
        this.activator = null;
        this.uniquePOAId = getPOAFactory(orb).newPOAId();
        if (pOAImpl == null) {
            this.numLevels = 1;
        } else {
            this.numLevels = pOAImpl.numLevels + 1;
            pOAImpl.children.put(str, this);
        }
        String[] strArr = new String[this.numLevels];
        int i3 = this.numLevels - 1;
        for (POAImpl pOAImpl2 = this; pOAImpl2 != null; pOAImpl2 = pOAImpl2.parent) {
            int i4 = i3;
            i3--;
            strArr[i4] = pOAImpl2.name;
        }
        this.poaId = new ObjectAdapterIdArray(strArr);
        this.invocationCount = 0;
        this.poaMutex = new ReentrantMutex(orb.poaConcurrencyDebugFlag);
        this.adapterActivatorCV = new CondVar(this.poaMutex, orb.poaConcurrencyDebugFlag);
        this.invokeCV = new CondVar(this.poaMutex, orb.poaConcurrencyDebugFlag);
        this.beingDestroyedCV = new CondVar(this.poaMutex, orb.poaConcurrencyDebugFlag);
        this.isDestroying = new ThreadLocal() { // from class: com.sun.corba.se.impl.oa.poa.POAImpl.1
            @Override // java.lang.ThreadLocal
            protected Object initialValue() {
                return Boolean.FALSE;
            }
        };
    }

    private void initialize(POAManagerImpl pOAManagerImpl, Policies policies) {
        if (this.debug) {
            ORBUtility.dprint(this, "Initializing poa " + ((Object) this) + " with POAManager=" + ((Object) pOAManagerImpl) + " policies=" + ((Object) policies));
        }
        this.manager = pOAManagerImpl;
        pOAManagerImpl.addPOA(this);
        this.mediator = POAPolicyMediatorFactory.create(policies, this);
        POAObjectKeyTemplate pOAObjectKeyTemplate = new POAObjectKeyTemplate(getORB(), this.mediator.getScid(), this.mediator.getServerId(), getORB().getORBData().getORBId(), this.poaId);
        if (this.debug) {
            ORBUtility.dprint(this, "Initializing poa: oktemp=" + ((Object) pOAObjectKeyTemplate));
        }
        initializeTemplate(pOAObjectKeyTemplate, true, policies, null, null, pOAObjectKeyTemplate.getObjectAdapterId());
        if (this.state == 0) {
            this.state = 3;
        } else {
            if (this.state == 1) {
                this.state = 2;
                return;
            }
            throw lifecycleWrapper().illegalPoaStateTrans();
        }
    }

    private boolean waitUntilRunning() {
        if (this.debug) {
            ORBUtility.dprint(this, "Calling waitUntilRunning on poa " + ((Object) this));
        }
        while (this.state < 3) {
            try {
                this.adapterActivatorCV.await();
            } catch (InterruptedException e2) {
            }
        }
        if (this.debug) {
            ORBUtility.dprint(this, "Exiting waitUntilRunning on poa " + ((Object) this));
        }
        return this.state == 3;
    }

    private boolean destroyIfNotInitDone() {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling destroyIfNotInitDone on poa " + ((Object) this));
            }
            boolean z2 = this.state == 2;
            if (z2) {
                this.state = 3;
            } else {
                new DestroyThread(false, this.debug).doIt(this, true);
            }
            return z2;
        } finally {
            this.adapterActivatorCV.broadcast();
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting destroyIfNotInitDone on poa " + ((Object) this));
            }
            unlock();
        }
    }

    private byte[] internalReferenceToId(Object object) throws WrongAdapter {
        IOR ior = ORBUtility.getIOR(object);
        if (!IORFactories.getIORTemplateList(getCurrentFactory()).isEquivalent(ior.getIORTemplates())) {
            throw new WrongAdapter();
        }
        Iterator it = ior.iterator();
        if (!it.hasNext()) {
            throw iorWrapper().noProfilesInIor();
        }
        return ((TaggedProfile) it.next()).getObjectId().getId();
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAImpl$DestroyThread.class */
    static class DestroyThread extends Thread {
        private boolean wait;
        private boolean etherealize;
        private boolean debug;
        private POAImpl thePoa;

        public DestroyThread(boolean z2, boolean z3) {
            this.etherealize = z2;
            this.debug = z3;
        }

        public void doIt(POAImpl pOAImpl, boolean z2) {
            if (this.debug) {
                ORBUtility.dprint(this, "Calling DestroyThread.doIt(thePOA=" + ((Object) pOAImpl) + " wait=" + z2 + " etherealize=" + this.etherealize);
            }
            this.thePoa = pOAImpl;
            this.wait = z2;
            if (z2) {
                run();
            } else {
                try {
                    setDaemon(true);
                } catch (Exception e2) {
                }
                start();
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            HashSet hashSet = new HashSet();
            performDestroy(this.thePoa, hashSet);
            Iterator<E> it = hashSet.iterator();
            ObjectReferenceTemplate[] objectReferenceTemplateArr = new ObjectReferenceTemplate[hashSet.size()];
            int i2 = 0;
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                objectReferenceTemplateArr[i3] = (ObjectReferenceTemplate) it.next();
            }
            this.thePoa.getORB().getPIHandler().adapterStateChanged(objectReferenceTemplateArr, (short) 4);
        }

        private boolean prepareForDestruction(POAImpl pOAImpl, Set set) {
            try {
                pOAImpl.lock();
                if (this.debug) {
                    ORBUtility.dprint(this, "Calling performDestroy on poa " + ((Object) pOAImpl));
                }
                if (pOAImpl.state <= 3) {
                    pOAImpl.state = 4;
                    pOAImpl.isDestroying.set(Boolean.TRUE);
                    POAImpl[] pOAImplArr = (POAImpl[]) pOAImpl.children.values().toArray(new POAImpl[0]);
                    pOAImpl.unlock();
                    for (POAImpl pOAImpl2 : pOAImplArr) {
                        performDestroy(pOAImpl2, set);
                    }
                    return true;
                }
                if (this.wait) {
                    while (pOAImpl.state != 5) {
                        try {
                            pOAImpl.beingDestroyedCV.await();
                        } catch (InterruptedException e2) {
                        }
                    }
                }
                return false;
            } finally {
                pOAImpl.unlock();
            }
        }

        public void performDestroy(POAImpl pOAImpl, Set set) {
            if (prepareForDestruction(pOAImpl, set)) {
                POAImpl pOAImpl2 = pOAImpl.parent;
                boolean z2 = pOAImpl2 == null;
                if (!z2) {
                    try {
                        pOAImpl2.lock();
                    } finally {
                        if (!z2) {
                            pOAImpl2.unlock();
                            pOAImpl.parent = null;
                        }
                    }
                }
                try {
                    pOAImpl.lock();
                    completeDestruction(pOAImpl, pOAImpl2, set);
                    pOAImpl.unlock();
                    if (z2) {
                        pOAImpl.manager.getFactory().registerRootPOA();
                    }
                } catch (Throwable th) {
                    pOAImpl.unlock();
                    if (z2) {
                        pOAImpl.manager.getFactory().registerRootPOA();
                    }
                    throw th;
                }
            }
        }

        private void completeDestruction(POAImpl pOAImpl, POAImpl pOAImpl2, Set set) {
            if (this.debug) {
                ORBUtility.dprint(this, "Calling completeDestruction on poa " + ((Object) pOAImpl));
            }
            while (pOAImpl.invocationCount != 0) {
                try {
                    try {
                        try {
                            pOAImpl.invokeCV.await();
                        } catch (InterruptedException e2) {
                        }
                    } catch (Throwable th) {
                        if (th instanceof ThreadDeath) {
                            throw ((ThreadDeath) th);
                        }
                        pOAImpl.lifecycleWrapper().unexpectedException(th, pOAImpl.toString());
                        pOAImpl.state = 5;
                        pOAImpl.beingDestroyedCV.broadcast();
                        pOAImpl.isDestroying.set(Boolean.FALSE);
                        if (this.debug) {
                            ORBUtility.dprint(this, "Exiting completeDestruction on poa " + ((Object) pOAImpl));
                            return;
                        }
                        return;
                    }
                } catch (Throwable th2) {
                    pOAImpl.state = 5;
                    pOAImpl.beingDestroyedCV.broadcast();
                    pOAImpl.isDestroying.set(Boolean.FALSE);
                    if (this.debug) {
                        ORBUtility.dprint(this, "Exiting completeDestruction on poa " + ((Object) pOAImpl));
                    }
                    throw th2;
                }
            }
            if (pOAImpl.mediator != null) {
                if (this.etherealize) {
                    pOAImpl.mediator.etherealizeAll();
                }
                pOAImpl.mediator.clearAOM();
            }
            if (pOAImpl.manager != null) {
                pOAImpl.manager.removePOA(pOAImpl);
            }
            if (pOAImpl2 != null) {
                pOAImpl2.children.remove(pOAImpl.name);
            }
            set.add(pOAImpl.getAdapterTemplate());
            pOAImpl.state = 5;
            pOAImpl.beingDestroyedCV.broadcast();
            pOAImpl.isDestroying.set(Boolean.FALSE);
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting completeDestruction on poa " + ((Object) pOAImpl));
            }
        }
    }

    void etherealizeAll() {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling etheralizeAll on poa " + ((Object) this));
            }
            this.mediator.etherealizeAll();
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting etheralizeAll on poa " + ((Object) this));
            }
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public POA create_POA(String str, POAManager pOAManager, Policy[] policyArr) throws InvalidPolicy, AdapterAlreadyExists {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling create_POA(name=" + str + " theManager=" + ((Object) pOAManager) + " policies=" + ((Object) policyArr) + ") on poa " + ((Object) this));
            }
            if (this.state > 3) {
                throw omgLifecycleWrapper().createPoaDestroy();
            }
            POAImpl pOAImpl = (POAImpl) this.children.get(str);
            if (pOAImpl == null) {
                pOAImpl = new POAImpl(str, this, getORB(), 0);
            }
            try {
                pOAImpl.lock();
                if (this.debug) {
                    ORBUtility.dprint(this, "Calling create_POA: new poa is " + ((Object) pOAImpl));
                }
                if (pOAImpl.state != 0 && pOAImpl.state != 1) {
                    throw new AdapterAlreadyExists();
                }
                POAManagerImpl pOAManagerImpl = (POAManagerImpl) pOAManager;
                if (pOAManagerImpl == null) {
                    pOAManagerImpl = new POAManagerImpl(this.manager.getFactory(), this.manager.getPIHandler());
                }
                pOAImpl.initialize(pOAManagerImpl, new Policies(policyArr, getORB().getCopierManager().getDefaultId()));
                POAImpl pOAImpl2 = pOAImpl;
                pOAImpl.unlock();
                unlock();
                return pOAImpl2;
            } catch (Throwable th) {
                pOAImpl.unlock();
                throw th;
            }
        } catch (Throwable th2) {
            unlock();
            throw th2;
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public POA find_POA(String str, boolean z2) throws AdapterNonExistent {
        boolean zDestroyIfNotInitDone;
        AdapterActivator adapterActivator = null;
        lock();
        if (this.debug) {
            ORBUtility.dprint(this, "Calling find_POA(name=" + str + " activate=" + z2 + ") on poa " + ((Object) this));
        }
        POAImpl pOAImpl = (POAImpl) this.children.get(str);
        if (pOAImpl != null) {
            if (this.debug) {
                ORBUtility.dprint(this, "Calling find_POA: found poa " + ((Object) pOAImpl));
            }
            try {
                pOAImpl.lock();
                unlock();
                if (!pOAImpl.waitUntilRunning()) {
                    throw omgLifecycleWrapper().poaDestroyed();
                }
            } finally {
                pOAImpl.unlock();
            }
        } else {
            try {
                if (this.debug) {
                    ORBUtility.dprint(this, "Calling find_POA: no poa found");
                }
                if (z2 && this.activator != null) {
                    pOAImpl = new POAImpl(str, this, getORB(), 1);
                    if (this.debug) {
                        ORBUtility.dprint(this, "Calling find_POA: created poa " + ((Object) pOAImpl));
                    }
                    adapterActivator = this.activator;
                } else {
                    throw new AdapterNonExistent();
                }
            } finally {
                unlock();
            }
        }
        if (adapterActivator != null) {
            boolean zUnknown_adapter = false;
            if (this.debug) {
                ORBUtility.dprint(this, "Calling find_POA: calling AdapterActivator");
            }
            try {
                try {
                    synchronized (adapterActivator) {
                        zUnknown_adapter = adapterActivator.unknown_adapter(this, str);
                    }
                    zDestroyIfNotInitDone = pOAImpl.destroyIfNotInitDone();
                } catch (SystemException e2) {
                    throw omgLifecycleWrapper().adapterActivatorException(e2, str, this.poaId.toString());
                } catch (Throwable th) {
                    lifecycleWrapper().unexpectedException(th, toString());
                    if (th instanceof ThreadDeath) {
                        throw ((ThreadDeath) th);
                    }
                    zDestroyIfNotInitDone = pOAImpl.destroyIfNotInitDone();
                }
                if (zUnknown_adapter) {
                    if (!zDestroyIfNotInitDone) {
                        throw omgLifecycleWrapper().adapterActivatorException(str, this.poaId.toString());
                    }
                } else {
                    if (this.debug) {
                        ORBUtility.dprint(this, "Calling find_POA: AdapterActivator returned false");
                    }
                    throw new AdapterNonExistent();
                }
            } catch (Throwable th2) {
                pOAImpl.destroyIfNotInitDone();
                throw th2;
            }
        }
        return pOAImpl;
    }

    @Override // org.omg.PortableServer.POAOperations
    public void destroy(boolean z2, boolean z3) {
        if (z3 && getORB().isDuringDispatch()) {
            throw lifecycleWrapper().destroyDeadlock();
        }
        new DestroyThread(z2, this.debug).doIt(this, z3);
    }

    @Override // org.omg.PortableServer.POAOperations
    public ThreadPolicy create_thread_policy(ThreadPolicyValue threadPolicyValue) {
        return new ThreadPolicyImpl(threadPolicyValue);
    }

    @Override // org.omg.PortableServer.POAOperations
    public LifespanPolicy create_lifespan_policy(LifespanPolicyValue lifespanPolicyValue) {
        return new LifespanPolicyImpl(lifespanPolicyValue);
    }

    @Override // org.omg.PortableServer.POAOperations
    public IdUniquenessPolicy create_id_uniqueness_policy(IdUniquenessPolicyValue idUniquenessPolicyValue) {
        return new IdUniquenessPolicyImpl(idUniquenessPolicyValue);
    }

    @Override // org.omg.PortableServer.POAOperations
    public IdAssignmentPolicy create_id_assignment_policy(IdAssignmentPolicyValue idAssignmentPolicyValue) {
        return new IdAssignmentPolicyImpl(idAssignmentPolicyValue);
    }

    @Override // org.omg.PortableServer.POAOperations
    public ImplicitActivationPolicy create_implicit_activation_policy(ImplicitActivationPolicyValue implicitActivationPolicyValue) {
        return new ImplicitActivationPolicyImpl(implicitActivationPolicyValue);
    }

    @Override // org.omg.PortableServer.POAOperations
    public ServantRetentionPolicy create_servant_retention_policy(ServantRetentionPolicyValue servantRetentionPolicyValue) {
        return new ServantRetentionPolicyImpl(servantRetentionPolicyValue);
    }

    @Override // org.omg.PortableServer.POAOperations
    public RequestProcessingPolicy create_request_processing_policy(RequestProcessingPolicyValue requestProcessingPolicyValue) {
        return new RequestProcessingPolicyImpl(requestProcessingPolicyValue);
    }

    @Override // org.omg.PortableServer.POAOperations
    public String the_name() {
        try {
            lock();
            return this.name;
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public POA the_parent() {
        try {
            lock();
            return this.parent;
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public POA[] the_children() {
        try {
            lock();
            Collection collectionValues = this.children.values();
            POA[] poaArr = new POA[collectionValues.size()];
            int i2 = 0;
            Iterator it = collectionValues.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                poaArr[i3] = (POA) it.next();
            }
            return poaArr;
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public POAManager the_POAManager() {
        try {
            lock();
            return this.manager;
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public AdapterActivator the_activator() {
        try {
            lock();
            return this.activator;
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public void the_activator(AdapterActivator adapterActivator) {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling the_activator on poa " + ((Object) this) + " activator=" + ((Object) adapterActivator));
            }
            this.activator = adapterActivator;
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public ServantManager get_servant_manager() throws WrongPolicy {
        try {
            lock();
            return this.mediator.getServantManager();
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public void set_servant_manager(ServantManager servantManager) throws WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling set_servant_manager on poa " + ((Object) this) + " servantManager=" + ((Object) servantManager));
            }
            this.mediator.setServantManager(servantManager);
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public Servant get_servant() throws NoServant, WrongPolicy {
        try {
            lock();
            return this.mediator.getDefaultServant();
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public void set_servant(Servant servant) throws WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling set_servant on poa " + ((Object) this) + " defaultServant=" + ((Object) servant));
            }
            this.mediator.setDefaultServant(servant);
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public byte[] activate_object(Servant servant) throws ServantAlreadyActive, WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling activate_object on poa " + ((Object) this) + " (servant=" + ((Object) servant) + ")");
            }
            byte[] bArrNewSystemId = this.mediator.newSystemId();
            try {
                this.mediator.activateObject(bArrNewSystemId, servant);
            } catch (ObjectAlreadyActive e2) {
            }
            return bArrNewSystemId;
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting activate_object on poa " + ((Object) this));
            }
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public void activate_object_with_id(byte[] bArr, Servant servant) throws ObjectAlreadyActive, ServantAlreadyActive, WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling activate_object_with_id on poa " + ((Object) this) + " (servant=" + ((Object) servant) + " id=" + ((Object) bArr) + ")");
            }
            this.mediator.activateObject((byte[]) bArr.clone(), servant);
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting activate_object_with_id on poa " + ((Object) this));
            }
            unlock();
        } catch (Throwable th) {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting activate_object_with_id on poa " + ((Object) this));
            }
            unlock();
            throw th;
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public void deactivate_object(byte[] bArr) throws ObjectNotActive, WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling deactivate_object on poa " + ((Object) this) + " (id=" + ((Object) bArr) + ")");
            }
            this.mediator.deactivateObject(bArr);
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting deactivate_object on poa " + ((Object) this));
            }
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public Object create_reference(String str) throws WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling create_reference(repId=" + str + ") on poa " + ((Object) this));
            }
            return makeObject(str, this.mediator.newSystemId());
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public Object create_reference_with_id(byte[] bArr, String str) {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling create_reference_with_id(oid=" + ((Object) bArr) + " repId=" + str + ") on poa " + ((Object) this));
            }
            Object objectMakeObject = makeObject(str, (byte[]) bArr.clone());
            unlock();
            return objectMakeObject;
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public byte[] servant_to_id(Servant servant) throws ServantNotActive, WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling servant_to_id(servant=" + ((Object) servant) + ") on poa " + ((Object) this));
            }
            return this.mediator.servantToId(servant);
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public Object servant_to_reference(Servant servant) throws ServantNotActive, WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling servant_to_reference(servant=" + ((Object) servant) + ") on poa " + ((Object) this));
            }
            byte[] bArrServantToId = this.mediator.servantToId(servant);
            Object objectCreate_reference_with_id = create_reference_with_id(bArrServantToId, servant._all_interfaces(this, bArrServantToId)[0]);
            unlock();
            return objectCreate_reference_with_id;
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public Servant reference_to_servant(Object object) throws ObjectNotActive, WrongAdapter, WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling reference_to_servant(reference=" + ((Object) object) + ") on poa " + ((Object) this));
            }
            if (this.state >= 4) {
                throw lifecycleWrapper().adapterDestroyed();
            }
            Servant servantIdToServant = this.mediator.idToServant(internalReferenceToId(object));
            unlock();
            return servantIdToServant;
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public byte[] reference_to_id(Object object) throws WrongAdapter, WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling reference_to_id(reference=" + ((Object) object) + ") on poa " + ((Object) this));
            }
            if (this.state >= 4) {
                throw lifecycleWrapper().adapterDestroyed();
            }
            return internalReferenceToId(object);
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public Servant id_to_servant(byte[] bArr) throws ObjectNotActive, WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling id_to_servant(id=" + ((Object) bArr) + ") on poa " + ((Object) this));
            }
            if (this.state >= 4) {
                throw lifecycleWrapper().adapterDestroyed();
            }
            return this.mediator.idToServant(bArr);
        } finally {
            unlock();
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public Object id_to_reference(byte[] bArr) throws ObjectNotActive, WrongPolicy {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling id_to_reference(id=" + ((Object) bArr) + ") on poa " + ((Object) this));
            }
            if (this.state >= 4) {
                throw lifecycleWrapper().adapterDestroyed();
            }
            Object objectMakeObject = makeObject(this.mediator.idToServant(bArr)._all_interfaces(this, bArr)[0], bArr);
            unlock();
            return objectMakeObject;
        } catch (Throwable th) {
            unlock();
            throw th;
        }
    }

    @Override // org.omg.PortableServer.POAOperations
    public byte[] id() {
        try {
            lock();
            return getAdapterId();
        } finally {
            unlock();
        }
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public Policy getEffectivePolicy(int i2) {
        return this.mediator.getPolicies().get_effective_policy(i2);
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public int getManagerId() {
        return this.manager.getManagerId();
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public short getState() {
        return this.manager.getORTState();
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public String[] getInterfaces(Object obj, byte[] bArr) {
        return ((Servant) obj)._all_interfaces(this, bArr);
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase
    protected ObjectCopierFactory getObjectCopierFactory() {
        return getORB().getCopierManager().getObjectCopierFactory(this.mediator.getPolicies().getCopierId());
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public void enter() throws OADestroyed {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling enter on poa " + ((Object) this));
            }
            while (this.state == 4 && this.isDestroying.get() == Boolean.FALSE) {
                try {
                    this.beingDestroyedCV.await();
                } catch (InterruptedException e2) {
                }
            }
            if (!waitUntilRunning()) {
                throw new OADestroyed();
            }
            this.invocationCount++;
            this.manager.enter();
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting enter on poa " + ((Object) this));
            }
            unlock();
        }
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public void exit() {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling exit on poa " + ((Object) this));
            }
            this.invocationCount--;
            if (this.invocationCount == 0 && this.state == 4) {
                this.invokeCV.broadcast();
            }
            this.manager.exit();
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting exit on poa " + ((Object) this));
            }
            unlock();
        }
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public void getInvocationServant(OAInvocationInfo oAInvocationInfo) {
        try {
            lock();
            if (this.debug) {
                ORBUtility.dprint(this, "Calling getInvocationServant on poa " + ((Object) this));
            }
            try {
                oAInvocationInfo.setServant(this.mediator.getInvocationServant(oAInvocationInfo.id(), oAInvocationInfo.getOperation()));
                if (this.debug) {
                    ORBUtility.dprint(this, "Exiting getInvocationServant on poa " + ((Object) this));
                }
                unlock();
            } catch (ForwardRequest e2) {
                throw new ForwardException(getORB(), e2.forward_reference);
            }
        } catch (Throwable th) {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting getInvocationServant on poa " + ((Object) this));
            }
            unlock();
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public Object getLocalServant(byte[] bArr) {
        return null;
    }

    @Override // com.sun.corba.se.spi.oa.ObjectAdapterBase, com.sun.corba.se.spi.oa.ObjectAdapter
    public void returnServant() {
        try {
            try {
                lock();
                if (this.debug) {
                    ORBUtility.dprint(this, "Calling returnServant on poa " + ((Object) this));
                }
                this.mediator.returnServant();
                if (this.debug) {
                    ORBUtility.dprint(this, "Exiting returnServant on poa " + ((Object) this));
                }
                unlock();
            } catch (Throwable th) {
                if (this.debug) {
                    ORBUtility.dprint(this, "Exception " + ((Object) th) + " in returnServant on poa " + ((Object) this));
                }
                if (th instanceof Error) {
                    throw ((Error) th);
                }
                if (th instanceof RuntimeException) {
                    throw ((RuntimeException) th);
                }
                if (this.debug) {
                    ORBUtility.dprint(this, "Exiting returnServant on poa " + ((Object) this));
                }
                unlock();
            }
        } catch (Throwable th2) {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting returnServant on poa " + ((Object) this));
            }
            unlock();
            throw th2;
        }
    }
}
