package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyValue.class */
public abstract class DOMKeyValue<K extends PublicKey> extends DOMStructure implements KeyValue {
    private static final String XMLDSIG_11_XMLNS = "http://www.w3.org/2009/xmldsig11#";
    private final K publicKey;

    abstract void marshalPublicKey(Node node, Document document, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException;

    abstract K unmarshalKeyValue(Element element) throws MarshalException;

    public DOMKeyValue(K k2) throws KeyException {
        if (k2 == null) {
            throw new NullPointerException("key cannot be null");
        }
        this.publicKey = k2;
    }

    public DOMKeyValue(Element element) throws MarshalException {
        this.publicKey = (K) unmarshalKeyValue(element);
    }

    static KeyValue unmarshal(Element element) throws MarshalException {
        Element firstChildElement = DOMUtils.getFirstChildElement(element);
        if (firstChildElement == null) {
            throw new MarshalException("KeyValue must contain at least one type");
        }
        String namespaceURI = firstChildElement.getNamespaceURI();
        if (firstChildElement.getLocalName().equals(Constants._TAG_DSAKEYVALUE) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
            return new DSA(firstChildElement);
        }
        if (firstChildElement.getLocalName().equals(Constants._TAG_RSAKEYVALUE) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
            return new RSA(firstChildElement);
        }
        if (firstChildElement.getLocalName().equals(Constants._TAG_ECKEYVALUE) && "http://www.w3.org/2009/xmldsig11#".equals(namespaceURI)) {
            return new EC(firstChildElement);
        }
        return new Unknown(firstChildElement);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyValue
    public PublicKey getPublicKey() throws KeyException {
        if (this.publicKey == null) {
            throw new KeyException("can't convert KeyValue to PublicKey");
        }
        return this.publicKey;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Document ownerDocument = DOMUtils.getOwnerDocument(node);
        Element elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_KEYVALUE, "http://www.w3.org/2000/09/xmldsig#", str);
        marshalPublicKey(elementCreateElement, ownerDocument, str, dOMCryptoContext);
        node.appendChild(elementCreateElement);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PublicKey generatePublicKey(KeyFactory keyFactory, KeySpec keySpec) {
        try {
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e2) {
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyValue)) {
            return false;
        }
        try {
            KeyValue keyValue = (KeyValue) obj;
            if (this.publicKey == null) {
                if (keyValue.getPublicKey() != null) {
                    return false;
                }
                return true;
            }
            if (!this.publicKey.equals(keyValue.getPublicKey())) {
                return false;
            }
            return true;
        } catch (KeyException e2) {
            return false;
        }
    }

    public static BigInteger decode(Element element) throws MarshalException {
        try {
            return new BigInteger(1, XMLUtils.decode(element.getFirstChild().getNodeValue()));
        } catch (Exception e2) {
            throw new MarshalException(e2);
        }
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.publicKey != null) {
            iHashCode = (31 * 17) + this.publicKey.hashCode();
        }
        return iHashCode;
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyValue$RSA.class */
    static final class RSA extends DOMKeyValue<RSAPublicKey> {
        private DOMCryptoBinary modulus;
        private DOMCryptoBinary exponent;
        private KeyFactory rsakf;

        RSA(RSAPublicKey rSAPublicKey) throws KeyException {
            super(rSAPublicKey);
            this.exponent = new DOMCryptoBinary(rSAPublicKey.getPublicExponent());
            this.modulus = new DOMCryptoBinary(rSAPublicKey.getModulus());
        }

        RSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMKeyValue
        void marshalPublicKey(Node node, Document document, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
            Element elementCreateElement = DOMUtils.createElement(document, Constants._TAG_RSAKEYVALUE, "http://www.w3.org/2000/09/xmldsig#", str);
            Element elementCreateElement2 = DOMUtils.createElement(document, Constants._TAG_MODULUS, "http://www.w3.org/2000/09/xmldsig#", str);
            Element elementCreateElement3 = DOMUtils.createElement(document, Constants._TAG_EXPONENT, "http://www.w3.org/2000/09/xmldsig#", str);
            this.modulus.marshal(elementCreateElement2, str, dOMCryptoContext);
            this.exponent.marshal(elementCreateElement3, str, dOMCryptoContext);
            elementCreateElement.appendChild(elementCreateElement2);
            elementCreateElement.appendChild(elementCreateElement3);
            node.appendChild(elementCreateElement);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.jcp.xml.dsig.internal.dom.DOMKeyValue
        public RSAPublicKey unmarshalKeyValue(Element element) throws MarshalException {
            if (this.rsakf == null) {
                try {
                    this.rsakf = KeyFactory.getInstance("RSA");
                } catch (NoSuchAlgorithmException e2) {
                    throw new RuntimeException("unable to create RSA KeyFactory: " + e2.getMessage());
                }
            }
            Element firstChildElement = DOMUtils.getFirstChildElement(element, Constants._TAG_MODULUS, "http://www.w3.org/2000/09/xmldsig#");
            return (RSAPublicKey) DOMKeyValue.generatePublicKey(this.rsakf, new RSAPublicKeySpec(decode(firstChildElement), decode(DOMUtils.getNextSiblingElement(firstChildElement, Constants._TAG_EXPONENT, "http://www.w3.org/2000/09/xmldsig#"))));
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyValue$DSA.class */
    static final class DSA extends DOMKeyValue<DSAPublicKey> {

        /* renamed from: p, reason: collision with root package name */
        private DOMCryptoBinary f13131p;

        /* renamed from: q, reason: collision with root package name */
        private DOMCryptoBinary f13132q;

        /* renamed from: g, reason: collision with root package name */
        private DOMCryptoBinary f13133g;

        /* renamed from: y, reason: collision with root package name */
        private DOMCryptoBinary f13134y;
        private KeyFactory dsakf;

        DSA(DSAPublicKey dSAPublicKey) throws KeyException {
            super(dSAPublicKey);
            DSAParams params = dSAPublicKey.getParams();
            this.f13131p = new DOMCryptoBinary(params.getP());
            this.f13132q = new DOMCryptoBinary(params.getQ());
            this.f13133g = new DOMCryptoBinary(params.getG());
            this.f13134y = new DOMCryptoBinary(dSAPublicKey.getY());
        }

        DSA(Element element) throws MarshalException {
            super(element);
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMKeyValue
        void marshalPublicKey(Node node, Document document, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
            Element elementCreateElement = DOMUtils.createElement(document, Constants._TAG_DSAKEYVALUE, "http://www.w3.org/2000/09/xmldsig#", str);
            Element elementCreateElement2 = DOMUtils.createElement(document, Constants._TAG_P, "http://www.w3.org/2000/09/xmldsig#", str);
            Element elementCreateElement3 = DOMUtils.createElement(document, "Q", "http://www.w3.org/2000/09/xmldsig#", str);
            Element elementCreateElement4 = DOMUtils.createElement(document, "G", "http://www.w3.org/2000/09/xmldsig#", str);
            Element elementCreateElement5 = DOMUtils.createElement(document, Constants._TAG_Y, "http://www.w3.org/2000/09/xmldsig#", str);
            this.f13131p.marshal(elementCreateElement2, str, dOMCryptoContext);
            this.f13132q.marshal(elementCreateElement3, str, dOMCryptoContext);
            this.f13133g.marshal(elementCreateElement4, str, dOMCryptoContext);
            this.f13134y.marshal(elementCreateElement5, str, dOMCryptoContext);
            elementCreateElement.appendChild(elementCreateElement2);
            elementCreateElement.appendChild(elementCreateElement3);
            elementCreateElement.appendChild(elementCreateElement4);
            elementCreateElement.appendChild(elementCreateElement5);
            node.appendChild(elementCreateElement);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.jcp.xml.dsig.internal.dom.DOMKeyValue
        public DSAPublicKey unmarshalKeyValue(Element element) throws MarshalException {
            if (this.dsakf == null) {
                try {
                    this.dsakf = KeyFactory.getInstance("DSA");
                } catch (NoSuchAlgorithmException e2) {
                    throw new RuntimeException("unable to create DSA KeyFactory: " + e2.getMessage());
                }
            }
            Element firstChildElement = DOMUtils.getFirstChildElement(element);
            if (firstChildElement == null) {
                throw new MarshalException("KeyValue must contain at least one type");
            }
            BigInteger bigIntegerDecode = null;
            BigInteger bigIntegerDecode2 = null;
            if (firstChildElement.getLocalName().equals(Constants._TAG_P) && "http://www.w3.org/2000/09/xmldsig#".equals(firstChildElement.getNamespaceURI())) {
                bigIntegerDecode = decode(firstChildElement);
                Element nextSiblingElement = DOMUtils.getNextSiblingElement(firstChildElement, "Q", "http://www.w3.org/2000/09/xmldsig#");
                bigIntegerDecode2 = decode(nextSiblingElement);
                firstChildElement = DOMUtils.getNextSiblingElement(nextSiblingElement);
            }
            BigInteger bigIntegerDecode3 = null;
            if (firstChildElement != null && firstChildElement.getLocalName().equals("G") && "http://www.w3.org/2000/09/xmldsig#".equals(firstChildElement.getNamespaceURI())) {
                bigIntegerDecode3 = decode(firstChildElement);
                firstChildElement = DOMUtils.getNextSiblingElement(firstChildElement, Constants._TAG_Y, "http://www.w3.org/2000/09/xmldsig#");
            }
            BigInteger bigIntegerDecode4 = null;
            if (firstChildElement != null) {
                bigIntegerDecode4 = decode(firstChildElement);
                DOMUtils.getNextSiblingElement(firstChildElement);
            }
            return (DSAPublicKey) DOMKeyValue.generatePublicKey(this.dsakf, new DSAPublicKeySpec(bigIntegerDecode4, bigIntegerDecode, bigIntegerDecode2, bigIntegerDecode3));
        }
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyValue$EC.class */
    static final class EC extends DOMKeyValue<ECPublicKey> {
        private byte[] ecPublicKey;
        private KeyFactory eckf;
        private ECParameterSpec ecParams;
        private static final Curve SECP256R1 = initializeCurve("secp256r1 [NIST P-256, X9.62 prime256v1]", "1.2.840.10045.3.1.7", "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF", "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC", "5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B", "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296", "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5", "FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551", 1);
        private static final Curve SECP384R1 = initializeCurve("secp384r1 [NIST P-384]", "1.3.132.0.34", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFC", "B3312FA7E23EE7E4988E056BE3F82D19181D9C6EFE8141120314088F5013875AC656398D8A2ED19D2A85C8EDD3EC2AEF", "AA87CA22BE8B05378EB1C71EF320AD746E1D3B628BA79B9859F741E082542A385502F25DBF55296C3A545E3872760AB7", "3617DE4A96262C6F5D9E98BF9292DC29F8F41DBD289A147CE9DA3113B5F0B8C00A60B1CE1D7E819D7A431D7C90EA0E5F", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC7634D81F4372DDF581A0DB248B0A77AECEC196ACCC52973", 1);
        private static final Curve SECP521R1 = initializeCurve("secp521r1 [NIST P-521]", "1.3.132.0.35", "01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", "01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC", "0051953EB9618E1C9A1F929A21A0B68540EEA2DA725B99B315F3B8B489918EF109E156193951EC7E937B1652C0BD3BB1BF073573DF883D2C34F1EF451FD46B503F00", "00C6858E06B70404E9CD9E3ECB662395B4429C648139053FB521F828AF606B4D3DBAA14B5E77EFE75928FE1DC127A2FFA8DE3348B3C1856A429BF97E7E31C2E5BD66", "011839296A789A3BC0045C8A5FB42C7D1BD998F54449579B446817AFBD17273E662C97EE72995EF42640C550B9013FAD0761353C7086A272C24088BE94769FD16650", "01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA51868783BF2F966B7FCC0148F709A5D03BB5C9B8899C47AEBB6FB71E91386409", 1);

        private static Curve initializeCurve(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int i2) {
            return new Curve(str, str2, new EllipticCurve(new ECFieldFp(DOMKeyValue.bigInt(str3)), DOMKeyValue.bigInt(str4), DOMKeyValue.bigInt(str5)), new ECPoint(DOMKeyValue.bigInt(str6), DOMKeyValue.bigInt(str7)), DOMKeyValue.bigInt(str8), i2);
        }

        EC(ECPublicKey eCPublicKey) throws KeyException {
            super(eCPublicKey);
            ECPoint w2 = eCPublicKey.getW();
            this.ecParams = eCPublicKey.getParams();
            this.ecPublicKey = encodePoint(w2, this.ecParams.getCurve());
        }

        EC(Element element) throws MarshalException {
            super(element);
        }

        private static ECPoint decodePoint(byte[] bArr, EllipticCurve ellipticCurve) throws IOException {
            if (bArr.length == 0 || bArr[0] != 4) {
                throw new IOException("Only uncompressed point format supported");
            }
            int length = (bArr.length - 1) / 2;
            if (length != ((ellipticCurve.getField().getFieldSize() + 7) >> 3)) {
                throw new IOException("Point does not match field size");
            }
            return new ECPoint(new BigInteger(1, Arrays.copyOfRange(bArr, 1, 1 + length)), new BigInteger(1, Arrays.copyOfRange(bArr, length + 1, length + 1 + length)));
        }

        private static byte[] encodePoint(ECPoint eCPoint, EllipticCurve ellipticCurve) {
            int fieldSize = (ellipticCurve.getField().getFieldSize() + 7) >> 3;
            byte[] bArrTrimZeroes = trimZeroes(eCPoint.getAffineX().toByteArray());
            byte[] bArrTrimZeroes2 = trimZeroes(eCPoint.getAffineY().toByteArray());
            if (bArrTrimZeroes.length > fieldSize || bArrTrimZeroes2.length > fieldSize) {
                throw new RuntimeException("Point coordinates do not match field size");
            }
            byte[] bArr = new byte[1 + (fieldSize << 1)];
            bArr[0] = 4;
            System.arraycopy(bArrTrimZeroes, 0, bArr, (fieldSize - bArrTrimZeroes.length) + 1, bArrTrimZeroes.length);
            System.arraycopy(bArrTrimZeroes2, 0, bArr, bArr.length - bArrTrimZeroes2.length, bArrTrimZeroes2.length);
            return bArr;
        }

        private static byte[] trimZeroes(byte[] bArr) {
            int i2 = 0;
            while (i2 < bArr.length - 1 && bArr[i2] == 0) {
                i2++;
            }
            if (i2 == 0) {
                return bArr;
            }
            return Arrays.copyOfRange(bArr, i2, bArr.length);
        }

        private static String getCurveOid(ECParameterSpec eCParameterSpec) {
            Curve curve;
            if (matchCurve(eCParameterSpec, SECP256R1)) {
                curve = SECP256R1;
            } else if (matchCurve(eCParameterSpec, SECP384R1)) {
                curve = SECP384R1;
            } else if (matchCurve(eCParameterSpec, SECP521R1)) {
                curve = SECP521R1;
            } else {
                return null;
            }
            return curve.getObjectId();
        }

        private static boolean matchCurve(ECParameterSpec eCParameterSpec, Curve curve) {
            if (curve.getCurve().getField().getFieldSize() == eCParameterSpec.getCurve().getField().getFieldSize() && curve.getCurve().equals(eCParameterSpec.getCurve()) && curve.getGenerator().equals(eCParameterSpec.getGenerator()) && curve.getOrder().equals(eCParameterSpec.getOrder()) && curve.getCofactor() == eCParameterSpec.getCofactor()) {
                return true;
            }
            return false;
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMKeyValue
        void marshalPublicKey(Node node, Document document, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
            String nSPrefix = DOMUtils.getNSPrefix(dOMCryptoContext, "http://www.w3.org/2009/xmldsig11#");
            Node nodeCreateElement = DOMUtils.createElement(document, Constants._TAG_ECKEYVALUE, "http://www.w3.org/2009/xmldsig11#", nSPrefix);
            Element elementCreateElement = DOMUtils.createElement(document, "NamedCurve", "http://www.w3.org/2009/xmldsig11#", nSPrefix);
            Node nodeCreateElement2 = DOMUtils.createElement(document, "PublicKey", "http://www.w3.org/2009/xmldsig11#", nSPrefix);
            String curveOid = getCurveOid(this.ecParams);
            if (curveOid == null) {
                throw new MarshalException("Invalid ECParameterSpec");
            }
            DOMUtils.setAttribute(elementCreateElement, Constants._ATT_URI, "urn:oid:" + curveOid);
            elementCreateElement.setAttributeNS("http://www.w3.org/2000/xmlns/", (nSPrefix == null || nSPrefix.length() == 0) ? "xmlns" : "xmlns:" + nSPrefix, "http://www.w3.org/2009/xmldsig11#");
            nodeCreateElement.appendChild(elementCreateElement);
            nodeCreateElement2.appendChild(DOMUtils.getOwnerDocument(nodeCreateElement2).createTextNode(XMLUtils.encodeToString(this.ecPublicKey)));
            nodeCreateElement.appendChild(nodeCreateElement2);
            node.appendChild(nodeCreateElement);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.jcp.xml.dsig.internal.dom.DOMKeyValue
        public ECPublicKey unmarshalKeyValue(Element element) throws MarshalException, DOMException {
            if (this.eckf == null) {
                try {
                    this.eckf = KeyFactory.getInstance("EC");
                } catch (NoSuchAlgorithmException e2) {
                    throw new RuntimeException("unable to create EC KeyFactory: " + e2.getMessage());
                }
            }
            Element firstChildElement = DOMUtils.getFirstChildElement(element);
            if (firstChildElement == null) {
                throw new MarshalException("KeyValue must contain at least one type");
            }
            if (firstChildElement.getLocalName().equals("ECParameters") && "http://www.w3.org/2009/xmldsig11#".equals(firstChildElement.getNamespaceURI())) {
                throw new UnsupportedOperationException("ECParameters not supported");
            }
            if (firstChildElement.getLocalName().equals("NamedCurve") && "http://www.w3.org/2009/xmldsig11#".equals(firstChildElement.getNamespaceURI())) {
                String attributeValue = DOMUtils.getAttributeValue(firstChildElement, Constants._ATT_URI);
                if (attributeValue.startsWith("urn:oid:")) {
                    ECParameterSpec eCParameterSpec = getECParameterSpec(attributeValue.substring("urn:oid:".length()));
                    if (eCParameterSpec == null) {
                        throw new MarshalException("Invalid curve OID");
                    }
                    try {
                        return (ECPublicKey) DOMKeyValue.generatePublicKey(this.eckf, new ECPublicKeySpec(decodePoint(XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(DOMUtils.getNextSiblingElement(firstChildElement, "PublicKey", "http://www.w3.org/2009/xmldsig11#"))), eCParameterSpec.getCurve()), eCParameterSpec));
                    } catch (IOException e3) {
                        throw new MarshalException("Invalid EC Point", e3);
                    }
                }
                throw new MarshalException("Invalid NamedCurve URI");
            }
            throw new MarshalException("Invalid ECKeyValue");
        }

        private static ECParameterSpec getECParameterSpec(String str) {
            if (str.equals(SECP256R1.getObjectId())) {
                return SECP256R1;
            }
            if (str.equals(SECP384R1.getObjectId())) {
                return SECP384R1;
            }
            if (str.equals(SECP521R1.getObjectId())) {
                return SECP521R1;
            }
            return null;
        }

        /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyValue$EC$Curve.class */
        static final class Curve extends ECParameterSpec {
            private final String name;
            private final String oid;

            Curve(String str, String str2, EllipticCurve ellipticCurve, ECPoint eCPoint, BigInteger bigInteger, int i2) {
                super(ellipticCurve, eCPoint, bigInteger, i2);
                this.name = str;
                this.oid = str2;
            }

            private String getName() {
                return this.name;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public String getObjectId() {
                return this.oid;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static BigInteger bigInt(String str) {
        return new BigInteger(str, 16);
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyValue$Unknown.class */
    static final class Unknown extends DOMKeyValue<PublicKey> {
        private javax.xml.crypto.dom.DOMStructure externalPublicKey;

        Unknown(Element element) throws MarshalException {
            super(element);
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMKeyValue
        PublicKey unmarshalKeyValue(Element element) throws MarshalException {
            this.externalPublicKey = new javax.xml.crypto.dom.DOMStructure(element);
            return null;
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMKeyValue
        void marshalPublicKey(Node node, Document document, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
            node.appendChild(this.externalPublicKey.getNode());
        }
    }
}
