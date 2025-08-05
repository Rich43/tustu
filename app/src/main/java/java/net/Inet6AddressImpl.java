package java.net;

import java.io.IOException;
import java.util.Enumeration;

/* loaded from: rt.jar:java/net/Inet6AddressImpl.class */
class Inet6AddressImpl implements InetAddressImpl {
    private InetAddress anyLocalAddress;
    private InetAddress loopbackAddress;

    @Override // java.net.InetAddressImpl
    public native String getLocalHostName() throws UnknownHostException;

    @Override // java.net.InetAddressImpl
    public native InetAddress[] lookupAllHostAddr(String str) throws UnknownHostException;

    @Override // java.net.InetAddressImpl
    public native String getHostByAddr(byte[] bArr) throws UnknownHostException;

    private native boolean isReachable0(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws IOException;

    Inet6AddressImpl() {
    }

    @Override // java.net.InetAddressImpl
    public boolean isReachable(InetAddress inetAddress, int i2, NetworkInterface networkInterface, int i3) throws IOException {
        byte[] address = null;
        int scopeId = -1;
        int scopeId2 = -1;
        if (networkInterface != null) {
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (true) {
                if (!inetAddresses.hasMoreElements()) {
                    break;
                }
                InetAddress inetAddressNextElement2 = inetAddresses.nextElement2();
                if (inetAddressNextElement2.getClass().isInstance(inetAddress)) {
                    address = inetAddressNextElement2.getAddress();
                    if (inetAddressNextElement2 instanceof Inet6Address) {
                        scopeId2 = ((Inet6Address) inetAddressNextElement2).getScopeId();
                    }
                }
            }
            if (address == null) {
                return false;
            }
        }
        if (inetAddress instanceof Inet6Address) {
            scopeId = ((Inet6Address) inetAddress).getScopeId();
        }
        return isReachable0(inetAddress.getAddress(), scopeId, i2, address, i3, scopeId2);
    }

    @Override // java.net.InetAddressImpl
    public synchronized InetAddress anyLocalAddress() {
        if (this.anyLocalAddress == null) {
            if (InetAddress.preferIPv6Address) {
                this.anyLocalAddress = new Inet6Address();
                this.anyLocalAddress.holder().hostName = "::";
            } else {
                this.anyLocalAddress = new Inet4AddressImpl().anyLocalAddress();
            }
        }
        return this.anyLocalAddress;
    }

    @Override // java.net.InetAddressImpl
    public synchronized InetAddress loopbackAddress() {
        if (this.loopbackAddress == null) {
            if (InetAddress.preferIPv6Address) {
                this.loopbackAddress = new Inet6Address("localhost", new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1});
            } else {
                this.loopbackAddress = new Inet4AddressImpl().loopbackAddress();
            }
        }
        return this.loopbackAddress;
    }
}
