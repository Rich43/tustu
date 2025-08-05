package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/ZeroException.class */
public class ZeroException extends MathIllegalNumberException {
    private static final long serialVersionUID = -1960874856936000015L;

    public ZeroException() {
        this(LocalizedFormats.ZERO_NOT_ALLOWED, new Object[0]);
    }

    public ZeroException(Localizable specific, Object... arguments) {
        super(specific, INTEGER_ZERO, arguments);
    }
}
