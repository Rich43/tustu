package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyMapMutator.class */
public abstract class PolicyMapMutator {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyMapMutator.class);
    private PolicyMap map = null;

    PolicyMapMutator() {
    }

    public void connect(PolicyMap map) {
        if (isConnected()) {
            throw ((IllegalStateException) LOGGER.logSevereException(new IllegalStateException(LocalizationMessages.WSP_0044_POLICY_MAP_MUTATOR_ALREADY_CONNECTED())));
        }
        this.map = map;
    }

    public PolicyMap getMap() {
        return this.map;
    }

    public void disconnect() {
        this.map = null;
    }

    public boolean isConnected() {
        return this.map != null;
    }
}
