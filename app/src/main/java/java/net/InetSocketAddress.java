package java.net;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.ObjectStreamField;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/net/InetSocketAddress.class */
public class InetSocketAddress extends SocketAddress {
    private final transient InetSocketAddressHolder holder;
    private static final long serialVersionUID = 5076001401234631237L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("hostname", String.class), new ObjectStreamField("addr", InetAddress.class), new ObjectStreamField(DeploymentDescriptorParser.ATTR_PORT, Integer.TYPE)};
    private static final long FIELDS_OFFSET;
    private static final Unsafe UNSAFE;

    /* loaded from: rt.jar:java/net/InetSocketAddress$InetSocketAddressHolder.class */
    private static class InetSocketAddressHolder {
        private String hostname;
        private InetAddress addr;
        private int port;

        private InetSocketAddressHolder(String str, InetAddress inetAddress, int i2) {
            this.hostname = str;
            this.addr = inetAddress;
            this.port = i2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getPort() {
            return this.port;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public InetAddress getAddress() {
            return this.addr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getHostName() {
            if (this.hostname != null) {
                return this.hostname;
            }
            if (this.addr != null) {
                return this.addr.getHostName();
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getHostString() {
            if (this.hostname != null) {
                return this.hostname;
            }
            if (this.addr != null) {
                if (this.addr.holder().getHostName() != null) {
                    return this.addr.holder().getHostName();
                }
                return this.addr.getHostAddress();
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isUnresolved() {
            return this.addr == null;
        }

        public String toString() {
            if (isUnresolved()) {
                return this.hostname + CallSiteDescriptor.TOKEN_DELIMITER + this.port;
            }
            return this.addr.toString() + CallSiteDescriptor.TOKEN_DELIMITER + this.port;
        }

        public final boolean equals(Object obj) {
            boolean zEquals;
            if (obj == null || !(obj instanceof InetSocketAddressHolder)) {
                return false;
            }
            InetSocketAddressHolder inetSocketAddressHolder = (InetSocketAddressHolder) obj;
            if (this.addr != null) {
                zEquals = this.addr.equals(inetSocketAddressHolder.addr);
            } else if (this.hostname != null) {
                zEquals = inetSocketAddressHolder.addr == null && this.hostname.equalsIgnoreCase(inetSocketAddressHolder.hostname);
            } else {
                zEquals = inetSocketAddressHolder.addr == null && inetSocketAddressHolder.hostname == null;
            }
            return zEquals && this.port == inetSocketAddressHolder.port;
        }

        public final int hashCode() {
            if (this.addr != null) {
                return this.addr.hashCode() + this.port;
            }
            if (this.hostname != null) {
                return this.hostname.toLowerCase().hashCode() + this.port;
            }
            return this.port;
        }
    }

    private static int checkPort(int i2) {
        if (i2 < 0 || i2 > 65535) {
            throw new IllegalArgumentException("port out of range:" + i2);
        }
        return i2;
    }

    private static String checkHost(String str) {
        if (str == null) {
            throw new IllegalArgumentException("hostname can't be null");
        }
        return str;
    }

    public InetSocketAddress(int i2) {
        this(InetAddress.anyLocalAddress(), i2);
    }

    public InetSocketAddress(InetAddress inetAddress, int i2) {
        this.holder = new InetSocketAddressHolder(null, inetAddress == null ? InetAddress.anyLocalAddress() : inetAddress, checkPort(i2));
    }

    public InetSocketAddress(String str, int i2) {
        checkHost(str);
        InetAddress byName = null;
        String str2 = null;
        try {
            byName = InetAddress.getByName(str);
        } catch (UnknownHostException e2) {
            str2 = str;
        }
        this.holder = new InetSocketAddressHolder(str2, byName, checkPort(i2));
    }

    private InetSocketAddress(int i2, String str) {
        this.holder = new InetSocketAddressHolder(str, null, i2);
    }

    public static InetSocketAddress createUnresolved(String str, int i2) {
        return new InetSocketAddress(checkPort(i2), checkHost(str));
    }

    static {
        try {
            Unsafe unsafe = Unsafe.getUnsafe();
            FIELDS_OFFSET = unsafe.objectFieldOffset(InetSocketAddress.class.getDeclaredField("holder"));
            UNSAFE = unsafe;
        } catch (ReflectiveOperationException e2) {
            throw new Error(e2);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("hostname", this.holder.hostname);
        putFieldPutFields.put("addr", this.holder.addr);
        putFieldPutFields.put(DeploymentDescriptorParser.ATTR_PORT, this.holder.port);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        String str = (String) fields.get("hostname", (Object) null);
        InetAddress inetAddress = (InetAddress) fields.get("addr", (Object) null);
        int i2 = fields.get(DeploymentDescriptorParser.ATTR_PORT, -1);
        checkPort(i2);
        if (str == null && inetAddress == null) {
            throw new InvalidObjectException("hostname and addr can't both be null");
        }
        UNSAFE.putObject(this, FIELDS_OFFSET, new InetSocketAddressHolder(str, inetAddress, i2));
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Stream data required");
    }

    public final int getPort() {
        return this.holder.getPort();
    }

    public final InetAddress getAddress() {
        return this.holder.getAddress();
    }

    public final String getHostName() {
        return this.holder.getHostName();
    }

    public final String getHostString() {
        return this.holder.getHostString();
    }

    public final boolean isUnresolved() {
        return this.holder.isUnresolved();
    }

    public String toString() {
        return this.holder.toString();
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof InetSocketAddress)) {
            return false;
        }
        return this.holder.equals(((InetSocketAddress) obj).holder);
    }

    public final int hashCode() {
        return this.holder.hashCode();
    }
}
