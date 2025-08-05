package sun.management.jdp;

/* loaded from: rt.jar:sun/management/jdp/JdpGenericPacket.class */
public abstract class JdpGenericPacket implements JdpPacket {
    private static final int MAGIC = -1056969150;
    private static final short PROTOCOL_VERSION = 1;

    protected JdpGenericPacket() {
    }

    public static void checkMagic(int i2) throws JdpException {
        if (i2 != MAGIC) {
            throw new JdpException("Invalid JDP magic header: " + i2);
        }
    }

    public static void checkVersion(short s2) throws JdpException {
        if (s2 > 1) {
            throw new JdpException("Unsupported protocol version: " + ((int) s2));
        }
    }

    public static int getMagic() {
        return MAGIC;
    }

    public static short getVersion() {
        return (short) 1;
    }
}
