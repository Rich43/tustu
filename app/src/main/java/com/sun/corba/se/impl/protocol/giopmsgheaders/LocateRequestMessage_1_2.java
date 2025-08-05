package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import java.io.IOException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/LocateRequestMessage_1_2.class */
public final class LocateRequestMessage_1_2 extends Message_1_2 implements LocateRequestMessage {
    private ORB orb;
    private ObjectKey objectKey;
    private TargetAddress target;

    LocateRequestMessage_1_2(ORB orb) {
        this.orb = null;
        this.objectKey = null;
        this.target = null;
        this.orb = orb;
    }

    LocateRequestMessage_1_2(ORB orb, int i2, TargetAddress targetAddress) {
        super(Message.GIOPBigMagic, GIOPVersion.V1_2, (byte) 0, (byte) 3, 0);
        this.orb = null;
        this.objectKey = null;
        this.target = null;
        this.orb = orb;
        this.request_id = i2;
        this.target = targetAddress;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage
    public int getRequestId() {
        return this.request_id;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage
    public ObjectKey getObjectKey() {
        if (this.objectKey == null) {
            this.objectKey = MessageBase.extractObjectKey(this.target, this.orb);
        }
        return this.objectKey;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
        this.request_id = inputStream.read_ulong();
        this.target = TargetAddressHelper.read(inputStream);
        getObjectKey();
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_2, com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        super.write(outputStream);
        outputStream.write_ulong(this.request_id);
        nullCheck(this.target);
        TargetAddressHelper.write(outputStream, this.target);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void callback(MessageHandler messageHandler) throws IOException {
        messageHandler.handleInput(this);
    }
}
