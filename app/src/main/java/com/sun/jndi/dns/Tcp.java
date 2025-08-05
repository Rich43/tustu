package com.sun.jndi.dns;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/* compiled from: DnsClient.java */
/* loaded from: rt.jar:com/sun/jndi/dns/Tcp.class */
class Tcp {
    private Socket sock;
    InputStream in;
    OutputStream out;

    Tcp(InetAddress inetAddress, int i2) throws IOException {
        this.sock = new Socket(inetAddress, i2);
        this.sock.setTcpNoDelay(true);
        this.out = new BufferedOutputStream(this.sock.getOutputStream());
        this.in = new BufferedInputStream(this.sock.getInputStream());
    }

    void close() throws IOException {
        this.sock.close();
    }
}
