package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.orb.ORB;
import java.io.IOException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/LocateRequestMessage_1_0.class */
public final class LocateRequestMessage_1_0 extends Message_1_0 implements LocateRequestMessage {
    private ORB orb;
    private int request_id;
    private byte[] object_key;
    private ObjectKey objectKey;

    LocateRequestMessage_1_0(ORB orb) {
        this.orb = null;
        this.request_id = 0;
        this.object_key = null;
        this.objectKey = null;
        this.orb = orb;
    }

    LocateRequestMessage_1_0(ORB orb, int i2, byte[] bArr) {
        super(Message.GIOPBigMagic, false, (byte) 3, 0);
        this.orb = null;
        this.request_id = 0;
        this.object_key = null;
        this.objectKey = null;
        this.orb = orb;
        this.request_id = i2;
        this.object_key = bArr;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage
    public int getRequestId() {
        return this.request_id;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage
    public ObjectKey getObjectKey() {
        if (this.objectKey == null) {
            this.objectKey = MessageBase.extractObjectKey(this.object_key, this.orb);
        }
        return this.objectKey;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_0, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
        this.request_id = inputStream.read_ulong();
        int i2 = inputStream.read_long();
        this.object_key = new byte[i2];
        inputStream.read_octet_array(this.object_key, 0, i2);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_0, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        super.write(outputStream);
        outputStream.write_ulong(this.request_id);
        nullCheck(this.object_key);
        outputStream.write_long(this.object_key.length);
        outputStream.write_octet_array(this.object_key, 0, this.object_key.length);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void callback(MessageHandler messageHandler) throws IOException {
        messageHandler.handleInput(this);
    }
}
