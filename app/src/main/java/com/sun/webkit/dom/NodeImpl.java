package com.sun.webkit.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.webkit.Disposer;
import netscape.javascript.JSException;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/NodeImpl.class */
public class NodeImpl extends JSObject implements Node, EventTarget {
    private static SelfDisposer[] hashTable = new SelfDisposer[64];
    private static int hashCount;
    public static final int ELEMENT_NODE = 1;
    public static final int ATTRIBUTE_NODE = 2;
    public static final int TEXT_NODE = 3;
    public static final int CDATA_SECTION_NODE = 4;
    public static final int ENTITY_REFERENCE_NODE = 5;
    public static final int ENTITY_NODE = 6;
    public static final int PROCESSING_INSTRUCTION_NODE = 7;
    public static final int COMMENT_NODE = 8;
    public static final int DOCUMENT_NODE = 9;
    public static final int DOCUMENT_TYPE_NODE = 10;
    public static final int DOCUMENT_FRAGMENT_NODE = 11;
    public static final int NOTATION_NODE = 12;
    public static final int DOCUMENT_POSITION_DISCONNECTED = 1;
    public static final int DOCUMENT_POSITION_PRECEDING = 2;
    public static final int DOCUMENT_POSITION_FOLLOWING = 4;
    public static final int DOCUMENT_POSITION_CONTAINS = 8;
    public static final int DOCUMENT_POSITION_CONTAINED_BY = 16;
    public static final int DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native String getNodeNameImpl(long j2);

    static native String getNodeValueImpl(long j2);

    static native void setNodeValueImpl(long j2, String str);

    static native short getNodeTypeImpl(long j2);

    static native long getParentNodeImpl(long j2);

    static native long getChildNodesImpl(long j2);

    static native long getFirstChildImpl(long j2);

    static native long getLastChildImpl(long j2);

    static native long getPreviousSiblingImpl(long j2);

    static native long getNextSiblingImpl(long j2);

    static native long getOwnerDocumentImpl(long j2);

    static native String getNamespaceURIImpl(long j2);

    static native String getPrefixImpl(long j2);

    static native void setPrefixImpl(long j2, String str);

    static native String getLocalNameImpl(long j2);

    static native long getAttributesImpl(long j2);

    static native String getBaseURIImpl(long j2);

    static native String getTextContentImpl(long j2);

    static native void setTextContentImpl(long j2, String str);

    static native long getParentElementImpl(long j2);

    static native long insertBeforeImpl(long j2, long j3, long j4);

    static native long replaceChildImpl(long j2, long j3, long j4);

    static native long removeChildImpl(long j2, long j3);

    static native long appendChildImpl(long j2, long j3);

    static native boolean hasChildNodesImpl(long j2);

    static native long cloneNodeImpl(long j2, boolean z2);

    static native void normalizeImpl(long j2);

    static native boolean isSupportedImpl(long j2, String str, String str2);

    static native boolean hasAttributesImpl(long j2);

    static native boolean isSameNodeImpl(long j2, long j3);

    static native boolean isEqualNodeImpl(long j2, long j3);

    static native String lookupPrefixImpl(long j2, String str);

    static native boolean isDefaultNamespaceImpl(long j2, String str);

    static native String lookupNamespaceURIImpl(long j2, String str);

    static native short compareDocumentPositionImpl(long j2, long j3);

    static native boolean containsImpl(long j2, long j3);

    static native void addEventListenerImpl(long j2, String str, long j3, boolean z2);

    static native void removeEventListenerImpl(long j2, String str, long j3, boolean z2);

    static native boolean dispatchEventImpl(long j2, long j3);

    @Override // com.sun.webkit.dom.JSObject
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.sun.webkit.dom.JSObject
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.sun.webkit.dom.JSObject
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ Object call(String str, Object[] objArr) throws JSException {
        return super.call(str, objArr);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ void setSlot(int i2, Object obj) throws JSException {
        super.setSlot(i2, obj);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ Object getSlot(int i2) throws JSException {
        return super.getSlot(i2);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ void removeMember(String str) throws JSException {
        super.removeMember(str);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ void setMember(String str, Object obj) throws JSException {
        super.setMember(str, obj);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ Object getMember(String str) {
        return super.getMember(str);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ Object eval(String str) throws JSException {
        return super.eval(str);
    }

    static /* synthetic */ int access$310() {
        int i2 = hashCount;
        hashCount = i2 - 1;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int hashPeer(long peer) {
        return ((int) ((peer ^ (-1)) ^ (peer >> 7))) & (hashTable.length - 1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Node getCachedImpl(long peer) {
        if (peer == 0) {
            return null;
        }
        int hash = hashPeer(peer);
        SelfDisposer head = hashTable[hash];
        SelfDisposer prev = null;
        SelfDisposer selfDisposer = head;
        while (true) {
            SelfDisposer disposer = selfDisposer;
            if (disposer == null) {
                break;
            }
            SelfDisposer next = disposer.next;
            if (disposer.peer == peer) {
                NodeImpl node = (NodeImpl) disposer.get();
                if (node != null) {
                    dispose(peer);
                    return node;
                }
                if (prev != null) {
                    prev.next = next;
                } else {
                    hashTable[hash] = next;
                }
            } else {
                prev = disposer;
                selfDisposer = next;
            }
        }
        NodeImpl node2 = (NodeImpl) createInterface(peer);
        SelfDisposer disposer2 = new SelfDisposer(node2, peer);
        Disposer.addRecord(disposer2);
        disposer2.next = head;
        hashTable[hash] = disposer2;
        if (3 * hashCount >= 2 * hashTable.length) {
            rehash();
        }
        hashCount++;
        return node2;
    }

    static int test_getHashCount() {
        return hashCount;
    }

    private static void rehash() {
        SelfDisposer[] oldTable = hashTable;
        int oldLength = oldTable.length;
        SelfDisposer[] newTable = new SelfDisposer[2 * oldLength];
        hashTable = newTable;
        int i2 = oldLength;
        while (true) {
            i2--;
            if (i2 >= 0) {
                SelfDisposer selfDisposer = oldTable[i2];
                while (true) {
                    SelfDisposer disposer = selfDisposer;
                    if (disposer != null) {
                        SelfDisposer next = disposer.next;
                        int hash = hashPeer(disposer.peer);
                        disposer.next = newTable[hash];
                        newTable[hash] = disposer;
                        selfDisposer = next;
                    }
                }
            } else {
                return;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/NodeImpl$SelfDisposer.class */
    private static final class SelfDisposer extends Disposer.WeakDisposerRecord {
        private final long peer;
        SelfDisposer next;

        SelfDisposer(Object referent, long _peer) {
            super(referent);
            this.peer = _peer;
        }

        @Override // com.sun.webkit.Disposer.WeakDisposerRecord, com.sun.webkit.DisposerRecord
        public void dispose() {
            int hash = NodeImpl.hashPeer(this.peer);
            SelfDisposer head = NodeImpl.hashTable[hash];
            SelfDisposer prev = null;
            SelfDisposer selfDisposer = head;
            while (true) {
                SelfDisposer disposer = selfDisposer;
                if (disposer == null) {
                    break;
                }
                SelfDisposer next = disposer.next;
                if (disposer.peer == this.peer) {
                    disposer.clear();
                    if (prev == null) {
                        NodeImpl.hashTable[hash] = next;
                    } else {
                        prev.next = next;
                    }
                    NodeImpl.access$310();
                } else {
                    prev = disposer;
                    selfDisposer = next;
                }
            }
            NodeImpl.dispose(this.peer);
        }
    }

    NodeImpl(long peer) {
        super(peer, 1);
    }

    static Node createInterface(long peer) {
        if (peer == 0) {
            return null;
        }
        switch (getNodeTypeImpl(peer)) {
            case 1:
                if (!ElementImpl.isHTMLElementImpl(peer)) {
                    return new ElementImpl(peer);
                }
                String tagName = ElementImpl.getTagNameImpl(peer).toUpperCase();
                return "A".equals(tagName) ? new HTMLAnchorElementImpl(peer) : "APPLET".equals(tagName) ? new HTMLAppletElementImpl(peer) : "AREA".equals(tagName) ? new HTMLAreaElementImpl(peer) : "BASE".equals(tagName) ? new HTMLBaseElementImpl(peer) : "BASEFONT".equals(tagName) ? new HTMLBaseFontElementImpl(peer) : "BODY".equals(tagName) ? new HTMLBodyElementImpl(peer) : "BR".equals(tagName) ? new HTMLBRElementImpl(peer) : "BUTTON".equals(tagName) ? new HTMLButtonElementImpl(peer) : "DIR".equals(tagName) ? new HTMLDirectoryElementImpl(peer) : "DIV".equals(tagName) ? new HTMLDivElementImpl(peer) : "DL".equals(tagName) ? new HTMLDListElementImpl(peer) : "FIELDSET".equals(tagName) ? new HTMLFieldSetElementImpl(peer) : "FONT".equals(tagName) ? new HTMLFontElementImpl(peer) : "FORM".equals(tagName) ? new HTMLFormElementImpl(peer) : "FRAME".equals(tagName) ? new HTMLFrameElementImpl(peer) : "FRAMESET".equals(tagName) ? new HTMLFrameSetElementImpl(peer) : "HEAD".equals(tagName) ? new HTMLHeadElementImpl(peer) : (tagName.length() != 2 || tagName.charAt(0) != 'H' || tagName.charAt(1) < '1' || tagName.charAt(1) > '6') ? "HR".equals(tagName) ? new HTMLHRElementImpl(peer) : "IFRAME".equals(tagName) ? new HTMLIFrameElementImpl(peer) : "IMG".equals(tagName) ? new HTMLImageElementImpl(peer) : "INPUT".equals(tagName) ? new HTMLInputElementImpl(peer) : "LABEL".equals(tagName) ? new HTMLLabelElementImpl(peer) : "LEGEND".equals(tagName) ? new HTMLLegendElementImpl(peer) : "LI".equals(tagName) ? new HTMLLIElementImpl(peer) : "LINK".equals(tagName) ? new HTMLLinkElementImpl(peer) : "MAP".equals(tagName) ? new HTMLMapElementImpl(peer) : "MENU".equals(tagName) ? new HTMLMenuElementImpl(peer) : "META".equals(tagName) ? new HTMLMetaElementImpl(peer) : ("INS".equals(tagName) || "DEL".equals(tagName)) ? new HTMLModElementImpl(peer) : "OBJECT".equals(tagName) ? new HTMLObjectElementImpl(peer) : "OL".equals(tagName) ? new HTMLOListElementImpl(peer) : "OPTGROUP".equals(tagName) ? new HTMLOptGroupElementImpl(peer) : "OPTION".equals(tagName) ? new HTMLOptionElementImpl(peer) : Constants._TAG_P.equals(tagName) ? new HTMLParagraphElementImpl(peer) : "PARAM".equals(tagName) ? new HTMLParamElementImpl(peer) : "PRE".equals(tagName) ? new HTMLPreElementImpl(peer) : "Q".equals(tagName) ? new HTMLQuoteElementImpl(peer) : "SCRIPT".equals(tagName) ? new HTMLScriptElementImpl(peer) : "SELECT".equals(tagName) ? new HTMLSelectElementImpl(peer) : "STYLE".equals(tagName) ? new HTMLStyleElementImpl(peer) : "CAPTION".equals(tagName) ? new HTMLTableCaptionElementImpl(peer) : PdfOps.TD_TOKEN.equals(tagName) ? new HTMLTableCellElementImpl(peer) : "COL".equals(tagName) ? new HTMLTableColElementImpl(peer) : "TABLE".equals(tagName) ? new HTMLTableElementImpl(peer) : "TR".equals(tagName) ? new HTMLTableRowElementImpl(peer) : ("THEAD".equals(tagName) || "TFOOT".equals(tagName) || "TBODY".equals(tagName)) ? new HTMLTableSectionElementImpl(peer) : "TEXTAREA".equals(tagName) ? new HTMLTextAreaElementImpl(peer) : "TITLE".equals(tagName) ? new HTMLTitleElementImpl(peer) : "UL".equals(tagName) ? new HTMLUListElementImpl(peer) : new HTMLElementImpl(peer) : new HTMLHeadingElementImpl(peer);
            case 2:
                return new AttrImpl(peer);
            case 3:
                return new TextImpl(peer);
            case 4:
                return new CDATASectionImpl(peer);
            case 5:
                return new EntityReferenceImpl(peer);
            case 6:
                return new EntityImpl(peer);
            case 7:
                return new ProcessingInstructionImpl(peer);
            case 8:
                return new CommentImpl(peer);
            case 9:
                if (DocumentImpl.isHTMLDocumentImpl(peer)) {
                    return new HTMLDocumentImpl(peer);
                }
                return new DocumentImpl(peer);
            case 10:
                return new DocumentTypeImpl(peer);
            case 11:
                return new DocumentFragmentImpl(peer);
            default:
                return new NodeImpl(peer);
        }
    }

    static Node create(long peer) {
        return getCachedImpl(peer);
    }

    static long getPeer(Node arg) {
        if (arg == null) {
            return 0L;
        }
        return ((NodeImpl) arg).getPeer();
    }

    static Node getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.Node
    public String getNodeName() {
        return getNodeNameImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public String getNodeValue() {
        return getNodeValueImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public void setNodeValue(String value) throws DOMException {
        setNodeValueImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.Node
    public short getNodeType() {
        return getNodeTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public Node getParentNode() {
        return getImpl(getParentNodeImpl(getPeer()));
    }

    @Override // org.w3c.dom.Node
    public NodeList getChildNodes() {
        return NodeListImpl.getImpl(getChildNodesImpl(getPeer()));
    }

    @Override // org.w3c.dom.Node
    public Node getFirstChild() {
        return getImpl(getFirstChildImpl(getPeer()));
    }

    @Override // org.w3c.dom.Node
    public Node getLastChild() {
        return getImpl(getLastChildImpl(getPeer()));
    }

    @Override // org.w3c.dom.Node
    public Node getPreviousSibling() {
        return getImpl(getPreviousSiblingImpl(getPeer()));
    }

    @Override // org.w3c.dom.Node
    public Node getNextSibling() {
        return getImpl(getNextSiblingImpl(getPeer()));
    }

    @Override // org.w3c.dom.Node
    public Document getOwnerDocument() {
        return DocumentImpl.getImpl(getOwnerDocumentImpl(getPeer()));
    }

    @Override // org.w3c.dom.Node
    public String getNamespaceURI() {
        return getNamespaceURIImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public String getPrefix() {
        return getPrefixImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public void setPrefix(String value) throws DOMException {
        setPrefixImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.Node
    public String getLocalName() {
        return getLocalNameImpl(getPeer());
    }

    public NamedNodeMap getAttributes() {
        return NamedNodeMapImpl.getImpl(getAttributesImpl(getPeer()));
    }

    @Override // org.w3c.dom.Node
    public String getBaseURI() {
        return getBaseURIImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public String getTextContent() {
        return getTextContentImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public void setTextContent(String value) throws DOMException {
        setTextContentImpl(getPeer(), value);
    }

    public Element getParentElement() {
        return ElementImpl.getImpl(getParentElementImpl(getPeer()));
    }

    @Override // org.w3c.dom.Node
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return getImpl(insertBeforeImpl(getPeer(), getPeer(newChild), getPeer(refChild)));
    }

    @Override // org.w3c.dom.Node
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return getImpl(replaceChildImpl(getPeer(), getPeer(newChild), getPeer(oldChild)));
    }

    @Override // org.w3c.dom.Node
    public Node removeChild(Node oldChild) throws DOMException {
        return getImpl(removeChildImpl(getPeer(), getPeer(oldChild)));
    }

    @Override // org.w3c.dom.Node
    public Node appendChild(Node newChild) throws DOMException {
        return getImpl(appendChildImpl(getPeer(), getPeer(newChild)));
    }

    @Override // org.w3c.dom.Node
    public boolean hasChildNodes() {
        return hasChildNodesImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public Node cloneNode(boolean deep) throws DOMException {
        return getImpl(cloneNodeImpl(getPeer(), deep));
    }

    @Override // org.w3c.dom.Node
    public void normalize() {
        normalizeImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public boolean isSupported(String feature, String version) {
        return isSupportedImpl(getPeer(), feature, version);
    }

    public boolean hasAttributes() {
        return hasAttributesImpl(getPeer());
    }

    @Override // org.w3c.dom.Node
    public boolean isSameNode(Node other) {
        return isSameNodeImpl(getPeer(), getPeer(other));
    }

    @Override // org.w3c.dom.Node
    public boolean isEqualNode(Node other) {
        return isEqualNodeImpl(getPeer(), getPeer(other));
    }

    @Override // org.w3c.dom.Node
    public String lookupPrefix(String namespaceURI) {
        return lookupPrefixImpl(getPeer(), namespaceURI);
    }

    @Override // org.w3c.dom.Node
    public boolean isDefaultNamespace(String namespaceURI) {
        return isDefaultNamespaceImpl(getPeer(), namespaceURI);
    }

    @Override // org.w3c.dom.Node
    public String lookupNamespaceURI(String prefix) {
        return lookupNamespaceURIImpl(getPeer(), prefix);
    }

    @Override // org.w3c.dom.Node
    public short compareDocumentPosition(Node other) {
        return compareDocumentPositionImpl(getPeer(), getPeer(other));
    }

    public boolean contains(Node other) {
        return containsImpl(getPeer(), getPeer(other));
    }

    @Override // org.w3c.dom.events.EventTarget
    public void addEventListener(String type, EventListener listener, boolean useCapture) {
        addEventListenerImpl(getPeer(), type, EventListenerImpl.getPeer(listener), useCapture);
    }

    @Override // org.w3c.dom.events.EventTarget
    public void removeEventListener(String type, EventListener listener, boolean useCapture) {
        removeEventListenerImpl(getPeer(), type, EventListenerImpl.getPeer(listener), useCapture);
    }

    @Override // org.w3c.dom.events.EventTarget
    public boolean dispatchEvent(Event event) throws DOMException {
        return dispatchEventImpl(getPeer(), EventImpl.getPeer(event));
    }

    @Override // org.w3c.dom.Node
    public Object getUserData(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Node
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Node
    public Object getFeature(String feature, String version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
