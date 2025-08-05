package sun.security.krb5.internal.ccache;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import sun.misc.IOUtils;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.AuthorizationData;
import sun.security.krb5.internal.AuthorizationDataEntry;
import sun.security.krb5.internal.HostAddress;
import sun.security.krb5.internal.HostAddresses;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.Ticket;
import sun.security.krb5.internal.TicketFlags;
import sun.security.krb5.internal.ccache.CredentialsCache;
import sun.security.krb5.internal.util.KrbDataInputStream;

/* loaded from: rt.jar:sun/security/krb5/internal/ccache/CCacheInputStream.class */
public class CCacheInputStream extends KrbDataInputStream implements FileCCacheConstants {
    private static boolean DEBUG = Krb5.DEBUG;

    public CCacheInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public Tag readTag() throws IOException {
        char[] cArr = new char[1024];
        int i2 = -1;
        Integer num = null;
        Integer num2 = null;
        int i3 = read(2);
        if (i3 < 0) {
            throw new IOException("stop.");
        }
        if (i3 > cArr.length) {
            throw new IOException("Invalid tag length.");
        }
        while (i3 > 0) {
            i2 = read(2);
            int i4 = read(2);
            switch (i2) {
                case 1:
                    num = new Integer(read(4));
                    num2 = new Integer(read(4));
                    break;
            }
            i3 -= 4 + i4;
        }
        return new Tag(i3, i2, num, num2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public PrincipalName readPrincipal(int i2) throws IOException, RealmException {
        int i3;
        if (i2 == 1281) {
            i3 = 0;
        } else {
            i3 = read(4);
        }
        int length4 = readLength4();
        ArrayList arrayList = new ArrayList();
        if (i2 == 1281) {
            length4--;
        }
        for (int i4 = 0; i4 <= length4; i4++) {
            arrayList.add(new String(IOUtils.readExactlyNBytes(this, readLength4())));
        }
        if (arrayList.isEmpty()) {
            throw new IOException("No realm or principal");
        }
        if (isRealm((String) arrayList.get(0))) {
            String str = (String) arrayList.remove(0);
            if (arrayList.isEmpty()) {
                throw new IOException("No principal name components");
            }
            return new PrincipalName(i3, (String[]) arrayList.toArray(new String[arrayList.size()]), new Realm(str));
        }
        try {
            return new PrincipalName(i3, (String[]) arrayList.toArray(new String[arrayList.size()]), Realm.getDefault());
        } catch (RealmException e2) {
            return null;
        }
    }

    boolean isRealm(String str) {
        try {
            new Realm(str);
            StringTokenizer stringTokenizer = new StringTokenizer(str, ".");
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                for (int i2 = 0; i2 < strNextToken.length(); i2++) {
                    if (strNextToken.charAt(i2) >= 141) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    EncryptionKey readKey(int i2) throws IOException {
        int i3 = read(2);
        if (i2 == 1283) {
            read(2);
        }
        return new EncryptionKey(IOUtils.readExactlyNBytes(this, readLength4()), i3, new Integer(i2));
    }

    long[] readTimes() throws IOException {
        return new long[]{read(4) * 1000, read(4) * 1000, read(4) * 1000, read(4) * 1000};
    }

    boolean readskey() throws IOException {
        if (read() == 0) {
            return false;
        }
        return true;
    }

    HostAddress[] readAddr() throws KrbApErrException, IOException {
        int length4 = readLength4();
        if (length4 > 0) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < length4; i2++) {
                int i3 = read(2);
                int length42 = readLength4();
                if (length42 != 4 && length42 != 16) {
                    if (DEBUG) {
                        System.out.println("Incorrect address format.");
                        return null;
                    }
                    return null;
                }
                byte[] bArr = new byte[length42];
                for (int i4 = 0; i4 < length42; i4++) {
                    bArr[i4] = (byte) read(1);
                }
                arrayList.add(new HostAddress(i3, bArr));
            }
            return (HostAddress[]) arrayList.toArray(new HostAddress[arrayList.size()]);
        }
        return null;
    }

    AuthorizationDataEntry[] readAuth() throws IOException {
        int length4 = readLength4();
        if (length4 > 0) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < length4; i2++) {
                arrayList.add(new AuthorizationDataEntry(read(2), IOUtils.readExactlyNBytes(this, readLength4())));
            }
            return (AuthorizationDataEntry[]) arrayList.toArray(new AuthorizationDataEntry[arrayList.size()]);
        }
        return null;
    }

    byte[] readData() throws IOException {
        int length4 = readLength4();
        if (length4 == 0) {
            return null;
        }
        return IOUtils.readExactlyNBytes(this, length4);
    }

    boolean[] readFlags() throws IOException {
        boolean[] zArr = new boolean[32];
        int i2 = read(4);
        if ((i2 & 1073741824) == 1073741824) {
            zArr[1] = true;
        }
        if ((i2 & 536870912) == 536870912) {
            zArr[2] = true;
        }
        if ((i2 & 268435456) == 268435456) {
            zArr[3] = true;
        }
        if ((i2 & 134217728) == 134217728) {
            zArr[4] = true;
        }
        if ((i2 & 67108864) == 67108864) {
            zArr[5] = true;
        }
        if ((i2 & 33554432) == 33554432) {
            zArr[6] = true;
        }
        if ((i2 & 16777216) == 16777216) {
            zArr[7] = true;
        }
        if ((i2 & 8388608) == 8388608) {
            zArr[8] = true;
        }
        if ((i2 & 4194304) == 4194304) {
            zArr[9] = true;
        }
        if ((i2 & 2097152) == 2097152) {
            zArr[10] = true;
        }
        if ((i2 & 1048576) == 1048576) {
            zArr[11] = true;
        }
        if (DEBUG) {
            String str = ">>> CCacheInputStream: readFlags() ";
            if (zArr[1]) {
                str = str + " FORWARDABLE;";
            }
            if (zArr[2]) {
                str = str + " FORWARDED;";
            }
            if (zArr[3]) {
                str = str + " PROXIABLE;";
            }
            if (zArr[4]) {
                str = str + " PROXY;";
            }
            if (zArr[5]) {
                str = str + " MAY_POSTDATE;";
            }
            if (zArr[6]) {
                str = str + " POSTDATED;";
            }
            if (zArr[7]) {
                str = str + " INVALID;";
            }
            if (zArr[8]) {
                str = str + " RENEWABLE;";
            }
            if (zArr[9]) {
                str = str + " INITIAL;";
            }
            if (zArr[10]) {
                str = str + " PRE_AUTH;";
            }
            if (zArr[11]) {
                str = str + " HW_AUTH;";
            }
            System.out.println(str);
        }
        return zArr;
    }

    Object readCred(int i2) throws KrbApErrException, Asn1Exception, IOException, RealmException {
        PrincipalName principal = null;
        try {
            principal = readPrincipal(i2);
        } catch (Exception e2) {
        }
        if (DEBUG) {
            System.out.println(">>>DEBUG <CCacheInputStream>  client principal is " + ((Object) principal));
        }
        PrincipalName principal2 = null;
        try {
            principal2 = readPrincipal(i2);
        } catch (Exception e3) {
        }
        if (DEBUG) {
            System.out.println(">>>DEBUG <CCacheInputStream> server principal is " + ((Object) principal2));
        }
        EncryptionKey key = readKey(i2);
        if (DEBUG) {
            System.out.println(">>>DEBUG <CCacheInputStream> key type: " + key.getEType());
        }
        long[] times = readTimes();
        KerberosTime kerberosTime = new KerberosTime(times[0]);
        KerberosTime kerberosTime2 = times[1] == 0 ? null : new KerberosTime(times[1]);
        KerberosTime kerberosTime3 = new KerberosTime(times[2]);
        KerberosTime kerberosTime4 = times[3] == 0 ? null : new KerberosTime(times[3]);
        if (DEBUG) {
            System.out.println(">>>DEBUG <CCacheInputStream> auth time: " + kerberosTime.toDate().toString());
            System.out.println(">>>DEBUG <CCacheInputStream> start time: " + (kerberosTime2 == null ? FXMLLoader.NULL_KEYWORD : kerberosTime2.toDate().toString()));
            System.out.println(">>>DEBUG <CCacheInputStream> end time: " + kerberosTime3.toDate().toString());
            System.out.println(">>>DEBUG <CCacheInputStream> renew_till time: " + (kerberosTime4 == null ? FXMLLoader.NULL_KEYWORD : kerberosTime4.toDate().toString()));
        }
        boolean z2 = readskey();
        TicketFlags ticketFlags = new TicketFlags(readFlags());
        HostAddress[] addr = readAddr();
        HostAddresses hostAddresses = null;
        if (addr != null) {
            hostAddresses = new HostAddresses(addr);
        }
        AuthorizationDataEntry[] auth = readAuth();
        AuthorizationData authorizationData = null;
        if (auth != null) {
            authorizationData = new AuthorizationData(auth);
        }
        byte[] data = readData();
        byte[] data2 = readData();
        if (principal == null || principal2 == null) {
            return null;
        }
        try {
            if (principal2.getRealmString().equals("X-CACHECONF:")) {
                String[] nameStrings = principal2.getNameStrings();
                if (nameStrings[0].equals("krb5_ccache_conf_data")) {
                    return new CredentialsCache.ConfigEntry(nameStrings[1], nameStrings.length > 2 ? new PrincipalName(nameStrings[2]) : null, data);
                }
            }
            return new Credentials(principal, principal2, key, kerberosTime, kerberosTime2, kerberosTime3, kerberosTime4, z2, ticketFlags, hostAddresses, authorizationData, data != null ? new Ticket(data) : null, data2 != null ? new Ticket(data2) : null);
        } catch (Exception e4) {
            if (DEBUG) {
                e4.printStackTrace(System.out);
                return null;
            }
            return null;
        }
    }
}
