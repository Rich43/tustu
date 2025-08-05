package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.dom.events.EventImpl;
import com.sun.org.apache.xerces.internal.dom.events.MutationEventImpl;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Vector;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.ranges.DocumentRange;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DocumentImpl.class */
public class DocumentImpl extends CoreDocumentImpl implements DocumentTraversal, DocumentEvent, DocumentRange {
    static final long serialVersionUID = 515687835542616694L;
    protected List<NodeIterator> iterators;
    protected List<Range> ranges;
    protected Map<NodeImpl, List<LEntry>> eventListeners;
    protected boolean mutationEvents;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("iterators", Vector.class), new ObjectStreamField("ranges", Vector.class), new ObjectStreamField("eventListeners", Hashtable.class), new ObjectStreamField("mutationEvents", Boolean.TYPE)};
    EnclosingAttr savedEnclosingAttr;

    public DocumentImpl() {
        this.mutationEvents = false;
    }

    public DocumentImpl(boolean grammarAccess) {
        super(grammarAccess);
        this.mutationEvents = false;
    }

    public DocumentImpl(DocumentType doctype) {
        super(doctype);
        this.mutationEvents = false;
    }

    public DocumentImpl(DocumentType doctype, boolean grammarAccess) {
        super(doctype, grammarAccess);
        this.mutationEvents = false;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.ChildNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        DocumentImpl newdoc = new DocumentImpl();
        callUserDataHandlers(this, newdoc, (short) 1);
        cloneNode(newdoc, deep);
        newdoc.mutationEvents = this.mutationEvents;
        return newdoc;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return DOMImplementationImpl.getDOMImplementation();
    }

    public NodeIterator createNodeIterator(Node root, short whatToShow, NodeFilter filter) {
        return createNodeIterator(root, whatToShow, filter, true);
    }

    @Override // org.w3c.dom.traversal.DocumentTraversal
    public NodeIterator createNodeIterator(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion) throws MissingResourceException {
        if (root == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
            throw new DOMException((short) 9, msg);
        }
        NodeIterator iterator = new NodeIteratorImpl(this, root, whatToShow, filter, entityReferenceExpansion);
        if (this.iterators == null) {
            this.iterators = new ArrayList();
        }
        this.iterators.add(iterator);
        return iterator;
    }

    public TreeWalker createTreeWalker(Node root, short whatToShow, NodeFilter filter) {
        return createTreeWalker(root, whatToShow, filter, true);
    }

    @Override // org.w3c.dom.traversal.DocumentTraversal
    public TreeWalker createTreeWalker(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion) throws MissingResourceException {
        if (root == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
            throw new DOMException((short) 9, msg);
        }
        return new TreeWalkerImpl(root, whatToShow, filter, entityReferenceExpansion);
    }

    void removeNodeIterator(NodeIterator nodeIterator) {
        if (nodeIterator == null || this.iterators == null) {
            return;
        }
        this.iterators.remove(nodeIterator);
    }

    @Override // org.w3c.dom.ranges.DocumentRange
    public Range createRange() {
        if (this.ranges == null) {
            this.ranges = new ArrayList();
        }
        Range range = new RangeImpl(this);
        this.ranges.add(range);
        return range;
    }

    void removeRange(Range range) {
        if (range == null || this.ranges == null) {
            return;
        }
        this.ranges.remove(range);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void replacedText(NodeImpl node) {
        if (this.ranges != null) {
            int size = this.ranges.size();
            for (int i2 = 0; i2 != size; i2++) {
                ((RangeImpl) this.ranges.get(i2)).receiveReplacedText(node);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void deletedText(NodeImpl node, int offset, int count) {
        if (this.ranges != null) {
            int size = this.ranges.size();
            for (int i2 = 0; i2 != size; i2++) {
                ((RangeImpl) this.ranges.get(i2)).receiveDeletedText(node, offset, count);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void insertedText(NodeImpl node, int offset, int count) {
        if (this.ranges != null) {
            int size = this.ranges.size();
            for (int i2 = 0; i2 != size; i2++) {
                ((RangeImpl) this.ranges.get(i2)).receiveInsertedText(node, offset, count);
            }
        }
    }

    void splitData(Node node, Node newNode, int offset) {
        if (this.ranges != null) {
            int size = this.ranges.size();
            for (int i2 = 0; i2 != size; i2++) {
                ((RangeImpl) this.ranges.get(i2)).receiveSplitData(node, newNode, offset);
            }
        }
    }

    @Override // org.w3c.dom.events.DocumentEvent
    public Event createEvent(String type) throws DOMException, MissingResourceException {
        if (type.equalsIgnoreCase("Events") || "Event".equals(type)) {
            return new EventImpl();
        }
        if (type.equalsIgnoreCase("MutationEvents") || "MutationEvent".equals(type)) {
            return new MutationEventImpl();
        }
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
        throw new DOMException((short) 9, msg);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void setMutationEvents(boolean set) {
        this.mutationEvents = set;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    boolean getMutationEvents() {
        return this.mutationEvents;
    }

    private void setEventListeners(NodeImpl n2, List<LEntry> listeners) {
        if (this.eventListeners == null) {
            this.eventListeners = new HashMap();
        }
        if (listeners == null) {
            this.eventListeners.remove(n2);
            if (this.eventListeners.isEmpty()) {
                this.mutationEvents = false;
                return;
            }
            return;
        }
        this.eventListeners.put(n2, listeners);
        this.mutationEvents = true;
    }

    private List<LEntry> getEventListeners(NodeImpl n2) {
        if (this.eventListeners == null) {
            return null;
        }
        return this.eventListeners.get(n2);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DocumentImpl$LEntry.class */
    class LEntry implements Serializable {
        private static final long serialVersionUID = -8426757059492421631L;
        String type;
        EventListener listener;
        boolean useCapture;

        LEntry(String type, EventListener listener, boolean useCapture) {
            this.type = type;
            this.listener = listener;
            this.useCapture = useCapture;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    protected void addEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture) {
        if (type == null || type.equals("") || listener == null) {
            return;
        }
        removeEventListener(node, type, listener, useCapture);
        List<LEntry> nodeListeners = getEventListeners(node);
        if (nodeListeners == null) {
            nodeListeners = new ArrayList();
            setEventListeners(node, nodeListeners);
        }
        nodeListeners.add(new LEntry(type, listener, useCapture));
        LCount lc = LCount.lookup(type);
        if (useCapture) {
            lc.captures++;
            lc.total++;
        } else {
            lc.bubbles++;
            lc.total++;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    protected void removeEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture) {
        List<LEntry> nodeListeners;
        if (type == null || type.equals("") || listener == null || (nodeListeners = getEventListeners(node)) == null) {
            return;
        }
        for (int i2 = nodeListeners.size() - 1; i2 >= 0; i2--) {
            LEntry le = nodeListeners.get(i2);
            if (le.useCapture == useCapture && le.listener == listener && le.type.equals(type)) {
                nodeListeners.remove(i2);
                if (nodeListeners.isEmpty()) {
                    setEventListeners(node, null);
                }
                LCount lc = LCount.lookup(type);
                if (useCapture) {
                    lc.captures--;
                    lc.total--;
                    return;
                } else {
                    lc.bubbles--;
                    lc.total--;
                    return;
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    protected void copyEventListeners(NodeImpl src, NodeImpl tgt) {
        List<LEntry> nodeListeners = getEventListeners(src);
        if (nodeListeners == null) {
            return;
        }
        setEventListeners(tgt, new ArrayList(nodeListeners));
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    protected boolean dispatchEvent(NodeImpl node, Event event) throws MissingResourceException {
        if (event == null) {
            return false;
        }
        EventImpl evt = (EventImpl) event;
        if (!evt.initialized || evt.type == null || evt.type.equals("")) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "UNSPECIFIED_EVENT_TYPE_ERR", null);
            throw new EventException((short) 0, msg);
        }
        LCount lc = LCount.lookup(evt.getType());
        if (lc.total == 0) {
            return evt.preventDefault;
        }
        evt.target = node;
        evt.stopPropagation = false;
        evt.preventDefault = false;
        List<Node> pv = new ArrayList<>(10);
        Node parentNode = node.getParentNode();
        while (true) {
            Node n2 = parentNode;
            if (n2 == null) {
                break;
            }
            pv.add(n2);
            parentNode = n2.getParentNode();
        }
        if (lc.captures > 0) {
            evt.eventPhase = (short) 1;
            for (int j2 = pv.size() - 1; j2 >= 0 && !evt.stopPropagation; j2--) {
                NodeImpl nn = (NodeImpl) pv.get(j2);
                evt.currentTarget = nn;
                List<LEntry> nodeListeners = getEventListeners(nn);
                if (nodeListeners != null) {
                    List<LEntry> nl = (List) ((ArrayList) nodeListeners).clone();
                    int nlsize = nl.size();
                    for (int i2 = 0; i2 < nlsize; i2++) {
                        LEntry le = nl.get(i2);
                        if (le.useCapture && le.type.equals(evt.type) && nodeListeners.contains(le)) {
                            try {
                                le.listener.handleEvent(evt);
                            } catch (Exception e2) {
                            }
                        }
                    }
                }
            }
        }
        if (lc.bubbles > 0) {
            evt.eventPhase = (short) 2;
            evt.currentTarget = node;
            List<LEntry> nodeListeners2 = getEventListeners(node);
            if (!evt.stopPropagation && nodeListeners2 != null) {
                List<LEntry> nl2 = (List) ((ArrayList) nodeListeners2).clone();
                int nlsize2 = nl2.size();
                for (int i3 = 0; i3 < nlsize2; i3++) {
                    LEntry le2 = nl2.get(i3);
                    if (!le2.useCapture && le2.type.equals(evt.type) && nodeListeners2.contains(le2)) {
                        try {
                            le2.listener.handleEvent(evt);
                        } catch (Exception e3) {
                        }
                    }
                }
            }
            if (evt.bubbles) {
                evt.eventPhase = (short) 3;
                int pvsize = pv.size();
                for (int j3 = 0; j3 < pvsize && !evt.stopPropagation; j3++) {
                    NodeImpl nn2 = (NodeImpl) pv.get(j3);
                    evt.currentTarget = nn2;
                    List<LEntry> nodeListeners3 = getEventListeners(nn2);
                    if (nodeListeners3 != null) {
                        List<LEntry> nl3 = (List) ((ArrayList) nodeListeners3).clone();
                        int nlsize3 = nl3.size();
                        for (int i4 = 0; i4 < nlsize3; i4++) {
                            LEntry le3 = nl3.get(i4);
                            if (!le3.useCapture && le3.type.equals(evt.type) && nodeListeners3.contains(le3)) {
                                try {
                                    le3.listener.handleEvent(evt);
                                } catch (Exception e4) {
                                }
                            }
                        }
                    }
                }
            }
        }
        if (lc.defaults <= 0 || !evt.cancelable || !evt.preventDefault) {
        }
        return evt.preventDefault;
    }

    protected void dispatchEventToSubtree(Node n2, Event e2) {
        ((NodeImpl) n2).dispatchEvent(e2);
        if (n2.getNodeType() == 1) {
            NamedNodeMap a2 = n2.getAttributes();
            for (int i2 = a2.getLength() - 1; i2 >= 0; i2--) {
                dispatchingEventToSubtree(a2.item(i2), e2);
            }
        }
        dispatchingEventToSubtree(n2.getFirstChild(), e2);
    }

    protected void dispatchingEventToSubtree(Node n2, Event e2) {
        if (n2 == null) {
            return;
        }
        ((NodeImpl) n2).dispatchEvent(e2);
        if (n2.getNodeType() == 1) {
            NamedNodeMap a2 = n2.getAttributes();
            for (int i2 = a2.getLength() - 1; i2 >= 0; i2--) {
                dispatchingEventToSubtree(a2.item(i2), e2);
            }
        }
        dispatchingEventToSubtree(n2.getFirstChild(), e2);
        dispatchingEventToSubtree(n2.getNextSibling(), e2);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DocumentImpl$EnclosingAttr.class */
    class EnclosingAttr implements Serializable {
        private static final long serialVersionUID = 5208387723391647216L;
        AttrImpl node;
        String oldvalue;

        EnclosingAttr() {
        }
    }

    protected void dispatchAggregateEvents(NodeImpl node, EnclosingAttr ea) throws MissingResourceException {
        if (ea != null) {
            dispatchAggregateEvents(node, ea.node, ea.oldvalue, (short) 1);
        } else {
            dispatchAggregateEvents(node, null, null, (short) 0);
        }
    }

    protected void dispatchAggregateEvents(NodeImpl node, AttrImpl enclosingAttr, String oldvalue, short change) throws MissingResourceException {
        NodeImpl owner = null;
        if (enclosingAttr != null) {
            LCount lc = LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            owner = (NodeImpl) enclosingAttr.getOwnerElement();
            if (lc.total > 0 && owner != null) {
                MutationEventImpl me = new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_ATTR_MODIFIED, true, false, enclosingAttr, oldvalue, enclosingAttr.getNodeValue(), enclosingAttr.getNodeName(), change);
                owner.dispatchEvent(me);
            }
        }
        LCount lc2 = LCount.lookup(MutationEventImpl.DOM_SUBTREE_MODIFIED);
        if (lc2.total > 0) {
            MutationEvent me2 = new MutationEventImpl();
            me2.initMutationEvent(MutationEventImpl.DOM_SUBTREE_MODIFIED, true, false, null, null, null, null, (short) 0);
            if (enclosingAttr != null) {
                dispatchEvent(enclosingAttr, me2);
                if (owner != null) {
                    dispatchEvent(owner, me2);
                    return;
                }
                return;
            }
            dispatchEvent(node, me2);
        }
    }

    protected void saveEnclosingAttr(NodeImpl node) {
        this.savedEnclosingAttr = null;
        LCount lc = LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
        if (lc.total > 0) {
            NodeImpl nodeImplParentNode = node;
            while (true) {
                NodeImpl eventAncestor = nodeImplParentNode;
                if (eventAncestor == null) {
                    return;
                }
                int type = eventAncestor.getNodeType();
                if (type == 2) {
                    EnclosingAttr retval = new EnclosingAttr();
                    retval.node = (AttrImpl) eventAncestor;
                    retval.oldvalue = retval.node.getNodeValue();
                    this.savedEnclosingAttr = retval;
                    return;
                }
                if (type == 5) {
                    nodeImplParentNode = eventAncestor.parentNode();
                } else if (type == 3) {
                    nodeImplParentNode = eventAncestor.parentNode();
                } else {
                    return;
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void modifyingCharacterData(NodeImpl node, boolean replace) {
        if (this.mutationEvents && !replace) {
            saveEnclosingAttr(node);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void modifiedCharacterData(NodeImpl node, String oldvalue, String value, boolean replace) throws MissingResourceException {
        if (this.mutationEvents && !replace) {
            LCount lc = LCount.lookup(MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED);
            if (lc.total > 0) {
                MutationEvent me = new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED, true, false, null, oldvalue, value, null, (short) 0);
                dispatchEvent(node, me);
            }
            dispatchAggregateEvents(node, this.savedEnclosingAttr);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void replacedCharacterData(NodeImpl node, String oldvalue, String value) throws MissingResourceException {
        modifiedCharacterData(node, oldvalue, value, false);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void insertingNode(NodeImpl node, boolean replace) {
        if (this.mutationEvents && !replace) {
            saveEnclosingAttr(node);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void insertedNode(NodeImpl node, NodeImpl newInternal, boolean replace) throws MissingResourceException {
        if (this.mutationEvents) {
            LCount lc = LCount.lookup(MutationEventImpl.DOM_NODE_INSERTED);
            if (lc.total > 0) {
                MutationEventImpl me = new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_NODE_INSERTED, true, false, node, null, null, null, (short) 0);
                dispatchEvent(newInternal, me);
            }
            LCount lc2 = LCount.lookup(MutationEventImpl.DOM_NODE_INSERTED_INTO_DOCUMENT);
            if (lc2.total > 0) {
                NodeImpl eventAncestor = node;
                if (this.savedEnclosingAttr != null) {
                    eventAncestor = (NodeImpl) this.savedEnclosingAttr.node.getOwnerElement();
                }
                if (eventAncestor != null) {
                    NodeImpl nodeImplParentNode = eventAncestor;
                    while (true) {
                        NodeImpl p2 = nodeImplParentNode;
                        if (p2 == null) {
                            break;
                        }
                        eventAncestor = p2;
                        if (p2.getNodeType() == 2) {
                            nodeImplParentNode = (NodeImpl) ((AttrImpl) p2).getOwnerElement();
                        } else {
                            nodeImplParentNode = p2.parentNode();
                        }
                    }
                    if (eventAncestor.getNodeType() == 9) {
                        MutationEventImpl me2 = new MutationEventImpl();
                        me2.initMutationEvent(MutationEventImpl.DOM_NODE_INSERTED_INTO_DOCUMENT, false, false, null, null, null, null, (short) 0);
                        dispatchEventToSubtree(newInternal, me2);
                    }
                }
            }
            if (!replace) {
                dispatchAggregateEvents(node, this.savedEnclosingAttr);
            }
        }
        if (this.ranges != null) {
            int size = this.ranges.size();
            for (int i2 = 0; i2 != size; i2++) {
                ((RangeImpl) this.ranges.get(i2)).insertedNodeFromDOM(newInternal);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void removingNode(NodeImpl node, NodeImpl oldChild, boolean replace) throws MissingResourceException {
        if (this.iterators != null) {
            int size = this.iterators.size();
            for (int i2 = 0; i2 != size; i2++) {
                ((NodeIteratorImpl) this.iterators.get(i2)).removeNode(oldChild);
            }
        }
        if (this.ranges != null) {
            int size2 = this.ranges.size();
            for (int i3 = 0; i3 != size2; i3++) {
                ((RangeImpl) this.ranges.get(i3)).removeNode(oldChild);
            }
        }
        if (this.mutationEvents) {
            if (!replace) {
                saveEnclosingAttr(node);
            }
            LCount lc = LCount.lookup(MutationEventImpl.DOM_NODE_REMOVED);
            if (lc.total > 0) {
                MutationEventImpl me = new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_NODE_REMOVED, true, false, node, null, null, null, (short) 0);
                dispatchEvent(oldChild, me);
            }
            LCount lc2 = LCount.lookup(MutationEventImpl.DOM_NODE_REMOVED_FROM_DOCUMENT);
            if (lc2.total > 0) {
                NodeImpl eventAncestor = this;
                if (this.savedEnclosingAttr != null) {
                    eventAncestor = (NodeImpl) this.savedEnclosingAttr.node.getOwnerElement();
                }
                if (eventAncestor != null) {
                    NodeImpl nodeImplParentNode = eventAncestor.parentNode();
                    while (true) {
                        NodeImpl p2 = nodeImplParentNode;
                        if (p2 == null) {
                            break;
                        }
                        eventAncestor = p2;
                        nodeImplParentNode = p2.parentNode();
                    }
                    if (eventAncestor.getNodeType() == 9) {
                        MutationEventImpl me2 = new MutationEventImpl();
                        me2.initMutationEvent(MutationEventImpl.DOM_NODE_REMOVED_FROM_DOCUMENT, false, false, null, null, null, null, (short) 0);
                        dispatchEventToSubtree(oldChild, me2);
                    }
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void removedNode(NodeImpl node, boolean replace) throws MissingResourceException {
        if (this.mutationEvents && !replace) {
            dispatchAggregateEvents(node, this.savedEnclosingAttr);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void replacingNode(NodeImpl node) {
        if (this.mutationEvents) {
            saveEnclosingAttr(node);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void replacingData(NodeImpl node) {
        if (this.mutationEvents) {
            saveEnclosingAttr(node);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void replacedNode(NodeImpl node) throws MissingResourceException {
        if (this.mutationEvents) {
            dispatchAggregateEvents(node, this.savedEnclosingAttr);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void modifiedAttrValue(AttrImpl attr, String oldvalue) throws MissingResourceException {
        if (this.mutationEvents) {
            dispatchAggregateEvents(attr, attr, oldvalue, (short) 1);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void setAttrNode(AttrImpl attr, AttrImpl previous) throws MissingResourceException {
        if (this.mutationEvents) {
            if (previous == null) {
                dispatchAggregateEvents(attr.ownerNode, attr, null, (short) 2);
            } else {
                dispatchAggregateEvents(attr.ownerNode, attr, previous.getNodeValue(), (short) 1);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void removedAttrNode(AttrImpl attr, NodeImpl oldOwner, String name) throws MissingResourceException {
        if (this.mutationEvents) {
            LCount lc = LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            if (lc.total > 0) {
                MutationEventImpl me = new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_ATTR_MODIFIED, true, false, attr, attr.getNodeValue(), null, name, (short) 3);
                dispatchEvent(oldOwner, me);
            }
            dispatchAggregateEvents(oldOwner, null, null, (short) 0);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void renamedAttrNode(Attr oldAt, Attr newAt) {
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    void renamedElement(Element oldEl, Element newEl) {
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Vector<NodeIterator> it = this.iterators == null ? null : new Vector<>(this.iterators);
        Vector<Range> r2 = this.ranges == null ? null : new Vector<>(this.ranges);
        Hashtable<NodeImpl, Vector<LEntry>> el = null;
        if (this.eventListeners != null) {
            el = new Hashtable<>();
            for (Map.Entry<NodeImpl, List<LEntry>> e2 : this.eventListeners.entrySet()) {
                el.put(e2.getKey(), new Vector<>(e2.getValue()));
            }
        }
        ObjectOutputStream.PutField pf = out.putFields();
        pf.put("iterators", it);
        pf.put("ranges", r2);
        pf.put("eventListeners", el);
        pf.put("mutationEvents", this.mutationEvents);
        out.writeFields();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField gf = in.readFields();
        Vector<NodeIterator> it = (Vector) gf.get("iterators", (Object) null);
        Vector<Range> r2 = (Vector) gf.get("ranges", (Object) null);
        Hashtable<NodeImpl, Vector<LEntry>> el = (Hashtable) gf.get("eventListeners", (Object) null);
        this.mutationEvents = gf.get("mutationEvents", false);
        if (it != null) {
            this.iterators = new ArrayList(it);
        }
        if (r2 != null) {
            this.ranges = new ArrayList(r2);
        }
        if (el != null) {
            this.eventListeners = new HashMap();
            for (Map.Entry<NodeImpl, Vector<LEntry>> e2 : el.entrySet()) {
                this.eventListeners.put(e2.getKey(), new ArrayList(e2.getValue()));
            }
        }
    }
}
