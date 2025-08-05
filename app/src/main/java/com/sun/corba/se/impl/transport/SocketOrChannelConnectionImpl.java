package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.encoding.CDROutputObject;
import com.sun.corba.se.impl.encoding.CachedCodeBase;
import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ConnectionCache;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.pept.transport.EventHandler;
import com.sun.corba.se.pept.transport.InboundConnectionCache;
import com.sun.corba.se.pept.transport.OutboundConnectionCache;
import com.sun.corba.se.pept.transport.ResponseWaitingRoom;
import com.sun.corba.se.pept.transport.Selector;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;
import com.sun.corba.se.spi.orbutil.threadpool.NoSuchWorkQueueException;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import com.sun.corba.se.spi.transport.CorbaResponseWaitingRoom;
import com.sun.corba.se.spi.transport.ReadTimeouts;
import com.sun.org.omg.SendingContext.CodeBase;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.SystemException;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/SocketOrChannelConnectionImpl.class */
public class SocketOrChannelConnectionImpl extends EventHandlerBase implements CorbaConnection, Work {
    public static boolean dprintWriteLocks = false;
    protected long enqueueTime;
    protected SocketChannel socketChannel;
    protected CorbaContactInfo contactInfo;
    protected Acceptor acceptor;
    protected ConnectionCache connectionCache;
    protected Socket socket;
    protected long timeStamp;
    protected boolean isServer;
    protected int requestId;
    protected CorbaResponseWaitingRoom responseWaitingRoom;
    protected int state;
    protected Object stateEvent;
    protected Object writeEvent;
    protected boolean writeLocked;
    protected int serverRequestCount;
    Map serverRequestMap;
    protected boolean postInitialContexts;
    protected IOR codeBaseServerIOR;
    protected CachedCodeBase cachedCodeBase;
    protected ORBUtilSystemException wrapper;
    protected ReadTimeouts readTimeouts;
    protected boolean shouldReadGiopHeaderOnly;
    protected CorbaMessageMediator partialMessageMediator;
    protected CodeSetComponentInfo.CodeSetContext codeSetContext;
    protected MessageMediator clientReply_1_1;
    protected MessageMediator serverRequest_1_1;

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    protected SocketOrChannelConnectionImpl(ORB orb) {
        this.timeStamp = 0L;
        this.isServer = false;
        this.requestId = 5;
        this.stateEvent = new Object();
        this.writeEvent = new Object();
        this.serverRequestCount = 0;
        this.serverRequestMap = null;
        this.postInitialContexts = false;
        this.cachedCodeBase = new CachedCodeBase(this);
        this.partialMessageMediator = null;
        this.codeSetContext = null;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_TRANSPORT);
        setWork(this);
        this.responseWaitingRoom = new CorbaResponseWaitingRoomImpl(orb, this);
        setReadTimeouts(orb.getORBData().getTransportTCPReadTimeouts());
    }

    protected SocketOrChannelConnectionImpl(ORB orb, boolean z2, boolean z3) {
        this(orb);
        setUseSelectThreadToWait(z2);
        setUseWorkerThreadForEvent(z3);
    }

    public SocketOrChannelConnectionImpl(ORB orb, CorbaContactInfo corbaContactInfo, boolean z2, boolean z3, String str, String str2, int i2) {
        this(orb, z2, z3);
        this.contactInfo = corbaContactInfo;
        try {
            this.socket = orb.getORBData().getSocketFactory().createSocket(str, new InetSocketAddress(str2, i2));
            this.socketChannel = this.socket.getChannel();
            if (this.socketChannel != null) {
                this.socketChannel.configureBlocking(!z2);
            } else {
                setUseSelectThreadToWait(false);
            }
            if (orb.transportDebugFlag) {
                dprint(".initialize: connection created: " + ((Object) this.socket));
            }
            this.state = 1;
        } catch (Throwable th) {
            throw this.wrapper.connectFailure(th, str, str2, Integer.toString(i2));
        }
    }

    public SocketOrChannelConnectionImpl(ORB orb, CorbaContactInfo corbaContactInfo, String str, String str2, int i2) {
        this(orb, corbaContactInfo, orb.getORBData().connectionSocketUseSelectThreadToWait(), orb.getORBData().connectionSocketUseWorkerThreadForEvent(), str, str2, i2);
    }

    public SocketOrChannelConnectionImpl(ORB orb, Acceptor acceptor, Socket socket, boolean z2, boolean z3) {
        this(orb, z2, z3);
        this.socket = socket;
        this.socketChannel = socket.getChannel();
        if (this.socketChannel != null) {
            try {
                this.socketChannel.configureBlocking(!z2);
            } catch (IOException e2) {
                RuntimeException runtimeException = new RuntimeException();
                runtimeException.initCause(e2);
                throw runtimeException;
            }
        }
        this.acceptor = acceptor;
        this.serverRequestMap = Collections.synchronizedMap(new HashMap());
        this.isServer = true;
        this.state = 2;
    }

    public SocketOrChannelConnectionImpl(ORB orb, Acceptor acceptor, Socket socket) {
        this(orb, acceptor, socket, socket.getChannel() == null ? false : orb.getORBData().connectionSocketUseSelectThreadToWait(), socket.getChannel() == null ? false : orb.getORBData().connectionSocketUseWorkerThreadForEvent());
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public boolean shouldRegisterReadEvent() {
        return true;
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public boolean shouldRegisterServerReadEvent() {
        return true;
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public boolean read() {
        try {
            if (this.orb.transportDebugFlag) {
                dprint(".read->: " + ((Object) this));
            }
            CorbaMessageMediator bits = readBits();
            if (bits != null) {
                return dispatch(bits);
            }
            if (this.orb.transportDebugFlag) {
                dprint(".read<-: " + ((Object) this));
            }
            return true;
        } finally {
            if (this.orb.transportDebugFlag) {
                dprint(".read<-: " + ((Object) this));
            }
        }
    }

    protected CorbaMessageMediator readBits() {
        MessageMediator messageMediatorCreateMessageMediator;
        try {
            try {
                try {
                    if (this.orb.transportDebugFlag) {
                        dprint(".readBits->: " + ((Object) this));
                    }
                    if (this.contactInfo != null) {
                        messageMediatorCreateMessageMediator = this.contactInfo.createMessageMediator(this.orb, this);
                    } else if (this.acceptor != null) {
                        messageMediatorCreateMessageMediator = this.acceptor.createMessageMediator(this.orb, this);
                    } else {
                        throw new RuntimeException("SocketOrChannelConnectionImpl.readBits");
                    }
                    CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediatorCreateMessageMediator;
                    if (this.orb.transportDebugFlag) {
                        dprint(".readBits<-: " + ((Object) this));
                    }
                    return corbaMessageMediator;
                } catch (ThreadDeath e2) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".readBits: " + ((Object) this) + ": ThreadDeath: " + ((Object) e2), e2);
                    }
                    try {
                        purgeCalls(this.wrapper.connectionAbort(e2), false, false);
                    } catch (Throwable th) {
                        if (this.orb.transportDebugFlag) {
                            dprint(".readBits: " + ((Object) this) + ": purgeCalls: Throwable: " + ((Object) th), th);
                        }
                    }
                    throw e2;
                }
            } catch (Throwable th2) {
                if (this.orb.transportDebugFlag) {
                    dprint(".readBits: " + ((Object) this) + ": Throwable: " + ((Object) th2), th2);
                }
                try {
                    if (th2 instanceof INTERNAL) {
                        sendMessageError(GIOPVersion.DEFAULT_VERSION);
                    }
                } catch (IOException e3) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".readBits: " + ((Object) this) + ": sendMessageError: IOException: " + ((Object) e3), e3);
                    }
                }
                Selector selector = this.orb.getTransportManager().getSelector(0);
                if (selector != null) {
                    selector.unregisterForEvent(this);
                }
                purgeCalls(this.wrapper.connectionAbort(th2), true, false);
                if (this.orb.transportDebugFlag) {
                    dprint(".readBits<-: " + ((Object) this));
                    return null;
                }
                return null;
            }
        } catch (Throwable th3) {
            if (this.orb.transportDebugFlag) {
                dprint(".readBits<-: " + ((Object) this));
            }
            throw th3;
        }
    }

    protected CorbaMessageMediator finishReadingBits(MessageMediator messageMediator) {
        MessageMediator messageMediatorFinishCreatingMessageMediator;
        try {
            try {
                try {
                    if (this.orb.transportDebugFlag) {
                        dprint(".finishReadingBits->: " + ((Object) this));
                    }
                    if (this.contactInfo != null) {
                        messageMediatorFinishCreatingMessageMediator = this.contactInfo.finishCreatingMessageMediator(this.orb, this, messageMediator);
                    } else if (this.acceptor != null) {
                        messageMediatorFinishCreatingMessageMediator = this.acceptor.finishCreatingMessageMediator(this.orb, this, messageMediator);
                    } else {
                        throw new RuntimeException("SocketOrChannelConnectionImpl.finishReadingBits");
                    }
                    CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediatorFinishCreatingMessageMediator;
                    if (this.orb.transportDebugFlag) {
                        dprint(".finishReadingBits<-: " + ((Object) this));
                    }
                    return corbaMessageMediator;
                } catch (ThreadDeath e2) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".finishReadingBits: " + ((Object) this) + ": ThreadDeath: " + ((Object) e2), e2);
                    }
                    try {
                        purgeCalls(this.wrapper.connectionAbort(e2), false, false);
                    } catch (Throwable th) {
                        if (this.orb.transportDebugFlag) {
                            dprint(".finishReadingBits: " + ((Object) this) + ": purgeCalls: Throwable: " + ((Object) th), th);
                        }
                    }
                    throw e2;
                }
            } catch (Throwable th2) {
                if (this.orb.transportDebugFlag) {
                    dprint(".finishReadingBits: " + ((Object) this) + ": Throwable: " + ((Object) th2), th2);
                }
                try {
                    if (th2 instanceof INTERNAL) {
                        sendMessageError(GIOPVersion.DEFAULT_VERSION);
                    }
                } catch (IOException e3) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".finishReadingBits: " + ((Object) this) + ": sendMessageError: IOException: " + ((Object) e3), e3);
                    }
                }
                this.orb.getTransportManager().getSelector(0).unregisterForEvent(this);
                purgeCalls(this.wrapper.connectionAbort(th2), true, false);
                if (this.orb.transportDebugFlag) {
                    dprint(".finishReadingBits<-: " + ((Object) this));
                    return null;
                }
                return null;
            }
        } catch (Throwable th3) {
            if (this.orb.transportDebugFlag) {
                dprint(".finishReadingBits<-: " + ((Object) this));
            }
            throw th3;
        }
    }

    protected boolean dispatch(CorbaMessageMediator corbaMessageMediator) {
        try {
            try {
                if (this.orb.transportDebugFlag) {
                    dprint(".dispatch->: " + ((Object) this));
                }
                boolean zHandleRequest = corbaMessageMediator.getProtocolHandler().handleRequest(corbaMessageMediator);
                if (this.orb.transportDebugFlag) {
                    dprint(".dispatch<-: " + ((Object) this));
                }
                return zHandleRequest;
            } catch (ThreadDeath e2) {
                if (this.orb.transportDebugFlag) {
                    dprint(".dispatch: ThreadDeath", e2);
                }
                try {
                    purgeCalls(this.wrapper.connectionAbort(e2), false, false);
                } catch (Throwable th) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".dispatch: purgeCalls: Throwable", th);
                    }
                }
                throw e2;
            } catch (Throwable th2) {
                if (this.orb.transportDebugFlag) {
                    dprint(".dispatch: Throwable", th2);
                }
                try {
                    if (th2 instanceof INTERNAL) {
                        sendMessageError(GIOPVersion.DEFAULT_VERSION);
                    }
                } catch (IOException e3) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".dispatch: sendMessageError: IOException", e3);
                    }
                }
                purgeCalls(this.wrapper.connectionAbort(th2), false, false);
                if (this.orb.transportDebugFlag) {
                    dprint(".dispatch<-: " + ((Object) this));
                    return true;
                }
                return true;
            }
        } catch (Throwable th3) {
            if (this.orb.transportDebugFlag) {
                dprint(".dispatch<-: " + ((Object) this));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public boolean shouldUseDirectByteBuffers() {
        return getSocketChannel() != null;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public ByteBuffer read(int i2, int i3, int i4, long j2) throws IOException {
        if (shouldUseDirectByteBuffers()) {
            ByteBuffer byteBuffer = this.orb.getByteBufferPool().getByteBuffer(i2);
            if (this.orb.transportDebugFlag) {
                int iIdentityHashCode = System.identityHashCode(byteBuffer);
                StringBuffer stringBuffer = new StringBuffer(80);
                stringBuffer.append(".read: got ByteBuffer id (");
                stringBuffer.append(iIdentityHashCode).append(") from ByteBufferPool.");
                dprint(stringBuffer.toString());
            }
            byteBuffer.position(i3);
            byteBuffer.limit(i2);
            readFully(byteBuffer, i4, j2);
            return byteBuffer;
        }
        byte[] bArr = new byte[i2];
        readFully(getSocket().getInputStream(), bArr, i3, i4, j2);
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.limit(i2);
        return byteBufferWrap;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public ByteBuffer read(ByteBuffer byteBuffer, int i2, int i3, long j2) throws IOException {
        int i4 = i2 + i3;
        if (shouldUseDirectByteBuffers()) {
            if (!byteBuffer.isDirect()) {
                throw this.wrapper.unexpectedNonDirectByteBufferWithChannelSocket();
            }
            if (i4 > byteBuffer.capacity()) {
                if (this.orb.transportDebugFlag) {
                    int iIdentityHashCode = System.identityHashCode(byteBuffer);
                    StringBuffer stringBuffer = new StringBuffer(80);
                    stringBuffer.append(".read: releasing ByteBuffer id (").append(iIdentityHashCode).append(") to ByteBufferPool.");
                    dprint(stringBuffer.toString());
                }
                this.orb.getByteBufferPool().releaseByteBuffer(byteBuffer);
                byteBuffer = this.orb.getByteBufferPool().getByteBuffer(i4);
            }
            byteBuffer.position(i2);
            byteBuffer.limit(i4);
            readFully(byteBuffer, i3, j2);
            byteBuffer.position(0);
            byteBuffer.limit(i4);
            return byteBuffer;
        }
        if (byteBuffer.isDirect()) {
            throw this.wrapper.unexpectedDirectByteBufferWithNonChannelSocket();
        }
        byte[] bArr = new byte[i4];
        readFully(getSocket().getInputStream(), bArr, i2, i3, j2);
        return ByteBuffer.wrap(bArr);
    }

    public void readFully(ByteBuffer byteBuffer, int i2, long j2) throws IOException {
        int i3 = 0;
        long j3 = this.readTimeouts.get_initial_time_to_wait();
        long j4 = 0;
        do {
            int i4 = getSocketChannel().read(byteBuffer);
            if (i4 < 0) {
                throw new IOException("End-of-stream");
            }
            if (i4 == 0) {
                try {
                    Thread.sleep(j3);
                    j4 += j3;
                    j3 = (long) (j3 * this.readTimeouts.get_backoff_factor());
                } catch (InterruptedException e2) {
                    if (this.orb.transportDebugFlag) {
                        dprint("readFully(): unexpected exception " + e2.toString());
                    }
                }
            } else {
                i3 += i4;
            }
            if (i3 >= i2) {
                break;
            }
        } while (j4 < j2);
        if (i3 < i2 && j4 >= j2) {
            throw this.wrapper.transportReadTimeoutExceeded(new Integer(i2), new Integer(i3), new Long(j2), new Long(j4));
        }
        getConnectionCache().stampTime(this);
    }

    public void readFully(InputStream inputStream, byte[] bArr, int i2, int i3, long j2) throws IOException {
        int i4 = 0;
        long j3 = this.readTimeouts.get_initial_time_to_wait();
        long j4 = 0;
        do {
            int i5 = inputStream.read(bArr, i2 + i4, i3 - i4);
            if (i5 < 0) {
                throw new IOException("End-of-stream");
            }
            if (i5 == 0) {
                try {
                    Thread.sleep(j3);
                    j4 += j3;
                    j3 = (long) (j3 * this.readTimeouts.get_backoff_factor());
                } catch (InterruptedException e2) {
                    if (this.orb.transportDebugFlag) {
                        dprint("readFully(): unexpected exception " + e2.toString());
                    }
                }
            } else {
                i4 += i5;
            }
            if (i4 >= i3) {
                break;
            }
        } while (j4 < j2);
        if (i4 < i3 && j4 >= j2) {
            throw this.wrapper.transportReadTimeoutExceeded(new Integer(i3), new Integer(i4), new Long(j2), new Long(j4));
        }
        getConnectionCache().stampTime(this);
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void write(ByteBuffer byteBuffer) throws IOException {
        if (shouldUseDirectByteBuffers()) {
            do {
                getSocketChannel().write(byteBuffer);
            } while (byteBuffer.hasRemaining());
        } else {
            if (!byteBuffer.hasArray()) {
                throw this.wrapper.unexpectedDirectByteBufferWithNonChannelSocket();
            }
            getSocket().getOutputStream().write(byteBuffer.array(), 0, byteBuffer.limit());
            getSocket().getOutputStream().flush();
        }
        getConnectionCache().stampTime(this);
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public synchronized void close() {
        boolean z2;
        try {
            if (this.orb.transportDebugFlag) {
                dprint(".close->: " + ((Object) this));
            }
            writeLock();
            if (isBusy()) {
                writeUnlock();
                if (this.orb.transportDebugFlag) {
                    dprint(".close: isBusy so no close: " + ((Object) this));
                }
                if (z2) {
                    return;
                } else {
                    return;
                }
            }
            try {
                try {
                    sendCloseConnection(GIOPVersion.V1_0);
                } catch (Exception e2) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".close: exception: " + ((Object) this), e2);
                    }
                }
            } catch (Throwable th) {
                this.wrapper.exceptionWhenSendingCloseConnection(th);
            }
            synchronized (this.stateEvent) {
                this.state = 3;
                this.stateEvent.notifyAll();
            }
            purgeCalls(this.wrapper.connectionRebind(), false, true);
            try {
                Selector selector = this.orb.getTransportManager().getSelector(0);
                if (selector != null) {
                    selector.unregisterForEvent(this);
                }
                if (this.socketChannel != null) {
                    this.socketChannel.close();
                }
                this.socket.close();
            } catch (IOException e3) {
                if (this.orb.transportDebugFlag) {
                    dprint(".close: " + ((Object) this), e3);
                }
            }
            closeConnectionResources();
            if (this.orb.transportDebugFlag) {
                dprint(".close<-: " + ((Object) this));
            }
        } finally {
            if (this.orb.transportDebugFlag) {
                dprint(".close<-: " + ((Object) this));
            }
        }
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void closeConnectionResources() {
        if (this.orb.transportDebugFlag) {
            dprint(".closeConnectionResources->: " + ((Object) this));
        }
        Selector selector = this.orb.getTransportManager().getSelector(0);
        if (selector != null) {
            selector.unregisterForEvent(this);
        }
        try {
            if (this.socketChannel != null) {
                this.socketChannel.close();
            }
            if (this.socket != null && !this.socket.isClosed()) {
                this.socket.close();
            }
        } catch (IOException e2) {
            if (this.orb.transportDebugFlag) {
                dprint(".closeConnectionResources: " + ((Object) this), e2);
            }
        }
        if (this.orb.transportDebugFlag) {
            dprint(".closeConnectionResources<-: " + ((Object) this));
        }
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public Acceptor getAcceptor() {
        return this.acceptor;
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public ContactInfo getContactInfo() {
        return this.contactInfo;
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public EventHandler getEventHandler() {
        return this;
    }

    public OutputObject createOutputObject(MessageMediator messageMediator) {
        throw new RuntimeException("*****SocketOrChannelConnectionImpl.createOutputObject - should not be called.");
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public boolean isServer() {
        return this.isServer;
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public boolean isBusy() {
        if (this.serverRequestCount > 0 || getResponseWaitingRoom().numberRegistered() > 0) {
            return true;
        }
        return false;
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public long getTimeStamp() {
        return this.timeStamp;
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public void setTimeStamp(long j2) {
        this.timeStamp = j2;
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public void setState(String str) {
        synchronized (this.stateEvent) {
            if (str.equals("ESTABLISHED")) {
                this.state = 2;
                this.stateEvent.notifyAll();
            }
        }
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public void writeLock() {
        try {
            if (dprintWriteLocks && this.orb.transportDebugFlag) {
                dprint(".writeLock->: " + ((Object) this));
            }
            while (true) {
                switch (this.state) {
                    case 1:
                        synchronized (this.stateEvent) {
                            if (this.state == 1) {
                                try {
                                    this.stateEvent.wait();
                                } catch (InterruptedException e2) {
                                    if (this.orb.transportDebugFlag) {
                                        dprint(".writeLock: OPENING InterruptedException: " + ((Object) this));
                                    }
                                }
                            }
                        }
                    case 2:
                        synchronized (this.writeEvent) {
                            if (!this.writeLocked) {
                                this.writeLocked = true;
                            } else {
                                while (this.state == 2 && this.writeLocked) {
                                    try {
                                        this.writeEvent.wait(100L);
                                    } catch (InterruptedException e3) {
                                        if (this.orb.transportDebugFlag) {
                                            dprint(".writeLock: ESTABLISHED InterruptedException: " + ((Object) this));
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if (!dprintWriteLocks || !this.orb.transportDebugFlag) {
                            return;
                        }
                        dprint(".writeLock<-: " + ((Object) this));
                        return;
                    case 3:
                    default:
                        if (this.orb.transportDebugFlag) {
                            dprint(".writeLock: default: " + ((Object) this));
                        }
                        throw new RuntimeException(".writeLock: bad state");
                    case 4:
                        synchronized (this.stateEvent) {
                            if (this.state == 4) {
                                throw this.wrapper.connectionCloseRebind();
                            }
                        }
                    case 5:
                        synchronized (this.stateEvent) {
                            if (this.state == 5) {
                                throw this.wrapper.writeErrorSend();
                            }
                        }
                }
            }
        } catch (Throwable th) {
            if (dprintWriteLocks && this.orb.transportDebugFlag) {
                dprint(".writeLock<-: " + ((Object) this));
            }
            throw th;
        }
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public void writeUnlock() {
        try {
            if (dprintWriteLocks && this.orb.transportDebugFlag) {
                dprint(".writeUnlock->: " + ((Object) this));
            }
            synchronized (this.writeEvent) {
                this.writeLocked = false;
                this.writeEvent.notify();
            }
            if (dprintWriteLocks && this.orb.transportDebugFlag) {
                dprint(".writeUnlock<-: " + ((Object) this));
            }
        } catch (Throwable th) {
            if (dprintWriteLocks && this.orb.transportDebugFlag) {
                dprint(".writeUnlock<-: " + ((Object) this));
            }
            throw th;
        }
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public void sendWithoutLock(OutputObject outputObject) {
        try {
            ((CDROutputObject) outputObject).writeTo(this);
        } catch (IOException e2) {
            COMM_FAILURE comm_failureWriteErrorSend = this.wrapper.writeErrorSend(e2);
            purgeCalls(comm_failureWriteErrorSend, false, true);
            throw comm_failureWriteErrorSend;
        }
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public void registerWaiter(MessageMediator messageMediator) {
        this.responseWaitingRoom.registerWaiter(messageMediator);
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public void unregisterWaiter(MessageMediator messageMediator) {
        this.responseWaitingRoom.unregisterWaiter(messageMediator);
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public InputObject waitForResponse(MessageMediator messageMediator) {
        return this.responseWaitingRoom.waitForResponse(messageMediator);
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public void setConnectionCache(ConnectionCache connectionCache) {
        this.connectionCache = connectionCache;
    }

    @Override // com.sun.corba.se.pept.transport.Connection
    public ConnectionCache getConnectionCache() {
        return this.connectionCache;
    }

    @Override // com.sun.corba.se.impl.transport.EventHandlerBase, com.sun.corba.se.pept.transport.EventHandler
    public void setUseSelectThreadToWait(boolean z2) {
        this.useSelectThreadToWait = z2;
        setReadGiopHeaderOnly(shouldUseSelectThreadToWait());
    }

    @Override // com.sun.corba.se.impl.transport.EventHandlerBase, com.sun.corba.se.pept.transport.EventHandler
    public void handleEvent() {
        if (this.orb.transportDebugFlag) {
            dprint(".handleEvent->: " + ((Object) this));
        }
        getSelectionKey().interestOps(getSelectionKey().interestOps() & (getInterestOps() ^ (-1)));
        if (shouldUseWorkerThreadForEvent()) {
            Throwable th = null;
            try {
                int threadPoolToUse = 0;
                if (shouldReadGiopHeaderOnly()) {
                    this.partialMessageMediator = readBits();
                    threadPoolToUse = this.partialMessageMediator.getThreadPoolToUse();
                }
                if (this.orb.transportDebugFlag) {
                    dprint(".handleEvent: addWork to pool: " + threadPoolToUse);
                }
                this.orb.getThreadPoolManager().getThreadPool(threadPoolToUse).getWorkQueue(0).addWork(getWork());
            } catch (NoSuchThreadPoolException e2) {
                th = e2;
            } catch (NoSuchWorkQueueException e3) {
                th = e3;
            }
            if (th != null) {
                if (this.orb.transportDebugFlag) {
                    dprint(".handleEvent: " + ((Object) th));
                }
                INTERNAL internal = new INTERNAL("NoSuchThreadPoolException");
                internal.initCause(th);
                throw internal;
            }
        } else {
            if (this.orb.transportDebugFlag) {
                dprint(".handleEvent: doWork");
            }
            getWork().doWork();
        }
        if (this.orb.transportDebugFlag) {
            dprint(".handleEvent<-: " + ((Object) this));
        }
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public SelectableChannel getChannel() {
        return this.socketChannel;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public int getInterestOps() {
        return 1;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public Connection getConnection() {
        return this;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public String getName() {
        return toString();
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public void doWork() {
        try {
            try {
                if (this.orb.transportDebugFlag) {
                    dprint(".doWork->: " + ((Object) this));
                }
                if (!shouldReadGiopHeaderOnly()) {
                    read();
                } else {
                    CorbaMessageMediator corbaMessageMediatorFinishReadingBits = finishReadingBits(getPartialMessageMediator());
                    if (corbaMessageMediatorFinishReadingBits != null) {
                        dispatch(corbaMessageMediatorFinishReadingBits);
                    }
                }
                if (this.orb.transportDebugFlag) {
                    dprint(".doWork<-: " + ((Object) this));
                }
            } catch (Throwable th) {
                if (this.orb.transportDebugFlag) {
                    dprint(".doWork: ignoring Throwable: " + ((Object) th) + " " + ((Object) this));
                }
                if (this.orb.transportDebugFlag) {
                    dprint(".doWork<-: " + ((Object) this));
                }
            }
        } catch (Throwable th2) {
            if (this.orb.transportDebugFlag) {
                dprint(".doWork<-: " + ((Object) this));
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

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public boolean shouldReadGiopHeaderOnly() {
        return this.shouldReadGiopHeaderOnly;
    }

    protected void setReadGiopHeaderOnly(boolean z2) {
        this.shouldReadGiopHeaderOnly = z2;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public ResponseWaitingRoom getResponseWaitingRoom() {
        return this.responseWaitingRoom;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void serverRequestMapPut(int i2, CorbaMessageMediator corbaMessageMediator) {
        this.serverRequestMap.put(new Integer(i2), corbaMessageMediator);
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public CorbaMessageMediator serverRequestMapGet(int i2) {
        return (CorbaMessageMediator) this.serverRequestMap.get(new Integer(i2));
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void serverRequestMapRemove(int i2) {
        this.serverRequestMap.remove(new Integer(i2));
    }

    @Override // com.sun.corba.se.spi.legacy.connection.Connection
    public Socket getSocket() {
        return this.socket;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public synchronized void serverRequestProcessingBegins() {
        this.serverRequestCount++;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public synchronized void serverRequestProcessingEnds() {
        this.serverRequestCount--;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public synchronized int getNextRequestId() {
        int i2 = this.requestId;
        this.requestId = i2 + 1;
        return i2;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public ORB getBroker() {
        return this.orb;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public CodeSetComponentInfo.CodeSetContext getCodeSetContext() {
        CodeSetComponentInfo.CodeSetContext codeSetContext;
        if (this.codeSetContext == null) {
            synchronized (this) {
                codeSetContext = this.codeSetContext;
            }
            return codeSetContext;
        }
        return this.codeSetContext;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public synchronized void setCodeSetContext(CodeSetComponentInfo.CodeSetContext codeSetContext) {
        if (this.codeSetContext == null) {
            if (OSFCodeSetRegistry.lookupEntry(codeSetContext.getCharCodeSet()) == null || OSFCodeSetRegistry.lookupEntry(codeSetContext.getWCharCodeSet()) == null) {
                throw this.wrapper.badCodesetsFromClient();
            }
            this.codeSetContext = codeSetContext;
        }
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public MessageMediator clientRequestMapGet(int i2) {
        return this.responseWaitingRoom.getMessageMediator(i2);
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void clientReply_1_1_Put(MessageMediator messageMediator) {
        this.clientReply_1_1 = messageMediator;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public MessageMediator clientReply_1_1_Get() {
        return this.clientReply_1_1;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void clientReply_1_1_Remove() {
        this.clientReply_1_1 = null;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void serverRequest_1_1_Put(MessageMediator messageMediator) {
        this.serverRequest_1_1 = messageMediator;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public MessageMediator serverRequest_1_1_Get() {
        return this.serverRequest_1_1;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void serverRequest_1_1_Remove() {
        this.serverRequest_1_1 = null;
    }

    protected String getStateString(int i2) {
        synchronized (this.stateEvent) {
            switch (i2) {
                case 1:
                    return "OPENING";
                case 2:
                    return "ESTABLISHED";
                case 3:
                    return "CLOSE_SENT";
                case 4:
                    return "CLOSE_RECVD";
                case 5:
                    return "ABORT";
                default:
                    return "???";
            }
        }
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public synchronized boolean isPostInitialContexts() {
        return this.postInitialContexts;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public synchronized void setPostInitialContexts() {
        this.postInitialContexts = true;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void purgeCalls(SystemException systemException, boolean z2, boolean z3) {
        boolean z4;
        int i2 = systemException.minor;
        try {
            if (this.orb.transportDebugFlag) {
                dprint(".purgeCalls->: " + i2 + "/" + z2 + "/" + z3 + " " + ((Object) this));
            }
            synchronized (this.stateEvent) {
                if (this.state == 5 || this.state == 4) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".purgeCalls: exiting since state is: " + getStateString(this.state) + " " + ((Object) this));
                    }
                    if (z4) {
                        return;
                    } else {
                        return;
                    }
                }
                if (!z3) {
                    try {
                        writeLock();
                    } catch (SystemException e2) {
                        if (this.orb.transportDebugFlag) {
                            dprint(".purgeCalls: SystemException" + ((Object) e2) + "; continuing " + ((Object) this));
                        }
                    }
                }
                synchronized (this.stateEvent) {
                    if (i2 == 1398079697) {
                        this.state = 4;
                        systemException.completed = CompletionStatus.COMPLETED_NO;
                    } else {
                        this.state = 5;
                        systemException.completed = CompletionStatus.COMPLETED_MAYBE;
                    }
                    this.stateEvent.notifyAll();
                }
                try {
                    this.socket.getInputStream().close();
                    this.socket.getOutputStream().close();
                    this.socket.close();
                } catch (Exception e3) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".purgeCalls: Exception closing socket: " + ((Object) e3) + " " + ((Object) this));
                    }
                }
                this.responseWaitingRoom.signalExceptionToAllWaiters(systemException);
                if (this.contactInfo != null) {
                    ((OutboundConnectionCache) getConnectionCache()).remove(this.contactInfo);
                } else if (this.acceptor != null) {
                    ((InboundConnectionCache) getConnectionCache()).remove(this);
                }
                writeUnlock();
                if (this.orb.transportDebugFlag) {
                    dprint(".purgeCalls<-: " + i2 + "/" + z2 + "/" + z3 + " " + ((Object) this));
                }
            }
        } finally {
            if (this.contactInfo != null) {
                ((OutboundConnectionCache) getConnectionCache()).remove(this.contactInfo);
            } else if (this.acceptor != null) {
                ((InboundConnectionCache) getConnectionCache()).remove(this);
            }
            writeUnlock();
            if (this.orb.transportDebugFlag) {
                dprint(".purgeCalls<-: " + i2 + "/" + z2 + "/" + z3 + " " + ((Object) this));
            }
        }
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void sendCloseConnection(GIOPVersion gIOPVersion) throws IOException {
        sendHelper(gIOPVersion, MessageBase.createCloseConnection(gIOPVersion));
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void sendMessageError(GIOPVersion gIOPVersion) throws IOException {
        sendHelper(gIOPVersion, MessageBase.createMessageError(gIOPVersion));
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void sendCancelRequest(GIOPVersion gIOPVersion, int i2) throws IOException {
        sendHelper(gIOPVersion, MessageBase.createCancelRequest(gIOPVersion, i2));
    }

    protected void sendHelper(GIOPVersion gIOPVersion, Message message) throws IOException {
        CDROutputObject cDROutputObjectNewCDROutputObject = OutputStreamFactory.newCDROutputObject(this.orb, null, gIOPVersion, this, message, (byte) 1);
        message.write(cDROutputObjectNewCDROutputObject);
        cDROutputObjectNewCDROutputObject.writeTo(this);
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public void sendCancelRequestWithLock(GIOPVersion gIOPVersion, int i2) throws IOException {
        writeLock();
        try {
            sendCancelRequest(gIOPVersion, i2);
        } finally {
            writeUnlock();
        }
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public final void setCodeBaseIOR(IOR ior) {
        this.codeBaseServerIOR = ior;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public final IOR getCodeBaseIOR() {
        return this.codeBaseServerIOR;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnection
    public final CodeBase getCodeBase() {
        return this.cachedCodeBase;
    }

    protected void setReadTimeouts(ReadTimeouts readTimeouts) {
        this.readTimeouts = readTimeouts;
    }

    protected void setPartialMessageMediator(CorbaMessageMediator corbaMessageMediator) {
        this.partialMessageMediator = corbaMessageMediator;
    }

    protected CorbaMessageMediator getPartialMessageMediator() {
        return this.partialMessageMediator;
    }

    public String toString() {
        String str;
        synchronized (this.stateEvent) {
            str = "SocketOrChannelConnectionImpl[ " + (this.socketChannel == null ? this.socket.toString() : this.socketChannel.toString()) + " " + getStateString(this.state) + " " + shouldUseSelectThreadToWait() + " " + shouldUseWorkerThreadForEvent() + " " + shouldReadGiopHeaderOnly() + "]";
        }
        return str;
    }

    public void dprint(String str) {
        ORBUtility.dprint("SocketOrChannelConnectionImpl", str);
    }

    protected void dprint(String str, Throwable th) {
        dprint(str);
        th.printStackTrace(System.out);
    }
}
