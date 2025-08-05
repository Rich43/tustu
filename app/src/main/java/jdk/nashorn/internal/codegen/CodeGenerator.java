package jdk.nashorn.internal.codegen;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.parsers.XMLGrammarCachingConfiguration;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import jdk.nashorn.internal.AssertsEnabled;
import jdk.nashorn.internal.IntDeque;
import jdk.nashorn.internal.codegen.ClassEmitter;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.codegen.types.ArrayType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BaseNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.JumpStatement;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.SetSplitState;
import jdk.nashorn.internal.ir.SplitReturn;
import jdk.nashorn.internal.ir.Splittable;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.OptimisticReturnFilters;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.RewriteException;
import jdk.nashorn.internal.runtime.Scope;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.options.Options;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;

@Logger(name = "codegen")
/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator.class */
final class CodeGenerator extends NodeOperatorVisitor<CodeGeneratorLexicalContext> implements Loggable {
    private static final Type SCOPE_TYPE;
    private static final String GLOBAL_OBJECT;
    private static final CompilerConstants.Call CREATE_REWRITE_EXCEPTION;
    private static final CompilerConstants.Call CREATE_REWRITE_EXCEPTION_REST_OF;
    private static final CompilerConstants.Call ENSURE_INT;
    private static final CompilerConstants.Call ENSURE_NUMBER;
    private static final CompilerConstants.Call CREATE_FUNCTION_OBJECT;
    private static final CompilerConstants.Call CREATE_FUNCTION_OBJECT_NO_SCOPE;
    private static final CompilerConstants.Call TO_NUMBER_FOR_EQ;
    private static final CompilerConstants.Call TO_NUMBER_FOR_STRICT_EQ;
    private static final Class<?> ITERATOR_CLASS;
    private static final Type ITERATOR_TYPE;
    private static final Type EXCEPTION_TYPE;
    private static final Integer INT_ZERO;
    private final Compiler compiler;
    private final boolean evalCode;
    private final int callSiteFlags;
    private int regexFieldCount;
    private int lastLineNumber;
    private static final int MAX_REGEX_FIELDS = 2048;
    private MethodEmitter method;
    private CompileUnit unit;
    private final DebugLogger log;
    static final int OBJECT_SPILL_THRESHOLD;
    private final Set<String> emittedMethods;
    private ContinuationInfo continuationInfo;
    private final Deque<Label> scopeEntryLabels;
    private static final Label METHOD_BOUNDARY;
    private final Deque<Label> catchLabels;
    private final IntDeque labeledBlockBreakLiveLocals;
    private final int[] continuationEntryPoints;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$SplitLiteralCreator.class */
    interface SplitLiteralCreator {
        void populateRange(MethodEmitter methodEmitter, Type type, int i2, int i3, int i4);
    }

    static {
        $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
        SCOPE_TYPE = Type.typeFor((Class<?>) ScriptObject.class);
        GLOBAL_OBJECT = Type.getInternalName(Global.class);
        CREATE_REWRITE_EXCEPTION = CompilerConstants.staticCallNoLookup(RewriteException.class, "create", RewriteException.class, UnwarrantedOptimismException.class, Object[].class, String[].class);
        CREATE_REWRITE_EXCEPTION_REST_OF = CompilerConstants.staticCallNoLookup(RewriteException.class, "create", RewriteException.class, UnwarrantedOptimismException.class, Object[].class, String[].class, int[].class);
        ENSURE_INT = CompilerConstants.staticCallNoLookup(OptimisticReturnFilters.class, "ensureInt", Integer.TYPE, Object.class, Integer.TYPE);
        ENSURE_NUMBER = CompilerConstants.staticCallNoLookup(OptimisticReturnFilters.class, "ensureNumber", Double.TYPE, Object.class, Integer.TYPE);
        CREATE_FUNCTION_OBJECT = CompilerConstants.staticCallNoLookup(ScriptFunction.class, "create", ScriptFunction.class, Object[].class, Integer.TYPE, ScriptObject.class);
        CREATE_FUNCTION_OBJECT_NO_SCOPE = CompilerConstants.staticCallNoLookup(ScriptFunction.class, "create", ScriptFunction.class, Object[].class, Integer.TYPE);
        TO_NUMBER_FOR_EQ = CompilerConstants.staticCallNoLookup(JSType.class, "toNumberForEq", Double.TYPE, Object.class);
        TO_NUMBER_FOR_STRICT_EQ = CompilerConstants.staticCallNoLookup(JSType.class, "toNumberForStrictEq", Double.TYPE, Object.class);
        ITERATOR_CLASS = Iterator.class;
        if (!$assertionsDisabled && ITERATOR_CLASS != CompilerConstants.ITERATOR_PREFIX.type()) {
            throw new AssertionError();
        }
        ITERATOR_TYPE = Type.typeFor(ITERATOR_CLASS);
        EXCEPTION_TYPE = Type.typeFor(CompilerConstants.EXCEPTION_PREFIX.type());
        INT_ZERO = 0;
        OBJECT_SPILL_THRESHOLD = Options.getIntProperty("nashorn.spill.threshold", 256);
        METHOD_BOUNDARY = new Label("");
    }

    CodeGenerator(Compiler compiler, int[] continuationEntryPoints) {
        super(new CodeGeneratorLexicalContext());
        this.lastLineNumber = -1;
        this.emittedMethods = new HashSet();
        this.scopeEntryLabels = new ArrayDeque();
        this.catchLabels = new ArrayDeque();
        this.labeledBlockBreakLiveLocals = new IntDeque();
        this.compiler = compiler;
        this.evalCode = compiler.getSource().isEvalCode();
        this.continuationEntryPoints = continuationEntryPoints;
        this.callSiteFlags = compiler.getScriptEnvironment()._callsite_flags;
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

    int getCallSiteFlags() {
        return ((CodeGeneratorLexicalContext) this.lc).getCurrentFunction().getCallSiteFlags() | this.callSiteFlags;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getScopeCallSiteFlags(Symbol symbol) {
        if (!$assertionsDisabled && !symbol.isScope()) {
            throw new AssertionError();
        }
        int flags = getCallSiteFlags() | 1;
        if (isEvalCode() && symbol.isGlobal()) {
            return flags;
        }
        return isFastScope(symbol) ? flags | 4 : flags;
    }

    boolean isEvalCode() {
        return this.evalCode;
    }

    boolean useDualFields() {
        return this.compiler.getContext().useDualFields();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter loadIdent(IdentNode identNode, final TypeBounds resultBounds) {
        checkTemporalDeadZone(identNode);
        final Symbol symbol = identNode.getSymbol();
        if (!symbol.isScope()) {
            Type type = identNode.getType();
            if (type == Type.UNDEFINED) {
                return this.method.loadUndefined(resultBounds.widest);
            }
            if ($assertionsDisabled || symbol.hasSlot() || symbol.isParam()) {
                return this.method.load(identNode);
            }
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !identNode.getSymbol().isScope()) {
            throw new AssertionError((Object) (((Object) identNode) + " is not in scope!"));
        }
        final int flags = getScopeCallSiteFlags(symbol);
        if (isFastScope(symbol)) {
            if (symbol.getUseCount() > 200 && !identNode.isOptimistic()) {
                new OptimisticOperation(identNode, TypeBounds.OBJECT) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.1
                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        CodeGenerator.this.loadSharedScopeVar(resultBounds.widest, symbol, flags);
                    }
                }.emit();
            } else {
                new LoadFastScopeVar(identNode, resultBounds, flags).emit();
            }
        } else {
            new LoadScopeVar(identNode, resultBounds, flags).emit();
        }
        return this.method;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkTemporalDeadZone(IdentNode identNode) {
        if (identNode.isDead()) {
            this.method.load(identNode.getSymbol().getName()).invoke(ScriptRuntime.THROW_REFERENCE_ERROR);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAssignTarget(Expression expression) {
        if ((expression instanceof IdentNode) && ((IdentNode) expression).getSymbol().isConst()) {
            this.method.load(((IdentNode) expression).getSymbol().getName()).invoke(ScriptRuntime.THROW_CONST_TYPE_ERROR);
        }
    }

    private boolean isRestOf() {
        return this.continuationEntryPoints != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCurrentContinuationEntryPoint(int programPoint) {
        return isRestOf() && getCurrentContinuationEntryPoint() == programPoint;
    }

    private int[] getContinuationEntryPoints() {
        if (isRestOf()) {
            return this.continuationEntryPoints;
        }
        return null;
    }

    private int getCurrentContinuationEntryPoint() {
        if (isRestOf()) {
            return this.continuationEntryPoints[0];
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isContinuationEntryPoint(int programPoint) {
        if (isRestOf()) {
            if (!$assertionsDisabled && this.continuationEntryPoints == null) {
                throw new AssertionError();
            }
            for (int cep : this.continuationEntryPoints) {
                if (cep == programPoint) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFastScope(Symbol symbol) {
        if (!symbol.isScope()) {
            return false;
        }
        if (!((CodeGeneratorLexicalContext) this.lc).inDynamicScope()) {
            if ($assertionsDisabled || symbol.isGlobal() || ((CodeGeneratorLexicalContext) this.lc).getDefiningBlock(symbol).needsScope()) {
                return true;
            }
            throw new AssertionError((Object) symbol.getName());
        }
        if (symbol.isGlobal()) {
            return false;
        }
        String name = symbol.getName();
        boolean previousWasBlock = false;
        Iterator<LexicalContextNode> it = ((CodeGeneratorLexicalContext) this.lc).getAllNodes();
        while (it.hasNext()) {
            LexicalContextNode node = it.next();
            if (node instanceof Block) {
                Block block = (Block) node;
                if (block.getExistingSymbol(name) == symbol) {
                    if ($assertionsDisabled || block.needsScope()) {
                        return true;
                    }
                    throw new AssertionError();
                }
                previousWasBlock = true;
            } else {
                if ((node instanceof WithNode) && previousWasBlock) {
                    return false;
                }
                if ((node instanceof FunctionNode) && ((FunctionNode) node).needsDynamicScope()) {
                    return false;
                }
                previousWasBlock = false;
            }
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter loadSharedScopeVar(Type valueType, Symbol symbol, int flags) {
        if (!$assertionsDisabled && !isFastScope(symbol)) {
            throw new AssertionError();
        }
        this.method.load(getScopeProtoDepth(((CodeGeneratorLexicalContext) this.lc).getCurrentBlock(), symbol));
        return ((CodeGeneratorLexicalContext) this.lc).getScopeGet(this.unit, symbol, valueType, flags).generateInvoke(this.method);
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$LoadScopeVar.class */
    private class LoadScopeVar extends OptimisticOperation {
        final IdentNode identNode;
        private final int flags;

        LoadScopeVar(IdentNode identNode, TypeBounds resultBounds, int flags) {
            super(identNode, resultBounds);
            this.identNode = identNode;
            this.flags = flags;
        }

        @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
        void loadStack() {
            CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
            getProto();
        }

        void getProto() {
        }

        @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
        void consumeStack() {
            if (this.identNode.isCompileTimePropertyName()) {
                CodeGenerator.this.method.dynamicGet(Type.OBJECT, this.identNode.getSymbol().getName(), this.flags, this.identNode.isFunction(), false);
                replaceCompileTimeProperty();
            } else {
                dynamicGet(this.identNode.getSymbol().getName(), this.flags, this.identNode.isFunction(), false);
            }
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$LoadFastScopeVar.class */
    private class LoadFastScopeVar extends LoadScopeVar {
        LoadFastScopeVar(IdentNode identNode, TypeBounds resultBounds, int flags) {
            super(identNode, resultBounds, flags);
        }

        @Override // jdk.nashorn.internal.codegen.CodeGenerator.LoadScopeVar
        void getProto() {
            CodeGenerator.this.loadFastScopeProto(this.identNode.getSymbol(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter storeFastScopeVar(Symbol symbol, int flags) {
        loadFastScopeProto(symbol, true);
        this.method.dynamicSet(symbol.getName(), flags, false);
        return this.method;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getScopeProtoDepth(Block startingBlock, Symbol symbol) {
        int depth;
        FunctionNode fn = ((CodeGeneratorLexicalContext) this.lc).getCurrentFunction();
        int externalDepth = this.compiler.getScriptFunctionData(fn.getId()).getExternalSymbolDepth(symbol.getName());
        int internalDepth = FindScopeDepths.findInternalDepth(this.lc, fn, startingBlock, symbol);
        int scopesToStart = FindScopeDepths.findScopesToStart(this.lc, fn, startingBlock);
        if (internalDepth == -1) {
            depth = scopesToStart + externalDepth;
        } else {
            if (!$assertionsDisabled && internalDepth > scopesToStart) {
                throw new AssertionError();
            }
            depth = internalDepth;
        }
        return depth;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadFastScopeProto(Symbol symbol, boolean swap) {
        int depth = getScopeProtoDepth(((CodeGeneratorLexicalContext) this.lc).getCurrentBlock(), symbol);
        if (!$assertionsDisabled && depth == -1) {
            throw new AssertionError((Object) ("Couldn't find scope depth for symbol " + symbol.getName() + " in " + ((Object) ((CodeGeneratorLexicalContext) this.lc).getCurrentFunction())));
        }
        if (depth > 0) {
            if (swap) {
                this.method.swap();
            }
            for (int i2 = 0; i2 < depth; i2++) {
                this.method.invoke(ScriptObject.GET_PROTO);
            }
            if (swap) {
                this.method.swap();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter loadExpressionUnbounded(Expression expr) {
        return loadExpression(expr, TypeBounds.UNBOUNDED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter loadExpressionAsObject(Expression expr) {
        return loadExpression(expr, TypeBounds.OBJECT);
    }

    MethodEmitter loadExpressionAsBoolean(Expression expr) {
        return loadExpression(expr, TypeBounds.BOOLEAN);
    }

    private static boolean noToPrimitiveConversion(Type source, Type target) {
        return source.isJSPrimitive() || !target.isJSPrimitive() || target.isBoolean();
    }

    MethodEmitter loadBinaryOperands(BinaryNode binaryNode) {
        return loadBinaryOperands(binaryNode.lhs(), binaryNode.rhs(), TypeBounds.UNBOUNDED.notWiderThan(binaryNode.getWidestOperandType()), false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter loadBinaryOperands(Expression lhs, Expression rhs, TypeBounds explicitOperandBounds, boolean baseAlreadyOnStack, boolean forceConversionSeparation) {
        Type lhsType = undefinedToNumber(lhs.getType());
        Type rhsType = undefinedToNumber(rhs.getType());
        Type narrowestOperandType = Type.narrowest(Type.widest(lhsType, rhsType), explicitOperandBounds.widest);
        TypeBounds operandBounds = explicitOperandBounds.notNarrowerThan(narrowestOperandType);
        if (noToPrimitiveConversion(lhsType, explicitOperandBounds.widest) || rhs.isLocal()) {
            if (forceConversionSeparation) {
                TypeBounds safeConvertBounds = TypeBounds.UNBOUNDED.notNarrowerThan(narrowestOperandType);
                loadExpression(lhs, safeConvertBounds, baseAlreadyOnStack);
                this.method.convert(operandBounds.within(this.method.peekType()));
                loadExpression(rhs, safeConvertBounds, false);
                this.method.convert(operandBounds.within(this.method.peekType()));
            } else {
                loadExpression(lhs, operandBounds, baseAlreadyOnStack);
                loadExpression(rhs, operandBounds, false);
            }
        } else {
            TypeBounds safeConvertBounds2 = TypeBounds.UNBOUNDED.notNarrowerThan(narrowestOperandType);
            loadExpression(lhs, safeConvertBounds2, baseAlreadyOnStack);
            Type lhsLoadedType = this.method.peekType();
            loadExpression(rhs, safeConvertBounds2, false);
            Type convertedLhsType = operandBounds.within(this.method.peekType());
            if (convertedLhsType != lhsLoadedType) {
                this.method.swap().convert(convertedLhsType).swap();
            }
            this.method.convert(operandBounds.within(this.method.peekType()));
        }
        if (!$assertionsDisabled && Type.generic(this.method.peekType()) != operandBounds.narrowest) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || Type.generic(this.method.peekType(1)) == operandBounds.narrowest) {
            return this.method;
        }
        throw new AssertionError();
    }

    MethodEmitter loadComparisonOperands(BinaryNode cmp) {
        Expression lhs = cmp.lhs();
        Expression rhs = cmp.rhs();
        Type lhsType = lhs.getType();
        Type rhsType = rhs.getType();
        if (!$assertionsDisabled && lhsType.isObject() && rhsType.isObject()) {
            throw new AssertionError();
        }
        if (lhsType.isObject() || rhsType.isObject()) {
            boolean canReorder = lhsType.isPrimitive() || rhs.isLocal();
            boolean canCombineLoadAndConvert = canReorder && cmp.isRelational();
            loadExpression(lhs, (!canCombineLoadAndConvert || lhs.isOptimistic()) ? TypeBounds.UNBOUNDED : TypeBounds.NUMBER);
            Type lhsLoadedType = this.method.peekType();
            TokenType tt = cmp.tokenType();
            if (canReorder) {
                emitObjectToNumberComparisonConversion(this.method, tt);
                loadExpression(rhs, (!canCombineLoadAndConvert || rhs.isOptimistic()) ? TypeBounds.UNBOUNDED : TypeBounds.NUMBER);
            } else {
                loadExpression(rhs, TypeBounds.UNBOUNDED);
                if (lhsLoadedType != Type.NUMBER) {
                    this.method.swap();
                    emitObjectToNumberComparisonConversion(this.method, tt);
                    this.method.swap();
                }
            }
            emitObjectToNumberComparisonConversion(this.method, tt);
            return this.method;
        }
        return loadBinaryOperands(cmp);
    }

    private static void emitObjectToNumberComparisonConversion(MethodEmitter method, TokenType tt) {
        switch (tt) {
            case EQ:
            case NE:
                if (method.peekType().isObject()) {
                    TO_NUMBER_FOR_EQ.invoke(method);
                    return;
                }
                break;
            case EQ_STRICT:
            case NE_STRICT:
                if (method.peekType().isObject()) {
                    TO_NUMBER_FOR_STRICT_EQ.invoke(method);
                    return;
                }
                break;
        }
        method.convert(Type.NUMBER);
    }

    private static final Type undefinedToNumber(Type type) {
        return type == Type.UNDEFINED ? Type.NUMBER : type;
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$TypeBounds.class */
    private static final class TypeBounds {
        final Type narrowest;
        final Type widest;
        static final TypeBounds UNBOUNDED;
        static final TypeBounds INT;
        static final TypeBounds NUMBER;
        static final TypeBounds OBJECT;
        static final TypeBounds BOOLEAN;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
            UNBOUNDED = new TypeBounds(Type.UNKNOWN, Type.OBJECT);
            INT = exact(Type.INT);
            NUMBER = exact(Type.NUMBER);
            OBJECT = exact(Type.OBJECT);
            BOOLEAN = exact(Type.BOOLEAN);
        }

        static TypeBounds exact(Type type) {
            return new TypeBounds(type, type);
        }

        TypeBounds(Type narrowest, Type widest) {
            if (!$assertionsDisabled && (widest == null || widest == Type.UNDEFINED || widest == Type.UNKNOWN)) {
                throw new AssertionError(widest);
            }
            if (!$assertionsDisabled && (narrowest == null || narrowest == Type.UNDEFINED)) {
                throw new AssertionError(narrowest);
            }
            if (!$assertionsDisabled && narrowest.widerThan(widest)) {
                throw new AssertionError((Object) (((Object) narrowest) + " wider than " + ((Object) widest)));
            }
            if (!$assertionsDisabled && widest.narrowerThan(narrowest)) {
                throw new AssertionError();
            }
            this.narrowest = Type.generic(narrowest);
            this.widest = Type.generic(widest);
        }

        TypeBounds notNarrowerThan(Type type) {
            return maybeNew(Type.narrowest(Type.widest(this.narrowest, type), this.widest), this.widest);
        }

        TypeBounds notWiderThan(Type type) {
            return maybeNew(Type.narrowest(this.narrowest, type), Type.narrowest(this.widest, type));
        }

        boolean canBeNarrowerThan(Type type) {
            return this.narrowest.narrowerThan(type);
        }

        TypeBounds maybeNew(Type newNarrowest, Type newWidest) {
            if (newNarrowest == this.narrowest && newWidest == this.widest) {
                return this;
            }
            return new TypeBounds(newNarrowest, newWidest);
        }

        TypeBounds booleanToInt() {
            return maybeNew(CodeGenerator.booleanToInt(this.narrowest), CodeGenerator.booleanToInt(this.widest));
        }

        TypeBounds objectToNumber() {
            return maybeNew(CodeGenerator.objectToNumber(this.narrowest), CodeGenerator.objectToNumber(this.widest));
        }

        Type within(Type type) {
            if (type.narrowerThan(this.narrowest)) {
                return this.narrowest;
            }
            if (type.widerThan(this.widest)) {
                return this.widest;
            }
            return type;
        }

        public String toString() {
            return "[" + ((Object) this.narrowest) + ", " + ((Object) this.widest) + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Type booleanToInt(Type t2) {
        return t2 == Type.BOOLEAN ? Type.INT : t2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Type objectToNumber(Type t2) {
        return t2.isObject() ? Type.NUMBER : t2;
    }

    MethodEmitter loadExpressionAsType(Expression expr, Type type) {
        if (type == Type.BOOLEAN) {
            return loadExpressionAsBoolean(expr);
        }
        if (type == Type.UNDEFINED) {
            if ($assertionsDisabled || expr.getType() == Type.UNDEFINED) {
                return loadExpressionAsObject(expr);
            }
            throw new AssertionError();
        }
        return loadExpression(expr, TypeBounds.UNBOUNDED.notNarrowerThan(type)).convert(type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter loadExpression(Expression expr, TypeBounds resultBounds) {
        return loadExpression(expr, resultBounds, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter loadExpression(Expression expr, final TypeBounds resultBounds, final boolean baseAlreadyOnStack) {
        boolean isCurrentDiscard = ((CodeGeneratorLexicalContext) this.lc).isCurrentDiscard(expr);
        expr.accept(new NodeOperatorVisitor<LexicalContext>(new LexicalContext()) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.2
            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterIdentNode(IdentNode identNode) {
                CodeGenerator.this.loadIdent(identNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterAccessNode(final AccessNode accessNode) {
                new OptimisticOperation(accessNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.2.1
                    static final /* synthetic */ boolean $assertionsDisabled;

                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    static {
                        $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        if (!baseAlreadyOnStack) {
                            CodeGenerator.this.loadExpressionAsObject(accessNode.getBase());
                        }
                        if (!$assertionsDisabled && !CodeGenerator.this.method.peekType().isObject()) {
                            throw new AssertionError();
                        }
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        int flags = CodeGenerator.this.getCallSiteFlags();
                        dynamicGet(accessNode.getProperty(), flags, accessNode.isFunction(), accessNode.isIndex());
                    }
                }.emit(baseAlreadyOnStack ? 1 : 0);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterIndexNode(final IndexNode indexNode) {
                new OptimisticOperation(indexNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.2.2
                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        if (!baseAlreadyOnStack) {
                            CodeGenerator.this.loadExpressionAsObject(indexNode.getBase());
                            CodeGenerator.this.loadExpressionUnbounded(indexNode.getIndex());
                        }
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        int flags = CodeGenerator.this.getCallSiteFlags();
                        dynamicGetIndex(flags, indexNode.isFunction());
                    }
                }.emit(baseAlreadyOnStack ? 2 : 0);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterFunctionNode(FunctionNode functionNode) {
                this.lc.pop(functionNode);
                functionNode.accept(this);
                this.lc.push(functionNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_ADD(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_ADD(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_BIT_AND(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_BIT_AND(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_BIT_OR(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_BIT_OR(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_BIT_XOR(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_BIT_XOR(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_DIV(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_DIV(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_MOD(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_MOD(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_MUL(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_MUL(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_SAR(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_SAR(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_SHL(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_SHL(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_SHR(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_SHR(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterASSIGN_SUB(BinaryNode binaryNode) {
                CodeGenerator.this.checkAssignTarget(binaryNode.lhs());
                CodeGenerator.this.loadASSIGN_SUB(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterCallNode(CallNode callNode) {
                return CodeGenerator.this.loadCallNode(callNode, resultBounds);
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterLiteralNode(LiteralNode<?> literalNode) {
                CodeGenerator.this.loadLiteral(literalNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterTernaryNode(TernaryNode ternaryNode) {
                CodeGenerator.this.loadTernaryNode(ternaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterADD(BinaryNode binaryNode) {
                CodeGenerator.this.loadADD(binaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterSUB(UnaryNode unaryNode) {
                CodeGenerator.this.loadSUB(unaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterSUB(BinaryNode binaryNode) {
                CodeGenerator.this.loadSUB(binaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterMUL(BinaryNode binaryNode) {
                CodeGenerator.this.loadMUL(binaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterDIV(BinaryNode binaryNode) {
                CodeGenerator.this.loadDIV(binaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterMOD(BinaryNode binaryNode) {
                CodeGenerator.this.loadMOD(binaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterSAR(BinaryNode binaryNode) {
                CodeGenerator.this.loadSAR(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterSHL(BinaryNode binaryNode) {
                CodeGenerator.this.loadSHL(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterSHR(BinaryNode binaryNode) {
                CodeGenerator.this.loadSHR(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterCOMMALEFT(BinaryNode binaryNode) {
                CodeGenerator.this.loadCOMMALEFT(binaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterCOMMARIGHT(BinaryNode binaryNode) {
                CodeGenerator.this.loadCOMMARIGHT(binaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterAND(BinaryNode binaryNode) {
                CodeGenerator.this.loadAND_OR(binaryNode, resultBounds, true);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterOR(BinaryNode binaryNode) {
                CodeGenerator.this.loadAND_OR(binaryNode, resultBounds, false);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterNOT(UnaryNode unaryNode) {
                CodeGenerator.this.loadNOT(unaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterADD(UnaryNode unaryNode) {
                CodeGenerator.this.loadADD(unaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterBIT_NOT(UnaryNode unaryNode) {
                CodeGenerator.this.loadBIT_NOT(unaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterBIT_AND(BinaryNode binaryNode) {
                CodeGenerator.this.loadBIT_AND(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterBIT_OR(BinaryNode binaryNode) {
                CodeGenerator.this.loadBIT_OR(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterBIT_XOR(BinaryNode binaryNode) {
                CodeGenerator.this.loadBIT_XOR(binaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterVOID(UnaryNode unaryNode) {
                CodeGenerator.this.loadVOID(unaryNode, resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterEQ(BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.EQ);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterEQ_STRICT(BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.EQ);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterGE(BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.GE);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterGT(BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.GT);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterLE(BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.LE);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterLT(BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.LT);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterNE(BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.NE);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterNE_STRICT(BinaryNode binaryNode) {
                CodeGenerator.this.loadCmp(binaryNode, Condition.NE);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterObjectNode(ObjectNode objectNode) {
                CodeGenerator.this.loadObjectNode(objectNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterRuntimeNode(RuntimeNode runtimeNode) {
                CodeGenerator.this.loadRuntimeNode(runtimeNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterNEW(UnaryNode unaryNode) {
                CodeGenerator.this.loadNEW(unaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeOperatorVisitor
            public boolean enterDECINC(UnaryNode unaryNode) {
                CodeGenerator.this.checkAssignTarget(unaryNode.getExpression());
                CodeGenerator.this.loadDECINC(unaryNode);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterJoinPredecessorExpression(JoinPredecessorExpression joinExpr) {
                CodeGenerator.this.loadMaybeDiscard(joinExpr, joinExpr.getExpression(), resultBounds);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterGetSplitState(GetSplitState getSplitState) {
                CodeGenerator.this.method.loadScope();
                CodeGenerator.this.method.invoke(Scope.GET_SPLIT_STATE);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterDefault(Node otherNode) {
                throw new AssertionError((Object) otherNode.getClass().getName());
            }
        });
        if (!isCurrentDiscard) {
            coerceStackTop(resultBounds);
        }
        return this.method;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter coerceStackTop(TypeBounds typeBounds) {
        return this.method.convert(typeBounds.within(this.method.peekType()));
    }

    private void closeBlockVariables(Block block) {
        for (Symbol symbol : block.getSymbols()) {
            if (symbol.isBytecodeLocal()) {
                this.method.closeLocalVariable(symbol, block.getBreakLabel());
            }
        }
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterBlock(Block block) {
        Label entryLabel = block.getEntryLabel();
        if (entryLabel.isBreakTarget()) {
            if (!$assertionsDisabled && this.method.isReachable()) {
                throw new AssertionError();
            }
            this.method.breakLabel(entryLabel, ((CodeGeneratorLexicalContext) this.lc).getUsedSlotCount());
        } else {
            this.method.label(entryLabel);
        }
        if (!this.method.isReachable()) {
            return false;
        }
        if (((CodeGeneratorLexicalContext) this.lc).isFunctionBody() && this.emittedMethods.contains(((CodeGeneratorLexicalContext) this.lc).getCurrentFunction().getName())) {
            return false;
        }
        initLocals(block);
        if ($assertionsDisabled || ((CodeGeneratorLexicalContext) this.lc).getUsedSlotCount() == this.method.getFirstTemp()) {
            return true;
        }
        throw new AssertionError();
    }

    boolean useOptimisticTypes() {
        return !((CodeGeneratorLexicalContext) this.lc).inSplitNode() && this.compiler.useOptimisticTypes();
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveBlock(Block block) {
        popBlockScope(block);
        this.method.beforeJoinPoint(block);
        closeBlockVariables(block);
        ((CodeGeneratorLexicalContext) this.lc).releaseSlots();
        if (!$assertionsDisabled && this.method.isReachable()) {
            if ((((CodeGeneratorLexicalContext) this.lc).isFunctionBody() ? 0 : ((CodeGeneratorLexicalContext) this.lc).getUsedSlotCount()) != this.method.getFirstTemp()) {
                throw new AssertionError((Object) ("reachable=" + this.method.isReachable() + " isFunctionBody=" + ((CodeGeneratorLexicalContext) this.lc).isFunctionBody() + " usedSlotCount=" + ((CodeGeneratorLexicalContext) this.lc).getUsedSlotCount() + " firstTemp=" + this.method.getFirstTemp()));
            }
        }
        return block;
    }

    private void popBlockScope(Block block) {
        Label breakLabel = block.getBreakLabel();
        if (!block.needsScope() || ((CodeGeneratorLexicalContext) this.lc).isFunctionBody()) {
            emitBlockBreakLabel(breakLabel);
            return;
        }
        Label beginTryLabel = this.scopeEntryLabels.pop();
        Label recoveryLabel = new Label("block_popscope_catch");
        emitBlockBreakLabel(breakLabel);
        boolean bodyCanThrow = breakLabel.isAfter(beginTryLabel);
        if (bodyCanThrow) {
            this.method._try(beginTryLabel, breakLabel, recoveryLabel);
        }
        Label afterCatchLabel = null;
        if (this.method.isReachable()) {
            popScope();
            if (bodyCanThrow) {
                afterCatchLabel = new Label("block_after_catch");
                this.method._goto(afterCatchLabel);
            }
        }
        if (bodyCanThrow) {
            if (!$assertionsDisabled && this.method.isReachable()) {
                throw new AssertionError();
            }
            this.method._catch(recoveryLabel);
            popScopeException();
            this.method.athrow();
        }
        if (afterCatchLabel != null) {
            this.method.label(afterCatchLabel);
        }
    }

    private void emitBlockBreakLabel(Label breakLabel) {
        LabelNode labelNode = ((CodeGeneratorLexicalContext) this.lc).getCurrentBlockLabelNode();
        if (labelNode != null) {
            if (!$assertionsDisabled && labelNode.getLocalVariableConversion() != null && !this.method.isReachable()) {
                throw new AssertionError();
            }
            this.method.beforeJoinPoint(labelNode);
            this.method.breakLabel(breakLabel, this.labeledBlockBreakLiveLocals.pop());
            return;
        }
        this.method.label(breakLabel);
    }

    private void popScope() {
        popScopes(1);
    }

    private void popScopeException() {
        Label catchLabel;
        popScope();
        ContinuationInfo ci = getContinuationInfo();
        if (ci != null && (catchLabel = ci.catchLabel) != METHOD_BOUNDARY && catchLabel == this.catchLabels.peek()) {
            ContinuationInfo.access$4804(ci);
        }
    }

    private void popScopesUntil(LexicalContextNode until) {
        popScopes(((CodeGeneratorLexicalContext) this.lc).getScopeNestingLevelTo(until));
    }

    private void popScopes(int count) {
        if (count == 0) {
            return;
        }
        if (!$assertionsDisabled && count <= 0) {
            throw new AssertionError();
        }
        if (!this.method.hasScope()) {
            return;
        }
        this.method.loadCompilerConstant(CompilerConstants.SCOPE);
        for (int i2 = 0; i2 < count; i2++) {
            this.method.invoke(ScriptObject.GET_PROTO);
        }
        this.method.storeCompilerConstant(CompilerConstants.SCOPE);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterBreakNode(BreakNode breakNode) {
        return enterJumpStatement(breakNode);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterJumpToInlinedFinally(JumpToInlinedFinally jumpToInlinedFinally) {
        return enterJumpStatement(jumpToInlinedFinally);
    }

    private boolean enterJumpStatement(JumpStatement jump) {
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(jump);
        this.method.beforeJoinPoint(jump);
        popScopesUntil(jump.getPopScopeLimit(this.lc));
        Label targetLabel = jump.getTargetLabel(this.lc);
        targetLabel.markAsBreakTarget();
        this.method._goto(targetLabel);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int loadArgs(List<Expression> args) {
        int argCount = args.size();
        if (argCount > 250) {
            loadArgsArray(args);
            return 1;
        }
        for (Expression arg : args) {
            if (!$assertionsDisabled && arg == null) {
                throw new AssertionError();
            }
            loadExpressionUnbounded(arg);
        }
        return argCount;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean loadCallNode(final CallNode callNode, final TypeBounds resultBounds) {
        lineNumber(callNode.getLineNumber());
        final List<Expression> args = callNode.getArgs();
        final Expression function = callNode.getFunction();
        final Block currentBlock = ((CodeGeneratorLexicalContext) this.lc).getCurrentBlock();
        final CodeGeneratorLexicalContext codegenLexicalContext = (CodeGeneratorLexicalContext) this.lc;
        function.accept(new SimpleNodeVisitor() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.3
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
            }

            private MethodEmitter sharedScopeCall(final IdentNode identNode, final int flags) {
                final Symbol symbol = identNode.getSymbol();
                final boolean isFastScope = CodeGenerator.this.isFastScope(symbol);
                new OptimisticOperation(callNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.3.1
                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                        if (isFastScope) {
                            CodeGenerator.this.method.load(CodeGenerator.this.getScopeProtoDepth(currentBlock, symbol));
                        } else {
                            CodeGenerator.this.method.load(-1);
                        }
                        CodeGenerator.this.loadArgs(args);
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        Type[] paramTypes = CodeGenerator.this.method.getTypesFromStack(args.size());
                        for (int i2 = 0; i2 < paramTypes.length; i2++) {
                            paramTypes[i2] = Type.generic(paramTypes[i2]);
                        }
                        SharedScopeCall scopeCall = codegenLexicalContext.getScopeCall(CodeGenerator.this.unit, symbol, identNode.getType(), resultBounds.widest, paramTypes, flags);
                        scopeCall.generateInvoke(CodeGenerator.this.method);
                    }
                }.emit();
                return CodeGenerator.this.method;
            }

            private void scopeCall(final IdentNode ident, final int flags) {
                new OptimisticOperation(callNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.3.2
                    int argsCount;

                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(ident);
                        CodeGenerator.this.method.loadUndefined(Type.OBJECT);
                        this.argsCount = CodeGenerator.this.loadArgs(args);
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        dynamicCall(2 + this.argsCount, flags, ident.getName());
                    }
                }.emit();
            }

            private void evalCall(final IdentNode ident, final int flags) {
                final Label invoke_direct_eval = new Label("invoke_direct_eval");
                final Label is_not_eval = new Label("is_not_eval");
                final Label eval_done = new Label("eval_done");
                new OptimisticOperation(callNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.3.3
                    int argsCount;

                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(ident.setIsNotFunction());
                        CodeGenerator.this.globalIsEval();
                        CodeGenerator.this.method.ifeq(is_not_eval);
                        CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                        List<Expression> evalArgs = callNode.getEvalArgs().getArgs();
                        CodeGenerator.this.loadExpressionAsObject(evalArgs.get(0));
                        int numArgs = evalArgs.size();
                        for (int i2 = 1; i2 < numArgs; i2++) {
                            CodeGenerator.this.loadAndDiscard(evalArgs.get(i2));
                        }
                        CodeGenerator.this.method._goto(invoke_direct_eval);
                        CodeGenerator.this.method.label(is_not_eval);
                        CodeGenerator.this.loadExpressionAsObject(ident);
                        CodeGenerator.this.method.loadNull();
                        this.argsCount = CodeGenerator.this.loadArgs(callNode.getArgs());
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        dynamicCall(2 + this.argsCount, flags, Constants.ELEMNAME_EVAL_STRING);
                        CodeGenerator.this.method._goto(eval_done);
                        CodeGenerator.this.method.label(invoke_direct_eval);
                        CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.THIS);
                        CodeGenerator.this.method.load(callNode.getEvalArgs().getLocation());
                        CodeGenerator.this.method.load(((CodeGeneratorLexicalContext) CodeGenerator.this.lc).getCurrentFunction().isStrict());
                        CodeGenerator.this.globalDirectEval();
                        convertOptimisticReturnValue();
                        CodeGenerator.this.coerceStackTop(resultBounds);
                    }
                }.emit();
                CodeGenerator.this.method.label(eval_done);
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterIdentNode(IdentNode node) {
                Symbol symbol = node.getSymbol();
                if (symbol.isScope()) {
                    int flags = CodeGenerator.this.getScopeCallSiteFlags(symbol);
                    int useCount = symbol.getUseCount();
                    if (callNode.isEval()) {
                        evalCall(node, flags);
                    } else if (useCount <= 4 || ((!CodeGenerator.this.isFastScope(symbol) && useCount <= 500) || ((CodeGeneratorLexicalContext) CodeGenerator.this.lc).inDynamicScope() || callNode.isOptimistic())) {
                        scopeCall(node, flags);
                    } else {
                        sharedScopeCall(node, flags);
                    }
                    if (!$assertionsDisabled && !CodeGenerator.this.method.peekType().equals(resultBounds.within(callNode.getType()))) {
                        throw new AssertionError((Object) (((Object) CodeGenerator.this.method.peekType()) + " != " + ((Object) resultBounds) + "(" + ((Object) callNode.getType()) + ")"));
                    }
                    return false;
                }
                enterDefault(node);
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterAccessNode(final AccessNode node) {
                final int flags = CodeGenerator.this.getCallSiteFlags() | (callNode.isApplyToCall() ? 16 : 0);
                new OptimisticOperation(callNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.3.4
                    int argCount;
                    static final /* synthetic */ boolean $assertionsDisabled;

                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    static {
                        $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(node.getBase());
                        CodeGenerator.this.method.dup();
                        if (!$assertionsDisabled && node.isOptimistic()) {
                            throw new AssertionError();
                        }
                        CodeGenerator.this.method.dynamicGet(node.getType(), node.getProperty(), flags, true, node.isIndex());
                        CodeGenerator.this.method.swap();
                        this.argCount = CodeGenerator.this.loadArgs(args);
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        dynamicCall(2 + this.argCount, flags, node.toString(false));
                    }
                }.emit();
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterFunctionNode(final FunctionNode origCallee) {
                new OptimisticOperation(callNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.3.5
                    FunctionNode callee;
                    int argsCount;

                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        this.callee = (FunctionNode) origCallee.accept(CodeGenerator.this);
                        if (this.callee.isStrict()) {
                            CodeGenerator.this.method.loadUndefined(Type.OBJECT);
                        } else {
                            CodeGenerator.this.globalInstance();
                        }
                        this.argsCount = CodeGenerator.this.loadArgs(args);
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        dynamicCall(2 + this.argsCount, CodeGenerator.this.getCallSiteFlags(), null);
                    }
                }.emit();
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            public boolean enterIndexNode(final IndexNode node) {
                new OptimisticOperation(callNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.3.6
                    int argsCount;
                    static final /* synthetic */ boolean $assertionsDisabled;

                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    static {
                        $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(node.getBase());
                        CodeGenerator.this.method.dup();
                        Type indexType = node.getIndex().getType();
                        if (indexType.isObject() || indexType.isBoolean()) {
                            CodeGenerator.this.loadExpressionAsObject(node.getIndex());
                        } else {
                            CodeGenerator.this.loadExpressionUnbounded(node.getIndex());
                        }
                        if (!$assertionsDisabled && node.isOptimistic()) {
                            throw new AssertionError();
                        }
                        CodeGenerator.this.method.dynamicGetIndex(node.getType(), CodeGenerator.this.getCallSiteFlags(), true);
                        CodeGenerator.this.method.swap();
                        this.argsCount = CodeGenerator.this.loadArgs(args);
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        dynamicCall(2 + this.argsCount, CodeGenerator.this.getCallSiteFlags(), node.toString(false));
                    }
                }.emit();
                return false;
            }

            @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
            protected boolean enterDefault(final Node node) {
                new OptimisticOperation(callNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.3.7
                    int argsCount;

                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        CodeGenerator.this.loadExpressionAsObject(function);
                        CodeGenerator.this.method.loadUndefined(Type.OBJECT);
                        this.argsCount = CodeGenerator.this.loadArgs(args);
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        int flags = CodeGenerator.this.getCallSiteFlags() | 1;
                        dynamicCall(2 + this.argsCount, flags, node.toString(false));
                    }
                }.emit();
                return false;
            }
        });
        return false;
    }

    static int nonOptimisticFlags(int flags) {
        return flags & XMLGrammarCachingConfiguration.BIG_PRIME;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterContinueNode(ContinueNode continueNode) {
        return enterJumpStatement(continueNode);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterEmptyNode(EmptyNode emptyNode) {
        return false;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterExpressionStatement(ExpressionStatement expressionStatement) {
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(expressionStatement);
        loadAndDiscard(expressionStatement.getExpression());
        if ($assertionsDisabled || this.method.getStackSize() == 0) {
            return false;
        }
        throw new AssertionError((Object) ("stack not empty in " + ((Object) expressionStatement)));
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterBlockStatement(BlockStatement blockStatement) {
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(blockStatement);
        blockStatement.getBlock().accept(this);
        return false;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterForNode(ForNode forNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(forNode);
        if (forNode.isForIn()) {
            enterForIn(forNode);
            return false;
        }
        Expression init = forNode.getInit();
        if (init != null) {
            loadAndDiscard(init);
        }
        enterForOrWhile(forNode, forNode.getModify());
        return false;
    }

    private void enterForIn(final ForNode forNode) {
        loadExpression(forNode.getModify(), TypeBounds.OBJECT);
        this.method.invoke(forNode.isForEach() ? ScriptRuntime.TO_VALUE_ITERATOR : ScriptRuntime.TO_PROPERTY_ITERATOR);
        Symbol iterSymbol = forNode.getIterator();
        final int iterSlot = iterSymbol.getSlot(Type.OBJECT);
        this.method.store(iterSymbol, ITERATOR_TYPE);
        this.method.beforeJoinPoint(forNode);
        Label continueLabel = forNode.getContinueLabel();
        Label breakLabel = forNode.getBreakLabel();
        this.method.label(continueLabel);
        this.method.load(ITERATOR_TYPE, iterSlot);
        this.method.invoke(CompilerConstants.interfaceCallNoLookup(ITERATOR_CLASS, "hasNext", Boolean.TYPE, new Class[0]));
        JoinPredecessorExpression test = forNode.getTest();
        Block body = forNode.getBody();
        if (LocalVariableConversion.hasLiveConversion(test)) {
            Label afterConversion = new Label("for_in_after_test_conv");
            this.method.ifne(afterConversion);
            this.method.beforeJoinPoint(test);
            this.method._goto(breakLabel);
            this.method.label(afterConversion);
        } else {
            this.method.ifeq(breakLabel);
        }
        new Store<Expression>(forNode.getInit()) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.4
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
            protected void storeNonDiscard() {
            }

            @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
            protected void evaluate() {
                new OptimisticOperation((Optimistic) forNode.getInit(), TypeBounds.UNBOUNDED) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.4.1
                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        CodeGenerator.this.method.load(CodeGenerator.ITERATOR_TYPE, iterSlot);
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        CodeGenerator.this.method.invoke(CompilerConstants.interfaceCallNoLookup(CodeGenerator.ITERATOR_CLASS, com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.NEXT, Object.class, new Class[0]));
                        convertOptimisticReturnValue();
                    }
                }.emit();
            }
        }.store();
        body.accept(this);
        if (this.method.isReachable()) {
            this.method._goto(continueLabel);
        }
        this.method.label(breakLabel);
    }

    private void initLocals(Block block) {
        Symbol paramSymbol;
        IdentNode nextParam;
        Type type;
        ((CodeGeneratorLexicalContext) this.lc).onEnterBlock(block);
        boolean isFunctionBody = ((CodeGeneratorLexicalContext) this.lc).isFunctionBody();
        FunctionNode function = ((CodeGeneratorLexicalContext) this.lc).getCurrentFunction();
        if (isFunctionBody) {
            initializeMethodParameters(function);
            if (!function.isVarArg()) {
                expandParameterSlots(function);
            }
            if (this.method.hasScope()) {
                if (function.needsParentScope()) {
                    this.method.loadCompilerConstant(CompilerConstants.CALLEE);
                    this.method.invoke(ScriptFunction.GET_SCOPE);
                } else {
                    if (!$assertionsDisabled && !function.hasScopeBlock()) {
                        throw new AssertionError();
                    }
                    this.method.loadNull();
                }
                this.method.storeCompilerConstant(CompilerConstants.SCOPE);
            }
            if (function.needsArguments()) {
                initArguments(function);
            }
        }
        if (block.needsScope()) {
            boolean varsInScope = function.allVarsInScope();
            boolean hasArguments = function.needsArguments();
            List<MapTuple<Symbol>> tuples = new ArrayList<>();
            Iterator<IdentNode> paramIter = function.getParameters().iterator();
            for (Symbol symbol : block.getSymbols()) {
                if (!symbol.isInternal() && !symbol.isThis()) {
                    if (symbol.isVar()) {
                        if (!$assertionsDisabled && varsInScope && !symbol.isScope()) {
                            throw new AssertionError();
                        }
                        if (varsInScope || symbol.isScope()) {
                            if (!$assertionsDisabled && !symbol.isScope()) {
                                throw new AssertionError((Object) ("scope for " + ((Object) symbol) + " should have been set in Lower already " + function.getName()));
                            }
                            if (!$assertionsDisabled && symbol.hasSlot()) {
                                throw new AssertionError((Object) ("slot for " + ((Object) symbol) + " should have been removed in Lower already" + function.getName()));
                            }
                            tuples.add(new MapTuple<>(symbol.getName(), symbol, null));
                        } else if (!$assertionsDisabled && !symbol.hasSlot() && symbol.slotCount() != 0) {
                            throw new AssertionError((Object) (((Object) symbol) + " should have a slot only, no scope"));
                        }
                    } else if (symbol.isParam() && (varsInScope || hasArguments || symbol.isScope())) {
                        if (!$assertionsDisabled && !symbol.isScope()) {
                            throw new AssertionError((Object) ("scope for " + ((Object) symbol) + " should have been set in AssignSymbols already " + function.getName() + " varsInScope=" + varsInScope + " hasArguments=" + hasArguments + " symbol.isScope()=" + symbol.isScope()));
                        }
                        if (!$assertionsDisabled && hasArguments && symbol.hasSlot()) {
                            throw new AssertionError((Object) ("slot for " + ((Object) symbol) + " should have been removed in Lower already " + function.getName()));
                        }
                        if (hasArguments) {
                            if (!$assertionsDisabled && symbol.hasSlot()) {
                                throw new AssertionError((Object) ("slot for " + ((Object) symbol) + " should have been removed in Lower already "));
                            }
                            paramSymbol = null;
                            type = null;
                        } else {
                            paramSymbol = symbol;
                            do {
                                nextParam = paramIter.next();
                            } while (!nextParam.getName().equals(symbol.getName()));
                            type = nextParam.getType();
                        }
                        final Type paramType = type;
                        tuples.add(new MapTuple<Symbol>(symbol.getName(), symbol, paramType, paramSymbol) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.5
                            @Override // jdk.nashorn.internal.codegen.MapTuple
                            public Class<?> getValueType() {
                                if (!CodeGenerator.this.useDualFields() || this.value == 0 || paramType == null || paramType.isBoolean()) {
                                    return Object.class;
                                }
                                return paramType.getTypeClass();
                            }
                        });
                    }
                }
            }
            new FieldObjectCreator<Symbol>(this, tuples, true, hasArguments) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.6
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // jdk.nashorn.internal.codegen.ObjectCreator
                public void loadValue(Symbol value, Type type2) {
                    CodeGenerator.this.method.load(value, type2);
                }
            }.makeObject(this.method);
            if (isFunctionBody && function.isProgram()) {
                this.method.invoke(ScriptRuntime.MERGE_SCOPE);
            }
            this.method.storeCompilerConstant(CompilerConstants.SCOPE);
            if (!isFunctionBody) {
                Label scopeEntryLabel = new Label("scope_entry");
                this.scopeEntryLabels.push(scopeEntryLabel);
                this.method.label(scopeEntryLabel);
            }
        } else if (isFunctionBody && function.isVarArg()) {
            int nextParam2 = 0;
            for (IdentNode param : function.getParameters()) {
                int i2 = nextParam2;
                nextParam2++;
                param.getSymbol().setFieldIndex(i2);
            }
        }
        printSymbols(block, function, (isFunctionBody ? "Function " : "Block in ") + (function.getIdent() == null ? "<anonymous>" : function.getIdent().getName()));
    }

    private void initializeMethodParameters(FunctionNode function) {
        Label functionStart = new Label("fn_start");
        this.method.label(functionStart);
        int nextSlot = 0;
        if (function.needsCallee()) {
            nextSlot = 0 + 1;
            initializeInternalFunctionParameter(CompilerConstants.CALLEE, function, functionStart, 0);
        }
        int i2 = nextSlot;
        int nextSlot2 = nextSlot + 1;
        initializeInternalFunctionParameter(CompilerConstants.THIS, function, functionStart, i2);
        if (function.isVarArg()) {
            int i3 = nextSlot2 + 1;
            initializeInternalFunctionParameter(CompilerConstants.VARARGS, function, functionStart, nextSlot2);
            return;
        }
        for (IdentNode param : function.getParameters()) {
            Symbol symbol = param.getSymbol();
            if (symbol.isBytecodeLocal()) {
                this.method.initializeMethodParameter(symbol, param.getType(), functionStart);
            }
        }
    }

    private void initializeInternalFunctionParameter(CompilerConstants cc, FunctionNode fn, Label functionStart, int slot) {
        Symbol symbol = initializeInternalFunctionOrSplitParameter(cc, fn, functionStart, slot);
        if (!$assertionsDisabled && symbol.getFirstSlot() != slot) {
            throw new AssertionError();
        }
    }

    private Symbol initializeInternalFunctionOrSplitParameter(CompilerConstants cc, FunctionNode fn, Label functionStart, int slot) {
        Symbol symbol = fn.getBody().getExistingSymbol(cc.symbolName());
        Type type = Type.typeFor(cc.type());
        this.method.initializeMethodParameter(symbol, type, functionStart);
        this.method.onLocalStore(type, slot);
        return symbol;
    }

    private void expandParameterSlots(FunctionNode function) {
        List<IdentNode> parameters = function.getParameters();
        int currentIncomingSlot = function.needsCallee() ? 2 : 1;
        Iterator<IdentNode> it = parameters.iterator();
        while (it.hasNext()) {
            currentIncomingSlot += it.next().getType().getSlots();
        }
        int i2 = parameters.size();
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 > 0) {
                IdentNode parameter = parameters.get(i2);
                Type parameterType = parameter.getType();
                int typeWidth = parameterType.getSlots();
                currentIncomingSlot -= typeWidth;
                Symbol symbol = parameter.getSymbol();
                int slotCount = symbol.slotCount();
                if (!$assertionsDisabled && slotCount <= 0) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && !symbol.isBytecodeLocal() && slotCount != typeWidth) {
                    throw new AssertionError();
                }
                this.method.onLocalStore(parameterType, currentIncomingSlot);
                if (currentIncomingSlot != symbol.getSlot(parameterType)) {
                    this.method.load(parameterType, currentIncomingSlot);
                    this.method.store(symbol, parameterType);
                }
            } else {
                return;
            }
        }
    }

    private void initArguments(FunctionNode function) {
        this.method.loadCompilerConstant(CompilerConstants.VARARGS);
        if (function.needsCallee()) {
            this.method.loadCompilerConstant(CompilerConstants.CALLEE);
        } else {
            if (!$assertionsDisabled && !function.isStrict()) {
                throw new AssertionError();
            }
            this.method.loadNull();
        }
        this.method.load(function.getParameters().size());
        globalAllocateArguments();
        this.method.storeCompilerConstant(CompilerConstants.ARGUMENTS);
    }

    private boolean skipFunction(FunctionNode functionNode) {
        ScriptEnvironment env = this.compiler.getScriptEnvironment();
        boolean lazy = env._lazy_compilation;
        boolean onDemand = this.compiler.isOnDemandCompilation();
        if ((onDemand || lazy) && ((CodeGeneratorLexicalContext) this.lc).getOutermostFunction() != functionNode) {
            return true;
        }
        return !onDemand && lazy && env._optimistic_types && functionNode.isProgram();
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterFunctionNode(FunctionNode functionNode) {
        if (skipFunction(functionNode)) {
            newFunctionObject(functionNode, false);
            return false;
        }
        String fnName = functionNode.getName();
        if (!this.emittedMethods.contains(fnName)) {
            this.log.info("=== BEGIN ", fnName);
            if (!$assertionsDisabled && functionNode.getCompileUnit() == null) {
                throw new AssertionError((Object) ("no compile unit for " + fnName + " " + Debug.id(functionNode)));
            }
            this.unit = ((CodeGeneratorLexicalContext) this.lc).pushCompileUnit(functionNode.getCompileUnit());
            if (!$assertionsDisabled && !((CodeGeneratorLexicalContext) this.lc).hasCompileUnits()) {
                throw new AssertionError();
            }
            ClassEmitter classEmitter = this.unit.getClassEmitter();
            pushMethodEmitter(isRestOf() ? classEmitter.restOfMethod(functionNode) : classEmitter.method(functionNode));
            this.method.setPreventUndefinedLoad();
            if (useOptimisticTypes()) {
                ((CodeGeneratorLexicalContext) this.lc).pushUnwarrantedOptimismHandlers();
            }
            this.lastLineNumber = -1;
            this.method.begin();
            if (isRestOf()) {
                if (!$assertionsDisabled && this.continuationInfo != null) {
                    throw new AssertionError();
                }
                this.continuationInfo = new ContinuationInfo();
                this.method.gotoLoopStart(this.continuationInfo.getHandlerLabel());
                return true;
            }
            return true;
        }
        return true;
    }

    private void pushMethodEmitter(MethodEmitter newMethod) {
        this.method = ((CodeGeneratorLexicalContext) this.lc).pushMethodEmitter(newMethod);
        this.catchLabels.push(METHOD_BOUNDARY);
    }

    private void popMethodEmitter() {
        this.method = ((CodeGeneratorLexicalContext) this.lc).popMethodEmitter(this.method);
        if (!$assertionsDisabled && this.catchLabels.peek() != METHOD_BOUNDARY) {
            throw new AssertionError();
        }
        this.catchLabels.pop();
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveFunctionNode(FunctionNode functionNode) {
        boolean markOptimistic;
        try {
            if (this.emittedMethods.add(functionNode.getName())) {
                markOptimistic = generateUnwarrantedOptimismExceptionHandlers(functionNode);
                generateContinuationHandler();
                this.method.end();
                this.unit = ((CodeGeneratorLexicalContext) this.lc).popCompileUnit(functionNode.getCompileUnit());
                popMethodEmitter();
                this.log.info("=== END ", functionNode.getName());
            } else {
                markOptimistic = false;
            }
            FunctionNode newFunctionNode = functionNode;
            if (markOptimistic) {
                newFunctionNode = newFunctionNode.setFlag(this.lc, 2048);
            }
            newFunctionObject(newFunctionNode, true);
            return newFunctionNode;
        } catch (Throwable t2) {
            Context.printStackTrace(t2);
            VerifyError e2 = new VerifyError("Code generation bug in \"" + functionNode.getName() + "\": likely stack misaligned: " + ((Object) t2) + " " + functionNode.getSource().getName());
            e2.initCause(t2);
            throw e2;
        }
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterIfNode(IfNode ifNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(ifNode);
        Expression test = ifNode.getTest();
        Block pass = ifNode.getPass();
        Block fail = ifNode.getFail();
        if (Expression.isAlwaysTrue(test)) {
            loadAndDiscard(test);
            pass.accept(this);
            return false;
        }
        if (Expression.isAlwaysFalse(test)) {
            loadAndDiscard(test);
            if (fail != null) {
                fail.accept(this);
                return false;
            }
            return false;
        }
        boolean hasFailConversion = LocalVariableConversion.hasLiveConversion(ifNode);
        Label failLabel = new Label("if_fail");
        Label afterLabel = (fail != null || hasFailConversion) ? new Label("if_done") : null;
        emitBranch(test, failLabel, false);
        pass.accept(this);
        if (this.method.isReachable() && afterLabel != null) {
            this.method._goto(afterLabel);
        }
        this.method.label(failLabel);
        if (fail != null) {
            fail.accept(this);
        } else if (hasFailConversion) {
            this.method.beforeJoinPoint(ifNode);
        }
        if (afterLabel != null && afterLabel.isReachable()) {
            this.method.label(afterLabel);
            return false;
        }
        return false;
    }

    private void emitBranch(Expression test, Label label, boolean jumpWhenTrue) {
        new BranchOptimizer(this, this.method).execute(test, label, jumpWhenTrue);
    }

    private void enterStatement(Statement statement) {
        lineNumber(statement);
    }

    private void lineNumber(Statement statement) {
        lineNumber(statement.getLineNumber());
    }

    private void lineNumber(int lineNumber) {
        if (lineNumber != this.lastLineNumber && lineNumber != -1) {
            this.method.lineNumber(lineNumber);
            this.lastLineNumber = lineNumber;
        }
    }

    int getLastLineNumber() {
        return this.lastLineNumber;
    }

    private void loadArray(LiteralNode.ArrayLiteralNode arrayLiteralNode, ArrayType arrayType) {
        if (!$assertionsDisabled && arrayType != Type.INT_ARRAY && arrayType != Type.NUMBER_ARRAY && arrayType != Type.OBJECT_ARRAY) {
            throw new AssertionError();
        }
        final Expression[] nodes = arrayLiteralNode.getValue();
        Object presets = arrayLiteralNode.getPresets();
        final int[] postsets = arrayLiteralNode.getPostsets();
        List<Splittable.SplitRange> ranges = arrayLiteralNode.getSplitRanges();
        loadConstant(presets);
        final Type elementType = arrayType.getElementType();
        if (ranges != null) {
            loadSplitLiteral(new SplitLiteralCreator() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.7
                @Override // jdk.nashorn.internal.codegen.CodeGenerator.SplitLiteralCreator
                public void populateRange(MethodEmitter method, Type type, int slot, int start, int end) {
                    for (int i2 = start; i2 < end; i2++) {
                        method.load(type, slot);
                        CodeGenerator.this.storeElement(nodes, elementType, postsets[i2]);
                    }
                    method.load(type, slot);
                }
            }, ranges, arrayType);
            return;
        }
        if (postsets.length > 0) {
            int arraySlot = this.method.getUsedSlotsWithLiveTemporaries();
            this.method.storeTemp(arrayType, arraySlot);
            for (int postset : postsets) {
                this.method.load(arrayType, arraySlot);
                storeElement(nodes, elementType, postset);
            }
            this.method.load(arrayType, arraySlot);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeElement(Expression[] nodes, Type elementType, int index) {
        this.method.load(index);
        Expression element = nodes[index];
        if (element == null) {
            this.method.loadEmpty(elementType);
        } else {
            loadExpressionAsType(element, elementType);
        }
        this.method.arraystore();
    }

    private MethodEmitter loadArgsArray(List<Expression> args) {
        Object[] array = new Object[args.size()];
        loadConstant(array);
        for (int i2 = 0; i2 < args.size(); i2++) {
            this.method.dup();
            this.method.load(i2);
            loadExpression(args.get(i2), TypeBounds.OBJECT);
            this.method.arraystore();
        }
        return this.method;
    }

    void loadConstant(String string) {
        String unitClassName = this.unit.getUnitClassName();
        ClassEmitter classEmitter = this.unit.getClassEmitter();
        int index = this.compiler.getConstantData().add(string);
        this.method.load(index);
        this.method.invokestatic(unitClassName, CompilerConstants.GET_STRING.symbolName(), CompilerConstants.methodDescriptor(String.class, Integer.TYPE));
        classEmitter.needGetConstantMethod(String.class);
    }

    void loadConstant(Object object) {
        loadConstant(object, this.unit, this.method);
    }

    private void loadConstant(Object object, CompileUnit compileUnit, MethodEmitter methodEmitter) {
        String unitClassName = compileUnit.getUnitClassName();
        ClassEmitter classEmitter = compileUnit.getClassEmitter();
        int index = this.compiler.getConstantData().add(object);
        Class<?> cls = object.getClass();
        if (cls == PropertyMap.class) {
            methodEmitter.load(index);
            methodEmitter.invokestatic(unitClassName, CompilerConstants.GET_MAP.symbolName(), CompilerConstants.methodDescriptor(PropertyMap.class, Integer.TYPE));
            classEmitter.needGetConstantMethod(PropertyMap.class);
        } else {
            if (cls.isArray()) {
                methodEmitter.load(index);
                String methodName = ClassEmitter.getArrayMethodName(cls);
                methodEmitter.invokestatic(unitClassName, methodName, CompilerConstants.methodDescriptor(cls, Integer.TYPE));
                classEmitter.needGetConstantMethod(cls);
                return;
            }
            methodEmitter.loadConstants().load(index).arrayload();
            if (object instanceof ArrayData) {
                methodEmitter.checkcast(ArrayData.class);
                methodEmitter.invoke(CompilerConstants.virtualCallNoLookup(ArrayData.class, "copy", ArrayData.class, new Class[0]));
            } else if (cls != Object.class) {
                methodEmitter.checkcast(cls);
            }
        }
    }

    private void loadConstantsAndIndex(Object object, MethodEmitter methodEmitter) {
        methodEmitter.loadConstants().load(this.compiler.getConstantData().add(object));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadLiteral(LiteralNode<?> node, TypeBounds resultBounds) {
        Object value = node.getValue();
        if (value == null) {
            this.method.loadNull();
            return;
        }
        if (value instanceof Undefined) {
            this.method.loadUndefined(resultBounds.within(Type.OBJECT));
            return;
        }
        if (value instanceof String) {
            String string = (String) value;
            if (string.length() > 10922) {
                loadConstant(string);
                return;
            } else {
                this.method.load(string);
                return;
            }
        }
        if (value instanceof Lexer.RegexToken) {
            loadRegex((Lexer.RegexToken) value);
            return;
        }
        if (value instanceof Boolean) {
            this.method.load(((Boolean) value).booleanValue());
            return;
        }
        if (value instanceof Integer) {
            if (!resultBounds.canBeNarrowerThan(Type.OBJECT)) {
                this.method.load(((Integer) value).intValue());
                this.method.convert(Type.OBJECT);
                return;
            } else if (!resultBounds.canBeNarrowerThan(Type.NUMBER)) {
                this.method.load(((Integer) value).doubleValue());
                return;
            } else {
                this.method.load(((Integer) value).intValue());
                return;
            }
        }
        if (value instanceof Double) {
            if (!resultBounds.canBeNarrowerThan(Type.OBJECT)) {
                this.method.load(((Double) value).doubleValue());
                this.method.convert(Type.OBJECT);
                return;
            } else {
                this.method.load(((Double) value).doubleValue());
                return;
            }
        }
        if (node instanceof LiteralNode.ArrayLiteralNode) {
            LiteralNode.ArrayLiteralNode arrayLiteral = (LiteralNode.ArrayLiteralNode) node;
            ArrayType atype = arrayLiteral.getArrayType();
            loadArray(arrayLiteral, atype);
            globalAllocateArray(atype);
            return;
        }
        throw new UnsupportedOperationException("Unknown literal for " + ((Object) node.getClass()) + " " + ((Object) value.getClass()) + " " + value);
    }

    private MethodEmitter loadRegexToken(Lexer.RegexToken value) {
        this.method.load(value.getExpression());
        this.method.load(value.getOptions());
        return globalNewRegExp();
    }

    private MethodEmitter loadRegex(Lexer.RegexToken regexToken) {
        if (this.regexFieldCount > 2048) {
            return loadRegexToken(regexToken);
        }
        String regexName = ((CodeGeneratorLexicalContext) this.lc).getCurrentFunction().uniqueName(CompilerConstants.REGEX_PREFIX.symbolName());
        ClassEmitter classEmitter = this.unit.getClassEmitter();
        classEmitter.field(EnumSet.of(ClassEmitter.Flag.PRIVATE, ClassEmitter.Flag.STATIC), regexName, Object.class);
        this.regexFieldCount++;
        this.method.getStatic(this.unit.getUnitClassName(), regexName, CompilerConstants.typeDescriptor(Object.class));
        this.method.dup();
        Label cachedLabel = new Label("cached");
        this.method.ifnonnull(cachedLabel);
        this.method.pop();
        loadRegexToken(regexToken);
        this.method.dup();
        this.method.putStatic(this.unit.getUnitClassName(), regexName, CompilerConstants.typeDescriptor(Object.class));
        this.method.label(cachedLabel);
        globalRegExpCopy();
        return this.method;
    }

    /* renamed from: jdk.nashorn.internal.codegen.CodeGenerator$8, reason: invalid class name */
    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$8.class */
    static class AnonymousClass8 implements Supplier<Boolean> {
        boolean contains;
        final /* synthetic */ Expression val$value;
        final /* synthetic */ int val$pp;

        AnonymousClass8(Expression expression, int i2) {
            this.val$value = expression;
            this.val$pp = i2;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        public Boolean get() {
            this.val$value.accept(new SimpleNodeVisitor() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.8.1
                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterFunctionNode(FunctionNode functionNode) {
                    return false;
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterDefault(Node node) {
                    if (AnonymousClass8.this.contains) {
                        return false;
                    }
                    if ((node instanceof Optimistic) && ((Optimistic) node).getProgramPoint() == AnonymousClass8.this.val$pp) {
                        AnonymousClass8.this.contains = true;
                        return false;
                    }
                    return true;
                }
            });
            return Boolean.valueOf(this.contains);
        }
    }

    private static boolean propertyValueContains(Expression value, int pp) {
        return new AnonymousClass8(value, pp).get().booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadObjectNode(ObjectNode objectNode) {
        ObjectCreator<?> oc;
        List<PropertyNode> elements = objectNode.getElements();
        List<MapTuple<Expression>> tuples = new ArrayList<>();
        List<PropertyNode> gettersSetters = new ArrayList<>();
        int ccp = getCurrentContinuationEntryPoint();
        List<Splittable.SplitRange> ranges = objectNode.getSplitRanges();
        Expression protoNode = null;
        boolean restOfProperty = false;
        for (PropertyNode propertyNode : elements) {
            Expression value = propertyNode.getValue();
            String key = propertyNode.getKeyName();
            Symbol symbol = value == null ? null : new Symbol(key, 0);
            if (value == null) {
                gettersSetters.add(propertyNode);
            } else if ((propertyNode.getKey() instanceof IdentNode) && key.equals(ScriptObject.PROTO_PROPERTY_NAME)) {
                protoNode = value;
            }
            restOfProperty |= value != null && UnwarrantedOptimismException.isValid(ccp) && propertyValueContains(value, ccp);
            Class<?> valueType = (!useDualFields() || value == null || value.getType().isBoolean()) ? Object.class : value.getType().getTypeClass();
            tuples.add(new MapTuple<Expression>(key, symbol, Type.typeFor(valueType), value) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.9
                @Override // jdk.nashorn.internal.codegen.MapTuple
                public Class<?> getValueType() {
                    return this.type.getTypeClass();
                }
            });
        }
        if (elements.size() > OBJECT_SPILL_THRESHOLD) {
            oc = new SpillObjectCreator(this, tuples);
        } else {
            oc = new FieldObjectCreator<Expression>(this, tuples) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.10
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // jdk.nashorn.internal.codegen.ObjectCreator
                public void loadValue(Expression node, Type type) {
                    CodeGenerator.this.loadExpressionAsType(node, Type.generic(type));
                }
            };
        }
        if (ranges != null) {
            oc.createObject(this.method);
            loadSplitLiteral(oc, ranges, Type.typeFor(oc.getAllocatorClass()));
        } else {
            oc.makeObject(this.method);
        }
        if (restOfProperty) {
            ContinuationInfo ci = getContinuationInfo();
            ci.setObjectLiteralMap(this.method.getStackSize(), oc.getMap());
        }
        this.method.dup();
        if (protoNode != null) {
            loadExpressionAsObject(protoNode);
            this.method.convert(Type.OBJECT);
            this.method.invoke(ScriptObject.SET_PROTO_FROM_LITERAL);
        } else {
            this.method.invoke(ScriptObject.SET_GLOBAL_OBJECT_PROTO);
        }
        for (PropertyNode propertyNode2 : gettersSetters) {
            FunctionNode getter = propertyNode2.getGetter();
            FunctionNode setter = propertyNode2.getSetter();
            if (!$assertionsDisabled && getter == null && setter == null) {
                throw new AssertionError();
            }
            this.method.dup().loadKey(propertyNode2.getKey());
            if (getter == null) {
                this.method.loadNull();
            } else {
                getter.accept(this);
            }
            if (setter == null) {
                this.method.loadNull();
            } else {
                setter.accept(this);
            }
            this.method.invoke(ScriptObject.SET_USER_ACCESSORS);
        }
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterReturnNode(ReturnNode returnNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(returnNode);
        Type returnType = ((CodeGeneratorLexicalContext) this.lc).getCurrentFunction().getReturnType();
        Expression expression = returnNode.getExpression();
        if (expression != null) {
            loadExpressionUnbounded(expression);
        } else {
            this.method.loadUndefined(returnType);
        }
        this.method._return(returnType);
        return false;
    }

    private boolean undefinedCheck(RuntimeNode runtimeNode, List<Expression> args) {
        Symbol undefinedSymbol;
        RuntimeNode.Request request = runtimeNode.getRequest();
        if (!RuntimeNode.Request.isUndefinedCheck(request)) {
            return false;
        }
        Expression lhs = args.get(0);
        Expression rhs = args.get(1);
        Symbol lhsSymbol = lhs instanceof IdentNode ? ((IdentNode) lhs).getSymbol() : null;
        Symbol rhsSymbol = rhs instanceof IdentNode ? ((IdentNode) rhs).getSymbol() : null;
        if (!$assertionsDisabled && lhsSymbol == null && rhsSymbol == null) {
            throw new AssertionError();
        }
        if (isUndefinedSymbol(lhsSymbol)) {
            undefinedSymbol = lhsSymbol;
        } else {
            if (!$assertionsDisabled && !isUndefinedSymbol(rhsSymbol)) {
                throw new AssertionError();
            }
            undefinedSymbol = rhsSymbol;
        }
        if (!$assertionsDisabled && undefinedSymbol == null) {
            throw new AssertionError();
        }
        if (!undefinedSymbol.isScope()) {
            return false;
        }
        if ((lhsSymbol == undefinedSymbol && lhs.getType().isPrimitive()) || isDeoptimizedExpression(lhs) || !this.compiler.isGlobalSymbol(((CodeGeneratorLexicalContext) this.lc).getCurrentFunction(), "undefined")) {
            return false;
        }
        boolean isUndefinedCheck = request == RuntimeNode.Request.IS_UNDEFINED;
        Expression expr = undefinedSymbol == lhsSymbol ? rhs : lhs;
        if (expr.getType().isPrimitive()) {
            loadAndDiscard(expr);
            this.method.load(!isUndefinedCheck);
            return true;
        }
        Label checkTrue = new Label("ud_check_true");
        Label end = new Label(AsmConstants.END);
        loadExpressionAsObject(expr);
        this.method.loadUndefined(Type.OBJECT);
        this.method.if_acmpeq(checkTrue);
        this.method.load(!isUndefinedCheck);
        this.method._goto(end);
        this.method.label(checkTrue);
        this.method.load(isUndefinedCheck);
        this.method.label(end);
        return true;
    }

    private static boolean isUndefinedSymbol(Symbol symbol) {
        return symbol != null && "undefined".equals(symbol.getName());
    }

    private static boolean isNullLiteral(Node node) {
        return (node instanceof LiteralNode) && ((LiteralNode) node).isNull();
    }

    private boolean nullCheck(RuntimeNode runtimeNode, List<Expression> args) {
        Label popLabel;
        RuntimeNode.Request request = runtimeNode.getRequest();
        if (!RuntimeNode.Request.isEQ(request) && !RuntimeNode.Request.isNE(request)) {
            return false;
        }
        if (!$assertionsDisabled && args.size() != 2) {
            throw new AssertionError((Object) "EQ or NE or TYPEOF need two args");
        }
        Expression lhs = args.get(0);
        Expression rhs = args.get(1);
        if (isNullLiteral(lhs)) {
            lhs = rhs;
            rhs = lhs;
        }
        if (!isNullLiteral(rhs) || !lhs.getType().isObject() || isDeoptimizedExpression(lhs)) {
            return false;
        }
        Label trueLabel = new Label("trueLabel");
        Label falseLabel = new Label("falseLabel");
        Label endLabel = new Label(AsmConstants.END);
        loadExpressionUnbounded(lhs);
        if (!RuntimeNode.Request.isStrict(request)) {
            this.method.dup();
            popLabel = new Label("pop");
        } else {
            popLabel = null;
        }
        if (RuntimeNode.Request.isEQ(request)) {
            this.method.ifnull(!RuntimeNode.Request.isStrict(request) ? popLabel : trueLabel);
            if (!RuntimeNode.Request.isStrict(request)) {
                this.method.loadUndefined(Type.OBJECT);
                this.method.if_acmpeq(trueLabel);
            }
            this.method.label(falseLabel);
            this.method.load(false);
            this.method._goto(endLabel);
            if (!RuntimeNode.Request.isStrict(request)) {
                this.method.label(popLabel);
                this.method.pop();
            }
            this.method.label(trueLabel);
            this.method.load(true);
            this.method.label(endLabel);
        } else if (RuntimeNode.Request.isNE(request)) {
            this.method.ifnull(!RuntimeNode.Request.isStrict(request) ? popLabel : falseLabel);
            if (!RuntimeNode.Request.isStrict(request)) {
                this.method.loadUndefined(Type.OBJECT);
                this.method.if_acmpeq(falseLabel);
            }
            this.method.label(trueLabel);
            this.method.load(true);
            this.method._goto(endLabel);
            if (!RuntimeNode.Request.isStrict(request)) {
                this.method.label(popLabel);
                this.method.pop();
            }
            this.method.label(falseLabel);
            this.method.load(false);
            this.method.label(endLabel);
        }
        if (!$assertionsDisabled && !runtimeNode.getType().isBoolean()) {
            throw new AssertionError();
        }
        this.method.convert(runtimeNode.getType());
        return true;
    }

    private boolean isDeoptimizedExpression(Expression rootExpr) {
        if (!isRestOf()) {
            return false;
        }
        return new AnonymousClass11(rootExpr).get().booleanValue();
    }

    /* renamed from: jdk.nashorn.internal.codegen.CodeGenerator$11, reason: invalid class name */
    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$11.class */
    class AnonymousClass11 implements Supplier<Boolean> {
        boolean contains;
        final /* synthetic */ Expression val$rootExpr;

        AnonymousClass11(Expression expression) {
            this.val$rootExpr = expression;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        public Boolean get() {
            this.val$rootExpr.accept(new SimpleNodeVisitor() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.11.1
                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterFunctionNode(FunctionNode functionNode) {
                    return false;
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterDefault(Node node) {
                    if (!AnonymousClass11.this.contains && (node instanceof Optimistic)) {
                        int pp = ((Optimistic) node).getProgramPoint();
                        AnonymousClass11.this.contains = UnwarrantedOptimismException.isValid(pp) && CodeGenerator.this.isContinuationEntryPoint(pp);
                    }
                    return !AnonymousClass11.this.contains;
                }
            });
            return Boolean.valueOf(this.contains);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadRuntimeNode(RuntimeNode runtimeNode) {
        RuntimeNode newRuntimeNode;
        List<Expression> args = new ArrayList<>(runtimeNode.getArgs());
        if (nullCheck(runtimeNode, args) || undefinedCheck(runtimeNode, args)) {
            return;
        }
        RuntimeNode.Request request = runtimeNode.getRequest();
        if (RuntimeNode.Request.isUndefinedCheck(request)) {
            newRuntimeNode = runtimeNode.setRequest(request == RuntimeNode.Request.IS_UNDEFINED ? RuntimeNode.Request.EQ_STRICT : RuntimeNode.Request.NE_STRICT);
        } else {
            newRuntimeNode = runtimeNode;
        }
        for (Expression arg : args) {
            loadExpression(arg, TypeBounds.OBJECT);
        }
        this.method.invokestatic(CompilerConstants.className(ScriptRuntime.class), newRuntimeNode.getRequest().toString(), new FunctionSignature(false, false, newRuntimeNode.getType(), args.size()).toString());
        this.method.convert(newRuntimeNode.getType());
    }

    private void defineCommonSplitMethodParameters() {
        defineSplitMethodParameter(0, CompilerConstants.CALLEE);
        defineSplitMethodParameter(1, CompilerConstants.THIS);
        defineSplitMethodParameter(2, CompilerConstants.SCOPE);
    }

    private void defineSplitMethodParameter(int slot, CompilerConstants cc) {
        defineSplitMethodParameter(slot, Type.typeFor(cc.type()));
    }

    private void defineSplitMethodParameter(int slot, Type type) {
        this.method.defineBlockLocalVariable(slot, slot + type.getSlots());
        this.method.onLocalStore(type, slot);
    }

    private void loadSplitLiteral(SplitLiteralCreator creator, List<Splittable.SplitRange> ranges, Type literalType) {
        if (!$assertionsDisabled && ranges == null) {
            throw new AssertionError();
        }
        MethodEmitter savedMethod = this.method;
        FunctionNode currentFunction = ((CodeGeneratorLexicalContext) this.lc).getCurrentFunction();
        for (Splittable.SplitRange splitRange : ranges) {
            this.unit = ((CodeGeneratorLexicalContext) this.lc).pushCompileUnit(splitRange.getCompileUnit());
            if (!$assertionsDisabled && this.unit == null) {
                throw new AssertionError();
            }
            String className = this.unit.getUnitClassName();
            String name = currentFunction.uniqueName(CompilerConstants.SPLIT_PREFIX.symbolName());
            Class<?> clazz = literalType.getTypeClass();
            String signature = CompilerConstants.methodDescriptor(clazz, ScriptFunction.class, Object.class, ScriptObject.class, clazz);
            pushMethodEmitter(this.unit.getClassEmitter().method(EnumSet.of(ClassEmitter.Flag.PUBLIC, ClassEmitter.Flag.STATIC), name, signature));
            this.method.setFunctionNode(currentFunction);
            this.method.begin();
            defineCommonSplitMethodParameters();
            defineSplitMethodParameter(CompilerConstants.SPLIT_ARRAY_ARG.slot(), literalType);
            int literalSlot = fixScopeSlot(currentFunction, 3);
            ((CodeGeneratorLexicalContext) this.lc).enterSplitNode();
            creator.populateRange(this.method, literalType, literalSlot, splitRange.getLow(), splitRange.getHigh());
            this.method._return();
            ((CodeGeneratorLexicalContext) this.lc).exitSplitNode();
            this.method.end();
            ((CodeGeneratorLexicalContext) this.lc).releaseSlots();
            popMethodEmitter();
            if (!$assertionsDisabled && this.method != savedMethod) {
                throw new AssertionError();
            }
            this.method.loadCompilerConstant(CompilerConstants.CALLEE).swap();
            this.method.loadCompilerConstant(CompilerConstants.THIS).swap();
            this.method.loadCompilerConstant(CompilerConstants.SCOPE).swap();
            this.method.invokestatic(className, name, signature);
            this.unit = ((CodeGeneratorLexicalContext) this.lc).popCompileUnit(this.unit);
        }
    }

    private int fixScopeSlot(FunctionNode functionNode, int extraSlot) {
        int actualScopeSlot = functionNode.compilerConstant(CompilerConstants.SCOPE).getSlot(SCOPE_TYPE);
        int defaultScopeSlot = CompilerConstants.SCOPE.slot();
        int newExtraSlot = extraSlot;
        if (actualScopeSlot != defaultScopeSlot) {
            if (actualScopeSlot == extraSlot) {
                newExtraSlot = extraSlot + 1;
                this.method.defineBlockLocalVariable(newExtraSlot, newExtraSlot + 1);
                this.method.load(Type.OBJECT, extraSlot);
                this.method.storeHidden(Type.OBJECT, newExtraSlot);
            } else {
                this.method.defineBlockLocalVariable(actualScopeSlot, actualScopeSlot + 1);
            }
            this.method.load(SCOPE_TYPE, defaultScopeSlot);
            this.method.storeCompilerConstant(CompilerConstants.SCOPE);
        }
        return newExtraSlot;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterSplitReturn(SplitReturn splitReturn) {
        if (this.method.isReachable()) {
            this.method.loadUndefined(((CodeGeneratorLexicalContext) this.lc).getCurrentFunction().getReturnType())._return();
            return false;
        }
        return false;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterSetSplitState(SetSplitState setSplitState) {
        if (this.method.isReachable()) {
            this.method.setSplitState(setSplitState.getState());
            return false;
        }
        return false;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterSwitchNode(SwitchNode switchNode) {
        Label fallThroughLabel;
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(switchNode);
        Expression expression = switchNode.getExpression();
        List<CaseNode> cases = switchNode.getCases();
        if (cases.isEmpty()) {
            loadAndDiscard(expression);
            return false;
        }
        CaseNode defaultCase = switchNode.getDefaultCase();
        Label breakLabel = switchNode.getBreakLabel();
        int liveLocalsOnBreak = this.method.getUsedSlotsWithLiveTemporaries();
        if (defaultCase != null && cases.size() == 1) {
            if (!$assertionsDisabled && cases.get(0) != defaultCase) {
                throw new AssertionError();
            }
            loadAndDiscard(expression);
            defaultCase.getBody().accept(this);
            this.method.breakLabel(breakLabel, liveLocalsOnBreak);
            return false;
        }
        Label defaultLabel = defaultCase != null ? defaultCase.getEntry() : breakLabel;
        boolean hasSkipConversion = LocalVariableConversion.hasLiveConversion(switchNode);
        if (switchNode.isUniqueInteger()) {
            TreeMap<Integer, Label> tree = new TreeMap<>();
            for (CaseNode caseNode : cases) {
                Node test = caseNode.getTest();
                if (test != null) {
                    Integer value = (Integer) ((LiteralNode) test).getValue();
                    Label entry = caseNode.getEntry();
                    if (!tree.containsKey(value)) {
                        tree.put(value, entry);
                    }
                }
            }
            int size = tree.size();
            Integer[] values = (Integer[]) tree.keySet().toArray(new Integer[size]);
            Label[] labels = (Label[]) tree.values().toArray(new Label[size]);
            int lo = values[0].intValue();
            int hi = values[size - 1].intValue();
            long range = (hi - lo) + 1;
            int deflt = Integer.MIN_VALUE;
            for (Integer num : values) {
                int value2 = num.intValue();
                if (deflt == value2) {
                    deflt++;
                } else if (deflt < value2) {
                    break;
                }
            }
            loadExpressionUnbounded(expression);
            Type type = expression.getType();
            if (!type.isInteger()) {
                this.method.load(deflt);
                Class<?> exprClass = type.getTypeClass();
                MethodEmitter methodEmitter = this.method;
                Class<Integer> cls = Integer.TYPE;
                Class[] clsArr = new Class[2];
                clsArr[0] = exprClass.isPrimitive() ? exprClass : Object.class;
                clsArr[1] = Integer.TYPE;
                methodEmitter.invoke(CompilerConstants.staticCallNoLookup(ScriptRuntime.class, "switchTagAsInt", cls, clsArr));
            }
            if (hasSkipConversion) {
                if (!$assertionsDisabled && defaultLabel != breakLabel) {
                    throw new AssertionError();
                }
                defaultLabel = new Label("switch_skip");
            }
            if (range + 1 <= size * 2 && range <= 2147483647L) {
                Label[] table = new Label[(int) range];
                Arrays.fill(table, defaultLabel);
                for (int i2 = 0; i2 < size; i2++) {
                    table[values[i2].intValue() - lo] = labels[i2];
                }
                this.method.tableswitch(lo, hi, defaultLabel, table);
            } else {
                int[] ints = new int[size];
                for (int i3 = 0; i3 < size; i3++) {
                    ints[i3] = values[i3].intValue();
                }
                this.method.lookupswitch(defaultLabel, ints, labels);
            }
            if (hasSkipConversion) {
                this.method.label(defaultLabel);
                this.method.beforeJoinPoint(switchNode);
                this.method._goto(breakLabel);
            }
        } else {
            Symbol tagSymbol = switchNode.getTag();
            int tagSlot = tagSymbol.getSlot(Type.OBJECT);
            loadExpressionAsObject(expression);
            this.method.store(tagSymbol, Type.OBJECT);
            for (CaseNode caseNode2 : cases) {
                Expression test2 = caseNode2.getTest();
                if (test2 != null) {
                    this.method.load(Type.OBJECT, tagSlot);
                    loadExpressionAsObject(test2);
                    this.method.invoke(ScriptRuntime.EQ_STRICT);
                    this.method.ifne(caseNode2.getEntry());
                }
            }
            if (defaultCase != null) {
                this.method._goto(defaultLabel);
            } else {
                this.method.beforeJoinPoint(switchNode);
                this.method._goto(breakLabel);
            }
        }
        if (!$assertionsDisabled && this.method.isReachable()) {
            throw new AssertionError();
        }
        for (CaseNode caseNode3 : cases) {
            if (caseNode3.getLocalVariableConversion() != null && this.method.isReachable()) {
                fallThroughLabel = new Label("fallthrough");
                this.method._goto(fallThroughLabel);
            } else {
                fallThroughLabel = null;
            }
            this.method.label(caseNode3.getEntry());
            this.method.beforeJoinPoint(caseNode3);
            if (fallThroughLabel != null) {
                this.method.label(fallThroughLabel);
            }
            caseNode3.getBody().accept(this);
        }
        this.method.breakLabel(breakLabel, liveLocalsOnBreak);
        return false;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterThrowNode(ThrowNode throwNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(throwNode);
        if (throwNode.isSyntheticRethrow()) {
            this.method.beforeJoinPoint(throwNode);
            IdentNode exceptionExpr = (IdentNode) throwNode.getExpression();
            Symbol exceptionSymbol = exceptionExpr.getSymbol();
            this.method.load(exceptionSymbol, EXCEPTION_TYPE);
            this.method.checkcast(EXCEPTION_TYPE.getTypeClass());
            this.method.athrow();
            return false;
        }
        Source source = getCurrentSource();
        Expression expression = throwNode.getExpression();
        int position = throwNode.position();
        int line = throwNode.getLineNumber();
        int column = source.getColumn(position);
        loadExpressionAsObject(expression);
        this.method.load(source.getName());
        this.method.load(line);
        this.method.load(column);
        this.method.invoke(ECMAException.CREATE);
        this.method.beforeJoinPoint(throwNode);
        this.method.athrow();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Source getCurrentSource() {
        return ((CodeGeneratorLexicalContext) this.lc).getCurrentFunction().getSource();
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterTryNode(TryNode tryNode) {
        Label nextCatch;
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(tryNode);
        Block body = tryNode.getBody();
        List<Block> catchBlocks = tryNode.getCatchBlocks();
        final Symbol vmException = tryNode.getException();
        Label entry = new Label("try");
        Label recovery = new Label("catch");
        Label exit = new Label("end_try");
        Label skip = new Label(SchemaSymbols.ATTVAL_SKIP);
        this.method.canThrow(recovery);
        this.method.beforeTry(tryNode, recovery);
        this.method.label(entry);
        this.catchLabels.push(recovery);
        try {
            body.accept(this);
            if (!$assertionsDisabled && this.catchLabels.peek() != recovery) {
                throw new AssertionError();
            }
            this.catchLabels.pop();
            this.method.label(exit);
            boolean bodyCanThrow = exit.isAfter(entry);
            if (!bodyCanThrow) {
                return false;
            }
            this.method._try(entry, exit, recovery, Throwable.class);
            if (this.method.isReachable()) {
                this.method._goto(skip);
            }
            for (Block inlinedFinally : tryNode.getInlinedFinallies()) {
                TryNode.getLabelledInlinedFinallyBlock(inlinedFinally).accept(this);
                if (!$assertionsDisabled && this.method.isReachable()) {
                    throw new AssertionError();
                }
            }
            this.method._catch(recovery);
            this.method.store(vmException, EXCEPTION_TYPE);
            int catchBlockCount = catchBlocks.size();
            Label afterCatch = new Label("after_catch");
            for (int i2 = 0; i2 < catchBlockCount; i2++) {
                if (!$assertionsDisabled && !this.method.isReachable()) {
                    throw new AssertionError();
                }
                Block catchBlock = catchBlocks.get(i2);
                ((CodeGeneratorLexicalContext) this.lc).push(catchBlock);
                enterBlock(catchBlock);
                final CatchNode catchNode = (CatchNode) catchBlocks.get(i2).getStatements().get(0);
                IdentNode exception = catchNode.getException();
                Expression exceptionCondition = catchNode.getExceptionCondition();
                Block catchBody = catchNode.getBody();
                new Store<IdentNode>(exception) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.12
                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
                    protected void storeNonDiscard() {
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
                    protected void evaluate() {
                        if (catchNode.isSyntheticRethrow()) {
                            CodeGenerator.this.method.load(vmException, CodeGenerator.EXCEPTION_TYPE);
                            return;
                        }
                        Label notEcmaException = new Label("no_ecma_exception");
                        CodeGenerator.this.method.load(vmException, CodeGenerator.EXCEPTION_TYPE).dup()._instanceof(ECMAException.class).ifeq(notEcmaException);
                        CodeGenerator.this.method.checkcast(ECMAException.class);
                        CodeGenerator.this.method.getField(ECMAException.THROWN);
                        CodeGenerator.this.method.label(notEcmaException);
                    }
                }.store();
                boolean isConditionalCatch = exceptionCondition != null;
                if (isConditionalCatch) {
                    loadExpressionAsBoolean(exceptionCondition);
                    nextCatch = new Label("next_catch");
                    nextCatch.markAsBreakTarget();
                    this.method.ifeq(nextCatch);
                } else {
                    nextCatch = null;
                }
                catchBody.accept(this);
                leaveBlock(catchBlock);
                ((CodeGeneratorLexicalContext) this.lc).pop(catchBlock);
                if (nextCatch != null) {
                    if (this.method.isReachable()) {
                        this.method._goto(afterCatch);
                    }
                    this.method.breakLabel(nextCatch, ((CodeGeneratorLexicalContext) this.lc).getUsedSlotCount());
                }
            }
            this.method.label(afterCatch);
            if (this.method.isReachable()) {
                this.method.markDeadLocalVariable(vmException);
            }
            this.method.label(skip);
            if ($assertionsDisabled || tryNode.getFinallyBody() == null) {
                return false;
            }
            throw new AssertionError();
        } catch (Throwable th) {
            if (!$assertionsDisabled && this.catchLabels.peek() != recovery) {
                throw new AssertionError();
            }
            this.catchLabels.pop();
            throw th;
        }
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterVarNode(VarNode varNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        Expression init = varNode.getInit();
        IdentNode identNode = varNode.getName();
        Symbol identSymbol = identNode.getSymbol();
        if (!$assertionsDisabled && identSymbol == null) {
            throw new AssertionError((Object) ("variable node " + ((Object) varNode) + " requires a name with a symbol"));
        }
        boolean needsScope = identSymbol.isScope();
        if (init == null) {
            if (needsScope && varNode.isBlockScoped()) {
                this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                this.method.loadUndefined(Type.OBJECT);
                int flags = getScopeCallSiteFlags(identSymbol) | (varNode.isBlockScoped() ? 32 : 0);
                if (!$assertionsDisabled && !isFastScope(identSymbol)) {
                    throw new AssertionError();
                }
                storeFastScopeVar(identSymbol, flags);
                return false;
            }
            return false;
        }
        enterStatement(varNode);
        if (!$assertionsDisabled && this.method == null) {
            throw new AssertionError();
        }
        if (needsScope) {
            this.method.loadCompilerConstant(CompilerConstants.SCOPE);
        }
        if (needsScope) {
            loadExpressionUnbounded(init);
            int flags2 = getScopeCallSiteFlags(identSymbol) | (varNode.isBlockScoped() ? 32 : 0);
            if (isFastScope(identSymbol)) {
                storeFastScopeVar(identSymbol, flags2);
                return false;
            }
            this.method.dynamicSet(identNode.getName(), flags2, false);
            return false;
        }
        Type identType = identNode.getType();
        if (identType == Type.UNDEFINED) {
            if (!$assertionsDisabled && init.getType() != Type.UNDEFINED && identNode.getSymbol().slotCount() != 0) {
                throw new AssertionError();
            }
            loadAndDiscard(init);
            return false;
        }
        loadExpressionAsType(init, identType);
        storeIdentWithCatchConversion(identNode, identType);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeIdentWithCatchConversion(IdentNode identNode, Type type) {
        LocalVariableConversion conversion = identNode.getLocalVariableConversion();
        Symbol symbol = identNode.getSymbol();
        if (conversion != null && conversion.isLive()) {
            if (!$assertionsDisabled && symbol != conversion.getSymbol()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !symbol.isBytecodeLocal()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && conversion.getNext() != null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && conversion.getFrom() != type) {
                throw new AssertionError();
            }
            Label catchLabel = this.catchLabels.peek();
            if (!$assertionsDisabled && catchLabel == METHOD_BOUNDARY) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !catchLabel.isReachable()) {
                throw new AssertionError();
            }
            Type joinType = conversion.getTo();
            Label.Stack catchStack = catchLabel.getStack();
            int joinSlot = symbol.getSlot(joinType);
            if (catchStack.getUsedSlotsWithLiveTemporaries() > joinSlot) {
                this.method.dup();
                this.method.convert(joinType);
                this.method.store(symbol, joinType);
                catchLabel.getStack().onLocalStore(joinType, joinSlot, true);
                this.method.canThrow(catchLabel);
                this.method.store(symbol, type, false);
                return;
            }
        }
        this.method.store(symbol, type, true);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterWhileNode(WhileNode whileNode) {
        if (!this.method.isReachable()) {
            return false;
        }
        if (whileNode.isDoWhile()) {
            enterDoWhile(whileNode);
            return false;
        }
        enterStatement(whileNode);
        enterForOrWhile(whileNode, null);
        return false;
    }

    private void enterForOrWhile(LoopNode loopNode, JoinPredecessorExpression modify) {
        int liveLocalsOnBreak = this.method.getUsedSlotsWithLiveTemporaries();
        JoinPredecessorExpression test = loopNode.getTest();
        if (Expression.isAlwaysFalse(test)) {
            loadAndDiscard(test);
            return;
        }
        this.method.beforeJoinPoint(loopNode);
        Label continueLabel = loopNode.getContinueLabel();
        Label repeatLabel = modify != null ? new Label("for_repeat") : continueLabel;
        this.method.label(repeatLabel);
        int liveLocalsOnContinue = this.method.getUsedSlotsWithLiveTemporaries();
        Block body = loopNode.getBody();
        Label breakLabel = loopNode.getBreakLabel();
        boolean testHasLiveConversion = test != null && LocalVariableConversion.hasLiveConversion(test);
        if (Expression.isAlwaysTrue(test)) {
            if (test != null) {
                loadAndDiscard(test);
                if (testHasLiveConversion) {
                    this.method.beforeJoinPoint(test);
                }
            }
        } else if (test != null) {
            if (testHasLiveConversion) {
                emitBranch(test.getExpression(), body.getEntryLabel(), true);
                this.method.beforeJoinPoint(test);
                this.method._goto(breakLabel);
            } else {
                emitBranch(test.getExpression(), breakLabel, false);
            }
        }
        body.accept(this);
        if (repeatLabel != continueLabel) {
            emitContinueLabel(continueLabel, liveLocalsOnContinue);
        }
        if (loopNode.hasPerIterationScope() && ((CodeGeneratorLexicalContext) this.lc).getCurrentBlock().needsScope()) {
            this.method.loadCompilerConstant(CompilerConstants.SCOPE);
            this.method.invoke(CompilerConstants.virtualCallNoLookup(ScriptObject.class, "copy", ScriptObject.class, new Class[0]));
            this.method.storeCompilerConstant(CompilerConstants.SCOPE);
        }
        if (this.method.isReachable()) {
            if (modify != null) {
                lineNumber(loopNode);
                loadAndDiscard(modify);
                this.method.beforeJoinPoint(modify);
            }
            this.method._goto(repeatLabel);
        }
        this.method.breakLabel(breakLabel, liveLocalsOnBreak);
    }

    private void emitContinueLabel(Label continueLabel, int liveLocals) {
        boolean reachable = this.method.isReachable();
        this.method.breakLabel(continueLabel, liveLocals);
        if (!reachable) {
            this.method.undefineLocalVariables(((CodeGeneratorLexicalContext) this.lc).getUsedSlotCount(), false);
        }
    }

    private void enterDoWhile(WhileNode whileNode) {
        int liveLocalsOnContinueOrBreak = this.method.getUsedSlotsWithLiveTemporaries();
        this.method.beforeJoinPoint(whileNode);
        Block body = whileNode.getBody();
        body.accept(this);
        emitContinueLabel(whileNode.getContinueLabel(), liveLocalsOnContinueOrBreak);
        if (this.method.isReachable()) {
            lineNumber(whileNode);
            JoinPredecessorExpression test = whileNode.getTest();
            Label bodyEntryLabel = body.getEntryLabel();
            boolean testHasLiveConversion = LocalVariableConversion.hasLiveConversion(test);
            if (Expression.isAlwaysFalse(test)) {
                loadAndDiscard(test);
                if (testHasLiveConversion) {
                    this.method.beforeJoinPoint(test);
                }
            } else if (testHasLiveConversion) {
                Label beforeExit = new Label("do_while_preexit");
                emitBranch(test.getExpression(), beforeExit, false);
                this.method.beforeJoinPoint(test);
                this.method._goto(bodyEntryLabel);
                this.method.label(beforeExit);
                this.method.beforeJoinPoint(test);
            } else {
                emitBranch(test.getExpression(), bodyEntryLabel, true);
            }
        }
        this.method.breakLabel(whileNode.getBreakLabel(), liveLocalsOnContinueOrBreak);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterWithNode(WithNode withNode) {
        Label tryLabel;
        if (!this.method.isReachable()) {
            return false;
        }
        enterStatement(withNode);
        Expression expression = withNode.getExpression();
        Block body = withNode.getBody();
        boolean hasScope = this.method.hasScope();
        if (hasScope) {
            this.method.loadCompilerConstant(CompilerConstants.SCOPE);
        }
        loadExpressionAsObject(expression);
        if (hasScope) {
            this.method.invoke(ScriptRuntime.OPEN_WITH);
            this.method.storeCompilerConstant(CompilerConstants.SCOPE);
            tryLabel = new Label("with_try");
            this.method.label(tryLabel);
        } else {
            globalCheckObjectCoercible();
            tryLabel = null;
        }
        body.accept(this);
        if (hasScope) {
            Label endLabel = new Label("with_end");
            Label catchLabel = new Label("with_catch");
            Label exitLabel = new Label("with_exit");
            this.method.label(endLabel);
            boolean bodyCanThrow = endLabel.isAfter(tryLabel);
            if (bodyCanThrow) {
                this.method._try(tryLabel, endLabel, catchLabel);
            }
            boolean reachable = this.method.isReachable();
            if (reachable) {
                popScope();
                if (bodyCanThrow) {
                    this.method._goto(exitLabel);
                }
            }
            if (bodyCanThrow) {
                this.method._catch(catchLabel);
                popScopeException();
                this.method.athrow();
                if (reachable) {
                    this.method.label(exitLabel);
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadADD(UnaryNode unaryNode, TypeBounds resultBounds) {
        loadExpression(unaryNode.getExpression(), resultBounds.booleanToInt().notWiderThan(Type.NUMBER));
        if (this.method.peekType() == Type.BOOLEAN) {
            this.method.convert(Type.INT);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadBIT_NOT(UnaryNode unaryNode) {
        loadExpression(unaryNode.getExpression(), TypeBounds.INT).load(-1).xor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadDECINC(final UnaryNode unaryNode) {
        final Expression operand = unaryNode.getExpression();
        final Type type = unaryNode.getType();
        final TypeBounds typeBounds = new TypeBounds(type, Type.NUMBER);
        TokenType tokenType = unaryNode.tokenType();
        final boolean isPostfix = tokenType == TokenType.DECPOSTFIX || tokenType == TokenType.INCPOSTFIX;
        final boolean isIncrement = tokenType == TokenType.INCPREFIX || tokenType == TokenType.INCPOSTFIX;
        if (!$assertionsDisabled && type.isObject()) {
            throw new AssertionError();
        }
        new SelfModifyingStore<UnaryNode>(unaryNode, operand) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.13
            /* JADX INFO: Access modifiers changed from: private */
            public void loadRhs() {
                CodeGenerator.this.loadExpression(operand, typeBounds, true);
            }

            @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
            protected void evaluate() {
                if (!isPostfix) {
                    new OptimisticOperation(unaryNode, typeBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.13.1
                        {
                            CodeGenerator codeGenerator = CodeGenerator.this;
                        }

                        @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                        void loadStack() {
                            loadRhs();
                            loadMinusOne();
                        }

                        @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                        void consumeStack() {
                            doDecInc(getProgramPoint());
                        }
                    }.emit(CodeGenerator.getOptimisticIgnoreCountForSelfModifyingExpression(operand));
                } else {
                    loadRhs();
                }
            }

            @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
            protected void storeNonDiscard() {
                super.storeNonDiscard();
                if (isPostfix) {
                    new OptimisticOperation(unaryNode, typeBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.13.2
                        {
                            CodeGenerator codeGenerator = CodeGenerator.this;
                        }

                        @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                        void loadStack() {
                            loadMinusOne();
                        }

                        @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                        void consumeStack() {
                            doDecInc(getProgramPoint());
                        }
                    }.emit(1);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void loadMinusOne() {
                if (type.isInteger()) {
                    CodeGenerator.this.method.load(isIncrement ? 1 : -1);
                } else {
                    CodeGenerator.this.method.load(isIncrement ? 1.0d : -1.0d);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void doDecInc(int programPoint) {
                CodeGenerator.this.method.add(programPoint);
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getOptimisticIgnoreCountForSelfModifyingExpression(Expression target) {
        if (target instanceof AccessNode) {
            return 1;
        }
        return target instanceof IndexNode ? 2 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadAndDiscard(Expression expr) {
        if ((expr instanceof LiteralNode.PrimitiveLiteralNode) | isLocalVariable(expr)) {
            if (!$assertionsDisabled && ((CodeGeneratorLexicalContext) this.lc).isCurrentDiscard(expr)) {
                throw new AssertionError();
            }
            return;
        }
        ((CodeGeneratorLexicalContext) this.lc).pushDiscard(expr);
        loadExpression(expr, TypeBounds.UNBOUNDED);
        if (((CodeGeneratorLexicalContext) this.lc).popDiscardIfCurrent(expr)) {
            if (!$assertionsDisabled && expr.isAssignment()) {
                throw new AssertionError();
            }
            this.method.pop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMaybeDiscard(Expression parent, Expression expr, TypeBounds resultBounds) {
        loadMaybeDiscard(((CodeGeneratorLexicalContext) this.lc).popDiscardIfCurrent(parent), expr, resultBounds);
    }

    private void loadMaybeDiscard(boolean discard, Expression expr, TypeBounds resultBounds) {
        if (discard) {
            loadAndDiscard(expr);
        } else {
            loadExpression(expr, resultBounds);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadNEW(UnaryNode unaryNode) {
        CallNode callNode = (CallNode) unaryNode.getExpression();
        List<Expression> args = callNode.getArgs();
        Expression func = callNode.getFunction();
        loadExpressionAsObject(func);
        this.method.dynamicNew(1 + loadArgs(args), getCallSiteFlags(), func.toString(false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadNOT(UnaryNode unaryNode) {
        Expression expr = unaryNode.getExpression();
        if ((expr instanceof UnaryNode) && expr.isTokenType(TokenType.NOT)) {
            loadExpressionAsBoolean(((UnaryNode) expr).getExpression());
            return;
        }
        Label trueLabel = new Label("true");
        Label afterLabel = new Label("after");
        emitBranch(expr, trueLabel, true);
        this.method.load(true);
        this.method._goto(afterLabel);
        this.method.label(trueLabel);
        this.method.load(false);
        this.method.label(afterLabel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSUB(final UnaryNode unaryNode, TypeBounds resultBounds) {
        final Type type = unaryNode.getType();
        if (!$assertionsDisabled && !type.isNumeric()) {
            throw new AssertionError();
        }
        final TypeBounds numericBounds = resultBounds.booleanToInt();
        new OptimisticOperation(unaryNode, numericBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.14
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
            void loadStack() {
                Expression expr = unaryNode.getExpression();
                CodeGenerator.this.loadExpression(expr, numericBounds.notWiderThan(Type.NUMBER));
            }

            @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
            void consumeStack() {
                if (type.isNumber()) {
                    CodeGenerator.this.method.convert(type);
                }
                CodeGenerator.this.method.neg(getProgramPoint());
            }
        }.emit();
    }

    public void loadVOID(UnaryNode unaryNode, TypeBounds resultBounds) {
        loadAndDiscard(unaryNode.getExpression());
        if (!((CodeGeneratorLexicalContext) this.lc).popDiscardIfCurrent(unaryNode)) {
            this.method.loadUndefined(resultBounds.widest);
        }
    }

    public void loadADD(final BinaryNode binaryNode, final TypeBounds resultBounds) {
        new OptimisticOperation(binaryNode, resultBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.15
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
            void loadStack() {
                TypeBounds operandBounds;
                boolean isOptimistic = UnwarrantedOptimismException.isValid(getProgramPoint());
                boolean forceConversionSeparation = false;
                if (isOptimistic) {
                    operandBounds = new TypeBounds(binaryNode.getType(), Type.OBJECT);
                } else {
                    Type widestOperationType = binaryNode.getWidestOperationType();
                    operandBounds = new TypeBounds(Type.narrowest(binaryNode.getWidestOperandType(), resultBounds.widest), widestOperationType);
                    forceConversionSeparation = widestOperationType.narrowerThan(resultBounds.widest);
                }
                CodeGenerator.this.loadBinaryOperands(binaryNode.lhs(), binaryNode.rhs(), operandBounds, false, forceConversionSeparation);
            }

            @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
            void consumeStack() {
                CodeGenerator.this.method.add(getProgramPoint());
            }
        }.emit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadAND_OR(BinaryNode binaryNode, TypeBounds resultBounds, boolean isAnd) {
        Type narrowestOperandType = Type.widestReturnType(binaryNode.lhs().getType(), binaryNode.rhs().getType());
        boolean isCurrentDiscard = ((CodeGeneratorLexicalContext) this.lc).popDiscardIfCurrent(binaryNode);
        Label skip = new Label(SchemaSymbols.ATTVAL_SKIP);
        if (narrowestOperandType == Type.BOOLEAN) {
            Label onTrue = new Label("andor_true");
            emitBranch(binaryNode, onTrue, true);
            if (isCurrentDiscard) {
                this.method.label(onTrue);
                return;
            }
            this.method.load(false);
            this.method._goto(skip);
            this.method.label(onTrue);
            this.method.load(true);
            this.method.label(skip);
            return;
        }
        TypeBounds outBounds = resultBounds.notNarrowerThan(narrowestOperandType);
        JoinPredecessorExpression lhs = (JoinPredecessorExpression) binaryNode.lhs();
        boolean lhsConvert = LocalVariableConversion.hasLiveConversion(lhs);
        Label evalRhs = lhsConvert ? new Label("eval_rhs") : null;
        loadExpression(lhs, outBounds);
        if (!isCurrentDiscard) {
            this.method.dup();
        }
        this.method.convert(Type.BOOLEAN);
        if (isAnd) {
            if (lhsConvert) {
                this.method.ifne(evalRhs);
            } else {
                this.method.ifeq(skip);
            }
        } else if (lhsConvert) {
            this.method.ifeq(evalRhs);
        } else {
            this.method.ifne(skip);
        }
        if (lhsConvert) {
            this.method.beforeJoinPoint(lhs);
            this.method._goto(skip);
            this.method.label(evalRhs);
        }
        if (!isCurrentDiscard) {
            this.method.pop();
        }
        JoinPredecessorExpression rhs = (JoinPredecessorExpression) binaryNode.rhs();
        loadMaybeDiscard(isCurrentDiscard, rhs, outBounds);
        this.method.beforeJoinPoint(rhs);
        this.method.label(skip);
    }

    private static boolean isLocalVariable(Expression lhs) {
        return (lhs instanceof IdentNode) && isLocalVariable((IdentNode) lhs);
    }

    private static boolean isLocalVariable(IdentNode lhs) {
        return lhs.getSymbol().isBytecodeLocal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN(BinaryNode binaryNode) {
        Expression lhs = binaryNode.lhs();
        final Expression rhs = binaryNode.rhs();
        final Type rhsType = rhs.getType();
        if (lhs instanceof IdentNode) {
            Symbol symbol = ((IdentNode) lhs).getSymbol();
            if (!symbol.isScope() && !symbol.hasSlotFor(rhsType) && ((CodeGeneratorLexicalContext) this.lc).popDiscardIfCurrent(binaryNode)) {
                loadAndDiscard(rhs);
                this.method.markDeadLocalVariable(symbol);
                return;
            }
        }
        new Store<BinaryNode>(binaryNode, lhs) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.16
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
            protected void evaluate() {
                CodeGenerator.this.loadExpressionAsType(rhs, rhsType);
            }
        }.store();
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$BinaryOptimisticSelfAssignment.class */
    private abstract class BinaryOptimisticSelfAssignment extends SelfModifyingStore<BinaryNode> {
        protected abstract void op(OptimisticOperation optimisticOperation);

        BinaryOptimisticSelfAssignment(BinaryNode node) {
            super(node, node.lhs());
        }

        @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
        protected void evaluate() {
            final Expression lhs = ((BinaryNode) this.assignNode).lhs();
            final Expression rhs = ((BinaryNode) this.assignNode).rhs();
            final Type widestOperationType = ((BinaryNode) this.assignNode).getWidestOperationType();
            final TypeBounds bounds = new TypeBounds(((BinaryNode) this.assignNode).getType(), widestOperationType);
            new OptimisticOperation((Optimistic) this.assignNode, bounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.BinaryOptimisticSelfAssignment.1
                {
                    CodeGenerator codeGenerator = CodeGenerator.this;
                }

                @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                void loadStack() {
                    boolean forceConversionSeparation;
                    if (!UnwarrantedOptimismException.isValid(getProgramPoint()) && widestOperationType != Type.NUMBER) {
                        Type operandType = Type.widest(CodeGenerator.booleanToInt(CodeGenerator.objectToNumber(lhs.getType())), CodeGenerator.booleanToInt(CodeGenerator.objectToNumber(rhs.getType())));
                        forceConversionSeparation = operandType.narrowerThan(widestOperationType);
                    } else {
                        forceConversionSeparation = false;
                    }
                    CodeGenerator.this.loadBinaryOperands(lhs, rhs, bounds, true, forceConversionSeparation);
                }

                @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                void consumeStack() {
                    BinaryOptimisticSelfAssignment.this.op(this);
                }
            }.emit(CodeGenerator.getOptimisticIgnoreCountForSelfModifyingExpression(lhs));
            CodeGenerator.this.method.convert(((BinaryNode) this.assignNode).getType());
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$BinarySelfAssignment.class */
    private abstract class BinarySelfAssignment extends SelfModifyingStore<BinaryNode> {
        protected abstract void op();

        BinarySelfAssignment(BinaryNode node) {
            super(node, node.lhs());
        }

        @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
        protected void evaluate() {
            CodeGenerator.this.loadBinaryOperands(((BinaryNode) this.assignNode).lhs(), ((BinaryNode) this.assignNode).rhs(), TypeBounds.UNBOUNDED.notWiderThan(((BinaryNode) this.assignNode).getWidestOperandType()), true, false);
            op();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_ADD(final BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.17
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
            }

            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinaryOptimisticSelfAssignment
            protected void op(OptimisticOperation oo) {
                if (!$assertionsDisabled && binaryNode.getType().isObject() && oo.isOptimistic) {
                    throw new AssertionError();
                }
                CodeGenerator.this.method.add(oo.getProgramPoint());
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_BIT_AND(BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.18
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinarySelfAssignment
            protected void op() {
                CodeGenerator.this.method.and();
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_BIT_OR(BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.19
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinarySelfAssignment
            protected void op() {
                CodeGenerator.this.method.or();
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_BIT_XOR(BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.20
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinarySelfAssignment
            protected void op() {
                CodeGenerator.this.method.xor();
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_DIV(BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.21
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinaryOptimisticSelfAssignment
            protected void op(OptimisticOperation oo) {
                CodeGenerator.this.method.div(oo.getProgramPoint());
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_MOD(BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.22
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinaryOptimisticSelfAssignment
            protected void op(OptimisticOperation oo) {
                CodeGenerator.this.method.rem(oo.getProgramPoint());
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_MUL(BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.23
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinaryOptimisticSelfAssignment
            protected void op(OptimisticOperation oo) {
                CodeGenerator.this.method.mul(oo.getProgramPoint());
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_SAR(BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.24
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinarySelfAssignment
            protected void op() {
                CodeGenerator.this.method.sar();
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_SHL(BinaryNode binaryNode) {
        new BinarySelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.25
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinarySelfAssignment
            protected void op() {
                CodeGenerator.this.method.shl();
            }
        }.store();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_SHR(final BinaryNode binaryNode) {
        new SelfModifyingStore<BinaryNode>(binaryNode, binaryNode.lhs()) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.26
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
            protected void evaluate() {
                new OptimisticOperation((Optimistic) this.assignNode, new TypeBounds(Type.INT, Type.NUMBER)) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.26.1
                    static final /* synthetic */ boolean $assertionsDisabled;

                    {
                        CodeGenerator codeGenerator = CodeGenerator.this;
                    }

                    static {
                        $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void loadStack() {
                        if (!$assertionsDisabled && ((BinaryNode) AnonymousClass26.this.assignNode).getWidestOperandType() != Type.INT) {
                            throw new AssertionError();
                        }
                        if (CodeGenerator.isRhsZero(binaryNode)) {
                            CodeGenerator.this.loadExpression(binaryNode.lhs(), TypeBounds.INT, true);
                        } else {
                            CodeGenerator.this.loadBinaryOperands(binaryNode.lhs(), binaryNode.rhs(), TypeBounds.INT, true, false);
                            CodeGenerator.this.method.shr();
                        }
                    }

                    @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                    void consumeStack() {
                        if (CodeGenerator.isOptimistic(binaryNode)) {
                            CodeGenerator.this.toUint32Optimistic(binaryNode.getProgramPoint());
                        } else {
                            CodeGenerator.this.toUint32Double();
                        }
                    }
                }.emit(CodeGenerator.getOptimisticIgnoreCountForSelfModifyingExpression(binaryNode.lhs()));
                CodeGenerator.this.method.convert(((BinaryNode) this.assignNode).getType());
            }
        }.store();
    }

    private void doSHR(final BinaryNode binaryNode) {
        new OptimisticOperation(binaryNode, new TypeBounds(Type.INT, Type.NUMBER)) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.27
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
            void loadStack() {
                if (CodeGenerator.isRhsZero(binaryNode)) {
                    CodeGenerator.this.loadExpressionAsType(binaryNode.lhs(), Type.INT);
                } else {
                    CodeGenerator.this.loadBinaryOperands(binaryNode);
                    CodeGenerator.this.method.shr();
                }
            }

            @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
            void consumeStack() {
                if (CodeGenerator.isOptimistic(binaryNode)) {
                    CodeGenerator.this.toUint32Optimistic(binaryNode.getProgramPoint());
                } else {
                    CodeGenerator.this.toUint32Double();
                }
            }
        }.emit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toUint32Optimistic(int programPoint) {
        this.method.load(programPoint);
        JSType.TO_UINT32_OPTIMISTIC.invoke(this.method);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toUint32Double() {
        JSType.TO_UINT32_DOUBLE.invoke(this.method);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadASSIGN_SUB(BinaryNode binaryNode) {
        new BinaryOptimisticSelfAssignment(binaryNode) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.28
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinaryOptimisticSelfAssignment
            protected void op(OptimisticOperation oo) {
                CodeGenerator.this.method.sub(oo.getProgramPoint());
            }
        }.store();
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$BinaryArith.class */
    private abstract class BinaryArith {
        protected abstract void op(int i2);

        private BinaryArith() {
        }

        protected void evaluate(final BinaryNode node, TypeBounds resultBounds) {
            final TypeBounds numericBounds = resultBounds.booleanToInt().objectToNumber();
            new OptimisticOperation(node, numericBounds) { // from class: jdk.nashorn.internal.codegen.CodeGenerator.BinaryArith.1
                static final /* synthetic */ boolean $assertionsDisabled;

                {
                    CodeGenerator codeGenerator = CodeGenerator.this;
                }

                static {
                    $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
                }

                @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                void loadStack() {
                    TypeBounds operandBounds;
                    boolean forceConversionSeparation = false;
                    if (numericBounds.narrowest == Type.NUMBER) {
                        if (!$assertionsDisabled && numericBounds.widest != Type.NUMBER) {
                            throw new AssertionError();
                        }
                        operandBounds = numericBounds;
                    } else {
                        boolean isOptimistic = UnwarrantedOptimismException.isValid(getProgramPoint());
                        if (isOptimistic || node.isTokenType(TokenType.DIV) || node.isTokenType(TokenType.MOD)) {
                            operandBounds = new TypeBounds(node.getType(), Type.NUMBER);
                        } else {
                            operandBounds = new TypeBounds(Type.narrowest(node.getWidestOperandType(), numericBounds.widest), Type.NUMBER);
                            forceConversionSeparation = true;
                        }
                    }
                    CodeGenerator.this.loadBinaryOperands(node.lhs(), node.rhs(), operandBounds, false, forceConversionSeparation);
                }

                @Override // jdk.nashorn.internal.codegen.CodeGenerator.OptimisticOperation
                void consumeStack() {
                    BinaryArith.this.op(getProgramPoint());
                }
            }.emit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadBIT_AND(BinaryNode binaryNode) {
        loadBinaryOperands(binaryNode);
        this.method.and();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadBIT_OR(BinaryNode binaryNode) {
        if (isRhsZero(binaryNode)) {
            loadExpressionAsType(binaryNode.lhs(), Type.INT);
        } else {
            loadBinaryOperands(binaryNode);
            this.method.or();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isRhsZero(BinaryNode binaryNode) {
        Expression rhs = binaryNode.rhs();
        return (rhs instanceof LiteralNode) && INT_ZERO.equals(((LiteralNode) rhs).getValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadBIT_XOR(BinaryNode binaryNode) {
        loadBinaryOperands(binaryNode);
        this.method.xor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadCOMMARIGHT(BinaryNode binaryNode, TypeBounds resultBounds) {
        loadAndDiscard(binaryNode.lhs());
        loadMaybeDiscard(binaryNode, binaryNode.rhs(), resultBounds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadCOMMALEFT(BinaryNode binaryNode, TypeBounds resultBounds) {
        loadMaybeDiscard(binaryNode, binaryNode.lhs(), resultBounds);
        loadAndDiscard(binaryNode.rhs());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadDIV(BinaryNode binaryNode, TypeBounds resultBounds) {
        new BinaryArith() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.29
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinaryArith
            protected void op(int programPoint) {
                CodeGenerator.this.method.div(programPoint);
            }
        }.evaluate(binaryNode, resultBounds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadCmp(BinaryNode binaryNode, Condition cond) {
        loadComparisonOperands(binaryNode);
        Label trueLabel = new Label("trueLabel");
        Label afterLabel = new Label(SchemaSymbols.ATTVAL_SKIP);
        this.method.conditionalJump(cond, trueLabel);
        this.method.load(Boolean.FALSE.booleanValue());
        this.method._goto(afterLabel);
        this.method.label(trueLabel);
        this.method.load(Boolean.TRUE.booleanValue());
        this.method.label(afterLabel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMOD(BinaryNode binaryNode, TypeBounds resultBounds) {
        new BinaryArith() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.30
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinaryArith
            protected void op(int programPoint) {
                CodeGenerator.this.method.rem(programPoint);
            }
        }.evaluate(binaryNode, resultBounds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMUL(BinaryNode binaryNode, TypeBounds resultBounds) {
        new BinaryArith() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.31
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinaryArith
            protected void op(int programPoint) {
                CodeGenerator.this.method.mul(programPoint);
            }
        }.evaluate(binaryNode, resultBounds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSAR(BinaryNode binaryNode) {
        loadBinaryOperands(binaryNode);
        this.method.sar();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSHL(BinaryNode binaryNode) {
        loadBinaryOperands(binaryNode);
        this.method.shl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSHR(BinaryNode binaryNode) {
        doSHR(binaryNode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSUB(BinaryNode binaryNode, TypeBounds resultBounds) {
        new BinaryArith() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.32
            @Override // jdk.nashorn.internal.codegen.CodeGenerator.BinaryArith
            protected void op(int programPoint) {
                CodeGenerator.this.method.sub(programPoint);
            }
        }.evaluate(binaryNode, resultBounds);
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public boolean enterLabelNode(LabelNode labelNode) {
        this.labeledBlockBreakLiveLocals.push(((CodeGeneratorLexicalContext) this.lc).getUsedSlotCount());
        return true;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    protected boolean enterDefault(Node node) {
        throw new AssertionError((Object) ("Code generator entered node of type " + node.getClass().getName()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadTernaryNode(TernaryNode ternaryNode, TypeBounds resultBounds) {
        Expression test = ternaryNode.getTest();
        JoinPredecessorExpression trueExpr = ternaryNode.getTrueExpression();
        JoinPredecessorExpression falseExpr = ternaryNode.getFalseExpression();
        Label falseLabel = new Label("ternary_false");
        Label exitLabel = new Label("ternary_exit");
        Type outNarrowest = Type.narrowest(resultBounds.widest, Type.generic(Type.widestReturnType(trueExpr.getType(), falseExpr.getType())));
        TypeBounds outBounds = resultBounds.notNarrowerThan(outNarrowest);
        emitBranch(test, falseLabel, false);
        boolean isCurrentDiscard = ((CodeGeneratorLexicalContext) this.lc).popDiscardIfCurrent(ternaryNode);
        loadMaybeDiscard(isCurrentDiscard, trueExpr.getExpression(), outBounds);
        if (!$assertionsDisabled && !isCurrentDiscard && Type.generic(this.method.peekType()) != outBounds.narrowest) {
            throw new AssertionError();
        }
        this.method.beforeJoinPoint(trueExpr);
        this.method._goto(exitLabel);
        this.method.label(falseLabel);
        loadMaybeDiscard(isCurrentDiscard, falseExpr.getExpression(), outBounds);
        if (!$assertionsDisabled && !isCurrentDiscard && Type.generic(this.method.peekType()) != outBounds.narrowest) {
            throw new AssertionError();
        }
        this.method.beforeJoinPoint(falseExpr);
        this.method.label(exitLabel);
    }

    void generateScopeCalls() {
        for (SharedScopeCall scopeAccess : ((CodeGeneratorLexicalContext) this.lc).getScopeCalls()) {
            scopeAccess.generateScopeCall();
        }
    }

    private void printSymbols(Block block, FunctionNode function, String ident) {
        if (this.compiler.getScriptEnvironment()._print_symbols || function.getFlag(2097152)) {
            PrintWriter out = this.compiler.getScriptEnvironment().getErr();
            out.println("[BLOCK in '" + ident + "']");
            if (!block.printSymbols(out)) {
                out.println("<no symbols>");
            }
            out.println();
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$SelfModifyingStore.class */
    private abstract class SelfModifyingStore<T extends Expression> extends Store<T> {
        protected SelfModifyingStore(T assignNode, Expression target) {
            super(assignNode, target);
        }

        @Override // jdk.nashorn.internal.codegen.CodeGenerator.Store
        protected boolean isSelfModifying() {
            return true;
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$Store.class */
    private abstract class Store<T extends Expression> {
        protected final T assignNode;
        private final Expression target;
        private int depth;
        private IdentNode quick;
        static final /* synthetic */ boolean $assertionsDisabled;

        protected abstract void evaluate();

        static {
            $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
        }

        protected Store(T assignNode, Expression target) {
            this.assignNode = assignNode;
            this.target = target;
        }

        protected Store(CodeGenerator codeGenerator, T assignNode) {
            this(assignNode, assignNode);
        }

        protected boolean isSelfModifying() {
            return false;
        }

        private void prologue() {
            this.target.accept(new SimpleNodeVisitor() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.Store.1
                static final /* synthetic */ boolean $assertionsDisabled;

                static {
                    $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
                }

                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterIdentNode(IdentNode node) {
                    if (node.getSymbol().isScope()) {
                        CodeGenerator.this.method.loadCompilerConstant(CompilerConstants.SCOPE);
                        Store.this.depth += Type.SCOPE.getSlots();
                        if ($assertionsDisabled || Store.this.depth == 1) {
                            return false;
                        }
                        throw new AssertionError();
                    }
                    return false;
                }

                private void enterBaseNode() {
                    if ($assertionsDisabled || (Store.this.target instanceof BaseNode)) {
                        BaseNode baseNode = (BaseNode) Store.this.target;
                        Expression base = baseNode.getBase();
                        CodeGenerator.this.loadExpressionAsObject(base);
                        Store.this.depth += Type.OBJECT.getSlots();
                        if (!$assertionsDisabled && Store.this.depth != 1) {
                            throw new AssertionError();
                        }
                        if (Store.this.isSelfModifying()) {
                            CodeGenerator.this.method.dup();
                            return;
                        }
                        return;
                    }
                    throw new AssertionError((Object) ("error - base node " + ((Object) Store.this.target) + " must be instanceof BaseNode"));
                }

                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterAccessNode(AccessNode node) {
                    enterBaseNode();
                    return false;
                }

                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterIndexNode(IndexNode node) {
                    enterBaseNode();
                    Expression index = node.getIndex();
                    if (!index.getType().isNumeric()) {
                        CodeGenerator.this.loadExpressionAsObject(index);
                    } else {
                        CodeGenerator.this.loadExpressionUnbounded(index);
                    }
                    Store.this.depth += index.getType().getSlots();
                    if (Store.this.isSelfModifying()) {
                        CodeGenerator.this.method.dup(1);
                        return false;
                    }
                    return false;
                }
            });
        }

        private IdentNode quickLocalVariable(Type type) {
            String name = ((CodeGeneratorLexicalContext) CodeGenerator.this.lc).getCurrentFunction().uniqueName(CompilerConstants.QUICK_PREFIX.symbolName());
            Symbol symbol = new Symbol(name, 1088);
            symbol.setHasSlotFor(type);
            symbol.setFirstSlot(((CodeGeneratorLexicalContext) CodeGenerator.this.lc).quickSlot(type));
            IdentNode quickIdent = IdentNode.createInternalIdentifier(symbol).setType(type);
            return quickIdent;
        }

        protected void storeNonDiscard() {
            if (!((CodeGeneratorLexicalContext) CodeGenerator.this.lc).popDiscardIfCurrent(this.assignNode)) {
                if (CodeGenerator.this.method.dup(this.depth) == null) {
                    CodeGenerator.this.method.dup();
                    Type quickType = CodeGenerator.this.method.peekType();
                    this.quick = quickLocalVariable(quickType);
                    Symbol quickSymbol = this.quick.getSymbol();
                    CodeGenerator.this.method.storeTemp(quickType, quickSymbol.getFirstSlot());
                    return;
                }
                return;
            }
            if (!$assertionsDisabled && !this.assignNode.isAssignment()) {
                throw new AssertionError();
            }
        }

        private void epilogue() {
            this.target.accept(new SimpleNodeVisitor() { // from class: jdk.nashorn.internal.codegen.CodeGenerator.Store.2
                static final /* synthetic */ boolean $assertionsDisabled;

                static {
                    $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
                }

                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                protected boolean enterDefault(Node node) {
                    throw new AssertionError((Object) ("Unexpected node " + ((Object) node) + " in store epilogue"));
                }

                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterIdentNode(IdentNode node) {
                    Symbol symbol = node.getSymbol();
                    if (!$assertionsDisabled && symbol == null) {
                        throw new AssertionError();
                    }
                    if (symbol.isScope()) {
                        int flags = CodeGenerator.this.getScopeCallSiteFlags(symbol);
                        if (CodeGenerator.this.isFastScope(symbol)) {
                            CodeGenerator.this.storeFastScopeVar(symbol, flags);
                            return false;
                        }
                        CodeGenerator.this.method.dynamicSet(node.getName(), flags, false);
                        return false;
                    }
                    Type storeType = Store.this.assignNode.getType();
                    if (!$assertionsDisabled && storeType == Type.LONG) {
                        throw new AssertionError();
                    }
                    if (symbol.hasSlotFor(storeType)) {
                        CodeGenerator.this.method.convert(storeType);
                    }
                    CodeGenerator.this.storeIdentWithCatchConversion(node, storeType);
                    return false;
                }

                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterAccessNode(AccessNode node) {
                    CodeGenerator.this.method.dynamicSet(node.getProperty(), CodeGenerator.this.getCallSiteFlags(), node.isIndex());
                    return false;
                }

                @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
                public boolean enterIndexNode(IndexNode node) {
                    CodeGenerator.this.method.dynamicSetIndex(CodeGenerator.this.getCallSiteFlags());
                    return false;
                }
            });
        }

        void store() {
            if (this.target instanceof IdentNode) {
                CodeGenerator.this.checkTemporalDeadZone((IdentNode) this.target);
            }
            prologue();
            evaluate();
            storeNonDiscard();
            epilogue();
            if (this.quick != null) {
                CodeGenerator.this.method.load(this.quick);
            }
        }
    }

    private void newFunctionObject(FunctionNode functionNode, boolean addInitializer) {
        if (!$assertionsDisabled && ((CodeGeneratorLexicalContext) this.lc).peek() != functionNode) {
            throw new AssertionError();
        }
        RecompilableScriptFunctionData data = this.compiler.getScriptFunctionData(functionNode.getId());
        if (functionNode.isProgram() && !this.compiler.isOnDemandCompilation()) {
            MethodEmitter createFunction = functionNode.getCompileUnit().getClassEmitter().method(EnumSet.of(ClassEmitter.Flag.PUBLIC, ClassEmitter.Flag.STATIC), CompilerConstants.CREATE_PROGRAM_FUNCTION.symbolName(), ScriptFunction.class, ScriptObject.class);
            createFunction.begin();
            loadConstantsAndIndex(data, createFunction);
            createFunction.load(SCOPE_TYPE, 0);
            createFunction.invoke(CREATE_FUNCTION_OBJECT);
            createFunction._return();
            createFunction.end();
        }
        if (addInitializer && !this.compiler.isOnDemandCompilation()) {
            functionNode.getCompileUnit().addFunctionInitializer(data, functionNode);
        }
        if (((CodeGeneratorLexicalContext) this.lc).getOutermostFunction() == functionNode) {
            return;
        }
        loadConstantsAndIndex(data, this.method);
        if (functionNode.needsParentScope()) {
            this.method.loadCompilerConstant(CompilerConstants.SCOPE);
            this.method.invoke(CREATE_FUNCTION_OBJECT);
        } else {
            this.method.invoke(CREATE_FUNCTION_OBJECT_NO_SCOPE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter globalInstance() {
        return this.method.invokestatic(GLOBAL_OBJECT, "instance", "()L" + GLOBAL_OBJECT + ';');
    }

    private MethodEmitter globalAllocateArguments() {
        return this.method.invokestatic(GLOBAL_OBJECT, "allocateArguments", CompilerConstants.methodDescriptor(ScriptObject.class, Object[].class, Object.class, Integer.TYPE));
    }

    private MethodEmitter globalNewRegExp() {
        return this.method.invokestatic(GLOBAL_OBJECT, "newRegExp", CompilerConstants.methodDescriptor(Object.class, String.class, String.class));
    }

    private MethodEmitter globalRegExpCopy() {
        return this.method.invokestatic(GLOBAL_OBJECT, "regExpCopy", CompilerConstants.methodDescriptor(Object.class, Object.class));
    }

    private MethodEmitter globalAllocateArray(ArrayType type) {
        return this.method.invokestatic(GLOBAL_OBJECT, "allocate", "(" + type.getDescriptor() + ")Ljdk/nashorn/internal/objects/NativeArray;");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter globalIsEval() {
        return this.method.invokestatic(GLOBAL_OBJECT, "isEval", CompilerConstants.methodDescriptor(Boolean.TYPE, Object.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter globalReplaceLocationPropertyPlaceholder() {
        return this.method.invokestatic(GLOBAL_OBJECT, "replaceLocationPropertyPlaceholder", CompilerConstants.methodDescriptor(Object.class, Object.class, Object.class));
    }

    private MethodEmitter globalCheckObjectCoercible() {
        return this.method.invokestatic(GLOBAL_OBJECT, "checkObjectCoercible", CompilerConstants.methodDescriptor(Void.TYPE, Object.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodEmitter globalDirectEval() {
        return this.method.invokestatic(GLOBAL_OBJECT, "directEval", CompilerConstants.methodDescriptor(Object.class, Object.class, Object.class, Object.class, Object.class, Boolean.TYPE));
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$OptimisticOperation.class */
    private abstract class OptimisticOperation {
        private final boolean isOptimistic;
        private final Expression expression;
        private final Optimistic optimistic;
        private final TypeBounds resultBounds;
        static final /* synthetic */ boolean $assertionsDisabled;

        abstract void loadStack();

        abstract void consumeStack();

        static {
            $assertionsDisabled = !CodeGenerator.class.desiredAssertionStatus();
        }

        /* JADX WARN: Multi-variable type inference failed */
        OptimisticOperation(Optimistic optimistic, TypeBounds resultBounds) {
            this.optimistic = optimistic;
            this.expression = (Expression) optimistic;
            this.resultBounds = resultBounds;
            this.isOptimistic = CodeGenerator.isOptimistic(optimistic) && CodeGenerator.this.useOptimisticTypes() && resultBounds.within(Type.generic(((Expression) optimistic).getType())).narrowerThan(resultBounds.widest);
        }

        MethodEmitter emit() {
            return emit(0);
        }

        MethodEmitter emit(int ignoredArgCount) {
            Label catchLabel;
            Label beginTry;
            int programPoint = this.optimistic.getProgramPoint();
            boolean optimisticOrContinuation = this.isOptimistic || CodeGenerator.this.isContinuationEntryPoint(programPoint);
            boolean currentContinuationEntryPoint = CodeGenerator.this.isCurrentContinuationEntryPoint(programPoint);
            int stackSizeOnEntry = CodeGenerator.this.method.getStackSize() - ignoredArgCount;
            storeStack(ignoredArgCount, optimisticOrContinuation);
            loadStack();
            int liveLocalsCount = storeStack(CodeGenerator.this.method.getStackSize() - stackSizeOnEntry, optimisticOrContinuation);
            if (!$assertionsDisabled) {
                if (optimisticOrContinuation != (liveLocalsCount != -1)) {
                    throw new AssertionError();
                }
            }
            Label afterConsumeStack = (this.isOptimistic || currentContinuationEntryPoint) ? new Label("after_consume_stack") : null;
            if (this.isOptimistic) {
                beginTry = new Label("try_optimistic");
                String catchLabelName = (afterConsumeStack == null ? "" : afterConsumeStack.toString()) + "_handler";
                catchLabel = new Label(catchLabelName);
                CodeGenerator.this.method.label(beginTry);
            } else {
                catchLabel = null;
                beginTry = null;
            }
            consumeStack();
            if (this.isOptimistic) {
                CodeGenerator.this.method._try(beginTry, afterConsumeStack, catchLabel, UnwarrantedOptimismException.class);
            }
            if (this.isOptimistic || currentContinuationEntryPoint) {
                CodeGenerator.this.method.label(afterConsumeStack);
                int[] localLoads = CodeGenerator.this.method.getLocalLoadsOnStack(0, stackSizeOnEntry);
                if (!$assertionsDisabled && !CodeGenerator.everyStackValueIsLocalLoad(localLoads)) {
                    throw new AssertionError((Object) (Arrays.toString(localLoads) + ", " + stackSizeOnEntry + ", " + ignoredArgCount));
                }
                List<Type> localTypesList = CodeGenerator.this.method.getLocalVariableTypes();
                int usedLocals = CodeGenerator.this.method.getUsedSlotsWithLiveTemporaries();
                List<Type> localTypes = CodeGenerator.this.method.getWidestLiveLocals(localTypesList.subList(0, usedLocals));
                if (!$assertionsDisabled && !CodeGenerator.everyLocalLoadIsValid(localLoads, usedLocals)) {
                    throw new AssertionError((Object) (Arrays.toString(localLoads) + " ~ " + ((Object) localTypes)));
                }
                if (this.isOptimistic) {
                    addUnwarrantedOptimismHandlerLabel(localTypes, catchLabel);
                }
                if (currentContinuationEntryPoint) {
                    ContinuationInfo ci = CodeGenerator.this.getContinuationInfo();
                    if (!$assertionsDisabled && ci == null) {
                        throw new AssertionError((Object) ("no continuation info found for " + ((Object) ((CodeGeneratorLexicalContext) CodeGenerator.this.lc).getCurrentFunction())));
                    }
                    if (!$assertionsDisabled && ci.hasTargetLabel()) {
                        throw new AssertionError();
                    }
                    ci.setTargetLabel(afterConsumeStack);
                    ci.getHandlerLabel().markAsOptimisticContinuationHandlerFor(afterConsumeStack);
                    ci.lvarCount = localTypes.size();
                    ci.setStackStoreSpec(localLoads);
                    ci.setStackTypes((Type[]) Arrays.copyOf(CodeGenerator.this.method.getTypesFromStack(CodeGenerator.this.method.getStackSize()), stackSizeOnEntry));
                    if (!$assertionsDisabled && ci.getStackStoreSpec().length != ci.getStackTypes().length) {
                        throw new AssertionError();
                    }
                    ci.setReturnValueType(CodeGenerator.this.method.peekType());
                    ci.lineNumber = CodeGenerator.this.getLastLineNumber();
                    ci.catchLabel = (Label) CodeGenerator.this.catchLabels.peek();
                }
            }
            return CodeGenerator.this.method;
        }

        private int storeStack(int ignoreArgCount, boolean optimisticOrContinuation) {
            if (optimisticOrContinuation) {
                int stackSize = CodeGenerator.this.method.getStackSize();
                Type[] stackTypes = CodeGenerator.this.method.getTypesFromStack(stackSize);
                int[] localLoadsOnStack = CodeGenerator.this.method.getLocalLoadsOnStack(0, stackSize);
                int usedSlots = CodeGenerator.this.method.getUsedSlotsWithLiveTemporaries();
                int firstIgnored = stackSize - ignoreArgCount;
                int firstNonLoad = 0;
                while (firstNonLoad < firstIgnored && localLoadsOnStack[firstNonLoad] != -1) {
                    firstNonLoad++;
                }
                if (firstNonLoad >= firstIgnored) {
                    return usedSlots;
                }
                int tempSlotsNeeded = 0;
                for (int i2 = firstNonLoad; i2 < stackSize; i2++) {
                    if (localLoadsOnStack[i2] == -1) {
                        tempSlotsNeeded += stackTypes[i2].getSlots();
                    }
                }
                int lastTempSlot = usedSlots + tempSlotsNeeded;
                int ignoreSlotCount = 0;
                int i3 = stackSize;
                while (true) {
                    int i4 = i3;
                    i3--;
                    if (i4 <= firstNonLoad) {
                        break;
                    }
                    if (localLoadsOnStack[i3] != -1) {
                        CodeGenerator.this.method.pop();
                    } else {
                        Type type = stackTypes[i3];
                        int slots = type.getSlots();
                        lastTempSlot -= slots;
                        if (i3 >= firstIgnored) {
                            ignoreSlotCount += slots;
                        }
                        CodeGenerator.this.method.storeTemp(type, lastTempSlot);
                    }
                }
                if (!$assertionsDisabled && lastTempSlot != usedSlots) {
                    throw new AssertionError();
                }
                List<Type> localTypesList = CodeGenerator.this.method.getLocalVariableTypes();
                for (int i5 = firstNonLoad; i5 < stackSize; i5++) {
                    int loadSlot = localLoadsOnStack[i5];
                    Type stackType = stackTypes[i5];
                    boolean isLoad = loadSlot != -1;
                    int lvarSlot = isLoad ? loadSlot : lastTempSlot;
                    Type lvarType = localTypesList.get(lvarSlot);
                    CodeGenerator.this.method.load(lvarType, lvarSlot);
                    if (isLoad) {
                        CodeGenerator.this.method.convert(stackType);
                    } else {
                        if (!$assertionsDisabled && lvarType != stackType) {
                            throw new AssertionError();
                        }
                        lastTempSlot += lvarType.getSlots();
                    }
                }
                if ($assertionsDisabled || lastTempSlot == usedSlots + tempSlotsNeeded) {
                    return lastTempSlot - ignoreSlotCount;
                }
                throw new AssertionError();
            }
            return -1;
        }

        private void addUnwarrantedOptimismHandlerLabel(List<Type> localTypes, Label label) {
            String lvarTypesDescriptor = CodeGenerator.this.getLvarTypesDescriptor(localTypes);
            Map<String, Collection<Label>> unwarrantedOptimismHandlers = ((CodeGeneratorLexicalContext) CodeGenerator.this.lc).getUnwarrantedOptimismHandlers();
            Collection<Label> labels = unwarrantedOptimismHandlers.get(lvarTypesDescriptor);
            if (labels == null) {
                labels = new LinkedList();
                unwarrantedOptimismHandlers.put(lvarTypesDescriptor, labels);
            }
            CodeGenerator.this.method.markLabelAsOptimisticCatchHandler(label, localTypes.size());
            labels.add(label);
        }

        MethodEmitter dynamicGet(String name, int flags, boolean isMethod, boolean isIndex) {
            return this.isOptimistic ? CodeGenerator.this.method.dynamicGet(getOptimisticCoercedType(), name, getOptimisticFlags(flags), isMethod, isIndex) : CodeGenerator.this.method.dynamicGet(this.resultBounds.within(this.expression.getType()), name, CodeGenerator.nonOptimisticFlags(flags), isMethod, isIndex);
        }

        MethodEmitter dynamicGetIndex(int flags, boolean isMethod) {
            return this.isOptimistic ? CodeGenerator.this.method.dynamicGetIndex(getOptimisticCoercedType(), getOptimisticFlags(flags), isMethod) : CodeGenerator.this.method.dynamicGetIndex(this.resultBounds.within(this.expression.getType()), CodeGenerator.nonOptimisticFlags(flags), isMethod);
        }

        MethodEmitter dynamicCall(int argCount, int flags, String msg) {
            return this.isOptimistic ? CodeGenerator.this.method.dynamicCall(getOptimisticCoercedType(), argCount, getOptimisticFlags(flags), msg) : CodeGenerator.this.method.dynamicCall(this.resultBounds.within(this.expression.getType()), argCount, CodeGenerator.nonOptimisticFlags(flags), msg);
        }

        int getOptimisticFlags(int flags) {
            return flags | 8 | (this.optimistic.getProgramPoint() << 11);
        }

        int getProgramPoint() {
            if (this.isOptimistic) {
                return this.optimistic.getProgramPoint();
            }
            return -1;
        }

        void convertOptimisticReturnValue() {
            if (this.isOptimistic) {
                Type optimisticType = getOptimisticCoercedType();
                if (!optimisticType.isObject()) {
                    CodeGenerator.this.method.load(this.optimistic.getProgramPoint());
                    if (optimisticType.isInteger()) {
                        CodeGenerator.this.method.invoke(CodeGenerator.ENSURE_INT);
                    } else {
                        if (optimisticType.isNumber()) {
                            CodeGenerator.this.method.invoke(CodeGenerator.ENSURE_NUMBER);
                            return;
                        }
                        throw new AssertionError(optimisticType);
                    }
                }
            }
        }

        void replaceCompileTimeProperty() {
            IdentNode identNode = (IdentNode) this.expression;
            String name = identNode.getSymbol().getName();
            if (CompilerConstants.__FILE__.name().equals(name)) {
                replaceCompileTimeProperty(CodeGenerator.this.getCurrentSource().getName());
            } else if (CompilerConstants.__DIR__.name().equals(name)) {
                replaceCompileTimeProperty(CodeGenerator.this.getCurrentSource().getBase());
            } else if (CompilerConstants.__LINE__.name().equals(name)) {
                replaceCompileTimeProperty(Integer.valueOf(CodeGenerator.this.getCurrentSource().getLine(identNode.position())));
            }
        }

        private void replaceCompileTimeProperty(Object propertyValue) {
            if (!$assertionsDisabled && !CodeGenerator.this.method.peekType().isObject()) {
                throw new AssertionError();
            }
            if ((propertyValue instanceof String) || propertyValue == null) {
                CodeGenerator.this.method.load((String) propertyValue);
            } else if (propertyValue instanceof Integer) {
                CodeGenerator.this.method.load(((Integer) propertyValue).intValue());
                CodeGenerator.this.method.convert(Type.OBJECT);
            } else {
                throw new AssertionError();
            }
            CodeGenerator.this.globalReplaceLocationPropertyPlaceholder();
            convertOptimisticReturnValue();
        }

        private Type getOptimisticCoercedType() {
            Type optimisticType = this.expression.getType();
            if (!$assertionsDisabled && !this.resultBounds.widest.widerThan(optimisticType)) {
                throw new AssertionError();
            }
            Type narrowest = this.resultBounds.narrowest;
            if (narrowest.isBoolean() || narrowest.narrowerThan(optimisticType)) {
                if ($assertionsDisabled || !optimisticType.isObject()) {
                    return optimisticType;
                }
                throw new AssertionError();
            }
            if ($assertionsDisabled || !narrowest.isObject()) {
                return narrowest;
            }
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static boolean isOptimistic(Optimistic optimistic) {
        if (!optimistic.canBeOptimistic()) {
            return false;
        }
        Expression expr = (Expression) optimistic;
        return expr.getType().narrowerThan(expr.getWidestOperationType());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean everyLocalLoadIsValid(int[] loads, int localCount) {
        for (int load : loads) {
            if (load < 0 || load >= localCount) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean everyStackValueIsLocalLoad(int[] loads) {
        for (int load : loads) {
            if (load == -1) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLvarTypesDescriptor(List<Type> localVarTypes) {
        int count = localVarTypes.size();
        StringBuilder desc = new StringBuilder(count);
        int iAppendType = 0;
        while (true) {
            int i2 = iAppendType;
            if (i2 < count) {
                iAppendType = i2 + appendType(desc, localVarTypes.get(i2));
            } else {
                return this.method.markSymbolBoundariesInLvarTypesDescriptor(desc.toString());
            }
        }
    }

    private static int appendType(StringBuilder b2, Type t2) {
        b2.append(t2.getBytecodeStackType());
        return t2.getSlots();
    }

    private static int countSymbolsInLvarTypeDescriptor(String lvarTypeDescriptor) {
        int count = 0;
        for (int i2 = 0; i2 < lvarTypeDescriptor.length(); i2++) {
            if (Character.isUpperCase(lvarTypeDescriptor.charAt(i2))) {
                count++;
            }
        }
        return count;
    }

    private boolean generateUnwarrantedOptimismExceptionHandlers(FunctionNode fn) {
        String commonLvarSpec;
        int firstArrayIndex;
        int lvarIndex;
        int firstLvarIndex;
        Label delegationLabel;
        if (!useOptimisticTypes()) {
            return false;
        }
        Map<String, Collection<Label>> unwarrantedOptimismHandlers = ((CodeGeneratorLexicalContext) this.lc).popUnwarrantedOptimismHandlers();
        if (unwarrantedOptimismHandlers.isEmpty()) {
            return false;
        }
        this.method.lineNumber(0);
        List<OptimismExceptionHandlerSpec> handlerSpecs = new ArrayList<>((unwarrantedOptimismHandlers.size() * 4) / 3);
        Iterator<String> it = unwarrantedOptimismHandlers.keySet().iterator();
        while (it.hasNext()) {
            handlerSpecs.add(new OptimismExceptionHandlerSpec(it.next(), true));
        }
        Collections.sort(handlerSpecs, Collections.reverseOrder());
        Map<String, Label> delegationLabels = new HashMap<>();
        int handlerIndex = 0;
        while (handlerIndex < handlerSpecs.size()) {
            OptimismExceptionHandlerSpec spec = handlerSpecs.get(handlerIndex);
            String lvarSpec = spec.lvarSpec;
            if (spec.catchTarget) {
                if (!$assertionsDisabled && this.method.isReachable()) {
                    throw new AssertionError();
                }
                this.method._catch(unwarrantedOptimismHandlers.get(lvarSpec));
                this.method.load(countSymbolsInLvarTypeDescriptor(lvarSpec));
                this.method.newarray(Type.OBJECT_ARRAY);
            }
            if (spec.delegationTarget) {
                this.method.label(delegationLabels.get(lvarSpec));
            }
            boolean lastHandler = handlerIndex == handlerSpecs.size() - 1;
            if (lastHandler) {
                lvarIndex = 0;
                firstLvarIndex = 0;
                firstArrayIndex = 0;
                delegationLabel = null;
                commonLvarSpec = null;
            } else {
                int nextHandlerIndex = handlerIndex + 1;
                String nextLvarSpec = handlerSpecs.get(nextHandlerIndex).lvarSpec;
                commonLvarSpec = commonPrefix(lvarSpec, nextLvarSpec);
                if (!$assertionsDisabled && !Character.isUpperCase(commonLvarSpec.charAt(commonLvarSpec.length() - 1))) {
                    throw new AssertionError();
                }
                boolean addNewHandler = true;
                int commonHandlerIndex = nextHandlerIndex;
                while (true) {
                    if (commonHandlerIndex >= handlerSpecs.size()) {
                        break;
                    }
                    OptimismExceptionHandlerSpec forwardHandlerSpec = handlerSpecs.get(commonHandlerIndex);
                    String forwardLvarSpec = forwardHandlerSpec.lvarSpec;
                    if (forwardLvarSpec.equals(commonLvarSpec)) {
                        addNewHandler = false;
                        forwardHandlerSpec.delegationTarget = true;
                        break;
                    }
                    if (!forwardLvarSpec.startsWith(commonLvarSpec)) {
                        break;
                    }
                    commonHandlerIndex++;
                }
                if (addNewHandler) {
                    handlerSpecs.add(commonHandlerIndex, new OptimismExceptionHandlerSpec(commonLvarSpec, false));
                }
                firstArrayIndex = countSymbolsInLvarTypeDescriptor(commonLvarSpec);
                lvarIndex = 0;
                for (int j2 = 0; j2 < commonLvarSpec.length(); j2++) {
                    lvarIndex += CodeGeneratorLexicalContext.getTypeForSlotDescriptor(commonLvarSpec.charAt(j2)).getSlots();
                }
                firstLvarIndex = lvarIndex;
                delegationLabel = delegationLabels.get(commonLvarSpec);
                if (delegationLabel == null) {
                    delegationLabel = new Label("uo_pa_" + commonLvarSpec);
                    delegationLabels.put(commonLvarSpec, delegationLabel);
                }
            }
            int args = 0;
            boolean symbolHadValue = false;
            for (int typeIndex = commonLvarSpec == null ? 0 : commonLvarSpec.length(); typeIndex < lvarSpec.length(); typeIndex++) {
                char typeDesc = lvarSpec.charAt(typeIndex);
                Type lvarType = CodeGeneratorLexicalContext.getTypeForSlotDescriptor(typeDesc);
                if (!lvarType.isUnknown()) {
                    this.method.load(lvarType, lvarIndex);
                    symbolHadValue = true;
                    args++;
                } else if (typeDesc == 'U' && !symbolHadValue) {
                    if (this.method.peekType() == Type.UNDEFINED) {
                        this.method.dup();
                    } else {
                        this.method.loadUndefined(Type.OBJECT);
                    }
                    args++;
                }
                if (Character.isUpperCase(typeDesc)) {
                    symbolHadValue = false;
                }
                lvarIndex += lvarType.getSlots();
            }
            if (!$assertionsDisabled && args <= 0) {
                throw new AssertionError();
            }
            this.method.dynamicArrayPopulatorCall(args + 1, firstArrayIndex);
            if (delegationLabel != null) {
                if (!$assertionsDisabled && lastHandler) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && commonLvarSpec == null) {
                    throw new AssertionError();
                }
                this.method.undefineLocalVariables(firstLvarIndex, true);
                OptimismExceptionHandlerSpec nextSpec = handlerSpecs.get(handlerIndex + 1);
                if (!nextSpec.lvarSpec.equals(commonLvarSpec) || nextSpec.catchTarget) {
                    this.method._goto(delegationLabel);
                }
            } else {
                if (!$assertionsDisabled && !lastHandler) {
                    throw new AssertionError();
                }
                loadConstant(getByteCodeSymbolNames(fn));
                if (isRestOf()) {
                    loadConstant(getContinuationEntryPoints());
                    this.method.invoke(CREATE_REWRITE_EXCEPTION_REST_OF);
                } else {
                    this.method.invoke(CREATE_REWRITE_EXCEPTION);
                }
                this.method.athrow();
            }
            handlerIndex++;
        }
        return true;
    }

    private static String[] getByteCodeSymbolNames(FunctionNode fn) {
        List<String> names = new ArrayList<>();
        for (Symbol symbol : fn.getBody().getSymbols()) {
            if (symbol.hasSlot()) {
                if (symbol.isScope()) {
                    if (!$assertionsDisabled && !symbol.isParam()) {
                        throw new AssertionError();
                    }
                    names.add(null);
                } else {
                    names.add(symbol.getName());
                }
            }
        }
        return (String[]) names.toArray(new String[names.size()]);
    }

    private static String commonPrefix(String s1, String s2) {
        int l1 = s1.length();
        int l2 = Math.min(l1, s2.length());
        int lms = -1;
        for (int i2 = 0; i2 < l2; i2++) {
            char c1 = s1.charAt(i2);
            if (c1 != s2.charAt(i2)) {
                return s1.substring(0, lms + 1);
            }
            if (Character.isUpperCase(c1)) {
                lms = i2;
            }
        }
        return l2 == l1 ? s1 : s2;
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$OptimismExceptionHandlerSpec.class */
    private static class OptimismExceptionHandlerSpec implements Comparable<OptimismExceptionHandlerSpec> {
        private final String lvarSpec;
        private final boolean catchTarget;
        private boolean delegationTarget;

        OptimismExceptionHandlerSpec(String lvarSpec, boolean catchTarget) {
            this.lvarSpec = lvarSpec;
            this.catchTarget = catchTarget;
            if (!catchTarget) {
                this.delegationTarget = true;
            }
        }

        @Override // java.lang.Comparable
        public int compareTo(OptimismExceptionHandlerSpec o2) {
            return this.lvarSpec.compareTo(o2.lvarSpec);
        }

        public String toString() {
            StringBuilder b2 = new StringBuilder(64).append("[HandlerSpec ").append(this.lvarSpec);
            if (this.catchTarget) {
                b2.append(", catchTarget");
            }
            if (this.delegationTarget) {
                b2.append(", delegationTarget");
            }
            return b2.append("]").toString();
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CodeGenerator$ContinuationInfo.class */
    private static class ContinuationInfo {
        private final Label handlerLabel = new Label("continuation_handler");
        private Label targetLabel;
        int lvarCount;
        private int[] stackStoreSpec;
        private Type[] stackTypes;
        private Type returnValueType;
        private Map<Integer, PropertyMap> objectLiteralMaps;
        private int lineNumber;
        private Label catchLabel;
        private int exceptionScopePops;

        static /* synthetic */ int access$4804(ContinuationInfo x0) {
            int i2 = x0.exceptionScopePops + 1;
            x0.exceptionScopePops = i2;
            return i2;
        }

        ContinuationInfo() {
        }

        Label getHandlerLabel() {
            return this.handlerLabel;
        }

        boolean hasTargetLabel() {
            return this.targetLabel != null;
        }

        Label getTargetLabel() {
            return this.targetLabel;
        }

        void setTargetLabel(Label targetLabel) {
            this.targetLabel = targetLabel;
        }

        int[] getStackStoreSpec() {
            return (int[]) this.stackStoreSpec.clone();
        }

        void setStackStoreSpec(int[] stackStoreSpec) {
            this.stackStoreSpec = stackStoreSpec;
        }

        Type[] getStackTypes() {
            return (Type[]) this.stackTypes.clone();
        }

        void setStackTypes(Type[] stackTypes) {
            this.stackTypes = stackTypes;
        }

        Type getReturnValueType() {
            return this.returnValueType;
        }

        void setReturnValueType(Type returnValueType) {
            this.returnValueType = returnValueType;
        }

        void setObjectLiteralMap(int objectLiteralStackDepth, PropertyMap objectLiteralMap) {
            if (this.objectLiteralMaps == null) {
                this.objectLiteralMaps = new HashMap();
            }
            this.objectLiteralMaps.put(Integer.valueOf(objectLiteralStackDepth), objectLiteralMap);
        }

        PropertyMap getObjectLiteralMap(int stackDepth) {
            if (this.objectLiteralMaps == null) {
                return null;
            }
            return this.objectLiteralMaps.get(Integer.valueOf(stackDepth));
        }

        public String toString() {
            return "[localVariableTypes=" + ((Object) this.targetLabel.getStack().getLocalVariableTypesCopy()) + ", stackStoreSpec=" + Arrays.toString(this.stackStoreSpec) + ", returnValueType=" + ((Object) this.returnValueType) + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ContinuationInfo getContinuationInfo() {
        return this.continuationInfo;
    }

    private void generateContinuationHandler() {
        if (!isRestOf()) {
            return;
        }
        ContinuationInfo ci = getContinuationInfo();
        this.method.label(ci.getHandlerLabel());
        this.method.lineNumber(0);
        Label.Stack stack = ci.getTargetLabel().getStack();
        List<Type> lvarTypes = stack.getLocalVariableTypesCopy();
        BitSet symbolBoundary = stack.getSymbolBoundaryCopy();
        int lvarCount = ci.lvarCount;
        Type rewriteExceptionType = Type.typeFor((Class<?>) RewriteException.class);
        this.method.load(rewriteExceptionType, 0);
        this.method.storeTemp(rewriteExceptionType, lvarCount);
        this.method.load(rewriteExceptionType, 0);
        this.method.invoke(RewriteException.GET_BYTECODE_SLOTS);
        int arrayIndex = 0;
        int i2 = 0;
        while (true) {
            int lvarIndex = i2;
            if (lvarIndex >= lvarCount) {
                break;
            }
            Type lvarType = lvarTypes.get(lvarIndex);
            if (!lvarType.isUnknown()) {
                this.method.dup();
                this.method.load(arrayIndex).arrayload();
                Class<?> typeClass = lvarType.getTypeClass();
                if (typeClass == long[].class) {
                    this.method.load(rewriteExceptionType, lvarCount);
                    this.method.invoke(RewriteException.TO_LONG_ARRAY);
                } else if (typeClass == double[].class) {
                    this.method.load(rewriteExceptionType, lvarCount);
                    this.method.invoke(RewriteException.TO_DOUBLE_ARRAY);
                } else if (typeClass == Object[].class) {
                    this.method.load(rewriteExceptionType, lvarCount);
                    this.method.invoke(RewriteException.TO_OBJECT_ARRAY);
                } else {
                    if (!typeClass.isPrimitive() && typeClass != Object.class) {
                        this.method.loadType(Type.getInternalName(typeClass));
                        this.method.invoke(RewriteException.INSTANCE_OR_NULL);
                    }
                    this.method.convert(lvarType);
                }
                this.method.storeHidden(lvarType, lvarIndex, false);
            }
            int nextLvarIndex = lvarIndex + lvarType.getSlots();
            if (symbolBoundary.get(nextLvarIndex - 1)) {
                arrayIndex++;
            }
            i2 = nextLvarIndex;
        }
        if (AssertsEnabled.assertsEnabled()) {
            this.method.load(arrayIndex);
            this.method.invoke(RewriteException.ASSERT_ARRAY_LENGTH);
        } else {
            this.method.pop();
        }
        int[] stackStoreSpec = ci.getStackStoreSpec();
        Type[] stackTypes = ci.getStackTypes();
        boolean isStackEmpty = stackStoreSpec.length == 0;
        int replacedObjectLiteralMaps = 0;
        if (!isStackEmpty) {
            for (int i3 = 0; i3 < stackStoreSpec.length; i3++) {
                int slot = stackStoreSpec[i3];
                this.method.load(lvarTypes.get(slot), slot);
                this.method.convert(stackTypes[i3]);
                PropertyMap map = ci.getObjectLiteralMap(i3);
                if (map != null) {
                    this.method.dup();
                    if (!$assertionsDisabled && !ScriptObject.class.isAssignableFrom(this.method.peekType().getTypeClass())) {
                        throw new AssertionError((Object) (((Object) this.method.peekType().getTypeClass()) + " is not a script object"));
                    }
                    loadConstant(map);
                    this.method.invoke(ScriptObject.SET_MAP);
                    replacedObjectLiteralMaps++;
                }
            }
        }
        if (!$assertionsDisabled && ci.objectLiteralMaps != null && ci.objectLiteralMaps.size() != replacedObjectLiteralMaps) {
            throw new AssertionError();
        }
        this.method.load(rewriteExceptionType, lvarCount);
        this.method.loadNull();
        this.method.storeHidden(Type.OBJECT, lvarCount);
        this.method.markDeadSlots(lvarCount, Type.OBJECT.getSlots());
        this.method.invoke(RewriteException.GET_RETURN_VALUE);
        Type returnValueType = ci.getReturnValueType();
        boolean needsCatch = false;
        Label targetCatchLabel = ci.catchLabel;
        Label _try = null;
        if (returnValueType.isPrimitive()) {
            this.method.lineNumber(ci.lineNumber);
            if (targetCatchLabel != METHOD_BOUNDARY) {
                _try = new Label("");
                this.method.label(_try);
                needsCatch = true;
            }
        }
        this.method.convert(returnValueType);
        int scopePopCount = needsCatch ? ci.exceptionScopePops : 0;
        Label catchLabel = scopePopCount > 0 ? new Label("") : targetCatchLabel;
        if (needsCatch) {
            Label _end_try = new Label("");
            this.method.label(_end_try);
            this.method._try(_try, _end_try, catchLabel);
        }
        this.method._goto(ci.getTargetLabel());
        if (catchLabel != targetCatchLabel) {
            this.method.lineNumber(0);
            if (!$assertionsDisabled && scopePopCount <= 0) {
                throw new AssertionError();
            }
            this.method._catch(catchLabel);
            popScopes(scopePopCount);
            this.method.uncheckedGoto(targetCatchLabel);
        }
    }
}
