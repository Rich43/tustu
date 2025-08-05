package com.sun.scenario.effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/FilterContext.class */
public class FilterContext {
    private Object referent;

    protected FilterContext(Object referent) {
        if (referent == null) {
            throw new IllegalArgumentException("Referent must be non-null");
        }
        this.referent = referent;
    }

    public final Object getReferent() {
        return this.referent;
    }

    public int hashCode() {
        return this.referent.hashCode();
    }

    public boolean equals(Object o2) {
        if (!(o2 instanceof FilterContext)) {
            return false;
        }
        FilterContext that = (FilterContext) o2;
        return this.referent.equals(that.referent);
    }
}
