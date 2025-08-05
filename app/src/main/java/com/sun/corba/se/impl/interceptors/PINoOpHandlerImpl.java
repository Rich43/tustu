package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.corba.RequestImpl;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.protocol.PIHandler;
import org.omg.CORBA.Any;
import org.omg.CORBA.NVList;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.PortableInterceptor.Current;
import org.omg.PortableInterceptor.Interceptor;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;
import org.omg.PortableInterceptor.PolicyFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/PINoOpHandlerImpl.class */
public class PINoOpHandlerImpl implements PIHandler {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void initialize() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void destroyInterceptors() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void objectAdapterCreated(ObjectAdapter objectAdapter) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void adapterManagerStateChanged(int i2, short s2) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void adapterStateChanged(ObjectReferenceTemplate[] objectReferenceTemplateArr, short s2) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void disableInterceptorsThisThread() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void enableInterceptorsThisThread() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void invokeClientPIStartingPoint() throws RemarshalException {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public Exception invokeClientPIEndingPoint(int i2, Exception exc) {
        return null;
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public Exception makeCompletedClientRequest(int i2, Exception exc) {
        return null;
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void initiateClientPIRequest(boolean z2) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void cleanupClientPIRequest() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setClientPIInfo(CorbaMessageMediator corbaMessageMediator) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setClientPIInfo(RequestImpl requestImpl) {
    }

    public final void sendCancelRequestIfFinalFragmentNotSent() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void invokeServerPIStartingPoint() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void invokeServerPIIntermediatePoint() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void invokeServerPIEndingPoint(ReplyMessage replyMessage) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIInfo(Exception exc) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIInfo(NVList nVList) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIExceptionInfo(Any any) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIInfo(Any any) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void initializeServerPIInfo(CorbaMessageMediator corbaMessageMediator, ObjectAdapter objectAdapter, byte[] bArr, ObjectKeyTemplate objectKeyTemplate) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIInfo(Object obj, String str) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void cleanupServerPIRequest() {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void register_interceptor(Interceptor interceptor, int i2) throws DuplicateName {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public Current getPICurrent() {
        return null;
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public Policy create_policy(int i2, Any any) throws PolicyError {
        return null;
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void registerPolicyFactory(int i2, PolicyFactory policyFactory) {
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public int allocateServerRequestId() {
        return 0;
    }
}
