package com.sun.org.apache.xerces.internal.jaxp.validation;

import java.util.HashMap;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/AbstractXMLSchema.class */
abstract class AbstractXMLSchema extends Schema implements XSGrammarPoolContainer {
    private final HashMap fFeatures = new HashMap();
    private final HashMap fProperties = new HashMap();

    @Override // javax.xml.validation.Schema
    public final Validator newValidator() {
        return new ValidatorImpl(this);
    }

    @Override // javax.xml.validation.Schema
    public final ValidatorHandler newValidatorHandler() {
        return new ValidatorHandlerImpl(this);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public final Boolean getFeature(String featureId) {
        return (Boolean) this.fFeatures.get(featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public final void setFeature(String featureId, boolean state) {
        this.fFeatures.put(featureId, state ? Boolean.TRUE : Boolean.FALSE);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public final Object getProperty(String propertyId) {
        return this.fProperties.get(propertyId);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
    public final void setProperty(String propertyId, Object state) {
        this.fProperties.put(propertyId, state);
    }
}
