package jdk.nashorn.internal.ir;

import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/BreakableStatement.class */
abstract class BreakableStatement extends LexicalContextStatement implements BreakableNode {
    private static final long serialVersionUID = 1;
    protected final Label breakLabel;
    final LocalVariableConversion conversion;

    abstract JoinPredecessor setLocalVariableConversionChanged(LexicalContext lexicalContext, LocalVariableConversion localVariableConversion);

    protected BreakableStatement(int lineNumber, long token, int finish, Label breakLabel) {
        super(lineNumber, token, finish);
        this.breakLabel = breakLabel;
        this.conversion = null;
    }

    protected BreakableStatement(BreakableStatement breakableNode, LocalVariableConversion conversion) {
        super(breakableNode);
        this.breakLabel = new Label(breakableNode.getBreakLabel());
        this.conversion = conversion;
    }

    @Override // jdk.nashorn.internal.ir.BreakableNode
    public boolean isBreakableWithoutLabel() {
        return true;
    }

    @Override // jdk.nashorn.internal.ir.BreakableNode
    public Label getBreakLabel() {
        return this.breakLabel;
    }

    @Override // jdk.nashorn.internal.ir.Labels
    public List<Label> getLabels() {
        return Collections.unmodifiableList(Collections.singletonList(this.breakLabel));
    }

    @Override // jdk.nashorn.internal.ir.JoinPredecessor
    public JoinPredecessor setLocalVariableConversion(LexicalContext lc, LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return setLocalVariableConversionChanged(lc, conversion);
    }

    @Override // jdk.nashorn.internal.ir.JoinPredecessor
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
}
