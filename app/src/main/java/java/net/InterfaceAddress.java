package java.net;

/* loaded from: rt.jar:java/net/InterfaceAddress.class */
public class InterfaceAddress {
    private InetAddress address = null;
    private Inet4Address broadcast = null;
    private short maskLength = 0;

    InterfaceAddress() {
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public InetAddress getBroadcast() {
        return this.broadcast;
    }

    public short getNetworkPrefixLength() {
        return this.maskLength;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof InterfaceAddress)) {
            return false;
        }
        InterfaceAddress interfaceAddress = (InterfaceAddress) obj;
        if (this.address == null) {
            if (interfaceAddress.address != null) {
                return false;
            }
        } else if (!this.address.equals(interfaceAddress.address)) {
            return false;
        }
        if (this.broadcast == null) {
            if (interfaceAddress.broadcast != null) {
                return false;
            }
        } else if (!this.broadcast.equals(interfaceAddress.broadcast)) {
            return false;
        }
        if (this.maskLength != interfaceAddress.maskLength) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.address.hashCode() + (this.broadcast != null ? this.broadcast.hashCode() : 0) + this.maskLength;
    }

    public String toString() {
        return ((Object) this.address) + "/" + ((int) this.maskLength) + " [" + ((Object) this.broadcast) + "]";
    }
}
