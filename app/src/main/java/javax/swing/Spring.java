package javax.swing;

import java.awt.Component;

/* loaded from: rt.jar:javax/swing/Spring.class */
public abstract class Spring {
    public static final int UNSET = Integer.MIN_VALUE;

    public abstract int getMinimumValue();

    public abstract int getPreferredValue();

    public abstract int getMaximumValue();

    public abstract int getValue();

    public abstract void setValue(int i2);

    protected Spring() {
    }

    private double range(boolean z2) {
        return z2 ? getPreferredValue() - getMinimumValue() : getMaximumValue() - getPreferredValue();
    }

    double getStrain() {
        return (getValue() - getPreferredValue()) / range(getValue() < getPreferredValue());
    }

    void setStrain(double d2) {
        setValue(getPreferredValue() + ((int) (d2 * range(d2 < 0.0d))));
    }

    boolean isCyclic(SpringLayout springLayout) {
        return false;
    }

    /* loaded from: rt.jar:javax/swing/Spring$AbstractSpring.class */
    static abstract class AbstractSpring extends Spring {
        protected int size = Integer.MIN_VALUE;

        AbstractSpring() {
        }

        @Override // javax.swing.Spring
        public int getValue() {
            return this.size != Integer.MIN_VALUE ? this.size : getPreferredValue();
        }

        @Override // javax.swing.Spring
        public final void setValue(int i2) {
            if (this.size == i2) {
                return;
            }
            if (i2 == Integer.MIN_VALUE) {
                clear();
            } else {
                setNonClearValue(i2);
            }
        }

        protected void clear() {
            this.size = Integer.MIN_VALUE;
        }

        protected void setNonClearValue(int i2) {
            this.size = i2;
        }
    }

    /* loaded from: rt.jar:javax/swing/Spring$StaticSpring.class */
    private static class StaticSpring extends AbstractSpring {
        protected int min;
        protected int pref;
        protected int max;

        public StaticSpring(int i2) {
            this(i2, i2, i2);
        }

        public StaticSpring(int i2, int i3, int i4) {
            this.min = i2;
            this.pref = i3;
            this.max = i4;
        }

        public String toString() {
            return "StaticSpring [" + this.min + ", " + this.pref + ", " + this.max + "]";
        }

        @Override // javax.swing.Spring
        public int getMinimumValue() {
            return this.min;
        }

        @Override // javax.swing.Spring
        public int getPreferredValue() {
            return this.pref;
        }

        @Override // javax.swing.Spring
        public int getMaximumValue() {
            return this.max;
        }
    }

    /* loaded from: rt.jar:javax/swing/Spring$NegativeSpring.class */
    private static class NegativeSpring extends Spring {

        /* renamed from: s, reason: collision with root package name */
        private Spring f12805s;

        public NegativeSpring(Spring spring) {
            this.f12805s = spring;
        }

        @Override // javax.swing.Spring
        public int getMinimumValue() {
            return -this.f12805s.getMaximumValue();
        }

        @Override // javax.swing.Spring
        public int getPreferredValue() {
            return -this.f12805s.getPreferredValue();
        }

        @Override // javax.swing.Spring
        public int getMaximumValue() {
            return -this.f12805s.getMinimumValue();
        }

        @Override // javax.swing.Spring
        public int getValue() {
            return -this.f12805s.getValue();
        }

        @Override // javax.swing.Spring
        public void setValue(int i2) {
            this.f12805s.setValue(-i2);
        }

        @Override // javax.swing.Spring
        boolean isCyclic(SpringLayout springLayout) {
            return this.f12805s.isCyclic(springLayout);
        }
    }

    /* loaded from: rt.jar:javax/swing/Spring$ScaleSpring.class */
    private static class ScaleSpring extends Spring {

        /* renamed from: s, reason: collision with root package name */
        private Spring f12806s;
        private float factor;

        private ScaleSpring(Spring spring, float f2) {
            this.f12806s = spring;
            this.factor = f2;
        }

        @Override // javax.swing.Spring
        public int getMinimumValue() {
            return Math.round((this.factor < 0.0f ? this.f12806s.getMaximumValue() : this.f12806s.getMinimumValue()) * this.factor);
        }

        @Override // javax.swing.Spring
        public int getPreferredValue() {
            return Math.round(this.f12806s.getPreferredValue() * this.factor);
        }

        @Override // javax.swing.Spring
        public int getMaximumValue() {
            return Math.round((this.factor < 0.0f ? this.f12806s.getMinimumValue() : this.f12806s.getMaximumValue()) * this.factor);
        }

        @Override // javax.swing.Spring
        public int getValue() {
            return Math.round(this.f12806s.getValue() * this.factor);
        }

        @Override // javax.swing.Spring
        public void setValue(int i2) {
            if (i2 == Integer.MIN_VALUE) {
                this.f12806s.setValue(Integer.MIN_VALUE);
            } else {
                this.f12806s.setValue(Math.round(i2 / this.factor));
            }
        }

        @Override // javax.swing.Spring
        boolean isCyclic(SpringLayout springLayout) {
            return this.f12806s.isCyclic(springLayout);
        }
    }

    /* loaded from: rt.jar:javax/swing/Spring$WidthSpring.class */
    static class WidthSpring extends AbstractSpring {

        /* renamed from: c, reason: collision with root package name */
        Component f12808c;

        public WidthSpring(Component component) {
            this.f12808c = component;
        }

        @Override // javax.swing.Spring
        public int getMinimumValue() {
            return this.f12808c.getMinimumSize().width;
        }

        @Override // javax.swing.Spring
        public int getPreferredValue() {
            return this.f12808c.getPreferredSize().width;
        }

        @Override // javax.swing.Spring
        public int getMaximumValue() {
            return Math.min(Short.MAX_VALUE, this.f12808c.getMaximumSize().width);
        }
    }

    /* loaded from: rt.jar:javax/swing/Spring$HeightSpring.class */
    static class HeightSpring extends AbstractSpring {

        /* renamed from: c, reason: collision with root package name */
        Component f12804c;

        public HeightSpring(Component component) {
            this.f12804c = component;
        }

        @Override // javax.swing.Spring
        public int getMinimumValue() {
            return this.f12804c.getMinimumSize().height;
        }

        @Override // javax.swing.Spring
        public int getPreferredValue() {
            return this.f12804c.getPreferredSize().height;
        }

        @Override // javax.swing.Spring
        public int getMaximumValue() {
            return Math.min(Short.MAX_VALUE, this.f12804c.getMaximumSize().height);
        }
    }

    /* loaded from: rt.jar:javax/swing/Spring$SpringMap.class */
    static abstract class SpringMap extends Spring {

        /* renamed from: s, reason: collision with root package name */
        private Spring f12807s;

        protected abstract int map(int i2);

        protected abstract int inv(int i2);

        public SpringMap(Spring spring) {
            this.f12807s = spring;
        }

        @Override // javax.swing.Spring
        public int getMinimumValue() {
            return map(this.f12807s.getMinimumValue());
        }

        @Override // javax.swing.Spring
        public int getPreferredValue() {
            return map(this.f12807s.getPreferredValue());
        }

        @Override // javax.swing.Spring
        public int getMaximumValue() {
            return Math.min(Short.MAX_VALUE, map(this.f12807s.getMaximumValue()));
        }

        @Override // javax.swing.Spring
        public int getValue() {
            return map(this.f12807s.getValue());
        }

        @Override // javax.swing.Spring
        public void setValue(int i2) {
            if (i2 == Integer.MIN_VALUE) {
                this.f12807s.setValue(Integer.MIN_VALUE);
            } else {
                this.f12807s.setValue(inv(i2));
            }
        }

        @Override // javax.swing.Spring
        boolean isCyclic(SpringLayout springLayout) {
            return this.f12807s.isCyclic(springLayout);
        }
    }

    /* loaded from: rt.jar:javax/swing/Spring$CompoundSpring.class */
    static abstract class CompoundSpring extends StaticSpring {
        protected Spring s1;
        protected Spring s2;

        protected abstract int op(int i2, int i3);

        public CompoundSpring(Spring spring, Spring spring2) {
            super(Integer.MIN_VALUE);
            this.s1 = spring;
            this.s2 = spring2;
        }

        @Override // javax.swing.Spring.StaticSpring
        public String toString() {
            return "CompoundSpring of " + ((Object) this.s1) + " and " + ((Object) this.s2);
        }

        @Override // javax.swing.Spring.AbstractSpring
        protected void clear() {
            super.clear();
            this.max = Integer.MIN_VALUE;
            this.pref = Integer.MIN_VALUE;
            this.min = Integer.MIN_VALUE;
            this.s1.setValue(Integer.MIN_VALUE);
            this.s2.setValue(Integer.MIN_VALUE);
        }

        @Override // javax.swing.Spring.StaticSpring, javax.swing.Spring
        public int getMinimumValue() {
            if (this.min == Integer.MIN_VALUE) {
                this.min = op(this.s1.getMinimumValue(), this.s2.getMinimumValue());
            }
            return this.min;
        }

        @Override // javax.swing.Spring.StaticSpring, javax.swing.Spring
        public int getPreferredValue() {
            if (this.pref == Integer.MIN_VALUE) {
                this.pref = op(this.s1.getPreferredValue(), this.s2.getPreferredValue());
            }
            return this.pref;
        }

        @Override // javax.swing.Spring.StaticSpring, javax.swing.Spring
        public int getMaximumValue() {
            if (this.max == Integer.MIN_VALUE) {
                this.max = op(this.s1.getMaximumValue(), this.s2.getMaximumValue());
            }
            return this.max;
        }

        @Override // javax.swing.Spring.AbstractSpring, javax.swing.Spring
        public int getValue() {
            if (this.size == Integer.MIN_VALUE) {
                this.size = op(this.s1.getValue(), this.s2.getValue());
            }
            return this.size;
        }

        @Override // javax.swing.Spring
        boolean isCyclic(SpringLayout springLayout) {
            return springLayout.isCyclic(this.s1) || springLayout.isCyclic(this.s2);
        }
    }

    /* loaded from: rt.jar:javax/swing/Spring$SumSpring.class */
    private static class SumSpring extends CompoundSpring {
        public SumSpring(Spring spring, Spring spring2) {
            super(spring, spring2);
        }

        @Override // javax.swing.Spring.CompoundSpring
        protected int op(int i2, int i3) {
            return i2 + i3;
        }

        @Override // javax.swing.Spring.AbstractSpring
        protected void setNonClearValue(int i2) {
            super.setNonClearValue(i2);
            this.s1.setStrain(getStrain());
            this.s2.setValue(i2 - this.s1.getValue());
        }
    }

    /* loaded from: rt.jar:javax/swing/Spring$MaxSpring.class */
    private static class MaxSpring extends CompoundSpring {
        public MaxSpring(Spring spring, Spring spring2) {
            super(spring, spring2);
        }

        @Override // javax.swing.Spring.CompoundSpring
        protected int op(int i2, int i3) {
            return Math.max(i2, i3);
        }

        @Override // javax.swing.Spring.AbstractSpring
        protected void setNonClearValue(int i2) {
            super.setNonClearValue(i2);
            this.s1.setValue(i2);
            this.s2.setValue(i2);
        }
    }

    public static Spring constant(int i2) {
        return constant(i2, i2, i2);
    }

    public static Spring constant(int i2, int i3, int i4) {
        return new StaticSpring(i2, i3, i4);
    }

    public static Spring minus(Spring spring) {
        return new NegativeSpring(spring);
    }

    public static Spring sum(Spring spring, Spring spring2) {
        return new SumSpring(spring, spring2);
    }

    public static Spring max(Spring spring, Spring spring2) {
        return new MaxSpring(spring, spring2);
    }

    static Spring difference(Spring spring, Spring spring2) {
        return sum(spring, minus(spring2));
    }

    public static Spring scale(Spring spring, float f2) {
        checkArg(spring);
        return new ScaleSpring(f2);
    }

    public static Spring width(Component component) {
        checkArg(component);
        return new WidthSpring(component);
    }

    public static Spring height(Component component) {
        checkArg(component);
        return new HeightSpring(component);
    }

    private static void checkArg(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Argument must not be null");
        }
    }
}
