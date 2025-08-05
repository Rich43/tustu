package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/SplitReturn.class */
public final class SplitReturn extends Statement {
    private static final long serialVersionUID = 1;
    public static final SplitReturn INSTANCE = new SplitReturn();

    private SplitReturn() {
        super(-1, 0L, 0);
    }

    @Override // jdk.nashorn.internal.ir.Statement, jdk.nashorn.internal.ir.Terminal
    public boolean isTerminal() {
        return true;
    }

    @Override // jdk.nashorn.internal.ir.Node
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        return visitor.enterSplitReturn(this) ? visitor.leaveSplitReturn(this) : this;
    }

    @Override // jdk.nashorn.internal.ir.Node
    public void toString(StringBuilder sb, boolean printType) {
        sb.append(":splitreturn;");
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
