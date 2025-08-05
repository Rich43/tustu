package com.sun.xml.internal.ws.addressing.policy;

import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicySubject;
import com.sun.xml.internal.ws.policy.jaxws.spi.PolicyMapConfigurator;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
import com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.ws.soap.AddressingFeature;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/policy/AddressingPolicyMapConfigurator.class */
public class AddressingPolicyMapConfigurator implements PolicyMapConfigurator {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) AddressingPolicyMapConfigurator.class);

    /* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/policy/AddressingPolicyMapConfigurator$AddressingAssertion.class */
    private static final class AddressingAssertion extends PolicyAssertion {
        AddressingAssertion(AssertionData assertionData, AssertionSet nestedAlternative) {
            super(assertionData, null, nestedAlternative);
        }

        AddressingAssertion(AssertionData assertionData) {
            super(assertionData, null, null);
        }
    }

    @Override // com.sun.xml.internal.ws.policy.jaxws.spi.PolicyMapConfigurator
    public Collection<PolicySubject> update(PolicyMap policyMap, SEIModel model, WSBinding wsBinding) throws IllegalArgumentException, PolicyException {
        LOGGER.entering(policyMap, model, wsBinding);
        Collection<PolicySubject> subjects = new ArrayList<>();
        if (policyMap != null) {
            AddressingFeature addressingFeature = (AddressingFeature) wsBinding.getFeature(AddressingFeature.class);
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("addressingFeature = " + ((Object) addressingFeature));
            }
            if (addressingFeature != null && addressingFeature.isEnabled()) {
                addWsamAddressing(subjects, policyMap, model, addressingFeature);
            }
        }
        LOGGER.exiting(subjects);
        return subjects;
    }

    private void addWsamAddressing(Collection<PolicySubject> subjects, PolicyMap policyMap, SEIModel model, AddressingFeature addressingFeature) throws IllegalArgumentException, PolicyException {
        QName bindingName = model.getBoundPortTypeName();
        WsdlBindingSubject wsdlSubject = WsdlBindingSubject.createBindingSubject(bindingName);
        Policy addressingPolicy = createWsamAddressingPolicy(bindingName, addressingFeature);
        PolicySubject addressingPolicySubject = new PolicySubject(wsdlSubject, addressingPolicy);
        subjects.add(addressingPolicySubject);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Added addressing policy with ID \"" + addressingPolicy.getIdOrName() + "\" to binding element \"" + ((Object) bindingName) + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
    }

    private Policy createWsamAddressingPolicy(QName bindingName, AddressingFeature af2) throws IllegalArgumentException {
        ArrayList<AssertionSet> assertionSets = new ArrayList<>(1);
        ArrayList<PolicyAssertion> assertions = new ArrayList<>(1);
        AssertionData addressingData = AssertionData.createAssertionData(W3CAddressingMetadataConstants.WSAM_ADDRESSING_ASSERTION);
        if (!af2.isRequired()) {
            addressingData.setOptionalAttribute(true);
        }
        try {
            AddressingFeature.Responses responses = af2.getResponses();
            if (responses == AddressingFeature.Responses.ANONYMOUS) {
                AssertionData nestedAsserData = AssertionData.createAssertionData(W3CAddressingMetadataConstants.WSAM_ANONYMOUS_NESTED_ASSERTION);
                PolicyAssertion nestedAsser = new AddressingAssertion(nestedAsserData, null);
                assertions.add(new AddressingAssertion(addressingData, AssertionSet.createAssertionSet(Collections.singleton(nestedAsser))));
            } else if (responses == AddressingFeature.Responses.NON_ANONYMOUS) {
                AssertionData nestedAsserData2 = AssertionData.createAssertionData(W3CAddressingMetadataConstants.WSAM_NONANONYMOUS_NESTED_ASSERTION);
                PolicyAssertion nestedAsser2 = new AddressingAssertion(nestedAsserData2, null);
                assertions.add(new AddressingAssertion(addressingData, AssertionSet.createAssertionSet(Collections.singleton(nestedAsser2))));
            } else {
                assertions.add(new AddressingAssertion(addressingData, AssertionSet.createAssertionSet(null)));
            }
        } catch (NoSuchMethodError e2) {
            assertions.add(new AddressingAssertion(addressingData, AssertionSet.createAssertionSet(null)));
        }
        assertionSets.add(AssertionSet.createAssertionSet(assertions));
        return Policy.createPolicy(null, bindingName.getLocalPart() + "_WSAM_Addressing_Policy", assertionSets);
    }
}
