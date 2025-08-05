package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.nio.ByteBuffer;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/Message_1_1.class */
public class Message_1_1 extends MessageBase {
    static final int UPPER_THREE_BYTES_OF_INT_MASK = 255;
    private static ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PROTOCOL);
    int magic;
    GIOPVersion GIOP_version;
    byte flags;
    byte message_type;
    int message_size;

    Message_1_1() {
        this.magic = 0;
        this.GIOP_version = null;
        this.flags = (byte) 0;
        this.message_type = (byte) 0;
        this.message_size = 0;
    }

    Message_1_1(int i2, GIOPVersion gIOPVersion, byte b2, byte b3, int i3) {
        this.magic = 0;
        this.GIOP_version = null;
        this.flags = (byte) 0;
        this.message_type = (byte) 0;
        this.message_size = 0;
        this.magic = i2;
        this.GIOP_version = gIOPVersion;
        this.flags = b2;
        this.message_type = b3;
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
        return (this.flags & 1) == 1;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public boolean moreFragmentsToFollow() {
        return (this.flags & 2) == 2;
    }

    public void setThreadPoolToUse(int i2) {
        this.flags = (byte) (((i2 << 2) & 255) | this.flags);
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
        switch (this.message_type) {
            case 2:
            case 5:
            case 6:
                throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
            case 3:
            case 4:
                if (this.GIOP_version.equals(GIOPVersion.V1_1)) {
                    throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
                }
                break;
        }
        if (this.GIOP_version.equals(GIOPVersion.V1_1)) {
            return new FragmentMessage_1_1(this);
        }
        if (this.GIOP_version.equals(GIOPVersion.V1_2)) {
            return new FragmentMessage_1_2(this);
        }
        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        outputStream.write_long(this.magic);
        nullCheck(this.GIOP_version);
        this.GIOP_version.write(outputStream);
        outputStream.write_octet(this.flags);
        outputStream.write_octet(this.message_type);
        outputStream.write_ulong(this.message_size);
    }
}
