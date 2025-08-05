package jdk.nashorn.internal.ir;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.Source;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LexicalContext.class */
public class LexicalContext {
    private LexicalContextNode[] stack = new LexicalContextNode[16];
    private int[] flags = new int[16];
    private int sp;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LexicalContext.class.desiredAssertionStatus();
    }

    public void setFlag(LexicalContextNode node, int flag) {
        if (flag != 0) {
            if (!$assertionsDisabled && flag == 1 && (node instanceof Block)) {
                throw new AssertionError();
            }
            for (int i2 = this.sp - 1; i2 >= 0; i2--) {
                if (this.stack[i2] == node) {
                    int[] iArr = this.flags;
                    int i3 = i2;
                    iArr[i3] = iArr[i3] | flag;
                    return;
                }
            }
        }
        if (!$assertionsDisabled) {
            throw new AssertionError();
        }
    }

    public void setBlockNeedsScope(Block block) {
        for (int i2 = this.sp - 1; i2 >= 0; i2--) {
            if (this.stack[i2] == block) {
                int[] iArr = this.flags;
                int i3 = i2;
                iArr[i3] = iArr[i3] | 1;
                for (int j2 = i2 - 1; j2 >= 0; j2--) {
                    if (this.stack[j2] instanceof FunctionNode) {
                        int[] iArr2 = this.flags;
                        int i4 = j2;
                        iArr2[i4] = iArr2[i4] | 128;
                        return;
                    }
                }
            }
        }
        if (!$assertionsDisabled) {
            throw new AssertionError();
        }
    }

    public int getFlags(LexicalContextNode node) {
        for (int i2 = this.sp - 1; i2 >= 0; i2--) {
            if (this.stack[i2] == node) {
                return this.flags[i2];
            }
        }
        throw new AssertionError((Object) "flag node not on context stack");
    }

    public Block getFunctionBody(FunctionNode functionNode) {
        for (int i2 = this.sp - 1; i2 >= 0; i2--) {
            if (this.stack[i2] == functionNode) {
                return (Block) this.stack[i2 + 1];
            }
        }
        throw new AssertionError((Object) (functionNode.getName() + " not on context stack"));
    }

    public Iterator<LexicalContextNode> getAllNodes() {
        return new NodeIterator(this, LexicalContextNode.class);
    }

    public FunctionNode getOutermostFunction() {
        return (FunctionNode) this.stack[0];
    }

    public <T extends LexicalContextNode> T push(T node) {
        if (!$assertionsDisabled && contains(node)) {
            throw new AssertionError();
        }
        if (this.sp == this.stack.length) {
            LexicalContextNode[] newStack = new LexicalContextNode[this.sp * 2];
            System.arraycopy(this.stack, 0, newStack, 0, this.sp);
            this.stack = newStack;
            int[] newFlags = new int[this.sp * 2];
            System.arraycopy(this.flags, 0, newFlags, 0, this.sp);
            this.flags = newFlags;
        }
        this.stack[this.sp] = node;
        this.flags[this.sp] = 0;
        this.sp++;
        return node;
    }

    public boolean isEmpty() {
        return this.sp == 0;
    }

    public int size() {
        return this.sp;
    }

    public <T extends Node> T pop(T node) {
        this.sp--;
        LexicalContextNode lexicalContextNode = this.stack[this.sp];
        this.stack[this.sp] = null;
        if (lexicalContextNode instanceof Flags) {
            return (T) ((Flags) lexicalContextNode).setFlag(this, this.flags[this.sp]);
        }
        return (T) lexicalContextNode;
    }

    public <T extends LexicalContextNode & Flags<T>> T applyTopFlags(T t2) {
        if ($assertionsDisabled || t2 == peek()) {
            return (T) ((Flags) t2).setFlag(this, this.flags[this.sp - 1]);
        }
        throw new AssertionError();
    }

    public LexicalContextNode peek() {
        return this.stack[this.sp - 1];
    }

    public boolean contains(LexicalContextNode node) {
        for (int i2 = 0; i2 < this.sp; i2++) {
            if (this.stack[i2] == node) {
                return true;
            }
        }
        return false;
    }

    public LexicalContextNode replace(LexicalContextNode oldNode, LexicalContextNode newNode) {
        int i2 = this.sp - 1;
        while (true) {
            if (i2 < 0) {
                break;
            }
            if (this.stack[i2] != oldNode) {
                i2--;
            } else {
                if (!$assertionsDisabled && i2 != this.sp - 1) {
                    throw new AssertionError((Object) ("violation of contract - we always expect to find the replacement node on top of the lexical context stack: " + ((Object) newNode) + " has " + ((Object) this.stack[i2 + 1].getClass()) + " above it"));
                }
                this.stack[i2] = newNode;
            }
        }
        return newNode;
    }

    public Iterator<Block> getBlocks() {
        return new NodeIterator(this, Block.class);
    }

    public Iterator<FunctionNode> getFunctions() {
        return new NodeIterator(this, FunctionNode.class);
    }

    public Block getParentBlock() {
        Iterator<Block> iter = new NodeIterator<>(Block.class, getCurrentFunction());
        iter.next();
        if (iter.hasNext()) {
            return iter.next();
        }
        return null;
    }

    public LabelNode getCurrentBlockLabelNode() {
        if (!$assertionsDisabled && !(this.stack[this.sp - 1] instanceof Block)) {
            throw new AssertionError();
        }
        if (this.sp < 2) {
            return null;
        }
        LexicalContextNode parent = this.stack[this.sp - 2];
        if (parent instanceof LabelNode) {
            return (LabelNode) parent;
        }
        return null;
    }

    public Iterator<Block> getAncestorBlocks(Block block) {
        Iterator<Block> iter = getBlocks();
        while (iter.hasNext()) {
            Block b2 = iter.next();
            if (block == b2) {
                return iter;
            }
        }
        throw new AssertionError((Object) "Block is not on the current lexical context stack");
    }

    public Iterator<Block> getBlocks(final Block block) {
        final Iterator<Block> iter = getAncestorBlocks(block);
        return new Iterator<Block>() { // from class: jdk.nashorn.internal.ir.LexicalContext.1
            boolean blockReturned = false;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return iter.hasNext() || !this.blockReturned;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Block next() {
                if (this.blockReturned) {
                    return (Block) iter.next();
                }
                this.blockReturned = true;
                return block;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public FunctionNode getFunction(Block block) {
        Iterator<LexicalContextNode> iter = new NodeIterator<>(this, LexicalContextNode.class);
        while (iter.hasNext()) {
            LexicalContextNode next = iter.next();
            if (next == block) {
                while (iter.hasNext()) {
                    LexicalContextNode next2 = iter.next();
                    if (next2 instanceof FunctionNode) {
                        return (FunctionNode) next2;
                    }
                }
            }
        }
        if ($assertionsDisabled) {
            return null;
        }
        throw new AssertionError();
    }

    public Block getCurrentBlock() {
        return getBlocks().next();
    }

    public FunctionNode getCurrentFunction() {
        for (int i2 = this.sp - 1; i2 >= 0; i2--) {
            if (this.stack[i2] instanceof FunctionNode) {
                return (FunctionNode) this.stack[i2];
            }
        }
        return null;
    }

    public Block getDefiningBlock(Symbol symbol) {
        String name = symbol.getName();
        Iterator<Block> it = getBlocks();
        while (it.hasNext()) {
            Block next = it.next();
            if (next.getExistingSymbol(name) == symbol) {
                return next;
            }
        }
        throw new AssertionError((Object) ("Couldn't find symbol " + name + " in the context"));
    }

    public FunctionNode getDefiningFunction(Symbol symbol) {
        String name = symbol.getName();
        Iterator<LexicalContextNode> iter = new NodeIterator<>(this, LexicalContextNode.class);
        while (iter.hasNext()) {
            LexicalContextNode next = iter.next();
            if ((next instanceof Block) && ((Block) next).getExistingSymbol(name) == symbol) {
                while (iter.hasNext()) {
                    LexicalContextNode next2 = iter.next();
                    if (next2 instanceof FunctionNode) {
                        return (FunctionNode) next2;
                    }
                }
                throw new AssertionError((Object) ("Defining block for symbol " + name + " has no function in the context"));
            }
        }
        throw new AssertionError((Object) ("Couldn't find symbol " + name + " in the context"));
    }

    public boolean isFunctionBody() {
        return getParentBlock() == null;
    }

    public boolean isSplitBody() {
        return this.sp >= 2 && (this.stack[this.sp - 1] instanceof Block) && (this.stack[this.sp - 2] instanceof SplitNode);
    }

    public FunctionNode getParentFunction(FunctionNode functionNode) {
        Iterator<FunctionNode> iter = new NodeIterator<>(this, FunctionNode.class);
        while (iter.hasNext()) {
            FunctionNode next = iter.next();
            if (next == functionNode) {
                if (iter.hasNext()) {
                    return iter.next();
                }
                return null;
            }
        }
        if ($assertionsDisabled) {
            return null;
        }
        throw new AssertionError();
    }

    public int getScopeNestingLevelTo(LexicalContextNode until) {
        LexicalContextNode node;
        if (!$assertionsDisabled && until == null) {
            throw new AssertionError();
        }
        int n2 = 0;
        Iterator<LexicalContextNode> iter = getAllNodes();
        while (iter.hasNext() && (node = iter.next()) != until) {
            if (!$assertionsDisabled && (node instanceof FunctionNode)) {
                throw new AssertionError();
            }
            if ((node instanceof WithNode) || ((node instanceof Block) && ((Block) node).needsScope())) {
                n2++;
            }
        }
        return n2;
    }

    private BreakableNode getBreakable() {
        NodeIterator<BreakableNode> iter = new NodeIterator<>(BreakableNode.class, getCurrentFunction());
        while (iter.hasNext()) {
            BreakableNode next = (BreakableNode) iter.next();
            if (next.isBreakableWithoutLabel()) {
                return next;
            }
        }
        return null;
    }

    public boolean inLoop() {
        return getCurrentLoop() != null;
    }

    public LoopNode getCurrentLoop() {
        Iterator<LoopNode> iter = new NodeIterator<>(LoopNode.class, getCurrentFunction());
        if (iter.hasNext()) {
            return iter.next();
        }
        return null;
    }

    public BreakableNode getBreakable(String labelName) {
        if (labelName != null) {
            LabelNode foundLabel = findLabel(labelName);
            if (foundLabel != null) {
                BreakableNode breakable = null;
                NodeIterator<BreakableNode> iter = new NodeIterator<>(BreakableNode.class, foundLabel);
                while (iter.hasNext()) {
                    breakable = (BreakableNode) iter.next();
                }
                return breakable;
            }
            return null;
        }
        return getBreakable();
    }

    private LoopNode getContinueTo() {
        return getCurrentLoop();
    }

    public LoopNode getContinueTo(String labelName) {
        if (labelName != null) {
            LabelNode foundLabel = findLabel(labelName);
            if (foundLabel != null) {
                LoopNode loop = null;
                NodeIterator<LoopNode> iter = new NodeIterator<>(LoopNode.class, foundLabel);
                while (iter.hasNext()) {
                    loop = (LoopNode) iter.next();
                }
                return loop;
            }
            return null;
        }
        return getContinueTo();
    }

    public Block getInlinedFinally(String labelName) {
        NodeIterator<TryNode> iter = new NodeIterator<>(this, TryNode.class);
        while (iter.hasNext()) {
            Block inlinedFinally = ((TryNode) iter.next()).getInlinedFinally(labelName);
            if (inlinedFinally != null) {
                return inlinedFinally;
            }
        }
        return null;
    }

    public TryNode getTryNodeForInlinedFinally(String labelName) {
        NodeIterator<TryNode> iter = new NodeIterator<>(this, TryNode.class);
        while (iter.hasNext()) {
            TryNode tryNode = (TryNode) iter.next();
            if (tryNode.getInlinedFinally(labelName) != null) {
                return tryNode;
            }
        }
        return null;
    }

    public LabelNode findLabel(String name) {
        Iterator<LabelNode> iter = new NodeIterator<>(LabelNode.class, getCurrentFunction());
        while (iter.hasNext()) {
            LabelNode next = iter.next();
            if (next.getLabelName().equals(name)) {
                return next;
            }
        }
        return null;
    }

    public boolean isExternalTarget(SplitNode splitNode, BreakableNode target) {
        int i2 = this.sp;
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 > 0) {
                LexicalContextNode next = this.stack[i2];
                if (next == splitNode) {
                    return true;
                }
                if (next == target) {
                    return false;
                }
                if (next instanceof TryNode) {
                    for (Block inlinedFinally : ((TryNode) next).getInlinedFinallies()) {
                        if (TryNode.getLabelledInlinedFinallyBlock(inlinedFinally) == target) {
                            return false;
                        }
                    }
                }
            } else {
                throw new AssertionError((Object) (((Object) target) + " was expected in lexical context " + ((Object) this) + " but wasn't"));
            }
        }
    }

    public boolean inUnprotectedSwitchContext() {
        for (int i2 = this.sp; i2 > 0; i2--) {
            LexicalContextNode next = this.stack[i2];
            if (next instanceof Block) {
                return this.stack[i2 - 1] instanceof SwitchNode;
            }
        }
        return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        for (int i2 = 0; i2 < this.sp; i2++) {
            Object node = this.stack[i2];
            sb.append(node.getClass().getSimpleName());
            sb.append('@');
            sb.append(Debug.id(node));
            sb.append(':');
            if (node instanceof FunctionNode) {
                FunctionNode fn = (FunctionNode) node;
                Source source = fn.getSource();
                String src = source.toString();
                if (src.contains(File.pathSeparator)) {
                    src = src.substring(src.lastIndexOf(File.pathSeparator));
                }
                sb.append((src + ' ') + fn.getLineNumber());
            }
            sb.append(' ');
        }
        sb.append(" ==> ]");
        return sb.toString();
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LexicalContext$NodeIterator.class */
    private class NodeIterator<T extends LexicalContextNode> implements Iterator<T> {
        private int index;
        private T next;
        private final Class<T> clazz;
        private LexicalContextNode until;

        NodeIterator(LexicalContext lexicalContext, Class<T> clazz) {
            this(clazz, null);
        }

        NodeIterator(Class<T> cls, LexicalContextNode lexicalContextNode) {
            this.index = LexicalContext.this.sp - 1;
            this.clazz = cls;
            this.until = lexicalContextNode;
            this.next = (T) findNext();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            T t2 = this.next;
            this.next = (T) findNext();
            return t2;
        }

        private T findNext() {
            LexicalContextNode node;
            for (int i2 = this.index; i2 >= 0 && (node = LexicalContext.this.stack[i2]) != this.until; i2--) {
                if (this.clazz.isAssignableFrom(node.getClass())) {
                    this.index = i2 - 1;
                    return (T) node;
                }
            }
            return null;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
