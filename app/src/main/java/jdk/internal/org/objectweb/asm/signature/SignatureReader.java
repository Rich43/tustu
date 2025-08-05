package jdk.internal.org.objectweb.asm.signature;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/signature/SignatureReader.class */
public class SignatureReader {
    private final String signature;

    public SignatureReader(String str) {
        this.signature = str;
    }

    public void accept(SignatureVisitor signatureVisitor) {
        int i2;
        char cCharAt;
        String str = this.signature;
        int length = str.length();
        if (str.charAt(0) == '<') {
            i2 = 2;
            do {
                int iIndexOf = str.indexOf(58, i2);
                signatureVisitor.visitFormalTypeParameter(str.substring(i2 - 1, iIndexOf));
                int type = iIndexOf + 1;
                char cCharAt2 = str.charAt(type);
                if (cCharAt2 == 'L' || cCharAt2 == '[' || cCharAt2 == 'T') {
                    type = parseType(str, type, signatureVisitor.visitClassBound());
                }
                while (true) {
                    int i3 = type;
                    i2 = type + 1;
                    cCharAt = str.charAt(i3);
                    if (cCharAt != ':') {
                        break;
                    } else {
                        type = parseType(str, i2, signatureVisitor.visitInterfaceBound());
                    }
                }
            } while (cCharAt != '>');
        } else {
            i2 = 0;
        }
        if (str.charAt(i2) == '(') {
            int type2 = i2 + 1;
            while (str.charAt(type2) != ')') {
                type2 = parseType(str, type2, signatureVisitor.visitParameterType());
            }
            int type3 = parseType(str, type2 + 1, signatureVisitor.visitReturnType());
            while (true) {
                int i4 = type3;
                if (i4 < length) {
                    type3 = parseType(str, i4 + 1, signatureVisitor.visitExceptionType());
                } else {
                    return;
                }
            }
        } else {
            int type4 = parseType(str, i2, signatureVisitor.visitSuperclass());
            while (true) {
                int i5 = type4;
                if (i5 < length) {
                    type4 = parseType(str, i5, signatureVisitor.visitInterface());
                } else {
                    return;
                }
            }
        }
    }

    public void acceptType(SignatureVisitor signatureVisitor) {
        parseType(this.signature, 0, signatureVisitor);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:30:0x0145. Please report as an issue. */
    private static int parseType(String str, int i2, SignatureVisitor signatureVisitor) {
        int type = i2 + 1;
        char cCharAt = str.charAt(i2);
        switch (cCharAt) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'V':
            case 'Z':
                signatureVisitor.visitBaseType(cCharAt);
                return type;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                int i3 = type;
                boolean z2 = false;
                boolean z3 = false;
                while (true) {
                    int i4 = type;
                    type++;
                    char cCharAt2 = str.charAt(i4);
                    switch (cCharAt2) {
                        case '.':
                        case ';':
                            if (!z2) {
                                String strSubstring = str.substring(i3, type - 1);
                                if (z3) {
                                    signatureVisitor.visitInnerClassType(strSubstring);
                                } else {
                                    signatureVisitor.visitClassType(strSubstring);
                                }
                            }
                            if (cCharAt2 == ';') {
                                signatureVisitor.visitEnd();
                                return type;
                            }
                            i3 = type;
                            z2 = false;
                            z3 = true;
                            break;
                        case '<':
                            String strSubstring2 = str.substring(i3, type - 1);
                            if (z3) {
                                signatureVisitor.visitInnerClassType(strSubstring2);
                            } else {
                                signatureVisitor.visitClassType(strSubstring2);
                            }
                            z2 = true;
                            while (true) {
                                char cCharAt3 = str.charAt(type);
                                switch (cCharAt3) {
                                    case '*':
                                        type++;
                                        signatureVisitor.visitTypeArgument();
                                    case '+':
                                    case '-':
                                        type = parseType(str, type + 1, signatureVisitor.visitTypeArgument(cCharAt3));
                                    case '>':
                                        break;
                                    default:
                                        type = parseType(str, type, signatureVisitor.visitTypeArgument('='));
                                }
                            }
                            break;
                    }
                }
                break;
            case 'T':
                int iIndexOf = str.indexOf(59, type);
                signatureVisitor.visitTypeVariable(str.substring(type, iIndexOf));
                return iIndexOf + 1;
            case '[':
                return parseType(str, type, signatureVisitor.visitArrayType());
        }
    }
}
