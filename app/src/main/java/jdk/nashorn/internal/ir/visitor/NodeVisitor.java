package jdk.nashorn.internal.ir.visitor;

import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.SetSplitState;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.SplitReturn;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.WithNode;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/visitor/NodeVisitor.class */
public abstract class NodeVisitor<T extends LexicalContext> {
    protected final T lc;

    public NodeVisitor(T lc) {
        this.lc = lc;
    }

    public T getLexicalContext() {
        return this.lc;
    }

    protected boolean enterDefault(Node node) {
        return true;
    }

    protected Node leaveDefault(Node node) {
        return node;
    }

    public boolean enterAccessNode(AccessNode accessNode) {
        return enterDefault(accessNode);
    }

    public Node leaveAccessNode(AccessNode accessNode) {
        return leaveDefault(accessNode);
    }

    public boolean enterBlock(Block block) {
        return enterDefault(block);
    }

    public Node leaveBlock(Block block) {
        return leaveDefault(block);
    }

    public boolean enterBinaryNode(BinaryNode binaryNode) {
        return enterDefault(binaryNode);
    }

    public Node leaveBinaryNode(BinaryNode binaryNode) {
        return leaveDefault(binaryNode);
    }

    public boolean enterBreakNode(BreakNode breakNode) {
        return enterDefault(breakNode);
    }

    public Node leaveBreakNode(BreakNode breakNode) {
        return leaveDefault(breakNode);
    }

    public boolean enterCallNode(CallNode callNode) {
        return enterDefault(callNode);
    }

    public Node leaveCallNode(CallNode callNode) {
        return leaveDefault(callNode);
    }

    public boolean enterCaseNode(CaseNode caseNode) {
        return enterDefault(caseNode);
    }

    public Node leaveCaseNode(CaseNode caseNode) {
        return leaveDefault(caseNode);
    }

    public boolean enterCatchNode(CatchNode catchNode) {
        return enterDefault(catchNode);
    }

    public Node leaveCatchNode(CatchNode catchNode) {
        return leaveDefault(catchNode);
    }

    public boolean enterContinueNode(ContinueNode continueNode) {
        return enterDefault(continueNode);
    }

    public Node leaveContinueNode(ContinueNode continueNode) {
        return leaveDefault(continueNode);
    }

    public boolean enterEmptyNode(EmptyNode emptyNode) {
        return enterDefault(emptyNode);
    }

    public Node leaveEmptyNode(EmptyNode emptyNode) {
        return leaveDefault(emptyNode);
    }

    public boolean enterExpressionStatement(ExpressionStatement expressionStatement) {
        return enterDefault(expressionStatement);
    }

    public Node leaveExpressionStatement(ExpressionStatement expressionStatement) {
        return leaveDefault(expressionStatement);
    }

    public boolean enterBlockStatement(BlockStatement blockStatement) {
        return enterDefault(blockStatement);
    }

    public Node leaveBlockStatement(BlockStatement blockStatement) {
        return leaveDefault(blockStatement);
    }

    public boolean enterForNode(ForNode forNode) {
        return enterDefault(forNode);
    }

    public Node leaveForNode(ForNode forNode) {
        return leaveDefault(forNode);
    }

    public boolean enterFunctionNode(FunctionNode functionNode) {
        return enterDefault(functionNode);
    }

    public Node leaveFunctionNode(FunctionNode functionNode) {
        return leaveDefault(functionNode);
    }

    public boolean enterGetSplitState(GetSplitState getSplitState) {
        return enterDefault(getSplitState);
    }

    public Node leaveGetSplitState(GetSplitState getSplitState) {
        return leaveDefault(getSplitState);
    }

    public boolean enterIdentNode(IdentNode identNode) {
        return enterDefault(identNode);
    }

    public Node leaveIdentNode(IdentNode identNode) {
        return leaveDefault(identNode);
    }

    public boolean enterIfNode(IfNode ifNode) {
        return enterDefault(ifNode);
    }

    public Node leaveIfNode(IfNode ifNode) {
        return leaveDefault(ifNode);
    }

    public boolean enterIndexNode(IndexNode indexNode) {
        return enterDefault(indexNode);
    }

    public Node leaveIndexNode(IndexNode indexNode) {
        return leaveDefault(indexNode);
    }

    public boolean enterJumpToInlinedFinally(JumpToInlinedFinally jumpToInlinedFinally) {
        return enterDefault(jumpToInlinedFinally);
    }

    public Node leaveJumpToInlinedFinally(JumpToInlinedFinally jumpToInlinedFinally) {
        return leaveDefault(jumpToInlinedFinally);
    }

    public boolean enterLabelNode(LabelNode labelNode) {
        return enterDefault(labelNode);
    }

    public Node leaveLabelNode(LabelNode labelNode) {
        return leaveDefault(labelNode);
    }

    public boolean enterLiteralNode(LiteralNode<?> literalNode) {
        return enterDefault(literalNode);
    }

    public Node leaveLiteralNode(LiteralNode<?> literalNode) {
        return leaveDefault(literalNode);
    }

    public boolean enterObjectNode(ObjectNode objectNode) {
        return enterDefault(objectNode);
    }

    public Node leaveObjectNode(ObjectNode objectNode) {
        return leaveDefault(objectNode);
    }

    public boolean enterPropertyNode(PropertyNode propertyNode) {
        return enterDefault(propertyNode);
    }

    public Node leavePropertyNode(PropertyNode propertyNode) {
        return leaveDefault(propertyNode);
    }

    public boolean enterReturnNode(ReturnNode returnNode) {
        return enterDefault(returnNode);
    }

    public Node leaveReturnNode(ReturnNode returnNode) {
        return leaveDefault(returnNode);
    }

    public boolean enterRuntimeNode(RuntimeNode runtimeNode) {
        return enterDefault(runtimeNode);
    }

    public Node leaveRuntimeNode(RuntimeNode runtimeNode) {
        return leaveDefault(runtimeNode);
    }

    public boolean enterSetSplitState(SetSplitState setSplitState) {
        return enterDefault(setSplitState);
    }

    public Node leaveSetSplitState(SetSplitState setSplitState) {
        return leaveDefault(setSplitState);
    }

    public boolean enterSplitNode(SplitNode splitNode) {
        return enterDefault(splitNode);
    }

    public Node leaveSplitNode(SplitNode splitNode) {
        return leaveDefault(splitNode);
    }

    public boolean enterSplitReturn(SplitReturn splitReturn) {
        return enterDefault(splitReturn);
    }

    public Node leaveSplitReturn(SplitReturn splitReturn) {
        return leaveDefault(splitReturn);
    }

    public boolean enterSwitchNode(SwitchNode switchNode) {
        return enterDefault(switchNode);
    }

    public Node leaveSwitchNode(SwitchNode switchNode) {
        return leaveDefault(switchNode);
    }

    public boolean enterTernaryNode(TernaryNode ternaryNode) {
        return enterDefault(ternaryNode);
    }

    public Node leaveTernaryNode(TernaryNode ternaryNode) {
        return leaveDefault(ternaryNode);
    }

    public boolean enterThrowNode(ThrowNode throwNode) {
        return enterDefault(throwNode);
    }

    public Node leaveThrowNode(ThrowNode throwNode) {
        return leaveDefault(throwNode);
    }

    public boolean enterTryNode(TryNode tryNode) {
        return enterDefault(tryNode);
    }

    public Node leaveTryNode(TryNode tryNode) {
        return leaveDefault(tryNode);
    }

    public boolean enterUnaryNode(UnaryNode unaryNode) {
        return enterDefault(unaryNode);
    }

    public Node leaveUnaryNode(UnaryNode unaryNode) {
        return leaveDefault(unaryNode);
    }

    public boolean enterJoinPredecessorExpression(JoinPredecessorExpression expr) {
        return enterDefault(expr);
    }

    public Node leaveJoinPredecessorExpression(JoinPredecessorExpression expr) {
        return leaveDefault(expr);
    }

    public boolean enterVarNode(VarNode varNode) {
        return enterDefault(varNode);
    }

    public Node leaveVarNode(VarNode varNode) {
        return leaveDefault(varNode);
    }

    public boolean enterWhileNode(WhileNode whileNode) {
        return enterDefault(whileNode);
    }

    public Node leaveWhileNode(WhileNode whileNode) {
        return leaveDefault(whileNode);
    }

    public boolean enterWithNode(WithNode withNode) {
        return enterDefault(withNode);
    }

    public Node leaveWithNode(WithNode withNode) {
        return leaveDefault(withNode);
    }
}
