package com.sun.jndi.ldap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.security.AccessController;
import java.util.Arrays;
import javax.naming.CommunicationException;
import javax.naming.InterruptedNamingException;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:com/sun/jndi/ldap/Connection.class */
public final class Connection implements Runnable {
    private static final boolean debug = false;
    private static final int dump = 0;
    private final Thread worker;
    public final String host;
    public final int port;
    private OutputStream traceFile;
    private String traceTagIn;
    private String traceTagOut;
    public InputStream inStream;
    public OutputStream outStream;
    public Socket sock;
    private final LdapClient parent;
    int readTimeout;
    int connectTimeout;
    private volatile boolean isUpgradedToStartTls;
    private static final boolean IS_HOSTNAME_VERIFICATION_DISABLED = hostnameVerificationDisabledValue();
    private boolean v3 = true;
    private boolean bound = false;
    private int outMsgId = 0;
    private LdapRequest pendingRequests = null;
    volatile IOException closureReason = null;
    volatile boolean useable = true;
    final Object startTlsLock = new Object();
    private final Object pauseLock = new Object();
    private boolean paused = false;

    private static boolean hostnameVerificationDisabledValue() {
        String str = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("com.sun.jndi.ldap.object.disableEndpointIdentification");
        });
        if (str == null) {
            return false;
        }
        if (str.isEmpty()) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    void setV3(boolean z2) {
        this.v3 = z2;
    }

    void setBound() {
        this.bound = true;
    }

    Connection(LdapClient ldapClient, String str, int i2, String str2, int i3, int i4, OutputStream outputStream) throws NamingException {
        this.traceFile = null;
        this.traceTagIn = null;
        this.traceTagOut = null;
        this.host = str;
        this.port = i2;
        this.parent = ldapClient;
        this.readTimeout = i4;
        this.connectTimeout = i3;
        if (outputStream != null) {
            this.traceFile = outputStream;
            this.traceTagIn = "<- " + str + CallSiteDescriptor.TOKEN_DELIMITER + i2 + "\n\n";
            this.traceTagOut = "-> " + str + CallSiteDescriptor.TOKEN_DELIMITER + i2 + "\n\n";
        }
        try {
            this.sock = createSocket(str, i2, str2, i3);
            this.inStream = new BufferedInputStream(this.sock.getInputStream());
            this.outStream = new BufferedOutputStream(this.sock.getOutputStream());
            this.worker = Obj.helper.createThread(this);
            this.worker.setDaemon(true);
            this.worker.start();
        } catch (InvocationTargetException e2) {
            Throwable targetException = e2.getTargetException();
            CommunicationException communicationException = new CommunicationException(str + CallSiteDescriptor.TOKEN_DELIMITER + i2);
            communicationException.setRootCause(targetException);
            throw communicationException;
        } catch (Exception e3) {
            CommunicationException communicationException2 = new CommunicationException(str + CallSiteDescriptor.TOKEN_DELIMITER + i2);
            communicationException2.setRootCause(e3);
            throw communicationException2;
        }
    }

    private Object createInetSocketAddress(String str, int i2) throws NoSuchMethodException {
        try {
            return Class.forName("java.net.InetSocketAddress").getConstructor(String.class, Integer.TYPE).newInstance(str, new Integer(i2));
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e2) {
            throw new NoSuchMethodException();
        }
    }

    private Socket createSocket(String str, int i2, String str2, int i3) throws Exception {
        Socket socket = null;
        if (str2 != null) {
            Class<?> clsLoadClass = Obj.helper.loadClass(str2);
            Object objInvoke = clsLoadClass.getMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
            if (i3 > 0) {
                try {
                    Method method = clsLoadClass.getMethod("createSocket", new Class[0]);
                    Method method2 = Socket.class.getMethod(SecurityConstants.SOCKET_CONNECT_ACTION, Class.forName("java.net.SocketAddress"), Integer.TYPE);
                    Object objCreateInetSocketAddress = createInetSocketAddress(str, i2);
                    socket = (Socket) method.invoke(objInvoke, new Object[0]);
                    method2.invoke(socket, objCreateInetSocketAddress, new Integer(i3));
                } catch (NoSuchMethodException e2) {
                }
            }
            if (socket == null) {
                socket = (Socket) clsLoadClass.getMethod("createSocket", String.class, Integer.TYPE).invoke(objInvoke, str, new Integer(i2));
            }
        } else {
            if (i3 > 0) {
                try {
                    Constructor constructor = Socket.class.getConstructor(new Class[0]);
                    Method method3 = Socket.class.getMethod(SecurityConstants.SOCKET_CONNECT_ACTION, Class.forName("java.net.SocketAddress"), Integer.TYPE);
                    Object objCreateInetSocketAddress2 = createInetSocketAddress(str, i2);
                    socket = (Socket) constructor.newInstance(new Object[0]);
                    method3.invoke(socket, objCreateInetSocketAddress2, new Integer(i3));
                } catch (NoSuchMethodException e3) {
                }
            }
            if (socket == null) {
                socket = new Socket(str, i2);
            }
        }
        if (socket instanceof SSLSocket) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            if (!IS_HOSTNAME_VERIFICATION_DISABLED) {
                SSLParameters sSLParameters = sSLSocket.getSSLParameters();
                sSLParameters.setEndpointIdentificationAlgorithm("LDAPS");
                sSLSocket.setSSLParameters(sSLParameters);
            }
            if (i3 > 0) {
                int soTimeout = sSLSocket.getSoTimeout();
                sSLSocket.setSoTimeout(i3);
                sSLSocket.startHandshake();
                sSLSocket.setSoTimeout(soTimeout);
            }
        }
        return socket;
    }

    synchronized int getMsgId() {
        int i2 = this.outMsgId + 1;
        this.outMsgId = i2;
        return i2;
    }

    LdapRequest writeRequest(BerEncoder berEncoder, int i2) throws IOException {
        return writeRequest(berEncoder, i2, false, -1);
    }

    LdapRequest writeRequest(BerEncoder berEncoder, int i2, boolean z2) throws IOException {
        return writeRequest(berEncoder, i2, z2, -1);
    }

    LdapRequest writeRequest(BerEncoder berEncoder, int i2, boolean z2, int i3) throws IOException {
        LdapRequest ldapRequest = new LdapRequest(i2, z2, i3);
        addRequest(ldapRequest);
        if (this.traceFile != null) {
            Ber.dumpBER(this.traceFile, this.traceTagOut, berEncoder.getBuf(), 0, berEncoder.getDataLen());
        }
        unpauseReader();
        try {
            synchronized (this) {
                this.outStream.write(berEncoder.getBuf(), 0, berEncoder.getDataLen());
                this.outStream.flush();
            }
            return ldapRequest;
        } catch (IOException e2) {
            cleanup(null, true);
            this.closureReason = e2;
            throw e2;
        }
    }

    BerDecoder readReply(LdapRequest ldapRequest) throws IOException, NamingException {
        BerDecoder replyBer;
        NamingException namingException = null;
        try {
            replyBer = ldapRequest.getReplyBer(this.readTimeout);
        } catch (InterruptedException e2) {
            throw new InterruptedNamingException("Interrupted during LDAP operation");
        } catch (CommunicationException e3) {
            throw e3;
        } catch (NamingException e4) {
            namingException = e4;
            replyBer = null;
        }
        if (replyBer == null) {
            abandonRequest(ldapRequest, null);
        }
        if (namingException != null) {
            throw namingException;
        }
        return replyBer;
    }

    private synchronized void addRequest(LdapRequest ldapRequest) {
        if (this.pendingRequests == null) {
            this.pendingRequests = ldapRequest;
            ldapRequest.next = null;
        } else {
            ldapRequest.next = this.pendingRequests;
            this.pendingRequests = ldapRequest;
        }
    }

    synchronized LdapRequest findRequest(int i2) {
        LdapRequest ldapRequest = this.pendingRequests;
        while (true) {
            LdapRequest ldapRequest2 = ldapRequest;
            if (ldapRequest2 != null) {
                if (ldapRequest2.msgId == i2) {
                    return ldapRequest2;
                }
                ldapRequest = ldapRequest2.next;
            } else {
                return null;
            }
        }
    }

    synchronized void removeRequest(LdapRequest ldapRequest) {
        LdapRequest ldapRequest2 = null;
        for (LdapRequest ldapRequest3 = this.pendingRequests; ldapRequest3 != null; ldapRequest3 = ldapRequest3.next) {
            if (ldapRequest3 == ldapRequest) {
                ldapRequest3.cancel();
                if (ldapRequest2 != null) {
                    ldapRequest2.next = ldapRequest3.next;
                } else {
                    this.pendingRequests = ldapRequest3.next;
                }
                ldapRequest3.next = null;
            }
            ldapRequest2 = ldapRequest3;
        }
    }

    void abandonRequest(LdapRequest ldapRequest, Control[] controlArr) {
        removeRequest(ldapRequest);
        BerEncoder berEncoder = new BerEncoder(256);
        int msgId = getMsgId();
        try {
            berEncoder.beginSeq(48);
            berEncoder.encodeInt(msgId);
            berEncoder.encodeInt(ldapRequest.msgId, 80);
            if (this.v3) {
                LdapClient.encodeControls(berEncoder, controlArr);
            }
            berEncoder.endSeq();
            if (this.traceFile != null) {
                Ber.dumpBER(this.traceFile, this.traceTagOut, berEncoder.getBuf(), 0, berEncoder.getDataLen());
            }
            synchronized (this) {
                this.outStream.write(berEncoder.getBuf(), 0, berEncoder.getDataLen());
                this.outStream.flush();
            }
        } catch (IOException e2) {
        }
    }

    synchronized void abandonOutstandingReqs(Control[] controlArr) {
        LdapRequest ldapRequest = this.pendingRequests;
        while (ldapRequest != null) {
            abandonRequest(ldapRequest, controlArr);
            LdapRequest ldapRequest2 = ldapRequest.next;
            ldapRequest = ldapRequest2;
            this.pendingRequests = ldapRequest2;
        }
    }

    private void ldapUnbind(Control[] controlArr) {
        BerEncoder berEncoder = new BerEncoder(256);
        int msgId = getMsgId();
        try {
            berEncoder.beginSeq(48);
            berEncoder.encodeInt(msgId);
            berEncoder.encodeByte(66);
            berEncoder.encodeByte(0);
            if (this.v3) {
                LdapClient.encodeControls(berEncoder, controlArr);
            }
            berEncoder.endSeq();
            if (this.traceFile != null) {
                Ber.dumpBER(this.traceFile, this.traceTagOut, berEncoder.getBuf(), 0, berEncoder.getDataLen());
            }
            synchronized (this) {
                this.outStream.write(berEncoder.getBuf(), 0, berEncoder.getDataLen());
                this.outStream.flush();
            }
        } catch (IOException e2) {
        }
    }

    /* JADX WARN: Finally extract failed */
    void cleanup(Control[] controlArr, boolean z2) {
        boolean z3 = false;
        synchronized (this) {
            this.useable = false;
            if (this.sock != null) {
                if (!z2) {
                    try {
                        abandonOutstandingReqs(controlArr);
                    } catch (Throwable th) {
                        try {
                            this.outStream.flush();
                            this.sock.close();
                            unpauseReader();
                        } catch (IOException e2) {
                        }
                        if (!z2) {
                            for (LdapRequest ldapRequest = this.pendingRequests; ldapRequest != null; ldapRequest = ldapRequest.next) {
                                ldapRequest.cancel();
                            }
                        }
                        this.sock = null;
                        throw th;
                    }
                }
                if (this.bound) {
                    ldapUnbind(controlArr);
                }
                try {
                    this.outStream.flush();
                    this.sock.close();
                    unpauseReader();
                } catch (IOException e3) {
                }
                if (!z2) {
                    for (LdapRequest ldapRequest2 = this.pendingRequests; ldapRequest2 != null; ldapRequest2 = ldapRequest2.next) {
                        ldapRequest2.cancel();
                    }
                }
                this.sock = null;
                z3 = z2;
            }
            if (z3) {
                for (LdapRequest ldapRequest3 = this.pendingRequests; ldapRequest3 != null; ldapRequest3 = ldapRequest3.next) {
                    ldapRequest3.close();
                }
            }
        }
        if (z3) {
            this.parent.processConnectionClosure();
        }
    }

    public synchronized void replaceStreams(InputStream inputStream, OutputStream outputStream) {
        this.inStream = inputStream;
        try {
            this.outStream.flush();
        } catch (IOException e2) {
        }
        this.outStream = outputStream;
    }

    public synchronized void replaceStreams(InputStream inputStream, OutputStream outputStream, boolean z2) {
        synchronized (this.startTlsLock) {
            replaceStreams(inputStream, outputStream);
            this.isUpgradedToStartTls = z2;
        }
    }

    public boolean isUpgradedToStartTls() {
        return this.isUpgradedToStartTls;
    }

    private synchronized InputStream getInputStream() {
        return this.inStream;
    }

    private void unpauseReader() throws IOException {
        synchronized (this.pauseLock) {
            if (this.paused) {
                this.paused = false;
                this.pauseLock.notify();
            }
        }
    }

    private void pauseReader() throws IOException {
        this.paused = true;
        while (this.paused) {
            try {
                this.pauseLock.wait();
            } catch (InterruptedException e2) {
                throw new InterruptedIOException("Pause/unpause reader has problems.");
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:77:0x01ec, code lost:
    
        cleanup(null, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0210, code lost:
    
        return;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v80, types: [int] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            Method dump skipped, instructions count: 529
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jndi.ldap.Connection.run():void");
    }

    private static byte[] readFully(InputStream inputStream, int i2) throws IOException {
        int length;
        byte[] bArrCopyOf = new byte[Math.min(i2, 8192)];
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= i2) {
                break;
            }
            if (i4 >= bArrCopyOf.length) {
                length = Math.min(i2 - i4, bArrCopyOf.length + 8192);
                if (bArrCopyOf.length < i4 + length) {
                    bArrCopyOf = Arrays.copyOf(bArrCopyOf, i4 + length);
                }
            } else {
                length = bArrCopyOf.length - i4;
            }
            int i5 = inputStream.read(bArrCopyOf, i4, length);
            if (i5 < 0) {
                if (bArrCopyOf.length != i4) {
                    bArrCopyOf = Arrays.copyOf(bArrCopyOf, i4);
                }
            } else {
                i3 = i4 + i5;
            }
        }
        return bArrCopyOf;
    }
}
