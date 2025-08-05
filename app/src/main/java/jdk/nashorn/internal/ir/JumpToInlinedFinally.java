package jdk.nashorn.internal.ir;

import java.util.Objects;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/JumpToInlinedFinally.class */
public final class JumpToInlinedFinally extends JumpStatement {
    private static final long serialVersionUID = 1;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JumpToInlinedFinally.class.desiredAssertionStatus();
    }

    public JumpToInlinedFinally(String labelName) {
        super(-1, 0L, 0, (String) Objects.requireNonNull(labelName));
    }

    private JumpToInlinedFinally(JumpToInlinedFinally breakNode, LocalVariableConversion conversion) {
        super(breakNode, conversion);
    }

    @Override // jdk.nashorn.internal.ir.Node
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterJumpToInlinedFinally(this)) {
            return visitor.leaveJumpToInlinedFinally(this);
        }
        return this;
    }

    @Override // jdk.nashorn.internal.ir.JumpStatement
    JumpStatement createNewJumpStatement(LocalVariableConversion conversion) {
        return new JumpToInlinedFinally(this, conversion);
    }

    @Override // jdk.nashorn.internal.ir.JumpStatement
    String getStatementName() {
        return ":jumpToInlinedFinally";
    }

    @Override // jdk.nashorn.internal.ir.JumpStatement
    public Block getTarget(LexicalContext lc) {
        return lc.getInlinedFinally(getLabelName());
    }

    @Override // jdk.nashorn.internal.ir.JumpStatement
    public TryNode getPopScopeLimit(LexicalContext lc) {
        return lc.getTryNodeForInlinedFinally(getLabelName());
    }

    @Override // jdk.nashorn.internal.ir.JumpStatement
    Label getTargetLabel(BreakableNode target) {
        if ($assertionsDisabled || target != null) {
            return ((Block) target).getEntryLabel();
        }
        throw new AssertionError();
    }
}
