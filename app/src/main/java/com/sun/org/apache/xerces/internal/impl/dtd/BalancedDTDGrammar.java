package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.XNIException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/BalancedDTDGrammar.class */
final class BalancedDTDGrammar extends DTDGrammar {
    private boolean fMixed;
    private int fDepth;
    private short[] fOpStack;
    private int[][] fGroupIndexStack;
    private int[] fGroupIndexStackSizes;

    public BalancedDTDGrammar(SymbolTable symbolTable, XMLDTDDescription desc) {
        super(symbolTable, desc);
        this.fDepth = 0;
        this.fOpStack = null;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar, com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public final void startContentModel(String elementName, Augmentations augs) throws XNIException {
        this.fDepth = 0;
        initializeContentModelStacks();
        super.startContentModel(elementName, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar, com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public final void startGroup(Augmentations augs) throws XNIException {
        this.fDepth++;
        initializeContentModelStacks();
        this.fMixed = false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar, com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public final void pcdata(Augmentations augs) throws XNIException {
        this.fMixed = true;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar, com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public final void element(String elementName, Augmentations augs) throws XNIException {
        addToCurrentGroup(addUniqueLeafNode(elementName));
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar, com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public final void separator(short separator, Augmentations augs) throws XNIException {
        if (separator == 0) {
            this.fOpStack[this.fDepth] = 4;
        } else if (separator == 1) {
            this.fOpStack[this.fDepth] = 5;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar, com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public final void occurrence(short occurrence, Augmentations augs) throws XNIException {
        if (!this.fMixed) {
            int currentIndex = this.fGroupIndexStackSizes[this.fDepth] - 1;
            if (occurrence == 2) {
                this.fGroupIndexStack[this.fDepth][currentIndex] = addContentSpecNode((short) 1, this.fGroupIndexStack[this.fDepth][currentIndex], -1);
            } else if (occurrence == 3) {
                this.fGroupIndexStack[this.fDepth][currentIndex] = addContentSpecNode((short) 2, this.fGroupIndexStack[this.fDepth][currentIndex], -1);
            } else if (occurrence == 4) {
                this.fGroupIndexStack[this.fDepth][currentIndex] = addContentSpecNode((short) 3, this.fGroupIndexStack[this.fDepth][currentIndex], -1);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar, com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public final void endGroup(Augmentations augs) throws XNIException {
        int length = this.fGroupIndexStackSizes[this.fDepth];
        int group = length > 0 ? addContentSpecNodes(0, length - 1) : addUniqueLeafNode(null);
        this.fDepth--;
        addToCurrentGroup(group);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public final void endDTD(Augmentations augs) throws XNIException {
        super.endDTD(augs);
        this.fOpStack = null;
        this.fGroupIndexStack = (int[][]) null;
        this.fGroupIndexStackSizes = null;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar
    protected final void addContentSpecToElement(XMLElementDecl elementDecl) {
        int contentSpec = this.fGroupIndexStackSizes[0] > 0 ? this.fGroupIndexStack[0][0] : -1;
        setContentSpecIndex(this.fCurrentElementIndex, contentSpec);
    }

    private int addContentSpecNodes(int begin, int end) {
        if (begin == end) {
            return this.fGroupIndexStack[this.fDepth][begin];
        }
        int middle = (begin + end) >>> 1;
        return addContentSpecNode(this.fOpStack[this.fDepth], addContentSpecNodes(begin, middle), addContentSpecNodes(middle + 1, end));
    }

    /* JADX WARN: Type inference failed for: r0v14, types: [int[], int[][], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v19, types: [int[], int[][]] */
    private void initializeContentModelStacks() {
        if (this.fOpStack == null) {
            this.fOpStack = new short[8];
            this.fGroupIndexStack = new int[8];
            this.fGroupIndexStackSizes = new int[8];
        } else if (this.fDepth == this.fOpStack.length) {
            short[] newOpStack = new short[this.fDepth * 2];
            System.arraycopy(this.fOpStack, 0, newOpStack, 0, this.fDepth);
            this.fOpStack = newOpStack;
            ?? r0 = new int[this.fDepth * 2];
            System.arraycopy(this.fGroupIndexStack, 0, r0, 0, this.fDepth);
            this.fGroupIndexStack = r0;
            int[] newGroupIndexStackLengths = new int[this.fDepth * 2];
            System.arraycopy(this.fGroupIndexStackSizes, 0, newGroupIndexStackLengths, 0, this.fDepth);
            this.fGroupIndexStackSizes = newGroupIndexStackLengths;
        }
        this.fOpStack[this.fDepth] = -1;
        this.fGroupIndexStackSizes[this.fDepth] = 0;
    }

    private void addToCurrentGroup(int contentSpec) {
        int[] currentGroup = this.fGroupIndexStack[this.fDepth];
        int[] iArr = this.fGroupIndexStackSizes;
        int i2 = this.fDepth;
        int length = iArr[i2];
        iArr[i2] = length + 1;
        if (currentGroup == null) {
            currentGroup = new int[8];
            this.fGroupIndexStack[this.fDepth] = currentGroup;
        } else if (length == currentGroup.length) {
            int[] newGroup = new int[currentGroup.length * 2];
            System.arraycopy(currentGroup, 0, newGroup, 0, currentGroup.length);
            currentGroup = newGroup;
            this.fGroupIndexStack[this.fDepth] = currentGroup;
        }
        currentGroup[length] = contentSpec;
    }
}
