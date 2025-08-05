package org.apache.commons.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/DatagramSocketFactory.class */
public interface DatagramSocketFactory {
    DatagramSocket createDatagramSocket() throws SocketException;

    DatagramSocket createDatagramSocket(int i2) throws SocketException;

    DatagramSocket createDatagramSocket(int i2, InetAddress inetAddress) throws SocketException;
}
