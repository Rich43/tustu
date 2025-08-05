package sun.rmi.transport.tcp;

/* loaded from: rt.jar:sun/rmi/transport/tcp/MultiplexConnectionInfo.class */
class MultiplexConnectionInfo {
    int id;
    MultiplexInputStream in = null;
    MultiplexOutputStream out = null;
    boolean closed = false;

    MultiplexConnectionInfo(int i2) {
        this.id = i2;
    }
}
