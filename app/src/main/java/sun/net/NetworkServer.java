package sun.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/* loaded from: rt.jar:sun/net/NetworkServer.class */
public class NetworkServer implements Runnable, Cloneable {
    public Socket clientSocket = null;
    private Thread serverInstance;
    private ServerSocket serverSocket;
    public PrintStream clientOutput;
    public InputStream clientInput;

    public void close() throws IOException {
        this.clientSocket.close();
        this.clientSocket = null;
        this.clientInput = null;
        this.clientOutput = null;
    }

    public boolean clientIsOpen() {
        return this.clientSocket != null;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.serverSocket != null) {
            Thread.currentThread().setPriority(10);
            while (true) {
                try {
                    Socket socketAccept = this.serverSocket.accept();
                    NetworkServer networkServer = (NetworkServer) clone();
                    networkServer.serverSocket = null;
                    networkServer.clientSocket = socketAccept;
                    new Thread(networkServer).start();
                } catch (Exception e2) {
                    System.out.print("Server failure\n");
                    e2.printStackTrace();
                    try {
                        this.serverSocket.close();
                    } catch (IOException e3) {
                    }
                    System.out.print("cs=" + ((Object) this.serverSocket) + "\n");
                    return;
                }
            }
        } else {
            try {
                this.clientOutput = new PrintStream((OutputStream) new BufferedOutputStream(this.clientSocket.getOutputStream()), false, "ISO8859_1");
                this.clientInput = new BufferedInputStream(this.clientSocket.getInputStream());
                serviceRequest();
            } catch (Exception e4) {
            }
            try {
                close();
            } catch (IOException e5) {
            }
        }
    }

    public final void startServer(int i2) throws IOException {
        this.serverSocket = new ServerSocket(i2, 50);
        this.serverInstance = new Thread(this);
        this.serverInstance.start();
    }

    public void serviceRequest() throws IOException {
        byte[] bArr = new byte[300];
        this.clientOutput.print("Echo server " + getClass().getName() + "\n");
        this.clientOutput.flush();
        while (true) {
            int i2 = this.clientInput.read(bArr, 0, bArr.length);
            if (i2 >= 0) {
                this.clientOutput.write(bArr, 0, i2);
            } else {
                return;
            }
        }
    }

    public static void main(String[] strArr) {
        try {
            new NetworkServer().startServer(8888);
        } catch (IOException e2) {
            System.out.print("Server failed: " + ((Object) e2) + "\n");
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
