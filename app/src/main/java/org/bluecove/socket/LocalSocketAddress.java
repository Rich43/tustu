package org.bluecove.socket;

import java.net.SocketAddress;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluecove/socket/LocalSocketAddress.class */
public class LocalSocketAddress extends SocketAddress {
    private static final long serialVersionUID = -8958827981306326634L;
    private String name;
    private boolean abstractNamespace;

    public LocalSocketAddress(String path) {
        this(path, false);
    }

    public LocalSocketAddress(String name, boolean abstractNamespace) {
        if (name == null) {
            throw new NullPointerException("socket Name is null");
        }
        this.name = name;
        this.abstractNamespace = abstractNamespace;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAbstractNamespace() {
        return this.abstractNamespace;
    }

    public boolean equals(Object o2) {
        return (o2 instanceof LocalSocketAddress) && this.abstractNamespace == ((LocalSocketAddress) o2).abstractNamespace && this.name.equals(((LocalSocketAddress) o2).name);
    }

    public int hashCode() {
        return (this.name.hashCode() * 2) ^ (this.abstractNamespace ? 0 : 1);
    }
}
