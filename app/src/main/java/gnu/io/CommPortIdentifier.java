package gnu.io;

import java.io.FileDescriptor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

/* loaded from: RXTXcomm.jar:gnu/io/CommPortIdentifier.class */
public class CommPortIdentifier {
    public static final int PORT_SERIAL = 1;
    public static final int PORT_PARALLEL = 2;
    public static final int PORT_I2C = 3;
    public static final int PORT_RS485 = 4;
    public static final int PORT_RAW = 5;
    private String PortName;
    private String Owner;
    private CommPort commport;
    private CommDriver RXTXDriver;
    static CommPortIdentifier CommPortIndex;
    private int PortType;
    private static final boolean debug = false;
    static Object Sync = new Object();
    Vector ownershipListener;
    private boolean HideOwnerEvents;
    private boolean Available = true;
    CommPortIdentifier next = null;

    private native String native_psmisc_report_owner(String str);

    static {
        try {
            ((CommDriver) Class.forName("gnu.io.RXTXCommDriver").newInstance()).initialize();
        } catch (Throwable th) {
            System.err.println(new StringBuffer().append((Object) th).append(" thrown while loading ").append("gnu.io.RXTXCommDriver").toString());
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") == -1) {
        }
        System.loadLibrary("rxtxSerial");
    }

    CommPortIdentifier(String str, CommPort commPort, int i2, CommDriver commDriver) {
        this.PortName = str;
        this.commport = commPort;
        this.PortType = i2;
        this.RXTXDriver = commDriver;
    }

    public static void addPortName(String str, int i2, CommDriver commDriver) {
        AddIdentifierToList(new CommPortIdentifier(str, null, i2, commDriver));
    }

    private static void AddIdentifierToList(CommPortIdentifier commPortIdentifier) {
        synchronized (Sync) {
            if (CommPortIndex == null) {
                CommPortIndex = commPortIdentifier;
            } else {
                CommPortIdentifier commPortIdentifier2 = CommPortIndex;
                while (commPortIdentifier2.next != null) {
                    commPortIdentifier2 = commPortIdentifier2.next;
                }
                commPortIdentifier2.next = commPortIdentifier;
            }
        }
    }

    public void addPortOwnershipListener(CommPortOwnershipListener commPortOwnershipListener) {
        if (this.ownershipListener == null) {
            this.ownershipListener = new Vector();
        }
        if (!this.ownershipListener.contains(commPortOwnershipListener)) {
            this.ownershipListener.addElement(commPortOwnershipListener);
        }
    }

    public String getCurrentOwner() {
        return this.Owner;
    }

    public String getName() {
        return this.PortName;
    }

    public static CommPortIdentifier getPortIdentifier(String str) throws NoSuchPortException {
        CommPortIdentifier commPortIdentifier;
        synchronized (Sync) {
            commPortIdentifier = CommPortIndex;
            while (commPortIdentifier != null && !commPortIdentifier.PortName.equals(str)) {
                commPortIdentifier = commPortIdentifier.next;
            }
            if (commPortIdentifier == null) {
                getPortIdentifiers();
                commPortIdentifier = CommPortIndex;
                while (commPortIdentifier != null && !commPortIdentifier.PortName.equals(str)) {
                    commPortIdentifier = commPortIdentifier.next;
                }
            }
        }
        if (commPortIdentifier != null) {
            return commPortIdentifier;
        }
        throw new NoSuchPortException();
    }

    public static CommPortIdentifier getPortIdentifier(CommPort commPort) throws NoSuchPortException {
        CommPortIdentifier commPortIdentifier;
        synchronized (Sync) {
            commPortIdentifier = CommPortIndex;
            while (commPortIdentifier != null && commPortIdentifier.commport != commPort) {
                commPortIdentifier = commPortIdentifier.next;
            }
        }
        if (commPortIdentifier != null) {
            return commPortIdentifier;
        }
        throw new NoSuchPortException();
    }

    public static Enumeration getPortIdentifiers() {
        synchronized (Sync) {
            HashMap map = new HashMap();
            for (CommPortIdentifier commPortIdentifier = CommPortIndex; commPortIdentifier != null; commPortIdentifier = commPortIdentifier.next) {
                map.put(commPortIdentifier.PortName, commPortIdentifier);
            }
            CommPortIndex = null;
            try {
                ((CommDriver) Class.forName("gnu.io.RXTXCommDriver").newInstance()).initialize();
                CommPortIdentifier commPortIdentifier2 = null;
                for (CommPortIdentifier commPortIdentifier3 = CommPortIndex; commPortIdentifier3 != null; commPortIdentifier3 = commPortIdentifier3.next) {
                    CommPortIdentifier commPortIdentifier4 = (CommPortIdentifier) map.get(commPortIdentifier3.PortName);
                    if (commPortIdentifier4 != null && commPortIdentifier4.PortType == commPortIdentifier3.PortType) {
                        commPortIdentifier4.RXTXDriver = commPortIdentifier3.RXTXDriver;
                        commPortIdentifier4.next = commPortIdentifier3.next;
                        if (commPortIdentifier2 == null) {
                            CommPortIndex = commPortIdentifier4;
                        } else {
                            commPortIdentifier2.next = commPortIdentifier4;
                        }
                        commPortIdentifier2 = commPortIdentifier4;
                    } else {
                        commPortIdentifier2 = commPortIdentifier3;
                    }
                }
            } catch (Throwable th) {
                System.err.println(new StringBuffer().append((Object) th).append(" thrown while loading ").append("gnu.io.RXTXCommDriver").toString());
                System.err.flush();
            }
        }
        return new CommPortEnumerator();
    }

    public int getPortType() {
        return this.PortType;
    }

    public synchronized boolean isCurrentlyOwned() {
        return !this.Available;
    }

    public synchronized CommPort open(FileDescriptor fileDescriptor) throws UnsupportedCommOperationException {
        throw new UnsupportedCommOperationException();
    }

    public CommPort open(String str, int i2) throws PortInUseException {
        boolean z2;
        synchronized (this) {
            z2 = this.Available;
            if (z2) {
                this.Available = false;
                this.Owner = str;
            }
        }
        if (!z2) {
            long jCurrentTimeMillis = System.currentTimeMillis() + i2;
            fireOwnershipEvent(3);
            synchronized (this) {
                while (!this.Available) {
                    long jCurrentTimeMillis2 = System.currentTimeMillis();
                    if (jCurrentTimeMillis2 >= jCurrentTimeMillis) {
                        break;
                    }
                    try {
                        wait(jCurrentTimeMillis - jCurrentTimeMillis2);
                    } catch (InterruptedException e2) {
                        Thread.currentThread().interrupt();
                    }
                }
                z2 = this.Available;
                if (z2) {
                    this.Available = false;
                    this.Owner = str;
                }
            }
        }
        if (!z2) {
            throw new PortInUseException(getCurrentOwner());
        }
        try {
            if (this.commport == null) {
                this.commport = this.RXTXDriver.getCommPort(this.PortName, this.PortType);
            }
            if (this.commport != null) {
                fireOwnershipEvent(1);
                CommPort commPort = this.commport;
                if (this.commport == null) {
                    synchronized (this) {
                        this.Available = true;
                        this.Owner = null;
                    }
                }
                return commPort;
            }
            throw new PortInUseException(native_psmisc_report_owner(this.PortName));
        } catch (Throwable th) {
            if (this.commport == null) {
                synchronized (this) {
                    this.Available = true;
                    this.Owner = null;
                }
            }
            throw th;
        }
    }

    public void removePortOwnershipListener(CommPortOwnershipListener commPortOwnershipListener) {
        if (this.ownershipListener != null) {
            this.ownershipListener.removeElement(commPortOwnershipListener);
        }
    }

    void internalClosePort() {
        synchronized (this) {
            this.Owner = null;
            this.Available = true;
            this.commport = null;
            notifyAll();
        }
        fireOwnershipEvent(2);
    }

    void fireOwnershipEvent(int i2) {
        if (this.ownershipListener != null) {
            Enumeration enumerationElements = this.ownershipListener.elements();
            while (enumerationElements.hasMoreElements()) {
                ((CommPortOwnershipListener) enumerationElements.nextElement()).ownershipChange(i2);
            }
        }
    }
}
