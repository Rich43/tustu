package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.encoding.CDROutputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import java.io.IOException;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Principal;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/RequestMessage_1_2.class */
public final class RequestMessage_1_2 extends Message_1_2 implements RequestMessage {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private byte response_flags;
    private byte[] reserved;
    private TargetAddress target;
    private String operation;
    private ServiceContexts service_contexts;
    private ObjectKey objectKey;

    RequestMessage_1_2(ORB orb) {
        this.orb = null;
        this.wrapper = null;
        this.response_flags = (byte) 0;
        this.reserved = null;
        this.target = null;
        this.operation = null;
        this.service_contexts = null;
        this.objectKey = null;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    RequestMessage_1_2(ORB orb, int i2, byte b2, byte[] bArr, TargetAddress targetAddress, String str, ServiceContexts serviceContexts) {
        super(Message.GIOPBigMagic, GIOPVersion.V1_2, (byte) 0, (byte) 0, 0);
        this.orb = null;
        this.wrapper = null;
        this.response_flags = (byte) 0;
        this.reserved = null;
        this.target = null;
        this.operation = null;
        this.service_contexts = null;
        this.objectKey = null;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.request_id = i2;
        this.response_flags = b2;
        this.reserved = bArr;
        this.target = targetAddress;
        this.operation = str;
        this.service_contexts = serviceContexts;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public int getRequestId() {
        return this.request_id;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public boolean isResponseExpected() {
        if ((this.response_flags & 1) == 1) {
            return true;
        }
        return false;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public byte[] getReserved() {
        return this.reserved;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public ObjectKey getObjectKey() {
        if (this.objectKey == null) {
            this.objectKey = MessageBase.extractObjectKey(this.target, this.orb);
        }
        return this.objectKey;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public String getOperation() {
        return this.operation;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public Principal getPrincipal() {
        return null;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public ServiceContexts getServiceContexts() {
        return this.service_contexts;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
        this.request_id = inputStream.read_ulong();
        this.response_flags = inputStream.read_octet();
        this.reserved = new byte[3];
        for (int i2 = 0; i2 < 3; i2++) {
            this.reserved[i2] = inputStream.read_octet();
        }
        this.target = TargetAddressHelper.read(inputStream);
        getObjectKey();
        this.operation = inputStream.read_string();
        this.service_contexts = new ServiceContexts((org.omg.CORBA_2_3.portable.InputStream) inputStream);
        ((CDRInputStream) inputStream).setHeaderPadding(true);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_2, com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_1, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) throws SystemException {
        super.write(outputStream);
        outputStream.write_ulong(this.request_id);
        outputStream.write_octet(this.response_flags);
        nullCheck(this.reserved);
        if (this.reserved.length != 3) {
            throw this.wrapper.badReservedLength(CompletionStatus.COMPLETED_MAYBE);
        }
        for (int i2 = 0; i2 < 3; i2++) {
            outputStream.write_octet(this.reserved[i2]);
        }
        nullCheck(this.target);
        TargetAddressHelper.write(outputStream, this.target);
        outputStream.write_string(this.operation);
        if (this.service_contexts != null) {
            this.service_contexts.write((org.omg.CORBA_2_3.portable.OutputStream) outputStream, GIOPVersion.V1_2);
        } else {
            ServiceContexts.writeNullServiceContext((org.omg.CORBA_2_3.portable.OutputStream) outputStream);
        }
        ((CDROutputStream) outputStream).setHeaderPadding(true);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void callback(MessageHandler messageHandler) throws IOException {
        messageHandler.handleInput(this);
    }
}
