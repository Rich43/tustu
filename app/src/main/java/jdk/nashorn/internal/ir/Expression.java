package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/Expression.class */
public abstract class Expression extends Node {
    private static final long serialVersionUID = 1;
    static final String OPT_IDENTIFIER = "%";

    public abstract Type getType();

    protected Expression(long token, int start, int finish) {
        super(token, start, finish);
    }

    Expression(long token, int finish) {
        super(token, finish);
    }

    Expression(Expression expr) {
        super(expr);
    }

    public boolean isLocal() {
        return false;
    }

    public boolean isSelfModifying() {
        return false;
    }

    public Type getWidestOperationType() {
        return Type.OBJECT;
    }

    public final boolean isOptimistic() {
        return getType().narrowerThan(getWidestOperationType());
    }

    void optimisticTypeToString(StringBuilder sb) {
        optimisticTypeToString(sb, isOptimistic());
    }

    /* JADX WARN: Multi-variable type inference failed */
    void optimisticTypeToString(StringBuilder sb, boolean optimistic) {
        sb.append('{');
        Type type = getType();
        String desc = type == Type.UNDEFINED ? "U" : type.getDescriptor();
        sb.append(desc.charAt(desc.length() - 1) == ';' ? "O" : desc);
        if (isOptimistic() && optimistic) {
            sb.append("%");
            int pp = ((Optimistic) this).getProgramPoint();
            if (UnwarrantedOptimismException.isValid(pp)) {
                sb.append('_').append(pp);
            }
        }
        sb.append('}');
    }

    public boolean isAlwaysFalse() {
        return false;
    }

    public boolean isAlwaysTrue() {
        return false;
    }

    public static boolean isAlwaysFalse(Expression test) {
        return test != null && test.isAlwaysFalse();
    }

    public static boolean isAlwaysTrue(Expression test) {
        return test == null || test.isAlwaysTrue();
    }
}
