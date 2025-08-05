package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.Axis;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.org.apache.xml.internal.dtm.DTMException;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import javax.xml.transform.Source;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers.class */
public abstract class DTMDefaultBaseTraversers extends DTMDefaultBase {
    public DTMDefaultBaseTraversers(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
        super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
    }

    public DTMDefaultBaseTraversers(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable) {
        super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTM
    public DTMAxisTraverser getAxisTraverser(int axis) {
        DTMAxisTraverser traverser;
        if (null == this.m_traversers) {
            this.m_traversers = new DTMAxisTraverser[Axis.getNamesLength()];
        } else {
            DTMAxisTraverser traverser2 = this.m_traversers[axis];
            if (traverser2 != null) {
                return traverser2;
            }
        }
        switch (axis) {
            case 0:
                traverser = new AncestorTraverser();
                break;
            case 1:
                traverser = new AncestorOrSelfTraverser();
                break;
            case 2:
                traverser = new AttributeTraverser();
                break;
            case 3:
                traverser = new ChildTraverser();
                break;
            case 4:
                traverser = new DescendantTraverser();
                break;
            case 5:
                traverser = new DescendantOrSelfTraverser();
                break;
            case 6:
                traverser = new FollowingTraverser();
                break;
            case 7:
                traverser = new FollowingSiblingTraverser();
                break;
            case 8:
                traverser = new NamespaceDeclsTraverser();
                break;
            case 9:
                traverser = new NamespaceTraverser();
                break;
            case 10:
                traverser = new ParentTraverser();
                break;
            case 11:
                traverser = new PrecedingTraverser();
                break;
            case 12:
                traverser = new PrecedingSiblingTraverser();
                break;
            case 13:
                traverser = new SelfTraverser();
                break;
            case 14:
                traverser = new AllFromNodeTraverser();
                break;
            case 15:
                traverser = new PrecedingAndAncestorTraverser();
                break;
            case 16:
                traverser = new AllFromRootTraverser();
                break;
            case 17:
                traverser = new DescendantFromRootTraverser();
                break;
            case 18:
                traverser = new DescendantOrSelfFromRootTraverser();
                break;
            case 19:
                traverser = new RootTraverser();
                break;
            case 20:
                return null;
            default:
                throw new DTMException(XMLMessages.createXMLMessage("ER_UNKNOWN_AXIS_TYPE", new Object[]{Integer.toString(axis)}));
        }
        if (null == traverser) {
            throw new DTMException(XMLMessages.createXMLMessage("ER_AXIS_TRAVERSER_NOT_SUPPORTED", new Object[]{Axis.getNames(axis)}));
        }
        this.m_traversers[axis] = traverser;
        return traverser;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$AncestorTraverser.class */
    private class AncestorTraverser extends DTMAxisTraverser {
        private AncestorTraverser() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            return DTMDefaultBaseTraversers.this.getParent(current);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);
            do {
                int iElementAt = DTMDefaultBaseTraversers.this.m_parent.elementAt(current2);
                current2 = iElementAt;
                if (-1 == iElementAt) {
                    return -1;
                }
            } while (DTMDefaultBaseTraversers.this.m_exptype.elementAt(current2) != expandedTypeID);
            return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$AncestorOrSelfTraverser.class */
    private class AncestorOrSelfTraverser extends AncestorTraverser {
        private AncestorOrSelfTraverser() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context) {
            return context;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context, int expandedTypeID) {
            return DTMDefaultBaseTraversers.this.getExpandedTypeID(context) == expandedTypeID ? context : next(context, context, expandedTypeID);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$AttributeTraverser.class */
    private class AttributeTraverser extends DTMAxisTraverser {
        private AttributeTraverser() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            return context == current ? DTMDefaultBaseTraversers.this.getFirstAttribute(context) : DTMDefaultBaseTraversers.this.getNextAttribute(current);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            int current2 = context == current ? DTMDefaultBaseTraversers.this.getFirstAttribute(context) : DTMDefaultBaseTraversers.this.getNextAttribute(current);
            while (DTMDefaultBaseTraversers.this.getExpandedTypeID(current2) != expandedTypeID) {
                int nextAttribute = DTMDefaultBaseTraversers.this.getNextAttribute(current2);
                current2 = nextAttribute;
                if (-1 == nextAttribute) {
                    return -1;
                }
            }
            return current2;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$ChildTraverser.class */
    private class ChildTraverser extends DTMAxisTraverser {
        private ChildTraverser() {
        }

        protected int getNextIndexed(int axisRoot, int nextPotential, int expandedTypeID) {
            int nsIndex = DTMDefaultBaseTraversers.this.m_expandedNameTable.getNamespaceID(expandedTypeID);
            int lnIndex = DTMDefaultBaseTraversers.this.m_expandedNameTable.getLocalNameID(expandedTypeID);
            while (true) {
                int nextID = DTMDefaultBaseTraversers.this.findElementFromIndex(nsIndex, lnIndex, nextPotential);
                if (-2 != nextID) {
                    int parentID = DTMDefaultBaseTraversers.this.m_parent.elementAt(nextID);
                    if (parentID == axisRoot) {
                        return nextID;
                    }
                    if (parentID < axisRoot) {
                        return -1;
                    }
                    do {
                        parentID = DTMDefaultBaseTraversers.this.m_parent.elementAt(parentID);
                        if (parentID < axisRoot) {
                            return -1;
                        }
                    } while (parentID > axisRoot);
                    nextPotential = nextID + 1;
                } else {
                    DTMDefaultBaseTraversers.this.nextNode();
                    if (DTMDefaultBaseTraversers.this.m_nextsib.elementAt(axisRoot) != -2) {
                        return -1;
                    }
                }
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context) {
            return DTMDefaultBaseTraversers.this.getFirstChild(context);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context, int expandedTypeID) {
            int identity = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
            int firstMatch = getNextIndexed(identity, DTMDefaultBaseTraversers.this._firstch(identity), expandedTypeID);
            return DTMDefaultBaseTraversers.this.makeNodeHandle(firstMatch);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            return DTMDefaultBaseTraversers.this.getNextSibling(current);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            int i_nextsib = DTMDefaultBaseTraversers.this._nextsib(DTMDefaultBaseTraversers.this.makeNodeIdentity(current));
            while (true) {
                int current2 = i_nextsib;
                if (-1 != current2) {
                    if (DTMDefaultBaseTraversers.this.m_exptype.elementAt(current2) != expandedTypeID) {
                        i_nextsib = DTMDefaultBaseTraversers.this._nextsib(current2);
                    } else {
                        return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
                    }
                } else {
                    return -1;
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$IndexedDTMAxisTraverser.class */
    private abstract class IndexedDTMAxisTraverser extends DTMAxisTraverser {
        protected abstract boolean isAfterAxis(int i2, int i3);

        protected abstract boolean axisHasBeenProcessed(int i2);

        private IndexedDTMAxisTraverser() {
        }

        protected final boolean isIndexed(int expandedTypeID) {
            return DTMDefaultBaseTraversers.this.m_indexing && 1 == DTMDefaultBaseTraversers.this.m_expandedNameTable.getType(expandedTypeID);
        }

        protected int getNextIndexed(int axisRoot, int nextPotential, int expandedTypeID) {
            int nsIndex = DTMDefaultBaseTraversers.this.m_expandedNameTable.getNamespaceID(expandedTypeID);
            int lnIndex = DTMDefaultBaseTraversers.this.m_expandedNameTable.getLocalNameID(expandedTypeID);
            while (true) {
                int next = DTMDefaultBaseTraversers.this.findElementFromIndex(nsIndex, lnIndex, nextPotential);
                if (-2 != next) {
                    if (isAfterAxis(axisRoot, next)) {
                        return -1;
                    }
                    return next;
                }
                if (!axisHasBeenProcessed(axisRoot)) {
                    DTMDefaultBaseTraversers.this.nextNode();
                } else {
                    return -1;
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$DescendantTraverser.class */
    private class DescendantTraverser extends IndexedDTMAxisTraverser {
        private DescendantTraverser() {
            super();
        }

        protected int getFirstPotential(int identity) {
            return identity + 1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.IndexedDTMAxisTraverser
        protected boolean axisHasBeenProcessed(int axisRoot) {
            return DTMDefaultBaseTraversers.this.m_nextsib.elementAt(axisRoot) != -2;
        }

        protected int getSubtreeRoot(int handle) {
            return DTMDefaultBaseTraversers.this.makeNodeIdentity(handle);
        }

        protected boolean isDescendant(int subtreeRootIdentity, int identity) {
            return DTMDefaultBaseTraversers.this._parent(identity) >= subtreeRootIdentity;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.IndexedDTMAxisTraverser
        protected boolean isAfterAxis(int axisRoot, int identity) {
            while (identity != axisRoot) {
                identity = DTMDefaultBaseTraversers.this.m_parent.elementAt(identity);
                if (identity < axisRoot) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context, int expandedTypeID) {
            if (isIndexed(expandedTypeID)) {
                int identity = getSubtreeRoot(context);
                int firstPotential = getFirstPotential(identity);
                return DTMDefaultBaseTraversers.this.makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
            }
            return next(context, context, expandedTypeID);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            int subtreeRootIdent = getSubtreeRoot(context);
            int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
            while (true) {
                int type = DTMDefaultBaseTraversers.this._type(current2);
                if (!isDescendant(subtreeRootIdent, current2)) {
                    return -1;
                }
                if (2 == type || 13 == type) {
                    current2++;
                } else {
                    return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
                }
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            int subtreeRootIdent = getSubtreeRoot(context);
            int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
            if (isIndexed(expandedTypeID)) {
                return DTMDefaultBaseTraversers.this.makeNodeHandle(getNextIndexed(subtreeRootIdent, current2, expandedTypeID));
            }
            while (true) {
                int exptype = DTMDefaultBaseTraversers.this._exptype(current2);
                if (!isDescendant(subtreeRootIdent, current2)) {
                    return -1;
                }
                if (exptype != expandedTypeID) {
                    current2++;
                } else {
                    return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$DescendantOrSelfTraverser.class */
    private class DescendantOrSelfTraverser extends DescendantTraverser {
        private DescendantOrSelfTraverser() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser
        protected int getFirstPotential(int identity) {
            return identity;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context) {
            return context;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$AllFromNodeTraverser.class */
    private class AllFromNodeTraverser extends DescendantOrSelfTraverser {
        private AllFromNodeTraverser() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
            int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
            DTMDefaultBaseTraversers.this._exptype(current2);
            if (!isDescendant(subtreeRootIdent, current2)) {
                return -1;
            }
            return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$FollowingTraverser.class */
    private class FollowingTraverser extends DescendantTraverser {
        private FollowingTraverser() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context) {
            int first;
            int context2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
            int type = DTMDefaultBaseTraversers.this._type(context2);
            if (2 == type || 13 == type) {
                context2 = DTMDefaultBaseTraversers.this._parent(context2);
                int first2 = DTMDefaultBaseTraversers.this._firstch(context2);
                if (-1 != first2) {
                    return DTMDefaultBaseTraversers.this.makeNodeHandle(first2);
                }
            }
            do {
                first = DTMDefaultBaseTraversers.this._nextsib(context2);
                if (-1 == first) {
                    context2 = DTMDefaultBaseTraversers.this._parent(context2);
                }
                if (-1 != first) {
                    break;
                }
            } while (-1 != context2);
            return DTMDefaultBaseTraversers.this.makeNodeHandle(first);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context, int expandedTypeID) {
            int first;
            int type = DTMDefaultBaseTraversers.this.getNodeType(context);
            if (2 == type || 13 == type) {
                context = DTMDefaultBaseTraversers.this.getParent(context);
                int first2 = DTMDefaultBaseTraversers.this.getFirstChild(context);
                if (-1 != first2) {
                    if (DTMDefaultBaseTraversers.this.getExpandedTypeID(first2) == expandedTypeID) {
                        return first2;
                    }
                    return next(context, first2, expandedTypeID);
                }
            }
            do {
                first = DTMDefaultBaseTraversers.this.getNextSibling(context);
                if (-1 == first) {
                    context = DTMDefaultBaseTraversers.this.getParent(context);
                    if (-1 != first) {
                        break;
                    }
                } else {
                    if (DTMDefaultBaseTraversers.this.getExpandedTypeID(first) == expandedTypeID) {
                        return first;
                    }
                    return next(context, first, expandedTypeID);
                }
            } while (-1 != context);
            return first;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);
            while (true) {
                current2++;
                int type = DTMDefaultBaseTraversers.this._type(current2);
                if (-1 == type) {
                    return -1;
                }
                if (2 != type && 13 != type) {
                    return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
                }
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            int etype;
            int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);
            do {
                current2++;
                etype = DTMDefaultBaseTraversers.this._exptype(current2);
                if (-1 == etype) {
                    return -1;
                }
            } while (etype != expandedTypeID);
            return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$FollowingSiblingTraverser.class */
    private class FollowingSiblingTraverser extends DTMAxisTraverser {
        private FollowingSiblingTraverser() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            return DTMDefaultBaseTraversers.this.getNextSibling(current);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            do {
                int nextSibling = DTMDefaultBaseTraversers.this.getNextSibling(current);
                current = nextSibling;
                if (-1 == nextSibling) {
                    return -1;
                }
            } while (DTMDefaultBaseTraversers.this.getExpandedTypeID(current) != expandedTypeID);
            return current;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$NamespaceDeclsTraverser.class */
    private class NamespaceDeclsTraverser extends DTMAxisTraverser {
        private NamespaceDeclsTraverser() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            if (context == current) {
                return DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, false);
            }
            return DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, false);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            int nextNamespaceNode;
            if (context == current) {
                nextNamespaceNode = DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, false);
            } else {
                nextNamespaceNode = DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, false);
            }
            int current2 = nextNamespaceNode;
            while (DTMDefaultBaseTraversers.this.getExpandedTypeID(current2) != expandedTypeID) {
                int nextNamespaceNode2 = DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current2, false);
                current2 = nextNamespaceNode2;
                if (-1 == nextNamespaceNode2) {
                    return -1;
                }
            }
            return current2;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$NamespaceTraverser.class */
    private class NamespaceTraverser extends DTMAxisTraverser {
        private NamespaceTraverser() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            if (context == current) {
                return DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, true);
            }
            return DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, true);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            int nextNamespaceNode;
            if (context == current) {
                nextNamespaceNode = DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, true);
            } else {
                nextNamespaceNode = DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, true);
            }
            int current2 = nextNamespaceNode;
            while (DTMDefaultBaseTraversers.this.getExpandedTypeID(current2) != expandedTypeID) {
                int nextNamespaceNode2 = DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current2, true);
                current2 = nextNamespaceNode2;
                if (-1 == nextNamespaceNode2) {
                    return -1;
                }
            }
            return current2;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$ParentTraverser.class */
    private class ParentTraverser extends DTMAxisTraverser {
        private ParentTraverser() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context) {
            return DTMDefaultBaseTraversers.this.getParent(context);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int current, int expandedTypeID) {
            int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);
            do {
                int iElementAt = DTMDefaultBaseTraversers.this.m_parent.elementAt(current2);
                current2 = iElementAt;
                if (-1 == iElementAt) {
                    return -1;
                }
            } while (DTMDefaultBaseTraversers.this.m_exptype.elementAt(current2) != expandedTypeID);
            return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$PrecedingTraverser.class */
    private class PrecedingTraverser extends DTMAxisTraverser {
        private PrecedingTraverser() {
        }

        protected boolean isAncestor(int contextIdent, int currentIdent) {
            int iElementAt = DTMDefaultBaseTraversers.this.m_parent.elementAt(contextIdent);
            while (true) {
                int contextIdent2 = iElementAt;
                if (-1 != contextIdent2) {
                    if (contextIdent2 != currentIdent) {
                        iElementAt = DTMDefaultBaseTraversers.this.m_parent.elementAt(contextIdent2);
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
            for (int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current2 >= 0; current2--) {
                short type = DTMDefaultBaseTraversers.this._type(current2);
                if (2 != type && 13 != type && !isAncestor(subtreeRootIdent, current2)) {
                    return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
                }
            }
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
            for (int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current2 >= 0; current2--) {
                int exptype = DTMDefaultBaseTraversers.this.m_exptype.elementAt(current2);
                if (exptype == expandedTypeID && !isAncestor(subtreeRootIdent, current2)) {
                    return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
                }
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$PrecedingAndAncestorTraverser.class */
    private class PrecedingAndAncestorTraverser extends DTMAxisTraverser {
        private PrecedingAndAncestorTraverser() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
            for (int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current2 >= 0; current2--) {
                short type = DTMDefaultBaseTraversers.this._type(current2);
                if (2 != type && 13 != type) {
                    return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
                }
            }
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
            for (int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current2 >= 0; current2--) {
                int exptype = DTMDefaultBaseTraversers.this.m_exptype.elementAt(current2);
                if (exptype == expandedTypeID) {
                    return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
                }
            }
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$PrecedingSiblingTraverser.class */
    private class PrecedingSiblingTraverser extends DTMAxisTraverser {
        private PrecedingSiblingTraverser() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            return DTMDefaultBaseTraversers.this.getPreviousSibling(current);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            do {
                int previousSibling = DTMDefaultBaseTraversers.this.getPreviousSibling(current);
                current = previousSibling;
                if (-1 == previousSibling) {
                    return -1;
                }
            } while (DTMDefaultBaseTraversers.this.getExpandedTypeID(current) != expandedTypeID);
            return current;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$SelfTraverser.class */
    private class SelfTraverser extends DTMAxisTraverser {
        private SelfTraverser() {
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context) {
            return context;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context, int expandedTypeID) {
            if (DTMDefaultBaseTraversers.this.getExpandedTypeID(context) == expandedTypeID) {
                return context;
            }
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$AllFromRootTraverser.class */
    private class AllFromRootTraverser extends AllFromNodeTraverser {
        private AllFromRootTraverser() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantOrSelfTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context) {
            return DTMDefaultBaseTraversers.this.getDocumentRoot(context);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context, int expandedTypeID) {
            return DTMDefaultBaseTraversers.this.getExpandedTypeID(DTMDefaultBaseTraversers.this.getDocumentRoot(context)) == expandedTypeID ? context : next(context, context, expandedTypeID);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.AllFromNodeTraverser, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
            int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
            int type = DTMDefaultBaseTraversers.this._type(current2);
            if (type == -1) {
                return -1;
            }
            return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
            int current2 = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
            while (true) {
                int exptype = DTMDefaultBaseTraversers.this._exptype(current2);
                if (exptype == -1) {
                    return -1;
                }
                if (exptype != expandedTypeID) {
                    current2++;
                } else {
                    return DTMDefaultBaseTraversers.this.makeNodeHandle(current2);
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$RootTraverser.class */
    private class RootTraverser extends AllFromRootTraverser {
        private RootTraverser() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.AllFromRootTraverser, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context, int expandedTypeID) {
            int root = DTMDefaultBaseTraversers.this.getDocumentRoot(context);
            if (DTMDefaultBaseTraversers.this.getExpandedTypeID(root) == expandedTypeID) {
                return root;
            }
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.AllFromRootTraverser, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.AllFromNodeTraverser, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current) {
            return -1;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.AllFromRootTraverser, com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int next(int context, int current, int expandedTypeID) {
            return -1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$DescendantOrSelfFromRootTraverser.class */
    private class DescendantOrSelfFromRootTraverser extends DescendantTraverser {
        private DescendantOrSelfFromRootTraverser() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser
        protected int getFirstPotential(int identity) {
            return identity;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser
        protected int getSubtreeRoot(int handle) {
            return DTMDefaultBaseTraversers.this.makeNodeIdentity(DTMDefaultBaseTraversers.this.getDocument());
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context) {
            return DTMDefaultBaseTraversers.this.getDocumentRoot(context);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context, int expandedTypeID) {
            if (isIndexed(expandedTypeID)) {
                int firstPotential = getFirstPotential(0);
                return DTMDefaultBaseTraversers.this.makeNodeHandle(getNextIndexed(0, firstPotential, expandedTypeID));
            }
            int root = first(context);
            return next(root, root, expandedTypeID);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMDefaultBaseTraversers$DescendantFromRootTraverser.class */
    private class DescendantFromRootTraverser extends DescendantTraverser {
        private DescendantFromRootTraverser() {
            super();
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser
        protected int getFirstPotential(int identity) {
            return DTMDefaultBaseTraversers.this._firstch(0);
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser
        protected int getSubtreeRoot(int handle) {
            return 0;
        }

        @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context) {
            return DTMDefaultBaseTraversers.this.makeNodeHandle(DTMDefaultBaseTraversers.this._firstch(0));
        }

        @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers.DescendantTraverser, com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser
        public int first(int context, int expandedTypeID) {
            if (isIndexed(expandedTypeID)) {
                int firstPotential = getFirstPotential(0);
                return DTMDefaultBaseTraversers.this.makeNodeHandle(getNextIndexed(0, firstPotential, expandedTypeID));
            }
            int root = DTMDefaultBaseTraversers.this.getDocumentRoot(context);
            return next(root, root, expandedTypeID);
        }
    }
}
