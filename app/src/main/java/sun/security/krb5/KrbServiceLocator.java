package sun.security.krb5;

import java.net.SocketPermission;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.spi.NamingManager;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.krb5.internal.Krb5;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/security/krb5/KrbServiceLocator.class */
class KrbServiceLocator {
    private static final String SRV_RR = "SRV";
    private static final String[] SRV_RR_ATTR = {SRV_RR};
    private static final String SRV_TXT = "TXT";
    private static final String[] SRV_TXT_ATTR = {SRV_TXT};
    private static final Random random = new Random();
    private static final boolean DEBUG = Krb5.DEBUG;

    private KrbServiceLocator() {
    }

    static String[] getKerberosService(String str) {
        Context uRLContext;
        Attribute attribute;
        String str2 = "dns:///_kerberos." + str;
        String[] strArr = null;
        try {
            uRLContext = NamingManager.getURLContext("dns", new Hashtable(0));
        } catch (NamingException e2) {
        }
        if (!(uRLContext instanceof DirContext)) {
            return null;
        }
        try {
            Attributes attributes = (Attributes) AccessController.doPrivileged(() -> {
                return ((DirContext) uRLContext).getAttributes(str2, SRV_TXT_ATTR);
            }, (AccessControlContext) null, new SocketPermission("*", SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION));
            if (attributes != null && (attribute = attributes.get(SRV_TXT)) != null) {
                int size = attribute.size();
                String[] strArr2 = new String[size];
                int i2 = 0;
                for (int i3 = 0; i3 < size; i3++) {
                    try {
                        strArr2[i2] = (String) attribute.get(i3);
                        i2++;
                    } catch (Exception e3) {
                    }
                }
                int i4 = i2;
                if (i4 < size) {
                    String[] strArr3 = new String[i4];
                    System.arraycopy(strArr2, 0, strArr3, 0, i4);
                    strArr = strArr3;
                } else {
                    strArr = strArr2;
                }
            }
            return strArr;
        } catch (PrivilegedActionException e4) {
            throw ((NamingException) e4.getCause());
        }
    }

    static String[] getKerberosService(String str, String str2) {
        Context uRLContext;
        Attribute attribute;
        String str3 = "dns:///_kerberos." + str2 + "." + str;
        String[] strArrExtractHostports = null;
        try {
            uRLContext = NamingManager.getURLContext("dns", new Hashtable(0));
        } catch (NamingException e2) {
        }
        if (!(uRLContext instanceof DirContext)) {
            return null;
        }
        try {
            Attributes attributes = (Attributes) AccessController.doPrivileged(() -> {
                return ((DirContext) uRLContext).getAttributes(str3, SRV_RR_ATTR);
            }, (AccessControlContext) null, new SocketPermission("*", SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION));
            if (attributes != null && (attribute = attributes.get(SRV_RR)) != null) {
                int size = attribute.size();
                SrvRecord[] srvRecordArr = new SrvRecord[size];
                int i2 = 0;
                for (int i3 = 0; i3 < size; i3++) {
                    try {
                        srvRecordArr[i2] = new SrvRecord((String) attribute.get(i3));
                        i2++;
                    } catch (Exception e3) {
                    }
                }
                int i4 = i2;
                if (i4 < size) {
                    SrvRecord[] srvRecordArr2 = new SrvRecord[i4];
                    System.arraycopy(srvRecordArr, 0, srvRecordArr2, 0, i4);
                    srvRecordArr = srvRecordArr2;
                }
                if (i4 > 1) {
                    Arrays.sort(srvRecordArr);
                }
                strArrExtractHostports = extractHostports(srvRecordArr);
            }
            return strArrExtractHostports;
        } catch (PrivilegedActionException e4) {
            throw ((NamingException) e4.getCause());
        }
    }

    private static String[] extractHostports(SrvRecord[] srvRecordArr) {
        String[] strArr = null;
        int i2 = 0;
        int i3 = 0;
        while (i3 < srvRecordArr.length) {
            if (strArr == null) {
                strArr = new String[srvRecordArr.length];
            }
            int i4 = i3;
            while (i3 < srvRecordArr.length - 1 && srvRecordArr[i3].priority == srvRecordArr[i3 + 1].priority) {
                i3++;
            }
            int i5 = i3;
            int i6 = (i5 - i4) + 1;
            for (int i7 = 0; i7 < i6; i7++) {
                int i8 = i2;
                i2++;
                strArr[i8] = selectHostport(srvRecordArr, i4, i5);
            }
            i3++;
        }
        return strArr;
    }

    private static String selectHostport(SrvRecord[] srvRecordArr, int i2, int i3) {
        if (i2 == i3) {
            return srvRecordArr[i2].hostport;
        }
        int i4 = 0;
        for (int i5 = i2; i5 <= i3; i5++) {
            if (srvRecordArr[i5] != null) {
                i4 += srvRecordArr[i5].weight;
                srvRecordArr[i5].sum = i4;
            }
        }
        String str = null;
        int iNextInt = i4 == 0 ? 0 : random.nextInt(i4 + 1);
        int i6 = i2;
        while (true) {
            if (i6 > i3) {
                break;
            }
            if (srvRecordArr[i6] == null || srvRecordArr[i6].sum < iNextInt) {
                i6++;
            } else {
                str = srvRecordArr[i6].hostport;
                srvRecordArr[i6] = null;
                break;
            }
        }
        return str;
    }

    /* loaded from: rt.jar:sun/security/krb5/KrbServiceLocator$SrvRecord.class */
    static class SrvRecord implements Comparable<SrvRecord> {
        int priority;
        int weight;
        int sum;
        String hostport;

        SrvRecord(String str) throws Exception {
            StringTokenizer stringTokenizer = new StringTokenizer(str, " ");
            if (stringTokenizer.countTokens() == 4) {
                this.priority = Integer.parseInt(stringTokenizer.nextToken());
                this.weight = Integer.parseInt(stringTokenizer.nextToken());
                this.hostport = stringTokenizer.nextToken() + CallSiteDescriptor.TOKEN_DELIMITER + stringTokenizer.nextToken();
                return;
            }
            throw new IllegalArgumentException();
        }

        @Override // java.lang.Comparable
        public int compareTo(SrvRecord srvRecord) {
            if (this.priority > srvRecord.priority) {
                return 1;
            }
            if (this.priority < srvRecord.priority) {
                return -1;
            }
            if (this.weight == 0 && srvRecord.weight != 0) {
                return -1;
            }
            if (this.weight != 0 && srvRecord.weight == 0) {
                return 1;
            }
            return 0;
        }
    }
}
