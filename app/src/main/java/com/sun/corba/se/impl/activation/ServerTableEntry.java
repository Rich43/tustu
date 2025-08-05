package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.logging.ActivationSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.activation.EndPointInfo;
import com.sun.corba.se.spi.activation.InvalidORBid;
import com.sun.corba.se.spi.activation.ORBAlreadyRegistered;
import com.sun.corba.se.spi.activation.ORBPortInfo;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import com.sun.corba.se.spi.activation.Server;
import com.sun.corba.se.spi.activation.ServerHeldDown;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ServerTableEntry.class */
public class ServerTableEntry {
    private static final int DE_ACTIVATED = 0;
    private static final int ACTIVATING = 1;
    private static final int ACTIVATED = 2;
    private static final int RUNNING = 3;
    private static final int HELD_DOWN = 4;
    private static final long waitTime = 2000;
    private static final int ActivationRetryMax = 5;
    private int serverId;
    private Server serverObj;
    private ServerDef serverDef;
    private Process process;
    private int activateRetryCount;
    private String activationCmd;
    private ActivationSystemException wrapper;
    private static String javaHome = System.getProperty("java.home");
    private static String classPath = System.getProperty("java.class.path");
    private static String fileSep = System.getProperty("file.separator");
    private static String pathSep = System.getProperty("path.separator");
    private boolean debug;
    private HashMap orbAndPortInfo = new HashMap(255);
    private int state = 1;

    private String printState() {
        String str = "UNKNOWN";
        switch (this.state) {
            case 0:
                str = "DE_ACTIVATED";
                break;
            case 1:
                str = "ACTIVATING  ";
                break;
            case 2:
                str = "ACTIVATED   ";
                break;
            case 3:
                str = "RUNNING     ";
                break;
            case 4:
                str = "HELD_DOWN   ";
                break;
        }
        return str;
    }

    public String toString() {
        return "ServerTableEntry[state=" + printState() + " serverId=" + this.serverId + " activateRetryCount=" + this.activateRetryCount + "]";
    }

    ServerTableEntry(ActivationSystemException activationSystemException, int i2, ServerDef serverDef, int i3, String str, boolean z2, boolean z3) {
        this.activateRetryCount = 0;
        this.debug = false;
        this.wrapper = activationSystemException;
        this.serverId = i2;
        this.serverDef = serverDef;
        this.debug = z3;
        this.activateRetryCount = 0;
        this.activationCmd = javaHome + fileSep + "bin" + fileSep + "java " + serverDef.serverVmArgs + " -Dioser=" + System.getProperty("ioser") + " -D" + ORBConstants.INITIAL_PORT_PROPERTY + "=" + i3 + " -D" + ORBConstants.DB_DIR_PROPERTY + "=" + str + " -D" + ORBConstants.ACTIVATED_PROPERTY + "=true -D" + ORBConstants.SERVER_ID_PROPERTY + "=" + i2 + " -D" + ORBConstants.SERVER_NAME_PROPERTY + "=" + serverDef.serverName + " " + (z2 ? "-Dcom.sun.CORBA.activation.ORBServerVerify=true " : "") + "-classpath " + classPath + (serverDef.serverClassPath.equals("") ? "" : pathSep) + serverDef.serverClassPath + " com.sun.corba.se.impl.activation.ServerMain " + serverDef.serverArgs + (z3 ? " -debug" : "");
        if (z3) {
            System.out.println("ServerTableEntry constructed with activation command " + this.activationCmd);
        }
    }

    public int verify() {
        try {
            if (this.debug) {
                System.out.println("Server being verified w/" + this.activationCmd);
            }
            this.process = Runtime.getRuntime().exec(this.activationCmd);
            int iWaitFor = this.process.waitFor();
            if (this.debug) {
                printDebug("verify", "returns " + ServerMain.printResult(iWaitFor));
            }
            return iWaitFor;
        } catch (Exception e2) {
            if (this.debug) {
                printDebug("verify", "returns unknown error because of exception " + ((Object) e2));
                return 4;
            }
            return 4;
        }
    }

    private void printDebug(String str, String str2) {
        System.out.println("ServerTableEntry: method  =" + str);
        System.out.println("ServerTableEntry: server  =" + this.serverId);
        System.out.println("ServerTableEntry: state   =" + printState());
        System.out.println("ServerTableEntry: message =" + str2);
        System.out.println();
    }

    synchronized void activate() throws SystemException {
        this.state = 2;
        try {
            if (this.debug) {
                printDebug("activate", "activating server");
            }
            this.process = Runtime.getRuntime().exec(this.activationCmd);
        } catch (Exception e2) {
            deActivate();
            if (this.debug) {
                printDebug("activate", "throwing premature process exit");
            }
            throw this.wrapper.unableToStartProcess();
        }
    }

    synchronized void register(Server server) {
        if (this.state == 2) {
            this.serverObj = server;
            if (this.debug) {
                printDebug("register", "process registered back");
                return;
            }
            return;
        }
        if (this.debug) {
            printDebug("register", "throwing premature process exit");
        }
        throw this.wrapper.serverNotExpectedToRegister();
    }

    synchronized void registerPorts(String str, EndPointInfo[] endPointInfoArr) throws ORBAlreadyRegistered {
        if (this.orbAndPortInfo.containsKey(str)) {
            throw new ORBAlreadyRegistered(str);
        }
        int length = endPointInfoArr.length;
        EndPointInfo[] endPointInfoArr2 = new EndPointInfo[length];
        for (int i2 = 0; i2 < length; i2++) {
            endPointInfoArr2[i2] = new EndPointInfo(endPointInfoArr[i2].endpointType, endPointInfoArr[i2].port);
            if (this.debug) {
                System.out.println("registering type: " + endPointInfoArr2[i2].endpointType + "  port  " + endPointInfoArr2[i2].port);
            }
        }
        this.orbAndPortInfo.put(str, endPointInfoArr2);
        if (this.state == 2) {
            this.state = 3;
            notifyAll();
        }
        if (this.debug) {
            printDebug("registerPorts", "process registered Ports");
        }
    }

    void install() {
        Server server;
        synchronized (this) {
            if (this.state == 3) {
                server = this.serverObj;
            } else {
                throw this.wrapper.serverNotRunning();
            }
        }
        if (server != null) {
            server.install();
        }
    }

    void uninstall() {
        Server server;
        Process process;
        synchronized (this) {
            server = this.serverObj;
            process = this.process;
            if (this.state == 3) {
                deActivate();
            } else {
                throw this.wrapper.serverNotRunning();
            }
        }
        if (server != null) {
            try {
                server.shutdown();
                server.uninstall();
            } catch (Exception e2) {
                return;
            }
        }
        if (process != null) {
            process.destroy();
        }
    }

    synchronized void holdDown() {
        this.state = 4;
        if (this.debug) {
            printDebug("holdDown", "server held down");
        }
        notifyAll();
    }

    synchronized void deActivate() {
        this.state = 0;
        if (this.debug) {
            printDebug("deActivate", "server deactivated");
        }
        notifyAll();
    }

    synchronized void checkProcessHealth() {
        if (this.state == 3) {
            try {
                this.process.exitValue();
                synchronized (this) {
                    this.orbAndPortInfo.clear();
                    deActivate();
                }
            } catch (IllegalThreadStateException e2) {
            }
        }
    }

    synchronized boolean isValid() throws SystemException {
        if (this.state == 1 || this.state == 4) {
            if (this.debug) {
                printDebug("isValid", "returns true");
                return true;
            }
            return true;
        }
        try {
            this.process.exitValue();
            if (this.state == 2) {
                if (this.activateRetryCount < 5) {
                    if (this.debug) {
                        printDebug("isValid", "reactivating server");
                    }
                    this.activateRetryCount++;
                    activate();
                    return true;
                }
                if (this.debug) {
                    printDebug("isValid", "holding server down");
                }
                holdDown();
                return true;
            }
            deActivate();
            return false;
        } catch (IllegalThreadStateException e2) {
            return true;
        }
    }

    synchronized ORBPortInfo[] lookup(String str) throws ServerHeldDown {
        while (true) {
            if (this.state != 1 && this.state != 2) {
                break;
            }
            try {
                wait(waitTime);
            } catch (Exception e2) {
            }
            if (!isValid()) {
                break;
            }
        }
        if (this.state == 3) {
            ORBPortInfo[] oRBPortInfoArr = new ORBPortInfo[this.orbAndPortInfo.size()];
            int i2 = 0;
            for (String str2 : this.orbAndPortInfo.keySet()) {
                try {
                    EndPointInfo[] endPointInfoArr = (EndPointInfo[]) this.orbAndPortInfo.get(str2);
                    int i3 = -1;
                    int i4 = 0;
                    while (true) {
                        if (i4 >= endPointInfoArr.length) {
                            break;
                        }
                        if (this.debug) {
                            System.out.println("lookup num-ports " + endPointInfoArr.length + "   " + endPointInfoArr[i4].endpointType + "   " + endPointInfoArr[i4].port);
                        }
                        if (!endPointInfoArr[i4].endpointType.equals(str)) {
                            i4++;
                        } else {
                            i3 = endPointInfoArr[i4].port;
                            break;
                        }
                    }
                    oRBPortInfoArr[i2] = new ORBPortInfo(str2, i3);
                    i2++;
                } catch (NoSuchElementException e3) {
                }
            }
            return oRBPortInfoArr;
        }
        if (this.debug) {
            printDebug("lookup", "throwing server held down error");
        }
        throw new ServerHeldDown(this.serverId);
    }

    synchronized EndPointInfo[] lookupForORB(String str) throws InvalidORBid, ServerHeldDown {
        while (true) {
            if (this.state != 1 && this.state != 2) {
                break;
            }
            try {
                wait(waitTime);
            } catch (Exception e2) {
            }
            if (!isValid()) {
                break;
            }
        }
        if (this.state == 3) {
            try {
                EndPointInfo[] endPointInfoArr = (EndPointInfo[]) this.orbAndPortInfo.get(str);
                EndPointInfo[] endPointInfoArr2 = new EndPointInfo[endPointInfoArr.length];
                for (int i2 = 0; i2 < endPointInfoArr.length; i2++) {
                    if (this.debug) {
                        System.out.println("lookup num-ports " + endPointInfoArr.length + "   " + endPointInfoArr[i2].endpointType + "   " + endPointInfoArr[i2].port);
                    }
                    endPointInfoArr2[i2] = new EndPointInfo(endPointInfoArr[i2].endpointType, endPointInfoArr[i2].port);
                }
                return endPointInfoArr2;
            } catch (NoSuchElementException e3) {
                throw new InvalidORBid();
            }
        }
        if (this.debug) {
            printDebug("lookup", "throwing server held down error");
        }
        throw new ServerHeldDown(this.serverId);
    }

    synchronized String[] getORBList() {
        String[] strArr = new String[this.orbAndPortInfo.size()];
        Iterator it = this.orbAndPortInfo.keySet().iterator();
        int i2 = 0;
        while (it.hasNext()) {
            try {
                int i3 = i2;
                i2++;
                strArr[i3] = (String) it.next();
            } catch (NoSuchElementException e2) {
            }
        }
        return strArr;
    }

    int getServerId() {
        return this.serverId;
    }

    boolean isActive() {
        return this.state == 3 || this.state == 2;
    }

    synchronized void destroy() {
        Server server;
        Process process;
        server = this.serverObj;
        process = this.process;
        deActivate();
        if (server != null) {
            try {
                server.shutdown();
            } catch (Exception e2) {
                if (this.debug) {
                    printDebug("destroy", "server shutdown threw exception" + ((Object) e2));
                }
            }
        }
        if (this.debug) {
            printDebug("destroy", "server shutdown successfully");
        }
        if (process != null) {
            try {
                process.destroy();
            } catch (Exception e3) {
                if (this.debug) {
                    printDebug("destroy", "process destroy threw exception" + ((Object) e3));
                    return;
                }
                return;
            }
        }
        if (this.debug) {
            printDebug("destroy", "process destroyed successfully");
        }
    }
}
