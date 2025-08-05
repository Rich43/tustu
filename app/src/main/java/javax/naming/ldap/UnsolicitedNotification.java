package javax.naming.ldap;

import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/ldap/UnsolicitedNotification.class */
public interface UnsolicitedNotification extends ExtendedResponse, HasControls {
    String[] getReferrals();

    NamingException getException();
}
