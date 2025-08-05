package sun.security.krb5.internal;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import sun.security.krb5.Asn1Exception;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/HostAddress.class */
public class HostAddress implements Cloneable {
    int addrType;
    byte[] address;
    private static InetAddress localInetAddress;
    private static final boolean DEBUG = Krb5.DEBUG;
    private volatile int hashCode;

    private HostAddress(int i2) {
        this.address = null;
        this.hashCode = 0;
    }

    public Object clone() {
        HostAddress hostAddress = new HostAddress(0);
        hostAddress.addrType = this.addrType;
        if (this.address != null) {
            hostAddress.address = (byte[]) this.address.clone();
        }
        return hostAddress;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int i2 = (37 * 17) + this.addrType;
            if (this.address != null) {
                for (int i3 = 0; i3 < this.address.length; i3++) {
                    i2 = (37 * i2) + this.address[i3];
                }
            }
            this.hashCode = i2;
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HostAddress)) {
            return false;
        }
        HostAddress hostAddress = (HostAddress) obj;
        if (this.addrType != hostAddress.addrType) {
            return false;
        }
        if (this.address != null && hostAddress.address == null) {
            return false;
        }
        if (this.address == null && hostAddress.address != null) {
            return false;
        }
        if (this.address != null && hostAddress.address != null) {
            if (this.address.length != hostAddress.address.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.address.length; i2++) {
                if (this.address[i2] != hostAddress.address[i2]) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private static synchronized InetAddress getLocalInetAddress() throws UnknownHostException {
        if (localInetAddress == null) {
            localInetAddress = InetAddress.getLocalHost();
        }
        if (localInetAddress == null) {
            throw new UnknownHostException();
        }
        return localInetAddress;
    }

    public InetAddress getInetAddress() throws UnknownHostException {
        if (this.addrType == 2 || this.addrType == 24) {
            return InetAddress.getByAddress(this.address);
        }
        return null;
    }

    private int getAddrType(InetAddress inetAddress) {
        int i2 = 0;
        if (inetAddress instanceof Inet4Address) {
            i2 = 2;
        } else if (inetAddress instanceof Inet6Address) {
            i2 = 24;
        }
        return i2;
    }

    public HostAddress() throws UnknownHostException {
        this.address = null;
        this.hashCode = 0;
        InetAddress localInetAddress2 = getLocalInetAddress();
        this.addrType = getAddrType(localInetAddress2);
        this.address = localInetAddress2.getAddress();
    }

    public HostAddress(int i2, byte[] bArr) throws KrbApErrException, UnknownHostException {
        this.address = null;
        this.hashCode = 0;
        switch (i2) {
            case 2:
                if (bArr.length != 4) {
                    throw new KrbApErrException(0, "Invalid Internet address");
                }
                break;
            case 5:
                if (bArr.length != 2) {
                    throw new KrbApErrException(0, "Invalid CHAOSnet address");
                }
                break;
            case 6:
                if (bArr.length != 6) {
                    throw new KrbApErrException(0, "Invalid XNS address");
                }
                break;
            case 12:
                if (bArr.length != 2) {
                    throw new KrbApErrException(0, "Invalid DECnet Phase IV address");
                }
                break;
            case 16:
                if (bArr.length != 3) {
                    throw new KrbApErrException(0, "Invalid DDP address");
                }
                break;
            case 24:
                if (bArr.length != 16) {
                    throw new KrbApErrException(0, "Invalid Internet IPv6 address");
                }
                break;
        }
        this.addrType = i2;
        if (bArr != null) {
            this.address = (byte[]) bArr.clone();
        }
        if (DEBUG) {
            if (this.addrType == 2 || this.addrType == 24) {
                System.out.println("Host address is " + ((Object) InetAddress.getByAddress(this.address)));
            }
        }
    }

    public HostAddress(InetAddress inetAddress) {
        this.address = null;
        this.hashCode = 0;
        this.addrType = getAddrType(inetAddress);
        this.address = inetAddress.getAddress();
    }

    public HostAddress(DerValue derValue) throws Asn1Exception, IOException {
        this.address = null;
        this.hashCode = 0;
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.addrType = derValue2.getData().getBigInteger().intValue();
            DerValue derValue3 = derValue.getData().getDerValue();
            if ((derValue3.getTag() & 31) == 1) {
                this.address = derValue3.getData().getOctetString();
                if (derValue.getData().available() > 0) {
                    throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                }
                return;
            }
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(this.addrType);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putOctetString(this.address);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        return derOutputStream4.toByteArray();
    }

    public static HostAddress parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new HostAddress(derValue.getData().getDerValue());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Arrays.toString(this.address));
        sb.append('(').append(this.addrType).append(')');
        return sb.toString();
    }
}
