package org.apache.commons.math3.geometry.spherical.twod;

import java.io.Serializable;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.spherical.oned.Sphere1D;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/Sphere2D.class */
public class Sphere2D implements Serializable, Space {
    private static final long serialVersionUID = 20131218;

    private Sphere2D() {
    }

    public static Sphere2D getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override // org.apache.commons.math3.geometry.Space
    public int getDimension() {
        return 2;
    }

    @Override // org.apache.commons.math3.geometry.Space
    public Sphere1D getSubSpace() {
        return Sphere1D.getInstance();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/Sphere2D$LazyHolder.class */
    private static class LazyHolder {
        private static final Sphere2D INSTANCE = new Sphere2D();

        private LazyHolder() {
        }
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
