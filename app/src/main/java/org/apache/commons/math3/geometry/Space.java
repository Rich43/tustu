package org.apache.commons.math3.geometry;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/Space.class */
public interface Space extends Serializable {
    int getDimension();

    Space getSubSpace() throws MathUnsupportedOperationException;
}
