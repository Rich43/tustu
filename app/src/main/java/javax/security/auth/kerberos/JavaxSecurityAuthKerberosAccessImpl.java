package javax.security.auth.kerberos;

import sun.security.krb5.JavaxSecurityAuthKerberosAccess;

/* loaded from: rt.jar:javax/security/auth/kerberos/JavaxSecurityAuthKerberosAccessImpl.class */
class JavaxSecurityAuthKerberosAccessImpl implements JavaxSecurityAuthKerberosAccess {
    JavaxSecurityAuthKerberosAccessImpl() {
    }

    @Override // sun.security.krb5.JavaxSecurityAuthKerberosAccess
    public sun.security.krb5.internal.ktab.KeyTab keyTabTakeSnapshot(KeyTab keyTab) {
        return keyTab.takeSnapshot();
    }

    @Override // sun.security.krb5.JavaxSecurityAuthKerberosAccess
    public KerberosPrincipal kerberosTicketGetClientAlias(KerberosTicket kerberosTicket) {
        return kerberosTicket.clientAlias;
    }

    @Override // sun.security.krb5.JavaxSecurityAuthKerberosAccess
    public void kerberosTicketSetClientAlias(KerberosTicket kerberosTicket, KerberosPrincipal kerberosPrincipal) {
        kerberosTicket.clientAlias = kerberosPrincipal;
    }

    @Override // sun.security.krb5.JavaxSecurityAuthKerberosAccess
    public KerberosPrincipal kerberosTicketGetServerAlias(KerberosTicket kerberosTicket) {
        return kerberosTicket.serverAlias;
    }

    @Override // sun.security.krb5.JavaxSecurityAuthKerberosAccess
    public void kerberosTicketSetServerAlias(KerberosTicket kerberosTicket, KerberosPrincipal kerberosPrincipal) {
        kerberosTicket.serverAlias = kerberosPrincipal;
    }

    @Override // sun.security.krb5.JavaxSecurityAuthKerberosAccess
    public KerberosTicket kerberosTicketGetProxy(KerberosTicket kerberosTicket) {
        return kerberosTicket.proxy;
    }

    @Override // sun.security.krb5.JavaxSecurityAuthKerberosAccess
    public void kerberosTicketSetProxy(KerberosTicket kerberosTicket, KerberosTicket kerberosTicket2) {
        kerberosTicket.proxy = kerberosTicket2;
    }
}
