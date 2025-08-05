package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import com.sun.corba.se.pept.protocol.ProtocolHandler;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.UnknownException;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/CorbaProtocolHandler.class */
public interface CorbaProtocolHandler extends ProtocolHandler {
    void handleRequest(RequestMessage requestMessage, CorbaMessageMediator corbaMessageMediator);

    void handleRequest(LocateRequestMessage locateRequestMessage, CorbaMessageMediator corbaMessageMediator);

    CorbaMessageMediator createResponse(CorbaMessageMediator corbaMessageMediator, ServiceContexts serviceContexts);

    CorbaMessageMediator createUserExceptionResponse(CorbaMessageMediator corbaMessageMediator, ServiceContexts serviceContexts);

    CorbaMessageMediator createUnknownExceptionResponse(CorbaMessageMediator corbaMessageMediator, UnknownException unknownException);

    CorbaMessageMediator createSystemExceptionResponse(CorbaMessageMediator corbaMessageMediator, SystemException systemException, ServiceContexts serviceContexts);

    CorbaMessageMediator createLocationForward(CorbaMessageMediator corbaMessageMediator, IOR ior, ServiceContexts serviceContexts);

    void handleThrowableDuringServerDispatch(CorbaMessageMediator corbaMessageMediator, Throwable th, CompletionStatus completionStatus);
}
