package sun.security.krb5.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Vector;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.Config;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.internal.ccache.CCacheOutputStream;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/HostAddresses.class */
public class HostAddresses implements Cloneable {
    private static boolean DEBUG = Krb5.DEBUG;
    private HostAddress[] addresses;
    private volatile int hashCode;

    public HostAddresses(HostAddress[] hostAddressArr) throws IOException {
        this.addresses = null;
        this.hashCode = 0;
        if (hostAddressArr != null) {
            this.addresses = new HostAddress[hostAddressArr.length];
            for (int i2 = 0; i2 < hostAddressArr.length; i2++) {
                if (hostAddressArr[i2] == null) {
                    throw new IOException("Cannot create a HostAddress");
                }
                this.addresses[i2] = (HostAddress) hostAddressArr[i2].clone();
            }
        }
    }

    public HostAddresses() throws UnknownHostException {
        this.addresses = null;
        this.hashCode = 0;
        this.addresses = new HostAddress[1];
        this.addresses[0] = new HostAddress();
    }

    private HostAddresses(int i2) {
        this.addresses = null;
        this.hashCode = 0;
    }

    public HostAddresses(PrincipalName principalName) throws UnknownHostException, KrbException {
        this.addresses = null;
        this.hashCode = 0;
        String[] nameStrings = principalName.getNameStrings();
        if (principalName.getNameType() != 3 || nameStrings.length < 2) {
            throw new KrbException(60, "Bad name");
        }
        InetAddress[] allByName = InetAddress.getAllByName(nameStrings[1]);
        HostAddress[] hostAddressArr = new HostAddress[allByName.length];
        for (int i2 = 0; i2 < allByName.length; i2++) {
            hostAddressArr[i2] = new HostAddress(allByName[i2]);
        }
        this.addresses = hostAddressArr;
    }

    public Object clone() {
        HostAddresses hostAddresses = new HostAddresses(0);
        if (this.addresses != null) {
            hostAddresses.addresses = new HostAddress[this.addresses.length];
            for (int i2 = 0; i2 < this.addresses.length; i2++) {
                hostAddresses.addresses[i2] = (HostAddress) this.addresses[i2].clone();
            }
        }
        return hostAddresses;
    }

    public boolean inList(HostAddress hostAddress) {
        if (this.addresses != null) {
            for (int i2 = 0; i2 < this.addresses.length; i2++) {
                if (this.addresses[i2].equals(hostAddress)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int iHashCode = 17;
            if (this.addresses != null) {
                for (int i2 = 0; i2 < this.addresses.length; i2++) {
                    iHashCode = (37 * iHashCode) + this.addresses[i2].hashCode();
                }
            }
            this.hashCode = iHashCode;
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HostAddresses)) {
            return false;
        }
        HostAddresses hostAddresses = (HostAddresses) obj;
        if (this.addresses == null && hostAddresses.addresses != null) {
            return false;
        }
        if (this.addresses != null && hostAddresses.addresses == null) {
            return false;
        }
        if (this.addresses != null && hostAddresses.addresses != null) {
            if (this.addresses.length != hostAddresses.addresses.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.addresses.length; i2++) {
                if (!this.addresses[i2].equals(hostAddresses.addresses[i2])) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public HostAddresses(DerValue derValue) throws Asn1Exception, IOException {
        this.addresses = null;
        this.hashCode = 0;
        Vector vector = new Vector();
        while (derValue.getData().available() > 0) {
            vector.addElement(new HostAddress(derValue.getData().getDerValue()));
        }
        if (vector.size() > 0) {
            this.addresses = new HostAddress[vector.size()];
            vector.copyInto(this.addresses);
        }
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        if (this.addresses != null && this.addresses.length > 0) {
            for (int i2 = 0; i2 < this.addresses.length; i2++) {
                derOutputStream.write(this.addresses[i2].asn1Encode());
            }
        }
        derOutputStream2.write((byte) 48, derOutputStream);
        return derOutputStream2.toByteArray();
    }

    public static HostAddresses parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new HostAddresses(derValue.getData().getDerValue());
    }

    public void writeAddrs(CCacheOutputStream cCacheOutputStream) throws IOException {
        if (this.addresses == null || this.addresses.length == 0) {
            cCacheOutputStream.write32(0);
            return;
        }
        cCacheOutputStream.write32(this.addresses.length);
        for (int i2 = 0; i2 < this.addresses.length; i2++) {
            cCacheOutputStream.write16(this.addresses[i2].addrType);
            cCacheOutputStream.write32(this.addresses[i2].address.length);
            cCacheOutputStream.write(this.addresses[i2].address, 0, this.addresses[i2].address.length);
        }
    }

    public InetAddress[] getInetAddresses() {
        if (this.addresses == null || this.addresses.length == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(this.addresses.length);
        for (int i2 = 0; i2 < this.addresses.length; i2++) {
            try {
                if (this.addresses[i2].addrType == 2 || this.addresses[i2].addrType == 24) {
                    arrayList.add(this.addresses[i2].getInetAddress());
                }
            } catch (UnknownHostException e2) {
                return null;
            }
        }
        return (InetAddress[]) arrayList.toArray(new InetAddress[arrayList.size()]);
    }

    public static HostAddresses getLocalAddresses() throws IOException {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        try {
            if (DEBUG) {
                System.out.println(">>> KrbKdcReq local addresses are:");
            }
            String all = Config.getInstance().getAll("libdefaults", "extra_addresses");
            if (all != null) {
                for (String str : all.split("\\s+")) {
                    linkedHashSet.add(InetAddress.getByName(str));
                    if (DEBUG) {
                        System.out.println("   extra_addresses: " + ((Object) InetAddress.getByName(str)));
                    }
                }
            }
            Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                NetworkInterface networkInterface = (NetworkInterface) it.next();
                if (DEBUG) {
                    System.out.println("   NetworkInterface " + ((Object) networkInterface) + CallSiteDescriptor.TOKEN_DELIMITER);
                    System.out.println(GoToActionDialog.EMPTY_DESTINATION + ((Object) Collections.list(networkInterface.getInetAddresses())));
                }
                linkedHashSet.addAll(Collections.list(networkInterface.getInetAddresses()));
            }
            return new HostAddresses((InetAddress[]) linkedHashSet.toArray(new InetAddress[linkedHashSet.size()]));
        } catch (Exception e2) {
            throw new IOException(e2.toString());
        }
    }

    public HostAddresses(InetAddress[] inetAddressArr) {
        this.addresses = null;
        this.hashCode = 0;
        if (inetAddressArr == null) {
            this.addresses = null;
            return;
        }
        this.addresses = new HostAddress[inetAddressArr.length];
        for (int i2 = 0; i2 < inetAddressArr.length; i2++) {
            this.addresses[i2] = new HostAddress(inetAddressArr[i2]);
        }
    }

    public String toString() {
        return Arrays.toString(this.addresses);
    }
}
