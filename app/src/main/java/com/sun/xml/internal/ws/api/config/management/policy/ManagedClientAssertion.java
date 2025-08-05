package com.sun.xml.internal.ws.api.config.management.policy;

import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
import com.sun.xml.internal.ws.resources.ManagementMessages;
import java.util.Collection;
import javafx.fxml.FXMLLoader;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/config/management/policy/ManagedClientAssertion.class */
public class ManagedClientAssertion extends ManagementAssertion {
    public static final QName MANAGED_CLIENT_QNAME = new QName(PolicyConstants.SUN_MANAGEMENT_NAMESPACE, "ManagedClient");
    private static final Logger LOGGER = Logger.getLogger(ManagedClientAssertion.class);

    public static ManagedClientAssertion getAssertion(WSPortInfo portInfo) throws WebServiceException {
        if (portInfo == null) {
            return null;
        }
        LOGGER.entering(portInfo);
        PolicyMap policyMap = portInfo.getPolicyMap();
        ManagedClientAssertion assertion = (ManagedClientAssertion) ManagementAssertion.getAssertion(MANAGED_CLIENT_QNAME, policyMap, portInfo.getServiceName(), portInfo.getPortName(), ManagedClientAssertion.class);
        LOGGER.exiting(assertion);
        return assertion;
    }

    public ManagedClientAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters) throws AssertionCreationException {
        super(MANAGED_CLIENT_QNAME, data, assertionParameters);
    }

    @Override // com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion
    public boolean isManagementEnabled() {
        String management = getAttributeValue(MANAGEMENT_ATTRIBUTE_QNAME);
        if (management != null) {
            if (management.trim().toLowerCase().equals(FXMLLoader.EVENT_HANDLER_PREFIX) || Boolean.parseBoolean(management)) {
                LOGGER.warning(ManagementMessages.WSM_1006_CLIENT_MANAGEMENT_ENABLED());
                return false;
            }
            return false;
        }
        return false;
    }
}
