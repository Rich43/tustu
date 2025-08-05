package com.sun.xml.internal.ws.streaming;

import com.sun.istack.internal.FinalArrayList;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.XMLStreamException2;
import com.sun.xml.internal.ws.util.xml.DummyLocation;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

/* loaded from: rt.jar:com/sun/xml/internal/ws/streaming/DOMStreamReader.class */
public class DOMStreamReader implements XMLStreamReader, NamespaceContext {
    protected Node _current;
    private Node _start;
    private NamedNodeMap _namedNodeMap;
    protected String wholeText;
    private final FinalArrayList<Attr> _currentAttributes;
    protected Scope[] scopes;
    protected int depth;
    protected int _state;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/streaming/DOMStreamReader$Scope.class */
    protected static final class Scope {
        final Scope parent;
        final FinalArrayList<Attr> currentNamespaces = new FinalArrayList<>();
        final FinalArrayList<String> additionalNamespaces = new FinalArrayList<>();

        Scope(Scope parent) {
            this.parent = parent;
        }

        void reset() {
            this.currentNamespaces.clear();
            this.additionalNamespaces.clear();
        }

        int getNamespaceCount() {
            return this.currentNamespaces.size() + (this.additionalNamespaces.size() / 2);
        }

        String getNamespacePrefix(int index) {
            int sz = this.currentNamespaces.size();
            if (index < sz) {
                Attr attr = this.currentNamespaces.get(index);
                String result = attr.getLocalName();
                if (result == null) {
                    result = QName.valueOf(attr.getNodeName()).getLocalPart();
                }
                if (result.equals("xmlns")) {
                    return null;
                }
                return result;
            }
            return this.additionalNamespaces.get((index - sz) * 2);
        }

        String getNamespaceURI(int index) {
            int sz = this.currentNamespaces.size();
            if (index < sz) {
                return this.currentNamespaces.get(index).getValue();
            }
            return this.additionalNamespaces.get(((index - sz) * 2) + 1);
        }

        String getPrefix(String nsUri) {
            Scope scope = this;
            while (true) {
                Scope sp = scope;
                if (sp != null) {
                    for (int i2 = sp.currentNamespaces.size() - 1; i2 >= 0; i2--) {
                        String result = DOMStreamReader.getPrefixForAttr(sp.currentNamespaces.get(i2), nsUri);
                        if (result != null) {
                            return result;
                        }
                    }
                    for (int i3 = sp.additionalNamespaces.size() - 2; i3 >= 0; i3 -= 2) {
                        if (sp.additionalNamespaces.get(i3 + 1).equals(nsUri)) {
                            return sp.additionalNamespaces.get(i3);
                        }
                    }
                    scope = sp.parent;
                } else {
                    return null;
                }
            }
        }

        String getNamespaceURI(@NotNull String prefix) {
            String nsDeclName = prefix.length() == 0 ? "xmlns" : "xmlns:" + prefix;
            Scope scope = this;
            while (true) {
                Scope sp = scope;
                if (sp != null) {
                    for (int i2 = sp.currentNamespaces.size() - 1; i2 >= 0; i2--) {
                        Attr a2 = sp.currentNamespaces.get(i2);
                        if (a2.getNodeName().equals(nsDeclName)) {
                            return a2.getValue();
                        }
                    }
                    for (int i3 = sp.additionalNamespaces.size() - 2; i3 >= 0; i3 -= 2) {
                        if (sp.additionalNamespaces.get(i3).equals(prefix)) {
                            return sp.additionalNamespaces.get(i3 + 1);
                        }
                    }
                    scope = sp.parent;
                } else {
                    return null;
                }
            }
        }
    }

    public DOMStreamReader() {
        this._currentAttributes = new FinalArrayList<>();
        this.scopes = new Scope[8];
        this.depth = 0;
    }

    public DOMStreamReader(Node node) {
        this._currentAttributes = new FinalArrayList<>();
        this.scopes = new Scope[8];
        this.depth = 0;
        setCurrentNode(node);
    }

    public void setCurrentNode(Node node) {
        this.scopes[0] = new Scope(null);
        this.depth = 0;
        this._current = node;
        this._start = node;
        this._state = 7;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void close() throws XMLStreamException {
    }

    protected void splitAttributes() {
        this._currentAttributes.clear();
        Scope scope = allocateScope();
        this._namedNodeMap = this._current.getAttributes();
        if (this._namedNodeMap != null) {
            int n2 = this._namedNodeMap.getLength();
            for (int i2 = 0; i2 < n2; i2++) {
                Attr attr = (Attr) this._namedNodeMap.item(i2);
                String attrName = attr.getNodeName();
                if (attrName.startsWith("xmlns:") || attrName.equals("xmlns")) {
                    scope.currentNamespaces.add(attr);
                } else {
                    this._currentAttributes.add(attr);
                }
            }
        }
        ensureNs(this._current);
        for (int i3 = this._currentAttributes.size() - 1; i3 >= 0; i3--) {
            Attr a2 = this._currentAttributes.get(i3);
            if (fixNull(a2.getNamespaceURI()).length() > 0) {
                ensureNs(a2);
            }
        }
    }

    private void ensureNs(Node n2) {
        String prefix = fixNull(n2.getPrefix());
        String uri = fixNull(n2.getNamespaceURI());
        Scope scope = this.scopes[this.depth];
        String currentUri = scope.getNamespaceURI(prefix);
        if (prefix.length() == 0) {
            if (fixNull(currentUri).equals(uri)) {
                return;
            }
        } else if (currentUri != null && currentUri.equals(uri)) {
            return;
        }
        if (prefix.equals("xml") || prefix.equals("xmlns")) {
            return;
        }
        scope.additionalNamespaces.add(prefix);
        scope.additionalNamespaces.add(uri);
    }

    private Scope allocateScope() {
        int length = this.scopes.length;
        int i2 = this.depth + 1;
        this.depth = i2;
        if (length == i2) {
            Scope[] newBuf = new Scope[this.scopes.length * 2];
            System.arraycopy(this.scopes, 0, newBuf, 0, this.scopes.length);
            this.scopes = newBuf;
        }
        Scope scope = this.scopes[this.depth];
        if (scope == null) {
            Scope[] scopeArr = this.scopes;
            int i3 = this.depth;
            Scope scope2 = new Scope(this.scopes[this.depth - 1]);
            scopeArr[i3] = scope2;
            scope = scope2;
        } else {
            scope.reset();
        }
        return scope;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getAttributeCount() {
        if (this._state == 1) {
            return this._currentAttributes.size();
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeCount() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeLocalName(int index) {
        if (this._state == 1) {
            String localName = this._currentAttributes.get(index).getLocalName();
            return localName != null ? localName : QName.valueOf(this._currentAttributes.get(index).getNodeName()).getLocalPart();
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeLocalName() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getAttributeName(int index) {
        if (this._state == 1) {
            Node attr = this._currentAttributes.get(index);
            String localName = attr.getLocalName();
            if (localName != null) {
                String prefix = attr.getPrefix();
                String uri = attr.getNamespaceURI();
                return new QName(fixNull(uri), localName, fixNull(prefix));
            }
            return QName.valueOf(attr.getNodeName());
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeName() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeNamespace(int index) {
        if (this._state == 1) {
            String uri = this._currentAttributes.get(index).getNamespaceURI();
            return fixNull(uri);
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeNamespace() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributePrefix(int index) {
        if (this._state == 1) {
            String prefix = this._currentAttributes.get(index).getPrefix();
            return fixNull(prefix);
        }
        throw new IllegalStateException("DOMStreamReader: getAttributePrefix() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeType(int index) {
        if (this._state == 1) {
            return "CDATA";
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeType() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(int index) {
        if (this._state == 1) {
            return this._currentAttributes.get(index).getNodeValue();
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeValue() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(String namespaceURI, String localName) {
        Node attr;
        if (this._state == 1) {
            if (this._namedNodeMap == null || (attr = this._namedNodeMap.getNamedItemNS(namespaceURI, localName)) == null) {
                return null;
            }
            return attr.getNodeValue();
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeValue() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getCharacterEncodingScheme() {
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getElementText() throws XMLStreamException {
        throw new RuntimeException("DOMStreamReader: getElementText() not implemented");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getEncoding() {
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getEventType() {
        return this._state;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getLocalName() {
        if (this._state == 1 || this._state == 2) {
            String localName = this._current.getLocalName();
            return localName != null ? localName : QName.valueOf(this._current.getNodeName()).getLocalPart();
        }
        if (this._state == 9) {
            return this._current.getNodeName();
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeValue() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Location getLocation() {
        return DummyLocation.INSTANCE;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getName() {
        if (this._state == 1 || this._state == 2) {
            String localName = this._current.getLocalName();
            if (localName != null) {
                String prefix = this._current.getPrefix();
                String uri = this._current.getNamespaceURI();
                return new QName(fixNull(uri), localName, fixNull(prefix));
            }
            return QName.valueOf(this._current.getNodeName());
        }
        throw new IllegalStateException("DOMStreamReader: getName() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public NamespaceContext getNamespaceContext() {
        return this;
    }

    private Scope getCheckedScope() {
        if (this._state == 1 || this._state == 2) {
            return this.scopes[this.depth];
        }
        throw new IllegalStateException("DOMStreamReader: neither on START_ELEMENT nor END_ELEMENT");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getNamespaceCount() {
        return getCheckedScope().getNamespaceCount();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespacePrefix(int index) {
        return getCheckedScope().getNamespacePrefix(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(int index) {
        return getCheckedScope().getNamespaceURI(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI() {
        if (this._state == 1 || this._state == 2) {
            String uri = this._current.getNamespaceURI();
            return fixNull(uri);
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("DOMStreamReader: getNamespaceURI(String) call with a null prefix");
        }
        if (prefix.equals("xml")) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        if (prefix.equals("xmlns")) {
            return "http://www.w3.org/2000/xmlns/";
        }
        String nsUri = this.scopes[this.depth].getNamespaceURI(prefix);
        if (nsUri != null) {
            return nsUri;
        }
        String nsDeclName = prefix.length() == 0 ? "xmlns" : "xmlns:" + prefix;
        for (Node node = findRootElement(); node.getNodeType() != 9; node = node.getParentNode()) {
            NamedNodeMap namedNodeMap = node.getAttributes();
            Attr attr = (Attr) namedNodeMap.getNamedItem(nsDeclName);
            if (attr != null) {
                return attr.getValue();
            }
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String nsUri) {
        if (nsUri == null) {
            throw new IllegalArgumentException("DOMStreamReader: getPrefix(String) call with a null namespace URI");
        }
        if (nsUri.equals("http://www.w3.org/XML/1998/namespace")) {
            return "xml";
        }
        if (nsUri.equals("http://www.w3.org/2000/xmlns/")) {
            return "xmlns";
        }
        String prefix = this.scopes[this.depth].getPrefix(nsUri);
        if (prefix != null) {
            return prefix;
        }
        Node nodeFindRootElement = findRootElement();
        while (true) {
            Node node = nodeFindRootElement;
            if (node.getNodeType() != 9) {
                NamedNodeMap namedNodeMap = node.getAttributes();
                for (int i2 = namedNodeMap.getLength() - 1; i2 >= 0; i2--) {
                    Attr attr = (Attr) namedNodeMap.item(i2);
                    String prefix2 = getPrefixForAttr(attr, nsUri);
                    if (prefix2 != null) {
                        return prefix2;
                    }
                }
                nodeFindRootElement = node.getParentNode();
            } else {
                return null;
            }
        }
    }

    private Node findRootElement() {
        Node node;
        Node parentNode = this._start;
        while (true) {
            node = parentNode;
            int type = node.getNodeType();
            if (type == 9 || type == 1) {
                break;
            }
            parentNode = node.getParentNode();
        }
        return node;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getPrefixForAttr(Attr attr, String nsUri) {
        String attrName = attr.getNodeName();
        if ((attrName.startsWith("xmlns:") || attrName.equals("xmlns")) && attr.getValue().equals(nsUri)) {
            if (attrName.equals("xmlns")) {
                return "";
            }
            String localName = attr.getLocalName();
            return localName != null ? localName : QName.valueOf(attrName).getLocalPart();
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator getPrefixes(String nsUri) {
        String prefix = getPrefix(nsUri);
        return prefix == null ? Collections.emptyList().iterator() : Collections.singletonList(prefix).iterator();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPIData() {
        if (this._state == 3) {
            return ((ProcessingInstruction) this._current).getData();
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPITarget() {
        if (this._state == 3) {
            return ((ProcessingInstruction) this._current).getTarget();
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPrefix() {
        if (this._state == 1 || this._state == 2) {
            String prefix = this._current.getPrefix();
            return fixNull(prefix);
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Object getProperty(String str) throws IllegalArgumentException {
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getText() {
        if (this._state == 4) {
            return this.wholeText;
        }
        if (this._state == 12 || this._state == 5 || this._state == 9) {
            return this._current.getNodeValue();
        }
        throw new IllegalStateException("DOMStreamReader: getTextLength() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public char[] getTextCharacters() {
        return getText().toCharArray();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int targetLength) throws XMLStreamException {
        String text = getText();
        int copiedSize = Math.min(targetLength, text.length() - sourceStart);
        text.getChars(sourceStart, sourceStart + copiedSize, target, targetStart);
        return copiedSize;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextLength() {
        return getText().length();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextStart() {
        if (this._state == 4 || this._state == 12 || this._state == 5 || this._state == 9) {
            return 0;
        }
        throw new IllegalStateException("DOMStreamReader: getTextStart() called in illegal state");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getVersion() {
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasName() {
        return this._state == 1 || this._state == 2;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasNext() throws XMLStreamException {
        return this._state != 8;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasText() {
        return (this._state == 4 || this._state == 12 || this._state == 5 || this._state == 9) && getText().trim().length() > 0;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isAttributeSpecified(int param) {
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isCharacters() {
        return this._state == 4;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isEndElement() {
        return this._state == 2;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStandalone() {
        return true;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStartElement() {
        return this._state == 1;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isWhiteSpace() {
        return (this._state == 4 || this._state == 12) && getText().trim().length() == 0;
    }

    private static int mapNodeTypeToState(int nodetype) {
        switch (nodetype) {
            case 1:
                return 1;
            case 2:
            case 9:
            case 10:
            case 11:
            default:
                throw new RuntimeException("DOMStreamReader: Unexpected node type");
            case 3:
                return 4;
            case 4:
                return 12;
            case 5:
                return 9;
            case 6:
                return 15;
            case 7:
                return 3;
            case 8:
                return 5;
            case 12:
                return 14;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // javax.xml.stream.XMLStreamReader
    public int next() throws javax.xml.stream.XMLStreamException {
        /*
            r3 = this;
        L0:
            r0 = r3
            int r0 = r0._next()
            r4 = r0
            r0 = r4
            switch(r0) {
                case 1: goto L5c;
                case 4: goto L20;
                default: goto L62;
            }
        L20:
            r0 = r3
            org.w3c.dom.Node r0 = r0._current
            org.w3c.dom.Node r0 = r0.getPreviousSibling()
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L3b
            r0 = r5
            short r0 = r0.getNodeType()
            r1 = 3
            if (r0 != r1) goto L3b
            goto L0
        L3b:
            r0 = r3
            org.w3c.dom.Node r0 = r0._current
            org.w3c.dom.Text r0 = (org.w3c.dom.Text) r0
            r6 = r0
            r0 = r3
            r1 = r6
            java.lang.String r1 = r1.getWholeText()
            r0.wholeText = r1
            r0 = r3
            java.lang.String r0 = r0.wholeText
            int r0 = r0.length()
            if (r0 != 0) goto L5a
            goto L0
        L5a:
            r0 = 4
            return r0
        L5c:
            r0 = r3
            r0.splitAttributes()
            r0 = 1
            return r0
        L62:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.streaming.DOMStreamReader.next():int");
    }

    protected int _next() throws XMLStreamException {
        switch (this._state) {
            case 1:
                Node child = this._current.getFirstChild();
                if (child == null) {
                    this._state = 2;
                    return 2;
                }
                this._current = child;
                int iMapNodeTypeToState = mapNodeTypeToState(this._current.getNodeType());
                this._state = iMapNodeTypeToState;
                return iMapNodeTypeToState;
            case 2:
                this.depth--;
                break;
            case 3:
            case 4:
            case 5:
            case 9:
            case 12:
                break;
            case 6:
            case 10:
            case 11:
            case 13:
            default:
                throw new RuntimeException("DOMStreamReader: Unexpected internal state");
            case 7:
                if (this._current.getNodeType() == 1) {
                    this._state = 1;
                    return 1;
                }
                Node child2 = this._current.getFirstChild();
                if (child2 == null) {
                    this._state = 8;
                    return 8;
                }
                this._current = child2;
                int iMapNodeTypeToState2 = mapNodeTypeToState(this._current.getNodeType());
                this._state = iMapNodeTypeToState2;
                return iMapNodeTypeToState2;
            case 8:
                throw new IllegalStateException("DOMStreamReader: Calling next() at END_DOCUMENT");
        }
        if (this._current == this._start) {
            this._state = 8;
            return 8;
        }
        Node sibling = this._current.getNextSibling();
        if (sibling == null) {
            this._current = this._current.getParentNode();
            this._state = (this._current == null || this._current.getNodeType() == 9) ? 8 : 2;
            return this._state;
        }
        this._current = sibling;
        int iMapNodeTypeToState3 = mapNodeTypeToState(this._current.getNodeType());
        this._state = iMapNodeTypeToState3;
        return iMapNodeTypeToState3;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int nextTag() throws XMLStreamException {
        int eventType;
        int next = next();
        while (true) {
            eventType = next;
            if ((eventType != 4 || !isWhiteSpace()) && ((eventType != 12 || !isWhiteSpace()) && eventType != 6 && eventType != 3 && eventType != 5)) {
                break;
            }
            next = next();
        }
        if (eventType != 1 && eventType != 2) {
            throw new XMLStreamException2("DOMStreamReader: Expected start or end tag");
        }
        return eventType;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        if (type != this._state) {
            throw new XMLStreamException2("DOMStreamReader: Required event type not found");
        }
        if (namespaceURI != null && !namespaceURI.equals(getNamespaceURI())) {
            throw new XMLStreamException2("DOMStreamReader: Required namespaceURI not found");
        }
        if (localName != null && !localName.equals(getLocalName())) {
            throw new XMLStreamException2("DOMStreamReader: Required localName not found");
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean standaloneSet() {
        return true;
    }

    private static void displayDOM(Node node, OutputStream ostream) {
        try {
            System.out.println("\n====\n");
            XmlUtil.newTransformer().transform(new DOMSource(node), new StreamResult(ostream));
            System.out.println("\n====\n");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private static void verifyDOMIntegrity(Node node) {
        switch (node.getNodeType()) {
            case 1:
            case 2:
                if (node.getLocalName() == null) {
                    System.out.println("WARNING: DOM level 1 node found");
                    System.out.println(" -> node.getNodeName() = " + node.getNodeName());
                    System.out.println(" -> node.getNamespaceURI() = " + node.getNamespaceURI());
                    System.out.println(" -> node.getLocalName() = " + node.getLocalName());
                    System.out.println(" -> node.getPrefix() = " + node.getPrefix());
                }
                if (node.getNodeType() != 2) {
                    NamedNodeMap attrs = node.getAttributes();
                    for (int i2 = 0; i2 < attrs.getLength(); i2++) {
                        verifyDOMIntegrity(attrs.item(i2));
                    }
                    break;
                } else {
                    return;
                }
            case 9:
                break;
            default:
                return;
        }
        NodeList children = node.getChildNodes();
        for (int i3 = 0; i3 < children.getLength(); i3++) {
            verifyDOMIntegrity(children.item(i3));
        }
    }

    private static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }
}
