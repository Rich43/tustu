package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import java.nio.ByteBuffer;
import org.omg.CORBA.Request;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/CorbaMessageMediator.class */
public interface CorbaMessageMediator extends MessageMediator, ResponseHandler {
    void setReplyHeader(LocateReplyOrReplyMessage locateReplyOrReplyMessage);

    LocateReplyMessage getLocateReplyHeader();

    ReplyMessage getReplyHeader();

    void setReplyExceptionDetailMessage(String str);

    RequestMessage getRequestHeader();

    GIOPVersion getGIOPVersion();

    byte getEncodingVersion();

    int getRequestId();

    Integer getRequestIdInteger();

    boolean isOneWay();

    short getAddrDisposition();

    String getOperationName();

    ServiceContexts getRequestServiceContexts();

    ServiceContexts getReplyServiceContexts();

    Message getDispatchHeader();

    void setDispatchHeader(Message message);

    ByteBuffer getDispatchBuffer();

    void setDispatchBuffer(ByteBuffer byteBuffer);

    int getThreadPoolToUse();

    byte getStreamFormatVersion();

    byte getStreamFormatVersionForReply();

    void sendCancelRequestIfFinalFragmentNotSent();

    void setDIIInfo(Request request);

    boolean isDIIRequest();

    Exception unmarshalDIIUserException(String str, InputStream inputStream);

    void setDIIException(Exception exc);

    void handleDIIReply(InputStream inputStream);

    boolean isSystemExceptionReply();

    boolean isUserExceptionReply();

    boolean isLocationForwardReply();

    boolean isDifferentAddrDispositionRequestedReply();

    short getAddrDispositionReply();

    IOR getForwardedIOR();

    SystemException getSystemExceptionReply();

    ObjectKey getObjectKey();

    void setProtocolHandler(CorbaProtocolHandler corbaProtocolHandler);

    CorbaProtocolHandler getProtocolHandler();

    OutputStream createReply();

    OutputStream createExceptionReply();

    boolean executeReturnServantInResponseConstructor();

    void setExecuteReturnServantInResponseConstructor(boolean z2);

    boolean executeRemoveThreadInfoInResponseConstructor();

    void setExecuteRemoveThreadInfoInResponseConstructor(boolean z2);

    boolean executePIInResponseConstructor();

    void setExecutePIInResponseConstructor(boolean z2);
}
