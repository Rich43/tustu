package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.CorbaInvocationInfo;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.legacy.connection.Connection;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.protocol.RetryType;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import com.sun.corba.se.spi.transport.CorbaContactInfoListIterator;
import java.util.HashMap;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.Dynamic.Parameter;
import org.omg.IOP.ServiceContext;
import org.omg.IOP.TaggedComponent;
import org.omg.IOP.TaggedProfile;
import org.omg.PortableInterceptor.ClientRequestInfo;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/ClientRequestInfoImpl.class */
public final class ClientRequestInfoImpl extends RequestInfoImpl implements ClientRequestInfo {
    static final int CALL_SEND_REQUEST = 0;
    static final int CALL_SEND_POLL = 1;
    static final int CALL_RECEIVE_REPLY = 0;
    static final int CALL_RECEIVE_EXCEPTION = 1;
    static final int CALL_RECEIVE_OTHER = 2;
    private RetryType retryRequest;
    private int entryCount;
    private Request request;
    private boolean diiInitiate;
    private CorbaMessageMediator messageMediator;
    private Object cachedTargetObject;
    private Object cachedEffectiveTargetObject;
    private Parameter[] cachedArguments;
    private TypeCode[] cachedExceptions;
    private String[] cachedContexts;
    private String[] cachedOperationContext;
    private String cachedReceivedExceptionId;
    private Any cachedResult;
    private Any cachedReceivedException;
    private TaggedProfile cachedEffectiveProfile;
    private HashMap cachedRequestServiceContexts;
    private HashMap cachedReplyServiceContexts;
    private HashMap cachedEffectiveComponents;
    protected boolean piCurrentPushed;
    protected static final int MID_TARGET = 14;
    protected static final int MID_EFFECTIVE_TARGET = 15;
    protected static final int MID_EFFECTIVE_PROFILE = 16;
    protected static final int MID_RECEIVED_EXCEPTION = 17;
    protected static final int MID_RECEIVED_EXCEPTION_ID = 18;
    protected static final int MID_GET_EFFECTIVE_COMPONENT = 19;
    protected static final int MID_GET_EFFECTIVE_COMPONENTS = 20;
    protected static final int MID_GET_REQUEST_POLICY = 21;
    protected static final int MID_ADD_REQUEST_SERVICE_CONTEXT = 22;
    private static final boolean[][] validCall = {new boolean[]{true, true, true, true, true}, new boolean[]{true, true, true, true, true}, new boolean[]{true, false, true, false, false}, new boolean[]{true, false, true, true, true}, new boolean[]{true, false, true, true, true}, new boolean[]{true, false, true, true, true}, new boolean[]{false, false, true, false, false}, new boolean[]{true, true, true, true, true}, new boolean[]{true, false, true, true, true}, new boolean[]{false, false, true, true, true}, new boolean[]{false, false, false, false, true}, new boolean[]{true, true, true, true, true}, new boolean[]{true, false, true, true, true}, new boolean[]{false, false, true, true, true}, new boolean[]{true, true, true, true, true}, new boolean[]{true, true, true, true, true}, new boolean[]{true, true, true, true, true}, new boolean[]{false, false, false, true, false}, new boolean[]{false, false, false, true, false}, new boolean[]{true, false, true, true, true}, new boolean[]{true, false, true, true, true}, new boolean[]{true, false, true, true, true}, new boolean[]{true, false, false, false, false}};

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl
    void reset() {
        super.reset();
        this.retryRequest = RetryType.NONE;
        this.request = null;
        this.diiInitiate = false;
        this.messageMediator = null;
        this.cachedTargetObject = null;
        this.cachedEffectiveTargetObject = null;
        this.cachedArguments = null;
        this.cachedExceptions = null;
        this.cachedContexts = null;
        this.cachedOperationContext = null;
        this.cachedReceivedExceptionId = null;
        this.cachedResult = null;
        this.cachedReceivedException = null;
        this.cachedEffectiveProfile = null;
        this.cachedRequestServiceContexts = null;
        this.cachedReplyServiceContexts = null;
        this.cachedEffectiveComponents = null;
        this.piCurrentPushed = false;
        this.startingPointCall = 0;
        this.endingPointCall = 0;
    }

    protected ClientRequestInfoImpl(ORB orb) {
        super(orb);
        this.entryCount = 0;
        this.startingPointCall = 0;
        this.endingPointCall = 0;
    }

    @Override // org.omg.PortableInterceptor.ClientRequestInfoOperations
    public Object target() {
        if (this.cachedTargetObject == null) {
            this.cachedTargetObject = iorToObject(((CorbaContactInfo) this.messageMediator.getContactInfo()).getTargetIOR());
        }
        return this.cachedTargetObject;
    }

    @Override // org.omg.PortableInterceptor.ClientRequestInfoOperations
    public Object effective_target() {
        if (this.cachedEffectiveTargetObject == null) {
            this.cachedEffectiveTargetObject = iorToObject(((CorbaContactInfo) this.messageMediator.getContactInfo()).getEffectiveTargetIOR());
        }
        return this.cachedEffectiveTargetObject;
    }

    @Override // org.omg.PortableInterceptor.ClientRequestInfoOperations
    public TaggedProfile effective_profile() {
        if (this.cachedEffectiveProfile == null) {
            this.cachedEffectiveProfile = ((CorbaContactInfo) this.messageMediator.getContactInfo()).getEffectiveProfile().getIOPProfile();
        }
        return this.cachedEffectiveProfile;
    }

    @Override // org.omg.PortableInterceptor.ClientRequestInfoOperations
    public Any received_exception() throws BAD_INV_ORDER {
        checkAccess(17);
        if (this.cachedReceivedException == null) {
            this.cachedReceivedException = exceptionToAny(this.exception);
        }
        return this.cachedReceivedException;
    }

    @Override // org.omg.PortableInterceptor.ClientRequestInfoOperations
    public String received_exception_id() throws BAD_INV_ORDER {
        checkAccess(18);
        if (this.cachedReceivedExceptionId == null) {
            String id = null;
            if (this.exception == null) {
                throw this.wrapper.exceptionWasNull();
            }
            if (this.exception instanceof SystemException) {
                id = ORBUtility.repositoryIdOf(this.exception.getClass().getName());
            } else if (this.exception instanceof ApplicationException) {
                id = ((ApplicationException) this.exception).getId();
            }
            this.cachedReceivedExceptionId = id;
        }
        return this.cachedReceivedExceptionId;
    }

    @Override // org.omg.PortableInterceptor.ClientRequestInfoOperations
    public TaggedComponent get_effective_component(int i2) throws BAD_INV_ORDER {
        checkAccess(19);
        return get_effective_components(i2)[0];
    }

    @Override // org.omg.PortableInterceptor.ClientRequestInfoOperations
    public TaggedComponent[] get_effective_components(int i2) throws BAD_INV_ORDER {
        checkAccess(20);
        Integer num = new Integer(i2);
        TaggedComponent[] iOPComponents = null;
        boolean z2 = false;
        if (this.cachedEffectiveComponents == null) {
            this.cachedEffectiveComponents = new HashMap();
            z2 = true;
        } else {
            iOPComponents = (TaggedComponent[]) this.cachedEffectiveComponents.get(num);
        }
        if (iOPComponents == null && (z2 || !this.cachedEffectiveComponents.containsKey(num))) {
            iOPComponents = ((IIOPProfileTemplate) ((CorbaContactInfo) this.messageMediator.getContactInfo()).getEffectiveProfile().getTaggedProfileTemplate()).getIOPComponents(this.myORB, i2);
            this.cachedEffectiveComponents.put(num, iOPComponents);
        }
        if (iOPComponents == null || iOPComponents.length == 0) {
            throw this.stdWrapper.invalidComponentId(num);
        }
        return iOPComponents;
    }

    @Override // org.omg.PortableInterceptor.ClientRequestInfoOperations
    public Policy get_request_policy(int i2) throws BAD_INV_ORDER {
        checkAccess(21);
        throw this.wrapper.piOrbNotPolicyBased();
    }

    @Override // org.omg.PortableInterceptor.ClientRequestInfoOperations
    public void add_request_service_context(ServiceContext serviceContext, boolean z2) throws BAD_INV_ORDER {
        checkAccess(22);
        if (this.cachedRequestServiceContexts == null) {
            this.cachedRequestServiceContexts = new HashMap();
        }
        addServiceContext(this.cachedRequestServiceContexts, this.messageMediator.getRequestServiceContexts(), serviceContext, z2);
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public int request_id() {
        return this.messageMediator.getRequestId();
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public String operation() {
        return this.messageMediator.getOperationName();
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public Parameter[] arguments() throws BAD_INV_ORDER {
        checkAccess(2);
        if (this.cachedArguments == null) {
            if (this.request == null) {
                throw this.stdWrapper.piOperationNotSupported1();
            }
            this.cachedArguments = nvListToParameterArray(this.request.arguments());
        }
        return this.cachedArguments;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public TypeCode[] exceptions() throws BAD_INV_ORDER {
        checkAccess(3);
        if (this.cachedExceptions == null) {
            if (this.request == null) {
                throw this.stdWrapper.piOperationNotSupported2();
            }
            ExceptionList exceptionListExceptions = this.request.exceptions();
            int iCount = exceptionListExceptions.count();
            TypeCode[] typeCodeArr = new TypeCode[iCount];
            for (int i2 = 0; i2 < iCount; i2++) {
                try {
                    typeCodeArr[i2] = exceptionListExceptions.item(i2);
                } catch (Exception e2) {
                    throw this.wrapper.exceptionInExceptions(e2);
                }
            }
            this.cachedExceptions = typeCodeArr;
        }
        return this.cachedExceptions;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public String[] contexts() throws BAD_INV_ORDER {
        checkAccess(4);
        if (this.cachedContexts == null) {
            if (this.request == null) {
                throw this.stdWrapper.piOperationNotSupported3();
            }
            ContextList contextListContexts = this.request.contexts();
            int iCount = contextListContexts.count();
            String[] strArr = new String[iCount];
            for (int i2 = 0; i2 < iCount; i2++) {
                try {
                    strArr[i2] = contextListContexts.item(i2);
                } catch (Exception e2) {
                    throw this.wrapper.exceptionInContexts(e2);
                }
            }
            this.cachedContexts = strArr;
        }
        return this.cachedContexts;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public String[] operation_context() throws BAD_INV_ORDER {
        checkAccess(5);
        if (this.cachedOperationContext == null) {
            if (this.request == null) {
                throw this.stdWrapper.piOperationNotSupported4();
            }
            NVList nVList = this.request.ctx().get_values("", 15, "*");
            String[] strArr = new String[nVList.count() * 2];
            if (nVList != null && nVList.count() != 0) {
                int i2 = 0;
                for (int i3 = 0; i3 < nVList.count(); i3++) {
                    try {
                        NamedValue namedValueItem = nVList.item(i3);
                        strArr[i2] = namedValueItem.name();
                        int i4 = i2 + 1;
                        strArr[i4] = namedValueItem.value().extract_string();
                        i2 = i4 + 1;
                    } catch (Exception e2) {
                        return (String[]) null;
                    }
                }
            }
            this.cachedOperationContext = strArr;
        }
        return this.cachedOperationContext;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public Any result() throws BAD_INV_ORDER {
        checkAccess(6);
        if (this.cachedResult == null) {
            if (this.request == null) {
                throw this.stdWrapper.piOperationNotSupported5();
            }
            NamedValue namedValueResult = this.request.result();
            if (namedValueResult == null) {
                throw this.wrapper.piDiiResultIsNull();
            }
            this.cachedResult = namedValueResult.value();
        }
        return this.cachedResult;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public boolean response_expected() {
        return !this.messageMediator.isOneWay();
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public Object forward_reference() throws BAD_INV_ORDER {
        checkAccess(10);
        if (this.replyStatus != 3) {
            throw this.stdWrapper.invalidPiCall1();
        }
        return iorToObject(getLocatedIOR());
    }

    private IOR getLocatedIOR() {
        return ((CorbaContactInfoList) this.messageMediator.getContactInfo().getContactInfoList()).getEffectiveTargetIOR();
    }

    protected void setLocatedIOR(IOR ior) {
        ((CorbaContactInfoListIterator) ((CorbaInvocationInfo) ((ORB) this.messageMediator.getBroker()).getInvocationInfo()).getContactInfoListIterator()).reportRedirect((CorbaContactInfo) this.messageMediator.getContactInfo(), ior);
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public ServiceContext get_request_service_context(int i2) throws BAD_INV_ORDER {
        checkAccess(12);
        if (this.cachedRequestServiceContexts == null) {
            this.cachedRequestServiceContexts = new HashMap();
        }
        return getServiceContext(this.cachedRequestServiceContexts, this.messageMediator.getRequestServiceContexts(), i2);
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, org.omg.PortableInterceptor.RequestInfoOperations
    public ServiceContext get_reply_service_context(int i2) throws BAD_INV_ORDER {
        checkAccess(13);
        if (this.cachedReplyServiceContexts == null) {
            this.cachedReplyServiceContexts = new HashMap();
        }
        try {
            ServiceContexts replyServiceContexts = this.messageMediator.getReplyServiceContexts();
            if (replyServiceContexts == null) {
                throw new NullPointerException();
            }
            return getServiceContext(this.cachedReplyServiceContexts, replyServiceContexts, i2);
        } catch (NullPointerException e2) {
            throw this.stdWrapper.invalidServiceContextId(e2);
        }
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl, com.sun.corba.se.spi.legacy.interceptor.RequestInfoExt
    public Connection connection() {
        return (Connection) this.messageMediator.getConnection();
    }

    protected void setInfo(MessageMediator messageMediator) {
        this.messageMediator = (CorbaMessageMediator) messageMediator;
        this.messageMediator.setDIIInfo(this.request);
    }

    void setRetryRequest(RetryType retryType) {
        this.retryRequest = retryType;
    }

    RetryType getRetryRequest() {
        return this.retryRequest;
    }

    void incrementEntryCount() {
        this.entryCount++;
    }

    void decrementEntryCount() {
        this.entryCount--;
    }

    int getEntryCount() {
        return this.entryCount;
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

    protected void setDIIRequest(Request request) {
        this.request = request;
    }

    protected void setDIIInitiate(boolean z2) {
        this.diiInitiate = z2;
    }

    protected boolean isDIIInitiate() {
        return this.diiInitiate;
    }

    protected void setPICurrentPushed(boolean z2) {
        this.piCurrentPushed = z2;
    }

    protected boolean isPICurrentPushed() {
        return this.piCurrentPushed;
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl
    protected void setException(Exception exc) {
        super.setException(exc);
        this.cachedReceivedException = null;
        this.cachedReceivedExceptionId = null;
    }

    protected boolean getIsOneWay() {
        return !response_expected();
    }

    @Override // com.sun.corba.se.impl.interceptors.RequestInfoImpl
    protected void checkAccess(int i2) throws BAD_INV_ORDER {
        boolean z2 = false;
        boolean z3 = z2;
        switch (this.currentExecutionPoint) {
            case 0:
                z3 = z2;
                switch (this.startingPointCall) {
                    case 0:
                        z3 = false;
                        break;
                    case 1:
                        z3 = true;
                        break;
                }
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
