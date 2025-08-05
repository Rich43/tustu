package jdk.nashorn.internal.runtime;

import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenStream;
import jdk.nashorn.internal.parser.TokenType;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Debug.class */
public final class Debug {
    private Debug() {
    }

    public static String firstJSFrame(Throwable t2) {
        for (StackTraceElement ste : t2.getStackTrace()) {
            if (ECMAErrors.isScriptFrame(ste)) {
                return ste.toString();
            }
        }
        return "<native code>";
    }

    public static String firstJSFrame() {
        return firstJSFrame(new Throwable());
    }

    public static String scriptStack() {
        return NashornException.getScriptStackString(new Throwable());
    }

    public static String id(Object x2) {
        return String.format("0x%08x", Integer.valueOf(System.identityHashCode(x2)));
    }

    public static int intId(Object x2) {
        return System.identityHashCode(x2);
    }

    public static String stackTraceElementAt(int depth) {
        return new Throwable().getStackTrace()[depth + 1].toString();
    }

    public static String caller(int depth, int count, String... ignores) {
        String result = "";
        StackTraceElement[] callers = Thread.currentThread().getStackTrace();
        int c2 = count;
        for (int i2 = depth + 1; i2 < callers.length && c2 != 0; i2++) {
            StackTraceElement element = callers[i2];
            String method = element.getMethodName();
            int length = ignores.length;
            int i3 = 0;
            while (true) {
                if (i3 < length) {
                    String ignore = ignores[i3];
                    if (method.compareTo(ignore) == 0) {
                        break;
                    }
                    i3++;
                } else {
                    result = result + (method + CallSiteDescriptor.TOKEN_DELIMITER + element.getLineNumber() + "                              ").substring(0, 30);
                    c2--;
                    break;
                }
            }
        }
        return result.isEmpty() ? "<no caller>" : result;
    }

    public static void dumpTokens(Source source, Lexer lexer, TokenStream stream) {
        int k2 = 0;
        while (true) {
            if (k2 > stream.last()) {
                lexer.lexify();
            } else {
                long token = stream.get(k2);
                TokenType type = Token.descType(token);
                System.out.println("" + k2 + ": " + Token.toString(source, token, true));
                k2++;
                if (type == TokenType.EOF) {
                    return;
                }
            }
        }
    }
}
