package com.sun.corba.se.spi.orb;

import com.sun.corba.se.impl.corba.TypeCodeFactory;
import com.sun.corba.se.impl.corba.TypeCodeImpl;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.presentation.rmi.PresentationManagerImpl;
import com.sun.corba.se.impl.transport.ByteBufferPoolImpl;
import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.transport.ByteBufferPool;
import com.sun.corba.se.spi.copyobject.CopierManager;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.ObjectKeyFactory;
import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.logging.LogWrapperBase;
import com.sun.corba.se.spi.logging.LogWrapperFactory;
import com.sun.corba.se.spi.monitoring.MonitoringConstants;
import com.sun.corba.se.spi.monitoring.MonitoringFactories;
import com.sun.corba.se.spi.monitoring.MonitoringManager;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
import com.sun.corba.se.spi.presentation.rmi.PresentationDefaults;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import com.sun.corba.se.spi.protocol.ClientDelegateFactory;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import com.sun.corba.se.spi.protocol.PIHandler;
import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
import com.sun.corba.se.spi.resolver.LocalResolver;
import com.sun.corba.se.spi.resolver.Resolver;
import com.sun.corba.se.spi.servicecontext.ServiceContextRegistry;
import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
import com.sun.corba.se.spi.transport.CorbaTransportManager;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import sun.awt.AppContext;
import sun.corba.SharedSecrets;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/ORB.class */
public abstract class ORB extends com.sun.corba.se.org.omg.CORBA.ORB implements Broker, TypeCodeFactory {
    protected static ORBUtilSystemException staticWrapper;
    ByteBufferPool byteBufferPool;
    public static boolean ORBInitDebug = false;
    private static final Object pmLock = new Object();
    private static Map staticWrapperMap = new ConcurrentHashMap();
    public boolean transportDebugFlag = false;
    public boolean subcontractDebugFlag = false;
    public boolean poaDebugFlag = false;
    public boolean poaConcurrencyDebugFlag = false;
    public boolean poaFSMDebugFlag = false;
    public boolean orbdDebugFlag = false;
    public boolean namingDebugFlag = false;
    public boolean serviceContextDebugFlag = false;
    public boolean transientObjectManagerDebugFlag = false;
    public boolean giopVersionDebugFlag = false;
    public boolean shutdownDebugFlag = false;
    public boolean giopDebugFlag = false;
    public boolean invocationTimingDebugFlag = false;
    public boolean orbInitDebugFlag = false;
    private Map wrapperMap = new ConcurrentHashMap();
    protected ORBUtilSystemException wrapper = ORBUtilSystemException.get(this, CORBALogDomains.RPC_PRESENTATION);
    protected OMGSystemException omgWrapper = OMGSystemException.get(this, CORBALogDomains.RPC_PRESENTATION);
    private Map typeCodeMap = new HashMap();
    private TypeCodeImpl[] primitiveTypeCodeConstants = {new TypeCodeImpl(this, 0), new TypeCodeImpl(this, 1), new TypeCodeImpl(this, 2), new TypeCodeImpl(this, 3), new TypeCodeImpl(this, 4), new TypeCodeImpl(this, 5), new TypeCodeImpl(this, 6), new TypeCodeImpl(this, 7), new TypeCodeImpl(this, 8), new TypeCodeImpl(this, 9), new TypeCodeImpl(this, 10), new TypeCodeImpl(this, 11), new TypeCodeImpl(this, 12), new TypeCodeImpl(this, 13), new TypeCodeImpl(this, 14), null, null, null, new TypeCodeImpl(this, 18), null, null, null, null, new TypeCodeImpl(this, 23), new TypeCodeImpl(this, 24), new TypeCodeImpl(this, 25), new TypeCodeImpl(this, 26), new TypeCodeImpl(this, 27), new TypeCodeImpl(this, 28), new TypeCodeImpl(this, 29), new TypeCodeImpl(this, 30), new TypeCodeImpl(this, 31), new TypeCodeImpl(this, 32)};
    protected MonitoringManager monitoringManager = MonitoringFactories.getMonitoringManagerFactory().createMonitoringManager("orb", MonitoringConstants.DEFAULT_MONITORING_ROOT_DESCRIPTION);

    public abstract boolean isLocalHost(String str);

    public abstract boolean isLocalServerId(int i2, int i3);

    public abstract OAInvocationInfo peekInvocationInfo();

    public abstract void pushInvocationInfo(OAInvocationInfo oAInvocationInfo);

    public abstract OAInvocationInfo popInvocationInfo();

    public abstract CorbaTransportManager getCorbaTransportManager();

    public abstract LegacyServerSocketManager getLegacyServerSocketManager();

    public abstract void set_parameters(Properties properties);

    public abstract ORBVersion getORBVersion();

    public abstract void setORBVersion(ORBVersion oRBVersion);

    public abstract IOR getFVDCodeBaseIOR();

    public abstract void handleBadServerId(ObjectKey objectKey);

    public abstract void setBadServerIdHandler(BadServerIdHandler badServerIdHandler);

    public abstract void initBadServerIdHandler();

    public abstract void notifyORB();

    public abstract PIHandler getPIHandler();

    public abstract void checkShutdownState();

    public abstract boolean isDuringDispatch();

    public abstract void startingDispatch();

    public abstract void finishedDispatch();

    public abstract int getTransientServerId();

    public abstract ServiceContextRegistry getServiceContextRegistry();

    public abstract RequestDispatcherRegistry getRequestDispatcherRegistry();

    public abstract ORBData getORBData();

    public abstract void setClientDelegateFactory(ClientDelegateFactory clientDelegateFactory);

    public abstract ClientDelegateFactory getClientDelegateFactory();

    public abstract void setCorbaContactInfoListFactory(CorbaContactInfoListFactory corbaContactInfoListFactory);

    public abstract CorbaContactInfoListFactory getCorbaContactInfoListFactory();

    public abstract void setResolver(Resolver resolver);

    public abstract Resolver getResolver();

    public abstract void setLocalResolver(LocalResolver localResolver);

    public abstract LocalResolver getLocalResolver();

    public abstract void setURLOperation(Operation operation);

    public abstract Operation getURLOperation();

    public abstract void setINSDelegate(CorbaServerRequestDispatcher corbaServerRequestDispatcher);

    public abstract TaggedComponentFactoryFinder getTaggedComponentFactoryFinder();

    public abstract IdentifiableFactoryFinder getTaggedProfileFactoryFinder();

    public abstract IdentifiableFactoryFinder getTaggedProfileTemplateFactoryFinder();

    public abstract ObjectKeyFactory getObjectKeyFactory();

    public abstract void setObjectKeyFactory(ObjectKeyFactory objectKeyFactory);

    public abstract void setThreadPoolManager(ThreadPoolManager threadPoolManager);

    public abstract ThreadPoolManager getThreadPoolManager();

    public abstract CopierManager getCopierManager();

    public abstract void validateIORClass(String str);

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/ORB$Holder.class */
    static class Holder {
        static final PresentationManager defaultPresentationManager = ORB.setupPresentationManager();

        Holder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PresentationManager setupPresentationManager() {
        staticWrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PRESENTATION);
        boolean zBooleanValue = ((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.spi.orb.ORB.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return Boolean.valueOf(Boolean.getBoolean(ORBConstants.USE_DYNAMIC_STUB_PROPERTY));
            }
        })).booleanValue();
        PresentationManager.StubFactoryFactory stubFactoryFactory = (PresentationManager.StubFactoryFactory) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.spi.orb.ORB.2
            @Override // java.security.PrivilegedAction
            public Object run() {
                PresentationManager.StubFactoryFactory proxyStubFactoryFactory = PresentationDefaults.getProxyStubFactoryFactory();
                String property = System.getProperty(ORBConstants.DYNAMIC_STUB_FACTORY_FACTORY_CLASS, "com.sun.corba.se.impl.presentation.rmi.bcel.StubFactoryFactoryBCELImpl");
                try {
                    proxyStubFactoryFactory = (PresentationManager.StubFactoryFactory) SharedSecrets.getJavaCorbaAccess().loadClass(property).newInstance();
                } catch (Exception e2) {
                    ORB.staticWrapper.errorInSettingDynamicStubFactoryFactory(e2, property);
                }
                return proxyStubFactoryFactory;
            }
        });
        PresentationManagerImpl presentationManagerImpl = new PresentationManagerImpl(zBooleanValue);
        presentationManagerImpl.setStubFactoryFactory(false, PresentationDefaults.getStaticStubFactoryFactory());
        presentationManagerImpl.setStubFactoryFactory(true, stubFactoryFactory);
        return presentationManagerImpl;
    }

    @Override // org.omg.CORBA.ORB
    public void destroy() {
        this.wrapper = null;
        this.omgWrapper = null;
        this.typeCodeMap = null;
        this.primitiveTypeCodeConstants = null;
        this.byteBufferPool = null;
    }

    public static PresentationManager getPresentationManager() {
        AppContext appContext;
        PresentationManager presentationManager;
        if (System.getSecurityManager() != null && AppContext.getAppContexts().size() > 0 && (appContext = AppContext.getAppContext()) != null) {
            synchronized (pmLock) {
                PresentationManager presentationManager2 = (PresentationManager) appContext.get(PresentationManager.class);
                if (presentationManager2 == null) {
                    presentationManager2 = setupPresentationManager();
                    appContext.put(PresentationManager.class, presentationManager2);
                }
                presentationManager = presentationManager2;
            }
            return presentationManager;
        }
        return Holder.defaultPresentationManager;
    }

    public static PresentationManager.StubFactoryFactory getStubFactoryFactory() {
        PresentationManager presentationManager = getPresentationManager();
        return presentationManager.getStubFactoryFactory(presentationManager.useDynamicStubs());
    }

    protected ORB() {
    }

    public TypeCodeImpl get_primitive_tc(int i2) {
        synchronized (this) {
            checkShutdownState();
        }
        try {
            return this.primitiveTypeCodeConstants[i2];
        } catch (Throwable th) {
            throw this.wrapper.invalidTypecodeKind(th, new Integer(i2));
        }
    }

    @Override // com.sun.corba.se.impl.corba.TypeCodeFactory
    public synchronized void setTypeCode(String str, TypeCodeImpl typeCodeImpl) {
        checkShutdownState();
        this.typeCodeMap.put(str, typeCodeImpl);
    }

    @Override // com.sun.corba.se.impl.corba.TypeCodeFactory
    public synchronized TypeCodeImpl getTypeCode(String str) {
        checkShutdownState();
        return (TypeCodeImpl) this.typeCodeMap.get(str);
    }

    public MonitoringManager getMonitoringManager() {
        synchronized (this) {
            checkShutdownState();
        }
        return this.monitoringManager;
    }

    public Logger getLogger(String str) {
        String oRBId;
        synchronized (this) {
            checkShutdownState();
        }
        ORBData oRBData = getORBData();
        if (oRBData == null) {
            oRBId = "_INITIALIZING_";
        } else {
            oRBId = oRBData.getORBId();
            if (oRBId.equals("")) {
                oRBId = "_DEFAULT_";
            }
        }
        return getCORBALogger(oRBId, str);
    }

    public static Logger staticGetLogger(String str) {
        return getCORBALogger("_CORBA_", str);
    }

    private static Logger getCORBALogger(String str, String str2) {
        return Logger.getLogger("javax.enterprise.resource.corba." + str + "." + str2, ORBConstants.LOG_RESOURCE_FILE);
    }

    public LogWrapperBase getLogWrapper(String str, String str2, LogWrapperFactory logWrapperFactory) {
        StringPair stringPair = new StringPair(str, str2);
        LogWrapperBase logWrapperBaseCreate = (LogWrapperBase) this.wrapperMap.get(stringPair);
        if (logWrapperBaseCreate == null) {
            logWrapperBaseCreate = logWrapperFactory.create(getLogger(str));
            this.wrapperMap.put(stringPair, logWrapperBaseCreate);
        }
        return logWrapperBaseCreate;
    }

    public static LogWrapperBase staticGetLogWrapper(String str, String str2, LogWrapperFactory logWrapperFactory) {
        StringPair stringPair = new StringPair(str, str2);
        LogWrapperBase logWrapperBaseCreate = (LogWrapperBase) staticWrapperMap.get(stringPair);
        if (logWrapperBaseCreate == null) {
            logWrapperBaseCreate = logWrapperFactory.create(staticGetLogger(str));
            staticWrapperMap.put(stringPair, logWrapperBaseCreate);
        }
        return logWrapperBaseCreate;
    }

    public ByteBufferPool getByteBufferPool() {
        synchronized (this) {
            checkShutdownState();
        }
        if (this.byteBufferPool == null) {
            this.byteBufferPool = new ByteBufferPoolImpl(this);
        }
        return this.byteBufferPool;
    }
}
