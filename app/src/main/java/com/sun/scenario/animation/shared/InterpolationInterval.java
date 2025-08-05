package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.NumberTangentInterpolator;
import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableBooleanValue;
import javafx.beans.value.WritableDoubleValue;
import javafx.beans.value.WritableFloatValue;
import javafx.beans.value.WritableIntegerValue;
import javafx.beans.value.WritableLongValue;
import javafx.beans.value.WritableValue;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval.class */
public abstract class InterpolationInterval {
    protected final long ticks;
    protected final Interpolator rightInterpolator;

    public abstract void interpolate(double d2);

    public abstract void recalculateStartValue();

    protected InterpolationInterval(long ticks, Interpolator rightInterpolator) {
        this.ticks = ticks;
        this.rightInterpolator = rightInterpolator;
    }

    public static InterpolationInterval create(KeyValue rightKeyValue, long ticks, KeyValue leftKeyValue, long duration) {
        switch (rightKeyValue.getType()) {
            case BOOLEAN:
                return new BooleanInterpolationInterval(rightKeyValue, ticks, leftKeyValue.getEndValue());
            case DOUBLE:
                return ((leftKeyValue.getInterpolator() instanceof NumberTangentInterpolator) || (rightKeyValue.getInterpolator() instanceof NumberTangentInterpolator)) ? new TangentDoubleInterpolationInterval(rightKeyValue, ticks, leftKeyValue, duration) : new DoubleInterpolationInterval(rightKeyValue, ticks, leftKeyValue.getEndValue());
            case FLOAT:
                return ((leftKeyValue.getInterpolator() instanceof NumberTangentInterpolator) || (rightKeyValue.getInterpolator() instanceof NumberTangentInterpolator)) ? new TangentFloatInterpolationInterval(rightKeyValue, ticks, leftKeyValue, duration) : new FloatInterpolationInterval(rightKeyValue, ticks, leftKeyValue.getEndValue());
            case INTEGER:
                return ((leftKeyValue.getInterpolator() instanceof NumberTangentInterpolator) || (rightKeyValue.getInterpolator() instanceof NumberTangentInterpolator)) ? new TangentIntegerInterpolationInterval(rightKeyValue, ticks, leftKeyValue, duration) : new IntegerInterpolationInterval(rightKeyValue, ticks, leftKeyValue.getEndValue());
            case LONG:
                return ((leftKeyValue.getInterpolator() instanceof NumberTangentInterpolator) || (rightKeyValue.getInterpolator() instanceof NumberTangentInterpolator)) ? new TangentLongInterpolationInterval(rightKeyValue, ticks, leftKeyValue, duration) : new LongInterpolationInterval(rightKeyValue, ticks, leftKeyValue.getEndValue());
            case OBJECT:
                return new ObjectInterpolationInterval(rightKeyValue, ticks, leftKeyValue.getEndValue());
            default:
                throw new RuntimeException("Should not reach here");
        }
    }

    public static InterpolationInterval create(KeyValue rightKeyValue, long ticks) {
        switch (rightKeyValue.getType()) {
            case BOOLEAN:
                return new BooleanInterpolationInterval(rightKeyValue, ticks);
            case DOUBLE:
                return rightKeyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentDoubleInterpolationInterval(rightKeyValue, ticks) : new DoubleInterpolationInterval(rightKeyValue, ticks);
            case FLOAT:
                return rightKeyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentFloatInterpolationInterval(rightKeyValue, ticks) : new FloatInterpolationInterval(rightKeyValue, ticks);
            case INTEGER:
                return rightKeyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentIntegerInterpolationInterval(rightKeyValue, ticks) : new IntegerInterpolationInterval(rightKeyValue, ticks);
            case LONG:
                return rightKeyValue.getInterpolator() instanceof NumberTangentInterpolator ? new TangentLongInterpolationInterval(rightKeyValue, ticks) : new LongInterpolationInterval(rightKeyValue, ticks);
            case OBJECT:
                return new ObjectInterpolationInterval(rightKeyValue, ticks);
            default:
                throw new RuntimeException("Should not reach here");
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$TangentInterpolationInterval.class */
    private static abstract class TangentInterpolationInterval extends InterpolationInterval {
        private final double duration;
        private final double p2;
        protected final double p3;
        private final NumberTangentInterpolator leftInterpolator;
        protected double p0;
        private double p1;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private TangentInterpolationInterval(KeyValue rightKeyValue, long ticks, KeyValue leftKeyValue, long duration) {
            super(ticks, rightKeyValue.getInterpolator());
            if (!$assertionsDisabled && (!(rightKeyValue.getEndValue() instanceof Number) || !(leftKeyValue.getEndValue() instanceof Number))) {
                throw new AssertionError();
            }
            this.duration = duration;
            Interpolator rawLeftInterpolator = leftKeyValue.getInterpolator();
            this.leftInterpolator = rawLeftInterpolator instanceof NumberTangentInterpolator ? (NumberTangentInterpolator) rawLeftInterpolator : null;
            recalculateStartValue(((Number) leftKeyValue.getEndValue()).doubleValue());
            NumberTangentInterpolator interpolator = this.rightInterpolator instanceof NumberTangentInterpolator ? (NumberTangentInterpolator) this.rightInterpolator : null;
            this.p3 = ((Number) rightKeyValue.getEndValue()).doubleValue();
            double p2Delta = interpolator == null ? 0.0d : (((interpolator.getInValue() - this.p3) * duration) / interpolator.getInTicks()) / 3.0d;
            this.p2 = this.p3 + p2Delta;
        }

        private TangentInterpolationInterval(KeyValue rightKeyValue, long ticks) {
            super(ticks, rightKeyValue.getInterpolator());
            if (!$assertionsDisabled && !(rightKeyValue.getEndValue() instanceof Number)) {
                throw new AssertionError();
            }
            this.duration = ticks;
            this.leftInterpolator = null;
            NumberTangentInterpolator interpolator = this.rightInterpolator instanceof NumberTangentInterpolator ? (NumberTangentInterpolator) this.rightInterpolator : null;
            this.p3 = ((Number) rightKeyValue.getEndValue()).doubleValue();
            double p2Delta = interpolator == null ? 0.0d : (((interpolator.getInValue() - this.p3) * this.duration) / interpolator.getInTicks()) / 3.0d;
            this.p2 = this.p3 + p2Delta;
        }

        protected double calculate(double t2) {
            double oneMinusT = 1.0d - t2;
            double tSquared = t2 * t2;
            double oneMinusTSquared = oneMinusT * oneMinusT;
            return (oneMinusTSquared * oneMinusT * this.p0) + (3.0d * oneMinusTSquared * t2 * this.p1) + (3.0d * oneMinusT * tSquared * this.p2) + (tSquared * t2 * this.p3);
        }

        protected final void recalculateStartValue(double leftValue) {
            this.p0 = leftValue;
            double p1Delta = this.leftInterpolator == null ? 0.0d : (((this.leftInterpolator.getOutValue() - this.p0) * this.duration) / this.leftInterpolator.getOutTicks()) / 3.0d;
            this.p1 = this.p0 + p1Delta;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$BooleanInterpolationInterval.class */
    private static class BooleanInterpolationInterval extends InterpolationInterval {
        private final WritableBooleanValue target;
        private boolean leftValue;
        private final boolean rightValue;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private BooleanInterpolationInterval(KeyValue keyValue, long ticks, Object leftValue) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableBooleanValue) || !(keyValue.getEndValue() instanceof Boolean) || !(leftValue instanceof Boolean))) {
                throw new AssertionError();
            }
            this.target = (WritableBooleanValue) keyValue.getTarget();
            this.rightValue = ((Boolean) keyValue.getEndValue()).booleanValue();
            this.leftValue = ((Boolean) leftValue).booleanValue();
        }

        private BooleanInterpolationInterval(KeyValue keyValue, long ticks) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableBooleanValue) || !(keyValue.getEndValue() instanceof Boolean))) {
                throw new AssertionError();
            }
            this.target = (WritableBooleanValue) keyValue.getTarget();
            this.rightValue = ((Boolean) keyValue.getEndValue()).booleanValue();
            this.leftValue = this.target.get();
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            boolean value = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, frac);
            this.target.set(value);
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$DoubleInterpolationInterval.class */
    private static class DoubleInterpolationInterval extends InterpolationInterval {
        private final WritableDoubleValue target;
        private double leftValue;
        private final double rightValue;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private DoubleInterpolationInterval(KeyValue keyValue, long ticks, Object leftValue) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableDoubleValue) || !(keyValue.getEndValue() instanceof Number) || !(leftValue instanceof Number))) {
                throw new AssertionError();
            }
            this.target = (WritableDoubleValue) keyValue.getTarget();
            this.rightValue = ((Number) keyValue.getEndValue()).doubleValue();
            this.leftValue = ((Number) leftValue).doubleValue();
        }

        private DoubleInterpolationInterval(KeyValue keyValue, long ticks) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableDoubleValue) || !(keyValue.getEndValue() instanceof Number))) {
                throw new AssertionError();
            }
            this.target = (WritableDoubleValue) keyValue.getTarget();
            this.rightValue = ((Number) keyValue.getEndValue()).doubleValue();
            this.leftValue = this.target.get();
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            double value = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, frac);
            this.target.set(value);
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$TangentDoubleInterpolationInterval.class */
    private static class TangentDoubleInterpolationInterval extends TangentInterpolationInterval {
        private final WritableDoubleValue target;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private TangentDoubleInterpolationInterval(KeyValue rightKeyValue, long ticks, KeyValue leftKeyValue, long duration) {
            super(rightKeyValue, ticks, leftKeyValue, duration);
            if (!$assertionsDisabled && !(rightKeyValue.getTarget() instanceof WritableDoubleValue)) {
                throw new AssertionError();
            }
            this.target = (WritableDoubleValue) rightKeyValue.getTarget();
        }

        private TangentDoubleInterpolationInterval(KeyValue rightKeyValue, long ticks) {
            super(rightKeyValue, ticks);
            if (!$assertionsDisabled && !(rightKeyValue.getTarget() instanceof WritableDoubleValue)) {
                throw new AssertionError();
            }
            this.target = (WritableDoubleValue) rightKeyValue.getTarget();
            recalculateStartValue(this.target.get());
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            this.target.set(calculate(frac));
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            recalculateStartValue(this.target.get());
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$FloatInterpolationInterval.class */
    private static class FloatInterpolationInterval extends InterpolationInterval {
        private final WritableFloatValue target;
        private float leftValue;
        private final float rightValue;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private FloatInterpolationInterval(KeyValue keyValue, long ticks, Object leftValue) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableFloatValue) || !(keyValue.getEndValue() instanceof Number) || !(leftValue instanceof Number))) {
                throw new AssertionError();
            }
            this.target = (WritableFloatValue) keyValue.getTarget();
            this.rightValue = ((Number) keyValue.getEndValue()).floatValue();
            this.leftValue = ((Number) leftValue).floatValue();
        }

        private FloatInterpolationInterval(KeyValue keyValue, long ticks) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableFloatValue) || !(keyValue.getEndValue() instanceof Number))) {
                throw new AssertionError();
            }
            this.target = (WritableFloatValue) keyValue.getTarget();
            this.rightValue = ((Number) keyValue.getEndValue()).floatValue();
            this.leftValue = this.target.get();
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            float value = (float) this.rightInterpolator.interpolate(this.leftValue, this.rightValue, frac);
            this.target.set(value);
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$TangentFloatInterpolationInterval.class */
    private static class TangentFloatInterpolationInterval extends TangentInterpolationInterval {
        private final WritableFloatValue target;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private TangentFloatInterpolationInterval(KeyValue rightKeyValue, long ticks, KeyValue leftKeyValue, long duration) {
            super(rightKeyValue, ticks, leftKeyValue, duration);
            if (!$assertionsDisabled && !(rightKeyValue.getTarget() instanceof WritableFloatValue)) {
                throw new AssertionError();
            }
            this.target = (WritableFloatValue) rightKeyValue.getTarget();
        }

        private TangentFloatInterpolationInterval(KeyValue rightKeyValue, long ticks) {
            super(rightKeyValue, ticks);
            if (!$assertionsDisabled && !(rightKeyValue.getTarget() instanceof WritableFloatValue)) {
                throw new AssertionError();
            }
            this.target = (WritableFloatValue) rightKeyValue.getTarget();
            recalculateStartValue(this.target.get());
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            this.target.set((float) calculate(frac));
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            recalculateStartValue(this.target.get());
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$IntegerInterpolationInterval.class */
    private static class IntegerInterpolationInterval extends InterpolationInterval {
        private final WritableIntegerValue target;
        private int leftValue;
        private final int rightValue;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private IntegerInterpolationInterval(KeyValue keyValue, long ticks, Object leftValue) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableIntegerValue) || !(keyValue.getEndValue() instanceof Number) || !(leftValue instanceof Number))) {
                throw new AssertionError();
            }
            this.target = (WritableIntegerValue) keyValue.getTarget();
            this.rightValue = ((Number) keyValue.getEndValue()).intValue();
            this.leftValue = ((Number) leftValue).intValue();
        }

        private IntegerInterpolationInterval(KeyValue keyValue, long ticks) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableIntegerValue) || !(keyValue.getEndValue() instanceof Number))) {
                throw new AssertionError();
            }
            this.target = (WritableIntegerValue) keyValue.getTarget();
            this.rightValue = ((Number) keyValue.getEndValue()).intValue();
            this.leftValue = this.target.get();
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            int value = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, frac);
            this.target.set(value);
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$TangentIntegerInterpolationInterval.class */
    private static class TangentIntegerInterpolationInterval extends TangentInterpolationInterval {
        private final WritableIntegerValue target;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private TangentIntegerInterpolationInterval(KeyValue rightKeyValue, long ticks, KeyValue leftKeyValue, long duration) {
            super(rightKeyValue, ticks, leftKeyValue, duration);
            if (!$assertionsDisabled && !(rightKeyValue.getTarget() instanceof WritableIntegerValue)) {
                throw new AssertionError();
            }
            this.target = (WritableIntegerValue) rightKeyValue.getTarget();
        }

        private TangentIntegerInterpolationInterval(KeyValue rightKeyValue, long ticks) {
            super(rightKeyValue, ticks);
            if (!$assertionsDisabled && !(rightKeyValue.getTarget() instanceof WritableIntegerValue)) {
                throw new AssertionError();
            }
            this.target = (WritableIntegerValue) rightKeyValue.getTarget();
            recalculateStartValue(this.target.get());
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            this.target.set((int) Math.round(calculate(frac)));
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            recalculateStartValue(this.target.get());
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$LongInterpolationInterval.class */
    private static class LongInterpolationInterval extends InterpolationInterval {
        private final WritableLongValue target;
        private long leftValue;
        private final long rightValue;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private LongInterpolationInterval(KeyValue keyValue, long ticks, Object leftValue) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableLongValue) || !(keyValue.getEndValue() instanceof Number) || !(leftValue instanceof Number))) {
                throw new AssertionError();
            }
            this.target = (WritableLongValue) keyValue.getTarget();
            this.rightValue = ((Number) keyValue.getEndValue()).longValue();
            this.leftValue = ((Number) leftValue).longValue();
        }

        private LongInterpolationInterval(KeyValue keyValue, long ticks) {
            super(ticks, keyValue.getInterpolator());
            if (!$assertionsDisabled && (!(keyValue.getTarget() instanceof WritableLongValue) || !(keyValue.getEndValue() instanceof Number))) {
                throw new AssertionError();
            }
            this.target = (WritableLongValue) keyValue.getTarget();
            this.rightValue = ((Number) keyValue.getEndValue()).longValue();
            this.leftValue = this.target.get();
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            long value = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, frac);
            this.target.set(value);
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            this.leftValue = this.target.get();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$TangentLongInterpolationInterval.class */
    private static class TangentLongInterpolationInterval extends TangentInterpolationInterval {
        private final WritableLongValue target;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !InterpolationInterval.class.desiredAssertionStatus();
        }

        private TangentLongInterpolationInterval(KeyValue rightKeyValue, long ticks, KeyValue leftKeyValue, long duration) {
            super(rightKeyValue, ticks, leftKeyValue, duration);
            if (!$assertionsDisabled && !(rightKeyValue.getTarget() instanceof WritableLongValue)) {
                throw new AssertionError();
            }
            this.target = (WritableLongValue) rightKeyValue.getTarget();
        }

        private TangentLongInterpolationInterval(KeyValue rightKeyValue, long ticks) {
            super(rightKeyValue, ticks);
            if (!$assertionsDisabled && !(rightKeyValue.getTarget() instanceof WritableLongValue)) {
                throw new AssertionError();
            }
            this.target = (WritableLongValue) rightKeyValue.getTarget();
            recalculateStartValue(this.target.get());
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            this.target.set(Math.round(calculate(frac)));
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            recalculateStartValue(this.target.get());
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InterpolationInterval$ObjectInterpolationInterval.class */
    private static class ObjectInterpolationInterval extends InterpolationInterval {
        private final WritableValue target;
        private Object leftValue;
        private final Object rightValue;

        private ObjectInterpolationInterval(KeyValue keyValue, long ticks, Object leftValue) {
            super(ticks, keyValue.getInterpolator());
            this.target = keyValue.getTarget();
            this.rightValue = keyValue.getEndValue();
            this.leftValue = leftValue;
        }

        private ObjectInterpolationInterval(KeyValue keyValue, long ticks) {
            super(ticks, keyValue.getInterpolator());
            this.target = keyValue.getTarget();
            this.rightValue = keyValue.getEndValue();
            this.leftValue = this.target.getValue();
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void interpolate(double frac) {
            Object value = this.rightInterpolator.interpolate(this.leftValue, this.rightValue, frac);
            this.target.setValue(value);
        }

        @Override // com.sun.scenario.animation.shared.InterpolationInterval
        public void recalculateStartValue() {
            this.leftValue = this.target.getValue();
        }
    }
}
