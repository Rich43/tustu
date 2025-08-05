package javax.naming.ldap;

import java.io.Serializable;

/* loaded from: rt.jar:javax/naming/ldap/ExtendedResponse.class */
public interface ExtendedResponse extends Serializable {
    String getID();

    byte[] getEncodedValue();
}
