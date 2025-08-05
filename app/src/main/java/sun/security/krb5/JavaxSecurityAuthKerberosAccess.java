package sun.security.krb5;

import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosTicket;
import sun.security.krb5.internal.ktab.KeyTab;

/* loaded from: rt.jar:sun/security/krb5/JavaxSecurityAuthKerberosAccess.class */
public interface JavaxSecurityAuthKerberosAccess {
    KeyTab keyTabTakeSnapshot(javax.security.auth.kerberos.KeyTab keyTab);

    KerberosPrincipal kerberosTicketGetClientAlias(KerberosTicket kerberosTicket);

    void kerberosTicketSetClientAlias(KerberosTicket kerberosTicket, KerberosPrincipal kerberosPrincipal);

    KerberosPrincipal kerberosTicketGetServerAlias(KerberosTicket kerberosTicket);

    void kerberosTicketSetServerAlias(KerberosTicket kerberosTicket, KerberosPrincipal kerberosPrincipal);

    KerberosTicket kerberosTicketGetProxy(KerberosTicket kerberosTicket);

    void kerberosTicketSetProxy(KerberosTicket kerberosTicket, KerberosTicket kerberosTicket2);
}
