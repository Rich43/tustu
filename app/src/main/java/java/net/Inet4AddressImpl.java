package java.net;

import java.io.IOException;
import java.util.Enumeration;

/* loaded from: rt.jar:java/net/Inet4AddressImpl.class */
class Inet4AddressImpl implements InetAddressImpl {
    private InetAddress anyLocalAddress;
    private InetAddress loopbackAddress;

    @Override // java.net.InetAddressImpl
    public native String getLocalHostName() throws UnknownHostException;

    @Override // java.net.InetAddressImpl
    public native InetAddress[] lookupAllHostAddr(String str) throws UnknownHostException;

    @Override // java.net.InetAddressImpl
    public native String getHostByAddr(byte[] bArr) throws UnknownHostException;

    private native boolean isReachable0(byte[] bArr, int i2, byte[] bArr2, int i3) throws IOException;

    Inet4AddressImpl() {
    }

    @Override // java.net.InetAddressImpl
    public synchronized InetAddress anyLocalAddress() {
        if (this.anyLocalAddress == null) {
            this.anyLocalAddress = new Inet4Address();
            this.anyLocalAddress.holder().hostName = "0.0.0.0";
        }
        return this.anyLocalAddress;
    }

    @Override // java.net.InetAddressImpl
    public synchronized InetAddress loopbackAddress() {
        if (this.loopbackAddress == null) {
            this.loopbackAddress = new Inet4Address("localhost", new byte[]{Byte.MAX_VALUE, 0, 0, 1});
        }
        return this.loopbackAddress;
    }

    @Override // java.net.InetAddressImpl
    public boolean isReachable(InetAddress inetAddress, int i2, NetworkInterface networkInterface, int i3) throws IOException {
        InetAddress inetAddress2;
        byte[] address = null;
        if (networkInterface != null) {
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            InetAddress inetAddressNextElement2 = null;
            while (true) {
                inetAddress2 = inetAddressNextElement2;
                if ((inetAddress2 instanceof Inet4Address) || !inetAddresses.hasMoreElements()) {
                    break;
                }
                inetAddressNextElement2 = inetAddresses.nextElement2();
            }
            if (inetAddress2 instanceof Inet4Address) {
                address = inetAddress2.getAddress();
            }
        }
        return isReachable0(inetAddress.getAddress(), i2, address, i3);
    }
}
