package sun.security.krb5.internal;

import java.io.IOException;
import java.util.LinkedList;
import sun.security.krb5.Config;
import sun.security.krb5.Credentials;
import sun.security.krb5.KrbException;
import sun.security.krb5.KrbTgsReq;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.internal.ReferralsCache;

/* loaded from: rt.jar:sun/security/krb5/internal/CredentialsUtil.class */
public class CredentialsUtil {
    private static boolean DEBUG = Krb5.DEBUG;

    /* loaded from: rt.jar:sun/security/krb5/internal/CredentialsUtil$S4U2Type.class */
    private enum S4U2Type {
        NONE,
        SELF,
        PROXY
    }

    public static Credentials acquireS4U2selfCreds(PrincipalName principalName, Credentials credentials) throws IOException, KrbException {
        if (!credentials.isForwardable()) {
            throw new KrbException("S4U2self needs a FORWARDABLE ticket");
        }
        PrincipalName client = credentials.getClient();
        String realmString = principalName.getRealmString();
        if (!realmString.equals(credentials.getClient().getRealmString())) {
            if (Config.DISABLE_REFERRALS) {
                throw new KrbException("Cross-realm S4U2Self request not possible when referrals are disabled.");
            }
            if (credentials.getClientAlias() != null) {
                client = credentials.getClientAlias();
            }
            client = new PrincipalName(client.getNameType(), client.getNameStrings(), new Realm(realmString));
        }
        Credentials credentialsServiceCreds = serviceCreds(KDCOptions.with(1), credentials, credentials.getClient(), client, principalName, null, new PAData[]{new PAData(129, new PAForUserEnc(principalName, credentials.getSessionKey()).asn1Encode()), new PAData(167, new PaPacOptions().setResourceBasedConstrainedDelegation(true).setClaims(true).asn1Encode())}, S4U2Type.SELF);
        if (!credentialsServiceCreds.getClient().equals(principalName)) {
            throw new KrbException("S4U2self request not honored by KDC");
        }
        if (!credentialsServiceCreds.isForwardable()) {
            throw new KrbException("S4U2self ticket must be FORWARDABLE");
        }
        return credentialsServiceCreds;
    }

    public static Credentials acquireS4U2proxyCreds(String str, Ticket ticket, PrincipalName principalName, Credentials credentials) throws IOException, KrbException {
        PrincipalName principalName2 = new PrincipalName(str);
        String realmString = principalName2.getRealmString();
        String realmString2 = credentials.getClient().getRealmString();
        if (!realmString.equals(realmString2)) {
            if (Config.DISABLE_REFERRALS) {
                throw new KrbException("Cross-realm S4U2Proxy request not possible when referrals are disabled.");
            }
            principalName2 = new PrincipalName(principalName2.getNameType(), principalName2.getNameStrings(), new Realm(realmString2));
        }
        Credentials credentialsServiceCreds = serviceCreds(KDCOptions.with(14, 1), credentials, credentials.getClient(), principalName2, null, new Ticket[]{ticket}, new PAData[]{new PAData(167, new PaPacOptions().setResourceBasedConstrainedDelegation(true).setClaims(true).asn1Encode())}, S4U2Type.PROXY);
        if (!credentialsServiceCreds.getClient().equals(principalName)) {
            throw new KrbException("S4U2proxy request not honored by KDC");
        }
        return credentialsServiceCreds;
    }

    public static Credentials acquireServiceCreds(String str, Credentials credentials) throws IOException, KrbException {
        return serviceCreds(new PrincipalName(str, 0), credentials);
    }

    private static Credentials getTGTforRealm(String str, String str2, Credentials credentials, boolean[] zArr) throws KrbException {
        Credentials credentialsServiceCreds;
        String[] realmsList = Realm.getRealmsList(str, str2);
        Credentials credentials2 = null;
        zArr[0] = true;
        Credentials credentials3 = credentials;
        int i2 = 0;
        while (true) {
            if (i2 >= realmsList.length) {
                break;
            }
            PrincipalName principalNameTgsService = PrincipalName.tgsService(str2, realmsList[i2]);
            if (DEBUG) {
                System.out.println(">>> Credentials acquireServiceCreds: main loop: [" + i2 + "] tempService=" + ((Object) principalNameTgsService));
            }
            try {
                credentialsServiceCreds = serviceCreds(principalNameTgsService, credentials3);
            } catch (Exception e2) {
                credentialsServiceCreds = null;
            }
            if (credentialsServiceCreds == null) {
                if (DEBUG) {
                    System.out.println(">>> Credentials acquireServiceCreds: no tgt; searching thru capath");
                }
                credentialsServiceCreds = null;
                for (int i3 = i2 + 1; credentialsServiceCreds == null && i3 < realmsList.length; i3++) {
                    PrincipalName principalNameTgsService2 = PrincipalName.tgsService(realmsList[i3], realmsList[i2]);
                    if (DEBUG) {
                        System.out.println(">>> Credentials acquireServiceCreds: inner loop: [" + i3 + "] tempService=" + ((Object) principalNameTgsService2));
                    }
                    try {
                        credentialsServiceCreds = serviceCreds(principalNameTgsService2, credentials3);
                    } catch (Exception e3) {
                        credentialsServiceCreds = null;
                    }
                }
            }
            if (credentialsServiceCreds == null) {
                if (DEBUG) {
                    System.out.println(">>> Credentials acquireServiceCreds: no tgt; cannot get creds");
                }
            } else {
                String instanceComponent = credentialsServiceCreds.getServer().getInstanceComponent();
                if (zArr[0] && !credentialsServiceCreds.checkDelegate()) {
                    if (DEBUG) {
                        System.out.println(">>> Credentials acquireServiceCreds: global OK-AS-DELEGATE turned off at " + ((Object) credentialsServiceCreds.getServer()));
                    }
                    zArr[0] = false;
                }
                if (DEBUG) {
                    System.out.println(">>> Credentials acquireServiceCreds: got tgt");
                }
                if (instanceComponent.equals(str2)) {
                    credentials2 = credentialsServiceCreds;
                    break;
                }
                int i4 = i2 + 1;
                while (i4 < realmsList.length && !instanceComponent.equals(realmsList[i4])) {
                    i4++;
                }
                if (i4 >= realmsList.length) {
                    break;
                }
                i2 = i4;
                credentials3 = credentialsServiceCreds;
                if (DEBUG) {
                    System.out.println(">>> Credentials acquireServiceCreds: continuing with main loop counter reset to " + i2);
                }
            }
        }
        return credentials2;
    }

    private static Credentials serviceCreds(PrincipalName principalName, Credentials credentials) throws IOException, KrbException {
        return serviceCreds(new KDCOptions(), credentials, credentials.getClient(), principalName, null, null, null, S4U2Type.NONE);
    }

    private static Credentials serviceCreds(KDCOptions kDCOptions, Credentials credentials, PrincipalName principalName, PrincipalName principalName2, PrincipalName principalName3, Ticket[] ticketArr, PAData[] pADataArr, S4U2Type s4U2Type) throws IOException, KrbException {
        if (!Config.DISABLE_REFERRALS) {
            try {
                return serviceCredsReferrals(kDCOptions, credentials, principalName, principalName2, s4U2Type, principalName3, ticketArr, pADataArr);
            } catch (KrbException e2) {
            }
        }
        return serviceCredsSingle(kDCOptions, credentials, principalName, credentials.getClientAlias(), principalName2, principalName2, s4U2Type, principalName3, ticketArr, pADataArr);
    }

    private static Credentials serviceCredsReferrals(KDCOptions kDCOptions, Credentials credentials, PrincipalName principalName, PrincipalName principalName2, S4U2Type s4U2Type, PrincipalName principalName3, Ticket[] ticketArr, PAData[] pADataArr) throws IOException, ArrayIndexOutOfBoundsException, KrbException {
        KDCOptions kDCOptions2 = new KDCOptions(kDCOptions.toBooleanArray());
        kDCOptions2.set(15, true);
        PrincipalName principalName4 = principalName2;
        Credentials creds = null;
        boolean z2 = false;
        LinkedList linkedList = new LinkedList();
        PrincipalName clientAlias = credentials.getClientAlias();
        while (linkedList.size() <= Config.MAX_REFERRALS) {
            ReferralsCache.ReferralCacheEntry referralCacheEntry = ReferralsCache.get(principalName, principalName2, principalName3, ticketArr, principalName4.getRealmString());
            String toRealm = null;
            if (referralCacheEntry == null) {
                creds = serviceCredsSingle(kDCOptions2, credentials, principalName, clientAlias, principalName4, principalName2, s4U2Type, principalName3, ticketArr, pADataArr);
                PrincipalName server = creds.getServer();
                if (!principalName4.equals(server)) {
                    String[] nameStrings = server.getNameStrings();
                    if (nameStrings.length == 2 && nameStrings[0].equals(PrincipalName.TGS_DEFAULT_SRV_NAME) && !principalName4.getRealmAsString().equals(nameStrings[1])) {
                        ReferralsCache.put(principalName, principalName2, principalName3, ticketArr, server.getRealmString(), nameStrings[1], creds);
                        toRealm = nameStrings[1];
                        z2 = true;
                    }
                }
            } else {
                creds = referralCacheEntry.getCreds();
                toRealm = referralCacheEntry.getToRealm();
                z2 = true;
            }
            if (!z2) {
                break;
            }
            if (s4U2Type == S4U2Type.PROXY) {
                Credentials[] credentialsArr = {creds, null};
                toRealm = handleS4U2ProxyReferral(credentials, credentialsArr, principalName2);
                creds = credentialsArr[0];
                if (ticketArr == null || ticketArr.length == 0 || credentialsArr[1] == null) {
                    throw new KrbException("Additional tickets expected for S4U2Proxy.");
                }
                ticketArr[0] = credentialsArr[1].getTicket();
            } else if (s4U2Type == S4U2Type.SELF) {
                handleS4U2SelfReferral(pADataArr, principalName3, creds);
            }
            if (linkedList.contains(toRealm)) {
                return null;
            }
            credentials = creds;
            principalName4 = new PrincipalName(principalName4.getNameString(), principalName4.getNameType(), toRealm);
            linkedList.add(toRealm);
            z2 = false;
        }
        return creds;
    }

    private static Credentials serviceCredsSingle(KDCOptions kDCOptions, Credentials credentials, PrincipalName principalName, PrincipalName principalName2, PrincipalName principalName3, PrincipalName principalName4, S4U2Type s4U2Type, PrincipalName principalName5, Ticket[] ticketArr, PAData[] pADataArr) throws IOException, KrbException {
        boolean[] zArr = {true};
        String str = credentials.getServer().getNameStrings()[1];
        String realmString = principalName3.getRealmString();
        if (!realmString.equals(str)) {
            if (DEBUG) {
                System.out.println(">>> serviceCredsSingle: cross-realm authentication");
                System.out.println(">>> serviceCredsSingle: obtaining credentials from " + str + " to " + realmString);
            }
            Credentials tGTforRealm = getTGTforRealm(str, realmString, credentials, zArr);
            if (tGTforRealm == null) {
                throw new KrbApErrException(63, "No service creds");
            }
            if (DEBUG) {
                System.out.println(">>> Cross-realm TGT Credentials serviceCredsSingle: ");
                Credentials.printDebug(tGTforRealm);
            }
            if (s4U2Type == S4U2Type.SELF) {
                handleS4U2SelfReferral(pADataArr, principalName5, tGTforRealm);
            }
            credentials = tGTforRealm;
            principalName = credentials.getClient();
        } else if (DEBUG) {
            System.out.println(">>> Credentials serviceCredsSingle: same realm");
        }
        Credentials credentialsSendAndGetCreds = new KrbTgsReq(kDCOptions, credentials, principalName, principalName2, principalName3, principalName4, ticketArr, pADataArr).sendAndGetCreds();
        if (credentialsSendAndGetCreds != null) {
            if (DEBUG) {
                System.out.println(">>> TGS credentials serviceCredsSingle:");
                Credentials.printDebug(credentialsSendAndGetCreds);
            }
            if (!zArr[0]) {
                credentialsSendAndGetCreds.resetDelegate();
            }
        }
        return credentialsSendAndGetCreds;
    }

    private static void handleS4U2SelfReferral(PAData[] pADataArr, PrincipalName principalName, Credentials credentials) throws IOException, KrbException {
        if (DEBUG) {
            System.out.println(">>> Handling S4U2Self referral");
        }
        for (int i2 = 0; i2 < pADataArr.length; i2++) {
            if (pADataArr[i2].getType() == 129) {
                pADataArr[i2] = new PAData(129, new PAForUserEnc(principalName, credentials.getSessionKey()).asn1Encode());
                return;
            }
        }
    }

    private static String handleS4U2ProxyReferral(Credentials credentials, Credentials[] credentialsArr, PrincipalName principalName) throws IOException, KrbException {
        Credentials tGTforRealm;
        if (DEBUG) {
            System.out.println(">>> Handling S4U2Proxy referral");
        }
        String realmString = serviceCreds(principalName, credentials).getServer().getRealmString();
        String str = credentialsArr[0].getServer().getNameStrings()[1];
        if (!str.equals(realmString)) {
            tGTforRealm = getTGTforRealm(str, realmString, credentialsArr[0], new boolean[1]);
        } else {
            tGTforRealm = credentialsArr[0];
        }
        credentialsArr[0] = getTGTforRealm(credentials.getClient().getRealmString(), realmString, credentials, new boolean[1]);
        credentialsArr[1] = tGTforRealm;
        return realmString;
    }
}
