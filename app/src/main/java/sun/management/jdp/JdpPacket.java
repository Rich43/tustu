package sun.management.jdp;

import java.io.IOException;

/* loaded from: rt.jar:sun/management/jdp/JdpPacket.class */
public interface JdpPacket {
    byte[] getPacketData() throws IOException;
}
