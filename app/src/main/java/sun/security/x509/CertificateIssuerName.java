package sun.security.x509;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.security.auth.x500.X500Principal;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/CertificateIssuerName.class */
public class CertificateIssuerName implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.issuer";
    public static final String NAME = "issuer";
    public static final String DN_NAME = "dname";
    public static final String DN_PRINCIPAL = "x500principal";
    private X500Name dnName;
    private X500Principal dnPrincipal;

    public CertificateIssuerName(X500Name x500Name) {
        this.dnName = x500Name;
    }

    public CertificateIssuerName(DerInputStream derInputStream) throws IOException {
        this.dnName = new X500Name(derInputStream);
    }

    public CertificateIssuerName(InputStream inputStream) throws IOException {
        this.dnName = new X500Name(new DerValue(inputStream));
    }

    @Override // sun.security.x509.CertAttrSet
    public String toString() {
        return this.dnName == null ? "" : this.dnName.toString();
    }

    @Override // sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        this.dnName.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    @Override // sun.security.x509.CertAttrSet
    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof X500Name)) {
            throw new IOException("Attribute must be of type X500Name.");
        }
        if (str.equalsIgnoreCase("dname")) {
            this.dnName = (X500Name) obj;
            this.dnPrincipal = null;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuerName.");
    }

    @Override // sun.security.x509.CertAttrSet
    public Object get(String str) throws IOException {
        if (str.equalsIgnoreCase("dname")) {
            return this.dnName;
        }
        if (str.equalsIgnoreCase("x500principal")) {
            if (this.dnPrincipal == null && this.dnName != null) {
                this.dnPrincipal = this.dnName.asX500Principal();
            }
            return this.dnPrincipal;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuerName.");
    }

    @Override // sun.security.x509.CertAttrSet
    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("dname")) {
            this.dnName = null;
            this.dnPrincipal = null;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuerName.");
    }

    @Override // sun.security.x509.CertAttrSet
    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("dname");
        return attributeNameEnumeration.elements();
    }

    @Override // sun.security.x509.CertAttrSet
    public String getName() {
        return "issuer";
    }
}
