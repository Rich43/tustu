package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/NotFiniteNumberException.class */
public class NotFiniteNumberException extends MathIllegalNumberException {
    private static final long serialVersionUID = -6100997100383932834L;

    public NotFiniteNumberException(Number wrong, Object... args) {
        this(LocalizedFormats.NOT_FINITE_NUMBER, wrong, args);
    }

    public NotFiniteNumberException(Localizable specific, Number wrong, Object... args) {
        super(specific, wrong, args);
    }
}
