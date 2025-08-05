package jdk.internal.dynalink.support;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;

/* loaded from: nashorn.jar:jdk/internal/dynalink/support/CompositeGuardingDynamicLinker.class */
public class CompositeGuardingDynamicLinker implements GuardingDynamicLinker, Serializable {
    private static final long serialVersionUID = 1;
    private final GuardingDynamicLinker[] linkers;

    public CompositeGuardingDynamicLinker(Iterable<? extends GuardingDynamicLinker> linkers) {
        List<GuardingDynamicLinker> l2 = new LinkedList<>();
        for (GuardingDynamicLinker linker : linkers) {
            l2.add(linker);
        }
        this.linkers = (GuardingDynamicLinker[]) l2.toArray(new GuardingDynamicLinker[l2.size()]);
    }

    @Override // jdk.internal.dynalink.linker.GuardingDynamicLinker
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        for (GuardingDynamicLinker linker : this.linkers) {
            GuardedInvocation invocation = linker.getGuardedInvocation(linkRequest, linkerServices);
            if (invocation != null) {
                return invocation;
            }
        }
        return null;
    }
}
