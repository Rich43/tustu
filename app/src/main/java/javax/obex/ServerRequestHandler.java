package javax.obex;

import com.intel.bluetooth.obex.OBEXClientSessionImpl;

/* loaded from: bluecove-2.1.1.jar:javax/obex/ServerRequestHandler.class */
public class ServerRequestHandler {
    private long connectionID = -1;

    protected ServerRequestHandler() {
    }

    public final HeaderSet createHeaderSet() {
        return OBEXClientSessionImpl.createOBEXHeaderSet();
    }

    public void setConnectionID(long id) {
        if (id != -1 && (id < 0 || id > 4294967295L)) {
            throw new IllegalArgumentException(new StringBuffer().append("Invalid connectionID ").append(id).toString());
        }
        this.connectionID = id;
    }

    public long getConnectionID() {
        return this.connectionID;
    }

    public int onConnect(HeaderSet request, HeaderSet reply) {
        return 160;
    }

    public void onDisconnect(HeaderSet request, HeaderSet reply) {
    }

    public int onSetPath(HeaderSet request, HeaderSet reply, boolean backup, boolean create) {
        return 209;
    }

    public int onDelete(HeaderSet request, HeaderSet reply) {
        return 209;
    }

    public int onPut(Operation op) {
        return 209;
    }

    public int onGet(Operation op) {
        return 209;
    }

    public void onAuthenticationFailure(byte[] userName) {
    }
}
