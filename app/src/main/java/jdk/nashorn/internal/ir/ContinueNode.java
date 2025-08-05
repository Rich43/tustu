package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/ContinueNode.class */
public class ContinueNode extends JumpStatement {
    private static final long serialVersionUID = 1;

    public ContinueNode(int lineNumber, long token, int finish, String labelName) {
        super(lineNumber, token, finish, labelName);
    }

    private ContinueNode(ContinueNode continueNode, LocalVariableConversion conversion) {
        super(continueNode, conversion);
    }

    @Override // jdk.nashorn.internal.ir.Node
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterContinueNode(this)) {
            return visitor.leaveContinueNode(this);
        }
        return this;
    }

    @Override // jdk.nashorn.internal.ir.JumpStatement
    JumpStatement createNewJumpStatement(LocalVariableConversion conversion) {
        return new ContinueNode(this, conversion);
    }

    @Override // jdk.nashorn.internal.ir.JumpStatement
    String getStatementName() {
        return "continue";
    }

    @Override // jdk.nashorn.internal.ir.JumpStatement
    public BreakableNode getTarget(LexicalContext lc) {
        return lc.getContinueTo(getLabelName());
    }

    @Override // jdk.nashorn.internal.ir.JumpStatement
    Label getTargetLabel(BreakableNode target) {
        return ((LoopNode) target).getContinueLabel();
    }
}
