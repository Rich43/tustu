package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.dom.ParentNode;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.util.XML11Char;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.utils.ConfigurationError;
import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.MissingResourceException;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/CoreDocumentImpl.class */
public class CoreDocumentImpl extends ParentNode implements Document {
    static final long serialVersionUID = 0;
    protected DocumentTypeImpl docType;
    protected ElementImpl docElement;
    transient NodeListCache fFreeNLCache;
    protected String encoding;
    protected String actualEncoding;
    protected String version;
    protected boolean standalone;
    protected String fDocumentURI;
    private Map<Node, Map<String, ParentNode.UserDataRecord>> nodeUserData;
    protected Map<String, Node> identifiers;
    transient DOMNormalizer domNormalizer;
    transient DOMConfigurationImpl fConfiguration;
    transient Object fXPathEvaluator;
    private static final int[] kidOK = new int[13];
    protected int changes;
    protected boolean allowGrammarAccess;
    protected boolean errorChecking;
    protected boolean ancestorChecking;
    protected boolean xmlVersionChanged;
    private int documentNumber;
    private int nodeCounter;
    private Map<Node, Integer> nodeTable;
    private boolean xml11Version;
    private static final ObjectStreamField[] serialPersistentFields;

    static {
        kidOK[9] = 1410;
        int[] iArr = kidOK;
        int[] iArr2 = kidOK;
        int[] iArr3 = kidOK;
        kidOK[1] = 442;
        iArr3[5] = 442;
        iArr2[6] = 442;
        iArr[11] = 442;
        kidOK[2] = 40;
        int[] iArr4 = kidOK;
        int[] iArr5 = kidOK;
        int[] iArr6 = kidOK;
        int[] iArr7 = kidOK;
        int[] iArr8 = kidOK;
        kidOK[12] = 0;
        iArr8[4] = 0;
        iArr7[3] = 0;
        iArr6[8] = 0;
        iArr5[7] = 0;
        iArr4[10] = 0;
        serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("docType", DocumentTypeImpl.class), new ObjectStreamField("docElement", ElementImpl.class), new ObjectStreamField("fFreeNLCache", NodeListCache.class), new ObjectStreamField("encoding", String.class), new ObjectStreamField("actualEncoding", String.class), new ObjectStreamField("version", String.class), new ObjectStreamField("standalone", Boolean.TYPE), new ObjectStreamField("fDocumentURI", String.class), new ObjectStreamField("userData", Hashtable.class), new ObjectStreamField("identifiers", Hashtable.class), new ObjectStreamField("changes", Integer.TYPE), new ObjectStreamField("allowGrammarAccess", Boolean.TYPE), new ObjectStreamField("errorChecking", Boolean.TYPE), new ObjectStreamField("ancestorChecking", Boolean.TYPE), new ObjectStreamField("xmlVersionChanged", Boolean.TYPE), new ObjectStreamField("documentNumber", Integer.TYPE), new ObjectStreamField("nodeCounter", Integer.TYPE), new ObjectStreamField("nodeTable", Hashtable.class), new ObjectStreamField("xml11Version", Boolean.TYPE)};
    }

    public CoreDocumentImpl() {
        this(false);
    }

    public CoreDocumentImpl(boolean grammarAccess) {
        super(null);
        this.domNormalizer = null;
        this.fConfiguration = null;
        this.fXPathEvaluator = null;
        this.changes = 0;
        this.errorChecking = true;
        this.ancestorChecking = true;
        this.xmlVersionChanged = false;
        this.documentNumber = 0;
        this.nodeCounter = 0;
        this.xml11Version = false;
        this.ownerDocument = this;
        this.allowGrammarAccess = grammarAccess;
        String systemProp = SecuritySupport.getSystemProperty("http://java.sun.com/xml/dom/properties/ancestor-check");
        if (systemProp != null && systemProp.equalsIgnoreCase("false")) {
            this.ancestorChecking = false;
        }
    }

    public CoreDocumentImpl(DocumentType doctype) {
        this(doctype, false);
    }

    public CoreDocumentImpl(DocumentType doctype, boolean grammarAccess) throws MissingResourceException {
        this(grammarAccess);
        if (doctype != null) {
            try {
                DocumentTypeImpl doctypeImpl = (DocumentTypeImpl) doctype;
                doctypeImpl.ownerDocument = this;
                appendChild(doctype);
            } catch (ClassCastException e2) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException((short) 4, msg);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public final Document getOwnerDocument() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 9;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        return "#document";
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.ChildNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        CoreDocumentImpl newdoc = new CoreDocumentImpl();
        callUserDataHandlers(this, newdoc, (short) 1);
        cloneNode(newdoc, deep);
        return newdoc;
    }

    protected void cloneNode(CoreDocumentImpl newdoc, boolean deep) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (deep) {
            Map<Node, String> reversedIdentifiers = null;
            if (this.identifiers != null) {
                reversedIdentifiers = new HashMap<>(this.identifiers.size());
                for (String elementId : this.identifiers.keySet()) {
                    reversedIdentifiers.put(this.identifiers.get(elementId), elementId);
                }
            }
            ChildNode childNode = this.firstChild;
            while (true) {
                ChildNode kid = childNode;
                if (kid == null) {
                    break;
                }
                newdoc.appendChild(newdoc.importNode(kid, true, true, reversedIdentifiers));
                childNode = kid.nextSibling;
            }
        }
        newdoc.allowGrammarAccess = this.allowGrammarAccess;
        newdoc.errorChecking = this.errorChecking;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node insertBefore(Node newChild, Node refChild) throws DOMException, MissingResourceException {
        int type = newChild.getNodeType();
        if (this.errorChecking && ((type == 1 && this.docElement != null) || (type == 10 && this.docType != null))) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
            throw new DOMException((short) 3, msg);
        }
        if (newChild.getOwnerDocument() == null && (newChild instanceof DocumentTypeImpl)) {
            ((DocumentTypeImpl) newChild).ownerDocument = this;
        }
        super.insertBefore(newChild, refChild);
        if (type == 1) {
            this.docElement = (ElementImpl) newChild;
        } else if (type == 10) {
            this.docType = (DocumentTypeImpl) newChild;
        }
        return newChild;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node removeChild(Node oldChild) throws DOMException {
        super.removeChild(oldChild);
        int type = oldChild.getNodeType();
        if (type == 1) {
            this.docElement = null;
        } else if (type == 10) {
            this.docType = null;
        }
        return oldChild;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        if (newChild.getOwnerDocument() == null && (newChild instanceof DocumentTypeImpl)) {
            ((DocumentTypeImpl) newChild).ownerDocument = this;
        }
        if (this.errorChecking && ((this.docType != null && oldChild.getNodeType() != 10 && newChild.getNodeType() == 10) || (this.docElement != null && oldChild.getNodeType() != 1 && newChild.getNodeType() == 1))) {
            throw new DOMException((short) 3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
        }
        super.replaceChild(newChild, oldChild);
        int type = oldChild.getNodeType();
        if (type == 1) {
            this.docElement = (ElementImpl) newChild;
        } else if (type == 10) {
            this.docType = (DocumentTypeImpl) newChild;
        }
        return oldChild;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void setTextContent(String textContent) throws DOMException {
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Object getFeature(String feature, String version) throws ConfigurationError {
        boolean anyVersion = version == null || version.length() == 0;
        if (feature.equalsIgnoreCase("+XPath") && (anyVersion || version.equals("3.0"))) {
            if (this.fXPathEvaluator != null) {
                return this.fXPathEvaluator;
            }
            try {
                Class xpathClass = ObjectFactory.findProviderClass("com.sun.org.apache.xpath.internal.domapi.XPathEvaluatorImpl", true);
                Constructor xpathClassConstr = xpathClass.getConstructor(Document.class);
                Class[] interfaces = xpathClass.getInterfaces();
                for (Class cls : interfaces) {
                    if (cls.getName().equals("org.w3c.dom.xpath.XPathEvaluator")) {
                        this.fXPathEvaluator = xpathClassConstr.newInstance(this);
                        return this.fXPathEvaluator;
                    }
                }
                return null;
            } catch (Exception e2) {
                return null;
            }
        }
        return super.getFeature(feature, version);
    }

    @Override // org.w3c.dom.Document
    public Attr createAttribute(String name) throws DOMException, MissingResourceException {
        if (this.errorChecking && !isXMLName(name, this.xml11Version)) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
            throw new DOMException((short) 5, msg);
        }
        return new AttrImpl(this, name);
    }

    @Override // org.w3c.dom.Document
    public CDATASection createCDATASection(String data) throws DOMException {
        return new CDATASectionImpl(this, data);
    }

    @Override // org.w3c.dom.Document
    public Comment createComment(String data) {
        return new CommentImpl(this, data);
    }

    @Override // org.w3c.dom.Document
    public DocumentFragment createDocumentFragment() {
        return new DocumentFragmentImpl(this);
    }

    @Override // org.w3c.dom.Document
    public Element createElement(String tagName) throws DOMException, MissingResourceException {
        if (this.errorChecking && !isXMLName(tagName, this.xml11Version)) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
            throw new DOMException((short) 5, msg);
        }
        return new ElementImpl(this, tagName);
    }

    @Override // org.w3c.dom.Document
    public EntityReference createEntityReference(String name) throws DOMException, MissingResourceException {
        if (this.errorChecking && !isXMLName(name, this.xml11Version)) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
            throw new DOMException((short) 5, msg);
        }
        return new EntityReferenceImpl(this, name);
    }

    @Override // org.w3c.dom.Document
    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException, MissingResourceException {
        if (this.errorChecking && !isXMLName(target, this.xml11Version)) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
            throw new DOMException((short) 5, msg);
        }
        return new ProcessingInstructionImpl(this, target, data);
    }

    @Override // org.w3c.dom.Document
    public Text createTextNode(String data) {
        return new TextImpl(this, data);
    }

    @Override // org.w3c.dom.Document
    public DocumentType getDoctype() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.docType;
    }

    @Override // org.w3c.dom.Document
    public Element getDocumentElement() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.docElement;
    }

    @Override // org.w3c.dom.Document
    public NodeList getElementsByTagName(String tagname) {
        return new DeepNodeListImpl(this, tagname);
    }

    @Override // org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return CoreDOMImplementationImpl.getDOMImplementation();
    }

    public void setErrorChecking(boolean check) {
        this.errorChecking = check;
    }

    @Override // org.w3c.dom.Document
    public void setStrictErrorChecking(boolean check) {
        this.errorChecking = check;
    }

    public boolean getErrorChecking() {
        return this.errorChecking;
    }

    @Override // org.w3c.dom.Document
    public boolean getStrictErrorChecking() {
        return this.errorChecking;
    }

    @Override // org.w3c.dom.Document
    public String getInputEncoding() {
        return this.actualEncoding;
    }

    public void setInputEncoding(String value) {
        this.actualEncoding = value;
    }

    public void setXmlEncoding(String value) {
        this.encoding = value;
    }

    public void setEncoding(String value) {
        setXmlEncoding(value);
    }

    @Override // org.w3c.dom.Document
    public String getXmlEncoding() {
        return this.encoding;
    }

    public String getEncoding() {
        return getXmlEncoding();
    }

    @Override // org.w3c.dom.Document
    public void setXmlVersion(String value) throws MissingResourceException {
        if (value.equals("1.0") || value.equals(SerializerConstants.XMLVERSION11)) {
            if (!getXmlVersion().equals(value)) {
                this.xmlVersionChanged = true;
                isNormalized(false);
                this.version = value;
            }
            if (getXmlVersion().equals(SerializerConstants.XMLVERSION11)) {
                this.xml11Version = true;
                return;
            } else {
                this.xml11Version = false;
                return;
            }
        }
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
        throw new DOMException((short) 9, msg);
    }

    public void setVersion(String value) throws MissingResourceException {
        setXmlVersion(value);
    }

    @Override // org.w3c.dom.Document
    public String getXmlVersion() {
        return this.version == null ? "1.0" : this.version;
    }

    public String getVersion() {
        return getXmlVersion();
    }

    @Override // org.w3c.dom.Document
    public void setXmlStandalone(boolean value) throws DOMException {
        this.standalone = value;
    }

    public void setStandalone(boolean value) throws DOMException {
        setXmlStandalone(value);
    }

    @Override // org.w3c.dom.Document
    public boolean getXmlStandalone() {
        return this.standalone;
    }

    public boolean getStandalone() {
        return getXmlStandalone();
    }

    @Override // org.w3c.dom.Document
    public String getDocumentURI() {
        return this.fDocumentURI;
    }

    @Override // org.w3c.dom.Document
    public Node renameNode(Node n2, String namespaceURI, String name) throws DOMException, MissingResourceException {
        if (this.errorChecking && n2.getOwnerDocument() != this && n2 != this) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
            throw new DOMException((short) 4, msg);
        }
        switch (n2.getNodeType()) {
            case 1:
                ElementImpl el = (ElementImpl) n2;
                if (el instanceof ElementNSImpl) {
                    ((ElementNSImpl) el).rename(namespaceURI, name);
                    callUserDataHandlers(el, null, (short) 4);
                } else if (namespaceURI == null) {
                    if (this.errorChecking) {
                        int colon1 = name.indexOf(58);
                        if (colon1 != -1) {
                            String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                            throw new DOMException((short) 14, msg2);
                        }
                        if (!isXMLName(name, this.xml11Version)) {
                            String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
                            throw new DOMException((short) 5, msg3);
                        }
                    }
                    el.rename(name);
                    callUserDataHandlers(el, null, (short) 4);
                } else {
                    ElementNSImpl nel = new ElementNSImpl(this, namespaceURI, name);
                    copyEventListeners(el, nel);
                    Map<String, ParentNode.UserDataRecord> data = removeUserDataTable(el);
                    Node parent = el.getParentNode();
                    Node nextSib = el.getNextSibling();
                    if (parent != null) {
                        parent.removeChild(el);
                    }
                    Node firstChild = el.getFirstChild();
                    while (true) {
                        Node child = firstChild;
                        if (child != null) {
                            el.removeChild(child);
                            nel.appendChild(child);
                            firstChild = el.getFirstChild();
                        } else {
                            nel.moveSpecifiedAttributes(el);
                            setUserDataTable(nel, data);
                            callUserDataHandlers(el, nel, (short) 4);
                            if (parent != null) {
                                parent.insertBefore(nel, nextSib);
                            }
                            el = nel;
                        }
                    }
                }
                renamedElement((Element) n2, el);
                return el;
            case 2:
                AttrImpl at2 = (AttrImpl) n2;
                Element el2 = at2.getOwnerElement();
                if (el2 != null) {
                    el2.removeAttributeNode(at2);
                }
                if (n2 instanceof AttrNSImpl) {
                    ((AttrNSImpl) at2).rename(namespaceURI, name);
                    if (el2 != null) {
                        el2.setAttributeNodeNS(at2);
                    }
                    callUserDataHandlers(at2, null, (short) 4);
                } else if (namespaceURI == null) {
                    at2.rename(name);
                    if (el2 != null) {
                        el2.setAttributeNode(at2);
                    }
                    callUserDataHandlers(at2, null, (short) 4);
                } else {
                    AttrNSImpl nat = new AttrNSImpl(this, namespaceURI, name);
                    copyEventListeners(at2, nat);
                    Map<String, ParentNode.UserDataRecord> data2 = removeUserDataTable(at2);
                    Node firstChild2 = at2.getFirstChild();
                    while (true) {
                        Node child2 = firstChild2;
                        if (child2 != null) {
                            at2.removeChild(child2);
                            nat.appendChild(child2);
                            firstChild2 = at2.getFirstChild();
                        } else {
                            setUserDataTable(nat, data2);
                            callUserDataHandlers(at2, nat, (short) 4);
                            if (el2 != null) {
                                el2.setAttributeNode(nat);
                            }
                            at2 = nat;
                        }
                    }
                }
                renamedAttrNode((Attr) n2, at2);
                return at2;
            default:
                String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
                throw new DOMException((short) 9, msg4);
        }
    }

    @Override // org.w3c.dom.Document
    public void normalizeDocument() throws XNIException {
        if (isNormalized() && !isNormalizeDocRequired()) {
            return;
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (this.domNormalizer == null) {
            this.domNormalizer = new DOMNormalizer();
        }
        if (this.fConfiguration == null) {
            this.fConfiguration = new DOMConfigurationImpl();
        } else {
            this.fConfiguration.reset();
        }
        this.domNormalizer.normalizeDocument(this, this.fConfiguration);
        isNormalized(true);
        this.xmlVersionChanged = false;
    }

    @Override // org.w3c.dom.Document
    public DOMConfiguration getDomConfig() {
        if (this.fConfiguration == null) {
            this.fConfiguration = new DOMConfigurationImpl();
        }
        return this.fConfiguration;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getBaseURI() {
        if (this.fDocumentURI != null && this.fDocumentURI.length() != 0) {
            try {
                return new URI(this.fDocumentURI).toString();
            } catch (URI.MalformedURIException e2) {
                return null;
            }
        }
        return this.fDocumentURI;
    }

    @Override // org.w3c.dom.Document
    public void setDocumentURI(String documentURI) {
        this.fDocumentURI = documentURI;
    }

    public boolean getAsync() {
        return false;
    }

    public void setAsync(boolean async) throws MissingResourceException {
        if (async) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
            throw new DOMException((short) 9, msg);
        }
    }

    public void abort() {
    }

    public boolean load(String uri) {
        return false;
    }

    public boolean loadXML(String source) {
        return false;
    }

    public String saveXML(Node node) throws DOMException, MissingResourceException {
        if (this.errorChecking && node != null && this != node.getOwnerDocument()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
            throw new DOMException((short) 4, msg);
        }
        DOMImplementationLS domImplLS = (DOMImplementationLS) DOMImplementationImpl.getDOMImplementation();
        LSSerializer xmlWriter = domImplLS.createLSSerializer();
        if (node == null) {
            node = this;
        }
        return xmlWriter.writeToString(node);
    }

    void setMutationEvents(boolean set) {
    }

    boolean getMutationEvents() {
        return false;
    }

    public DocumentType createDocumentType(String qualifiedName, String publicID, String systemID) throws DOMException {
        return new DocumentTypeImpl(this, qualifiedName, publicID, systemID);
    }

    public Entity createEntity(String name) throws DOMException, MissingResourceException {
        if (this.errorChecking && !isXMLName(name, this.xml11Version)) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
            throw new DOMException((short) 5, msg);
        }
        return new EntityImpl(this, name);
    }

    public Notation createNotation(String name) throws DOMException, MissingResourceException {
        if (this.errorChecking && !isXMLName(name, this.xml11Version)) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
            throw new DOMException((short) 5, msg);
        }
        return new NotationImpl(this, name);
    }

    public ElementDefinitionImpl createElementDefinition(String name) throws DOMException, MissingResourceException {
        if (this.errorChecking && !isXMLName(name, this.xml11Version)) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
            throw new DOMException((short) 5, msg);
        }
        return new ElementDefinitionImpl(this, name);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    protected int getNodeNumber() {
        if (this.documentNumber == 0) {
            CoreDOMImplementationImpl cd = (CoreDOMImplementationImpl) CoreDOMImplementationImpl.getDOMImplementation();
            this.documentNumber = cd.assignDocumentNumber();
        }
        return this.documentNumber;
    }

    protected int getNodeNumber(Node node) {
        int num;
        if (this.nodeTable == null) {
            this.nodeTable = new HashMap();
            int i2 = this.nodeCounter - 1;
            this.nodeCounter = i2;
            num = i2;
            this.nodeTable.put(node, new Integer(num));
        } else {
            Integer n2 = this.nodeTable.get(node);
            if (n2 == null) {
                int i3 = this.nodeCounter - 1;
                this.nodeCounter = i3;
                num = i3;
                this.nodeTable.put(node, Integer.valueOf(num));
            } else {
                num = n2.intValue();
            }
        }
        return num;
    }

    @Override // org.w3c.dom.Document
    public Node importNode(Node source, boolean deep) throws DOMException {
        return importNode(source, deep, false, null);
    }

    private Node importNode(Node source, boolean deep, boolean cloningDoc, Map<Node, String> reversedIdentifiers) throws DOMException, MissingResourceException {
        Node newnode;
        Element newElement;
        String elementId;
        Map<String, ParentNode.UserDataRecord> userData = null;
        if (source instanceof NodeImpl) {
            userData = ((NodeImpl) source).getUserDataRecord();
        }
        int type = source.getNodeType();
        switch (type) {
            case 1:
                boolean domLevel20 = source.getOwnerDocument().getImplementation().hasFeature("XML", "2.0");
                if (!domLevel20 || source.getLocalName() == null) {
                    newElement = createElement(source.getNodeName());
                } else {
                    newElement = createElementNS(source.getNamespaceURI(), source.getNodeName());
                }
                NamedNodeMap sourceAttrs = source.getAttributes();
                if (sourceAttrs != null) {
                    int length = sourceAttrs.getLength();
                    for (int index = 0; index < length; index++) {
                        Attr attr = (Attr) sourceAttrs.item(index);
                        if (attr.getSpecified() || cloningDoc) {
                            Attr newAttr = (Attr) importNode(attr, true, cloningDoc, reversedIdentifiers);
                            if (!domLevel20 || attr.getLocalName() == null) {
                                newElement.setAttributeNode(newAttr);
                            } else {
                                newElement.setAttributeNodeNS(newAttr);
                            }
                        }
                    }
                }
                if (reversedIdentifiers != null && (elementId = reversedIdentifiers.get(source)) != null) {
                    if (this.identifiers == null) {
                        this.identifiers = new HashMap();
                    }
                    this.identifiers.put(elementId, newElement);
                }
                newnode = newElement;
                break;
            case 2:
                if (!source.getOwnerDocument().getImplementation().hasFeature("XML", "2.0") || source.getLocalName() == null) {
                    newnode = createAttribute(source.getNodeName());
                } else {
                    newnode = createAttributeNS(source.getNamespaceURI(), source.getNodeName());
                }
                if (source instanceof AttrImpl) {
                    AttrImpl attr2 = (AttrImpl) source;
                    if (attr2.hasStringValue()) {
                        AttrImpl newattr = (AttrImpl) newnode;
                        newattr.setValue(attr2.getValue());
                        deep = false;
                        break;
                    } else {
                        deep = true;
                        break;
                    }
                } else if (source.getFirstChild() == null) {
                    newnode.setNodeValue(source.getNodeValue());
                    deep = false;
                    break;
                } else {
                    deep = true;
                    break;
                }
                break;
            case 3:
                newnode = createTextNode(source.getNodeValue());
                break;
            case 4:
                newnode = createCDATASection(source.getNodeValue());
                break;
            case 5:
                newnode = createEntityReference(source.getNodeName());
                deep = false;
                break;
            case 6:
                Entity srcentity = (Entity) source;
                EntityImpl newentity = (EntityImpl) createEntity(source.getNodeName());
                newentity.setPublicId(srcentity.getPublicId());
                newentity.setSystemId(srcentity.getSystemId());
                newentity.setNotationName(srcentity.getNotationName());
                newentity.isReadOnly(false);
                newnode = newentity;
                break;
            case 7:
                newnode = createProcessingInstruction(source.getNodeName(), source.getNodeValue());
                break;
            case 8:
                newnode = createComment(source.getNodeValue());
                break;
            case 9:
            default:
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
                throw new DOMException((short) 9, msg);
            case 10:
                if (!cloningDoc) {
                    String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
                    throw new DOMException((short) 9, msg2);
                }
                DocumentType srcdoctype = (DocumentType) source;
                DocumentTypeImpl newdoctype = (DocumentTypeImpl) createDocumentType(srcdoctype.getNodeName(), srcdoctype.getPublicId(), srcdoctype.getSystemId());
                NamedNodeMap smap = srcdoctype.getEntities();
                NamedNodeMap tmap = newdoctype.getEntities();
                if (smap != null) {
                    for (int i2 = 0; i2 < smap.getLength(); i2++) {
                        tmap.setNamedItem(importNode(smap.item(i2), true, true, reversedIdentifiers));
                    }
                }
                NamedNodeMap smap2 = srcdoctype.getNotations();
                NamedNodeMap tmap2 = newdoctype.getNotations();
                if (smap2 != null) {
                    for (int i3 = 0; i3 < smap2.getLength(); i3++) {
                        tmap2.setNamedItem(importNode(smap2.item(i3), true, true, reversedIdentifiers));
                    }
                }
                newnode = newdoctype;
                break;
            case 11:
                newnode = createDocumentFragment();
                break;
            case 12:
                Notation srcnotation = (Notation) source;
                NotationImpl newnotation = (NotationImpl) createNotation(source.getNodeName());
                newnotation.setPublicId(srcnotation.getPublicId());
                newnotation.setSystemId(srcnotation.getSystemId());
                newnode = newnotation;
                break;
        }
        if (userData != null) {
            callUserDataHandlers(source, newnode, (short) 2, userData);
        }
        if (deep) {
            Node firstChild = source.getFirstChild();
            while (true) {
                Node srckid = firstChild;
                if (srckid != null) {
                    newnode.appendChild(importNode(srckid, true, cloningDoc, reversedIdentifiers));
                    firstChild = srckid.getNextSibling();
                }
            }
        }
        if (newnode.getNodeType() == 6) {
            ((NodeImpl) newnode).setReadOnly(true, true);
        }
        return newnode;
    }

    @Override // org.w3c.dom.Document
    public Node adoptNode(Node source) throws DOMException, MissingResourceException {
        Map<String, ParentNode.UserDataRecord> userData;
        DOMImplementation thisImpl;
        DOMImplementation otherImpl;
        try {
            NodeImpl node = (NodeImpl) source;
            if (source == null) {
                return null;
            }
            if (source.getOwnerDocument() != null && (thisImpl = getImplementation()) != (otherImpl = source.getOwnerDocument().getImplementation())) {
                if ((thisImpl instanceof DOMImplementationImpl) && (otherImpl instanceof DeferredDOMImplementationImpl)) {
                    undeferChildren(node);
                } else if (!(thisImpl instanceof DeferredDOMImplementationImpl) || !(otherImpl instanceof DOMImplementationImpl)) {
                    return null;
                }
            }
            switch (node.getNodeType()) {
                case 1:
                    userData = node.getUserDataRecord();
                    Node parent = node.getParentNode();
                    if (parent != null) {
                        parent.removeChild(source);
                    }
                    node.setOwnerDocument(this);
                    if (userData != null) {
                        setUserDataTable(node, userData);
                    }
                    ((ElementImpl) node).reconcileDefaultAttributes();
                    break;
                case 2:
                    AttrImpl attr = (AttrImpl) node;
                    if (attr.getOwnerElement() != null) {
                        attr.getOwnerElement().removeAttributeNode(attr);
                    }
                    attr.isSpecified(true);
                    userData = node.getUserDataRecord();
                    attr.setOwnerDocument(this);
                    if (userData != null) {
                        setUserDataTable(node, userData);
                        break;
                    }
                    break;
                case 3:
                case 4:
                case 7:
                case 8:
                case 11:
                default:
                    userData = node.getUserDataRecord();
                    Node parent2 = node.getParentNode();
                    if (parent2 != null) {
                        parent2.removeChild(source);
                    }
                    node.setOwnerDocument(this);
                    if (userData != null) {
                        setUserDataTable(node, userData);
                        break;
                    }
                    break;
                case 5:
                    userData = node.getUserDataRecord();
                    Node parent3 = node.getParentNode();
                    if (parent3 != null) {
                        parent3.removeChild(source);
                    }
                    while (true) {
                        Node child = node.getFirstChild();
                        if (child != null) {
                            node.removeChild(child);
                        } else {
                            node.setOwnerDocument(this);
                            if (userData != null) {
                                setUserDataTable(node, userData);
                            }
                            if (this.docType != null) {
                                NamedNodeMap entities = this.docType.getEntities();
                                Node entityNode = entities.getNamedItem(node.getNodeName());
                                if (entityNode != null) {
                                    Node firstChild = entityNode.getFirstChild();
                                    while (true) {
                                        Node child2 = firstChild;
                                        if (child2 == null) {
                                            break;
                                        } else {
                                            Node childClone = child2.cloneNode(true);
                                            node.appendChild(childClone);
                                            firstChild = child2.getNextSibling();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 6:
                case 12:
                    String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                    throw new DOMException((short) 7, msg);
                case 9:
                case 10:
                    String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
                    throw new DOMException((short) 9, msg2);
            }
            if (userData != null) {
                callUserDataHandlers(source, null, (short) 5, userData);
            }
            return node;
        } catch (ClassCastException e2) {
            return null;
        }
    }

    protected void undeferChildren(Node node) {
        while (null != node) {
            if (((NodeImpl) node).needsSyncData()) {
                ((NodeImpl) node).synchronizeData();
            }
            NamedNodeMap attributes = node.getAttributes();
            if (attributes != null) {
                int length = attributes.getLength();
                for (int i2 = 0; i2 < length; i2++) {
                    undeferChildren(attributes.item(i2));
                }
            }
            Node nextNode = node.getFirstChild();
            while (null == nextNode && !node.equals(node)) {
                nextNode = node.getNextSibling();
                if (null == nextNode) {
                    node = node.getParentNode();
                    if (null == node || node.equals(node)) {
                        nextNode = null;
                        break;
                    }
                }
            }
            node = nextNode;
        }
    }

    @Override // org.w3c.dom.Document
    public Element getElementById(String elementId) {
        return getIdentifier(elementId);
    }

    protected final void clearIdentifiers() {
        if (this.identifiers != null) {
            this.identifiers.clear();
        }
    }

    public void putIdentifier(String idName, Element element) {
        if (element == null) {
            removeIdentifier(idName);
            return;
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.identifiers == null) {
            this.identifiers = new HashMap();
        }
        this.identifiers.put(idName, element);
    }

    public Element getIdentifier(String idName) {
        Element elem;
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.identifiers != null && (elem = (Element) this.identifiers.get(idName)) != null) {
            Node parentNode = elem.getParentNode();
            while (true) {
                Node parent = parentNode;
                if (parent != null) {
                    if (parent == this) {
                        return elem;
                    }
                    parentNode = parent.getParentNode();
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public void removeIdentifier(String idName) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.identifiers == null) {
            return;
        }
        this.identifiers.remove(idName);
    }

    @Override // org.w3c.dom.Document
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return new ElementNSImpl(this, namespaceURI, qualifiedName);
    }

    public Element createElementNS(String namespaceURI, String qualifiedName, String localpart) throws DOMException {
        return new ElementNSImpl(this, namespaceURI, qualifiedName, localpart);
    }

    @Override // org.w3c.dom.Document
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return new AttrNSImpl(this, namespaceURI, qualifiedName);
    }

    public Attr createAttributeNS(String namespaceURI, String qualifiedName, String localpart) throws DOMException {
        return new AttrNSImpl(this, namespaceURI, qualifiedName, localpart);
    }

    @Override // org.w3c.dom.Document
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return new DeepNodeListImpl(this, namespaceURI, localName);
    }

    public Object clone() throws CloneNotSupportedException {
        CoreDocumentImpl newdoc = (CoreDocumentImpl) super.clone();
        newdoc.docType = null;
        newdoc.docElement = null;
        return newdoc;
    }

    public static final boolean isXMLName(String s2, boolean xml11Version) {
        if (s2 == null) {
            return false;
        }
        if (!xml11Version) {
            return XMLChar.isValidName(s2);
        }
        return XML11Char.isXML11ValidName(s2);
    }

    public static final boolean isValidQName(String prefix, String local, boolean xml11Version) {
        boolean validNCName;
        if (local == null) {
            return false;
        }
        if (!xml11Version) {
            validNCName = (prefix == null || XMLChar.isValidNCName(prefix)) && XMLChar.isValidNCName(local);
        } else {
            validNCName = (prefix == null || XML11Char.isXML11ValidNCName(prefix)) && XML11Char.isXML11ValidNCName(local);
        }
        return validNCName;
    }

    protected boolean isKidOK(Node parent, Node child) {
        return (this.allowGrammarAccess && parent.getNodeType() == 10) ? child.getNodeType() == 1 : 0 != (kidOK[parent.getNodeType()] & (1 << child.getNodeType()));
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    protected void changed() {
        this.changes++;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    protected int changes() {
        return this.changes;
    }

    NodeListCache getNodeListCache(ParentNode owner) {
        if (this.fFreeNLCache == null) {
            return new NodeListCache(owner);
        }
        NodeListCache c2 = this.fFreeNLCache;
        this.fFreeNLCache = this.fFreeNLCache.next;
        c2.fChild = null;
        c2.fChildIndex = -1;
        c2.fLength = -1;
        if (c2.fOwner != null) {
            c2.fOwner.fNodeListCache = null;
        }
        c2.fOwner = owner;
        return c2;
    }

    void freeNodeListCache(NodeListCache c2) {
        c2.next = this.fFreeNLCache;
        this.fFreeNLCache = c2;
    }

    public Object setUserData(Node n2, String key, Object data, UserDataHandler handler) {
        Map<String, ParentNode.UserDataRecord> t2;
        Map<String, ParentNode.UserDataRecord> t3;
        ParentNode.UserDataRecord r2;
        if (data == null) {
            if (this.nodeUserData != null && (t3 = this.nodeUserData.get(n2)) != null && (r2 = t3.remove(key)) != null) {
                return r2.fData;
            }
            return null;
        }
        if (this.nodeUserData == null) {
            this.nodeUserData = new HashMap();
            t2 = new HashMap();
            this.nodeUserData.put(n2, t2);
        } else {
            t2 = this.nodeUserData.get(n2);
            if (t2 == null) {
                t2 = new HashMap();
                this.nodeUserData.put(n2, t2);
            }
        }
        ParentNode.UserDataRecord r3 = t2.put(key, new ParentNode.UserDataRecord(data, handler));
        if (r3 != null) {
            return r3.fData;
        }
        return null;
    }

    public Object getUserData(Node n2, String key) {
        Map<String, ParentNode.UserDataRecord> t2;
        ParentNode.UserDataRecord r2;
        if (this.nodeUserData != null && (t2 = this.nodeUserData.get(n2)) != null && (r2 = t2.get(key)) != null) {
            return r2.fData;
        }
        return null;
    }

    protected Map<String, ParentNode.UserDataRecord> getUserDataRecord(Node n2) {
        Map<String, ParentNode.UserDataRecord> t2;
        if (this.nodeUserData == null || (t2 = this.nodeUserData.get(n2)) == null) {
            return null;
        }
        return t2;
    }

    Map<String, ParentNode.UserDataRecord> removeUserDataTable(Node n2) {
        if (this.nodeUserData == null) {
            return null;
        }
        return this.nodeUserData.get(n2);
    }

    void setUserDataTable(Node n2, Map<String, ParentNode.UserDataRecord> data) {
        if (this.nodeUserData == null) {
            this.nodeUserData = new HashMap();
        }
        if (data != null) {
            this.nodeUserData.put(n2, data);
        }
    }

    void callUserDataHandlers(Node n2, Node c2, short operation) {
        Map<String, ParentNode.UserDataRecord> t2;
        if (this.nodeUserData == null || !(n2 instanceof NodeImpl) || (t2 = ((NodeImpl) n2).getUserDataRecord()) == null || t2.isEmpty()) {
            return;
        }
        callUserDataHandlers(n2, c2, operation, t2);
    }

    void callUserDataHandlers(Node n2, Node c2, short operation, Map<String, ParentNode.UserDataRecord> userData) {
        if (userData == null || userData.isEmpty()) {
            return;
        }
        for (String key : userData.keySet()) {
            ParentNode.UserDataRecord r2 = userData.get(key);
            if (r2.fHandler != null) {
                r2.fHandler.handle(operation, key, r2.fData, n2, c2);
            }
        }
    }

    protected final void checkNamespaceWF(String qname, int colon1, int colon2) throws MissingResourceException {
        if (!this.errorChecking) {
            return;
        }
        if (colon1 == 0 || colon1 == qname.length() - 1 || colon2 != colon1) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
            throw new DOMException((short) 14, msg);
        }
    }

    protected final void checkDOMNSErr(String prefix, String namespace) throws MissingResourceException {
        if (this.errorChecking) {
            if (namespace == null) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                throw new DOMException((short) 14, msg);
            }
            if (prefix.equals("xml") && !namespace.equals(NamespaceContext.XML_URI)) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                throw new DOMException((short) 14, msg2);
            }
            if ((prefix.equals("xmlns") && !namespace.equals(NamespaceContext.XMLNS_URI)) || (!prefix.equals("xmlns") && namespace.equals(NamespaceContext.XMLNS_URI))) {
                String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                throw new DOMException((short) 14, msg3);
            }
        }
    }

    protected final void checkQName(String prefix, String local) throws MissingResourceException {
        boolean validNCName;
        if (!this.errorChecking) {
            return;
        }
        if (!this.xml11Version) {
            validNCName = (prefix == null || XMLChar.isValidNCName(prefix)) && XMLChar.isValidNCName(local);
        } else {
            validNCName = (prefix == null || XML11Char.isXML11ValidNCName(prefix)) && XML11Char.isXML11ValidNCName(local);
        }
        if (!validNCName) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
            throw new DOMException((short) 5, msg);
        }
    }

    boolean isXML11Version() {
        return this.xml11Version;
    }

    boolean isNormalizeDocRequired() {
        return true;
    }

    boolean isXMLVersionChanged() {
        return this.xmlVersionChanged;
    }

    protected void setUserData(NodeImpl n2, Object data) {
        setUserData(n2, "XERCES1DOMUSERDATA", data, null);
    }

    protected Object getUserData(NodeImpl n2) {
        return getUserData(n2, "XERCES1DOMUSERDATA");
    }

    protected void addEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture) {
    }

    protected void removeEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture) {
    }

    protected void copyEventListeners(NodeImpl src, NodeImpl tgt) {
    }

    protected boolean dispatchEvent(NodeImpl node, Event event) {
        return false;
    }

    void replacedText(NodeImpl node) {
    }

    void deletedText(NodeImpl node, int offset, int count) {
    }

    void insertedText(NodeImpl node, int offset, int count) {
    }

    void modifyingCharacterData(NodeImpl node, boolean replace) {
    }

    void modifiedCharacterData(NodeImpl node, String oldvalue, String value, boolean replace) {
    }

    void insertingNode(NodeImpl node, boolean replace) {
    }

    void insertedNode(NodeImpl node, NodeImpl newInternal, boolean replace) {
    }

    void removingNode(NodeImpl node, NodeImpl oldChild, boolean replace) {
    }

    void removedNode(NodeImpl node, boolean replace) {
    }

    void replacingNode(NodeImpl node) {
    }

    void replacedNode(NodeImpl node) {
    }

    void replacingData(NodeImpl node) {
    }

    void replacedCharacterData(NodeImpl node, String oldvalue, String value) {
    }

    void modifiedAttrValue(AttrImpl attr, String oldvalue) {
    }

    void setAttrNode(AttrImpl attr, AttrImpl previous) {
    }

    void removedAttrNode(AttrImpl attr, NodeImpl oldOwner, String name) {
    }

    void renamedAttrNode(Attr oldAt, Attr newAt) {
    }

    void renamedElement(Element oldEl, Element newEl) {
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Hashtable<Node, Hashtable<String, ParentNode.UserDataRecord>> nud = null;
        if (this.nodeUserData != null) {
            nud = new Hashtable<>();
            for (Map.Entry<Node, Map<String, ParentNode.UserDataRecord>> e2 : this.nodeUserData.entrySet()) {
                nud.put(e2.getKey(), new Hashtable<>(e2.getValue()));
            }
        }
        Hashtable<String, Node> ids = this.identifiers == null ? null : new Hashtable<>(this.identifiers);
        Hashtable<Node, Integer> nt = this.nodeTable == null ? null : new Hashtable<>(this.nodeTable);
        ObjectOutputStream.PutField pf = out.putFields();
        pf.put("docType", this.docType);
        pf.put("docElement", this.docElement);
        pf.put("fFreeNLCache", this.fFreeNLCache);
        pf.put("encoding", this.encoding);
        pf.put("actualEncoding", this.actualEncoding);
        pf.put("version", this.version);
        pf.put("standalone", this.standalone);
        pf.put("fDocumentURI", this.fDocumentURI);
        pf.put("userData", nud);
        pf.put("identifiers", ids);
        pf.put("changes", this.changes);
        pf.put("allowGrammarAccess", this.allowGrammarAccess);
        pf.put("errorChecking", this.errorChecking);
        pf.put("ancestorChecking", this.ancestorChecking);
        pf.put("xmlVersionChanged", this.xmlVersionChanged);
        pf.put("documentNumber", this.documentNumber);
        pf.put("nodeCounter", this.nodeCounter);
        pf.put("nodeTable", nt);
        pf.put("xml11Version", this.xml11Version);
        out.writeFields();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField gf = in.readFields();
        this.docType = (DocumentTypeImpl) gf.get("docType", (Object) null);
        this.docElement = (ElementImpl) gf.get("docElement", (Object) null);
        this.fFreeNLCache = (NodeListCache) gf.get("fFreeNLCache", (Object) null);
        this.encoding = (String) gf.get("encoding", (Object) null);
        this.actualEncoding = (String) gf.get("actualEncoding", (Object) null);
        this.version = (String) gf.get("version", (Object) null);
        this.standalone = gf.get("standalone", false);
        this.fDocumentURI = (String) gf.get("fDocumentURI", (Object) null);
        Hashtable<Node, Hashtable<String, ParentNode.UserDataRecord>> nud = (Hashtable) gf.get("userData", (Object) null);
        Hashtable<String, Node> ids = (Hashtable) gf.get("identifiers", (Object) null);
        this.changes = gf.get("changes", 0);
        this.allowGrammarAccess = gf.get("allowGrammarAccess", false);
        this.errorChecking = gf.get("errorChecking", true);
        this.ancestorChecking = gf.get("ancestorChecking", true);
        this.xmlVersionChanged = gf.get("xmlVersionChanged", false);
        this.documentNumber = gf.get("documentNumber", 0);
        this.nodeCounter = gf.get("nodeCounter", 0);
        Hashtable<Node, Integer> nt = (Hashtable) gf.get("nodeTable", (Object) null);
        this.xml11Version = gf.get("xml11Version", false);
        if (nud != null) {
            this.nodeUserData = new HashMap();
            for (Map.Entry<Node, Hashtable<String, ParentNode.UserDataRecord>> e2 : nud.entrySet()) {
                this.nodeUserData.put(e2.getKey(), new HashMap(e2.getValue()));
            }
        }
        if (ids != null) {
            this.identifiers = new HashMap(ids);
        }
        if (nt != null) {
            this.nodeTable = new HashMap(nt);
        }
    }
}
