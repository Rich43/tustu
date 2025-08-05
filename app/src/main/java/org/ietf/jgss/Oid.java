package org.ietf.jgss;

import java.io.IOException;
import java.io.InputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:org/ietf/jgss/Oid.class */
public class Oid {
    private ObjectIdentifier oid;
    private byte[] derEncoding;

    public Oid(String str) throws GSSException {
        try {
            this.oid = new ObjectIdentifier(str);
            this.derEncoding = null;
        } catch (Exception e2) {
            throw new GSSException(11, "Improperly formatted Object Identifier String - " + str);
        }
    }

    public Oid(InputStream inputStream) throws GSSException {
        try {
            DerValue derValue = new DerValue(inputStream);
            this.derEncoding = derValue.toByteArray();
            this.oid = derValue.getOID();
        } catch (IOException e2) {
            throw new GSSException(11, "Improperly formatted ASN.1 DER encoding for Oid");
        }
    }

    public Oid(byte[] bArr) throws GSSException {
        try {
            DerValue derValue = new DerValue(bArr);
            this.derEncoding = derValue.toByteArray();
            this.oid = derValue.getOID();
        } catch (IOException e2) {
            throw new GSSException(11, "Improperly formatted ASN.1 DER encoding for Oid");
        }
    }

    static Oid getInstance(String str) {
        Oid oid = null;
        try {
            oid = new Oid(str);
        } catch (GSSException e2) {
        }
        return oid;
    }

    public String toString() {
        return this.oid.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Oid) {
            return this.oid.equals((Object) ((Oid) obj).oid);
        }
        if (obj instanceof ObjectIdentifier) {
            return this.oid.equals(obj);
        }
        return false;
    }

    public byte[] getDER() throws GSSException {
        if (this.derEncoding == null) {
            DerOutputStream derOutputStream = new DerOutputStream();
            try {
                derOutputStream.putOID(this.oid);
                this.derEncoding = derOutputStream.toByteArray();
            } catch (IOException e2) {
                throw new GSSException(11, e2.getMessage());
            }
        }
        return (byte[]) this.derEncoding.clone();
    }

    public boolean containedIn(Oid[] oidArr) {
        for (Oid oid : oidArr) {
            if (oid.equals(this)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.oid.hashCode();
    }
}
