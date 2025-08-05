package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/MathInternalError.class */
public class MathInternalError extends MathIllegalStateException {
    private static final long serialVersionUID = -6276776513966934846L;
    private static final String REPORT_URL = "https://issues.apache.org/jira/browse/MATH";

    public MathInternalError() {
        getContext().addMessage(LocalizedFormats.INTERNAL_ERROR, REPORT_URL);
    }

    public MathInternalError(Throwable cause) {
        super(cause, LocalizedFormats.INTERNAL_ERROR, REPORT_URL);
    }

    public MathInternalError(Localizable pattern, Object... args) {
        super(pattern, args);
    }
}
