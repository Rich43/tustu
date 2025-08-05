package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.logging.ActivationSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.BadServerDefinition;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import com.sun.corba.se.spi.activation.ServerAlreadyInstalled;
import com.sun.corba.se.spi.activation.ServerAlreadyRegistered;
import com.sun.corba.se.spi.activation.ServerAlreadyUninstalled;
import com.sun.corba.se.spi.activation.ServerNotRegistered;
import com.sun.corba.se.spi.activation._RepositoryImplBase;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.SocketOrChannelAcceptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/corba/se/impl/activation/RepositoryImpl.class */
public class RepositoryImpl extends _RepositoryImplBase implements Serializable {
    private static final long serialVersionUID = 8458417785209341858L;
    private transient boolean debug;
    static final int illegalServerId = -1;
    private transient RepositoryDB db;
    transient ORB orb;
    transient ActivationSystemException wrapper;

    RepositoryImpl(ORB orb, File file, boolean z2) {
        this.debug = false;
        this.db = null;
        this.orb = null;
        this.debug = z2;
        this.orb = orb;
        this.wrapper = ActivationSystemException.get(orb, CORBALogDomains.ORBD_REPOSITORY);
        File file2 = new File(file, "servers.db");
        if (!file2.exists()) {
            this.db = new RepositoryDB(file2);
            this.db.flush();
        } else {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file2));
                this.db = (RepositoryDB) objectInputStream.readObject();
                objectInputStream.close();
            } catch (Exception e2) {
                throw this.wrapper.cannotReadRepositoryDb(e2);
            }
        }
        orb.connect(this);
    }

    private String printServerDef(ServerDef serverDef) {
        return "ServerDef[applicationName=" + serverDef.applicationName + " serverName=" + serverDef.serverName + " serverClassPath=" + serverDef.serverClassPath + " serverArgs=" + serverDef.serverArgs + " serverVmArgs=" + serverDef.serverVmArgs + "]";
    }

    public int registerServer(ServerDef serverDef, int i2) throws ServerAlreadyRegistered {
        int iIncrementServerIdCounter;
        int i3;
        synchronized (this.db) {
            Enumeration enumerationElements = this.db.serverTable.elements();
            while (enumerationElements.hasMoreElements()) {
                DBServerDef dBServerDef = (DBServerDef) enumerationElements.nextElement2();
                if (serverDef.applicationName.equals(dBServerDef.applicationName)) {
                    if (this.debug) {
                        System.out.println("RepositoryImpl: registerServer called to register ServerDef " + printServerDef(serverDef) + " with " + (i2 == -1 ? "a new server Id" : "server Id " + i2) + " FAILED because it is already registered.");
                    }
                    throw new ServerAlreadyRegistered(dBServerDef.id);
                }
            }
            if (i2 == -1) {
                iIncrementServerIdCounter = this.db.incrementServerIdCounter();
            } else {
                iIncrementServerIdCounter = i2;
            }
            this.db.serverTable.put(new Integer(iIncrementServerIdCounter), new DBServerDef(serverDef, iIncrementServerIdCounter));
            this.db.flush();
            if (this.debug) {
                if (i2 == -1) {
                    System.out.println("RepositoryImpl: registerServer called to register ServerDef " + printServerDef(serverDef) + " with new serverId " + iIncrementServerIdCounter);
                } else {
                    System.out.println("RepositoryImpl: registerServer called to register ServerDef " + printServerDef(serverDef) + " with assigned serverId " + iIncrementServerIdCounter);
                }
            }
            i3 = iIncrementServerIdCounter;
        }
        return i3;
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public int registerServer(ServerDef serverDef) throws ServerAlreadyRegistered, BadServerDefinition {
        switch (new ServerTableEntry(this.wrapper, -1, serverDef, ((SocketOrChannelAcceptor) this.orb.getLegacyServerSocketManager().legacyGetEndpoint(LegacyServerSocketEndPointInfo.BOOT_NAMING)).getServerSocket().getLocalPort(), "", true, this.debug).verify()) {
            case 0:
                return registerServer(serverDef, -1);
            case 1:
                throw new BadServerDefinition("main class not found.");
            case 2:
                throw new BadServerDefinition("no main method found.");
            case 3:
                throw new BadServerDefinition("server application error.");
            default:
                throw new BadServerDefinition("unknown Exception.");
        }
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public void unregisterServer(int i2) throws ServerNotRegistered {
        Integer num = new Integer(i2);
        synchronized (this.db) {
            if (((DBServerDef) this.db.serverTable.get(num)) == null) {
                if (this.debug) {
                    System.out.println("RepositoryImpl: unregisterServer for serverId " + i2 + " called: server not registered");
                }
                throw new ServerNotRegistered();
            }
            this.db.serverTable.remove(num);
            this.db.flush();
        }
        if (this.debug) {
            System.out.println("RepositoryImpl: unregisterServer for serverId " + i2 + " called");
        }
    }

    private DBServerDef getDBServerDef(int i2) throws ServerNotRegistered {
        DBServerDef dBServerDef = (DBServerDef) this.db.serverTable.get(new Integer(i2));
        if (dBServerDef == null) {
            throw new ServerNotRegistered(i2);
        }
        return dBServerDef;
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public ServerDef getServer(int i2) throws ServerNotRegistered {
        DBServerDef dBServerDef = getDBServerDef(i2);
        ServerDef serverDef = new ServerDef(dBServerDef.applicationName, dBServerDef.name, dBServerDef.classPath, dBServerDef.args, dBServerDef.vmArgs);
        if (this.debug) {
            System.out.println("RepositoryImpl: getServer for serverId " + i2 + " returns " + printServerDef(serverDef));
        }
        return serverDef;
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public boolean isInstalled(int i2) throws ServerNotRegistered {
        return getDBServerDef(i2).isInstalled;
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public void install(int i2) throws ServerNotRegistered, ServerAlreadyInstalled {
        DBServerDef dBServerDef = getDBServerDef(i2);
        if (dBServerDef.isInstalled) {
            throw new ServerAlreadyInstalled(i2);
        }
        dBServerDef.isInstalled = true;
        this.db.flush();
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public void uninstall(int i2) throws ServerNotRegistered, ServerAlreadyUninstalled {
        DBServerDef dBServerDef = getDBServerDef(i2);
        if (!dBServerDef.isInstalled) {
            throw new ServerAlreadyUninstalled(i2);
        }
        dBServerDef.isInstalled = false;
        this.db.flush();
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public int[] listRegisteredServers() {
        int[] iArr;
        synchronized (this.db) {
            int i2 = 0;
            iArr = new int[this.db.serverTable.size()];
            Enumeration enumerationElements = this.db.serverTable.elements();
            while (enumerationElements.hasMoreElements()) {
                int i3 = i2;
                i2++;
                iArr[i3] = ((DBServerDef) enumerationElements.nextElement2()).id;
            }
            if (this.debug) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i4 : iArr) {
                    stringBuffer.append(' ');
                    stringBuffer.append(i4);
                }
                System.out.println("RepositoryImpl: listRegisteredServers returns" + stringBuffer.toString());
            }
        }
        return iArr;
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public int getServerID(String str) throws ServerNotRegistered {
        int i2;
        synchronized (this.db) {
            int iIntValue = -1;
            Enumeration enumerationKeys = this.db.serverTable.keys();
            while (true) {
                if (!enumerationKeys.hasMoreElements()) {
                    break;
                }
                Integer num = (Integer) enumerationKeys.nextElement2();
                if (((DBServerDef) this.db.serverTable.get(num)).applicationName.equals(str)) {
                    iIntValue = num.intValue();
                    break;
                }
            }
            if (this.debug) {
                System.out.println("RepositoryImpl: getServerID for " + str + " is " + iIntValue);
            }
            if (iIntValue == -1) {
                throw new ServerNotRegistered();
            }
            i2 = iIntValue;
        }
        return i2;
    }

    @Override // com.sun.corba.se.spi.activation.RepositoryOperations
    public String[] getApplicationNames() {
        String[] strArr;
        synchronized (this.db) {
            Vector vector = new Vector();
            Enumeration enumerationKeys = this.db.serverTable.keys();
            while (enumerationKeys.hasMoreElements()) {
                DBServerDef dBServerDef = (DBServerDef) this.db.serverTable.get((Integer) enumerationKeys.nextElement2());
                if (!dBServerDef.applicationName.equals("")) {
                    vector.addElement(dBServerDef.applicationName);
                }
            }
            strArr = new String[vector.size()];
            for (int i2 = 0; i2 < vector.size(); i2++) {
                strArr[i2] = (String) vector.elementAt(i2);
            }
            if (this.debug) {
                StringBuffer stringBuffer = new StringBuffer();
                for (String str : strArr) {
                    stringBuffer.append(' ');
                    stringBuffer.append(str);
                }
                System.out.println("RepositoryImpl: getApplicationNames returns " + stringBuffer.toString());
            }
        }
        return strArr;
    }

    public static void main(String[] strArr) {
        boolean z2 = false;
        for (String str : strArr) {
            if (str.equals("-debug")) {
                z2 = true;
            }
        }
        try {
            Properties properties = new Properties();
            properties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
            ORB orb = (ORB) ORB.init(strArr, properties);
            new RepositoryImpl(orb, new File(System.getProperty(ORBConstants.DB_PROPERTY, ORBConstants.DEFAULT_DB_NAME)), z2);
            orb.run();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/activation/RepositoryImpl$RepositoryDB.class */
    class RepositoryDB implements Serializable {
        File db;
        Hashtable serverTable = new Hashtable(255);
        Integer serverIdCounter = new Integer(256);

        RepositoryDB(File file) {
            this.db = file;
        }

        int incrementServerIdCounter() {
            int iIntValue = this.serverIdCounter.intValue() + 1;
            this.serverIdCounter = new Integer(iIntValue);
            return iIntValue;
        }

        void flush() {
            try {
                this.db.delete();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.db));
                objectOutputStream.writeObject(this);
                objectOutputStream.flush();
                objectOutputStream.close();
            } catch (Exception e2) {
                throw RepositoryImpl.this.wrapper.cannotWriteRepositoryDb(e2);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/activation/RepositoryImpl$DBServerDef.class */
    class DBServerDef implements Serializable {
        String applicationName;
        String name;
        String classPath;
        String args;
        String vmArgs;
        boolean isInstalled = false;
        int id;

        public String toString() {
            return "DBServerDef(applicationName=" + this.applicationName + ", name=" + this.name + ", classPath=" + this.classPath + ", args=" + this.args + ", vmArgs=" + this.vmArgs + ", id=" + this.id + ", isInstalled=" + this.isInstalled + ")";
        }

        DBServerDef(ServerDef serverDef, int i2) {
            this.applicationName = serverDef.applicationName;
            this.name = serverDef.serverName;
            this.classPath = serverDef.serverClassPath;
            this.args = serverDef.serverArgs;
            this.vmArgs = serverDef.serverVmArgs;
            this.id = i2;
        }
    }
}
