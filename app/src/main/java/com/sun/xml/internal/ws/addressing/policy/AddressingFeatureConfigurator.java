package com.sun.xml.internal.ws.addressing.policy;

import com.sun.xml.internal.bind.util.Which;
import com.sun.xml.internal.ws.addressing.W3CAddressingConstants;
import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.NestedPolicy;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicyMapKey;
import com.sun.xml.internal.ws.policy.jaxws.spi.PolicyFeatureConfigurator;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.resources.ModelerMessages;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.AddressingFeature;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/policy/AddressingFeatureConfigurator.class */
public class AddressingFeatureConfigurator implements PolicyFeatureConfigurator {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) AddressingFeatureConfigurator.class);
    private static final QName[] ADDRESSING_ASSERTIONS = {new QName(AddressingVersion.MEMBER.policyNsUri, W3CAddressingConstants.WSAW_USING_ADDRESSING_NAME)};

    @Override // com.sun.xml.internal.ws.policy.jaxws.spi.PolicyFeatureConfigurator
    public Collection<WebServiceFeature> getFeatures(PolicyMapKey key, PolicyMap policyMap) throws PolicyException {
        WebServiceFeature feature;
        LOGGER.entering(key, policyMap);
        Collection<WebServiceFeature> features = new LinkedList<>();
        if (key != null && policyMap != null) {
            Policy policy = policyMap.getEndpointEffectivePolicy(key);
            for (QName addressingAssertionQName : ADDRESSING_ASSERTIONS) {
                if (policy != null && policy.contains(addressingAssertionQName)) {
                    Iterator<AssertionSet> assertions = policy.iterator();
                    while (assertions.hasNext()) {
                        AssertionSet assertionSet = assertions.next();
                        Iterator<PolicyAssertion> policyAssertion = assertionSet.iterator();
                        while (policyAssertion.hasNext()) {
                            PolicyAssertion assertion = policyAssertion.next();
                            if (assertion.getName().equals(addressingAssertionQName)) {
                                WebServiceFeature feature2 = AddressingVersion.getFeature(addressingAssertionQName.getNamespaceURI(), true, !assertion.isOptional());
                                if (LOGGER.isLoggable(Level.FINE)) {
                                    LOGGER.fine("Added addressing feature \"" + ((Object) feature2) + "\" for element \"" + ((Object) key) + PdfOps.DOUBLE_QUOTE__TOKEN);
                                }
                                features.add(feature2);
                            }
                        }
                    }
                }
            }
            if (policy != null && policy.contains(W3CAddressingMetadataConstants.WSAM_ADDRESSING_ASSERTION)) {
                Iterator<AssertionSet> it = policy.iterator();
                while (it.hasNext()) {
                    Iterator<PolicyAssertion> it2 = it.next().iterator();
                    while (it2.hasNext()) {
                        PolicyAssertion assertion2 = it2.next();
                        if (assertion2.getName().equals(W3CAddressingMetadataConstants.WSAM_ADDRESSING_ASSERTION)) {
                            NestedPolicy nestedPolicy = assertion2.getNestedPolicy();
                            boolean requiresAnonymousResponses = false;
                            boolean requiresNonAnonymousResponses = false;
                            if (nestedPolicy != null) {
                                requiresAnonymousResponses = nestedPolicy.contains(W3CAddressingMetadataConstants.WSAM_ANONYMOUS_NESTED_ASSERTION);
                                requiresNonAnonymousResponses = nestedPolicy.contains(W3CAddressingMetadataConstants.WSAM_NONANONYMOUS_NESTED_ASSERTION);
                            }
                            if (requiresAnonymousResponses && requiresNonAnonymousResponses) {
                                throw new WebServiceException("Only one among AnonymousResponses and NonAnonymousResponses can be nested in an Addressing assertion");
                            }
                            if (requiresAnonymousResponses) {
                                try {
                                    feature = new AddressingFeature(true, !assertion2.isOptional(), AddressingFeature.Responses.ANONYMOUS);
                                } catch (NoSuchMethodError e2) {
                                    throw ((PolicyException) LOGGER.logSevereException(new PolicyException(ModelerMessages.RUNTIME_MODELER_ADDRESSING_RESPONSES_NOSUCHMETHOD(toJar(Which.which(AddressingFeature.class))), e2)));
                                }
                            } else if (requiresNonAnonymousResponses) {
                                feature = new AddressingFeature(true, !assertion2.isOptional(), AddressingFeature.Responses.NON_ANONYMOUS);
                            } else {
                                feature = new AddressingFeature(true, !assertion2.isOptional());
                            }
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.fine("Added addressing feature \"" + ((Object) feature) + "\" for element \"" + ((Object) key) + PdfOps.DOUBLE_QUOTE__TOKEN);
                            }
                            features.add(feature);
                        }
                    }
                }
            }
        }
        LOGGER.exiting(features);
        return features;
    }

    private static String toJar(String url) {
        if (!url.startsWith("jar:")) {
            return url;
        }
        String url2 = url.substring(4);
        return url2.substring(0, url2.lastIndexOf(33));
    }
}
