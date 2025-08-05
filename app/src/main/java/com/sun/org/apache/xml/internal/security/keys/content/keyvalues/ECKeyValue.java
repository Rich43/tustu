package com.sun.org.apache.xml.internal.security.keys.content.keyvalues;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.I18n;
import com.sun.org.apache.xml.internal.security.utils.Signature11ElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.xml.crypto.MarshalException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/keyvalues/ECKeyValue.class */
public class ECKeyValue extends Signature11ElementProxy implements KeyValueContent {
    private static final Curve SECP256R1 = initializeCurve("secp256r1 [NIST P-256, X9.62 prime256v1]", "1.2.840.10045.3.1.7", "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF", "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC", "5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B", "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296", "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5", "FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551", 1);
    private static final Curve SECP384R1 = initializeCurve("secp384r1 [NIST P-384]", "1.3.132.0.34", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFC", "B3312FA7E23EE7E4988E056BE3F82D19181D9C6EFE8141120314088F5013875AC656398D8A2ED19D2A85C8EDD3EC2AEF", "AA87CA22BE8B05378EB1C71EF320AD746E1D3B628BA79B9859F741E082542A385502F25DBF55296C3A545E3872760AB7", "3617DE4A96262C6F5D9E98BF9292DC29F8F41DBD289A147CE9DA3113B5F0B8C00A60B1CE1D7E819D7A431D7C90EA0E5F", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC7634D81F4372DDF581A0DB248B0A77AECEC196ACCC52973", 1);
    private static final Curve SECP521R1 = initializeCurve("secp521r1 [NIST P-521]", "1.3.132.0.35", "01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", "01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC", "0051953EB9618E1C9A1F929A21A0B68540EEA2DA725B99B315F3B8B489918EF109E156193951EC7E937B1652C0BD3BB1BF073573DF883D2C34F1EF451FD46B503F00", "00C6858E06B70404E9CD9E3ECB662395B4429C648139053FB521F828AF606B4D3DBAA14B5E77EFE75928FE1DC127A2FFA8DE3348B3C1856A429BF97E7E31C2E5BD66", "011839296A789A3BC0045C8A5FB42C7D1BD998F54449579B446817AFBD17273E662C97EE72995EF42640C550B9013FAD0761353C7086A272C24088BE94769FD16650", "01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA51868783BF2F966B7FCC0148F709A5D03BB5C9B8899C47AEBB6FB71E91386409", 1);

    private static Curve initializeCurve(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int i2) {
        return new Curve(str, str2, new EllipticCurve(new ECFieldFp(bigInt(str3)), bigInt(str4), bigInt(str5)), new ECPoint(bigInt(str6), bigInt(str7)), bigInt(str8), i2);
    }

    public ECKeyValue(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public ECKeyValue(Document document, Key key) throws DOMException, IllegalArgumentException {
        super(document);
        addReturnToSelf();
        if (key instanceof ECPublicKey) {
            ECParameterSpec params = ((ECPublicKey) key).getParams();
            String curveOid = getCurveOid(params);
            if (curveOid == null) {
                throw new IllegalArgumentException("Invalid ECParameterSpec");
            }
            Element elementCreateElementInSignature11Space = XMLUtils.createElementInSignature11Space(getDocument(), "NamedCurve");
            elementCreateElementInSignature11Space.setAttributeNS(null, Constants._ATT_URI, "urn:oid:" + curveOid);
            appendSelf(elementCreateElementInSignature11Space);
            addReturnToSelf();
            String strEncodeToString = XMLUtils.encodeToString(encodePoint(((ECPublicKey) key).getW(), params.getCurve()));
            Node nodeCreateElementInSignature11Space = XMLUtils.createElementInSignature11Space(getDocument(), "PublicKey");
            nodeCreateElementInSignature11Space.appendChild(getDocument().createTextNode(strEncodeToString));
            appendSelf(nodeCreateElementInSignature11Space);
            addReturnToSelf();
            return;
        }
        throw new IllegalArgumentException(I18n.translate("KeyValue.IllegalArgument", new Object[]{Constants._TAG_ECKEYVALUE, key.getClass().getName()}));
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.content.keyvalues.KeyValueContent
    public PublicKey getPublicKey() throws MarshalException, DOMException, XMLSecurityException {
        try {
            Element firstChildElement = getFirstChildElement(getElement());
            if (firstChildElement == null) {
                throw new MarshalException("KeyValue must contain at least one type");
            }
            if ("ECParameters".equals(firstChildElement.getLocalName()) && Constants.SignatureSpec11NS.equals(firstChildElement.getNamespaceURI())) {
                throw new UnsupportedOperationException("ECParameters not supported");
            }
            if ("NamedCurve".equals(firstChildElement.getLocalName()) && Constants.SignatureSpec11NS.equals(firstChildElement.getNamespaceURI())) {
                String attributeNS = null;
                if (firstChildElement.hasAttributeNS(null, Constants._ATT_URI)) {
                    attributeNS = firstChildElement.getAttributeNS(null, Constants._ATT_URI);
                }
                if (attributeNS.startsWith("urn:oid:")) {
                    ECParameterSpec eCParameterSpec = getECParameterSpec(attributeNS.substring("urn:oid:".length()));
                    if (eCParameterSpec == null) {
                        throw new MarshalException("Invalid curve OID");
                    }
                    try {
                        return KeyFactory.getInstance("EC").generatePublic(new ECPublicKeySpec(decodePoint(XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(getNextSiblingElement(firstChildElement, "PublicKey", Constants.SignatureSpec11NS))), eCParameterSpec.getCurve()), eCParameterSpec));
                    } catch (IOException e2) {
                        throw new MarshalException("Invalid EC Point", e2);
                    }
                }
                throw new MarshalException("Invalid NamedCurve URI");
            }
            throw new MarshalException("Invalid ECKeyValue");
        } catch (NoSuchAlgorithmException e3) {
            throw new XMLSecurityException(e3);
        } catch (InvalidKeySpecException e4) {
            throw new XMLSecurityException(e4);
        } catch (MarshalException e5) {
            throw new XMLSecurityException(e5);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_ECKEYVALUE;
    }

    private static Element getFirstChildElement(Node node) {
        Node node2;
        Node firstChild = node.getFirstChild();
        while (true) {
            node2 = firstChild;
            if (node2 == null || node2.getNodeType() == 1) {
                break;
            }
            firstChild = node2.getNextSibling();
        }
        return (Element) node2;
    }

    private static Element getNextSiblingElement(Node node, String str, String str2) throws MarshalException {
        return verifyElement(getNextSiblingElement(node), str, str2);
    }

    private static Element getNextSiblingElement(Node node) {
        Node node2;
        Node nextSibling = node.getNextSibling();
        while (true) {
            node2 = nextSibling;
            if (node2 == null || node2.getNodeType() == 1) {
                break;
            }
            nextSibling = node2.getNextSibling();
        }
        return (Element) node2;
    }

    private static Element verifyElement(Element element, String str, String str2) throws MarshalException {
        if (element == null) {
            throw new MarshalException("Missing " + str + " element");
        }
        String localName = element.getLocalName();
        String namespaceURI = element.getNamespaceURI();
        if (!localName.equals(str) || ((namespaceURI == null && str2 != null) || (namespaceURI != null && !namespaceURI.equals(str2)))) {
            throw new MarshalException("Invalid element name: " + namespaceURI + CallSiteDescriptor.TOKEN_DELIMITER + localName + ", expected " + str2 + CallSiteDescriptor.TOKEN_DELIMITER + str);
        }
        return element;
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
        return curve.getCurve().getField().getFieldSize() == eCParameterSpec.getCurve().getField().getFieldSize() && curve.getCurve().equals(eCParameterSpec.getCurve()) && curve.getGenerator().equals(eCParameterSpec.getGenerator()) && curve.getOrder().equals(eCParameterSpec.getOrder()) && curve.getCofactor() == eCParameterSpec.getCofactor();
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

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/keyvalues/ECKeyValue$Curve.class */
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

    private static BigInteger bigInt(String str) {
        return new BigInteger(str, 16);
    }
}
