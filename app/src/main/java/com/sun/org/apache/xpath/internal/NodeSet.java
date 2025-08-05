package com.sun.org.apache.xpath.internal;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.DOM2Helper;
import com.sun.org.apache.xpath.internal.axes.ContextNodeList;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/NodeSet.class */
public class NodeSet implements NodeList, NodeIterator, Cloneable, ContextNodeList {
    protected transient int m_next;
    protected transient boolean m_mutable;
    protected transient boolean m_cacheNodes;
    private transient int m_last;
    private int m_blocksize;
    Node[] m_map;
    protected int m_firstFree;
    private int m_mapSize;

    public NodeSet() {
        this.m_next = 0;
        this.m_mutable = true;
        this.m_cacheNodes = true;
        this.m_last = 0;
        this.m_firstFree = 0;
        this.m_blocksize = 32;
        this.m_mapSize = 0;
    }

    public NodeSet(int blocksize) {
        this.m_next = 0;
        this.m_mutable = true;
        this.m_cacheNodes = true;
        this.m_last = 0;
        this.m_firstFree = 0;
        this.m_blocksize = blocksize;
        this.m_mapSize = 0;
    }

    public NodeSet(NodeList nodelist) {
        this(32);
        addNodes(nodelist);
    }

    public NodeSet(NodeSet nodelist) throws DOMException {
        this(32);
        addNodes((NodeIterator) nodelist);
    }

    public NodeSet(NodeIterator ni) throws DOMException {
        this(32);
        addNodes(ni);
    }

    public NodeSet(Node node) {
        this(32);
        addNode(node);
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public Node getRoot() {
        return null;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public NodeIterator cloneWithReset() throws CloneNotSupportedException {
        NodeSet clone = (NodeSet) clone();
        clone.reset();
        return clone;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public void reset() {
        this.m_next = 0;
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public int getWhatToShow() {
        return -17;
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public NodeFilter getFilter() {
        return null;
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public boolean getExpandEntityReferences() {
        return true;
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public Node nextNode() throws DOMException {
        if (this.m_next < size()) {
            Node next = elementAt(this.m_next);
            this.m_next++;
            return next;
        }
        return null;
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public Node previousNode() throws DOMException {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_ITERATE", null));
        }
        if (this.m_next - 1 > 0) {
            this.m_next--;
            return elementAt(this.m_next);
        }
        return null;
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public void detach() {
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public boolean isFresh() {
        return this.m_next == 0;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public void runTo(int index) {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", null));
        }
        if (index >= 0 && this.m_next < this.m_firstFree) {
            this.m_next = index;
        } else {
            this.m_next = this.m_firstFree - 1;
        }
    }

    @Override // org.w3c.dom.NodeList
    public Node item(int index) {
        runTo(index);
        return elementAt(index);
    }

    @Override // org.w3c.dom.NodeList
    public int getLength() {
        runTo(-1);
        return size();
    }

    public void addNode(Node n2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        addElement(n2);
    }

    public void insertNode(Node n2, int pos) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        insertElementAt(n2, pos);
    }

    public void removeNode(Node n2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        removeElement(n2);
    }

    public void addNodes(NodeList nodelist) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        if (null != nodelist) {
            int nChildren = nodelist.getLength();
            for (int i2 = 0; i2 < nChildren; i2++) {
                Node obj = nodelist.item(i2);
                if (null != obj) {
                    addElement(obj);
                }
            }
        }
    }

    public void addNodes(NodeSet ns) throws DOMException {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        addNodes((NodeIterator) ns);
    }

    public void addNodes(NodeIterator iterator) throws DOMException {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        if (null == iterator) {
            return;
        }
        while (true) {
            Node obj = iterator.nextNode();
            if (null != obj) {
                addElement(obj);
            } else {
                return;
            }
        }
    }

    public void addNodesInDocOrder(NodeList nodelist, XPathContext support) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        int nChildren = nodelist.getLength();
        for (int i2 = 0; i2 < nChildren; i2++) {
            Node node = nodelist.item(i2);
            if (null != node) {
                addNodeInDocOrder(node, support);
            }
        }
    }

    public void addNodesInDocOrder(NodeIterator iterator, XPathContext support) throws DOMException {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        while (true) {
            Node node = iterator.nextNode();
            if (null != node) {
                addNodeInDocOrder(node, support);
            } else {
                return;
            }
        }
    }

    private boolean addNodesInDocOrder(int start, int end, int testIndex, NodeList nodelist, XPathContext support) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        Node node = nodelist.item(testIndex);
        int i2 = end;
        while (true) {
            if (i2 < start) {
                break;
            }
            Node child = elementAt(i2);
            if (child == node) {
                i2 = -2;
                break;
            }
            if (DOM2Helper.isNodeAfter(node, child)) {
                i2--;
            } else {
                insertElementAt(node, i2 + 1);
                int testIndex2 = testIndex - 1;
                if (testIndex2 > 0) {
                    boolean foundPrev = addNodesInDocOrder(0, i2, testIndex2, nodelist, support);
                    if (!foundPrev) {
                        addNodesInDocOrder(i2, size() - 1, testIndex2, nodelist, support);
                    }
                }
            }
        }
        if (i2 == -1) {
            insertElementAt(node, 0);
        }
        return false;
    }

    public int addNodeInDocOrder(Node node, boolean test, XPathContext support) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        int insertIndex = -1;
        if (test) {
            int size = size();
            int i2 = size - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                }
                Node child = elementAt(i2);
                if (child == node) {
                    i2 = -2;
                    break;
                }
                if (!DOM2Helper.isNodeAfter(node, child)) {
                    break;
                }
                i2--;
            }
            if (i2 != -2) {
                insertIndex = i2 + 1;
                insertElementAt(node, insertIndex);
            }
        } else {
            insertIndex = size();
            boolean foundit = false;
            int i3 = 0;
            while (true) {
                if (i3 >= insertIndex) {
                    break;
                }
                if (!item(i3).equals(node)) {
                    i3++;
                } else {
                    foundit = true;
                    break;
                }
            }
            if (!foundit) {
                addElement(node);
            }
        }
        return insertIndex;
    }

    public int addNodeInDocOrder(Node node, XPathContext support) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        return addNodeInDocOrder(node, true, support);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public int getCurrentPos() {
        return this.m_next;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public void setCurrentPos(int i2) {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", null));
        }
        this.m_next = i2;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public Node getCurrentNode() {
        if (!this.m_cacheNodes) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", null));
        }
        int saved = this.m_next;
        Node n2 = this.m_next < this.m_firstFree ? elementAt(this.m_next) : null;
        this.m_next = saved;
        return n2;
    }

    public boolean getShouldCacheNodes() {
        return this.m_cacheNodes;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public void setShouldCacheNodes(boolean b2) {
        if (!isFresh()) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_CALL_SETSHOULDCACHENODE", null));
        }
        this.m_cacheNodes = b2;
        this.m_mutable = true;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public int getLast() {
        return this.m_last;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public void setLast(int last) {
        this.m_last = last;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public Object clone() throws CloneNotSupportedException {
        NodeSet clone = (NodeSet) super.clone();
        if (null != this.m_map && this.m_map == clone.m_map) {
            clone.m_map = new Node[this.m_map.length];
            System.arraycopy(this.m_map, 0, clone.m_map, 0, this.m_map.length);
        }
        return clone;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ContextNodeList
    public int size() {
        return this.m_firstFree;
    }

    public void addElement(Node value) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            if (null == this.m_map) {
                this.m_map = new Node[this.m_blocksize];
                this.m_mapSize = this.m_blocksize;
            } else {
                this.m_mapSize += this.m_blocksize;
                Node[] newMap = new Node[this.m_mapSize];
                System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
                this.m_map = newMap;
            }
        }
        this.m_map[this.m_firstFree] = value;
        this.m_firstFree++;
    }

    public final void push(Node value) {
        int ff = this.m_firstFree;
        if (ff + 1 >= this.m_mapSize) {
            if (null == this.m_map) {
                this.m_map = new Node[this.m_blocksize];
                this.m_mapSize = this.m_blocksize;
            } else {
                this.m_mapSize += this.m_blocksize;
                Node[] newMap = new Node[this.m_mapSize];
                System.arraycopy(this.m_map, 0, newMap, 0, ff + 1);
                this.m_map = newMap;
            }
        }
        this.m_map[ff] = value;
        this.m_firstFree = ff + 1;
    }

    public final Node pop() {
        this.m_firstFree--;
        Node n2 = this.m_map[this.m_firstFree];
        this.m_map[this.m_firstFree] = null;
        return n2;
    }

    public final Node popAndTop() {
        this.m_firstFree--;
        this.m_map[this.m_firstFree] = null;
        if (this.m_firstFree == 0) {
            return null;
        }
        return this.m_map[this.m_firstFree - 1];
    }

    public final void popQuick() {
        this.m_firstFree--;
        this.m_map[this.m_firstFree] = null;
    }

    public final Node peepOrNull() {
        if (null == this.m_map || this.m_firstFree <= 0) {
            return null;
        }
        return this.m_map[this.m_firstFree - 1];
    }

    public final void pushPair(Node v1, Node v2) {
        if (null == this.m_map) {
            this.m_map = new Node[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
        } else if (this.m_firstFree + 2 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            Node[] newMap = new Node[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = v1;
        this.m_map[this.m_firstFree + 1] = v2;
        this.m_firstFree += 2;
    }

    public final void popPair() {
        this.m_firstFree -= 2;
        this.m_map[this.m_firstFree] = null;
        this.m_map[this.m_firstFree + 1] = null;
    }

    public final void setTail(Node n2) {
        this.m_map[this.m_firstFree - 1] = n2;
    }

    public final void setTailSub1(Node n2) {
        this.m_map[this.m_firstFree - 2] = n2;
    }

    public final Node peepTail() {
        return this.m_map[this.m_firstFree - 1];
    }

    public final Node peepTailSub1() {
        return this.m_map[this.m_firstFree - 2];
    }

    public void insertElementAt(Node value, int at2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        if (null == this.m_map) {
            this.m_map = new Node[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
        } else if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            Node[] newMap = new Node[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        if (at2 <= this.m_firstFree - 1) {
            System.arraycopy(this.m_map, at2, this.m_map, at2 + 1, this.m_firstFree - at2);
        }
        this.m_map[at2] = value;
        this.m_firstFree++;
    }

    public void appendNodes(NodeSet nodes) {
        int nNodes = nodes.size();
        if (null == this.m_map) {
            this.m_mapSize = nNodes + this.m_blocksize;
            this.m_map = new Node[this.m_mapSize];
        } else if (this.m_firstFree + nNodes >= this.m_mapSize) {
            this.m_mapSize += nNodes + this.m_blocksize;
            Node[] newMap = new Node[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + nNodes);
            this.m_map = newMap;
        }
        System.arraycopy(nodes.m_map, 0, this.m_map, this.m_firstFree, nNodes);
        this.m_firstFree += nNodes;
    }

    public void removeAllElements() {
        if (null == this.m_map) {
            return;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            this.m_map[i2] = null;
        }
        this.m_firstFree = 0;
    }

    public boolean removeElement(Node s2) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        if (null == this.m_map) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            Node node = this.m_map[i2];
            if (null != node && node.equals(s2)) {
                if (i2 < this.m_firstFree - 1) {
                    System.arraycopy(this.m_map, i2 + 1, this.m_map, i2, (this.m_firstFree - i2) - 1);
                }
                this.m_firstFree--;
                this.m_map[this.m_firstFree] = null;
                return true;
            }
        }
        return false;
    }

    public void removeElementAt(int i2) {
        if (null == this.m_map) {
            return;
        }
        if (i2 >= this.m_firstFree) {
            throw new ArrayIndexOutOfBoundsException(i2 + " >= " + this.m_firstFree);
        }
        if (i2 < 0) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        if (i2 < this.m_firstFree - 1) {
            System.arraycopy(this.m_map, i2 + 1, this.m_map, i2, (this.m_firstFree - i2) - 1);
        }
        this.m_firstFree--;
        this.m_map[this.m_firstFree] = null;
    }

    public void setElementAt(Node node, int index) {
        if (!this.m_mutable) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
        }
        if (null == this.m_map) {
            this.m_map = new Node[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
        }
        this.m_map[index] = node;
    }

    public Node elementAt(int i2) {
        if (null == this.m_map) {
            return null;
        }
        return this.m_map[i2];
    }

    public boolean contains(Node s2) {
        runTo(-1);
        if (null == this.m_map) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            Node node = this.m_map[i2];
            if (null != node && node.equals(s2)) {
                return true;
            }
        }
        return false;
    }

    public int indexOf(Node elem, int index) {
        runTo(-1);
        if (null == this.m_map) {
            return -1;
        }
        for (int i2 = index; i2 < this.m_firstFree; i2++) {
            Node node = this.m_map[i2];
            if (null != node && node.equals(elem)) {
                return i2;
            }
        }
        return -1;
    }

    public int indexOf(Node elem) {
        runTo(-1);
        if (null == this.m_map) {
            return -1;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            Node node = this.m_map[i2];
            if (null != node && node.equals(elem)) {
                return i2;
            }
        }
        return -1;
    }
}
