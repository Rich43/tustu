package org.apache.commons.math3.geometry.enclosing;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/enclosing/Encloser.class */
public interface Encloser<S extends Space, P extends Point<S>> {
    EnclosingBall<S, P> enclose(Iterable<P> iterable);
}
