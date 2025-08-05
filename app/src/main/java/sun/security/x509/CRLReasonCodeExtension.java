package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CRLReason;
import java.util.Enumeration;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/CRLReasonCodeExtension.class */
public class CRLReasonCodeExtension extends Extension implements CertAttrSet<String> {
    public static final String NAME = "CRLReasonCode";
    public static final String REASON = "reason";
    private static CRLReason[] values = CRLReason.values();
    private int reasonCode;

    private void encodeThis() throws IOException {
        if (this.reasonCode == 0) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putEnumerated(this.reasonCode);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public CRLReasonCodeExtension(int i2) throws IOException {
        this(false, i2);
    }

    public CRLReasonCodeExtension(boolean z2, int i2) throws IOException {
        this.reasonCode = 0;
        this.extensionId = PKIXExtensions.ReasonCode_Id;
        this.critical = z2;
        this.reasonCode = i2;
        encodeThis();
    }

    public CRLReasonCodeExtension(Boolean bool, Object obj) throws IOException {
        this.reasonCode = 0;
        this.extensionId = PKIXExtensions.ReasonCode_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        this.reasonCode = new DerValue(this.extensionValue).getEnumerated();
    }

    @Override // sun.security.x509.CertAttrSet
    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof Integer)) {
            throw new IOException("Attribute must be of type Integer.");
        }
        if (str.equalsIgnoreCase(REASON)) {
            this.reasonCode = ((Integer) obj).intValue();
            encodeThis();
            return;
        }
        throw new IOException("Name not supported by CRLReasonCodeExtension");
    }

    @Override // sun.security.x509.CertAttrSet
    public Integer get(String str) throws IOException {
        if (str.equalsIgnoreCase(REASON)) {
            return new Integer(this.reasonCode);
        }
        throw new IOException("Name not supported by CRLReasonCodeExtension");
    }

    @Override // sun.security.x509.CertAttrSet
    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(REASON)) {
            this.reasonCode = 0;
            encodeThis();
            return;
        }
        throw new IOException("Name not supported by CRLReasonCodeExtension");
    }

    @Override // sun.security.x509.Extension
    public String toString() {
        return super.toString() + "    Reason Code: " + ((Object) getReasonCode());
    }

    @Override // sun.security.x509.Extension, java.security.cert.Extension, sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.ReasonCode_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    @Override // sun.security.x509.CertAttrSet
    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(REASON);
        return attributeNameEnumeration.elements();
    }

    @Override // sun.security.x509.CertAttrSet
    public String getName() {
        return NAME;
    }

    public CRLReason getReasonCode() {
        if (this.reasonCode > 0 && this.reasonCode < values.length) {
            return values[this.reasonCode];
        }
        return CRLReason.UNSPECIFIED;
    }
}
