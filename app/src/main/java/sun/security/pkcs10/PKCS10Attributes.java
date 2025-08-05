package sun.security.pkcs10;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/pkcs10/PKCS10Attributes.class */
public class PKCS10Attributes implements DerEncoder {
    private Hashtable<String, PKCS10Attribute> map = new Hashtable<>(3);

    public PKCS10Attributes() {
    }

    public PKCS10Attributes(PKCS10Attribute[] pKCS10AttributeArr) {
        for (int i2 = 0; i2 < pKCS10AttributeArr.length; i2++) {
            this.map.put(pKCS10AttributeArr[i2].getAttributeId().toString(), pKCS10AttributeArr[i2]);
        }
    }

    public PKCS10Attributes(DerInputStream derInputStream) throws IOException {
        DerValue[] set = derInputStream.getSet(3, true);
        if (set == null) {
            throw new IOException("Illegal encoding of attributes");
        }
        for (DerValue derValue : set) {
            PKCS10Attribute pKCS10Attribute = new PKCS10Attribute(derValue);
            this.map.put(pKCS10Attribute.getAttributeId().toString(), pKCS10Attribute);
        }
    }

    public void encode(OutputStream outputStream) throws IOException {
        derEncode(outputStream);
    }

    @Override // sun.security.util.DerEncoder
    public void derEncode(OutputStream outputStream) throws IOException {
        PKCS10Attribute[] pKCS10AttributeArr = (PKCS10Attribute[]) this.map.values().toArray(new PKCS10Attribute[this.map.size()]);
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putOrderedSetOf(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), pKCS10AttributeArr);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void setAttribute(String str, Object obj) {
        if (obj instanceof PKCS10Attribute) {
            this.map.put(str, (PKCS10Attribute) obj);
        }
    }

    public Object getAttribute(String str) {
        return this.map.get(str);
    }

    public void deleteAttribute(String str) {
        this.map.remove(str);
    }

    public Enumeration<PKCS10Attribute> getElements() {
        return this.map.elements();
    }

    public Collection<PKCS10Attribute> getAttributes() {
        return Collections.unmodifiableCollection(this.map.values());
    }

    public boolean equals(Object obj) {
        PKCS10Attribute pKCS10Attribute;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PKCS10Attributes)) {
            return false;
        }
        Collection<PKCS10Attribute> attributes = ((PKCS10Attributes) obj).getAttributes();
        PKCS10Attribute[] pKCS10AttributeArr = (PKCS10Attribute[]) attributes.toArray(new PKCS10Attribute[attributes.size()]);
        if (pKCS10AttributeArr.length != this.map.size()) {
            return false;
        }
        for (PKCS10Attribute pKCS10Attribute2 : pKCS10AttributeArr) {
            String string = pKCS10Attribute2.getAttributeId().toString();
            if (string == null || (pKCS10Attribute = this.map.get(string)) == null || !pKCS10Attribute.equals(pKCS10Attribute2)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public String toString() {
        return this.map.size() + "\n" + this.map.toString();
    }
}
