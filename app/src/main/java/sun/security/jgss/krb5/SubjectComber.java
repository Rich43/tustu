package sun.security.jgss.krb5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.kerberos.KeyTab;
import sun.security.krb5.JavaxSecurityAuthKerberosAccess;
import sun.security.krb5.KerberosSecrets;

/* loaded from: rt.jar:sun/security/jgss/krb5/SubjectComber.class */
class SubjectComber {
    private static final boolean DEBUG = Krb5Util.DEBUG;

    private SubjectComber() {
    }

    static <T> T find(Subject subject, String str, String str2, Class<T> cls) {
        return cls.cast(findAux(subject, str, str2, cls, true));
    }

    static <T> List<T> findMany(Subject subject, String str, String str2, Class<T> cls) {
        return (List) findAux(subject, str, str2, cls, false);
    }

    private static <T> Object findAux(Subject subject, String str, String str2, Class<T> cls, boolean z2) {
        String strFindClientMatch;
        if (subject == null) {
            return null;
        }
        ArrayList arrayList = z2 ? null : new ArrayList();
        if (cls == KeyTab.class) {
            for (T t2 : subject.getPrivateCredentials(KeyTab.class)) {
                if (str != null && t2.isBound()) {
                    KerberosPrincipal principal = t2.getPrincipal();
                    if (principal != null) {
                        if (!str.equals(principal.getName())) {
                            continue;
                        }
                    } else {
                        boolean z3 = false;
                        Iterator it = subject.getPrincipals(KerberosPrincipal.class).iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            if (((KerberosPrincipal) it.next()).getName().equals(str)) {
                                z3 = true;
                                break;
                            }
                        }
                        if (!z3) {
                            continue;
                        }
                    }
                }
                if (DEBUG) {
                    System.out.println("Found " + cls.getSimpleName() + " " + ((Object) t2));
                }
                if (z2) {
                    return t2;
                }
                arrayList.add(cls.cast(t2));
            }
        } else if (cls == KerberosKey.class) {
            for (T t3 : subject.getPrivateCredentials(KerberosKey.class)) {
                String name = t3.getPrincipal().getName();
                if (str == null || str.equals(name)) {
                    if (DEBUG) {
                        System.out.println("Found " + cls.getSimpleName() + " for " + name);
                    }
                    if (z2) {
                        return t3;
                    }
                    arrayList.add(cls.cast(t3));
                }
            }
        } else if (cls == KerberosTicket.class) {
            Set<Object> privateCredentials = subject.getPrivateCredentials();
            synchronized (privateCredentials) {
                Iterator<Object> it2 = privateCredentials.iterator();
                while (it2.hasNext()) {
                    Object next = it2.next();
                    if (next instanceof KerberosTicket) {
                        KerberosTicket kerberosTicket = (KerberosTicket) next;
                        if (DEBUG) {
                            System.out.println("Found ticket for " + ((Object) kerberosTicket.getClient()) + " to go to " + ((Object) kerberosTicket.getServer()) + " expiring on " + ((Object) kerberosTicket.getEndTime()));
                        }
                        if (!kerberosTicket.isCurrent()) {
                            if (!subject.isReadOnly()) {
                                it2.remove();
                                try {
                                    kerberosTicket.destroy();
                                    if (DEBUG) {
                                        System.out.println("Removed and destroyed the expired Ticket \n" + ((Object) kerberosTicket));
                                    }
                                } catch (DestroyFailedException e2) {
                                    if (DEBUG) {
                                        System.out.println("Expired ticket not detroyed successfully. " + ((Object) e2));
                                    }
                                }
                            }
                        } else {
                            String strFindServerMatch = findServerMatch(str, kerberosTicket);
                            if (strFindServerMatch != null && (strFindClientMatch = findClientMatch(str2, kerberosTicket)) != null) {
                                if (z2) {
                                    return kerberosTicket;
                                }
                                if (str2 == null) {
                                    str2 = strFindClientMatch;
                                }
                                if (str == null) {
                                    str = strFindServerMatch;
                                }
                                arrayList.add(cls.cast(kerberosTicket));
                            }
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    private static String findServerMatch(String str, KerberosTicket kerberosTicket) {
        KerberosPrincipal kerberosPrincipalKerberosTicketGetServerAlias = KerberosSecrets.getJavaxSecurityAuthKerberosAccess().kerberosTicketGetServerAlias(kerberosTicket);
        if (str != null) {
            if ((kerberosPrincipalKerberosTicketGetServerAlias == null || !str.equals(kerberosPrincipalKerberosTicketGetServerAlias.getName())) && !str.equals(kerberosTicket.getServer().getName())) {
                return null;
            }
            return str;
        }
        if (kerberosPrincipalKerberosTicketGetServerAlias != null) {
            return kerberosPrincipalKerberosTicketGetServerAlias.getName();
        }
        return kerberosTicket.getServer().getName();
    }

    private static String findClientMatch(String str, KerberosTicket kerberosTicket) {
        JavaxSecurityAuthKerberosAccess javaxSecurityAuthKerberosAccess = KerberosSecrets.getJavaxSecurityAuthKerberosAccess();
        KerberosPrincipal kerberosPrincipalKerberosTicketGetClientAlias = javaxSecurityAuthKerberosAccess.kerberosTicketGetClientAlias(kerberosTicket);
        KerberosTicket kerberosTicketKerberosTicketGetProxy = javaxSecurityAuthKerberosAccess.kerberosTicketGetProxy(kerberosTicket);
        if (str != null) {
            if ((kerberosPrincipalKerberosTicketGetClientAlias == null || !str.equals(kerberosPrincipalKerberosTicketGetClientAlias.getName())) && ((kerberosTicketKerberosTicketGetProxy == null || !str.equals(kerberosTicketKerberosTicketGetProxy.getClient().getName())) && !(kerberosTicketKerberosTicketGetProxy == null && str.equals(kerberosTicket.getClient().getName())))) {
                return null;
            }
            return str;
        }
        if (kerberosPrincipalKerberosTicketGetClientAlias != null) {
            return kerberosPrincipalKerberosTicketGetClientAlias.getName();
        }
        if (kerberosTicketKerberosTicketGetProxy != null) {
            return kerberosTicketKerberosTicketGetProxy.getClient().getName();
        }
        return kerberosTicket.getClient().getName();
    }
}
