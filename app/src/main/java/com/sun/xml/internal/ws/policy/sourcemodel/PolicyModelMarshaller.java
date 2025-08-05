package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.PolicyException;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyModelMarshaller.class */
public abstract class PolicyModelMarshaller {
    private static final PolicyModelMarshaller defaultXmlMarshaller = new XmlPolicyModelMarshaller(false);
    private static final PolicyModelMarshaller invisibleAssertionXmlMarshaller = new XmlPolicyModelMarshaller(true);

    public abstract void marshal(PolicySourceModel policySourceModel, Object obj) throws PolicyException;

    public abstract void marshal(Collection<PolicySourceModel> collection, Object obj) throws PolicyException;

    PolicyModelMarshaller() {
    }

    public static PolicyModelMarshaller getXmlMarshaller(boolean marshallInvisible) {
        return marshallInvisible ? invisibleAssertionXmlMarshaller : defaultXmlMarshaller;
    }
}
