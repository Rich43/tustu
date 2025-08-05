package com.sun.org.apache.xerces.internal.impl.dtd.models;

import com.sun.org.apache.xerces.internal.xni.QName;
import java.util.HashMap;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/models/DFAContentModel.class */
public class DFAContentModel implements ContentModelValidator {
    private static String fEpsilonString;
    private static String fEOCString;
    private static final boolean DEBUG_VALIDATE_CONTENT = false;
    private boolean fMixed;
    private int fLeafCount;
    private QName[] fElemMap = null;
    private int[] fElemMapType = null;
    private int fElemMapSize = 0;
    private int fEOCPos = 0;
    private boolean[] fFinalStateFlags = null;
    private CMStateSet[] fFollowList = null;
    private CMNode fHeadNode = null;
    private CMLeaf[] fLeafList = null;
    private int[] fLeafListType = null;
    private int[][] fTransTable = (int[][]) null;
    private int fTransTableSize = 0;
    private boolean fEmptyContentIsValid = false;
    private final QName fQName = new QName();

    static {
        fEpsilonString = "<<CMNODE_EPSILON>>";
        fEOCString = "<<CMNODE_EOC>>";
        fEpsilonString = fEpsilonString.intern();
        fEOCString = fEOCString.intern();
    }

    public DFAContentModel(CMNode syntaxTree, int leafCount, boolean mixed) {
        this.fLeafCount = 0;
        this.fLeafCount = leafCount;
        this.fMixed = mixed;
        buildDFA(syntaxTree);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator
    public int validate(QName[] children, int offset, int length) {
        if (length == 0) {
            return this.fEmptyContentIsValid ? -1 : 0;
        }
        int curState = 0;
        for (int childIndex = 0; childIndex < length; childIndex++) {
            QName curElem = children[offset + childIndex];
            if (!this.fMixed || curElem.localpart != null) {
                int elemIndex = 0;
                while (elemIndex < this.fElemMapSize) {
                    int type = this.fElemMapType[elemIndex] & 15;
                    if (type == 0) {
                        if (this.fElemMap[elemIndex].rawname == curElem.rawname) {
                            break;
                        }
                        elemIndex++;
                    } else if (type == 6) {
                        String uri = this.fElemMap[elemIndex].uri;
                        if (uri == null || uri == curElem.uri) {
                            break;
                        }
                        elemIndex++;
                    } else if (type == 8) {
                        if (curElem.uri == null) {
                            break;
                        }
                        elemIndex++;
                    } else {
                        if (type == 7 && this.fElemMap[elemIndex].uri != curElem.uri) {
                            break;
                        }
                        elemIndex++;
                    }
                }
                if (elemIndex == this.fElemMapSize) {
                    return childIndex;
                }
                curState = this.fTransTable[curState][elemIndex];
                if (curState == -1) {
                    return childIndex;
                }
            }
        }
        if (!this.fFinalStateFlags[curState]) {
            return length;
        }
        return -1;
    }

    /* JADX WARN: Type inference failed for: r0v85, types: [int[], int[][], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v39, types: [int[], int[][]] */
    private void buildDFA(CMNode syntaxTree) {
        this.fQName.setValues(null, fEOCString, fEOCString, null);
        CMLeaf nodeEOC = new CMLeaf(this.fQName);
        this.fHeadNode = new CMBinOp(5, syntaxTree, nodeEOC);
        this.fEOCPos = this.fLeafCount;
        int i2 = this.fLeafCount;
        this.fLeafCount = i2 + 1;
        nodeEOC.setPosition(i2);
        this.fLeafList = new CMLeaf[this.fLeafCount];
        this.fLeafListType = new int[this.fLeafCount];
        postTreeBuildInit(this.fHeadNode, 0);
        this.fFollowList = new CMStateSet[this.fLeafCount];
        for (int index = 0; index < this.fLeafCount; index++) {
            this.fFollowList[index] = new CMStateSet(this.fLeafCount);
        }
        calcFollowList(this.fHeadNode);
        this.fElemMap = new QName[this.fLeafCount];
        this.fElemMapType = new int[this.fLeafCount];
        this.fElemMapSize = 0;
        for (int outIndex = 0; outIndex < this.fLeafCount; outIndex++) {
            this.fElemMap[outIndex] = new QName();
            QName element = this.fLeafList[outIndex].getElement();
            int inIndex = 0;
            while (inIndex < this.fElemMapSize && this.fElemMap[inIndex].rawname != element.rawname) {
                inIndex++;
            }
            if (inIndex == this.fElemMapSize) {
                this.fElemMap[this.fElemMapSize].setValues(element);
                this.fElemMapType[this.fElemMapSize] = this.fLeafListType[outIndex];
                this.fElemMapSize++;
            }
        }
        int[] fLeafSorter = new int[this.fLeafCount + this.fElemMapSize];
        int fSortCount = 0;
        for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
            for (int leafIndex = 0; leafIndex < this.fLeafCount; leafIndex++) {
                QName leaf = this.fLeafList[leafIndex].getElement();
                if (leaf.rawname == this.fElemMap[elemIndex].rawname) {
                    int i3 = fSortCount;
                    fSortCount++;
                    fLeafSorter[i3] = leafIndex;
                }
            }
            int i4 = fSortCount;
            fSortCount++;
            fLeafSorter[i4] = -1;
        }
        int curArraySize = this.fLeafCount * 4;
        CMStateSet[] statesToDo = new CMStateSet[curArraySize];
        this.fFinalStateFlags = new boolean[curArraySize];
        this.fTransTable = new int[curArraySize];
        CMStateSet setT = this.fHeadNode.firstPos();
        int unmarkedState = 0;
        this.fTransTable[0] = makeDefStateList();
        statesToDo[0] = setT;
        int curState = 0 + 1;
        HashMap stateTable = new HashMap();
        while (unmarkedState < curState) {
            CMStateSet setT2 = statesToDo[unmarkedState];
            int[] transEntry = this.fTransTable[unmarkedState];
            this.fFinalStateFlags[unmarkedState] = setT2.getBit(this.fEOCPos);
            unmarkedState++;
            CMStateSet newSet = null;
            int sorterIndex = 0;
            for (int elemIndex2 = 0; elemIndex2 < this.fElemMapSize; elemIndex2++) {
                if (newSet == null) {
                    newSet = new CMStateSet(this.fLeafCount);
                } else {
                    newSet.zeroBits();
                }
                int i5 = sorterIndex;
                sorterIndex++;
                int i6 = fLeafSorter[i5];
                while (true) {
                    int leafIndex2 = i6;
                    if (leafIndex2 == -1) {
                        break;
                    }
                    if (setT2.getBit(leafIndex2)) {
                        newSet.union(this.fFollowList[leafIndex2]);
                    }
                    int i7 = sorterIndex;
                    sorterIndex++;
                    i6 = fLeafSorter[i7];
                }
                if (!newSet.isEmpty()) {
                    Integer stateObj = (Integer) stateTable.get(newSet);
                    int stateIndex = stateObj == null ? curState : stateObj.intValue();
                    if (stateIndex == curState) {
                        statesToDo[curState] = newSet;
                        this.fTransTable[curState] = makeDefStateList();
                        stateTable.put(newSet, new Integer(curState));
                        curState++;
                        newSet = null;
                    }
                    transEntry[elemIndex2] = stateIndex;
                    if (curState == curArraySize) {
                        int newSize = (int) (curArraySize * 1.5d);
                        CMStateSet[] newToDo = new CMStateSet[newSize];
                        boolean[] newFinalFlags = new boolean[newSize];
                        ?? r0 = new int[newSize];
                        System.arraycopy(statesToDo, 0, newToDo, 0, curArraySize);
                        System.arraycopy(this.fFinalStateFlags, 0, newFinalFlags, 0, curArraySize);
                        System.arraycopy(this.fTransTable, 0, r0, 0, curArraySize);
                        curArraySize = newSize;
                        statesToDo = newToDo;
                        this.fFinalStateFlags = newFinalFlags;
                        this.fTransTable = r0;
                    }
                }
            }
        }
        this.fEmptyContentIsValid = ((CMBinOp) this.fHeadNode).getLeft().isNullable();
        this.fHeadNode = null;
        this.fLeafList = null;
        this.fFollowList = null;
    }

    private void calcFollowList(CMNode nodeCur) {
        if (nodeCur.type() == 4) {
            calcFollowList(((CMBinOp) nodeCur).getLeft());
            calcFollowList(((CMBinOp) nodeCur).getRight());
            return;
        }
        if (nodeCur.type() == 5) {
            calcFollowList(((CMBinOp) nodeCur).getLeft());
            calcFollowList(((CMBinOp) nodeCur).getRight());
            CMStateSet last = ((CMBinOp) nodeCur).getLeft().lastPos();
            CMStateSet first = ((CMBinOp) nodeCur).getRight().firstPos();
            for (int index = 0; index < this.fLeafCount; index++) {
                if (last.getBit(index)) {
                    this.fFollowList[index].union(first);
                }
            }
            return;
        }
        if (nodeCur.type() == 2 || nodeCur.type() == 3) {
            calcFollowList(((CMUniOp) nodeCur).getChild());
            CMStateSet first2 = nodeCur.firstPos();
            CMStateSet last2 = nodeCur.lastPos();
            for (int index2 = 0; index2 < this.fLeafCount; index2++) {
                if (last2.getBit(index2)) {
                    this.fFollowList[index2].union(first2);
                }
            }
            return;
        }
        if (nodeCur.type() == 1) {
            calcFollowList(((CMUniOp) nodeCur).getChild());
        }
    }

    private void dumpTree(CMNode nodeCur, int level) {
        for (int index = 0; index < level; index++) {
            System.out.print("   ");
        }
        int type = nodeCur.type();
        if (type == 4 || type == 5) {
            if (type == 4) {
                System.out.print("Choice Node ");
            } else {
                System.out.print("Seq Node ");
            }
            if (nodeCur.isNullable()) {
                System.out.print("Nullable ");
            }
            System.out.print("firstPos=");
            System.out.print(nodeCur.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(nodeCur.lastPos().toString());
            dumpTree(((CMBinOp) nodeCur).getLeft(), level + 1);
            dumpTree(((CMBinOp) nodeCur).getRight(), level + 1);
            return;
        }
        if (nodeCur.type() == 2) {
            System.out.print("Rep Node ");
            if (nodeCur.isNullable()) {
                System.out.print("Nullable ");
            }
            System.out.print("firstPos=");
            System.out.print(nodeCur.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(nodeCur.lastPos().toString());
            dumpTree(((CMUniOp) nodeCur).getChild(), level + 1);
            return;
        }
        if (nodeCur.type() == 0) {
            System.out.print("Leaf: (pos=" + ((CMLeaf) nodeCur).getPosition() + "), " + ((Object) ((CMLeaf) nodeCur).getElement()) + "(elemIndex=" + ((Object) ((CMLeaf) nodeCur).getElement()) + ") ");
            if (nodeCur.isNullable()) {
                System.out.print(" Nullable ");
            }
            System.out.print("firstPos=");
            System.out.print(nodeCur.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(nodeCur.lastPos().toString());
            return;
        }
        throw new RuntimeException("ImplementationMessages.VAL_NIICM");
    }

    private int[] makeDefStateList() {
        int[] retArray = new int[this.fElemMapSize];
        for (int index = 0; index < this.fElemMapSize; index++) {
            retArray[index] = -1;
        }
        return retArray;
    }

    private int postTreeBuildInit(CMNode nodeCur, int curIndex) {
        nodeCur.setMaxStates(this.fLeafCount);
        if ((nodeCur.type() & 15) == 6 || (nodeCur.type() & 15) == 8 || (nodeCur.type() & 15) == 7) {
            QName qname = new QName(null, null, null, ((CMAny) nodeCur).getURI());
            this.fLeafList[curIndex] = new CMLeaf(qname, ((CMAny) nodeCur).getPosition());
            this.fLeafListType[curIndex] = nodeCur.type();
            curIndex++;
        } else if (nodeCur.type() == 4 || nodeCur.type() == 5) {
            curIndex = postTreeBuildInit(((CMBinOp) nodeCur).getRight(), postTreeBuildInit(((CMBinOp) nodeCur).getLeft(), curIndex));
        } else if (nodeCur.type() == 2 || nodeCur.type() == 3 || nodeCur.type() == 1) {
            curIndex = postTreeBuildInit(((CMUniOp) nodeCur).getChild(), curIndex);
        } else if (nodeCur.type() == 0) {
            QName node = ((CMLeaf) nodeCur).getElement();
            if (node.localpart != fEpsilonString) {
                this.fLeafList[curIndex] = (CMLeaf) nodeCur;
                this.fLeafListType[curIndex] = 0;
                curIndex++;
            }
        } else {
            throw new RuntimeException("ImplementationMessages.VAL_NIICM: type=" + nodeCur.type());
        }
        return curIndex;
    }
}
