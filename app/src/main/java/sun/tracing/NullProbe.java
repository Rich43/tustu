package sun.tracing;

/* compiled from: NullProviderFactory.java */
/* loaded from: rt.jar:sun/tracing/NullProbe.class */
class NullProbe extends ProbeSkeleton {
    public NullProbe(Class<?>[] clsArr) {
        super(clsArr);
    }

    @Override // sun.tracing.ProbeSkeleton, com.sun.tracing.Probe
    public boolean isEnabled() {
        return false;
    }

    @Override // sun.tracing.ProbeSkeleton
    public void uncheckedTrigger(Object[] objArr) {
    }
}
