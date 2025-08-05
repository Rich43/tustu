package javax.naming.ldap;

import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/ldap/HasControls.class */
public interface HasControls {
    Control[] getControls() throws NamingException;
}
