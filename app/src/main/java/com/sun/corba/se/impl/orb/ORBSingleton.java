package com.sun.corba.se.impl.orb;

import com.sun.corba.se.impl.corba.AnyImpl;
import com.sun.corba.se.impl.corba.ContextListImpl;
import com.sun.corba.se.impl.corba.EnvironmentImpl;
import com.sun.corba.se.impl.corba.ExceptionListImpl;
import com.sun.corba.se.impl.corba.NVListImpl;
import com.sun.corba.se.impl.corba.NamedValueImpl;
import com.sun.corba.se.impl.corba.TypeCodeImpl;
import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
import com.sun.corba.se.pept.transport.ConnectionCache;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.pept.transport.Selector;
import com.sun.corba.se.pept.transport.TransportManager;
import com.sun.corba.se.spi.copyobject.CopierManager;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.ObjectKeyFactory;
import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBData;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orbutil.closure.Closure;
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
import java.applet.Applet;
import java.net.URL;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;
import org.omg.CORBA.Any;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.Current;
import org.omg.CORBA.Environment;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NO_IMPLEMENT;
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
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ValueFactory;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ORBSingleton.class */
public class ORBSingleton extends ORB {
    private ORB fullORB;
    private static PresentationManager.StubFactoryFactory staticStubFactoryFactory = PresentationDefaults.getStaticStubFactoryFactory();

    @Override // com.sun.corba.se.spi.orb.ORB
    public void set_parameters(Properties properties) {
    }

    @Override // org.omg.CORBA.ORB
    protected void set_parameters(Applet applet, Properties properties) {
    }

    @Override // org.omg.CORBA.ORB
    protected void set_parameters(String[] strArr, Properties properties) {
    }

    @Override // org.omg.CORBA.ORB
    public OutputStream create_output_stream() {
        return OutputStreamFactory.newEncapsOutputStream(this);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_struct_tc(String str, String str2, StructMember[] structMemberArr) {
        return new TypeCodeImpl(this, 15, str, str2, structMemberArr);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_union_tc(String str, String str2, TypeCode typeCode, UnionMember[] unionMemberArr) {
        return new TypeCodeImpl(this, 16, str, str2, typeCode, unionMemberArr);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_enum_tc(String str, String str2, String[] strArr) {
        return new TypeCodeImpl(this, 17, str, str2, strArr);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_alias_tc(String str, String str2, TypeCode typeCode) {
        return new TypeCodeImpl(this, 21, str, str2, typeCode);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_exception_tc(String str, String str2, StructMember[] structMemberArr) {
        return new TypeCodeImpl(this, 22, str, str2, structMemberArr);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_interface_tc(String str, String str2) {
        return new TypeCodeImpl(this, 14, str, str2);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_string_tc(int i2) {
        return new TypeCodeImpl(this, 18, i2);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_wstring_tc(int i2) {
        return new TypeCodeImpl(this, 27, i2);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_sequence_tc(int i2, TypeCode typeCode) {
        return new TypeCodeImpl(this, 19, i2, typeCode);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_recursive_sequence_tc(int i2, int i3) {
        return new TypeCodeImpl(this, 19, i2, i3);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_array_tc(int i2, TypeCode typeCode) {
        return new TypeCodeImpl(this, 20, i2, typeCode);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_native_tc(String str, String str2) {
        return new TypeCodeImpl(this, 31, str, str2);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_abstract_interface_tc(String str, String str2) {
        return new TypeCodeImpl(this, 32, str, str2);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_fixed_tc(short s2, short s3) {
        return new TypeCodeImpl((ORB) this, 28, s2, s3);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_value_tc(String str, String str2, short s2, TypeCode typeCode, ValueMember[] valueMemberArr) {
        return new TypeCodeImpl(this, 29, str, str2, s2, typeCode, valueMemberArr);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_recursive_tc(String str) {
        return new TypeCodeImpl(this, str);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode create_value_box_tc(String str, String str2, TypeCode typeCode) {
        return new TypeCodeImpl(this, 30, str, str2, typeCode);
    }

    @Override // org.omg.CORBA.ORB
    public TypeCode get_primitive_tc(TCKind tCKind) {
        return get_primitive_tc(tCKind.value());
    }

    @Override // org.omg.CORBA.ORB
    public Any create_any() {
        return new AnyImpl(this);
    }

    @Override // org.omg.CORBA.ORB
    public NVList create_list(int i2) {
        return new NVListImpl(this, i2);
    }

    @Override // org.omg.CORBA.ORB
    public NVList create_operation_list(Object object) {
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA.ORB
    public NamedValue create_named_value(String str, Any any, int i2) {
        return new NamedValueImpl(this, str, any, i2);
    }

    @Override // org.omg.CORBA.ORB
    public ExceptionList create_exception_list() {
        return new ExceptionListImpl();
    }

    @Override // org.omg.CORBA.ORB
    public ContextList create_context_list() {
        return new ContextListImpl(this);
    }

    @Override // org.omg.CORBA.ORB
    public Context get_default_context() {
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA.ORB
    public Environment create_environment() {
        return new EnvironmentImpl();
    }

    @Override // org.omg.CORBA.ORB
    public Current get_current() {
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA.ORB
    public String[] list_initial_services() {
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA.ORB
    public Object resolve_initial_references(String str) throws InvalidName {
        throw this.wrapper.genericNoImpl();
    }

    @Override // com.sun.corba.se.org.omg.CORBA.ORB
    public void register_initial_reference(String str, Object object) throws InvalidName {
        throw this.wrapper.genericNoImpl();
    }

    @Override // org.omg.CORBA.ORB
    public void send_multiple_requests_oneway(Request[] requestArr) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public void send_multiple_requests_deferred(Request[] requestArr) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public boolean poll_next_response() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public Request get_next_response() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public String object_to_string(Object object) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public Object string_to_object(String str) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public Remote string_to_remote(String str) throws RemoteException {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public void connect(Object object) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public void disconnect(Object object) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public void run() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public void shutdown(boolean z2) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    protected void shutdownServants(boolean z2) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    protected void destroyConnections() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // com.sun.corba.se.spi.orb.ORB, org.omg.CORBA.ORB
    public void destroy() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public boolean work_pending() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public void perform_work() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA_2_3.ORB
    public ValueFactory register_value_factory(String str, ValueFactory valueFactory) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA_2_3.ORB
    public void unregister_value_factory(String str) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA_2_3.ORB
    public ValueFactory lookup_value_factory(String str) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // com.sun.corba.se.pept.broker.Broker
    public TransportManager getTransportManager() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public CorbaTransportManager getCorbaTransportManager() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public LegacyServerSocketManager getLegacyServerSocketManager() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    private synchronized ORB getFullORB() {
        if (this.fullORB == null) {
            Properties properties = new Properties();
            this.fullORB = new ORBImpl();
            this.fullORB.set_parameters(properties);
        }
        return this.fullORB;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public RequestDispatcherRegistry getRequestDispatcherRegistry() {
        return getFullORB().getRequestDispatcherRegistry();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ServiceContextRegistry getServiceContextRegistry() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public int getTransientServerId() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public int getORBInitialPort() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public String getORBInitialHost() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public String getORBServerHost() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public int getORBServerPort() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public CodeSetComponentInfo getCodeSetComponentInfo() {
        return new CodeSetComponentInfo();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public boolean isLocalHost(String str) {
        return false;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public boolean isLocalServerId(int i2, int i3) {
        return false;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ORBVersion getORBVersion() {
        return ORBVersionFactory.getORBVersion();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setORBVersion(ORBVersion oRBVersion) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public String getAppletHost() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public URL getAppletCodeBase() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public int getHighWaterMark() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public int getLowWaterMark() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public int getNumberToReclaim() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    public int getGIOPFragmentSize() {
        return 1024;
    }

    public int getGIOPBuffMgrStrategy(GIOPVersion gIOPVersion) {
        return 0;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public IOR getFVDCodeBaseIOR() {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // org.omg.CORBA.ORB
    public Policy create_policy(int i2, Any any) throws PolicyError {
        throw new NO_IMPLEMENT();
    }

    public LegacyServerSocketEndPointInfo getServerEndpoint() {
        return null;
    }

    public void setPersistentServerId(int i2) {
    }

    @Override // com.sun.corba.se.impl.corba.TypeCodeFactory
    public TypeCodeImpl getTypeCodeForClass(Class cls) {
        return null;
    }

    @Override // com.sun.corba.se.impl.corba.TypeCodeFactory
    public void setTypeCodeForClass(Class cls, TypeCodeImpl typeCodeImpl) {
    }

    public boolean alwaysSendCodeSetServiceContext() {
        return true;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public boolean isDuringDispatch() {
        return false;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void notifyORB() {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public PIHandler getPIHandler() {
        return null;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void checkShutdownState() {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void startingDispatch() {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void finishedDispatch() {
    }

    public void registerInitialReference(String str, Closure closure) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ORBData getORBData() {
        return getFullORB().getORBData();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setClientDelegateFactory(ClientDelegateFactory clientDelegateFactory) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ClientDelegateFactory getClientDelegateFactory() {
        return getFullORB().getClientDelegateFactory();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setCorbaContactInfoListFactory(CorbaContactInfoListFactory corbaContactInfoListFactory) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public CorbaContactInfoListFactory getCorbaContactInfoListFactory() {
        return getFullORB().getCorbaContactInfoListFactory();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public Operation getURLOperation() {
        return null;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setINSDelegate(CorbaServerRequestDispatcher corbaServerRequestDispatcher) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public TaggedComponentFactoryFinder getTaggedComponentFactoryFinder() {
        return getFullORB().getTaggedComponentFactoryFinder();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public IdentifiableFactoryFinder getTaggedProfileFactoryFinder() {
        return getFullORB().getTaggedProfileFactoryFinder();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public IdentifiableFactoryFinder getTaggedProfileTemplateFactoryFinder() {
        return getFullORB().getTaggedProfileTemplateFactoryFinder();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ObjectKeyFactory getObjectKeyFactory() {
        return getFullORB().getObjectKeyFactory();
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setObjectKeyFactory(ObjectKeyFactory objectKeyFactory) {
        throw new SecurityException("ORBSingleton: access denied");
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void handleBadServerId(ObjectKey objectKey) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public OAInvocationInfo peekInvocationInfo() {
        return null;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void pushInvocationInfo(OAInvocationInfo oAInvocationInfo) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public OAInvocationInfo popInvocationInfo() {
        return null;
    }

    @Override // com.sun.corba.se.pept.broker.Broker
    public ClientInvocationInfo createOrIncrementInvocationInfo() {
        return null;
    }

    @Override // com.sun.corba.se.pept.broker.Broker
    public void releaseOrDecrementInvocationInfo() {
    }

    @Override // com.sun.corba.se.pept.broker.Broker
    public ClientInvocationInfo getInvocationInfo() {
        return null;
    }

    public ConnectionCache getConnectionCache(ContactInfo contactInfo) {
        return null;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setResolver(Resolver resolver) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public Resolver getResolver() {
        return null;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setLocalResolver(LocalResolver localResolver) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public LocalResolver getLocalResolver() {
        return null;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setURLOperation(Operation operation) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setBadServerIdHandler(BadServerIdHandler badServerIdHandler) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void initBadServerIdHandler() {
    }

    public Selector getSelector(int i2) {
        return null;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void setThreadPoolManager(ThreadPoolManager threadPoolManager) {
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public ThreadPoolManager getThreadPoolManager() {
        return null;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public CopierManager getCopierManager() {
        return null;
    }

    @Override // com.sun.corba.se.spi.orb.ORB
    public void validateIORClass(String str) {
        getFullORB().validateIORClass(str);
    }
}
