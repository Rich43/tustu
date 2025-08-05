package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import java.io.IOException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/FragmentMessage_1_2.class */
public final class FragmentMessage_1_2 extends Message_1_2 implements FragmentMessage {
    FragmentMessage_1_2() {
    }

    FragmentMessage_1_2(int i2) {
        super(Message.GIOPBigMagic, GIOPVersion.V1_2, (byte) 0, (byte) 7, 0);
        this.message_type = (byte) 7;
        this.request_id = i2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    FragmentMessage_1_2(Message_1_1 message_1_1) {
        this.magic = message_1_1.magic;
        this.GIOP_version = message_1_1.GIOP_version;
        this.flags = message_1_1.flags;
        this.message_type = (byte) 7;
        this.message_size = 0;
        switch (message_1_1.message_type) {
            case 0:
                this.request_id = ((RequestMessage) message_1_1).getRequestId();
                break;
            case 1:
                this.request_id = ((ReplyMessage) message_1_1).getRequestId();
                break;
            case 3:
                this.request_id = ((LocateRequestMessage) message_1_1).getRequestId();
                break;
            case 4:
                this.request_id = ((LocateReplyMessage) message_1_1).getRequestId();
                break;
            case 7:
                this.request_id = ((FragmentMessage) message_1_1).getRequestId();
                break;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage
    public int getRequestId() {
        return this.request_id;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage
    public int getHeaderLength() {
        return 16;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
        this.request_id = inputStream.read_ulong();
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_2, com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        super.write(outputStream);
        outputStream.write_ulong(this.request_id);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void callback(MessageHandler messageHandler) throws IOException {
        messageHandler.handleInput(this);
    }
}
