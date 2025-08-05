package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import java.nio.ByteBuffer;
import org.omg.CORBA.portable.OutputStream;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/Message_1_2.class */
public class Message_1_2 extends Message_1_1 {
    protected int request_id;

    Message_1_2() {
        this.request_id = 0;
    }

    Message_1_2(int i2, GIOPVersion gIOPVersion, byte b2, byte b3, int i3) {
        super(i2, gIOPVersion, b2, b3, i3);
        this.request_id = 0;
    }

    public void unmarshalRequestID(ByteBuffer byteBuffer) {
        int i2;
        int i3;
        int i4;
        int i5;
        if (!isLittleEndian()) {
            i2 = (byteBuffer.get(12) << 24) & (-16777216);
            i3 = (byteBuffer.get(13) << 16) & 16711680;
            i4 = (byteBuffer.get(14) << 8) & NormalizerImpl.CC_MASK;
            i5 = (byteBuffer.get(15) << 0) & 255;
        } else {
            i2 = (byteBuffer.get(15) << 24) & (-16777216);
            i3 = (byteBuffer.get(14) << 16) & 16711680;
            i4 = (byteBuffer.get(13) << 8) & NormalizerImpl.CC_MASK;
            i5 = (byteBuffer.get(12) << 0) & 255;
        }
        this.request_id = i2 | i3 | i4 | i5;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        if (this.encodingVersion == 0) {
            super.write(outputStream);
            return;
        }
        GIOPVersion gIOPVersion = this.GIOP_version;
        this.GIOP_version = GIOPVersion.getInstance((byte) 13, this.encodingVersion);
        super.write(outputStream);
        this.GIOP_version = gIOPVersion;
    }
}
