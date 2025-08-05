package sun.management.jdp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:sun/management/jdp/JdpPacketReader.class */
public final class JdpPacketReader {
    private final DataInputStream pkt;
    private Map<String, String> pmap = null;

    public JdpPacketReader(byte[] bArr) throws JdpException {
        this.pkt = new DataInputStream(new ByteArrayInputStream(bArr));
        try {
            JdpGenericPacket.checkMagic(this.pkt.readInt());
            try {
                JdpGenericPacket.checkVersion(this.pkt.readShort());
            } catch (IOException e2) {
                throw new JdpException("Invalid JDP packet received, bad protocol version");
            }
        } catch (IOException e3) {
            throw new JdpException("Invalid JDP packet received, bad magic");
        }
    }

    public String getEntry() throws JdpException, EOFException {
        try {
            int i2 = this.pkt.readShort();
            if (i2 < 1 && i2 > this.pkt.available()) {
                throw new JdpException("Broken JDP packet. Invalid entry length field.");
            }
            byte[] bArr = new byte[i2];
            if (this.pkt.read(bArr) != i2) {
                throw new JdpException("Broken JDP packet. Unable to read entry.");
            }
            return new String(bArr, "UTF-8");
        } catch (EOFException e2) {
            throw e2;
        } catch (UnsupportedEncodingException e3) {
            throw new JdpException("Broken JDP packet. Unable to decode entry.");
        } catch (IOException e4) {
            throw new JdpException("Broken JDP packet. Unable to read entry.");
        }
    }

    public Map<String, String> getDiscoveryDataAsMap() throws JdpException {
        if (this.pmap != null) {
            return this.pmap;
        }
        String entry = null;
        String entry2 = null;
        HashMap map = new HashMap();
        while (true) {
            try {
                entry = getEntry();
                entry2 = getEntry();
                map.put(entry, entry2);
            } catch (EOFException e2) {
                if (entry2 == null) {
                    throw new JdpException("Broken JDP packet. Key without value." + entry);
                }
                this.pmap = Collections.unmodifiableMap(map);
                return this.pmap;
            }
        }
    }
}
