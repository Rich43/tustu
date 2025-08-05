package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/MaxCountExceededException.class */
public class MaxCountExceededException extends MathIllegalStateException {
    private static final long serialVersionUID = 4330003017885151975L;
    private final Number max;

    public MaxCountExceededException(Number max) {
        this(LocalizedFormats.MAX_COUNT_EXCEEDED, max, new Object[0]);
    }

    public MaxCountExceededException(Localizable specific, Number max, Object... args) {
        getContext().addMessage(specific, max, args);
        this.max = max;
    }

    public Number getMax() {
        return this.max;
    }
}
