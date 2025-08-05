package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.io.IOException;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/LocateReplyMessage_1_2.class */
public final class LocateReplyMessage_1_2 extends Message_1_2 implements LocateReplyMessage {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private int reply_status;
    private IOR ior;
    private String exClassName;
    private int minorCode;
    private CompletionStatus completionStatus;
    private short addrDisposition;

    LocateReplyMessage_1_2(ORB orb) {
        this.orb = null;
        this.wrapper = null;
        this.reply_status = 0;
        this.ior = null;
        this.exClassName = null;
        this.minorCode = 0;
        this.completionStatus = null;
        this.addrDisposition = (short) 0;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    LocateReplyMessage_1_2(ORB orb, int i2, int i3, IOR ior) {
        super(Message.GIOPBigMagic, GIOPVersion.V1_2, (byte) 0, (byte) 4, 0);
        this.orb = null;
        this.wrapper = null;
        this.reply_status = 0;
        this.ior = null;
        this.exClassName = null;
        this.minorCode = 0;
        this.completionStatus = null;
        this.addrDisposition = (short) 0;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.request_id = i2;
        this.reply_status = i3;
        this.ior = ior;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public int getRequestId() {
        return this.request_id;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public int getReplyStatus() {
        return this.reply_status;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public short getAddrDisposition() {
        return this.addrDisposition;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public SystemException getSystemException(String str) {
        return MessageBase.getSystemException(this.exClassName, this.minorCode, this.completionStatus, str, this.wrapper);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public IOR getIOR() {
        return this.ior;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
        this.request_id = inputStream.read_ulong();
        this.reply_status = inputStream.read_long();
        isValidReplyStatus(this.reply_status);
        if (this.reply_status != 4) {
            if (this.reply_status == 2 || this.reply_status == 3) {
                this.ior = IORFactories.makeIOR((CDRInputStream) inputStream);
                return;
            } else {
                if (this.reply_status == 5) {
                    this.addrDisposition = AddressingDispositionHelper.read(inputStream);
                    return;
                }
                return;
            }
        }
        this.exClassName = ORBUtility.classNameOf(inputStream.read_string());
        this.minorCode = inputStream.read_long();
        int i2 = inputStream.read_long();
        switch (i2) {
            case 0:
                this.completionStatus = CompletionStatus.COMPLETED_YES;
                return;
            case 1:
                this.completionStatus = CompletionStatus.COMPLETED_NO;
                return;
            case 2:
                this.completionStatus = CompletionStatus.COMPLETED_MAYBE;
                return;
            default:
                throw this.wrapper.badCompletionStatusInLocateReply(CompletionStatus.COMPLETED_MAYBE, new Integer(i2));
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_2, com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) {
        super.write(outputStream);
        outputStream.write_ulong(this.request_id);
        outputStream.write_long(this.reply_status);
    }

    public static void isValidReplyStatus(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
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
