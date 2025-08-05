package com.sun.corba.se.impl.orb;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.legacy.connection.USLPort;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.transport.DefaultIORToSocketInfoImpl;
import com.sun.corba.se.impl.transport.DefaultSocketFactoryImpl;
import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.pept.transport.EventHandler;
import com.sun.corba.se.pept.transport.InboundConnectionCache;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.legacy.connection.ORBSocketFactory;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orb.OperationFactory;
import com.sun.corba.se.spi.orb.ParserData;
import com.sun.corba.se.spi.orb.ParserDataFactory;
import com.sun.corba.se.spi.orb.StringPair;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
import com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo;
import com.sun.corba.se.spi.transport.IORToSocketInfo;
import com.sun.corba.se.spi.transport.ReadTimeouts;
import com.sun.corba.se.spi.transport.SocketInfo;
import com.sun.corba.se.spi.transport.TransportDefault;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.ORB;
import org.omg.PortableInterceptor.ORBInitInfo;
import org.omg.PortableInterceptor.ORBInitializer;
import sun.corba.SharedSecrets;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable.class */
public class ParserTable {
    private static String MY_CLASS_NAME = ParserTable.class.getName();
    private static ParserTable myInstance = new ParserTable();
    private ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.ORB_LIFECYCLE);
    private ParserData[] parserData;

    public static ParserTable get() {
        return myInstance;
    }

    public ParserData[] getParserData() {
        ParserData[] parserDataArr = new ParserData[this.parserData.length];
        System.arraycopy(this.parserData, 0, parserDataArr, 0, this.parserData.length);
        return parserDataArr;
    }

    private ParserTable() {
        String[] strArr = {"subcontract", "poa", WSDLConstants.ATTR_TRANSPORT};
        USLPort[] uSLPortArr = {new USLPort("FOO", 2701), new USLPort("BAR", 3333)};
        ReadTimeouts readTimeoutsCreate = TransportDefault.makeReadTimeoutsFactory().create(100, 3000, 300, 20);
        ORBInitializer[] oRBInitializerArr = {null, new TestORBInitializer1(), new TestORBInitializer2()};
        StringPair[] stringPairArr = {new StringPair("foo.bar.blech.NonExistent", "dummy"), new StringPair(MY_CLASS_NAME + "$TestORBInitializer1", "dummy"), new StringPair(MY_CLASS_NAME + "$TestORBInitializer2", "dummy")};
        Acceptor[] acceptorArr = {new TestAcceptor2(), new TestAcceptor1(), null};
        StringPair[] stringPairArr2 = {new StringPair("foo.bar.blech.NonExistent", "dummy"), new StringPair(MY_CLASS_NAME + "$TestAcceptor1", "dummy"), new StringPair(MY_CLASS_NAME + "$TestAcceptor2", "dummy")};
        StringPair[] stringPairArr3 = {new StringPair("Foo", "ior:930492049394"), new StringPair("Bar", "ior:3453465785633576")};
        try {
            new URL("corbaloc::camelot/NameService");
        } catch (Exception e2) {
        }
        this.parserData = new ParserData[]{ParserDataFactory.make(ORBConstants.DEBUG_PROPERTY, OperationFactory.listAction(",", OperationFactory.stringAction()), "debugFlags", new String[0], strArr, "subcontract,poa,transport"), ParserDataFactory.make(ORBConstants.INITIAL_HOST_PROPERTY, OperationFactory.stringAction(), "ORBInitialHost", "", "Foo", "Foo"), ParserDataFactory.make(ORBConstants.INITIAL_PORT_PROPERTY, OperationFactory.integerAction(), "ORBInitialPort", new Integer(900), new Integer(27314), "27314"), ParserDataFactory.make(ORBConstants.SERVER_HOST_PROPERTY, OperationFactory.stringAction(), "ORBServerHost", "", "camelot", "camelot"), ParserDataFactory.make(ORBConstants.SERVER_PORT_PROPERTY, OperationFactory.integerAction(), "ORBServerPort", new Integer(0), new Integer(38143), "38143"), ParserDataFactory.make(ORBConstants.LISTEN_ON_ALL_INTERFACES, OperationFactory.stringAction(), "listenOnAllInterfaces", ORBConstants.LISTEN_ON_ALL_INTERFACES, "foo", "foo"), ParserDataFactory.make(ORBConstants.ORB_ID_PROPERTY, OperationFactory.stringAction(), "orbId", "", "foo", "foo"), ParserDataFactory.make(ORBConstants.OLD_ORB_ID_PROPERTY, OperationFactory.stringAction(), "orbId", "", "foo", "foo"), ParserDataFactory.make(ORBConstants.ORB_SERVER_ID_PROPERTY, OperationFactory.integerAction(), "persistentServerId", new Integer(-1), new Integer(1234), "1234"), ParserDataFactory.make(ORBConstants.ORB_SERVER_ID_PROPERTY, OperationFactory.setFlagAction(), "persistentServerIdInitialized", Boolean.FALSE, Boolean.TRUE, "1234"), ParserDataFactory.make(ORBConstants.ORB_SERVER_ID_PROPERTY, OperationFactory.setFlagAction(), "orbServerIdPropertySpecified", Boolean.FALSE, Boolean.TRUE, "1234"), ParserDataFactory.make(ORBConstants.HIGH_WATER_MARK_PROPERTY, OperationFactory.integerAction(), "highWaterMark", new Integer(240), new Integer(3745), "3745"), ParserDataFactory.make(ORBConstants.LOW_WATER_MARK_PROPERTY, OperationFactory.integerAction(), "lowWaterMark", new Integer(100), new Integer(12), "12"), ParserDataFactory.make(ORBConstants.NUMBER_TO_RECLAIM_PROPERTY, OperationFactory.integerAction(), "numberToReclaim", new Integer(5), new Integer(231), "231"), ParserDataFactory.make(ORBConstants.GIOP_VERSION, makeGVOperation(), "giopVersion", GIOPVersion.DEFAULT_VERSION, new GIOPVersion(2, 3), "2.3"), ParserDataFactory.make(ORBConstants.GIOP_FRAGMENT_SIZE, makeFSOperation(), "giopFragmentSize", new Integer(1024), new Integer(65536), "65536"), ParserDataFactory.make(ORBConstants.GIOP_BUFFER_SIZE, OperationFactory.integerAction(), "giopBufferSize", new Integer(1024), new Integer(234000), "234000"), ParserDataFactory.make(ORBConstants.GIOP_11_BUFFMGR, makeBMGROperation(), "giop11BuffMgr", new Integer(0), new Integer(1), "CLCT"), ParserDataFactory.make(ORBConstants.GIOP_12_BUFFMGR, makeBMGROperation(), "giop12BuffMgr", new Integer(2), new Integer(0), "GROW"), ParserDataFactory.make(ORBConstants.GIOP_TARGET_ADDRESSING, OperationFactory.compose(OperationFactory.integerRangeAction(0, 3), OperationFactory.convertIntegerToShort()), "giopTargetAddressPreference", new Short((short) 3), new Short((short) 2), "2"), ParserDataFactory.make(ORBConstants.GIOP_TARGET_ADDRESSING, makeADOperation(), "giopAddressDisposition", new Short((short) 0), new Short((short) 2), "2"), ParserDataFactory.make(ORBConstants.ALWAYS_SEND_CODESET_CTX_PROPERTY, OperationFactory.booleanAction(), "alwaysSendCodeSetCtx", Boolean.TRUE, Boolean.FALSE, "false"), ParserDataFactory.make(ORBConstants.USE_BOMS, OperationFactory.booleanAction(), "useByteOrderMarkers", true, Boolean.FALSE, "false"), ParserDataFactory.make(ORBConstants.USE_BOMS_IN_ENCAPS, OperationFactory.booleanAction(), "useByteOrderMarkersInEncaps", false, Boolean.FALSE, "false"), ParserDataFactory.make(ORBConstants.CHAR_CODESETS, makeCSOperation(), "charData", CodeSetComponentInfo.JAVASOFT_DEFAULT_CODESETS.getCharComponent(), CodeSetComponentInfo.createFromString("65537,65801,65568"), "65537,65801,65568"), ParserDataFactory.make(ORBConstants.WCHAR_CODESETS, makeCSOperation(), "wcharData", CodeSetComponentInfo.JAVASOFT_DEFAULT_CODESETS.getWCharComponent(), CodeSetComponentInfo.createFromString("65537,65801,65568"), "65537,65801,65568"), ParserDataFactory.make(ORBConstants.ALLOW_LOCAL_OPTIMIZATION, OperationFactory.booleanAction(), "allowLocalOptimization", Boolean.FALSE, Boolean.TRUE, "true"), ParserDataFactory.make(ORBConstants.LEGACY_SOCKET_FACTORY_CLASS_PROPERTY, makeLegacySocketFactoryOperation(), "legacySocketFactory", null, new TestLegacyORBSocketFactory(), MY_CLASS_NAME + "$TestLegacyORBSocketFactory"), ParserDataFactory.make(ORBConstants.SOCKET_FACTORY_CLASS_PROPERTY, makeSocketFactoryOperation(), "socketFactory", new DefaultSocketFactoryImpl(), new TestORBSocketFactory(), MY_CLASS_NAME + "$TestORBSocketFactory"), ParserDataFactory.make(ORBConstants.LISTEN_SOCKET_PROPERTY, makeUSLOperation(), "userSpecifiedListenPorts", new USLPort[0], uSLPortArr, "FOO:2701,BAR:3333"), ParserDataFactory.make(ORBConstants.IOR_TO_SOCKET_INFO_CLASS_PROPERTY, makeIORToSocketInfoOperation(), "iorToSocketInfo", new DefaultIORToSocketInfoImpl(), new TestIORToSocketInfo(), MY_CLASS_NAME + "$TestIORToSocketInfo"), ParserDataFactory.make(ORBConstants.IIOP_PRIMARY_TO_CONTACT_INFO_CLASS_PROPERTY, makeIIOPPrimaryToContactInfoOperation(), "iiopPrimaryToContactInfo", null, new TestIIOPPrimaryToContactInfo(), MY_CLASS_NAME + "$TestIIOPPrimaryToContactInfo"), ParserDataFactory.make(ORBConstants.CONTACT_INFO_LIST_FACTORY_CLASS_PROPERTY, makeContactInfoListFactoryOperation(), "corbaContactInfoListFactory", null, new TestContactInfoListFactory(), MY_CLASS_NAME + "$TestContactInfoListFactory"), ParserDataFactory.make(ORBConstants.PERSISTENT_SERVER_PORT_PROPERTY, OperationFactory.integerAction(), "persistentServerPort", new Integer(0), new Integer(2743), "2743"), ParserDataFactory.make(ORBConstants.PERSISTENT_SERVER_PORT_PROPERTY, OperationFactory.setFlagAction(), "persistentPortInitialized", Boolean.FALSE, Boolean.TRUE, "2743"), ParserDataFactory.make(ORBConstants.SERVER_ID_PROPERTY, OperationFactory.integerAction(), "persistentServerId", new Integer(0), new Integer(294), "294"), ParserDataFactory.make(ORBConstants.SERVER_ID_PROPERTY, OperationFactory.setFlagAction(), "persistentServerIdInitialized", Boolean.FALSE, Boolean.TRUE, "294"), ParserDataFactory.make(ORBConstants.SERVER_ID_PROPERTY, OperationFactory.setFlagAction(), "orbServerIdPropertySpecified", Boolean.FALSE, Boolean.TRUE, "294"), ParserDataFactory.make(ORBConstants.ACTIVATED_PROPERTY, OperationFactory.booleanAction(), "serverIsORBActivated", Boolean.FALSE, Boolean.TRUE, "true"), ParserDataFactory.make(ORBConstants.BAD_SERVER_ID_HANDLER_CLASS_PROPERTY, OperationFactory.classAction(), "badServerIdHandlerClass", null, TestBadServerIdHandler.class, MY_CLASS_NAME + "$TestBadServerIdHandler"), ParserDataFactory.make(ORBConstants.PI_ORB_INITIALIZER_CLASS_PREFIX, makeROIOperation(), "orbInitializers", new ORBInitializer[0], oRBInitializerArr, stringPairArr, ORBInitializer.class), ParserDataFactory.make(ORBConstants.ACCEPTOR_CLASS_PREFIX_PROPERTY, makeAcceptorInstantiationOperation(), "acceptors", new Acceptor[0], acceptorArr, stringPairArr2, Acceptor.class), ParserDataFactory.make(ORBConstants.ACCEPTOR_SOCKET_TYPE_PROPERTY, OperationFactory.stringAction(), "acceptorSocketType", ORBConstants.SOCKETCHANNEL, "foo", "foo"), ParserDataFactory.make(ORBConstants.USE_NIO_SELECT_TO_WAIT_PROPERTY, OperationFactory.booleanAction(), "acceptorSocketUseSelectThreadToWait", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make(ORBConstants.ACCEPTOR_SOCKET_USE_WORKER_THREAD_FOR_EVENT_PROPERTY, OperationFactory.booleanAction(), "acceptorSocketUseWorkerThreadForEvent", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make(ORBConstants.CONNECTION_SOCKET_TYPE_PROPERTY, OperationFactory.stringAction(), "connectionSocketType", ORBConstants.SOCKETCHANNEL, "foo", "foo"), ParserDataFactory.make(ORBConstants.USE_NIO_SELECT_TO_WAIT_PROPERTY, OperationFactory.booleanAction(), "connectionSocketUseSelectThreadToWait", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make(ORBConstants.CONNECTION_SOCKET_USE_WORKER_THREAD_FOR_EVENT_PROPERTY, OperationFactory.booleanAction(), "connectionSocketUseWorkerThreadForEvent", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make(ORBConstants.DISABLE_DIRECT_BYTE_BUFFER_USE_PROPERTY, OperationFactory.booleanAction(), "disableDirectByteBufferUse", Boolean.FALSE, Boolean.TRUE, "true"), ParserDataFactory.make(ORBConstants.TRANSPORT_TCP_READ_TIMEOUTS_PROPERTY, makeTTCPRTOperation(), "readTimeouts", TransportDefault.makeReadTimeoutsFactory().create(100, 3000, 300, 20), readTimeoutsCreate, "100:3000:300:20"), ParserDataFactory.make(ORBConstants.ENABLE_JAVA_SERIALIZATION_PROPERTY, OperationFactory.booleanAction(), "enableJavaSerialization", Boolean.FALSE, Boolean.FALSE, "false"), ParserDataFactory.make(ORBConstants.USE_REP_ID, OperationFactory.booleanAction(), "useRepId", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make(ORBConstants.ORB_INIT_REF_PROPERTY, OperationFactory.identityAction(), "orbInitialReferences", new StringPair[0], stringPairArr3, stringPairArr3, StringPair.class)};
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestBadServerIdHandler.class */
    public final class TestBadServerIdHandler implements BadServerIdHandler {
        public TestBadServerIdHandler() {
        }

        public boolean equals(Object obj) {
            return obj instanceof TestBadServerIdHandler;
        }

        public int hashCode() {
            return 1;
        }

        @Override // com.sun.corba.se.impl.oa.poa.BadServerIdHandler
        public void handle(ObjectKey objectKey) {
        }
    }

    private Operation makeTTCPRTOperation() {
        return OperationFactory.compose(OperationFactory.sequenceAction(CallSiteDescriptor.TOKEN_DELIMITER, new Operation[]{OperationFactory.integerAction(), OperationFactory.integerAction(), OperationFactory.integerAction(), OperationFactory.integerAction()}), new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.1
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                Object[] objArr = (Object[]) obj;
                return TransportDefault.makeReadTimeoutsFactory().create(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), ((Integer) objArr[2]).intValue(), ((Integer) objArr[3]).intValue());
            }
        });
    }

    private Operation makeUSLOperation() {
        return OperationFactory.listAction(",", OperationFactory.compose(OperationFactory.sequenceAction(CallSiteDescriptor.TOKEN_DELIMITER, new Operation[]{OperationFactory.stringAction(), OperationFactory.integerAction()}), new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.2
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                Object[] objArr = (Object[]) obj;
                return new USLPort((String) objArr[0], ((Integer) objArr[1]).intValue());
            }
        }));
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestLegacyORBSocketFactory.class */
    public static final class TestLegacyORBSocketFactory implements ORBSocketFactory {
        public boolean equals(Object obj) {
            return obj instanceof TestLegacyORBSocketFactory;
        }

        public int hashCode() {
            return 1;
        }

        @Override // com.sun.corba.se.spi.legacy.connection.ORBSocketFactory
        public ServerSocket createServerSocket(String str, int i2) {
            return null;
        }

        @Override // com.sun.corba.se.spi.legacy.connection.ORBSocketFactory
        public SocketInfo getEndPointInfo(ORB orb, IOR ior, SocketInfo socketInfo) {
            return null;
        }

        @Override // com.sun.corba.se.spi.legacy.connection.ORBSocketFactory
        public Socket createSocket(SocketInfo socketInfo) {
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestORBSocketFactory.class */
    public static final class TestORBSocketFactory implements com.sun.corba.se.spi.transport.ORBSocketFactory {
        public boolean equals(Object obj) {
            return obj instanceof TestORBSocketFactory;
        }

        public int hashCode() {
            return 1;
        }

        @Override // com.sun.corba.se.spi.transport.ORBSocketFactory
        public void setORB(com.sun.corba.se.spi.orb.ORB orb) {
        }

        @Override // com.sun.corba.se.spi.transport.ORBSocketFactory
        public ServerSocket createServerSocket(String str, InetSocketAddress inetSocketAddress) {
            return null;
        }

        @Override // com.sun.corba.se.spi.transport.ORBSocketFactory
        public Socket createSocket(String str, InetSocketAddress inetSocketAddress) {
            return null;
        }

        @Override // com.sun.corba.se.spi.transport.ORBSocketFactory
        public void setAcceptedSocketOptions(Acceptor acceptor, ServerSocket serverSocket, Socket socket) {
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestIORToSocketInfo.class */
    public static final class TestIORToSocketInfo implements IORToSocketInfo {
        public boolean equals(Object obj) {
            return obj instanceof TestIORToSocketInfo;
        }

        public int hashCode() {
            return 1;
        }

        @Override // com.sun.corba.se.spi.transport.IORToSocketInfo
        public List getSocketInfo(IOR ior) {
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestIIOPPrimaryToContactInfo.class */
    public static final class TestIIOPPrimaryToContactInfo implements IIOPPrimaryToContactInfo {
        @Override // com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo
        public void reset(ContactInfo contactInfo) {
        }

        @Override // com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo
        public boolean hasNext(ContactInfo contactInfo, ContactInfo contactInfo2, List list) {
            return true;
        }

        @Override // com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo
        public ContactInfo next(ContactInfo contactInfo, ContactInfo contactInfo2, List list) {
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestContactInfoListFactory.class */
    public static final class TestContactInfoListFactory implements CorbaContactInfoListFactory {
        public boolean equals(Object obj) {
            return obj instanceof TestContactInfoListFactory;
        }

        public int hashCode() {
            return 1;
        }

        @Override // com.sun.corba.se.spi.transport.CorbaContactInfoListFactory
        public void setORB(com.sun.corba.se.spi.orb.ORB orb) {
        }

        @Override // com.sun.corba.se.spi.transport.CorbaContactInfoListFactory
        public CorbaContactInfoList create(IOR ior) {
            return null;
        }
    }

    private Operation makeMapOperation(final Map map) {
        return new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.3
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                return map.get(obj);
            }
        };
    }

    private Operation makeBMGROperation() {
        HashMap map = new HashMap();
        map.put("GROW", new Integer(0));
        map.put("CLCT", new Integer(1));
        map.put("STRM", new Integer(2));
        return makeMapOperation(map);
    }

    private Operation makeLegacySocketFactoryOperation() {
        return new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.4
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                String str = (String) obj;
                try {
                    Class<?> clsLoadClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
                    if (!ORBSocketFactory.class.isAssignableFrom(clsLoadClass)) {
                        throw ParserTable.this.wrapper.illegalSocketFactoryType(clsLoadClass.toString());
                    }
                    return clsLoadClass.newInstance();
                } catch (Exception e2) {
                    throw ParserTable.this.wrapper.badCustomSocketFactory(e2, str);
                }
            }
        };
    }

    private Operation makeSocketFactoryOperation() {
        return new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.5
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                String str = (String) obj;
                try {
                    Class<?> clsLoadClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
                    if (!com.sun.corba.se.spi.transport.ORBSocketFactory.class.isAssignableFrom(clsLoadClass)) {
                        throw ParserTable.this.wrapper.illegalSocketFactoryType(clsLoadClass.toString());
                    }
                    return clsLoadClass.newInstance();
                } catch (Exception e2) {
                    throw ParserTable.this.wrapper.badCustomSocketFactory(e2, str);
                }
            }
        };
    }

    private Operation makeIORToSocketInfoOperation() {
        return new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.6
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                String str = (String) obj;
                try {
                    Class<?> clsLoadClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
                    if (!IORToSocketInfo.class.isAssignableFrom(clsLoadClass)) {
                        throw ParserTable.this.wrapper.illegalIorToSocketInfoType(clsLoadClass.toString());
                    }
                    return clsLoadClass.newInstance();
                } catch (Exception e2) {
                    throw ParserTable.this.wrapper.badCustomIorToSocketInfo(e2, str);
                }
            }
        };
    }

    private Operation makeIIOPPrimaryToContactInfoOperation() {
        return new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.7
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                String str = (String) obj;
                try {
                    Class<?> clsLoadClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
                    if (!IIOPPrimaryToContactInfo.class.isAssignableFrom(clsLoadClass)) {
                        throw ParserTable.this.wrapper.illegalIiopPrimaryToContactInfoType(clsLoadClass.toString());
                    }
                    return clsLoadClass.newInstance();
                } catch (Exception e2) {
                    throw ParserTable.this.wrapper.badCustomIiopPrimaryToContactInfo(e2, str);
                }
            }
        };
    }

    private Operation makeContactInfoListFactoryOperation() {
        return new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.8
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                String str = (String) obj;
                try {
                    Class<?> clsLoadClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
                    if (CorbaContactInfoListFactory.class.isAssignableFrom(clsLoadClass)) {
                        return clsLoadClass.newInstance();
                    }
                    throw ParserTable.this.wrapper.illegalContactInfoListFactoryType(clsLoadClass.toString());
                } catch (Exception e2) {
                    throw ParserTable.this.wrapper.badContactInfoListFactory(e2, str);
                }
            }
        };
    }

    private Operation makeCSOperation() {
        return new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.9
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                return CodeSetComponentInfo.createFromString((String) obj);
            }
        };
    }

    private Operation makeADOperation() {
        return OperationFactory.compose(OperationFactory.compose(OperationFactory.integerRangeAction(0, 3), new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.10
            private Integer[] map = {new Integer(0), new Integer(1), new Integer(2), new Integer(0)};

            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                return this.map[((Integer) obj).intValue()];
            }
        }), OperationFactory.convertIntegerToShort());
    }

    private Operation makeFSOperation() {
        return OperationFactory.compose(OperationFactory.integerAction(), new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.11
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                int iIntValue = ((Integer) obj).intValue();
                if (iIntValue < 32) {
                    throw ParserTable.this.wrapper.fragmentSizeMinimum(new Integer(iIntValue), new Integer(32));
                }
                if (iIntValue % 8 != 0) {
                    throw ParserTable.this.wrapper.fragmentSizeDiv(new Integer(iIntValue), new Integer(8));
                }
                return obj;
            }
        });
    }

    private Operation makeGVOperation() {
        return OperationFactory.compose(OperationFactory.listAction(".", OperationFactory.integerAction()), new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.12
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                Object[] objArr = (Object[]) obj;
                return new GIOPVersion(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue());
            }
        });
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestORBInitializer1.class */
    public static final class TestORBInitializer1 extends LocalObject implements ORBInitializer {
        public boolean equals(Object obj) {
            return obj instanceof TestORBInitializer1;
        }

        public int hashCode() {
            return 1;
        }

        @Override // org.omg.PortableInterceptor.ORBInitializerOperations
        public void pre_init(ORBInitInfo oRBInitInfo) {
        }

        @Override // org.omg.PortableInterceptor.ORBInitializerOperations
        public void post_init(ORBInitInfo oRBInitInfo) {
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestORBInitializer2.class */
    public static final class TestORBInitializer2 extends LocalObject implements ORBInitializer {
        public boolean equals(Object obj) {
            return obj instanceof TestORBInitializer2;
        }

        public int hashCode() {
            return 1;
        }

        @Override // org.omg.PortableInterceptor.ORBInitializerOperations
        public void pre_init(ORBInitInfo oRBInitInfo) {
        }

        @Override // org.omg.PortableInterceptor.ORBInitializerOperations
        public void post_init(ORBInitInfo oRBInitInfo) {
        }
    }

    private Operation makeROIOperation() {
        return OperationFactory.compose(OperationFactory.maskErrorAction(OperationFactory.compose(OperationFactory.suffixAction(), OperationFactory.classAction())), new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.13
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                final Class cls = (Class) obj;
                if (cls == null) {
                    return null;
                }
                if (!ORBInitializer.class.isAssignableFrom(cls)) {
                    throw ParserTable.this.wrapper.orbInitializerType(cls.getName());
                }
                try {
                    return (ORBInitializer) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.corba.se.impl.orb.ParserTable.13.1
                        @Override // java.security.PrivilegedExceptionAction
                        public Object run() throws IllegalAccessException, InstantiationException {
                            return cls.newInstance();
                        }
                    });
                } catch (PrivilegedActionException e2) {
                    throw ParserTable.this.wrapper.orbInitializerFailure(e2.getException(), cls.getName());
                } catch (Exception e3) {
                    throw ParserTable.this.wrapper.orbInitializerFailure(e3, cls.getName());
                }
            }
        });
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestAcceptor1.class */
    public static final class TestAcceptor1 implements Acceptor {
        public boolean equals(Object obj) {
            return obj instanceof TestAcceptor1;
        }

        public int hashCode() {
            return 1;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public boolean initialize() {
            return true;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public boolean initialized() {
            return true;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public String getConnectionCacheType() {
            return "FOO";
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public void setConnectionCache(InboundConnectionCache inboundConnectionCache) {
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public InboundConnectionCache getConnectionCache() {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public boolean shouldRegisterAcceptEvent() {
            return true;
        }

        public void setUseSelectThreadForConnections(boolean z2) {
        }

        public boolean shouldUseSelectThreadForConnections() {
            return true;
        }

        public void setUseWorkerThreadForConnections(boolean z2) {
        }

        public boolean shouldUseWorkerThreadForConnections() {
            return true;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public void accept() {
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public void close() {
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public EventHandler getEventHandler() {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public MessageMediator createMessageMediator(Broker broker, Connection connection) {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public MessageMediator finishCreatingMessageMediator(Broker broker, Connection connection, MessageMediator messageMediator) {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public InputObject createInputObject(Broker broker, MessageMediator messageMediator) {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public OutputObject createOutputObject(Broker broker, MessageMediator messageMediator) {
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserTable$TestAcceptor2.class */
    public static final class TestAcceptor2 implements Acceptor {
        public boolean equals(Object obj) {
            return obj instanceof TestAcceptor2;
        }

        public int hashCode() {
            return 1;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public boolean initialize() {
            return true;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public boolean initialized() {
            return true;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public String getConnectionCacheType() {
            return "FOO";
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public void setConnectionCache(InboundConnectionCache inboundConnectionCache) {
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public InboundConnectionCache getConnectionCache() {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public boolean shouldRegisterAcceptEvent() {
            return true;
        }

        public void setUseSelectThreadForConnections(boolean z2) {
        }

        public boolean shouldUseSelectThreadForConnections() {
            return true;
        }

        public void setUseWorkerThreadForConnections(boolean z2) {
        }

        public boolean shouldUseWorkerThreadForConnections() {
            return true;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public void accept() {
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public void close() {
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public EventHandler getEventHandler() {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public MessageMediator createMessageMediator(Broker broker, Connection connection) {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public MessageMediator finishCreatingMessageMediator(Broker broker, Connection connection, MessageMediator messageMediator) {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public InputObject createInputObject(Broker broker, MessageMediator messageMediator) {
            return null;
        }

        @Override // com.sun.corba.se.pept.transport.Acceptor
        public OutputObject createOutputObject(Broker broker, MessageMediator messageMediator) {
            return null;
        }
    }

    private Operation makeAcceptorInstantiationOperation() {
        return OperationFactory.compose(OperationFactory.maskErrorAction(OperationFactory.compose(OperationFactory.suffixAction(), OperationFactory.classAction())), new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.14
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                final Class cls = (Class) obj;
                if (cls == null) {
                    return null;
                }
                if (!Acceptor.class.isAssignableFrom(cls)) {
                    throw ParserTable.this.wrapper.acceptorInstantiationTypeFailure(cls.getName());
                }
                try {
                    return (Acceptor) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.corba.se.impl.orb.ParserTable.14.1
                        @Override // java.security.PrivilegedExceptionAction
                        public Object run() throws IllegalAccessException, InstantiationException {
                            return cls.newInstance();
                        }
                    });
                } catch (PrivilegedActionException e2) {
                    throw ParserTable.this.wrapper.acceptorInstantiationFailure(e2.getException(), cls.getName());
                } catch (Exception e3) {
                    throw ParserTable.this.wrapper.acceptorInstantiationFailure(e3, cls.getName());
                }
            }
        });
    }

    private Operation makeInitRefOperation() {
        return new Operation() { // from class: com.sun.corba.se.impl.orb.ParserTable.15
            @Override // com.sun.corba.se.spi.orb.Operation
            public Object operate(Object obj) {
                String[] strArr = (String[]) obj;
                if (strArr.length != 2) {
                    throw ParserTable.this.wrapper.orbInitialreferenceSyntax();
                }
                return strArr[0] + "=" + strArr[1];
            }
        };
    }
}
