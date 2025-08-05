package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.PolicyException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyModelUnmarshaller.class */
public abstract class PolicyModelUnmarshaller {
    private static final PolicyModelUnmarshaller xmlUnmarshaller = new XmlPolicyModelUnmarshaller();

    public abstract PolicySourceModel unmarshalModel(Object obj) throws PolicyException;

    PolicyModelUnmarshaller() {
    }

    public static PolicyModelUnmarshaller getXmlUnmarshaller() {
        return xmlUnmarshaller;
    }
}
