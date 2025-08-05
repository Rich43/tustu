package org.apache.commons.math3.optim.linear;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/linear/NoFeasibleSolutionException.class */
public class NoFeasibleSolutionException extends MathIllegalStateException {
    private static final long serialVersionUID = -3044253632189082760L;

    public NoFeasibleSolutionException() {
        super(LocalizedFormats.NO_FEASIBLE_SOLUTION, new Object[0]);
    }
}
