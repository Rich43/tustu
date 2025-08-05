package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.encoding.CDRInputObject;
import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.impl.encoding.EncapsInputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.pept.transport.OutboundConnectionCache;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.CodeSetsComponent;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.servicecontext.CodeSetServiceContext;
import com.sun.corba.se.spi.servicecontext.MaxStreamFormatVersionServiceContext;
import com.sun.corba.se.spi.servicecontext.ORBVersionServiceContext;
import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;
import com.sun.corba.se.spi.servicecontext.ServiceContext;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;
import com.sun.corba.se.spi.servicecontext.UnknownServiceContext;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import com.sun.corba.se.spi.transport.CorbaContactInfoListIterator;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.UnknownException;
import org.omg.CORBA_2_3.portable.InputStream;
import sun.corba.EncapsInputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/CorbaClientRequestDispatcherImpl.class */
public class CorbaClientRequestDispatcherImpl implements ClientRequestDispatcher {
    private ConcurrentMap<ContactInfo, Object> locks = new ConcurrentHashMap();

    @Override // com.sun.corba.se.pept.protocol.ClientRequestDispatcher
    public OutputObject beginRequest(Object obj, String str, boolean z2, ContactInfo contactInfo) {
        ORB orb = null;
        try {
            orb = (ORB) contactInfo.getBroker();
            if (orb.subcontractDebugFlag) {
                dprint(".beginRequest->: op/" + str);
            }
            orb.getPIHandler().initiateClientPIRequest(false);
            CorbaConnection corbaConnection = null;
            Object objPutIfAbsent = this.locks.get(contactInfo);
            if (objPutIfAbsent == null) {
                Object obj2 = new Object();
                objPutIfAbsent = this.locks.putIfAbsent(contactInfo, obj2);
                if (objPutIfAbsent == null) {
                    objPutIfAbsent = obj2;
                }
            }
            synchronized (objPutIfAbsent) {
                if (contactInfo.isConnectionBased()) {
                    if (contactInfo.shouldCacheConnection()) {
                        corbaConnection = (CorbaConnection) orb.getTransportManager().getOutboundConnectionCache(contactInfo).get(contactInfo);
                    }
                    if (corbaConnection == null) {
                        try {
                            corbaConnection = (CorbaConnection) contactInfo.createConnection();
                            if (orb.subcontractDebugFlag) {
                                dprint(".beginRequest: op/" + str + ": Using created connection: " + ((Object) corbaConnection));
                            }
                            if (corbaConnection.shouldRegisterReadEvent()) {
                                orb.getTransportManager().getSelector(0).registerForEvent(corbaConnection.getEventHandler());
                                corbaConnection.setState("ESTABLISHED");
                            }
                            if (contactInfo.shouldCacheConnection()) {
                                OutboundConnectionCache outboundConnectionCache = orb.getTransportManager().getOutboundConnectionCache(contactInfo);
                                outboundConnectionCache.stampTime(corbaConnection);
                                outboundConnectionCache.put(contactInfo, corbaConnection);
                            }
                        } catch (RuntimeException e2) {
                            if (orb.subcontractDebugFlag) {
                                dprint(".beginRequest: op/" + str + ": failed to create connection: " + ((Object) e2));
                            }
                            if (!getContactInfoListIterator(orb).reportException(contactInfo, e2)) {
                                throw e2;
                            }
                            if (!getContactInfoListIterator(orb).hasNext()) {
                                throw e2;
                            }
                            ContactInfo contactInfo2 = (ContactInfo) getContactInfoListIterator(orb).next();
                            unregisterWaiter(orb);
                            OutputObject outputObjectBeginRequest = beginRequest(obj, str, z2, contactInfo2);
                            if (orb.subcontractDebugFlag) {
                                dprint(".beginRequest<-: op/" + str);
                            }
                            return outputObjectBeginRequest;
                        }
                    } else if (orb.subcontractDebugFlag) {
                        dprint(".beginRequest: op/" + str + ": Using cached connection: " + ((Object) corbaConnection));
                    }
                }
            }
            CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) contactInfo.createMessageMediator(orb, contactInfo, corbaConnection, str, z2);
            if (orb.subcontractDebugFlag) {
                dprint(".beginRequest: " + opAndId(corbaMessageMediator) + ": created message mediator: " + ((Object) corbaMessageMediator));
            }
            orb.getInvocationInfo().setMessageMediator(corbaMessageMediator);
            if (corbaConnection != null && corbaConnection.getCodeSetContext() == null) {
                performCodeSetNegotiation(corbaMessageMediator);
            }
            addServiceContexts(corbaMessageMediator);
            OutputObject outputObjectCreateOutputObject = contactInfo.createOutputObject(corbaMessageMediator);
            if (orb.subcontractDebugFlag) {
                dprint(".beginRequest: " + opAndId(corbaMessageMediator) + ": created output object: " + ((Object) outputObjectCreateOutputObject));
            }
            registerWaiter(corbaMessageMediator);
            synchronized (objPutIfAbsent) {
                if (contactInfo.isConnectionBased() && contactInfo.shouldCacheConnection()) {
                    orb.getTransportManager().getOutboundConnectionCache(contactInfo).reclaim();
                }
            }
            orb.getPIHandler().setClientPIInfo(corbaMessageMediator);
            try {
                orb.getPIHandler().invokeClientPIStartingPoint();
                corbaMessageMediator.initializeMessage();
                if (orb.subcontractDebugFlag) {
                    dprint(".beginRequest: " + opAndId(corbaMessageMediator) + ": initialized message");
                }
                if (orb.subcontractDebugFlag) {
                    dprint(".beginRequest<-: op/" + str);
                }
                return outputObjectCreateOutputObject;
            } catch (RemarshalException e3) {
                if (orb.subcontractDebugFlag) {
                    dprint(".beginRequest: " + opAndId(corbaMessageMediator) + ": Remarshal");
                }
                if (!getContactInfoListIterator(orb).hasNext()) {
                    if (orb.subcontractDebugFlag) {
                        dprint("RemarshalException: hasNext false");
                    }
                    throw ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL).remarshalWithNowhereToGo();
                }
                ContactInfo contactInfo3 = (ContactInfo) getContactInfoListIterator(orb).next();
                if (orb.subcontractDebugFlag) {
                    dprint("RemarshalException: hasNext true\ncontact info " + ((Object) contactInfo3));
                }
                orb.getPIHandler().makeCompletedClientRequest(3, null);
                unregisterWaiter(orb);
                orb.getPIHandler().cleanupClientPIRequest();
                OutputObject outputObjectBeginRequest2 = beginRequest(obj, str, z2, contactInfo3);
                if (orb.subcontractDebugFlag) {
                    dprint(".beginRequest<-: op/" + str);
                }
                return outputObjectBeginRequest2;
            }
        } catch (Throwable th) {
            if (orb.subcontractDebugFlag) {
                dprint(".beginRequest<-: op/" + str);
            }
            throw th;
        }
    }

    @Override // com.sun.corba.se.pept.protocol.ClientRequestDispatcher
    public InputObject marshalingComplete(Object obj, OutputObject outputObject) throws ApplicationException, RemarshalException {
        ORB orb = null;
        CorbaMessageMediator corbaMessageMediator = null;
        try {
            corbaMessageMediator = (CorbaMessageMediator) outputObject.getMessageMediator();
            orb = (ORB) corbaMessageMediator.getBroker();
            if (orb.subcontractDebugFlag) {
                dprint(".marshalingComplete->: " + opAndId(corbaMessageMediator));
            }
            InputObject inputObjectProcessResponse = processResponse(orb, corbaMessageMediator, marshalingComplete1(orb, corbaMessageMediator));
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

    public InputObject marshalingComplete1(ORB orb, CorbaMessageMediator corbaMessageMediator) throws ApplicationException, SystemException, RemarshalException {
        try {
            corbaMessageMediator.finishSendingRequest();
            if (orb.subcontractDebugFlag) {
                dprint(".marshalingComplete: " + opAndId(corbaMessageMediator) + ": finished sending request");
            }
            return corbaMessageMediator.waitForResponse();
        } catch (RuntimeException e2) {
            if (orb.subcontractDebugFlag) {
                dprint(".marshalingComplete: " + opAndId(corbaMessageMediator) + ": exception: " + e2.toString());
            }
            boolean zReportException = getContactInfoListIterator(orb).reportException(corbaMessageMediator.getContactInfo(), e2);
            Exception excInvokeClientPIEndingPoint = orb.getPIHandler().invokeClientPIEndingPoint(2, e2);
            if (zReportException) {
                if (excInvokeClientPIEndingPoint == e2) {
                    continueOrThrowSystemOrRemarshal(corbaMessageMediator, new RemarshalException());
                    return null;
                }
                continueOrThrowSystemOrRemarshal(corbaMessageMediator, excInvokeClientPIEndingPoint);
                return null;
            }
            if (excInvokeClientPIEndingPoint instanceof RuntimeException) {
                throw ((RuntimeException) excInvokeClientPIEndingPoint);
            }
            if (excInvokeClientPIEndingPoint instanceof RemarshalException) {
                throw ((RemarshalException) excInvokeClientPIEndingPoint);
            }
            throw e2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected InputObject processResponse(ORB orb, CorbaMessageMediator corbaMessageMediator, InputObject inputObject) throws ApplicationException, SystemException, RemarshalException {
        Exception excUnmarshalDIIUserException;
        Exception excInvokeClientPIEndingPoint;
        UEInfoServiceContext uEInfoServiceContext;
        ORBUtilSystemException oRBUtilSystemException = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        if (orb.subcontractDebugFlag) {
            dprint(".processResponse: " + opAndId(corbaMessageMediator) + ": response received");
        }
        if (corbaMessageMediator.getConnection() != null) {
            ((CorbaConnection) corbaMessageMediator.getConnection()).setPostInitialContexts();
        }
        Exception exc = null;
        if (corbaMessageMediator.isOneWay()) {
            getContactInfoListIterator(orb).reportSuccess(corbaMessageMediator.getContactInfo());
            continueOrThrowSystemOrRemarshal(corbaMessageMediator, orb.getPIHandler().invokeClientPIEndingPoint(0, null));
            return null;
        }
        consumeServiceContexts(orb, corbaMessageMediator);
        ((CDRInputObject) inputObject).performORBVersionSpecificInit();
        if (corbaMessageMediator.isSystemExceptionReply()) {
            SystemException systemExceptionReply = corbaMessageMediator.getSystemExceptionReply();
            if (orb.subcontractDebugFlag) {
                dprint(".processResponse: " + opAndId(corbaMessageMediator) + ": received system exception: " + ((Object) systemExceptionReply));
            }
            if (getContactInfoListIterator(orb).reportException(corbaMessageMediator.getContactInfo(), systemExceptionReply)) {
                Exception excInvokeClientPIEndingPoint2 = orb.getPIHandler().invokeClientPIEndingPoint(2, systemExceptionReply);
                if (systemExceptionReply == excInvokeClientPIEndingPoint2) {
                    continueOrThrowSystemOrRemarshal(corbaMessageMediator, new RemarshalException());
                    throw oRBUtilSystemException.statementNotReachable1();
                }
                continueOrThrowSystemOrRemarshal(corbaMessageMediator, excInvokeClientPIEndingPoint2);
                throw oRBUtilSystemException.statementNotReachable2();
            }
            ServiceContexts replyServiceContexts = corbaMessageMediator.getReplyServiceContexts();
            if (replyServiceContexts != null && (uEInfoServiceContext = (UEInfoServiceContext) replyServiceContexts.get(9)) != null) {
                continueOrThrowSystemOrRemarshal(corbaMessageMediator, orb.getPIHandler().invokeClientPIEndingPoint(2, new UnknownException(uEInfoServiceContext.getUE())));
                throw oRBUtilSystemException.statementNotReachable3();
            }
            continueOrThrowSystemOrRemarshal(corbaMessageMediator, orb.getPIHandler().invokeClientPIEndingPoint(2, systemExceptionReply));
            throw oRBUtilSystemException.statementNotReachable4();
        }
        if (corbaMessageMediator.isUserExceptionReply()) {
            if (orb.subcontractDebugFlag) {
                dprint(".processResponse: " + opAndId(corbaMessageMediator) + ": received user exception");
            }
            getContactInfoListIterator(orb).reportSuccess(corbaMessageMediator.getContactInfo());
            String strPeekUserExceptionId = peekUserExceptionId(inputObject);
            if (corbaMessageMediator.isDIIRequest()) {
                excUnmarshalDIIUserException = corbaMessageMediator.unmarshalDIIUserException(strPeekUserExceptionId, (InputStream) inputObject);
                excInvokeClientPIEndingPoint = orb.getPIHandler().invokeClientPIEndingPoint(1, excUnmarshalDIIUserException);
                corbaMessageMediator.setDIIException(excInvokeClientPIEndingPoint);
            } else {
                ApplicationException applicationException = new ApplicationException(strPeekUserExceptionId, (org.omg.CORBA.portable.InputStream) inputObject);
                excUnmarshalDIIUserException = applicationException;
                excInvokeClientPIEndingPoint = orb.getPIHandler().invokeClientPIEndingPoint(1, applicationException);
            }
            if (excInvokeClientPIEndingPoint != excUnmarshalDIIUserException) {
                continueOrThrowSystemOrRemarshal(corbaMessageMediator, excInvokeClientPIEndingPoint);
            }
            if (excInvokeClientPIEndingPoint instanceof ApplicationException) {
                throw ((ApplicationException) excInvokeClientPIEndingPoint);
            }
            return inputObject;
        }
        if (corbaMessageMediator.isLocationForwardReply()) {
            if (orb.subcontractDebugFlag) {
                dprint(".processResponse: " + opAndId(corbaMessageMediator) + ": received location forward");
            }
            getContactInfoListIterator(orb).reportRedirect((CorbaContactInfo) corbaMessageMediator.getContactInfo(), corbaMessageMediator.getForwardedIOR());
            Exception excInvokeClientPIEndingPoint3 = orb.getPIHandler().invokeClientPIEndingPoint(3, null);
            if (!(excInvokeClientPIEndingPoint3 instanceof RemarshalException)) {
                exc = excInvokeClientPIEndingPoint3;
            }
            if (exc != null) {
                continueOrThrowSystemOrRemarshal(corbaMessageMediator, exc);
            }
            continueOrThrowSystemOrRemarshal(corbaMessageMediator, new RemarshalException());
            throw oRBUtilSystemException.statementNotReachable5();
        }
        if (corbaMessageMediator.isDifferentAddrDispositionRequestedReply()) {
            if (orb.subcontractDebugFlag) {
                dprint(".processResponse: " + opAndId(corbaMessageMediator) + ": received different addressing dispostion request");
            }
            getContactInfoListIterator(orb).reportAddrDispositionRetry((CorbaContactInfo) corbaMessageMediator.getContactInfo(), corbaMessageMediator.getAddrDispositionReply());
            Exception excInvokeClientPIEndingPoint4 = orb.getPIHandler().invokeClientPIEndingPoint(5, null);
            if (!(excInvokeClientPIEndingPoint4 instanceof RemarshalException)) {
                exc = excInvokeClientPIEndingPoint4;
            }
            if (exc != null) {
                continueOrThrowSystemOrRemarshal(corbaMessageMediator, exc);
            }
            continueOrThrowSystemOrRemarshal(corbaMessageMediator, new RemarshalException());
            throw oRBUtilSystemException.statementNotReachable6();
        }
        if (orb.subcontractDebugFlag) {
            dprint(".processResponse: " + opAndId(corbaMessageMediator) + ": received normal response");
        }
        getContactInfoListIterator(orb).reportSuccess(corbaMessageMediator.getContactInfo());
        corbaMessageMediator.handleDIIReply((InputStream) inputObject);
        continueOrThrowSystemOrRemarshal(corbaMessageMediator, orb.getPIHandler().invokeClientPIEndingPoint(0, null));
        return inputObject;
    }

    protected void continueOrThrowSystemOrRemarshal(CorbaMessageMediator corbaMessageMediator, Exception exc) throws SystemException, RemarshalException {
        ORB orb = (ORB) corbaMessageMediator.getBroker();
        if (exc != null) {
            if (exc instanceof RemarshalException) {
                orb.getInvocationInfo().setIsRetryInvocation(true);
                unregisterWaiter(orb);
                if (orb.subcontractDebugFlag) {
                    dprint(".continueOrThrowSystemOrRemarshal: " + opAndId(corbaMessageMediator) + ": throwing Remarshal");
                }
                throw ((RemarshalException) exc);
            }
            if (orb.subcontractDebugFlag) {
                dprint(".continueOrThrowSystemOrRemarshal: " + opAndId(corbaMessageMediator) + ": throwing sex:" + ((Object) exc));
            }
            throw ((SystemException) exc);
        }
    }

    protected CorbaContactInfoListIterator getContactInfoListIterator(ORB orb) {
        return (CorbaContactInfoListIterator) ((CorbaInvocationInfo) orb.getInvocationInfo()).getContactInfoListIterator();
    }

    protected void registerWaiter(CorbaMessageMediator corbaMessageMediator) {
        if (corbaMessageMediator.getConnection() != null) {
            corbaMessageMediator.getConnection().registerWaiter(corbaMessageMediator);
        }
    }

    protected void unregisterWaiter(ORB orb) {
        MessageMediator messageMediator = orb.getInvocationInfo().getMessageMediator();
        if (messageMediator != null && messageMediator.getConnection() != null) {
            messageMediator.getConnection().unregisterWaiter(messageMediator);
        }
    }

    protected void addServiceContexts(CorbaMessageMediator corbaMessageMediator) {
        ORB orb = (ORB) corbaMessageMediator.getBroker();
        CorbaConnection corbaConnection = (CorbaConnection) corbaMessageMediator.getConnection();
        GIOPVersion gIOPVersion = corbaMessageMediator.getGIOPVersion();
        ServiceContexts requestServiceContexts = corbaMessageMediator.getRequestServiceContexts();
        addCodeSetServiceContext(corbaConnection, requestServiceContexts, gIOPVersion);
        requestServiceContexts.put(MaxStreamFormatVersionServiceContext.singleton);
        requestServiceContexts.put(new ORBVersionServiceContext(ORBVersionFactory.getORBVersion()));
        if (corbaConnection != null && !corbaConnection.isPostInitialContexts()) {
            requestServiceContexts.put(new SendingContextServiceContext(orb.getFVDCodeBaseIOR()));
        }
    }

    protected void consumeServiceContexts(ORB orb, CorbaMessageMediator corbaMessageMediator) {
        ServiceContexts replyServiceContexts = corbaMessageMediator.getReplyServiceContexts();
        ORBUtilSystemException oRBUtilSystemException = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        if (replyServiceContexts == null) {
            return;
        }
        ServiceContext serviceContext = replyServiceContexts.get(6);
        if (serviceContext != null) {
            IOR ior = ((SendingContextServiceContext) serviceContext).getIOR();
            try {
                if (corbaMessageMediator.getConnection() != null) {
                    ((CorbaConnection) corbaMessageMediator.getConnection()).setCodeBaseIOR(ior);
                }
            } catch (ThreadDeath e2) {
                throw e2;
            } catch (Throwable th) {
                throw oRBUtilSystemException.badStringifiedIor(th);
            }
        }
        ServiceContext serviceContext2 = replyServiceContexts.get(1313165056);
        if (serviceContext2 != null) {
            orb.setORBVersion(((ORBVersionServiceContext) serviceContext2).getVersion());
        }
        getExceptionDetailMessage(corbaMessageMediator, oRBUtilSystemException);
    }

    protected void getExceptionDetailMessage(CorbaMessageMediator corbaMessageMediator, ORBUtilSystemException oRBUtilSystemException) {
        ServiceContext serviceContext = corbaMessageMediator.getReplyServiceContexts().get(14);
        if (serviceContext == null) {
            return;
        }
        if (!(serviceContext instanceof UnknownServiceContext)) {
            throw oRBUtilSystemException.badExceptionDetailMessageServiceContextType();
        }
        byte[] data = ((UnknownServiceContext) serviceContext).getData();
        EncapsInputStream encapsInputStreamNewEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream((ORB) corbaMessageMediator.getBroker(), data, data.length);
        encapsInputStreamNewEncapsInputStream.consumeEndian();
        corbaMessageMediator.setReplyExceptionDetailMessage("----------BEGIN server-side stack trace----------\n" + encapsInputStreamNewEncapsInputStream.read_wstring() + "\n----------END server-side stack trace----------");
    }

    @Override // com.sun.corba.se.pept.protocol.ClientRequestDispatcher
    public void endRequest(Broker broker, Object obj, InputObject inputObject) {
        ORB orb = (ORB) broker;
        try {
            try {
                if (orb.subcontractDebugFlag) {
                    dprint(".endRequest->");
                }
                MessageMediator messageMediator = orb.getInvocationInfo().getMessageMediator();
                if (messageMediator != null) {
                    if (messageMediator.getConnection() != null) {
                        ((CorbaMessageMediator) messageMediator).sendCancelRequestIfFinalFragmentNotSent();
                    }
                    InputObject inputObject2 = messageMediator.getInputObject();
                    if (inputObject2 != null) {
                        inputObject2.close();
                    }
                    OutputObject outputObject = messageMediator.getOutputObject();
                    if (outputObject != null) {
                        outputObject.close();
                    }
                }
                unregisterWaiter(orb);
                orb.getPIHandler().cleanupClientPIRequest();
                if (orb.subcontractDebugFlag) {
                    dprint(".endRequest<-");
                }
            } catch (IOException e2) {
                if (orb.subcontractDebugFlag) {
                    dprint(".endRequest: ignoring IOException - " + e2.toString());
                }
                if (orb.subcontractDebugFlag) {
                    dprint(".endRequest<-");
                }
            }
        } catch (Throwable th) {
            if (orb.subcontractDebugFlag) {
                dprint(".endRequest<-");
            }
            throw th;
        }
    }

    protected void performCodeSetNegotiation(CorbaMessageMediator corbaMessageMediator) {
        CorbaConnection corbaConnection = (CorbaConnection) corbaMessageMediator.getConnection();
        IOR effectiveTargetIOR = ((CorbaContactInfo) corbaMessageMediator.getContactInfo()).getEffectiveTargetIOR();
        GIOPVersion gIOPVersion = corbaMessageMediator.getGIOPVersion();
        if (corbaConnection != null && corbaConnection.getCodeSetContext() == null && !gIOPVersion.equals(GIOPVersion.V1_0)) {
            synchronized (corbaConnection) {
                if (corbaConnection.getCodeSetContext() != null) {
                    return;
                }
                Iterator itIteratorById = ((IIOPProfileTemplate) effectiveTargetIOR.getProfile().getTaggedProfileTemplate()).iteratorById(1);
                if (itIteratorById.hasNext()) {
                    corbaConnection.setCodeSetContext(CodeSetConversion.impl().negotiate(corbaConnection.getBroker().getORBData().getCodeSetComponentInfo(), ((CodeSetsComponent) itIteratorById.next()).getCodeSetComponentInfo()));
                }
            }
        }
    }

    protected void addCodeSetServiceContext(CorbaConnection corbaConnection, ServiceContexts serviceContexts, GIOPVersion gIOPVersion) {
        if (gIOPVersion.equals(GIOPVersion.V1_0) || corbaConnection == null) {
            return;
        }
        CodeSetComponentInfo.CodeSetContext codeSetContext = null;
        if (corbaConnection.getBroker().getORBData().alwaysSendCodeSetServiceContext() || !corbaConnection.isPostInitialContexts()) {
            codeSetContext = corbaConnection.getCodeSetContext();
        }
        if (codeSetContext == null) {
            return;
        }
        serviceContexts.put(new CodeSetServiceContext(codeSetContext));
    }

    protected String peekUserExceptionId(InputObject inputObject) {
        CDRInputObject cDRInputObject = (CDRInputObject) inputObject;
        cDRInputObject.mark(Integer.MAX_VALUE);
        String str = cDRInputObject.read_string();
        cDRInputObject.reset();
        return str;
    }

    protected void dprint(String str) {
        ORBUtility.dprint("CorbaClientRequestDispatcherImpl", str);
    }

    protected String opAndId(CorbaMessageMediator corbaMessageMediator) {
        return ORBUtility.operationNameAndRequestId(corbaMessageMediator);
    }
}
