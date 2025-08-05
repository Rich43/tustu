package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferManagerRead.class */
public interface BufferManagerRead {
    void processFragment(ByteBuffer byteBuffer, FragmentMessage fragmentMessage);

    ByteBufferWithInfo underflow(ByteBufferWithInfo byteBufferWithInfo);

    void init(Message message);

    MarkAndResetHandler getMarkAndResetHandler();

    void cancelProcessing(int i2);

    void close(ByteBufferWithInfo byteBufferWithInfo);
}
