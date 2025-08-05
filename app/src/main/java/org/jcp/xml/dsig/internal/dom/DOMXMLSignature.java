package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.Manifest;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignContext;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMXMLSignature.class */
public final class DOMXMLSignature extends DOMStructure implements XMLSignature {
    private static final Logger LOG = LoggerFactory.getLogger(DOMXMLSignature.class);
    private String id;
    private XMLSignature.SignatureValue sv;
    private KeyInfo ki;
    private List<XMLObject> objects;
    private SignedInfo si;
    private Document ownerDoc;
    private Element localSigElem;
    private Element sigElem;
    private boolean validationStatus;
    private boolean validated;
    private KeySelectorResult ksr;
    private Map<String, XMLStructure> signatureIdMap;

    static {
        Init.init();
    }

    public DOMXMLSignature(SignedInfo signedInfo, KeyInfo keyInfo, List<? extends XMLObject> list, String str, String str2) {
        this.ownerDoc = null;
        this.localSigElem = null;
        this.sigElem = null;
        this.validated = false;
        if (signedInfo == null) {
            throw new NullPointerException("signedInfo cannot be null");
        }
        this.si = signedInfo;
        this.id = str;
        this.sv = new DOMSignatureValue(str2);
        if (list == null) {
            this.objects = Collections.emptyList();
        } else {
            this.objects = Collections.unmodifiableList(new ArrayList(list));
            int size = this.objects.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (!(this.objects.get(i2) instanceof XMLObject)) {
                    throw new ClassCastException("objs[" + i2 + "] is not an XMLObject");
                }
            }
        }
        this.ki = keyInfo;
    }

    public DOMXMLSignature(Element element, XMLCryptoContext xMLCryptoContext, Provider provider) throws MarshalException {
        this.ownerDoc = null;
        this.localSigElem = null;
        this.sigElem = null;
        this.validated = false;
        this.localSigElem = element;
        this.ownerDoc = this.localSigElem.getOwnerDocument();
        this.id = DOMUtils.getAttributeValue(this.localSigElem, Constants._ATT_ID);
        Element firstChildElement = DOMUtils.getFirstChildElement(this.localSigElem, Constants._TAG_SIGNEDINFO, "http://www.w3.org/2000/09/xmldsig#");
        this.si = new DOMSignedInfo(firstChildElement, xMLCryptoContext, provider);
        Element nextSiblingElement = DOMUtils.getNextSiblingElement(firstChildElement, Constants._TAG_SIGNATUREVALUE, "http://www.w3.org/2000/09/xmldsig#");
        this.sv = new DOMSignatureValue(nextSiblingElement);
        Element nextSiblingElement2 = DOMUtils.getNextSiblingElement(nextSiblingElement);
        if (nextSiblingElement2 != null && nextSiblingElement2.getLocalName().equals(Constants._TAG_KEYINFO) && "http://www.w3.org/2000/09/xmldsig#".equals(nextSiblingElement2.getNamespaceURI())) {
            this.ki = new DOMKeyInfo(nextSiblingElement2, xMLCryptoContext, provider);
            nextSiblingElement2 = DOMUtils.getNextSiblingElement(nextSiblingElement2);
        }
        if (nextSiblingElement2 == null) {
            this.objects = Collections.emptyList();
            return;
        }
        ArrayList arrayList = new ArrayList();
        while (nextSiblingElement2 != null) {
            String localName = nextSiblingElement2.getLocalName();
            String namespaceURI = nextSiblingElement2.getNamespaceURI();
            if (!"Object".equals(localName) || !"http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                throw new MarshalException("Invalid element name: " + namespaceURI + CallSiteDescriptor.TOKEN_DELIMITER + localName + ", expected KeyInfo or Object");
            }
            arrayList.add(new DOMXMLObject(nextSiblingElement2, xMLCryptoContext, provider));
            nextSiblingElement2 = DOMUtils.getNextSiblingElement(nextSiblingElement2);
        }
        this.objects = Collections.unmodifiableList(arrayList);
    }

    @Override // javax.xml.crypto.dsig.XMLSignature
    public String getId() {
        return this.id;
    }

    @Override // javax.xml.crypto.dsig.XMLSignature
    public KeyInfo getKeyInfo() {
        return this.ki;
    }

    @Override // javax.xml.crypto.dsig.XMLSignature
    public SignedInfo getSignedInfo() {
        return this.si;
    }

    @Override // javax.xml.crypto.dsig.XMLSignature
    public List<XMLObject> getObjects() {
        return this.objects;
    }

    @Override // javax.xml.crypto.dsig.XMLSignature
    public XMLSignature.SignatureValue getSignatureValue() {
        return this.sv;
    }

    @Override // javax.xml.crypto.dsig.XMLSignature
    public KeySelectorResult getKeySelectorResult() {
        return this.ksr;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        marshal(node, null, str, dOMCryptoContext);
    }

    public void marshal(Node node, Node node2, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        this.ownerDoc = DOMUtils.getOwnerDocument(node);
        this.sigElem = DOMUtils.createElement(this.ownerDoc, Constants._TAG_SIGNATURE, "http://www.w3.org/2000/09/xmldsig#", str);
        if (str == null || str.length() == 0) {
            this.sigElem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/09/xmldsig#");
        } else {
            this.sigElem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str, "http://www.w3.org/2000/09/xmldsig#");
        }
        ((DOMSignedInfo) this.si).marshal(this.sigElem, str, dOMCryptoContext);
        ((DOMSignatureValue) this.sv).marshal(this.sigElem, str, dOMCryptoContext);
        if (this.ki != null) {
            ((DOMKeyInfo) this.ki).marshal(this.sigElem, null, str, dOMCryptoContext);
        }
        int size = this.objects.size();
        for (int i2 = 0; i2 < size; i2++) {
            ((DOMXMLObject) this.objects.get(i2)).marshal(this.sigElem, str, dOMCryptoContext);
        }
        DOMUtils.setAttributeID(this.sigElem, Constants._ATT_ID, this.id);
        node.insertBefore(this.sigElem, node2);
    }

    @Override // javax.xml.crypto.dsig.XMLSignature
    public boolean validate(XMLValidateContext xMLValidateContext) throws XMLSignatureException {
        if (xMLValidateContext == null) {
            throw new NullPointerException("validateContext is null");
        }
        if (!(xMLValidateContext instanceof DOMValidateContext)) {
            throw new ClassCastException("validateContext must be of type DOMValidateContext");
        }
        if (this.validated) {
            return this.validationStatus;
        }
        if (!this.sv.validate(xMLValidateContext)) {
            this.validationStatus = false;
            this.validated = true;
            return this.validationStatus;
        }
        List references = this.si.getReferences();
        boolean z2 = true;
        int size = references.size();
        for (int i2 = 0; z2 && i2 < size; i2++) {
            Reference reference = (Reference) references.get(i2);
            boolean zValidate = reference.validate(xMLValidateContext);
            LOG.debug("Reference [{}] is valid: {}", reference.getURI(), Boolean.valueOf(zValidate));
            z2 &= zValidate;
        }
        if (!z2) {
            LOG.debug("Couldn't validate the References");
            this.validationStatus = false;
            this.validated = true;
            return this.validationStatus;
        }
        boolean z3 = true;
        if (Boolean.TRUE.equals(xMLValidateContext.getProperty("org.jcp.xml.dsig.validateManifests"))) {
            int size2 = this.objects.size();
            for (int i3 = 0; z3 && i3 < size2; i3++) {
                List content = this.objects.get(i3).getContent();
                int size3 = content.size();
                for (int i4 = 0; z3 && i4 < size3; i4++) {
                    XMLStructure xMLStructure = (XMLStructure) content.get(i4);
                    if (xMLStructure instanceof Manifest) {
                        LOG.debug("validating manifest");
                        List references2 = ((Manifest) xMLStructure).getReferences();
                        int size4 = references2.size();
                        for (int i5 = 0; z3 && i5 < size4; i5++) {
                            Reference reference2 = (Reference) references2.get(i5);
                            boolean zValidate2 = reference2.validate(xMLValidateContext);
                            LOG.debug("Manifest ref [{}] is valid: {}", reference2.getURI(), Boolean.valueOf(zValidate2));
                            z3 &= zValidate2;
                        }
                    }
                }
            }
        }
        this.validationStatus = z3;
        this.validated = true;
        return this.validationStatus;
    }

    @Override // javax.xml.crypto.dsig.XMLSignature
    public void sign(XMLSignContext xMLSignContext) throws MarshalException, DOMException, XMLSignatureException {
        if (xMLSignContext == null) {
            throw new NullPointerException("signContext cannot be null");
        }
        DOMSignContext dOMSignContext = (DOMSignContext) xMLSignContext;
        marshal(dOMSignContext.getParent(), dOMSignContext.getNextSibling(), DOMUtils.getSignaturePrefix(dOMSignContext), dOMSignContext);
        ArrayList<Reference> arrayList = new ArrayList();
        this.signatureIdMap = new HashMap();
        this.signatureIdMap.put(this.id, this);
        this.signatureIdMap.put(this.si.getId(), this.si);
        List<Reference> references = this.si.getReferences();
        for (Reference reference : references) {
            this.signatureIdMap.put(reference.getId(), reference);
        }
        for (XMLObject xMLObject : this.objects) {
            this.signatureIdMap.put(xMLObject.getId(), xMLObject);
            for (XMLStructure xMLStructure : xMLObject.getContent()) {
                if (xMLStructure instanceof Manifest) {
                    Manifest manifest = (Manifest) xMLStructure;
                    this.signatureIdMap.put(manifest.getId(), manifest);
                    for (Reference reference2 : manifest.getReferences()) {
                        arrayList.add(reference2);
                        this.signatureIdMap.put(reference2.getId(), reference2);
                    }
                }
            }
        }
        arrayList.addAll(references);
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            digestReference((DOMReference) ((Reference) it.next()), xMLSignContext);
        }
        for (Reference reference3 : arrayList) {
            if (!((DOMReference) reference3).isDigested()) {
                ((DOMReference) reference3).digest(xMLSignContext);
            }
        }
        try {
            KeySelectorResult keySelectorResultSelect = xMLSignContext.getKeySelector().select(this.ki, KeySelector.Purpose.SIGN, this.si.getSignatureMethod(), xMLSignContext);
            Key key = keySelectorResultSelect.getKey();
            if (key == null) {
                throw new XMLSignatureException("the keySelector did not find a signing key");
            }
            this.ksr = keySelectorResultSelect;
            try {
                ((DOMSignatureValue) this.sv).setValue(((AbstractDOMSignatureMethod) this.si.getSignatureMethod()).sign(key, this.si, xMLSignContext));
                this.localSigElem = this.sigElem;
            } catch (InvalidKeyException e2) {
                throw new XMLSignatureException(e2);
            }
        } catch (KeySelectorException e3) {
            throw new XMLSignatureException("cannot find signing key", e3);
        }
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        boolean zEquals2;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XMLSignature)) {
            return false;
        }
        XMLSignature xMLSignature = (XMLSignature) obj;
        if (this.id == null) {
            zEquals = xMLSignature.getId() == null;
        } else {
            zEquals = this.id.equals(xMLSignature.getId());
        }
        boolean z2 = zEquals;
        if (this.ki == null) {
            zEquals2 = xMLSignature.getKeyInfo() == null;
        } else {
            zEquals2 = this.ki.equals(xMLSignature.getKeyInfo());
        }
        return z2 && zEquals2 && this.sv.equals(xMLSignature.getSignatureValue()) && this.si.equals(xMLSignature.getSignedInfo()) && this.objects.equals(xMLSignature.getObjects());
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.id != null) {
            iHashCode = (31 * 17) + this.id.hashCode();
        }
        if (this.ki != null) {
            iHashCode = (31 * iHashCode) + this.ki.hashCode();
        }
        return (31 * ((31 * ((31 * iHashCode) + this.sv.hashCode())) + this.si.hashCode())) + this.objects.hashCode();
    }

    private void digestReference(DOMReference dOMReference, XMLSignContext xMLSignContext) throws DOMException, XMLSignatureException {
        if (dOMReference.isDigested()) {
            return;
        }
        String uri = dOMReference.getURI();
        if (Utils.sameDocumentURI(uri)) {
            String idFromSameDocumentURI = Utils.parseIdFromSameDocumentURI(uri);
            if (idFromSameDocumentURI != null && this.signatureIdMap.containsKey(idFromSameDocumentURI)) {
                XMLStructure xMLStructure = this.signatureIdMap.get(idFromSameDocumentURI);
                if (xMLStructure instanceof DOMReference) {
                    digestReference((DOMReference) xMLStructure, xMLSignContext);
                } else if (xMLStructure instanceof Manifest) {
                    List<Reference> manifestReferences = DOMManifest.getManifestReferences((Manifest) xMLStructure);
                    int size = manifestReferences.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        digestReference((DOMReference) manifestReferences.get(i2), xMLSignContext);
                    }
                }
            }
            if (uri.length() == 0) {
                Iterator<Transform> it = dOMReference.getTransforms().iterator();
                while (it.hasNext()) {
                    String algorithm = it.next().getAlgorithm();
                    if (algorithm.equals("http://www.w3.org/TR/1999/REC-xpath-19991116") || algorithm.equals("http://www.w3.org/2002/06/xmldsig-filter2")) {
                        return;
                    }
                }
            }
        }
        dOMReference.digest(xMLSignContext);
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMXMLSignature$DOMSignatureValue.class */
    public class DOMSignatureValue extends DOMStructure implements XMLSignature.SignatureValue {
        private String id;
        private byte[] value;
        private String valueBase64;
        private Element sigValueElem;
        private boolean validated = false;
        private boolean validationStatus;

        DOMSignatureValue(String str) {
            this.id = str;
        }

        DOMSignatureValue(Element element) throws MarshalException, DOMException {
            this.value = XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(element));
            Attr attributeNodeNS = element.getAttributeNodeNS(null, Constants._ATT_ID);
            if (attributeNodeNS != null) {
                this.id = attributeNodeNS.getValue();
                element.setIdAttributeNode(attributeNodeNS, true);
            } else {
                this.id = null;
            }
            this.sigValueElem = element;
        }

        @Override // javax.xml.crypto.dsig.XMLSignature.SignatureValue
        public String getId() {
            return this.id;
        }

        @Override // javax.xml.crypto.dsig.XMLSignature.SignatureValue
        public byte[] getValue() {
            if (this.value == null) {
                return null;
            }
            return (byte[]) this.value.clone();
        }

        public String getEncodedValue() {
            return this.valueBase64;
        }

        @Override // javax.xml.crypto.dsig.XMLSignature.SignatureValue
        public boolean validate(XMLValidateContext xMLValidateContext) throws XMLSignatureException {
            if (xMLValidateContext == null) {
                throw new NullPointerException("context cannot be null");
            }
            if (!this.validated) {
                SignatureMethod signatureMethod = DOMXMLSignature.this.si.getSignatureMethod();
                Key key = null;
                KeySelectorResult keySelectorResultSelect = null;
                try {
                    KeySelector keySelector = xMLValidateContext.getKeySelector();
                    if (keySelector != null) {
                        keySelectorResultSelect = keySelector.select(DOMXMLSignature.this.ki, KeySelector.Purpose.VERIFY, signatureMethod, xMLValidateContext);
                        if (keySelectorResultSelect != null) {
                            key = keySelectorResultSelect.getKey();
                        }
                    }
                    if (key == null) {
                        throw new XMLSignatureException("the keyselector did not find a validation key");
                    }
                    try {
                        this.validationStatus = ((AbstractDOMSignatureMethod) signatureMethod).verify(key, DOMXMLSignature.this.si, this.value, xMLValidateContext);
                        this.validated = true;
                        DOMXMLSignature.this.ksr = keySelectorResultSelect;
                        return this.validationStatus;
                    } catch (Exception e2) {
                        throw new XMLSignatureException(e2);
                    }
                } catch (KeySelectorException e3) {
                    throw new XMLSignatureException("cannot find validation key", e3);
                }
            }
            return this.validationStatus;
        }

        public boolean equals(Object obj) {
            boolean zEquals;
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof XMLSignature.SignatureValue)) {
                return false;
            }
            XMLSignature.SignatureValue signatureValue = (XMLSignature.SignatureValue) obj;
            if (this.id == null) {
                zEquals = signatureValue.getId() == null;
            } else {
                zEquals = this.id.equals(signatureValue.getId());
            }
            return zEquals;
        }

        public int hashCode() {
            int iHashCode = 17;
            if (this.id != null) {
                iHashCode = (31 * 17) + this.id.hashCode();
            }
            return iHashCode;
        }

        @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
        public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
            this.sigValueElem = DOMUtils.createElement(DOMXMLSignature.this.ownerDoc, Constants._TAG_SIGNATUREVALUE, "http://www.w3.org/2000/09/xmldsig#", str);
            if (this.valueBase64 != null) {
                this.sigValueElem.appendChild(DOMXMLSignature.this.ownerDoc.createTextNode(this.valueBase64));
            }
            DOMUtils.setAttributeID(this.sigValueElem, Constants._ATT_ID, this.id);
            node.appendChild(this.sigValueElem);
        }

        void setValue(byte[] bArr) {
            this.value = bArr;
            this.valueBase64 = XMLUtils.encodeToString(bArr);
            this.sigValueElem.appendChild(DOMXMLSignature.this.ownerDoc.createTextNode(this.valueBase64));
        }
    }
}
