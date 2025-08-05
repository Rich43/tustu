package com.sun.xml.internal.ws.api.policy;

import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import com.sun.xml.internal.ws.policy.EffectivePolicyModifier;
import com.sun.xml.internal.ws.policy.PolicyException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/policy/AlternativeSelector.class */
public class AlternativeSelector extends EffectiveAlternativeSelector {
    public static void doSelection(EffectivePolicyModifier modifier) throws PolicyException {
        ValidationProcessor validationProcessor = ValidationProcessor.getInstance();
        selectAlternatives(modifier, validationProcessor);
    }
}
