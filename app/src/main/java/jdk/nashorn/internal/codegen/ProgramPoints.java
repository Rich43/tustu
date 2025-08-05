package jdk.nashorn.internal.codegen;

import java.util.HashSet;
import java.util.Set;
import jdk.nashorn.internal.IntDeque;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/ProgramPoints.class */
class ProgramPoints extends SimpleNodeVisitor {
    private final IntDeque nextProgramPoint = new IntDeque();
    private final Set<Node> noProgramPoint = new HashSet();

    ProgramPoints() {
    }

    private int next() {
        int next = this.nextProgramPoint.getAndIncrement();
        if (next > 2097151) {
            throw new AssertionError((Object) "Function has more than 2097151 program points");
        }
        return next;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterFunctionNode(FunctionNode functionNode) {
        this.nextProgramPoint.push(1);
        return true;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveFunctionNode(FunctionNode functionNode) {
        this.nextProgramPoint.pop();
        return functionNode;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Expression setProgramPoint(Optimistic optimistic) {
        if (this.noProgramPoint.contains(optimistic)) {
            return (Expression) optimistic;
        }
        return (Expression) (optimistic.canBeOptimistic() ? optimistic.setProgramPoint(next()) : optimistic);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterVarNode(VarNode varNode) {
        this.noProgramPoint.add(varNode.getName());
        return true;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterIdentNode(IdentNode identNode) {
        if (identNode.isInternal()) {
            this.noProgramPoint.add(identNode);
            return true;
        }
        return true;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveIdentNode(IdentNode identNode) {
        if (identNode.isPropertyName()) {
            return identNode;
        }
        return setProgramPoint(identNode);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveCallNode(CallNode callNode) {
        return setProgramPoint(callNode);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveAccessNode(AccessNode accessNode) {
        return setProgramPoint(accessNode);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveIndexNode(IndexNode indexNode) {
        return setProgramPoint(indexNode);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveBinaryNode(BinaryNode binaryNode) {
        return setProgramPoint(binaryNode);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveUnaryNode(UnaryNode unaryNode) {
        return setProgramPoint(unaryNode);
    }
}
