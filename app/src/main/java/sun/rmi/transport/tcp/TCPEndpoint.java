package sun.rmi.transport.tcp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.ConnectIOException;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.security.AccessController;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.rmi.runtime.Log;
import sun.rmi.runtime.NewThreadAction;
import sun.rmi.transport.Channel;
import sun.rmi.transport.Endpoint;
import sun.rmi.transport.Target;
import sun.rmi.transport.Transport;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetIntegerAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/transport/tcp/TCPEndpoint.class */
public class TCPEndpoint implements Endpoint {
    private String host;
    private int port;
    private final RMIClientSocketFactory csf;
    private final RMIServerSocketFactory ssf;
    private int listenPort;
    private TCPTransport transport;
    private static String localHost;
    private static boolean localHostKnown;
    private static final Map<TCPEndpoint, LinkedList<TCPEndpoint>> localEndpoints;
    private static final int FORMAT_HOST_PORT = 0;
    private static final int FORMAT_HOST_PORT_FACTORY = 1;

    /* JADX INFO: Access modifiers changed from: private */
    public static int getInt(String str, int i2) {
        return ((Integer) AccessController.doPrivileged(new GetIntegerAction(str, i2))).intValue();
    }

    private static boolean getBoolean(String str) {
        return ((Boolean) AccessController.doPrivileged(new GetBooleanAction(str))).booleanValue();
    }

    private static String getHostnameProperty() {
        return (String) AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.hostname"));
    }

    static {
        localHostKnown = true;
        localHost = getHostnameProperty();
        if (localHost == null) {
            try {
                InetAddress localHost2 = InetAddress.getLocalHost();
                byte[] address = localHost2.getAddress();
                if (address[0] == Byte.MAX_VALUE && address[1] == 0 && address[2] == 0 && address[3] == 1) {
                    localHostKnown = false;
                }
                if (getBoolean("java.rmi.server.useLocalHostName")) {
                    localHost = FQDN.attemptFQDN(localHost2);
                } else {
                    localHost = localHost2.getHostAddress();
                }
            } catch (Exception e2) {
                localHostKnown = false;
                localHost = null;
            }
        }
        if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
            TCPTransport.tcpLog.log(Log.BRIEF, "localHostKnown = " + localHostKnown + ", localHost = " + localHost);
        }
        localEndpoints = new HashMap();
    }

    public TCPEndpoint(String str, int i2) {
        this(str, i2, null, null);
    }

    public TCPEndpoint(String str, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) {
        this.listenPort = -1;
        this.transport = null;
        this.host = str == null ? "" : str;
        this.port = i2;
        this.csf = rMIClientSocketFactory;
        this.ssf = rMIServerSocketFactory;
    }

    public static TCPEndpoint getLocalEndpoint(int i2) {
        return getLocalEndpoint(i2, null, null);
    }

    public static TCPEndpoint getLocalEndpoint(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) {
        TCPEndpoint last;
        synchronized (localEndpoints) {
            TCPEndpoint tCPEndpoint = new TCPEndpoint(null, i2, rMIClientSocketFactory, rMIServerSocketFactory);
            LinkedList<TCPEndpoint> linkedList = localEndpoints.get(tCPEndpoint);
            String strResampleLocalHost = resampleLocalHost();
            if (linkedList == null) {
                last = new TCPEndpoint(strResampleLocalHost, i2, rMIClientSocketFactory, rMIServerSocketFactory);
                LinkedList<TCPEndpoint> linkedList2 = new LinkedList<>();
                linkedList2.add(last);
                last.listenPort = i2;
                last.transport = new TCPTransport(linkedList2);
                localEndpoints.put(tCPEndpoint, linkedList2);
                if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
                    TCPTransport.tcpLog.log(Log.BRIEF, "created local endpoint for socket factory " + ((Object) rMIServerSocketFactory) + " on port " + i2);
                }
            } else {
                synchronized (linkedList) {
                    last = linkedList.getLast();
                    String str = last.host;
                    int i3 = last.port;
                    TCPTransport tCPTransport = last.transport;
                    if (strResampleLocalHost != null && !strResampleLocalHost.equals(str)) {
                        if (i3 != 0) {
                            linkedList.clear();
                        }
                        last = new TCPEndpoint(strResampleLocalHost, i3, rMIClientSocketFactory, rMIServerSocketFactory);
                        last.listenPort = i2;
                        last.transport = tCPTransport;
                        linkedList.add(last);
                    }
                }
            }
        }
        return last;
    }

    private static String resampleLocalHost() {
        String str;
        String hostnameProperty = getHostnameProperty();
        synchronized (localEndpoints) {
            if (hostnameProperty != null) {
                if (!localHostKnown) {
                    setLocalHost(hostnameProperty);
                } else if (!hostnameProperty.equals(localHost)) {
                    localHost = hostnameProperty;
                    if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
                        TCPTransport.tcpLog.log(Log.BRIEF, "updated local hostname to: " + localHost);
                    }
                }
                str = localHost;
            } else {
                str = localHost;
            }
        }
        return str;
    }

    static void setLocalHost(String str) {
        synchronized (localEndpoints) {
            if (!localHostKnown) {
                localHost = str;
                localHostKnown = true;
                if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
                    TCPTransport.tcpLog.log(Log.BRIEF, "local host set to " + str);
                }
                for (LinkedList<TCPEndpoint> linkedList : localEndpoints.values()) {
                    synchronized (linkedList) {
                        Iterator<TCPEndpoint> it = linkedList.iterator();
                        while (it.hasNext()) {
                            it.next().host = str;
                        }
                    }
                }
            }
        }
    }

    static void setDefaultPort(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) {
        TCPEndpoint tCPEndpoint = new TCPEndpoint(null, 0, rMIClientSocketFactory, rMIServerSocketFactory);
        synchronized (localEndpoints) {
            LinkedList<TCPEndpoint> linkedList = localEndpoints.get(tCPEndpoint);
            synchronized (linkedList) {
                int size = linkedList.size();
                TCPEndpoint last = linkedList.getLast();
                Iterator<TCPEndpoint> it = linkedList.iterator();
                while (it.hasNext()) {
                    it.next().port = i2;
                }
                if (size > 1) {
                    linkedList.clear();
                    linkedList.add(last);
                }
            }
            localEndpoints.put(new TCPEndpoint(null, i2, rMIClientSocketFactory, rMIServerSocketFactory), linkedList);
            if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
                TCPTransport.tcpLog.log(Log.BRIEF, "default port for server socket factory " + ((Object) rMIServerSocketFactory) + " and client socket factory " + ((Object) rMIClientSocketFactory) + " set to " + i2);
            }
        }
    }

    @Override // sun.rmi.transport.Endpoint
    public Transport getOutboundTransport() {
        return getLocalEndpoint(0, null, null).transport;
    }

    private static Collection<TCPTransport> allKnownTransports() {
        HashSet hashSet;
        synchronized (localEndpoints) {
            hashSet = new HashSet(localEndpoints.size());
            Iterator<LinkedList<TCPEndpoint>> it = localEndpoints.values().iterator();
            while (it.hasNext()) {
                hashSet.add(it.next().getFirst().transport);
            }
        }
        return hashSet;
    }

    public static void shedConnectionCaches() {
        Iterator<TCPTransport> it = allKnownTransports().iterator();
        while (it.hasNext()) {
            it.next().shedConnectionCaches();
        }
    }

    @Override // sun.rmi.transport.Endpoint
    public void exportObject(Target target) throws RemoteException {
        this.transport.exportObject(target);
    }

    @Override // sun.rmi.transport.Endpoint
    public Channel getChannel() {
        return getOutboundTransport().getChannel(this);
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public int getListenPort() {
        return this.listenPort;
    }

    @Override // sun.rmi.transport.Endpoint
    public Transport getInboundTransport() {
        return this.transport;
    }

    public RMIClientSocketFactory getClientSocketFactory() {
        return this.csf;
    }

    public RMIServerSocketFactory getServerSocketFactory() {
        return this.ssf;
    }

    public String toString() {
        return "[" + this.host + CallSiteDescriptor.TOKEN_DELIMITER + this.port + (this.ssf != null ? "," + ((Object) this.ssf) : "") + (this.csf != null ? "," + ((Object) this.csf) : "") + "]";
    }

    public int hashCode() {
        return this.port;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof TCPEndpoint)) {
            TCPEndpoint tCPEndpoint = (TCPEndpoint) obj;
            if (this.port != tCPEndpoint.port || !this.host.equals(tCPEndpoint.host)) {
                return false;
            }
            if ((this.csf == null) ^ (tCPEndpoint.csf == null)) {
                return false;
            }
            if ((this.ssf == null) ^ (tCPEndpoint.ssf == null)) {
                return false;
            }
            if (this.csf != null && (this.csf.getClass() != tCPEndpoint.csf.getClass() || !this.csf.equals(tCPEndpoint.csf))) {
                return false;
            }
            if (this.ssf != null) {
                if (this.ssf.getClass() != tCPEndpoint.ssf.getClass() || !this.ssf.equals(tCPEndpoint.ssf)) {
                    return false;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    public void write(ObjectOutput objectOutput) throws IOException {
        if (this.csf == null) {
            objectOutput.writeByte(0);
            objectOutput.writeUTF(this.host);
            objectOutput.writeInt(this.port);
        } else {
            objectOutput.writeByte(1);
            objectOutput.writeUTF(this.host);
            objectOutput.writeInt(this.port);
            objectOutput.writeObject(this.csf);
        }
    }

    public static TCPEndpoint read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        String utf;
        int i2;
        RMIClientSocketFactory rMIClientSocketFactory = null;
        switch (objectInput.readByte()) {
            case 0:
                utf = objectInput.readUTF();
                i2 = objectInput.readInt();
                break;
            case 1:
                utf = objectInput.readUTF();
                i2 = objectInput.readInt();
                rMIClientSocketFactory = (RMIClientSocketFactory) objectInput.readObject();
                if (rMIClientSocketFactory != null && Proxy.isProxyClass(rMIClientSocketFactory.getClass())) {
                    throw new IOException("Invalid SocketFactory");
                }
                break;
            default:
                throw new IOException("invalid endpoint format");
        }
        return new TCPEndpoint(utf, i2, rMIClientSocketFactory, null);
    }

    public void writeHostPortFormat(DataOutput dataOutput) throws IOException {
        if (this.csf != null) {
            throw new InternalError("TCPEndpoint.writeHostPortFormat: called for endpoint with non-null socket factory");
        }
        dataOutput.writeUTF(this.host);
        dataOutput.writeInt(this.port);
    }

    public static TCPEndpoint readHostPortFormat(DataInput dataInput) throws IOException {
        return new TCPEndpoint(dataInput.readUTF(), dataInput.readInt());
    }

    private static RMISocketFactory chooseFactory() {
        RMISocketFactory socketFactory = RMISocketFactory.getSocketFactory();
        if (socketFactory == null) {
            socketFactory = TCPTransport.defaultSocketFactory;
        }
        return socketFactory;
    }

    Socket newSocket() throws RemoteException {
        if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
            TCPTransport.tcpLog.log(Log.VERBOSE, "opening socket to " + ((Object) this));
        }
        try {
            RMIClientSocketFactory rMIClientSocketFactoryChooseFactory = this.csf;
            if (rMIClientSocketFactoryChooseFactory == null) {
                rMIClientSocketFactoryChooseFactory = chooseFactory();
            }
            Socket socketCreateSocket = rMIClientSocketFactoryChooseFactory.createSocket(this.host, this.port);
            try {
                socketCreateSocket.setTcpNoDelay(true);
            } catch (Exception e2) {
            }
            try {
                socketCreateSocket.setKeepAlive(true);
            } catch (Exception e3) {
            }
            return socketCreateSocket;
        } catch (ConnectException e4) {
            throw new java.rmi.ConnectException("Connection refused to host: " + this.host, e4);
        } catch (UnknownHostException e5) {
            throw new java.rmi.UnknownHostException("Unknown host: " + this.host, e5);
        } catch (IOException e6) {
            try {
                shedConnectionCaches();
            } catch (Exception | OutOfMemoryError e7) {
            }
            throw new ConnectIOException("Exception creating connection to: " + this.host, e6);
        }
    }

    ServerSocket newServerSocket() throws IOException {
        if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
            TCPTransport.tcpLog.log(Log.VERBOSE, "creating server socket on " + ((Object) this));
        }
        RMIServerSocketFactory rMIServerSocketFactoryChooseFactory = this.ssf;
        if (rMIServerSocketFactoryChooseFactory == null) {
            rMIServerSocketFactoryChooseFactory = chooseFactory();
        }
        ServerSocket serverSocketCreateServerSocket = rMIServerSocketFactoryChooseFactory.createServerSocket(this.listenPort);
        if (this.listenPort == 0) {
            setDefaultPort(serverSocketCreateServerSocket.getLocalPort(), this.csf, this.ssf);
        }
        return serverSocketCreateServerSocket;
    }

    /* loaded from: rt.jar:sun/rmi/transport/tcp/TCPEndpoint$FQDN.class */
    private static class FQDN implements Runnable {
        private String reverseLookup;
        private String hostAddress;

        private FQDN(String str) {
            this.hostAddress = str;
        }

        static String attemptFQDN(InetAddress inetAddress) throws UnknownHostException {
            String hostName = inetAddress.getHostName();
            if (hostName.indexOf(46) < 0) {
                String hostAddress = inetAddress.getHostAddress();
                FQDN fqdn = new FQDN(hostAddress);
                int i2 = TCPEndpoint.getInt("sun.rmi.transport.tcp.localHostNameTimeOut", 10000);
                try {
                    synchronized (fqdn) {
                        fqdn.getFQDN();
                        fqdn.wait(i2);
                    }
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                }
                hostName = fqdn.getHost();
                if (hostName == null || hostName.equals("") || hostName.indexOf(46) < 0) {
                    hostName = hostAddress;
                }
            }
            return hostName;
        }

        private void getFQDN() {
            ((Thread) AccessController.doPrivileged(new NewThreadAction(this, "FQDN Finder", true))).start();
        }

        private synchronized String getHost() {
            return this.reverseLookup;
        }

        @Override // java.lang.Runnable
        public void run() {
            String hostName = null;
            try {
                hostName = InetAddress.getByName(this.hostAddress).getHostName();
                synchronized (this) {
                    this.reverseLookup = hostName;
                    notify();
                }
            } catch (UnknownHostException e2) {
                synchronized (this) {
                    this.reverseLookup = hostName;
                    notify();
                }
            } catch (Throwable th) {
                synchronized (this) {
                    this.reverseLookup = hostName;
                    notify();
                    throw th;
                }
            }
        }
    }
}
