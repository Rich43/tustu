package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/NoDataException.class */
public class NoDataException extends MathIllegalArgumentException {
    private static final long serialVersionUID = -3629324471511904459L;

    public NoDataException() {
        this(LocalizedFormats.NO_DATA);
    }

    public NoDataException(Localizable specific) {
        super(specific, new Object[0]);
    }
}
