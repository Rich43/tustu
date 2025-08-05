package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.Axis;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xml.internal.dtm.DTMException;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.utils.NodeVector;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import javax.xml.transform.Source;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators.class */
public abstract class DTMDefaultBaseIterators extends DTMDefaultBaseTraversers {
    public DTMDefaultBaseIterators(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
        super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
    }

    public DTMDefaultBaseIterators(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable) {
        super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
        DTMAxisIterator iterator;
        switch (axis) {
            case 0:
                return new TypedAncestorIterator(type);
            case 1:
                return new TypedAncestorIterator(type).includeSelf();
            case 2:
                return new TypedAttributeIterator(type);
            case 3:
                iterator = new TypedChildrenIterator(type);
                break;
            case 4:
                iterator = new TypedDescendantIterator(type);
                break;
            case 5:
                iterator = new TypedDescendantIterator(type).includeSelf();
                break;
            case 6:
                iterator = new TypedFollowingIterator(type);
                break;
            case 7:
                iterator = new TypedFollowingSiblingIterator(type);
                break;
            case 8:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            default:
                throw new DTMException(XMLMessages.createXMLMessage("ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", new Object[]{Axis.getNames(axis)}));
            case 9:
                iterator = new TypedNamespaceIterator(type);
                break;
            case 10:
                return new ParentIterator().setNodeType(type);
            case 11:
                iterator = new TypedPrecedingIterator(type);
                break;
            case 12:
                iterator = new TypedPrecedingSiblingIterator(type);
                break;
            case 13:
                iterator = new TypedSingletonIterator(type);
                break;
            case 19:
                iterator = new TypedRootIterator(type);
                break;
        }
        return iterator;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisIterator getAxisIterator(int axis) {
        DTMAxisIterator iterator;
        switch (axis) {
            case 0:
                return new AncestorIterator();
            case 1:
                return new AncestorIterator().includeSelf();
            case 2:
                return new AttributeIterator();
            case 3:
                iterator = new ChildrenIterator();
                break;
            case 4:
                iterator = new DescendantIterator();
                break;
            case 5:
                iterator = new DescendantIterator().includeSelf();
                break;
            case 6:
                iterator = new FollowingIterator();
                break;
            case 7:
                iterator = new FollowingSiblingIterator();
                break;
            case 8:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            default:
                throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_AXIS_NOT_IMPLEMENTED", new Object[]{Axis.getNames(axis)}));
            case 9:
                iterator = new NamespaceIterator();
                break;
            case 10:
                return new ParentIterator();
            case 11:
                iterator = new PrecedingIterator();
                break;
            case 12:
                iterator = new PrecedingSiblingIterator();
                break;
            case 13:
                iterator = new SingletonIterator(this);
                break;
            case 19:
                iterator = new RootIterator();
                break;
        }
        return iterator;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$InternalAxisIteratorBase.class */
    public abstract class InternalAxisIteratorBase extends DTMAxisIteratorBase {
        protected int _currentNode;

        public InternalAxisIteratorBase() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            this._markedNode = this._currentNode;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            this._currentNode = this._markedNode;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$ChildrenIterator.class */
    public final class ChildrenIterator extends InternalAxisIteratorBase {
        public ChildrenIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = node == -1 ? -1 : DTMDefaultBaseIterators.this._firstch(DTMDefaultBaseIterators.this.makeNodeIdentity(node));
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._currentNode != -1) {
                int node = this._currentNode;
                this._currentNode = DTMDefaultBaseIterators.this._nextsib(node);
                return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$ParentIterator.class */
    public final class ParentIterator extends InternalAxisIteratorBase {
        private int _nodeType;

        public ParentIterator() {
            super();
            this._nodeType = -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = DTMDefaultBaseIterators.this.getParent(node);
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
            if (this._nodeType >= 14) {
                if (this._nodeType != DTMDefaultBaseIterators.this.getExpandedTypeID(this._currentNode)) {
                    result = -1;
                }
            } else if (this._nodeType != -1 && this._nodeType != DTMDefaultBaseIterators.this.getNodeType(this._currentNode)) {
                result = -1;
            }
            this._currentNode = -1;
            return returnNode(result);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedChildrenIterator.class */
    public final class TypedChildrenIterator extends InternalAxisIteratorBase {
        private final int _nodeType;

        public TypedChildrenIterator(int nodeType) {
            super();
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = node == -1 ? -1 : DTMDefaultBaseIterators.this._firstch(DTMDefaultBaseIterators.this.makeNodeIdentity(this._startNode));
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            int nodeType = this._nodeType;
            if (nodeType >= 14) {
                while (node != -1 && DTMDefaultBaseIterators.this._exptype(node) != nodeType) {
                    node = DTMDefaultBaseIterators.this._nextsib(node);
                }
            } else {
                while (node != -1) {
                    int eType = DTMDefaultBaseIterators.this._exptype(node);
                    if (eType < 14) {
                        if (eType == nodeType) {
                            break;
                        }
                        node = DTMDefaultBaseIterators.this._nextsib(node);
                    } else {
                        if (DTMDefaultBaseIterators.this.m_expandedNameTable.getType(eType) == nodeType) {
                            break;
                        }
                        node = DTMDefaultBaseIterators.this._nextsib(node);
                    }
                }
            }
            if (node == -1) {
                this._currentNode = -1;
                return -1;
            }
            this._currentNode = DTMDefaultBaseIterators.this._nextsib(node);
            return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$NamespaceChildrenIterator.class */
    public final class NamespaceChildrenIterator extends InternalAxisIteratorBase {
        private final int _nsType;

        public NamespaceChildrenIterator(int type) {
            super();
            this._nsType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = node == -1 ? -1 : -2;
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int i_nextsib;
            if (this._currentNode != -1) {
                if (-2 == this._currentNode) {
                    i_nextsib = DTMDefaultBaseIterators.this._firstch(DTMDefaultBaseIterators.this.makeNodeIdentity(this._startNode));
                } else {
                    i_nextsib = DTMDefaultBaseIterators.this._nextsib(this._currentNode);
                }
                while (true) {
                    int node = i_nextsib;
                    if (node != -1) {
                        if (DTMDefaultBaseIterators.this.m_expandedNameTable.getNamespaceID(DTMDefaultBaseIterators.this._exptype(node)) != this._nsType) {
                            i_nextsib = DTMDefaultBaseIterators.this._nextsib(node);
                        } else {
                            this._currentNode = node;
                            return returnNode(node);
                        }
                    } else {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$NamespaceIterator.class */
    public class NamespaceIterator extends InternalAxisIteratorBase {
        public NamespaceIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = DTMDefaultBaseIterators.this.getFirstNamespaceNode(node, true);
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            if (-1 != node) {
                this._currentNode = DTMDefaultBaseIterators.this.getNextNamespaceNode(this._startNode, node, true);
            }
            return returnNode(node);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedNamespaceIterator.class */
    public class TypedNamespaceIterator extends NamespaceIterator {
        private final int _nodeType;

        public TypedNamespaceIterator(int nodeType) {
            super();
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.NamespaceIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node;
            int nextNamespaceNode = this._currentNode;
            while (true) {
                node = nextNamespaceNode;
                if (node != -1) {
                    if (DTMDefaultBaseIterators.this.getExpandedTypeID(node) == this._nodeType || DTMDefaultBaseIterators.this.getNodeType(node) == this._nodeType || DTMDefaultBaseIterators.this.getNamespaceType(node) == this._nodeType) {
                        break;
                    }
                    nextNamespaceNode = DTMDefaultBaseIterators.this.getNextNamespaceNode(this._startNode, node, true);
                } else {
                    this._currentNode = -1;
                    return -1;
                }
            }
            this._currentNode = node;
            return returnNode(node);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$RootIterator.class */
    public class RootIterator extends InternalAxisIteratorBase {
        public RootIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (this._isRestartable) {
                this._startNode = DTMDefaultBaseIterators.this.getDocumentRoot(node);
                this._currentNode = -1;
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._startNode == this._currentNode) {
                return -1;
            }
            this._currentNode = this._startNode;
            return returnNode(this._startNode);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedRootIterator.class */
    public class TypedRootIterator extends RootIterator {
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
            int nodeType = this._nodeType;
            int node = this._startNode;
            int expType = DTMDefaultBaseIterators.this.getExpandedTypeID(node);
            this._currentNode = node;
            if (nodeType >= 14) {
                if (nodeType == expType) {
                    return returnNode(node);
                }
                return -1;
            }
            if (expType < 14) {
                if (expType == nodeType) {
                    return returnNode(node);
                }
                return -1;
            }
            if (DTMDefaultBaseIterators.this.m_expandedNameTable.getType(expType) == nodeType) {
                return returnNode(node);
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$NamespaceAttributeIterator.class */
    public final class NamespaceAttributeIterator extends InternalAxisIteratorBase {
        private final int _nsType;

        public NamespaceAttributeIterator(int nsType) {
            super();
            this._nsType = nsType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = DTMDefaultBaseIterators.this.getFirstNamespaceNode(node, false);
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            if (-1 != node) {
                this._currentNode = DTMDefaultBaseIterators.this.getNextNamespaceNode(this._startNode, node, false);
            }
            return returnNode(node);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$FollowingSiblingIterator.class */
    public class FollowingSiblingIterator extends InternalAxisIteratorBase {
        public FollowingSiblingIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            this._currentNode = this._currentNode == -1 ? -1 : DTMDefaultBaseIterators.this._nextsib(this._currentNode);
            return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(this._currentNode));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedFollowingSiblingIterator.class */
    public final class TypedFollowingSiblingIterator extends FollowingSiblingIterator {
        private final int _nodeType;

        public TypedFollowingSiblingIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.FollowingSiblingIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._currentNode == -1) {
                return -1;
            }
            int node = this._currentNode;
            int nodeType = this._nodeType;
            if (nodeType >= 14) {
                do {
                    node = DTMDefaultBaseIterators.this._nextsib(node);
                    if (node == -1) {
                        break;
                    }
                } while (DTMDefaultBaseIterators.this._exptype(node) != nodeType);
            } else {
                while (true) {
                    int i_nextsib = DTMDefaultBaseIterators.this._nextsib(node);
                    node = i_nextsib;
                    if (i_nextsib == -1) {
                        break;
                    }
                    int eType = DTMDefaultBaseIterators.this._exptype(node);
                    if (eType < 14) {
                        if (eType == nodeType) {
                            break;
                        }
                    } else if (DTMDefaultBaseIterators.this.m_expandedNameTable.getType(eType) == nodeType) {
                        break;
                    }
                }
            }
            this._currentNode = node;
            if (this._currentNode == -1) {
                return -1;
            }
            return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(this._currentNode));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$AttributeIterator.class */
    public final class AttributeIterator extends InternalAxisIteratorBase {
        public AttributeIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = DTMDefaultBaseIterators.this.getFirstAttributeIdentity(DTMDefaultBaseIterators.this.makeNodeIdentity(node));
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            if (node != -1) {
                this._currentNode = DTMDefaultBaseIterators.this.getNextAttributeIdentity(node);
                return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedAttributeIterator.class */
    public final class TypedAttributeIterator extends InternalAxisIteratorBase {
        private final int _nodeType;

        public TypedAttributeIterator(int nodeType) {
            super();
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = DTMDefaultBaseIterators.this.getTypedAttribute(node, this._nodeType);
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

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$PrecedingSiblingIterator.class */
    public class PrecedingSiblingIterator extends InternalAxisIteratorBase {
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
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                int node2 = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
                this._startNodeID = node2;
                if (node2 == -1) {
                    this._currentNode = node2;
                    return resetPosition();
                }
                int type = DTMDefaultBaseIterators.this.m_expandedNameTable.getType(DTMDefaultBaseIterators.this._exptype(node2));
                if (2 == type || 13 == type) {
                    this._currentNode = node2;
                } else {
                    this._currentNode = DTMDefaultBaseIterators.this._parent(node2);
                    if (-1 != this._currentNode) {
                        this._currentNode = DTMDefaultBaseIterators.this._firstch(this._currentNode);
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
            this._currentNode = DTMDefaultBaseIterators.this._nextsib(node);
            return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedPrecedingSiblingIterator.class */
    public final class TypedPrecedingSiblingIterator extends PrecedingSiblingIterator {
        private final int _nodeType;

        public TypedPrecedingSiblingIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.PrecedingSiblingIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            int nodeType = this._nodeType;
            int startID = this._startNodeID;
            if (nodeType >= 14) {
                while (node != -1 && node != startID && DTMDefaultBaseIterators.this._exptype(node) != nodeType) {
                    node = DTMDefaultBaseIterators.this._nextsib(node);
                }
            } else {
                while (node != -1 && node != startID) {
                    int expType = DTMDefaultBaseIterators.this._exptype(node);
                    if (expType < 14) {
                        if (expType == nodeType) {
                            break;
                        }
                        node = DTMDefaultBaseIterators.this._nextsib(node);
                    } else {
                        if (DTMDefaultBaseIterators.this.m_expandedNameTable.getType(expType) == nodeType) {
                            break;
                        }
                        node = DTMDefaultBaseIterators.this._nextsib(node);
                    }
                }
            }
            if (node == -1 || node == this._startNodeID) {
                this._currentNode = -1;
                return -1;
            }
            this._currentNode = DTMDefaultBaseIterators.this._nextsib(node);
            return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$PrecedingIterator.class */
    public class PrecedingIterator extends InternalAxisIteratorBase {
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
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                int node2 = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
                if (DTMDefaultBaseIterators.this._type(node2) == 2) {
                    node2 = DTMDefaultBaseIterators.this._parent(node2);
                }
                this._startNode = node2;
                int index = 0;
                this._stack[0] = node2;
                int parent = node2;
                while (true) {
                    int i_parent = DTMDefaultBaseIterators.this._parent(parent);
                    parent = i_parent;
                    if (i_parent == -1) {
                        break;
                    }
                    index++;
                    if (index == this._stack.length) {
                        int[] stack = new int[index + 4];
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
                    if (DTMDefaultBaseIterators.this._type(this._currentNode) != 2 && DTMDefaultBaseIterators.this._type(this._currentNode) != 13) {
                        return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(this._currentNode));
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

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedPrecedingIterator.class */
    public final class TypedPrecedingIterator extends PrecedingIterator {
        private final int _nodeType;

        public TypedPrecedingIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.PrecedingIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
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
                    } else if (DTMDefaultBaseIterators.this._exptype(node) == nodeType) {
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
                        int expType = DTMDefaultBaseIterators.this._exptype(node);
                        if (expType < 14) {
                            if (expType == nodeType) {
                                break;
                            }
                        } else if (DTMDefaultBaseIterators.this.m_expandedNameTable.getType(expType) == nodeType) {
                            break;
                        }
                    }
                }
            }
            this._currentNode = node;
            if (node == -1) {
                return -1;
            }
            return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$FollowingIterator.class */
    public class FollowingIterator extends InternalAxisIteratorBase {
        DTMAxisTraverser m_traverser;

        public FollowingIterator() {
            super();
            this.m_traverser = DTMDefaultBaseIterators.this.getAxisTraverser(6);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                this._startNode = node;
                this._currentNode = this.m_traverser.first(node);
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node = this._currentNode;
            this._currentNode = this.m_traverser.next(this._startNode, this._currentNode);
            return returnNode(node);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedFollowingIterator.class */
    public final class TypedFollowingIterator extends FollowingIterator {
        private final int _nodeType;

        public TypedFollowingIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.FollowingIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node;
            do {
                node = this._currentNode;
                this._currentNode = this.m_traverser.next(this._startNode, this._currentNode);
                if (node == -1 || DTMDefaultBaseIterators.this.getExpandedTypeID(node) == this._nodeType) {
                    break;
                }
            } while (DTMDefaultBaseIterators.this.getNodeType(node) != this._nodeType);
            if (node == -1) {
                return -1;
            }
            return returnNode(node);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$AncestorIterator.class */
    public class AncestorIterator extends InternalAxisIteratorBase {
        NodeVector m_ancestors;
        int m_ancestorsPos;
        int m_markedPos;
        int m_realStartNode;

        public AncestorIterator() {
            super();
            this.m_ancestors = new NodeVector();
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
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            this.m_realStartNode = node;
            if (this._isRestartable) {
                int nodeID = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
                if (!this._includeSelf && node != -1) {
                    nodeID = DTMDefaultBaseIterators.this._parent(nodeID);
                    node = DTMDefaultBaseIterators.this.makeNodeHandle(nodeID);
                }
                this._startNode = node;
                while (nodeID != -1) {
                    this.m_ancestors.addElement(node);
                    nodeID = DTMDefaultBaseIterators.this._parent(nodeID);
                    node = DTMDefaultBaseIterators.this.makeNodeHandle(nodeID);
                }
                this.m_ancestorsPos = this.m_ancestors.size() - 1;
                this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            this.m_ancestorsPos = this.m_ancestors.size() - 1;
            this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
            return resetPosition();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int next = this._currentNode;
            int pos = this.m_ancestorsPos - 1;
            this.m_ancestorsPos = pos;
            this._currentNode = pos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
            return returnNode(next);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void setMark() {
            this.m_markedPos = this.m_ancestorsPos;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public void gotoMark() {
            this.m_ancestorsPos = this.m_markedPos;
            this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedAncestorIterator.class */
    public final class TypedAncestorIterator extends AncestorIterator {
        private final int _nodeType;

        public TypedAncestorIterator(int type) {
            super();
            this._nodeType = type;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.AncestorIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            this.m_realStartNode = node;
            if (this._isRestartable) {
                int nodeID = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
                int nodeType = this._nodeType;
                if (!this._includeSelf && node != -1) {
                    nodeID = DTMDefaultBaseIterators.this._parent(nodeID);
                }
                this._startNode = node;
                if (nodeType >= 14) {
                    while (nodeID != -1) {
                        if (DTMDefaultBaseIterators.this._exptype(nodeID) == nodeType) {
                            this.m_ancestors.addElement(DTMDefaultBaseIterators.this.makeNodeHandle(nodeID));
                        }
                        nodeID = DTMDefaultBaseIterators.this._parent(nodeID);
                    }
                } else {
                    while (nodeID != -1) {
                        int eType = DTMDefaultBaseIterators.this._exptype(nodeID);
                        if ((eType >= 14 && DTMDefaultBaseIterators.this.m_expandedNameTable.getType(eType) == nodeType) || (eType < 14 && eType == nodeType)) {
                            this.m_ancestors.addElement(DTMDefaultBaseIterators.this.makeNodeHandle(nodeID));
                        }
                        nodeID = DTMDefaultBaseIterators.this._parent(nodeID);
                    }
                }
                this.m_ancestorsPos = this.m_ancestors.size() - 1;
                this._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
                return resetPosition();
            }
            return this;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$DescendantIterator.class */
    public class DescendantIterator extends InternalAxisIteratorBase {
        public DescendantIterator() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isRestartable) {
                int node2 = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
                this._startNode = node2;
                if (this._includeSelf) {
                    node2--;
                }
                this._currentNode = node2;
                return resetPosition();
            }
            return this;
        }

        protected boolean isDescendant(int identity) {
            return DTMDefaultBaseIterators.this._parent(identity) >= this._startNode || this._startNode == identity;
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x0053, code lost:
        
            r6._currentNode = -1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0059, code lost:
        
            return -1;
         */
        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int next() {
            /*
                r6 = this;
                r0 = r6
                int r0 = r0._startNode
                r1 = -1
                if (r0 != r1) goto La
                r0 = -1
                return r0
            La:
                r0 = r6
                boolean r0 = r0._includeSelf
                if (r0 == 0) goto L35
                r0 = r6
                int r0 = r0._currentNode
                r1 = 1
                int r0 = r0 + r1
                r1 = r6
                int r1 = r1._startNode
                if (r0 != r1) goto L35
                r0 = r6
                r1 = r6
                com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators r1 = com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.this
                r2 = r6
                r3 = r2
                int r3 = r3._currentNode
                r4 = 1
                int r3 = r3 + r4
                r4 = r3; r3 = r2; r2 = r4; 
                r3._currentNode = r4
                int r1 = r1.makeNodeHandle(r2)
                int r0 = r0.returnNode(r1)
                return r0
            L35:
                r0 = r6
                int r0 = r0._currentNode
                r7 = r0
            L3a:
                int r7 = r7 + 1
                r0 = r6
                com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators r0 = com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.this
                r1 = r7
                short r0 = r0._type(r1)
                r8 = r0
                r0 = -1
                r1 = r8
                if (r0 == r1) goto L53
                r0 = r6
                r1 = r7
                boolean r0 = r0.isDescendant(r1)
                if (r0 != 0) goto L5a
            L53:
                r0 = r6
                r1 = -1
                r0._currentNode = r1
                r0 = -1
                return r0
            L5a:
                r0 = 2
                r1 = r8
                if (r0 == r1) goto L3a
                r0 = 3
                r1 = r8
                if (r0 == r1) goto L3a
                r0 = 13
                r1 = r8
                if (r0 == r1) goto L3a
                r0 = r6
                r1 = r7
                r0._currentNode = r1
                r0 = r6
                r1 = r6
                com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators r1 = com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.this
                r2 = r7
                int r1 = r1.makeNodeHandle(r2)
                int r0 = r0.returnNode(r1)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.DescendantIterator.next():int");
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            boolean temp = this._isRestartable;
            this._isRestartable = true;
            setStartNode(DTMDefaultBaseIterators.this.makeNodeHandle(this._startNode));
            this._isRestartable = temp;
            return this;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedDescendantIterator.class */
    public final class TypedDescendantIterator extends DescendantIterator {
        private final int _nodeType;

        public TypedDescendantIterator(int nodeType) {
            super();
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.DescendantIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            if (this._startNode == -1) {
                return -1;
            }
            int node = this._currentNode;
            do {
                node++;
                int type = DTMDefaultBaseIterators.this._type(node);
                if (-1 == type || !isDescendant(node)) {
                    this._currentNode = -1;
                    return -1;
                }
                if (type == this._nodeType) {
                    break;
                }
            } while (DTMDefaultBaseIterators.this._exptype(node) != this._nodeType);
            this._currentNode = node;
            return returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$NthDescendantIterator.class */
    public class NthDescendantIterator extends DescendantIterator {
        int _pos;

        public NthDescendantIterator(int pos) {
            super();
            this._pos = pos;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.DescendantIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int node;
            int child;
            int i_nextsib;
            do {
                int node2 = super.next();
                if (node2 != -1) {
                    node = DTMDefaultBaseIterators.this.makeNodeIdentity(node2);
                    int parent = DTMDefaultBaseIterators.this._parent(node);
                    child = DTMDefaultBaseIterators.this._firstch(parent);
                    int pos = 0;
                    do {
                        int type = DTMDefaultBaseIterators.this._type(child);
                        if (1 == type) {
                            pos++;
                        }
                        if (pos >= this._pos) {
                            break;
                        }
                        i_nextsib = DTMDefaultBaseIterators.this._nextsib(child);
                        child = i_nextsib;
                    } while (i_nextsib != -1);
                } else {
                    return -1;
                }
            } while (node != child);
            return node;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$SingletonIterator.class */
    public class SingletonIterator extends InternalAxisIteratorBase {
        private boolean _isConstant;

        public SingletonIterator(DTMDefaultBaseIterators this$0) {
            this(Integer.MIN_VALUE, false);
        }

        public SingletonIterator(DTMDefaultBaseIterators this$0, int node) {
            this(node, false);
        }

        public SingletonIterator(int node, boolean constant) {
            super();
            this._startNode = node;
            this._currentNode = node;
            this._isConstant = constant;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator setStartNode(int node) {
            if (node == 0) {
                node = DTMDefaultBaseIterators.this.getDocument();
            }
            if (this._isConstant) {
                this._currentNode = this._startNode;
                return resetPosition();
            }
            if (this._isRestartable) {
                int i2 = node;
                this._startNode = i2;
                this._currentNode = i2;
                return resetPosition();
            }
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public DTMAxisIterator reset() {
            if (this._isConstant) {
                this._currentNode = this._startNode;
                return resetPosition();
            }
            boolean temp = this._isRestartable;
            this._isRestartable = true;
            setStartNode(this._startNode);
            this._isRestartable = temp;
            return this;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int result = this._currentNode;
            this._currentNode = -1;
            return returnNode(result);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseIterators$TypedSingletonIterator.class */
    public final class TypedSingletonIterator extends SingletonIterator {
        private final int _nodeType;

        public TypedSingletonIterator(int nodeType) {
            super(DTMDefaultBaseIterators.this);
            this._nodeType = nodeType;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.SingletonIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
        public int next() {
            int result = this._currentNode;
            int nodeType = this._nodeType;
            this._currentNode = -1;
            if (nodeType >= 14) {
                if (DTMDefaultBaseIterators.this.getExpandedTypeID(result) == nodeType) {
                    return returnNode(result);
                }
                return -1;
            }
            if (DTMDefaultBaseIterators.this.getNodeType(result) == nodeType) {
                return returnNode(result);
            }
            return -1;
        }
    }
}
