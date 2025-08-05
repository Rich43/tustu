package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode;
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionCreator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyModelTranslator.class */
public class PolicyModelTranslator {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyModelTranslator.class);
    private static final PolicyAssertionCreator defaultCreator = new DefaultPolicyAssertionCreator();
    private final Map<String, PolicyAssertionCreator> assertionCreators;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyModelTranslator$ContentDecomposition.class */
    private static final class ContentDecomposition {
        final List<Collection<ModelNode>> exactlyOneContents;
        final List<ModelNode> assertions;

        private ContentDecomposition() {
            this.exactlyOneContents = new LinkedList();
            this.assertions = new LinkedList();
        }

        void reset() {
            this.exactlyOneContents.clear();
            this.assertions.clear();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyModelTranslator$RawAssertion.class */
    private static final class RawAssertion {
        ModelNode originalNode;
        Collection<RawAlternative> nestedAlternatives = null;
        final Collection<ModelNode> parameters;

        RawAssertion(ModelNode originalNode, Collection<ModelNode> parameters) {
            this.parameters = parameters;
            this.originalNode = originalNode;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyModelTranslator$RawAlternative.class */
    private static final class RawAlternative {
        private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) RawAlternative.class);
        final List<RawPolicy> allNestedPolicies = new LinkedList();
        final Collection<RawAssertion> nestedAssertions = new LinkedList();

        RawAlternative(Collection<ModelNode> assertionNodes) throws PolicyException {
            RawPolicy rawPolicy;
            for (ModelNode node : assertionNodes) {
                RawAssertion assertion = new RawAssertion(node, new LinkedList());
                this.nestedAssertions.add(assertion);
                for (ModelNode assertionNodeChild : assertion.originalNode.getChildren()) {
                    switch (assertionNodeChild.getType()) {
                        case ASSERTION_PARAMETER_NODE:
                            assertion.parameters.add(assertionNodeChild);
                            break;
                        case POLICY:
                        case POLICY_REFERENCE:
                            if (assertion.nestedAlternatives == null) {
                                assertion.nestedAlternatives = new LinkedList();
                                if (assertionNodeChild.getType() == ModelNode.Type.POLICY) {
                                    rawPolicy = new RawPolicy(assertionNodeChild, assertion.nestedAlternatives);
                                } else {
                                    rawPolicy = new RawPolicy(PolicyModelTranslator.getReferencedModelRootNode(assertionNodeChild), assertion.nestedAlternatives);
                                }
                                RawPolicy nestedPolicy = rawPolicy;
                                this.allNestedPolicies.add(nestedPolicy);
                                break;
                            } else {
                                throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0006_UNEXPECTED_MULTIPLE_POLICY_NODES())));
                            }
                        default:
                            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0008_UNEXPECTED_CHILD_MODEL_TYPE(assertionNodeChild.getType()))));
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyModelTranslator$RawPolicy.class */
    private static final class RawPolicy {
        final Collection<ModelNode> originalContent;
        final Collection<RawAlternative> alternatives;

        RawPolicy(ModelNode policyNode, Collection<RawAlternative> alternatives) {
            this.originalContent = policyNode.getChildren();
            this.alternatives = alternatives;
        }
    }

    private PolicyModelTranslator() throws PolicyException {
        this(null);
    }

    protected PolicyModelTranslator(Collection<PolicyAssertionCreator> creators) throws PolicyException {
        LOGGER.entering(creators);
        Collection<PolicyAssertionCreator> allCreators = new LinkedList<>();
        PolicyAssertionCreator[] discoveredCreators = (PolicyAssertionCreator[]) PolicyUtils.ServiceProvider.load(PolicyAssertionCreator.class);
        for (PolicyAssertionCreator policyAssertionCreator : discoveredCreators) {
            allCreators.add(policyAssertionCreator);
        }
        if (creators != null) {
            Iterator<PolicyAssertionCreator> it = creators.iterator();
            while (it.hasNext()) {
                allCreators.add(it.next());
            }
        }
        Map<String, PolicyAssertionCreator> pacMap = new HashMap<>();
        for (PolicyAssertionCreator creator : allCreators) {
            String[] supportedURIs = creator.getSupportedDomainNamespaceURIs();
            String creatorClassName = creator.getClass().getName();
            if (supportedURIs == null || supportedURIs.length == 0) {
                LOGGER.warning(LocalizationMessages.WSP_0077_ASSERTION_CREATOR_DOES_NOT_SUPPORT_ANY_URI(creatorClassName));
            } else {
                for (String supportedURI : supportedURIs) {
                    LOGGER.config(LocalizationMessages.WSP_0078_ASSERTION_CREATOR_DISCOVERED(creatorClassName, supportedURI));
                    if (supportedURI == null || supportedURI.length() == 0) {
                        throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0070_ERROR_REGISTERING_ASSERTION_CREATOR(creatorClassName))));
                    }
                    PolicyAssertionCreator oldCreator = pacMap.put(supportedURI, creator);
                    if (oldCreator != null) {
                        throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0071_ERROR_MULTIPLE_ASSERTION_CREATORS_FOR_NAMESPACE(supportedURI, oldCreator.getClass().getName(), creator.getClass().getName()))));
                    }
                }
            }
        }
        this.assertionCreators = Collections.unmodifiableMap(pacMap);
        LOGGER.exiting();
    }

    public static PolicyModelTranslator getTranslator() throws PolicyException {
        return new PolicyModelTranslator();
    }

    public Policy translate(PolicySourceModel model) throws PolicyException {
        Policy policy;
        LOGGER.entering(model);
        if (model == null) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0043_POLICY_MODEL_TRANSLATION_ERROR_INPUT_PARAM_NULL())));
        }
        try {
            PolicySourceModel localPolicyModelCopy = model.m2553clone();
            String policyId = localPolicyModelCopy.getPolicyId();
            String policyName = localPolicyModelCopy.getPolicyName();
            Collection<AssertionSet> alternatives = createPolicyAlternatives(localPolicyModelCopy);
            LOGGER.finest(LocalizationMessages.WSP_0052_NUMBER_OF_ALTERNATIVE_COMBINATIONS_CREATED(Integer.valueOf(alternatives.size())));
            if (alternatives.size() == 0) {
                policy = Policy.createNullPolicy(model.getNamespaceVersion(), policyName, policyId);
                LOGGER.finest(LocalizationMessages.WSP_0055_NO_ALTERNATIVE_COMBINATIONS_CREATED());
            } else if (alternatives.size() == 1 && alternatives.iterator().next().isEmpty()) {
                policy = Policy.createEmptyPolicy(model.getNamespaceVersion(), policyName, policyId);
                LOGGER.finest(LocalizationMessages.WSP_0026_SINGLE_EMPTY_ALTERNATIVE_COMBINATION_CREATED());
            } else {
                policy = Policy.createPolicy(model.getNamespaceVersion(), policyName, policyId, alternatives);
                LOGGER.finest(LocalizationMessages.WSP_0057_N_ALTERNATIVE_COMBINATIONS_M_POLICY_ALTERNATIVES_CREATED(Integer.valueOf(alternatives.size()), Integer.valueOf(policy.getNumberOfAssertionSets())));
            }
            LOGGER.exiting(policy);
            return policy;
        } catch (CloneNotSupportedException e2) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0016_UNABLE_TO_CLONE_POLICY_SOURCE_MODEL(), e2)));
        }
    }

    private Collection<AssertionSet> createPolicyAlternatives(PolicySourceModel model) throws PolicyException {
        Collection<ModelNode> collectionPoll;
        RawPolicy rawPolicyPoll;
        ContentDecomposition decomposition = new ContentDecomposition();
        Queue<RawPolicy> policyQueue = new LinkedList<>();
        Queue<Collection<ModelNode>> contentQueue = new LinkedList<>();
        RawPolicy rootPolicy = new RawPolicy(model.getRootNode(), new LinkedList());
        RawPolicy processedPolicy = rootPolicy;
        do {
            Collection<ModelNode> processedContent = processedPolicy.originalContent;
            do {
                decompose(processedContent, decomposition);
                if (decomposition.exactlyOneContents.isEmpty()) {
                    RawAlternative alternative = new RawAlternative(decomposition.assertions);
                    processedPolicy.alternatives.add(alternative);
                    if (!alternative.allNestedPolicies.isEmpty()) {
                        policyQueue.addAll(alternative.allNestedPolicies);
                    }
                } else {
                    Collection<Collection<ModelNode>> combinations = PolicyUtils.Collections.combine(decomposition.assertions, decomposition.exactlyOneContents, false);
                    if (combinations != null && !combinations.isEmpty()) {
                        contentQueue.addAll(combinations);
                    }
                }
                collectionPoll = contentQueue.poll();
                processedContent = collectionPoll;
            } while (collectionPoll != null);
            rawPolicyPoll = policyQueue.poll();
            processedPolicy = rawPolicyPoll;
        } while (rawPolicyPoll != null);
        Collection<AssertionSet> assertionSets = new LinkedList<>();
        for (RawAlternative rootAlternative : rootPolicy.alternatives) {
            Collection<AssertionSet> normalizedAlternatives = normalizeRawAlternative(rootAlternative);
            assertionSets.addAll(normalizedAlternatives);
        }
        return assertionSets;
    }

    private void decompose(Collection<ModelNode> content, ContentDecomposition decomposition) throws PolicyException {
        decomposition.reset();
        Queue<ModelNode> allContentQueue = new LinkedList<>(content);
        while (true) {
            ModelNode node = allContentQueue.poll();
            if (node != null) {
                switch (node.getType()) {
                    case POLICY:
                    case ALL:
                        allContentQueue.addAll(node.getChildren());
                        break;
                    case POLICY_REFERENCE:
                        allContentQueue.addAll(getReferencedModelRootNode(node).getChildren());
                        break;
                    case EXACTLY_ONE:
                        decomposition.exactlyOneContents.add(expandsExactlyOneContent(node.getChildren()));
                        break;
                    case ASSERTION:
                        decomposition.assertions.add(node);
                        break;
                    default:
                        throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0007_UNEXPECTED_MODEL_NODE_TYPE_FOUND(node.getType()))));
                }
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ModelNode getReferencedModelRootNode(ModelNode policyReferenceNode) throws PolicyException {
        PolicySourceModel referencedModel = policyReferenceNode.getReferencedModel();
        if (referencedModel == null) {
            PolicyReferenceData refData = policyReferenceNode.getPolicyReferenceData();
            if (refData == null) {
                throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0041_POLICY_REFERENCE_NODE_FOUND_WITH_NO_POLICY_REFERENCE_IN_IT())));
            }
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0010_UNEXPANDED_POLICY_REFERENCE_NODE_FOUND_REFERENCING(refData.getReferencedModelUri()))));
        }
        return referencedModel.getRootNode();
    }

    private Collection<ModelNode> expandsExactlyOneContent(Collection<ModelNode> content) throws PolicyException {
        Collection<ModelNode> result = new LinkedList<>();
        Queue<ModelNode> eoContentQueue = new LinkedList<>(content);
        while (true) {
            ModelNode node = eoContentQueue.poll();
            if (node != null) {
                switch (node.getType()) {
                    case POLICY:
                    case ALL:
                    case ASSERTION:
                        result.add(node);
                        break;
                    case POLICY_REFERENCE:
                        result.add(getReferencedModelRootNode(node));
                        break;
                    case EXACTLY_ONE:
                        eoContentQueue.addAll(node.getChildren());
                        break;
                    default:
                        throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0001_UNSUPPORTED_MODEL_NODE_TYPE(node.getType()))));
                }
            } else {
                return result;
            }
        }
    }

    private List<AssertionSet> normalizeRawAlternative(RawAlternative alternative) throws PolicyException {
        List<PolicyAssertion> normalizedContentBase = new LinkedList<>();
        Collection<List<PolicyAssertion>> normalizedContentOptions = new LinkedList<>();
        if (!alternative.nestedAssertions.isEmpty()) {
            Queue<RawAssertion> nestedAssertionsQueue = new LinkedList<>(alternative.nestedAssertions);
            while (true) {
                RawAssertion rawAssertion = nestedAssertionsQueue.poll();
                if (rawAssertion == null) {
                    break;
                }
                List<PolicyAssertion> normalized = normalizeRawAssertion(rawAssertion);
                if (normalized.size() == 1) {
                    normalizedContentBase.addAll(normalized);
                } else {
                    normalizedContentOptions.add(normalized);
                }
            }
        }
        List<AssertionSet> options = new LinkedList<>();
        if (normalizedContentOptions.isEmpty()) {
            options.add(AssertionSet.createAssertionSet(normalizedContentBase));
        } else {
            Collection<Collection<PolicyAssertion>> contentCombinations = PolicyUtils.Collections.combine(normalizedContentBase, normalizedContentOptions, true);
            for (Collection<PolicyAssertion> contentOption : contentCombinations) {
                options.add(AssertionSet.createAssertionSet(contentOption));
            }
        }
        return options;
    }

    private List<PolicyAssertion> normalizeRawAssertion(RawAssertion assertion) throws PolicyException {
        List<PolicyAssertion> parameters;
        if (assertion.parameters.isEmpty()) {
            parameters = null;
        } else {
            parameters = new ArrayList<>(assertion.parameters.size());
            for (ModelNode parameterNode : assertion.parameters) {
                parameters.add(createPolicyAssertionParameter(parameterNode));
            }
        }
        List<AssertionSet> nestedAlternatives = new LinkedList<>();
        if (assertion.nestedAlternatives != null && !assertion.nestedAlternatives.isEmpty()) {
            Queue<RawAlternative> nestedAlternativeQueue = new LinkedList<>(assertion.nestedAlternatives);
            while (true) {
                RawAlternative rawAlternative = nestedAlternativeQueue.poll();
                if (rawAlternative == null) {
                    break;
                }
                nestedAlternatives.addAll(normalizeRawAlternative(rawAlternative));
            }
        }
        List<PolicyAssertion> assertionOptions = new LinkedList<>();
        boolean nestedAlternativesAvailable = !nestedAlternatives.isEmpty();
        if (nestedAlternativesAvailable) {
            for (AssertionSet nestedAlternative : nestedAlternatives) {
                assertionOptions.add(createPolicyAssertion(assertion.originalNode.getNodeData(), parameters, nestedAlternative));
            }
        } else {
            assertionOptions.add(createPolicyAssertion(assertion.originalNode.getNodeData(), parameters, null));
        }
        return assertionOptions;
    }

    private PolicyAssertion createPolicyAssertionParameter(ModelNode parameterNode) throws PolicyException {
        if (parameterNode.getType() != ModelNode.Type.ASSERTION_PARAMETER_NODE) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0065_INCONSISTENCY_IN_POLICY_SOURCE_MODEL(parameterNode.getType()))));
        }
        List<PolicyAssertion> childParameters = null;
        if (parameterNode.hasChildren()) {
            childParameters = new ArrayList<>(parameterNode.childrenSize());
            Iterator<ModelNode> it = parameterNode.iterator();
            while (it.hasNext()) {
                ModelNode childParameterNode = it.next();
                childParameters.add(createPolicyAssertionParameter(childParameterNode));
            }
        }
        return createPolicyAssertion(parameterNode.getNodeData(), childParameters, null);
    }

    private PolicyAssertion createPolicyAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters, AssertionSet nestedAlternative) throws AssertionCreationException {
        String assertionNamespace = data.getName().getNamespaceURI();
        PolicyAssertionCreator domainSpecificPAC = this.assertionCreators.get(assertionNamespace);
        if (domainSpecificPAC == null) {
            return defaultCreator.createAssertion(data, assertionParameters, nestedAlternative, null);
        }
        return domainSpecificPAC.createAssertion(data, assertionParameters, nestedAlternative, defaultCreator);
    }
}
