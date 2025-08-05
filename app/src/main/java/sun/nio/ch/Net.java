package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.UnresolvedAddressException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import jdk.net.ExtendedSocketOptions;
import jdk.net.SocketFlow;
import sun.net.ExtendedOptionsImpl;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/nio/ch/Net.class */
public class Net {
    private static final boolean exclusiveBind;
    private static final boolean fastLoopback;
    private static volatile boolean isIPv6Available;
    public static final int SHUT_RD = 0;
    public static final int SHUT_WR = 1;
    public static final int SHUT_RDWR = 2;
    public static final short POLLIN;
    public static final short POLLOUT;
    public static final short POLLERR;
    public static final short POLLHUP;
    public static final short POLLNVAL;
    public static final short POLLCONN;
    static final ProtocolFamily UNSPEC = new ProtocolFamily() { // from class: sun.nio.ch.Net.1
        @Override // java.net.ProtocolFamily
        public String name() {
            return "UNSPEC";
        }
    };
    private static volatile boolean checkedIPv6 = false;

    private static native boolean isIPv6Available0();

    private static native int isExclusiveBindAvailable();

    private static native boolean canIPv6SocketJoinIPv4Group0();

    private static native boolean canJoin6WithIPv4Group0();

    private static native int socket0(boolean z2, boolean z3, boolean z4, boolean z5);

    private static native void bind0(FileDescriptor fileDescriptor, boolean z2, boolean z3, InetAddress inetAddress, int i2) throws IOException;

    static native void listen(FileDescriptor fileDescriptor, int i2) throws IOException;

    private static native int connect0(boolean z2, FileDescriptor fileDescriptor, InetAddress inetAddress, int i2) throws IOException;

    static native void shutdown(FileDescriptor fileDescriptor, int i2) throws IOException;

    private static native int localPort(FileDescriptor fileDescriptor) throws IOException;

    private static native InetAddress localInetAddress(FileDescriptor fileDescriptor) throws IOException;

    private static native int remotePort(FileDescriptor fileDescriptor) throws IOException;

    private static native InetAddress remoteInetAddress(FileDescriptor fileDescriptor) throws IOException;

    private static native int getIntOption0(FileDescriptor fileDescriptor, boolean z2, int i2, int i3) throws IOException;

    private static native void setIntOption0(FileDescriptor fileDescriptor, boolean z2, int i2, int i3, int i4, boolean z3) throws IOException;

    static native int poll(FileDescriptor fileDescriptor, int i2, long j2) throws IOException;

    private static native int joinOrDrop4(boolean z2, FileDescriptor fileDescriptor, int i2, int i3, int i4) throws IOException;

    private static native int blockOrUnblock4(boolean z2, FileDescriptor fileDescriptor, int i2, int i3, int i4) throws IOException;

    private static native int joinOrDrop6(boolean z2, FileDescriptor fileDescriptor, byte[] bArr, int i2, byte[] bArr2) throws IOException;

    static native int blockOrUnblock6(boolean z2, FileDescriptor fileDescriptor, byte[] bArr, int i2, byte[] bArr2) throws IOException;

    static native void setInterface4(FileDescriptor fileDescriptor, int i2) throws IOException;

    static native int getInterface4(FileDescriptor fileDescriptor) throws IOException;

    static native void setInterface6(FileDescriptor fileDescriptor, int i2) throws IOException;

    static native int getInterface6(FileDescriptor fileDescriptor) throws IOException;

    private static native void initIDs();

    static native short pollinValue();

    static native short polloutValue();

    static native short pollerrValue();

    static native short pollhupValue();

    static native short pollnvalValue();

    static native short pollconnValue();

    private Net() {
    }

    static {
        IOUtil.load();
        initIDs();
        POLLIN = pollinValue();
        POLLOUT = polloutValue();
        POLLERR = pollerrValue();
        POLLHUP = pollhupValue();
        POLLNVAL = pollnvalValue();
        POLLCONN = pollconnValue();
        int iIsExclusiveBindAvailable = isExclusiveBindAvailable();
        if (iIsExclusiveBindAvailable >= 0) {
            String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.nio.ch.Net.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public String run() {
                    return System.getProperty("sun.net.useExclusiveBind");
                }
            });
            if (str != null) {
                exclusiveBind = str.length() == 0 ? true : Boolean.parseBoolean(str);
            } else if (iIsExclusiveBindAvailable == 1) {
                exclusiveBind = true;
            } else {
                exclusiveBind = false;
            }
        } else {
            exclusiveBind = false;
        }
        fastLoopback = isFastTcpLoopbackRequested();
    }

    static boolean isIPv6Available() {
        if (!checkedIPv6) {
            isIPv6Available = isIPv6Available0();
            checkedIPv6 = true;
        }
        return isIPv6Available;
    }

    static boolean useExclusiveBind() {
        return exclusiveBind;
    }

    static boolean canIPv6SocketJoinIPv4Group() {
        return canIPv6SocketJoinIPv4Group0();
    }

    static boolean canJoin6WithIPv4Group() {
        return canJoin6WithIPv4Group0();
    }

    public static InetSocketAddress checkAddress(SocketAddress socketAddress) {
        if (socketAddress == null) {
            throw new NullPointerException();
        }
        if (!(socketAddress instanceof InetSocketAddress)) {
            throw new UnsupportedAddressTypeException();
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        if (inetSocketAddress.isUnresolved()) {
            throw new UnresolvedAddressException();
        }
        InetAddress address = inetSocketAddress.getAddress();
        if (!(address instanceof Inet4Address) && !(address instanceof Inet6Address)) {
            throw new IllegalArgumentException("Invalid address type");
        }
        return inetSocketAddress;
    }

    static InetSocketAddress asInetSocketAddress(SocketAddress socketAddress) {
        if (!(socketAddress instanceof InetSocketAddress)) {
            throw new UnsupportedAddressTypeException();
        }
        return (InetSocketAddress) socketAddress;
    }

    static void translateToSocketException(Exception exc) throws SocketException {
        if (exc instanceof SocketException) {
            throw ((SocketException) exc);
        }
        Exception socketException = exc;
        if (exc instanceof ClosedChannelException) {
            socketException = new SocketException("Socket is closed");
        } else if (exc instanceof NotYetConnectedException) {
            socketException = new SocketException("Socket is not connected");
        } else if (exc instanceof AlreadyBoundException) {
            socketException = new SocketException("Already bound");
        } else if (exc instanceof NotYetBoundException) {
            socketException = new SocketException("Socket is not bound yet");
        } else if (exc instanceof UnsupportedAddressTypeException) {
            socketException = new SocketException("Unsupported address type");
        } else if (exc instanceof UnresolvedAddressException) {
            socketException = new SocketException("Unresolved address");
        }
        if (socketException != exc) {
            socketException.initCause(exc);
        }
        if (socketException instanceof SocketException) {
            throw ((SocketException) socketException);
        }
        if (socketException instanceof RuntimeException) {
            throw ((RuntimeException) socketException);
        }
        throw new Error("Untranslated exception", socketException);
    }

    static void translateException(Exception exc, boolean z2) throws IOException {
        if (exc instanceof IOException) {
            throw ((IOException) exc);
        }
        if (z2 && (exc instanceof UnresolvedAddressException)) {
            throw new UnknownHostException();
        }
        translateToSocketException(exc);
    }

    static void translateException(Exception exc) throws IOException {
        translateException(exc, false);
    }

    static InetSocketAddress getRevealedLocalAddress(InetSocketAddress inetSocketAddress) {
        SecurityManager securityManager = System.getSecurityManager();
        if (inetSocketAddress == null || securityManager == null) {
            return inetSocketAddress;
        }
        try {
            securityManager.checkConnect(inetSocketAddress.getAddress().getHostAddress(), -1);
        } catch (SecurityException e2) {
            inetSocketAddress = getLoopbackAddress(inetSocketAddress.getPort());
        }
        return inetSocketAddress;
    }

    static String getRevealedLocalAddressAsString(InetSocketAddress inetSocketAddress) {
        return System.getSecurityManager() == null ? inetSocketAddress.toString() : getLoopbackAddress(inetSocketAddress.getPort()).toString();
    }

    private static InetSocketAddress getLoopbackAddress(int i2) {
        return new InetSocketAddress(InetAddress.getLoopbackAddress(), i2);
    }

    static Inet4Address anyInet4Address(final NetworkInterface networkInterface) {
        return (Inet4Address) AccessController.doPrivileged(new PrivilegedAction<Inet4Address>() { // from class: sun.nio.ch.Net.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Inet4Address run() {
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    if (inetAddressNextElement instanceof Inet4Address) {
                        return (Inet4Address) inetAddressNextElement;
                    }
                }
                return null;
            }
        });
    }

    static int inet4AsInt(InetAddress inetAddress) {
        if (inetAddress instanceof Inet4Address) {
            byte[] address = inetAddress.getAddress();
            return (address[3] & 255) | ((address[2] << 8) & NormalizerImpl.CC_MASK) | ((address[1] << 16) & 16711680) | ((address[0] << 24) & (-16777216));
        }
        throw new AssertionError((Object) "Should not reach here");
    }

    static InetAddress inet4FromInt(int i2) {
        try {
            return InetAddress.getByAddress(new byte[]{(byte) ((i2 >>> 24) & 255), (byte) ((i2 >>> 16) & 255), (byte) ((i2 >>> 8) & 255), (byte) (i2 & 255)});
        } catch (UnknownHostException e2) {
            throw new AssertionError((Object) "Should not reach here");
        }
    }

    static byte[] inet6AsByteArray(InetAddress inetAddress) {
        if (inetAddress instanceof Inet6Address) {
            return inetAddress.getAddress();
        }
        if (inetAddress instanceof Inet4Address) {
            byte[] address = inetAddress.getAddress();
            byte[] bArr = new byte[16];
            bArr[10] = -1;
            bArr[11] = -1;
            bArr[12] = address[0];
            bArr[13] = address[1];
            bArr[14] = address[2];
            bArr[15] = address[3];
            return bArr;
        }
        throw new AssertionError((Object) "Should not reach here");
    }

    static void setSocketOption(FileDescriptor fileDescriptor, ProtocolFamily protocolFamily, SocketOption<?> socketOption, Object obj) throws IOException {
        int iIntValue;
        int iIntValue2;
        int iIntValue3;
        if (obj == null) {
            throw new IllegalArgumentException("Invalid option value");
        }
        Class<?> clsType = socketOption.type();
        if (clsType == SocketFlow.class) {
            ExtendedOptionsImpl.checkSetOptionPermission(socketOption);
            ExtendedOptionsImpl.checkValueType(obj, SocketFlow.class);
            ExtendedOptionsImpl.setFlowOption(fileDescriptor, (SocketFlow) obj);
            return;
        }
        if (socketOption == ExtendedSocketOptions.TCP_KEEPINTERVAL) {
            ExtendedOptionsImpl.checkSetOptionPermission(socketOption);
            ExtendedOptionsImpl.checkValueType(obj, Integer.class);
            ExtendedOptionsImpl.setTcpKeepAliveIntvl(fileDescriptor, ((Integer) obj).intValue());
            return;
        }
        if (socketOption == ExtendedSocketOptions.TCP_KEEPIDLE) {
            ExtendedOptionsImpl.checkSetOptionPermission(socketOption);
            ExtendedOptionsImpl.checkValueType(obj, Integer.class);
            ExtendedOptionsImpl.setTcpKeepAliveTime(fileDescriptor, ((Integer) obj).intValue());
            return;
        }
        if (socketOption == ExtendedSocketOptions.TCP_KEEPCOUNT) {
            ExtendedOptionsImpl.checkSetOptionPermission(socketOption);
            ExtendedOptionsImpl.checkValueType(obj, Integer.class);
            ExtendedOptionsImpl.setTcpKeepAliveProbes(fileDescriptor, ((Integer) obj).intValue());
            return;
        }
        if (clsType != Integer.class && clsType != Boolean.class) {
            throw new AssertionError((Object) "Should not reach here");
        }
        if ((socketOption == StandardSocketOptions.SO_RCVBUF || socketOption == StandardSocketOptions.SO_SNDBUF) && ((Integer) obj).intValue() < 0) {
            throw new IllegalArgumentException("Invalid send/receive buffer size");
        }
        if (socketOption == StandardSocketOptions.SO_LINGER) {
            int iIntValue4 = ((Integer) obj).intValue();
            if (iIntValue4 < 0) {
                obj = -1;
            }
            if (iIntValue4 > 65535) {
                obj = 65535;
            }
        }
        if (socketOption == StandardSocketOptions.IP_TOS && ((iIntValue3 = ((Integer) obj).intValue()) < 0 || iIntValue3 > 255)) {
            throw new IllegalArgumentException("Invalid IP_TOS value");
        }
        if (socketOption == StandardSocketOptions.IP_MULTICAST_TTL && ((iIntValue2 = ((Integer) obj).intValue()) < 0 || iIntValue2 > 255)) {
            throw new IllegalArgumentException("Invalid TTL/hop value");
        }
        OptionKey optionKeyFindOption = SocketOptionRegistry.findOption(socketOption, protocolFamily);
        if (optionKeyFindOption == null) {
            throw new AssertionError((Object) "Option not found");
        }
        if (clsType == Integer.class) {
            iIntValue = ((Integer) obj).intValue();
        } else {
            iIntValue = ((Boolean) obj).booleanValue() ? 1 : 0;
        }
        setIntOption0(fileDescriptor, protocolFamily == UNSPEC, optionKeyFindOption.level(), optionKeyFindOption.name(), iIntValue, protocolFamily == StandardProtocolFamily.INET6);
    }

    static Object getSocketOption(FileDescriptor fileDescriptor, ProtocolFamily protocolFamily, SocketOption<?> socketOption) throws IOException {
        Class<?> clsType = socketOption.type();
        if (clsType == SocketFlow.class) {
            ExtendedOptionsImpl.checkGetOptionPermission(socketOption);
            SocketFlow socketFlowCreate = SocketFlow.create();
            ExtendedOptionsImpl.getFlowOption(fileDescriptor, socketFlowCreate);
            return socketFlowCreate;
        }
        if (socketOption == ExtendedSocketOptions.TCP_KEEPINTERVAL) {
            ExtendedOptionsImpl.checkGetOptionPermission(socketOption);
            return Integer.valueOf(ExtendedOptionsImpl.getTcpKeepAliveIntvl(fileDescriptor));
        }
        if (socketOption == ExtendedSocketOptions.TCP_KEEPIDLE) {
            ExtendedOptionsImpl.checkGetOptionPermission(socketOption);
            return Integer.valueOf(ExtendedOptionsImpl.getTcpKeepAliveTime(fileDescriptor));
        }
        if (socketOption == ExtendedSocketOptions.TCP_KEEPCOUNT) {
            ExtendedOptionsImpl.checkGetOptionPermission(socketOption);
            return Integer.valueOf(ExtendedOptionsImpl.getTcpKeepAliveProbes(fileDescriptor));
        }
        if (clsType != Integer.class && clsType != Boolean.class) {
            throw new AssertionError((Object) "Should not reach here");
        }
        OptionKey optionKeyFindOption = SocketOptionRegistry.findOption(socketOption, protocolFamily);
        if (optionKeyFindOption == null) {
            throw new AssertionError((Object) "Option not found");
        }
        int intOption0 = getIntOption0(fileDescriptor, protocolFamily == UNSPEC, optionKeyFindOption.level(), optionKeyFindOption.name());
        if (clsType == Integer.class) {
            return Integer.valueOf(intOption0);
        }
        return intOption0 == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    public static boolean isFastTcpLoopbackRequested() {
        boolean z2;
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.nio.ch.Net.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return System.getProperty("jdk.net.useFastTcpLoopback");
            }
        });
        if ("".equals(str)) {
            z2 = true;
        } else {
            z2 = Boolean.parseBoolean(str);
        }
        return z2;
    }

    static FileDescriptor socket(boolean z2) throws IOException {
        return socket(UNSPEC, z2);
    }

    static FileDescriptor socket(ProtocolFamily protocolFamily, boolean z2) throws IOException {
        return IOUtil.newFD(socket0(isIPv6Available() && protocolFamily != StandardProtocolFamily.INET, z2, false, fastLoopback));
    }

    static FileDescriptor serverSocket(boolean z2) {
        return IOUtil.newFD(socket0(isIPv6Available(), z2, true, fastLoopback));
    }

    public static void bind(FileDescriptor fileDescriptor, InetAddress inetAddress, int i2) throws IOException {
        bind(UNSPEC, fileDescriptor, inetAddress, i2);
    }

    static void bind(ProtocolFamily protocolFamily, FileDescriptor fileDescriptor, InetAddress inetAddress, int i2) throws IOException {
        bind0(fileDescriptor, isIPv6Available() && protocolFamily != StandardProtocolFamily.INET, exclusiveBind, inetAddress, i2);
    }

    static int connect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i2) throws IOException {
        return connect(UNSPEC, fileDescriptor, inetAddress, i2);
    }

    static int connect(ProtocolFamily protocolFamily, FileDescriptor fileDescriptor, InetAddress inetAddress, int i2) throws IOException {
        return connect0(isIPv6Available() && protocolFamily != StandardProtocolFamily.INET, fileDescriptor, inetAddress, i2);
    }

    public static InetSocketAddress localAddress(FileDescriptor fileDescriptor) throws IOException {
        return new InetSocketAddress(localInetAddress(fileDescriptor), localPort(fileDescriptor));
    }

    static InetSocketAddress remoteAddress(FileDescriptor fileDescriptor) throws IOException {
        return new InetSocketAddress(remoteInetAddress(fileDescriptor), remotePort(fileDescriptor));
    }

    static int join4(FileDescriptor fileDescriptor, int i2, int i3, int i4) throws IOException {
        return joinOrDrop4(true, fileDescriptor, i2, i3, i4);
    }

    static void drop4(FileDescriptor fileDescriptor, int i2, int i3, int i4) throws IOException {
        joinOrDrop4(false, fileDescriptor, i2, i3, i4);
    }

    static int block4(FileDescriptor fileDescriptor, int i2, int i3, int i4) throws IOException {
        return blockOrUnblock4(true, fileDescriptor, i2, i3, i4);
    }

    static void unblock4(FileDescriptor fileDescriptor, int i2, int i3, int i4) throws IOException {
        blockOrUnblock4(false, fileDescriptor, i2, i3, i4);
    }

    static int join6(FileDescriptor fileDescriptor, byte[] bArr, int i2, byte[] bArr2) throws IOException {
        return joinOrDrop6(true, fileDescriptor, bArr, i2, bArr2);
    }

    static void drop6(FileDescriptor fileDescriptor, byte[] bArr, int i2, byte[] bArr2) throws IOException {
        joinOrDrop6(false, fileDescriptor, bArr, i2, bArr2);
    }

    static int block6(FileDescriptor fileDescriptor, byte[] bArr, int i2, byte[] bArr2) throws IOException {
        return blockOrUnblock6(true, fileDescriptor, bArr, i2, bArr2);
    }

    static void unblock6(FileDescriptor fileDescriptor, byte[] bArr, int i2, byte[] bArr2) throws IOException {
        blockOrUnblock6(false, fileDescriptor, bArr, i2, bArr2);
    }
}
