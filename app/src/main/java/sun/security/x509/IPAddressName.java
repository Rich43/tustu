package sun.security.x509;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import sun.misc.HexDumpEncoder;
import sun.security.util.BitArray;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/IPAddressName.class */
public class IPAddressName implements GeneralNameInterface {
    private byte[] address;
    private boolean isIPv4;
    private String name;
    private static final int MASKSIZE = 16;

    public IPAddressName(DerValue derValue) throws IOException {
        this(derValue.getOctetString());
    }

    public IPAddressName(byte[] bArr) throws IOException {
        if (bArr.length == 4 || bArr.length == 8) {
            this.isIPv4 = true;
        } else if (bArr.length == 16 || bArr.length == 32) {
            this.isIPv4 = false;
        } else {
            throw new IOException("Invalid IPAddressName");
        }
        this.address = bArr;
    }

    public IPAddressName(String str) throws IOException, NumberFormatException, ArrayIndexOutOfBoundsException {
        if (str == null || str.length() == 0) {
            throw new IOException("IPAddress cannot be null or empty");
        }
        if (str.charAt(str.length() - 1) == '/') {
            throw new IOException("Invalid IPAddress: " + str);
        }
        if (str.indexOf(58) >= 0) {
            parseIPv6(str);
            this.isIPv4 = false;
        } else {
            if (str.indexOf(46) >= 0) {
                parseIPv4(str);
                this.isIPv4 = true;
                return;
            }
            throw new IOException("Invalid IPAddress: " + str);
        }
    }

    private void parseIPv4(String str) throws IOException {
        int iIndexOf = str.indexOf(47);
        if (iIndexOf == -1) {
            this.address = InetAddress.getByName(str).getAddress();
            return;
        }
        this.address = new byte[8];
        byte[] address = InetAddress.getByName(str.substring(iIndexOf + 1)).getAddress();
        System.arraycopy(InetAddress.getByName(str.substring(0, iIndexOf)).getAddress(), 0, this.address, 0, 4);
        System.arraycopy(address, 0, this.address, 4, 4);
    }

    private void parseIPv6(String str) throws NumberFormatException, IOException, ArrayIndexOutOfBoundsException {
        int iIndexOf = str.indexOf(47);
        if (iIndexOf == -1) {
            this.address = InetAddress.getByName(str).getAddress();
            return;
        }
        this.address = new byte[32];
        System.arraycopy(InetAddress.getByName(str.substring(0, iIndexOf)).getAddress(), 0, this.address, 0, 16);
        int i2 = Integer.parseInt(str.substring(iIndexOf + 1));
        if (i2 < 0 || i2 > 128) {
            throw new IOException("IPv6Address prefix length (" + i2 + ") in out of valid range [0,128]");
        }
        BitArray bitArray = new BitArray(128);
        for (int i3 = 0; i3 < i2; i3++) {
            bitArray.set(i3, true);
        }
        byte[] byteArray = bitArray.toByteArray();
        for (int i4 = 0; i4 < 16; i4++) {
            this.address[16 + i4] = byteArray[i4];
        }
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int getType() {
        return 7;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putOctetString(this.address);
    }

    public String toString() {
        try {
            return "IPAddress: " + getName();
        } catch (IOException e2) {
            return "IPAddress: " + new HexDumpEncoder().encodeBuffer(this.address);
        }
    }

    public String getName() throws IOException {
        if (this.name != null) {
            return this.name;
        }
        if (this.isIPv4) {
            byte[] bArr = new byte[4];
            System.arraycopy(this.address, 0, bArr, 0, 4);
            this.name = InetAddress.getByAddress(bArr).getHostAddress();
            if (this.address.length == 8) {
                byte[] bArr2 = new byte[4];
                System.arraycopy(this.address, 4, bArr2, 0, 4);
                this.name += "/" + InetAddress.getByAddress(bArr2).getHostAddress();
            }
        } else {
            byte[] bArr3 = new byte[16];
            System.arraycopy(this.address, 0, bArr3, 0, 16);
            this.name = InetAddress.getByAddress(bArr3).getHostAddress();
            if (this.address.length == 32) {
                byte[] bArr4 = new byte[16];
                for (int i2 = 16; i2 < 32; i2++) {
                    bArr4[i2 - 16] = this.address[i2];
                }
                BitArray bitArray = new BitArray(128, bArr4);
                int i3 = 0;
                while (i3 < 128 && bitArray.get(i3)) {
                    i3++;
                }
                this.name += "/" + i3;
                while (i3 < 128) {
                    if (!bitArray.get(i3)) {
                        i3++;
                    } else {
                        throw new IOException("Invalid IPv6 subdomain - set bit " + i3 + " not contiguous");
                    }
                }
            }
        }
        return this.name;
    }

    public byte[] getBytes() {
        return (byte[]) this.address.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IPAddressName)) {
            return false;
        }
        byte[] bArr = ((IPAddressName) obj).address;
        if (bArr.length != this.address.length) {
            return false;
        }
        if (this.address.length == 8 || this.address.length == 32) {
            int length = this.address.length / 2;
            for (int i2 = 0; i2 < length; i2++) {
                if (((byte) (this.address[i2] & this.address[i2 + length])) != ((byte) (bArr[i2] & bArr[i2 + length]))) {
                    return false;
                }
            }
            for (int i3 = length; i3 < this.address.length; i3++) {
                if (this.address[i3] != bArr[i3]) {
                    return false;
                }
            }
            return true;
        }
        return Arrays.equals(bArr, this.address);
    }

    public int hashCode() {
        int i2 = 0;
        for (int i3 = 0; i3 < this.address.length; i3++) {
            i2 += this.address[i3] * i3;
        }
        return i2;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        int i2;
        if (generalNameInterface == null || generalNameInterface.getType() != 7) {
            i2 = -1;
        } else if (((IPAddressName) generalNameInterface).equals(this)) {
            i2 = 0;
        } else {
            byte[] bArr = ((IPAddressName) generalNameInterface).address;
            if (bArr.length == 4 && this.address.length == 4) {
                i2 = 3;
            } else if ((bArr.length == 8 && this.address.length == 8) || (bArr.length == 32 && this.address.length == 32)) {
                boolean z2 = true;
                boolean z3 = true;
                boolean z4 = false;
                boolean z5 = false;
                int length = this.address.length / 2;
                for (int i3 = 0; i3 < length; i3++) {
                    if (((byte) (this.address[i3] & this.address[i3 + length])) != this.address[i3]) {
                        z4 = true;
                    }
                    if (((byte) (bArr[i3] & bArr[i3 + length])) != bArr[i3]) {
                        z5 = true;
                    }
                    if (((byte) (this.address[i3 + length] & bArr[i3 + length])) != this.address[i3 + length] || ((byte) (this.address[i3] & this.address[i3 + length])) != ((byte) (bArr[i3] & this.address[i3 + length]))) {
                        z2 = false;
                    }
                    if (((byte) (bArr[i3 + length] & this.address[i3 + length])) != bArr[i3 + length] || ((byte) (bArr[i3] & bArr[i3 + length])) != ((byte) (this.address[i3] & bArr[i3 + length]))) {
                        z3 = false;
                    }
                }
                if (z4 || z5) {
                    if (z4 && z5) {
                        i2 = 0;
                    } else if (z4) {
                        i2 = 2;
                    } else {
                        i2 = 1;
                    }
                } else if (z2) {
                    i2 = 1;
                } else if (z3) {
                    i2 = 2;
                } else {
                    i2 = 3;
                }
            } else if (bArr.length == 8 || bArr.length == 32) {
                int i4 = 0;
                int length2 = bArr.length / 2;
                while (i4 < length2 && (this.address[i4] & bArr[i4 + length2]) == bArr[i4]) {
                    i4++;
                }
                if (i4 == length2) {
                    i2 = 2;
                } else {
                    i2 = 3;
                }
            } else if (this.address.length == 8 || this.address.length == 32) {
                int i5 = 0;
                int length3 = this.address.length / 2;
                while (i5 < length3 && (bArr[i5] & this.address[i5 + length3]) == this.address[i5]) {
                    i5++;
                }
                if (i5 == length3) {
                    i2 = 1;
                } else {
                    i2 = 3;
                }
            } else {
                i2 = 3;
            }
        }
        return i2;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int subtreeDepth() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("subtreeDepth() not defined for IPAddressName");
    }
}
