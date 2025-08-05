package org.apache.commons.math3.geometry.enclosing;

import java.util.List;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/enclosing/SupportBallGenerator.class */
public interface SupportBallGenerator<S extends Space, P extends Point<S>> {
    EnclosingBall<S, P> ballOnSupport(List<P> list);
}
