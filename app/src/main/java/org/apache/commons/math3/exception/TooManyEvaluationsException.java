package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/TooManyEvaluationsException.class */
public class TooManyEvaluationsException extends MaxCountExceededException {
    private static final long serialVersionUID = 4330003017885151975L;

    public TooManyEvaluationsException(Number max) {
        super(max);
        getContext().addMessage(LocalizedFormats.EVALUATIONS, new Object[0]);
    }
}
