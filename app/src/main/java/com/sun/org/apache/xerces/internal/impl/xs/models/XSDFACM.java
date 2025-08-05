package com.sun.org.apache.xerces.internal.impl.xs.models;

import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMStateSet;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler;
import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl;
import com.sun.org.apache.xerces.internal.xni.QName;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/models/XSDFACM.class */
public class XSDFACM implements XSCMValidator {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_VALIDATE_CONTENT = false;
    private int fLeafCount;
    private int[] fElemMapCounter;
    private int[] fElemMapCounterLowerBound;
    private int[] fElemMapCounterUpperBound;
    private static long time = 0;
    private Object[] fElemMap = null;
    private int[] fElemMapType = null;
    private int[] fElemMapId = null;
    private int fElemMapSize = 0;
    private boolean[] fFinalStateFlags = null;
    private CMStateSet[] fFollowList = null;
    private CMNode fHeadNode = null;
    private XSCMLeaf[] fLeafList = null;
    private int[] fLeafListType = null;
    private int[][] fTransTable = (int[][]) null;
    private Occurence[] fCountingStates = null;
    private int fTransTableSize = 0;

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/models/XSDFACM$Occurence.class */
    static final class Occurence {
        final int minOccurs;
        final int maxOccurs;
        final int elemIndex;

        public Occurence(XSCMRepeatingLeaf leaf, int elemIndex) {
            this.minOccurs = leaf.getMinOccurs();
            this.maxOccurs = leaf.getMaxOccurs();
            this.elemIndex = elemIndex;
        }

        public String toString() {
            return "minOccurs=" + this.minOccurs + ";maxOccurs=" + (this.maxOccurs != -1 ? Integer.toString(this.maxOccurs) : SchemaSymbols.ATTVAL_UNBOUNDED);
        }
    }

    public XSDFACM(CMNode syntaxTree, int leafCount) throws RuntimeException {
        this.fLeafCount = 0;
        this.fLeafCount = leafCount;
        buildDFA(syntaxTree);
    }

    public boolean isFinalState(int state) {
        if (state < 0) {
            return false;
        }
        return this.fFinalStateFlags[state];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public Object oneTransition(QName curElem, int[] state, SubstitutionGroupHandler subGroupHandler) {
        int curState = state[0];
        if (curState == -1 || curState == -2) {
            if (curState == -1) {
                state[0] = -2;
            }
            return findMatchingDecl(curElem, subGroupHandler);
        }
        int nextState = 0;
        int elemIndex = 0;
        Object matchingDecl = null;
        while (true) {
            if (elemIndex >= this.fElemMapSize) {
                break;
            }
            nextState = this.fTransTable[curState][elemIndex];
            if (nextState != -1) {
                int type = this.fElemMapType[elemIndex];
                if (type == 1) {
                    matchingDecl = subGroupHandler.getMatchingElemDecl(curElem, (XSElementDecl) this.fElemMap[elemIndex]);
                    if (matchingDecl != null) {
                        if (this.fElemMapCounter[elemIndex] >= 0) {
                            int[] iArr = this.fElemMapCounter;
                            int i2 = elemIndex;
                            iArr[i2] = iArr[i2] + 1;
                        }
                    }
                } else if (type == 2 && ((XSWildcardDecl) this.fElemMap[elemIndex]).allowNamespace(curElem.uri)) {
                    matchingDecl = this.fElemMap[elemIndex];
                    if (this.fElemMapCounter[elemIndex] >= 0) {
                        int[] iArr2 = this.fElemMapCounter;
                        int i3 = elemIndex;
                        iArr2[i3] = iArr2[i3] + 1;
                    }
                }
            }
            elemIndex++;
        }
        if (elemIndex == this.fElemMapSize) {
            state[1] = state[0];
            state[0] = -1;
            return findMatchingDecl(curElem, subGroupHandler);
        }
        if (this.fCountingStates != null) {
            Occurence o2 = this.fCountingStates[curState];
            if (o2 != null) {
                if (curState == nextState) {
                    int i4 = state[2] + 1;
                    state[2] = i4;
                    if (i4 > o2.maxOccurs && o2.maxOccurs != -1) {
                        return findMatchingDecl(curElem, state, subGroupHandler, elemIndex);
                    }
                } else {
                    if (state[2] < o2.minOccurs) {
                        state[1] = state[0];
                        state[0] = -1;
                        return findMatchingDecl(curElem, subGroupHandler);
                    }
                    Occurence o3 = this.fCountingStates[nextState];
                    if (o3 != null) {
                        state[2] = elemIndex == o3.elemIndex ? 1 : 0;
                    }
                }
            } else {
                Occurence o4 = this.fCountingStates[nextState];
                if (o4 != null) {
                    state[2] = elemIndex == o4.elemIndex ? 1 : 0;
                }
            }
        }
        state[0] = nextState;
        return matchingDecl;
    }

    Object findMatchingDecl(QName curElem, SubstitutionGroupHandler subGroupHandler) {
        for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
            int type = this.fElemMapType[elemIndex];
            if (type == 1) {
                Object matchingDecl = subGroupHandler.getMatchingElemDecl(curElem, (XSElementDecl) this.fElemMap[elemIndex]);
                if (matchingDecl != null) {
                    return matchingDecl;
                }
            } else if (type == 2 && ((XSWildcardDecl) this.fElemMap[elemIndex]).allowNamespace(curElem.uri)) {
                return this.fElemMap[elemIndex];
            }
        }
        return null;
    }

    Object findMatchingDecl(QName curElem, int[] state, SubstitutionGroupHandler subGroupHandler, int elemIndex) {
        int curState = state[0];
        int nextState = 0;
        Object matchingDecl = null;
        while (true) {
            elemIndex++;
            if (elemIndex >= this.fElemMapSize) {
                break;
            }
            nextState = this.fTransTable[curState][elemIndex];
            if (nextState != -1) {
                int type = this.fElemMapType[elemIndex];
                if (type == 1) {
                    matchingDecl = subGroupHandler.getMatchingElemDecl(curElem, (XSElementDecl) this.fElemMap[elemIndex]);
                    if (matchingDecl != null) {
                        break;
                    }
                } else if (type == 2 && ((XSWildcardDecl) this.fElemMap[elemIndex]).allowNamespace(curElem.uri)) {
                    matchingDecl = this.fElemMap[elemIndex];
                    break;
                }
            }
        }
        if (elemIndex == this.fElemMapSize) {
            state[1] = state[0];
            state[0] = -1;
            return findMatchingDecl(curElem, subGroupHandler);
        }
        state[0] = nextState;
        Occurence o2 = this.fCountingStates[nextState];
        if (o2 != null) {
            state[2] = elemIndex == o2.elemIndex ? 1 : 0;
        }
        return matchingDecl;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public int[] startContentModel() {
        for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
            if (this.fElemMapCounter[elemIndex] != -1) {
                this.fElemMapCounter[elemIndex] = 0;
            }
        }
        return new int[3];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public boolean endContentModel(int[] state) {
        Occurence o2;
        int curState = state[0];
        if (this.fFinalStateFlags[curState]) {
            if (this.fCountingStates != null && (o2 = this.fCountingStates[curState]) != null && state[2] < o2.minOccurs) {
                return false;
            }
            return true;
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v101, types: [int[], int[][], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v49, types: [int[], int[][]] */
    private void buildDFA(CMNode syntaxTree) throws RuntimeException {
        int EOCPos = this.fLeafCount;
        int i2 = this.fLeafCount;
        this.fLeafCount = i2 + 1;
        XSCMLeaf nodeEOC = new XSCMLeaf(1, null, -1, i2);
        this.fHeadNode = new XSCMBinOp(102, syntaxTree, nodeEOC);
        this.fLeafList = new XSCMLeaf[this.fLeafCount];
        this.fLeafListType = new int[this.fLeafCount];
        postTreeBuildInit(this.fHeadNode);
        this.fFollowList = new CMStateSet[this.fLeafCount];
        for (int index = 0; index < this.fLeafCount; index++) {
            this.fFollowList[index] = new CMStateSet(this.fLeafCount);
        }
        calcFollowList(this.fHeadNode);
        this.fElemMap = new Object[this.fLeafCount];
        this.fElemMapType = new int[this.fLeafCount];
        this.fElemMapId = new int[this.fLeafCount];
        this.fElemMapCounter = new int[this.fLeafCount];
        this.fElemMapCounterLowerBound = new int[this.fLeafCount];
        this.fElemMapCounterUpperBound = new int[this.fLeafCount];
        this.fElemMapSize = 0;
        Occurence[] elemOccurenceMap = null;
        for (int outIndex = 0; outIndex < this.fLeafCount; outIndex++) {
            this.fElemMap[outIndex] = null;
            int inIndex = 0;
            int id = this.fLeafList[outIndex].getParticleId();
            while (inIndex < this.fElemMapSize && id != this.fElemMapId[inIndex]) {
                inIndex++;
            }
            if (inIndex == this.fElemMapSize) {
                XSCMLeaf leaf = this.fLeafList[outIndex];
                this.fElemMap[this.fElemMapSize] = leaf.getLeaf();
                if (leaf instanceof XSCMRepeatingLeaf) {
                    if (elemOccurenceMap == null) {
                        elemOccurenceMap = new Occurence[this.fLeafCount];
                    }
                    elemOccurenceMap[this.fElemMapSize] = new Occurence((XSCMRepeatingLeaf) leaf, this.fElemMapSize);
                }
                this.fElemMapType[this.fElemMapSize] = this.fLeafListType[outIndex];
                this.fElemMapId[this.fElemMapSize] = id;
                int[] bounds = (int[]) leaf.getUserData();
                if (bounds != null) {
                    this.fElemMapCounter[this.fElemMapSize] = 0;
                    this.fElemMapCounterLowerBound[this.fElemMapSize] = bounds[0];
                    this.fElemMapCounterUpperBound[this.fElemMapSize] = bounds[1];
                } else {
                    this.fElemMapCounter[this.fElemMapSize] = -1;
                    this.fElemMapCounterLowerBound[this.fElemMapSize] = -1;
                    this.fElemMapCounterUpperBound[this.fElemMapSize] = -1;
                }
                this.fElemMapSize++;
            }
        }
        this.fElemMapSize--;
        int[] fLeafSorter = new int[this.fLeafCount + this.fElemMapSize];
        int fSortCount = 0;
        for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
            int id2 = this.fElemMapId[elemIndex];
            for (int leafIndex = 0; leafIndex < this.fLeafCount; leafIndex++) {
                if (id2 == this.fLeafList[leafIndex].getParticleId()) {
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
            this.fFinalStateFlags[unmarkedState] = setT2.getBit(EOCPos);
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
        if (elemOccurenceMap != null) {
            this.fCountingStates = new Occurence[curState];
            for (int i8 = 0; i8 < curState; i8++) {
                int[] transitions = this.fTransTable[i8];
                int j2 = 0;
                while (true) {
                    if (j2 >= transitions.length) {
                        break;
                    }
                    if (i8 != transitions[j2]) {
                        j2++;
                    } else {
                        this.fCountingStates[i8] = elemOccurenceMap[j2];
                        break;
                    }
                }
            }
        }
        this.fHeadNode = null;
        this.fLeafList = null;
        this.fFollowList = null;
        this.fLeafListType = null;
        this.fElemMapId = null;
    }

    private void calcFollowList(CMNode nodeCur) {
        if (nodeCur.type() == 101) {
            calcFollowList(((XSCMBinOp) nodeCur).getLeft());
            calcFollowList(((XSCMBinOp) nodeCur).getRight());
            return;
        }
        if (nodeCur.type() == 102) {
            calcFollowList(((XSCMBinOp) nodeCur).getLeft());
            calcFollowList(((XSCMBinOp) nodeCur).getRight());
            CMStateSet last = ((XSCMBinOp) nodeCur).getLeft().lastPos();
            CMStateSet first = ((XSCMBinOp) nodeCur).getRight().firstPos();
            for (int index = 0; index < this.fLeafCount; index++) {
                if (last.getBit(index)) {
                    this.fFollowList[index].union(first);
                }
            }
            return;
        }
        if (nodeCur.type() == 4 || nodeCur.type() == 6) {
            calcFollowList(((XSCMUniOp) nodeCur).getChild());
            CMStateSet first2 = nodeCur.firstPos();
            CMStateSet last2 = nodeCur.lastPos();
            for (int index2 = 0; index2 < this.fLeafCount; index2++) {
                if (last2.getBit(index2)) {
                    this.fFollowList[index2].union(first2);
                }
            }
            return;
        }
        if (nodeCur.type() == 5) {
            calcFollowList(((XSCMUniOp) nodeCur).getChild());
        }
    }

    private void dumpTree(CMNode nodeCur, int level) {
        for (int index = 0; index < level; index++) {
            System.out.print("   ");
        }
        int type = nodeCur.type();
        switch (type) {
            case 1:
                System.out.print("Leaf: (pos=" + ((XSCMLeaf) nodeCur).getPosition() + "), (elemIndex=" + ((XSCMLeaf) nodeCur).getLeaf() + ") ");
                if (nodeCur.isNullable()) {
                    System.out.print(" Nullable ");
                }
                System.out.print("firstPos=");
                System.out.print(nodeCur.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(nodeCur.lastPos().toString());
                return;
            case 2:
                System.out.print("Any Node: ");
                System.out.print("firstPos=");
                System.out.print(nodeCur.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(nodeCur.lastPos().toString());
                return;
            case 4:
            case 5:
            case 6:
                System.out.print("Rep Node ");
                if (nodeCur.isNullable()) {
                    System.out.print("Nullable ");
                }
                System.out.print("firstPos=");
                System.out.print(nodeCur.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(nodeCur.lastPos().toString());
                dumpTree(((XSCMUniOp) nodeCur).getChild(), level + 1);
                return;
            case 101:
            case 102:
                if (type == 101) {
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
                dumpTree(((XSCMBinOp) nodeCur).getLeft(), level + 1);
                dumpTree(((XSCMBinOp) nodeCur).getRight(), level + 1);
                return;
            default:
                throw new RuntimeException("ImplementationMessages.VAL_NIICM");
        }
    }

    private int[] makeDefStateList() {
        int[] retArray = new int[this.fElemMapSize];
        for (int index = 0; index < this.fElemMapSize; index++) {
            retArray[index] = -1;
        }
        return retArray;
    }

    private void postTreeBuildInit(CMNode nodeCur) throws RuntimeException {
        nodeCur.setMaxStates(this.fLeafCount);
        if (nodeCur.type() == 2) {
            XSCMLeaf leaf = (XSCMLeaf) nodeCur;
            int pos = leaf.getPosition();
            this.fLeafList[pos] = leaf;
            this.fLeafListType[pos] = 2;
            return;
        }
        if (nodeCur.type() == 101 || nodeCur.type() == 102) {
            postTreeBuildInit(((XSCMBinOp) nodeCur).getLeft());
            postTreeBuildInit(((XSCMBinOp) nodeCur).getRight());
            return;
        }
        if (nodeCur.type() == 4 || nodeCur.type() == 6 || nodeCur.type() == 5) {
            postTreeBuildInit(((XSCMUniOp) nodeCur).getChild());
        } else {
            if (nodeCur.type() == 1) {
                XSCMLeaf leaf2 = (XSCMLeaf) nodeCur;
                int pos2 = leaf2.getPosition();
                this.fLeafList[pos2] = leaf2;
                this.fLeafListType[pos2] = 1;
                return;
            }
            throw new RuntimeException("ImplementationMessages.VAL_NIICM");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x00c9  */
    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean checkUniqueParticleAttribution(com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler r10) throws com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException {
        /*
            Method dump skipped, instructions count: 380
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xs.models.XSDFACM.checkUniqueParticleAttribution(com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0073  */
    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Vector whatCanGoHere(int[] r5) {
        /*
            r4 = this;
            r0 = r5
            r1 = 0
            r0 = r0[r1]
            r6 = r0
            r0 = r6
            if (r0 >= 0) goto Lc
            r0 = r5
            r1 = 1
            r0 = r0[r1]
            r6 = r0
        Lc:
            r0 = r4
            com.sun.org.apache.xerces.internal.impl.xs.models.XSDFACM$Occurence[] r0 = r0.fCountingStates
            if (r0 == 0) goto L1c
            r0 = r4
            com.sun.org.apache.xerces.internal.impl.xs.models.XSDFACM$Occurence[] r0 = r0.fCountingStates
            r1 = r6
            r0 = r0[r1]
            goto L1d
        L1c:
            r0 = 0
        L1d:
            r7 = r0
            r0 = r5
            r1 = 2
            r0 = r0[r1]
            r8 = r0
            java.util.Vector r0 = new java.util.Vector
            r1 = r0
            r1.<init>()
            r9 = r0
            r0 = 0
            r10 = r0
        L2f:
            r0 = r10
            r1 = r4
            int r1 = r1.fElemMapSize
            if (r0 >= r1) goto L85
            r0 = r4
            int[][] r0 = r0.fTransTable
            r1 = r6
            r0 = r0[r1]
            r1 = r10
            r0 = r0[r1]
            r11 = r0
            r0 = r11
            r1 = -1
            if (r0 == r1) goto L7f
            r0 = r7
            if (r0 == 0) goto L73
            r0 = r6
            r1 = r11
            if (r0 != r1) goto L67
            r0 = r8
            r1 = r7
            int r1 = r1.maxOccurs
            if (r0 < r1) goto L73
            r0 = r7
            int r0 = r0.maxOccurs
            r1 = -1
            if (r0 == r1) goto L73
            goto L7f
        L67:
            r0 = r8
            r1 = r7
            int r1 = r1.minOccurs
            if (r0 >= r1) goto L73
            goto L7f
        L73:
            r0 = r9
            r1 = r4
            java.lang.Object[] r1 = r1.fElemMap
            r2 = r10
            r1 = r1[r2]
            r0.addElement(r1)
        L7f:
            int r10 = r10 + 1
            goto L2f
        L85:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xs.models.XSDFACM.whatCanGoHere(int[]):java.util.Vector");
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public ArrayList checkMinMaxBounds() {
        ArrayList result = null;
        for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
            int count = this.fElemMapCounter[elemIndex];
            if (count != -1) {
                int minOccurs = this.fElemMapCounterLowerBound[elemIndex];
                int maxOccurs = this.fElemMapCounterUpperBound[elemIndex];
                if (count < minOccurs) {
                    if (result == null) {
                        result = new ArrayList();
                    }
                    result.add("cvc-complex-type.2.4.b");
                    result.add(VectorFormat.DEFAULT_PREFIX + this.fElemMap[elemIndex] + "}");
                }
                if (maxOccurs != -1 && count > maxOccurs) {
                    if (result == null) {
                        result = new ArrayList();
                    }
                    result.add("cvc-complex-type.2.4.e");
                    result.add(VectorFormat.DEFAULT_PREFIX + this.fElemMap[elemIndex] + "}");
                }
            }
        }
        return result;
    }
}
