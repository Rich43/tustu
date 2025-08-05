package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.exception.util.ExceptionContextProvider;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/MathArithmeticException.class */
public class MathArithmeticException extends ArithmeticException implements ExceptionContextProvider {
    private static final long serialVersionUID = -6024911025449780478L;
    private final ExceptionContext context = new ExceptionContext(this);

    public MathArithmeticException() {
        this.context.addMessage(LocalizedFormats.ARITHMETIC_EXCEPTION, new Object[0]);
    }

    public MathArithmeticException(Localizable pattern, Object... args) {
        this.context.addMessage(pattern, args);
    }

    @Override // org.apache.commons.math3.exception.util.ExceptionContextProvider
    public ExceptionContext getContext() {
        return this.context;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.context.getMessage();
    }

    @Override // java.lang.Throwable
    public String getLocalizedMessage() {
        return this.context.getLocalizedMessage();
    }
}
