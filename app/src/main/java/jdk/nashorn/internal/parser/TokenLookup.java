package jdk.nashorn.internal.parser;

/* loaded from: nashorn.jar:jdk/nashorn/internal/parser/TokenLookup.class */
public final class TokenLookup {
    private static final TokenType[] table;
    private static final int tableBase = 32;
    private static final int tableLimit = 126;
    private static final int tableLength = 95;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        TokenType next;
        $assertionsDisabled = !TokenLookup.class.desiredAssertionStatus();
        table = new TokenType[95];
        for (TokenType tokenType : TokenType.getValues()) {
            String name = tokenType.getName();
            if (name != null && tokenType.getKind() != TokenKind.SPECIAL) {
                char first = name.charAt(0);
                int index = first - ' ';
                if (!$assertionsDisabled && index >= 95) {
                    throw new AssertionError((Object) "Token name does not fit lookup table");
                }
                int length = tokenType.getLength();
                TokenType prev = null;
                TokenType next2 = table[index];
                while (true) {
                    next = next2;
                    if (next == null || next.getLength() <= length) {
                        break;
                    }
                    prev = next;
                    next2 = next.getNext();
                }
                tokenType.setNext(next);
                if (prev == null) {
                    table[index] = tokenType;
                } else {
                    prev.setNext(tokenType);
                }
            }
        }
    }

    private TokenLookup() {
    }

    public static TokenType lookupKeyword(char[] content, int position, int length) {
        if (!$assertionsDisabled && table == null) {
            throw new AssertionError((Object) "Token lookup table is not initialized");
        }
        char first = content[position];
        if ('a' <= first && first <= 'z') {
            int index = first - ' ';
            TokenType next = table[index];
            while (true) {
                TokenType tokenType = next;
                if (tokenType == null) {
                    break;
                }
                int tokenLength = tokenType.getLength();
                if (tokenLength == length) {
                    String name = tokenType.getName();
                    int i2 = 0;
                    while (i2 < length && content[position + i2] == name.charAt(i2)) {
                        i2++;
                    }
                    if (i2 == length) {
                        return tokenType;
                    }
                } else if (tokenLength < length) {
                    break;
                }
                next = tokenType.getNext();
            }
        }
        return TokenType.IDENT;
    }

    public static TokenType lookupOperator(char ch0, char ch1, char ch2, char ch3) {
        if (!$assertionsDisabled && table == null) {
            throw new AssertionError((Object) "Token lookup table is not initialized");
        }
        if (' ' >= ch0 || ch0 > '~') {
            return null;
        }
        if ('a' > ch0 || ch0 > 'z') {
            int index = ch0 - ' ';
            TokenType next = table[index];
            while (true) {
                TokenType tokenType = next;
                if (tokenType != null) {
                    String name = tokenType.getName();
                    switch (name.length()) {
                        case 1:
                            return tokenType;
                        case 2:
                            if (name.charAt(1) != ch1) {
                                break;
                            } else {
                                return tokenType;
                            }
                        case 3:
                            if (name.charAt(1) != ch1 || name.charAt(2) != ch2) {
                                break;
                            } else {
                                return tokenType;
                            }
                        case 4:
                            if (name.charAt(1) != ch1 || name.charAt(2) != ch2 || name.charAt(3) != ch3) {
                                break;
                            } else {
                                return tokenType;
                            }
                            break;
                    }
                    next = tokenType.getNext();
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }
}
