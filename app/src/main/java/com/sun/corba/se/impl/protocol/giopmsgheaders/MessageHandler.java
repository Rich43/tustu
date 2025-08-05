package com.sun.corba.se.impl.protocol.giopmsgheaders;

import java.io.IOException;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/MessageHandler.class */
public interface MessageHandler {
    void handleInput(Message message) throws IOException;

    void handleInput(RequestMessage_1_0 requestMessage_1_0) throws IOException;

    void handleInput(RequestMessage_1_1 requestMessage_1_1) throws IOException;

    void handleInput(RequestMessage_1_2 requestMessage_1_2) throws IOException;

    void handleInput(ReplyMessage_1_0 replyMessage_1_0) throws IOException;

    void handleInput(ReplyMessage_1_1 replyMessage_1_1) throws IOException;

    void handleInput(ReplyMessage_1_2 replyMessage_1_2) throws IOException;

    void handleInput(LocateRequestMessage_1_0 locateRequestMessage_1_0) throws IOException;

    void handleInput(LocateRequestMessage_1_1 locateRequestMessage_1_1) throws IOException;

    void handleInput(LocateRequestMessage_1_2 locateRequestMessage_1_2) throws IOException;

    void handleInput(LocateReplyMessage_1_0 locateReplyMessage_1_0) throws IOException;

    void handleInput(LocateReplyMessage_1_1 locateReplyMessage_1_1) throws IOException;

    void handleInput(LocateReplyMessage_1_2 locateReplyMessage_1_2) throws IOException;

    void handleInput(FragmentMessage_1_1 fragmentMessage_1_1) throws IOException;

    void handleInput(FragmentMessage_1_2 fragmentMessage_1_2) throws IOException;

    void handleInput(CancelRequestMessage cancelRequestMessage) throws IOException;
}
