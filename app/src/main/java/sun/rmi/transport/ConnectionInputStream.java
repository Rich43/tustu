package sun.rmi.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.rmi.runtime.Log;
import sun.rmi.server.MarshalInputStream;

/* loaded from: rt.jar:sun/rmi/transport/ConnectionInputStream.class */
class ConnectionInputStream extends MarshalInputStream {
    private boolean dgcAckNeeded;
    private Map<Endpoint, List<LiveRef>> incomingRefTable;
    private UID ackID;

    ConnectionInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
        this.dgcAckNeeded = false;
        this.incomingRefTable = new HashMap(5);
    }

    void readID() throws IOException {
        this.ackID = UID.read(this);
    }

    void saveRef(LiveRef liveRef) {
        Endpoint endpoint = liveRef.getEndpoint();
        List<LiveRef> arrayList = this.incomingRefTable.get(endpoint);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.incomingRefTable.put(endpoint, arrayList);
        }
        arrayList.add(liveRef);
    }

    void discardRefs() {
        this.incomingRefTable.clear();
    }

    void registerRefs() throws IOException {
        if (!this.incomingRefTable.isEmpty()) {
            for (Map.Entry<Endpoint, List<LiveRef>> entry : this.incomingRefTable.entrySet()) {
                DGCClient.registerRefs(entry.getKey(), entry.getValue());
            }
        }
    }

    void setAckNeeded() {
        this.dgcAckNeeded = true;
    }

    void done(Connection connection) {
        if (this.dgcAckNeeded) {
            Connection connectionNewConnection = null;
            Channel channel = null;
            boolean z2 = true;
            DGCImpl.dgcLog.log(Log.VERBOSE, "send ack");
            try {
                channel = connection.getChannel();
                connectionNewConnection = channel.newConnection();
                DataOutputStream dataOutputStream = new DataOutputStream(connectionNewConnection.getOutputStream());
                dataOutputStream.writeByte(84);
                if (this.ackID == null) {
                    this.ackID = new UID();
                }
                this.ackID.write(dataOutputStream);
                connectionNewConnection.releaseOutputStream();
                connectionNewConnection.getInputStream().available();
                connectionNewConnection.releaseInputStream();
            } catch (RemoteException e2) {
                z2 = false;
            } catch (IOException e3) {
                z2 = false;
            }
            if (connectionNewConnection != null) {
                try {
                    channel.free(connectionNewConnection, z2);
                } catch (RemoteException e4) {
                }
            }
        }
    }
}
