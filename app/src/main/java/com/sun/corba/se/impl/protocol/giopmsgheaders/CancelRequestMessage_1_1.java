package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import java.io.IOException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/CancelRequestMessage_1_1.class */
public final class CancelRequestMessage_1_1 extends Message_1_1 implements CancelRequestMessage {
    private int request_id;

    CancelRequestMessage_1_1() {
        this.request_id = 0;
    }

    CancelRequestMessage_1_1(int i2) {
        super(Message.GIOPBigMagic, GIOPVersion.V1_1, (byte) 0, (byte) 2, 4);
        this.request_id = 0;
        this.request_id = i2;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.CancelRequestMessage
    public int getRequestId() {
        return this.request_id;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
        this.request_id = inputStream.read_ulong();
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        super.write(outputStream);
        outputStream.write_ulong(this.request_id);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void callback(MessageHandler messageHandler) throws IOException {
        messageHandler.handleInput((CancelRequestMessage) this);
    }
}
