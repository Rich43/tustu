package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/SelectionPolicy.class */
public interface SelectionPolicy {
    ChromosomePair select(Population population) throws MathIllegalArgumentException;
}
