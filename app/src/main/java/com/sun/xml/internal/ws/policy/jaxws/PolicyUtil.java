package com.sun.xml.internal.ws.policy.jaxws;

import com.sun.xml.internal.ws.addressing.policy.AddressingFeatureConfigurator;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
import com.sun.xml.internal.ws.encoding.policy.FastInfosetFeatureConfigurator;
import com.sun.xml.internal.ws.encoding.policy.MtomFeatureConfigurator;
import com.sun.xml.internal.ws.encoding.policy.SelectOptimalEncodingFeatureConfigurator;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicyMapKey;
import com.sun.xml.internal.ws.policy.jaxws.spi.PolicyFeatureConfigurator;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/PolicyUtil.class */
public class PolicyUtil {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyUtil.class);
    private static final Collection<PolicyFeatureConfigurator> CONFIGURATORS = new LinkedList();

    static {
        CONFIGURATORS.add(new AddressingFeatureConfigurator());
        CONFIGURATORS.add(new MtomFeatureConfigurator());
        CONFIGURATORS.add(new FastInfosetFeatureConfigurator());
        CONFIGURATORS.add(new SelectOptimalEncodingFeatureConfigurator());
        addServiceProviders(CONFIGURATORS, PolicyFeatureConfigurator.class);
    }

    public static <T> void addServiceProviders(Collection<T> providers, Class<T> service) {
        Iterator<T> foundProviders = ServiceFinder.find(service).iterator();
        while (foundProviders.hasNext()) {
            providers.add(foundProviders.next());
        }
    }

    public static void configureModel(WSDLModel model, PolicyMap policyMap) throws PolicyException {
        LOGGER.entering(model, policyMap);
        for (WSDLService service : model.getServices().values()) {
            for (WSDLPort port : service.getPorts()) {
                Collection<WebServiceFeature> features = getPortScopedFeatures(policyMap, service.getName(), port.getName());
                for (WebServiceFeature feature : features) {
                    port.addFeature(feature);
                    port.getBinding().addFeature(feature);
                }
            }
        }
        LOGGER.exiting();
    }

    public static Collection<WebServiceFeature> getPortScopedFeatures(PolicyMap policyMap, QName serviceName, QName portName) {
        LOGGER.entering(policyMap, serviceName, portName);
        Collection<WebServiceFeature> features = new ArrayList<>();
        try {
            PolicyMapKey key = PolicyMap.createWsdlEndpointScopeKey(serviceName, portName);
            for (PolicyFeatureConfigurator configurator : CONFIGURATORS) {
                Collection<WebServiceFeature> additionalFeatures = configurator.getFeatures(key, policyMap);
                if (additionalFeatures != null) {
                    features.addAll(additionalFeatures);
                }
            }
            LOGGER.exiting(features);
            return features;
        } catch (PolicyException e2) {
            throw new WebServiceException(e2);
        }
    }
}
