package sun.security.pkcs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/pkcs/PKCS9Attributes.class */
public class PKCS9Attributes {
    private final Hashtable<ObjectIdentifier, PKCS9Attribute> attributes;
    private final Hashtable<ObjectIdentifier, ObjectIdentifier> permittedAttributes;
    private final byte[] derEncoding;
    private boolean ignoreUnsupportedAttributes;

    public PKCS9Attributes(ObjectIdentifier[] objectIdentifierArr, DerInputStream derInputStream) throws IOException {
        this.attributes = new Hashtable<>(3);
        this.ignoreUnsupportedAttributes = false;
        if (objectIdentifierArr != null) {
            this.permittedAttributes = new Hashtable<>(objectIdentifierArr.length);
            for (int i2 = 0; i2 < objectIdentifierArr.length; i2++) {
                this.permittedAttributes.put(objectIdentifierArr[i2], objectIdentifierArr[i2]);
            }
        } else {
            this.permittedAttributes = null;
        }
        this.derEncoding = decode(derInputStream);
    }

    public PKCS9Attributes(DerInputStream derInputStream) throws IOException {
        this(derInputStream, false);
    }

    public PKCS9Attributes(DerInputStream derInputStream, boolean z2) throws IOException {
        this.attributes = new Hashtable<>(3);
        this.ignoreUnsupportedAttributes = false;
        this.ignoreUnsupportedAttributes = z2;
        this.derEncoding = decode(derInputStream);
        this.permittedAttributes = null;
    }

    public PKCS9Attributes(PKCS9Attribute[] pKCS9AttributeArr) throws IOException, IllegalArgumentException {
        this.attributes = new Hashtable<>(3);
        this.ignoreUnsupportedAttributes = false;
        for (int i2 = 0; i2 < pKCS9AttributeArr.length; i2++) {
            ObjectIdentifier oid = pKCS9AttributeArr[i2].getOID();
            if (this.attributes.containsKey(oid)) {
                throw new IllegalArgumentException("PKCSAttribute " + ((Object) pKCS9AttributeArr[i2].getOID()) + " duplicated while constructing PKCS9Attributes.");
            }
            this.attributes.put(oid, pKCS9AttributeArr[i2]);
        }
        this.derEncoding = generateDerEncoding();
        this.permittedAttributes = null;
    }

    private byte[] decode(DerInputStream derInputStream) throws IOException {
        PKCS9Attribute pKCS9Attribute;
        ObjectIdentifier oid;
        byte[] byteArray = derInputStream.getDerValue().toByteArray();
        byteArray[0] = 49;
        boolean z2 = true;
        for (DerValue derValue : new DerInputStream(byteArray).getSet(3, true)) {
            try {
                pKCS9Attribute = new PKCS9Attribute(derValue);
                oid = pKCS9Attribute.getOID();
            } catch (ParsingException e2) {
                if (this.ignoreUnsupportedAttributes) {
                    z2 = false;
                } else {
                    throw e2;
                }
            }
            if (this.attributes.get(oid) != null) {
                throw new IOException("Duplicate PKCS9 attribute: " + ((Object) oid));
            }
            if (this.permittedAttributes != null && !this.permittedAttributes.containsKey(oid)) {
                throw new IOException("Attribute " + ((Object) oid) + " not permitted in this attribute set");
            }
            this.attributes.put(oid, pKCS9Attribute);
        }
        return z2 ? byteArray : generateDerEncoding();
    }

    public void encode(byte b2, OutputStream outputStream) throws IOException {
        outputStream.write(b2);
        outputStream.write(this.derEncoding, 1, this.derEncoding.length - 1);
    }

    private byte[] generateDerEncoding() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putOrderedSetOf((byte) 49, castToDerEncoder(this.attributes.values().toArray()));
        return derOutputStream.toByteArray();
    }

    public byte[] getDerEncoding() throws IOException {
        return (byte[]) this.derEncoding.clone();
    }

    public PKCS9Attribute getAttribute(ObjectIdentifier objectIdentifier) {
        return this.attributes.get(objectIdentifier);
    }

    public PKCS9Attribute getAttribute(String str) {
        return this.attributes.get(PKCS9Attribute.getOID(str));
    }

    public PKCS9Attribute[] getAttributes() {
        PKCS9Attribute[] pKCS9AttributeArr = new PKCS9Attribute[this.attributes.size()];
        int i2 = 0;
        for (int i3 = 1; i3 < PKCS9Attribute.PKCS9_OIDS.length && i2 < pKCS9AttributeArr.length; i3++) {
            if (PKCS9Attribute.PKCS9_OIDS[i3] != null) {
                pKCS9AttributeArr[i2] = getAttribute(PKCS9Attribute.PKCS9_OIDS[i3]);
                if (pKCS9AttributeArr[i2] != null) {
                    i2++;
                }
            }
        }
        return pKCS9AttributeArr;
    }

    public Object getAttributeValue(ObjectIdentifier objectIdentifier) throws IOException {
        try {
            return getAttribute(objectIdentifier).getValue();
        } catch (NullPointerException e2) {
            throw new IOException("No value found for attribute " + ((Object) objectIdentifier));
        }
    }

    public Object getAttributeValue(String str) throws IOException {
        ObjectIdentifier oid = PKCS9Attribute.getOID(str);
        if (oid == null) {
            throw new IOException("Attribute name " + str + " not recognized or not supported.");
        }
        return getAttributeValue(oid);
    }

    public String toString() {
        PKCS9Attribute attribute;
        StringBuffer stringBuffer = new StringBuffer(200);
        stringBuffer.append("PKCS9 Attributes: [\n\t");
        boolean z2 = true;
        for (int i2 = 1; i2 < PKCS9Attribute.PKCS9_OIDS.length; i2++) {
            if (PKCS9Attribute.PKCS9_OIDS[i2] != null && (attribute = getAttribute(PKCS9Attribute.PKCS9_OIDS[i2])) != null) {
                if (z2) {
                    z2 = false;
                } else {
                    stringBuffer.append(";\n\t");
                }
                stringBuffer.append((Object) attribute);
            }
        }
        stringBuffer.append("\n\t] (end PKCS9 Attributes)");
        return stringBuffer.toString();
    }

    static DerEncoder[] castToDerEncoder(Object[] objArr) {
        DerEncoder[] derEncoderArr = new DerEncoder[objArr.length];
        for (int i2 = 0; i2 < derEncoderArr.length; i2++) {
            derEncoderArr[i2] = (DerEncoder) objArr[i2];
        }
        return derEncoderArr;
    }
}
