package sun.security.ssl.krb5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import javax.net.ssl.SSLKeyException;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.kerberos.ServicePermission;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.krb5.Krb5Util;
import sun.security.jgss.krb5.ServiceCreds;
import sun.security.krb5.EncryptedData;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.internal.EncTicketPart;
import sun.security.krb5.internal.Ticket;
import sun.security.ssl.Krb5Helper;
import sun.security.ssl.KrbClientKeyExchangeHelper;
import sun.security.ssl.SSLLogger;
import sun.security.util.SecurityConstants;

/* loaded from: jsse.jar:sun/security/ssl/krb5/KrbClientKeyExchangeHelperImpl.class */
public final class KrbClientKeyExchangeHelperImpl implements KrbClientKeyExchangeHelper {
    private byte[] preMaster;
    private byte[] preMasterEnc;
    private byte[] encodedTicket;
    private KerberosPrincipal peerPrincipal;
    private KerberosPrincipal localPrincipal;

    @Override // sun.security.ssl.KrbClientKeyExchangeHelper
    public void init(byte[] bArr, String str, AccessControlContext accessControlContext) throws AccessControlException, IOException {
        this.preMaster = bArr;
        KerberosTicket serviceTicket = getServiceTicket(str, accessControlContext);
        this.encodedTicket = serviceTicket.getEncoded();
        this.peerPrincipal = serviceTicket.getServer();
        this.localPrincipal = serviceTicket.getClient();
        encryptPremasterSecret(new EncryptionKey(serviceTicket.getSessionKeyType(), serviceTicket.getSessionKey().getEncoded()));
    }

    @Override // sun.security.ssl.KrbClientKeyExchangeHelper
    public void init(byte[] bArr, byte[] bArr2, Object obj, AccessControlContext accessControlContext) throws IOException {
        EncryptionKey encryptionKey;
        EncryptedData encryptedData;
        PrincipalName principalName;
        final ServiceCreds serviceCreds;
        final KerberosPrincipal kerberosPrincipal;
        KerberosKey[] kerberosKeyArr;
        this.encodedTicket = bArr;
        this.preMasterEnc = bArr2;
        try {
            Ticket ticket = new Ticket(bArr);
            encryptedData = ticket.encPart;
            principalName = ticket.sname;
            serviceCreds = (ServiceCreds) obj;
            kerberosPrincipal = new KerberosPrincipal(principalName.toString());
            if (serviceCreds.getName() == null) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    try {
                        securityManager.checkPermission(Krb5Helper.getServicePermission(principalName.toString(), SecurityConstants.SOCKET_ACCEPT_ACTION), accessControlContext);
                    } catch (SecurityException e2) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                            SSLLogger.fine("Permission to access Kerberos secret key denied", new Object[0]);
                        }
                        throw new IOException("Kerberos service not allowed");
                    }
                }
            }
            kerberosKeyArr = (KerberosKey[]) AccessController.doPrivileged(new PrivilegedAction<KerberosKey[]>() { // from class: sun.security.ssl.krb5.KrbClientKeyExchangeHelperImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public KerberosKey[] run2() {
                    return serviceCreds.getKKeys(kerberosPrincipal);
                }
            });
        } catch (Exception e3) {
            encryptionKey = null;
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Error getting the Kerberos session key to decrypt the pre-master secret", new Object[0]);
            }
        }
        if (kerberosKeyArr.length == 0) {
            throw new IOException("Found no key for " + ((Object) kerberosPrincipal) + (serviceCreds.getName() == null ? "" : ", this keytab is for " + serviceCreds.getName() + " only"));
        }
        int eType = encryptedData.getEType();
        try {
            KerberosKey kerberosKeyFindKey = findKey(eType, encryptedData.getKeyVersionNumber(), kerberosKeyArr);
            if (kerberosKeyFindKey == null) {
                throw new IOException("Cannot find key of appropriate type to decrypt ticket - need etype " + eType);
            }
            EncTicketPart encTicketPart = new EncTicketPart(encryptedData.reset(encryptedData.decrypt(new EncryptionKey(eType, kerberosKeyFindKey.getEncoded()), 2)));
            this.peerPrincipal = new KerberosPrincipal(encTicketPart.cname.getName());
            this.localPrincipal = new KerberosPrincipal(principalName.getName());
            encryptionKey = encTicketPart.key;
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("server principal: " + ((Object) principalName), new Object[0]);
                SSLLogger.fine("cname: " + encTicketPart.cname.toString(), new Object[0]);
            }
            if (encryptionKey != null) {
                decryptPremasterSecret(encryptionKey);
            }
        } catch (KrbException e4) {
            throw new IOException("Cannot find key matching version number", e4);
        }
    }

    @Override // sun.security.ssl.KrbClientKeyExchangeHelper
    public byte[] getEncodedTicket() {
        return this.encodedTicket;
    }

    @Override // sun.security.ssl.KrbClientKeyExchangeHelper
    public byte[] getEncryptedPreMasterSecret() {
        return this.preMasterEnc;
    }

    @Override // sun.security.ssl.KrbClientKeyExchangeHelper
    public byte[] getPlainPreMasterSecret() {
        return this.preMaster;
    }

    @Override // sun.security.ssl.KrbClientKeyExchangeHelper
    public KerberosPrincipal getPeerPrincipal() {
        return this.peerPrincipal;
    }

    @Override // sun.security.ssl.KrbClientKeyExchangeHelper
    public KerberosPrincipal getLocalPrincipal() {
        return this.localPrincipal;
    }

    private void encryptPremasterSecret(EncryptionKey encryptionKey) throws IOException {
        if (encryptionKey.getEType() == 16) {
            throw new IOException("session keys with des3-cbc-hmac-sha1-kd encryption type are not supported for TLS Kerberos cipher suites");
        }
        try {
            this.preMasterEnc = new EncryptedData(encryptionKey, this.preMaster, 0).getBytes();
        } catch (KrbException e2) {
            throw ((IOException) new SSLKeyException("Kerberos pre-master secret error").initCause(e2));
        }
    }

    private void decryptPremasterSecret(EncryptionKey encryptionKey) throws IOException {
        if (encryptionKey.getEType() == 16) {
            throw new IOException("session keys with des3-cbc-hmac-sha1-kd encryption type are not supported for TLS Kerberos cipher suites");
        }
        try {
            EncryptedData encryptedData = new EncryptedData(encryptionKey.getEType(), (Integer) null, this.preMasterEnc);
            byte[] bArrDecrypt = encryptedData.decrypt(encryptionKey, 0);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake") && this.preMasterEnc != null) {
                SSLLogger.fine("decrypted premaster secret", bArrDecrypt);
            }
            if (bArrDecrypt.length == 52 && encryptedData.getEType() == 1) {
                if (paddingByteIs(bArrDecrypt, 52, (byte) 4) || paddingByteIs(bArrDecrypt, 52, (byte) 0)) {
                    bArrDecrypt = Arrays.copyOf(bArrDecrypt, 48);
                }
            } else if (bArrDecrypt.length == 56 && encryptedData.getEType() == 3 && paddingByteIs(bArrDecrypt, 56, (byte) 8)) {
                bArrDecrypt = Arrays.copyOf(bArrDecrypt, 48);
            }
            this.preMaster = bArrDecrypt;
        } catch (Exception e2) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Error decrypting the pre-master secret", new Object[0]);
            }
        }
    }

    private static boolean paddingByteIs(byte[] bArr, int i2, byte b2) {
        for (int i3 = 48; i3 < i2; i3++) {
            if (bArr[i3] != b2) {
                return false;
            }
        }
        return true;
    }

    private static KerberosTicket getServiceTicket(String str, final AccessControlContext accessControlContext) throws AccessControlException, IOException {
        if ("localhost".equals(str) || "localhost.localdomain".equals(str)) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Get the local hostname", new Object[0]);
            }
            String str2 = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.ssl.krb5.KrbClientKeyExchangeHelperImpl.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public String run2() {
                    try {
                        return InetAddress.getLocalHost().getHostName();
                    } catch (UnknownHostException e2) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                            SSLLogger.fine("Warning, cannot get the local hostname: " + e2.getMessage(), new Object[0]);
                            return null;
                        }
                        return null;
                    }
                }
            });
            if (str2 != null) {
                str = str2;
            }
        }
        String str3 = "host/" + str;
        try {
            PrincipalName principalName = new PrincipalName(str3, 3);
            String realmAsString = principalName.getRealmAsString();
            final String string = principalName.toString();
            final String str4 = "krbtgt/" + realmAsString + "@" + realmAsString;
            final String str5 = null;
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new ServicePermission(string, "initiate"), accessControlContext);
            }
            try {
                KerberosTicket kerberosTicket = (KerberosTicket) AccessController.doPrivileged(new PrivilegedExceptionAction<KerberosTicket>() { // from class: sun.security.ssl.krb5.KrbClientKeyExchangeHelperImpl.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public KerberosTicket run() throws Exception {
                        return Krb5Util.getTicketFromSubjectAndTgs(GSSCaller.CALLER_SSL_CLIENT, str5, string, str4, accessControlContext);
                    }
                });
                if (kerberosTicket == null) {
                    throw new IOException("Failed to find any kerberos service ticket for " + string);
                }
                return kerberosTicket;
            } catch (PrivilegedActionException e2) {
                IOException iOException = new IOException("Attempt to obtain kerberos service ticket for " + string + " failed!");
                iOException.initCause(e2);
                throw iOException;
            }
        } catch (SecurityException e3) {
            throw e3;
        } catch (Exception e4) {
            IOException iOException2 = new IOException("Invalid service principal name: " + str3);
            iOException2.initCause(e4);
            throw iOException2;
        }
    }

    private static boolean versionMatches(Integer num, int i2) {
        if (num == null || num.intValue() == 0 || i2 == 0) {
            return true;
        }
        return num.equals(Integer.valueOf(i2));
    }

    private static KerberosKey findKey(int i2, Integer num, KerberosKey[] kerberosKeyArr) throws KrbException {
        boolean z2 = false;
        int i3 = 0;
        KerberosKey kerberosKey = null;
        for (int i4 = 0; i4 < kerberosKeyArr.length; i4++) {
            if (i2 == kerberosKeyArr[i4].getKeyType()) {
                int versionNumber = kerberosKeyArr[i4].getVersionNumber();
                z2 = true;
                if (versionMatches(num, versionNumber)) {
                    return kerberosKeyArr[i4];
                }
                if (versionNumber > i3) {
                    kerberosKey = kerberosKeyArr[i4];
                    i3 = versionNumber;
                }
            }
        }
        if (i2 == 1 || i2 == 3) {
            for (int i5 = 0; i5 < kerberosKeyArr.length; i5++) {
                int keyType = kerberosKeyArr[i5].getKeyType();
                if (keyType == 1 || keyType == 3) {
                    int versionNumber2 = kerberosKeyArr[i5].getVersionNumber();
                    z2 = true;
                    if (versionMatches(num, versionNumber2)) {
                        return new KerberosKey(kerberosKeyArr[i5].getPrincipal(), kerberosKeyArr[i5].getEncoded(), i2, versionNumber2);
                    }
                    if (versionNumber2 > i3) {
                        kerberosKey = new KerberosKey(kerberosKeyArr[i5].getPrincipal(), kerberosKeyArr[i5].getEncoded(), i2, versionNumber2);
                        i3 = versionNumber2;
                    }
                }
            }
        }
        if (z2) {
            return kerberosKey;
        }
        return null;
    }
}
