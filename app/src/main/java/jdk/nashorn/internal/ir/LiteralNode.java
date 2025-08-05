package jdk.nashorn.internal.ir;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXMLLoader;
import jdk.nashorn.internal.codegen.types.ArrayType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.Splittable;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Undefined;

@Immutable
/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode.class */
public abstract class LiteralNode<T> extends Expression implements PropertyKey {
    private static final long serialVersionUID = 1;
    protected final T value;
    public static final Object POSTSET_MARKER;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LiteralNode.class.desiredAssertionStatus();
        POSTSET_MARKER = new Object();
    }

    protected LiteralNode(long token, int finish, T value) {
        super(token, finish);
        this.value = value;
    }

    protected LiteralNode(LiteralNode<T> literalNode) {
        this(literalNode, literalNode.value);
    }

    protected LiteralNode(LiteralNode<T> literalNode, T newValue) {
        super(literalNode);
        this.value = newValue;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public LiteralNode<?> initialize(LexicalContext lc) {
        return this;
    }

    public boolean isNull() {
        return this.value == null;
    }

    @Override // jdk.nashorn.internal.ir.Expression
    public Type getType() {
        return Type.typeFor(this.value.getClass());
    }

    @Override // jdk.nashorn.internal.ir.PropertyKey
    public String getPropertyName() {
        return JSType.toString(getObject());
    }

    public boolean getBoolean() {
        return JSType.toBoolean(this.value);
    }

    public int getInt32() {
        return JSType.toInt32(this.value);
    }

    public long getUint32() {
        return JSType.toUint32(this.value);
    }

    public long getLong() {
        return JSType.toLong(this.value);
    }

    public double getNumber() {
        return JSType.toNumber(this.value);
    }

    public String getString() {
        return JSType.toString(this.value);
    }

    public Object getObject() {
        return this.value;
    }

    public boolean isString() {
        return this.value instanceof String;
    }

    public boolean isNumeric() {
        return this.value instanceof Number;
    }

    @Override // jdk.nashorn.internal.ir.Node
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterLiteralNode(this)) {
            return visitor.leaveLiteralNode(this);
        }
        return this;
    }

    @Override // jdk.nashorn.internal.ir.Node
    public void toString(StringBuilder sb, boolean printType) {
        if (this.value == null) {
            sb.append(FXMLLoader.NULL_KEYWORD);
        } else {
            sb.append(this.value.toString());
        }
    }

    public final T getValue() {
        return this.value;
    }

    private static Expression[] valueToArray(List<Expression> value) {
        return (Expression[]) value.toArray(new Expression[value.size()]);
    }

    public static LiteralNode<Object> newInstance(long token, int finish) {
        return new NullLiteralNode(token, finish);
    }

    public static LiteralNode<Object> newInstance(Node parent) {
        return new NullLiteralNode(parent.getToken(), parent.getFinish());
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode$PrimitiveLiteralNode.class */
    public static class PrimitiveLiteralNode<T> extends LiteralNode<T> {
        private static final long serialVersionUID = 1;

        private PrimitiveLiteralNode(long token, int finish, T value) {
            super(token, finish, value);
        }

        private PrimitiveLiteralNode(PrimitiveLiteralNode<T> literalNode) {
            super(literalNode);
        }

        public boolean isTrue() {
            return JSType.toBoolean(this.value);
        }

        @Override // jdk.nashorn.internal.ir.Expression
        public boolean isLocal() {
            return true;
        }

        @Override // jdk.nashorn.internal.ir.Expression
        public boolean isAlwaysFalse() {
            return !isTrue();
        }

        @Override // jdk.nashorn.internal.ir.Expression
        public boolean isAlwaysTrue() {
            return isTrue();
        }
    }

    @Immutable
    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode$BooleanLiteralNode.class */
    private static final class BooleanLiteralNode extends PrimitiveLiteralNode<Boolean> {
        private static final long serialVersionUID = 1;

        private BooleanLiteralNode(long token, int finish, boolean value) {
            super(Token.recast(token, value ? TokenType.TRUE : TokenType.FALSE), finish, Boolean.valueOf(value));
        }

        private BooleanLiteralNode(BooleanLiteralNode literalNode) {
            super(literalNode);
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode.PrimitiveLiteralNode
        public boolean isTrue() {
            return ((Boolean) this.value).booleanValue();
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Expression
        public Type getType() {
            return Type.BOOLEAN;
        }

        @Override // jdk.nashorn.internal.ir.Expression
        public Type getWidestOperationType() {
            return Type.BOOLEAN;
        }
    }

    public static LiteralNode<Boolean> newInstance(long token, int finish, boolean value) {
        return new BooleanLiteralNode(token, finish, value);
    }

    public static LiteralNode<?> newInstance(Node parent, boolean value) {
        return new BooleanLiteralNode(parent.getToken(), parent.getFinish(), value);
    }

    @Immutable
    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode$NumberLiteralNode.class */
    private static final class NumberLiteralNode extends PrimitiveLiteralNode<Number> {
        private static final long serialVersionUID = 1;
        private final Type type;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LiteralNode.class.desiredAssertionStatus();
        }

        private NumberLiteralNode(long j2, int i2, Number number) {
            super(Token.recast(j2, TokenType.DECIMAL), i2, number);
            this.type = numberGetType((Number) this.value);
        }

        private NumberLiteralNode(NumberLiteralNode numberLiteralNode) {
            super(numberLiteralNode);
            this.type = numberGetType((Number) this.value);
        }

        private static Type numberGetType(Number number) {
            if (number instanceof Integer) {
                return Type.INT;
            }
            if (number instanceof Double) {
                return Type.NUMBER;
            }
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Expression
        public Type getType() {
            return this.type;
        }

        @Override // jdk.nashorn.internal.ir.Expression
        public Type getWidestOperationType() {
            return getType();
        }
    }

    public static LiteralNode<Number> newInstance(long token, int finish, Number value) {
        if ($assertionsDisabled || !(value instanceof Long)) {
            return new NumberLiteralNode(token, finish, value);
        }
        throw new AssertionError();
    }

    public static LiteralNode<?> newInstance(Node parent, Number value) {
        return new NumberLiteralNode(parent.getToken(), parent.getFinish(), value);
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode$UndefinedLiteralNode.class */
    private static class UndefinedLiteralNode extends PrimitiveLiteralNode<Undefined> {
        private static final long serialVersionUID = 1;

        private UndefinedLiteralNode(long token, int finish) {
            super(Token.recast(token, TokenType.OBJECT), finish, ScriptRuntime.UNDEFINED);
        }

        private UndefinedLiteralNode(UndefinedLiteralNode literalNode) {
            super(literalNode);
        }
    }

    public static LiteralNode<Undefined> newInstance(long token, int finish, Undefined value) {
        return new UndefinedLiteralNode(token, finish);
    }

    public static LiteralNode<?> newInstance(Node parent, Undefined value) {
        return new UndefinedLiteralNode(parent.getToken(), parent.getFinish());
    }

    @Immutable
    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode$StringLiteralNode.class */
    private static class StringLiteralNode extends PrimitiveLiteralNode<String> {
        private static final long serialVersionUID = 1;

        private StringLiteralNode(long token, int finish, String value) {
            super(Token.recast(token, TokenType.STRING), finish, value);
        }

        private StringLiteralNode(StringLiteralNode literalNode) {
            super(literalNode);
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Node
        public void toString(StringBuilder sb, boolean z2) {
            sb.append('\"');
            sb.append((String) this.value);
            sb.append('\"');
        }
    }

    public static LiteralNode<String> newInstance(long token, int finish, String value) {
        return new StringLiteralNode(token, finish, value);
    }

    public static LiteralNode<?> newInstance(Node parent, String value) {
        return new StringLiteralNode(parent.getToken(), parent.getFinish(), value);
    }

    @Immutable
    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode$LexerTokenLiteralNode.class */
    private static class LexerTokenLiteralNode extends LiteralNode<Lexer.LexerToken> {
        private static final long serialVersionUID = 1;

        private LexerTokenLiteralNode(long token, int finish, Lexer.LexerToken value) {
            super(Token.recast(token, TokenType.STRING), finish, value);
        }

        private LexerTokenLiteralNode(LexerTokenLiteralNode literalNode) {
            super(literalNode);
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Expression
        public Type getType() {
            return Type.OBJECT;
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Node
        public void toString(StringBuilder sb, boolean z2) {
            sb.append(((Lexer.LexerToken) this.value).toString());
        }
    }

    public static LiteralNode<Lexer.LexerToken> newInstance(long token, int finish, Lexer.LexerToken value) {
        return new LexerTokenLiteralNode(token, finish, value);
    }

    public static LiteralNode<?> newInstance(Node parent, Lexer.LexerToken value) {
        return new LexerTokenLiteralNode(parent.getToken(), parent.getFinish(), value);
    }

    public static Object objectAsConstant(Object object) {
        if (object == null) {
            return null;
        }
        if ((object instanceof Number) || (object instanceof String) || (object instanceof Boolean)) {
            return object;
        }
        if (object instanceof LiteralNode) {
            return objectAsConstant(((LiteralNode) object).getValue());
        }
        return POSTSET_MARKER;
    }

    public static boolean isConstant(Object object) {
        return objectAsConstant(object) != POSTSET_MARKER;
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode$NullLiteralNode.class */
    private static final class NullLiteralNode extends PrimitiveLiteralNode<Object> {
        private static final long serialVersionUID = 1;

        private NullLiteralNode(long token, int finish) {
            super(Token.recast(token, TokenType.OBJECT), finish, null);
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Node
        public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
            if (visitor.enterLiteralNode(this)) {
                return visitor.leaveLiteralNode(this);
            }
            return this;
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Expression
        public Type getType() {
            return Type.OBJECT;
        }

        @Override // jdk.nashorn.internal.ir.Expression
        public Type getWidestOperationType() {
            return Type.OBJECT;
        }
    }

    @Immutable
    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode$ArrayLiteralNode.class */
    public static final class ArrayLiteralNode extends LiteralNode<Expression[]> implements LexicalContextNode, Splittable {
        private static final long serialVersionUID = 1;
        private final Type elementType;
        private final Object presets;
        private final int[] postsets;
        private final List<Splittable.SplitRange> splitRanges;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LiteralNode.class.desiredAssertionStatus();
        }

        /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LiteralNode$ArrayLiteralNode$ArrayLiteralInitializer.class */
        private static final class ArrayLiteralInitializer {
            static final /* synthetic */ boolean $assertionsDisabled;

            private ArrayLiteralInitializer() {
            }

            static {
                $assertionsDisabled = !LiteralNode.class.desiredAssertionStatus();
            }

            static ArrayLiteralNode initialize(ArrayLiteralNode arrayLiteralNode) {
                Type typeComputeElementType = computeElementType((Expression[]) arrayLiteralNode.value);
                int[] iArrComputePostsets = computePostsets((Expression[]) arrayLiteralNode.value);
                return new ArrayLiteralNode(arrayLiteralNode, (Expression[]) arrayLiteralNode.value, typeComputeElementType, iArrComputePostsets, computePresets((Expression[]) arrayLiteralNode.value, typeComputeElementType, iArrComputePostsets), arrayLiteralNode.splitRanges);
            }

            private static Type computeElementType(Expression[] value) {
                Type widestElementType = Type.INT;
                int length = value.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    Expression elem = value[i2];
                    if (elem == null) {
                        widestElementType = widestElementType.widest(Type.OBJECT);
                        break;
                    }
                    Type type = elem.getType().isUnknown() ? Type.OBJECT : elem.getType();
                    if (type.isBoolean()) {
                        widestElementType = widestElementType.widest(Type.OBJECT);
                        break;
                    }
                    widestElementType = widestElementType.widest(type);
                    if (widestElementType.isObject()) {
                        break;
                    }
                    i2++;
                }
                return widestElementType;
            }

            private static int[] computePostsets(Expression[] value) {
                int[] computed = new int[value.length];
                int nComputed = 0;
                for (int i2 = 0; i2 < value.length; i2++) {
                    Expression element = value[i2];
                    if (element == null || !LiteralNode.isConstant(element)) {
                        int i3 = nComputed;
                        nComputed++;
                        computed[i3] = i2;
                    }
                }
                return Arrays.copyOf(computed, nComputed);
            }

            private static boolean setArrayElement(int[] array, int i2, Object n2) {
                if (n2 instanceof Number) {
                    array[i2] = ((Number) n2).intValue();
                    return true;
                }
                return false;
            }

            private static boolean setArrayElement(long[] array, int i2, Object n2) {
                if (n2 instanceof Number) {
                    array[i2] = ((Number) n2).longValue();
                    return true;
                }
                return false;
            }

            private static boolean setArrayElement(double[] array, int i2, Object n2) {
                if (n2 instanceof Number) {
                    array[i2] = ((Number) n2).doubleValue();
                    return true;
                }
                return false;
            }

            private static int[] presetIntArray(Expression[] value, int[] postsets) {
                int[] array = new int[value.length];
                int nComputed = 0;
                for (int i2 = 0; i2 < value.length; i2++) {
                    if (!setArrayElement(array, i2, LiteralNode.objectAsConstant(value[i2])) && !$assertionsDisabled) {
                        int i3 = nComputed;
                        nComputed++;
                        if (postsets[i3] != i2) {
                            throw new AssertionError();
                        }
                    }
                }
                if ($assertionsDisabled || postsets.length == nComputed) {
                    return array;
                }
                throw new AssertionError();
            }

            private static long[] presetLongArray(Expression[] value, int[] postsets) {
                long[] array = new long[value.length];
                int nComputed = 0;
                for (int i2 = 0; i2 < value.length; i2++) {
                    if (!setArrayElement(array, i2, LiteralNode.objectAsConstant(value[i2])) && !$assertionsDisabled) {
                        int i3 = nComputed;
                        nComputed++;
                        if (postsets[i3] != i2) {
                            throw new AssertionError();
                        }
                    }
                }
                if ($assertionsDisabled || postsets.length == nComputed) {
                    return array;
                }
                throw new AssertionError();
            }

            private static double[] presetDoubleArray(Expression[] value, int[] postsets) {
                double[] array = new double[value.length];
                int nComputed = 0;
                for (int i2 = 0; i2 < value.length; i2++) {
                    if (!setArrayElement(array, i2, LiteralNode.objectAsConstant(value[i2])) && !$assertionsDisabled) {
                        int i3 = nComputed;
                        nComputed++;
                        if (postsets[i3] != i2) {
                            throw new AssertionError();
                        }
                    }
                }
                if ($assertionsDisabled || postsets.length == nComputed) {
                    return array;
                }
                throw new AssertionError();
            }

            private static Object[] presetObjectArray(Expression[] value, int[] postsets) {
                Object[] array = new Object[value.length];
                int nComputed = 0;
                for (int i2 = 0; i2 < value.length; i2++) {
                    Expression expression = value[i2];
                    if (expression == null) {
                        if ($assertionsDisabled) {
                            continue;
                        } else {
                            int i3 = nComputed;
                            nComputed++;
                            if (postsets[i3] != i2) {
                                throw new AssertionError();
                            }
                        }
                    } else {
                        Object element = LiteralNode.objectAsConstant(expression);
                        if (element != LiteralNode.POSTSET_MARKER) {
                            array[i2] = element;
                        } else if ($assertionsDisabled) {
                            continue;
                        } else {
                            int i4 = nComputed;
                            nComputed++;
                            if (postsets[i4] != i2) {
                                throw new AssertionError();
                            }
                        }
                    }
                }
                if ($assertionsDisabled || postsets.length == nComputed) {
                    return array;
                }
                throw new AssertionError();
            }

            static Object computePresets(Expression[] value, Type elementType, int[] postsets) {
                if (!$assertionsDisabled && elementType.isUnknown()) {
                    throw new AssertionError();
                }
                if (elementType.isInteger()) {
                    return presetIntArray(value, postsets);
                }
                if (elementType.isNumeric()) {
                    return presetDoubleArray(value, postsets);
                }
                return presetObjectArray(value, postsets);
            }
        }

        protected ArrayLiteralNode(long token, int finish, Expression[] value) {
            super(Token.recast(token, TokenType.ARRAY), finish, value);
            this.elementType = Type.UNKNOWN;
            this.presets = null;
            this.postsets = null;
            this.splitRanges = null;
        }

        private ArrayLiteralNode(ArrayLiteralNode node, Expression[] value, Type elementType, int[] postsets, Object presets, List<Splittable.SplitRange> splitRanges) {
            super(node, value);
            this.elementType = elementType;
            this.postsets = postsets;
            this.presets = presets;
            this.splitRanges = splitRanges;
        }

        public List<Expression> getElementExpressions() {
            return Collections.unmodifiableList(Arrays.asList((Object[]) this.value));
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode
        public ArrayLiteralNode initialize(LexicalContext lc) {
            return (ArrayLiteralNode) Node.replaceInLexicalContext(lc, this, ArrayLiteralInitializer.initialize(this));
        }

        public ArrayType getArrayType() {
            return getArrayType(getElementType());
        }

        private static ArrayType getArrayType(Type elementType) {
            if (elementType.isInteger()) {
                return Type.INT_ARRAY;
            }
            if (elementType.isNumeric()) {
                return Type.NUMBER_ARRAY;
            }
            return Type.OBJECT_ARRAY;
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Expression
        public Type getType() {
            return Type.typeFor((Class<?>) NativeArray.class);
        }

        public Type getElementType() {
            if ($assertionsDisabled || !this.elementType.isUnknown()) {
                return this.elementType;
            }
            throw new AssertionError((Object) (((Object) this) + " has elementType=unknown"));
        }

        public int[] getPostsets() {
            if ($assertionsDisabled || this.postsets != null) {
                return this.postsets;
            }
            throw new AssertionError((Object) (((Object) this) + " elementType=" + ((Object) this.elementType) + " has no postsets"));
        }

        private boolean presetsMatchElementType() {
            if (this.elementType == Type.INT) {
                return this.presets instanceof int[];
            }
            if (this.elementType == Type.NUMBER) {
                return this.presets instanceof double[];
            }
            return this.presets instanceof Object[];
        }

        public Object getPresets() {
            if ($assertionsDisabled || (this.presets != null && presetsMatchElementType())) {
                return this.presets;
            }
            throw new AssertionError((Object) (((Object) this) + " doesn't have presets, or invalid preset type: " + this.presets));
        }

        @Override // jdk.nashorn.internal.ir.Splittable
        public List<Splittable.SplitRange> getSplitRanges() {
            if (this.splitRanges == null) {
                return null;
            }
            return Collections.unmodifiableList(this.splitRanges);
        }

        public ArrayLiteralNode setSplitRanges(LexicalContext lexicalContext, List<Splittable.SplitRange> list) {
            if (this.splitRanges == list) {
                return this;
            }
            return (ArrayLiteralNode) Node.replaceInLexicalContext(lexicalContext, this, new ArrayLiteralNode(this, (Expression[]) this.value, this.elementType, this.postsets, this.presets, list));
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Node
        public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
            return LexicalContextNode.Acceptor.accept(this, visitor);
        }

        @Override // jdk.nashorn.internal.ir.LexicalContextNode
        public Node accept(LexicalContext lexicalContext, NodeVisitor<? extends LexicalContext> nodeVisitor) {
            if (nodeVisitor.enterLiteralNode(this)) {
                List<Expression> listAsList = Arrays.asList((Object[]) this.value);
                List<Expression> listAccept = Node.accept(nodeVisitor, listAsList);
                return nodeVisitor.leaveLiteralNode(listAsList != listAccept ? setValue(lexicalContext, listAccept) : this);
            }
            return this;
        }

        private ArrayLiteralNode setValue(LexicalContext lexicalContext, Expression[] expressionArr) {
            if (this.value == expressionArr) {
                return this;
            }
            return (ArrayLiteralNode) Node.replaceInLexicalContext(lexicalContext, this, new ArrayLiteralNode(this, expressionArr, this.elementType, this.postsets, this.presets, this.splitRanges));
        }

        private ArrayLiteralNode setValue(LexicalContext lc, List<Expression> value) {
            return setValue(lc, (Expression[]) value.toArray(new Expression[value.size()]));
        }

        @Override // jdk.nashorn.internal.ir.LiteralNode, jdk.nashorn.internal.ir.Node
        public void toString(StringBuilder sb, boolean z2) {
            sb.append('[');
            boolean z3 = true;
            for (Expression expression : (Expression[]) this.value) {
                if (!z3) {
                    sb.append(',');
                    sb.append(' ');
                }
                if (expression == null) {
                    sb.append("undefined");
                } else {
                    expression.toString(sb, z2);
                }
                z3 = false;
            }
            sb.append(']');
        }
    }

    public static LiteralNode<Expression[]> newInstance(long token, int finish, List<Expression> value) {
        return new ArrayLiteralNode(token, finish, valueToArray(value));
    }

    public static LiteralNode<?> newInstance(Node parent, List<Expression> value) {
        return new ArrayLiteralNode(parent.getToken(), parent.getFinish(), valueToArray(value));
    }

    public static LiteralNode<Expression[]> newInstance(long token, int finish, Expression[] value) {
        return new ArrayLiteralNode(token, finish, value);
    }
}
