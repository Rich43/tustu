package java.net;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.util.Arrays;
import java.util.Enumeration;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.misc.Unsafe;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/net/Inet6Address.class */
public final class Inet6Address extends InetAddress {
    static final int INADDRSZ = 16;
    private transient int cached_scope_id;
    private final transient Inet6AddressHolder holder6;
    private static final long serialVersionUID = 6880410070516793377L;
    private static final ObjectStreamField[] serialPersistentFields;
    private static final long FIELDS_OFFSET;
    private static final Unsafe UNSAFE;
    private static final int INT16SZ = 2;

    private static native void init();

    /* loaded from: rt.jar:java/net/Inet6Address$Inet6AddressHolder.class */
    private class Inet6AddressHolder {
        byte[] ipaddress;
        int scope_id;
        boolean scope_id_set;
        NetworkInterface scope_ifname;
        boolean scope_ifname_set;

        private Inet6AddressHolder() {
            this.ipaddress = new byte[16];
        }

        private Inet6AddressHolder(byte[] bArr, int i2, boolean z2, NetworkInterface networkInterface, boolean z3) {
            this.ipaddress = bArr;
            this.scope_id = i2;
            this.scope_id_set = z2;
            this.scope_ifname_set = z3;
            this.scope_ifname = networkInterface;
        }

        void setAddr(byte[] bArr) {
            if (bArr.length == 16) {
                System.arraycopy(bArr, 0, this.ipaddress, 0, 16);
            }
        }

        void init(byte[] bArr, int i2) {
            setAddr(bArr);
            if (i2 >= 0) {
                this.scope_id = i2;
                this.scope_id_set = true;
            }
        }

        void init(byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
            setAddr(bArr);
            if (networkInterface != null) {
                this.scope_id = Inet6Address.deriveNumericScope(this.ipaddress, networkInterface);
                this.scope_id_set = true;
                this.scope_ifname = networkInterface;
                this.scope_ifname_set = true;
            }
        }

        String getHostAddress() {
            String strNumericToTextFormat = Inet6Address.numericToTextFormat(this.ipaddress);
            if (this.scope_ifname != null) {
                strNumericToTextFormat = strNumericToTextFormat + FXMLLoader.RESOURCE_KEY_PREFIX + this.scope_ifname.getName();
            } else if (this.scope_id_set) {
                strNumericToTextFormat = strNumericToTextFormat + FXMLLoader.RESOURCE_KEY_PREFIX + this.scope_id;
            }
            return strNumericToTextFormat;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Inet6AddressHolder)) {
                return false;
            }
            return Arrays.equals(this.ipaddress, ((Inet6AddressHolder) obj).ipaddress);
        }

        public int hashCode() {
            if (this.ipaddress != null) {
                int i2 = 0;
                int i3 = 0;
                while (i3 < 16) {
                    int i4 = 0;
                    int i5 = 0;
                    while (i4 < 4 && i3 < 16) {
                        i5 = (i5 << 8) + this.ipaddress[i3];
                        i4++;
                        i3++;
                    }
                    i2 += i5;
                }
                return i2;
            }
            return 0;
        }

        boolean isIPv4CompatibleAddress() {
            if (this.ipaddress[0] == 0 && this.ipaddress[1] == 0 && this.ipaddress[2] == 0 && this.ipaddress[3] == 0 && this.ipaddress[4] == 0 && this.ipaddress[5] == 0 && this.ipaddress[6] == 0 && this.ipaddress[7] == 0 && this.ipaddress[8] == 0 && this.ipaddress[9] == 0 && this.ipaddress[10] == 0 && this.ipaddress[11] == 0) {
                return true;
            }
            return false;
        }

        boolean isMulticastAddress() {
            return (this.ipaddress[0] & 255) == 255;
        }

        boolean isAnyLocalAddress() {
            byte b2 = 0;
            for (int i2 = 0; i2 < 16; i2++) {
                b2 = (byte) (b2 | this.ipaddress[i2]);
            }
            return b2 == 0;
        }

        boolean isLoopbackAddress() {
            byte b2 = 0;
            for (int i2 = 0; i2 < 15; i2++) {
                b2 = (byte) (b2 | this.ipaddress[i2]);
            }
            return b2 == 0 && this.ipaddress[15] == 1;
        }

        boolean isLinkLocalAddress() {
            return (this.ipaddress[0] & 255) == 254 && (this.ipaddress[1] & 192) == 128;
        }

        boolean isSiteLocalAddress() {
            return (this.ipaddress[0] & 255) == 254 && (this.ipaddress[1] & 192) == 192;
        }

        boolean isMCGlobal() {
            return (this.ipaddress[0] & 255) == 255 && (this.ipaddress[1] & 15) == 14;
        }

        boolean isMCNodeLocal() {
            return (this.ipaddress[0] & 255) == 255 && (this.ipaddress[1] & 15) == 1;
        }

        boolean isMCLinkLocal() {
            return (this.ipaddress[0] & 255) == 255 && (this.ipaddress[1] & 15) == 2;
        }

        boolean isMCSiteLocal() {
            return (this.ipaddress[0] & 255) == 255 && (this.ipaddress[1] & 15) == 5;
        }

        boolean isMCOrgLocal() {
            return (this.ipaddress[0] & 255) == 255 && (this.ipaddress[1] & 15) == 8;
        }
    }

    static {
        init();
        serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("ipaddress", byte[].class), new ObjectStreamField("scope_id", Integer.TYPE), new ObjectStreamField("scope_id_set", Boolean.TYPE), new ObjectStreamField("scope_ifname_set", Boolean.TYPE), new ObjectStreamField("ifname", String.class)};
        try {
            Unsafe unsafe = Unsafe.getUnsafe();
            FIELDS_OFFSET = unsafe.objectFieldOffset(Inet6Address.class.getDeclaredField("holder6"));
            UNSAFE = unsafe;
        } catch (ReflectiveOperationException e2) {
            throw new Error(e2);
        }
    }

    Inet6Address() {
        this.holder.init(null, 2);
        this.holder6 = new Inet6AddressHolder();
    }

    Inet6Address(String str, byte[] bArr, int i2) {
        this.holder.init(str, 2);
        this.holder6 = new Inet6AddressHolder();
        this.holder6.init(bArr, i2);
    }

    Inet6Address(String str, byte[] bArr) {
        this.holder6 = new Inet6AddressHolder();
        try {
            initif(str, bArr, null);
        } catch (UnknownHostException e2) {
        }
    }

    Inet6Address(String str, byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
        this.holder6 = new Inet6AddressHolder();
        initif(str, bArr, networkInterface);
    }

    Inet6Address(String str, byte[] bArr, String str2) throws UnknownHostException {
        this.holder6 = new Inet6AddressHolder();
        initstr(str, bArr, str2);
    }

    public static Inet6Address getByAddress(String str, byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
        if (str != null && str.length() > 0 && str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
            str = str.substring(1, str.length() - 1);
        }
        if (bArr != null && bArr.length == 16) {
            return new Inet6Address(str, bArr, networkInterface);
        }
        throw new UnknownHostException("addr is of illegal length");
    }

    public static Inet6Address getByAddress(String str, byte[] bArr, int i2) throws UnknownHostException {
        if (str != null && str.length() > 0 && str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
            str = str.substring(1, str.length() - 1);
        }
        if (bArr != null && bArr.length == 16) {
            return new Inet6Address(str, bArr, i2);
        }
        throw new UnknownHostException("addr is of illegal length");
    }

    private void initstr(String str, byte[] bArr, String str2) throws UnknownHostException {
        try {
            NetworkInterface byName = NetworkInterface.getByName(str2);
            if (byName == null) {
                throw new UnknownHostException("no such interface " + str2);
            }
            initif(str, bArr, byName);
        } catch (SocketException e2) {
            throw new UnknownHostException("SocketException thrown" + str2);
        }
    }

    private void initif(String str, byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
        int i2 = -1;
        this.holder6.init(bArr, networkInterface);
        if (bArr.length == 16) {
            i2 = 2;
        }
        this.holder.init(str, i2);
    }

    private static boolean isDifferentLocalAddressType(byte[] bArr, byte[] bArr2) {
        if (isLinkLocalAddress(bArr) && !isLinkLocalAddress(bArr2)) {
            return false;
        }
        if (isSiteLocalAddress(bArr) && !isSiteLocalAddress(bArr2)) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int deriveNumericScope(byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddressNextElement = inetAddresses.nextElement2();
            if (inetAddressNextElement instanceof Inet6Address) {
                Inet6Address inet6Address = (Inet6Address) inetAddressNextElement;
                if (isDifferentLocalAddressType(bArr, inet6Address.getAddress())) {
                    return inet6Address.getScopeId();
                }
            }
        }
        throw new UnknownHostException("no scope_id found");
    }

    private int deriveNumericScope(String str) throws UnknownHostException {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterfaceNextElement = networkInterfaces.nextElement2();
                if (networkInterfaceNextElement.getName().equals(str)) {
                    return deriveNumericScope(this.holder6.ipaddress, networkInterfaceNextElement);
                }
            }
            throw new UnknownHostException("No matching address found for interface : " + str);
        } catch (SocketException e2) {
            throw new UnknownHostException("could not enumerate local network interfaces");
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        NetworkInterface byName = null;
        if (getClass().getClassLoader() != null) {
            throw new SecurityException("invalid address type");
        }
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        byte[] bArr = (byte[]) fields.get("ipaddress", (Object) null);
        int iDeriveNumericScope = fields.get("scope_id", -1);
        boolean z2 = fields.get("scope_id_set", false);
        boolean z3 = fields.get("scope_ifname_set", false);
        String str = (String) fields.get("ifname", (Object) null);
        if (str != null && !"".equals(str)) {
            try {
                byName = NetworkInterface.getByName(str);
                if (byName == null) {
                    z2 = false;
                    z3 = false;
                    iDeriveNumericScope = 0;
                } else {
                    z3 = true;
                    try {
                        iDeriveNumericScope = deriveNumericScope(bArr, byName);
                    } catch (UnknownHostException e2) {
                    }
                }
            } catch (SocketException e3) {
            }
        }
        byte[] bArr2 = (byte[]) bArr.clone();
        if (bArr2.length != 16) {
            throw new InvalidObjectException("invalid address length: " + bArr2.length);
        }
        if (this.holder.getFamily() != 2) {
            throw new InvalidObjectException("invalid address family type");
        }
        UNSAFE.putObject(this, FIELDS_OFFSET, new Inet6AddressHolder(bArr2, iDeriveNumericScope, z2, byName, z3));
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        String name = null;
        if (this.holder6.scope_ifname != null) {
            name = this.holder6.scope_ifname.getName();
            this.holder6.scope_ifname_set = true;
        }
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("ipaddress", this.holder6.ipaddress);
        putFieldPutFields.put("scope_id", this.holder6.scope_id);
        putFieldPutFields.put("scope_id_set", this.holder6.scope_id_set);
        putFieldPutFields.put("scope_ifname_set", this.holder6.scope_ifname_set);
        putFieldPutFields.put("ifname", name);
        objectOutputStream.writeFields();
    }

    @Override // java.net.InetAddress
    public boolean isMulticastAddress() {
        return this.holder6.isMulticastAddress();
    }

    @Override // java.net.InetAddress
    public boolean isAnyLocalAddress() {
        return this.holder6.isAnyLocalAddress();
    }

    @Override // java.net.InetAddress
    public boolean isLoopbackAddress() {
        return this.holder6.isLoopbackAddress();
    }

    @Override // java.net.InetAddress
    public boolean isLinkLocalAddress() {
        return this.holder6.isLinkLocalAddress();
    }

    static boolean isLinkLocalAddress(byte[] bArr) {
        return (bArr[0] & 255) == 254 && (bArr[1] & 192) == 128;
    }

    @Override // java.net.InetAddress
    public boolean isSiteLocalAddress() {
        return this.holder6.isSiteLocalAddress();
    }

    static boolean isSiteLocalAddress(byte[] bArr) {
        return (bArr[0] & 255) == 254 && (bArr[1] & 192) == 192;
    }

    @Override // java.net.InetAddress
    public boolean isMCGlobal() {
        return this.holder6.isMCGlobal();
    }

    @Override // java.net.InetAddress
    public boolean isMCNodeLocal() {
        return this.holder6.isMCNodeLocal();
    }

    @Override // java.net.InetAddress
    public boolean isMCLinkLocal() {
        return this.holder6.isMCLinkLocal();
    }

    @Override // java.net.InetAddress
    public boolean isMCSiteLocal() {
        return this.holder6.isMCSiteLocal();
    }

    @Override // java.net.InetAddress
    public boolean isMCOrgLocal() {
        return this.holder6.isMCOrgLocal();
    }

    @Override // java.net.InetAddress
    public byte[] getAddress() {
        return (byte[]) this.holder6.ipaddress.clone();
    }

    public int getScopeId() {
        return this.holder6.scope_id;
    }

    public NetworkInterface getScopedInterface() {
        return this.holder6.scope_ifname;
    }

    @Override // java.net.InetAddress
    public String getHostAddress() {
        return this.holder6.getHostAddress();
    }

    @Override // java.net.InetAddress
    public int hashCode() {
        return this.holder6.hashCode();
    }

    @Override // java.net.InetAddress
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Inet6Address)) {
            return false;
        }
        return this.holder6.equals(((Inet6Address) obj).holder6);
    }

    public boolean isIPv4CompatibleAddress() {
        return this.holder6.isIPv4CompatibleAddress();
    }

    static String numericToTextFormat(byte[] bArr) {
        StringBuilder sb = new StringBuilder(39);
        for (int i2 = 0; i2 < 8; i2++) {
            sb.append(Integer.toHexString(((bArr[i2 << 1] << 8) & NormalizerImpl.CC_MASK) | (bArr[(i2 << 1) + 1] & 255)));
            if (i2 < 7) {
                sb.append(CallSiteDescriptor.TOKEN_DELIMITER);
            }
        }
        return sb.toString();
    }
}
