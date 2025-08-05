package java.net;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.misc.Unsafe;
import sun.net.InetAddressCachePolicy;
import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;
import sun.net.util.IPAddressUtil;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/net/InetAddress.class */
public class InetAddress implements Serializable {
    static final int IPv4 = 1;
    static final int IPv6 = 2;
    static transient boolean preferIPv6Address;
    private static List<NameService> nameServices;
    private static final long serialVersionUID = 3286316764910316507L;
    private static final ConcurrentMap<String, Addresses> cache;
    private static final NavigableSet<CachedAddresses> expirySet;
    static InetAddressImpl impl;
    private static volatile CachedLocalHost cachedLocalHost;
    private static final long FIELDS_OFFSET;
    private static final Unsafe UNSAFE;
    private static final ObjectStreamField[] serialPersistentFields;
    private transient String canonicalHostName = null;
    final transient InetAddressHolder holder = new InetAddressHolder();

    /* loaded from: rt.jar:java/net/InetAddress$Addresses.class */
    private interface Addresses {
        InetAddress[] get() throws UnknownHostException;
    }

    private static native void init();

    static {
        preferIPv6Address = false;
        nameServices = null;
        preferIPv6Address = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("java.net.preferIPv6Addresses"))).booleanValue();
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.net.InetAddress.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("net");
                return null;
            }
        });
        init();
        cache = new ConcurrentHashMap();
        expirySet = new ConcurrentSkipListSet();
        impl = InetAddressImplFactory.create();
        int i2 = 1;
        nameServices = new ArrayList();
        Object objDoPrivileged = AccessController.doPrivileged(new GetPropertyAction("sun.net.spi.nameservice.provider.1"));
        while (true) {
            String str = (String) objDoPrivileged;
            if (str == null) {
                break;
            }
            NameService nameServiceCreateNSProvider = createNSProvider(str);
            if (nameServiceCreateNSProvider != null) {
                nameServices.add(nameServiceCreateNSProvider);
            }
            i2++;
            objDoPrivileged = AccessController.doPrivileged(new GetPropertyAction("sun.net.spi.nameservice.provider." + i2));
        }
        if (nameServices.size() == 0) {
            nameServices.add(createNSProvider("default"));
        }
        try {
            Unsafe unsafe = Unsafe.getUnsafe();
            FIELDS_OFFSET = unsafe.objectFieldOffset(InetAddress.class.getDeclaredField("holder"));
            UNSAFE = unsafe;
            serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("hostName", String.class), new ObjectStreamField("address", Integer.TYPE), new ObjectStreamField("family", Integer.TYPE)};
        } catch (ReflectiveOperationException e2) {
            throw new Error(e2);
        }
    }

    /* loaded from: rt.jar:java/net/InetAddress$InetAddressHolder.class */
    static class InetAddressHolder {
        String originalHostName;
        String hostName;
        int address;
        int family;

        InetAddressHolder() {
        }

        InetAddressHolder(String str, int i2, int i3) {
            this.originalHostName = str;
            this.hostName = str;
            this.address = i2;
            this.family = i3;
        }

        void init(String str, int i2) {
            this.originalHostName = str;
            this.hostName = str;
            if (i2 != -1) {
                this.family = i2;
            }
        }

        String getHostName() {
            return this.hostName;
        }

        String getOriginalHostName() {
            return this.originalHostName;
        }

        int getAddress() {
            return this.address;
        }

        int getFamily() {
            return this.family;
        }
    }

    InetAddressHolder holder() {
        return this.holder;
    }

    InetAddress() {
    }

    private Object readResolve() throws ObjectStreamException {
        return new Inet4Address(holder().getHostName(), holder().getAddress());
    }

    public boolean isMulticastAddress() {
        return false;
    }

    public boolean isAnyLocalAddress() {
        return false;
    }

    public boolean isLoopbackAddress() {
        return false;
    }

    public boolean isLinkLocalAddress() {
        return false;
    }

    public boolean isSiteLocalAddress() {
        return false;
    }

    public boolean isMCGlobal() {
        return false;
    }

    public boolean isMCNodeLocal() {
        return false;
    }

    public boolean isMCLinkLocal() {
        return false;
    }

    public boolean isMCSiteLocal() {
        return false;
    }

    public boolean isMCOrgLocal() {
        return false;
    }

    public boolean isReachable(int i2) throws IOException {
        return isReachable(null, 0, i2);
    }

    public boolean isReachable(NetworkInterface networkInterface, int i2, int i3) throws IOException {
        if (i2 < 0) {
            throw new IllegalArgumentException("ttl can't be negative");
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("timeout can't be negative");
        }
        return impl.isReachable(this, i3, networkInterface, i2);
    }

    public String getHostName() {
        return getHostName(true);
    }

    String getHostName(boolean z2) {
        if (holder().getHostName() == null) {
            holder().hostName = getHostFromNameService(this, z2);
        }
        return holder().getHostName();
    }

    public String getCanonicalHostName() {
        if (this.canonicalHostName == null) {
            this.canonicalHostName = getHostFromNameService(this, true);
        }
        return this.canonicalHostName;
    }

    private static String getHostFromNameService(InetAddress inetAddress, boolean z2) {
        SecurityManager securityManager;
        String hostAddress = null;
        Iterator<NameService> it = nameServices.iterator();
        while (it.hasNext()) {
            try {
                hostAddress = it.next().getHostByAddr(inetAddress.getAddress());
                if (z2 && (securityManager = System.getSecurityManager()) != null) {
                    securityManager.checkConnect(hostAddress, -1);
                }
                InetAddress[] allByName0 = getAllByName0(hostAddress, z2);
                boolean zEquals = false;
                if (allByName0 != null) {
                    for (int i2 = 0; !zEquals && i2 < allByName0.length; i2++) {
                        zEquals = inetAddress.equals(allByName0[i2]);
                    }
                }
                if (zEquals) {
                    break;
                }
                return inetAddress.getHostAddress();
            } catch (SecurityException e2) {
                hostAddress = inetAddress.getHostAddress();
            } catch (UnknownHostException e3) {
                hostAddress = inetAddress.getHostAddress();
            }
        }
        return hostAddress;
    }

    public byte[] getAddress() {
        return null;
    }

    public String getHostAddress() {
        return null;
    }

    public int hashCode() {
        return -1;
    }

    public boolean equals(Object obj) {
        return false;
    }

    public String toString() {
        String hostName = holder().getHostName();
        return (hostName != null ? hostName : "") + "/" + getHostAddress();
    }

    /* loaded from: rt.jar:java/net/InetAddress$CachedAddresses.class */
    private static final class CachedAddresses implements Addresses, Comparable<CachedAddresses> {
        private static final AtomicLong seq = new AtomicLong();
        final String host;
        final InetAddress[] inetAddresses;
        final long expiryTime;
        final long id = seq.incrementAndGet();

        CachedAddresses(String str, InetAddress[] inetAddressArr, long j2) {
            this.host = str;
            this.inetAddresses = inetAddressArr;
            this.expiryTime = j2;
        }

        @Override // java.net.InetAddress.Addresses
        public InetAddress[] get() throws UnknownHostException {
            if (this.inetAddresses == null) {
                throw new UnknownHostException(this.host);
            }
            return this.inetAddresses;
        }

        @Override // java.lang.Comparable
        public int compareTo(CachedAddresses cachedAddresses) {
            long j2 = this.expiryTime - cachedAddresses.expiryTime;
            if (j2 < 0) {
                return -1;
            }
            if (j2 > 0) {
                return 1;
            }
            return Long.compare(this.id, cachedAddresses.id);
        }
    }

    /* loaded from: rt.jar:java/net/InetAddress$NameServiceAddresses.class */
    private static final class NameServiceAddresses implements Addresses {
        private final String host;
        private final InetAddress reqAddr;

        NameServiceAddresses(String str, InetAddress inetAddress) {
            this.host = str;
            this.reqAddr = inetAddress;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v5, types: [java.net.InetAddress$Addresses] */
        @Override // java.net.InetAddress.Addresses
        public InetAddress[] get() throws UnknownHostException {
            InetAddress[] addressesFromNameService;
            UnknownHostException unknownHostException;
            int negative;
            synchronized (this) {
                NameServiceAddresses nameServiceAddresses = (Addresses) InetAddress.cache.putIfAbsent(this.host, this);
                if (nameServiceAddresses == null) {
                    nameServiceAddresses = this;
                }
                if (nameServiceAddresses == this) {
                    try {
                        addressesFromNameService = InetAddress.getAddressesFromNameService(this.host, this.reqAddr);
                        unknownHostException = null;
                        negative = InetAddressCachePolicy.get();
                    } catch (UnknownHostException e2) {
                        addressesFromNameService = null;
                        unknownHostException = e2;
                        negative = InetAddressCachePolicy.getNegative();
                    }
                    if (negative == 0) {
                        InetAddress.cache.remove(this.host, this);
                    } else {
                        CachedAddresses cachedAddresses = new CachedAddresses(this.host, addressesFromNameService, negative == -1 ? 0L : System.nanoTime() + (NativeMediaPlayer.ONE_SECOND * negative));
                        if (InetAddress.cache.replace(this.host, this, cachedAddresses) && negative != -1) {
                            InetAddress.expirySet.add(cachedAddresses);
                        }
                    }
                    if (addressesFromNameService == null) {
                        if (unknownHostException == null) {
                            throw new UnknownHostException(this.host);
                        }
                        throw unknownHostException;
                    }
                    return addressesFromNameService;
                }
                return nameServiceAddresses.get();
            }
        }
    }

    private static NameService createNSProvider(final String str) {
        if (str == null) {
            return null;
        }
        NameService nameService = null;
        if (str.equals("default")) {
            nameService = new NameService() { // from class: java.net.InetAddress.2
                @Override // sun.net.spi.nameservice.NameService
                public InetAddress[] lookupAllHostAddr(String str2) throws UnknownHostException {
                    InetAddress.validate(str2);
                    return InetAddress.impl.lookupAllHostAddr(str2);
                }

                @Override // sun.net.spi.nameservice.NameService
                public String getHostByAddr(byte[] bArr) throws UnknownHostException {
                    return InetAddress.impl.getHostByAddr(bArr);
                }
            };
        } else {
            try {
                nameService = (NameService) AccessController.doPrivileged(new PrivilegedExceptionAction<NameService>() { // from class: java.net.InetAddress.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public NameService run() {
                        Iterator it = ServiceLoader.load(NameServiceDescriptor.class).iterator();
                        while (it.hasNext()) {
                            NameServiceDescriptor nameServiceDescriptor = (NameServiceDescriptor) it.next();
                            if (str.equalsIgnoreCase(nameServiceDescriptor.getType() + "," + nameServiceDescriptor.getProviderName())) {
                                try {
                                    return nameServiceDescriptor.createNameService();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                    System.err.println("Cannot create name service:" + str + ": " + ((Object) e2));
                                }
                            }
                        }
                        return null;
                    }
                });
            } catch (PrivilegedActionException e2) {
            }
        }
        return nameService;
    }

    public static InetAddress getByAddress(String str, byte[] bArr) throws UnknownHostException {
        if (str != null && str.length() > 0 && str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
            str = str.substring(1, str.length() - 1);
        }
        if (bArr != null) {
            if (bArr.length == 4) {
                return new Inet4Address(str, bArr);
            }
            if (bArr.length == 16) {
                byte[] bArrConvertFromIPv4MappedAddress = IPAddressUtil.convertFromIPv4MappedAddress(bArr);
                if (bArrConvertFromIPv4MappedAddress != null) {
                    return new Inet4Address(str, bArrConvertFromIPv4MappedAddress);
                }
                return new Inet6Address(str, bArr);
            }
        }
        throw new UnknownHostException("addr is of illegal length");
    }

    public static InetAddress getByName(String str) throws UnknownHostException {
        return getAllByName(str)[0];
    }

    private static InetAddress getByName(String str, InetAddress inetAddress) throws UnknownHostException {
        return getAllByName(str, inetAddress)[0];
    }

    public static InetAddress[] getAllByName(String str) throws UnknownHostException {
        return getAllByName(str, null);
    }

    private static InetAddress[] getAllByName(String str, InetAddress inetAddress) throws UnknownHostException {
        if (str == null || str.length() == 0) {
            return new InetAddress[]{impl.loopbackAddress()};
        }
        validate(str);
        boolean z2 = false;
        if (str.charAt(0) == '[') {
            if (str.length() > 2 && str.charAt(str.length() - 1) == ']') {
                str = str.substring(1, str.length() - 1);
                z2 = true;
            } else {
                throw invalidIPv6LiteralException(str, false);
            }
        }
        if (IPAddressUtil.digit(str.charAt(0), 16) != -1 || str.charAt(0) == ':') {
            byte[] bArrValidateNumericFormatV4 = null;
            int iCheckNumericZone = -1;
            String strSubstring = null;
            if (!z2) {
                try {
                    bArrValidateNumericFormatV4 = IPAddressUtil.validateNumericFormatV4(str);
                } catch (IllegalArgumentException e2) {
                    UnknownHostException unknownHostException = new UnknownHostException(str);
                    unknownHostException.initCause(e2);
                    throw unknownHostException;
                }
            }
            if (bArrValidateNumericFormatV4 == null) {
                int iIndexOf = str.indexOf(37);
                if (iIndexOf != -1) {
                    iCheckNumericZone = checkNumericZone(str);
                    if (iCheckNumericZone == -1) {
                        strSubstring = str.substring(iIndexOf + 1);
                    }
                }
                byte[] bArrTextToNumericFormatV6 = IPAddressUtil.textToNumericFormatV6(str);
                bArrValidateNumericFormatV4 = bArrTextToNumericFormatV6;
                if (bArrTextToNumericFormatV6 == null && (str.contains(CallSiteDescriptor.TOKEN_DELIMITER) || z2)) {
                    throw invalidIPv6LiteralException(str, z2);
                }
            }
            if (bArrValidateNumericFormatV4 != null) {
                InetAddress[] inetAddressArr = new InetAddress[1];
                if (bArrValidateNumericFormatV4.length == 4) {
                    if (iCheckNumericZone != -1 || strSubstring != null) {
                        throw new UnknownHostException(str + ": invalid IPv4-mapped address");
                    }
                    inetAddressArr[0] = new Inet4Address((String) null, bArrValidateNumericFormatV4);
                } else if (strSubstring != null) {
                    inetAddressArr[0] = new Inet6Address((String) null, bArrValidateNumericFormatV4, strSubstring);
                } else {
                    inetAddressArr[0] = new Inet6Address((String) null, bArrValidateNumericFormatV4, iCheckNumericZone);
                }
                return inetAddressArr;
            }
        } else if (z2) {
            throw invalidIPv6LiteralException(str, true);
        }
        return getAllByName0(str, inetAddress, true, true);
    }

    private static UnknownHostException invalidIPv6LiteralException(String str, boolean z2) {
        return new UnknownHostException((z2 ? "[" + str + "]" : str) + ": invalid IPv6 address literal");
    }

    public static InetAddress getLoopbackAddress() {
        return impl.loopbackAddress();
    }

    private static int checkNumericZone(String str) throws UnknownHostException {
        int iIndexOf = str.indexOf(37);
        int length = str.length();
        int i2 = 0;
        if (iIndexOf == -1) {
            return -1;
        }
        for (int i3 = iIndexOf + 1; i3 < length; i3++) {
            int asciiDigit = IPAddressUtil.parseAsciiDigit(str.charAt(i3), 10);
            if (asciiDigit < 0 || i2 > 214748364) {
                return -1;
            }
            i2 = (i2 * 10) + asciiDigit;
            if (i2 < 0) {
                return -1;
            }
        }
        return i2;
    }

    private static InetAddress[] getAllByName0(String str) throws UnknownHostException {
        return getAllByName0(str, true);
    }

    static InetAddress[] getAllByName0(String str, boolean z2) throws UnknownHostException {
        return getAllByName0(str, null, z2, true);
    }

    private static InetAddress[] getAllByName0(String str, InetAddress inetAddress, boolean z2, boolean z3) throws UnknownHostException {
        Addresses addressesRemove;
        SecurityManager securityManager;
        if (z2 && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkConnect(str, -1);
        }
        long jNanoTime = System.nanoTime();
        for (CachedAddresses cachedAddresses : expirySet) {
            if (cachedAddresses.expiryTime - jNanoTime >= 0) {
                break;
            }
            if (expirySet.remove(cachedAddresses)) {
                cache.remove(cachedAddresses.host, cachedAddresses);
            }
        }
        if (z3) {
            addressesRemove = cache.get(str);
        } else {
            addressesRemove = cache.remove(str);
            if (addressesRemove != null) {
                if (addressesRemove instanceof CachedAddresses) {
                    expirySet.remove(addressesRemove);
                }
                addressesRemove = null;
            }
        }
        if (addressesRemove == null) {
            ConcurrentMap<String, Addresses> concurrentMap = cache;
            NameServiceAddresses nameServiceAddresses = new NameServiceAddresses(str, inetAddress);
            addressesRemove = nameServiceAddresses;
            Addresses addressesPutIfAbsent = concurrentMap.putIfAbsent(str, nameServiceAddresses);
            if (addressesPutIfAbsent != null) {
                addressesRemove = addressesPutIfAbsent;
            }
        }
        return (InetAddress[]) addressesRemove.get().clone();
    }

    static InetAddress[] getAddressesFromNameService(String str, InetAddress inetAddress) throws UnknownHostException {
        InetAddress[] inetAddressArrLookupAllHostAddr = null;
        UnknownHostException unknownHostException = null;
        Iterator<NameService> it = nameServices.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            try {
                inetAddressArrLookupAllHostAddr = it.next().lookupAllHostAddr(str);
                break;
            } catch (UnknownHostException e2) {
                if (str.equalsIgnoreCase("localhost")) {
                    inetAddressArrLookupAllHostAddr = new InetAddress[]{impl.loopbackAddress()};
                    break;
                }
                unknownHostException = e2;
            }
        }
        if (inetAddressArrLookupAllHostAddr == null) {
            if (unknownHostException == null) {
                throw new UnknownHostException(str);
            }
            throw unknownHostException;
        }
        if (inetAddress != null && inetAddressArrLookupAllHostAddr.length > 1 && !inetAddressArrLookupAllHostAddr[0].equals(inetAddress)) {
            int i2 = 1;
            while (i2 < inetAddressArrLookupAllHostAddr.length && !inetAddressArrLookupAllHostAddr[i2].equals(inetAddress)) {
                i2++;
            }
            if (i2 < inetAddressArrLookupAllHostAddr.length) {
                InetAddress inetAddress2 = inetAddress;
                for (int i3 = 0; i3 < i2; i3++) {
                    InetAddress inetAddress3 = inetAddressArrLookupAllHostAddr[i3];
                    inetAddressArrLookupAllHostAddr[i3] = inetAddress2;
                    inetAddress2 = inetAddress3;
                }
                inetAddressArrLookupAllHostAddr[i2] = inetAddress2;
            }
        }
        return inetAddressArrLookupAllHostAddr;
    }

    public static InetAddress getByAddress(byte[] bArr) throws UnknownHostException {
        return getByAddress(null, bArr);
    }

    /* loaded from: rt.jar:java/net/InetAddress$CachedLocalHost.class */
    private static final class CachedLocalHost {
        final String host;
        final InetAddress addr;
        final long expiryTime = System.nanoTime() + 5000000000L;

        CachedLocalHost(String str, InetAddress inetAddress) {
            this.host = str;
            this.addr = inetAddress;
        }
    }

    public static InetAddress getLocalHost() throws UnknownHostException {
        InetAddress inetAddressLoopbackAddress;
        SecurityManager securityManager = System.getSecurityManager();
        try {
            CachedLocalHost cachedLocalHost2 = cachedLocalHost;
            if (cachedLocalHost2 != null && cachedLocalHost2.expiryTime - System.nanoTime() >= 0) {
                if (securityManager != null) {
                    securityManager.checkConnect(cachedLocalHost2.host, -1);
                }
                return cachedLocalHost2.addr;
            }
            String localHostName = impl.getLocalHostName();
            if (securityManager != null) {
                securityManager.checkConnect(localHostName, -1);
            }
            if (localHostName.equals("localhost")) {
                inetAddressLoopbackAddress = impl.loopbackAddress();
            } else {
                try {
                    inetAddressLoopbackAddress = getAllByName0(localHostName, null, false, false)[0];
                } catch (UnknownHostException e2) {
                    UnknownHostException unknownHostException = new UnknownHostException(localHostName + ": " + e2.getMessage());
                    unknownHostException.initCause(e2);
                    throw unknownHostException;
                }
            }
            cachedLocalHost = new CachedLocalHost(localHostName, inetAddressLoopbackAddress);
            return inetAddressLoopbackAddress;
        } catch (SecurityException e3) {
            return impl.loopbackAddress();
        }
    }

    static InetAddress anyLocalAddress() {
        return impl.anyLocalAddress();
    }

    static InetAddressImpl loadImpl(String str) {
        Object objNewInstance = null;
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("impl.prefix", ""));
        try {
            objNewInstance = Class.forName("java.net." + str2 + str).newInstance();
        } catch (ClassNotFoundException e2) {
            System.err.println("Class not found: java.net." + str2 + str + ":\ncheck impl.prefix property in your properties file.");
        } catch (IllegalAccessException e3) {
            System.err.println("Cannot access class: java.net." + str2 + str + ":\ncheck impl.prefix property in your properties file.");
        } catch (InstantiationException e4) {
            System.err.println("Could not instantiate: java.net." + str2 + str + ":\ncheck impl.prefix property in your properties file.");
        }
        if (objNewInstance == null) {
            try {
                objNewInstance = Class.forName(str).newInstance();
            } catch (Exception e5) {
                throw new Error("System property impl.prefix incorrect");
            }
        }
        return (InetAddressImpl) objNewInstance;
    }

    private void readObjectNoData(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (getClass().getClassLoader() != null) {
            throw new SecurityException("invalid address type");
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (getClass().getClassLoader() != null) {
            throw new SecurityException("invalid address type");
        }
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        String str = (String) fields.get("hostName", (Object) null);
        int i2 = fields.get("address", 0);
        int i3 = fields.get("family", 0);
        if (i3 != 1 && i3 != 2) {
            throw new InvalidObjectException("invalid address family type: " + i3);
        }
        UNSAFE.putObject(this, FIELDS_OFFSET, new InetAddressHolder(str, i2, i3));
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (getClass().getClassLoader() != null) {
            throw new SecurityException("invalid address type");
        }
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("hostName", holder().getHostName());
        putFieldPutFields.put("address", holder().getAddress());
        putFieldPutFields.put("family", holder().getFamily());
        objectOutputStream.writeFields();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validate(String str) throws UnknownHostException {
        if (str.indexOf(0) != -1) {
            throw new UnknownHostException("NUL character not allowed in hostname");
        }
    }
}
