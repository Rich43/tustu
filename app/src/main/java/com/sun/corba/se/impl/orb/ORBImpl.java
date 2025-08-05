package com.sun.corba.se.impl.orb;

import com.sun.corba.se.impl.copyobject.CopierManagerImpl;
import com.sun.corba.se.impl.corba.AnyImpl;
import com.sun.corba.se.impl.corba.AsynchInvoke;
import com.sun.corba.se.impl.corba.ContextListImpl;
import com.sun.corba.se.impl.corba.EnvironmentImpl;
import com.sun.corba.se.impl.corba.ExceptionListImpl;
import com.sun.corba.se.impl.corba.NVListImpl;
import com.sun.corba.se.impl.corba.NamedValueImpl;
import com.sun.corba.se.impl.corba.RequestImpl;
import com.sun.corba.se.impl.corba.TypeCodeImpl;
import com.sun.corba.se.impl.encoding.CachedCodeBase;
import com.sun.corba.se.impl.interceptors.PIHandlerImpl;
import com.sun.corba.se.impl.interceptors.PINoOpHandlerImpl;
import com.sun.corba.se.impl.ior.IORTypeCheckRegistryImpl;
import com.sun.corba.se.impl.ior.TaggedComponentFactoryFinderImpl;
import com.sun.corba.se.impl.ior.TaggedProfileFactoryFinderImpl;
import com.sun.corba.se.impl.ior.TaggedProfileTemplateFactoryFinderImpl;
import com.sun.corba.se.impl.legacy.connection.LegacyServerSocketManagerImpl;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
import com.sun.corba.se.impl.oa.poa.POAFactory;
import com.sun.corba.se.impl.oa.toa.TOAFactory;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.orbutil.StackImpl;
import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolManagerImpl;
import com.sun.corba.se.impl.protocol.CorbaInvocationInfo;
import com.sun.corba.se.impl.protocol.RequestDispatcherRegistryImpl;
import com.sun.corba.se.impl.transport.CorbaTransportManagerImpl;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
import com.sun.corba.se.pept.transport.TransportManager;
import com.sun.corba.se.spi.copyobject.CopierManager;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORTypeCheckRegistry;
import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.ObjectKeyFactory;
import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import com.sun.corba.se.spi.orb.DataCollector;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBConfigurator;
import com.sun.corba.se.spi.orb.ORBData;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orb.OperationFactory;
import com.sun.corba.se.spi.orb.ParserImplBase;
import com.sun.corba.se.spi.orb.PropertyParser;
import com.sun.corba.se.spi.orbutil.closure.ClosureFactory;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import com.sun.corba.se.spi.protocol.ClientDelegateFactory;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import com.sun.corba.se.spi.protocol.PIHandler;
import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
import com.sun.corba.se.spi.resolver.LocalResolver;
import com.sun.corba.se.spi.resolver.Resolver;
import com.sun.corba.se.spi.servicecontext.ServiceContextRegistry;
import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
import com.sun.corba.se.spi.transport.CorbaTransportManager;
import com.sun.org.omg.SendingContext.CodeBase;
import java.applet.Applet;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.WeakHashMap;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.Current;
import org.omg.CORBA.Environment;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;
import org.omg.CORBA.Request;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UnionMember;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.WrongTransaction;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ValueFactory;
import org.omg.PortableServer.Servant;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ORBImpl.class */
public class ORBImpl extends ORB {
    protected TransportManager transportManager;
    protected LegacyServerSocketManager legacyServerSocketManager;
    private ThreadLocal OAInvocationInfoStack;
    private ThreadLocal clientInvocationInfoStack;
    private static IOR codeBaseIOR;
    private Vector dynamicRequests;
    private SynchVariable svResponseReceived;
    private static final byte STATUS_OPERATING = 1;
    private static final byte STATUS_SHUTTING_DOWN = 2;
    private static final byte STATUS_SHUTDOWN = 3;
    private static final byte STATUS_DESTROYED = 4;
    private Map typeCodeForClassMap;
    private ThreadLocal orbVersionThreadLocal;
    private RequestDispatcherRegistry requestDispatcherRegistry;
    private CopierManager copierManager;
    private int transientServerId;
    private ServiceContextRegistry serviceContextRegistry;
    private IORTypeCheckRegistry iorTypeCheckRegistry;
    private TOAFactory toaFactory;
    private POAFactory poaFactory;
    private PIHandler pihandler;
    private ORBData configData;
    private BadServerIdHandler badServerIdHandler;
    private ClientDelegateFactory clientDelegateFactory;
    private CorbaContactInfoListFactory corbaContactInfoListFactory;
    private Resolver resolver;
    private LocalResolver localResolver;
    private Operation urlOperation;
    private CorbaServerRequestDispatcher insNamingDelegate;
    private static final String IORTYPECHECKREGISTRY_FILTER_PROPNAME = "com.sun.CORBA.ORBIorTypeCheckRegistryFilter";
    private TaggedComponentFactoryFinder taggedComponentFactoryFinder;
    private IdentifiableFactoryFinder taggedProfileFactoryFinder;
    private IdentifiableFactoryFinder taggedProfileTemplateFactoryFinder;
    private ObjectKeyFactory objectKeyFactory;
    private ThreadPoolManager threadpoolMgr;
    private static String localHostString = null;
    private Object runObj = new Object();
    private Object shutdownObj = new Object();
    private Object waitForCompletionObj = new Object();
    private byte status = 1;
    private Object invocationObj = new Object();
    private int numInvocations = 0;
    private ThreadLocal isProcessingInvocation = new ThreadLocal() { // from class: com.sun.corba.se.impl.orb.ORBImpl.1
        @Override // java.lang.ThreadLocal
        protected Object initialValue() {
            return Boolean.FALSE;
        }
    };
    private Hashtable valueFactoryCache = new Hashtable();
    private final Object urlOperationLock = new Object();
    private final Object resolverLock = new Object();
    private boolean orbOwnsThreadPoolManager = false;
    private Object badServerIdHandlerAccessLock = new Object();
    private Object clientDelegateFactoryAccessorLock = new Object();
    private Object corbaContactInfoListFactoryAccessLock = new Object();
    private Object objectKeyFactoryAccessLock = new Object();
    private Object transportManagerAccessorLock = new Object();
    private Object legacyServerSocketManagerAccessLock = new Object();
    private Object threadPoolManagerAccessLock = new Object();

    private void dprint(String str) {
        ORBUtility.dprint(this, str);
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ORBData getORBData() {
        return this.configData;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public PIHandler getPIHandler() {
        return this.pihandler;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ORBVersion getORBVersion() {
        synchronized (this) {
            checkShutdownState();
        }
        return (ORBVersion) this.orbVersionThreadLocal.get();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setORBVersion(ORBVersion oRBVersion) {
        synchronized (this) {
            checkShutdownState();
        }
        this.orbVersionThreadLocal.set(oRBVersion);
    }

    private void preInit(String[] strArr, Properties properties) {
        this.pihandler = new PINoOpHandlerImpl();
        this.transientServerId = (int) System.currentTimeMillis();
        this.orbVersionThreadLocal = new ThreadLocal() { // from class: com.sun.corba.se.impl.orb.ORBImpl.2
            @Override // java.lang.ThreadLocal
            protected Object initialValue() {
                return ORBVersionFactory.getORBVersion();
            }
        };
        this.requestDispatcherRegistry = new RequestDispatcherRegistryImpl(this, 2);
        this.copierManager = new CopierManagerImpl(this);
        this.taggedComponentFactoryFinder = new TaggedComponentFactoryFinderImpl(this);
        this.taggedProfileFactoryFinder = new TaggedProfileFactoryFinderImpl(this);
        this.taggedProfileTemplateFactoryFinder = new TaggedProfileTemplateFactoryFinderImpl(this);
        this.dynamicRequests = new Vector();
        this.svResponseReceived = new SynchVariable();
        this.OAInvocationInfoStack = new ThreadLocal() { // from class: com.sun.corba.se.impl.orb.ORBImpl.3
            @Override // java.lang.ThreadLocal
            protected Object initialValue() {
                return new StackImpl();
            }
        };
        this.clientInvocationInfoStack = new ThreadLocal() { // from class: com.sun.corba.se.impl.orb.ORBImpl.4
            @Override // java.lang.ThreadLocal
            protected Object initialValue() {
                return new StackImpl();
            }
        };
        this.serviceContextRegistry = new ServiceContextRegistry(this);
    }

    private void initIORTypeCheckRegistry() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.corba.se.impl.orb.ORBImpl.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                String property = System.getProperty(ORBImpl.IORTYPECHECKREGISTRY_FILTER_PROPNAME);
                if (property == null) {
                    property = Security.getProperty(ORBImpl.IORTYPECHECKREGISTRY_FILTER_PROPNAME);
                }
                return property;
            }
        });
        if (str != null) {
            try {
                this.iorTypeCheckRegistry = new IORTypeCheckRegistryImpl(str, this);
                if (this.orbInitDebugFlag) {
                    dprint(".initIORTypeCheckRegistry, IORTypeCheckRegistryImpl created for properties == " + str);
                    return;
                }
                return;
            } catch (Exception e2) {
                throw this.wrapper.bootstrapException(e2);
            }
        }
        if (this.orbInitDebugFlag) {
            dprint(".initIORTypeCheckRegistry, IORTypeCheckRegistryImpl NOT created for properties == ");
        }
    }

    protected void setDebugFlags(String[] strArr) {
        for (String str : strArr) {
            try {
                Field field = getClass().getField(str + "DebugFlag");
                int modifiers = field.getModifiers();
                if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers) && field.getType() == Boolean.TYPE) {
                    field.setBoolean(this, true);
                }
            } catch (Exception e2) {
            }
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ORBImpl$ConfigParser.class */
    private static class ConfigParser extends ParserImplBase {
        public Class configurator;

        private ConfigParser() {
            this.configurator = ORBConfiguratorImpl.class;
        }

        @Override // com.sun.corba.se.spi.orb.ParserImplBase
        public PropertyParser makeParser() {
            PropertyParser propertyParser = new PropertyParser();
            propertyParser.add("com.sun.CORBA.ORBConfigurator", OperationFactory.classAction(), "configurator");
            return propertyParser;
        }
    }

    private void postInit(String[] strArr, DataCollector dataCollector) {
        this.configData = new ORBDataParserImpl(this, dataCollector);
        setDebugFlags(this.configData.getORBDebugFlags());
        getTransportManager();
        getLegacyServerSocketManager();
        ConfigParser configParser = new ConfigParser();
        configParser.init(dataCollector);
        try {
            try {
                ((ORBConfigurator) configParser.configurator.newInstance()).configure(dataCollector, this);
                this.pihandler = new PIHandlerImpl(this, strArr);
                this.pihandler.initialize();
                getThreadPoolManager();
                super.getByteBufferPool();
                initIORTypeCheckRegistry();
            } catch (Exception e2) {
                throw this.wrapper.orbConfiguratorError(e2);
            }
        } catch (Exception e3) {
            throw this.wrapper.badOrbConfigurator(e3, configParser.configurator.getName());
        }
    }

    private synchronized POAFactory getPOAFactory() {
        if (this.poaFactory == null) {
            this.poaFactory = (POAFactory) this.requestDispatcherRegistry.getObjectAdapterFactory(32);
        }
        return this.poaFactory;
    }

    private synchronized TOAFactory getTOAFactory() {
        if (this.toaFactory == null) {
            this.toaFactory = (TOAFactory) this.requestDispatcherRegistry.getObjectAdapterFactory(2);
        }
        return this.toaFactory;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void set_parameters(Properties properties) {
        synchronized (this) {
            checkShutdownState();
        }
        preInit(null, properties);
        postInit(null, DataCollectorFactory.create(properties, getLocalHostName()));
    }

    @Override // org.omg.CORBA.ORB
    protected void set_parameters(Applet applet, Properties properties) {
        preInit(null, properties);
        postInit(null, DataCollectorFactory.create(applet, properties, getLocalHostName()));
    }

    @Override // org.omg.CORBA.ORB
    protected void set_parameters(String[] strArr, Properties properties) {
        preInit(strArr, properties);
        postInit(strArr, DataCollectorFactory.create(strArr, properties, getLocalHostName()));
    }

    @Override // org.omg.CORBA.ORB
    public synchronized OutputStream create_output_stream() {
        checkShutdownState();
        return OutputStreamFactory.newEncapsOutputStream(this);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized Current get_current() {
        checkShutdownState();
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA.ORB
    public synchronized NVList create_list(int i2) {
        checkShutdownState();
        return new NVListImpl(this, i2);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized NVList create_operation_list(Object object) {
        checkShutdownState();
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA.ORB
    public synchronized NamedValue create_named_value(String str, Any any, int i2) {
        checkShutdownState();
        return new NamedValueImpl(this, str, any, i2);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized ExceptionList create_exception_list() {
        checkShutdownState();
        return new ExceptionListImpl();
    }

    @Override // org.omg.CORBA.ORB
    public synchronized ContextList create_context_list() {
        checkShutdownState();
        return new ContextListImpl(this);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized Context get_default_context() {
        checkShutdownState();
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA.ORB
    public synchronized Environment create_environment() {
        checkShutdownState();
        return new EnvironmentImpl();
    }

    @Override // org.omg.CORBA.ORB
    public synchronized void send_multiple_requests_oneway(Request[] requestArr) {
        checkShutdownState();
        for (Request request : requestArr) {
            request.send_oneway();
        }
    }

    @Override // org.omg.CORBA.ORB
    public synchronized void send_multiple_requests_deferred(Request[] requestArr) {
        checkShutdownState();
        for (Request request : requestArr) {
            this.dynamicRequests.addElement(request);
        }
        for (Request request2 : requestArr) {
            new Thread(new AsynchInvoke(this, (RequestImpl) request2, true)).start();
        }
    }

    @Override // org.omg.CORBA.ORB
    public synchronized boolean poll_next_response() {
        checkShutdownState();
        Enumeration enumerationElements = this.dynamicRequests.elements();
        while (enumerationElements.hasMoreElements()) {
            if (((Request) enumerationElements.nextElement2()).poll_response()) {
                return true;
            }
        }
        return false;
    }

    @Override // org.omg.CORBA.ORB
    public Request get_next_response() throws WrongTransaction {
        synchronized (this) {
            checkShutdownState();
        }
        while (true) {
            synchronized (this.dynamicRequests) {
                Enumeration enumerationElements = this.dynamicRequests.elements();
                while (enumerationElements.hasMoreElements()) {
                    Request request = (Request) enumerationElements.nextElement2();
                    if (request.poll_response()) {
                        request.get_response();
                        this.dynamicRequests.removeElement(request);
                        return request;
                    }
                }
            }
            synchronized (this.svResponseReceived) {
                while (!this.svResponseReceived.value()) {
                    try {
                        this.svResponseReceived.wait();
                    } catch (InterruptedException e2) {
                    }
                }
                this.svResponseReceived.reset();
            }
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void notifyORB() {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.svResponseReceived) {
            this.svResponseReceived.set();
            this.svResponseReceived.notify();
        }
    }

    @Override // org.omg.CORBA.ORB
    public synchronized String object_to_string(Object object) {
        checkShutdownState();
        if (object == null) {
            return IORFactories.makeIOR(this).stringify();
        }
        try {
            return ORBUtility.connectAndGetIOR(this, object).stringify();
        } catch (BAD_PARAM e2) {
            if (e2.minor == 1398079694) {
                throw this.omgWrapper.notAnObjectImpl(e2);
            }
            throw e2;
        }
    }

    @Override // org.omg.CORBA.ORB
    public Object string_to_object(String str) {
        Operation operation;
        Object object;
        synchronized (this) {
            checkShutdownState();
            operation = this.urlOperation;
        }
        if (str == null) {
            throw this.wrapper.nullParam();
        }
        synchronized (this.urlOperationLock) {
            object = (Object) operation.operate(str);
        }
        return object;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public synchronized IOR getFVDCodeBaseIOR() {
        checkShutdownState();
        if (codeBaseIOR != null) {
            return codeBaseIOR;
        }
        return ORBUtility.connectAndGetIOR(this, (CodeBase) ORBUtility.createValueHandler().getRunTimeCodeBase());
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode get_primitive_tc(TCKind tCKind) {
        checkShutdownState();
        return get_primitive_tc(tCKind.value());
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_struct_tc(String str, String str2, StructMember[] structMemberArr) {
        checkShutdownState();
        return new TypeCodeImpl(this, 15, str, str2, structMemberArr);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_union_tc(String str, String str2, TypeCode typeCode, UnionMember[] unionMemberArr) {
        checkShutdownState();
        return new TypeCodeImpl(this, 16, str, str2, typeCode, unionMemberArr);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_enum_tc(String str, String str2, String[] strArr) {
        checkShutdownState();
        return new TypeCodeImpl(this, 17, str, str2, strArr);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_alias_tc(String str, String str2, TypeCode typeCode) {
        checkShutdownState();
        return new TypeCodeImpl(this, 21, str, str2, typeCode);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_exception_tc(String str, String str2, StructMember[] structMemberArr) {
        checkShutdownState();
        return new TypeCodeImpl(this, 22, str, str2, structMemberArr);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_interface_tc(String str, String str2) {
        checkShutdownState();
        return new TypeCodeImpl(this, 14, str, str2);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_string_tc(int i2) {
        checkShutdownState();
        return new TypeCodeImpl(this, 18, i2);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_wstring_tc(int i2) {
        checkShutdownState();
        return new TypeCodeImpl(this, 27, i2);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_sequence_tc(int i2, TypeCode typeCode) {
        checkShutdownState();
        return new TypeCodeImpl(this, 19, i2, typeCode);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_recursive_sequence_tc(int i2, int i3) {
        checkShutdownState();
        return new TypeCodeImpl(this, 19, i2, i3);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_array_tc(int i2, TypeCode typeCode) {
        checkShutdownState();
        return new TypeCodeImpl(this, 20, i2, typeCode);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_native_tc(String str, String str2) {
        checkShutdownState();
        return new TypeCodeImpl(this, 31, str, str2);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_abstract_interface_tc(String str, String str2) {
        checkShutdownState();
        return new TypeCodeImpl(this, 32, str, str2);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_fixed_tc(short s2, short s3) {
        checkShutdownState();
        return new TypeCodeImpl((ORB) this, 28, s2, s3);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_value_tc(String str, String str2, short s2, TypeCode typeCode, ValueMember[] valueMemberArr) {
        checkShutdownState();
        return new TypeCodeImpl(this, 29, str, str2, s2, typeCode, valueMemberArr);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_recursive_tc(String str) {
        checkShutdownState();
        return new TypeCodeImpl(this, str);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized TypeCode create_value_box_tc(String str, String str2, TypeCode typeCode) {
        checkShutdownState();
        return new TypeCodeImpl(this, 30, str, str2, typeCode);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized Any create_any() {
        checkShutdownState();
        return new AnyImpl(this);
    }

    @Override // com.sun.corba.se.impl.corba.TypeCodeFactory
    public synchronized void setTypeCodeForClass(Class cls, TypeCodeImpl typeCodeImpl) {
        checkShutdownState();
        if (this.typeCodeForClassMap == null) {
            this.typeCodeForClassMap = Collections.synchronizedMap(new WeakHashMap(64));
        }
        if (!this.typeCodeForClassMap.containsKey(cls)) {
            this.typeCodeForClassMap.put(cls, typeCodeImpl);
        }
    }

    @Override // com.sun.corba.se.impl.corba.TypeCodeFactory
    public synchronized TypeCodeImpl getTypeCodeForClass(Class cls) {
        checkShutdownState();
        if (this.typeCodeForClassMap == null) {
            return null;
        }
        return (TypeCodeImpl) this.typeCodeForClassMap.get(cls);
    }

    @Override // org.omg.CORBA.ORB
    public String[] list_initial_services() {
        Resolver resolver;
        String[] strArr;
        synchronized (this) {
            checkShutdownState();
            resolver = this.resolver;
        }
        synchronized (this.resolverLock) {
            Set list = resolver.list();
            strArr = (String[]) list.toArray(new String[list.size()]);
        }
        return strArr;
    }

    @Override // org.omg.CORBA.ORB
    public Object resolve_initial_references(String str) throws InvalidName {
        Resolver resolver;
        Object objectResolve;
        synchronized (this) {
            checkShutdownState();
            resolver = this.resolver;
        }
        synchronized (this.resolverLock) {
            objectResolve = resolver.resolve(str);
            if (objectResolve == null) {
                throw new InvalidName();
            }
        }
        return objectResolve;
    }

    @Override // com.sun.corba.se.org.omg.CORBA.ORB
    public void register_initial_reference(String str, Object object) throws InvalidName {
        CorbaServerRequestDispatcher corbaServerRequestDispatcher;
        synchronized (this) {
            checkShutdownState();
        }
        if (str == null || str.length() == 0) {
            throw new InvalidName();
        }
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.resolverLock) {
            corbaServerRequestDispatcher = this.insNamingDelegate;
            if (this.localResolver.resolve(str) != null) {
                throw new InvalidName(str + " already registered");
            }
            this.localResolver.register(str, ClosureFactory.makeConstant(object));
        }
        synchronized (this) {
            if (StubAdapter.isStub(object)) {
                this.requestDispatcherRegistry.registerServerRequestDispatcher(corbaServerRequestDispatcher, str);
            }
        }
    }

    @Override // org.omg.CORBA.ORB
    public void run() {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.runObj) {
            try {
                this.runObj.wait();
            } catch (InterruptedException e2) {
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x0074, code lost:
    
        r3.shutdownObj.wait();
     */
    @Override // org.omg.CORBA.ORB
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void shutdown(boolean r4) {
        /*
            Method dump skipped, instructions count: 238
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.orb.ORBImpl.shutdown(boolean):void");
    }

    protected void shutdownServants(boolean z2) {
        HashSet hashSet;
        synchronized (this) {
            hashSet = new HashSet(this.requestDispatcherRegistry.getObjectAdapterFactories());
        }
        Iterator<E> it = hashSet.iterator();
        while (it.hasNext()) {
            ((ObjectAdapterFactory) it.next()).shutdown(z2);
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void checkShutdownState() {
        if (this.status == 4) {
            throw this.wrapper.orbDestroyed();
        }
        if (this.status == 3) {
            throw this.omgWrapper.badOperationAfterShutdown();
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public boolean isDuringDispatch() {
        synchronized (this) {
            checkShutdownState();
        }
        return ((Boolean) this.isProcessingInvocation.get()).booleanValue();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void startingDispatch() {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.invocationObj) {
            this.isProcessingInvocation.set(Boolean.TRUE);
            this.numInvocations++;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void finishedDispatch() {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.invocationObj) {
            this.numInvocations--;
            this.isProcessingInvocation.set(false);
            if (this.numInvocations == 0) {
                synchronized (this.waitForCompletionObj) {
                    this.waitForCompletionObj.notifyAll();
                }
            } else if (this.numInvocations < 0) {
                throw this.wrapper.numInvocationsAlreadyZero(CompletionStatus.COMPLETED_YES);
            }
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB, org.omg.CORBA.ORB
    public void destroy() {
        boolean z2;
        synchronized (this) {
            z2 = this.status == 1;
        }
        if (z2) {
            shutdown(true);
        }
        synchronized (this) {
            if (this.status < 4) {
                getCorbaTransportManager().close();
                getPIHandler().destroyInterceptors();
                this.status = (byte) 4;
            }
        }
        synchronized (this.threadPoolManagerAccessLock) {
            if (this.orbOwnsThreadPoolManager) {
                try {
                    this.threadpoolMgr.close();
                    this.threadpoolMgr = null;
                } catch (IOException e2) {
                    this.wrapper.ioExceptionOnClose(e2);
                }
            }
        }
        try {
            this.monitoringManager.close();
            this.monitoringManager = null;
        } catch (IOException e3) {
            this.wrapper.ioExceptionOnClose(e3);
        }
        CachedCodeBase.cleanCache(this);
        try {
            this.pihandler.close();
        } catch (IOException e4) {
            this.wrapper.ioExceptionOnClose(e4);
        }
        super.destroy();
        this.badServerIdHandlerAccessLock = null;
        this.clientDelegateFactoryAccessorLock = null;
        this.corbaContactInfoListFactoryAccessLock = null;
        this.objectKeyFactoryAccessLock = null;
        this.legacyServerSocketManagerAccessLock = null;
        this.threadPoolManagerAccessLock = null;
        this.transportManager = null;
        this.legacyServerSocketManager = null;
        this.OAInvocationInfoStack = null;
        this.clientInvocationInfoStack = null;
        codeBaseIOR = null;
        this.dynamicRequests = null;
        this.svResponseReceived = null;
        this.runObj = null;
        this.shutdownObj = null;
        this.waitForCompletionObj = null;
        this.invocationObj = null;
        this.isProcessingInvocation = null;
        this.typeCodeForClassMap = null;
        this.valueFactoryCache = null;
        this.orbVersionThreadLocal = null;
        this.requestDispatcherRegistry = null;
        this.copierManager = null;
        this.toaFactory = null;
        this.poaFactory = null;
        this.pihandler = null;
        this.configData = null;
        this.badServerIdHandler = null;
        this.clientDelegateFactory = null;
        this.corbaContactInfoListFactory = null;
        this.resolver = null;
        this.localResolver = null;
        this.insNamingDelegate = null;
        this.urlOperation = null;
        this.taggedComponentFactoryFinder = null;
        this.taggedProfileFactoryFinder = null;
        this.taggedProfileTemplateFactoryFinder = null;
        this.objectKeyFactory = null;
    }

    @Override // org.omg.CORBA_2_3.ORB
    public synchronized ValueFactory register_value_factory(String str, ValueFactory valueFactory) {
        checkShutdownState();
        if (str == null || valueFactory == null) {
            throw this.omgWrapper.unableRegisterValueFactory();
        }
        return (ValueFactory) this.valueFactoryCache.put(str, valueFactory);
    }

    @Override // org.omg.CORBA_2_3.ORB
    public synchronized void unregister_value_factory(String str) {
        checkShutdownState();
        if (this.valueFactoryCache.remove(str) == null) {
            throw this.wrapper.nullParam();
        }
    }

    @Override // org.omg.CORBA_2_3.ORB
    public synchronized ValueFactory lookup_value_factory(String str) {
        checkShutdownState();
        ValueFactory factory = (ValueFactory) this.valueFactoryCache.get(str);
        if (factory == null) {
            try {
                factory = Utility.getFactory(null, null, null, str);
            } catch (MARSHAL e2) {
                throw this.wrapper.unableFindValueFactory(e2);
            }
        }
        return factory;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public OAInvocationInfo peekInvocationInfo() {
        synchronized (this) {
            checkShutdownState();
        }
        return (OAInvocationInfo) ((StackImpl) this.OAInvocationInfoStack.get()).peek();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void pushInvocationInfo(OAInvocationInfo oAInvocationInfo) {
        synchronized (this) {
            checkShutdownState();
        }
        ((StackImpl) this.OAInvocationInfoStack.get()).push(oAInvocationInfo);
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public OAInvocationInfo popInvocationInfo() {
        synchronized (this) {
            checkShutdownState();
        }
        return (OAInvocationInfo) ((StackImpl) this.OAInvocationInfoStack.get()).pop();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void initBadServerIdHandler() {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.badServerIdHandlerAccessLock) {
            Class badServerIdHandler = this.configData.getBadServerIdHandler();
            if (badServerIdHandler != null) {
                try {
                    this.badServerIdHandler = (BadServerIdHandler) badServerIdHandler.getConstructor(org.omg.CORBA.ORB.class).newInstance(this);
                } catch (Exception e2) {
                    throw this.wrapper.errorInitBadserveridhandler(e2);
                }
            }
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setBadServerIdHandler(BadServerIdHandler badServerIdHandler) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.badServerIdHandlerAccessLock) {
            this.badServerIdHandler = badServerIdHandler;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void handleBadServerId(ObjectKey objectKey) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.badServerIdHandlerAccessLock) {
            if (this.badServerIdHandler == null) {
                throw this.wrapper.badServerId();
            }
            this.badServerIdHandler.handle(objectKey);
        }
    }

    @Override // org.omg.CORBA.ORB
    public synchronized Policy create_policy(int i2, Any any) throws PolicyError {
        checkShutdownState();
        return this.pihandler.create_policy(i2, any);
    }

    @Override // org.omg.CORBA.ORB
    public synchronized void connect(Object object) {
        checkShutdownState();
        if (getTOAFactory() == null) {
            throw this.wrapper.noToa();
        }
        try {
            getTOAFactory().getTOA(Util.getCodebase(object.getClass())).connect(object);
        } catch (Exception e2) {
            throw this.wrapper.orbConnectError(e2);
        }
    }

    @Override // org.omg.CORBA.ORB
    public synchronized void disconnect(Object object) {
        checkShutdownState();
        if (getTOAFactory() == null) {
            throw this.wrapper.noToa();
        }
        try {
            getTOAFactory().getTOA().disconnect(object);
        } catch (Exception e2) {
            throw this.wrapper.orbConnectError(e2);
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public int getTransientServerId() {
        synchronized (this) {
            checkShutdownState();
        }
        if (this.configData.getORBServerIdPropertySpecified()) {
            return this.configData.getPersistentServerId();
        }
        return this.transientServerId;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public RequestDispatcherRegistry getRequestDispatcherRegistry() {
        synchronized (this) {
            checkShutdownState();
        }
        return this.requestDispatcherRegistry;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ServiceContextRegistry getServiceContextRegistry() {
        synchronized (this) {
            checkShutdownState();
        }
        return this.serviceContextRegistry;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public boolean isLocalHost(String str) {
        synchronized (this) {
            checkShutdownState();
        }
        return str.equals(this.configData.getORBServerHost()) || str.equals(getLocalHostName());
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public boolean isLocalServerId(int i2, int i3) {
        synchronized (this) {
            checkShutdownState();
        }
        return (i2 < 32 || i2 > 63) ? i3 == getTransientServerId() : ORBConstants.isTransient(i2) ? i3 == getTransientServerId() : this.configData.getPersistentServerIdInitialized() && i3 == this.configData.getPersistentServerId();
    }

    private String getHostName(String str) throws UnknownHostException {
        return InetAddress.getByName(str).getHostAddress();
    }

    private synchronized String getLocalHostName() {
        if (localHostString == null) {
            try {
                localHostString = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e2) {
                throw this.wrapper.getLocalHostFailed(e2);
            }
        }
        return localHostString;
    }

    @Override // org.omg.CORBA.ORB
    public synchronized boolean work_pending() {
        checkShutdownState();
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA.ORB
    public synchronized void perform_work() {
        checkShutdownState();
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA_2_3.ORB
    public synchronized void set_delegate(Object obj) {
        checkShutdownState();
        POAFactory pOAFactory = getPOAFactory();
        if (pOAFactory != null) {
            ((Servant) obj)._set_delegate(pOAFactory.getDelegateImpl());
            return;
        }
        throw this.wrapper.noPoa();
    }

    @Override // com.sun.corba.se.pept.broker.Broker
    public ClientInvocationInfo createOrIncrementInvocationInfo() {
        synchronized (this) {
            checkShutdownState();
        }
        StackImpl stackImpl = (StackImpl) this.clientInvocationInfoStack.get();
        ClientInvocationInfo corbaInvocationInfo = null;
        if (!stackImpl.empty()) {
            corbaInvocationInfo = (ClientInvocationInfo) stackImpl.peek();
        }
        if (corbaInvocationInfo == null || !corbaInvocationInfo.isRetryInvocation()) {
            corbaInvocationInfo = new CorbaInvocationInfo(this);
            startingDispatch();
            stackImpl.push(corbaInvocationInfo);
        }
        corbaInvocationInfo.setIsRetryInvocation(false);
        corbaInvocationInfo.incrementEntryCount();
        return corbaInvocationInfo;
    }

    @Override // com.sun.corba.se.pept.broker.Broker
    public void releaseOrDecrementInvocationInfo() {
        synchronized (this) {
            checkShutdownState();
        }
        StackImpl stackImpl = (StackImpl) this.clientInvocationInfoStack.get();
        if (!stackImpl.empty()) {
            ClientInvocationInfo clientInvocationInfo = (ClientInvocationInfo) stackImpl.peek();
            clientInvocationInfo.decrementEntryCount();
            clientInvocationInfo.getEntryCount();
            if (clientInvocationInfo.getEntryCount() == 0) {
                if (!clientInvocationInfo.isRetryInvocation()) {
                    stackImpl.pop();
                }
                finishedDispatch();
                return;
            }
            return;
        }
        throw this.wrapper.invocationInfoStackEmpty();
    }

    @Override // com.sun.corba.se.pept.broker.Broker
    public ClientInvocationInfo getInvocationInfo() {
        synchronized (this) {
            checkShutdownState();
        }
        return (ClientInvocationInfo) ((StackImpl) this.clientInvocationInfoStack.get()).peek();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setClientDelegateFactory(ClientDelegateFactory clientDelegateFactory) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.clientDelegateFactoryAccessorLock) {
            this.clientDelegateFactory = clientDelegateFactory;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ClientDelegateFactory getClientDelegateFactory() {
        ClientDelegateFactory clientDelegateFactory;
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.clientDelegateFactoryAccessorLock) {
            clientDelegateFactory = this.clientDelegateFactory;
        }
        return clientDelegateFactory;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setCorbaContactInfoListFactory(CorbaContactInfoListFactory corbaContactInfoListFactory) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.corbaContactInfoListFactoryAccessLock) {
            this.corbaContactInfoListFactory = corbaContactInfoListFactory;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public synchronized CorbaContactInfoListFactory getCorbaContactInfoListFactory() {
        checkShutdownState();
        return this.corbaContactInfoListFactory;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setResolver(Resolver resolver) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.resolverLock) {
            this.resolver = resolver;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public Resolver getResolver() {
        Resolver resolver;
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.resolverLock) {
            resolver = this.resolver;
        }
        return resolver;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setLocalResolver(LocalResolver localResolver) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.resolverLock) {
            this.localResolver = localResolver;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public LocalResolver getLocalResolver() {
        LocalResolver localResolver;
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.resolverLock) {
            localResolver = this.localResolver;
        }
        return localResolver;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setURLOperation(Operation operation) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.urlOperationLock) {
            this.urlOperation = operation;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public Operation getURLOperation() {
        Operation operation;
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.urlOperationLock) {
            operation = this.urlOperation;
        }
        return operation;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setINSDelegate(CorbaServerRequestDispatcher corbaServerRequestDispatcher) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.resolverLock) {
            this.insNamingDelegate = corbaServerRequestDispatcher;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public TaggedComponentFactoryFinder getTaggedComponentFactoryFinder() {
        synchronized (this) {
            checkShutdownState();
        }
        return this.taggedComponentFactoryFinder;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public IdentifiableFactoryFinder getTaggedProfileFactoryFinder() {
        synchronized (this) {
            checkShutdownState();
        }
        return this.taggedProfileFactoryFinder;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public IdentifiableFactoryFinder getTaggedProfileTemplateFactoryFinder() {
        synchronized (this) {
            checkShutdownState();
        }
        return this.taggedProfileTemplateFactoryFinder;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ObjectKeyFactory getObjectKeyFactory() {
        ObjectKeyFactory objectKeyFactory;
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.objectKeyFactoryAccessLock) {
            objectKeyFactory = this.objectKeyFactory;
        }
        return objectKeyFactory;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setObjectKeyFactory(ObjectKeyFactory objectKeyFactory) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.objectKeyFactoryAccessLock) {
            this.objectKeyFactory = objectKeyFactory;
        }
    }

    @Override // com.sun.corba.se.pept.broker.Broker
    public TransportManager getTransportManager() {
        TransportManager transportManager;
        synchronized (this.transportManagerAccessorLock) {
            if (this.transportManager == null) {
                this.transportManager = new CorbaTransportManagerImpl(this);
            }
            transportManager = this.transportManager;
        }
        return transportManager;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public CorbaTransportManager getCorbaTransportManager() {
        return (CorbaTransportManager) getTransportManager();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public LegacyServerSocketManager getLegacyServerSocketManager() {
        LegacyServerSocketManager legacyServerSocketManager;
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.legacyServerSocketManagerAccessLock) {
            if (this.legacyServerSocketManager == null) {
                this.legacyServerSocketManager = new LegacyServerSocketManagerImpl(this);
            }
            legacyServerSocketManager = this.legacyServerSocketManager;
        }
        return legacyServerSocketManager;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setThreadPoolManager(ThreadPoolManager threadPoolManager) {
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.threadPoolManagerAccessLock) {
            this.threadpoolMgr = threadPoolManager;
        }
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ThreadPoolManager getThreadPoolManager() {
        ThreadPoolManager threadPoolManager;
        synchronized (this) {
            checkShutdownState();
        }
        synchronized (this.threadPoolManagerAccessLock) {
            if (this.threadpoolMgr == null) {
                this.threadpoolMgr = new ThreadPoolManagerImpl();
                this.orbOwnsThreadPoolManager = true;
            }
            threadPoolManager = this.threadpoolMgr;
        }
        return threadPoolManager;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public CopierManager getCopierManager() {
        synchronized (this) {
            checkShutdownState();
        }
        return this.copierManager;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void validateIORClass(String str) {
        if (this.iorTypeCheckRegistry != null && !this.iorTypeCheckRegistry.isValidIORType(str)) {
            throw ORBUtilSystemException.get(this, CORBALogDomains.OA_IOR).badStringifiedIor();
        }
    }
}
