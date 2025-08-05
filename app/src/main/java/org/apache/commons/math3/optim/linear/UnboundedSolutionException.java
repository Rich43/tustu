package org.apache.commons.math3.optim.linear;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/linear/UnboundedSolutionException.class */
public class UnboundedSolutionException extends MathIllegalStateException {
    private static final long serialVersionUID = 940539497277290619L;

    public UnboundedSolutionException() {
        super(LocalizedFormats.UNBOUNDED_SOLUTION, new Object[0]);
    }
}
