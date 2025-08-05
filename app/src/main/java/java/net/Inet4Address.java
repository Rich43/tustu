package java.net;

import java.io.ObjectStreamException;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/net/Inet4Address.class */
public final class Inet4Address extends InetAddress {
    static final int INADDRSZ = 4;
    private static final long serialVersionUID = 3286316764910316507L;

    private static native void init();

    static {
        init();
    }

    Inet4Address() {
        holder().hostName = null;
        holder().address = 0;
        holder().family = 1;
    }

    Inet4Address(String str, byte[] bArr) {
        holder().hostName = str;
        holder().family = 1;
        if (bArr != null && bArr.length == 4) {
            holder().address = (bArr[3] & 255) | ((bArr[2] << 8) & NormalizerImpl.CC_MASK) | ((bArr[1] << 16) & 16711680) | ((bArr[0] << 24) & (-16777216));
        }
        holder().originalHostName = str;
    }

    Inet4Address(String str, int i2) {
        holder().hostName = str;
        holder().family = 1;
        holder().address = i2;
        holder().originalHostName = str;
    }

    private Object writeReplace() throws ObjectStreamException {
        InetAddress inetAddress = new InetAddress();
        inetAddress.holder().hostName = holder().getHostName();
        inetAddress.holder().address = holder().getAddress();
        inetAddress.holder().family = 2;
        return inetAddress;
    }

    @Override // java.net.InetAddress
    public boolean isMulticastAddress() {
        return (holder().getAddress() & (-268435456)) == -536870912;
    }

    @Override // java.net.InetAddress
    public boolean isAnyLocalAddress() {
        return holder().getAddress() == 0;
    }

    @Override // java.net.InetAddress
    public boolean isLoopbackAddress() {
        return getAddress()[0] == Byte.MAX_VALUE;
    }

    @Override // java.net.InetAddress
    public boolean isLinkLocalAddress() {
        int address = holder().getAddress();
        return ((address >>> 24) & 255) == 169 && ((address >>> 16) & 255) == 254;
    }

    @Override // java.net.InetAddress
    public boolean isSiteLocalAddress() {
        int address = holder().getAddress();
        return ((address >>> 24) & 255) == 10 || (((address >>> 24) & 255) == 172 && ((address >>> 16) & 240) == 16) || (((address >>> 24) & 255) == 192 && ((address >>> 16) & 255) == 168);
    }

    @Override // java.net.InetAddress
    public boolean isMCGlobal() {
        byte[] address = getAddress();
        return (address[0] & 255) >= 224 && (address[0] & 255) <= 238 && !((address[0] & 255) == 224 && address[1] == 0 && address[2] == 0);
    }

    @Override // java.net.InetAddress
    public boolean isMCNodeLocal() {
        return false;
    }

    @Override // java.net.InetAddress
    public boolean isMCLinkLocal() {
        int address = holder().getAddress();
        return ((address >>> 24) & 255) == 224 && ((address >>> 16) & 255) == 0 && ((address >>> 8) & 255) == 0;
    }

    @Override // java.net.InetAddress
    public boolean isMCSiteLocal() {
        int address = holder().getAddress();
        return ((address >>> 24) & 255) == 239 && ((address >>> 16) & 255) == 255;
    }

    @Override // java.net.InetAddress
    public boolean isMCOrgLocal() {
        int address = holder().getAddress();
        return ((address >>> 24) & 255) == 239 && ((address >>> 16) & 255) >= 192 && ((address >>> 16) & 255) <= 195;
    }

    @Override // java.net.InetAddress
    public byte[] getAddress() {
        int address = holder().getAddress();
        return new byte[]{(byte) ((address >>> 24) & 255), (byte) ((address >>> 16) & 255), (byte) ((address >>> 8) & 255), (byte) (address & 255)};
    }

    @Override // java.net.InetAddress
    public String getHostAddress() {
        return numericToTextFormat(getAddress());
    }

    @Override // java.net.InetAddress
    public int hashCode() {
        return holder().getAddress();
    }

    @Override // java.net.InetAddress
    public boolean equals(Object obj) {
        return obj != null && (obj instanceof Inet4Address) && ((InetAddress) obj).holder().getAddress() == holder().getAddress();
    }

    static String numericToTextFormat(byte[] bArr) {
        return (bArr[0] & 255) + "." + (bArr[1] & 255) + "." + (bArr[2] & 255) + "." + (bArr[3] & 255);
    }
}
