package com.sun.org.apache.xml.internal.dtm.ref.sax2dtm;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMException;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import com.sun.org.apache.xml.internal.dtm.ref.ExtendedType;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xml.internal.utils.XMLStringDefault;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import java.util.Vector;
import javax.xml.transform.Source;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2.class */
public class SAX2DTM2 extends SAX2DTM {
    private int[] m_exptype_map0;
    private int[] m_nextsib_map0;
    private int[] m_firstch_map0;
    private int[] m_parent_map0;
    private int[][] m_exptype_map;
    private int[][] m_nextsib_map;
    private int[][] m_firstch_map;
    private int[][] m_parent_map;
    protected ExtendedType[] m_extendedTypes;
    protected Vector m_values;
    private int m_valueIndex;
    private int m_maxNodeIndex;
    protected int m_SHIFT;
    protected int m_MASK;
    protected int m_blocksize;
    protected static final int TEXT_LENGTH_BITS = 10;
    protected static final int TEXT_OFFSET_BITS = 21;
    protected static final int TEXT_LENGTH_MAX = 1023;
    protected static final int TEXT_OFFSET_MAX = 2097151;
    protected boolean m_buildIdIndex;
    private static final String EMPTY_STR = "";
    private static final XMLString EMPTY_XML_STR = new XMLStringDefault("");

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$ChildrenIterator.class */
    public final class ChildrenIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        public ChildrenIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = node == -1 ? -1 : SAX2DTM2.this._firstch2(SAX2DTM2.this.makeNodeIdentity(node));
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._currentNode != -1) {
                int node = this._currentNode;
                this._currentNode = SAX2DTM2.this._nextsib2(node);
                return returnNode(SAX2DTM2.this.makeNodeHandle(node));
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$ParentIterator.class */
    public final class ParentIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private int _nodeType;

        public ParentIterator() {
            super();
            this._nodeType = -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                if (node != -1) {
                    this._currentNode = SAX2DTM2.this._parent2(SAX2DTM2.this.makeNodeIdentity(node));
                } else {
                    this._currentNode = -1;
                }
                return resetPosition();
            }
            return this;
        }

        public DTMAxisIterator setNodeType(int type) {
            this._nodeType = type;
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int result = this._currentNode;
            if (result == -1) {
                return -1;
            }
            if (this._nodeType == -1) {
                this._currentNode = -1;
                return returnNode(SAX2DTM2.this.makeNodeHandle(result));
            }
            if (this._nodeType >= 14) {
                if (this._nodeType == SAX2DTM2.this._exptype2(result)) {
                    this._currentNode = -1;
                    return returnNode(SAX2DTM2.this.makeNodeHandle(result));
                }
                return -1;
            }
            if (this._nodeType == SAX2DTM2.this._type2(result)) {
                this._currentNode = -1;
                return returnNode(SAX2DTM2.this.makeNodeHandle(result));
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedChildrenIterator.class */
    public final class TypedChildrenIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _nodeType;

        public TypedChildrenIterator(int nodeType) {
            super();
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = node == -1 ? -1 : SAX2DTM2.this._firstch2(SAX2DTM2.this.makeNodeIdentity(this._startNode));
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            if (node == -1) {
                return -1;
            }
            int nodeType = this._nodeType;
            if (nodeType != 1) {
                while (node != -1 && SAX2DTM2.this._exptype2(node) != nodeType) {
                    node = SAX2DTM2.this._nextsib2(node);
                }
            } else {
                while (node != -1) {
                    int eType = SAX2DTM2.this._exptype2(node);
                    if (eType >= 14) {
                        break;
                    }
                    node = SAX2DTM2.this._nextsib2(node);
                }
            }
            if (node == -1) {
                this._currentNode = -1;
                return -1;
            }
            this._currentNode = SAX2DTM2.this._nextsib2(node);
            return returnNode(SAX2DTM2.this.makeNodeHandle(node));
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getNodeByPosition(int position) {
            if (position <= 0) {
                return -1;
            }
            int node = this._currentNode;
            int pos = 0;
            int nodeType = this._nodeType;
            if (nodeType != 1) {
                while (node != -1) {
                    if (SAX2DTM2.this._exptype2(node) == nodeType) {
                        pos++;
                        if (pos == position) {
                            return SAX2DTM2.this.makeNodeHandle(node);
                        }
                    }
                    node = SAX2DTM2.this._nextsib2(node);
                }
                return -1;
            }
            while (node != -1) {
                if (SAX2DTM2.this._exptype2(node) >= 14) {
                    pos++;
                    if (pos == position) {
                        return SAX2DTM2.this.makeNodeHandle(node);
                    }
                }
                node = SAX2DTM2.this._nextsib2(node);
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedRootIterator.class */
    public class TypedRootIterator extends DTMDefaultBaseIterators.RootIterator {
        private final int _nodeType;

        public TypedRootIterator(int nodeType) {
            super();
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.RootIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._startNode == this._currentNode) {
                return -1;
            }
            int node = this._startNode;
            int expType = SAX2DTM2.this._exptype2(SAX2DTM2.this.makeNodeIdentity(node));
            this._currentNode = node;
            if (this._nodeType >= 14) {
                if (this._nodeType == expType) {
                    return returnNode(node);
                }
                return -1;
            }
            if (expType < 14) {
                if (expType == this._nodeType) {
                    return returnNode(node);
                }
                return -1;
            }
            if (SAX2DTM2.this.m_extendedTypes[expType].getNodeType() == this._nodeType) {
                return returnNode(node);
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$FollowingSiblingIterator.class */
    public class FollowingSiblingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        public FollowingSiblingIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = SAX2DTM2.this.makeNodeIdentity(node);
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            this._currentNode = this._currentNode == -1 ? -1 : SAX2DTM2.this._nextsib2(this._currentNode);
            return returnNode(SAX2DTM2.this.makeNodeHandle(this._currentNode));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedFollowingSiblingIterator.class */
    public final class TypedFollowingSiblingIterator extends FollowingSiblingIterator {
        private final int _nodeType;

        public TypedFollowingSiblingIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.FollowingSiblingIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._currentNode == -1) {
                return -1;
            }
            int node = this._currentNode;
            int nodeType = this._nodeType;
            if (nodeType == 1) {
                do {
                    int i_nextsib2 = SAX2DTM2.this._nextsib2(node);
                    node = i_nextsib2;
                    if (i_nextsib2 == -1) {
                        break;
                    }
                } while (SAX2DTM2.this._exptype2(node) < 14);
            } else {
                do {
                    int i_nextsib22 = SAX2DTM2.this._nextsib2(node);
                    node = i_nextsib22;
                    if (i_nextsib22 == -1) {
                        break;
                    }
                } while (SAX2DTM2.this._exptype2(node) != nodeType);
            }
            this._currentNode = node;
            if (node == -1) {
                return -1;
            }
            return returnNode(SAX2DTM2.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$AttributeIterator.class */
    public final class AttributeIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        public AttributeIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = SAX2DTM2.this.getFirstAttributeIdentity(SAX2DTM2.this.makeNodeIdentity(node));
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            if (node != -1) {
                this._currentNode = SAX2DTM2.this.getNextAttributeIdentity(node);
                return returnNode(SAX2DTM2.this.makeNodeHandle(node));
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedAttributeIterator.class */
    public final class TypedAttributeIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _nodeType;

        public TypedAttributeIterator(int nodeType) {
            super();
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = SAX2DTM2.this.getTypedAttribute(node, this._nodeType);
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            this._currentNode = -1;
            return returnNode(node);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$PrecedingSiblingIterator.class */
    public class PrecedingSiblingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        protected int _startNodeID;

        public PrecedingSiblingIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public boolean isReverse() {
            return true;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                int node2 = SAX2DTM2.this.makeNodeIdentity(node);
                this._startNodeID = node2;
                if (node2 == -1) {
                    this._currentNode = node2;
                    return resetPosition();
                }
                int type = SAX2DTM2.this._type2(node2);
                if (2 == type || 13 == type) {
                    this._currentNode = node2;
                } else {
                    this._currentNode = SAX2DTM2.this._parent2(node2);
                    if (-1 != this._currentNode) {
                        this._currentNode = SAX2DTM2.this._firstch2(this._currentNode);
                    } else {
                        this._currentNode = node2;
                    }
                }
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._currentNode == this._startNodeID || this._currentNode == -1) {
                return -1;
            }
            int node = this._currentNode;
            this._currentNode = SAX2DTM2.this._nextsib2(node);
            return returnNode(SAX2DTM2.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedPrecedingSiblingIterator.class */
    public final class TypedPrecedingSiblingIterator extends PrecedingSiblingIterator {
        private final int _nodeType;

        public TypedPrecedingSiblingIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.PrecedingSiblingIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            int nodeType = this._nodeType;
            int startNodeID = this._startNodeID;
            if (nodeType != 1) {
                while (node != -1 && node != startNodeID && SAX2DTM2.this._exptype2(node) != nodeType) {
                    node = SAX2DTM2.this._nextsib2(node);
                }
            } else {
                while (node != -1 && node != startNodeID && SAX2DTM2.this._exptype2(node) < 14) {
                    node = SAX2DTM2.this._nextsib2(node);
                }
            }
            if (node == -1 || node == startNodeID) {
                this._currentNode = -1;
                return -1;
            }
            this._currentNode = SAX2DTM2.this._nextsib2(node);
            return returnNode(SAX2DTM2.this.makeNodeHandle(node));
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getLast() {
            if (this._last != -1) {
                return this._last;
            }
            setMark();
            int node = this._currentNode;
            int nodeType = this._nodeType;
            int startNodeID = this._startNodeID;
            int last = 0;
            if (nodeType != 1) {
                while (node != -1 && node != startNodeID) {
                    if (SAX2DTM2.this._exptype2(node) == nodeType) {
                        last++;
                    }
                    node = SAX2DTM2.this._nextsib2(node);
                }
            } else {
                while (node != -1 && node != startNodeID) {
                    if (SAX2DTM2.this._exptype2(node) >= 14) {
                        last++;
                    }
                    node = SAX2DTM2.this._nextsib2(node);
                }
            }
            gotoMark();
            int i2 = last;
            this._last = i2;
            return i2;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$PrecedingIterator.class */
    public class PrecedingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private final int _maxAncestors = 8;
        protected int[] _stack;
        protected int _sp;
        protected int _oldsp;
        protected int _markedsp;
        protected int _markedNode;
        protected int _markedDescendant;

        public PrecedingIterator() {
            super();
            this._maxAncestors = 8;
            this._stack = new int[8];
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public boolean isReverse() {
            return true;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator cloneIterator() {
            this._isRestartable = false;
            try {
                PrecedingIterator clone = (PrecedingIterator) super.clone();
                int[] stackCopy = new int[this._stack.length];
                System.arraycopy(this._stack, 0, stackCopy, 0, this._stack.length);
                clone._stack = stackCopy;
                return clone;
            } catch (CloneNotSupportedException e2) {
                throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            if (this._isRestartable) {
                int node2 = SAX2DTM2.this.makeNodeIdentity(node);
                if (SAX2DTM2.this._type2(node2) == 2) {
                    node2 = SAX2DTM2.this._parent2(node2);
                }
                this._startNode = node2;
                int index = 0;
                this._stack[0] = node2;
                int parent = node2;
                while (true) {
                    int i_parent2 = SAX2DTM2.this._parent2(parent);
                    parent = i_parent2;
                    if (i_parent2 == -1) {
                        break;
                    }
                    index++;
                    if (index == this._stack.length) {
                        int[] stack = new int[index * 2];
                        System.arraycopy(this._stack, 0, stack, 0, index);
                        this._stack = stack;
                    }
                    this._stack[index] = parent;
                }
                if (index > 0) {
                    index--;
                }
                this._currentNode = this._stack[index];
                int i2 = index;
                this._sp = i2;
                this._oldsp = i2;
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            this._currentNode++;
            while (this._sp >= 0) {
                if (this._currentNode < this._stack[this._sp]) {
                    int type = SAX2DTM2.this._type2(this._currentNode);
                    if (type != 2 && type != 13) {
                        return returnNode(SAX2DTM2.this.makeNodeHandle(this._currentNode));
                    }
                } else {
                    this._sp--;
                }
                this._currentNode++;
            }
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            this._sp = this._oldsp;
            return resetPosition();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            this._markedsp = this._sp;
            this._markedNode = this._currentNode;
            this._markedDescendant = this._stack[0];
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            this._sp = this._markedsp;
            this._currentNode = this._markedNode;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedPrecedingIterator.class */
    public final class TypedPrecedingIterator extends PrecedingIterator {
        private final int _nodeType;

        public TypedPrecedingIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.PrecedingIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            int nodeType = this._nodeType;
            if (nodeType >= 14) {
                while (true) {
                    node++;
                    if (this._sp < 0) {
                        node = -1;
                        break;
                    }
                    if (node >= this._stack[this._sp]) {
                        int i2 = this._sp - 1;
                        this._sp = i2;
                        if (i2 < 0) {
                            node = -1;
                            break;
                        }
                    } else if (SAX2DTM2.this._exptype2(node) == nodeType) {
                        break;
                    }
                }
            } else {
                while (true) {
                    node++;
                    if (this._sp < 0) {
                        node = -1;
                        break;
                    }
                    if (node >= this._stack[this._sp]) {
                        int i3 = this._sp - 1;
                        this._sp = i3;
                        if (i3 < 0) {
                            node = -1;
                            break;
                        }
                    } else {
                        int expType = SAX2DTM2.this._exptype2(node);
                        if (expType < 14) {
                            if (expType == nodeType) {
                                break;
                            }
                        } else if (SAX2DTM2.this.m_extendedTypes[expType].getNodeType() == nodeType) {
                            break;
                        }
                    }
                }
            }
            this._currentNode = node;
            if (node == -1) {
                return -1;
            }
            return returnNode(SAX2DTM2.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$FollowingIterator.class */
    public class FollowingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        public FollowingIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            int first;
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                int node2 = SAX2DTM2.this.makeNodeIdentity(node);
                int type = SAX2DTM2.this._type2(node2);
                if (2 == type || 13 == type) {
                    node2 = SAX2DTM2.this._parent2(node2);
                    int first2 = SAX2DTM2.this._firstch2(node2);
                    if (-1 != first2) {
                        this._currentNode = SAX2DTM2.this.makeNodeHandle(first2);
                        return resetPosition();
                    }
                }
                do {
                    first = SAX2DTM2.this._nextsib2(node2);
                    if (-1 == first) {
                        node2 = SAX2DTM2.this._parent2(node2);
                    }
                    if (-1 != first) {
                        break;
                    }
                } while (-1 != node2);
                this._currentNode = SAX2DTM2.this.makeNodeHandle(first);
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            int current = SAX2DTM2.this.makeNodeIdentity(node);
            while (true) {
                current++;
                int type = SAX2DTM2.this._type2(current);
                if (-1 == type) {
                    this._currentNode = -1;
                    return returnNode(node);
                }
                if (2 != type && 13 != type) {
                    this._currentNode = SAX2DTM2.this.makeNodeHandle(current);
                    return returnNode(node);
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedFollowingIterator.class */
    public final class TypedFollowingIterator extends FollowingIterator {
        private final int _nodeType;

        public TypedFollowingIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.FollowingIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node;
            int type;
            int type2;
            int nodeType = this._nodeType;
            int currentNodeID = SAX2DTM2.this.makeNodeIdentity(this._currentNode);
            if (nodeType >= 14) {
                do {
                    node = currentNodeID;
                    int current = node;
                    while (true) {
                        current++;
                        type2 = SAX2DTM2.this._type2(current);
                        if (type2 == -1 || (2 != type2 && 13 != type2)) {
                            break;
                        }
                    }
                    currentNodeID = type2 != -1 ? current : -1;
                    if (node == -1) {
                        break;
                    }
                } while (SAX2DTM2.this._exptype2(node) != nodeType);
            } else {
                do {
                    node = currentNodeID;
                    int current2 = node;
                    while (true) {
                        current2++;
                        type = SAX2DTM2.this._type2(current2);
                        if (type == -1 || (2 != type && 13 != type)) {
                            break;
                        }
                    }
                    currentNodeID = type != -1 ? current2 : -1;
                    if (node == -1 || SAX2DTM2.this._exptype2(node) == nodeType) {
                        break;
                    }
                } while (SAX2DTM2.this._type2(node) != nodeType);
            }
            this._currentNode = SAX2DTM2.this.makeNodeHandle(currentNodeID);
            if (node == -1) {
                return -1;
            }
            return returnNode(SAX2DTM2.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$AncestorIterator.class */
    public class AncestorIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        private static final int m_blocksize = 32;
        int[] m_ancestors;
        int m_size;
        int m_ancestorsPos;
        int m_markedPos;
        int m_realStartNode;

        public AncestorIterator() {
            super();
            this.m_ancestors = new int[32];
            this.m_size = 0;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getStartNode() {
            return this.m_realStartNode;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public final boolean isReverse() {
            return true;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator cloneIterator() {
            this._isRestartable = false;
            try {
                AncestorIterator clone = (AncestorIterator) super.clone();
                clone._startNode = this._startNode;
                return clone;
            } catch (CloneNotSupportedException e2) {
                throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            this.m_realStartNode = node;
            if (this._isRestartable) {
                int nodeID = SAX2DTM2.this.makeNodeIdentity(node);
                this.m_size = 0;
                if (nodeID == -1) {
                    this._currentNode = -1;
                    this.m_ancestorsPos = 0;
                    return this;
                }
                if (!this._includeSelf) {
                    nodeID = SAX2DTM2.this._parent2(nodeID);
                    node = SAX2DTM2.this.makeNodeHandle(nodeID);
                }
                this._startNode = node;
                while (nodeID != -1) {
                    if (this.m_size >= this.m_ancestors.length) {
                        int[] newAncestors = new int[this.m_size * 2];
                        System.arraycopy(this.m_ancestors, 0, newAncestors, 0, this.m_ancestors.length);
                        this.m_ancestors = newAncestors;
                    }
                    int[] iArr = this.m_ancestors;
                    int i2 = this.m_size;
                    this.m_size = i2 + 1;
                    iArr[i2] = node;
                    nodeID = SAX2DTM2.this._parent2(nodeID);
                    node = SAX2DTM2.this.makeNodeHandle(nodeID);
                }
                this.m_ancestorsPos = this.m_size - 1;
                this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            this.m_ancestorsPos = this.m_size - 1;
            this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
            return resetPosition();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int next = this._currentNode;
            int pos = this.m_ancestorsPos - 1;
            this.m_ancestorsPos = pos;
            this._currentNode = pos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
            return returnNode(next);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            this.m_markedPos = this.m_ancestorsPos;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            this.m_ancestorsPos = this.m_markedPos;
            this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedAncestorIterator.class */
    public final class TypedAncestorIterator extends AncestorIterator {
        private final int _nodeType;

        public TypedAncestorIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.AncestorIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            this.m_realStartNode = node;
            if (this._isRestartable) {
                int nodeID = SAX2DTM2.this.makeNodeIdentity(node);
                this.m_size = 0;
                if (nodeID == -1) {
                    this._currentNode = -1;
                    this.m_ancestorsPos = 0;
                    return this;
                }
                int nodeType = this._nodeType;
                if (!this._includeSelf) {
                    nodeID = SAX2DTM2.this._parent2(nodeID);
                    node = SAX2DTM2.this.makeNodeHandle(nodeID);
                }
                this._startNode = node;
                if (nodeType >= 14) {
                    while (nodeID != -1) {
                        if (SAX2DTM2.this._exptype2(nodeID) == nodeType) {
                            if (this.m_size >= this.m_ancestors.length) {
                                int[] newAncestors = new int[this.m_size * 2];
                                System.arraycopy(this.m_ancestors, 0, newAncestors, 0, this.m_ancestors.length);
                                this.m_ancestors = newAncestors;
                            }
                            int[] iArr = this.m_ancestors;
                            int i2 = this.m_size;
                            this.m_size = i2 + 1;
                            iArr[i2] = SAX2DTM2.this.makeNodeHandle(nodeID);
                        }
                        nodeID = SAX2DTM2.this._parent2(nodeID);
                    }
                } else {
                    while (nodeID != -1) {
                        int eType = SAX2DTM2.this._exptype2(nodeID);
                        if ((eType < 14 && eType == nodeType) || (eType >= 14 && SAX2DTM2.this.m_extendedTypes[eType].getNodeType() == nodeType)) {
                            if (this.m_size >= this.m_ancestors.length) {
                                int[] newAncestors2 = new int[this.m_size * 2];
                                System.arraycopy(this.m_ancestors, 0, newAncestors2, 0, this.m_ancestors.length);
                                this.m_ancestors = newAncestors2;
                            }
                            int[] iArr2 = this.m_ancestors;
                            int i3 = this.m_size;
                            this.m_size = i3 + 1;
                            iArr2[i3] = SAX2DTM2.this.makeNodeHandle(nodeID);
                        }
                        nodeID = SAX2DTM2.this._parent2(nodeID);
                    }
                }
                this.m_ancestorsPos = this.m_size - 1;
                this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1;
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getNodeByPosition(int position) {
            if (position > 0 && position <= this.m_size) {
                return this.m_ancestors[position - 1];
            }
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int getLast() {
            return this.m_size;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$DescendantIterator.class */
    public class DescendantIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
        public DescendantIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = SAX2DTM2.this.getDocument();
            }
            if (this._isRestartable) {
                int node2 = SAX2DTM2.this.makeNodeIdentity(node);
                this._startNode = node2;
                if (this._includeSelf) {
                    node2--;
                }
                this._currentNode = node2;
                return resetPosition();
            }
            return this;
        }

        protected final boolean isDescendant(int identity) {
            return SAX2DTM2.this._parent2(identity) >= this._startNode || this._startNode == identity;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int type;
            int startNode = this._startNode;
            if (startNode == -1) {
                return -1;
            }
            if (this._includeSelf && this._currentNode + 1 == startNode) {
                SAX2DTM2 sax2dtm2 = SAX2DTM2.this;
                int i2 = this._currentNode + 1;
                this._currentNode = i2;
                return returnNode(sax2dtm2.makeNodeHandle(i2));
            }
            int node = this._currentNode;
            if (startNode != 0) {
                while (true) {
                    node++;
                    int type2 = SAX2DTM2.this._type2(node);
                    if (-1 != type2 && isDescendant(node)) {
                        if (2 != type2 && 3 != type2 && 13 != type2) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                this._currentNode = -1;
                return -1;
            }
            while (true) {
                node++;
                int eType = SAX2DTM2.this._exptype2(node);
                if (-1 == eType) {
                    this._currentNode = -1;
                    return -1;
                }
                if (eType != 3 && (type = SAX2DTM2.this.m_extendedTypes[eType].getNodeType()) != 2 && type != 13) {
                    break;
                }
            }
            this._currentNode = node;
            return returnNode(SAX2DTM2.this.makeNodeHandle(node));
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            boolean temp = this._isRestartable;
            this._isRestartable = true;
            setStartNode(SAX2DTM2.this.makeNodeHandle(this._startNode));
            this._isRestartable = temp;
            return this;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedDescendantIterator.class */
    public final class TypedDescendantIterator extends DescendantIterator {
        private final int _nodeType;

        public TypedDescendantIterator(int nodeType) {
            super();
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.DescendantIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int expType;
            int startNode = this._startNode;
            if (this._startNode == -1) {
                return -1;
            }
            int node = this._currentNode;
            int nodeType = this._nodeType;
            if (nodeType != 1) {
                do {
                    node++;
                    expType = SAX2DTM2.this._exptype2(node);
                    if (-1 == expType || (SAX2DTM2.this._parent2(node) < startNode && startNode != node)) {
                        this._currentNode = -1;
                        return -1;
                    }
                } while (expType != nodeType);
            } else {
                if (startNode != 0) {
                    while (true) {
                        node++;
                        int expType2 = SAX2DTM2.this._exptype2(node);
                        if (-1 != expType2 && (SAX2DTM2.this._parent2(node) >= startNode || startNode == node)) {
                            if (expType2 >= 14 && SAX2DTM2.this.m_extendedTypes[expType2].getNodeType() == 1) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    this._currentNode = -1;
                    return -1;
                }
                while (true) {
                    node++;
                    int expType3 = SAX2DTM2.this._exptype2(node);
                    if (-1 == expType3) {
                        this._currentNode = -1;
                        return -1;
                    }
                    if (expType3 >= 14 && SAX2DTM2.this.m_extendedTypes[expType3].getNodeType() == 1) {
                        break;
                    }
                }
            }
            this._currentNode = node;
            return returnNode(SAX2DTM2.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/sax2dtm/SAX2DTM2$TypedSingletonIterator.class */
    public final class TypedSingletonIterator extends DTMDefaultBaseIterators.SingletonIterator {
        private final int _nodeType;

        public TypedSingletonIterator(int nodeType) {
            super(SAX2DTM2.this);
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.SingletonIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int result = this._currentNode;
            if (result == -1) {
                return -1;
            }
            this._currentNode = -1;
            if (this._nodeType >= 14) {
                if (SAX2DTM2.this._exptype2(SAX2DTM2.this.makeNodeIdentity(result)) == this._nodeType) {
                    return returnNode(result);
                }
                return -1;
            }
            if (SAX2DTM2.this._type2(SAX2DTM2.this.makeNodeIdentity(result)) == this._nodeType) {
                return returnNode(result);
            }
            return -1;
        }
    }

    public SAX2DTM2(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
        this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, true, false);
    }

    public SAX2DTM2(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean buildIdIndex, boolean newNameTable) {
        super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
        this.m_valueIndex = 0;
        this.m_buildIdIndex = true;
        int shift = 0;
        while (true) {
            int i2 = blocksize >>> 1;
            blocksize = i2;
            if (i2 == 0) {
                this.m_blocksize = 1 << shift;
                this.m_SHIFT = shift;
                this.m_MASK = this.m_blocksize - 1;
                this.m_buildIdIndex = buildIdIndex;
                this.m_values = new Vector(32, 512);
                this.m_maxNodeIndex = 65536;
                this.m_exptype_map0 = this.m_exptype.getMap0();
                this.m_nextsib_map0 = this.m_nextsib.getMap0();
                this.m_firstch_map0 = this.m_firstch.getMap0();
                this.m_parent_map0 = this.m_parent.getMap0();
                return;
            }
            shift++;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    public final int _exptype(int identity) {
        return this.m_exptype.elementAt(identity);
    }

    public final int _exptype2(int identity) {
        if (identity < this.m_blocksize) {
            return this.m_exptype_map0[identity];
        }
        return this.m_exptype_map[identity >>> this.m_SHIFT][identity & this.m_MASK];
    }

    public final int _nextsib2(int identity) {
        if (identity < this.m_blocksize) {
            return this.m_nextsib_map0[identity];
        }
        return this.m_nextsib_map[identity >>> this.m_SHIFT][identity & this.m_MASK];
    }

    public final int _firstch2(int identity) {
        if (identity < this.m_blocksize) {
            return this.m_firstch_map0[identity];
        }
        return this.m_firstch_map[identity >>> this.m_SHIFT][identity & this.m_MASK];
    }

    public final int _parent2(int identity) {
        if (identity < this.m_blocksize) {
            return this.m_parent_map0[identity];
        }
        return this.m_parent_map[identity >>> this.m_SHIFT][identity & this.m_MASK];
    }

    public final int _type2(int identity) {
        int eType;
        if (identity < this.m_blocksize) {
            eType = this.m_exptype_map0[identity];
        } else {
            eType = this.m_exptype_map[identity >>> this.m_SHIFT][identity & this.m_MASK];
        }
        if (-1 != eType) {
            return this.m_extendedTypes[eType].getNodeType();
        }
        return -1;
    }

    public final int getExpandedTypeID2(int nodeHandle) {
        int nodeID = makeNodeIdentity(nodeHandle);
        if (nodeID != -1) {
            if (nodeID < this.m_blocksize) {
                return this.m_exptype_map0[nodeID];
            }
            return this.m_exptype_map[nodeID >>> this.m_SHIFT][nodeID & this.m_MASK];
        }
        return -1;
    }

    public final int _exptype2Type(int exptype) {
        if (-1 != exptype) {
            return this.m_extendedTypes[exptype].getNodeType();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM
    public int getIdForNamespace(String uri) {
        int index = this.m_values.indexOf(uri);
        if (index < 0) {
            this.m_values.addElement(uri);
            int i2 = this.m_valueIndex;
            this.m_valueIndex = i2 + 1;
            return i2;
        }
        return index;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        boolean shouldStripWhitespace;
        int nodeType;
        charactersFlush();
        int exName = this.m_expandedNameTable.getExpandedTypeID(uri, localName, 1);
        int prefixIndex = qName.length() != localName.length() ? this.m_valuesOrPrefixes.stringToIndex(qName) : 0;
        int elemNode = addNode(1, exName, this.m_parents.peek(), this.m_previous, prefixIndex, true);
        if (this.m_indexing) {
            indexNode(exName, elemNode);
        }
        this.m_parents.push(elemNode);
        int startDecls = this.m_contextIndexes.peek();
        int nDecls = this.m_prefixMappings.size();
        if (!this.m_pastFirstElement) {
            int exName2 = this.m_expandedNameTable.getExpandedTypeID(null, "xml", 13);
            this.m_values.addElement("http://www.w3.org/XML/1998/namespace");
            int val = this.m_valueIndex;
            this.m_valueIndex = val + 1;
            addNode(13, exName2, elemNode, -1, val, false);
            this.m_pastFirstElement = true;
        }
        for (int i2 = startDecls; i2 < nDecls; i2 += 2) {
            String prefix = (String) this.m_prefixMappings.elementAt(i2);
            if (prefix != null) {
                String declURL = (String) this.m_prefixMappings.elementAt(i2 + 1);
                int exName3 = this.m_expandedNameTable.getExpandedTypeID(null, prefix, 13);
                this.m_values.addElement(declURL);
                int val2 = this.m_valueIndex;
                this.m_valueIndex = val2 + 1;
                addNode(13, exName3, elemNode, -1, val2, false);
            }
        }
        int n2 = attributes.getLength();
        for (int i3 = 0; i3 < n2; i3++) {
            String attrUri = attributes.getURI(i3);
            String attrQName = attributes.getQName(i3);
            String valString = attributes.getValue(i3);
            String attrLocalName = attributes.getLocalName(i3);
            if (null != attrQName && (attrQName.equals("xmlns") || attrQName.startsWith("xmlns:"))) {
                if (!declAlreadyDeclared(getPrefix(attrQName, attrUri))) {
                    nodeType = 13;
                }
            } else {
                nodeType = 2;
                if (this.m_buildIdIndex && attributes.getType(i3).equalsIgnoreCase("ID")) {
                    setIDAttribute(valString, elemNode);
                }
            }
            if (null == valString) {
                valString = "";
            }
            this.m_values.addElement(valString);
            int i4 = this.m_valueIndex;
            this.m_valueIndex = i4 + 1;
            int val3 = i4;
            if (attrLocalName.length() != attrQName.length()) {
                int prefixIndex2 = this.m_valuesOrPrefixes.stringToIndex(attrQName);
                int dataIndex = this.m_data.size();
                this.m_data.addElement(prefixIndex2);
                this.m_data.addElement(val3);
                val3 = -dataIndex;
            }
            addNode(nodeType, this.m_expandedNameTable.getExpandedTypeID(attrUri, attrLocalName, nodeType), elemNode, -1, val3, false);
        }
        if (null != this.m_wsfilter) {
            short wsv = this.m_wsfilter.getShouldStripSpace(makeNodeHandle(elemNode), this);
            if (3 == wsv) {
                shouldStripWhitespace = getShouldStripWhitespace();
            } else {
                shouldStripWhitespace = 2 == wsv;
            }
            boolean shouldStrip = shouldStripWhitespace;
            pushShouldStripWhitespace(shouldStrip);
        }
        this.m_previous = -1;
        this.m_contextIndexes.push(this.m_prefixMappings.size());
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        charactersFlush();
        this.m_contextIndexes.quickPop(1);
        int topContextIndex = this.m_contextIndexes.peek();
        if (topContextIndex != this.m_prefixMappings.size()) {
            this.m_prefixMappings.setSize(topContextIndex);
        }
        this.m_previous = this.m_parents.pop();
        popShouldStripWhitespace();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.m_insideDTD) {
            return;
        }
        charactersFlush();
        this.m_values.addElement(new String(ch, start, length));
        int dataIndex = this.m_valueIndex;
        this.m_valueIndex = dataIndex + 1;
        this.m_previous = addNode(8, 8, this.m_parents.peek(), this.m_previous, dataIndex, false);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        int doc = addNode(9, 9, -1, -1, 0, true);
        this.m_parents.push(doc);
        this.m_previous = -1;
        this.m_contextIndexes.push(this.m_prefixMappings.size());
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        super.endDocument();
        this.m_exptype.addElement(-1);
        this.m_parent.addElement(-1);
        this.m_nextsib.addElement(-1);
        this.m_firstch.addElement(-1);
        this.m_extendedTypes = this.m_expandedNameTable.getExtendedTypes();
        this.m_exptype_map = this.m_exptype.getMap();
        this.m_nextsib_map = this.m_nextsib.getMap();
        this.m_firstch_map = this.m_firstch.getMap();
        this.m_parent_map = this.m_parent.getMap();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM
    protected final int addNode(int type, int expandedTypeID, int parentIndex, int previousSibling, int dataOrPrefix, boolean canHaveFirstChild) {
        int nodeIndex = this.m_size;
        this.m_size = nodeIndex + 1;
        if (nodeIndex == this.m_maxNodeIndex) {
            addNewDTMID(nodeIndex);
            this.m_maxNodeIndex += 65536;
        }
        this.m_firstch.addElement(-1);
        this.m_nextsib.addElement(-1);
        this.m_parent.addElement(parentIndex);
        this.m_exptype.addElement(expandedTypeID);
        this.m_dataOrQName.addElement(dataOrPrefix);
        if (this.m_prevsib != null) {
            this.m_prevsib.addElement(previousSibling);
        }
        if (this.m_locator != null && this.m_useSourceLocationProperty) {
            setSourceLocation();
        }
        switch (type) {
            case 2:
                break;
            case 13:
                declareNamespaceInContext(parentIndex, nodeIndex);
                break;
            default:
                if (-1 != previousSibling) {
                    this.m_nextsib.setElementAt(nodeIndex, previousSibling);
                    break;
                } else if (-1 != parentIndex) {
                    this.m_firstch.setElementAt(nodeIndex, parentIndex);
                    break;
                }
                break;
        }
        return nodeIndex;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM
    protected final void charactersFlush() {
        if (this.m_textPendingStart >= 0) {
            int length = this.m_chars.size() - this.m_textPendingStart;
            boolean doStrip = false;
            if (getShouldStripWhitespace()) {
                doStrip = this.m_chars.isWhitespace(this.m_textPendingStart, length);
            }
            if (doStrip) {
                this.m_chars.setLength(this.m_textPendingStart);
            } else if (length > 0) {
                if (length <= 1023 && this.m_textPendingStart <= 2097151) {
                    this.m_previous = addNode(this.m_coalescedTextType, 3, this.m_parents.peek(), this.m_previous, length + (this.m_textPendingStart << 10), false);
                } else {
                    int dataIndex = this.m_data.size();
                    this.m_previous = addNode(this.m_coalescedTextType, 3, this.m_parents.peek(), this.m_previous, -dataIndex, false);
                    this.m_data.addElement(this.m_textPendingStart);
                    this.m_data.addElement(length);
                }
            }
            this.m_textPendingStart = -1;
            this.m_coalescedTextType = 3;
            this.m_textType = 3;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        charactersFlush();
        int dataIndex = this.m_data.size();
        this.m_previous = addNode(7, 7, this.m_parents.peek(), this.m_previous, -dataIndex, false);
        this.m_data.addElement(this.m_valuesOrPrefixes.stringToIndex(target));
        this.m_values.addElement(data);
        SuballocatedIntVector suballocatedIntVector = this.m_data;
        int i2 = this.m_valueIndex;
        this.m_valueIndex = i2 + 1;
        suballocatedIntVector.addElement(i2);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public final int getFirstAttribute(int nodeHandle) {
        int type;
        int nodeID = makeNodeIdentity(nodeHandle);
        if (nodeID != -1 && 1 == _type2(nodeID)) {
            do {
                nodeID++;
                type = _type2(nodeID);
                if (type == 2) {
                    return makeNodeHandle(nodeID);
                }
            } while (13 == type);
            return -1;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    protected int getFirstAttributeIdentity(int identity) {
        int type;
        if (identity != -1 && 1 == _type2(identity)) {
            do {
                identity++;
                type = _type2(identity);
                if (type == 2) {
                    return identity;
                }
            } while (13 == type);
            return -1;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    protected int getNextAttributeIdentity(int identity) {
        int type;
        do {
            identity++;
            type = _type2(identity);
            if (type == 2) {
                return identity;
            }
        } while (type == 13);
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
    protected final int getTypedAttribute(int nodeHandle, int attType) {
        int nodeID = makeNodeIdentity(nodeHandle);
        if (nodeID == -1 || 1 != _type2(nodeID)) {
            return -1;
        }
        while (true) {
            nodeID++;
            int expType = _exptype2(nodeID);
            if (expType != -1) {
                int type = this.m_extendedTypes[expType].getNodeType();
                if (type == 2) {
                    if (expType == attType) {
                        return makeNodeHandle(nodeID);
                    }
                } else if (13 != type) {
                    return -1;
                }
            } else {
                return -1;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getLocalName(int nodeHandle) {
        int expType = _exptype(makeNodeIdentity(nodeHandle));
        if (expType == 7) {
            int dataIndex = _dataOrQName(makeNodeIdentity(nodeHandle));
            return this.m_valuesOrPrefixes.indexToString(this.m_data.elementAt(-dataIndex));
        }
        return this.m_expandedNameTable.getLocalName(expType);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public final String getNodeNameX(int nodeHandle) {
        int nodeID = makeNodeIdentity(nodeHandle);
        int eType = _exptype2(nodeID);
        if (eType == 7) {
            int dataIndex = _dataOrQName(nodeID);
            return this.m_valuesOrPrefixes.indexToString(this.m_data.elementAt(-dataIndex));
        }
        ExtendedType extType = this.m_extendedTypes[eType];
        if (extType.getNamespace().length() == 0) {
            return extType.getLocalName();
        }
        int qnameIndex = this.m_dataOrQName.elementAt(nodeID);
        if (qnameIndex == 0) {
            return extType.getLocalName();
        }
        if (qnameIndex < 0) {
            qnameIndex = this.m_data.elementAt(-qnameIndex);
        }
        return this.m_valuesOrPrefixes.indexToString(qnameIndex);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeName(int nodeHandle) {
        int nodeID = makeNodeIdentity(nodeHandle);
        int eType = _exptype2(nodeID);
        ExtendedType extType = this.m_extendedTypes[eType];
        if (extType.getNamespace().length() == 0) {
            int type = extType.getNodeType();
            String localName = extType.getLocalName();
            if (type == 13) {
                if (localName.length() == 0) {
                    return "xmlns";
                }
                return "xmlns:" + localName;
            }
            if (type == 7) {
                int dataIndex = _dataOrQName(nodeID);
                return this.m_valuesOrPrefixes.indexToString(this.m_data.elementAt(-dataIndex));
            }
            if (localName.length() == 0) {
                return getFixedNames(type);
            }
            return localName;
        }
        int qnameIndex = this.m_dataOrQName.elementAt(nodeID);
        if (qnameIndex == 0) {
            return extType.getLocalName();
        }
        if (qnameIndex < 0) {
            qnameIndex = this.m_data.elementAt(-qnameIndex);
        }
        return this.m_valuesOrPrefixes.indexToString(qnameIndex);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public XMLString getStringValue(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        if (identity == -1) {
            return EMPTY_XML_STR;
        }
        int type = _type2(identity);
        if (type == 1 || type == 9) {
            int identity2 = _firstch2(identity);
            if (-1 != identity2) {
                int offset = -1;
                int length = 0;
                do {
                    int type2 = _exptype2(identity2);
                    if (type2 == 3 || type2 == 4) {
                        int dataIndex = this.m_dataOrQName.elementAt(identity2);
                        if (dataIndex >= 0) {
                            if (-1 == offset) {
                                offset = dataIndex >>> 10;
                            }
                            length += dataIndex & 1023;
                        } else {
                            if (-1 == offset) {
                                offset = this.m_data.elementAt(-dataIndex);
                            }
                            length += this.m_data.elementAt((-dataIndex) + 1);
                        }
                    }
                    identity2++;
                } while (_parent2(identity2) >= identity);
                if (length > 0) {
                    if (this.m_xstrf != null) {
                        return this.m_xstrf.newstr(this.m_chars, offset, length);
                    }
                    return new XMLStringDefault(this.m_chars.getString(offset, length));
                }
                return EMPTY_XML_STR;
            }
            return EMPTY_XML_STR;
        }
        if (3 == type || 4 == type) {
            int dataIndex2 = this.m_dataOrQName.elementAt(identity);
            if (dataIndex2 >= 0) {
                if (this.m_xstrf != null) {
                    return this.m_xstrf.newstr(this.m_chars, dataIndex2 >>> 10, dataIndex2 & 1023);
                }
                return new XMLStringDefault(this.m_chars.getString(dataIndex2 >>> 10, dataIndex2 & 1023));
            }
            if (this.m_xstrf != null) {
                return this.m_xstrf.newstr(this.m_chars, this.m_data.elementAt(-dataIndex2), this.m_data.elementAt((-dataIndex2) + 1));
            }
            return new XMLStringDefault(this.m_chars.getString(this.m_data.elementAt(-dataIndex2), this.m_data.elementAt((-dataIndex2) + 1)));
        }
        int dataIndex3 = this.m_dataOrQName.elementAt(identity);
        if (dataIndex3 < 0) {
            dataIndex3 = this.m_data.elementAt((-dataIndex3) + 1);
        }
        if (this.m_xstrf != null) {
            return this.m_xstrf.newstr((String) this.m_values.elementAt(dataIndex3));
        }
        return new XMLStringDefault((String) this.m_values.elementAt(dataIndex3));
    }

    public final String getStringValueX(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        if (identity == -1) {
            return "";
        }
        int type = _type2(identity);
        if (type == 1 || type == 9) {
            int identity2 = _firstch2(identity);
            if (-1 != identity2) {
                int offset = -1;
                int length = 0;
                do {
                    int type2 = _exptype2(identity2);
                    if (type2 == 3 || type2 == 4) {
                        int dataIndex = this.m_dataOrQName.elementAt(identity2);
                        if (dataIndex >= 0) {
                            if (-1 == offset) {
                                offset = dataIndex >>> 10;
                            }
                            length += dataIndex & 1023;
                        } else {
                            if (-1 == offset) {
                                offset = this.m_data.elementAt(-dataIndex);
                            }
                            length += this.m_data.elementAt((-dataIndex) + 1);
                        }
                    }
                    identity2++;
                } while (_parent2(identity2) >= identity);
                if (length > 0) {
                    return this.m_chars.getString(offset, length);
                }
                return "";
            }
            return "";
        }
        if (3 == type || 4 == type) {
            int dataIndex2 = this.m_dataOrQName.elementAt(identity);
            if (dataIndex2 >= 0) {
                return this.m_chars.getString(dataIndex2 >>> 10, dataIndex2 & 1023);
            }
            return this.m_chars.getString(this.m_data.elementAt(-dataIndex2), this.m_data.elementAt((-dataIndex2) + 1));
        }
        int dataIndex3 = this.m_dataOrQName.elementAt(identity);
        if (dataIndex3 < 0) {
            dataIndex3 = this.m_data.elementAt((-dataIndex3) + 1);
        }
        return (String) this.m_values.elementAt(dataIndex3);
    }

    public String getStringValue() {
        int child = _firstch2(0);
        if (child == -1) {
            return "";
        }
        if (_exptype2(child) == 3 && _nextsib2(child) == -1) {
            int dataIndex = this.m_dataOrQName.elementAt(child);
            if (dataIndex >= 0) {
                return this.m_chars.getString(dataIndex >>> 10, dataIndex & 1023);
            }
            return this.m_chars.getString(this.m_data.elementAt(-dataIndex), this.m_data.elementAt((-dataIndex) + 1));
        }
        return getStringValueX(getDocument());
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public final void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException {
        int identity = makeNodeIdentity(nodeHandle);
        if (identity == -1) {
            return;
        }
        int type = _type2(identity);
        if (type == 1 || type == 9) {
            int identity2 = _firstch2(identity);
            if (-1 != identity2) {
                int offset = -1;
                int length = 0;
                do {
                    int type2 = _exptype2(identity2);
                    if (type2 == 3 || type2 == 4) {
                        int dataIndex = this.m_dataOrQName.elementAt(identity2);
                        if (dataIndex >= 0) {
                            if (-1 == offset) {
                                offset = dataIndex >>> 10;
                            }
                            length += dataIndex & 1023;
                        } else {
                            if (-1 == offset) {
                                offset = this.m_data.elementAt(-dataIndex);
                            }
                            length += this.m_data.elementAt((-dataIndex) + 1);
                        }
                    }
                    identity2++;
                } while (_parent2(identity2) >= identity);
                if (length > 0) {
                    if (normalize) {
                        this.m_chars.sendNormalizedSAXcharacters(ch, offset, length);
                        return;
                    } else {
                        this.m_chars.sendSAXcharacters(ch, offset, length);
                        return;
                    }
                }
                return;
            }
            return;
        }
        if (3 == type || 4 == type) {
            int dataIndex2 = this.m_dataOrQName.elementAt(identity);
            if (dataIndex2 >= 0) {
                if (normalize) {
                    this.m_chars.sendNormalizedSAXcharacters(ch, dataIndex2 >>> 10, dataIndex2 & 1023);
                    return;
                } else {
                    this.m_chars.sendSAXcharacters(ch, dataIndex2 >>> 10, dataIndex2 & 1023);
                    return;
                }
            }
            if (normalize) {
                this.m_chars.sendNormalizedSAXcharacters(ch, this.m_data.elementAt(-dataIndex2), this.m_data.elementAt((-dataIndex2) + 1));
                return;
            } else {
                this.m_chars.sendSAXcharacters(ch, this.m_data.elementAt(-dataIndex2), this.m_data.elementAt((-dataIndex2) + 1));
                return;
            }
        }
        int dataIndex3 = this.m_dataOrQName.elementAt(identity);
        if (dataIndex3 < 0) {
            dataIndex3 = this.m_data.elementAt((-dataIndex3) + 1);
        }
        String str = (String) this.m_values.elementAt(dataIndex3);
        if (normalize) {
            FastStringBuffer.sendNormalizedSAXcharacters(str.toCharArray(), 0, str.length(), ch);
        } else {
            ch.characters(str.toCharArray(), 0, str.length());
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase, com.sun.org.apache.xml.internal.dtm.DTM
    public String getNodeValue(int nodeHandle) {
        int identity = makeNodeIdentity(nodeHandle);
        int type = _type2(identity);
        if (type == 3 || type == 4) {
            int dataIndex = _dataOrQName(identity);
            if (dataIndex > 0) {
                return this.m_chars.getString(dataIndex >>> 10, dataIndex & 1023);
            }
            return this.m_chars.getString(this.m_data.elementAt(-dataIndex), this.m_data.elementAt((-dataIndex) + 1));
        }
        if (1 == type || 11 == type || 9 == type) {
            return null;
        }
        int dataIndex2 = this.m_dataOrQName.elementAt(identity);
        if (dataIndex2 < 0) {
            dataIndex2 = this.m_data.elementAt((-dataIndex2) + 1);
        }
        return (String) this.m_values.elementAt(dataIndex2);
    }

    protected final void copyTextNode(int nodeID, SerializationHandler handler) throws SAXException {
        if (nodeID != -1) {
            int dataIndex = this.m_dataOrQName.elementAt(nodeID);
            if (dataIndex >= 0) {
                this.m_chars.sendSAXcharacters(handler, dataIndex >>> 10, dataIndex & 1023);
            } else {
                this.m_chars.sendSAXcharacters(handler, this.m_data.elementAt(-dataIndex), this.m_data.elementAt((-dataIndex) + 1));
            }
        }
    }

    protected final String copyElement(int nodeID, int exptype, SerializationHandler handler) throws SAXException, ArrayIndexOutOfBoundsException {
        String prefix;
        ExtendedType extType = this.m_extendedTypes[exptype];
        String uri = extType.getNamespace();
        String name = extType.getLocalName();
        if (uri.length() == 0) {
            handler.startElement(name);
            return name;
        }
        int qnameIndex = this.m_dataOrQName.elementAt(nodeID);
        if (qnameIndex == 0) {
            handler.startElement(name);
            handler.namespaceAfterStartElement("", uri);
            return name;
        }
        if (qnameIndex < 0) {
            qnameIndex = this.m_data.elementAt(-qnameIndex);
        }
        String qName = this.m_valuesOrPrefixes.indexToString(qnameIndex);
        handler.startElement(qName);
        int prefixIndex = qName.indexOf(58);
        if (prefixIndex > 0) {
            prefix = qName.substring(0, prefixIndex);
        } else {
            prefix = null;
        }
        handler.namespaceAfterStartElement(prefix, uri);
        return qName;
    }

    protected final void copyNS(int nodeID, SerializationHandler handler, boolean inScope) throws SAXException {
        int nextNSNode;
        if (this.m_namespaceDeclSetElements != null && this.m_namespaceDeclSetElements.size() == 1 && this.m_namespaceDeclSets != null && ((SuballocatedIntVector) this.m_namespaceDeclSets.elementAt(0)).size() == 1) {
            return;
        }
        SuballocatedIntVector nsContext = null;
        if (inScope) {
            nsContext = findNamespaceContext(nodeID);
            if (nsContext == null || nsContext.size() < 1) {
                return;
            } else {
                nextNSNode = makeNodeIdentity(nsContext.elementAt(0));
            }
        } else {
            nextNSNode = getNextNamespaceNode2(nodeID);
        }
        int nsIndex = 1;
        while (nextNSNode != -1) {
            int eType = _exptype2(nextNSNode);
            String nodeName = this.m_extendedTypes[eType].getLocalName();
            int dataIndex = this.m_dataOrQName.elementAt(nextNSNode);
            if (dataIndex < 0) {
                dataIndex = this.m_data.elementAt((-dataIndex) + 1);
            }
            String nodeValue = (String) this.m_values.elementAt(dataIndex);
            handler.namespaceAfterStartElement(nodeName, nodeValue);
            if (inScope) {
                if (nsIndex < nsContext.size()) {
                    nextNSNode = makeNodeIdentity(nsContext.elementAt(nsIndex));
                    nsIndex++;
                } else {
                    return;
                }
            } else {
                nextNSNode = getNextNamespaceNode2(nextNSNode);
            }
        }
    }

    protected final int getNextNamespaceNode2(int baseID) {
        int type;
        do {
            baseID++;
            type = _type2(baseID);
        } while (type == 2);
        if (type == 13) {
            return baseID;
        }
        return -1;
    }

    protected final void copyAttributes(int nodeID, SerializationHandler handler) throws SAXException, ArrayIndexOutOfBoundsException {
        int firstAttributeIdentity = getFirstAttributeIdentity(nodeID);
        while (true) {
            int current = firstAttributeIdentity;
            if (current != -1) {
                int eType = _exptype2(current);
                copyAttribute(current, eType, handler);
                firstAttributeIdentity = getNextAttributeIdentity(current);
            } else {
                return;
            }
        }
    }

    protected final void copyAttribute(int nodeID, int exptype, SerializationHandler handler) throws SAXException, ArrayIndexOutOfBoundsException {
        ExtendedType extType = this.m_extendedTypes[exptype];
        String uri = extType.getNamespace();
        String localName = extType.getLocalName();
        String prefix = null;
        String qname = null;
        int dataIndex = _dataOrQName(nodeID);
        int valueIndex = dataIndex;
        if (dataIndex <= 0) {
            int prefixIndex = this.m_data.elementAt(-dataIndex);
            valueIndex = this.m_data.elementAt((-dataIndex) + 1);
            qname = this.m_valuesOrPrefixes.indexToString(prefixIndex);
            int colonIndex = qname.indexOf(58);
            if (colonIndex > 0) {
                prefix = qname.substring(0, colonIndex);
            }
        }
        if (uri.length() != 0) {
            handler.namespaceAfterStartElement(prefix, uri);
        }
        String nodeName = prefix != null ? qname : localName;
        String nodeValue = (String) this.m_values.elementAt(valueIndex);
        handler.addAttribute(uri, localName, nodeName, "CDATA", nodeValue);
    }
}
