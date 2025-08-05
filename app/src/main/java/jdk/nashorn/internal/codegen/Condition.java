package jdk.nashorn.internal.codegen;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/Condition.class */
enum Condition {
    EQ,
    NE,
    LE,
    LT,
    GE,
    GT;

    static int toUnary(Condition c2) {
        switch (c2) {
            case EQ:
                return 153;
            case NE:
                return 154;
            case LE:
                return 158;
            case LT:
                return 155;
            case GE:
                return 156;
            case GT:
                return 157;
            default:
                throw new UnsupportedOperationException("toUnary:" + c2.toString());
        }
    }

    static int toBinary(Condition c2, boolean isObject) {
        switch (c2) {
            case EQ:
                return isObject ? 165 : 159;
            case NE:
                return isObject ? 166 : 160;
            case LE:
                return 164;
            case LT:
                return 161;
            case GE:
                return 162;
            case GT:
                return 163;
            default:
                throw new UnsupportedOperationException("toBinary:" + c2.toString());
        }
    }
}
