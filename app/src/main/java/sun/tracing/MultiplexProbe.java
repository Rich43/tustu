package sun.tracing;

import com.sun.tracing.Probe;
import com.sun.tracing.Provider;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* compiled from: MultiplexProviderFactory.java */
/* loaded from: rt.jar:sun/tracing/MultiplexProbe.class */
class MultiplexProbe extends ProbeSkeleton {
    private Set<Probe> probes;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MultiplexProbe.class.desiredAssertionStatus();
    }

    MultiplexProbe(Method method, Set<Provider> set) {
        super(method.getParameterTypes());
        this.probes = new HashSet();
        Iterator<Provider> it = set.iterator();
        while (it.hasNext()) {
            Probe probe = it.next().getProbe(method);
            if (probe != null) {
                this.probes.add(probe);
            }
        }
    }

    @Override // sun.tracing.ProbeSkeleton, com.sun.tracing.Probe
    public boolean isEnabled() {
        Iterator<Probe> it = this.probes.iterator();
        while (it.hasNext()) {
            if (it.next().isEnabled()) {
                return true;
            }
        }
        return false;
    }

    @Override // sun.tracing.ProbeSkeleton
    public void uncheckedTrigger(Object[] objArr) {
        for (Probe probe : this.probes) {
            try {
                ((ProbeSkeleton) probe).uncheckedTrigger(objArr);
            } catch (ClassCastException e2) {
                try {
                    Probe.class.getMethod("trigger", Class.forName("[java.lang.Object")).invoke(probe, objArr);
                } catch (Exception e3) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                }
            }
        }
    }
}
