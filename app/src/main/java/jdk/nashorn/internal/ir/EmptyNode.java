package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/EmptyNode.class */
public final class EmptyNode extends Statement {
    private static final long serialVersionUID = 1;

    public EmptyNode(Statement node) {
        super(node);
    }

    public EmptyNode(int lineNumber, long token, int finish) {
        super(lineNumber, token, finish);
    }

    @Override // jdk.nashorn.internal.ir.Node
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterEmptyNode(this)) {
            return visitor.leaveEmptyNode(this);
        }
        return this;
    }

    @Override // jdk.nashorn.internal.ir.Node
    public void toString(StringBuilder sb, boolean printTypes) {
        sb.append(';');
    }
}
