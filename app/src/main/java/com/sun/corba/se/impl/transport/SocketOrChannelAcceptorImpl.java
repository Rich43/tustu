package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.encoding.CDRInputObject;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.oa.poa.Policies;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.EventHandler;
import com.sun.corba.se.pept.transport.InboundConnectionCache;
import com.sun.corba.se.pept.transport.Selector;
import com.sun.corba.se.spi.extension.RequestPartitioningPolicy;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
import com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.transport.CorbaAcceptor;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.corba.se.spi.transport.SocketInfo;
import com.sun.corba.se.spi.transport.SocketOrChannelAcceptor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/SocketOrChannelAcceptorImpl.class */
public class SocketOrChannelAcceptorImpl extends EventHandlerBase implements CorbaAcceptor, SocketOrChannelAcceptor, Work, SocketInfo, LegacyServerSocketEndPointInfo {
    protected ServerSocketChannel serverSocketChannel;
    protected ServerSocket serverSocket;
    protected int port;
    protected long enqueueTime;
    protected boolean initialized;
    protected ORBUtilSystemException wrapper;
    protected InboundConnectionCache connectionCache;
    protected String type;
    protected String name;
    protected String hostname;
    protected int locatorPort;

    public SocketOrChannelAcceptorImpl(ORB orb) {
        this.type = "";
        this.name = "";
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_TRANSPORT);
        setWork(this);
        this.initialized = false;
        this.hostname = orb.getORBData().getORBServerHost();
        this.name = LegacyServerSocketEndPointInfo.NO_NAME;
        this.locatorPort = -1;
    }

    public SocketOrChannelAcceptorImpl(ORB orb, int i2) {
        this(orb);
        this.port = i2;
    }

    public SocketOrChannelAcceptorImpl(ORB orb, int i2, String str, String str2) {
        this(orb, i2);
        this.name = str;
        this.type = str2;
    }

    public boolean initialize() {
        InetSocketAddress inetSocketAddress;
        if (this.initialized) {
            return false;
        }
        if (this.orb.transportDebugFlag) {
            dprint(".initialize: " + ((Object) this));
        }
        try {
            if (this.orb.getORBData().getListenOnAllInterfaces().equals(ORBConstants.LISTEN_ON_ALL_INTERFACES)) {
                inetSocketAddress = new InetSocketAddress(this.port);
            } else {
                inetSocketAddress = new InetSocketAddress(this.orb.getORBData().getORBServerHost(), this.port);
            }
            this.serverSocket = this.orb.getORBData().getSocketFactory().createServerSocket(this.type, inetSocketAddress);
            internalInitialize();
            this.initialized = true;
            return true;
        } catch (Throwable th) {
            throw this.wrapper.createListenerFailed(th, Integer.toString(this.port));
        }
    }

    protected void internalInitialize() throws Exception {
        this.port = this.serverSocket.getLocalPort();
        this.orb.getCorbaTransportManager().getInboundConnectionCache(this);
        this.serverSocketChannel = this.serverSocket.getChannel();
        if (this.serverSocketChannel != null) {
            setUseSelectThreadToWait(this.orb.getORBData().acceptorSocketUseSelectThreadToWait());
            this.serverSocketChannel.configureBlocking(!this.orb.getORBData().acceptorSocketUseSelectThreadToWait());
        } else {
            setUseSelectThreadToWait(false);
        }
        setUseWorkerThreadForEvent(this.orb.getORBData().acceptorSocketUseWorkerThreadForEvent());
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public boolean initialized() {
        return this.initialized;
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public String getConnectionCacheType() {
        return getClass().toString();
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public void setConnectionCache(InboundConnectionCache inboundConnectionCache) {
        this.connectionCache = inboundConnectionCache;
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public InboundConnectionCache getConnectionCache() {
        return this.connectionCache;
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public boolean shouldRegisterAcceptEvent() {
        return true;
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public void accept() {
        Socket socket;
        Selector selector;
        String string;
        try {
            if (this.serverSocketChannel == null) {
                socket = this.serverSocket.accept();
            } else {
                socket = this.serverSocketChannel.accept().socket();
            }
            this.orb.getORBData().getSocketFactory().setAcceptedSocketOptions(this, this.serverSocket, socket);
            if (this.orb.transportDebugFlag) {
                StringBuilder sbAppend = new StringBuilder().append(".accept: ");
                if (this.serverSocketChannel == null) {
                    string = this.serverSocket.toString();
                } else {
                    string = this.serverSocketChannel.toString();
                }
                dprint(sbAppend.append(string).toString());
            }
            SocketOrChannelConnectionImpl socketOrChannelConnectionImpl = new SocketOrChannelConnectionImpl(this.orb, this, socket);
            if (this.orb.transportDebugFlag) {
                dprint(".accept: new: " + ((Object) socketOrChannelConnectionImpl));
            }
            getConnectionCache().stampTime(socketOrChannelConnectionImpl);
            getConnectionCache().put(this, socketOrChannelConnectionImpl);
            if (socketOrChannelConnectionImpl.shouldRegisterServerReadEvent() && (selector = this.orb.getTransportManager().getSelector(0)) != null) {
                if (this.orb.transportDebugFlag) {
                    dprint(".accept: registerForEvent: " + ((Object) socketOrChannelConnectionImpl));
                }
                selector.registerForEvent(socketOrChannelConnectionImpl.getEventHandler());
            }
            getConnectionCache().reclaim();
        } catch (IOException e2) {
            if (this.orb.transportDebugFlag) {
                dprint(".accept:", e2);
            }
            Selector selector2 = this.orb.getTransportManager().getSelector(0);
            if (selector2 != null) {
                selector2.unregisterForEvent(this);
                selector2.registerForEvent(this);
            }
        }
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public void close() {
        try {
            try {
                if (this.orb.transportDebugFlag) {
                    dprint(".close->:");
                }
                Selector selector = this.orb.getTransportManager().getSelector(0);
                if (selector != null) {
                    selector.unregisterForEvent(this);
                }
                if (this.serverSocketChannel != null) {
                    this.serverSocketChannel.close();
                }
                if (this.serverSocket != null) {
                    this.serverSocket.close();
                }
                if (this.orb.transportDebugFlag) {
                    dprint(".close<-:");
                }
            } catch (IOException e2) {
                if (this.orb.transportDebugFlag) {
                    dprint(".close:", e2);
                }
                if (this.orb.transportDebugFlag) {
                    dprint(".close<-:");
                }
            }
        } catch (Throwable th) {
            if (this.orb.transportDebugFlag) {
                dprint(".close<-:");
            }
            throw th;
        }
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public EventHandler getEventHandler() {
        return this;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaAcceptor
    public String getObjectAdapterId() {
        return null;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaAcceptor
    public String getObjectAdapterManagerId() {
        return null;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaAcceptor
    public void addToIORTemplate(IORTemplate iORTemplate, Policies policies, String str) {
        int iLegacyGetPersistentServerPort;
        Iterator itIteratorById = iORTemplate.iteratorById(0);
        String oRBServerHost = this.orb.getORBData().getORBServerHost();
        if (itIteratorById.hasNext()) {
            AlternateIIOPAddressComponent alternateIIOPAddressComponentMakeAlternateIIOPAddressComponent = IIOPFactories.makeAlternateIIOPAddressComponent(IIOPFactories.makeIIOPAddress(this.orb, oRBServerHost, this.port));
            while (itIteratorById.hasNext()) {
                ((TaggedProfileTemplate) itIteratorById.next()).add(alternateIIOPAddressComponentMakeAlternateIIOPAddressComponent);
            }
            return;
        }
        GIOPVersion gIOPVersion = this.orb.getORBData().getGIOPVersion();
        if (policies.forceZeroPort()) {
            iLegacyGetPersistentServerPort = 0;
        } else if (policies.isTransient()) {
            iLegacyGetPersistentServerPort = this.port;
        } else {
            iLegacyGetPersistentServerPort = this.orb.getLegacyServerSocketManager().legacyGetPersistentServerPort("IIOP_CLEAR_TEXT");
        }
        IIOPProfileTemplate iIOPProfileTemplateMakeIIOPProfileTemplate = IIOPFactories.makeIIOPProfileTemplate(this.orb, gIOPVersion, IIOPFactories.makeIIOPAddress(this.orb, oRBServerHost, iLegacyGetPersistentServerPort));
        if (gIOPVersion.supportsIORIIOPProfileComponents()) {
            iIOPProfileTemplateMakeIIOPProfileTemplate.add(IIOPFactories.makeCodeSetsComponent(this.orb));
            iIOPProfileTemplateMakeIIOPProfileTemplate.add(IIOPFactories.makeMaxStreamFormatVersionComponent());
            RequestPartitioningPolicy requestPartitioningPolicy = (RequestPartitioningPolicy) policies.get_effective_policy(ORBConstants.REQUEST_PARTITIONING_POLICY);
            if (requestPartitioningPolicy != null) {
                iIOPProfileTemplateMakeIIOPProfileTemplate.add(IIOPFactories.makeRequestPartitioningComponent(requestPartitioningPolicy.getValue()));
            }
            if (str != null && str != "") {
                iIOPProfileTemplateMakeIIOPProfileTemplate.add(IIOPFactories.makeJavaCodebaseComponent(str));
            }
            if (this.orb.getORBData().isJavaSerializationEnabled()) {
                iIOPProfileTemplateMakeIIOPProfileTemplate.add(IIOPFactories.makeJavaSerializationComponent());
            }
        }
        iORTemplate.add(iIOPProfileTemplateMakeIIOPProfileTemplate);
    }

    @Override // com.sun.corba.se.spi.transport.CorbaAcceptor
    public String getMonitoringName() {
        return "AcceptedConnections";
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public SelectableChannel getChannel() {
        return this.serverSocketChannel;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public int getInterestOps() {
        return 16;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public Acceptor getAcceptor() {
        return this;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public Connection getConnection() {
        throw new RuntimeException("Should not happen.");
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public void doWork() {
        try {
            try {
                try {
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork->: " + ((Object) this));
                    }
                    if (this.selectionKey.isAcceptable()) {
                        accept();
                    } else if (this.orb.transportDebugFlag) {
                        dprint(".doWork: ! selectionKey.isAcceptable: " + ((Object) this));
                    }
                    Selector selector = this.orb.getTransportManager().getSelector(0);
                    if (selector != null) {
                        selector.registerInterestOps(this);
                    }
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork<-:" + ((Object) this));
                    }
                } catch (Exception e2) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork: ignoring Exception: " + ((Object) e2) + " " + ((Object) this));
                    }
                    this.wrapper.exceptionInAccept(e2);
                    Selector selector2 = this.orb.getTransportManager().getSelector(0);
                    if (selector2 != null) {
                        selector2.registerInterestOps(this);
                    }
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork<-:" + ((Object) this));
                    }
                } catch (Throwable th) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork: ignoring Throwable: " + ((Object) th) + " " + ((Object) this));
                    }
                    Selector selector3 = this.orb.getTransportManager().getSelector(0);
                    if (selector3 != null) {
                        selector3.registerInterestOps(this);
                    }
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork<-:" + ((Object) this));
                    }
                }
            } catch (SecurityException e3) {
                if (this.orb.transportDebugFlag) {
                    dprint(".doWork: ignoring SecurityException: " + ((Object) e3) + " " + ((Object) this));
                }
                this.wrapper.securityExceptionInAccept(e3, ORBUtility.getClassSecurityInfo(getClass()));
                Selector selector4 = this.orb.getTransportManager().getSelector(0);
                if (selector4 != null) {
                    selector4.registerInterestOps(this);
                }
                if (this.orb.transportDebugFlag) {
                    dprint(".doWork<-:" + ((Object) this));
                }
            }
        } catch (Throwable th2) {
            Selector selector5 = this.orb.getTransportManager().getSelector(0);
            if (selector5 != null) {
                selector5.registerInterestOps(this);
            }
            if (this.orb.transportDebugFlag) {
                dprint(".doWork<-:" + ((Object) this));
            }
            throw th2;
        }
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public void setEnqueueTime(long j2) {
        this.enqueueTime = j2;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public long getEnqueueTime() {
        return this.enqueueTime;
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public MessageMediator createMessageMediator(Broker broker, Connection connection) {
        return new SocketOrChannelContactInfoImpl().createMessageMediator(broker, connection);
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public MessageMediator finishCreatingMessageMediator(Broker broker, Connection connection, MessageMediator messageMediator) {
        return new SocketOrChannelContactInfoImpl().finishCreatingMessageMediator(broker, connection, messageMediator);
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public InputObject createInputObject(Broker broker, MessageMediator messageMediator) {
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        return new CDRInputObject((ORB) broker, (CorbaConnection) messageMediator.getConnection(), corbaMessageMediator.getDispatchBuffer(), corbaMessageMediator.getDispatchHeader());
    }

    @Override // com.sun.corba.se.pept.transport.Acceptor
    public OutputObject createOutputObject(Broker broker, MessageMediator messageMediator) {
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        return OutputStreamFactory.newCDROutputObject((ORB) broker, corbaMessageMediator, corbaMessageMediator.getReplyHeader(), corbaMessageMediator.getStreamFormatVersion());
    }

    @Override // com.sun.corba.se.spi.transport.SocketOrChannelAcceptor
    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public String toString() {
        String string;
        if (this.serverSocketChannel == null) {
            if (this.serverSocket == null) {
                string = "(not initialized)";
            } else {
                string = this.serverSocket.toString();
            }
        } else {
            string = this.serverSocketChannel.toString();
        }
        return toStringName() + "[" + string + " " + this.type + " " + shouldUseSelectThreadToWait() + " " + shouldUseWorkerThreadForEvent() + "]";
    }

    protected String toStringName() {
        return "SocketOrChannelAcceptorImpl";
    }

    protected void dprint(String str) {
        ORBUtility.dprint(toStringName(), str);
    }

    protected void dprint(String str, Throwable th) {
        dprint(str);
        th.printStackTrace(System.out);
    }

    @Override // com.sun.corba.se.spi.transport.SocketInfo, com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public String getType() {
        return this.type;
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public String getHostName() {
        return this.hostname;
    }

    @Override // com.sun.corba.se.spi.transport.SocketInfo
    public String getHost() {
        return this.hostname;
    }

    @Override // com.sun.corba.se.spi.transport.SocketInfo, com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public int getPort() {
        return this.port;
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public int getLocatorPort() {
        return this.locatorPort;
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public void setLocatorPort(int i2) {
        this.locatorPort = i2;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public String getName() {
        return this.name.equals(LegacyServerSocketEndPointInfo.NO_NAME) ? toString() : this.name;
    }
}
