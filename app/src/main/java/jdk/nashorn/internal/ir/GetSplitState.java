package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.runtime.Scope;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/GetSplitState.class */
public final class GetSplitState extends Expression {
    private static final long serialVersionUID = 1;
    public static final GetSplitState INSTANCE = new GetSplitState();

    private GetSplitState() {
        super(0L, 0);
    }

    @Override // jdk.nashorn.internal.ir.Expression
    public Type getType() {
        return Type.INT;
    }

    @Override // jdk.nashorn.internal.ir.Node
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        return visitor.enterGetSplitState(this) ? visitor.leaveGetSplitState(this) : this;
    }

    @Override // jdk.nashorn.internal.ir.Node
    public void toString(StringBuilder sb, boolean printType) {
        if (printType) {
            sb.append("{I}");
        }
        sb.append(CompilerConstants.SCOPE.symbolName()).append('.').append(Scope.GET_SPLIT_STATE.name()).append("()");
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
