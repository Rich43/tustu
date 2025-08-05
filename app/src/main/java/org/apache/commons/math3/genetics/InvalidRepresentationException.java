package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.Localizable;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/InvalidRepresentationException.class */
public class InvalidRepresentationException extends MathIllegalArgumentException {
    private static final long serialVersionUID = 1;

    public InvalidRepresentationException(Localizable pattern, Object... args) {
        super(pattern, args);
    }
}
