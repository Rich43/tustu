package javafx.animation;

import com.sun.scenario.animation.NumberTangentInterpolator;
import com.sun.scenario.animation.SplineInterpolator;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/Interpolator.class */
public abstract class Interpolator {
    private static final double EPSILON = 1.0E-12d;
    public static final Interpolator DISCRETE = new Interpolator() { // from class: javafx.animation.Interpolator.1
        @Override // javafx.animation.Interpolator
        protected double curve(double t2) {
            return Math.abs(t2 - 1.0d) < 1.0E-12d ? 1.0d : 0.0d;
        }

        public String toString() {
            return "Interpolator.DISCRETE";
        }
    };
    public static final Interpolator LINEAR = new Interpolator() { // from class: javafx.animation.Interpolator.2
        @Override // javafx.animation.Interpolator
        protected double curve(double t2) {
            return t2;
        }

        public String toString() {
            return "Interpolator.LINEAR";
        }
    };
    public static final Interpolator EASE_BOTH = new Interpolator() { // from class: javafx.animation.Interpolator.3
        @Override // javafx.animation.Interpolator
        protected double curve(double t2) {
            return Interpolator.clamp(t2 < 0.2d ? 3.125d * t2 * t2 : t2 > 0.8d ? ((((-3.125d) * t2) * t2) + (6.25d * t2)) - 2.125d : (1.25d * t2) - 0.125d);
        }

        public String toString() {
            return "Interpolator.EASE_BOTH";
        }
    };
    public static final Interpolator EASE_IN = new Interpolator() { // from class: javafx.animation.Interpolator.4
        private static final double S1 = 2.7777777777777777d;
        private static final double S3 = 1.1111111111111112d;
        private static final double S4 = 0.1111111111111111d;

        @Override // javafx.animation.Interpolator
        protected double curve(double t2) {
            return Interpolator.clamp(t2 < 0.2d ? S1 * t2 * t2 : (S3 * t2) - S4);
        }

        public String toString() {
            return "Interpolator.EASE_IN";
        }
    };
    public static final Interpolator EASE_OUT = new Interpolator() { // from class: javafx.animation.Interpolator.5
        private static final double S1 = -2.7777777777777777d;
        private static final double S2 = 5.555555555555555d;
        private static final double S3 = -1.7777777777777777d;
        private static final double S4 = 1.1111111111111112d;

        @Override // javafx.animation.Interpolator
        protected double curve(double t2) {
            return Interpolator.clamp(t2 > 0.8d ? (S1 * t2 * t2) + (S2 * t2) + S3 : S4 * t2);
        }

        public String toString() {
            return "Interpolator.EASE_OUT";
        }
    };

    protected abstract double curve(double d2);

    protected Interpolator() {
    }

    public static Interpolator SPLINE(double x1, double y1, double x2, double y2) {
        return new SplineInterpolator(x1, y1, x2, y2);
    }

    public static Interpolator TANGENT(Duration t1, double v1, Duration t2, double v2) {
        return new NumberTangentInterpolator(t1, v1, t2, v2);
    }

    public static Interpolator TANGENT(Duration t2, double v2) {
        return new NumberTangentInterpolator(t2, v2);
    }

    public Object interpolate(Object startValue, Object endValue, double fraction) {
        if ((startValue instanceof Number) && (endValue instanceof Number)) {
            double start = ((Number) startValue).doubleValue();
            double end = ((Number) endValue).doubleValue();
            double val = start + ((end - start) * curve(fraction));
            if ((startValue instanceof Double) || (endValue instanceof Double)) {
                return Double.valueOf(val);
            }
            if ((startValue instanceof Float) || (endValue instanceof Float)) {
                return Float.valueOf((float) val);
            }
            if ((startValue instanceof Long) || (endValue instanceof Long)) {
                return Long.valueOf(Math.round(val));
            }
            return Integer.valueOf((int) Math.round(val));
        }
        if ((startValue instanceof Interpolatable) && (endValue instanceof Interpolatable)) {
            return ((Interpolatable) startValue).interpolate(endValue, curve(fraction));
        }
        return curve(fraction) == 1.0d ? endValue : startValue;
    }

    public boolean interpolate(boolean startValue, boolean endValue, double fraction) {
        return Math.abs(curve(fraction) - 1.0d) < 1.0E-12d ? endValue : startValue;
    }

    public double interpolate(double startValue, double endValue, double fraction) {
        return startValue + ((endValue - startValue) * curve(fraction));
    }

    public int interpolate(int startValue, int endValue, double fraction) {
        return startValue + ((int) Math.round((endValue - startValue) * curve(fraction)));
    }

    public long interpolate(long startValue, long endValue, double fraction) {
        return startValue + Math.round((endValue - startValue) * curve(fraction));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double clamp(double t2) {
        if (t2 < 0.0d) {
            return 0.0d;
        }
        if (t2 > 1.0d) {
            return 1.0d;
        }
        return t2;
    }
}
