package jdk.nashorn.internal.codegen;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CacheAst.class */
class CacheAst extends SimpleNodeVisitor {
    private final Deque<RecompilableScriptFunctionData> dataStack = new ArrayDeque();
    private final Compiler compiler;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CacheAst.class.desiredAssertionStatus();
    }

    CacheAst(Compiler compiler) {
        this.compiler = compiler;
        if (!$assertionsDisabled && compiler.isOnDemandCompilation()) {
            throw new AssertionError();
        }
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterFunctionNode(FunctionNode functionNode) {
        int id = functionNode.getId();
        this.dataStack.push(this.dataStack.isEmpty() ? this.compiler.getScriptFunctionData(id) : this.dataStack.peek().getScriptFunctionData(id));
        return true;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveFunctionNode(FunctionNode functionNode) {
        RecompilableScriptFunctionData data = this.dataStack.pop();
        if (functionNode.isSplit()) {
            data.setCachedAst(functionNode);
        }
        if (!this.dataStack.isEmpty() && (this.dataStack.peek().getFunctionFlags() & 16) != 0) {
            return functionNode.setBody(this.lc, functionNode.getBody().setStatements(null, Collections.emptyList()));
        }
        return functionNode;
    }
}
