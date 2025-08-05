package com.sun.jndi.ldap;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Hashtable;
import javax.naming.ldap.Control;
import javax.security.sasl.Sasl;

/* loaded from: rt.jar:com/sun/jndi/ldap/DigestClientId.class */
class DigestClientId extends SimpleClientId {
    private static final String[] SASL_PROPS = {"java.naming.security.sasl.authorizationId", "java.naming.security.sasl.realm", Sasl.QOP, Sasl.STRENGTH, Sasl.REUSE, Sasl.SERVER_AUTH, Sasl.MAX_BUFFER, Sasl.POLICY_NOPLAINTEXT, Sasl.POLICY_NOACTIVE, Sasl.POLICY_NODICTIONARY, Sasl.POLICY_NOANONYMOUS, Sasl.POLICY_FORWARD_SECRECY, Sasl.POLICY_PASS_CREDENTIALS};
    private final String[] propvals;
    private final int myHash;

    DigestClientId(int i2, String str, int i3, String str2, Control[] controlArr, OutputStream outputStream, String str3, String str4, Object obj, Hashtable<?, ?> hashtable) {
        super(i2, str, i3, str2, controlArr, outputStream, str3, str4, obj);
        if (hashtable == null) {
            this.propvals = null;
        } else {
            this.propvals = new String[SASL_PROPS.length];
            for (int i4 = 0; i4 < SASL_PROPS.length; i4++) {
                this.propvals[i4] = (String) hashtable.get(SASL_PROPS[i4]);
            }
        }
        this.myHash = super.hashCode() ^ Arrays.hashCode(this.propvals);
    }

    @Override // com.sun.jndi.ldap.SimpleClientId, com.sun.jndi.ldap.ClientId
    public boolean equals(Object obj) {
        if (!(obj instanceof DigestClientId)) {
            return false;
        }
        DigestClientId digestClientId = (DigestClientId) obj;
        return this.myHash == digestClientId.myHash && super.equals(obj) && Arrays.equals(this.propvals, digestClientId.propvals);
    }

    @Override // com.sun.jndi.ldap.SimpleClientId, com.sun.jndi.ldap.ClientId
    public int hashCode() {
        return this.myHash;
    }

    @Override // com.sun.jndi.ldap.SimpleClientId, com.sun.jndi.ldap.ClientId
    public String toString() {
        if (this.propvals != null) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i2 = 0; i2 < this.propvals.length; i2++) {
                stringBuffer.append(':');
                if (this.propvals[i2] != null) {
                    stringBuffer.append(this.propvals[i2]);
                }
            }
            return super.toString() + stringBuffer.toString();
        }
        return super.toString();
    }
}
