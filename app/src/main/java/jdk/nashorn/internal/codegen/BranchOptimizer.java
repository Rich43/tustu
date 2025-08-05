package jdk.nashorn.internal.codegen;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.parser.TokenType;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/BranchOptimizer.class */
final class BranchOptimizer {
    private final CodeGenerator codegen;
    private final MethodEmitter method;

    BranchOptimizer(CodeGenerator codegen, MethodEmitter method) {
        this.codegen = codegen;
        this.method = method;
    }

    void execute(Expression node, Label label, boolean state) {
        branchOptimizer(node, label, state);
    }

    private void branchOptimizer(UnaryNode unaryNode, Label label, boolean state) {
        if (unaryNode.isTokenType(TokenType.NOT)) {
            branchOptimizer(unaryNode.getExpression(), label, !state);
        } else {
            loadTestAndJump(unaryNode, label, state);
        }
    }

    private void branchOptimizer(BinaryNode binaryNode, Label label, boolean state) {
        Expression lhs = binaryNode.lhs();
        Expression rhs = binaryNode.rhs();
        switch (binaryNode.tokenType()) {
            case AND:
                if (state) {
                    Label skip = new Label(SchemaSymbols.ATTVAL_SKIP);
                    optimizeLogicalOperand(lhs, skip, false, false);
                    optimizeLogicalOperand(rhs, label, true, true);
                    this.method.label(skip);
                    break;
                } else {
                    optimizeLogicalOperand(lhs, label, false, false);
                    optimizeLogicalOperand(rhs, label, false, true);
                    break;
                }
            case OR:
                if (state) {
                    optimizeLogicalOperand(lhs, label, true, false);
                    optimizeLogicalOperand(rhs, label, true, true);
                    break;
                } else {
                    Label skip2 = new Label(SchemaSymbols.ATTVAL_SKIP);
                    optimizeLogicalOperand(lhs, skip2, true, false);
                    optimizeLogicalOperand(rhs, label, false, true);
                    this.method.label(skip2);
                    break;
                }
            case EQ:
            case EQ_STRICT:
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.EQ : Condition.NE, true, label);
                break;
            case NE:
            case NE_STRICT:
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.NE : Condition.EQ, true, label);
                break;
            case GE:
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.GE : Condition.LT, false, label);
                break;
            case GT:
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.GT : Condition.LE, false, label);
                break;
            case LE:
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.LE : Condition.GT, true, label);
                break;
            case LT:
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.LT : Condition.GE, true, label);
                break;
            default:
                loadTestAndJump(binaryNode, label, state);
                break;
        }
    }

    private void optimizeLogicalOperand(Expression expr, Label label, boolean state, boolean isRhs) {
        JoinPredecessorExpression jpexpr = (JoinPredecessorExpression) expr;
        if (LocalVariableConversion.hasLiveConversion(jpexpr)) {
            Label after = new Label("after");
            branchOptimizer(jpexpr.getExpression(), after, !state);
            this.method.beforeJoinPoint(jpexpr);
            this.method._goto(label);
            this.method.label(after);
            if (isRhs) {
                this.method.beforeJoinPoint(jpexpr);
                return;
            }
            return;
        }
        branchOptimizer(jpexpr.getExpression(), label, state);
    }

    private void branchOptimizer(Expression node, Label label, boolean state) {
        if (node instanceof BinaryNode) {
            branchOptimizer((BinaryNode) node, label, state);
        } else if (node instanceof UnaryNode) {
            branchOptimizer((UnaryNode) node, label, state);
        } else {
            loadTestAndJump(node, label, state);
        }
    }

    private void loadTestAndJump(Expression node, Label label, boolean state) {
        this.codegen.loadExpressionAsBoolean(node);
        if (state) {
            this.method.ifne(label);
        } else {
            this.method.ifeq(label);
        }
    }
}
