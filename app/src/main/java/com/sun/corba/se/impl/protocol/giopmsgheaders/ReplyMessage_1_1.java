package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import java.io.IOException;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/ReplyMessage_1_1.class */
public final class ReplyMessage_1_1 extends Message_1_1 implements ReplyMessage {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private ServiceContexts service_contexts;
    private int request_id;
    private int reply_status;
    private IOR ior;
    private String exClassName;
    private int minorCode;
    private CompletionStatus completionStatus;

    ReplyMessage_1_1(ORB orb) {
        this.orb = null;
        this.wrapper = null;
        this.service_contexts = null;
        this.request_id = 0;
        this.reply_status = 0;
        this.ior = null;
        this.exClassName = null;
        this.minorCode = 0;
        this.completionStatus = null;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    ReplyMessage_1_1(ORB orb, ServiceContexts serviceContexts, int i2, int i3, IOR ior) {
        super(Message.GIOPBigMagic, GIOPVersion.V1_1, (byte) 0, (byte) 1, 0);
        this.orb = null;
        this.wrapper = null;
        this.service_contexts = null;
        this.request_id = 0;
        this.reply_status = 0;
        this.ior = null;
        this.exClassName = null;
        this.minorCode = 0;
        this.completionStatus = null;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.service_contexts = serviceContexts;
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
        return (short) 0;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage
    public ServiceContexts getServiceContexts() {
        return this.service_contexts;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage
    public void setServiceContexts(ServiceContexts serviceContexts) {
        this.service_contexts = serviceContexts;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public SystemException getSystemException(String str) {
        return MessageBase.getSystemException(this.exClassName, this.minorCode, this.completionStatus, str, this.wrapper);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
    public IOR getIOR() {
        return this.ior;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage
    public void setIOR(IOR ior) {
        this.ior = ior;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
        this.service_contexts = new ServiceContexts((org.omg.CORBA_2_3.portable.InputStream) inputStream);
        this.request_id = inputStream.read_ulong();
        this.reply_status = inputStream.read_long();
        isValidReplyStatus(this.reply_status);
        if (this.reply_status == 2) {
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
                    throw this.wrapper.badCompletionStatusInReply(CompletionStatus.COMPLETED_MAYBE, new Integer(i2));
            }
        }
        if (this.reply_status != 1 && this.reply_status == 3) {
            this.ior = IORFactories.makeIOR((CDRInputStream) inputStream);
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) throws SystemException {
        super.write(outputStream);
        if (this.service_contexts != null) {
            this.service_contexts.write((org.omg.CORBA_2_3.portable.OutputStream) outputStream, GIOPVersion.V1_1);
        } else {
            ServiceContexts.writeNullServiceContext((org.omg.CORBA_2_3.portable.OutputStream) outputStream);
        }
        outputStream.write_ulong(this.request_id);
        outputStream.write_long(this.reply_status);
    }

    public static void isValidReplyStatus(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
            case 3:
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
