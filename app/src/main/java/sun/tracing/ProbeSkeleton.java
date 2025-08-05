package sun.tracing;

import com.sun.tracing.Probe;

/* loaded from: rt.jar:sun/tracing/ProbeSkeleton.class */
public abstract class ProbeSkeleton implements Probe {
    protected Class<?>[] parameters;

    @Override // com.sun.tracing.Probe
    public abstract boolean isEnabled();

    public abstract void uncheckedTrigger(Object[] objArr);

    protected ProbeSkeleton(Class<?>[] clsArr) {
        this.parameters = clsArr;
    }

    private static boolean isAssignable(Object obj, Class<?> cls) {
        if (obj != null && !cls.isInstance(obj)) {
            if (cls.isPrimitive()) {
                try {
                    return cls.isAssignableFrom((Class) obj.getClass().getField("TYPE").get(null));
                } catch (Exception e2) {
                    return false;
                }
            }
            return false;
        }
        return true;
    }

    @Override // com.sun.tracing.Probe
    public void trigger(Object... objArr) {
        if (objArr.length != this.parameters.length) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
        for (int i2 = 0; i2 < this.parameters.length; i2++) {
            if (!isAssignable(objArr[i2], this.parameters[i2])) {
                throw new IllegalArgumentException("Wrong type of argument at position " + i2);
            }
        }
        uncheckedTrigger(objArr);
    }
}
