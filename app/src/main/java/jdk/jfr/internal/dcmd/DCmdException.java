package jdk.jfr.internal.dcmd;

import java.util.Formatter;

/* loaded from: jfr.jar:jdk/jfr/internal/dcmd/DCmdException.class */
final class DCmdException extends Exception {
    private static final long serialVersionUID = -3792411099340016465L;

    public DCmdException(String str, Object... objArr) {
        super(format(str, objArr));
    }

    public DCmdException(Throwable th, String str, Object... objArr) {
        super(format(str, objArr), th);
    }

    private static String format(String str, Object... objArr) {
        Formatter formatter = new Formatter();
        Throwable th = null;
        try {
            try {
                String string = formatter.format(str, objArr).toString();
                if (formatter != null) {
                    if (0 != 0) {
                        try {
                            formatter.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        formatter.close();
                    }
                }
                return string;
            } finally {
            }
        } catch (Throwable th3) {
            if (formatter != null) {
                if (th != null) {
                    try {
                        formatter.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    formatter.close();
                }
            }
            throw th3;
        }
    }
}
