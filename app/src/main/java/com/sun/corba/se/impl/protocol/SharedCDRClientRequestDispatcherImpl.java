package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.encoding.ByteBufferWithInfo;
import com.sun.corba.se.impl.encoding.CDRInputObject;
import com.sun.corba.se.impl.encoding.CDROutputObject;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/SharedCDRClientRequestDispatcherImpl.class */
public class SharedCDRClientRequestDispatcherImpl extends CorbaClientRequestDispatcherImpl {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v27, types: [com.sun.corba.se.impl.encoding.CDRInputObject, com.sun.corba.se.pept.encoding.InputObject] */
    /* JADX WARN: Type inference failed for: r0v31, types: [com.sun.corba.se.impl.protocol.CorbaMessageMediatorImpl] */
    /* JADX WARN: Type inference failed for: r0v49, types: [com.sun.corba.se.impl.encoding.CDRInputObject, com.sun.corba.se.pept.encoding.InputObject] */
    /* JADX WARN: Type inference failed for: r11v0 */
    /* JADX WARN: Type inference failed for: r11v1 */
    /* JADX WARN: Type inference failed for: r11v2, types: [com.sun.corba.se.pept.protocol.MessageMediator, com.sun.corba.se.spi.protocol.CorbaMessageMediator] */
    /* JADX WARN: Type inference failed for: r7v0, types: [com.sun.corba.se.impl.protocol.SharedCDRClientRequestDispatcherImpl] */
    @Override // com.sun.corba.se.impl.protocol.CorbaClientRequestDispatcherImpl, com.sun.corba.se.pept.protocol.ClientRequestDispatcher
    public InputObject marshalingComplete(Object obj, OutputObject outputObject) throws ApplicationException, RemarshalException {
        final ORB orb = null;
        CorbaMessageMediator corbaMessageMediator = 0;
        try {
            corbaMessageMediator = (CorbaMessageMediator) outputObject.getMessageMediator();
            orb = (ORB) corbaMessageMediator.getBroker();
            if (orb.subcontractDebugFlag) {
                dprint(".marshalingComplete->: " + opAndId(corbaMessageMediator));
            }
            CDROutputObject cDROutputObject = (CDROutputObject) outputObject;
            ByteBufferWithInfo byteBufferWithInfo = cDROutputObject.getByteBufferWithInfo();
            cDROutputObject.getMessageHeader().setSize(byteBufferWithInfo.byteBuffer, byteBufferWithInfo.getSize());
            final ByteBuffer byteBuffer = byteBufferWithInfo.byteBuffer;
            final Message messageHeader = cDROutputObject.getMessageHeader();
            ?? r0 = (CDRInputObject) AccessController.doPrivileged(new PrivilegedAction<CDRInputObject>() { // from class: com.sun.corba.se.impl.protocol.SharedCDRClientRequestDispatcherImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public CDRInputObject run2() {
                    return new CDRInputObject(orb, null, byteBuffer, messageHeader);
                }
            });
            corbaMessageMediator.setInputObject(r0);
            r0.setMessageMediator(corbaMessageMediator);
            ((CorbaMessageMediatorImpl) corbaMessageMediator).handleRequestRequest(corbaMessageMediator);
            try {
                r0.close();
            } catch (IOException e2) {
                if (orb.transportDebugFlag) {
                    dprint(".marshalingComplete: ignoring IOException - " + e2.toString());
                }
            }
            CDROutputObject cDROutputObject2 = (CDROutputObject) corbaMessageMediator.getOutputObject();
            ByteBufferWithInfo byteBufferWithInfo2 = cDROutputObject2.getByteBufferWithInfo();
            cDROutputObject2.getMessageHeader().setSize(byteBufferWithInfo2.byteBuffer, byteBufferWithInfo2.getSize());
            final ByteBuffer byteBuffer2 = byteBufferWithInfo2.byteBuffer;
            final Message messageHeader2 = cDROutputObject2.getMessageHeader();
            ?? r02 = (CDRInputObject) AccessController.doPrivileged(new PrivilegedAction<CDRInputObject>() { // from class: com.sun.corba.se.impl.protocol.SharedCDRClientRequestDispatcherImpl.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public CDRInputObject run2() {
                    return new CDRInputObject(orb, null, byteBuffer2, messageHeader2);
                }
            });
            corbaMessageMediator.setInputObject(r02);
            r02.setMessageMediator(corbaMessageMediator);
            r02.unmarshalHeader();
            InputObject inputObjectProcessResponse = processResponse(orb, corbaMessageMediator, r02);
            if (orb.subcontractDebugFlag) {
                dprint(".marshalingComplete<-: " + opAndId(corbaMessageMediator));
            }
            return inputObjectProcessResponse;
        } catch (Throwable th) {
            if (orb.subcontractDebugFlag) {
                dprint(".marshalingComplete<-: " + opAndId(corbaMessageMediator));
            }
            throw th;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.CorbaClientRequestDispatcherImpl
    protected void dprint(String str) {
        ORBUtility.dprint("SharedCDRClientRequestDispatcherImpl", str);
    }
}
