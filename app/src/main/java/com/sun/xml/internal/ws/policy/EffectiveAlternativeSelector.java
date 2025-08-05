package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/EffectiveAlternativeSelector.class */
public class EffectiveAlternativeSelector {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) EffectiveAlternativeSelector.class);

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/EffectiveAlternativeSelector$AlternativeFitness.class */
    private enum AlternativeFitness {
        UNEVALUATED { // from class: com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness.1
            @Override // com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness
            AlternativeFitness combine(PolicyAssertionValidator.Fitness assertionFitness) {
                switch (assertionFitness) {
                    case UNKNOWN:
                        return UNKNOWN;
                    case UNSUPPORTED:
                        return UNSUPPORTED;
                    case SUPPORTED:
                        return SUPPORTED;
                    case INVALID:
                        return INVALID;
                    default:
                        return UNEVALUATED;
                }
            }
        },
        INVALID { // from class: com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness.2
            @Override // com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness
            AlternativeFitness combine(PolicyAssertionValidator.Fitness assertionFitness) {
                return INVALID;
            }
        },
        UNKNOWN { // from class: com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness.3
            @Override // com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness
            AlternativeFitness combine(PolicyAssertionValidator.Fitness assertionFitness) {
                switch (assertionFitness) {
                    case UNKNOWN:
                        return UNKNOWN;
                    case UNSUPPORTED:
                        return UNSUPPORTED;
                    case SUPPORTED:
                        return PARTIALLY_SUPPORTED;
                    case INVALID:
                        return INVALID;
                    default:
                        return UNEVALUATED;
                }
            }
        },
        UNSUPPORTED { // from class: com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness.4
            @Override // com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness
            AlternativeFitness combine(PolicyAssertionValidator.Fitness assertionFitness) {
                switch (assertionFitness) {
                    case UNKNOWN:
                    case UNSUPPORTED:
                        return UNSUPPORTED;
                    case SUPPORTED:
                        return PARTIALLY_SUPPORTED;
                    case INVALID:
                        return INVALID;
                    default:
                        return UNEVALUATED;
                }
            }
        },
        PARTIALLY_SUPPORTED { // from class: com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness.5
            @Override // com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness
            AlternativeFitness combine(PolicyAssertionValidator.Fitness assertionFitness) {
                switch (assertionFitness) {
                    case UNKNOWN:
                    case UNSUPPORTED:
                    case SUPPORTED:
                        return PARTIALLY_SUPPORTED;
                    case INVALID:
                        return INVALID;
                    default:
                        return UNEVALUATED;
                }
            }
        },
        SUPPORTED_EMPTY { // from class: com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness.6
            @Override // com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness
            AlternativeFitness combine(PolicyAssertionValidator.Fitness assertionFitness) {
                throw new UnsupportedOperationException("Combine operation was called unexpectedly on 'SUPPORTED_EMPTY' alternative fitness enumeration state.");
            }
        },
        SUPPORTED { // from class: com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness.7
            @Override // com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.AlternativeFitness
            AlternativeFitness combine(PolicyAssertionValidator.Fitness assertionFitness) {
                switch (assertionFitness) {
                    case UNKNOWN:
                    case UNSUPPORTED:
                        return PARTIALLY_SUPPORTED;
                    case SUPPORTED:
                        return SUPPORTED;
                    case INVALID:
                        return INVALID;
                    default:
                        return UNEVALUATED;
                }
            }
        };

        abstract AlternativeFitness combine(PolicyAssertionValidator.Fitness fitness);
    }

    public static void doSelection(EffectivePolicyModifier modifier) throws IllegalArgumentException, PolicyException {
        AssertionValidationProcessor validationProcessor = AssertionValidationProcessor.getInstance();
        selectAlternatives(modifier, validationProcessor);
    }

    protected static void selectAlternatives(EffectivePolicyModifier modifier, AssertionValidationProcessor validationProcessor) throws IllegalArgumentException, PolicyException {
        PolicyMap map = modifier.getMap();
        for (PolicyMapKey mapKey : map.getAllServiceScopeKeys()) {
            Policy oldPolicy = map.getServiceEffectivePolicy(mapKey);
            modifier.setNewEffectivePolicyForServiceScope(mapKey, selectBestAlternative(oldPolicy, validationProcessor));
        }
        for (PolicyMapKey mapKey2 : map.getAllEndpointScopeKeys()) {
            Policy oldPolicy2 = map.getEndpointEffectivePolicy(mapKey2);
            modifier.setNewEffectivePolicyForEndpointScope(mapKey2, selectBestAlternative(oldPolicy2, validationProcessor));
        }
        for (PolicyMapKey mapKey3 : map.getAllOperationScopeKeys()) {
            Policy oldPolicy3 = map.getOperationEffectivePolicy(mapKey3);
            modifier.setNewEffectivePolicyForOperationScope(mapKey3, selectBestAlternative(oldPolicy3, validationProcessor));
        }
        for (PolicyMapKey mapKey4 : map.getAllInputMessageScopeKeys()) {
            Policy oldPolicy4 = map.getInputMessageEffectivePolicy(mapKey4);
            modifier.setNewEffectivePolicyForInputMessageScope(mapKey4, selectBestAlternative(oldPolicy4, validationProcessor));
        }
        for (PolicyMapKey mapKey5 : map.getAllOutputMessageScopeKeys()) {
            Policy oldPolicy5 = map.getOutputMessageEffectivePolicy(mapKey5);
            modifier.setNewEffectivePolicyForOutputMessageScope(mapKey5, selectBestAlternative(oldPolicy5, validationProcessor));
        }
        for (PolicyMapKey mapKey6 : map.getAllFaultMessageScopeKeys()) {
            Policy oldPolicy6 = map.getFaultMessageEffectivePolicy(mapKey6);
            modifier.setNewEffectivePolicyForFaultMessageScope(mapKey6, selectBestAlternative(oldPolicy6, validationProcessor));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0108  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static com.sun.xml.internal.ws.policy.Policy selectBestAlternative(com.sun.xml.internal.ws.policy.Policy r5, com.sun.xml.internal.ws.policy.AssertionValidationProcessor r6) throws com.sun.xml.internal.ws.policy.PolicyException {
        /*
            Method dump skipped, instructions count: 300
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector.selectBestAlternative(com.sun.xml.internal.ws.policy.Policy, com.sun.xml.internal.ws.policy.AssertionValidationProcessor):com.sun.xml.internal.ws.policy.Policy");
    }
}
