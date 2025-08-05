package sun.rmi.transport;

import java.io.IOException;
import java.rmi.server.UID;
import sun.rmi.server.MarshalOutputStream;

/* loaded from: rt.jar:sun/rmi/transport/ConnectionOutputStream.class */
class ConnectionOutputStream extends MarshalOutputStream {
    private final Connection conn;
    private final boolean resultStream;
    private final UID ackID;
    private DGCAckHandler dgcAckHandler;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ConnectionOutputStream.class.desiredAssertionStatus();
    }

    ConnectionOutputStream(Connection connection, boolean z2) throws IOException {
        super(connection.getOutputStream());
        this.dgcAckHandler = null;
        this.conn = connection;
        this.resultStream = z2;
        this.ackID = z2 ? new UID() : null;
    }

    void writeID() throws IOException {
        if (!$assertionsDisabled && !this.resultStream) {
            throw new AssertionError();
        }
        this.ackID.write(this);
    }

    boolean isResultStream() {
        return this.resultStream;
    }

    void saveObject(Object obj) {
        if (this.dgcAckHandler == null) {
            this.dgcAckHandler = new DGCAckHandler(this.ackID);
        }
        this.dgcAckHandler.add(obj);
    }

    DGCAckHandler getDGCAckHandler() {
        return this.dgcAckHandler;
    }

    void done() {
        if (this.dgcAckHandler != null) {
            this.dgcAckHandler.startTimer();
        }
    }
}
