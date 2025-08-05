package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex.class */
public class KeyIndex extends DTMAxisIteratorBase {
    private Map<String, IntegerArray> _index;
    private DOM _dom;
    private DOMEnhancedForDTM _enhancedDOM;
    private static final IntegerArray EMPTY_NODES = new IntegerArray(0);
    private int _currentDocumentNode = -1;
    private Map<Integer, Map> _rootToIndexMap = new HashMap();
    private IntegerArray _nodes = null;
    private int _markedPosition = 0;

    public KeyIndex(int dummy) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setRestartable(boolean flag) {
    }

    public void add(String value, int node, int rootNode) {
        if (this._currentDocumentNode != rootNode) {
            this._currentDocumentNode = rootNode;
            this._index = new HashMap();
            this._rootToIndexMap.put(Integer.valueOf(rootNode), this._index);
        }
        IntegerArray nodes = this._index.get(value);
        if (nodes == null) {
            IntegerArray nodes2 = new IntegerArray();
            this._index.put(value, nodes2);
            nodes2.add(node);
        } else if (node != nodes.at(nodes.cardinality() - 1)) {
            nodes.add(node);
        }
    }

    public void merge(KeyIndex other) {
        if (other != null && other._nodes != null) {
            if (this._nodes == null) {
                this._nodes = (IntegerArray) other._nodes.clone();
            } else {
                this._nodes.merge(other._nodes);
            }
        }
    }

    public void lookupId(Object value) {
        this._nodes = null;
        StringTokenizer values = new StringTokenizer((String) value, " \n\t");
        while (values.hasMoreElements()) {
            String token = (String) values.nextElement2();
            IntegerArray nodes = this._index.get(token);
            if (nodes == null && this._enhancedDOM != null && this._enhancedDOM.hasDOMSource()) {
                nodes = getDOMNodeById(token);
            }
            if (nodes != null) {
                if (this._nodes == null) {
                    this._nodes = (IntegerArray) nodes.clone();
                } else {
                    this._nodes.merge(nodes);
                }
            }
        }
    }

    public IntegerArray getDOMNodeById(String id) {
        int ident;
        IntegerArray nodes = null;
        if (this._enhancedDOM != null && (ident = this._enhancedDOM.getElementById(id)) != -1) {
            Integer root = new Integer(this._enhancedDOM.getDocument());
            Map<String, IntegerArray> index = this._rootToIndexMap.get(root);
            if (index == null) {
                index = new HashMap<>();
                this._rootToIndexMap.put(root, index);
            } else {
                nodes = index.get(id);
            }
            if (nodes == null) {
                nodes = new IntegerArray();
                index.put(id, nodes);
            }
            nodes.add(this._enhancedDOM.getNodeHandle(ident));
        }
        return nodes;
    }

    public void lookupKey(Object value) {
        IntegerArray nodes = this._index.get(value);
        this._nodes = nodes != null ? (IntegerArray) nodes.clone() : null;
        this._position = 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        if (this._nodes == null || this._position >= this._nodes.cardinality()) {
            return -1;
        }
        DOM dom = this._dom;
        IntegerArray integerArray = this._nodes;
        int i2 = this._position;
        this._position = i2 + 1;
        return dom.getNodeHandle(integerArray.at(i2));
    }

    public int containsID(int node, Object value) {
        String string = (String) value;
        int rootHandle = this._dom.getAxisIterator(19).setStartNode(node).next();
        Map<String, IntegerArray> index = this._rootToIndexMap.get(Integer.valueOf(rootHandle));
        StringTokenizer values = new StringTokenizer(string, " \n\t");
        while (values.hasMoreElements()) {
            String token = (String) values.nextElement2();
            IntegerArray nodes = null;
            if (index != null) {
                nodes = index.get(token);
            }
            if (nodes == null && this._enhancedDOM != null && this._enhancedDOM.hasDOMSource()) {
                nodes = getDOMNodeById(token);
            }
            if (nodes != null && nodes.indexOf(node) >= 0) {
                return 1;
            }
        }
        return 0;
    }

    public int containsKey(int node, Object value) {
        IntegerArray nodes;
        int rootHandle = this._dom.getAxisIterator(19).setStartNode(node).next();
        Map<String, IntegerArray> index = this._rootToIndexMap.get(new Integer(rootHandle));
        return (index == null || (nodes = index.get(value)) == null || nodes.indexOf(node) < 0) ? 0 : 1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator reset() {
        this._position = 0;
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getLast() {
        if (this._nodes == null) {
            return 0;
        }
        return this._nodes.cardinality();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getPosition() {
        return this._position;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setMark() {
        this._markedPosition = this._position;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void gotoMark() {
        this._position = this._markedPosition;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator setStartNode(int start) {
        if (start == -1) {
            this._nodes = null;
        } else if (this._nodes != null) {
            this._position = 0;
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getStartNode() {
        return 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public boolean isReverse() {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator cloneIterator() {
        KeyIndex other = new KeyIndex(0);
        other._index = this._index;
        other._rootToIndexMap = this._rootToIndexMap;
        other._nodes = this._nodes;
        other._position = this._position;
        return other;
    }

    public void setDom(DOM dom, int node) {
        this._dom = dom;
        if (dom instanceof MultiDOM) {
            dom = ((MultiDOM) dom).getDTM(node);
        }
        if (dom instanceof DOMEnhancedForDTM) {
            this._enhancedDOM = (DOMEnhancedForDTM) dom;
        } else if (dom instanceof DOMAdapter) {
            DOM idom = ((DOMAdapter) dom).getDOMImpl();
            if (idom instanceof DOMEnhancedForDTM) {
                this._enhancedDOM = (DOMEnhancedForDTM) idom;
            }
        }
    }

    public KeyIndexIterator getKeyIndexIterator(Object keyValue, boolean isKeyCall) {
        if (keyValue instanceof DTMAxisIterator) {
            return getKeyIndexIterator((DTMAxisIterator) keyValue, isKeyCall);
        }
        return getKeyIndexIterator(BasisLibrary.stringF(keyValue, this._dom), isKeyCall);
    }

    public KeyIndexIterator getKeyIndexIterator(String keyValue, boolean isKeyCall) {
        return new KeyIndexIterator(keyValue, isKeyCall);
    }

    public KeyIndexIterator getKeyIndexIterator(DTMAxisIterator keyValue, boolean isKeyCall) {
        return new KeyIndexIterator(keyValue, isKeyCall);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex$KeyIndexIterator.class */
    public class KeyIndexIterator extends MultiValuedNodeHeapIterator {
        private IntegerArray _nodes;
        private DTMAxisIterator _keyValueIterator;
        private String _keyValue;
        private boolean _isKeyIterator;

        /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex$KeyIndexIterator$KeyIndexHeapNode.class */
        protected class KeyIndexHeapNode extends MultiValuedNodeHeapIterator.HeapNode {
            private IntegerArray _nodes;
            private int _position;
            private int _markPosition;

            KeyIndexHeapNode(IntegerArray nodes) {
                super();
                this._position = 0;
                this._markPosition = -1;
                this._nodes = nodes;
            }

            @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
            public int step() {
                if (this._position < this._nodes.cardinality()) {
                    this._node = this._nodes.at(this._position);
                    this._position++;
                } else {
                    this._node = -1;
                }
                return this._node;
            }

            @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
            public MultiValuedNodeHeapIterator.HeapNode cloneHeapNode() {
                KeyIndexHeapNode clone = (KeyIndexHeapNode) super.cloneHeapNode();
                clone._nodes = this._nodes;
                clone._position = this._position;
                clone._markPosition = this._markPosition;
                return clone;
            }

            @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
            public void setMark() {
                this._markPosition = this._position;
            }

            @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
            public void gotoMark() {
                this._position = this._markPosition;
            }

            @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
            public boolean isLessThan(MultiValuedNodeHeapIterator.HeapNode heapNode) {
                return this._node < heapNode._node;
            }

            @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
            public MultiValuedNodeHeapIterator.HeapNode setStartNode(int node) {
                return this;
            }

            @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
            public MultiValuedNodeHeapIterator.HeapNode reset() {
                this._position = 0;
                return this;
            }
        }

        KeyIndexIterator(String keyValue, boolean isKeyIterator) {
            this._isKeyIterator = isKeyIterator;
            this._keyValue = keyValue;
        }

        KeyIndexIterator(DTMAxisIterator keyValues, boolean isKeyIterator) {
            this._keyValueIterator = keyValues;
            this._isKeyIterator = isKeyIterator;
        }

        protected IntegerArray lookupNodes(int root, String keyValue) {
            IntegerArray result = null;
            Map<String, IntegerArray> index = (Map) KeyIndex.this._rootToIndexMap.get(Integer.valueOf(root));
            if (!this._isKeyIterator) {
                StringTokenizer values = new StringTokenizer(keyValue, " \n\t");
                while (values.hasMoreElements()) {
                    String token = (String) values.nextElement2();
                    IntegerArray nodes = null;
                    if (index != null) {
                        nodes = index.get(token);
                    }
                    if (nodes == null && KeyIndex.this._enhancedDOM != null && KeyIndex.this._enhancedDOM.hasDOMSource()) {
                        nodes = KeyIndex.this.getDOMNodeById(token);
                    }
                    if (nodes != null) {
                        if (result == null) {
                            result = (IntegerArray) nodes.clone();
                        } else {
                            result.merge(nodes);
                        }
                    }
                }
            } else if (index != null) {
                result = index.get(keyValue);
            }
            return result;
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            this._startNode = node;
            if (this._keyValueIterator != null) {
                this._keyValueIterator = this._keyValueIterator.setStartNode(node);
            }
            init();
            return super.setStartNode(node);
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int nodeHandle;
            if (this._nodes != null) {
                if (this._position < this._nodes.cardinality()) {
                    nodeHandle = returnNode(this._nodes.at(this._position));
                } else {
                    nodeHandle = -1;
                }
            } else {
                nodeHandle = super.next();
            }
            return nodeHandle;
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator, com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            if (this._nodes == null) {
                init();
            } else {
                super.reset();
            }
            return resetPosition();
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator
        protected void init() {
            super.init();
            this._position = 0;
            int rootHandle = KeyIndex.this._dom.getAxisIterator(19).setStartNode(this._startNode).next();
            if (this._keyValueIterator == null) {
                this._nodes = lookupNodes(rootHandle, this._keyValue);
                if (this._nodes == null) {
                    this._nodes = KeyIndex.EMPTY_NODES;
                    return;
                }
                return;
            }
            DTMAxisIterator keyValues = this._keyValueIterator.reset();
            boolean foundNodes = false;
            this._nodes = null;
            int next = keyValues.next();
            while (true) {
                int keyValueNode = next;
                if (keyValueNode == -1) {
                    break;
                }
                String keyValue = BasisLibrary.stringF(keyValueNode, KeyIndex.this._dom);
                IntegerArray nodes = lookupNodes(rootHandle, keyValue);
                if (nodes != null) {
                    if (!foundNodes) {
                        this._nodes = nodes;
                        foundNodes = true;
                    } else {
                        if (this._nodes != null) {
                            addHeapNode(new KeyIndexHeapNode(this._nodes));
                            this._nodes = null;
                        }
                        addHeapNode(new KeyIndexHeapNode(nodes));
                    }
                }
                next = keyValues.next();
            }
            if (!foundNodes) {
                this._nodes = KeyIndex.EMPTY_NODES;
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getLast() {
            return this._nodes != null ? this._nodes.cardinality() : super.getLast();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getNodeByPosition(int position) {
            int node = -1;
            if (this._nodes != null) {
                if (position > 0) {
                    if (position <= this._nodes.cardinality()) {
                        this._position = position;
                        node = this._nodes.at(position - 1);
                    } else {
                        this._position = this._nodes.cardinality();
                    }
                }
            } else {
                node = super.getNodeByPosition(position);
            }
            return node;
        }
    }
}
