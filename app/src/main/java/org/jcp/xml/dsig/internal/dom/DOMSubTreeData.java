package org.jcp.xml.dsig.internal.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.xml.crypto.NodeSetData;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSubTreeData.class */
public class DOMSubTreeData implements NodeSetData {
    private boolean excludeComments;
    private Node root;

    public DOMSubTreeData(Node node, boolean z2) {
        this.root = node;
        this.excludeComments = z2;
    }

    @Override // javax.xml.crypto.NodeSetData
    public Iterator<Node> iterator() {
        return new DelayedNodeIterator(this.root, this.excludeComments);
    }

    public Node getRoot() {
        return this.root;
    }

    public boolean excludeComments() {
        return this.excludeComments;
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSubTreeData$DelayedNodeIterator.class */
    static class DelayedNodeIterator implements Iterator<Node> {
        private Node root;
        private List<Node> nodeSet;
        private ListIterator<Node> li;
        private boolean withComments;

        DelayedNodeIterator(Node node, boolean z2) {
            this.root = node;
            this.withComments = !z2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nodeSet == null) {
                this.nodeSet = dereferenceSameDocumentURI(this.root);
                this.li = this.nodeSet.listIterator();
            }
            return this.li.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Node next() {
            if (this.nodeSet == null) {
                this.nodeSet = dereferenceSameDocumentURI(this.root);
                this.li = this.nodeSet.listIterator();
            }
            if (this.li.hasNext()) {
                return this.li.next();
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private List<Node> dereferenceSameDocumentURI(Node node) {
            ArrayList arrayList = new ArrayList();
            if (node != null) {
                nodeSetMinusCommentNodes(node, arrayList, null);
            }
            return arrayList;
        }

        private void nodeSetMinusCommentNodes(Node node, List<Node> list, Node node2) {
            switch (node.getNodeType()) {
                case 1:
                    NamedNodeMap attributes = node.getAttributes();
                    if (attributes != null) {
                        int length = attributes.getLength();
                        for (int i2 = 0; i2 < length; i2++) {
                            list.add(attributes.item(i2));
                        }
                    }
                    list.add(node);
                    Node node3 = null;
                    Node firstChild = node.getFirstChild();
                    while (true) {
                        Node node4 = firstChild;
                        if (node4 == null) {
                            break;
                        } else {
                            nodeSetMinusCommentNodes(node4, list, node3);
                            node3 = node4;
                            firstChild = node4.getNextSibling();
                        }
                    }
                case 3:
                case 4:
                    if (node2 == null || (node2.getNodeType() != 3 && node2.getNodeType() != 4)) {
                        list.add(node);
                        break;
                    }
                    break;
                case 7:
                    list.add(node);
                    break;
                case 8:
                    if (this.withComments) {
                        list.add(node);
                        break;
                    }
                    break;
                case 9:
                    Node node5 = null;
                    Node firstChild2 = node.getFirstChild();
                    while (true) {
                        Node node6 = firstChild2;
                        if (node6 == null) {
                            break;
                        } else {
                            nodeSetMinusCommentNodes(node6, list, node5);
                            node5 = node6;
                            firstChild2 = node6.getNextSibling();
                        }
                    }
            }
        }
    }
}
