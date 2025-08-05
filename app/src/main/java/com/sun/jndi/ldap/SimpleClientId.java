package com.sun.jndi.ldap;

import java.io.OutputStream;
import java.util.Arrays;
import javax.naming.ldap.Control;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/jndi/ldap/SimpleClientId.class */
class SimpleClientId extends ClientId {
    private final String username;
    private final Object passwd;
    private final int myHash;

    SimpleClientId(int i2, String str, int i3, String str2, Control[] controlArr, OutputStream outputStream, String str3, String str4, Object obj) {
        super(i2, str, i3, str2, controlArr, outputStream, str3);
        this.username = str4;
        int iHashCode = 0;
        if (obj == null) {
            this.passwd = null;
        } else if (obj instanceof byte[]) {
            this.passwd = ((byte[]) obj).clone();
            iHashCode = Arrays.hashCode((byte[]) obj);
        } else if (obj instanceof char[]) {
            this.passwd = ((char[]) obj).clone();
            iHashCode = Arrays.hashCode((char[]) obj);
        } else {
            this.passwd = obj;
            iHashCode = obj.hashCode();
        }
        this.myHash = (super.hashCode() ^ (str4 != null ? str4.hashCode() : 0)) ^ iHashCode;
    }

    @Override // com.sun.jndi.ldap.ClientId
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SimpleClientId)) {
            return false;
        }
        SimpleClientId simpleClientId = (SimpleClientId) obj;
        return super.equals(obj) && (this.username == simpleClientId.username || (this.username != null && this.username.equals(simpleClientId.username))) && (this.passwd == simpleClientId.passwd || !(this.passwd == null || simpleClientId.passwd == null || ((!(this.passwd instanceof String) || !this.passwd.equals(simpleClientId.passwd)) && ((!(this.passwd instanceof byte[]) || !(simpleClientId.passwd instanceof byte[]) || !Arrays.equals((byte[]) this.passwd, (byte[]) simpleClientId.passwd)) && (!(this.passwd instanceof char[]) || !(simpleClientId.passwd instanceof char[]) || !Arrays.equals((char[]) this.passwd, (char[]) simpleClientId.passwd))))));
    }

    @Override // com.sun.jndi.ldap.ClientId
    public int hashCode() {
        return this.myHash;
    }

    @Override // com.sun.jndi.ldap.ClientId
    public String toString() {
        return super.toString() + CallSiteDescriptor.TOKEN_DELIMITER + this.username;
    }
}
