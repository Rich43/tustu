package sun.rmi.transport.proxy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/* compiled from: CGIHandler.java */
/* loaded from: rt.jar:sun/rmi/transport/proxy/CGIForwardCommand.class */
final class CGIForwardCommand implements CGICommandHandler {
    CGIForwardCommand() {
    }

    @Override // sun.rmi.transport.proxy.CGICommandHandler
    public String getName() {
        return "forward";
    }

    private String getLine(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readLine();
    }

    @Override // sun.rmi.transport.proxy.CGICommandHandler
    public void execute(String str) throws CGIClientException, NumberFormatException, CGIServerException {
        String line;
        if (!CGIHandler.RequestMethod.equals("POST")) {
            throw new CGIClientException("can only forward POST requests");
        }
        try {
            int i2 = Integer.parseInt(str);
            if (i2 <= 0 || i2 > 65535) {
                throw new CGIClientException("invalid port: " + i2);
            }
            if (i2 < 1024) {
                throw new CGIClientException("permission denied for port: " + i2);
            }
            try {
                Socket socket = new Socket(InetAddress.getLocalHost(), i2);
                DataInputStream dataInputStream = new DataInputStream(System.in);
                byte[] bArr = new byte[CGIHandler.ContentLength];
                try {
                    dataInputStream.readFully(bArr);
                    try {
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream.writeBytes("POST / HTTP/1.0\r\n");
                        dataOutputStream.writeBytes("Content-length: " + CGIHandler.ContentLength + "\r\n\r\n");
                        dataOutputStream.write(bArr);
                        dataOutputStream.flush();
                        try {
                            DataInputStream dataInputStream2 = new DataInputStream(socket.getInputStream());
                            String lowerCase = "Content-length:".toLowerCase();
                            boolean z2 = false;
                            int i3 = -1;
                            do {
                                try {
                                    line = getLine(dataInputStream2);
                                    if (line == null) {
                                        throw new CGIServerException("unexpected EOF reading server response");
                                    }
                                    if (line.toLowerCase().startsWith(lowerCase)) {
                                        if (z2) {
                                            throw new CGIServerException("Multiple Content-length entries found.");
                                        }
                                        i3 = Integer.parseInt(line.substring(lowerCase.length()).trim());
                                        z2 = true;
                                    }
                                    if (line.length() == 0 || line.charAt(0) == '\r') {
                                        break;
                                    }
                                } catch (IOException e2) {
                                    throw new CGIServerException("error reading from server", e2);
                                }
                            } while (line.charAt(0) != '\n');
                            if (!z2 || i3 < 0) {
                                throw new CGIServerException("missing or invalid content length in server response");
                            }
                            byte[] bArr2 = new byte[i3];
                            try {
                                dataInputStream2.readFully(bArr2);
                                System.out.println("Status: 200 OK");
                                System.out.println("Content-type: application/octet-stream");
                                System.out.println("");
                                try {
                                    System.out.write(bArr2);
                                    System.out.flush();
                                } catch (IOException e3) {
                                    throw new CGIServerException("error writing response", e3);
                                }
                            } catch (EOFException e4) {
                                throw new CGIServerException("unexpected EOF reading server response", e4);
                            } catch (IOException e5) {
                                throw new CGIServerException("error reading from server", e5);
                            }
                        } catch (IOException e6) {
                            throw new CGIServerException("error reading from server", e6);
                        }
                    } catch (IOException e7) {
                        throw new CGIServerException("error writing to server", e7);
                    }
                } catch (EOFException e8) {
                    throw new CGIClientException("unexpected EOF reading request body", e8);
                } catch (IOException e9) {
                    throw new CGIClientException("error reading request body", e9);
                }
            } catch (IOException e10) {
                throw new CGIServerException("could not connect to local port", e10);
            }
        } catch (NumberFormatException e11) {
            throw new CGIClientException("invalid port number.", e11);
        }
    }
}
