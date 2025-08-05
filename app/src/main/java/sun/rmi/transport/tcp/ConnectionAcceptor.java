package sun.rmi.transport.tcp;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import sun.rmi.runtime.NewThreadAction;
import sun.rmi.transport.Connection;

/* compiled from: TCPChannel.java */
/* loaded from: rt.jar:sun/rmi/transport/tcp/ConnectionAcceptor.class */
class ConnectionAcceptor implements Runnable {
    private TCPTransport transport;
    private List<Connection> queue = new ArrayList();
    private static int threadNum = 0;

    public ConnectionAcceptor(TCPTransport tCPTransport) {
        this.transport = tCPTransport;
    }

    public void startNewAcceptor() {
        StringBuilder sbAppend = new StringBuilder().append("Multiplex Accept-");
        int i2 = threadNum + 1;
        threadNum = i2;
        ((Thread) AccessController.doPrivileged(new NewThreadAction(this, sbAppend.append(i2).toString(), true))).start();
    }

    public void accept(Connection connection) {
        synchronized (this.queue) {
            this.queue.add(connection);
            this.queue.notify();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        Connection connectionRemove;
        synchronized (this.queue) {
            while (this.queue.size() == 0) {
                try {
                    this.queue.wait();
                } catch (InterruptedException e2) {
                }
            }
            startNewAcceptor();
            connectionRemove = this.queue.remove(0);
        }
        this.transport.handleMessages(connectionRemove, true);
    }
}
