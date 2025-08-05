package sun.management.jdp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:sun/management/jdp/JdpPacketWriter.class */
public final class JdpPacketWriter {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final DataOutputStream pkt = new DataOutputStream(this.baos);

    public JdpPacketWriter() throws IOException {
        this.pkt.writeInt(JdpGenericPacket.getMagic());
        this.pkt.writeShort(JdpGenericPacket.getVersion());
    }

    public void addEntry(String str) throws IOException {
        this.pkt.writeUTF(str);
    }

    public void addEntry(String str, String str2) throws IOException {
        if (str2 != null) {
            addEntry(str);
            addEntry(str2);
        }
    }

    public byte[] getPacketBytes() {
        return this.baos.toByteArray();
    }
}
