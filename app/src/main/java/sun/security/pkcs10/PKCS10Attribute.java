package sun.security.pkcs10;

import java.io.IOException;
import java.io.OutputStream;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.util.DerEncoder;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/pkcs10/PKCS10Attribute.class */
public class PKCS10Attribute implements DerEncoder {
    protected ObjectIdentifier attributeId;
    protected Object attributeValue;

    public PKCS10Attribute(DerValue derValue) throws IOException {
        this.attributeId = null;
        this.attributeValue = null;
        PKCS9Attribute pKCS9Attribute = new PKCS9Attribute(derValue);
        this.attributeId = pKCS9Attribute.getOID();
        this.attributeValue = pKCS9Attribute.getValue();
    }

    public PKCS10Attribute(ObjectIdentifier objectIdentifier, Object obj) {
        this.attributeId = null;
        this.attributeValue = null;
        this.attributeId = objectIdentifier;
        this.attributeValue = obj;
    }

    public PKCS10Attribute(PKCS9Attribute pKCS9Attribute) {
        this.attributeId = null;
        this.attributeValue = null;
        this.attributeId = pKCS9Attribute.getOID();
        this.attributeValue = pKCS9Attribute.getValue();
    }

    @Override // sun.security.util.DerEncoder
    public void derEncode(OutputStream outputStream) throws IOException {
        new PKCS9Attribute(this.attributeId, this.attributeValue).derEncode(outputStream);
    }

    public ObjectIdentifier getAttributeId() {
        return this.attributeId;
    }

    public Object getAttributeValue() {
        return this.attributeValue;
    }

    public String toString() {
        return this.attributeValue.toString();
    }
}
