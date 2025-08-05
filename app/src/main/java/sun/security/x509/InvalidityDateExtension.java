package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/InvalidityDateExtension.class */
public class InvalidityDateExtension extends Extension implements CertAttrSet<String> {
    public static final String NAME = "InvalidityDate";
    public static final String DATE = "date";
    private Date date;

    private void encodeThis() throws IOException {
        if (this.date == null) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putGeneralizedTime(this.date);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public InvalidityDateExtension(Date date) throws IOException {
        this(false, date);
    }

    public InvalidityDateExtension(boolean z2, Date date) throws IOException {
        this.extensionId = PKIXExtensions.InvalidityDate_Id;
        this.critical = z2;
        this.date = date;
        encodeThis();
    }

    public InvalidityDateExtension(Boolean bool, Object obj) throws IOException {
        this.extensionId = PKIXExtensions.InvalidityDate_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        this.date = new DerValue(this.extensionValue).getGeneralizedTime();
    }

    @Override // sun.security.x509.CertAttrSet
    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof Date)) {
            throw new IOException("Attribute must be of type Date.");
        }
        if (str.equalsIgnoreCase("date")) {
            this.date = (Date) obj;
            encodeThis();
            return;
        }
        throw new IOException("Name not supported by InvalidityDateExtension");
    }

    @Override // sun.security.x509.CertAttrSet
    public Date get(String str) throws IOException {
        if (str.equalsIgnoreCase("date")) {
            if (this.date == null) {
                return null;
            }
            return new Date(this.date.getTime());
        }
        throw new IOException("Name not supported by InvalidityDateExtension");
    }

    @Override // sun.security.x509.CertAttrSet
    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("date")) {
            this.date = null;
            encodeThis();
            return;
        }
        throw new IOException("Name not supported by InvalidityDateExtension");
    }

    @Override // sun.security.x509.Extension
    public String toString() {
        return super.toString() + "    Invalidity Date: " + String.valueOf(this.date);
    }

    @Override // sun.security.x509.Extension, java.security.cert.Extension, sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.InvalidityDate_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    @Override // sun.security.x509.CertAttrSet
    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("date");
        return attributeNameEnumeration.elements();
    }

    @Override // sun.security.x509.CertAttrSet
    public String getName() {
        return NAME;
    }

    public static InvalidityDateExtension toImpl(java.security.cert.Extension extension) throws IOException {
        if (extension instanceof InvalidityDateExtension) {
            return (InvalidityDateExtension) extension;
        }
        return new InvalidityDateExtension(Boolean.valueOf(extension.isCritical()), extension.getValue());
    }
}
