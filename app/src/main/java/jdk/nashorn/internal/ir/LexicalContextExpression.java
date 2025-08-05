package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LexicalContextExpression.class */
abstract class LexicalContextExpression extends Expression implements LexicalContextNode {
    private static final long serialVersionUID = 1;

    LexicalContextExpression(LexicalContextExpression expr) {
        super(expr);
    }

    LexicalContextExpression(long token, int start, int finish) {
        super(token, start, finish);
    }

    LexicalContextExpression(long token, int finish) {
        super(token, finish);
    }

    @Override // jdk.nashorn.internal.ir.Node
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        return LexicalContextNode.Acceptor.accept(this, visitor);
    }
}
