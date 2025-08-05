package sun.security.krb5;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.math3.optimization.direct.CMAESOptimizer;
import org.apache.commons.net.tftp.TFTP;
import org.icepdf.core.util.PdfOps;
import sun.security.krb5.internal.KRBError;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.NetClient;

/* loaded from: rt.jar:sun/security/krb5/KdcComm.class */
public final class KdcComm {
    private static int defaultKdcRetryLimit;
    private static int defaultKdcTimeout;
    private static int defaultUdpPrefLimit;
    private static final String BAD_POLICY_KEY = "krb5.kdc.bad.policy";
    private static BpType badPolicy;
    private String realm;
    private static final boolean DEBUG = Krb5.DEBUG;
    private static int tryLessMaxRetries = 1;
    private static int tryLessTimeout = TFTP.DEFAULT_TIMEOUT;

    /* loaded from: rt.jar:sun/security/krb5/KdcComm$BpType.class */
    private enum BpType {
        NONE,
        TRY_LAST,
        TRY_LESS
    }

    static {
        initStatic();
    }

    public static void initStatic() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.krb5.KdcComm.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty(KdcComm.BAD_POLICY_KEY);
            }
        });
        if (str != null) {
            String lowerCase = str.toLowerCase(Locale.ENGLISH);
            String[] strArrSplit = lowerCase.split(CallSiteDescriptor.TOKEN_DELIMITER);
            if ("tryless".equals(strArrSplit[0])) {
                if (strArrSplit.length > 1) {
                    String[] strArrSplit2 = strArrSplit[1].split(",");
                    try {
                        int i2 = Integer.parseInt(strArrSplit2[0]);
                        if (strArrSplit2.length > 1) {
                            tryLessTimeout = Integer.parseInt(strArrSplit2[1]);
                        }
                        tryLessMaxRetries = i2;
                    } catch (NumberFormatException e2) {
                        if (DEBUG) {
                            System.out.println("Invalid krb5.kdc.bad.policy parameter for tryLess: " + lowerCase + ", use default");
                        }
                    }
                }
                badPolicy = BpType.TRY_LESS;
            } else if ("trylast".equals(strArrSplit[0])) {
                badPolicy = BpType.TRY_LAST;
            } else {
                badPolicy = BpType.NONE;
            }
        } else {
            badPolicy = BpType.NONE;
        }
        int timeString = -1;
        int positiveIntString = -1;
        int positiveIntString2 = -1;
        try {
            Config config = Config.getInstance();
            timeString = parseTimeString(config.get("libdefaults", "kdc_timeout"));
            positiveIntString = parsePositiveIntString(config.get("libdefaults", "max_retries"));
            positiveIntString2 = parsePositiveIntString(config.get("libdefaults", "udp_preference_limit"));
        } catch (Exception e3) {
            if (DEBUG) {
                System.out.println("Exception in getting KDC communication settings, using default value " + e3.getMessage());
            }
        }
        defaultKdcTimeout = timeString > 0 ? timeString : CMAESOptimizer.DEFAULT_MAXITERATIONS;
        defaultKdcRetryLimit = positiveIntString > 0 ? positiveIntString : 3;
        if (positiveIntString2 < 0) {
            defaultUdpPrefLimit = Krb5.KDC_DEFAULT_UDP_PREF_LIMIT;
        } else if (positiveIntString2 > 32700) {
            defaultUdpPrefLimit = Krb5.KDC_HARD_UDP_LIMIT;
        } else {
            defaultUdpPrefLimit = positiveIntString2;
        }
        KdcAccessibility.reset();
    }

    public KdcComm(String str) throws KrbException {
        if (str == null) {
            str = Config.getInstance().getDefaultRealm();
            if (str == null) {
                throw new KrbException(60, "Cannot find default realm");
            }
        }
        this.realm = str;
    }

    public byte[] send(byte[] bArr) throws IOException, KrbException {
        int realmSpecificValue = getRealmSpecificValue(this.realm, "udp_preference_limit", defaultUdpPrefLimit);
        return send(bArr, realmSpecificValue > 0 && bArr != null && bArr.length > realmSpecificValue);
    }

    private byte[] send(byte[] bArr, boolean z2) throws Exception {
        if (bArr == null) {
            return null;
        }
        Config config = Config.getInstance();
        if (this.realm == null) {
            this.realm = config.getDefaultRealm();
            if (this.realm == null) {
                throw new KrbException(60, "Cannot find default realm");
            }
        }
        String kDCList = config.getKDCList(this.realm);
        if (kDCList == null) {
            throw new KrbException("Cannot get kdc for realm " + this.realm);
        }
        Iterator it = KdcAccessibility.list(kDCList).iterator();
        if (!it.hasNext()) {
            throw new KrbException("Cannot get kdc for realm " + this.realm);
        }
        byte[] bArrSendIfPossible = null;
        try {
            bArrSendIfPossible = sendIfPossible(bArr, (String) it.next(), z2);
        } catch (Exception e2) {
            boolean z3 = false;
            while (it.hasNext()) {
                try {
                    bArrSendIfPossible = sendIfPossible(bArr, (String) it.next(), z2);
                    z3 = true;
                    break;
                } catch (Exception e3) {
                }
            }
            if (!z3) {
                throw e2;
            }
        }
        if (bArrSendIfPossible == null) {
            throw new IOException("Cannot get a KDC reply");
        }
        return bArrSendIfPossible;
    }

    private byte[] sendIfPossible(byte[] bArr, String str, boolean z2) throws Exception {
        try {
            byte[] bArrSend = send(bArr, str, z2);
            KRBError kRBError = null;
            try {
                kRBError = new KRBError(bArrSend);
            } catch (Exception e2) {
            }
            if (kRBError != null && kRBError.getErrorCode() == 52) {
                bArrSend = send(bArr, str, true);
            }
            KdcAccessibility.removeBad(str);
            return bArrSend;
        } catch (Exception e3) {
            if (DEBUG) {
                System.out.println(">>> KrbKdcReq send: error trying " + str);
                e3.printStackTrace(System.out);
            }
            KdcAccessibility.addBad(str);
            throw e3;
        }
    }

    private byte[] send(byte[] bArr, String str, boolean z2) throws IOException, KrbException {
        String strSubstring;
        int positiveIntString;
        if (bArr == null) {
            return null;
        }
        int i2 = 88;
        int realmSpecificValue = getRealmSpecificValue(this.realm, "max_retries", defaultKdcRetryLimit);
        int realmSpecificValue2 = getRealmSpecificValue(this.realm, "kdc_timeout", defaultKdcTimeout);
        if (badPolicy == BpType.TRY_LESS && KdcAccessibility.isBad(str)) {
            if (realmSpecificValue > tryLessMaxRetries) {
                realmSpecificValue = tryLessMaxRetries;
            }
            if (realmSpecificValue2 > tryLessTimeout) {
                realmSpecificValue2 = tryLessTimeout;
            }
        }
        String strSubstring2 = null;
        if (str.charAt(0) == '[') {
            int iIndexOf = str.indexOf(93, 1);
            if (iIndexOf == -1) {
                throw new IOException("Illegal KDC: " + str);
            }
            strSubstring = str.substring(1, iIndexOf);
            if (iIndexOf != str.length() - 1) {
                if (str.charAt(iIndexOf + 1) != ':') {
                    throw new IOException("Illegal KDC: " + str);
                }
                strSubstring2 = str.substring(iIndexOf + 2);
            }
        } else {
            int iIndexOf2 = str.indexOf(58);
            if (iIndexOf2 == -1 || str.indexOf(58, iIndexOf2 + 1) > 0) {
                strSubstring = str;
            } else {
                strSubstring = str.substring(0, iIndexOf2);
                strSubstring2 = str.substring(iIndexOf2 + 1);
            }
        }
        if (strSubstring2 != null && (positiveIntString = parsePositiveIntString(strSubstring2)) > 0) {
            i2 = positiveIntString;
        }
        if (DEBUG) {
            System.out.println(">>> KrbKdcReq send: kdc=" + strSubstring + (z2 ? " TCP:" : " UDP:") + i2 + ", timeout=" + realmSpecificValue2 + ", number of retries =" + realmSpecificValue + ", #bytes=" + bArr.length);
        }
        try {
            byte[] bArr2 = (byte[]) AccessController.doPrivileged(new KdcCommunication(strSubstring, i2, z2, realmSpecificValue2, realmSpecificValue, bArr));
            if (DEBUG) {
                System.out.println(">>> KrbKdcReq send: #bytes read=" + (bArr2 != null ? bArr2.length : 0));
            }
            return bArr2;
        } catch (PrivilegedActionException e2) {
            Exception exception = e2.getException();
            if (exception instanceof IOException) {
                throw ((IOException) exception);
            }
            throw ((KrbException) exception);
        }
    }

    /* loaded from: rt.jar:sun/security/krb5/KdcComm$KdcCommunication.class */
    private static class KdcCommunication implements PrivilegedExceptionAction<byte[]> {
        private String kdc;
        private int port;
        private boolean useTCP;
        private int timeout;
        private int retries;
        private byte[] obuf;

        public KdcCommunication(String str, int i2, boolean z2, int i3, int i4, byte[] bArr) {
            this.kdc = str;
            this.port = i2;
            this.useTCP = z2;
            this.timeout = i3;
            this.retries = i4;
            this.obuf = bArr;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedExceptionAction
        public byte[] run() throws IOException, KrbException {
            byte[] bArrReceive = null;
            for (int i2 = 1; i2 <= this.retries; i2++) {
                String str = this.useTCP ? "TCP" : "UDP";
                if (KdcComm.DEBUG) {
                    System.out.println(">>> KDCCommunication: kdc=" + this.kdc + " " + str + CallSiteDescriptor.TOKEN_DELIMITER + this.port + ", timeout=" + this.timeout + ",Attempt =" + i2 + ", #bytes=" + this.obuf.length);
                }
                try {
                    NetClient netClient = NetClient.getInstance(str, this.kdc, this.port, this.timeout);
                    Throwable th = null;
                    try {
                        try {
                            netClient.send(this.obuf);
                            bArrReceive = netClient.receive();
                            if (netClient != null) {
                                if (0 != 0) {
                                    try {
                                        netClient.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    netClient.close();
                                }
                            }
                            break;
                        } finally {
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th3;
                    }
                } catch (SocketTimeoutException e2) {
                    if (KdcComm.DEBUG) {
                        System.out.println("SocketTimeOutException with attempt: " + i2);
                    }
                    if (i2 == this.retries) {
                        throw e2;
                    }
                }
            }
            return bArrReceive;
        }
    }

    private static int parseTimeString(String str) {
        if (str == null) {
            return -1;
        }
        if (str.endsWith(PdfOps.s_TOKEN)) {
            int positiveIntString = parsePositiveIntString(str.substring(0, str.length() - 1));
            if (positiveIntString < 0) {
                return -1;
            }
            return positiveIntString * 1000;
        }
        return parsePositiveIntString(str);
    }

    private int getRealmSpecificValue(String str, String str2, int i2) {
        int i3 = i2;
        if (str == null) {
            return i3;
        }
        int positiveIntString = -1;
        try {
            String str3 = Config.getInstance().get("realms", str, str2);
            if (str2.equals("kdc_timeout")) {
                positiveIntString = parseTimeString(str3);
            } else {
                positiveIntString = parsePositiveIntString(str3);
            }
        } catch (Exception e2) {
        }
        if (positiveIntString > 0) {
            i3 = positiveIntString;
        }
        return i3;
    }

    private static int parsePositiveIntString(String str) {
        if (str == null) {
            return -1;
        }
        try {
            int i2 = Integer.parseInt(str);
            if (i2 >= 0) {
                return i2;
            }
            return -1;
        } catch (Exception e2) {
            return -1;
        }
    }

    /* loaded from: rt.jar:sun/security/krb5/KdcComm$KdcAccessibility.class */
    static class KdcAccessibility {
        private static Set<String> bads = new HashSet();

        KdcAccessibility() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized void addBad(String str) {
            if (KdcComm.DEBUG) {
                System.out.println(">>> KdcAccessibility: add " + str);
            }
            bads.add(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized void removeBad(String str) {
            if (KdcComm.DEBUG) {
                System.out.println(">>> KdcAccessibility: remove " + str);
            }
            bads.remove(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized boolean isBad(String str) {
            return bads.contains(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized void reset() {
            if (KdcComm.DEBUG) {
                System.out.println(">>> KdcAccessibility: reset");
            }
            bads.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized List<String> list(String str) {
            StringTokenizer stringTokenizer = new StringTokenizer(str);
            ArrayList arrayList = new ArrayList();
            if (KdcComm.badPolicy == BpType.TRY_LAST) {
                ArrayList arrayList2 = new ArrayList();
                while (stringTokenizer.hasMoreTokens()) {
                    String strNextToken = stringTokenizer.nextToken();
                    if (bads.contains(strNextToken)) {
                        arrayList2.add(strNextToken);
                    } else {
                        arrayList.add(strNextToken);
                    }
                }
                arrayList.addAll(arrayList2);
            } else {
                while (stringTokenizer.hasMoreTokens()) {
                    arrayList.add(stringTokenizer.nextToken());
                }
            }
            return arrayList;
        }
    }
}
