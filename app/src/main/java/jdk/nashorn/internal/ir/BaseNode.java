package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.parser.TokenType;

@Immutable
/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/BaseNode.class */
public abstract class BaseNode extends Expression implements FunctionCall, Optimistic {
    private static final long serialVersionUID = 1;
    protected final Expression base;
    private final boolean isFunction;
    protected final Type type;
    protected final int programPoint;

    public abstract BaseNode setIsFunction();

    public BaseNode(long token, int finish, Expression base, boolean isFunction) {
        super(token, base.getStart(), finish);
        this.base = base;
        this.isFunction = isFunction;
        this.type = null;
        this.programPoint = -1;
    }

    protected BaseNode(BaseNode baseNode, Expression base, boolean isFunction, Type callSiteType, int programPoint) {
        super(baseNode);
        this.base = base;
        this.isFunction = isFunction;
        this.type = callSiteType;
        this.programPoint = programPoint;
    }

    public Expression getBase() {
        return this.base;
    }

    @Override // jdk.nashorn.internal.ir.FunctionCall
    public boolean isFunction() {
        return this.isFunction;
    }

    @Override // jdk.nashorn.internal.ir.Expression
    public Type getType() {
        return this.type == null ? getMostPessimisticType() : this.type;
    }

    @Override // jdk.nashorn.internal.ir.Optimistic
    public int getProgramPoint() {
        return this.programPoint;
    }

    @Override // jdk.nashorn.internal.ir.Optimistic
    public Type getMostOptimisticType() {
        return Type.INT;
    }

    @Override // jdk.nashorn.internal.ir.Optimistic
    public Type getMostPessimisticType() {
        return Type.OBJECT;
    }

    @Override // jdk.nashorn.internal.ir.Optimistic
    public boolean canBeOptimistic() {
        return true;
    }

    public boolean isIndex() {
        return isTokenType(TokenType.LBRACKET);
    }
}
