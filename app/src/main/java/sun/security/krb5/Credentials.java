package sun.security.krb5;

import java.io.IOException;
import java.net.InetAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.net.ftp.FTPClientConfig;
import sun.security.action.GetPropertyAction;
import sun.security.krb5.internal.AuthorizationData;
import sun.security.krb5.internal.CredentialsUtil;
import sun.security.krb5.internal.HostAddresses;
import sun.security.krb5.internal.KDCOptions;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.Ticket;
import sun.security.krb5.internal.TicketFlags;
import sun.security.krb5.internal.ccache.CredentialsCache;
import sun.security.krb5.internal.crypto.EType;

/* loaded from: rt.jar:sun/security/krb5/Credentials.class */
public class Credentials {
    Ticket ticket;
    PrincipalName client;
    PrincipalName clientAlias;
    PrincipalName server;
    PrincipalName serverAlias;
    EncryptionKey key;
    TicketFlags flags;
    KerberosTime authTime;
    KerberosTime startTime;
    KerberosTime endTime;
    KerberosTime renewTill;
    HostAddresses cAddr;
    AuthorizationData authzData;
    private static CredentialsCache cache;
    private Credentials proxy;
    private static boolean DEBUG = Krb5.DEBUG;
    static boolean alreadyLoaded = false;
    private static boolean alreadyTried = false;

    private static native Credentials acquireDefaultNativeCreds(int[] iArr);

    public Credentials getProxy() {
        return this.proxy;
    }

    public Credentials setProxy(Credentials credentials) {
        this.proxy = credentials;
        return this;
    }

    public Credentials(Ticket ticket, PrincipalName principalName, PrincipalName principalName2, PrincipalName principalName3, PrincipalName principalName4, EncryptionKey encryptionKey, TicketFlags ticketFlags, KerberosTime kerberosTime, KerberosTime kerberosTime2, KerberosTime kerberosTime3, KerberosTime kerberosTime4, HostAddresses hostAddresses, AuthorizationData authorizationData) {
        this(ticket, principalName, principalName2, principalName3, principalName4, encryptionKey, ticketFlags, kerberosTime, kerberosTime2, kerberosTime3, kerberosTime4, hostAddresses);
        this.authzData = authorizationData;
    }

    public Credentials(Ticket ticket, PrincipalName principalName, PrincipalName principalName2, PrincipalName principalName3, PrincipalName principalName4, EncryptionKey encryptionKey, TicketFlags ticketFlags, KerberosTime kerberosTime, KerberosTime kerberosTime2, KerberosTime kerberosTime3, KerberosTime kerberosTime4, HostAddresses hostAddresses) {
        this.proxy = null;
        this.ticket = ticket;
        this.client = principalName;
        this.clientAlias = principalName2;
        this.server = principalName3;
        this.serverAlias = principalName4;
        this.key = encryptionKey;
        this.flags = ticketFlags;
        this.authTime = kerberosTime;
        this.startTime = kerberosTime2;
        this.endTime = kerberosTime3;
        this.renewTill = kerberosTime4;
        this.cAddr = hostAddresses;
    }

    public Credentials(byte[] bArr, String str, String str2, String str3, String str4, byte[] bArr2, int i2, boolean[] zArr, Date date, Date date2, Date date3, Date date4, InetAddress[] inetAddressArr) throws IOException, KrbException {
        this(new Ticket(bArr), new PrincipalName(str, 1), str2 == null ? null : new PrincipalName(str2, 1), new PrincipalName(str3, 2), str4 == null ? null : new PrincipalName(str4, 2), new EncryptionKey(i2, bArr2), zArr == null ? null : new TicketFlags(zArr), date == null ? null : new KerberosTime(date), date2 == null ? null : new KerberosTime(date2), date3 == null ? null : new KerberosTime(date3), date4 == null ? null : new KerberosTime(date4), null);
    }

    public final PrincipalName getClient() {
        return this.client;
    }

    public final PrincipalName getClientAlias() {
        return this.clientAlias;
    }

    public final PrincipalName getServer() {
        return this.server;
    }

    public final PrincipalName getServerAlias() {
        return this.serverAlias;
    }

    public final EncryptionKey getSessionKey() {
        return this.key;
    }

    public final Date getAuthTime() {
        if (this.authTime != null) {
            return this.authTime.toDate();
        }
        return null;
    }

    public final Date getStartTime() {
        if (this.startTime != null) {
            return this.startTime.toDate();
        }
        return null;
    }

    public final Date getEndTime() {
        if (this.endTime != null) {
            return this.endTime.toDate();
        }
        return null;
    }

    public final Date getRenewTill() {
        if (this.renewTill != null) {
            return this.renewTill.toDate();
        }
        return null;
    }

    public final boolean[] getFlags() {
        if (this.flags == null) {
            return null;
        }
        return this.flags.toBooleanArray();
    }

    public final InetAddress[] getClientAddresses() {
        if (this.cAddr == null) {
            return null;
        }
        return this.cAddr.getInetAddresses();
    }

    public final byte[] getEncoded() {
        byte[] bArrAsn1Encode = null;
        try {
            bArrAsn1Encode = this.ticket.asn1Encode();
        } catch (IOException e2) {
            if (DEBUG) {
                System.out.println(e2);
            }
        } catch (Asn1Exception e3) {
            if (DEBUG) {
                System.out.println(e3);
            }
        }
        return bArrAsn1Encode;
    }

    public boolean isForwardable() {
        return this.flags.get(1);
    }

    public boolean isRenewable() {
        return this.flags.get(8);
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public TicketFlags getTicketFlags() {
        return this.flags;
    }

    public AuthorizationData getAuthzData() {
        return this.authzData;
    }

    public boolean checkDelegate() {
        return this.flags.get(13);
    }

    public void resetDelegate() {
        this.flags.set(13, false);
    }

    public Credentials renew() throws IOException, ArrayIndexOutOfBoundsException, KrbException {
        KDCOptions kDCOptions = new KDCOptions();
        kDCOptions.set(30, true);
        kDCOptions.set(8, true);
        return new KrbTgsReq(kDCOptions, this, this.server, this.serverAlias, null, null, null, null, this.cAddr, null, null, null).sendAndGetCreds();
    }

    public static Credentials acquireTGTFromCache(PrincipalName principalName, String str) throws IOException, KrbException {
        Credentials initialCreds;
        if (str == null) {
            String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("os.name"));
            if (str2.toUpperCase(Locale.ENGLISH).startsWith(FTPClientConfig.SYST_NT) || str2.toUpperCase(Locale.ENGLISH).contains("OS X")) {
                Credentials credentialsAcquireDefaultCreds = acquireDefaultCreds();
                if (credentialsAcquireDefaultCreds == null) {
                    if (DEBUG) {
                        System.out.println(">>> Found no TGT's in LSA");
                        return null;
                    }
                    return null;
                }
                if (principalName != null) {
                    if (credentialsAcquireDefaultCreds.getClient().equals(principalName)) {
                        if (DEBUG) {
                            System.out.println(">>> Obtained TGT from LSA: " + ((Object) credentialsAcquireDefaultCreds));
                        }
                        return credentialsAcquireDefaultCreds;
                    }
                    if (DEBUG) {
                        System.out.println(">>> LSA contains TGT for " + ((Object) credentialsAcquireDefaultCreds.getClient()) + " not " + ((Object) principalName));
                        return null;
                    }
                    return null;
                }
                if (DEBUG) {
                    System.out.println(">>> Obtained TGT from LSA: " + ((Object) credentialsAcquireDefaultCreds));
                }
                return credentialsAcquireDefaultCreds;
            }
        }
        CredentialsCache credentialsCache = CredentialsCache.getInstance(principalName, str);
        if (credentialsCache == null || (initialCreds = credentialsCache.getInitialCreds()) == null) {
            return null;
        }
        if (EType.isSupported(initialCreds.key.getEType())) {
            return initialCreds;
        }
        if (DEBUG) {
            System.out.println(">>> unsupported key type found the default TGT: " + initialCreds.key.getEType());
            return null;
        }
        return null;
    }

    public static synchronized Credentials acquireDefaultCreds() {
        Credentials initialCreds;
        Credentials credentialsAcquireDefaultNativeCreds = null;
        if (cache == null) {
            cache = CredentialsCache.getInstance();
        }
        if (cache != null && (initialCreds = cache.getInitialCreds()) != null) {
            if (DEBUG) {
                System.out.println(">>> KrbCreds found the default ticket granting ticket in credential cache.");
            }
            if (EType.isSupported(initialCreds.key.getEType())) {
                credentialsAcquireDefaultNativeCreds = initialCreds;
            } else if (DEBUG) {
                System.out.println(">>> unsupported key type found the default TGT: " + initialCreds.key.getEType());
            }
        }
        if (credentialsAcquireDefaultNativeCreds == null) {
            if (!alreadyTried) {
                try {
                    ensureLoaded();
                } catch (Exception e2) {
                    if (DEBUG) {
                        System.out.println("Can not load credentials cache");
                        e2.printStackTrace();
                    }
                    alreadyTried = true;
                }
            }
            if (alreadyLoaded) {
                if (DEBUG) {
                    System.out.println(">> Acquire default native Credentials");
                }
                try {
                    credentialsAcquireDefaultNativeCreds = acquireDefaultNativeCreds(EType.getDefaults("default_tkt_enctypes"));
                } catch (KrbException e3) {
                }
            }
        }
        return credentialsAcquireDefaultNativeCreds;
    }

    public static Credentials acquireServiceCreds(String str, Credentials credentials) throws IOException, KrbException {
        return CredentialsUtil.acquireServiceCreds(str, credentials);
    }

    public static Credentials acquireS4U2selfCreds(PrincipalName principalName, Credentials credentials) throws IOException, KrbException {
        return CredentialsUtil.acquireS4U2selfCreds(principalName, credentials);
    }

    public static Credentials acquireS4U2proxyCreds(String str, Ticket ticket, PrincipalName principalName, Credentials credentials) throws IOException, KrbException {
        return CredentialsUtil.acquireS4U2proxyCreds(str, ticket, principalName, credentials);
    }

    public CredentialsCache getCache() {
        return cache;
    }

    public static void printDebug(Credentials credentials) {
        System.out.println(">>> DEBUG: ----Credentials----");
        System.out.println("\tclient: " + credentials.client.toString());
        if (credentials.clientAlias != null) {
            System.out.println("\tclient alias: " + credentials.clientAlias.toString());
        }
        System.out.println("\tserver: " + credentials.server.toString());
        if (credentials.serverAlias != null) {
            System.out.println("\tserver alias: " + credentials.serverAlias.toString());
        }
        System.out.println("\tticket: sname: " + credentials.ticket.sname.toString());
        if (credentials.startTime != null) {
            System.out.println("\tstartTime: " + credentials.startTime.getTime());
        }
        System.out.println("\tendTime: " + credentials.endTime.getTime());
        System.out.println("        ----Credentials end----");
    }

    static void ensureLoaded() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.krb5.Credentials.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                if (System.getProperty("os.name").contains("OS X")) {
                    System.loadLibrary("osxkrb5");
                    return null;
                }
                System.loadLibrary("w2k_lsa_auth");
                return null;
            }
        });
        alreadyLoaded = true;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("Credentials:");
        stringBuffer.append("\n      client=").append((Object) this.client);
        if (this.clientAlias != null) {
            stringBuffer.append("\n      clientAlias=").append((Object) this.clientAlias);
        }
        stringBuffer.append("\n      server=").append((Object) this.server);
        if (this.serverAlias != null) {
            stringBuffer.append("\n      serverAlias=").append((Object) this.serverAlias);
        }
        if (this.authTime != null) {
            stringBuffer.append("\n    authTime=").append((Object) this.authTime);
        }
        if (this.startTime != null) {
            stringBuffer.append("\n   startTime=").append((Object) this.startTime);
        }
        stringBuffer.append("\n     endTime=").append((Object) this.endTime);
        stringBuffer.append("\n   renewTill=").append((Object) this.renewTill);
        stringBuffer.append("\n       flags=").append((Object) this.flags);
        stringBuffer.append("\nEType (skey)=").append(this.key.getEType());
        stringBuffer.append("\n   (tkt key)=").append(this.ticket.encPart.eType);
        return stringBuffer.toString();
    }

    public sun.security.krb5.internal.ccache.Credentials toCCacheCreds() {
        return new sun.security.krb5.internal.ccache.Credentials(getClient(), getServer(), getSessionKey(), date2kt(getAuthTime()), date2kt(getStartTime()), date2kt(getEndTime()), date2kt(getRenewTill()), false, this.flags, new HostAddresses(getClientAddresses()), getAuthzData(), getTicket(), null);
    }

    private static KerberosTime date2kt(Date date) {
        if (date == null) {
            return null;
        }
        return new KerberosTime(date);
    }
}
