package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferManagerReadGrow.class */
public class BufferManagerReadGrow implements BufferManagerRead, MarkAndResetHandler {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private Object streamMemento;
    private RestorableInputStream inputStream;
    private boolean markEngaged = false;

    BufferManagerReadGrow(ORB orb) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public void processFragment(ByteBuffer byteBuffer, FragmentMessage fragmentMessage) {
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public void init(Message message) {
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public ByteBufferWithInfo underflow(ByteBufferWithInfo byteBufferWithInfo) {
        throw this.wrapper.unexpectedEof();
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public void cancelProcessing(int i2) {
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public MarkAndResetHandler getMarkAndResetHandler() {
        return this;
    }

    @Override // com.sun.corba.se.impl.encoding.MarkAndResetHandler
    public void mark(RestorableInputStream restorableInputStream) {
        this.markEngaged = true;
        this.inputStream = restorableInputStream;
        this.streamMemento = this.inputStream.createStreamMemento();
    }

    @Override // com.sun.corba.se.impl.encoding.MarkAndResetHandler
    public void fragmentationOccured(ByteBufferWithInfo byteBufferWithInfo) {
    }

    @Override // com.sun.corba.se.impl.encoding.MarkAndResetHandler
    public void reset() {
        if (!this.markEngaged) {
            return;
        }
        this.markEngaged = false;
        this.inputStream.restoreInternalState(this.streamMemento);
        this.streamMemento = null;
    }

    @Override // com.sun.corba.se.impl.encoding.BufferManagerRead
    public void close(ByteBufferWithInfo byteBufferWithInfo) {
    }
}
