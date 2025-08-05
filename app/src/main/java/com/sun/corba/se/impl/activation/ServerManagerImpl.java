package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.logging.ActivationSystemException;
import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.EndPointInfo;
import com.sun.corba.se.spi.activation.InvalidORBid;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;
import com.sun.corba.se.spi.activation.NoSuchEndPoint;
import com.sun.corba.se.spi.activation.ORBAlreadyRegistered;
import com.sun.corba.se.spi.activation.ORBPortInfo;
import com.sun.corba.se.spi.activation.Repository;
import com.sun.corba.se.spi.activation.Server;
import com.sun.corba.se.spi.activation.ServerAlreadyActive;
import com.sun.corba.se.spi.activation.ServerAlreadyInstalled;
import com.sun.corba.se.spi.activation.ServerAlreadyUninstalled;
import com.sun.corba.se.spi.activation.ServerHeldDown;
import com.sun.corba.se.spi.activation.ServerNotActive;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import com.sun.corba.se.spi.activation._ServerManagerImplBase;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.ForwardException;
import com.sun.corba.se.spi.transport.CorbaTransportManager;
import com.sun.corba.se.spi.transport.SocketOrChannelAcceptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ServerManagerImpl.class */
public class ServerManagerImpl extends _ServerManagerImplBase implements BadServerIdHandler {
    HashMap serverTable = new HashMap(256);
    Repository repository;
    CorbaTransportManager transportManager;
    int initialPort;
    ORB orb;
    ActivationSystemException wrapper;
    String dbDirName;
    boolean debug;
    private int serverStartupDelay;

    ServerManagerImpl(ORB orb, CorbaTransportManager corbaTransportManager, Repository repository, String str, boolean z2) {
        this.debug = false;
        this.orb = orb;
        this.wrapper = ActivationSystemException.get(orb, CORBALogDomains.ORBD_ACTIVATOR);
        this.transportManager = corbaTransportManager;
        this.repository = repository;
        this.dbDirName = str;
        this.debug = z2;
        this.initialPort = ((SocketOrChannelAcceptor) orb.getLegacyServerSocketManager().legacyGetEndpoint(LegacyServerSocketEndPointInfo.BOOT_NAMING)).getServerSocket().getLocalPort();
        this.serverStartupDelay = 1000;
        String property = System.getProperty(ORBConstants.SERVER_STARTUP_DELAY);
        if (property != null) {
            try {
                this.serverStartupDelay = Integer.parseInt(property);
            } catch (Exception e2) {
            }
        }
        if (orb.getORBData().getBadServerIdHandler() == null) {
            orb.setBadServerIdHandler(this);
        } else {
            orb.initBadServerIdHandler();
        }
        orb.connect(this);
        ProcessMonitorThread.start(this.serverTable);
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void activate(int i2) throws ServerNotRegistered, ServerHeldDown, ServerAlreadyActive {
        ServerTableEntry serverTableEntry;
        Integer num = new Integer(i2);
        synchronized (this.serverTable) {
            serverTableEntry = (ServerTableEntry) this.serverTable.get(num);
        }
        if (serverTableEntry != null && serverTableEntry.isActive()) {
            if (this.debug) {
                System.out.println("ServerManagerImpl: activate for server Id " + i2 + " failed because server is already active. entry = " + ((Object) serverTableEntry));
            }
            throw new ServerAlreadyActive(i2);
        }
        try {
            ServerTableEntry entry = getEntry(i2);
            if (this.debug) {
                System.out.println("ServerManagerImpl: locateServer called with  serverId=" + i2 + " endpointType=IIOP_CLEAR_TEXT block=false");
            }
            ServerLocation serverLocationLocateServer = locateServer(entry, "IIOP_CLEAR_TEXT", false);
            if (this.debug) {
                System.out.println("ServerManagerImpl: activate for server Id " + i2 + " found location " + serverLocationLocateServer.hostname + " and activated it");
            }
        } catch (NoSuchEndPoint e2) {
            if (this.debug) {
                System.out.println("ServerManagerImpl: activate for server Id  threw NoSuchEndpoint exception, which was ignored");
            }
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void active(int i2, Server server) throws ServerNotRegistered {
        Integer num = new Integer(i2);
        synchronized (this.serverTable) {
            ServerTableEntry serverTableEntry = (ServerTableEntry) this.serverTable.get(num);
            if (serverTableEntry == null) {
                if (this.debug) {
                    System.out.println("ServerManagerImpl: active for server Id " + i2 + " called, but no such server is registered.");
                }
                throw this.wrapper.serverNotExpectedToRegister();
            }
            if (this.debug) {
                System.out.println("ServerManagerImpl: active for server Id " + i2 + " called.  This server is now active.");
            }
            serverTableEntry.register(server);
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void registerEndpoints(int i2, String str, EndPointInfo[] endPointInfoArr) throws NoSuchEndPoint, ORBAlreadyRegistered, ServerNotRegistered {
        Integer num = new Integer(i2);
        synchronized (this.serverTable) {
            ServerTableEntry serverTableEntry = (ServerTableEntry) this.serverTable.get(num);
            if (serverTableEntry == null) {
                if (this.debug) {
                    System.out.println("ServerManagerImpl: registerEndpoint for server Id " + i2 + " called, but no such server is registered.");
                }
                throw this.wrapper.serverNotExpectedToRegister();
            }
            if (this.debug) {
                System.out.println("ServerManagerImpl: registerEndpoints for server Id " + i2 + " called.  This server is now active.");
            }
            serverTableEntry.registerPorts(str, endPointInfoArr);
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public int[] getActiveServers() {
        int[] iArr;
        synchronized (this.serverTable) {
            ArrayList arrayList = new ArrayList(0);
            Iterator it = this.serverTable.keySet().iterator();
            while (it.hasNext()) {
                try {
                    ServerTableEntry serverTableEntry = (ServerTableEntry) this.serverTable.get((Integer) it.next());
                    if (serverTableEntry.isValid() && serverTableEntry.isActive()) {
                        arrayList.add(serverTableEntry);
                    }
                } catch (NoSuchElementException e2) {
                }
            }
            iArr = new int[arrayList.size()];
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                iArr[i2] = ((ServerTableEntry) arrayList.get(i2)).getServerId();
            }
        }
        if (this.debug) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i3 : iArr) {
                stringBuffer.append(' ');
                stringBuffer.append(i3);
            }
            System.out.println("ServerManagerImpl: getActiveServers returns" + stringBuffer.toString());
        }
        return iArr;
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void shutdown(int i2) throws ServerNotActive {
        Integer num = new Integer(i2);
        synchronized (this.serverTable) {
            ServerTableEntry serverTableEntry = (ServerTableEntry) this.serverTable.remove(num);
            if (serverTableEntry == null) {
                if (this.debug) {
                    System.out.println("ServerManagerImpl: shutdown for server Id " + i2 + " throws ServerNotActive.");
                }
                throw new ServerNotActive(i2);
            }
            try {
                serverTableEntry.destroy();
                if (this.debug) {
                    System.out.println("ServerManagerImpl: shutdown for server Id " + i2 + " completed.");
                }
            } catch (Exception e2) {
                if (this.debug) {
                    System.out.println("ServerManagerImpl: shutdown for server Id " + i2 + " threw exception " + ((Object) e2));
                }
            }
        }
    }

    private ServerTableEntry getEntry(int i2) throws ServerNotRegistered {
        ServerTableEntry serverTableEntry;
        Integer num = new Integer(i2);
        synchronized (this.serverTable) {
            serverTableEntry = (ServerTableEntry) this.serverTable.get(num);
            if (this.debug) {
                if (serverTableEntry == null) {
                    System.out.println("ServerManagerImpl: getEntry: no active server found.");
                } else {
                    System.out.println("ServerManagerImpl: getEntry:  active server found " + ((Object) serverTableEntry) + ".");
                }
            }
            if (serverTableEntry != null && !serverTableEntry.isValid()) {
                this.serverTable.remove(num);
                serverTableEntry = null;
            }
            if (serverTableEntry == null) {
                serverTableEntry = new ServerTableEntry(this.wrapper, i2, this.repository.getServer(i2), this.initialPort, this.dbDirName, false, this.debug);
                this.serverTable.put(num, serverTableEntry);
                serverTableEntry.activate();
            }
        }
        return serverTableEntry;
    }

    private ServerLocation locateServer(ServerTableEntry serverTableEntry, String str, boolean z2) throws NoSuchEndPoint, ServerNotRegistered, ServerHeldDown {
        int length;
        ServerLocation serverLocation = new ServerLocation();
        if (z2) {
            try {
                ORBPortInfo[] oRBPortInfoArrLookup = serverTableEntry.lookup(str);
                serverLocation.hostname = this.orb.getLegacyServerSocketManager().legacyGetEndpoint(LegacyServerSocketEndPointInfo.DEFAULT_ENDPOINT).getHostName();
                if (oRBPortInfoArrLookup != null) {
                    length = oRBPortInfoArrLookup.length;
                } else {
                    length = 0;
                }
                serverLocation.ports = new ORBPortInfo[length];
                for (int i2 = 0; i2 < length; i2++) {
                    serverLocation.ports[i2] = new ORBPortInfo(oRBPortInfoArrLookup[i2].orbId, oRBPortInfoArrLookup[i2].port);
                    if (this.debug) {
                        System.out.println("ServerManagerImpl: locateServer: server located at location " + serverLocation.hostname + " ORBid  " + oRBPortInfoArrLookup[i2].orbId + " Port " + oRBPortInfoArrLookup[i2].port);
                    }
                }
            } catch (Exception e2) {
                if (this.debug) {
                    System.out.println("ServerManagerImpl: locateServer: server held down");
                }
                throw new ServerHeldDown(serverTableEntry.getServerId());
            }
        }
        return serverLocation;
    }

    private ServerLocationPerORB locateServerForORB(ServerTableEntry serverTableEntry, String str, boolean z2) throws InvalidORBid, ServerNotRegistered, ServerHeldDown {
        int length;
        ServerLocationPerORB serverLocationPerORB = new ServerLocationPerORB();
        if (z2) {
            try {
                EndPointInfo[] endPointInfoArrLookupForORB = serverTableEntry.lookupForORB(str);
                serverLocationPerORB.hostname = this.orb.getLegacyServerSocketManager().legacyGetEndpoint(LegacyServerSocketEndPointInfo.DEFAULT_ENDPOINT).getHostName();
                if (endPointInfoArrLookupForORB != null) {
                    length = endPointInfoArrLookupForORB.length;
                } else {
                    length = 0;
                }
                serverLocationPerORB.ports = new EndPointInfo[length];
                for (int i2 = 0; i2 < length; i2++) {
                    serverLocationPerORB.ports[i2] = new EndPointInfo(endPointInfoArrLookupForORB[i2].endpointType, endPointInfoArrLookupForORB[i2].port);
                    if (this.debug) {
                        System.out.println("ServerManagerImpl: locateServer: server located at location " + serverLocationPerORB.hostname + " endpointType  " + endPointInfoArrLookupForORB[i2].endpointType + " Port " + endPointInfoArrLookupForORB[i2].port);
                    }
                }
            } catch (InvalidORBid e2) {
                throw e2;
            } catch (Exception e3) {
                if (this.debug) {
                    System.out.println("ServerManagerImpl: locateServerForORB: server held down");
                }
                throw new ServerHeldDown(serverTableEntry.getServerId());
            }
        }
        return serverLocationPerORB;
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public String[] getORBNames(int i2) throws ServerNotRegistered {
        try {
            return getEntry(i2).getORBList();
        } catch (Exception e2) {
            throw new ServerNotRegistered(i2);
        }
    }

    private ServerTableEntry getRunningEntry(int i2) throws ServerNotRegistered {
        ServerTableEntry entry = getEntry(i2);
        try {
            entry.lookup("IIOP_CLEAR_TEXT");
            return entry;
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void install(int i2) throws ServerNotRegistered, ServerAlreadyInstalled, ServerHeldDown {
        ServerTableEntry runningEntry = getRunningEntry(i2);
        if (runningEntry != null) {
            this.repository.install(i2);
            runningEntry.install();
        }
    }

    @Override // com.sun.corba.se.spi.activation.ActivatorOperations
    public void uninstall(int i2) throws ServerNotRegistered, ServerHeldDown, ServerAlreadyUninstalled {
        if (((ServerTableEntry) this.serverTable.get(new Integer(i2))) != null) {
            ServerTableEntry serverTableEntry = (ServerTableEntry) this.serverTable.remove(new Integer(i2));
            if (serverTableEntry == null) {
                if (this.debug) {
                    System.out.println("ServerManagerImpl: shutdown for server Id " + i2 + " throws ServerNotActive.");
                }
                throw new ServerHeldDown(i2);
            }
            serverTableEntry.uninstall();
        }
    }

    @Override // com.sun.corba.se.spi.activation.LocatorOperations
    public ServerLocation locateServer(int i2, String str) throws NoSuchEndPoint, ServerNotRegistered, ServerHeldDown {
        ServerTableEntry entry = getEntry(i2);
        if (this.debug) {
            System.out.println("ServerManagerImpl: locateServer called with  serverId=" + i2 + " endpointType=" + str + " block=true");
        }
        return locateServer(entry, str, true);
    }

    @Override // com.sun.corba.se.spi.activation.LocatorOperations
    public ServerLocationPerORB locateServerForORB(int i2, String str) throws InvalidORBid, ServerNotRegistered, ServerHeldDown {
        ServerTableEntry entry = getEntry(i2);
        if (this.debug) {
            System.out.println("ServerManagerImpl: locateServerForORB called with  serverId=" + i2 + " orbId=" + str + " block=true");
        }
        return locateServerForORB(entry, str, true);
    }

    @Override // com.sun.corba.se.impl.oa.poa.BadServerIdHandler
    public void handle(ObjectKey objectKey) {
        ObjectKeyTemplate template = objectKey.getTemplate();
        int serverId = template.getServerId();
        String oRBId = template.getORBId();
        try {
            ServerLocationPerORB serverLocationPerORBLocateServerForORB = locateServerForORB(getEntry(serverId), oRBId, true);
            if (this.debug) {
                System.out.println("ServerManagerImpl: handle called for server id" + serverId + "  orbid  " + oRBId);
            }
            int i2 = 0;
            EndPointInfo[] endPointInfoArr = serverLocationPerORBLocateServerForORB.ports;
            int i3 = 0;
            while (true) {
                if (i3 >= endPointInfoArr.length) {
                    break;
                }
                if (!endPointInfoArr[i3].endpointType.equals("IIOP_CLEAR_TEXT")) {
                    i3++;
                } else {
                    i2 = endPointInfoArr[i3].port;
                    break;
                }
            }
            IIOPProfileTemplate iIOPProfileTemplateMakeIIOPProfileTemplate = IIOPFactories.makeIIOPProfileTemplate(this.orb, GIOPVersion.V1_2, IIOPFactories.makeIIOPAddress(this.orb, serverLocationPerORBLocateServerForORB.hostname, i2));
            if (GIOPVersion.V1_2.supportsIORIIOPProfileComponents()) {
                iIOPProfileTemplateMakeIIOPProfileTemplate.add(IIOPFactories.makeCodeSetsComponent(this.orb));
                iIOPProfileTemplateMakeIIOPProfileTemplate.add(IIOPFactories.makeMaxStreamFormatVersionComponent());
            }
            IORTemplate iORTemplateMakeIORTemplate = IORFactories.makeIORTemplate(template);
            iORTemplateMakeIORTemplate.add(iIOPProfileTemplateMakeIIOPProfileTemplate);
            IOR iorMakeIOR = iORTemplateMakeIORTemplate.makeIOR(this.orb, "IDL:org/omg/CORBA/Object:1.0", objectKey.getId());
            if (this.debug) {
                System.out.println("ServerManagerImpl: handle throws ForwardException");
            }
            try {
                Thread.sleep(this.serverStartupDelay);
            } catch (Exception e2) {
                System.out.println("Exception = " + ((Object) e2));
                e2.printStackTrace();
            }
            throw new ForwardException(this.orb, iorMakeIOR);
        } catch (Exception e3) {
            throw this.wrapper.errorInBadServerIdHandler(e3);
        }
    }

    @Override // com.sun.corba.se.spi.activation.LocatorOperations
    public int getEndpoint(String str) throws NoSuchEndPoint {
        return this.orb.getLegacyServerSocketManager().legacyGetTransientServerPort(str);
    }

    @Override // com.sun.corba.se.spi.activation.LocatorOperations
    public int getServerPortForType(ServerLocationPerORB serverLocationPerORB, String str) throws NoSuchEndPoint {
        EndPointInfo[] endPointInfoArr = serverLocationPerORB.ports;
        for (int i2 = 0; i2 < endPointInfoArr.length; i2++) {
            if (endPointInfoArr[i2].endpointType.equals(str)) {
                return endPointInfoArr[i2].port;
            }
        }
        throw new NoSuchEndPoint();
    }
}
