package org.icepdf.core.pobjects.functions.postscript;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/postscript/OperatorNames.class */
public class OperatorNames {
    public static final int NO_OP = 0;
    public static final int OP_ABS = 1;
    public static final int OP_ADD = 2;
    public static final int OP_AND = 3;
    public static final int OP_ATAN = 4;
    public static final int OP_BITSHIFT = 5;
    public static final int OP_CEILING = 6;
    public static final int OP_COS = 7;
    public static final int OP_COPY = 8;
    public static final int OP_CVI = 9;
    public static final int OP_CVR = 10;
    public static final int OP_DIV = 11;
    public static final int OP_DUP = 12;
    public static final int OP_EQ = 13;
    public static final int OP_EXCH = 14;
    public static final int OP_EXP = 15;
    public static final int OP_FALSE = 16;
    public static final int OP_FLOOR = 17;
    public static final int OP_GE = 18;
    public static final int OP_GT = 19;
    public static final int OP_IDIV = 20;
    public static final int OP_IF = 21;
    public static final int OP_IFELSE = 22;
    public static final int OP_LN = 23;
    public static final int OP_INDEX = 24;
    public static final int OP_LE = 25;
    public static final int OP_LOG = 26;
    public static final int OP_LT = 27;
    public static final int OP_MOD = 28;
    public static final int OP_MUL = 29;
    public static final int OP_NE = 30;
    public static final int OP_NEG = 31;
    public static final int OP_NOT = 32;
    public static final int OP_OR = 33;
    public static final int OP_POP = 34;
    public static final int OP_ROLL = 35;
    public static final int OP_ROUND = 36;
    public static final int OP_SIN = 37;
    public static final int OP_SQRT = 38;
    public static final int OP_SUB = 39;
    public static final int OP_TRUE = 40;
    public static final int OP_TRUNCATE = 41;
    public static final int OP_XOR = 42;
    public static final int OP_EXP_START = 43;
    public static final int OP_EXP_END = 44;
    public static final int OP_PROC = 45;

    public static int getType(char[] ch, int offset, int length) {
        char c2 = ch[offset];
        switch (c2) {
            case 'A':
            case 'a':
                if (length == 4) {
                    return 4;
                }
                char c1 = ch[offset + 1];
                if (c1 == 'b' || c1 == 'B') {
                    return 1;
                }
                if (c1 == 'd' || c1 == 'D') {
                    return 2;
                }
                return 0;
            case 'B':
            case 'b':
                return 5;
            case 'C':
            case 'c':
                if (length == 8) {
                    return 6;
                }
                if (length == 4) {
                    return 8;
                }
                char c12 = ch[offset + 1];
                if (c12 == 'o' || c12 == 'O') {
                    return 7;
                }
                if (c12 == 'v' || c12 == 'V') {
                    char c22 = ch[offset + 2];
                    if (c22 == 'i' || c22 == 'I') {
                        return 9;
                    }
                    if (c22 == 'r' || c22 == 'R') {
                        return 10;
                    }
                    return 0;
                }
                return 0;
            case 'D':
            case 'd':
                char c13 = ch[offset + 1];
                if (c13 == 'i' || c13 == 'I') {
                    return 11;
                }
                if (c13 == 'u' || c13 == 'U') {
                    return 12;
                }
                return 0;
            case 'E':
            case 'e':
                if (length == 2) {
                    return 13;
                }
                if (length == 3) {
                    return 15;
                }
                return length == 4 ? 14 : 0;
            case 'F':
            case 'f':
                return 17;
            case 'G':
            case 'g':
                char c14 = ch[offset + 1];
                if (c14 == 'e' || c14 == 'E') {
                    return 18;
                }
                if (c14 == 't' || c14 == 'T') {
                    return 19;
                }
                return 0;
            case 'H':
            case 'J':
            case 'K':
            case 'Q':
            case 'U':
            case 'V':
            case 'W':
            case 'Y':
            case 'Z':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            case 'h':
            case 'j':
            case 'k':
            case 'q':
            case 'u':
            case 'v':
            case 'w':
            case 'y':
            case 'z':
            case '|':
            default:
                return 0;
            case 'I':
            case 'i':
                if (length == 6) {
                    return 22;
                }
                char c15 = ch[offset + 1];
                if (c15 == 'd' || c15 == 'D') {
                    return 20;
                }
                if (c15 == 'f' || c15 == 'F') {
                    return 21;
                }
                if (c15 == 'n' || c15 == 'N') {
                    if (length == 5) {
                        return 24;
                    }
                    return length == 2 ? 23 : 0;
                }
                return 0;
            case 'L':
            case 'l':
                if (length == 3) {
                    return 26;
                }
                char c16 = ch[offset + 1];
                if (c16 == 'e' || c16 == 'E') {
                    return 25;
                }
                if (c16 == 't' || c16 == 'T') {
                    return 27;
                }
                return 0;
            case 'M':
            case 'm':
                char c17 = ch[offset + 1];
                if (c17 == 'o' || c17 == 'O') {
                    return 28;
                }
                if (c17 == 'u' || c17 == 'U') {
                    return 29;
                }
                return 0;
            case 'N':
            case 'n':
                if (length == 2) {
                    return 30;
                }
                char c18 = ch[offset + 1];
                if (c18 == 'e' || c18 == 'e') {
                    return 31;
                }
                if (c18 == 'o' || c18 == 'O') {
                    return 32;
                }
                return 0;
            case 'O':
            case 'o':
                return 33;
            case 'P':
            case 'p':
                return 34;
            case 'R':
            case 'r':
                if (length == 4) {
                    return 35;
                }
                return length == 5 ? 36 : 0;
            case 'S':
            case 's':
                if (length == 4) {
                    return 38;
                }
                char c19 = ch[offset + 1];
                if (c19 == 'u' || c19 == 'U') {
                    return 39;
                }
                if (c19 == 'i' || c19 == 'I') {
                    return 37;
                }
                return 0;
            case 'T':
            case 't':
                return 41;
            case 'X':
            case 'x':
                return 42;
            case '{':
                return 43;
            case '}':
                return 44;
        }
    }
}
