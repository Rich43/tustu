package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import java.io.IOException;
import org.omg.CORBA.Principal;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/RequestMessage_1_0.class */
public final class RequestMessage_1_0 extends Message_1_0 implements RequestMessage {
    private ORB orb;
    private ServiceContexts service_contexts;
    private int request_id;
    private boolean response_expected;
    private byte[] object_key;
    private String operation;
    private Principal requesting_principal;
    private ObjectKey objectKey;

    RequestMessage_1_0(ORB orb) {
        this.orb = null;
        this.service_contexts = null;
        this.request_id = 0;
        this.response_expected = false;
        this.object_key = null;
        this.operation = null;
        this.requesting_principal = null;
        this.objectKey = null;
        this.orb = orb;
    }

    RequestMessage_1_0(ORB orb, ServiceContexts serviceContexts, int i2, boolean z2, byte[] bArr, String str, Principal principal) {
        super(Message.GIOPBigMagic, false, (byte) 0, 0);
        this.orb = null;
        this.service_contexts = null;
        this.request_id = 0;
        this.response_expected = false;
        this.object_key = null;
        this.operation = null;
        this.requesting_principal = null;
        this.objectKey = null;
        this.orb = orb;
        this.service_contexts = serviceContexts;
        this.request_id = i2;
        this.response_expected = z2;
        this.object_key = bArr;
        this.operation = str;
        this.requesting_principal = principal;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public ServiceContexts getServiceContexts() {
        return this.service_contexts;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public int getRequestId() {
        return this.request_id;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public boolean isResponseExpected() {
        return this.response_expected;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public byte[] getReserved() {
        return null;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public ObjectKey getObjectKey() {
        if (this.objectKey == null) {
            this.objectKey = MessageBase.extractObjectKey(this.object_key, this.orb);
        }
        return this.objectKey;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public String getOperation() {
        return this.operation;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public Principal getPrincipal() {
        return this.requesting_principal;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
    public void setThreadPoolToUse(int i2) {
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_0, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void read(InputStream inputStream) {
        super.read(inputStream);
        this.service_contexts = new ServiceContexts((org.omg.CORBA_2_3.portable.InputStream) inputStream);
        this.request_id = inputStream.read_ulong();
        this.response_expected = inputStream.read_boolean();
        int i2 = inputStream.read_long();
        this.object_key = new byte[i2];
        inputStream.read_octet_array(this.object_key, 0, i2);
        this.operation = inputStream.read_string();
        this.requesting_principal = inputStream.read_Principal();
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message_1_0, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void write(OutputStream outputStream) throws SystemException {
        super.write(outputStream);
        if (this.service_contexts != null) {
            this.service_contexts.write((org.omg.CORBA_2_3.portable.OutputStream) outputStream, GIOPVersion.V1_0);
        } else {
            ServiceContexts.writeNullServiceContext((org.omg.CORBA_2_3.portable.OutputStream) outputStream);
        }
        outputStream.write_ulong(this.request_id);
        outputStream.write_boolean(this.response_expected);
        nullCheck(this.object_key);
        outputStream.write_long(this.object_key.length);
        outputStream.write_octet_array(this.object_key, 0, this.object_key.length);
        outputStream.write_string(this.operation);
        if (this.requesting_principal != null) {
            outputStream.write_Principal(this.requesting_principal);
        } else {
            outputStream.write_long(0);
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase, com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void callback(MessageHandler messageHandler) throws IOException {
        messageHandler.handleInput(this);
    }
}
