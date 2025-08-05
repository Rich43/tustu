package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.corba.RequestImpl;
import com.sun.corba.se.impl.encoding.BufferManagerReadStream;
import com.sun.corba.se.impl.encoding.CDRInputObject;
import com.sun.corba.se.impl.encoding.CDROutputObject;
import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.logging.InterceptorsSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.giopmsgheaders.AddressingDispositionHelper;
import com.sun.corba.se.impl.protocol.giopmsgheaders.CancelRequestMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage_1_1;
import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage_1_2;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage_1_0;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage_1_1;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage_1_2;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage_1_0;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage_1_1;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage_1_2;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage_1_0;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage_1_1;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage_1_2;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage_1_0;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage_1_1;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage_1_2;
import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.protocol.ProtocolHandler;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.Writeable;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.ior.iiop.MaxStreamFormatVersionComponent;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.protocol.CorbaProtocolHandler;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import com.sun.corba.se.spi.protocol.ForwardException;
import com.sun.corba.se.spi.servicecontext.MaxStreamFormatVersionServiceContext;
import com.sun.corba.se.spi.servicecontext.ORBVersionServiceContext;
import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;
import com.sun.corba.se.spi.servicecontext.UnknownServiceContext;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.EmptyStackException;
import java.util.Iterator;
import org.omg.CORBA.Any;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.Request;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UNKNOWN;
import org.omg.CORBA.UnknownUserException;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.UnknownException;
import org.omg.CORBA_2_3.portable.InputStream;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/CorbaMessageMediatorImpl.class */
public class CorbaMessageMediatorImpl implements CorbaMessageMediator, CorbaProtocolHandler, MessageHandler {
    protected ORB orb;
    protected ORBUtilSystemException wrapper;
    protected InterceptorsSystemException interceptorWrapper;
    protected CorbaContactInfo contactInfo;
    protected CorbaConnection connection;
    protected short addrDisposition;
    protected CDROutputObject outputObject;
    protected CDRInputObject inputObject;
    protected Message messageHeader;
    protected RequestMessage requestHeader;
    protected LocateReplyOrReplyMessage replyHeader;
    protected String replyExceptionDetailMessage;
    protected IOR replyIOR;
    protected Integer requestIdInteger;
    protected Message dispatchHeader;
    protected ByteBuffer dispatchByteBuffer;
    protected byte streamFormatVersion;
    protected boolean streamFormatVersionSet;
    protected Request diiRequest;
    protected boolean cancelRequestAlreadySent;
    protected ProtocolHandler protocolHandler;
    protected boolean _executeReturnServantInResponseConstructor;
    protected boolean _executeRemoveThreadInfoInResponseConstructor;
    protected boolean _executePIInResponseConstructor;
    protected boolean isThreadDone;

    public CorbaMessageMediatorImpl(ORB orb, ContactInfo contactInfo, Connection connection, GIOPVersion gIOPVersion, IOR ior, int i2, short s2, String str, boolean z2) {
        this(orb, connection);
        this.contactInfo = (CorbaContactInfo) contactInfo;
        this.addrDisposition = s2;
        this.streamFormatVersion = getStreamFormatVersionForThisRequest(this.contactInfo.getEffectiveTargetIOR(), gIOPVersion);
        this.streamFormatVersionSet = true;
        this.requestHeader = MessageBase.createRequest(this.orb, gIOPVersion, ORBUtility.getEncodingVersion(orb, ior), i2, !z2, this.contactInfo.getEffectiveTargetIOR(), this.addrDisposition, str, new ServiceContexts(orb), null);
    }

    public CorbaMessageMediatorImpl(ORB orb, Connection connection) {
        this.streamFormatVersionSet = false;
        this.cancelRequestAlreadySent = false;
        this._executeReturnServantInResponseConstructor = false;
        this._executeRemoveThreadInfoInResponseConstructor = false;
        this._executePIInResponseConstructor = false;
        this.isThreadDone = false;
        this.orb = orb;
        this.connection = (CorbaConnection) connection;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.interceptorWrapper = InterceptorsSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    public CorbaMessageMediatorImpl(ORB orb, CorbaConnection corbaConnection, Message message, ByteBuffer byteBuffer) {
        this(orb, corbaConnection);
        this.dispatchHeader = message;
        this.dispatchByteBuffer = byteBuffer;
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public Broker getBroker() {
        return this.orb;
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public ContactInfo getContactInfo() {
        return this.contactInfo;
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public Connection getConnection() {
        return this.connection;
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public void initializeMessage() {
        getRequestHeader().write(this.outputObject);
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public void finishSendingRequest() {
        this.outputObject.finishSendingMessage();
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public InputObject waitForResponse() {
        if (getRequestHeader().isResponseExpected()) {
            return this.connection.waitForResponse(this);
        }
        return null;
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public void setOutputObject(OutputObject outputObject) {
        this.outputObject = (CDROutputObject) outputObject;
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public OutputObject getOutputObject() {
        return this.outputObject;
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public void setInputObject(InputObject inputObject) {
        this.inputObject = (CDRInputObject) inputObject;
    }

    @Override // com.sun.corba.se.pept.protocol.MessageMediator
    public InputObject getInputObject() {
        return this.inputObject;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setReplyHeader(LocateReplyOrReplyMessage locateReplyOrReplyMessage) {
        this.replyHeader = locateReplyOrReplyMessage;
        this.replyIOR = locateReplyOrReplyMessage.getIOR();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public LocateReplyMessage getLocateReplyHeader() {
        return (LocateReplyMessage) this.replyHeader;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public ReplyMessage getReplyHeader() {
        return (ReplyMessage) this.replyHeader;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setReplyExceptionDetailMessage(String str) {
        this.replyExceptionDetailMessage = str;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public RequestMessage getRequestHeader() {
        return this.requestHeader;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public GIOPVersion getGIOPVersion() {
        if (this.messageHeader != null) {
            return this.messageHeader.getGIOPVersion();
        }
        return getRequestHeader().getGIOPVersion();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public byte getEncodingVersion() {
        if (this.messageHeader != null) {
            return this.messageHeader.getEncodingVersion();
        }
        return getRequestHeader().getEncodingVersion();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public int getRequestId() {
        return getRequestHeader().getRequestId();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public Integer getRequestIdInteger() {
        if (this.requestIdInteger == null) {
            this.requestIdInteger = new Integer(getRequestHeader().getRequestId());
        }
        return this.requestIdInteger;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public boolean isOneWay() {
        return !getRequestHeader().isResponseExpected();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public short getAddrDisposition() {
        return this.addrDisposition;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public String getOperationName() {
        return getRequestHeader().getOperation();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public ServiceContexts getRequestServiceContexts() {
        return getRequestHeader().getServiceContexts();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public ServiceContexts getReplyServiceContexts() {
        return getReplyHeader().getServiceContexts();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void sendCancelRequestIfFinalFragmentNotSent() {
        if (sentFullMessage() || !sentFragment() || this.cancelRequestAlreadySent) {
            return;
        }
        try {
            try {
                if (this.orb.subcontractDebugFlag) {
                    dprint(".sendCancelRequestIfFinalFragmentNotSent->: " + opAndId(this));
                }
                this.connection.sendCancelRequestWithLock(getGIOPVersion(), getRequestId());
                this.cancelRequestAlreadySent = true;
                if (this.orb.subcontractDebugFlag) {
                    dprint(".sendCancelRequestIfFinalFragmentNotSent<-: " + opAndId(this));
                }
            } catch (IOException e2) {
                if (this.orb.subcontractDebugFlag) {
                    dprint(".sendCancelRequestIfFinalFragmentNotSent: !ERROR : " + opAndId(this), e2);
                }
                throw this.interceptorWrapper.ioexceptionDuringCancelRequest(CompletionStatus.COMPLETED_MAYBE, e2);
            }
        } catch (Throwable th) {
            if (this.orb.subcontractDebugFlag) {
                dprint(".sendCancelRequestIfFinalFragmentNotSent<-: " + opAndId(this));
            }
            throw th;
        }
    }

    public boolean sentFullMessage() {
        return this.outputObject.getBufferManager().sentFullMessage();
    }

    public boolean sentFragment() {
        return this.outputObject.getBufferManager().sentFragment();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setDIIInfo(Request request) {
        this.diiRequest = request;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public boolean isDIIRequest() {
        return this.diiRequest != null;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public Exception unmarshalDIIUserException(String str, InputStream inputStream) {
        if (!isDIIRequest()) {
            return null;
        }
        ExceptionList exceptionListExceptions = this.diiRequest.exceptions();
        for (int i2 = 0; i2 < exceptionListExceptions.count(); i2++) {
            try {
                TypeCode typeCodeItem = exceptionListExceptions.item(i2);
                if (typeCodeItem.id().equals(str)) {
                    Any anyCreate_any = this.orb.create_any();
                    anyCreate_any.read_value(inputStream, typeCodeItem);
                    return new UnknownUserException(anyCreate_any);
                }
            } catch (Exception e2) {
                throw this.wrapper.unexpectedDiiException(e2);
            }
        }
        return this.wrapper.unknownCorbaExc(CompletionStatus.COMPLETED_MAYBE);
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setDIIException(Exception exc) {
        this.diiRequest.env().exception(exc);
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void handleDIIReply(InputStream inputStream) throws MARSHAL {
        if (!isDIIRequest()) {
            return;
        }
        ((RequestImpl) this.diiRequest).unmarshalReply(inputStream);
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public Message getDispatchHeader() {
        return this.dispatchHeader;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setDispatchHeader(Message message) {
        this.dispatchHeader = message;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public ByteBuffer getDispatchBuffer() {
        return this.dispatchByteBuffer;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setDispatchBuffer(ByteBuffer byteBuffer) {
        this.dispatchByteBuffer = byteBuffer;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public int getThreadPoolToUse() {
        int threadPoolToUse = 0;
        Message dispatchHeader = getDispatchHeader();
        if (dispatchHeader != null) {
            threadPoolToUse = dispatchHeader.getThreadPoolToUse();
        }
        return threadPoolToUse;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public byte getStreamFormatVersion() {
        if (this.streamFormatVersionSet) {
            return this.streamFormatVersion;
        }
        return getStreamFormatVersionForReply();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public byte getStreamFormatVersionForReply() {
        MaxStreamFormatVersionServiceContext maxStreamFormatVersionServiceContext = (MaxStreamFormatVersionServiceContext) getRequestServiceContexts().get(17);
        if (maxStreamFormatVersionServiceContext != null) {
            return (byte) Math.min((int) ORBUtility.getMaxStreamFormatVersion(), (int) maxStreamFormatVersionServiceContext.getMaximumStreamFormatVersion());
        }
        if (getGIOPVersion().lessThan(GIOPVersion.V1_3)) {
            return (byte) 1;
        }
        return (byte) 2;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public boolean isSystemExceptionReply() {
        return this.replyHeader.getReplyStatus() == 2;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public boolean isUserExceptionReply() {
        return this.replyHeader.getReplyStatus() == 1;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public boolean isLocationForwardReply() {
        return this.replyHeader.getReplyStatus() == 3 || this.replyHeader.getReplyStatus() == 4;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public boolean isDifferentAddrDispositionRequestedReply() {
        return this.replyHeader.getReplyStatus() == 5;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public short getAddrDispositionReply() {
        return this.replyHeader.getAddrDisposition();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public IOR getForwardedIOR() {
        return this.replyHeader.getIOR();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public SystemException getSystemExceptionReply() {
        return this.replyHeader.getSystemException(this.replyExceptionDetailMessage);
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public ObjectKey getObjectKey() {
        return getRequestHeader().getObjectKey();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setProtocolHandler(CorbaProtocolHandler corbaProtocolHandler) {
        throw this.wrapper.methodShouldNotBeCalled();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public CorbaProtocolHandler getProtocolHandler() {
        return this;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator, org.omg.CORBA.portable.ResponseHandler
    public OutputStream createReply() {
        getProtocolHandler().createResponse(this, (ServiceContexts) null);
        return (org.omg.CORBA_2_3.portable.OutputStream) getOutputObject();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator, org.omg.CORBA.portable.ResponseHandler
    public OutputStream createExceptionReply() {
        getProtocolHandler().createUserExceptionResponse(this, (ServiceContexts) null);
        return (org.omg.CORBA_2_3.portable.OutputStream) getOutputObject();
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public boolean executeReturnServantInResponseConstructor() {
        return this._executeReturnServantInResponseConstructor;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setExecuteReturnServantInResponseConstructor(boolean z2) {
        this._executeReturnServantInResponseConstructor = z2;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public boolean executeRemoveThreadInfoInResponseConstructor() {
        return this._executeRemoveThreadInfoInResponseConstructor;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setExecuteRemoveThreadInfoInResponseConstructor(boolean z2) {
        this._executeRemoveThreadInfoInResponseConstructor = z2;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public boolean executePIInResponseConstructor() {
        return this._executePIInResponseConstructor;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaMessageMediator
    public void setExecutePIInResponseConstructor(boolean z2) {
        this._executePIInResponseConstructor = z2;
    }

    private byte getStreamFormatVersionForThisRequest(IOR ior, GIOPVersion gIOPVersion) {
        byte maxStreamFormatVersion = ORBUtility.getMaxStreamFormatVersion();
        Iterator itIteratorById = ((IIOPProfileTemplate) this.contactInfo.getEffectiveTargetIOR().getProfile().getTaggedProfileTemplate()).iteratorById(38);
        if (!itIteratorById.hasNext()) {
            if (gIOPVersion.lessThan(GIOPVersion.V1_3)) {
                return (byte) 1;
            }
            return (byte) 2;
        }
        return (byte) Math.min((int) maxStreamFormatVersion, (int) ((MaxStreamFormatVersionComponent) itIteratorById.next()).getMaxStreamFormatVersion());
    }

    @Override // com.sun.corba.se.pept.protocol.ProtocolHandler
    public boolean handleRequest(MessageMediator messageMediator) {
        try {
            this.dispatchHeader.callback(this);
        } catch (IOException e2) {
        }
        return this.isThreadDone;
    }

    private void setWorkThenPoolOrResumeSelect(Message message) {
        if (getConnection().getEventHandler().shouldUseSelectThreadToWait()) {
            resumeSelect(message);
            return;
        }
        this.isThreadDone = true;
        this.orb.getTransportManager().getSelector(0).unregisterForEvent(getConnection().getEventHandler());
        this.orb.getTransportManager().getSelector(0).registerForEvent(getConnection().getEventHandler());
    }

    private void setWorkThenReadOrResumeSelect(Message message) {
        if (getConnection().getEventHandler().shouldUseSelectThreadToWait()) {
            resumeSelect(message);
        } else {
            this.isThreadDone = false;
        }
    }

    private void resumeSelect(Message message) {
        if (transportDebug()) {
            dprint(".resumeSelect:->");
            String string = "?";
            if (message instanceof RequestMessage) {
                string = new Integer(((RequestMessage) message).getRequestId()).toString();
            } else if (message instanceof ReplyMessage) {
                string = new Integer(((ReplyMessage) message).getRequestId()).toString();
            } else if (message instanceof FragmentMessage_1_2) {
                string = new Integer(((FragmentMessage_1_2) message).getRequestId()).toString();
            }
            dprint(".resumeSelect: id/" + string + " " + ((Object) getConnection()));
        }
        this.orb.getTransportManager().getSelector(0).registerInterestOps(getConnection().getEventHandler());
        if (transportDebug()) {
            dprint(".resumeSelect:<-");
        }
    }

    private void setInputObject() {
        if (getConnection().getContactInfo() != null) {
            this.inputObject = (CDRInputObject) getConnection().getContactInfo().createInputObject(this.orb, this);
        } else if (getConnection().getAcceptor() != null) {
            this.inputObject = (CDRInputObject) getConnection().getAcceptor().createInputObject(this.orb, this);
        } else {
            throw new RuntimeException("CorbaMessageMediatorImpl.setInputObject");
        }
        this.inputObject.setMessageMediator(this);
        setInputObject(this.inputObject);
    }

    private void signalResponseReceived() {
        this.connection.getResponseWaitingRoom().responseReceived(this.inputObject);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(Message message) throws IOException {
        try {
            this.messageHeader = message;
            if (transportDebug()) {
                dprint(".handleInput->: " + MessageBase.typeToString(message.getType()));
            }
            setWorkThenReadOrResumeSelect(message);
            switch (message.getType()) {
                case 5:
                    if (transportDebug()) {
                        dprint(".handleInput: CloseConnection: purging");
                    }
                    this.connection.purgeCalls(this.wrapper.connectionRebind(), true, false);
                    break;
                case 6:
                    if (transportDebug()) {
                        dprint(".handleInput: MessageError: purging");
                    }
                    this.connection.purgeCalls(this.wrapper.recvMsgError(), true, false);
                    break;
                default:
                    if (transportDebug()) {
                        dprint(".handleInput: ERROR: " + MessageBase.typeToString(message.getType()));
                    }
                    throw this.wrapper.badGiopRequestType();
            }
            releaseByteBufferToPool();
        } finally {
            if (transportDebug()) {
                dprint(".handleInput<-: " + MessageBase.typeToString(message.getType()));
            }
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(RequestMessage_1_0 requestMessage_1_0) throws IOException {
        try {
            try {
                if (transportDebug()) {
                    dprint(".REQUEST 1.0->: " + ((Object) requestMessage_1_0));
                }
                try {
                    this.requestHeader = requestMessage_1_0;
                    this.messageHeader = requestMessage_1_0;
                    setInputObject();
                    setWorkThenPoolOrResumeSelect(requestMessage_1_0);
                    getProtocolHandler().handleRequest(requestMessage_1_0, this);
                    if (transportDebug()) {
                        dprint(".REQUEST 1.0<-: " + ((Object) requestMessage_1_0));
                    }
                } catch (Throwable th) {
                    setWorkThenPoolOrResumeSelect(requestMessage_1_0);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".REQUEST 1.0: !!ERROR!!: " + ((Object) requestMessage_1_0), th2);
                }
                if (transportDebug()) {
                    dprint(".REQUEST 1.0<-: " + ((Object) requestMessage_1_0));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".REQUEST 1.0<-: " + ((Object) requestMessage_1_0));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(RequestMessage_1_1 requestMessage_1_1) throws IOException {
        try {
            try {
                if (transportDebug()) {
                    dprint(".REQUEST 1.1->: " + ((Object) requestMessage_1_1));
                }
                try {
                    this.requestHeader = requestMessage_1_1;
                    this.messageHeader = requestMessage_1_1;
                    setInputObject();
                    this.connection.serverRequest_1_1_Put(this);
                    setWorkThenPoolOrResumeSelect(requestMessage_1_1);
                    getProtocolHandler().handleRequest(requestMessage_1_1, this);
                    if (transportDebug()) {
                        dprint(".REQUEST 1.1<-: " + ((Object) requestMessage_1_1));
                    }
                } catch (Throwable th) {
                    setWorkThenPoolOrResumeSelect(requestMessage_1_1);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".REQUEST 1.1: !!ERROR!!: " + ((Object) requestMessage_1_1), th2);
                }
                if (transportDebug()) {
                    dprint(".REQUEST 1.1<-: " + ((Object) requestMessage_1_1));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".REQUEST 1.1<-: " + ((Object) requestMessage_1_1));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(RequestMessage_1_2 requestMessage_1_2) throws IOException {
        try {
            try {
                try {
                    this.requestHeader = requestMessage_1_2;
                    this.messageHeader = requestMessage_1_2;
                    requestMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
                    setInputObject();
                    if (transportDebug()) {
                        dprint(".REQUEST 1.2->: id/" + requestMessage_1_2.getRequestId() + ": " + ((Object) requestMessage_1_2));
                    }
                    this.connection.serverRequestMapPut(requestMessage_1_2.getRequestId(), this);
                    setWorkThenPoolOrResumeSelect(requestMessage_1_2);
                    getProtocolHandler().handleRequest(requestMessage_1_2, this);
                    this.connection.serverRequestMapRemove(requestMessage_1_2.getRequestId());
                    if (transportDebug()) {
                        dprint(".REQUEST 1.2<-: id/" + requestMessage_1_2.getRequestId() + ": " + ((Object) requestMessage_1_2));
                    }
                } catch (Throwable th) {
                    if (transportDebug()) {
                        dprint(".REQUEST 1.2: id/" + requestMessage_1_2.getRequestId() + ": !!ERROR!!: " + ((Object) requestMessage_1_2), th);
                    }
                    this.connection.serverRequestMapRemove(requestMessage_1_2.getRequestId());
                    if (transportDebug()) {
                        dprint(".REQUEST 1.2<-: id/" + requestMessage_1_2.getRequestId() + ": " + ((Object) requestMessage_1_2));
                    }
                }
            } catch (Throwable th2) {
                setWorkThenPoolOrResumeSelect(requestMessage_1_2);
                throw th2;
            }
        } catch (Throwable th3) {
            this.connection.serverRequestMapRemove(requestMessage_1_2.getRequestId());
            if (transportDebug()) {
                dprint(".REQUEST 1.2<-: id/" + requestMessage_1_2.getRequestId() + ": " + ((Object) requestMessage_1_2));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(ReplyMessage_1_0 replyMessage_1_0) throws IOException {
        try {
            try {
                try {
                    if (transportDebug()) {
                        dprint(".REPLY 1.0->: " + ((Object) replyMessage_1_0));
                    }
                    this.replyHeader = replyMessage_1_0;
                    this.messageHeader = replyMessage_1_0;
                    setInputObject();
                    this.inputObject.unmarshalHeader();
                    signalResponseReceived();
                    setWorkThenReadOrResumeSelect(replyMessage_1_0);
                    if (transportDebug()) {
                        dprint(".REPLY 1.0<-: " + ((Object) replyMessage_1_0));
                    }
                } catch (Throwable th) {
                    setWorkThenReadOrResumeSelect(replyMessage_1_0);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".REPLY 1.0<-: " + ((Object) replyMessage_1_0));
                }
                throw th2;
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".REPLY 1.0: !!ERROR!!: " + ((Object) replyMessage_1_0), th3);
            }
            if (transportDebug()) {
                dprint(".REPLY 1.0<-: " + ((Object) replyMessage_1_0));
            }
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(ReplyMessage_1_1 replyMessage_1_1) throws IOException {
        try {
            try {
                if (transportDebug()) {
                    dprint(".REPLY 1.1->: " + ((Object) replyMessage_1_1));
                }
                this.replyHeader = replyMessage_1_1;
                this.messageHeader = replyMessage_1_1;
                setInputObject();
                if (replyMessage_1_1.moreFragmentsToFollow()) {
                    this.connection.clientReply_1_1_Put(this);
                    setWorkThenPoolOrResumeSelect(replyMessage_1_1);
                    this.inputObject.unmarshalHeader();
                    signalResponseReceived();
                } else {
                    this.inputObject.unmarshalHeader();
                    signalResponseReceived();
                    setWorkThenReadOrResumeSelect(replyMessage_1_1);
                }
                if (transportDebug()) {
                    dprint(".REPLY 1.1<-: " + ((Object) replyMessage_1_1));
                }
            } catch (Throwable th) {
                if (transportDebug()) {
                    dprint(".REPLY 1.1: !!ERROR!!: " + ((Object) replyMessage_1_1));
                }
                if (transportDebug()) {
                    dprint(".REPLY 1.1<-: " + ((Object) replyMessage_1_1));
                }
            }
        } catch (Throwable th2) {
            if (transportDebug()) {
                dprint(".REPLY 1.1<-: " + ((Object) replyMessage_1_1));
            }
            throw th2;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(ReplyMessage_1_2 replyMessage_1_2) throws IOException {
        try {
            try {
                try {
                    this.replyHeader = replyMessage_1_2;
                    this.messageHeader = replyMessage_1_2;
                    replyMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
                    if (transportDebug()) {
                        dprint(".REPLY 1.2->: id/" + replyMessage_1_2.getRequestId() + ": more?: " + replyMessage_1_2.moreFragmentsToFollow() + ": " + ((Object) replyMessage_1_2));
                    }
                    setInputObject();
                    signalResponseReceived();
                    setWorkThenReadOrResumeSelect(replyMessage_1_2);
                    if (transportDebug()) {
                        dprint(".REPLY 1.2<-: id/" + replyMessage_1_2.getRequestId() + ": " + ((Object) replyMessage_1_2));
                    }
                } catch (Throwable th) {
                    setWorkThenReadOrResumeSelect(replyMessage_1_2);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".REPLY 1.2: id/" + replyMessage_1_2.getRequestId() + ": !!ERROR!!: " + ((Object) replyMessage_1_2), th2);
                }
                if (transportDebug()) {
                    dprint(".REPLY 1.2<-: id/" + replyMessage_1_2.getRequestId() + ": " + ((Object) replyMessage_1_2));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".REPLY 1.2<-: id/" + replyMessage_1_2.getRequestId() + ": " + ((Object) replyMessage_1_2));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(LocateRequestMessage_1_0 locateRequestMessage_1_0) throws IOException {
        try {
            try {
                if (transportDebug()) {
                    dprint(".LOCATE_REQUEST 1.0->: " + ((Object) locateRequestMessage_1_0));
                }
                try {
                    this.messageHeader = locateRequestMessage_1_0;
                    setInputObject();
                    setWorkThenPoolOrResumeSelect(locateRequestMessage_1_0);
                    getProtocolHandler().handleRequest(locateRequestMessage_1_0, this);
                    if (transportDebug()) {
                        dprint(".LOCATE_REQUEST 1.0<-: " + ((Object) locateRequestMessage_1_0));
                    }
                } catch (Throwable th) {
                    setWorkThenPoolOrResumeSelect(locateRequestMessage_1_0);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".LOCATE_REQUEST 1.0: !!ERROR!!: " + ((Object) locateRequestMessage_1_0), th2);
                }
                if (transportDebug()) {
                    dprint(".LOCATE_REQUEST 1.0<-: " + ((Object) locateRequestMessage_1_0));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".LOCATE_REQUEST 1.0<-: " + ((Object) locateRequestMessage_1_0));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(LocateRequestMessage_1_1 locateRequestMessage_1_1) throws IOException {
        try {
            try {
                if (transportDebug()) {
                    dprint(".LOCATE_REQUEST 1.1->: " + ((Object) locateRequestMessage_1_1));
                }
                try {
                    this.messageHeader = locateRequestMessage_1_1;
                    setInputObject();
                    setWorkThenPoolOrResumeSelect(locateRequestMessage_1_1);
                    getProtocolHandler().handleRequest(locateRequestMessage_1_1, this);
                    if (transportDebug()) {
                        dprint(".LOCATE_REQUEST 1.1<-:" + ((Object) locateRequestMessage_1_1));
                    }
                } catch (Throwable th) {
                    setWorkThenPoolOrResumeSelect(locateRequestMessage_1_1);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".LOCATE_REQUEST 1.1: !!ERROR!!: " + ((Object) locateRequestMessage_1_1), th2);
                }
                if (transportDebug()) {
                    dprint(".LOCATE_REQUEST 1.1<-:" + ((Object) locateRequestMessage_1_1));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".LOCATE_REQUEST 1.1<-:" + ((Object) locateRequestMessage_1_1));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(LocateRequestMessage_1_2 locateRequestMessage_1_2) throws IOException {
        try {
            try {
                try {
                    this.messageHeader = locateRequestMessage_1_2;
                    locateRequestMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
                    setInputObject();
                    if (transportDebug()) {
                        dprint(".LOCATE_REQUEST 1.2->: id/" + locateRequestMessage_1_2.getRequestId() + ": " + ((Object) locateRequestMessage_1_2));
                    }
                    if (locateRequestMessage_1_2.moreFragmentsToFollow()) {
                        this.connection.serverRequestMapPut(locateRequestMessage_1_2.getRequestId(), this);
                    }
                    setWorkThenPoolOrResumeSelect(locateRequestMessage_1_2);
                    getProtocolHandler().handleRequest(locateRequestMessage_1_2, this);
                    if (transportDebug()) {
                        dprint(".LOCATE_REQUEST 1.2<-: id/" + locateRequestMessage_1_2.getRequestId() + ": " + ((Object) locateRequestMessage_1_2));
                    }
                } catch (Throwable th) {
                    if (transportDebug()) {
                        dprint(".LOCATE_REQUEST 1.2: id/" + locateRequestMessage_1_2.getRequestId() + ": !!ERROR!!: " + ((Object) locateRequestMessage_1_2), th);
                    }
                    if (transportDebug()) {
                        dprint(".LOCATE_REQUEST 1.2<-: id/" + locateRequestMessage_1_2.getRequestId() + ": " + ((Object) locateRequestMessage_1_2));
                    }
                }
            } catch (Throwable th2) {
                setWorkThenPoolOrResumeSelect(locateRequestMessage_1_2);
                throw th2;
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".LOCATE_REQUEST 1.2<-: id/" + locateRequestMessage_1_2.getRequestId() + ": " + ((Object) locateRequestMessage_1_2));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(LocateReplyMessage_1_0 locateReplyMessage_1_0) throws IOException {
        try {
            try {
                if (transportDebug()) {
                    dprint(".LOCATE_REPLY 1.0->:" + ((Object) locateReplyMessage_1_0));
                }
                try {
                    this.messageHeader = locateReplyMessage_1_0;
                    setInputObject();
                    this.inputObject.unmarshalHeader();
                    signalResponseReceived();
                    setWorkThenReadOrResumeSelect(locateReplyMessage_1_0);
                    if (transportDebug()) {
                        dprint(".LOCATE_REPLY 1.0<-: " + ((Object) locateReplyMessage_1_0));
                    }
                } catch (Throwable th) {
                    setWorkThenReadOrResumeSelect(locateReplyMessage_1_0);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".LOCATE_REPLY 1.0: !!ERROR!!: " + ((Object) locateReplyMessage_1_0), th2);
                }
                if (transportDebug()) {
                    dprint(".LOCATE_REPLY 1.0<-: " + ((Object) locateReplyMessage_1_0));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".LOCATE_REPLY 1.0<-: " + ((Object) locateReplyMessage_1_0));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(LocateReplyMessage_1_1 locateReplyMessage_1_1) throws IOException {
        try {
            try {
                if (transportDebug()) {
                    dprint(".LOCATE_REPLY 1.1->: " + ((Object) locateReplyMessage_1_1));
                }
                try {
                    this.messageHeader = locateReplyMessage_1_1;
                    setInputObject();
                    this.inputObject.unmarshalHeader();
                    signalResponseReceived();
                    setWorkThenReadOrResumeSelect(locateReplyMessage_1_1);
                    if (transportDebug()) {
                        dprint(".LOCATE_REPLY 1.1<-: " + ((Object) locateReplyMessage_1_1));
                    }
                } catch (Throwable th) {
                    setWorkThenReadOrResumeSelect(locateReplyMessage_1_1);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".LOCATE_REPLY 1.1: !!ERROR!!: " + ((Object) locateReplyMessage_1_1), th2);
                }
                if (transportDebug()) {
                    dprint(".LOCATE_REPLY 1.1<-: " + ((Object) locateReplyMessage_1_1));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".LOCATE_REPLY 1.1<-: " + ((Object) locateReplyMessage_1_1));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(LocateReplyMessage_1_2 locateReplyMessage_1_2) throws IOException {
        try {
            try {
                try {
                    this.messageHeader = locateReplyMessage_1_2;
                    locateReplyMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
                    setInputObject();
                    if (transportDebug()) {
                        dprint(".LOCATE_REPLY 1.2->: id/" + locateReplyMessage_1_2.getRequestId() + ": " + ((Object) locateReplyMessage_1_2));
                    }
                    signalResponseReceived();
                    setWorkThenPoolOrResumeSelect(locateReplyMessage_1_2);
                    if (transportDebug()) {
                        dprint(".LOCATE_REPLY 1.2<-: id/" + locateReplyMessage_1_2.getRequestId() + ": " + ((Object) locateReplyMessage_1_2));
                    }
                } catch (Throwable th) {
                    setWorkThenPoolOrResumeSelect(locateReplyMessage_1_2);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".LOCATE_REPLY 1.2: id/" + locateReplyMessage_1_2.getRequestId() + ": !!ERROR!!: " + ((Object) locateReplyMessage_1_2), th2);
                }
                if (transportDebug()) {
                    dprint(".LOCATE_REPLY 1.2<-: id/" + locateReplyMessage_1_2.getRequestId() + ": " + ((Object) locateReplyMessage_1_2));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".LOCATE_REPLY 1.2<-: id/" + locateReplyMessage_1_2.getRequestId() + ": " + ((Object) locateReplyMessage_1_2));
            }
            throw th3;
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(FragmentMessage_1_1 fragmentMessage_1_1) throws IOException {
        MessageMediator messageMediatorClientReply_1_1_Get;
        try {
            try {
                if (transportDebug()) {
                    dprint(".FRAGMENT 1.1->: more?: " + fragmentMessage_1_1.moreFragmentsToFollow() + ": " + ((Object) fragmentMessage_1_1));
                }
                try {
                    this.messageHeader = fragmentMessage_1_1;
                    CDRInputObject cDRInputObject = null;
                    if (this.connection.isServer()) {
                        messageMediatorClientReply_1_1_Get = this.connection.serverRequest_1_1_Get();
                    } else {
                        messageMediatorClientReply_1_1_Get = this.connection.clientReply_1_1_Get();
                    }
                    if (messageMediatorClientReply_1_1_Get != null) {
                        cDRInputObject = (CDRInputObject) messageMediatorClientReply_1_1_Get.getInputObject();
                    }
                    if (cDRInputObject == null) {
                        if (transportDebug()) {
                            dprint(".FRAGMENT 1.1: ++++DISCARDING++++: " + ((Object) fragmentMessage_1_1));
                        }
                        releaseByteBufferToPool();
                        setWorkThenReadOrResumeSelect(fragmentMessage_1_1);
                        if (transportDebug()) {
                            dprint(".FRAGMENT 1.1<-: " + ((Object) fragmentMessage_1_1));
                            return;
                        }
                        return;
                    }
                    cDRInputObject.getBufferManager().processFragment(this.dispatchByteBuffer, fragmentMessage_1_1);
                    if (!fragmentMessage_1_1.moreFragmentsToFollow()) {
                        if (this.connection.isServer()) {
                            this.connection.serverRequest_1_1_Remove();
                        } else {
                            this.connection.clientReply_1_1_Remove();
                        }
                    }
                    setWorkThenReadOrResumeSelect(fragmentMessage_1_1);
                    if (transportDebug()) {
                        dprint(".FRAGMENT 1.1<-: " + ((Object) fragmentMessage_1_1));
                    }
                } catch (Throwable th) {
                    setWorkThenReadOrResumeSelect(fragmentMessage_1_1);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".FRAGMENT 1.1: !!ERROR!!: " + ((Object) fragmentMessage_1_1), th2);
                }
                if (transportDebug()) {
                    dprint(".FRAGMENT 1.1<-: " + ((Object) fragmentMessage_1_1));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".FRAGMENT 1.1<-: " + ((Object) fragmentMessage_1_1));
            }
            throw th3;
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(FragmentMessage_1_2 fragmentMessage_1_2) throws IOException {
        try {
            try {
                try {
                    this.messageHeader = fragmentMessage_1_2;
                    fragmentMessage_1_2.unmarshalRequestID(this.dispatchByteBuffer);
                    if (transportDebug()) {
                        dprint(".FRAGMENT 1.2->: id/" + fragmentMessage_1_2.getRequestId() + ": more?: " + fragmentMessage_1_2.moreFragmentsToFollow() + ": " + ((Object) fragmentMessage_1_2));
                    }
                    InputObject inputObject = null;
                    MessageMediator messageMediatorServerRequestMapGet = this.connection.isServer() ? this.connection.serverRequestMapGet(fragmentMessage_1_2.getRequestId()) : this.connection.clientRequestMapGet(fragmentMessage_1_2.getRequestId());
                    if (messageMediatorServerRequestMapGet != null) {
                        inputObject = messageMediatorServerRequestMapGet.getInputObject();
                    }
                    if (inputObject != null) {
                        ((CDRInputObject) inputObject).getBufferManager().processFragment(this.dispatchByteBuffer, fragmentMessage_1_2);
                        if (!this.connection.isServer()) {
                        }
                        setWorkThenReadOrResumeSelect(fragmentMessage_1_2);
                        if (transportDebug()) {
                            dprint(".FRAGMENT 1.2<-: id/" + fragmentMessage_1_2.getRequestId() + ": " + ((Object) fragmentMessage_1_2));
                            return;
                        }
                        return;
                    }
                    if (transportDebug()) {
                        dprint(".FRAGMENT 1.2: id/" + fragmentMessage_1_2.getRequestId() + ": ++++DISCARDING++++: " + ((Object) fragmentMessage_1_2));
                    }
                    releaseByteBufferToPool();
                    setWorkThenReadOrResumeSelect(fragmentMessage_1_2);
                    if (transportDebug()) {
                        dprint(".FRAGMENT 1.2<-: id/" + fragmentMessage_1_2.getRequestId() + ": " + ((Object) fragmentMessage_1_2));
                    }
                } catch (Throwable th) {
                    setWorkThenReadOrResumeSelect(fragmentMessage_1_2);
                    throw th;
                }
            } catch (Throwable th2) {
                if (transportDebug()) {
                    dprint(".FRAGMENT 1.2: id/" + fragmentMessage_1_2.getRequestId() + ": !!ERROR!!: " + ((Object) fragmentMessage_1_2), th2);
                }
                if (transportDebug()) {
                    dprint(".FRAGMENT 1.2<-: id/" + fragmentMessage_1_2.getRequestId() + ": " + ((Object) fragmentMessage_1_2));
                }
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".FRAGMENT 1.2<-: id/" + fragmentMessage_1_2.getRequestId() + ": " + ((Object) fragmentMessage_1_2));
            }
            throw th3;
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler
    public void handleInput(CancelRequestMessage cancelRequestMessage) throws IOException {
        try {
            try {
                try {
                    this.messageHeader = cancelRequestMessage;
                    setInputObject();
                    this.inputObject.unmarshalHeader();
                    if (transportDebug()) {
                        dprint(".CANCEL->: id/" + cancelRequestMessage.getRequestId() + ": " + ((Object) cancelRequestMessage.getGIOPVersion()) + ": " + ((Object) cancelRequestMessage));
                    }
                    processCancelRequest(cancelRequestMessage.getRequestId());
                    releaseByteBufferToPool();
                    setWorkThenReadOrResumeSelect(cancelRequestMessage);
                    if (transportDebug()) {
                        dprint(".CANCEL<-: id/" + cancelRequestMessage.getRequestId() + ": " + ((Object) cancelRequestMessage.getGIOPVersion()) + ": " + ((Object) cancelRequestMessage));
                    }
                } catch (Throwable th) {
                    if (transportDebug()) {
                        dprint(".CANCEL: id/" + cancelRequestMessage.getRequestId() + ": !!ERROR!!: " + ((Object) cancelRequestMessage), th);
                    }
                    if (transportDebug()) {
                        dprint(".CANCEL<-: id/" + cancelRequestMessage.getRequestId() + ": " + ((Object) cancelRequestMessage.getGIOPVersion()) + ": " + ((Object) cancelRequestMessage));
                    }
                }
            } catch (Throwable th2) {
                setWorkThenReadOrResumeSelect(cancelRequestMessage);
                throw th2;
            }
        } catch (Throwable th3) {
            if (transportDebug()) {
                dprint(".CANCEL<-: id/" + cancelRequestMessage.getRequestId() + ": " + ((Object) cancelRequestMessage.getGIOPVersion()) + ": " + ((Object) cancelRequestMessage));
            }
            throw th3;
        }
    }

    private void throwNotImplemented() {
        this.isThreadDone = false;
        throwNotImplemented("");
    }

    private void throwNotImplemented(String str) {
        throw new RuntimeException("CorbaMessageMediatorImpl: not implemented " + str);
    }

    private void dprint(String str, Throwable th) {
        dprint(str);
        th.printStackTrace(System.out);
    }

    private void dprint(String str) {
        ORBUtility.dprint("CorbaMessageMediatorImpl", str);
    }

    protected String opAndId(CorbaMessageMediator corbaMessageMediator) {
        return ORBUtility.operationNameAndRequestId(corbaMessageMediator);
    }

    private boolean transportDebug() {
        return this.orb.transportDebugFlag;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v26, types: [com.sun.corba.se.pept.protocol.MessageMediator] */
    private final void processCancelRequest(int i2) {
        int requestId;
        if (!this.connection.isServer()) {
            return;
        }
        CorbaMessageMediator corbaMessageMediatorServerRequestMapGet = this.connection.serverRequestMapGet(i2);
        if (corbaMessageMediatorServerRequestMapGet == null) {
            corbaMessageMediatorServerRequestMapGet = this.connection.serverRequest_1_1_Get();
            if (corbaMessageMediatorServerRequestMapGet == null || (requestId = corbaMessageMediatorServerRequestMapGet.getRequestId()) != i2 || requestId == 0) {
                return;
            }
        } else {
            corbaMessageMediatorServerRequestMapGet.getRequestId();
        }
        if (corbaMessageMediatorServerRequestMapGet.getRequestHeader().getType() != 0) {
            this.wrapper.badMessageTypeForCancel();
        }
        ((BufferManagerReadStream) ((CDRInputObject) corbaMessageMediatorServerRequestMapGet.getInputObject()).getBufferManager()).cancelProcessing(i2);
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaProtocolHandler
    public void handleRequest(RequestMessage requestMessage, CorbaMessageMediator corbaMessageMediator) {
        try {
            try {
                beginRequest(corbaMessageMediator);
                try {
                    handleRequestRequest(corbaMessageMediator);
                } catch (Throwable th) {
                    if (corbaMessageMediator.isOneWay()) {
                        endRequest(corbaMessageMediator);
                        return;
                    }
                    handleThrowableDuringServerDispatch(corbaMessageMediator, th, CompletionStatus.COMPLETED_MAYBE);
                }
                if (corbaMessageMediator.isOneWay()) {
                    endRequest(corbaMessageMediator);
                } else {
                    sendResponse(corbaMessageMediator);
                    endRequest(corbaMessageMediator);
                }
            } catch (Throwable th2) {
                dispatchError(corbaMessageMediator, "RequestMessage", th2);
                endRequest(corbaMessageMediator);
            }
        } catch (Throwable th3) {
            endRequest(corbaMessageMediator);
            throw th3;
        }
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaProtocolHandler
    public void handleRequest(LocateRequestMessage locateRequestMessage, CorbaMessageMediator corbaMessageMediator) {
        try {
            try {
                beginRequest(corbaMessageMediator);
                try {
                    handleLocateRequest(corbaMessageMediator);
                } catch (Throwable th) {
                    handleThrowableDuringServerDispatch(corbaMessageMediator, th, CompletionStatus.COMPLETED_MAYBE);
                }
                sendResponse(corbaMessageMediator);
                endRequest(corbaMessageMediator);
            } catch (Throwable th2) {
                dispatchError(corbaMessageMediator, "LocateRequestMessage", th2);
                endRequest(corbaMessageMediator);
            }
        } catch (Throwable th3) {
            endRequest(corbaMessageMediator);
            throw th3;
        }
    }

    private void beginRequest(CorbaMessageMediator corbaMessageMediator) {
        if (((ORB) corbaMessageMediator.getBroker()).subcontractDebugFlag) {
            dprint(".handleRequest->:");
        }
        this.connection.serverRequestProcessingBegins();
    }

    private void dispatchError(CorbaMessageMediator corbaMessageMediator, String str, Throwable th) {
        if (this.orb.subcontractDebugFlag) {
            dprint(".handleRequest: " + opAndId(corbaMessageMediator) + ": !!ERROR!!: " + str, th);
        }
    }

    private void sendResponse(CorbaMessageMediator corbaMessageMediator) {
        if (this.orb.subcontractDebugFlag) {
            dprint(".handleRequest: " + opAndId(corbaMessageMediator) + ": sending response");
        }
        CDROutputObject cDROutputObject = (CDROutputObject) corbaMessageMediator.getOutputObject();
        if (cDROutputObject != null) {
            cDROutputObject.finishSendingMessage();
        }
    }

    private void endRequest(CorbaMessageMediator corbaMessageMediator) {
        ORB orb = (ORB) corbaMessageMediator.getBroker();
        if (orb.subcontractDebugFlag) {
            dprint(".handleRequest<-: " + opAndId(corbaMessageMediator));
        }
        try {
            try {
                OutputObject outputObject = corbaMessageMediator.getOutputObject();
                if (outputObject != null) {
                    outputObject.close();
                }
                InputObject inputObject = corbaMessageMediator.getInputObject();
                if (inputObject != null) {
                    inputObject.close();
                }
                ((CorbaConnection) corbaMessageMediator.getConnection()).serverRequestProcessingEnds();
            } catch (IOException e2) {
                if (orb.subcontractDebugFlag) {
                    dprint(".endRequest: IOException:" + e2.getMessage(), e2);
                }
                ((CorbaConnection) corbaMessageMediator.getConnection()).serverRequestProcessingEnds();
            }
        } catch (Throwable th) {
            ((CorbaConnection) corbaMessageMediator.getConnection()).serverRequestProcessingEnds();
            throw th;
        }
    }

    protected void handleRequestRequest(CorbaMessageMediator corbaMessageMediator) {
        ((CDRInputObject) corbaMessageMediator.getInputObject()).unmarshalHeader();
        ORB orb = (ORB) corbaMessageMediator.getBroker();
        synchronized (orb) {
            orb.checkShutdownState();
        }
        ObjectKey objectKey = corbaMessageMediator.getObjectKey();
        if (orb.subcontractDebugFlag) {
            dprint(".handleRequest: " + opAndId(corbaMessageMediator) + ": dispatching to scid: " + objectKey.getTemplate().getSubcontractId());
        }
        CorbaServerRequestDispatcher serverRequestDispatcher = objectKey.getServerRequestDispatcher(orb);
        if (orb.subcontractDebugFlag) {
            dprint(".handleRequest: " + opAndId(corbaMessageMediator) + ": dispatching to sc: " + ((Object) serverRequestDispatcher));
        }
        if (serverRequestDispatcher == null) {
            throw this.wrapper.noServerScInDispatch();
        }
        try {
            orb.startingDispatch();
            serverRequestDispatcher.dispatch(corbaMessageMediator);
            orb.finishedDispatch();
        } catch (Throwable th) {
            orb.finishedDispatch();
            throw th;
        }
    }

    protected void handleLocateRequest(CorbaMessageMediator corbaMessageMediator) {
        LocateReplyMessage locateReplyMessageCreateLocateReply;
        CorbaServerRequestDispatcher serverRequestDispatcher;
        ORB orb = (ORB) corbaMessageMediator.getBroker();
        LocateRequestMessage locateRequestMessage = (LocateRequestMessage) corbaMessageMediator.getDispatchHeader();
        IOR iorLocate = null;
        short sExpectedAddrDisp = -1;
        try {
            ((CDRInputObject) corbaMessageMediator.getInputObject()).unmarshalHeader();
            serverRequestDispatcher = locateRequestMessage.getObjectKey().getServerRequestDispatcher(orb);
        } catch (AddressingDispositionException e2) {
            locateReplyMessageCreateLocateReply = MessageBase.createLocateReply(orb, locateRequestMessage.getGIOPVersion(), locateRequestMessage.getEncodingVersion(), locateRequestMessage.getRequestId(), 5, null);
            sExpectedAddrDisp = e2.expectedAddrDisp();
        } catch (RequestCanceledException e3) {
            return;
        } catch (Exception e4) {
            locateReplyMessageCreateLocateReply = MessageBase.createLocateReply(orb, locateRequestMessage.getGIOPVersion(), locateRequestMessage.getEncodingVersion(), locateRequestMessage.getRequestId(), 0, null);
        }
        if (serverRequestDispatcher == null) {
            return;
        }
        iorLocate = serverRequestDispatcher.locate(locateRequestMessage.getObjectKey());
        if (iorLocate == null) {
            locateReplyMessageCreateLocateReply = MessageBase.createLocateReply(orb, locateRequestMessage.getGIOPVersion(), locateRequestMessage.getEncodingVersion(), locateRequestMessage.getRequestId(), 1, null);
        } else {
            locateReplyMessageCreateLocateReply = MessageBase.createLocateReply(orb, locateRequestMessage.getGIOPVersion(), locateRequestMessage.getEncodingVersion(), locateRequestMessage.getRequestId(), 2, iorLocate);
        }
        CDROutputObject cDROutputObjectCreateAppropriateOutputObject = createAppropriateOutputObject(corbaMessageMediator, locateRequestMessage, locateReplyMessageCreateLocateReply);
        corbaMessageMediator.setOutputObject(cDROutputObjectCreateAppropriateOutputObject);
        cDROutputObjectCreateAppropriateOutputObject.setMessageMediator(corbaMessageMediator);
        locateReplyMessageCreateLocateReply.write(cDROutputObjectCreateAppropriateOutputObject);
        if (iorLocate != null) {
            iorLocate.write(cDROutputObjectCreateAppropriateOutputObject);
        }
        if (sExpectedAddrDisp != -1) {
            AddressingDispositionHelper.write(cDROutputObjectCreateAppropriateOutputObject, sExpectedAddrDisp);
        }
    }

    private CDROutputObject createAppropriateOutputObject(CorbaMessageMediator corbaMessageMediator, Message message, LocateReplyMessage locateReplyMessage) {
        CDROutputObject cDROutputObjectNewCDROutputObject;
        if (message.getGIOPVersion().lessThan(GIOPVersion.V1_2)) {
            cDROutputObjectNewCDROutputObject = OutputStreamFactory.newCDROutputObject((ORB) corbaMessageMediator.getBroker(), this, GIOPVersion.V1_0, (CorbaConnection) corbaMessageMediator.getConnection(), locateReplyMessage, (byte) 1);
        } else {
            cDROutputObjectNewCDROutputObject = OutputStreamFactory.newCDROutputObject((ORB) corbaMessageMediator.getBroker(), corbaMessageMediator, locateReplyMessage, (byte) 1);
        }
        return cDROutputObjectNewCDROutputObject;
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaProtocolHandler
    public void handleThrowableDuringServerDispatch(CorbaMessageMediator corbaMessageMediator, Throwable th, CompletionStatus completionStatus) {
        if (((ORB) corbaMessageMediator.getBroker()).subcontractDebugFlag) {
            dprint(".handleThrowableDuringServerDispatch: " + opAndId(corbaMessageMediator) + ": " + ((Object) th));
        }
        handleThrowableDuringServerDispatch(corbaMessageMediator, th, completionStatus, 1);
    }

    protected void handleThrowableDuringServerDispatch(CorbaMessageMediator corbaMessageMediator, Throwable th, CompletionStatus completionStatus, int i2) {
        if (i2 > 10) {
            if (((ORB) corbaMessageMediator.getBroker()).subcontractDebugFlag) {
                dprint(".handleThrowableDuringServerDispatch: " + opAndId(corbaMessageMediator) + ": cannot handle: " + ((Object) th));
            }
            RuntimeException runtimeException = new RuntimeException("handleThrowableDuringServerDispatch: cannot create response.");
            runtimeException.initCause(th);
            throw runtimeException;
        }
        try {
            if (th instanceof ForwardException) {
                createLocationForward(corbaMessageMediator, ((ForwardException) th).getIOR(), null);
            } else if (th instanceof AddressingDispositionException) {
                handleAddressingDisposition(corbaMessageMediator, (AddressingDispositionException) th);
            } else {
                createSystemExceptionResponse(corbaMessageMediator, convertThrowableToSystemException(th, completionStatus), null);
            }
        } catch (Throwable th2) {
            handleThrowableDuringServerDispatch(corbaMessageMediator, th2, completionStatus, i2 + 1);
        }
    }

    protected SystemException convertThrowableToSystemException(Throwable th, CompletionStatus completionStatus) {
        if (th instanceof SystemException) {
            return (SystemException) th;
        }
        if (th instanceof RequestCanceledException) {
            return this.wrapper.requestCanceled(th);
        }
        return this.wrapper.runtimeexception(CompletionStatus.COMPLETED_MAYBE, th);
    }

    protected void handleAddressingDisposition(CorbaMessageMediator corbaMessageMediator, AddressingDispositionException addressingDispositionException) {
        switch (corbaMessageMediator.getRequestHeader().getType()) {
            case 0:
                ReplyMessage replyMessageCreateReply = MessageBase.createReply((ORB) corbaMessageMediator.getBroker(), corbaMessageMediator.getGIOPVersion(), corbaMessageMediator.getEncodingVersion(), corbaMessageMediator.getRequestId(), 5, null, null);
                CDROutputObject cDROutputObjectNewCDROutputObject = OutputStreamFactory.newCDROutputObject((ORB) corbaMessageMediator.getBroker(), this, corbaMessageMediator.getGIOPVersion(), (CorbaConnection) corbaMessageMediator.getConnection(), replyMessageCreateReply, (byte) 1);
                corbaMessageMediator.setOutputObject(cDROutputObjectNewCDROutputObject);
                cDROutputObjectNewCDROutputObject.setMessageMediator(corbaMessageMediator);
                replyMessageCreateReply.write(cDROutputObjectNewCDROutputObject);
                AddressingDispositionHelper.write(cDROutputObjectNewCDROutputObject, addressingDispositionException.expectedAddrDisp());
                break;
            case 3:
                LocateReplyMessage locateReplyMessageCreateLocateReply = MessageBase.createLocateReply((ORB) corbaMessageMediator.getBroker(), corbaMessageMediator.getGIOPVersion(), corbaMessageMediator.getEncodingVersion(), corbaMessageMediator.getRequestId(), 5, null);
                short sExpectedAddrDisp = addressingDispositionException.expectedAddrDisp();
                CDROutputObject cDROutputObjectCreateAppropriateOutputObject = createAppropriateOutputObject(corbaMessageMediator, corbaMessageMediator.getRequestHeader(), locateReplyMessageCreateLocateReply);
                corbaMessageMediator.setOutputObject(cDROutputObjectCreateAppropriateOutputObject);
                cDROutputObjectCreateAppropriateOutputObject.setMessageMediator(corbaMessageMediator);
                locateReplyMessageCreateLocateReply.write(cDROutputObjectCreateAppropriateOutputObject);
                Writeable writeable = null;
                if (0 != 0) {
                    writeable.write(cDROutputObjectCreateAppropriateOutputObject);
                }
                if (sExpectedAddrDisp != -1) {
                    AddressingDispositionHelper.write(cDROutputObjectCreateAppropriateOutputObject, sExpectedAddrDisp);
                    break;
                }
                break;
        }
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaProtocolHandler
    public CorbaMessageMediator createResponse(CorbaMessageMediator corbaMessageMediator, ServiceContexts serviceContexts) {
        return createResponseHelper(corbaMessageMediator, getServiceContextsForReply(corbaMessageMediator, null));
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaProtocolHandler
    public CorbaMessageMediator createUserExceptionResponse(CorbaMessageMediator corbaMessageMediator, ServiceContexts serviceContexts) {
        return createResponseHelper(corbaMessageMediator, getServiceContextsForReply(corbaMessageMediator, null), true);
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaProtocolHandler
    public CorbaMessageMediator createUnknownExceptionResponse(CorbaMessageMediator corbaMessageMediator, UnknownException unknownException) {
        UNKNOWN unknown = new UNKNOWN(0, CompletionStatus.COMPLETED_MAYBE);
        ServiceContexts serviceContexts = new ServiceContexts((ORB) corbaMessageMediator.getBroker());
        serviceContexts.put(new UEInfoServiceContext(unknown));
        return createSystemExceptionResponse(corbaMessageMediator, unknown, serviceContexts);
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaProtocolHandler
    public CorbaMessageMediator createSystemExceptionResponse(CorbaMessageMediator corbaMessageMediator, SystemException systemException, ServiceContexts serviceContexts) {
        if (corbaMessageMediator.getConnection() != null) {
            CorbaMessageMediatorImpl corbaMessageMediatorImpl = (CorbaMessageMediatorImpl) ((CorbaConnection) corbaMessageMediator.getConnection()).serverRequestMapGet(corbaMessageMediator.getRequestId());
            OutputObject outputObject = null;
            if (corbaMessageMediatorImpl != null) {
                outputObject = corbaMessageMediatorImpl.getOutputObject();
            }
            if (outputObject != null && corbaMessageMediatorImpl.sentFragment() && !corbaMessageMediatorImpl.sentFullMessage()) {
                return corbaMessageMediatorImpl;
            }
        }
        if (corbaMessageMediator.executePIInResponseConstructor()) {
            ((ORB) corbaMessageMediator.getBroker()).getPIHandler().setServerPIInfo(systemException);
        }
        if (((ORB) corbaMessageMediator.getBroker()).subcontractDebugFlag && systemException != null) {
            dprint(".createSystemExceptionResponse: " + opAndId(corbaMessageMediator), systemException);
        }
        ServiceContexts serviceContextsForReply = getServiceContextsForReply(corbaMessageMediator, serviceContexts);
        addExceptionDetailMessage(corbaMessageMediator, systemException, serviceContextsForReply);
        CorbaMessageMediator corbaMessageMediatorCreateResponseHelper = createResponseHelper(corbaMessageMediator, serviceContextsForReply, false);
        ORBUtility.writeSystemException(systemException, (org.omg.CORBA_2_3.portable.OutputStream) corbaMessageMediatorCreateResponseHelper.getOutputObject());
        return corbaMessageMediatorCreateResponseHelper;
    }

    private void addExceptionDetailMessage(CorbaMessageMediator corbaMessageMediator, SystemException systemException, ServiceContexts serviceContexts) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
        systemException.printStackTrace(printWriter);
        printWriter.flush();
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((ORB) corbaMessageMediator.getBroker());
        encapsOutputStreamNewEncapsOutputStream.putEndian();
        encapsOutputStreamNewEncapsOutputStream.write_wstring(byteArrayOutputStream.toString());
        serviceContexts.put(new UnknownServiceContext(14, encapsOutputStreamNewEncapsOutputStream.toByteArray()));
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaProtocolHandler
    public CorbaMessageMediator createLocationForward(CorbaMessageMediator corbaMessageMediator, IOR ior, ServiceContexts serviceContexts) {
        return createResponseHelper(corbaMessageMediator, MessageBase.createReply((ORB) corbaMessageMediator.getBroker(), corbaMessageMediator.getGIOPVersion(), corbaMessageMediator.getEncodingVersion(), corbaMessageMediator.getRequestId(), 3, getServiceContextsForReply(corbaMessageMediator, serviceContexts), ior), ior);
    }

    protected CorbaMessageMediator createResponseHelper(CorbaMessageMediator corbaMessageMediator, ServiceContexts serviceContexts) {
        return createResponseHelper(corbaMessageMediator, MessageBase.createReply((ORB) corbaMessageMediator.getBroker(), corbaMessageMediator.getGIOPVersion(), corbaMessageMediator.getEncodingVersion(), corbaMessageMediator.getRequestId(), 0, serviceContexts, null), (IOR) null);
    }

    protected CorbaMessageMediator createResponseHelper(CorbaMessageMediator corbaMessageMediator, ServiceContexts serviceContexts, boolean z2) {
        return createResponseHelper(corbaMessageMediator, MessageBase.createReply((ORB) corbaMessageMediator.getBroker(), corbaMessageMediator.getGIOPVersion(), corbaMessageMediator.getEncodingVersion(), corbaMessageMediator.getRequestId(), z2 ? 1 : 2, serviceContexts, null), (IOR) null);
    }

    protected CorbaMessageMediator createResponseHelper(CorbaMessageMediator corbaMessageMediator, ReplyMessage replyMessage, IOR ior) {
        OutputObject outputObjectCreateOutputObject;
        runServantPostInvoke(corbaMessageMediator);
        runInterceptors(corbaMessageMediator, replyMessage);
        runRemoveThreadInfo(corbaMessageMediator);
        if (((ORB) corbaMessageMediator.getBroker()).subcontractDebugFlag) {
            dprint(".createResponseHelper: " + opAndId(corbaMessageMediator) + ": " + ((Object) replyMessage));
        }
        corbaMessageMediator.setReplyHeader(replyMessage);
        if (corbaMessageMediator.getConnection() == null) {
            outputObjectCreateOutputObject = OutputStreamFactory.newCDROutputObject(this.orb, corbaMessageMediator, corbaMessageMediator.getReplyHeader(), corbaMessageMediator.getStreamFormatVersion(), 0);
        } else {
            outputObjectCreateOutputObject = corbaMessageMediator.getConnection().getAcceptor().createOutputObject(corbaMessageMediator.getBroker(), corbaMessageMediator);
        }
        corbaMessageMediator.setOutputObject(outputObjectCreateOutputObject);
        corbaMessageMediator.getOutputObject().setMessageMediator(corbaMessageMediator);
        replyMessage.write((org.omg.CORBA_2_3.portable.OutputStream) corbaMessageMediator.getOutputObject());
        if (replyMessage.getIOR() != null) {
            replyMessage.getIOR().write((org.omg.CORBA_2_3.portable.OutputStream) corbaMessageMediator.getOutputObject());
        }
        return corbaMessageMediator;
    }

    /* JADX WARN: Finally extract failed */
    protected void runServantPostInvoke(CorbaMessageMediator corbaMessageMediator) {
        if (corbaMessageMediator.executeReturnServantInResponseConstructor()) {
            corbaMessageMediator.setExecuteReturnServantInResponseConstructor(false);
            corbaMessageMediator.setExecuteRemoveThreadInfoInResponseConstructor(true);
            try {
                ObjectAdapter objectAdapterOa = ((ORB) corbaMessageMediator.getBroker()).peekInvocationInfo().oa();
                try {
                    try {
                        objectAdapterOa.returnServant();
                        objectAdapterOa.exit();
                    } catch (Throwable th) {
                        this.wrapper.unexpectedException(th);
                        if (th instanceof Error) {
                            throw ((Error) th);
                        }
                        if (th instanceof RuntimeException) {
                            throw ((RuntimeException) th);
                        }
                        objectAdapterOa.exit();
                    }
                } catch (Throwable th2) {
                    objectAdapterOa.exit();
                    throw th2;
                }
            } catch (EmptyStackException e2) {
                throw this.wrapper.emptyStackRunServantPostInvoke(e2);
            }
        }
    }

    protected void runInterceptors(CorbaMessageMediator corbaMessageMediator, ReplyMessage replyMessage) {
        if (corbaMessageMediator.executePIInResponseConstructor()) {
            ((ORB) corbaMessageMediator.getBroker()).getPIHandler().invokeServerPIEndingPoint(replyMessage);
            ((ORB) corbaMessageMediator.getBroker()).getPIHandler().cleanupServerPIRequest();
            corbaMessageMediator.setExecutePIInResponseConstructor(false);
        }
    }

    protected void runRemoveThreadInfo(CorbaMessageMediator corbaMessageMediator) {
        if (corbaMessageMediator.executeRemoveThreadInfoInResponseConstructor()) {
            corbaMessageMediator.setExecuteRemoveThreadInfoInResponseConstructor(false);
            ((ORB) corbaMessageMediator.getBroker()).popInvocationInfo();
        }
    }

    protected ServiceContexts getServiceContextsForReply(CorbaMessageMediator corbaMessageMediator, ServiceContexts serviceContexts) {
        CorbaConnection corbaConnection = (CorbaConnection) corbaMessageMediator.getConnection();
        if (((ORB) corbaMessageMediator.getBroker()).subcontractDebugFlag) {
            dprint(".getServiceContextsForReply: " + opAndId(corbaMessageMediator) + ": " + ((Object) corbaConnection));
        }
        if (serviceContexts == null) {
            serviceContexts = new ServiceContexts((ORB) corbaMessageMediator.getBroker());
        }
        if (corbaConnection != null && !corbaConnection.isPostInitialContexts()) {
            corbaConnection.setPostInitialContexts();
            SendingContextServiceContext sendingContextServiceContext = new SendingContextServiceContext(((ORB) corbaMessageMediator.getBroker()).getFVDCodeBaseIOR());
            if (serviceContexts.get(sendingContextServiceContext.getId()) != null) {
                throw this.wrapper.duplicateSendingContextServiceContext();
            }
            serviceContexts.put(sendingContextServiceContext);
            if (((ORB) corbaMessageMediator.getBroker()).subcontractDebugFlag) {
                dprint(".getServiceContextsForReply: " + opAndId(corbaMessageMediator) + ": added SendingContextServiceContext");
            }
        }
        ORBVersionServiceContext oRBVersionServiceContext = new ORBVersionServiceContext(ORBVersionFactory.getORBVersion());
        if (serviceContexts.get(oRBVersionServiceContext.getId()) != null) {
            throw this.wrapper.duplicateOrbVersionServiceContext();
        }
        serviceContexts.put(oRBVersionServiceContext);
        if (((ORB) corbaMessageMediator.getBroker()).subcontractDebugFlag) {
            dprint(".getServiceContextsForReply: " + opAndId(corbaMessageMediator) + ": added ORB version service context");
        }
        return serviceContexts;
    }

    private void releaseByteBufferToPool() {
        if (this.dispatchByteBuffer != null) {
            this.orb.getByteBufferPool().releaseByteBuffer(this.dispatchByteBuffer);
            if (transportDebug()) {
                int iIdentityHashCode = System.identityHashCode(this.dispatchByteBuffer);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(".handleInput: releasing ByteBuffer (" + iIdentityHashCode + ") to ByteBufferPool");
                dprint(stringBuffer.toString());
            }
        }
    }
}
