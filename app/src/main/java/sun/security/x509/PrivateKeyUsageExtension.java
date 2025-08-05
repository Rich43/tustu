package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/PrivateKeyUsageExtension.class */
public class PrivateKeyUsageExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.PrivateKeyUsage";
    public static final String NAME = "PrivateKeyUsage";
    public static final String NOT_BEFORE = "not_before";
    public static final String NOT_AFTER = "not_after";
    private static final byte TAG_BEFORE = 0;
    private static final byte TAG_AFTER = 1;
    private Date notBefore;
    private Date notAfter;

    private void encodeThis() throws IOException {
        if (this.notBefore == null && this.notAfter == null) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        if (this.notBefore != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putGeneralizedTime(this.notBefore);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 0), derOutputStream3);
        }
        if (this.notAfter != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putGeneralizedTime(this.notAfter);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1), derOutputStream4);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public PrivateKeyUsageExtension(Date date, Date date2) throws IOException {
        this.notBefore = null;
        this.notAfter = null;
        this.notBefore = date;
        this.notAfter = date2;
        this.extensionId = PKIXExtensions.PrivateKeyUsage_Id;
        this.critical = false;
        encodeThis();
    }

    public PrivateKeyUsageExtension(Boolean bool, Object obj) throws IOException, CertificateException {
        this.notBefore = null;
        this.notAfter = null;
        this.extensionId = PKIXExtensions.PrivateKeyUsage_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        for (DerValue derValue : new DerInputStream(this.extensionValue).getSequence(2)) {
            if (derValue.isContextSpecific((byte) 0) && !derValue.isConstructed()) {
                if (this.notBefore != null) {
                    throw new CertificateParsingException("Duplicate notBefore in PrivateKeyUsage.");
                }
                derValue.resetTag((byte) 24);
                this.notBefore = new DerInputStream(derValue.toByteArray()).getGeneralizedTime();
            } else if (derValue.isContextSpecific((byte) 1) && !derValue.isConstructed()) {
                if (this.notAfter != null) {
                    throw new CertificateParsingException("Duplicate notAfter in PrivateKeyUsage.");
                }
                derValue.resetTag((byte) 24);
                this.notAfter = new DerInputStream(derValue.toByteArray()).getGeneralizedTime();
            } else {
                throw new IOException("Invalid encoding of PrivateKeyUsageExtension");
            }
        }
    }

    @Override // sun.security.x509.Extension
    public String toString() {
        return super.toString() + "PrivateKeyUsage: [\n" + (this.notBefore == null ? "" : "From: " + this.notBefore.toString() + ", ") + (this.notAfter == null ? "" : "To: " + this.notAfter.toString()) + "]\n";
    }

    public void valid() throws CertificateNotYetValidException, CertificateExpiredException {
        valid(new Date());
    }

    public void valid(Date date) throws CertificateNotYetValidException, CertificateExpiredException {
        Objects.requireNonNull(date);
        if (this.notBefore != null && this.notBefore.after(date)) {
            throw new CertificateNotYetValidException("NotBefore: " + this.notBefore.toString());
        }
        if (this.notAfter != null && this.notAfter.before(date)) {
            throw new CertificateExpiredException("NotAfter: " + this.notAfter.toString());
        }
    }

    @Override // sun.security.x509.Extension, java.security.cert.Extension, sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.PrivateKeyUsage_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    @Override // sun.security.x509.CertAttrSet
    public void set(String str, Object obj) throws IOException, CertificateException {
        if (!(obj instanceof Date)) {
            throw new CertificateException("Attribute must be of type Date.");
        }
        if (str.equalsIgnoreCase(NOT_BEFORE)) {
            this.notBefore = (Date) obj;
        } else if (str.equalsIgnoreCase(NOT_AFTER)) {
            this.notAfter = (Date) obj;
        } else {
            throw new CertificateException("Attribute name not recognized by CertAttrSet:PrivateKeyUsage.");
        }
        encodeThis();
    }

    @Override // sun.security.x509.CertAttrSet
    public Date get(String str) throws CertificateException {
        if (str.equalsIgnoreCase(NOT_BEFORE)) {
            return new Date(this.notBefore.getTime());
        }
        if (str.equalsIgnoreCase(NOT_AFTER)) {
            return new Date(this.notAfter.getTime());
        }
        throw new CertificateException("Attribute name not recognized by CertAttrSet:PrivateKeyUsage.");
    }

    @Override // sun.security.x509.CertAttrSet
    public void delete(String str) throws IOException, CertificateException {
        if (str.equalsIgnoreCase(NOT_BEFORE)) {
            this.notBefore = null;
        } else if (str.equalsIgnoreCase(NOT_AFTER)) {
            this.notAfter = null;
        } else {
            throw new CertificateException("Attribute name not recognized by CertAttrSet:PrivateKeyUsage.");
        }
        encodeThis();
    }

    @Override // sun.security.x509.CertAttrSet
    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(NOT_BEFORE);
        attributeNameEnumeration.addElement(NOT_AFTER);
        return attributeNameEnumeration.elements();
    }

    @Override // sun.security.x509.CertAttrSet
    public String getName() {
        return NAME;
    }
}
