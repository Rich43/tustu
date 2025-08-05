package org.apache.commons.math3.geometry.enclosing;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/enclosing/WelzlEncloser.class */
public class WelzlEncloser<S extends Space, P extends Point<S>> implements Encloser<S, P> {
    private final double tolerance;
    private final SupportBallGenerator<S, P> generator;

    public WelzlEncloser(double tolerance, SupportBallGenerator<S, P> generator) {
        this.tolerance = tolerance;
        this.generator = generator;
    }

    @Override // org.apache.commons.math3.geometry.enclosing.Encloser
    public EnclosingBall<S, P> enclose(Iterable<P> points) {
        if (points == null || !points.iterator().hasNext()) {
            return this.generator.ballOnSupport(new ArrayList());
        }
        return pivotingBall(points);
    }

    private EnclosingBall<S, P> pivotingBall(Iterable<P> points) {
        P first = points.iterator().next();
        ArrayList arrayList = new ArrayList(first.getSpace().getDimension() + 1);
        ArrayList arrayList2 = new ArrayList(first.getSpace().getDimension() + 1);
        arrayList.add(first);
        EnclosingBall<S, P> ball = moveToFrontBall(arrayList, arrayList.size(), arrayList2);
        while (true) {
            Point pointSelectFarthest = selectFarthest(points, ball);
            if (ball.contains(pointSelectFarthest, this.tolerance)) {
                return ball;
            }
            arrayList2.clear();
            arrayList2.add(pointSelectFarthest);
            EnclosingBall<S, P> savedBall = ball;
            ball = moveToFrontBall(arrayList, arrayList.size(), arrayList2);
            if (ball.getRadius() < savedBall.getRadius()) {
                throw new MathInternalError();
            }
            arrayList.add(0, pointSelectFarthest);
            arrayList.subList(ball.getSupportSize(), arrayList.size()).clear();
        }
    }

    private EnclosingBall<S, P> moveToFrontBall(List<P> list, int nbExtreme, List<P> list2) {
        EnclosingBall<S, P> ball = this.generator.ballOnSupport(list2);
        if (ball.getSupportSize() <= ball.getCenter().getSpace().getDimension()) {
            for (int i2 = 0; i2 < nbExtreme; i2++) {
                P p2 = list.get(i2);
                if (!ball.contains(p2, this.tolerance)) {
                    list2.add(p2);
                    ball = moveToFrontBall(list, i2, list2);
                    list2.remove(list2.size() - 1);
                    for (int j2 = i2; j2 > 0; j2--) {
                        list.set(j2, list.get(j2 - 1));
                    }
                    list.set(0, p2);
                }
            }
        }
        return ball;
    }

    public P selectFarthest(Iterable<P> points, EnclosingBall<S, P> ball) {
        Point<S> center = ball.getCenter();
        P farthest = null;
        double dMax = -1.0d;
        for (P point : points) {
            double d2 = point.distance(center);
            if (d2 > dMax) {
                farthest = point;
                dMax = d2;
            }
        }
        return farthest;
    }
}
