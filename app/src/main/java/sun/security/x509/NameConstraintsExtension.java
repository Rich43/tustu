package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import sun.net.util.IPAddressUtil;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/NameConstraintsExtension.class */
public class NameConstraintsExtension extends Extension implements CertAttrSet<String>, Cloneable {
    public static final String IDENT = "x509.info.extensions.NameConstraints";
    public static final String NAME = "NameConstraints";
    public static final String PERMITTED_SUBTREES = "permitted_subtrees";
    public static final String EXCLUDED_SUBTREES = "excluded_subtrees";
    private static final byte TAG_PERMITTED = 0;
    private static final byte TAG_EXCLUDED = 1;
    private GeneralSubtrees permitted;
    private GeneralSubtrees excluded;
    private boolean hasMin;
    private boolean hasMax;
    private boolean minMaxValid;

    private void calcMinMax() throws IOException {
        this.hasMin = false;
        this.hasMax = false;
        if (this.excluded != null) {
            for (int i2 = 0; i2 < this.excluded.size(); i2++) {
                GeneralSubtree generalSubtree = this.excluded.get(i2);
                if (generalSubtree.getMinimum() != 0) {
                    this.hasMin = true;
                }
                if (generalSubtree.getMaximum() != -1) {
                    this.hasMax = true;
                }
            }
        }
        if (this.permitted != null) {
            for (int i3 = 0; i3 < this.permitted.size(); i3++) {
                GeneralSubtree generalSubtree2 = this.permitted.get(i3);
                if (generalSubtree2.getMinimum() != 0) {
                    this.hasMin = true;
                }
                if (generalSubtree2.getMaximum() != -1) {
                    this.hasMax = true;
                }
            }
        }
        this.minMaxValid = true;
    }

    private void encodeThis() throws IOException {
        this.minMaxValid = false;
        if (this.permitted == null && this.excluded == null) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        if (this.permitted != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            this.permitted.encode(derOutputStream3);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream3);
        }
        if (this.excluded != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            this.excluded.encode(derOutputStream4);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream4);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public NameConstraintsExtension(GeneralSubtrees generalSubtrees, GeneralSubtrees generalSubtrees2) throws IOException {
        this.permitted = null;
        this.excluded = null;
        this.minMaxValid = false;
        this.permitted = generalSubtrees;
        this.excluded = generalSubtrees2;
        this.extensionId = PKIXExtensions.NameConstraints_Id;
        this.critical = true;
        encodeThis();
    }

    public NameConstraintsExtension(Boolean bool, Object obj) throws IOException {
        this.permitted = null;
        this.excluded = null;
        this.minMaxValid = false;
        this.extensionId = PKIXExtensions.NameConstraints_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        DerValue derValue = new DerValue(this.extensionValue);
        if (derValue.tag != 48) {
            throw new IOException("Invalid encoding for NameConstraintsExtension.");
        }
        if (derValue.data == null) {
            return;
        }
        while (derValue.data.available() != 0) {
            DerValue derValue2 = derValue.data.getDerValue();
            if (derValue2.isContextSpecific((byte) 0) && derValue2.isConstructed()) {
                if (this.permitted != null) {
                    throw new IOException("Duplicate permitted GeneralSubtrees in NameConstraintsExtension.");
                }
                derValue2.resetTag((byte) 48);
                this.permitted = new GeneralSubtrees(derValue2);
            } else if (derValue2.isContextSpecific((byte) 1) && derValue2.isConstructed()) {
                if (this.excluded != null) {
                    throw new IOException("Duplicate excluded GeneralSubtrees in NameConstraintsExtension.");
                }
                derValue2.resetTag((byte) 48);
                this.excluded = new GeneralSubtrees(derValue2);
            } else {
                throw new IOException("Invalid encoding of NameConstraintsExtension.");
            }
        }
        this.minMaxValid = false;
    }

    @Override // sun.security.x509.Extension
    public String toString() {
        return super.toString() + "NameConstraints: [" + (this.permitted == null ? "" : "\n    Permitted:" + this.permitted.toString()) + (this.excluded == null ? "" : "\n    Excluded:" + this.excluded.toString()) + "   ]\n";
    }

    @Override // sun.security.x509.Extension, java.security.cert.Extension, sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.NameConstraints_Id;
            this.critical = true;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    @Override // sun.security.x509.CertAttrSet
    public void set(String str, Object obj) throws IOException {
        if (str.equalsIgnoreCase(PERMITTED_SUBTREES)) {
            if (!(obj instanceof GeneralSubtrees)) {
                throw new IOException("Attribute value should be of type GeneralSubtrees.");
            }
            this.permitted = (GeneralSubtrees) obj;
        } else if (str.equalsIgnoreCase(EXCLUDED_SUBTREES)) {
            if (!(obj instanceof GeneralSubtrees)) {
                throw new IOException("Attribute value should be of type GeneralSubtrees.");
            }
            this.excluded = (GeneralSubtrees) obj;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:NameConstraintsExtension.");
        }
        encodeThis();
    }

    @Override // sun.security.x509.CertAttrSet
    public GeneralSubtrees get(String str) throws IOException {
        if (str.equalsIgnoreCase(PERMITTED_SUBTREES)) {
            return this.permitted;
        }
        if (str.equalsIgnoreCase(EXCLUDED_SUBTREES)) {
            return this.excluded;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:NameConstraintsExtension.");
    }

    @Override // sun.security.x509.CertAttrSet
    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(PERMITTED_SUBTREES)) {
            this.permitted = null;
        } else if (str.equalsIgnoreCase(EXCLUDED_SUBTREES)) {
            this.excluded = null;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:NameConstraintsExtension.");
        }
        encodeThis();
    }

    @Override // sun.security.x509.CertAttrSet
    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(PERMITTED_SUBTREES);
        attributeNameEnumeration.addElement(EXCLUDED_SUBTREES);
        return attributeNameEnumeration.elements();
    }

    @Override // sun.security.x509.CertAttrSet
    public String getName() {
        return NAME;
    }

    public void merge(NameConstraintsExtension nameConstraintsExtension) throws IOException {
        GeneralSubtrees generalSubtreesIntersect;
        if (nameConstraintsExtension == null) {
            return;
        }
        GeneralSubtrees generalSubtrees = nameConstraintsExtension.get(EXCLUDED_SUBTREES);
        if (this.excluded == null) {
            this.excluded = generalSubtrees != null ? (GeneralSubtrees) generalSubtrees.clone() : null;
        } else if (generalSubtrees != null) {
            this.excluded.union(generalSubtrees);
        }
        GeneralSubtrees generalSubtrees2 = nameConstraintsExtension.get(PERMITTED_SUBTREES);
        if (this.permitted == null) {
            this.permitted = generalSubtrees2 != null ? (GeneralSubtrees) generalSubtrees2.clone() : null;
        } else if (generalSubtrees2 != null && (generalSubtreesIntersect = this.permitted.intersect(generalSubtrees2)) != null) {
            if (this.excluded != null) {
                this.excluded.union(generalSubtreesIntersect);
            } else {
                this.excluded = (GeneralSubtrees) generalSubtreesIntersect.clone();
            }
        }
        if (this.permitted != null) {
            this.permitted.reduce(this.excluded);
        }
        encodeThis();
    }

    public boolean verify(X509Certificate x509Certificate) throws IOException {
        String valueString;
        if (x509Certificate == null) {
            throw new IOException("Certificate is null");
        }
        if (!this.minMaxValid) {
            calcMinMax();
        }
        if (this.hasMin) {
            throw new IOException("Non-zero minimum BaseDistance in name constraints not supported");
        }
        if (this.hasMax) {
            throw new IOException("Maximum BaseDistance in name constraints not supported");
        }
        X500Name x500NameAsX500Name = X500Name.asX500Name(x509Certificate.getSubjectX500Principal());
        if (!x500NameAsX500Name.isEmpty() && !verify(x500NameAsX500Name)) {
            return false;
        }
        GeneralNames generalNames = null;
        try {
            SubjectAlternativeNameExtension subjectAlternativeNameExtension = X509CertImpl.toImpl(x509Certificate).getSubjectAlternativeNameExtension();
            if (subjectAlternativeNameExtension != null) {
                generalNames = subjectAlternativeNameExtension.get(SubjectAlternativeNameExtension.SUBJECT_NAME);
            }
            if (generalNames == null) {
                generalNames = new GeneralNames();
                for (AVA ava : x500NameAsX500Name.allAvas()) {
                    if (ava.getObjectIdentifier().equals(PKCS9Attribute.EMAIL_ADDRESS_OID) && (valueString = ava.getValueString()) != null) {
                        try {
                            generalNames.add(new GeneralName(new RFC822Name(valueString)));
                        } catch (IOException e2) {
                        }
                    }
                }
            }
            DerValue derValueFindMostSpecificAttribute = x500NameAsX500Name.findMostSpecificAttribute(X500Name.commonName_oid);
            String asString = derValueFindMostSpecificAttribute == null ? null : derValueFindMostSpecificAttribute.getAsString();
            if (asString != null) {
                try {
                    if (IPAddressUtil.isIPv4LiteralAddress(asString) || IPAddressUtil.isIPv6LiteralAddress(asString)) {
                        if (!hasNameType(generalNames, 7)) {
                            generalNames.add(new GeneralName(new IPAddressName(asString)));
                        }
                    } else if (!hasNameType(generalNames, 2)) {
                        generalNames.add(new GeneralName(new DNSName(asString)));
                    }
                } catch (IOException e3) {
                }
            }
            for (int i2 = 0; i2 < generalNames.size(); i2++) {
                if (!verify(generalNames.get(i2).getName())) {
                    return false;
                }
            }
            return true;
        } catch (CertificateException e4) {
            throw new IOException("Unable to extract extensions from certificate: " + e4.getMessage());
        }
    }

    private static boolean hasNameType(GeneralNames generalNames, int i2) {
        Iterator<GeneralName> it = generalNames.names().iterator();
        while (it.hasNext()) {
            if (it.next().getType() == i2) {
                return true;
            }
        }
        return false;
    }

    public boolean verify(GeneralNameInterface generalNameInterface) throws IOException {
        GeneralName name;
        GeneralNameInterface name2;
        GeneralName name3;
        GeneralNameInterface name4;
        if (generalNameInterface == null) {
            throw new IOException("name is null");
        }
        if (this.excluded != null && this.excluded.size() > 0) {
            for (int i2 = 0; i2 < this.excluded.size(); i2++) {
                GeneralSubtree generalSubtree = this.excluded.get(i2);
                if (generalSubtree != null && (name3 = generalSubtree.getName()) != null && (name4 = name3.getName()) != null) {
                    switch (name4.constrains(generalNameInterface)) {
                        case 0:
                        case 1:
                            return false;
                    }
                }
            }
        }
        if (this.permitted != null && this.permitted.size() > 0) {
            boolean z2 = false;
            for (int i3 = 0; i3 < this.permitted.size(); i3++) {
                GeneralSubtree generalSubtree2 = this.permitted.get(i3);
                if (generalSubtree2 != null && (name = generalSubtree2.getName()) != null && (name2 = name.getName()) != null) {
                    switch (name2.constrains(generalNameInterface)) {
                        case 0:
                        case 1:
                            return true;
                        case 2:
                        case 3:
                            z2 = true;
                            break;
                    }
                }
            }
            if (z2) {
                return false;
            }
            return true;
        }
        return true;
    }

    public Object clone() {
        try {
            NameConstraintsExtension nameConstraintsExtension = (NameConstraintsExtension) super.clone();
            if (this.permitted != null) {
                nameConstraintsExtension.permitted = (GeneralSubtrees) this.permitted.clone();
            }
            if (this.excluded != null) {
                nameConstraintsExtension.excluded = (GeneralSubtrees) this.excluded.clone();
            }
            return nameConstraintsExtension;
        } catch (CloneNotSupportedException e2) {
            throw new RuntimeException("CloneNotSupportedException while cloning NameConstraintsException. This should never happen.");
        }
    }
}
