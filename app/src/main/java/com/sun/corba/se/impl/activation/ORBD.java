package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.legacy.connection.SocketFactoryAcceptorImpl;
import com.sun.corba.se.impl.naming.cosnaming.TransientNameService;
import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.transport.SocketOrChannelAcceptorImpl;
import com.sun.corba.se.spi.activation.Activator;
import com.sun.corba.se.spi.activation.ActivatorHelper;
import com.sun.corba.se.spi.activation.Locator;
import com.sun.corba.se.spi.activation.LocatorHelper;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.io.File;
import java.util.Properties;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.INTERNAL;

/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ORBD.class */
public class ORBD {
    private int initSvcPort;
    protected File dbDir;
    private String dbDirName;
    protected Locator locator;
    protected Activator activator;
    protected RepositoryImpl repository;
    private static String[][] orbServers = {new String[]{""}};

    protected void initializeBootNaming(ORB orb) {
        SocketOrChannelAcceptorImpl socketFactoryAcceptorImpl;
        this.initSvcPort = orb.getORBData().getORBInitialPort();
        if (orb.getORBData().getLegacySocketFactory() == null) {
            socketFactoryAcceptorImpl = new SocketOrChannelAcceptorImpl(orb, this.initSvcPort, LegacyServerSocketEndPointInfo.BOOT_NAMING, "IIOP_CLEAR_TEXT");
        } else {
            socketFactoryAcceptorImpl = new SocketFactoryAcceptorImpl(orb, this.initSvcPort, LegacyServerSocketEndPointInfo.BOOT_NAMING, "IIOP_CLEAR_TEXT");
        }
        orb.getCorbaTransportManager().registerAcceptor(socketFactoryAcceptorImpl);
    }

    protected ORB createORB(String[] strArr) {
        Properties properties = System.getProperties();
        properties.put(ORBConstants.SERVER_ID_PROPERTY, "1000");
        properties.put(ORBConstants.PERSISTENT_SERVER_PORT_PROPERTY, properties.getProperty(ORBConstants.ORBD_PORT_PROPERTY, Integer.toString(ORBConstants.DEFAULT_ACTIVATION_PORT)));
        properties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
        return (ORB) ORB.init(strArr, properties);
    }

    private void run(String[] strArr) {
        try {
            processArgs(strArr);
            ORB orbCreateORB = createORB(strArr);
            if (orbCreateORB.orbdDebugFlag) {
                System.out.println("ORBD begins initialization.");
            }
            boolean zCreateSystemDirs = createSystemDirs(ORBConstants.DEFAULT_DB_DIR);
            startActivationObjects(orbCreateORB);
            if (zCreateSystemDirs) {
                installOrbServers(getRepository(), getActivator());
            }
            if (orbCreateORB.orbdDebugFlag) {
                System.out.println("ORBD is ready.");
                System.out.println("ORBD serverid: " + System.getProperty(ORBConstants.SERVER_ID_PROPERTY));
                System.out.println("activation dbdir: " + System.getProperty(ORBConstants.DB_DIR_PROPERTY));
                System.out.println("activation port: " + System.getProperty(ORBConstants.ORBD_PORT_PROPERTY));
                String property = System.getProperty(ORBConstants.SERVER_POLLING_TIME);
                if (property == null) {
                    property = Integer.toString(1000);
                }
                System.out.println("activation Server Polling Time: " + property + " milli-seconds ");
                String property2 = System.getProperty(ORBConstants.SERVER_STARTUP_DELAY);
                if (property2 == null) {
                    property2 = Integer.toString(1000);
                }
                System.out.println("activation Server Startup Delay: " + property2 + " milli-seconds ");
            }
            new NameServiceStartThread(orbCreateORB, this.dbDir).start();
            orbCreateORB.run();
        } catch (COMM_FAILURE e2) {
            System.out.println(CorbaResourceUtil.getText("orbd.commfailure"));
            System.out.println(e2);
            e2.printStackTrace();
        } catch (INTERNAL e3) {
            System.out.println(CorbaResourceUtil.getText("orbd.internalexception"));
            System.out.println(e3);
            e3.printStackTrace();
        } catch (Exception e4) {
            System.out.println(CorbaResourceUtil.getText("orbd.usage", CORBALogDomains.ORBD));
            System.out.println(e4);
            e4.printStackTrace();
        }
    }

    private void processArgs(String[] strArr) {
        Properties properties = System.getProperties();
        int i2 = 0;
        while (i2 < strArr.length) {
            if (strArr[i2].equals("-port")) {
                if (i2 + 1 < strArr.length) {
                    i2++;
                    properties.put(ORBConstants.ORBD_PORT_PROPERTY, strArr[i2]);
                } else {
                    System.out.println(CorbaResourceUtil.getText("orbd.usage", CORBALogDomains.ORBD));
                }
            } else if (strArr[i2].equals("-defaultdb")) {
                if (i2 + 1 < strArr.length) {
                    i2++;
                    properties.put(ORBConstants.DB_DIR_PROPERTY, strArr[i2]);
                } else {
                    System.out.println(CorbaResourceUtil.getText("orbd.usage", CORBALogDomains.ORBD));
                }
            } else if (strArr[i2].equals("-serverid")) {
                if (i2 + 1 < strArr.length) {
                    i2++;
                    properties.put(ORBConstants.SERVER_ID_PROPERTY, strArr[i2]);
                } else {
                    System.out.println(CorbaResourceUtil.getText("orbd.usage", CORBALogDomains.ORBD));
                }
            } else if (strArr[i2].equals("-serverPollingTime")) {
                if (i2 + 1 < strArr.length) {
                    i2++;
                    properties.put(ORBConstants.SERVER_POLLING_TIME, strArr[i2]);
                } else {
                    System.out.println(CorbaResourceUtil.getText("orbd.usage", CORBALogDomains.ORBD));
                }
            } else if (strArr[i2].equals("-serverStartupDelay")) {
                if (i2 + 1 < strArr.length) {
                    i2++;
                    properties.put(ORBConstants.SERVER_STARTUP_DELAY, strArr[i2]);
                } else {
                    System.out.println(CorbaResourceUtil.getText("orbd.usage", CORBALogDomains.ORBD));
                }
            }
            i2++;
        }
    }

    protected boolean createSystemDirs(String str) {
        boolean z2 = false;
        Properties properties = System.getProperties();
        this.dbDir = new File(properties.getProperty(ORBConstants.DB_DIR_PROPERTY, properties.getProperty("user.dir") + properties.getProperty("file.separator") + str));
        this.dbDirName = this.dbDir.getAbsolutePath();
        properties.put(ORBConstants.DB_DIR_PROPERTY, this.dbDirName);
        if (!this.dbDir.exists()) {
            this.dbDir.mkdir();
            z2 = true;
        }
        File file = new File(this.dbDir, ORBConstants.SERVER_LOG_DIR);
        if (!file.exists()) {
            file.mkdir();
        }
        return z2;
    }

    protected File getDbDir() {
        return this.dbDir;
    }

    protected String getDbDirName() {
        return this.dbDirName;
    }

    protected void startActivationObjects(ORB orb) throws Exception {
        initializeBootNaming(orb);
        this.repository = new RepositoryImpl(orb, this.dbDir, orb.orbdDebugFlag);
        orb.register_initial_reference(ORBConstants.SERVER_REPOSITORY_NAME, this.repository);
        ServerManagerImpl serverManagerImpl = new ServerManagerImpl(orb, orb.getCorbaTransportManager(), this.repository, getDbDirName(), orb.orbdDebugFlag);
        this.locator = LocatorHelper.narrow(serverManagerImpl);
        orb.register_initial_reference(ORBConstants.SERVER_LOCATOR_NAME, this.locator);
        this.activator = ActivatorHelper.narrow(serverManagerImpl);
        orb.register_initial_reference(ORBConstants.SERVER_ACTIVATOR_NAME, this.activator);
        new TransientNameService(orb, ORBConstants.TRANSIENT_NAME_SERVICE_NAME);
    }

    protected Locator getLocator() {
        return this.locator;
    }

    protected Activator getActivator() {
        return this.activator;
    }

    protected RepositoryImpl getRepository() {
        return this.repository;
    }

    protected void installOrbServers(RepositoryImpl repositoryImpl, Activator activator) {
        for (int i2 = 0; i2 < orbServers.length; i2++) {
            try {
                String[] strArr = orbServers[i2];
                ServerDef serverDef = new ServerDef(strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]);
                int iIntValue = Integer.valueOf(orbServers[i2][0]).intValue();
                repositoryImpl.registerServer(serverDef, iIntValue);
                activator.activate(iIntValue);
            } catch (Exception e2) {
            }
        }
    }

    public static void main(String[] strArr) {
        new ORBD().run(strArr);
    }
}
