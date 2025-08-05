package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/x509/CRLDistributionPointsExtension.class */
public class CRLDistributionPointsExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.CRLDistributionPoints";
    public static final String NAME = "CRLDistributionPoints";
    public static final String POINTS = "points";
    private List<DistributionPoint> distributionPoints;
    private String extensionName;

    public CRLDistributionPointsExtension(List<DistributionPoint> list) throws IOException {
        this(false, list);
    }

    public CRLDistributionPointsExtension(boolean z2, List<DistributionPoint> list) throws IOException {
        this(PKIXExtensions.CRLDistributionPoints_Id, z2, list, NAME);
    }

    protected CRLDistributionPointsExtension(ObjectIdentifier objectIdentifier, boolean z2, List<DistributionPoint> list, String str) throws IOException {
        this.extensionId = objectIdentifier;
        this.critical = z2;
        this.distributionPoints = list;
        encodeThis();
        this.extensionName = str;
    }

    public CRLDistributionPointsExtension(Boolean bool, Object obj) throws IOException {
        this(PKIXExtensions.CRLDistributionPoints_Id, bool, obj, NAME);
    }

    protected CRLDistributionPointsExtension(ObjectIdentifier objectIdentifier, Boolean bool, Object obj, String str) throws IOException {
        this.extensionId = objectIdentifier;
        this.critical = bool.booleanValue();
        if (!(obj instanceof byte[])) {
            throw new IOException("Illegal argument type");
        }
        this.extensionValue = (byte[]) obj;
        DerValue derValue = new DerValue(this.extensionValue);
        if (derValue.tag != 48) {
            throw new IOException("Invalid encoding for " + str + " extension.");
        }
        this.distributionPoints = new ArrayList();
        while (derValue.data.available() != 0) {
            this.distributionPoints.add(new DistributionPoint(derValue.data.getDerValue()));
        }
        this.extensionName = str;
    }

    @Override // sun.security.x509.CertAttrSet
    public String getName() {
        return this.extensionName;
    }

    @Override // sun.security.x509.Extension, java.security.cert.Extension, sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        encode(outputStream, PKIXExtensions.CRLDistributionPoints_Id, false);
    }

    protected void encode(OutputStream outputStream, ObjectIdentifier objectIdentifier, boolean z2) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = objectIdentifier;
            this.critical = z2;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    @Override // sun.security.x509.CertAttrSet
    public void set(String str, Object obj) throws IOException {
        if (str.equalsIgnoreCase(POINTS)) {
            if (!(obj instanceof List)) {
                throw new IOException("Attribute value should be of type List.");
            }
            this.distributionPoints = (List) obj;
            encodeThis();
            return;
        }
        throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:" + this.extensionName + ".");
    }

    @Override // sun.security.x509.CertAttrSet
    public List<DistributionPoint> get(String str) throws IOException {
        if (str.equalsIgnoreCase(POINTS)) {
            return this.distributionPoints;
        }
        throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:" + this.extensionName + ".");
    }

    @Override // sun.security.x509.CertAttrSet
    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(POINTS)) {
            this.distributionPoints = Collections.emptyList();
            encodeThis();
            return;
        }
        throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:" + this.extensionName + '.');
    }

    @Override // sun.security.x509.CertAttrSet
    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(POINTS);
        return attributeNameEnumeration.elements();
    }

    private void encodeThis() throws IOException {
        if (this.distributionPoints.isEmpty()) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        Iterator<DistributionPoint> it = this.distributionPoints.iterator();
        while (it.hasNext()) {
            it.next().encode(derOutputStream);
        }
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write((byte) 48, derOutputStream);
        this.extensionValue = derOutputStream2.toByteArray();
    }

    @Override // sun.security.x509.Extension
    public String toString() {
        return super.toString() + this.extensionName + " [\n  " + ((Object) this.distributionPoints) + "]\n";
    }
}
