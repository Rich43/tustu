package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.impl.corba.RequestImpl;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import java.io.Closeable;
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

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/PIHandler.class */
public interface PIHandler extends Closeable {
    void initialize();

    void destroyInterceptors();

    void objectAdapterCreated(ObjectAdapter objectAdapter);

    void adapterManagerStateChanged(int i2, short s2);

    void adapterStateChanged(ObjectReferenceTemplate[] objectReferenceTemplateArr, short s2);

    void disableInterceptorsThisThread();

    void enableInterceptorsThisThread();

    void invokeClientPIStartingPoint() throws RemarshalException;

    Exception invokeClientPIEndingPoint(int i2, Exception exc);

    Exception makeCompletedClientRequest(int i2, Exception exc);

    void initiateClientPIRequest(boolean z2);

    void cleanupClientPIRequest();

    void setClientPIInfo(RequestImpl requestImpl);

    void setClientPIInfo(CorbaMessageMediator corbaMessageMediator);

    void invokeServerPIStartingPoint();

    void invokeServerPIIntermediatePoint();

    void invokeServerPIEndingPoint(ReplyMessage replyMessage);

    void initializeServerPIInfo(CorbaMessageMediator corbaMessageMediator, ObjectAdapter objectAdapter, byte[] bArr, ObjectKeyTemplate objectKeyTemplate);

    void setServerPIInfo(Object obj, String str);

    void setServerPIInfo(Exception exc);

    void setServerPIInfo(NVList nVList);

    void setServerPIExceptionInfo(Any any);

    void setServerPIInfo(Any any);

    void cleanupServerPIRequest();

    Policy create_policy(int i2, Any any) throws PolicyError;

    void register_interceptor(Interceptor interceptor, int i2) throws DuplicateName;

    Current getPICurrent();

    void registerPolicyFactory(int i2, PolicyFactory policyFactory);

    int allocateServerRequestId();
}
