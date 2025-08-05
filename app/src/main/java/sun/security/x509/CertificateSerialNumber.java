package sun.security.x509;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Enumeration;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/CertificateSerialNumber.class */
public class CertificateSerialNumber implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.serialNumber";
    public static final String NAME = "serialNumber";
    public static final String NUMBER = "number";
    private SerialNumber serial;

    public CertificateSerialNumber(BigInteger bigInteger) {
        this.serial = new SerialNumber(bigInteger);
    }

    public CertificateSerialNumber(int i2) {
        this.serial = new SerialNumber(i2);
    }

    public CertificateSerialNumber(DerInputStream derInputStream) throws IOException {
        this.serial = new SerialNumber(derInputStream);
    }

    public CertificateSerialNumber(InputStream inputStream) throws IOException {
        this.serial = new SerialNumber(inputStream);
    }

    public CertificateSerialNumber(DerValue derValue) throws IOException {
        this.serial = new SerialNumber(derValue);
    }

    @Override // sun.security.x509.CertAttrSet
    public String toString() {
        return this.serial == null ? "" : this.serial.toString();
    }

    @Override // sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        this.serial.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    @Override // sun.security.x509.CertAttrSet
    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof SerialNumber)) {
            throw new IOException("Attribute must be of type SerialNumber.");
        }
        if (str.equalsIgnoreCase("number")) {
            this.serial = (SerialNumber) obj;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSerialNumber.");
    }

    @Override // sun.security.x509.CertAttrSet
    public SerialNumber get(String str) throws IOException {
        if (str.equalsIgnoreCase("number")) {
            return this.serial;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSerialNumber.");
    }

    @Override // sun.security.x509.CertAttrSet
    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("number")) {
            this.serial = null;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSerialNumber.");
    }

    @Override // sun.security.x509.CertAttrSet
    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("number");
        return attributeNameEnumeration.elements();
    }

    @Override // sun.security.x509.CertAttrSet
    public String getName() {
        return "serialNumber";
    }
}
