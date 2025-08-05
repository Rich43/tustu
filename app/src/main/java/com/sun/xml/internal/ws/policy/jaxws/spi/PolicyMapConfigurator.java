package com.sun.xml.internal.ws.policy.jaxws.spi;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicySubject;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/spi/PolicyMapConfigurator.class */
public interface PolicyMapConfigurator {
    Collection<PolicySubject> update(PolicyMap policyMap, SEIModel sEIModel, WSBinding wSBinding) throws PolicyException;
}
