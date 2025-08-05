package com.sun.xml.internal.ws.api.config.management.policy;

import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
import com.sun.xml.internal.ws.resources.ManagementMessages;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/config/management/policy/ManagedServiceAssertion.class */
public class ManagedServiceAssertion extends ManagementAssertion {
    public static final QName MANAGED_SERVICE_QNAME = new QName(PolicyConstants.SUN_MANAGEMENT_NAMESPACE, "ManagedService");
    private static final QName COMMUNICATION_SERVER_IMPLEMENTATIONS_PARAMETER_QNAME = new QName(PolicyConstants.SUN_MANAGEMENT_NAMESPACE, "CommunicationServerImplementations");
    private static final QName COMMUNICATION_SERVER_IMPLEMENTATION_PARAMETER_QNAME = new QName(PolicyConstants.SUN_MANAGEMENT_NAMESPACE, "CommunicationServerImplementation");
    private static final QName CONFIGURATOR_IMPLEMENTATION_PARAMETER_QNAME = new QName(PolicyConstants.SUN_MANAGEMENT_NAMESPACE, "ConfiguratorImplementation");
    private static final QName CONFIG_SAVER_IMPLEMENTATION_PARAMETER_QNAME = new QName(PolicyConstants.SUN_MANAGEMENT_NAMESPACE, "ConfigSaverImplementation");
    private static final QName CONFIG_READER_IMPLEMENTATION_PARAMETER_QNAME = new QName(PolicyConstants.SUN_MANAGEMENT_NAMESPACE, "ConfigReaderImplementation");
    private static final QName CLASS_NAME_ATTRIBUTE_QNAME = new QName("className");
    private static final QName ENDPOINT_DISPOSE_DELAY_ATTRIBUTE_QNAME = new QName("endpointDisposeDelay");
    private static final Logger LOGGER = Logger.getLogger(ManagedServiceAssertion.class);

    public static ManagedServiceAssertion getAssertion(WSEndpoint endpoint) throws WebServiceException {
        LOGGER.entering(endpoint);
        PolicyMap policyMap = endpoint.getPolicyMap();
        ManagedServiceAssertion assertion = (ManagedServiceAssertion) ManagementAssertion.getAssertion(MANAGED_SERVICE_QNAME, policyMap, endpoint.getServiceName(), endpoint.getPortName(), ManagedServiceAssertion.class);
        LOGGER.exiting(assertion);
        return assertion;
    }

    public ManagedServiceAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters) throws AssertionCreationException {
        super(MANAGED_SERVICE_QNAME, data, assertionParameters);
    }

    @Override // com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion
    public boolean isManagementEnabled() {
        String management = getAttributeValue(MANAGEMENT_ATTRIBUTE_QNAME);
        boolean result = true;
        if (management != null) {
            if (management.trim().toLowerCase().equals(FXMLLoader.EVENT_HANDLER_PREFIX)) {
                result = true;
            } else {
                result = Boolean.parseBoolean(management);
            }
        }
        return result;
    }

    public long getEndpointDisposeDelay(long defaultDelay) throws WebServiceException {
        long result = defaultDelay;
        String delayText = getAttributeValue(ENDPOINT_DISPOSE_DELAY_ATTRIBUTE_QNAME);
        if (delayText != null) {
            try {
                result = Long.parseLong(delayText);
            } catch (NumberFormatException e2) {
                throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(ManagementMessages.WSM_1008_EXPECTED_INTEGER_DISPOSE_DELAY_VALUE(delayText), e2)));
            }
        }
        return result;
    }

    public Collection<ImplementationRecord> getCommunicationServerImplementations() {
        Collection<ImplementationRecord> result = new LinkedList<>();
        Iterator<PolicyAssertion> parameters = getParametersIterator();
        while (parameters.hasNext()) {
            PolicyAssertion parameter = parameters.next();
            if (COMMUNICATION_SERVER_IMPLEMENTATIONS_PARAMETER_QNAME.equals(parameter.getName())) {
                Iterator<PolicyAssertion> implementations = parameter.getParametersIterator();
                if (!implementations.hasNext()) {
                    throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(ManagementMessages.WSM_1005_EXPECTED_COMMUNICATION_CHILD())));
                }
                while (implementations.hasNext()) {
                    PolicyAssertion implementation = implementations.next();
                    if (COMMUNICATION_SERVER_IMPLEMENTATION_PARAMETER_QNAME.equals(implementation.getName())) {
                        result.add(getImplementation(implementation));
                    } else {
                        throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(ManagementMessages.WSM_1004_EXPECTED_XML_TAG(COMMUNICATION_SERVER_IMPLEMENTATION_PARAMETER_QNAME, implementation.getName()))));
                    }
                }
            }
        }
        return result;
    }

    public ImplementationRecord getConfiguratorImplementation() {
        return findImplementation(CONFIGURATOR_IMPLEMENTATION_PARAMETER_QNAME);
    }

    public ImplementationRecord getConfigSaverImplementation() {
        return findImplementation(CONFIG_SAVER_IMPLEMENTATION_PARAMETER_QNAME);
    }

    public ImplementationRecord getConfigReaderImplementation() {
        return findImplementation(CONFIG_READER_IMPLEMENTATION_PARAMETER_QNAME);
    }

    private ImplementationRecord findImplementation(QName implementationName) {
        Iterator<PolicyAssertion> parameters = getParametersIterator();
        while (parameters.hasNext()) {
            PolicyAssertion parameter = parameters.next();
            if (implementationName.equals(parameter.getName())) {
                return getImplementation(parameter);
            }
        }
        return null;
    }

    private ImplementationRecord getImplementation(PolicyAssertion rootParameter) {
        String className = rootParameter.getAttributeValue(CLASS_NAME_ATTRIBUTE_QNAME);
        HashMap<QName, String> parameterMap = new HashMap<>();
        Iterator<PolicyAssertion> implementationParameters = rootParameter.getParametersIterator();
        Collection<NestedParameters> nestedParameters = new LinkedList<>();
        while (implementationParameters.hasNext()) {
            PolicyAssertion parameterAssertion = implementationParameters.next();
            QName parameterName = parameterAssertion.getName();
            if (parameterAssertion.hasParameters()) {
                Map<QName, String> nestedParameterMap = new HashMap<>();
                Iterator<PolicyAssertion> parameters = parameterAssertion.getParametersIterator();
                while (parameters.hasNext()) {
                    PolicyAssertion parameter = parameters.next();
                    String value = parameter.getValue();
                    if (value != null) {
                        value = value.trim();
                    }
                    nestedParameterMap.put(parameter.getName(), value);
                }
                nestedParameters.add(new NestedParameters(parameterName, nestedParameterMap));
            } else {
                String value2 = parameterAssertion.getValue();
                if (value2 != null) {
                    value2 = value2.trim();
                }
                parameterMap.put(parameterName, value2);
            }
        }
        return new ImplementationRecord(className, parameterMap, nestedParameters);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/config/management/policy/ManagedServiceAssertion$ImplementationRecord.class */
    public static class ImplementationRecord {
        private final String implementation;
        private final Map<QName, String> parameters;
        private final Collection<NestedParameters> nestedParameters;

        protected ImplementationRecord(String implementation, Map<QName, String> parameters, Collection<NestedParameters> nestedParameters) {
            this.implementation = implementation;
            this.parameters = parameters;
            this.nestedParameters = nestedParameters;
        }

        public String getImplementation() {
            return this.implementation;
        }

        public Map<QName, String> getParameters() {
            return this.parameters;
        }

        public Collection<NestedParameters> getNestedParameters() {
            return this.nestedParameters;
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ImplementationRecord other = (ImplementationRecord) obj;
            if (this.implementation == null) {
                if (other.implementation != null) {
                    return false;
                }
            } else if (!this.implementation.equals(other.implementation)) {
                return false;
            }
            if (this.parameters != other.parameters && (this.parameters == null || !this.parameters.equals(other.parameters))) {
                return false;
            }
            if (this.nestedParameters != other.nestedParameters) {
                if (this.nestedParameters == null || !this.nestedParameters.equals(other.nestedParameters)) {
                    return false;
                }
                return true;
            }
            return true;
        }

        public int hashCode() {
            int hash = (53 * 3) + (this.implementation != null ? this.implementation.hashCode() : 0);
            return (53 * ((53 * hash) + (this.parameters != null ? this.parameters.hashCode() : 0))) + (this.nestedParameters != null ? this.nestedParameters.hashCode() : 0);
        }

        public String toString() {
            StringBuilder text = new StringBuilder("ImplementationRecord: ");
            text.append("implementation = \"").append(this.implementation).append("\", ");
            text.append("parameters = \"").append((Object) this.parameters).append("\", ");
            text.append("nested parameters = \"").append((Object) this.nestedParameters).append(PdfOps.DOUBLE_QUOTE__TOKEN);
            return text.toString();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/config/management/policy/ManagedServiceAssertion$NestedParameters.class */
    public static class NestedParameters {
        private final QName name;
        private final Map<QName, String> parameters;

        private NestedParameters(QName name, Map<QName, String> parameters) {
            this.name = name;
            this.parameters = parameters;
        }

        public QName getName() {
            return this.name;
        }

        public Map<QName, String> getParameters() {
            return this.parameters;
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            NestedParameters other = (NestedParameters) obj;
            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!this.name.equals(other.name)) {
                return false;
            }
            if (this.parameters == other.parameters) {
                return true;
            }
            if (this.parameters == null || !this.parameters.equals(other.parameters)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int hash = (59 * 5) + (this.name != null ? this.name.hashCode() : 0);
            return (59 * hash) + (this.parameters != null ? this.parameters.hashCode() : 0);
        }

        public String toString() {
            StringBuilder text = new StringBuilder("NestedParameters: ");
            text.append("name = \"").append((Object) this.name).append("\", ");
            text.append("parameters = \"").append((Object) this.parameters).append(PdfOps.DOUBLE_QUOTE__TOKEN);
            return text.toString();
        }
    }
}
