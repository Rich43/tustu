package java.net;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/net/NetworkInterface.class */
public final class NetworkInterface {
    private String name;
    private String displayName;
    private int index;
    private InetAddress[] addrs;
    private InterfaceAddress[] bindings;
    private NetworkInterface[] childs;
    private NetworkInterface parent = null;
    private boolean virtual = false;
    private static final NetworkInterface defaultInterface;
    private static final int defaultIndex;

    private static native NetworkInterface[] getAll() throws SocketException;

    private static native NetworkInterface getByName0(String str) throws SocketException;

    private static native NetworkInterface getByIndex0(int i2) throws SocketException;

    private static native NetworkInterface getByInetAddress0(InetAddress inetAddress) throws SocketException;

    private static native boolean isUp0(String str, int i2) throws SocketException;

    private static native boolean isLoopback0(String str, int i2) throws SocketException;

    private static native boolean supportsMulticast0(String str, int i2) throws SocketException;

    private static native boolean isP2P0(String str, int i2) throws SocketException;

    private static native byte[] getMacAddr0(byte[] bArr, String str, int i2) throws SocketException;

    private static native int getMTU0(String str, int i2) throws SocketException;

    private static native void init();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.net.NetworkInterface.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("net");
                return null;
            }
        });
        init();
        defaultInterface = DefaultInterface.getDefault();
        if (defaultInterface != null) {
            defaultIndex = defaultInterface.getIndex();
        } else {
            defaultIndex = 0;
        }
    }

    NetworkInterface() {
    }

    NetworkInterface(String str, int i2, InetAddress[] inetAddressArr) {
        this.name = str;
        this.index = i2;
        this.addrs = inetAddressArr;
    }

    public String getName() {
        return this.name;
    }

    public Enumeration<InetAddress> getInetAddresses() {
        return new Enumeration<InetAddress>() { // from class: java.net.NetworkInterface.1checkedAddresses

            /* renamed from: i, reason: collision with root package name */
            private int f12446i = 0;
            private int count;
            private InetAddress[] local_addrs;

            {
                this.count = 0;
                this.local_addrs = new InetAddress[NetworkInterface.this.addrs.length];
                boolean z2 = true;
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    try {
                        securityManager.checkPermission(new NetPermission("getNetworkInformation"));
                    } catch (SecurityException e2) {
                        z2 = false;
                    }
                }
                for (int i2 = 0; i2 < NetworkInterface.this.addrs.length; i2++) {
                    if (securityManager != null && !z2) {
                        try {
                            securityManager.checkConnect(NetworkInterface.this.addrs[i2].getHostAddress(), -1);
                        } catch (SecurityException e3) {
                        }
                    }
                    InetAddress[] inetAddressArr = this.local_addrs;
                    int i3 = this.count;
                    this.count = i3 + 1;
                    inetAddressArr[i3] = NetworkInterface.this.addrs[i2];
                }
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            public InetAddress nextElement() {
                if (this.f12446i < this.count) {
                    InetAddress[] inetAddressArr = this.local_addrs;
                    int i2 = this.f12446i;
                    this.f12446i = i2 + 1;
                    return inetAddressArr[i2];
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.f12446i < this.count;
            }
        };
    }

    public List<InterfaceAddress> getInterfaceAddresses() {
        ArrayList arrayList = new ArrayList(1);
        SecurityManager securityManager = System.getSecurityManager();
        for (int i2 = 0; i2 < this.bindings.length; i2++) {
            if (securityManager != null) {
                try {
                    securityManager.checkConnect(this.bindings[i2].getAddress().getHostAddress(), -1);
                } catch (SecurityException e2) {
                }
            }
            arrayList.add(this.bindings[i2]);
        }
        return arrayList;
    }

    public Enumeration<NetworkInterface> getSubInterfaces() {
        return new Enumeration<NetworkInterface>() { // from class: java.net.NetworkInterface.1subIFs

            /* renamed from: i, reason: collision with root package name */
            private int f12447i = 0;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            public NetworkInterface nextElement() {
                if (this.f12447i < NetworkInterface.this.childs.length) {
                    NetworkInterface[] networkInterfaceArr = NetworkInterface.this.childs;
                    int i2 = this.f12447i;
                    this.f12447i = i2 + 1;
                    return networkInterfaceArr[i2];
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.f12447i < NetworkInterface.this.childs.length;
            }
        };
    }

    public NetworkInterface getParent() {
        return this.parent;
    }

    public int getIndex() {
        return this.index;
    }

    public String getDisplayName() {
        if ("".equals(this.displayName)) {
            return null;
        }
        return this.displayName;
    }

    public static NetworkInterface getByName(String str) throws SocketException {
        if (str == null) {
            throw new NullPointerException();
        }
        return getByName0(str);
    }

    public static NetworkInterface getByIndex(int i2) throws SocketException {
        if (i2 < 0) {
            throw new IllegalArgumentException("Interface index can't be negative");
        }
        return getByIndex0(i2);
    }

    public static NetworkInterface getByInetAddress(InetAddress inetAddress) throws SocketException {
        if (inetAddress == null) {
            throw new NullPointerException();
        }
        if (inetAddress instanceof Inet4Address) {
            Inet4Address inet4Address = (Inet4Address) inetAddress;
            if (inet4Address.holder.family != 1) {
                throw new IllegalArgumentException("invalid family type: " + inet4Address.holder.family);
            }
        } else if (inetAddress instanceof Inet6Address) {
            Inet6Address inet6Address = (Inet6Address) inetAddress;
            if (inet6Address.holder.family != 2) {
                throw new IllegalArgumentException("invalid family type: " + inet6Address.holder.family);
            }
        } else {
            throw new IllegalArgumentException("invalid address type: " + ((Object) inetAddress));
        }
        return getByInetAddress0(inetAddress);
    }

    public static Enumeration<NetworkInterface> getNetworkInterfaces() throws SocketException {
        final NetworkInterface[] all = getAll();
        if (all == null) {
            return null;
        }
        return new Enumeration<NetworkInterface>() { // from class: java.net.NetworkInterface.2

            /* renamed from: i, reason: collision with root package name */
            private int f12448i = 0;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            public NetworkInterface nextElement() {
                if (all != null && this.f12448i < all.length) {
                    NetworkInterface[] networkInterfaceArr = all;
                    int i2 = this.f12448i;
                    this.f12448i = i2 + 1;
                    return networkInterfaceArr[i2];
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return all != null && this.f12448i < all.length;
            }
        };
    }

    public boolean isUp() throws SocketException {
        return isUp0(this.name, this.index);
    }

    public boolean isLoopback() throws SocketException {
        return isLoopback0(this.name, this.index);
    }

    public boolean isPointToPoint() throws SocketException {
        return isP2P0(this.name, this.index);
    }

    public boolean supportsMulticast() throws SocketException {
        return supportsMulticast0(this.name, this.index);
    }

    public byte[] getHardwareAddress() throws SocketException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkPermission(new NetPermission("getNetworkInformation"));
            } catch (SecurityException e2) {
                if (!getInetAddresses().hasMoreElements()) {
                    return null;
                }
            }
        }
        for (InetAddress inetAddress : this.addrs) {
            if (inetAddress instanceof Inet4Address) {
                return getMacAddr0(((Inet4Address) inetAddress).getAddress(), this.name, this.index);
            }
        }
        return getMacAddr0(null, this.name, this.index);
    }

    public int getMTU() throws SocketException {
        return getMTU0(this.name, this.index);
    }

    public boolean isVirtual() {
        return this.virtual;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NetworkInterface)) {
            return false;
        }
        NetworkInterface networkInterface = (NetworkInterface) obj;
        if (this.name != null) {
            if (!this.name.equals(networkInterface.name)) {
                return false;
            }
        } else if (networkInterface.name != null) {
            return false;
        }
        if (this.addrs == null) {
            return networkInterface.addrs == null;
        }
        if (networkInterface.addrs == null || this.addrs.length != networkInterface.addrs.length) {
            return false;
        }
        InetAddress[] inetAddressArr = networkInterface.addrs;
        int length = inetAddressArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            boolean z2 = false;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    break;
                }
                if (!this.addrs[i2].equals(inetAddressArr[i3])) {
                    i3++;
                } else {
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        if (this.name == null) {
            return 0;
        }
        return this.name.hashCode();
    }

    public String toString() {
        String str = "name:" + (this.name == null ? FXMLLoader.NULL_KEYWORD : this.name);
        if (this.displayName != null) {
            str = str + " (" + this.displayName + ")";
        }
        return str;
    }

    static NetworkInterface getDefault() {
        return defaultInterface;
    }
}
