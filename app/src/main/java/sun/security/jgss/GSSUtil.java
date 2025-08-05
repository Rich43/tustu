package sun.security.jgss;

import com.sun.security.auth.callback.TextCallbackHandler;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.net.www.protocol.http.spnego.NegotiateCallbackHandler;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetPropertyAction;
import sun.security.jgss.krb5.Krb5NameElement;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.jgss.spnego.SpNegoCredElement;

/* loaded from: rt.jar:sun/security/jgss/GSSUtil.class */
public class GSSUtil {
    public static final Oid GSS_KRB5_MECH_OID;
    public static final Oid GSS_KRB5_MECH_OID2;
    public static final Oid GSS_KRB5_MECH_OID_MS;
    public static final Oid GSS_SPNEGO_MECH_OID;
    public static final Oid NT_GSS_KRB5_PRINCIPAL;
    private static final String DEFAULT_HANDLER = "auth.login.defaultCallbackHandler";
    static final boolean DEBUG;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GSSUtil.class.desiredAssertionStatus();
        GSS_KRB5_MECH_OID = createOid("1.2.840.113554.1.2.2");
        GSS_KRB5_MECH_OID2 = createOid("1.3.5.1.5.2");
        GSS_KRB5_MECH_OID_MS = createOid("1.2.840.48018.1.2.2");
        GSS_SPNEGO_MECH_OID = createOid("1.3.6.1.5.5.2");
        NT_GSS_KRB5_PRINCIPAL = createOid("1.2.840.113554.1.2.2.1");
        DEBUG = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.security.jgss.debug"))).booleanValue();
    }

    static void debug(String str) {
        if (DEBUG) {
            if (!$assertionsDisabled && str == null) {
                throw new AssertionError();
            }
            System.out.println(str);
        }
    }

    public static Oid createOid(String str) {
        try {
            return new Oid(str);
        } catch (GSSException e2) {
            debug("Ignored invalid OID: " + str);
            return null;
        }
    }

    public static boolean isSpNegoMech(Oid oid) {
        return GSS_SPNEGO_MECH_OID.equals(oid);
    }

    public static boolean isKerberosMech(Oid oid) {
        return GSS_KRB5_MECH_OID.equals(oid) || GSS_KRB5_MECH_OID2.equals(oid) || GSS_KRB5_MECH_OID_MS.equals(oid);
    }

    public static String getMechStr(Oid oid) {
        if (isSpNegoMech(oid)) {
            return "SPNEGO";
        }
        if (isKerberosMech(oid)) {
            return "Kerberos V5";
        }
        return oid.toString();
    }

    public static Subject getSubject(GSSName gSSName, GSSCredential gSSCredential) {
        HashSet hashSet;
        HashSet hashSet2 = new HashSet();
        HashSet hashSet3 = new HashSet();
        if (gSSName instanceof GSSNameImpl) {
            try {
                GSSNameSpi element = ((GSSNameImpl) gSSName).getElement(GSS_KRB5_MECH_OID);
                String string = element.toString();
                if (element instanceof Krb5NameElement) {
                    string = ((Krb5NameElement) element).getKrb5PrincipalName().getName();
                }
                hashSet3.add(new KerberosPrincipal(string));
            } catch (GSSException e2) {
                debug("Skipped name " + ((Object) gSSName) + " due to " + ((Object) e2));
            }
        }
        if (gSSCredential instanceof GSSCredentialImpl) {
            Set<GSSCredentialSpi> elements = ((GSSCredentialImpl) gSSCredential).getElements();
            hashSet = new HashSet(elements.size());
            populateCredentials(hashSet, elements);
        } else {
            hashSet = new HashSet();
        }
        debug("Created Subject with the following");
        debug("principals=" + ((Object) hashSet3));
        debug("public creds=" + ((Object) hashSet2));
        debug("private creds=" + ((Object) hashSet));
        return new Subject(false, hashSet3, hashSet2, hashSet);
    }

    private static void populateCredentials(Set<Object> set, Set<?> set2) {
        Iterator<?> it = set2.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof SpNegoCredElement) {
                next = ((SpNegoCredElement) next).getInternalCred();
            }
            if (next instanceof KerberosTicket) {
                if (!next.getClass().getName().equals("javax.security.auth.kerberos.KerberosTicket")) {
                    KerberosTicket kerberosTicket = (KerberosTicket) next;
                    next = new KerberosTicket(kerberosTicket.getEncoded(), kerberosTicket.getClient(), kerberosTicket.getServer(), kerberosTicket.getSessionKey().getEncoded(), kerberosTicket.getSessionKeyType(), kerberosTicket.getFlags(), kerberosTicket.getAuthTime(), kerberosTicket.getStartTime(), kerberosTicket.getEndTime(), kerberosTicket.getRenewTill(), kerberosTicket.getClientAddresses());
                }
                set.add(next);
            } else if (next instanceof KerberosKey) {
                if (!next.getClass().getName().equals("javax.security.auth.kerberos.KerberosKey")) {
                    KerberosKey kerberosKey = (KerberosKey) next;
                    next = new KerberosKey(kerberosKey.getPrincipal(), kerberosKey.getEncoded(), kerberosKey.getKeyType(), kerberosKey.getVersionNumber());
                }
                set.add(next);
            } else {
                debug("Skipped cred element: " + next);
            }
        }
    }

    public static Subject login(GSSCaller gSSCaller, Oid oid) throws LoginException {
        CallbackHandler textCallbackHandler;
        if (gSSCaller instanceof HttpCaller) {
            textCallbackHandler = new NegotiateCallbackHandler(((HttpCaller) gSSCaller).info());
        } else {
            String property = Security.getProperty(DEFAULT_HANDLER);
            if (property != null && property.length() != 0) {
                textCallbackHandler = null;
            } else {
                textCallbackHandler = new TextCallbackHandler();
            }
        }
        LoginContext loginContext = new LoginContext("", null, textCallbackHandler, new LoginConfigImpl(gSSCaller, oid));
        loginContext.login();
        return loginContext.getSubject();
    }

    public static boolean useSubjectCredsOnly(GSSCaller gSSCaller) {
        String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty("javax.security.auth.useSubjectCredsOnly");
        if (gSSCaller instanceof HttpCaller) {
            return "true".equalsIgnoreCase(strPrivilegedGetProperty);
        }
        return !"false".equalsIgnoreCase(strPrivilegedGetProperty);
    }

    public static boolean useMSInterop() {
        return !((String) AccessController.doPrivileged(new GetPropertyAction("sun.security.spnego.msinterop", "true"))).equalsIgnoreCase("false");
    }

    public static <T extends GSSCredentialSpi> Vector<T> searchSubject(final GSSNameSpi gSSNameSpi, final Oid oid, final boolean z2, final Class<? extends T> cls) {
        debug("Search Subject for " + getMechStr(oid) + (z2 ? " INIT" : " ACCEPT") + " cred (" + (gSSNameSpi == null ? "<<DEF>>" : gSSNameSpi.toString()) + ", " + cls.getName() + ")");
        final AccessControlContext context = AccessController.getContext();
        try {
            return (Vector) AccessController.doPrivileged(new PrivilegedExceptionAction<Vector<T>>() { // from class: sun.security.jgss.GSSUtil.1
                @Override // java.security.PrivilegedExceptionAction
                public Vector<T> run() throws Exception {
                    Subject subject = Subject.getSubject(context);
                    Vector vector = null;
                    if (subject != null) {
                        vector = new Vector();
                        for (T t2 : subject.getPrivateCredentials(GSSCredentialImpl.class)) {
                            GSSUtil.debug("...Found cred" + ((Object) t2));
                            try {
                                GSSCredentialSpi element = t2.getElement(oid, z2);
                                GSSUtil.debug("......Found element: " + ((Object) element));
                                if (element.getClass().equals(cls) && (gSSNameSpi == null || gSSNameSpi.equals((Object) element.getName()))) {
                                    vector.add(cls.cast(element));
                                } else {
                                    GSSUtil.debug("......Discard element");
                                }
                            } catch (GSSException e2) {
                                GSSUtil.debug("...Discard cred (" + ((Object) e2) + ")");
                            }
                        }
                    } else {
                        GSSUtil.debug("No Subject");
                    }
                    return vector;
                }
            });
        } catch (PrivilegedActionException e2) {
            debug("Unexpected exception when searching Subject:");
            if (DEBUG) {
                e2.printStackTrace();
                return null;
            }
            return null;
        }
    }
}
