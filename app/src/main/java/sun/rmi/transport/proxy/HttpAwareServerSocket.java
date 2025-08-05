package sun.rmi.transport.proxy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import sun.rmi.runtime.Log;

/* loaded from: rt.jar:sun/rmi/transport/proxy/HttpAwareServerSocket.class */
class HttpAwareServerSocket extends ServerSocket {
    public HttpAwareServerSocket(int i2) throws IOException {
        super(i2);
    }

    public HttpAwareServerSocket(int i2, int i3) throws IOException {
        super(i2, i3);
    }

    @Override // java.net.ServerSocket
    public Socket accept() throws IOException {
        Socket socketAccept = super.accept();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(socketAccept.getInputStream());
        RMIMasterSocketFactory.proxyLog.log(Log.BRIEF, "socket accepted (checking for POST)");
        bufferedInputStream.mark(4);
        boolean z2 = bufferedInputStream.read() == 80 && bufferedInputStream.read() == 79 && bufferedInputStream.read() == 83 && bufferedInputStream.read() == 84;
        bufferedInputStream.reset();
        if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.BRIEF)) {
            RMIMasterSocketFactory.proxyLog.log(Log.BRIEF, z2 ? "POST found, HTTP socket returned" : "POST not found, direct socket returned");
        }
        if (z2) {
            return new HttpReceiveSocket(socketAccept, bufferedInputStream, null);
        }
        return new WrappedSocket(socketAccept, bufferedInputStream, null);
    }

    @Override // java.net.ServerSocket
    public String toString() {
        return "HttpAware" + super.toString();
    }
}
