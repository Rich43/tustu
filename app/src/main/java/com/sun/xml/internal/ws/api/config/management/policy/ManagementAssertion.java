package com.sun.xml.internal.ws.api.config.management.policy;

import com.sun.istack.internal.logging.Logger;
import com.sun.org.glassfish.external.amx.AMX;
import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicyMapKey;
import com.sun.xml.internal.ws.policy.SimpleAssertion;
import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
import com.sun.xml.internal.ws.resources.ManagementMessages;
import java.util.Collection;
import java.util.Iterator;
import javafx.fxml.FXMLLoader;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/config/management/policy/ManagementAssertion.class */
public abstract class ManagementAssertion extends SimpleAssertion {
    protected static final QName MANAGEMENT_ATTRIBUTE_QNAME = new QName("management");
    protected static final QName MONITORING_ATTRIBUTE_QNAME = new QName(AMX.GROUP_MONITORING);
    private static final QName ID_ATTRIBUTE_QNAME = new QName("id");
    private static final QName START_ATTRIBUTE_QNAME = new QName("start");
    private static final Logger LOGGER = Logger.getLogger(ManagementAssertion.class);

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/config/management/policy/ManagementAssertion$Setting.class */
    public enum Setting {
        NOT_SET,
        OFF,
        ON
    }

    public abstract boolean isManagementEnabled();

    protected static <T extends ManagementAssertion> T getAssertion(QName name, PolicyMap policyMap, QName serviceName, QName portName, Class<T> type) throws WebServiceException, IllegalArgumentException {
        PolicyAssertion assertion = null;
        if (policyMap != null) {
            try {
                PolicyMapKey key = PolicyMap.createWsdlEndpointScopeKey(serviceName, portName);
                Policy policy = policyMap.getEndpointEffectivePolicy(key);
                if (policy != null) {
                    Iterator<AssertionSet> assertionSets = policy.iterator();
                    if (assertionSets.hasNext()) {
                        AssertionSet assertionSet = assertionSets.next();
                        Iterator<PolicyAssertion> assertions = assertionSet.get(name).iterator();
                        if (assertions.hasNext()) {
                            assertion = assertions.next();
                        }
                    }
                }
            } catch (PolicyException ex) {
                throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(ManagementMessages.WSM_1001_FAILED_ASSERTION(name), ex)));
            }
        }
        if (assertion == null) {
            return null;
        }
        return (T) assertion.getImplementation(type);
    }

    protected ManagementAssertion(QName name, AssertionData data, Collection<PolicyAssertion> assertionParameters) throws AssertionCreationException {
        super(data, assertionParameters);
        if (!name.equals(data.getName())) {
            throw ((AssertionCreationException) LOGGER.logSevereException(new AssertionCreationException(data, ManagementMessages.WSM_1002_EXPECTED_MANAGEMENT_ASSERTION(name))));
        }
        if (isManagementEnabled() && !data.containsAttribute(ID_ATTRIBUTE_QNAME)) {
            throw ((AssertionCreationException) LOGGER.logSevereException(new AssertionCreationException(data, ManagementMessages.WSM_1003_MANAGEMENT_ASSERTION_MISSING_ID(name))));
        }
    }

    public String getId() {
        return getAttributeValue(ID_ATTRIBUTE_QNAME);
    }

    public String getStart() {
        return getAttributeValue(START_ATTRIBUTE_QNAME);
    }

    public Setting monitoringAttribute() {
        String monitoring = getAttributeValue(MONITORING_ATTRIBUTE_QNAME);
        Setting result = Setting.NOT_SET;
        if (monitoring != null) {
            if (monitoring.trim().toLowerCase().equals(FXMLLoader.EVENT_HANDLER_PREFIX) || Boolean.parseBoolean(monitoring)) {
                result = Setting.ON;
            } else {
                result = Setting.OFF;
            }
        }
        return result;
    }
}
