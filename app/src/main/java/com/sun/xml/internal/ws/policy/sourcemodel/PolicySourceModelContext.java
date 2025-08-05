package com.sun.xml.internal.ws.policy.sourcemodel;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicySourceModelContext.class */
public final class PolicySourceModelContext {
    Map<URI, PolicySourceModel> policyModels;

    private PolicySourceModelContext() {
    }

    private Map<URI, PolicySourceModel> getModels() {
        if (null == this.policyModels) {
            this.policyModels = new HashMap();
        }
        return this.policyModels;
    }

    public void addModel(URI modelUri, PolicySourceModel model) {
        getModels().put(modelUri, model);
    }

    public static PolicySourceModelContext createContext() {
        return new PolicySourceModelContext();
    }

    public boolean containsModel(URI modelUri) {
        return getModels().containsKey(modelUri);
    }

    PolicySourceModel retrieveModel(URI modelUri) {
        return getModels().get(modelUri);
    }

    PolicySourceModel retrieveModel(URI modelUri, URI digestAlgorithm, String digest) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return "PolicySourceModelContext: policyModels = " + ((Object) this.policyModels);
    }
}
