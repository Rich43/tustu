package com.sun.jmx.remote.internal;

import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.IOException;
import java.io.InterruptedIOException;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/ClientCommunicatorAdmin.class */
public abstract class ClientCommunicatorAdmin {
    private final Checker checker;
    private long period;
    private static final int CONNECTED = 0;
    private static final int RE_CONNECTING = 1;
    private static final int FAILED = 2;
    private static final int TERMINATED = 3;
    private int state = 0;
    private final int[] lock = new int[0];
    private static volatile long threadNo = 1;
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ClientCommunicatorAdmin");

    protected abstract void checkConnection() throws IOException;

    protected abstract void doStart() throws IOException;

    protected abstract void doStop();

    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Thread, long] */
    public ClientCommunicatorAdmin(long j2) {
        this.period = j2;
        if (j2 > 0) {
            this.checker = new Checker();
            Checker checker = this.checker;
            StringBuilder sbAppend = new StringBuilder().append("JMX client heartbeat ");
            long j3 = threadNo + 1;
            threadNo = thread;
            ?? thread = new Thread(checker, sbAppend.append(j3).toString());
            thread.setDaemon(true);
            thread.start();
            return;
        }
        this.checker = null;
    }

    public void gotIOException(IOException iOException) throws IOException {
        restart(iOException);
    }

    public void terminate() {
        synchronized (this.lock) {
            if (this.state == 3) {
                return;
            }
            this.state = 3;
            this.lock.notifyAll();
            if (this.checker != null) {
                this.checker.stop();
            }
        }
    }

    private void restart(IOException iOException) throws IOException {
        synchronized (this.lock) {
            if (this.state == 3) {
                throw new IOException("The client has been closed.");
            }
            if (this.state == 2) {
                throw iOException;
            }
            if (this.state == 1) {
                while (this.state == 1) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e2) {
                        InterruptedIOException interruptedIOException = new InterruptedIOException(e2.toString());
                        EnvHelp.initCause(interruptedIOException, e2);
                        throw interruptedIOException;
                    }
                }
                if (this.state == 3) {
                    throw new IOException("The client has been closed.");
                }
                if (this.state != 0) {
                    throw iOException;
                }
                return;
            }
            this.state = 1;
            this.lock.notifyAll();
            try {
                doStart();
                synchronized (this.lock) {
                    if (this.state == 3) {
                        throw new IOException("The client has been closed.");
                    }
                    this.state = 0;
                    this.lock.notifyAll();
                }
            } catch (Exception e3) {
                logger.warning("restart", "Failed to restart: " + ((Object) e3));
                logger.debug("restart", e3);
                synchronized (this.lock) {
                    if (this.state == 3) {
                        throw new IOException("The client has been closed.");
                    }
                    this.state = 2;
                    this.lock.notifyAll();
                    try {
                        doStop();
                    } catch (Exception e4) {
                    }
                    terminate();
                    throw iOException;
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ClientCommunicatorAdmin$Checker.class */
    private class Checker implements Runnable {
        private Thread myThread;

        private Checker() {
        }

        @Override // java.lang.Runnable
        public void run() {
            this.myThread = Thread.currentThread();
            while (ClientCommunicatorAdmin.this.state != 3 && !this.myThread.isInterrupted()) {
                try {
                    Thread.sleep(ClientCommunicatorAdmin.this.period);
                } catch (InterruptedException e2) {
                }
                if (ClientCommunicatorAdmin.this.state == 3 || this.myThread.isInterrupted()) {
                    break;
                }
                try {
                    ClientCommunicatorAdmin.this.checkConnection();
                } catch (Exception e3) {
                    synchronized (ClientCommunicatorAdmin.this.lock) {
                        if (ClientCommunicatorAdmin.this.state == 3 || this.myThread.isInterrupted()) {
                            break;
                        }
                        Exception exc = (Exception) EnvHelp.getCause(e3);
                        if (!(exc instanceof IOException) || (exc instanceof InterruptedIOException)) {
                            ClientCommunicatorAdmin.logger.warning("Checker-run", "Failed to check the connection: " + ((Object) exc));
                            ClientCommunicatorAdmin.logger.debug("Checker-run", exc);
                            break;
                        } else {
                            try {
                                ClientCommunicatorAdmin.this.gotIOException((IOException) exc);
                            } catch (Exception e4) {
                                ClientCommunicatorAdmin.logger.warning("Checker-run", "Failed to check connection: " + ((Object) exc));
                                ClientCommunicatorAdmin.logger.warning("Checker-run", "stopping");
                                ClientCommunicatorAdmin.logger.debug("Checker-run", exc);
                            }
                        }
                    }
                }
            }
            if (ClientCommunicatorAdmin.logger.traceOn()) {
                ClientCommunicatorAdmin.logger.trace("Checker-run", "Finished.");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stop() {
            if (this.myThread != null && this.myThread != Thread.currentThread()) {
                this.myThread.interrupt();
            }
        }
    }
}
