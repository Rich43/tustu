package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.nio.ByteBuffer;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/Message_1_0.class */
public class Message_1_0 extends MessageBase {
    private static ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PROTOCOL);
    int magic;
    GIOPVersion GIOP_version;
    boolean byte_order;
    byte message_type;
    int message_size;

    Message_1_0() {
        this.magic = 0;
        this.GIOP_version = null;
        this.byte_order = false;
        this.message_type = (byte) 0;
        this.message_size = 0;
    }

    Message_1_0(int i2, boolean z2, byte b2, int i3) {
        this.magic = 0;
        this.GIOP_version = null;
        this.byte_order = false;
        this.message_type = (byte) 0;
        this.message_size = 0;
        this.magic = i2;
        this.GIOP_version = GIOPVersion.V1_0;
        this.byte_order = z2;
        this.message_type = b2;
        this.message_size = i3;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public GIOPVersion getGIOPVersion() {
        return this.GIOP_version;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public int getType() {
        return this.message_type;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public int getSize() {
        return this.message_size;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public boolean isLittleEndian() {
        return this.byte_order;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public boolean moreFragmentsToFollow() {
        return false;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void setSize(ByteBuffer byteBuffer, int i2) {
        this.message_size = i2;
        int i3 = i2 - 12;
        if (!isLittleEndian()) {
            byteBuffer.put(8, (byte) ((i3 >>> 24) & 255));
            byteBuffer.put(9, (byte) ((i3 >>> 16) & 255));
            byteBuffer.put(10, (byte) ((i3 >>> 8) & 255));
            byteBuffer.put(11, (byte) ((i3 >>> 0) & 255));
            return;
        }
        byteBuffer.put(8, (byte) ((i3 >>> 0) & 255));
        byteBuffer.put(9, (byte) ((i3 >>> 8) & 255));
        byteBuffer.put(10, (byte) ((i3 >>> 16) & 255));
        byteBuffer.put(11, (byte) ((i3 >>> 24) & 255));
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public FragmentMessage createFragmentMessage() {
        throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        outputStream.write_long(this.magic);
        nullCheck(this.GIOP_version);
        this.GIOP_version.write(outputStream);
        outputStream.write_boolean(this.byte_order);
        outputStream.write_octet(this.message_type);
        outputStream.write_ulong(this.message_size);
    }
}
