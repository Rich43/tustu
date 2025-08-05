package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/x509/Extension.class */
public class Extension implements java.security.cert.Extension {
    protected ObjectIdentifier extensionId;
    protected boolean critical;
    protected byte[] extensionValue;
    private static final int hashMagic = 31;

    public Extension() {
        this.extensionId = null;
        this.critical = false;
        this.extensionValue = null;
    }

    public Extension(DerValue derValue) throws IOException {
        this.extensionId = null;
        this.critical = false;
        this.extensionValue = null;
        DerInputStream derInputStream = derValue.toDerInputStream();
        this.extensionId = derInputStream.getOID();
        DerValue derValue2 = derInputStream.getDerValue();
        if (derValue2.tag == 1) {
            this.critical = derValue2.getBoolean();
            this.extensionValue = derInputStream.getDerValue().getOctetString();
        } else {
            this.critical = false;
            this.extensionValue = derValue2.getOctetString();
        }
    }

    public Extension(ObjectIdentifier objectIdentifier, boolean z2, byte[] bArr) throws IOException {
        this.extensionId = null;
        this.critical = false;
        this.extensionValue = null;
        this.extensionId = objectIdentifier;
        this.critical = z2;
        this.extensionValue = new DerValue(bArr).getOctetString();
    }

    public Extension(Extension extension) {
        this.extensionId = null;
        this.critical = false;
        this.extensionValue = null;
        this.extensionId = extension.extensionId;
        this.critical = extension.critical;
        this.extensionValue = extension.extensionValue;
    }

    public static Extension newExtension(ObjectIdentifier objectIdentifier, boolean z2, byte[] bArr) throws IOException {
        Extension extension = new Extension();
        extension.extensionId = objectIdentifier;
        extension.critical = z2;
        extension.extensionValue = bArr;
        return extension;
    }

    @Override // java.security.cert.Extension, sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new NullPointerException();
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream.putOID(this.extensionId);
        if (this.critical) {
            derOutputStream.putBoolean(this.critical);
        }
        derOutputStream.putOctetString(this.extensionValue);
        derOutputStream2.write((byte) 48, derOutputStream);
        outputStream.write(derOutputStream2.toByteArray());
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        if (this.extensionId == null) {
            throw new IOException("Null OID to encode for the extension!");
        }
        if (this.extensionValue == null) {
            throw new IOException("No value to encode for the extension!");
        }
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putOID(this.extensionId);
        if (this.critical) {
            derOutputStream2.putBoolean(this.critical);
        }
        derOutputStream2.putOctetString(this.extensionValue);
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    @Override // java.security.cert.Extension
    public boolean isCritical() {
        return this.critical;
    }

    public ObjectIdentifier getExtensionId() {
        return this.extensionId;
    }

    @Override // java.security.cert.Extension
    public byte[] getValue() {
        return (byte[]) this.extensionValue.clone();
    }

    public byte[] getExtensionValue() {
        return this.extensionValue;
    }

    @Override // java.security.cert.Extension
    public String getId() {
        return this.extensionId.toString();
    }

    public String toString() {
        String str;
        String str2 = "ObjectId: " + this.extensionId.toString();
        if (this.critical) {
            str = str2 + " Criticality=true\n";
        } else {
            str = str2 + " Criticality=false\n";
        }
        return str;
    }

    public int hashCode() {
        int i2 = 0;
        if (this.extensionValue != null) {
            byte[] bArr = this.extensionValue;
            int length = bArr.length;
            while (length > 0) {
                int i3 = length;
                length--;
                i2 += i3 * bArr[length];
            }
        }
        return (((i2 * 31) + this.extensionId.hashCode()) * 31) + (this.critical ? 1231 : 1237);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Extension)) {
            return false;
        }
        Extension extension = (Extension) obj;
        if (this.critical != extension.critical || !this.extensionId.equals((Object) extension.extensionId)) {
            return false;
        }
        return Arrays.equals(this.extensionValue, extension.extensionValue);
    }
}
