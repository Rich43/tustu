package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LexicalContextStatement.class */
abstract class LexicalContextStatement extends Statement implements LexicalContextNode {
    private static final long serialVersionUID = 1;

    protected LexicalContextStatement(int lineNumber, long token, int finish) {
        super(lineNumber, token, finish);
    }

    protected LexicalContextStatement(LexicalContextStatement node) {
        super(node);
    }

    @Override // jdk.nashorn.internal.ir.Node
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        return LexicalContextNode.Acceptor.accept(this, visitor);
    }
}
