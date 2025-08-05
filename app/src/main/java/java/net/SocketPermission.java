package java.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.net.PortConfig;
import sun.net.util.IPAddressUtil;
import sun.security.action.GetBooleanAction;
import sun.security.util.Debug;
import sun.security.util.RegisteredDomain;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/net/SocketPermission.class */
public final class SocketPermission extends Permission implements Serializable {
    private static final long serialVersionUID = -7204263841984476862L;
    private static final int CONNECT = 1;
    private static final int LISTEN = 2;
    private static final int ACCEPT = 4;
    private static final int RESOLVE = 8;
    private static final int NONE = 0;
    private static final int ALL = 15;
    private static final int PORT_MIN = 0;
    private static final int PORT_MAX = 65535;
    private static final int PRIV_PORT_MAX = 1023;
    private static final int DEF_EPH_LOW = 49152;
    private transient int mask;
    private String actions;
    private transient String hostname;
    private transient String cname;
    private transient InetAddress[] addresses;
    private transient boolean wildcard;
    private transient boolean init_with_ip;
    private transient boolean invalid;
    private transient int[] portrange;
    private transient boolean defaultDeny;
    private transient boolean untrusted;
    private transient boolean trusted;
    private transient String cdomain;
    private transient String hdomain;
    private static Debug debug = null;
    private static boolean debugInit = false;
    private static boolean trustNameService = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.net.trustNameService"))).booleanValue();

    /* loaded from: rt.jar:java/net/SocketPermission$EphemeralRange.class */
    private static class EphemeralRange {
        static final int low = SocketPermission.initEphemeralPorts("low", 49152);
        static final int high = SocketPermission.initEphemeralPorts("high", 65535);

        private EphemeralRange() {
        }
    }

    private static synchronized Debug getDebug() {
        if (!debugInit) {
            debug = Debug.getInstance("access");
            debugInit = true;
        }
        return debug;
    }

    public SocketPermission(String str, String str2) {
        super(getHost(str));
        this.defaultDeny = false;
        init(getName(), getMask(str2));
    }

    SocketPermission(String str, int i2) {
        super(getHost(str));
        this.defaultDeny = false;
        init(getName(), i2);
    }

    private void setDeny() {
        this.defaultDeny = true;
    }

    private static String getHost(String str) {
        if (str.equals("")) {
            return "localhost";
        }
        if (str.charAt(0) != '[' && str.indexOf(58) != str.lastIndexOf(58)) {
            int iCountTokens = new StringTokenizer(str, CallSiteDescriptor.TOKEN_DELIMITER).countTokens();
            if (iCountTokens == 9) {
                int iLastIndexOf = str.lastIndexOf(58);
                str = "[" + str.substring(0, iLastIndexOf) + "]" + str.substring(iLastIndexOf);
            } else if (iCountTokens == 8 && str.indexOf("::") == -1) {
                str = "[" + str + "]";
            } else {
                throw new IllegalArgumentException("Ambiguous hostport part");
            }
        }
        return str;
    }

    private int[] parsePort(String str) throws Exception {
        int i2;
        int i3;
        if (str == null || str.equals("") || str.equals("*")) {
            return new int[]{0, 65535};
        }
        int iIndexOf = str.indexOf(45);
        if (iIndexOf == -1) {
            int i4 = Integer.parseInt(str);
            return new int[]{i4, i4};
        }
        String strSubstring = str.substring(0, iIndexOf);
        String strSubstring2 = str.substring(iIndexOf + 1);
        if (strSubstring.equals("")) {
            i2 = 0;
        } else {
            i2 = Integer.parseInt(strSubstring);
        }
        if (strSubstring2.equals("")) {
            i3 = 65535;
        } else {
            i3 = Integer.parseInt(strSubstring2);
        }
        if (i2 < 0 || i3 < 0 || i3 < i2) {
            throw new IllegalArgumentException("invalid port range");
        }
        return new int[]{i2, i3};
    }

    private boolean includesEphemerals() {
        return this.portrange[0] == 0;
    }

    private void init(String str, int i2) {
        int iIndexOf;
        if ((i2 & 15) != i2) {
            throw new IllegalArgumentException("invalid actions mask");
        }
        this.mask = i2 | 8;
        if (str.charAt(0) == '[') {
            int iIndexOf2 = str.indexOf(93);
            if (iIndexOf2 != -1) {
                str = str.substring(1, iIndexOf2);
                iIndexOf = str.indexOf(58, iIndexOf2 + 1);
            } else {
                throw new IllegalArgumentException("invalid host/port: " + str);
            }
        } else {
            iIndexOf = str.indexOf(58, 0);
            if (iIndexOf != -1) {
                str = str.substring(0, iIndexOf);
            }
        }
        if (iIndexOf != -1) {
            String strSubstring = str.substring(iIndexOf + 1);
            try {
                this.portrange = parsePort(strSubstring);
            } catch (Exception e2) {
                throw new IllegalArgumentException("invalid port range: " + strSubstring);
            }
        } else {
            this.portrange = new int[]{0, 65535};
        }
        this.hostname = str;
        if (str.lastIndexOf(42) > 0) {
            throw new IllegalArgumentException("invalid host wildcard specification");
        }
        if (str.startsWith("*")) {
            this.wildcard = true;
            if (str.equals("*")) {
                this.cname = "";
                return;
            } else {
                if (str.startsWith("*.")) {
                    this.cname = str.substring(1).toLowerCase();
                    return;
                }
                throw new IllegalArgumentException("invalid host wildcard specification");
            }
        }
        if (str.length() > 0) {
            char cCharAt = str.charAt(0);
            if (cCharAt == ':' || IPAddressUtil.digit(cCharAt, 16) != -1) {
                byte[] bArrTextToNumericFormatV4 = IPAddressUtil.textToNumericFormatV4(str);
                if (bArrTextToNumericFormatV4 == null) {
                    bArrTextToNumericFormatV4 = IPAddressUtil.textToNumericFormatV6(str);
                }
                if (bArrTextToNumericFormatV4 != null) {
                    try {
                        this.addresses = new InetAddress[]{InetAddress.getByAddress(bArrTextToNumericFormatV4)};
                        this.init_with_ip = true;
                    } catch (UnknownHostException e3) {
                        this.invalid = true;
                    }
                }
            }
        }
    }

    private static int getMask(String str) {
        int i2;
        char c2;
        if (str == null) {
            throw new NullPointerException("action can't be null");
        }
        if (str.equals("")) {
            throw new IllegalArgumentException("action can't be empty");
        }
        int i3 = 0;
        if (str == SecurityConstants.SOCKET_RESOLVE_ACTION) {
            return 8;
        }
        if (str == SecurityConstants.SOCKET_CONNECT_ACTION) {
            return 1;
        }
        if (str == SecurityConstants.SOCKET_LISTEN_ACTION) {
            return 2;
        }
        if (str == SecurityConstants.SOCKET_ACCEPT_ACTION) {
            return 4;
        }
        if (str == SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION) {
            return 5;
        }
        char[] charArray = str.toCharArray();
        int length = charArray.length - 1;
        if (length < 0) {
            return 0;
        }
        while (length != -1) {
            while (length != -1 && ((c2 = charArray[length]) == ' ' || c2 == '\r' || c2 == '\n' || c2 == '\f' || c2 == '\t')) {
                length--;
            }
            if (length >= 6 && ((charArray[length - 6] == 'c' || charArray[length - 6] == 'C') && ((charArray[length - 5] == 'o' || charArray[length - 5] == 'O') && ((charArray[length - 4] == 'n' || charArray[length - 4] == 'N') && ((charArray[length - 3] == 'n' || charArray[length - 3] == 'N') && ((charArray[length - 2] == 'e' || charArray[length - 2] == 'E') && ((charArray[length - 1] == 'c' || charArray[length - 1] == 'C') && (charArray[length] == 't' || charArray[length] == 'T')))))))) {
                i2 = 7;
                i3 |= 1;
            } else if (length >= 6 && ((charArray[length - 6] == 'r' || charArray[length - 6] == 'R') && ((charArray[length - 5] == 'e' || charArray[length - 5] == 'E') && ((charArray[length - 4] == 's' || charArray[length - 4] == 'S') && ((charArray[length - 3] == 'o' || charArray[length - 3] == 'O') && ((charArray[length - 2] == 'l' || charArray[length - 2] == 'L') && ((charArray[length - 1] == 'v' || charArray[length - 1] == 'V') && (charArray[length] == 'e' || charArray[length] == 'E')))))))) {
                i2 = 7;
                i3 |= 8;
            } else if (length >= 5 && ((charArray[length - 5] == 'l' || charArray[length - 5] == 'L') && ((charArray[length - 4] == 'i' || charArray[length - 4] == 'I') && ((charArray[length - 3] == 's' || charArray[length - 3] == 'S') && ((charArray[length - 2] == 't' || charArray[length - 2] == 'T') && ((charArray[length - 1] == 'e' || charArray[length - 1] == 'E') && (charArray[length] == 'n' || charArray[length] == 'N'))))))) {
                i2 = 6;
                i3 |= 2;
            } else if (length >= 5 && ((charArray[length - 5] == 'a' || charArray[length - 5] == 'A') && ((charArray[length - 4] == 'c' || charArray[length - 4] == 'C') && ((charArray[length - 3] == 'c' || charArray[length - 3] == 'C') && ((charArray[length - 2] == 'e' || charArray[length - 2] == 'E') && ((charArray[length - 1] == 'p' || charArray[length - 1] == 'P') && (charArray[length] == 't' || charArray[length] == 'T'))))))) {
                i2 = 6;
                i3 |= 4;
            } else {
                throw new IllegalArgumentException("invalid permission: " + str);
            }
            boolean z2 = false;
            while (length >= i2 && !z2) {
                switch (charArray[length - i2]) {
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                        break;
                    case ',':
                        z2 = true;
                        break;
                    default:
                        throw new IllegalArgumentException("invalid permission: " + str);
                }
                length--;
            }
            length -= i2;
        }
        return i3;
    }

    private boolean isUntrusted() throws UnknownHostException {
        if (this.trusted) {
            return false;
        }
        if (this.invalid || this.untrusted) {
            return true;
        }
        try {
            if (!trustNameService && (this.defaultDeny || sun.net.www.URLConnection.isProxiedHost(this.hostname))) {
                if (this.cname == null) {
                    getCanonName();
                }
                if (!match(this.cname, this.hostname) && !authorized(this.hostname, this.addresses[0].getAddress())) {
                    this.untrusted = true;
                    Debug debug2 = getDebug();
                    if (debug2 != null && Debug.isOn("failure")) {
                        debug2.println("socket access restriction: proxied host (" + ((Object) this.addresses[0]) + ") does not match " + this.cname + " from reverse lookup");
                        return true;
                    }
                    return true;
                }
                this.trusted = true;
            }
            return false;
        } catch (UnknownHostException e2) {
            this.invalid = true;
            throw e2;
        }
    }

    void getCanonName() throws UnknownHostException {
        if (this.cname != null || this.invalid || this.untrusted) {
            return;
        }
        try {
            if (this.addresses == null) {
                getIP();
            }
            if (this.init_with_ip) {
                this.cname = this.addresses[0].getHostName(false).toLowerCase();
            } else {
                this.cname = InetAddress.getByName(this.addresses[0].getHostAddress()).getHostName(false).toLowerCase();
            }
        } catch (UnknownHostException e2) {
            this.invalid = true;
            throw e2;
        }
    }

    private boolean match(String str, String str2) {
        String lowerCase = str.toLowerCase();
        String lowerCase2 = str2.toLowerCase();
        if (lowerCase.startsWith(lowerCase2) && (lowerCase.length() == lowerCase2.length() || lowerCase.charAt(lowerCase2.length()) == '.')) {
            return true;
        }
        if (this.cdomain == null) {
            this.cdomain = (String) RegisteredDomain.from(lowerCase).map((v0) -> {
                return v0.name();
            }).orElse(lowerCase);
        }
        if (this.hdomain == null) {
            this.hdomain = (String) RegisteredDomain.from(lowerCase2).map((v0) -> {
                return v0.name();
            }).orElse(lowerCase2);
        }
        return (this.cdomain.length() == 0 || this.hdomain.length() == 0 || !this.cdomain.equals(this.hdomain)) ? false : true;
    }

    private boolean authorized(String str, byte[] bArr) {
        if (bArr.length == 4) {
            return authorizedIPv4(str, bArr);
        }
        if (bArr.length == 16) {
            return authorizedIPv6(str, bArr);
        }
        return false;
    }

    private boolean authorizedIPv4(String str, byte[] bArr) {
        String str2 = "";
        try {
            str2 = this.hostname + '.' + ("auth." + (bArr[3] & 255) + "." + (bArr[2] & 255) + "." + (bArr[1] & 255) + "." + (bArr[0] & 255) + ".in-addr.arpa");
            InetAddress inetAddress = InetAddress.getAllByName0(str2, false)[0];
            if (inetAddress.equals(InetAddress.getByAddress(bArr))) {
                return true;
            }
            Debug debug2 = getDebug();
            if (debug2 != null && Debug.isOn("failure")) {
                debug2.println("socket access restriction: IP address of " + ((Object) inetAddress) + " != " + ((Object) InetAddress.getByAddress(bArr)));
            }
            return false;
        } catch (UnknownHostException e2) {
            Debug debug3 = getDebug();
            if (debug3 != null && Debug.isOn("failure")) {
                debug3.println("socket access restriction: forward lookup failed for " + str2);
                return false;
            }
            return false;
        }
    }

    private boolean authorizedIPv6(String str, byte[] bArr) {
        String str2 = "";
        try {
            StringBuffer stringBuffer = new StringBuffer(39);
            for (int i2 = 15; i2 >= 0; i2--) {
                stringBuffer.append(Integer.toHexString(bArr[i2] & 15));
                stringBuffer.append('.');
                stringBuffer.append(Integer.toHexString((bArr[i2] >> 4) & 15));
                stringBuffer.append('.');
            }
            str2 = this.hostname + '.' + ("auth." + stringBuffer.toString() + "IP6.ARPA");
            InetAddress inetAddress = InetAddress.getAllByName0(str2, false)[0];
            if (inetAddress.equals(InetAddress.getByAddress(bArr))) {
                return true;
            }
            Debug debug2 = getDebug();
            if (debug2 != null && Debug.isOn("failure")) {
                debug2.println("socket access restriction: IP address of " + ((Object) inetAddress) + " != " + ((Object) InetAddress.getByAddress(bArr)));
            }
            return false;
        } catch (UnknownHostException e2) {
            Debug debug3 = getDebug();
            if (debug3 != null && Debug.isOn("failure")) {
                debug3.println("socket access restriction: forward lookup failed for " + str2);
                return false;
            }
            return false;
        }
    }

    void getIP() throws UnknownHostException {
        String strSubstring;
        if (this.addresses != null || this.wildcard || this.invalid) {
            return;
        }
        try {
            if (getName().charAt(0) == '[') {
                strSubstring = getName().substring(1, getName().indexOf(93));
            } else {
                int iIndexOf = getName().indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
                if (iIndexOf == -1) {
                    strSubstring = getName();
                } else {
                    strSubstring = getName().substring(0, iIndexOf);
                }
            }
            this.addresses = new InetAddress[]{InetAddress.getAllByName0(strSubstring, false)[0]};
        } catch (IndexOutOfBoundsException e2) {
            this.invalid = true;
            throw new UnknownHostException(getName());
        } catch (UnknownHostException e3) {
            this.invalid = true;
            throw e3;
        }
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof SocketPermission)) {
            return false;
        }
        if (permission == this) {
            return true;
        }
        SocketPermission socketPermission = (SocketPermission) permission;
        return (this.mask & socketPermission.mask) == socketPermission.mask && impliesIgnoreMask(socketPermission);
    }

    boolean impliesIgnoreMask(SocketPermission socketPermission) {
        if ((socketPermission.mask & 8) != socketPermission.mask && ((socketPermission.portrange[0] < this.portrange[0] || socketPermission.portrange[1] > this.portrange[1]) && ((!includesEphemerals() && !socketPermission.includesEphemerals()) || !inRange(this.portrange[0], this.portrange[1], socketPermission.portrange[0], socketPermission.portrange[1])))) {
            return false;
        }
        if (this.wildcard && "".equals(this.cname)) {
            return true;
        }
        if (this.invalid || socketPermission.invalid) {
            return compareHostnames(socketPermission);
        }
        try {
            if (this.init_with_ip) {
                if (socketPermission.wildcard) {
                    return false;
                }
                if (socketPermission.init_with_ip) {
                    return this.addresses[0].equals(socketPermission.addresses[0]);
                }
                if (socketPermission.addresses == null) {
                    socketPermission.getIP();
                }
                for (int i2 = 0; i2 < socketPermission.addresses.length; i2++) {
                    if (this.addresses[0].equals(socketPermission.addresses[i2])) {
                        return true;
                    }
                }
                return false;
            }
            if (this.wildcard || socketPermission.wildcard) {
                if (this.wildcard && socketPermission.wildcard) {
                    return socketPermission.cname.endsWith(this.cname);
                }
                if (socketPermission.wildcard) {
                    return false;
                }
                if (socketPermission.cname == null) {
                    socketPermission.getCanonName();
                }
                return socketPermission.cname.endsWith(this.cname);
            }
            if (this.addresses == null) {
                getIP();
            }
            if (socketPermission.addresses == null) {
                socketPermission.getIP();
            }
            if (!socketPermission.init_with_ip || !isUntrusted()) {
                for (int i3 = 0; i3 < this.addresses.length; i3++) {
                    for (int i4 = 0; i4 < socketPermission.addresses.length; i4++) {
                        if (this.addresses[i3].equals(socketPermission.addresses[i4])) {
                            return true;
                        }
                    }
                }
                if (this.cname == null) {
                    getCanonName();
                }
                if (socketPermission.cname == null) {
                    socketPermission.getCanonName();
                }
                return this.cname.equalsIgnoreCase(socketPermission.cname);
            }
            return false;
        } catch (UnknownHostException e2) {
            return compareHostnames(socketPermission);
        }
    }

    private boolean compareHostnames(SocketPermission socketPermission) {
        String str = this.hostname;
        String str2 = socketPermission.hostname;
        if (str == null) {
            return false;
        }
        if (this.wildcard) {
            int length = this.cname.length();
            return str2.regionMatches(true, str2.length() - length, this.cname, 0, length);
        }
        return str.equalsIgnoreCase(str2);
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SocketPermission)) {
            return false;
        }
        SocketPermission socketPermission = (SocketPermission) obj;
        if (this.mask != socketPermission.mask) {
            return false;
        }
        if ((socketPermission.mask & 8) != socketPermission.mask && (this.portrange[0] != socketPermission.portrange[0] || this.portrange[1] != socketPermission.portrange[1])) {
            return false;
        }
        if (getName().equalsIgnoreCase(socketPermission.getName())) {
            return true;
        }
        try {
            getCanonName();
            socketPermission.getCanonName();
            if (!this.invalid && !socketPermission.invalid && this.cname != null) {
                return this.cname.equalsIgnoreCase(socketPermission.cname);
            }
            return false;
        } catch (UnknownHostException e2) {
            return false;
        }
    }

    @Override // java.security.Permission
    public int hashCode() {
        if (this.init_with_ip || this.wildcard) {
            return getName().hashCode();
        }
        try {
            getCanonName();
        } catch (UnknownHostException e2) {
        }
        if (this.invalid || this.cname == null) {
            return getName().hashCode();
        }
        return this.cname.hashCode();
    }

    int getMask() {
        return this.mask;
    }

    private static String getActions(int i2) {
        StringBuilder sb = new StringBuilder();
        boolean z2 = false;
        if ((i2 & 1) == 1) {
            z2 = true;
            sb.append(SecurityConstants.SOCKET_CONNECT_ACTION);
        }
        if ((i2 & 2) == 2) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append(SecurityConstants.SOCKET_LISTEN_ACTION);
        }
        if ((i2 & 4) == 4) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append(SecurityConstants.SOCKET_ACCEPT_ACTION);
        }
        if ((i2 & 8) == 8) {
            if (z2) {
                sb.append(',');
            }
            sb.append(SecurityConstants.SOCKET_RESOLVE_ACTION);
        }
        return sb.toString();
    }

    @Override // java.security.Permission
    public String getActions() {
        if (this.actions == null) {
            this.actions = getActions(this.mask);
        }
        return this.actions;
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new SocketPermissionCollection();
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.actions == null) {
            getActions();
        }
        objectOutputStream.defaultWriteObject();
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(getName(), getMask(this.actions));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int initEphemeralPorts(final String str, int i2) {
        return ((Integer) AccessController.doPrivileged(new PrivilegedAction<Integer>() { // from class: java.net.SocketPermission.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Integer run2() {
                int iIntValue = Integer.getInteger("jdk.net.ephemeralPortRange." + str, -1).intValue();
                if (iIntValue != -1) {
                    return Integer.valueOf(iIntValue);
                }
                return Integer.valueOf(str.equals("low") ? PortConfig.getLower() : PortConfig.getUpper());
            }
        })).intValue();
    }

    private static boolean inRange(int i2, int i3, int i4, int i5) {
        int i6 = EphemeralRange.low;
        int i7 = EphemeralRange.high;
        if (i4 == 0) {
            if (!inRange(i2, i3, i6, i7)) {
                return false;
            }
            if (i5 == 0) {
                return true;
            }
            i4 = 1;
        }
        return (i2 == 0 && i3 == 0) ? i4 >= i6 && i5 <= i7 : i2 != 0 ? i4 >= i2 && i5 <= i3 : i3 >= i6 - 1 ? i5 <= i7 : (i4 <= i3 && i5 <= i3) || (i4 >= i6 && i5 <= i7);
    }
}
