package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xml.internal.dtm.Axis;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIterNodeList;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/MultiDOM.class */
public final class MultiDOM implements DOM {
    private static final int NO_TYPE = -2;
    private static final int INITIAL_SIZE = 4;
    private DOMAdapter _main;
    private DTMManager _dtmManager;
    private Map<String, Integer> _documents = new HashMap();
    private int _size = 4;
    private int _free = 1;
    private DOM[] _adapters = new DOM[4];

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/MultiDOM$AxisIterator.class */
    private final class AxisIterator extends DTMAxisIteratorBase {
        private final int _axis;
        private final int _type;
        private DTMAxisIterator _source;
        private int _dtmId = -1;

        public AxisIterator(int axis, int type) {
            this._axis = axis;
            this._type = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._source == null) {
                return -1;
            }
            return this._source.next();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setRestartable(boolean flag) {
            if (this._source != null) {
                this._source.setRestartable(flag);
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == -1) {
                return this;
            }
            int dom = node >>> 16;
            if (this._source == null || this._dtmId != dom) {
                if (this._type == -2) {
                    this._source = MultiDOM.this._adapters[dom].getAxisIterator(this._axis);
                } else if (this._axis == 3) {
                    this._source = MultiDOM.this._adapters[dom].getTypedChildren(this._type);
                } else {
                    this._source = MultiDOM.this._adapters[dom].getTypedAxisIterator(this._axis, this._type);
                }
            }
            this._dtmId = dom;
            this._source.setStartNode(node);
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            if (this._source != null) {
                this._source.reset();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getLast() {
            if (this._source != null) {
                return this._source.getLast();
            }
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getPosition() {
            if (this._source != null) {
                return this._source.getPosition();
            }
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public boolean isReverse() {
            return Axis.isReverse(this._axis);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            if (this._source != null) {
                this._source.setMark();
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            if (this._source != null) {
                this._source.gotoMark();
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator cloneIterator() {
            AxisIterator clone = MultiDOM.this.new AxisIterator(this._axis, this._type);
            if (this._source != null) {
                clone._source = this._source.cloneIterator();
            }
            clone._dtmId = this._dtmId;
            return clone;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/MultiDOM$NodeValueIterator.class */
    private final class NodeValueIterator extends DTMAxisIteratorBase {
        private DTMAxisIterator _source;
        private String _value;
        private boolean _op;
        private final boolean _isReverse;
        private int _returnType;

        public NodeValueIterator(DTMAxisIterator source, int returnType, String value, boolean op) {
            this._returnType = 1;
            this._source = source;
            this._returnType = returnType;
            this._value = value;
            this._op = op;
            this._isReverse = source.isReverse();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public boolean isReverse() {
            return this._isReverse;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator cloneIterator() {
            try {
                NodeValueIterator clone = (NodeValueIterator) super.clone();
                clone._source = this._source.cloneIterator();
                clone.setRestartable(false);
                return clone.reset();
            } catch (CloneNotSupportedException e2) {
                BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e2.toString());
                return null;
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setRestartable(boolean isRestartable) {
            this._isRestartable = isRestartable;
            this._source.setRestartable(isRestartable);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            this._source.reset();
            return resetPosition();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node;
            String val;
            do {
                node = this._source.next();
                if (node != -1) {
                    val = MultiDOM.this.getStringValueX(node);
                } else {
                    return -1;
                }
            } while (this._value.equals(val) != this._op);
            if (this._returnType == 0) {
                return returnNode(node);
            }
            return returnNode(MultiDOM.this.getParent(node));
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (this._isRestartable) {
                DTMAxisIterator dTMAxisIterator = this._source;
                this._startNode = node;
                dTMAxisIterator.setStartNode(node);
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            this._source.setMark();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            this._source.gotoMark();
        }
    }

    public MultiDOM(DOM main) {
        DOMAdapter adapter = (DOMAdapter) main;
        this._adapters[0] = adapter;
        this._main = adapter;
        Object dOMImpl = adapter.getDOMImpl();
        if (dOMImpl instanceof DTMDefaultBase) {
            this._dtmManager = ((DTMDefaultBase) dOMImpl).getManager();
        }
        addDOMAdapter(adapter, false);
    }

    public int nextMask() {
        return this._free;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces) {
    }

    public int addDOMAdapter(DOMAdapter adapter) {
        return addDOMAdapter(adapter, true);
    }

    private int addDOMAdapter(DOMAdapter adapter, boolean indexByURI) {
        Object dOMImpl = adapter.getDOMImpl();
        int domNo = 1;
        int dtmSize = 1;
        SuballocatedIntVector dtmIds = null;
        if (dOMImpl instanceof DTMDefaultBase) {
            DTMDefaultBase dtmdb = (DTMDefaultBase) dOMImpl;
            dtmIds = dtmdb.getDTMIDs();
            dtmSize = dtmIds.size();
            domNo = dtmIds.elementAt(dtmSize - 1) >>> 16;
        } else if (dOMImpl instanceof SimpleResultTreeImpl) {
            SimpleResultTreeImpl simpleRTF = (SimpleResultTreeImpl) dOMImpl;
            domNo = simpleRTF.getDocument() >>> 16;
        }
        if (domNo >= this._size) {
            int oldSize = this._size;
            do {
                this._size *= 2;
            } while (this._size <= domNo);
            DOMAdapter[] newArray = new DOMAdapter[this._size];
            System.arraycopy(this._adapters, 0, newArray, 0, oldSize);
            this._adapters = newArray;
        }
        this._free = domNo + 1;
        if (dtmSize == 1) {
            this._adapters[domNo] = adapter;
        } else if (dtmIds != null) {
            int domPos = 0;
            for (int i2 = dtmSize - 1; i2 >= 0; i2--) {
                domPos = dtmIds.elementAt(i2) >>> 16;
                this._adapters[domPos] = adapter;
            }
            domNo = domPos;
        }
        if (indexByURI) {
            String uri = adapter.getDocumentURI(0);
            this._documents.put(uri, Integer.valueOf(domNo));
        }
        if (dOMImpl instanceof AdaptiveResultTreeImpl) {
            AdaptiveResultTreeImpl adaptiveRTF = (AdaptiveResultTreeImpl) dOMImpl;
            DOM nestedDom = adaptiveRTF.getNestedDOM();
            if (nestedDom != null) {
                DOMAdapter newAdapter = new DOMAdapter(nestedDom, adapter.getNamesArray(), adapter.getUrisArray(), adapter.getTypesArray(), adapter.getNamespaceArray());
                addDOMAdapter(newAdapter);
            }
        }
        return domNo;
    }

    public int getDocumentMask(String uri) {
        Integer domIdx = this._documents.get(uri);
        if (domIdx == null) {
            return -1;
        }
        return domIdx.intValue();
    }

    public DOM getDOMAdapter(String uri) {
        Integer domIdx = this._documents.get(uri);
        if (domIdx == null) {
            return null;
        }
        return this._adapters[domIdx.intValue()];
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public int getDocument() {
        return this._main.getDocument();
    }

    public DTMManager getDTMManager() {
        return this._dtmManager;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getIterator() {
        return this._main.getIterator();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getStringValue() {
        return this._main.getStringValue();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getChildren(int node) {
        return this._adapters[getDTMId(node)].getChildren(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getTypedChildren(int type) {
        return new AxisIterator(3, type);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getAxisIterator(int axis) {
        return new AxisIterator(axis, -2);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
        return new AxisIterator(axis, type);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNthDescendant(int node, int n2, boolean includeself) {
        return this._adapters[getDTMId(node)].getNthDescendant(node, n2, includeself);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op) {
        return new NodeValueIterator(iterator, type, value, op);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
        DTMAxisIterator iterator = this._main.getNamespaceAxisIterator(axis, ns);
        return iterator;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
        return this._adapters[getDTMId(node)].orderNodes(source, node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public int getExpandedTypeID(int node) {
        if (node != -1) {
            return this._adapters[node >>> 16].getExpandedTypeID(node);
        }
        return -1;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNamespaceType(int node) {
        return this._adapters[getDTMId(node)].getNamespaceType(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNSType(int node) {
        return this._adapters[getDTMId(node)].getNSType(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public int getParent(int node) {
        if (node == -1) {
            return -1;
        }
        return this._adapters[node >>> 16].getParent(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getAttributeNode(int type, int el) {
        if (el == -1) {
            return -1;
        }
        return this._adapters[el >>> 16].getAttributeNode(type, el);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeName(int node) {
        if (node == -1) {
            return "";
        }
        return this._adapters[node >>> 16].getNodeName(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeNameX(int node) {
        if (node == -1) {
            return "";
        }
        return this._adapters[node >>> 16].getNodeNameX(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getNamespaceName(int node) {
        if (node == -1) {
            return "";
        }
        return this._adapters[node >>> 16].getNamespaceName(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getStringValueX(int node) {
        if (node == -1) {
            return "";
        }
        return this._adapters[node >>> 16].getStringValueX(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void copy(int node, SerializationHandler handler) throws TransletException {
        if (node != -1) {
            this._adapters[node >>> 16].copy(node, handler);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void copy(DTMAxisIterator nodes, SerializationHandler handler) throws TransletException {
        while (true) {
            int node = nodes.next();
            if (node != -1) {
                this._adapters[node >>> 16].copy(node, handler);
            } else {
                return;
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
        if (node == -1) {
            return "";
        }
        return this._adapters[node >>> 16].shallowCopy(node, handler);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean lessThan(int node1, int node2) {
        if (node1 == -1) {
            return true;
        }
        if (node2 == -1) {
            return false;
        }
        int dom1 = getDTMId(node1);
        int dom2 = getDTMId(node2);
        return dom1 == dom2 ? this._adapters[dom1].lessThan(node1, node2) : dom1 < dom2;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void characters(int textNode, SerializationHandler handler) throws TransletException {
        if (textNode != -1) {
            this._adapters[textNode >>> 16].characters(textNode, handler);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void setFilter(StripFilter filter) {
        for (int dom = 0; dom < this._free; dom++) {
            if (this._adapters[dom] != null) {
                this._adapters[dom].setFilter(filter);
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public Node makeNode(int index) {
        if (index == -1) {
            return null;
        }
        return this._adapters[getDTMId(index)].makeNode(index);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public Node makeNode(DTMAxisIterator iter) {
        return this._main.makeNode(iter);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public NodeList makeNodeList(int index) {
        if (index == -1) {
            return null;
        }
        return this._adapters[getDTMId(index)].makeNodeList(index);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public NodeList makeNodeList(DTMAxisIterator iter) {
        int index = iter.next();
        if (index == -1) {
            return new DTMAxisIterNodeList(null, null);
        }
        iter.reset();
        return this._adapters[getDTMId(index)].makeNodeList(iter);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getLanguage(int node) {
        return this._adapters[getDTMId(node)].getLanguage(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getSize() {
        int size = 0;
        for (int i2 = 0; i2 < this._size; i2++) {
            size += this._adapters[i2].getSize();
        }
        return size;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String getDocumentURI(int node) {
        if (node == -1) {
            node = 0;
        }
        return this._adapters[node >>> 16].getDocumentURI(0);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean isElement(int node) {
        if (node == -1) {
            return false;
        }
        return this._adapters[node >>> 16].isElement(node);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public boolean isAttribute(int node) {
        if (node == -1) {
            return false;
        }
        return this._adapters[node >>> 16].isAttribute(node);
    }

    public int getDTMId(int nodeHandle) {
        if (nodeHandle == -1) {
            return 0;
        }
        int id = nodeHandle >>> 16;
        while (id >= 2 && this._adapters[id] == this._adapters[id - 1]) {
            id--;
        }
        return id;
    }

    public DOM getDTM(int nodeHandle) {
        return this._adapters[getDTMId(nodeHandle)];
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNodeIdent(int nodeHandle) {
        return this._adapters[nodeHandle >>> 16].getNodeIdent(nodeHandle);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public int getNodeHandle(int nodeId) {
        return this._main.getNodeHandle(nodeId);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DOM getResultTreeFrag(int initSize, int rtfType) {
        return this._main.getResultTreeFrag(initSize, rtfType);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager) {
        return this._main.getResultTreeFrag(initSize, rtfType, addToManager);
    }

    public DOM getMain() {
        return this._main;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public SerializationHandler getOutputDomBuilder() {
        return this._main.getOutputDomBuilder();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public String lookupNamespace(int node, String prefix) throws TransletException {
        return this._main.lookupNamespace(node, prefix);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM, com.sun.org.apache.xml.internal.dtm.DTM
    public String getUnparsedEntityURI(String entity) {
        return this._main.getUnparsedEntityURI(entity);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public Map<String, Integer> getElementsWithIDs() {
        return this._main.getElementsWithIDs();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOM
    public void release() {
        this._main.release();
    }

    private boolean isMatchingAdapterEntry(DOM entry, DOMAdapter adapter) {
        DOM dom = adapter.getDOMImpl();
        return entry == adapter || ((dom instanceof AdaptiveResultTreeImpl) && (entry instanceof DOMAdapter) && ((AdaptiveResultTreeImpl) dom).getNestedDOM() == ((DOMAdapter) entry).getDOMImpl());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void removeDOMAdapter(DOMAdapter adapter) {
        this._documents.remove(adapter.getDocumentURI(0));
        DOM dOMImpl = adapter.getDOMImpl();
        if (dOMImpl instanceof DTMDefaultBase) {
            SuballocatedIntVector ids = ((DTMDefaultBase) dOMImpl).getDTMIDs();
            int idsSize = ids.size();
            for (int i2 = 0; i2 < idsSize; i2++) {
                this._adapters[ids.elementAt(i2) >>> 16] = null;
            }
            return;
        }
        int id = dOMImpl.getDocument() >>> 16;
        if (id > 0 && id < this._adapters.length && isMatchingAdapterEntry(this._adapters[id], adapter)) {
            this._adapters[id] = null;
            return;
        }
        for (int i3 = 0; i3 < this._adapters.length; i3++) {
            if (isMatchingAdapterEntry(this._adapters[id], adapter)) {
                this._adapters[i3] = null;
                return;
            }
        }
    }
}
