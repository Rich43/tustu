package com.sun.jndi.ldap;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.Rdn;
import javax.naming.spi.NamingManager;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/jndi/ldap/ServiceLocator.class */
class ServiceLocator {
    private static final String SRV_RR = "SRV";
    private static final String[] SRV_RR_ATTR = {SRV_RR};
    private static final Random random = new Random();

    private ServiceLocator() {
    }

    static String mapDnToDomainName(String str) throws InvalidNameException {
        if (str == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        List<Rdn> rdns = new javax.naming.ldap.LdapName(str).getRdns();
        for (int size = rdns.size() - 1; size >= 0; size--) {
            Rdn rdn = rdns.get(size);
            if (rdn.size() == 1 && "dc".equalsIgnoreCase(rdn.getType())) {
                Object value = rdn.getValue();
                if (value instanceof String) {
                    if (value.equals(".") || (stringBuffer.length() == 1 && stringBuffer.charAt(0) == '.')) {
                        stringBuffer.setLength(0);
                    }
                    if (stringBuffer.length() > 0) {
                        stringBuffer.append('.');
                    }
                    stringBuffer.append(value);
                } else {
                    stringBuffer.setLength(0);
                }
            } else {
                stringBuffer.setLength(0);
            }
        }
        if (stringBuffer.length() != 0) {
            return stringBuffer.toString();
        }
        return null;
    }

    static String[] getLdapService(String str, Map<?, ?> map) {
        if (map instanceof Hashtable) {
            return getLdapService(str, (Hashtable<?, ?>) map);
        }
        return getLdapService(str, (Hashtable<?, ?>) new Hashtable(map));
    }

    static String[] getLdapService(String str, Hashtable<?, ?> hashtable) {
        Context uRLContext;
        Attribute attribute;
        if (str == null || str.length() == 0) {
            return null;
        }
        String str2 = "dns:///_ldap._tcp." + str;
        String[] strArrExtractHostports = null;
        try {
            uRLContext = NamingManager.getURLContext("dns", hashtable);
        } catch (NamingException e2) {
        }
        if (!(uRLContext instanceof DirContext)) {
            return null;
        }
        Attributes attributes = ((DirContext) uRLContext).getAttributes(str2, SRV_RR_ATTR);
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

    /* loaded from: rt.jar:com/sun/jndi/ldap/ServiceLocator$SrvRecord.class */
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
