package org.apache.commons.net.smtp;

import java.util.Enumeration;
import java.util.Vector;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/smtp/RelayPath.class */
public final class RelayPath {
    Vector<String> _path = new Vector<>();
    String _emailAddress;

    public RelayPath(String emailAddress) {
        this._emailAddress = emailAddress;
    }

    public void addRelay(String hostname) {
        this._path.addElement(hostname);
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append('<');
        Enumeration<String> hosts = this._path.elements();
        if (hosts.hasMoreElements()) {
            buffer.append('@');
            buffer.append(hosts.nextElement2());
            while (hosts.hasMoreElements()) {
                buffer.append(",@");
                buffer.append(hosts.nextElement2());
            }
            buffer.append(':');
        }
        buffer.append(this._emailAddress);
        buffer.append('>');
        return buffer.toString();
    }
}
