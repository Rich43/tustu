package jdk.nashorn.internal.codegen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;

@Logger(name = "scopedepths")
/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/FindScopeDepths.class */
final class FindScopeDepths extends SimpleNodeVisitor implements Loggable {
    private final Compiler compiler;
    private final Map<Integer, Map<Integer, RecompilableScriptFunctionData>> fnIdToNestedFunctions = new HashMap();
    private final Map<Integer, Map<String, Integer>> externalSymbolDepths = new HashMap();
    private final Map<Integer, Set<String>> internalSymbols = new HashMap();
    private final Set<Block> withBodies = new HashSet();
    private final DebugLogger log;
    private int dynamicScopeCount;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FindScopeDepths.class.desiredAssertionStatus();
    }

    FindScopeDepths(Compiler compiler) {
        this.compiler = compiler;
        this.log = initLogger(compiler.getContext());
    }

    @Override // jdk.nashorn.internal.runtime.logging.Loggable
    public DebugLogger getLogger() {
        return this.log;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // jdk.nashorn.internal.runtime.logging.Loggable
    public DebugLogger initLogger(Context context) {
        return context.getLogger(getClass());
    }

    static int findScopesToStart(LexicalContext lc, FunctionNode fn, Block block) {
        Block bodyBlock = findBodyBlock(lc, fn, block);
        Iterator<Block> iter = lc.getBlocks(block);
        Block b2 = iter.next();
        int scopesToStart = 0;
        while (true) {
            if (b2.needsScope()) {
                scopesToStart++;
            }
            if (b2 != bodyBlock) {
                b2 = iter.next();
            } else {
                return scopesToStart;
            }
        }
    }

    static int findInternalDepth(LexicalContext lc, FunctionNode fn, Block block, Symbol symbol) {
        Block bodyBlock = findBodyBlock(lc, fn, block);
        Iterator<Block> iter = lc.getBlocks(block);
        int scopesToStart = 0;
        for (Block b2 = iter.next(); !definedInBlock(b2, symbol); b2 = iter.next()) {
            if (b2.needsScope()) {
                scopesToStart++;
            }
            if (b2 == bodyBlock) {
                return -1;
            }
        }
        return scopesToStart;
    }

    private static boolean definedInBlock(Block block, Symbol symbol) {
        if (!symbol.isGlobal()) {
            return block.getExistingSymbol(symbol.getName()) == symbol;
        }
        if (block.isGlobalScope()) {
            return true;
        }
        return false;
    }

    static Block findBodyBlock(LexicalContext lc, FunctionNode fn, Block block) {
        Iterator<Block> iter = lc.getBlocks(block);
        while (iter.hasNext()) {
            Block next = iter.next();
            if (fn.getBody() == next) {
                return next;
            }
        }
        return null;
    }

    private static Block findGlobalBlock(LexicalContext lc, Block block) {
        Iterator<Block> iter = lc.getBlocks(block);
        Block next = null;
        while (true) {
            Block globalBlock = next;
            if (iter.hasNext()) {
                next = iter.next();
            } else {
                return globalBlock;
            }
        }
    }

    private static boolean isDynamicScopeBoundary(FunctionNode fn) {
        return fn.needsDynamicScope();
    }

    private boolean isDynamicScopeBoundary(Block block) {
        return this.withBodies.contains(block);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterFunctionNode(FunctionNode functionNode) {
        if (this.compiler.isOnDemandCompilation()) {
            return true;
        }
        if (isDynamicScopeBoundary(functionNode)) {
            increaseDynamicScopeCount(functionNode);
        }
        int fnId = functionNode.getId();
        Map<Integer, RecompilableScriptFunctionData> nestedFunctions = this.fnIdToNestedFunctions.get(Integer.valueOf(fnId));
        if (nestedFunctions == null) {
            Map<Integer, RecompilableScriptFunctionData> nestedFunctions2 = new HashMap<>();
            this.fnIdToNestedFunctions.put(Integer.valueOf(fnId), nestedFunctions2);
            return true;
        }
        return true;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveFunctionNode(FunctionNode functionNode) {
        String name = functionNode.getName();
        FunctionNode newFunctionNode = functionNode;
        if (this.compiler.isOnDemandCompilation()) {
            RecompilableScriptFunctionData data = this.compiler.getScriptFunctionData(newFunctionNode.getId());
            if (data.inDynamicContext()) {
                this.log.fine("Reviving scriptfunction ", DebugLogger.quote(name), " as defined in previous (now lost) dynamic scope.");
                newFunctionNode = newFunctionNode.setInDynamicContext(this.lc);
            }
            if (newFunctionNode == this.lc.getOutermostFunction() && !newFunctionNode.hasApplyToCallSpecialization()) {
                data.setCachedAst(newFunctionNode);
            }
            return newFunctionNode;
        }
        if (inDynamicScope()) {
            this.log.fine("Tagging ", DebugLogger.quote(name), " as defined in dynamic scope");
            newFunctionNode = newFunctionNode.setInDynamicContext(this.lc);
        }
        int fnId = newFunctionNode.getId();
        Map<Integer, RecompilableScriptFunctionData> nestedFunctions = this.fnIdToNestedFunctions.remove(Integer.valueOf(fnId));
        if (!$assertionsDisabled && nestedFunctions == null) {
            throw new AssertionError();
        }
        RecompilableScriptFunctionData data2 = new RecompilableScriptFunctionData(newFunctionNode, this.compiler.getCodeInstaller(), ObjectClassGenerator.createAllocationStrategy(newFunctionNode.getThisProperties(), this.compiler.getContext().useDualFields()), nestedFunctions, this.externalSymbolDepths.get(Integer.valueOf(fnId)), this.internalSymbols.get(Integer.valueOf(fnId)));
        if (this.lc.getOutermostFunction() != newFunctionNode) {
            FunctionNode parentFn = this.lc.getParentFunction(newFunctionNode);
            if (parentFn != null) {
                this.fnIdToNestedFunctions.get(Integer.valueOf(parentFn.getId())).put(Integer.valueOf(fnId), data2);
            }
        } else {
            this.compiler.setData(data2);
        }
        if (isDynamicScopeBoundary(functionNode)) {
            decreaseDynamicScopeCount(functionNode);
        }
        return newFunctionNode;
    }

    private boolean inDynamicScope() {
        return this.dynamicScopeCount > 0;
    }

    private void increaseDynamicScopeCount(Node node) {
        if (!$assertionsDisabled && this.dynamicScopeCount < 0) {
            throw new AssertionError();
        }
        this.dynamicScopeCount++;
        if (this.log.isEnabled()) {
            this.log.finest(DebugLogger.quote(this.lc.getCurrentFunction().getName()), " ++dynamicScopeCount = ", Integer.valueOf(this.dynamicScopeCount), " at: ", node, node.getClass());
        }
    }

    private void decreaseDynamicScopeCount(Node node) {
        this.dynamicScopeCount--;
        if (!$assertionsDisabled && this.dynamicScopeCount < 0) {
            throw new AssertionError();
        }
        if (this.log.isEnabled()) {
            this.log.finest(DebugLogger.quote(this.lc.getCurrentFunction().getName()), " --dynamicScopeCount = ", Integer.valueOf(this.dynamicScopeCount), " at: ", node, node.getClass());
        }
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterWithNode(WithNode node) {
        this.withBodies.add(node.getBody());
        return true;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterBlock(Block block) {
        if (this.compiler.isOnDemandCompilation()) {
            return true;
        }
        if (isDynamicScopeBoundary(block)) {
            increaseDynamicScopeCount(block);
        }
        if (!this.lc.isFunctionBody()) {
            return true;
        }
        FunctionNode fn = this.lc.getCurrentFunction();
        final Set<Symbol> symbols = new HashSet<>();
        block.accept(new SimpleNodeVisitor() { // from class: jdk.nashorn.internal.codegen.FindScopeDepths.1
            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterIdentNode(IdentNode identNode) {
                Symbol symbol = identNode.getSymbol();
                if (symbol != null && symbol.isScope()) {
                    symbols.add(symbol);
                    return true;
                }
                return true;
            }
        });
        Map<String, Integer> internals = new HashMap<>();
        Block globalBlock = findGlobalBlock(this.lc, block);
        Block bodyBlock = findBodyBlock(this.lc, fn, block);
        if (!$assertionsDisabled && globalBlock == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && bodyBlock == null) {
            throw new AssertionError();
        }
        for (Symbol symbol : symbols) {
            int internalDepth = findInternalDepth(this.lc, fn, block, symbol);
            boolean internal = internalDepth >= 0;
            if (internal) {
                internals.put(symbol.getName(), Integer.valueOf(internalDepth));
            }
            if (!internal) {
                int depthAtStart = 0;
                Iterator<Block> iter = this.lc.getAncestorBlocks(bodyBlock);
                while (true) {
                    if (iter.hasNext()) {
                        Block b2 = iter.next();
                        if (definedInBlock(b2, symbol)) {
                            addExternalSymbol(fn, symbol, depthAtStart);
                            break;
                        }
                        if (b2.needsScope()) {
                            depthAtStart++;
                        }
                    }
                }
            }
        }
        addInternalSymbols(fn, internals.keySet());
        if (this.log.isEnabled()) {
            this.log.info(fn.getName() + " internals=" + ((Object) internals) + " externals=" + ((Object) this.externalSymbolDepths.get(Integer.valueOf(fn.getId()))));
            return true;
        }
        return true;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveBlock(Block block) {
        if (this.compiler.isOnDemandCompilation()) {
            return block;
        }
        if (isDynamicScopeBoundary(block)) {
            decreaseDynamicScopeCount(block);
        }
        return block;
    }

    private void addInternalSymbols(FunctionNode functionNode, Set<String> symbols) {
        int fnId = functionNode.getId();
        if (!$assertionsDisabled && this.internalSymbols.get(Integer.valueOf(fnId)) != null && !this.internalSymbols.get(Integer.valueOf(fnId)).equals(symbols)) {
            throw new AssertionError();
        }
        this.internalSymbols.put(Integer.valueOf(fnId), symbols);
    }

    private void addExternalSymbol(FunctionNode functionNode, Symbol symbol, int depthAtStart) {
        int fnId = functionNode.getId();
        Map<String, Integer> depths = this.externalSymbolDepths.get(Integer.valueOf(fnId));
        if (depths == null) {
            depths = new HashMap();
            this.externalSymbolDepths.put(Integer.valueOf(fnId), depths);
        }
        depths.put(symbol.getName(), Integer.valueOf(depthAtStart));
    }
}
