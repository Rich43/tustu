package com.sun.xml.internal.ws.api.policy;

import com.sun.xml.internal.ws.policy.jaxws.DefaultPolicyResolver;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/policy/PolicyResolverFactory.class */
public abstract class PolicyResolverFactory {
    public static final PolicyResolver DEFAULT_POLICY_RESOLVER = new DefaultPolicyResolver();

    public abstract PolicyResolver doCreate();

    public static PolicyResolver create() {
        Iterator it = ServiceFinder.find(PolicyResolverFactory.class).iterator();
        while (it.hasNext()) {
            PolicyResolverFactory factory = (PolicyResolverFactory) it.next();
            PolicyResolver policyResolver = factory.doCreate();
            if (policyResolver != null) {
                return policyResolver;
            }
        }
        return DEFAULT_POLICY_RESOLVER;
    }
}
