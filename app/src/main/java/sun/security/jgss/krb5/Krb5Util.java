package sun.security.jgss.krb5;

import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginException;
import sun.security.action.GetBooleanAction;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.GSSUtil;
import sun.security.krb5.Credentials;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KerberosSecrets;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.internal.ktab.KeyTab;

/* loaded from: rt.jar:sun/security/jgss/krb5/Krb5Util.class */
public class Krb5Util {
    static final boolean DEBUG = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.security.krb5.debug"))).booleanValue();

    private Krb5Util() {
    }

    public static KerberosTicket getTicketFromSubjectAndTgs(GSSCaller gSSCaller, String str, String str2, String str3, AccessControlContext accessControlContext) throws LoginException, IOException, KrbException {
        boolean z2;
        Credentials credentialsAcquireServiceCreds;
        Subject subject = Subject.getSubject(accessControlContext);
        KerberosTicket kerberosTicketCredsToTicket = (KerberosTicket) SubjectComber.find(subject, str2, str, KerberosTicket.class);
        if (kerberosTicketCredsToTicket != null) {
            return kerberosTicketCredsToTicket;
        }
        Subject subjectLogin = null;
        if (!GSSUtil.useSubjectCredsOnly(gSSCaller)) {
            try {
                subjectLogin = GSSUtil.login(gSSCaller, GSSUtil.GSS_KRB5_MECH_OID);
                kerberosTicketCredsToTicket = (KerberosTicket) SubjectComber.find(subjectLogin, str2, str, KerberosTicket.class);
                if (kerberosTicketCredsToTicket != null) {
                    return kerberosTicketCredsToTicket;
                }
            } catch (LoginException e2) {
            }
        }
        KerberosTicket kerberosTicket = (KerberosTicket) SubjectComber.find(subject, str3, str, KerberosTicket.class);
        if (kerberosTicket == null && subjectLogin != null) {
            kerberosTicket = (KerberosTicket) SubjectComber.find(subjectLogin, str3, str, KerberosTicket.class);
            z2 = false;
        } else {
            z2 = true;
        }
        if (kerberosTicket != null && (credentialsAcquireServiceCreds = Credentials.acquireServiceCreds(str2, ticketToCreds(kerberosTicket))) != null) {
            kerberosTicketCredsToTicket = credsToTicket(credentialsAcquireServiceCreds);
            if (z2 && subject != null && !subject.isReadOnly()) {
                subject.getPrivateCredentials().add(kerberosTicketCredsToTicket);
            }
        }
        return kerberosTicketCredsToTicket;
    }

    static KerberosTicket getServiceTicket(GSSCaller gSSCaller, String str, String str2, AccessControlContext accessControlContext) throws LoginException {
        return (KerberosTicket) SubjectComber.find(Subject.getSubject(accessControlContext), str2, str, KerberosTicket.class);
    }

    static KerberosTicket getInitialTicket(GSSCaller gSSCaller, String str, AccessControlContext accessControlContext) throws LoginException {
        KerberosTicket kerberosTicket = (KerberosTicket) SubjectComber.find(Subject.getSubject(accessControlContext), null, str, KerberosTicket.class);
        if (kerberosTicket == null && !GSSUtil.useSubjectCredsOnly(gSSCaller)) {
            kerberosTicket = (KerberosTicket) SubjectComber.find(GSSUtil.login(gSSCaller, GSSUtil.GSS_KRB5_MECH_OID), null, str, KerberosTicket.class);
        }
        return kerberosTicket;
    }

    public static Subject getSubject(GSSCaller gSSCaller, AccessControlContext accessControlContext) throws LoginException {
        Subject subject = Subject.getSubject(accessControlContext);
        if (subject == null && !GSSUtil.useSubjectCredsOnly(gSSCaller)) {
            subject = GSSUtil.login(gSSCaller, GSSUtil.GSS_KRB5_MECH_OID);
        }
        return subject;
    }

    public static ServiceCreds getServiceCreds(GSSCaller gSSCaller, String str, AccessControlContext accessControlContext) throws LoginException {
        Subject subject = Subject.getSubject(accessControlContext);
        ServiceCreds serviceCreds = null;
        if (subject != null) {
            serviceCreds = ServiceCreds.getInstance(subject, str);
        }
        if (serviceCreds == null && !GSSUtil.useSubjectCredsOnly(gSSCaller)) {
            serviceCreds = ServiceCreds.getInstance(GSSUtil.login(gSSCaller, GSSUtil.GSS_KRB5_MECH_OID), str);
        }
        return serviceCreds;
    }

    public static KerberosTicket credsToTicket(Credentials credentials) {
        EncryptionKey sessionKey = credentials.getSessionKey();
        KerberosTicket kerberosTicket = new KerberosTicket(credentials.getEncoded(), new KerberosPrincipal(credentials.getClient().getName()), new KerberosPrincipal(credentials.getServer().getName(), 2), sessionKey.getBytes(), sessionKey.getEType(), credentials.getFlags(), credentials.getAuthTime(), credentials.getStartTime(), credentials.getEndTime(), credentials.getRenewTill(), credentials.getClientAddresses());
        PrincipalName clientAlias = credentials.getClientAlias();
        PrincipalName serverAlias = credentials.getServerAlias();
        if (clientAlias != null) {
            KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketSetClientAlias(kerberosTicket, new KerberosPrincipal(clientAlias.getName(), clientAlias.getNameType()));
        }
        if (serverAlias != null) {
            KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketSetServerAlias(kerberosTicket, new KerberosPrincipal(serverAlias.getName(), serverAlias.getNameType()));
        }
        return kerberosTicket;
    }

    public static Credentials ticketToCreds(KerberosTicket kerberosTicket) throws IOException, KrbException {
        KerberosPrincipal kerberosPrincipalKerberosTicketGetClientAlias = KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketGetClientAlias(kerberosTicket);
        KerberosPrincipal kerberosPrincipalKerberosTicketGetServerAlias = KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketGetServerAlias(kerberosTicket);
        return new Credentials(kerberosTicket.getEncoded(), kerberosTicket.getClient().getName(), kerberosPrincipalKerberosTicketGetClientAlias != null ? kerberosPrincipalKerberosTicketGetClientAlias.getName() : null, kerberosTicket.getServer().getName(), kerberosPrincipalKerberosTicketGetServerAlias != null ? kerberosPrincipalKerberosTicketGetServerAlias.getName() : null, kerberosTicket.getSessionKey().getEncoded(), kerberosTicket.getSessionKeyType(), kerberosTicket.getFlags(), kerberosTicket.getAuthTime(), kerberosTicket.getStartTime(), kerberosTicket.getEndTime(), kerberosTicket.getRenewTill(), kerberosTicket.getClientAddresses());
    }

    public static KeyTab snapshotFromJavaxKeyTab(javax.security.auth.kerberos.KeyTab keyTab) {
        return KerberosSecrets.getJavaxSecurityAuthKerberosAccess().keyTabTakeSnapshot(keyTab);
    }

    public static EncryptionKey[] keysFromJavaxKeyTab(javax.security.auth.kerberos.KeyTab keyTab, PrincipalName principalName) {
        return snapshotFromJavaxKeyTab(keyTab).readServiceKeys(principalName);
    }
}
