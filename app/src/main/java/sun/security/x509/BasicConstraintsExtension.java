package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/BasicConstraintsExtension.class */
public class BasicConstraintsExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.BasicConstraints";
    public static final String NAME = "BasicConstraints";
    public static final String IS_CA = "is_ca";
    public static final String PATH_LEN = "path_len";
    private boolean ca;
    private int pathLen;

    private void encodeThis() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        if (this.ca) {
            derOutputStream2.putBoolean(this.ca);
            if (this.pathLen >= 0) {
                derOutputStream2.putInteger(this.pathLen);
            }
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public BasicConstraintsExtension(boolean z2, int i2) throws IOException {
        this(Boolean.valueOf(z2), z2, i2);
    }

    public BasicConstraintsExtension(Boolean bool, boolean z2, int i2) throws IOException {
        this.ca = false;
        this.pathLen = -1;
        this.ca = z2;
        this.pathLen = i2;
        this.extensionId = PKIXExtensions.BasicConstraints_Id;
        this.critical = bool.booleanValue();
        encodeThis();
    }

    public BasicConstraintsExtension(Boolean bool, Object obj) throws IOException {
        this.ca = false;
        this.pathLen = -1;
        this.extensionId = PKIXExtensions.BasicConstraints_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        DerValue derValue = new DerValue(this.extensionValue);
        if (derValue.tag != 48) {
            throw new IOException("Invalid encoding of BasicConstraints");
        }
        if (derValue.data == null || derValue.data.available() == 0) {
            return;
        }
        DerValue derValue2 = derValue.data.getDerValue();
        if (derValue2.tag != 1) {
            return;
        }
        this.ca = derValue2.getBoolean();
        if (derValue.data.available() == 0) {
            this.pathLen = Integer.MAX_VALUE;
            return;
        }
        DerValue derValue3 = derValue.data.getDerValue();
        if (derValue3.tag != 2) {
            throw new IOException("Invalid encoding of BasicConstraints");
        }
        this.pathLen = derValue3.getInteger();
    }

    @Override // sun.security.x509.Extension
    public String toString() {
        String str;
        String str2 = (super.toString() + "BasicConstraints:[\n") + (this.ca ? "  CA:true" : "  CA:false") + "\n";
        if (this.pathLen >= 0) {
            str = str2 + "  PathLen:" + this.pathLen + "\n";
        } else {
            str = str2 + "  PathLen: undefined\n";
        }
        return str + "]\n";
    }

    @Override // sun.security.x509.Extension, java.security.cert.Extension, sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.BasicConstraints_Id;
            if (this.ca) {
                this.critical = true;
            } else {
                this.critical = false;
            }
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    @Override // sun.security.x509.CertAttrSet
    public void set(String str, Object obj) throws IOException {
        if (str.equalsIgnoreCase(IS_CA)) {
            if (!(obj instanceof Boolean)) {
                throw new IOException("Attribute value should be of type Boolean.");
            }
            this.ca = ((Boolean) obj).booleanValue();
        } else if (str.equalsIgnoreCase(PATH_LEN)) {
            if (!(obj instanceof Integer)) {
                throw new IOException("Attribute value should be of type Integer.");
            }
            this.pathLen = ((Integer) obj).intValue();
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:BasicConstraints.");
        }
        encodeThis();
    }

    @Override // sun.security.x509.CertAttrSet
    public Object get(String str) throws IOException {
        if (str.equalsIgnoreCase(IS_CA)) {
            return Boolean.valueOf(this.ca);
        }
        if (str.equalsIgnoreCase(PATH_LEN)) {
            return Integer.valueOf(this.pathLen);
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:BasicConstraints.");
    }

    @Override // sun.security.x509.CertAttrSet
    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(IS_CA)) {
            this.ca = false;
        } else if (str.equalsIgnoreCase(PATH_LEN)) {
            this.pathLen = -1;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:BasicConstraints.");
        }
        encodeThis();
    }

    @Override // sun.security.x509.CertAttrSet
    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(IS_CA);
        attributeNameEnumeration.addElement(PATH_LEN);
        return attributeNameEnumeration.elements();
    }

    @Override // sun.security.x509.CertAttrSet
    public String getName() {
        return NAME;
    }
}
