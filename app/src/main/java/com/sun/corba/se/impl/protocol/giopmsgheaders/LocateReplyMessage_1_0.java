package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.io.IOException;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/LocateReplyMessage_1_0.class */
public final class LocateReplyMessage_1_0 extends Message_1_0 implements LocateReplyMessage {
    private ORB orb;
    private int request_id;
    private int locate_status;
    private IOR ior;

    LocateReplyMessage_1_0(ORB orb) {
        this.orb = null;
        this.request_id = 0;
        this.locate_status = 0;
        this.ior = null;
        this.orb = orb;
    }

    LocateReplyMessage_1_0(ORB orb, int i2, int i3, IOR ior) {
        super(Message.GIOPBigMagic, false, (byte) 4, 0);
        this.orb = null;
        this.request_id = 0;
        this.locate_status = 0;
        this.ior = null;
        this.orb = orb;
        this.request_id = i2;
        this.locate_status = i3;
        this.ior = ior;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public int getRequestId() {
        return this.request_id;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public int getReplyStatus() {
        return this.locate_status;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public short getAddrDisposition() {
        return (short) 0;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public SystemException getSystemException(String str) {
        return null;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public IOR getIOR() {
        return this.ior;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_0, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
        this.request_id = inputStream.read_ulong();
        this.locate_status = inputStream.read_long();
        isValidReplyStatus(this.locate_status);
        if (this.locate_status == 2) {
            this.ior = IORFactories.makeIOR((CDRInputStream) inputStream);
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_0, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        super.write(outputStream);
        outputStream.write_ulong(this.request_id);
        outputStream.write_long(this.locate_status);
    }

    public static void isValidReplyStatus(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
                return;
            default:
                throw ORBUtilSystemException.get(CORBALogDomains.RPC_PROTOCOL).illegalReplyStatus(CompletionStatus.COMPLETED_MAYBE);
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void callback(MessageHandler messageHandler) throws IOException {
        messageHandler.handleInput(this);
    }
}
