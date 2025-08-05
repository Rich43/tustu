package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.corba.RequestImpl;
import com.sun.corba.se.impl.logging.InterceptorsSystemException;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orbutil.closure.ClosureFactory;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.protocol.ForwardException;
import com.sun.corba.se.spi.protocol.PIHandler;
import com.sun.corba.se.spi.protocol.RetryType;
import java.util.HashMap;
import java.util.Stack;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.NVList;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.IOP.CodecFactory;
import org.omg.PortableInterceptor.Current;
import org.omg.PortableInterceptor.Interceptor;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.omg.PortableInterceptor.ORBInitializer;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;
import org.omg.PortableInterceptor.PolicyFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/PIHandlerImpl.class */
public class PIHandlerImpl implements PIHandler {
    private ORB orb;
    InterceptorsSystemException wrapper;
    ORBUtilSystemException orbutilWrapper;
    OMGSystemException omgWrapper;
    CodecFactory codecFactory;
    String[] arguments;
    private InterceptorList interceptorList;
    private boolean hasIORInterceptors;
    private boolean hasClientInterceptors;
    private boolean hasServerInterceptors;
    private InterceptorInvoker interceptorInvoker;
    private PICurrent current;
    private HashMap policyFactoryTable;
    private static final short[] REPLY_MESSAGE_TO_PI_REPLY_STATUS = {0, 2, 1, 3, 3, 4};
    boolean printPushPopEnabled = false;
    int pushLevel = 0;
    private int serverRequestIdCounter = 0;
    private ThreadLocal threadLocalClientRequestInfoStack = new ThreadLocal() { // from class: com.sun.corba.se.impl.interceptors.PIHandlerImpl.1
        @Override // java.lang.ThreadLocal
        protected Object initialValue() {
            return new RequestInfoStack();
        }
    };
    private ThreadLocal threadLocalServerRequestInfoStack = new ThreadLocal() { // from class: com.sun.corba.se.impl.interceptors.PIHandlerImpl.2
        @Override // java.lang.ThreadLocal
        protected Object initialValue() {
            return new RequestInfoStack();
        }
    };

    private void printPush() {
        if (this.printPushPopEnabled) {
            printSpaces(this.pushLevel);
            this.pushLevel++;
            System.out.println("PUSH");
        }
    }

    private void printPop() {
        if (this.printPushPopEnabled) {
            this.pushLevel--;
            printSpaces(this.pushLevel);
            System.out.println("POP");
        }
    }

    private void printSpaces(int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            System.out.print(" ");
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.orb = null;
        this.wrapper = null;
        this.orbutilWrapper = null;
        this.omgWrapper = null;
        this.codecFactory = null;
        this.arguments = null;
        this.interceptorList = null;
        this.interceptorInvoker = null;
        this.current = null;
        this.policyFactoryTable = null;
        this.threadLocalClientRequestInfoStack = null;
        this.threadLocalServerRequestInfoStack = null;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/PIHandlerImpl$RequestInfoStack.class */
    private final class RequestInfoStack extends Stack {
        public int disableCount;

        private RequestInfoStack() {
            this.disableCount = 0;
        }
    }

    public PIHandlerImpl(ORB orb, String[] strArr) {
        this.codecFactory = null;
        this.arguments = null;
        this.orb = orb;
        this.wrapper = InterceptorsSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.orbutilWrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.omgWrapper = OMGSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.arguments = strArr;
        this.codecFactory = new CodecFactoryImpl(orb);
        this.interceptorList = new InterceptorList(this.wrapper);
        this.current = new PICurrent(orb);
        this.interceptorInvoker = new InterceptorInvoker(orb, this.interceptorList, this.current);
        orb.getLocalResolver().register(ORBConstants.PI_CURRENT_NAME, ClosureFactory.makeConstant(this.current));
        orb.getLocalResolver().register(ORBConstants.CODEC_FACTORY_NAME, ClosureFactory.makeConstant(this.codecFactory));
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void initialize() {
        if (this.orb.getORBData().getORBInitializers() != null) {
            ORBInitInfoImpl oRBInitInfoImplCreateORBInitInfo = createORBInitInfo();
            this.current.setORBInitializing(true);
            preInitORBInitializers(oRBInitInfoImplCreateORBInitInfo);
            postInitORBInitializers(oRBInitInfoImplCreateORBInitInfo);
            this.interceptorList.sortInterceptors();
            this.current.setORBInitializing(false);
            oRBInitInfoImplCreateORBInitInfo.setStage(2);
            this.hasIORInterceptors = this.interceptorList.hasInterceptorsOfType(2);
            this.hasClientInterceptors = true;
            this.hasServerInterceptors = this.interceptorList.hasInterceptorsOfType(1);
            this.interceptorInvoker.setEnabled(true);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void destroyInterceptors() {
        this.interceptorList.destroyAll();
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void objectAdapterCreated(ObjectAdapter objectAdapter) {
        if (!this.hasIORInterceptors) {
            return;
        }
        this.interceptorInvoker.objectAdapterCreated(objectAdapter);
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void adapterManagerStateChanged(int i2, short s2) {
        if (!this.hasIORInterceptors) {
            return;
        }
        this.interceptorInvoker.adapterManagerStateChanged(i2, s2);
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void adapterStateChanged(ObjectReferenceTemplate[] objectReferenceTemplateArr, short s2) {
        if (!this.hasIORInterceptors) {
            return;
        }
        this.interceptorInvoker.adapterStateChanged(objectReferenceTemplateArr, s2);
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void disableInterceptorsThisThread() {
        if (this.hasClientInterceptors) {
            ((RequestInfoStack) this.threadLocalClientRequestInfoStack.get()).disableCount++;
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void enableInterceptorsThisThread() {
        if (this.hasClientInterceptors) {
            ((RequestInfoStack) this.threadLocalClientRequestInfoStack.get()).disableCount--;
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void invokeClientPIStartingPoint() throws RemarshalException {
        if (this.hasClientInterceptors && isClientPIEnabledForThisThread()) {
            ClientRequestInfoImpl clientRequestInfoImplPeekClientRequestInfoImplStack = peekClientRequestInfoImplStack();
            this.interceptorInvoker.invokeClientInterceptorStartingPoint(clientRequestInfoImplPeekClientRequestInfoImplStack);
            short replyStatus = clientRequestInfoImplPeekClientRequestInfoImplStack.getReplyStatus();
            if (replyStatus == 1 || replyStatus == 3) {
                Exception excInvokeClientPIEndingPoint = invokeClientPIEndingPoint(convertPIReplyStatusToReplyMessage(replyStatus), clientRequestInfoImplPeekClientRequestInfoImplStack.getException());
                if (excInvokeClientPIEndingPoint == null) {
                }
                if (excInvokeClientPIEndingPoint instanceof SystemException) {
                    throw ((SystemException) excInvokeClientPIEndingPoint);
                }
                if (excInvokeClientPIEndingPoint instanceof RemarshalException) {
                    throw ((RemarshalException) excInvokeClientPIEndingPoint);
                }
                if ((excInvokeClientPIEndingPoint instanceof UserException) || (excInvokeClientPIEndingPoint instanceof ApplicationException)) {
                    throw this.wrapper.exceptionInvalid();
                }
                return;
            }
            if (replyStatus != -1) {
                throw this.wrapper.replyStatusNotInit();
            }
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public Exception makeCompletedClientRequest(int i2, Exception exc) {
        return handleClientPIEndingPoint(i2, exc, false);
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public Exception invokeClientPIEndingPoint(int i2, Exception exc) {
        return handleClientPIEndingPoint(i2, exc, true);
    }

    public Exception handleClientPIEndingPoint(int i2, Exception exc, boolean z2) {
        if (this.hasClientInterceptors && isClientPIEnabledForThisThread()) {
            short replyStatus = REPLY_MESSAGE_TO_PI_REPLY_STATUS[i2];
            ClientRequestInfoImpl clientRequestInfoImplPeekClientRequestInfoImplStack = peekClientRequestInfoImplStack();
            clientRequestInfoImplPeekClientRequestInfoImplStack.setReplyStatus(replyStatus);
            clientRequestInfoImplPeekClientRequestInfoImplStack.setException(exc);
            if (z2) {
                this.interceptorInvoker.invokeClientInterceptorEndingPoint(clientRequestInfoImplPeekClientRequestInfoImplStack);
                replyStatus = clientRequestInfoImplPeekClientRequestInfoImplStack.getReplyStatus();
            }
            if (replyStatus == 3 || replyStatus == 4) {
                clientRequestInfoImplPeekClientRequestInfoImplStack.reset();
                if (z2) {
                    clientRequestInfoImplPeekClientRequestInfoImplStack.setRetryRequest(RetryType.AFTER_RESPONSE);
                } else {
                    clientRequestInfoImplPeekClientRequestInfoImplStack.setRetryRequest(RetryType.BEFORE_RESPONSE);
                }
                exc = new RemarshalException();
            } else if (replyStatus == 1 || replyStatus == 2) {
                exc = clientRequestInfoImplPeekClientRequestInfoImplStack.getException();
            }
            return exc;
        }
        return exc;
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void initiateClientPIRequest(boolean z2) {
        if (this.hasClientInterceptors && isClientPIEnabledForThisThread()) {
            RequestInfoStack requestInfoStack = (RequestInfoStack) this.threadLocalClientRequestInfoStack.get();
            ClientRequestInfoImpl clientRequestInfoImpl = null;
            if (!requestInfoStack.empty()) {
                clientRequestInfoImpl = (ClientRequestInfoImpl) requestInfoStack.peek();
            }
            if (!z2 && clientRequestInfoImpl != null && clientRequestInfoImpl.isDIIInitiate()) {
                clientRequestInfoImpl.setDIIInitiate(false);
                return;
            }
            if (clientRequestInfoImpl == null || !clientRequestInfoImpl.getRetryRequest().isRetry()) {
                clientRequestInfoImpl = new ClientRequestInfoImpl(this.orb);
                requestInfoStack.push(clientRequestInfoImpl);
                printPush();
            }
            clientRequestInfoImpl.setRetryRequest(RetryType.NONE);
            clientRequestInfoImpl.incrementEntryCount();
            clientRequestInfoImpl.setReplyStatus((short) -1);
            if (z2) {
                clientRequestInfoImpl.setDIIInitiate(true);
            }
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void cleanupClientPIRequest() {
        if (this.hasClientInterceptors && isClientPIEnabledForThisThread()) {
            ClientRequestInfoImpl clientRequestInfoImplPeekClientRequestInfoImplStack = peekClientRequestInfoImplStack();
            if (!clientRequestInfoImplPeekClientRequestInfoImplStack.getRetryRequest().equals(RetryType.BEFORE_RESPONSE) && clientRequestInfoImplPeekClientRequestInfoImplStack.getReplyStatus() == -1) {
                invokeClientPIEndingPoint(2, this.wrapper.unknownRequestInvoke(CompletionStatus.COMPLETED_MAYBE));
            }
            clientRequestInfoImplPeekClientRequestInfoImplStack.decrementEntryCount();
            if (clientRequestInfoImplPeekClientRequestInfoImplStack.getEntryCount() == 0 && !clientRequestInfoImplPeekClientRequestInfoImplStack.getRetryRequest().isRetry()) {
                ((RequestInfoStack) this.threadLocalClientRequestInfoStack.get()).pop();
                printPop();
            }
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setClientPIInfo(CorbaMessageMediator corbaMessageMediator) {
        if (this.hasClientInterceptors && isClientPIEnabledForThisThread()) {
            peekClientRequestInfoImplStack().setInfo(corbaMessageMediator);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setClientPIInfo(RequestImpl requestImpl) {
        if (this.hasClientInterceptors && isClientPIEnabledForThisThread()) {
            peekClientRequestInfoImplStack().setDIIRequest(requestImpl);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void invokeServerPIStartingPoint() {
        if (this.hasServerInterceptors) {
            ServerRequestInfoImpl serverRequestInfoImplPeekServerRequestInfoImplStack = peekServerRequestInfoImplStack();
            this.interceptorInvoker.invokeServerInterceptorStartingPoint(serverRequestInfoImplPeekServerRequestInfoImplStack);
            serverPIHandleExceptions(serverRequestInfoImplPeekServerRequestInfoImplStack);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void invokeServerPIIntermediatePoint() {
        if (this.hasServerInterceptors) {
            ServerRequestInfoImpl serverRequestInfoImplPeekServerRequestInfoImplStack = peekServerRequestInfoImplStack();
            this.interceptorInvoker.invokeServerInterceptorIntermediatePoint(serverRequestInfoImplPeekServerRequestInfoImplStack);
            serverRequestInfoImplPeekServerRequestInfoImplStack.releaseServant();
            serverPIHandleExceptions(serverRequestInfoImplPeekServerRequestInfoImplStack);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void invokeServerPIEndingPoint(ReplyMessage replyMessage) {
        if (this.hasServerInterceptors) {
            ServerRequestInfoImpl serverRequestInfoImplPeekServerRequestInfoImplStack = peekServerRequestInfoImplStack();
            serverRequestInfoImplPeekServerRequestInfoImplStack.setReplyMessage(replyMessage);
            serverRequestInfoImplPeekServerRequestInfoImplStack.setCurrentExecutionPoint(2);
            if (!serverRequestInfoImplPeekServerRequestInfoImplStack.getAlreadyExecuted()) {
                short s2 = REPLY_MESSAGE_TO_PI_REPLY_STATUS[replyMessage.getReplyStatus()];
                if (s2 == 3 || s2 == 4) {
                    serverRequestInfoImplPeekServerRequestInfoImplStack.setForwardRequest(replyMessage.getIOR());
                }
                Exception exception = serverRequestInfoImplPeekServerRequestInfoImplStack.getException();
                if (!serverRequestInfoImplPeekServerRequestInfoImplStack.isDynamic() && s2 == 2) {
                    serverRequestInfoImplPeekServerRequestInfoImplStack.setException(this.omgWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE));
                }
                serverRequestInfoImplPeekServerRequestInfoImplStack.setReplyStatus(s2);
                this.interceptorInvoker.invokeServerInterceptorEndingPoint(serverRequestInfoImplPeekServerRequestInfoImplStack);
                short replyStatus = serverRequestInfoImplPeekServerRequestInfoImplStack.getReplyStatus();
                Exception exception2 = serverRequestInfoImplPeekServerRequestInfoImplStack.getException();
                if (replyStatus == 1 && exception2 != exception) {
                    throw ((SystemException) exception2);
                }
                if (replyStatus == 3) {
                    if (s2 != 3) {
                        throw new ForwardException(this.orb, serverRequestInfoImplPeekServerRequestInfoImplStack.getForwardRequestIOR());
                    }
                    if (serverRequestInfoImplPeekServerRequestInfoImplStack.isForwardRequestRaisedInEnding()) {
                        replyMessage.setIOR(serverRequestInfoImplPeekServerRequestInfoImplStack.getForwardRequestIOR());
                    }
                }
            }
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIInfo(Exception exc) {
        if (this.hasServerInterceptors) {
            peekServerRequestInfoImplStack().setException(exc);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIInfo(NVList nVList) {
        if (this.hasServerInterceptors) {
            peekServerRequestInfoImplStack().setDSIArguments(nVList);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIExceptionInfo(Any any) {
        if (this.hasServerInterceptors) {
            peekServerRequestInfoImplStack().setDSIException(any);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIInfo(Any any) {
        if (this.hasServerInterceptors) {
            peekServerRequestInfoImplStack().setDSIResult(any);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void initializeServerPIInfo(CorbaMessageMediator corbaMessageMediator, ObjectAdapter objectAdapter, byte[] bArr, ObjectKeyTemplate objectKeyTemplate) {
        if (this.hasServerInterceptors) {
            RequestInfoStack requestInfoStack = (RequestInfoStack) this.threadLocalServerRequestInfoStack.get();
            ServerRequestInfoImpl serverRequestInfoImpl = new ServerRequestInfoImpl(this.orb);
            requestInfoStack.push(serverRequestInfoImpl);
            printPush();
            corbaMessageMediator.setExecutePIInResponseConstructor(true);
            serverRequestInfoImpl.setInfo(corbaMessageMediator, objectAdapter, bArr, objectKeyTemplate);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void setServerPIInfo(Object obj, String str) {
        if (this.hasServerInterceptors) {
            peekServerRequestInfoImplStack().setInfo(obj, str);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void cleanupServerPIRequest() {
        if (this.hasServerInterceptors) {
            ((RequestInfoStack) this.threadLocalServerRequestInfoStack.get()).pop();
            printPop();
        }
    }

    private void serverPIHandleExceptions(ServerRequestInfoImpl serverRequestInfoImpl) {
        int endingPointCall = serverRequestInfoImpl.getEndingPointCall();
        if (endingPointCall == 1) {
            throw ((SystemException) serverRequestInfoImpl.getException());
        }
        if (endingPointCall == 2 && serverRequestInfoImpl.getForwardRequestException() != null) {
            throw new ForwardException(this.orb, serverRequestInfoImpl.getForwardRequestIOR());
        }
    }

    private int convertPIReplyStatusToReplyMessage(short s2) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= REPLY_MESSAGE_TO_PI_REPLY_STATUS.length) {
                break;
            }
            if (REPLY_MESSAGE_TO_PI_REPLY_STATUS[i3] != s2) {
                i3++;
            } else {
                i2 = i3;
                break;
            }
        }
        return i2;
    }

    private ClientRequestInfoImpl peekClientRequestInfoImplStack() {
        RequestInfoStack requestInfoStack = (RequestInfoStack) this.threadLocalClientRequestInfoStack.get();
        if (!requestInfoStack.empty()) {
            return (ClientRequestInfoImpl) requestInfoStack.peek();
        }
        throw this.wrapper.clientInfoStackNull();
    }

    private ServerRequestInfoImpl peekServerRequestInfoImplStack() {
        RequestInfoStack requestInfoStack = (RequestInfoStack) this.threadLocalServerRequestInfoStack.get();
        if (!requestInfoStack.empty()) {
            return (ServerRequestInfoImpl) requestInfoStack.peek();
        }
        throw this.wrapper.serverInfoStackNull();
    }

    private boolean isClientPIEnabledForThisThread() {
        return ((RequestInfoStack) this.threadLocalClientRequestInfoStack.get()).disableCount == 0;
    }

    private void preInitORBInitializers(ORBInitInfoImpl oRBInitInfoImpl) {
        oRBInitInfoImpl.setStage(0);
        for (int i2 = 0; i2 < this.orb.getORBData().getORBInitializers().length; i2++) {
            ORBInitializer oRBInitializer = this.orb.getORBData().getORBInitializers()[i2];
            if (oRBInitializer != null) {
                try {
                    oRBInitializer.pre_init(oRBInitInfoImpl);
                } catch (Exception e2) {
                }
            }
        }
    }

    private void postInitORBInitializers(ORBInitInfoImpl oRBInitInfoImpl) {
        oRBInitInfoImpl.setStage(1);
        for (int i2 = 0; i2 < this.orb.getORBData().getORBInitializers().length; i2++) {
            ORBInitializer oRBInitializer = this.orb.getORBData().getORBInitializers()[i2];
            if (oRBInitializer != null) {
                try {
                    oRBInitializer.post_init(oRBInitInfoImpl);
                } catch (Exception e2) {
                }
            }
        }
    }

    private ORBInitInfoImpl createORBInitInfo() {
        return new ORBInitInfoImpl(this.orb, this.arguments, this.orb.getORBData().getORBId(), this.codecFactory);
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void register_interceptor(Interceptor interceptor, int i2) throws DuplicateName {
        if (i2 >= 3 || i2 < 0) {
            throw this.wrapper.typeOutOfRange(new Integer(i2));
        }
        if (interceptor.name() == null) {
            throw this.wrapper.nameNull();
        }
        this.interceptorList.register_interceptor(interceptor, i2);
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public Current getPICurrent() {
        return this.current;
    }

    private void nullParam() throws BAD_PARAM {
        throw this.orbutilWrapper.nullParam();
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public Policy create_policy(int i2, Any any) throws BAD_PARAM, PolicyError {
        if (any == null) {
            nullParam();
        }
        if (this.policyFactoryTable == null) {
            throw new PolicyError("There is no PolicyFactory Registered for type " + i2, (short) 0);
        }
        PolicyFactory policyFactory = (PolicyFactory) this.policyFactoryTable.get(new Integer(i2));
        if (policyFactory == null) {
            throw new PolicyError(" Could Not Find PolicyFactory for the Type " + i2, (short) 0);
        }
        return policyFactory.create_policy(i2, any);
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public void registerPolicyFactory(int i2, PolicyFactory policyFactory) {
        if (this.policyFactoryTable == null) {
            this.policyFactoryTable = new HashMap();
        }
        Integer num = new Integer(i2);
        if (this.policyFactoryTable.get(num) == null) {
            this.policyFactoryTable.put(num, policyFactory);
            return;
        }
        throw this.omgWrapper.policyFactoryRegFailed(new Integer(i2));
    }

    @Override // com.sun.corba.se.spi.protocol.PIHandler
    public synchronized int allocateServerRequestId() {
        int i2 = this.serverRequestIdCounter;
        this.serverRequestIdCounter = i2 + 1;
        return i2;
    }
}
