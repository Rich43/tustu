package com.sun.corba.se.impl.protocol.giopmsgheaders;

import java.io.IOException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/FragmentMessage_1_1.class */
public final class FragmentMessage_1_1 extends Message_1_1 implements FragmentMessage {
    FragmentMessage_1_1() {
    }

    FragmentMessage_1_1(Message_1_1 message_1_1) {
        this.magic = message_1_1.magic;
        this.GIOP_version = message_1_1.GIOP_version;
        this.flags = message_1_1.flags;
        this.message_type = (byte) 7;
        this.message_size = 0;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage
    public int getRequestId() {
        return -1;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage
    public int getHeaderLength() {
        return 12;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        super.write(outputStream);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void callback(MessageHandler messageHandler) throws IOException {
        messageHandler.handleInput(this);
    }
}
