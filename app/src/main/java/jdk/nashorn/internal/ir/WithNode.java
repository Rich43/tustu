package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/WithNode.class */
public final class WithNode extends LexicalContextStatement {
    private static final long serialVersionUID = 1;
    private final Expression expression;
    private final Block body;

    @Override // jdk.nashorn.internal.ir.LexicalContextStatement, jdk.nashorn.internal.ir.Node
    public /* bridge */ /* synthetic */ Node accept(NodeVisitor nodeVisitor) {
        return super.accept(nodeVisitor);
    }

    public WithNode(int lineNumber, long token, int finish) {
        super(lineNumber, token, finish);
        this.expression = null;
        this.body = null;
    }

    private WithNode(WithNode node, Expression expression, Block body) {
        super(node);
        this.expression = expression;
        this.body = body;
    }

    @Override // jdk.nashorn.internal.ir.LexicalContextNode
    public Node accept(LexicalContext lc, NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterWithNode(this)) {
            return visitor.leaveWithNode(setExpression(lc, (Expression) this.expression.accept(visitor)).setBody(lc, (Block) this.body.accept(visitor)));
        }
        return this;
    }

    @Override // jdk.nashorn.internal.ir.Statement, jdk.nashorn.internal.ir.Terminal
    public boolean isTerminal() {
        return this.body.isTerminal();
    }

    @Override // jdk.nashorn.internal.ir.Node
    public void toString(StringBuilder sb, boolean printType) {
        sb.append("with (");
        this.expression.toString(sb, printType);
        sb.append(')');
    }

    public Block getBody() {
        return this.body;
    }

    public WithNode setBody(LexicalContext lc, Block body) {
        if (this.body == body) {
            return this;
        }
        return (WithNode) Node.replaceInLexicalContext(lc, this, new WithNode(this, this.expression, body));
    }

    public Expression getExpression() {
        return this.expression;
    }

    public WithNode setExpression(LexicalContext lc, Expression expression) {
        if (this.expression == expression) {
            return this;
        }
        return (WithNode) Node.replaceInLexicalContext(lc, this, new WithNode(this, expression, this.body));
    }
}
