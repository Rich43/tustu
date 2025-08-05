package com.sun.jmx.remote.internal;

import com.sun.jmx.remote.util.ClassLogger;
import com.sun.org.apache.xalan.internal.templates.Constants;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: rt.jar:com/sun/jmx/remote/internal/ServerCommunicatorAdmin.class */
public abstract class ServerCommunicatorAdmin {
    private long timestamp;
    private long timeout;
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ServerCommunicatorAdmin");
    private static final ClassLogger timelogger = new ClassLogger("javax.management.remote.timeout", "ServerCommunicatorAdmin");
    private final int[] lock = new int[0];
    private int currentJobs = 0;
    private boolean terminated = false;

    protected abstract void doStop();

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$202(com.sun.jmx.remote.internal.ServerCommunicatorAdmin r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.timestamp = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jmx.remote.internal.ServerCommunicatorAdmin.access$202(com.sun.jmx.remote.internal.ServerCommunicatorAdmin, long):long");
    }

    public ServerCommunicatorAdmin(long j2) {
        if (logger.traceOn()) {
            logger.trace("Constructor", "Creates a new ServerCommunicatorAdmin object with the timeout " + j2);
        }
        this.timeout = j2;
        this.timestamp = 0L;
        if (j2 < Long.MAX_VALUE) {
            Thread thread = new Thread(new Timeout());
            thread.setName("JMX server connection timeout " + thread.getId());
            thread.setDaemon(true);
            thread.start();
        }
    }

    public boolean reqIncoming() {
        boolean z2;
        if (logger.traceOn()) {
            logger.trace("reqIncoming", "Receive a new request.");
        }
        synchronized (this.lock) {
            if (this.terminated) {
                logger.warning("reqIncoming", "The server has decided to close this client connection.");
            }
            this.currentJobs++;
            z2 = this.terminated;
        }
        return z2;
    }

    public boolean rspOutgoing() {
        boolean z2;
        if (logger.traceOn()) {
            logger.trace("reqIncoming", "Finish a request.");
        }
        synchronized (this.lock) {
            int i2 = this.currentJobs - 1;
            this.currentJobs = i2;
            if (i2 == 0) {
                this.timestamp = System.currentTimeMillis();
                logtime("Admin: Timestamp=", this.timestamp);
                this.lock.notify();
            }
            z2 = this.terminated;
        }
        return z2;
    }

    public void terminate() {
        if (logger.traceOn()) {
            logger.trace(Constants.ATTRNAME_TERMINATE, "terminate the ServerCommunicatorAdmin object.");
        }
        synchronized (this.lock) {
            if (this.terminated) {
                return;
            }
            this.terminated = true;
            this.lock.notify();
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/remote/internal/ServerCommunicatorAdmin$Timeout.class */
    private class Timeout implements Runnable {
        private Timeout() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:37:0x0128, code lost:
        
            if (com.sun.jmx.remote.internal.ServerCommunicatorAdmin.logger.traceOn() == false) goto L39;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x012b, code lost:
        
            com.sun.jmx.remote.internal.ServerCommunicatorAdmin.logger.trace("Timeout-run", "timeout elapsed");
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x0135, code lost:
        
            r7.this$0.logtime("Admin: timeout elapsed! " + r0 + ">", r7.this$0.timeout);
            r7.this$0.terminated = true;
            r8 = true;
         */
        /* JADX WARN: Failed to check method for inline after forced processcom.sun.jmx.remote.internal.ServerCommunicatorAdmin.access$202(com.sun.jmx.remote.internal.ServerCommunicatorAdmin, long):long */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 448
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.jmx.remote.internal.ServerCommunicatorAdmin.Timeout.run():void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logtime(String str, long j2) {
        timelogger.trace("synchro", str + j2);
    }

    static {
    }
}
