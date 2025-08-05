package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.legacy.connection.Connection;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import java.util.ArrayList;
import java.util.HashMap;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.NVList;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.TypeCode;
import org.omg.Dynamic.Parameter;
import org.omg.IOP.ServiceContext;
import org.omg.PortableInterceptor.InvalidSlot;
import org.omg.PortableInterceptor.ServerRequestInfo;
import org.omg.PortableServer.DynamicImplementation;
import org.omg.PortableServer.Servant;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/ServerRequestInfoImpl.class */
public final class ServerRequestInfoImpl extends RequestInfoImpl implements ServerRequestInfo {
    static final int CALL_RECEIVE_REQUEST_SERVICE_CONTEXT = 0;
    static final int CALL_RECEIVE_REQUEST = 0;
    static final int CALL_INTERMEDIATE_NONE = 1;
    static final int CALL_SEND_REPLY = 0;
    static final int CALL_SEND_EXCEPTION = 1;
    static final int CALL_SEND_OTHER = 2;
    private boolean forwardRequestRaisedInEnding;
    private CorbaMessageMediator request;
    private Object servant;
    private byte[] objectId;
    private ObjectKeyTemplate oktemp;
    private byte[] adapterId;
    private String[] adapterName;
    private ArrayList addReplyServiceContextQueue;
    private ReplyMessage replyMessage;
    private String targetMostDerivedInterface;
    private NVList dsiArguments;
    private Any dsiResult;
    private Any dsiException;
    private boolean isDynamic;
    private ObjectAdapter objectAdapter;
    private int serverRequestId;
    private Parameter[] cachedArguments;
    private Any cachedSendingException;
    private HashMap cachedRequestServiceContexts;
    private HashMap cachedReplyServiceContexts;
    protected static final int MID_SENDING_EXCEPTION = 14;
    protected static final int MID_OBJECT_ID = 15;
    protected static final int MID_ADAPTER_ID = 16;
    protected static final int MID_TARGET_MOST_DERIVED_INTERFACE = 17;
    protected static final int MID_GET_SERVER_POLICY = 18;
    protected static final int MID_SET_SLOT = 19;
    protected static final int MID_TARGET_IS_A = 20;
    protected static final int MID_ADD_REPLY_SERVICE_CONTEXT = 21;
    protected static final int MID_SERVER_ID = 22;
    protected static final int MID_ORB_ID = 23;
    protected static final int MID_ADAPTER_NAME = 24;
    private static final boolean[][] validCall = {new boolean[]{true, true, true, true, true}, new boolean[]{true, true, true, true, true}, new boolean[]{false, true, true, false, false}, new boolean[]{false, true, true, true, true}, new boolean[]{false, true, true, true, true}, new boolean[]{false, true, true, false, false}, new boolean[]{false, false, true, false, false}, new boolean[]{true, true, true, true, true}, new boolean[]{true, true, true, true, true}, new boolean[]{false, false, true, true, true}, new boolean[]{false, false, false, false, true}, new boolean[]{true, true, true, true, true}, new boolean[]{true, true, true, true, true}, new boolean[]{false, false, true, true, true}, new boolean[]{false, false, false, true, false}, new boolean[]{false, true, true, true, true}, new boolean[]{false, true, true, true, true}, new boolean[]{false, true, false, false, false}, new boolean[]{true, true, true, true, true}, new boolean[]{true, true, true, true, true}, new boolean[]{false, true, false, false, false}, new boolean[]{true, true, true, true, true}, new boolean[]{false, true, true, true, true}, new boolean[]{false, true, true, true, true}, new boolean[]{false, true, true, true, true}};

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl
    void reset() {
        super.reset();
        this.forwardRequestRaisedInEnding = false;
        this.request = null;
        this.servant = null;
        this.objectId = null;
        this.oktemp = null;
        this.adapterId = null;
        this.adapterName = null;
        this.addReplyServiceContextQueue = null;
        this.replyMessage = null;
        this.targetMostDerivedInterface = null;
        this.dsiArguments = null;
        this.dsiResult = null;
        this.dsiException = null;
        this.isDynamic = false;
        this.objectAdapter = null;
        this.serverRequestId = this.myORB.getPIHandler().allocateServerRequestId();
        this.cachedArguments = null;
        this.cachedSendingException = null;
        this.cachedRequestServiceContexts = null;
        this.cachedReplyServiceContexts = null;
        this.startingPointCall = 0;
        this.intermediatePointCall = 0;
        this.endingPointCall = 0;
    }

    ServerRequestInfoImpl(ORB orb) {
        super(orb);
        this.startingPointCall = 0;
        this.intermediatePointCall = 0;
        this.endingPointCall = 0;
        this.serverRequestId = orb.getPIHandler().allocateServerRequestId();
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public Any sending_exception() {
        Any anyExceptionToAny;
        checkAccess(14);
        if (this.cachedSendingException == null) {
            if (this.dsiException != null) {
                anyExceptionToAny = this.dsiException;
            } else if (this.exception != null) {
                anyExceptionToAny = exceptionToAny(this.exception);
            } else {
                throw this.wrapper.exceptionUnavailable();
            }
            this.cachedSendingException = anyExceptionToAny;
        }
        return this.cachedSendingException;
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public byte[] object_id() {
        checkAccess(15);
        if (this.objectId == null) {
            throw this.stdWrapper.piOperationNotSupported6();
        }
        return this.objectId;
    }

    private void checkForNullTemplate() {
        if (this.oktemp == null) {
            throw this.stdWrapper.piOperationNotSupported7();
        }
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public String server_id() {
        checkAccess(22);
        checkForNullTemplate();
        return Integer.toString(this.oktemp.getServerId());
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public String orb_id() {
        checkAccess(23);
        return this.myORB.getORBData().getORBId();
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public synchronized String[] adapter_name() {
        checkAccess(24);
        if (this.adapterName == null) {
            checkForNullTemplate();
            this.adapterName = this.oktemp.getObjectAdapterId().getAdapterName();
        }
        return this.adapterName;
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public synchronized byte[] adapter_id() {
        checkAccess(16);
        if (this.adapterId == null) {
            checkForNullTemplate();
            this.adapterId = this.oktemp.getAdapterId();
        }
        return this.adapterId;
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public String target_most_derived_interface() {
        checkAccess(17);
        return this.targetMostDerivedInterface;
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public Policy get_server_policy(int i2) {
        Policy effectivePolicy = null;
        if (this.objectAdapter != null) {
            effectivePolicy = this.objectAdapter.getEffectivePolicy(i2);
        }
        return effectivePolicy;
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public void set_slot(int i2, Any any) throws InvalidSlot {
        this.slotTable.set_slot(i2, any);
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public boolean target_is_a(String str) {
        boolean z_is_a;
        checkAccess(20);
        if (this.servant instanceof Servant) {
            z_is_a = ((Servant) this.servant)._is_a(str);
        } else if (StubAdapter.isStub(this.servant)) {
            z_is_a = ((Object) this.servant)._is_a(str);
        } else {
            throw this.wrapper.servantInvalid();
        }
        return z_is_a;
    }

    @Override // org.omg.PortableInterceptor.ServerRequestInfoOperations
    public void add_reply_service_context(ServiceContext serviceContext, boolean z2) {
        if (this.currentExecutionPoint == 2) {
            ServiceContexts serviceContexts = this.replyMessage.getServiceContexts();
            if (serviceContexts == null) {
                serviceContexts = new ServiceContexts(this.myORB);
                this.replyMessage.setServiceContexts(serviceContexts);
            }
            if (this.cachedReplyServiceContexts == null) {
                this.cachedReplyServiceContexts = new HashMap();
            }
            addServiceContext(this.cachedReplyServiceContexts, serviceContexts, serviceContext, z2);
        }
        AddReplyServiceContextCommand addReplyServiceContextCommand = new AddReplyServiceContextCommand();
        addReplyServiceContextCommand.service_context = serviceContext;
        addReplyServiceContextCommand.replace = z2;
        if (this.addReplyServiceContextQueue == null) {
            this.addReplyServiceContextQueue = new ArrayList();
        }
        enqueue(addReplyServiceContextCommand);
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public int request_id() {
        return this.serverRequestId;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public String operation() {
        return this.request.getOperationName();
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public Parameter[] arguments() {
        checkAccess(2);
        if (this.cachedArguments == null) {
            if (!this.isDynamic) {
                throw this.stdWrapper.piOperationNotSupported1();
            }
            if (this.dsiArguments == null) {
                throw this.stdWrapper.piOperationNotSupported8();
            }
            this.cachedArguments = nvListToParameterArray(this.dsiArguments);
        }
        return this.cachedArguments;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public TypeCode[] exceptions() {
        checkAccess(3);
        throw this.stdWrapper.piOperationNotSupported2();
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public String[] contexts() {
        checkAccess(4);
        throw this.stdWrapper.piOperationNotSupported3();
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public String[] operation_context() {
        checkAccess(5);
        throw this.stdWrapper.piOperationNotSupported4();
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public Any result() {
        checkAccess(6);
        if (!this.isDynamic) {
            throw this.stdWrapper.piOperationNotSupported5();
        }
        if (this.dsiResult == null) {
            throw this.wrapper.piDsiResultIsNull();
        }
        return this.dsiResult;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public boolean response_expected() {
        return !this.request.isOneWay();
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public Object forward_reference() {
        checkAccess(10);
        if (this.replyStatus != 3) {
            throw this.stdWrapper.invalidPiCall1();
        }
        return getForwardRequestException().forward;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public ServiceContext get_request_service_context(int i2) {
        checkAccess(12);
        if (this.cachedRequestServiceContexts == null) {
            this.cachedRequestServiceContexts = new HashMap();
        }
        return getServiceContext(this.cachedRequestServiceContexts, this.request.getRequestServiceContexts(), i2);
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public ServiceContext get_reply_service_context(int i2) {
        checkAccess(13);
        if (this.cachedReplyServiceContexts == null) {
            this.cachedReplyServiceContexts = new HashMap();
        }
        return getServiceContext(this.cachedReplyServiceContexts, this.replyMessage.getServiceContexts(), i2);
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/ServerRequestInfoImpl$AddReplyServiceContextCommand.class */
    private class AddReplyServiceContextCommand {
        ServiceContext service_context;
        boolean replace;

        private AddReplyServiceContextCommand() {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0068, code lost:
    
        if (r8 != false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x006b, code lost:
    
        r5.addReplyServiceContextQueue.add(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0074, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void enqueue(com.sun.corba.se.impl.interceptors.ServerRequestInfoImpl.AddReplyServiceContextCommand r6) {
        /*
            r5 = this;
            r0 = r5
            java.util.ArrayList r0 = r0.addReplyServiceContextQueue
            int r0 = r0.size()
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = 0
            r9 = r0
        Ld:
            r0 = r9
            r1 = r7
            if (r0 >= r1) goto L67
            r0 = r5
            java.util.ArrayList r0 = r0.addReplyServiceContextQueue
            r1 = r9
            java.lang.Object r0 = r0.get(r1)
            com.sun.corba.se.impl.interceptors.ServerRequestInfoImpl$AddReplyServiceContextCommand r0 = (com.sun.corba.se.impl.interceptors.ServerRequestInfoImpl.AddReplyServiceContextCommand) r0
            r10 = r0
            r0 = r10
            org.omg.IOP.ServiceContext r0 = r0.service_context
            int r0 = r0.context_id
            r1 = r6
            org.omg.IOP.ServiceContext r1 = r1.service_context
            int r1 = r1.context_id
            if (r0 != r1) goto L61
            r0 = 1
            r8 = r0
            r0 = r6
            boolean r0 = r0.replace
            if (r0 == 0) goto L4a
            r0 = r5
            java.util.ArrayList r0 = r0.addReplyServiceContextQueue
            r1 = r9
            r2 = r6
            java.lang.Object r0 = r0.set(r1, r2)
            goto L67
        L4a:
            r0 = r5
            com.sun.corba.se.impl.logging.OMGSystemException r0 = r0.stdWrapper
            java.lang.Integer r1 = new java.lang.Integer
            r2 = r1
            r3 = r10
            org.omg.IOP.ServiceContext r3 = r3.service_context
            int r3 = r3.context_id
            r2.<init>(r3)
            org.omg.CORBA.BAD_INV_ORDER r0 = r0.serviceContextAddFailed(r1)
            throw r0
        L61:
            int r9 = r9 + 1
            goto Ld
        L67:
            r0 = r8
            if (r0 != 0) goto L74
            r0 = r5
            java.util.ArrayList r0 = r0.addReplyServiceContextQueue
            r1 = r6
            boolean r0 = r0.add(r1)
        L74:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.interceptors.ServerRequestInfoImpl.enqueue(com.sun.corba.se.impl.interceptors.ServerRequestInfoImpl$AddReplyServiceContextCommand):void");
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl
    protected void setCurrentExecutionPoint(int i2) {
        super.setCurrentExecutionPoint(i2);
        if (i2 == 2 && this.addReplyServiceContextQueue != null) {
            int size = this.addReplyServiceContextQueue.size();
            for (int i3 = 0; i3 < size; i3++) {
                AddReplyServiceContextCommand addReplyServiceContextCommand = (AddReplyServiceContextCommand) this.addReplyServiceContextQueue.get(i3);
                try {
                    add_reply_service_context(addReplyServiceContextCommand.service_context, addReplyServiceContextCommand.replace);
                } catch (BAD_INV_ORDER e2) {
                }
            }
        }
    }

    protected void setInfo(CorbaMessageMediator corbaMessageMediator, ObjectAdapter objectAdapter, byte[] bArr, ObjectKeyTemplate objectKeyTemplate) {
        this.request = corbaMessageMediator;
        this.objectId = bArr;
        this.oktemp = objectKeyTemplate;
        this.objectAdapter = objectAdapter;
        this.connection = (Connection) corbaMessageMediator.getConnection();
    }

    protected void setDSIArguments(NVList nVList) {
        this.dsiArguments = nVList;
    }

    protected void setDSIException(Any any) {
        this.dsiException = any;
        this.cachedSendingException = null;
    }

    protected void setDSIResult(Any any) {
        this.dsiResult = any;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl
    protected void setException(Exception exc) {
        super.setException(exc);
        this.dsiException = null;
        this.cachedSendingException = null;
    }

    protected void setInfo(Object obj, String str) {
        this.servant = obj;
        this.targetMostDerivedInterface = str;
        this.isDynamic = (obj instanceof DynamicImplementation) || (obj instanceof org.omg.CORBA.DynamicImplementation);
    }

    void setReplyMessage(ReplyMessage replyMessage) {
        this.replyMessage = replyMessage;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl
    protected void setReplyStatus(short s2) {
        super.setReplyStatus(s2);
        switch (s2) {
            case 0:
                this.endingPointCall = 0;
                break;
            case 1:
            case 2:
                this.endingPointCall = 1;
                break;
            case 3:
            case 4:
                this.endingPointCall = 2;
                break;
        }
    }

    void releaseServant() {
        this.servant = null;
    }

    void setForwardRequestRaisedInEnding() {
        this.forwardRequestRaisedInEnding = true;
    }

    boolean isForwardRequestRaisedInEnding() {
        return this.forwardRequestRaisedInEnding;
    }

    boolean isDynamic() {
        return this.isDynamic;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl
    protected void checkAccess(int i2) {
        boolean z2 = false;
        boolean z3 = z2;
        switch (this.currentExecutionPoint) {
            case 0:
                z3 = false;
                break;
            case 1:
                z3 = true;
                break;
            case 2:
                z3 = z2;
                switch (this.endingPointCall) {
                    case 0:
                        z3 = 2;
                        break;
                    case 1:
                        z3 = 3;
                        break;
                    case 2:
                        z3 = 4;
                        break;
                }
        }
        if (!validCall[i2][z3 ? 1 : 0]) {
            throw this.stdWrapper.invalidPiCall2();
        }
    }
}
