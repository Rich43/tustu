package org.apache.commons.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/DefaultDatagramSocketFactory.class */
public class DefaultDatagramSocketFactory implements DatagramSocketFactory {
    @Override // org.apache.commons.net.DatagramSocketFactory
    public DatagramSocket createDatagramSocket() throws SocketException {
        return new DatagramSocket();
    }

    @Override // org.apache.commons.net.DatagramSocketFactory
    public DatagramSocket createDatagramSocket(int port) throws SocketException {
        return new DatagramSocket(port);
    }

    @Override // org.apache.commons.net.DatagramSocketFactory
    public DatagramSocket createDatagramSocket(int port, InetAddress laddr) throws SocketException {
        return new DatagramSocket(port, laddr);
    }
}
