package com.sun.org.apache.xml.internal.dtm.ref.dom2dtm;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
import com.sun.org.apache.xml.internal.dtm.ref.ExpandedNameTable;
import com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import com.sun.org.apache.xml.internal.utils.QName;
import com.sun.org.apache.xml.internal.utils.StringBufferPool;
import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
import com.sun.org.apache.xml.internal.utils.TreeWalker;
import com.sun.org.apache.xml.internal.utils.XMLCharacterRecognizer;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/dom2dtm/DOM2DTM.class */
public class DOM2DTM extends DTMDefaultBaseIterators {
    static final boolean JJK_DEBUG = false;
    static final boolean JJK_NEWCODE = true;
    static final String NAMESPACE_DECL_NS = "http://www.w3.org/XML/1998/namespace";
    private transient Node m_pos;
    private int m_last_parent;
    private int m_last_kid;
    private transient Node m_root;
    boolean m_processedFirstElement;
    private transient boolean m_nodesAreProcessed;
    protected Vector m_nodes;
    TreeWalker m_walker;

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/dom2dtm/DOM2DTM$CharacterNodeHandler.class */
    public interface CharacterNodeHandler {
        void characters(Node node) throws SAXException;
    }

    public DOM2DTM(DTMManager mgr, DOMSource domSource, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
        super(mgr, domSource, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
        this.m_last_parent = 0;
        this.m_last_kid = -1;
        this.m_processedFirstElement = false;
        this.m_nodes = new Vector();
        this.m_walker = new TreeWalker(null);
        Node node = domSource.getNode();
        this.m_root = node;
        this.m_pos = node;
        this.m_last_kid = -1;
        this.m_last_parent = -1;
        this.m_last_kid = addNode(this.m_root, this.m_last_parent, this.m_last_kid, -1);
        if (1 == this.m_root.getNodeType()) {
            NamedNodeMap attrs = this.m_root.getAttributes();
            int attrsize = attrs == null ? 0 : attrs.getLength();
            if (attrsize > 0) {
                int attrIndex = -1;
                for (int i2 = 0; i2 < attrsize; i2++) {
                    attrIndex = addNode(attrs.item(i2), 0, attrIndex, -1);
                    this.m_firstch.setElementAt(-1, attrIndex);
                }
                this.m_nextsib.setElementAt(-1, attrIndex);
            }
        }
        this.m_nodesAreProcessed = false;
    }

    protected int addNode(Node node, int parentIndex, int previousSibling, int forceNodeType) {
        int type;
        String localName;
        int expandedTypeID;
        int nodeIndex = this.m_nodes.size();
        if (this.m_dtmIdent.size() == (nodeIndex >>> 16)) {
            try {
                if (this.m_mgr == null) {
                    throw new ClassCastException();
                }
                DTMManagerDefault mgrD = (DTMManagerDefault) this.m_mgr;
                int id = mgrD.getFirstFreeDTMID();
                mgrD.addDTM(this, id, nodeIndex);
                this.m_dtmIdent.addElement(id << 16);
            } catch (ClassCastException e2) {
                error(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
            }
        }
        this.m_size++;
        if (-1 == forceNodeType) {
            type = node.getNodeType();
        } else {
            type = forceNodeType;
        }
        if (2 == type) {
            String name = node.getNodeName();
            if (name.startsWith("xmlns:") || name.equals("xmlns")) {
                type = 13;
            }
        }
        this.m_nodes.addElement(node);
        this.m_firstch.setElementAt(-2, nodeIndex);
        this.m_nextsib.setElementAt(-2, nodeIndex);
        this.m_prevsib.setElementAt(previousSibling, nodeIndex);
        this.m_parent.setElementAt(parentIndex, nodeIndex);
        if (-1 != parentIndex && type != 2 && type != 13 && -2 == this.m_firstch.elementAt(parentIndex)) {
            this.m_firstch.setElementAt(nodeIndex, parentIndex);
        }
        String nsURI = node.getNamespaceURI();
        if (type == 7) {
            localName = node.getNodeName();
        } else {
            localName = node.getLocalName();
        }
        String localName2 = localName;
        if ((type == 1 || type == 2) && null == localName2) {
            localName2 = node.getNodeName();
        }
        ExpandedNameTable exnt = this.m_expandedNameTable;
        if (node.getLocalName() != null || type == 1 || type == 2) {
        }
        if (null != localName2) {
            expandedTypeID = exnt.getExpandedTypeID(nsURI, localName2, type);
        } else {
            expandedTypeID = exnt.getExpandedTypeID(type);
        }
        int expandedNameID = expandedTypeID;
        this.m_exptype.setElementAt(expandedNameID, nodeIndex);
        indexNode(expandedNameID, nodeIndex);
        if (-1 != previousSibling) {
            this.m_nextsib.setElementAt(nodeIndex, previousSibling);
        }
        if (type == 13) {
            declareNamespaceInContext(parentIndex, nodeIndex);
        }
        return nodeIndex;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    public int getNumberOfNodes() {
        return this.m_nodes.size();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    protected boolean nextNode() {
        boolean shouldStripWhitespace;
        if (this.m_nodesAreProcessed) {
            return false;
        }
        Node pos = this.m_pos;
        Node next = null;
        int nexttype = -1;
        do {
            if (pos.hasChildNodes()) {
                next = pos.getFirstChild();
                if (next != null && 10 == next.getNodeType()) {
                    next = next.getNextSibling();
                }
                if (5 != pos.getNodeType()) {
                    this.m_last_parent = this.m_last_kid;
                    this.m_last_kid = -1;
                    if (null != this.m_wsfilter) {
                        short wsv = this.m_wsfilter.getShouldStripSpace(makeNodeHandle(this.m_last_parent), this);
                        if (3 == wsv) {
                            shouldStripWhitespace = getShouldStripWhitespace();
                        } else {
                            shouldStripWhitespace = 2 == wsv;
                        }
                        boolean shouldStrip = shouldStripWhitespace;
                        pushShouldStripWhitespace(shouldStrip);
                    }
                }
            } else {
                if (this.m_last_kid != -1 && this.m_firstch.elementAt(this.m_last_kid) == -2) {
                    this.m_firstch.setElementAt(-1, this.m_last_kid);
                }
                while (this.m_last_parent != -1) {
                    next = pos.getNextSibling();
                    if (next != null && 10 == next.getNodeType()) {
                        next = next.getNextSibling();
                    }
                    if (next != null) {
                        break;
                    }
                    pos = pos.getParentNode();
                    if (pos == null) {
                    }
                    if (pos == null || 5 != pos.getNodeType()) {
                        popShouldStripWhitespace();
                        if (this.m_last_kid == -1) {
                            this.m_firstch.setElementAt(-1, this.m_last_parent);
                        } else {
                            this.m_nextsib.setElementAt(-1, this.m_last_kid);
                        }
                        SuballocatedIntVector suballocatedIntVector = this.m_parent;
                        int i2 = this.m_last_parent;
                        this.m_last_kid = i2;
                        this.m_last_parent = suballocatedIntVector.elementAt(i2);
                    }
                }
                if (this.m_last_parent == -1) {
                    next = null;
                }
            }
            if (next != null) {
                nexttype = next.getNodeType();
            }
            if (5 == nexttype) {
                pos = next;
            }
        } while (5 == nexttype);
        if (next == null) {
            this.m_nextsib.setElementAt(-1, 0);
            this.m_nodesAreProcessed = true;
            this.m_pos = null;
            return false;
        }
        boolean suppressNode = false;
        Node lastTextNode = null;
        int nexttype2 = next.getNodeType();
        if (3 == nexttype2 || 4 == nexttype2) {
            suppressNode = null != this.m_wsfilter && getShouldStripWhitespace();
            Node nodeLogicalNextDOMTextNode = next;
            while (true) {
                Node n2 = nodeLogicalNextDOMTextNode;
                if (n2 == null) {
                    break;
                }
                lastTextNode = n2;
                if (3 == n2.getNodeType()) {
                    nexttype2 = 3;
                }
                suppressNode &= XMLCharacterRecognizer.isWhiteSpace(n2.getNodeValue());
                nodeLogicalNextDOMTextNode = logicalNextDOMTextNode(n2);
            }
        } else if (7 == nexttype2) {
            suppressNode = pos.getNodeName().toLowerCase().equals("xml");
        }
        if (!suppressNode) {
            int nextindex = addNode(next, this.m_last_parent, this.m_last_kid, nexttype2);
            this.m_last_kid = nextindex;
            if (1 == nexttype2) {
                int attrIndex = -1;
                NamedNodeMap attrs = next.getAttributes();
                int attrsize = attrs == null ? 0 : attrs.getLength();
                if (attrsize > 0) {
                    for (int i3 = 0; i3 < attrsize; i3++) {
                        attrIndex = addNode(attrs.item(i3), nextindex, attrIndex, -1);
                        this.m_firstch.setElementAt(-1, attrIndex);
                        if (!this.m_processedFirstElement && "xmlns:xml".equals(attrs.item(i3).getNodeName())) {
                            this.m_processedFirstElement = true;
                        }
                    }
                }
                if (!this.m_processedFirstElement) {
                    attrIndex = addNode(new DOM2DTMdefaultNamespaceDeclarationNode((Element) next, "xml", "http://www.w3.org/XML/1998/namespace", makeNodeHandle((attrIndex == -1 ? nextindex : attrIndex) + 1)), nextindex, attrIndex, -1);
                    this.m_firstch.setElementAt(-1, attrIndex);
                    this.m_processedFirstElement = true;
                }
                if (attrIndex != -1) {
                    this.m_nextsib.setElementAt(-1, attrIndex);
                }
            }
        }
        if (3 == nexttype2 || 4 == nexttype2) {
            next = lastTextNode;
        }
        this.m_pos = next;
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public Node getNode(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        return (Node) this.m_nodes.elementAt(identity);
    }

    protected Node lookupNode(int nodeIdentity) {
        return (Node) this.m_nodes.elementAt(nodeIdentity);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    protected int getNextNodeIdentity(int identity) {
        int identity2 = identity + 1;
        if (identity2 >= this.m_nodes.size() && !nextNode()) {
            identity2 = -1;
        }
        return identity2;
    }

    private int getHandleFromNode(Node node) {
        if (null != node) {
            int len = this.m_nodes.size();
            int i2 = 0;
            while (true) {
                if (i2 < len) {
                    if (this.m_nodes.elementAt(i2) != node) {
                        i2++;
                    } else {
                        return makeNodeHandle(i2);
                    }
                } else {
                    boolean isMore = nextNode();
                    len = this.m_nodes.size();
                    if (!isMore && i2 >= len) {
                        return -1;
                    }
                }
            }
        } else {
            return -1;
        }
    }

    public int getHandleOfNode(Node node) {
        if (null != node) {
            if (this.m_root == node || ((this.m_root.getNodeType() == 9 && this.m_root == node.getOwnerDocument()) || (this.m_root.getNodeType() != 9 && this.m_root.getOwnerDocument() == node.getOwnerDocument()))) {
                Node ownerElement = node;
                while (true) {
                    Node cursor = ownerElement;
                    if (cursor != null) {
                        if (cursor != this.m_root) {
                            if (cursor.getNodeType() != 2) {
                                ownerElement = cursor.getParentNode();
                            } else {
                                ownerElement = ((Attr) cursor).getOwnerElement();
                            }
                        } else {
                            return getHandleFromNode(node);
                        }
                    } else {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public int getAttributeNode(int nodeHandle, String namespaceURI, String name) {
        if (null == namespaceURI) {
            namespaceURI = "";
        }
        if (1 == getNodeType(nodeHandle)) {
            int identity = makeNodeIdentity(nodeHandle);
            while (true) {
                int nextNodeIdentity = getNextNodeIdentity(identity);
                identity = nextNodeIdentity;
                if (-1 != nextNodeIdentity) {
                    int type = _type(identity);
                    if (type == 2 || type == 13) {
                        Node node = lookupNode(identity);
                        String nodeuri = node.getNamespaceURI();
                        if (null == nodeuri) {
                            nodeuri = "";
                        }
                        String nodelocalname = node.getLocalName();
                        if (nodeuri.equals(namespaceURI) && name.equals(nodelocalname)) {
                            return makeNodeHandle(identity);
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            }
        } else {
            return -1;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public XMLString getStringValue(int nodeHandle) {
        int type = getNodeType(nodeHandle);
        Node node = getNode(nodeHandle);
        if (1 == type || 9 == type || 11 == type) {
            FastStringBuffer buf = StringBufferPool.get();
            try {
                getNodeData(node, buf);
                String s2 = buf.length() > 0 ? buf.toString() : "";
                return this.m_xstrf.newstr(s2);
            } finally {
                StringBufferPool.free(buf);
            }
        }
        if (3 == type || 4 == type) {
            FastStringBuffer buf2 = StringBufferPool.get();
            while (node != null) {
                buf2.append(node.getNodeValue());
                node = logicalNextDOMTextNode(node);
            }
            String s3 = buf2.length() > 0 ? buf2.toString() : "";
            StringBufferPool.free(buf2);
            return this.m_xstrf.newstr(s3);
        }
        return this.m_xstrf.newstr(node.getNodeValue());
    }

    public boolean isWhitespace(int nodeHandle) {
        int type = getNodeType(nodeHandle);
        Node node = getNode(nodeHandle);
        if (3 == type || 4 == type) {
            FastStringBuffer buf = StringBufferPool.get();
            while (node != null) {
                buf.append(node.getNodeValue());
                node = logicalNextDOMTextNode(node);
            }
            boolean b2 = buf.isWhitespace(0, buf.length());
            StringBufferPool.free(buf);
            return b2;
        }
        return false;
    }

    protected static void getNodeData(Node node, FastStringBuffer buf) {
        switch (node.getNodeType()) {
            case 1:
            case 9:
            case 11:
                Node firstChild = node.getFirstChild();
                while (true) {
                    Node child = firstChild;
                    if (null == child) {
                        break;
                    } else {
                        getNodeData(child, buf);
                        firstChild = child.getNextSibling();
                    }
                }
            case 2:
            case 3:
            case 4:
                buf.append(node.getNodeValue());
                break;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeName(int nodeHandle) {
        Node node = getNode(nodeHandle);
        return node.getNodeName();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeNameX(int nodeHandle) {
        String name;
        short type = getNodeType(nodeHandle);
        switch (type) {
            case 1:
            case 2:
            case 5:
            case 7:
                Node node = getNode(nodeHandle);
                name = node.getNodeName();
                break;
            case 3:
            case 4:
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            default:
                name = "";
                break;
            case 13:
                Node node2 = getNode(nodeHandle);
                name = node2.getNodeName();
                if (name.startsWith("xmlns:")) {
                    name = QName.getLocalPart(name);
                    break;
                } else if (name.equals("xmlns")) {
                    name = "";
                    break;
                }
                break;
        }
        return name;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getLocalName(int nodeHandle) {
        int id = makeNodeIdentity(nodeHandle);
        if (-1 == id) {
            return null;
        }
        Node newnode = (Node) this.m_nodes.elementAt(id);
        String newname = newnode.getLocalName();
        if (null == newname) {
            String qname = newnode.getNodeName();
            if ('#' == qname.charAt(0)) {
                newname = "";
            } else {
                int index = qname.indexOf(58);
                newname = index < 0 ? qname : qname.substring(index + 1);
            }
        }
        return newname;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getPrefix(int nodeHandle) {
        String prefix;
        short type = getNodeType(nodeHandle);
        switch (type) {
            case 1:
            case 2:
                Node node = getNode(nodeHandle);
                String qname = node.getNodeName();
                int index = qname.indexOf(58);
                prefix = index < 0 ? "" : qname.substring(0, index);
                break;
            case 13:
                Node node2 = getNode(nodeHandle);
                String qname2 = node2.getNodeName();
                int index2 = qname2.indexOf(58);
                prefix = index2 < 0 ? "" : qname2.substring(index2 + 1);
                break;
            default:
                prefix = "";
                break;
        }
        return prefix;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNamespaceURI(int nodeHandle) {
        int id = makeNodeIdentity(nodeHandle);
        if (id == -1) {
            return null;
        }
        Node node = (Node) this.m_nodes.elementAt(id);
        return node.getNamespaceURI();
    }

    private Node logicalNextDOMTextNode(Node n2) {
        Node n3;
        int ntype;
        Node p2 = n2.getNextSibling();
        if (p2 == null) {
            Node parentNode = n2.getParentNode();
            while (true) {
                Node n4 = parentNode;
                if (n4 == null || 5 != n4.getNodeType()) {
                    break;
                }
                p2 = n4.getNextSibling();
                if (p2 != null) {
                    break;
                }
                parentNode = n4.getParentNode();
            }
        }
        Node firstChild = p2;
        while (true) {
            n3 = firstChild;
            if (n3 == null || 5 != n3.getNodeType()) {
                break;
            }
            if (n3.hasChildNodes()) {
                firstChild = n3.getFirstChild();
            } else {
                firstChild = n3.getNextSibling();
            }
        }
        if (n3 != null && 3 != (ntype = n3.getNodeType()) && 4 != ntype) {
            n3 = null;
        }
        return n3;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeValue(int nodeHandle) {
        int type = -1 != _exptype(makeNodeIdentity(nodeHandle)) ? getNodeType(nodeHandle) : -1;
        if (3 != type && 4 != type) {
            return getNode(nodeHandle).getNodeValue();
        }
        Node node = getNode(nodeHandle);
        Node n2 = logicalNextDOMTextNode(node);
        if (n2 == null) {
            return node.getNodeValue();
        }
        FastStringBuffer buf = StringBufferPool.get();
        buf.append(node.getNodeValue());
        while (n2 != null) {
            buf.append(n2.getNodeValue());
            n2 = logicalNextDOMTextNode(n2);
        }
        String s2 = buf.length() > 0 ? buf.toString() : "";
        StringBufferPool.free(buf);
        return s2;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentTypeDeclarationSystemIdentifier() {
        Document doc;
        DocumentType dtd;
        if (this.m_root.getNodeType() == 9) {
            doc = (Document) this.m_root;
        } else {
            doc = this.m_root.getOwnerDocument();
        }
        if (null != doc && null != (dtd = doc.getDoctype())) {
            return dtd.getSystemId();
        }
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getDocumentTypeDeclarationPublicIdentifier() {
        Document doc;
        DocumentType dtd;
        if (this.m_root.getNodeType() == 9) {
            doc = (Document) this.m_root;
        } else {
            doc = this.m_root.getOwnerDocument();
        }
        if (null != doc && null != (dtd = doc.getDoctype())) {
            return dtd.getPublicId();
        }
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public int getElementById(String elementId) {
        Node elem;
        Document doc = this.m_root.getNodeType() == 9 ? (Document) this.m_root : this.m_root.getOwnerDocument();
        if (null != doc && null != (elem = doc.getElementById(elementId))) {
            int elemHandle = getHandleFromNode(elem);
            if (-1 == elemHandle) {
                int identity = this.m_nodes.size() - 1;
                while (true) {
                    int nextNodeIdentity = getNextNodeIdentity(identity);
                    identity = nextNodeIdentity;
                    if (-1 == nextNodeIdentity) {
                        break;
                    }
                    Node node = getNode(identity);
                    if (node == elem) {
                        elemHandle = getHandleFromNode(elem);
                        break;
                    }
                }
            }
            return elemHandle;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getUnparsedEntityURI(String name) {
        DocumentType doctype;
        String url = "";
        Document doc = this.m_root.getNodeType() == 9 ? (Document) this.m_root : this.m_root.getOwnerDocument();
        if (null != doc && null != (doctype = doc.getDoctype())) {
            NamedNodeMap entities = doctype.getEntities();
            if (null == entities) {
                return url;
            }
            Entity entity = (Entity) entities.getNamedItem(name);
            if (null == entity) {
                return url;
            }
            String notationName = entity.getNotationName();
            if (null != notationName) {
                url = entity.getSystemId();
                if (null == url) {
                    url = entity.getPublicId();
                }
            }
        }
        return url;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public boolean isAttributeSpecified(int attributeHandle) {
        int type = getNodeType(attributeHandle);
        if (2 == type) {
            Attr attr = (Attr) getNode(attributeHandle);
            return attr.getSpecified();
        }
        return false;
    }

    public void setIncrementalSAXSource(IncrementalSAXSource source) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public ContentHandler getContentHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public LexicalHandler getLexicalHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DeclHandler getDeclHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public boolean needsTwoThreads() {
        return false;
    }

    private static boolean isSpace(char ch) {
        return XMLCharacterRecognizer.isWhiteSpace(ch);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException, DOMException {
        if (normalize) {
            XMLString str = getStringValue(nodeHandle);
            str.fixWhiteSpace(true, true, false).dispatchCharactersEvents(ch);
            return;
        }
        int type = getNodeType(nodeHandle);
        Node node = getNode(nodeHandle);
        dispatchNodeData(node, ch, 0);
        if (3 != type && 4 != type) {
            return;
        }
        while (true) {
            Node nodeLogicalNextDOMTextNode = logicalNextDOMTextNode(node);
            node = nodeLogicalNextDOMTextNode;
            if (null != nodeLogicalNextDOMTextNode) {
                dispatchNodeData(node, ch, 0);
            } else {
                return;
            }
        }
    }

    protected static void dispatchNodeData(Node node, ContentHandler ch, int depth) throws SAXException, DOMException {
        switch (node.getNodeType()) {
            case 1:
            case 9:
            case 11:
                Node firstChild = node.getFirstChild();
                while (true) {
                    Node child = firstChild;
                    if (null != child) {
                        dispatchNodeData(child, ch, depth + 1);
                        firstChild = child.getNextSibling();
                    } else {
                        return;
                    }
                }
            case 2:
            case 3:
            case 4:
                break;
            case 5:
            case 6:
            case 10:
            default:
                return;
            case 7:
            case 8:
                if (0 != depth) {
                    return;
                }
                break;
        }
        String str = node.getNodeValue();
        if (ch instanceof CharacterNodeHandler) {
            ((CharacterNodeHandler) ch).characters(node);
        } else {
            ch.characters(str.toCharArray(), 0, str.length());
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public void dispatchToEvents(int nodeHandle, ContentHandler ch) throws SAXException {
        TreeWalker treeWalker = this.m_walker;
        ContentHandler prevCH = treeWalker.getContentHandler();
        if (null != prevCH) {
            treeWalker = new TreeWalker(null);
        }
        treeWalker.setContentHandler(ch);
        try {
            Node node = getNode(nodeHandle);
            treeWalker.traverseFragment(node);
            treeWalker.setContentHandler(null);
        } catch (Throwable th) {
            treeWalker.setContentHandler(null);
            throw th;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public void setProperty(String property, Object value) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public SourceLocator getSourceLocatorFor(int node) {
        return null;
    }
}
