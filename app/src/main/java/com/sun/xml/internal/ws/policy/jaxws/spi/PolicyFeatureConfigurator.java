package com.sun.xml.internal.ws.policy.jaxws.spi;

import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicyMapKey;
import java.util.Collection;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/spi/PolicyFeatureConfigurator.class */
public interface PolicyFeatureConfigurator {
    Collection<WebServiceFeature> getFeatures(PolicyMapKey policyMapKey, PolicyMap policyMap) throws PolicyException;
}
