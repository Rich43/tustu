package javax.naming.ldap;

import java.io.Serializable;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/ldap/ExtendedRequest.class */
public interface ExtendedRequest extends Serializable {
    String getID();

    byte[] getEncodedValue();

    ExtendedResponse createExtendedResponse(String str, byte[] bArr, int i2, int i3) throws NamingException;
}
