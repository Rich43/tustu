package com.sun.corba.se.spi.transport;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ResponseWaitingRoom;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.org.omg.SendingContext.CodeBase;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/CorbaConnection.class */
public interface CorbaConnection extends Connection, com.sun.corba.se.spi.legacy.connection.Connection {
    public static final int OPENING = 1;
    public static final int ESTABLISHED = 2;
    public static final int CLOSE_SENT = 3;
    public static final int CLOSE_RECVD = 4;
    public static final int ABORT = 5;

    boolean shouldUseDirectByteBuffers();

    boolean shouldReadGiopHeaderOnly();

    ByteBuffer read(int i2, int i3, int i4, long j2) throws IOException;

    ByteBuffer read(ByteBuffer byteBuffer, int i2, int i3, long j2) throws IOException;

    void write(ByteBuffer byteBuffer) throws IOException;

    void dprint(String str);

    int getNextRequestId();

    ORB getBroker();

    CodeSetComponentInfo.CodeSetContext getCodeSetContext();

    void setCodeSetContext(CodeSetComponentInfo.CodeSetContext codeSetContext);

    MessageMediator clientRequestMapGet(int i2);

    void clientReply_1_1_Put(MessageMediator messageMediator);

    MessageMediator clientReply_1_1_Get();

    void clientReply_1_1_Remove();

    void serverRequest_1_1_Put(MessageMediator messageMediator);

    MessageMediator serverRequest_1_1_Get();

    void serverRequest_1_1_Remove();

    boolean isPostInitialContexts();

    void setPostInitialContexts();

    void purgeCalls(SystemException systemException, boolean z2, boolean z3);

    void setCodeBaseIOR(IOR ior);

    IOR getCodeBaseIOR();

    CodeBase getCodeBase();

    void sendCloseConnection(GIOPVersion gIOPVersion) throws IOException;

    void sendMessageError(GIOPVersion gIOPVersion) throws IOException;

    void sendCancelRequest(GIOPVersion gIOPVersion, int i2) throws IOException;

    void sendCancelRequestWithLock(GIOPVersion gIOPVersion, int i2) throws IOException;

    ResponseWaitingRoom getResponseWaitingRoom();

    void serverRequestMapPut(int i2, CorbaMessageMediator corbaMessageMediator);

    CorbaMessageMediator serverRequestMapGet(int i2);

    void serverRequestMapRemove(int i2);

    SocketChannel getSocketChannel();

    void serverRequestProcessingBegins();

    void serverRequestProcessingEnds();

    void closeConnectionResources();
}
