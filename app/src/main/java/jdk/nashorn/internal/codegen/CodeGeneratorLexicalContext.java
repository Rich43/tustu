package jdk.nashorn.internal.codegen;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import jdk.nashorn.internal.IntDeque;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.WithNode;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGeneratorLexicalContext.class */
final class CodeGeneratorLexicalContext extends LexicalContext {
    private int dynamicScopeCount;
    private final Map<SharedScopeCall, SharedScopeCall> scopeCalls = new HashMap();
    private final Deque<CompileUnit> compileUnits = new ArrayDeque();
    private final Deque<MethodEmitter> methodEmitters = new ArrayDeque();
    private final Deque<Expression> discard = new ArrayDeque();
    private final Deque<Map<String, Collection<Label>>> unwarrantedOptimismHandlers = new ArrayDeque();
    private final Deque<StringBuilder> slotTypesDescriptors = new ArrayDeque();
    private final IntDeque splitNodes = new IntDeque();
    private int[] nextFreeSlots = new int[16];
    private int nextFreeSlotsSize;
    static final /* synthetic */ boolean $assertionsDisabled;

    CodeGeneratorLexicalContext() {
    }

    static {
        $assertionsDisabled = !CodeGeneratorLexicalContext.class.desiredAssertionStatus();
    }

    private boolean isWithBoundary(Object node) {
        return (node instanceof Block) && !isEmpty() && (peek() instanceof WithNode);
    }

    @Override // jdk.nashorn.internal.ir.LexicalContext
    public <T extends LexicalContextNode> T push(T t2) {
        if (isWithBoundary(t2)) {
            this.dynamicScopeCount++;
        } else if (t2 instanceof FunctionNode) {
            if (((FunctionNode) t2).inDynamicContext()) {
                this.dynamicScopeCount++;
            }
            this.splitNodes.push(0);
        }
        return (T) super.push(t2);
    }

    void enterSplitNode() {
        this.splitNodes.getAndIncrement();
        pushFreeSlots(this.methodEmitters.peek().getUsedSlotsWithLiveTemporaries());
    }

    void exitSplitNode() {
        int count = this.splitNodes.decrementAndGet();
        if (!$assertionsDisabled && count < 0) {
            throw new AssertionError();
        }
    }

    @Override // jdk.nashorn.internal.ir.LexicalContext
    public <T extends Node> T pop(T t2) {
        T t3 = (T) super.pop(t2);
        if (isWithBoundary(t2)) {
            this.dynamicScopeCount--;
            if (!$assertionsDisabled && this.dynamicScopeCount < 0) {
                throw new AssertionError();
            }
        } else if (t2 instanceof FunctionNode) {
            if (((FunctionNode) t2).inDynamicContext()) {
                this.dynamicScopeCount--;
                if (!$assertionsDisabled && this.dynamicScopeCount < 0) {
                    throw new AssertionError();
                }
            }
            if (!$assertionsDisabled && this.splitNodes.peek() != 0) {
                throw new AssertionError();
            }
            this.splitNodes.pop();
        }
        return t3;
    }

    boolean inDynamicScope() {
        return this.dynamicScopeCount > 0;
    }

    boolean inSplitNode() {
        return !this.splitNodes.isEmpty() && this.splitNodes.peek() > 0;
    }

    MethodEmitter pushMethodEmitter(MethodEmitter newMethod) {
        this.methodEmitters.push(newMethod);
        return newMethod;
    }

    MethodEmitter popMethodEmitter(MethodEmitter oldMethod) {
        if (!$assertionsDisabled && this.methodEmitters.peek() != oldMethod) {
            throw new AssertionError();
        }
        this.methodEmitters.pop();
        if (this.methodEmitters.isEmpty()) {
            return null;
        }
        return this.methodEmitters.peek();
    }

    void pushUnwarrantedOptimismHandlers() {
        this.unwarrantedOptimismHandlers.push(new HashMap());
        this.slotTypesDescriptors.push(new StringBuilder());
    }

    Map<String, Collection<Label>> getUnwarrantedOptimismHandlers() {
        return this.unwarrantedOptimismHandlers.peek();
    }

    Map<String, Collection<Label>> popUnwarrantedOptimismHandlers() {
        this.slotTypesDescriptors.pop();
        return this.unwarrantedOptimismHandlers.pop();
    }

    CompileUnit pushCompileUnit(CompileUnit newUnit) {
        this.compileUnits.push(newUnit);
        return newUnit;
    }

    CompileUnit popCompileUnit(CompileUnit oldUnit) {
        if (!$assertionsDisabled && this.compileUnits.peek() != oldUnit) {
            throw new AssertionError();
        }
        CompileUnit unit = this.compileUnits.pop();
        if (!$assertionsDisabled && !unit.hasCode()) {
            throw new AssertionError((Object) "compile unit popped without code");
        }
        unit.setUsed();
        if (this.compileUnits.isEmpty()) {
            return null;
        }
        return this.compileUnits.peek();
    }

    boolean hasCompileUnits() {
        return !this.compileUnits.isEmpty();
    }

    Collection<SharedScopeCall> getScopeCalls() {
        return Collections.unmodifiableCollection(this.scopeCalls.values());
    }

    SharedScopeCall getScopeCall(CompileUnit unit, Symbol symbol, Type valueType, Type returnType, Type[] paramTypes, int flags) {
        SharedScopeCall scopeCall = new SharedScopeCall(symbol, valueType, returnType, paramTypes, flags);
        if (this.scopeCalls.containsKey(scopeCall)) {
            return this.scopeCalls.get(scopeCall);
        }
        scopeCall.setClassAndName(unit, getCurrentFunction().uniqueName(":scopeCall"));
        this.scopeCalls.put(scopeCall, scopeCall);
        return scopeCall;
    }

    SharedScopeCall getScopeGet(CompileUnit unit, Symbol symbol, Type valueType, int flags) {
        return getScopeCall(unit, symbol, valueType, valueType, null, flags);
    }

    void onEnterBlock(Block block) {
        pushFreeSlots(assignSlots(block, isFunctionBody() ? 0 : getUsedSlotCount()));
    }

    private void pushFreeSlots(int freeSlots) {
        if (this.nextFreeSlotsSize == this.nextFreeSlots.length) {
            int[] newNextFreeSlots = new int[this.nextFreeSlotsSize * 2];
            System.arraycopy(this.nextFreeSlots, 0, newNextFreeSlots, 0, this.nextFreeSlotsSize);
            this.nextFreeSlots = newNextFreeSlots;
        }
        int[] iArr = this.nextFreeSlots;
        int i2 = this.nextFreeSlotsSize;
        this.nextFreeSlotsSize = i2 + 1;
        iArr[i2] = freeSlots;
    }

    int getUsedSlotCount() {
        return this.nextFreeSlots[this.nextFreeSlotsSize - 1];
    }

    void releaseSlots() {
        this.nextFreeSlotsSize--;
        int undefinedFromSlot = this.nextFreeSlotsSize == 0 ? 0 : this.nextFreeSlots[this.nextFreeSlotsSize - 1];
        if (!this.slotTypesDescriptors.isEmpty()) {
            this.slotTypesDescriptors.peek().setLength(undefinedFromSlot);
        }
        this.methodEmitters.peek().undefineLocalVariables(undefinedFromSlot, false);
    }

    private int assignSlots(Block block, int firstSlot) {
        int fromSlot = firstSlot;
        MethodEmitter method = this.methodEmitters.peek();
        for (Symbol symbol : block.getSymbols()) {
            if (symbol.hasSlot()) {
                symbol.setFirstSlot(fromSlot);
                int toSlot = fromSlot + symbol.slotCount();
                method.defineBlockLocalVariable(fromSlot, toSlot);
                fromSlot = toSlot;
            }
        }
        return fromSlot;
    }

    static Type getTypeForSlotDescriptor(char typeDesc) {
        switch (typeDesc) {
            case 'A':
            case 'a':
                return Type.OBJECT;
            case 'D':
            case 'd':
                return Type.NUMBER;
            case 'I':
            case 'i':
                return Type.INT;
            case 'J':
            case 'j':
                return Type.LONG;
            case 'U':
            case 'u':
                return Type.UNKNOWN;
            default:
                throw new AssertionError();
        }
    }

    void pushDiscard(Expression expr) {
        this.discard.push(expr);
    }

    boolean popDiscardIfCurrent(Expression expr) {
        if (isCurrentDiscard(expr)) {
            this.discard.pop();
            return true;
        }
        return false;
    }

    boolean isCurrentDiscard(Expression expr) {
        return this.discard.peek() == expr;
    }

    int quickSlot(Type type) {
        return this.methodEmitters.peek().defineTemporaryLocalVariable(type.getSlots());
    }
}
