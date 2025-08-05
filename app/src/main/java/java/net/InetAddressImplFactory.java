package java.net;

/* compiled from: InetAddress.java */
/* loaded from: rt.jar:java/net/InetAddressImplFactory.class */
class InetAddressImplFactory {
    static native boolean isIPv6Supported();

    InetAddressImplFactory() {
    }

    static InetAddressImpl create() {
        return InetAddress.loadImpl(isIPv6Supported() ? "Inet6AddressImpl" : "Inet4AddressImpl");
    }
}
