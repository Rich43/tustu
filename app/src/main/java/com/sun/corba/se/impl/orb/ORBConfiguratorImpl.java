package com.sun.corba.se.impl.orb;

import com.sun.corba.se.impl.dynamicany.DynAnyFactoryImpl;
import com.sun.corba.se.impl.legacy.connection.SocketFactoryAcceptorImpl;
import com.sun.corba.se.impl.legacy.connection.SocketFactoryContactInfoListImpl;
import com.sun.corba.se.impl.legacy.connection.USLPort;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.transport.SocketOrChannelAcceptorImpl;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.spi.activation.Activator;
import com.sun.corba.se.spi.activation.ActivatorHelper;
import com.sun.corba.se.spi.activation.EndPointInfo;
import com.sun.corba.se.spi.activation.Locator;
import com.sun.corba.se.spi.activation.LocatorHelper;
import com.sun.corba.se.spi.copyobject.CopierManager;
import com.sun.corba.se.spi.copyobject.CopyobjectDefaults;
import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
import com.sun.corba.se.spi.legacy.connection.ORBSocketFactory;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.OADefault;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import com.sun.corba.se.spi.orb.DataCollector;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBConfigurator;
import com.sun.corba.se.spi.orb.ORBData;
import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orb.OperationFactory;
import com.sun.corba.se.spi.orb.ParserImplBase;
import com.sun.corba.se.spi.orb.PropertyParser;
import com.sun.corba.se.spi.orbutil.closure.Closure;
import com.sun.corba.se.spi.orbutil.closure.ClosureFactory;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory;
import com.sun.corba.se.spi.protocol.RequestDispatcherDefault;
import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
import com.sun.corba.se.spi.resolver.LocalResolver;
import com.sun.corba.se.spi.resolver.Resolver;
import com.sun.corba.se.spi.resolver.ResolverDefault;
import com.sun.corba.se.spi.servicecontext.CodeSetServiceContext;
import com.sun.corba.se.spi.servicecontext.MaxStreamFormatVersionServiceContext;
import com.sun.corba.se.spi.servicecontext.ORBVersionServiceContext;
import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;
import com.sun.corba.se.spi.servicecontext.ServiceContextRegistry;
import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
import com.sun.corba.se.spi.transport.TransportDefault;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import org.omg.CORBA.CompletionStatus;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ORBConfiguratorImpl.class */
public class ORBConfiguratorImpl implements ORBConfigurator {
    private ORBUtilSystemException wrapper;
    private static final int ORB_STREAM = 0;

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ORBConfiguratorImpl$ConfigParser.class */
    public static class ConfigParser extends ParserImplBase {
        public Class[] userConfigurators = null;

        @Override // com.sun.corba.se.spi.orb.ParserImplBase
        public PropertyParser makeParser() {
            PropertyParser propertyParser = new PropertyParser();
            propertyParser.addPrefix("com.sun.CORBA.ORBUserConfigurators", OperationFactory.compose(OperationFactory.suffixAction(), OperationFactory.classAction()), "userConfigurators", Class.class);
            return propertyParser;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORBConfigurator
    public void configure(DataCollector dataCollector, ORB orb) throws Exception {
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.ORB_LIFECYCLE);
        initObjectCopiers(orb);
        initIORFinders(orb);
        orb.setClientDelegateFactory(TransportDefault.makeClientDelegateFactory(orb));
        initializeTransport(orb);
        initializeNaming(orb);
        initServiceContextRegistry(orb);
        initRequestDispatcherRegistry(orb);
        registerInitialReferences(orb);
        persistentServerInitialization(orb);
        runUserConfigurators(dataCollector, orb);
    }

    private void runUserConfigurators(DataCollector dataCollector, ORB orb) {
        ConfigParser configParser = new ConfigParser();
        configParser.init(dataCollector);
        if (configParser.userConfigurators != null) {
            for (int i2 = 0; i2 < configParser.userConfigurators.length; i2++) {
                try {
                    ((ORBConfigurator) configParser.userConfigurators[i2].newInstance()).configure(dataCollector, orb);
                } catch (Exception e2) {
                }
            }
        }
    }

    private void persistentServerInitialization(ORB orb) throws Exception {
        ORBData oRBData = orb.getORBData();
        if (oRBData.getServerIsORBActivated()) {
            try {
                Locator locatorNarrow = LocatorHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_LOCATOR_NAME));
                Activator activatorNarrow = ActivatorHelper.narrow(orb.resolve_initial_references(ORBConstants.SERVER_ACTIVATOR_NAME));
                Collection acceptors = orb.getCorbaTransportManager().getAcceptors(null, null);
                EndPointInfo[] endPointInfoArr = new EndPointInfo[acceptors.size()];
                int i2 = 0;
                for (Object obj : acceptors) {
                    if (obj instanceof LegacyServerSocketEndPointInfo) {
                        LegacyServerSocketEndPointInfo legacyServerSocketEndPointInfo = (LegacyServerSocketEndPointInfo) obj;
                        int endpoint = locatorNarrow.getEndpoint(legacyServerSocketEndPointInfo.getType());
                        if (endpoint == -1) {
                            endpoint = locatorNarrow.getEndpoint("IIOP_CLEAR_TEXT");
                            if (endpoint == -1) {
                                throw new Exception("ORBD must support IIOP_CLEAR_TEXT");
                            }
                        }
                        legacyServerSocketEndPointInfo.setLocatorPort(endpoint);
                        int i3 = i2;
                        i2++;
                        endPointInfoArr[i3] = new EndPointInfo(legacyServerSocketEndPointInfo.getType(), legacyServerSocketEndPointInfo.getPort());
                    }
                }
                activatorNarrow.registerEndpoints(oRBData.getPersistentServerId(), oRBData.getORBId(), endPointInfoArr);
            } catch (Exception e2) {
                throw this.wrapper.persistentServerInitError(CompletionStatus.COMPLETED_MAYBE, e2);
            }
        }
    }

    private void initializeTransport(final ORB orb) {
        ORBData oRBData = orb.getORBData();
        CorbaContactInfoListFactory corbaContactInfoListFactory = oRBData.getCorbaContactInfoListFactory();
        Acceptor[] acceptors = oRBData.getAcceptors();
        ORBSocketFactory legacySocketFactory = oRBData.getLegacySocketFactory();
        oRBData.getUserSpecifiedListenPorts();
        setLegacySocketFactoryORB(orb, legacySocketFactory);
        if (legacySocketFactory != null && corbaContactInfoListFactory != null) {
            throw this.wrapper.socketFactoryAndContactInfoListAtSameTime();
        }
        if (acceptors.length != 0 && legacySocketFactory != null) {
            throw this.wrapper.acceptorsAndLegacySocketFactoryAtSameTime();
        }
        oRBData.getSocketFactory().setORB(orb);
        if (legacySocketFactory != null) {
            corbaContactInfoListFactory = new CorbaContactInfoListFactory() { // from class: com.sun.corba.se.impl.orb.ORBConfiguratorImpl.1
                @Override // com.sun.corba.se.spi.transport.CorbaContactInfoListFactory
                public void setORB(ORB orb2) {
                }

                @Override // com.sun.corba.se.spi.transport.CorbaContactInfoListFactory
                public CorbaContactInfoList create(IOR ior) {
                    return new SocketFactoryContactInfoListImpl(orb, ior);
                }
            };
        } else if (corbaContactInfoListFactory != null) {
            corbaContactInfoListFactory.setORB(orb);
        } else {
            corbaContactInfoListFactory = TransportDefault.makeCorbaContactInfoListFactory(orb);
        }
        orb.setCorbaContactInfoListFactory(corbaContactInfoListFactory);
        int persistentServerPort = -1;
        if (oRBData.getORBServerPort() != 0) {
            persistentServerPort = oRBData.getORBServerPort();
        } else if (oRBData.getPersistentPortInitialized()) {
            persistentServerPort = oRBData.getPersistentServerPort();
        } else if (acceptors.length == 0) {
            persistentServerPort = 0;
        }
        if (persistentServerPort != -1) {
            createAndRegisterAcceptor(orb, legacySocketFactory, persistentServerPort, LegacyServerSocketEndPointInfo.DEFAULT_ENDPOINT, "IIOP_CLEAR_TEXT");
        }
        for (Acceptor acceptor : acceptors) {
            orb.getCorbaTransportManager().registerAcceptor(acceptor);
        }
        USLPort[] userSpecifiedListenPorts = oRBData.getUserSpecifiedListenPorts();
        if (userSpecifiedListenPorts != null) {
            for (int i2 = 0; i2 < userSpecifiedListenPorts.length; i2++) {
                createAndRegisterAcceptor(orb, legacySocketFactory, userSpecifiedListenPorts[i2].getPort(), LegacyServerSocketEndPointInfo.NO_NAME, userSpecifiedListenPorts[i2].getType());
            }
        }
    }

    private void createAndRegisterAcceptor(ORB orb, ORBSocketFactory oRBSocketFactory, int i2, String str, String str2) {
        SocketOrChannelAcceptorImpl socketFactoryAcceptorImpl;
        if (oRBSocketFactory == null) {
            socketFactoryAcceptorImpl = new SocketOrChannelAcceptorImpl(orb, i2, str, str2);
        } else {
            socketFactoryAcceptorImpl = new SocketFactoryAcceptorImpl(orb, i2, str, str2);
        }
        orb.getTransportManager().registerAcceptor(socketFactoryAcceptorImpl);
    }

    private void setLegacySocketFactoryORB(final ORB orb, final ORBSocketFactory oRBSocketFactory) {
        if (oRBSocketFactory == null) {
            return;
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.corba.se.impl.orb.ORBConfiguratorImpl.2
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IllegalAccessException, InstantiationException, IllegalArgumentException {
                    try {
                        oRBSocketFactory.getClass().getMethod("setORB", ORB.class).invoke(oRBSocketFactory, orb);
                        return null;
                    } catch (IllegalAccessException e2) {
                        RuntimeException runtimeException = new RuntimeException();
                        runtimeException.initCause(e2);
                        throw runtimeException;
                    } catch (NoSuchMethodException e3) {
                        return null;
                    } catch (InvocationTargetException e4) {
                        RuntimeException runtimeException2 = new RuntimeException();
                        runtimeException2.initCause(e4);
                        throw runtimeException2;
                    }
                }
            });
        } catch (Throwable th) {
            throw this.wrapper.unableToSetSocketFactoryOrb(th);
        }
    }

    private void initializeNaming(ORB orb) {
        LocalResolver localResolverMakeLocalResolver = ResolverDefault.makeLocalResolver();
        orb.setLocalResolver(localResolverMakeLocalResolver);
        Resolver resolverMakeBootstrapResolver = ResolverDefault.makeBootstrapResolver(orb, orb.getORBData().getORBInitialHost(), orb.getORBData().getORBInitialPort());
        Operation operationMakeINSURLOperation = ResolverDefault.makeINSURLOperation(orb, resolverMakeBootstrapResolver);
        orb.setURLOperation(operationMakeINSURLOperation);
        orb.setResolver(ResolverDefault.makeCompositeResolver(localResolverMakeLocalResolver, ResolverDefault.makeCompositeResolver(ResolverDefault.makeORBInitRefResolver(operationMakeINSURLOperation, orb.getORBData().getORBInitialReferences()), ResolverDefault.makeCompositeResolver(ResolverDefault.makeORBDefaultInitRefResolver(operationMakeINSURLOperation, orb.getORBData().getORBDefaultInitialReference()), resolverMakeBootstrapResolver))));
    }

    private void initServiceContextRegistry(ORB orb) {
        ServiceContextRegistry serviceContextRegistry = orb.getServiceContextRegistry();
        serviceContextRegistry.register(UEInfoServiceContext.class);
        serviceContextRegistry.register(CodeSetServiceContext.class);
        serviceContextRegistry.register(SendingContextServiceContext.class);
        serviceContextRegistry.register(ORBVersionServiceContext.class);
        serviceContextRegistry.register(MaxStreamFormatVersionServiceContext.class);
    }

    private void registerInitialReferences(final ORB orb) {
        orb.getLocalResolver().register(ORBConstants.DYN_ANY_FACTORY_NAME, ClosureFactory.makeFuture(new Closure() { // from class: com.sun.corba.se.impl.orb.ORBConfiguratorImpl.3
            @Override // com.sun.corba.se.spi.orbutil.closure.Closure
            public Object evaluate() {
                return new DynAnyFactoryImpl(orb);
            }
        }));
    }

    private void initObjectCopiers(ORB orb) {
        ObjectCopierFactory objectCopierFactoryMakeORBStreamObjectCopierFactory = CopyobjectDefaults.makeORBStreamObjectCopierFactory(orb);
        CopierManager copierManager = orb.getCopierManager();
        copierManager.setDefaultId(0);
        copierManager.registerObjectCopierFactory(objectCopierFactoryMakeORBStreamObjectCopierFactory, 0);
    }

    private void initIORFinders(ORB orb) {
        orb.getTaggedProfileFactoryFinder().registerFactory(IIOPFactories.makeIIOPProfileFactory());
        orb.getTaggedProfileTemplateFactoryFinder().registerFactory(IIOPFactories.makeIIOPProfileTemplateFactory());
        TaggedComponentFactoryFinder taggedComponentFactoryFinder = orb.getTaggedComponentFactoryFinder();
        taggedComponentFactoryFinder.registerFactory(IIOPFactories.makeCodeSetsComponentFactory());
        taggedComponentFactoryFinder.registerFactory(IIOPFactories.makeJavaCodebaseComponentFactory());
        taggedComponentFactoryFinder.registerFactory(IIOPFactories.makeORBTypeComponentFactory());
        taggedComponentFactoryFinder.registerFactory(IIOPFactories.makeMaxStreamFormatVersionComponentFactory());
        taggedComponentFactoryFinder.registerFactory(IIOPFactories.makeAlternateIIOPAddressComponentFactory());
        taggedComponentFactoryFinder.registerFactory(IIOPFactories.makeRequestPartitioningComponentFactory());
        taggedComponentFactoryFinder.registerFactory(IIOPFactories.makeJavaSerializationComponentFactory());
        IORFactories.registerValueFactories(orb);
        orb.setObjectKeyFactory(IORFactories.makeObjectKeyFactory(orb));
    }

    private void initRequestDispatcherRegistry(ORB orb) {
        RequestDispatcherRegistry requestDispatcherRegistry = orb.getRequestDispatcherRegistry();
        ClientRequestDispatcher clientRequestDispatcherMakeClientRequestDispatcher = RequestDispatcherDefault.makeClientRequestDispatcher();
        requestDispatcherRegistry.registerClientRequestDispatcher(clientRequestDispatcherMakeClientRequestDispatcher, 2);
        requestDispatcherRegistry.registerClientRequestDispatcher(clientRequestDispatcherMakeClientRequestDispatcher, 32);
        requestDispatcherRegistry.registerClientRequestDispatcher(clientRequestDispatcherMakeClientRequestDispatcher, ORBConstants.PERSISTENT_SCID);
        requestDispatcherRegistry.registerClientRequestDispatcher(clientRequestDispatcherMakeClientRequestDispatcher, 36);
        requestDispatcherRegistry.registerClientRequestDispatcher(clientRequestDispatcherMakeClientRequestDispatcher, ORBConstants.SC_PERSISTENT_SCID);
        requestDispatcherRegistry.registerClientRequestDispatcher(clientRequestDispatcherMakeClientRequestDispatcher, 40);
        requestDispatcherRegistry.registerClientRequestDispatcher(clientRequestDispatcherMakeClientRequestDispatcher, ORBConstants.IISC_PERSISTENT_SCID);
        requestDispatcherRegistry.registerClientRequestDispatcher(clientRequestDispatcherMakeClientRequestDispatcher, 44);
        requestDispatcherRegistry.registerClientRequestDispatcher(clientRequestDispatcherMakeClientRequestDispatcher, ORBConstants.MINSC_PERSISTENT_SCID);
        CorbaServerRequestDispatcher corbaServerRequestDispatcherMakeServerRequestDispatcher = RequestDispatcherDefault.makeServerRequestDispatcher(orb);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeServerRequestDispatcher, 2);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeServerRequestDispatcher, 32);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeServerRequestDispatcher, ORBConstants.PERSISTENT_SCID);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeServerRequestDispatcher, 36);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeServerRequestDispatcher, ORBConstants.SC_PERSISTENT_SCID);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeServerRequestDispatcher, 40);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeServerRequestDispatcher, ORBConstants.IISC_PERSISTENT_SCID);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeServerRequestDispatcher, 44);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeServerRequestDispatcher, ORBConstants.MINSC_PERSISTENT_SCID);
        orb.setINSDelegate(RequestDispatcherDefault.makeINSServerRequestDispatcher(orb));
        requestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(RequestDispatcherDefault.makeJIDLLocalClientRequestDispatcherFactory(orb), 2);
        LocalClientRequestDispatcherFactory localClientRequestDispatcherFactoryMakePOALocalClientRequestDispatcherFactory = RequestDispatcherDefault.makePOALocalClientRequestDispatcherFactory(orb);
        requestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localClientRequestDispatcherFactoryMakePOALocalClientRequestDispatcherFactory, 32);
        requestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localClientRequestDispatcherFactoryMakePOALocalClientRequestDispatcherFactory, ORBConstants.PERSISTENT_SCID);
        LocalClientRequestDispatcherFactory localClientRequestDispatcherFactoryMakeFullServantCacheLocalClientRequestDispatcherFactory = RequestDispatcherDefault.makeFullServantCacheLocalClientRequestDispatcherFactory(orb);
        requestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localClientRequestDispatcherFactoryMakeFullServantCacheLocalClientRequestDispatcherFactory, 36);
        requestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localClientRequestDispatcherFactoryMakeFullServantCacheLocalClientRequestDispatcherFactory, ORBConstants.SC_PERSISTENT_SCID);
        LocalClientRequestDispatcherFactory localClientRequestDispatcherFactoryMakeInfoOnlyServantCacheLocalClientRequestDispatcherFactory = RequestDispatcherDefault.makeInfoOnlyServantCacheLocalClientRequestDispatcherFactory(orb);
        requestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localClientRequestDispatcherFactoryMakeInfoOnlyServantCacheLocalClientRequestDispatcherFactory, 40);
        requestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localClientRequestDispatcherFactoryMakeInfoOnlyServantCacheLocalClientRequestDispatcherFactory, ORBConstants.IISC_PERSISTENT_SCID);
        LocalClientRequestDispatcherFactory localClientRequestDispatcherFactoryMakeMinimalServantCacheLocalClientRequestDispatcherFactory = RequestDispatcherDefault.makeMinimalServantCacheLocalClientRequestDispatcherFactory(orb);
        requestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localClientRequestDispatcherFactoryMakeMinimalServantCacheLocalClientRequestDispatcherFactory, 44);
        requestDispatcherRegistry.registerLocalClientRequestDispatcherFactory(localClientRequestDispatcherFactoryMakeMinimalServantCacheLocalClientRequestDispatcherFactory, ORBConstants.MINSC_PERSISTENT_SCID);
        CorbaServerRequestDispatcher corbaServerRequestDispatcherMakeBootstrapServerRequestDispatcher = RequestDispatcherDefault.makeBootstrapServerRequestDispatcher(orb);
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeBootstrapServerRequestDispatcher, "INIT");
        requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcherMakeBootstrapServerRequestDispatcher, "TINI");
        requestDispatcherRegistry.registerObjectAdapterFactory(OADefault.makeTOAFactory(orb), 2);
        ObjectAdapterFactory objectAdapterFactoryMakePOAFactory = OADefault.makePOAFactory(orb);
        requestDispatcherRegistry.registerObjectAdapterFactory(objectAdapterFactoryMakePOAFactory, 32);
        requestDispatcherRegistry.registerObjectAdapterFactory(objectAdapterFactoryMakePOAFactory, ORBConstants.PERSISTENT_SCID);
        requestDispatcherRegistry.registerObjectAdapterFactory(objectAdapterFactoryMakePOAFactory, 36);
        requestDispatcherRegistry.registerObjectAdapterFactory(objectAdapterFactoryMakePOAFactory, ORBConstants.SC_PERSISTENT_SCID);
        requestDispatcherRegistry.registerObjectAdapterFactory(objectAdapterFactoryMakePOAFactory, 40);
        requestDispatcherRegistry.registerObjectAdapterFactory(objectAdapterFactoryMakePOAFactory, ORBConstants.IISC_PERSISTENT_SCID);
        requestDispatcherRegistry.registerObjectAdapterFactory(objectAdapterFactoryMakePOAFactory, 44);
        requestDispatcherRegistry.registerObjectAdapterFactory(objectAdapterFactoryMakePOAFactory, ORBConstants.MINSC_PERSISTENT_SCID);
    }
}
